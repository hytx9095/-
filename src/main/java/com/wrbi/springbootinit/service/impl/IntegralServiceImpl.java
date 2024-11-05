package com.wrbi.springbootinit.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wrbi.springbootinit.common.ErrorCode;
import com.wrbi.springbootinit.common.ResultUtils;
import com.wrbi.springbootinit.common.UserContext;
import com.wrbi.springbootinit.constant.IntegralConstant;
import com.wrbi.springbootinit.constant.RedisConstant;
import com.wrbi.springbootinit.constant.UserConstant;
import com.wrbi.springbootinit.exception.BusinessException;
import com.wrbi.springbootinit.model.entity.Integral;
import com.wrbi.springbootinit.model.entity.IntegralLog;
import com.wrbi.springbootinit.model.enums.IntegralEnum;
import com.wrbi.springbootinit.model.enums.IntegralTypeEnum;
import com.wrbi.springbootinit.model.enums.ReSignInEnum;
import com.wrbi.springbootinit.service.IntegralLogService;
import com.wrbi.springbootinit.service.IntegralService;
import com.wrbi.springbootinit.mapper.IntegralMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
* @author wang'ren
* @description 针对表【integral(积分表)】的数据库操作Service实现
* @createDate 2024-09-11 13:17:04
*/
@Service
@Slf4j
public class IntegralServiceImpl extends ServiceImpl<IntegralMapper, Integral>
    implements IntegralService{

    @Resource
    private IntegralLogService integralLogService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean signIn(int month, int day, boolean isReSignIn) {

        long userId = UserContext.getUserId();
        //位图key
        String signKey = String.format(RedisConstant.USER_SIGN_IN, LocalDate.now().getYear(), month, userId);
        //检测是否用户今日签到过,用getBit可以取出该用户具体日期的签到状态(位图的值只有两个,1或者0,这里1代表true)
        if (stringRedisTemplate.opsForValue().getBit(signKey, day)) {
            return false;
        }
        Boolean setResult = stringRedisTemplate.opsForValue().setBit(signKey, day, true);
        if (setResult){
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "签到失败");
        }
        //保存积分记录
        IntegralLog integralLog = new IntegralLog();
        int signInCount = getSignInCount();
        integralLog.setUserId(userId);
        //是否补签
        if (isReSignIn){
            integralLog.setIntegralType(IntegralTypeEnum.REPAIR_SIGN_IN.getValue());
            integralLog.setIntegral(IntegralConstant.REPAIR_SIGN_IN);
            integralLog.setBak(IntegralTypeEnum.REPAIR_SIGN_IN.getText());
        } else {
            integralLog.setIntegralType(IntegralTypeEnum.SIGN_IN.getValue());
            integralLog.setIntegral(IntegralConstant.SIGN_IN);
            integralLog.setBak(IntegralTypeEnum.SIGN_IN.getText());
            //是否连续签到
            if (signInCount % 5 == 0){
                integralLog.setIntegralType(IntegralTypeEnum.CONTINUOUS_SIGN_IN.getValue());
                integralLog.setIntegral(IntegralConstant.CONTINUE_SIGN_IN_INTEGRAL);
            }
        }

        integralLog.setOperationTime(new Date());
        integralLog.setCreateTime(new Date());
        boolean save = integralLogService.save(integralLog);
        if (!save){
            stringRedisTemplate.opsForValue().setBit(signKey, day, false);
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "签到失败");
        }
        //更新积分表
        Integral integral = lambdaQuery().eq(Integral::getUserId, userId).one();
        if (signInCount % 5 == 0){
            integral.setTotalIntegral(integral.getTotalIntegral() + IntegralConstant.CONTINUE_SIGN_IN_INTEGRAL);
        }
        if (isReSignIn){
            integral.setTotalIntegral(integral.getTotalIntegral() + IntegralConstant.REPAIR_SIGN_IN);
            integral.setSignInYesterday(ReSignInEnum.REPAIR_SIGN_IN.getValue());
        } else {
            integral.setTotalIntegral(integral.getTotalIntegral() + IntegralConstant.SIGN_IN);
            integral.setSignInToday(1);
        }
        integral.setSignInCount(signInCount);
        this.updateById(integral);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean genChartByIntegral(long userId) {
        log.info("生成图表扣减积分");
        //保存积分记录
        IntegralLog integralLog = new IntegralLog();
        integralLog.setUserId(userId);
        integralLog.setIntegralType(IntegralTypeEnum.CONSUME.getValue());
        integralLog.setIntegral(IntegralConstant.GEN_CHART_INTEGRAL);
        integralLog.setBak(IntegralTypeEnum.CONSUME.getText());
        integralLog.setOperationTime(new Date());
        integralLog.setCreateTime(new Date());
        boolean save = integralLogService.save(integralLog);
        if (!save){
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        //更新总积分
        Integral integral = lambdaQuery().eq(Integral::getUserId, userId).one();
        integral.setTotalIntegral(integral.getTotalIntegral() + IntegralConstant.GEN_CHART_INTEGRAL);
        this.updateById(integral);
        return true;
    }

    @Override
    public int getSignInCount() {
        //日期
        int day = LocalDate.now().getDayOfMonth();
        int month = LocalDate.now().getMonthValue();
        //判断redis中是否存在key
        String signKey = String.format(RedisConstant.USER_SIGN_IN, LocalDate.now().getYear(), month, UserContext.getUserId());
        Boolean hasKey = stringRedisTemplate.hasKey(signKey);
        if (!hasKey){
            stringRedisTemplate.opsForValue().setBit(signKey, day, false);
        }
        //获取所有签到天数
        List<Long> longs = stringRedisTemplate.opsForValue().bitField(signKey,
                BitFieldSubCommands.create().get(BitFieldSubCommands.BitFieldType.unsigned(day)).valueAt(1));
        if (longs == null || longs.isEmpty()){
            return 0;
        }
        long num = longs.get(0);
        if (num == 0) {
            return 0;
        }
        //循环遍历
        int count = 0;
        while (true) {
            //让这个数字与1 做与运算，得到数字的最后一个bit位 判断这个数字是否为0
            if ((num & 1) == 0) {
                //如果为0，签到结束
                break;
            } else {
                count++;
            }
            //移除最后一个bit位
            num >>>= 1;
        }
        return count;
    }

    @Override
    public int getSignInToday() {

        //日期
        int month = LocalDate.now().getMonthValue();
        int day = LocalDate.now().getDayOfMonth();
        //判断redis中是否存在key
        String signKey = String.format(RedisConstant.USER_SIGN_IN, LocalDate.now().getYear(), month, UserContext.getUserId());
        Boolean hasKey = stringRedisTemplate.hasKey(signKey);
        if (!hasKey){
            stringRedisTemplate.opsForValue().setBit(signKey, day, false);
        }
        //检测是否用户今日签到过,用getBit可以取出该用户具体日期的签到状态(位图的值只有两个,1或者0,这里1代表true)
        if (stringRedisTemplate.opsForValue().getBit(signKey, day)) {
            return 1;
        }
        return 0;
    }

    @Override
    public int getSignInYesterday() {

        DateTime yesterday = DateUtil.yesterday();
        //日期
        int month = yesterday.month() + 1;
        int day = yesterday.dayOfMonth();
        //判断redis中是否存在key
        String signKey = String.format(RedisConstant.USER_SIGN_IN, LocalDate.now().getYear(), month, UserContext.getUserId());
        Boolean hasKey = stringRedisTemplate.hasKey(signKey);
        if (!hasKey){
            stringRedisTemplate.opsForValue().setBit(signKey, day, false);
        }
        //检测是否用户今日签到过,用getBit可以取出该用户具体日期的签到状态(位图的值只有两个,1或者0,这里1代表true)
        if (stringRedisTemplate.opsForValue().getBit(signKey, day)) {
            return 2;
        }
        return 0;
    }

    @Override
    public List<Integer> getSignInDates() {
        List<Integer> signInDates = new ArrayList<>();
        //日期
        int month = LocalDate.now().getMonthValue();
        int day = Integer.parseInt(LocalDate.now().format(DateTimeFormatter.ofPattern("dd")));
        //判断redis中是否存在key
        String signKey = String.format(RedisConstant.USER_SIGN_IN, LocalDate.now().getYear(), month, UserContext.getUserId());
        Boolean hasKey = stringRedisTemplate.hasKey(signKey);
        if (!hasKey){
            stringRedisTemplate.opsForValue().setBit(signKey, day, false);
        }
        //获取所有签到天数
        List<Long> longs = stringRedisTemplate.opsForValue().bitField(signKey,
                BitFieldSubCommands.create().get(BitFieldSubCommands.BitFieldType.unsigned(day)).valueAt(1));
        if (longs == null || longs.isEmpty()){
            return Collections.emptyList();
        }
        long num = longs.get(0);
        if (num == 0) {
            return Collections.emptyList();
        }
        //循环遍历
        while (day > 0) {
            //让这个数字与1 做与运算，得到数字的最后一个bit位 判断这个数字是否为0
            if ((num & 1) == 1) {
                //如果为0，签到结束
                signInDates.add(day);
            }
            num >>>= 1;
            day--;
        }
        return signInDates;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean buyIntegral(int price) {
        //保存积分记录
        IntegralLog integralLog = new IntegralLog();
        integralLog.setUserId(UserContext.getUserId());
        integralLog.setIntegralType(IntegralTypeEnum.BUY.getValue());
        integralLog.setIntegral(IntegralEnum.getIntegralNumByPrice(price));
        integralLog.setBak(IntegralTypeEnum.BUY.getText());
        integralLog.setOperationTime(new Date());
        integralLog.setCreateTime(new Date());
        boolean save = integralLogService.save(integralLog);
        if (!save){
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "保存积分记录失败");
        }
        //修改总积分
        Integral integral = lambdaQuery().eq(Integral::getUserId, UserContext.getUserId()).one();
        integral.setTotalIntegral(integral.getTotalIntegral() + IntegralEnum.getIntegralNumByPrice(price));
        boolean update = this.updateById(integral);
        if (!update){
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新积分表失败");
        }
        return true;
    }
}




