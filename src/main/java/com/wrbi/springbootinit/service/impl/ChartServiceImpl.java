package com.wrbi.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wrbi.springbootinit.model.entity.Chart;
import com.wrbi.springbootinit.service.ChartService;
import com.wrbi.springbootinit.mapper.ChartMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart>
    implements ChartService{

}




