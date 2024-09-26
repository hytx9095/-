package com.wrbi.springbootinit.model.dto.integralLog;

import com.wrbi.springbootinit.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 *

 */
@EqualsAndHashCode(callSuper = true)
@Data
public class IntegralLogQueryRequest extends PageRequest implements Serializable {

    private Long id;

    private static final long serialVersionUID = 1L;
}