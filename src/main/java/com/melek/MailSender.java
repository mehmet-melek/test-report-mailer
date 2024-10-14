package com.melek;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class MailSender {

    private MailSender() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void sendHtmlReportEmail(String toEmail, String subject) throws IOException, MessagingException {

        String smtpHost = "smtp.example.com";
        String smtpPort = "587";
        String username = "your-email@example.com";
        String password = "your-password";
        String htmlFilePath = "target/test-report-mail/report.html";

        // Read the HTML content from the file
        String htmlContent = new String(Files.readAllBytes(Paths.get(htmlFilePath)));

        // Setup mail server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort);

        // Create a session with authentication
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        // Compose the email message
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject(subject);

        // Set the HTML content as the body of the email
        message.setContent(htmlContent, "text/html; charset=utf-8");

        // Send the email
        Transport.send(message);
    }
}
