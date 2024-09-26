package com.wrbi.springbootinit.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求
 *

 */
@Data
public class UserSendEmailRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 邮件主题
     */
    private String subject;

    /**
     * 邮件内容
     */
    private String text;
}
