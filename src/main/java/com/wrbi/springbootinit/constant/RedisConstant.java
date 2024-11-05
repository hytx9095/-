package com.wrbi.springbootinit.constant;

public interface RedisConstant {
    //邮箱登录key
    String EMAIL_LOGIN_KEY = "wrbi:login:email:";
    //人员签到位图key,一个位图存一个用户一年的签到状态,以userSign为标识,后面的三个参数是今年的年份、月份和用户的id
    String USER_SIGN_IN = "wrbi:userSign:%d:%d:%d";
    //人员补签key,一个Hash列表存用户一个月的补签状态,以userSign:retroactive为标识,后面的两个参数是当月的月份和用户的id
    String USER_RETROACTIVE_SIGN_IN = "wrbi:userSign:retroactive:%d:%d";
    //人员签到总天数key,以userSign:count为标识,后面的参数是用户的id
    String USER_SIGN_IN_COUNT = "wrbi:userSign:count:%d";
}
