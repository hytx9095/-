package com.wrbi.springbootinit.model.dto.user;

import java.io.Serializable;
import lombok.Data;

/**
 * 用户更新个人信息请求
 *

 */
@Data
public class UserUpdateMyRequest implements Serializable {
    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 邮箱
     */
    private String email;

    private static final long serialVersionUID = 1L;
}