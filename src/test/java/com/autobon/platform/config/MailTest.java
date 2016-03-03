package com.autobon.platform.config;

import com.autobon.platform.MvcTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


/**
 * Created by yuh on 2016/3/3.
 */
public class MailTest extends MvcTest{

    @Autowired
    JavaMailSender mailSender;

    @Test
    public void testSendMail() throws MessagingException {
        MimeMessage msg = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
        helper.setFrom("339445148@qq.com");
        helper.setTo("339445148@qq.com");
        helper.setSubject("测试邮件");
        helper.setText("<h3>就是一个测试邮件！</h3>", true);
        mailSender.send(msg);
    }
}
