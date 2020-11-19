package com.tmp.demo.ssh;

public class Test5 {

    /**
     *
     *  新增字段：
     *      是否分片
     *      总分片
     *      当前分片
     *      JobGroup：汇总一次 调度 的所有 触发 记录；
     *      RetryParentId：记录重试分支任务的 Parent 任务；
     *
     * 1、分片
     *      LogId       任务类型      分片         GroupId        Status      RetryCont
     *      ————————————————————————————————————————————————————————————————————————————————————————
     *      1           分片                      0(Main)        SUC
     *      - 11        分片          1-3         1(branch)      SUC
     *      - 12        分片          2-3         1(branch)      SUC
     *      - 13        分片          3-3         1(branch)      SUC
     *
     *        更新Group ：分片成功 》 检查全部分片 》 寻找 Group 》DO
     *
     *
     * 2、重试
     *      LogId       任务类型     分片         GroupId        Status      RetryParentId
     *      ————————————————————————————————————————————————————————————————————————————————————————
     *      1           普通       1-1             0(Main)        SUC         0
     *      - 11        普通       1-1          1(branch)      FAIL        1
     *      - 12        普通       1-1          1(branch)      SUC         1
     *
     *        更新Group：普通重试成功 》寻找 Group 》 DO
     *
     * 3、分片 & 重试
     *      LogId       任务类型      分片         GroupId        Status      RetryParentId   RetryCont
     *      ————————————————————————————————————————————————————————————————————————————————————————
     *      1           分片                      0(Main)        SUC
     *      - 11        分片          1-3         1(branch)      SUC
     *      - 12        分片          2-3         1(branch)      SUC
     *      - 13        分片          3-3         1(branch)      FAIL       0              2>1
     *      - 131       分片          3-3         1(branch)      SUC        13
     *
     *        更新Group：分片重试成功 》 检查全部分片 》 寻找 Group 》DO
     *        （ 更新Group：分片重试成功 》 寻找 RetryParentId 》寻找 RetryParentId 的 Group 》 DO ）
     *
     */


}
