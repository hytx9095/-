package com.wrbi.springbootinit.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class IntegralDatesVO implements Serializable {

    /**
     * id
     */
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
     * 连续签到天数
     */
    private Integer signInCount;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 签到天数列表
     */
    private List<Integer> signInDates;

    private static final long serialVersionUID = 1L;
}
