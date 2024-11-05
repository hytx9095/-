package com.wrbi.springbootinit.constant;

import org.springframework.beans.factory.annotation.Value;

public interface EmailConstant {

    //接收者邮箱
    String RECEIVER_EMAIL = "3221481947@qq.com";

    //发送者邮箱
    String SENDER_EMAIL = "2167474918@qq.com";

    //邮箱授权码
    String PASSWORD = "zydxblryxvbwebei";

    //SMTP主机名
    String HOST = "smtp.qq.com";

    //主机端口号
    String PORT = "587";

    //是否需要用户认证
    String AUTH = "true";

    //启用TlS加密
    String STARTTLS = "true";

    //邮件主题
    String SUBJECT = "智能数据分析与图表生成系统登录验证码";

    //邮件内容
    String TEXT = "验证码";
}
