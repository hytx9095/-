package com.wrbi.springbootinit.model.dto.integral;

import com.wrbi.springbootinit.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 *

 */
@Data
public class BuyIntegralRequest implements Serializable {

    private Integer price;

    private static final long serialVersionUID = 1L;
}