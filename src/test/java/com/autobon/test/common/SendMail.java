package com.autobon.test.common;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * Created by admin on 2016/5/20.
 */
public class SendMail{
    public String Sender(String subject, String msg, String sendTo, String fromMail, String user, String pw, String fromName, String protocol, String host, String port){
        try{
            final String username = user;
            final String pass = pw;
            //需要认证
            Properties props = new Properties();
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.auth", "true");
            props.put("mail.transport.protocol", protocol);
            props.put("mail.from", fromMail);
            //创建发送器
            JavaMailSenderImpl sender = new JavaMailSenderImpl();
            sender.setHost(host);
            sender.setUsername(username);
            sender.setPassword(pass);
            //创建消息
            MimeMessage message = sender.createMimeMessage();
            message.addHeader("X-Mailer", "Java Mailer");
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setTo(sendTo);
            helper.setFrom(fromMail, fromName);
            helper.setSubject(subject);
            helper.setText(msg);
            helper.setSentDate(new Date());
            //开始发送
            sender.setJavaMailProperties(props);
            sender.send(message);
        }catch(Exception e){
            System.out.println("Error:" + e);
            return "Failure";
        }
        return "Success";
    }

    //测试
    public void main(String args[])throws Exception
    {
        String subject = "测试邮件";//标题
        String sendTo = "775473732@qq.com";//接收者邮件
        String fromMail = "775473732@qq.com";//发送者邮件
        String user = "775473732@qq.com";//发送者用户
        String pw = "8823194641025";//发送者邮件密码
        String fromName = "Chen";//发送者名字
        String protocol = "smtp";//协议
        String host = "smtp.my.com";//发送主机
        String port = "25";//端口
        String msg = "简单邮件发送。";//发送内容
        String ret = Sender(subject, msg, sendTo, fromMail, user, pw, fromName, protocol, host, port);

        System.out.println("邮件发送结果：" + ret);

    }
}
