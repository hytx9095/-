package com.wrbi.springbootinit.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wrbi.springbootinit.common.BaseResponse;
import com.wrbi.springbootinit.common.ErrorCode;
import com.wrbi.springbootinit.common.ResultUtils;
import com.wrbi.springbootinit.common.UserContext;
import com.wrbi.springbootinit.constant.CommonConstant;
import com.wrbi.springbootinit.constant.RedisConstant;
import com.wrbi.springbootinit.exception.BusinessException;
import com.wrbi.springbootinit.model.dto.integral.BuyIntegralRequest;
import com.wrbi.springbootinit.model.dto.integralLog.IntegralLogQueryRequest;
import com.wrbi.springbootinit.model.entity.Integral;
import com.wrbi.springbootinit.model.entity.IntegralLog;
import com.wrbi.springbootinit.model.enums.IntegralEnum;
import com.wrbi.springbootinit.model.vo.SignInDatesVO;
import com.wrbi.springbootinit.service.IntegralLogService;
import com.wrbi.springbootinit.service.IntegralService;
import com.wrbi.springbootinit.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/integral")
@Slf4j
public class IntegralController {

    @Resource
    private IntegralService integralService;
    @Resource
    private IntegralLogService integralLogService;

    /**
     * 获取当前登录用户
     *
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<Integral> getIntegralByUserId() {
        Integral integral = integralService.lambdaQuery().eq(Integral::getUserId, UserContext.getUserId()).one();
        if (integral == null) {
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        }
        //获取连续签到天数
        int signInCount = integralService.getSignInCount();
        //获取今日签到状态
        int signInToday = integralService.getSignInToday();
        if (integral.getSignInCount() != signInCount || signInToday != integral.getSignInToday()) {
            integral.setSignInCount(signInCount);
            integral.setSignInToday(signInToday);
            integralService.updateById(integral);
        }
        return ResultUtils.success(integral);
    }

    /**
     * 签到
     *
     * @return
     */
    @PostMapping("/signIn")
    public BaseResponse<Boolean> saveSignIn() {
        return ResultUtils.success(integralService.signIn());
    }

    /**
     * 购买积分
     *
     * @return
     */
    @PostMapping("/buy")
    public BaseResponse<Boolean> buyIntegral(@RequestBody BuyIntegralRequest buyIntegralRequest) {

        Integer price = buyIntegralRequest.getPrice();
        if (!IntegralEnum.containsPrice(price)){
           throw new BusinessException(ErrorCode.PARAMS_ERROR, "价格不正确");
        }
        return ResultUtils.success(integralService.buyIntegral(price));
    }
    /**
     * 获取签到记录
     *
     * @return
     */
    @PostMapping("/log/list/page")
    public BaseResponse<Page<IntegralLog>> listIntegralLogByPage(@RequestBody IntegralLogQueryRequest integralLogQueryRequest) {
        if (integralLogQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = integralLogQueryRequest.getCurrent();
        long pageSize = integralLogQueryRequest.getPageSize();
        Page<IntegralLog> page = integralLogService.page(new Page<>(current, pageSize), getQueryWrapper(integralLogQueryRequest));
        return ResultUtils.success(page);
    }

    /**
     * 获取签到天数数组
     *
     * @return
     */
    @PostMapping("/log/signIn/dates")
    public BaseResponse<SignInDatesVO> getSignInDates() {
        SignInDatesVO signInDatesVO = new SignInDatesVO();
        List<Integer> signInDates = integralService.getSignInDates();
        signInDatesVO.setSignInDates(signInDates);
        return ResultUtils.success(signInDatesVO);
    }

    private QueryWrapper<IntegralLog> getQueryWrapper(IntegralLogQueryRequest integralLogQueryRequest) {
        QueryWrapper<IntegralLog> queryWrapper = new QueryWrapper<>();
        if (integralLogQueryRequest == null) {
            return queryWrapper;
        }
        String sortField = integralLogQueryRequest.getSortField();
        String sortOrder = integralLogQueryRequest.getSortOrder();

        queryWrapper.eq("userId", UserContext.getUserId());
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }
}
