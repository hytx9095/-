package com.wrbi.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 积分表
 * @TableName integral
 */
@TableName(value ="integral")
@Data
public class Integral implements Serializable {
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
     * 总积分
     */
    private Integer totalIntegral;

    /**
     * 今日是否签到
     */
    private Integer signInToday;

    /**
     * 补签
     */
    private Integer signInYesterday;

    /**
     * 连续签到天数
     */
    private Integer signInCount;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否删除
     */
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}