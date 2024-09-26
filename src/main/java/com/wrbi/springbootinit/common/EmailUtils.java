package com.wrbi.springbootinit.common;

import cn.hutool.core.util.RandomUtil;
import com.wrbi.springbootinit.constant.EmailConstant;
import com.wrbi.springbootinit.constant.RedisConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Component
public class EmailUtils {

    private static StringRedisTemplate redisTemplate;

    @Autowired
    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        EmailUtils.redisTemplate = redisTemplate;
    }

    public static Session createSession() {

        //	账号信息
        String username = EmailConstant.SENDER_EMAIL;//邮箱发送账号
        String password = EmailConstant.PASSWORD;//	邮箱授权码

        //	创建一个配置文件，并保存
        Properties props = new Properties();

        //	SMTP服务器连接信息
        //  126——smtp.126.com
        //  163——smtp.163.com
        //  qqsmtp.qq.com"
        props.put("mail.smtp.host", EmailConstant.HOST);//	SMTP主机名

        //  126——25
        //  163——645
        props.put("mail.smtp.port", EmailConstant.PORT);//	主机端口号
        props.put("mail.smtp.auth", EmailConstant.AUTH);//	是否需要用户认证
        props.put("mail.smtp.starttls.enale", EmailConstant.STARTTLS);//	启用TlS加密

        Session session = Session.getInstance(props,new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // TODO Auto-generated method stub
                return new PasswordAuthentication(username,password);
            }
        });

        //  控制台打印调试信息
        session.setDebug(true);
        return session;

    }

//    public BaseResponse sendCodeMail(EmailDTO emailDTO) throws MessagingException {
//        //	创建Session会话
//        Session session = createSession();
//        //	创建邮件对象
//        MimeMessage message = new MimeMessage(session);
//        // 	设置主题
//        message.setSubject(emailDTO.getSubject());
//        // 	设置邮件正文
//        String verificationCode = RandomUtil.randomNumbers(6);
//        message.setText(emailDTO.getText() + verificationCode);
//        // 	设置发件人
//        message.setFrom(new InternetAddress(EmailConstant.getSenderEmail()));
//        // 	设置收件人
//        String email = emailDTO.getEmail();
//        message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
//        //	发送
//        Transport.send(message);
//        String key = RedisConstants.EMAIL_LOGIN_CODE + email;
//        redisTemplate.opsForValue().set(key, verificationCode, RedisConstants.EMAIL_LOGIN_TTL, TimeUnit.MINUTES);
//        System.out.println(VerificationCode.getCode());
//        return ResultUtils.success(null);
//    }

    public static BaseResponse sendCodeMail(String email) throws MessagingException {
        //	创建Session会话
        Session session = createSession();
        //	创建邮件对象
        MimeMessage message = new MimeMessage(session);
        // 	设置主题
        message.setSubject(EmailConstant.SUBJECT);
        // 	设置邮件正文
        String verificationCode = RandomUtil.randomNumbers(6);
        message.setText(EmailConstant.TEXT + verificationCode);
        // 	设置发件人
        message.setFrom(new InternetAddress(EmailConstant.SENDER_EMAIL));
        // 	设置收件人
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
        //	发送
        Transport.send(message);
        redisTemplate.opsForValue().set(RedisConstant.EMAIL_LOGIN_KEY + email, verificationCode);
        redisTemplate.expire(RedisConstant.EMAIL_LOGIN_KEY + email, 120, TimeUnit.SECONDS);
        return ResultUtils.success(null);
    }

//    public BaseResponse sendClockInMail(String email, String subject, String text) throws MessagingException {
//        //	创建Session会话
//        Session session = createSession();
//        //	创建邮件对象
//        MimeMessage message = new MimeMessage(session);
//        // 	设置主题
//        message.setSubject(subject);
//        // 	设置邮件正文
//        message.setText(text);
//        // 	设置发件人
//        message.setFrom(new InternetAddress(EmailConstant.getSenderEmail()));
//        // 	设置收件人
//        message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
//        //	发送
//        Transport.send(message);
//        return ResultUtils.success(null);
//    }
}
