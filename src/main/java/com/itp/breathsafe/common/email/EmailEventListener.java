package com.itp.breathsafe.common.email;

import com.itp.breathsafe.common.events.UserRegisteredEvent;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EmailEventListener {

    private final EmailService emailService;
    private static final Logger logger = LoggerFactory.getLogger(EmailEventListener.class);

    public EmailEventListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @EventListener
    public void handleUserRegistered(UserRegisteredEvent event) {
        try {
            String body = """
                    <html>
                      <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                        <div style="max-width: 600px; margin: auto; padding: 20px; border: 1px solid #eaeaea; border-radius: 10px;">
                          <h1 style="color: #2c7be5; text-align: center;">Welcome to BreathSafe 🎉</h1>
                          <p>Hi there,</p>
                          <p>Thank you for registering with <strong>BreathSafe</strong>. We’re excited to have you join our community!</p>
                          <p>
                            With your account, you can:
                            <ul>
                              <li>Request air quality sensors</li>
                              <li>Track your requests</li>
                              <li>Stay updated with the latest community actions</li>
                            </ul>
                          </p>
                          <p style="margin-top:20px;">
                            We’re here to make your environment healthier and safer.  
                            If you have any questions, just reply to this email.
                          </p>
                          <p style="margin-top:30px;">Best regards,<br><strong>The BreathSafe Team</strong></p>
                        </div>
                      </body>
                    </html>
                    """;
            emailService.sendHtmlEmail(event.email(), "Welcome to BreathSafe", body);
            logger.info("Email sent to {}", event.email());
        } catch (MessagingException e) {
            logger.error("Failed to send email: {}", e.getMessage());
        }
    }
}
