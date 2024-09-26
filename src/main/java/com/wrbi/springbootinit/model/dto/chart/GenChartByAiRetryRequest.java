package com.wrbi.springbootinit.model.dto.chart;

import lombok.Data;

import java.io.Serializable;

/**
 * 文件上传请求
 *

 */
@Data
public class GenChartByAiRetryRequest implements Serializable {

    /**
     * 图表id
     */
    private Long chartId;

    private static final long serialVersionUID = 1L;
}