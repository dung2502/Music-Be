package com.project.musicwebbe.service.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    public void sendPaymentSuccessEmail(String recipientEmail, String userName, String vipPackage, LocalDateTime expiredDate)
            throws MessagingException, IOException {

        String htmlTemplate = Files.readString(
                new ClassPathResource("templates/payment-success.html").getFile().toPath(),
                StandardCharsets.UTF_8
        );

        String formattedDate = expiredDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        String content = htmlTemplate
                .replace("{{userName}}", userName)
                .replace("{{vipPackage}}", vipPackage)
                .replace("{{expiredDate}}", formattedDate);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(recipientEmail);
        helper.setSubject("Xác nhận thanh toán VIP thành công");
        helper.setText(content, true);
        helper.setFrom(senderEmail);
        ClassPathResource image = new ClassPathResource("templates/logo-music.png");
        helper.addInline("logoMusic", image);
        mailSender.send(message);
    }
}
