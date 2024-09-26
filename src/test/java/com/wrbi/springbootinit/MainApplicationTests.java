package com.wrbi.springbootinit;

import com.wrbi.springbootinit.config.WxOpenConfig;
import javax.annotation.Resource;

import com.wrbi.springbootinit.service.IntegralService;
import com.wrbi.springbootinit.service.impl.IntegralServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

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

    @Test
    void contextLoads() {
        System.out.println(wxOpenConfig);
    }

}
