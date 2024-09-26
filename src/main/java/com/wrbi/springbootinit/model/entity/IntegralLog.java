package com.wrbi.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户
 * @TableName integral_log
 */
@TableName(value ="integral_log")
@Data
public class IntegralLog implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 积分类型 1.签到 2.连续签到 3.福利 4.补签 5.消费
     */
    private Integer integralType;

    /**
     * 积分
     */
    private Integer integral;

    /**
     * 积分补充文案
     */
    private String bak;

    /**
     * 操作时间(签到和补签的具体日期)
     */
    private Date operationTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否删除
     */
    private Integer isdelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}