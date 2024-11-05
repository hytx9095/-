package com.wrbi.springbootinit;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.wrbi.springbootinit.config.WxOpenConfig;
import javax.annotation.Resource;

import com.wrbi.springbootinit.service.IntegralService;
import com.wrbi.springbootinit.service.impl.IntegralServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.LocalDate;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 主类测试
 *

 */
@SpringBootTest
class MainApplicationTests {

    @Resource
    private WxOpenConfig wxOpenConfig;

    @Resource
    private IntegralServiceImpl integralServiceImpl;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void contextLoads() {
        DateTime yesterday = DateUtil.yesterday();
        DateTime today = DateTime.of(new Date());
        DateTime parse = DateUtil.parse("2024-01-02");
        System.out.println("yesterday:"+yesterday);
        System.out.println("today:"+today);
        int month1 = DateUtil.month(yesterday);
        int month2 = DateUtil.month(today);
        System.out.println(month1);
        System.out.println(month2);
        System.out.println(parse.month());
        int month = yesterday.month();
        int day = yesterday.dayOfMonth();
        System.out.println(month+":"+day);
    }

    @Test
    void redisTest() {
        Boolean aBoolean = stringRedisTemplate
                .opsForValue().setIfAbsent("lock_key_9", "123", 30, TimeUnit.SECONDS);
        System.out.println(aBoolean);
    }
}
