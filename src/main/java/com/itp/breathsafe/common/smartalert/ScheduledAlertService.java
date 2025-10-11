package com.itp.breathsafe.common.smartalert;

import com.itp.breathsafe.common.email.EmailService;
import com.itp.breathsafe.common.exception.CustomException;
import com.itp.breathsafe.data.entity.SensorData;
import com.itp.breathsafe.data.repository.SensorDataRepository;
import com.itp.breathsafe.subscription.entity.Subscription;
import com.itp.breathsafe.subscription.repository.SubscriptionRepository;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ScheduledAlertService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledAlertService.class);

    private final SubscriptionRepository subscriptionRepository;
    private final SensorDataRepository sensorDataRepository;
    private final SmartAlertService smartAlertService;
    private final EmailService emailService;

    // TODO : Update alert sending logic.
    // Key: Subscription ID, Value: Timestamp of the SensorData record that triggered the last alert.
    private final Map<Long, LocalDateTime> recentlyAlerted = new ConcurrentHashMap<>();

    public ScheduledAlertService(
            SubscriptionRepository subscriptionRepository,
            SensorDataRepository sensorDataRepository,
            SmartAlertService smartAlertService,
            EmailService emailService
    ) {
        this.subscriptionRepository = subscriptionRepository;
        this.sensorDataRepository = sensorDataRepository;
        this.smartAlertService = smartAlertService;
        this.emailService = emailService;
    }

    /**
     * Periodically checks subscriptions and sends smart alerts if conditions are met.
     * The schedule is configured in application.properties (e.g., every 1 minute for testing).
     */
    @Scheduled(fixedRateString = "${alerting.schedule.rate.ms}")
    public void checkSubscriptionsAndSendAlerts() {
        logger.info("Starting scheduled alert check...");

        // 1. Fetch all active subscriptions that have email notifications enabled.
        // This query uses JOIN FETCH to prevent N+1 issues when accessing user and sensor details.
        List<Subscription> subscriptions = subscriptionRepository.findAllActiveEmailSubscriptionsWithDetails();

        for (Subscription sub : subscriptions) {
            try {
                Optional<SensorData> latestDataOpt = sensorDataRepository.findFirstBySensorIdOrderByTimestampDesc(sub.getSensor().getId());

                if (latestDataOpt.isEmpty()) {
                    logger.warn("No sensor data found for sensor ID: {}", sub.getSensor().getId());
                    continue;
                }

                SensorData latestData = latestDataOpt.get();

                boolean thresholdExceeded = latestData.getAqiValue() >= sub.getAlertThreshold();

                boolean alreadyAlerted = hasBeenAlertedRecently(sub.getId(), latestData.getTimestamp());

                if (thresholdExceeded && !alreadyAlerted) {
                    logger.info("Alert condition met for subscription ID: {}. AQI: {}, Threshold: {}",
                            sub.getId(), latestData.getAqiValue(), sub.getAlertThreshold());

                    SmartAlertRequest alertRequest = new SmartAlertRequest(
                            latestData.getTemperature(),
                            latestData.getHumidity(),
                            latestData.getCo2Level(),
                            latestData.getAqiValue()
                    );
                    String smartMessage = smartAlertService.getSmartAlert(alertRequest);

                    String emailBody = buildAlertEmailBody(
                            sub.getUser().getFirstName() != null ? sub.getUser().getFirstName() : sub.getUser().getUsername(),
                            sub.getSensor().getName(),
                            sub.getSensor().getLocation(),
                            latestData.getAqiValue(),
                            latestData.getAqiCategory().name(),
                            smartMessage
                    );

                    emailService.sendHtmlEmail(sub.getUser().getEmail(), "Air Quality Alert for " + sub.getSensor().getName(), emailBody);
                    logger.info("Successfully sent alert email to {} for subscription ID {}", sub.getUser().getEmail(), sub.getId());

                    recentlyAlerted.put(sub.getId(), latestData.getTimestamp());
                }

            } catch (CustomException | MessagingException e) {
                logger.error("Failed to process alert for subscription ID {}: {}", sub.getId(), e.getMessage());
            }
        }
        logger.info("Finished scheduled alert check.");
    }

    private boolean hasBeenAlertedRecently(Long subscriptionId, LocalDateTime dataTimestamp) {
        LocalDateTime lastAlertTimestamp = recentlyAlerted.get(subscriptionId);
        return lastAlertTimestamp != null && lastAlertTimestamp.equals(dataTimestamp);
    }

    private String buildAlertEmailBody(String userName, String sensorName, String location, Integer aqi, String aqiCategory, String smartMessage) {
        return """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Air Quality Alert - BreathSafe</title>
        </head>
        <body style="margin: 0; padding: 0; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif; line-height: 1.6; background-color: #f8f9fa;">
            <div style="max-width: 600px; margin: 20px auto; background: white; border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.08); overflow: hidden;">
                <div style="background: linear-gradient(135deg, #FCA5A5 0%%, #B91C1C 100%%); padding: 30px; text-align: center;">
                    <h1 style="color: white; margin: 0; font-size: 28px; font-weight: bold;">Air Quality Alert!</h1>
                    <p style="color: #FECACA; margin: 8px 0 0; font-size: 16px;">Important health information for your subscribed sensor.</p>
                </div>
                <div style="padding: 30px;">
                    <p style="color: #333; font-size: 16px;">Hi %s, 👋</p>
                    <p style="color: #555; font-size: 15px;">An air quality reading from a sensor you're subscribed to has exceeded your alert threshold.</p>
                    <div style="margin: 25px 0; padding: 20px; background-color: #FFFBEB; border-left: 5px solid #FBBF24; border-radius: 8px;">
                        <h2 style="margin: 0 0 15px; font-size: 18px; color: #92400E;">Sensor Details</h2>
                        <p style="margin: 4px 0; font-size: 15px;"><strong style="color: #D97706;">Sensor:</strong> %s</p>
                        <p style="margin: 4px 0; font-size: 15px;"><strong style="color: #D97706;">Location:</strong> %s</p>
                        <p style="margin: 4px 0; font-size: 15px;"><strong style="color: #D97706;">AQI Value:</strong> <span style="font-size: 18px; font-weight: bold; color: #B91C1C;">%d</span></p>
                        <p style="margin: 4px 0; font-size: 15px;"><strong style="color: #D97706;">Category:</strong> %s</p>
                    </div>
    
                    <div style="margin: 25px 0; padding: 20px; background-color: #FEF2F2; border-left: 5px solid #DC2626; border-radius: 8px;">
                         <h2 style="margin: 0 0 10px; font-size: 18px; color: #991B1B;">Personalized Health Advisory</h2>
                         <p style="color: #B91C1C; font-size: 15px; line-height: 1.7;">%s</p>
                    </div>
                    <div style="text-align: center; margin-top: 30px;">
                        <a href="#" style="background-color: #1E40AF; color: white; text-decoration: none; padding: 12px 24px; border-radius: 8px; font-weight: 500;">View Dashboard</a>
                    </div>
                </div>
                <div style="background-color: #F1F5F9; padding: 20px; text-align: center; font-size: 12px; color: #64748B;">
                    <p style="margin: 0;">© 2025 BreathSafe. All rights reserved.</p>
                    <p style="margin: 5px 0 0;">You received this alert because you subscribed to notifications for this sensor.</p>
                </div>
            </div>
        </body>
        </html>
        """.formatted(userName, sensorName, location, aqi, aqiCategory, smartMessage);
    }
}