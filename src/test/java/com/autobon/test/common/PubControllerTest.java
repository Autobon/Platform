package com.autobon.test.common;

import com.autobon.test.MvcTest;
import org.junit.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Date;
import java.util.Properties;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by yuh on 2016/2/17.
 */
public class PubControllerTest extends MvcTest {

    @Test
    public void getSkills() throws Exception {
        this.mockMvc.perform(get("/api/pub/technician/skills"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.result", is(true)));
    }

    @Test
    public void getOrderTypes() throws Exception {
        this.mockMvc.perform(get("/api/pub/orderTypes"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.result", is(true)));
    }

    @Test
    public void getWorkItems() throws Exception {
        this.mockMvc.perform(get("/api/pub/technician/workItems")
                .param("orderType", "1"))
            .andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andExpect(jsonPath("$.result", is(true)));
    }

    @Test
    public void getSmsCode() throws Exception {
        this.mockMvc.perform(get("/api/pub/verifySms")
                .param("phone", "18827075338"))
            .andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andExpect(jsonPath("$.result", is(true)));
    }

    @Test
    public void SendMail(){
            try{
                final String username = "775473732@qq.com";
                final String pass = "mzgxhmxbrekabedb";
                //需要认证
                Properties props = new Properties();/*
                props.put("mail.smtp.host", "smtp.qq.com");*/
                props.put("mail.smtp.starttls.enable",true);
                props.put("mail.smtp.auth", "true");
                props.put("mail.transport.protocol", "smtp");
                props.put("mail.from", "775473732@qq.com");
                props.put("mail.smtp.timeout",2500);
                props.put("mail.smtp.starttls.enable",true);
                //创建发送器
                JavaMailSenderImpl sender = new JavaMailSenderImpl();
                sender.setHost("smtp.qq.com");
                sender.setUsername(username);
                sender.setPassword(pass);
               /* sender.setPort(465);*/
                //创建消息
                MimeMessage message = sender.createMimeMessage();
               // message.addHeader("X-Mailer", "Java Mailer");
               /* MimeMessageHelper helper = new MimeMessageHelper(message);*/
                MimeMessageHelper helper = new MimeMessageHelper(message,true,"utf-8");


                helper.setTo("775473732@qq.com");
                helper.setFrom("775473732@qq.com", "xsh");
                helper.setSubject("测试邮件");
                helper.setText("简单邮件发送。");
                helper.setSentDate(new Date());

                //增加附件
               /* FileSystemResource file1 = new FileSystemResource(new File("C:/Users/admin/Desktop/员工手册.pdf"));
                FileSystemResource file2 = new FileSystemResource(new File("C:/Users/admin/Desktop/新员工入职指引.pdf"));
                FileSystemResource file3 = new FileSystemResource(new File("C:/Users/admin/Desktop/11.png"));
                helper.setText("<html><body><h1>aaa这只是一个测试</h1><img src='cid:identifier1235'></body></html>", true);
                helper.addAttachment("员工手册.pdf",file1);
                helper.addAttachment("新员工入职指引.pdf",file2);
                helper.addAttachment("11.png",file3);
                helper.addInline("identifier1235",file3);*/
                //开始发送
                sender.setJavaMailProperties(props);
                sender.send(message);
            }catch(Exception e){
                System.out.println("Error:" + e);

            }

        }

        /*//测试
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

            System.out.println("邮件发送结果：" + ret);*/




}
