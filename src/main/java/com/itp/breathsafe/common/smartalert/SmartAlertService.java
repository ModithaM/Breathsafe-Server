package com.itp.breathsafe.common.smartalert;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itp.breathsafe.common.exception.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Iterator;

/**
 * SmartAlertService (no AQICategory enum).
 * The service derives an AQI category string from the numeric AQI value and sends a live generateContent request
 * to Gemini on each invocation. It sends payload in the exact contents/parts/text shape the API expects.
 * <p>
 * On client 4xx errors (other than 429) the method throws a CustomException containing the remote body.
 * Retries transient failures (5xx, 429) up to gemini.maxAttempts.
 */
@Service
public class SmartAlertService {

    private static final Logger logger = LoggerFactory.getLogger(SmartAlertService.class);

    private final String apiKey;
    private final String modelName;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;
    private final int maxAttempts;
    private final long initialBackoffMillis = 1000L;

    public SmartAlertService(
            @Value("${gemini.api.key:}") String apiKey,
            @Value("${gemini.model.name:gemini-2.0-flash:generateContent}") String modelName,
            @Value("${gemini.maxAttempts:3}") int maxAttempts
    ) {
        this.apiKey = apiKey;
        this.modelName = modelName;
        this.maxAttempts = Math.max(1, maxAttempts);
        this.objectMapper = new ObjectMapper();
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    public String getSmartAlert(SmartAlertRequest request) {
        if (request == null) throw new CustomException("SmartAlertRequest cannot be null");
        if (apiKey == null || apiKey.isBlank()) throw new CustomException("Gemini API key is not configured.");

        String prompt = buildPrompt(request);
        String endpoint = modelName.endsWith(":generateContent") ? modelName : modelName + ":generateContent";
        endpoint = "https://generativelanguage.googleapis.com/v1beta/models/" + endpoint;

        try {
            String requestJson = objectMapper.writeValueAsString(
                    objectMapper.createObjectNode()
                            .set("contents",
                                    objectMapper.createArrayNode().add(
                                            objectMapper.createObjectNode().set("parts",
                                                    objectMapper.createArrayNode().add(
                                                            objectMapper.createObjectNode().put("text", prompt)
                                                    )
                                            )
                                    )
                            )
            );

            long backoff = initialBackoffMillis;
            for (int attempt = 1; attempt <= maxAttempts; attempt++) {
                try {
                    HttpRequest httpRequest = HttpRequest.newBuilder()
                            .uri(URI.create(endpoint))
                            .header("Content-Type", "application/json")
                            .header("X-goog-api-key", apiKey)
                            .POST(HttpRequest.BodyPublishers.ofString(requestJson))
                            .timeout(Duration.ofSeconds(20))
                            .build();

                    logger.debug("Calling Gemini (attempt {}/{})", attempt, maxAttempts);
                    HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
                    int status = response.statusCode();

                    if (status / 100 == 2) {
                        JsonNode root = objectMapper.readTree(response.body());
                        String generated = extractFirstGeneratedText(root);
                        if (generated != null && !generated.isBlank() && !looksLikeId(generated)) {
                            return generated.trim();
                        }
                        logger.warn("Gemini returned no usable generated text on attempt {}. Response: {}", attempt, response.body());
                        // treat as transient and retry if attempts remain
                    } else {
                        if (status >= 400 && status < 500 && status != 429) {
                            throw new CustomException("Gemini API client error: HTTP " + status + " - " + response.body());
                        }
                        logger.warn("Gemini API transient error (HTTP {}). Body: {}. Attempt {}/{}", status, response.body(), attempt, maxAttempts);
                    }
                } catch (IOException ioe) {
                    logger.warn("IOException calling Gemini on attempt {}/{}: {}", attempt, maxAttempts, ioe.getMessage());
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new CustomException("Gemini call interrupted", ie);
                }

                if (attempt < maxAttempts) {
                    try {
                        Thread.sleep(backoff);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new CustomException("Retry interrupted", ie);
                    }
                    backoff *= 2;
                }
            }

            throw new CustomException("Failed to generate a smart alert from Gemini after " + maxAttempts + " attempts.");
        } catch (IOException e) {
            throw new CustomException("Failed to build request JSON: " + e.getMessage(), e);
        }
    }

    private String buildPrompt(SmartAlertRequest r) {
        String aqiCategory = deriveAqiCategory(r.aqiValue());
        return """
                You are an environmental health assistant. Your goal is to create a clear and helpful air-quality notification for a user, presented as a concise paragraph.
                Output ONLY the notification text. Do not include any extra formatting, metadata, IDs, or JSON.
                Analyze the following real-time sensor data to generate your notification.
                Your message should provide a brief description of what the current air quality means and offer clear, detailed steps the user should take to protect their health.
                The urgency and specific advice should directly reflect the severity of the conditions shown in the data.
                Temperature: %s °C
                Humidity: %s %%
                CO2 Level: %s ppm
                AQI Value: %s
                AQI Category: %s
                """.formatted(
                safeVal(r.temperature()),
                safeVal(r.humidity()),
                safeVal(r.co2Level()),
                safeVal(r.aqiValue()),
                aqiCategory
        );
    }

    private String deriveAqiCategory(Integer aqiValue) {
        if (aqiValue == null) return "UNKNOWN";
        int v = aqiValue;
        if (v <= 50) return "GOOD";
        if (v <= 100) return "MODERATE";
        if (v <= 150) return "UNHEALTHY_SENSITIVE";
        if (v <= 200) return "UNHEALTHY";
        if (v <= 300) return "VERY_UNHEALTHY";
        return "HAZARDOUS";
    }

    private String safeVal(Object o) {
        return o == null ? "unknown" : o.toString();
    }

    private boolean looksLikeId(String s) {
        return s.length() <= 40 && !s.contains(" ") && s.matches("^[A-Za-z0-9_-]+$");
    }

    private String extractFirstGeneratedText(JsonNode root) {
        if (root == null || root.isMissingNode()) return null;

        JsonNode candidates = root.path("candidates");
        if (candidates.isArray() && candidates.size() > 0) {
            JsonNode first = candidates.get(0);
            JsonNode content = first.path("content");
            if (content.isArray()) {
                for (JsonNode part : content) {
                    if (part.has("text")) {
                        String txt = part.path("text").asText();
                        if (isNaturalLanguage(txt)) return txt;
                    }
                }
            }
            if (first.has("text")) {
                String txt = first.path("text").asText();
                if (isNaturalLanguage(txt)) return txt;
            }
        }

        // 2) outputs -> content[*].text or outputs[*].text
        JsonNode outputs = root.path("outputs");
        if (outputs.isArray()) {
            for (JsonNode out : outputs) {
                JsonNode content = out.path("content");
                if (content.isArray()) {
                    for (JsonNode part : content) {
                        if (part.has("text")) {
                            String txt = part.path("text").asText();
                            if (isNaturalLanguage(txt)) return txt;
                        }
                    }
                }
                if (out.has("text")) {
                    String txt = out.path("text").asText();
                    if (isNaturalLanguage(txt)) return txt;
                }
            }
        }

        // 3) Best-effort scan but skip common metadata keys
        Iterator<String> fields = root.fieldNames();
        while (fields.hasNext()) {
            String fname = fields.next();
            if (fname == null) continue;
            String lf = fname.toLowerCase();
            if (lf.contains("model") || lf.equals("name") || lf.equals("id") || lf.contains("version")) continue;

            JsonNode node = root.path(fname);
            if (node.isTextual()) {
                String text = node.asText();
                if (isNaturalLanguage(text)) return text;
            } else if (node.isArray() || node.isObject()) {
                String found = findTextRecursively(node);
                if (found != null) return found;
            }
        }
        return null;
    }

    private String findTextRecursively(JsonNode node) {
        if (node == null) return null;
        if (node.isTextual()) {
            String t = node.asText();
            if (isNaturalLanguage(t)) return t;
            return null;
        }
        if (node.isArray()) {
            for (JsonNode item : node) {
                String f = findTextRecursively(item);
                if (f != null) return f;
            }
        } else if (node.isObject()) {
            Iterator<String> names = node.fieldNames();
            while (names.hasNext()) {
                String name = names.next();
                String ln = name.toLowerCase();
                if (ln.contains("model") || ln.equals("name") || ln.equals("id") || ln.contains("version")) continue;
                JsonNode child = node.get(name);
                String f = findTextRecursively(child);
                if (f != null) return f;
            }
        }
        return null;
    }

    private boolean isNaturalLanguage(String s) {
        if (s == null) return false;
        String trimmed = s.trim();
        if (trimmed.length() < 8) return false;
        if (!trimmed.contains(" ")) return false;
        if (!trimmed.matches(".*[A-Za-z].*")) return false;
        if (trimmed.matches("^[A-Za-z0-9_-]+$")) return false;
        return true;
    }
}
