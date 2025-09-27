package com.itp.breathsafe.common.email;

import com.itp.breathsafe.common.events.CreateRequestEvent;
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
                    <!DOCTYPE html>
                           <html>
                           <head>
                               <meta charset="UTF-8">
                               <meta name="viewport" content="width=device-width, initial-scale=1.0">
                               <title>Welcome to BreathSafe</title>
                           </head>
                           <body style="margin: 0; padding: 0; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif; line-height: 1.6; background: linear-gradient(135deg, #DCFCE7 0%, #FFEDD5 100%); min-height: 100vh;">

                               <!-- Main Container -->
                               <div style="max-width: 600px; margin: 0 auto; padding: 40px 20px;">
                    
                                   <!-- Email Card -->
                                   <div style="background: white; border-radius: 20px; box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04); overflow: hidden; border: 1px solid #f3f4f6;">
                    
                                       <!-- Header with Gradient -->
                                       <div style="background: linear-gradient(135deg, #65A30D 0%, #064E3B 100%); padding: 40px 30px; text-align: center; position: relative;">
                                           <!-- Decorative circles -->
                                           <div style="position: absolute; top: -10px; right: -10px; width: 80px; height: 80px; background: rgba(217, 249, 157, 0.2); border-radius: 50%;"></div>
                                           <div style="position: absolute; bottom: -20px; left: -20px; width: 60px; height: 60px; background: rgba(255, 255, 255, 0.1); border-radius: 50%;"></div>
                    
                                           <!-- BreathSafe Logo -->
                                           <div style="display: flex; align-items: center; justify-content: center; gap: 8px; margin: 0 auto 30px; position: relative; z-index: 1;">
                                               <!-- Logo Icon -->
                                               <div style="display: inline-grid; height: 50px; width: 50px; place-items: center; border-radius: 12px; background: linear-gradient(135deg, #DCFCE7 0%, #FFEDD5 100%); border: 1px solid rgba(0,0,0,0.05);">
                                                   <svg viewBox="0 0 24 24" aria-hidden="true" style="height: 24px; width: 24px; color: #65A30D;">
                                                       <path d="M5 12c0-3.866 3.134-7 7-7a7 7 0 0 1 0 14h-1.5a1.5 1.5 0 0 1-1.5-1.5v-3.25a.75.75 0 0 0-1.5 0V18A2 2 0 0 1 5 20" fill="currentColor"/>
                                                   </svg>
                                               </div>
                                               <!-- Logo Text -->
                                               <div style="font-size: 28px; font-weight: bold; letter-spacing: -0.025em; color: white;">
                                                   Breath<span style="color: rgba(255, 255, 255, 0.8);">Safe</span>
                                               </div>
                                           </div>
                    
                                           <h1 style="color: white; margin: 0; font-size: 32px; font-weight: bold; text-shadow: 0 2px 4px rgba(0,0,0,0.1);">
                                               Welcome to the Community! 🌱
                                           </h1>
                                           <p style="color: #D9F99D; margin: 10px 0 0; font-size: 18px; font-weight: 500;">
                                               Your journey to cleaner air starts here
                                           </p>
                                       </div>
                    
                                       <!-- Content -->
                                       <div style="padding: 40px 30px;">
                    
                                           <!-- Greeting -->
                                           <p style="color: #0F172A; font-size: 18px; margin: 0 0 20px; font-weight: 500;">
                                               Hi there! 👋
                                           </p>
                    
                                           <p style="color: #71717A; font-size: 16px; margin: 0 0 25px; line-height: 1.7;">
                                               Thank you for joining <strong style="color: #65A30D;">BreathSafe</strong>! We're thrilled to have you as part of our growing community working towards cleaner, healthier air for everyone.
                                           </p>
                    
                                           <!-- Features Card -->
                                           <div style="background: linear-gradient(135deg, #DCFCE7 0%, #FFEDD5 100%); border-radius: 16px; padding: 30px; margin: 30px 0; border: 1px solid #065F46;">
                                               <h2 style="color: #064E3B; font-size: 20px; margin: 0 0 20px; font-weight: bold; display: flex; align-items: center;">
                                                   <span style="background: #65A30D; color: white; width: 24px; height: 24px; border-radius: 50%; display: inline-flex; align-items: center; justify-content: center; font-size: 12px; margin-right: 12px;">✓</span>
                                                   What you can do with BreathSafe:
                                               </h2>
                    
                                               <div style="margin: 20px 0;">
                                                   <div style="display: flex; align-items: center; margin: 15px 0; padding: 12px 0; border-bottom: 1px solid rgba(101, 163, 13, 0.2);">
                                                       <div style="background: #65A30D; color: white; width: 32px; height: 32px; border-radius: 8px; display: flex; align-items: center; justify-content: center; margin-right: 15px; font-weight: bold; font-size: 14px;">📍</div>
                                                       <div>
                                                           <strong style="color: #064E3B; font-size: 16px;">Request Air Quality Sensors</strong>
                                                           <p style="color: #71717A; margin: 5px 0 0; font-size: 14px;">Help expand monitoring coverage in your community</p>
                                                       </div>
                                                   </div>
                    
                                                   <div style="display: flex; align-items: center; margin: 15px 0; padding: 12px 0; border-bottom: 1px solid rgba(101, 163, 13, 0.2);">
                                                       <div style="background: #65A30D; color: white; width: 32px; height: 32px; border-radius: 8px; display: flex; align-items: center; justify-content: center; margin-right: 15px; font-weight: bold; font-size: 14px;">📊</div>
                                                       <div>
                                                           <strong style="color: #064E3B; font-size: 16px;">Track Your Requests</strong>
                                                           <p style="color: #71717A; margin: 5px 0 0; font-size: 14px;">Monitor the status and progress of your submissions</p>
                                                       </div>
                                                   </div>
                    
                                                   <div style="display: flex; align-items: center; margin: 15px 0; padding: 12px 0;">
                                                       <div style="background: #65A30D; color: white; width: 32px; height: 32px; border-radius: 8px; display: flex; align-items: center; justify-content: center; margin-right: 15px; font-weight: bold; font-size: 14px;">🔔</div>
                                                       <div>
                                                           <strong style="color: #064E3B; font-size: 16px;">Stay Updated</strong>
                                                           <p style="color: #71717A; margin: 5px 0 0; font-size: 14px;">Get notifications about community actions and air quality alerts</p>
                                                       </div>
                                                   </div>
                                               </div>
                                           </div>
                    
                                           <!-- Call to Action -->
                                           <div style="text-align: center; margin: 35px 0;">
                                               <a href="#" style="background: linear-gradient(135deg, #65A30D 0%, #064E3B 100%); color: white; text-decoration: none; padding: 16px 32px; border-radius: 12px; font-weight: bold; font-size: 16px; display: inline-block; box-shadow: 0 10px 15px -3px rgba(101, 163, 13, 0.3); transition: all 0.3s ease;">
                                                   🚀 Get Started Now
                                               </a>
                                           </div>
                    
                                           <!-- Support Message -->
                                           <div style="background: #F0FDF4; border: 1px solid #D9F99D; border-radius: 12px; padding: 20px; margin: 30px 0;">
                                               <p style="color: #064E3B; margin: 0; font-size: 15px; line-height: 1.6;">
                                                   <strong>💚 We're here to help!</strong><br>
                                                   Our mission is to make your environment healthier and safer. If you have any questions or need assistance getting started, simply reply to this email or contact our support team.
                                               </p>
                                           </div>
                    
                                           <!-- Environmental Message -->
                                           <div style="text-align: center; margin: 30px 0; padding: 20px; background: linear-gradient(135deg, rgba(101, 163, 13, 0.1) 0%, rgba(6, 78, 59, 0.1) 100%); border-radius: 12px;">
                                               <p style="color: #064E3B; margin: 0; font-size: 16px; font-style: italic;">
                                                   "Together, we're building a network for cleaner air and healthier communities. Every sensor request makes a difference!" 🌍
                                               </p>
                                           </div>
                    
                                       </div>
                    
                                       <!-- Footer -->
                                       <div style="background: #064E3B; padding: 30px; text-align: center;">
                                           <p style="color: #D9F99D; margin: 0 0 10px; font-size: 18px; font-weight: 600;">
                                               Best regards,
                                           </p>
                                           <p style="color: white; margin: 0; font-size: 20px; font-weight: bold;">
                                               The BreathSafe Team 🌱
                                           </p>
                    
                                           <!-- Social Links Placeholder -->
                                           <div style="margin-top: 20px;">
                                               <div style="display: inline-block; margin: 0 10px;">
                                                   <div style="background: #65A30D; color: white; width: 40px; height: 40px; border-radius: 50%; display: inline-flex; align-items: center; justify-content: center; font-size: 16px;">🌐</div>
                                               </div>
                                               <div style="display: inline-block; margin: 0 10px;">
                                                   <div style="background: #65A30D; color: white; width: 40px; height: 40px; border-radius: 50%; display: inline-flex; align-items: center; justify-content: center; font-size: 16px;">📧</div>
                                               </div>
                                               <div style="display: inline-block; margin: 0 10px;">
                                                   <div style="background: #65A30D; color: white; width: 40px; height: 40px; border-radius: 50%; display: inline-flex; align-items: center; justify-content: center; font-size: 16px;">📱</div>
                                               </div>
                                           </div>
                    
                                           <p style="color: #71717A; margin: 20px 0 0; font-size: 12px;">
                                               © 2024 BreathSafe. Making air quality monitoring accessible to every community.
                                           </p>
                                       </div>
                    
                                   </div>
                    
                                   <!-- Footer Note -->
                                   <div style="text-align: center; margin: 20px 0;">
                                       <p style="color: #71717A; font-size: 12px; margin: 0;">
                                           This email was sent to you because you registered for a BreathSafe account.
                                       </p>
                                   </div>
                    
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

    @EventListener
    public void handleCreateRequest(CreateRequestEvent event) {
        try {
            String body = """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Request Submitted - BreathSafe</title>
        </head>
        <body style="margin: 0; padding: 0; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif; line-height: 1.6; background: linear-gradient(135deg, #DCFCE7 0%, #FFEDD5 100%); min-height: 100vh;">
           \s
            <!-- Main Container -->
            <div style="max-width: 600px; margin: 0 auto; padding: 40px 20px;">
               \s
                <!-- Email Card -->
                <div style="background: white; border-radius: 20px; box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04); overflow: hidden; border: 1px solid #f3f4f6;">
                   \s
                    <!-- Header with Gradient -->
                    <div style="background: linear-gradient(135deg, #65A30D 0%, #064E3B 100%); padding: 40px 30px; text-align: center; position: relative;">
                       \s
                        <!-- Decorative circles -->
                        <div style="position: absolute; top: -10px; right: -10px; width: 80px; height: 80px; background: rgba(217, 249, 157, 0.2); border-radius: 50%;"></div>
                        <div style="position: absolute; bottom: -20px; left: -20px; width: 60px; height: 60px; background: rgba(255, 255, 255, 0.1); border-radius: 50%;"></div>
                       \s
                        <!-- BreathSafe Logo -->
                        <div style="display: flex; align-items: center; justify-content: center; gap: 8px; margin: 0 auto 30px; position: relative; z-index: 1;">
                           \s
                            <!-- Logo Icon -->
                            <div style="display: inline-grid; height: 50px; width: 50px; place-items: center; border-radius: 12px; background: linear-gradient(135deg, #DCFCE7 0%, #FFEDD5 100%); border: 1px solid rgba(0,0,0,0.05);">
                                <svg viewBox="0 0 24 24" aria-hidden="true" style="height: 24px; width: 24px; color: #65A30D;">
                                    <path d="M5 12c0-3.866 3.134-7 7-7a7 7 0 0 1 0 14h-1.5a1.5 1.5 0 0 1-1.5-1.5v-3.25a.75.75 0 0 0-1.5 0V18A2 2 0 0 1 5 20" fill="currentColor"/>
                                </svg>
                            </div>
                           \s
                            <!-- Logo Text -->
                            <div style="font-size: 28px; font-weight: bold; letter-spacing: -0.025em; color: white;">
                                Breath<span style="color: rgba(255, 255, 255, 0.8);">Safe</span>
                            </div>
                           \s
                        </div>
                       \s
                        <h1 style="color: white; margin: 0; font-size: 32px; font-weight: bold; text-shadow: 0 2px 4px rgba(0,0,0,0.1);">
                            Request Submitted! ✅
                        </h1>
                        <p style="color: #D9F99D; margin: 10px 0 0; font-size: 18px; font-weight: 500;">
                            We've received your sensor request
                        </p>
                       \s
                    </div>
                   \s
                    <!-- Content -->
                    <div style="padding: 40px 30px;">
                       \s
                        <!-- Greeting -->
                        <p style="color: #0F172A; font-size: 18px; margin: 0 0 20px; font-weight: 500;">
                            Great news! 🎉
                        </p>
                       \s
                        <p style="color: #71717A; font-size: 16px; margin: 0 0 25px; line-height: 1.7;">
                            Your community sensor request has been successfully submitted to our team. We'll review it carefully and get back to you within <strong style="color: #65A30D;">5-7 business days</strong>.
                        </p>
                       \s
                        <!-- Request Details Card -->
                        <div style="background: linear-gradient(135deg, #DCFCE7 0%, #FFEDD5 100%); border-radius: 16px; padding: 30px; margin: 30px 0; border: 1px solid #065F46;">
                            <h2 style="color: #064E3B; font-size: 20px; margin: 0 0 20px; font-weight: bold; display: flex; align-items: center;">
                                <span style="background: #65A30D; color: white; width: 24px; height: 24px; border-radius: 50%; display: inline-flex; align-items: center; justify-content: center; font-size: 12px; margin-right: 12px;">📍</span>
                                Your Request Details:
                            </h2>
                           \s
                            <div style="margin: 20px 0;">
                               \s
                                <!-- Location -->
                                <div style="margin: 20px 0; padding: 15px; background: white; border-radius: 12px; border: 1px solid rgba(101, 163, 13, 0.2);">
                                    <div style="display: flex; align-items: center; margin-bottom: 10px;">
                                        <div style="background: #65A30D; color: white; width: 32px; height: 32px; border-radius: 8px; display: flex; align-items: center; justify-content: center; margin-right: 15px; font-size: 16px;">📍</div>
                                        <strong style="color: #064E3B; font-size: 16px;">Location</strong>
                                    </div>
                                    <p style="color: #0F172A; margin: 0; font-size: 18px; font-weight: 600; margin-left: 47px;">
                                        Your requested location details have been recorded
                                    </p>
                                    <p style="color: #71717A; margin: 5px 0 0 47px; font-size: 14px;">
                                        Coordinates and address information saved securely
                                    </p>
                                </div>
                               \s
                                <!-- Justification -->
                                <div style="margin: 20px 0; padding: 15px; background: white; border-radius: 12px; border: 1px solid rgba(101, 163, 13, 0.2);">
                                    <div style="display: flex; align-items: center; margin-bottom: 10px;">
                                        <div style="background: #65A30D; color: white; width: 32px; height: 32px; border-radius: 8px; display: flex; align-items: center; justify-content: center; margin-right: 15px; font-size: 16px;">💬</div>
                                        <strong style="color: #064E3B; font-size: 16px;">Justification</strong>
                                    </div>
                                    <p style="color: #71717A; margin: 0; font-size: 15px; line-height: 1.6; margin-left: 47px;">
                                        Your detailed reasoning for this sensor request has been submitted for review
                                    </p>
                                </div>
                               \s
                                <!-- Request Status -->
                                <div style="margin: 20px 0; padding: 15px; background: #FEF3C7; border-radius: 12px; border: 1px solid #F59E0B;">
                                    <div style="display: flex; align-items: center; margin-bottom: 10px;">
                                        <div style="background: #F59E0B; color: white; width: 32px; height: 32px; border-radius: 8px; display: flex; align-items: center; justify-content: center; margin-right: 15px; font-size: 16px;">⏳</div>
                                        <strong style="color: #92400E; font-size: 16px;">Status: Pending Review</strong>
                                    </div>
                                    <p style="color: #92400E; margin: 0; font-size: 14px; margin-left: 47px;">
                                        Our team will evaluate your request based on community impact, environmental factors, and sensor availability.
                                    </p>
                                </div>
                               \s
                            </div>
                        </div>
                       \s
                        <!-- What Happens Next -->
                        <div style="background: #F0FDF4; border: 1px solid #D9F99D; border-radius: 12px; padding: 20px; margin: 30px 0;">
                            <h3 style="color: #064E3B; margin: 0 0 15px; font-size: 18px; font-weight: bold; display: flex; align-items: center;">
                                <span style="margin-right: 8px;">🔄</span>
                                What happens next?
                            </h3>
                            <div style="color: #064E3B; font-size: 15px; line-height: 1.6;">
                                <div style="margin: 10px 0; display: flex; align-items: flex-start;">
                                    <span style="background: #65A30D; color: white; width: 20px; height: 20px; border-radius: 50%; display: inline-flex; align-items: center; justify-content: center; font-size: 12px; margin-right: 12px; margin-top: 2px; flex-shrink: 0;">1</span>
                                    <div>
                                        <strong>Review Process (1-3 days)</strong><br>
                                        <span style="color: #71717A;">Our team evaluates the request for feasibility and community impact.</span>
                                    </div>
                                </div>
                                <div style="margin: 10px 0; display: flex; align-items: flex-start;">
                                    <span style="background: #65A30D; color: white; width: 20px; height: 20px; border-radius: 50%; display: inline-flex; align-items: center; justify-content: center; font-size: 12px; margin-right: 12px; margin-top: 2px; flex-shrink: 0;">2</span>
                                    <div>
                                        <strong>Decision & Notification (5-7 days)</strong><br>
                                        <span style="color: #71717A;">You'll receive an email with our decision and next steps.</span>
                                    </div>
                                </div>
                                <div style="margin: 10px 0; display: flex; align-items: flex-start;">
                                    <span style="background: #65A30D; color: white; width: 20px; height: 20px; border-radius: 50%; display: inline-flex; align-items: center; justify-content: center; font-size: 12px; margin-right: 12px; margin-top: 2px; flex-shrink: 0;">3</span>
                                    <div>
                                        <strong>Installation (If approved)</strong><br>
                                        <span style="color: #71717A;">Sensor deployment and community activation.</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                       \s
                        <!-- Track Your Request -->
                        <div style="text-align: center; margin: 35px 0;">
                            <a href="#" style="background: linear-gradient(135deg, #65A30D 0%, #064E3B 100%); color: white; text-decoration: none; padding: 16px 32px; border-radius: 12px; font-weight: bold; font-size: 16px; display: inline-block; box-shadow: 0 10px 15px -3px rgba(101, 163, 13, 0.3);">
                                📊 Track Your Requests
                            </a>
                            <p style="color: #71717A; font-size: 14px; margin: 15px 0 0;">
                                View status updates and manage all your requests in your dashboard
                            </p>
                        </div>
                       \s
                        <!-- Support Message -->
                        <div style="background: #EFF6FF; border: 1px solid #DBEAFE; border-radius: 12px; padding: 20px; margin: 30px 0;">
                            <p style="color: #1E40AF; margin: 0; font-size: 15px; line-height: 1.6;">
                                <strong>💡 Need help or have questions?</strong><br>
                                Our support team is here to assist you. Simply reply to this email or contact us through your dashboard. We're committed to making air quality monitoring accessible to every community.
                            </p>
                        </div>
                       \s
                        <!-- Environmental Message -->
                        <div style="text-align: center; margin: 30px 0; padding: 20px; background: linear-gradient(135deg, rgba(101, 163, 13, 0.1) 0%, rgba(6, 78, 59, 0.1) 100%); border-radius: 12px;">
                            <p style="color: #064E3B; margin: 0; font-size: 16px; font-style: italic;">
                                "Thank you for taking action to improve air quality in your community. Every request brings us closer to cleaner, healthier air for everyone!" 🌍
                            </p>
                        </div>
                       \s
                    </div>
                   \s
                    <!-- Footer -->
                    <div style="background: #064E3B; padding: 30px; text-align: center;">
                       \s
                        <p style="color: #D9F99D; margin: 0 0 10px; font-size: 18px; font-weight: 600;">
                            Best regards,
                        </p>
                        <p style="color: white; margin: 0; font-size: 20px; font-weight: bold;">
                            The BreathSafe Team 🌱
                        </p>
                       \s
                        <!-- Social Links Placeholder -->
                        <div style="margin-top: 20px;">
                            <div style="display: inline-block; margin: 0 10px;">
                                <div style="background: #65A30D; color: white; width: 40px; height: 40px; border-radius: 50%; display: inline-flex; align-items: center; justify-content: center; font-size: 16px;">🌐</div>
                            </div>
                            <div style="display: inline-block; margin: 0 10px;">
                                <div style="background: #65A30D; color: white; width: 40px; height: 40px; border-radius: 50%; display: inline-flex; align-items: center; justify-content: center; font-size: 16px;">📧</div>
                            </div>
                            <div style="display: inline-block; margin: 0 10px;">
                                <div style="background: #65A30D; color: white; width: 40px; height: 40px; border-radius: 50%; display: inline-flex; align-items: center; justify-content: center; font-size: 16px;">📱</div>
                            </div>
                        </div>
                       \s
                        <p style="color: #71717A; margin: 20px 0 0; font-size: 12px;">
                            © 2024 BreathSafe. Making air quality monitoring accessible to every community.
                        </p>
                       \s
                    </div>
                   \s
                </div>
               \s
                <!-- Footer Note -->
                <div style="text-align: center; margin: 20px 0;">
                    <p style="color: #71717A; font-size: 12px; margin: 0;">
                        This confirmation was sent because you submitted a community sensor request.
                    </p>
                </div>
               \s
            </div>
           \s
        </body>
        </html>
       \s""";
            emailService.sendHtmlEmail(event.email(), "Welcome to BreathSafe", body);
            logger.info("Email sent to {}", event.email());
        } catch (MessagingException e) {
            logger.error("Failed to send email: {}", e.getMessage());
        }
    }
}
