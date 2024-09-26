package com.wrbi.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wrbi.springbootinit.common.BaseResponse;
import com.wrbi.springbootinit.common.UserContext;
import com.wrbi.springbootinit.constant.RedisConstant;
import com.wrbi.springbootinit.model.entity.IntegralLog;
import com.wrbi.springbootinit.service.IntegralLogService;
import com.wrbi.springbootinit.mapper.IntegralLogMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
* @author wang'ren
* @description 针对表【integral_log(用户)】的数据库操作Service实现
* @createDate 2024-09-10 22:29:33
*/
@Service
public class IntegralLogServiceImpl extends ServiceImpl<IntegralLogMapper, IntegralLog>
    implements IntegralLogService{


}




