package com.wrbi.springbootinit.service;

import com.wrbi.springbootinit.model.entity.Integral;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author wang'ren
* @description 针对表【integral(积分表)】的数据库操作Service
* @createDate 2024-09-11 13:17:04
*/
public interface IntegralService extends IService<Integral> {

    boolean signIn(int month, int day, boolean isReSignIn);
    boolean genChartByIntegral(long userId);
    int getSignInCount();

    int getSignInToday();
    int getSignInYesterday();

    List<Integer> getSignInDates();

    boolean buyIntegral(int price);

}
