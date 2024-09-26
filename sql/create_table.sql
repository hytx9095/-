# 建表脚本

-- 创建库
create database if not exists wrbi;

-- 切换库
use wrbi;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    index idx_userAccount (userAccount)
) comment '用户' collate = utf8mb4_unicode_ci;

-- 图表表
create table if not exists chart
(
    id          bigint auto_increment comment 'id' primary key,
    goal        text                               null comment '分析目标',
    name        varchar(128)                       null comment '图表名称',
    chartData   text                               null comment '图表数据',
    chartType   varchar(128)                       null comment '图表类型',
    genChart    text                               null comment '生成的图表数据',
    genResult   text                               null comment '生成的分析结论',
    status      varchar(128)                       not null default 'wait' comment 'wait,running,succeed,failed',
    execMessage text                               null comment '执行信息',
    userId      bigint                             null comment '创建用户 id',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除'
) comment '图表信息表' collate = utf8mb4_unicode_ci;

-- 积分表
create table if not exists integral
(
    id            bigint auto_increment comment 'id' primary key,
    userId        bigint             not null comment '用户id',
    totalIntegral int(16)  DEFAULT '0' COMMENT '总积分',
    signInToday   boolean  DEFAULT NULL COMMENT '今日是否签到',
    signInCount   int(3)   DEFAULT '0' COMMENT '连续签到天数',
    createTime    datetime DEFAULT NULL COMMENT '创建时间',
    isDelete      tinyint  default 0 not null comment '是否删除'
) comment '积分表' collate = utf8mb4_unicode_ci;

-- 用户积分记录表
create table if not exists integral_log
(
    id            bigint auto_increment comment 'id' primary key,
    userId       bigint                 not null comment '用户id',
    integralType  int(3)       DEFAULT NULL COMMENT '积分类型 1.签到 2.连续签到 3.福利 4.补签 5.消费',
    integral      int(16)      DEFAULT '0' COMMENT '积分',
    bak           varchar(100) DEFAULT NULL COMMENT '积分补充文案',
    operationTime date         DEFAULT NULL COMMENT '操作时间(签到和补签的具体日期)',
    createTime    datetime     DEFAULT NULL COMMENT '创建时间',
    isDelete      tinyint      default 0 not null comment '是否删除'
) comment '积分记录表' collate = utf8mb4_unicode_ci;