package com.itp.breathsafe.common.email;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final EmailService emailService;

    public NotificationController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public String sendEmail() {
        emailService.sendSimpleMessage(
                "receiver@example.com",
                "Test Email",
                "Hello! This is a test email from Spring Boot."
        );
        return "Email sent successfully!";
    }
}
