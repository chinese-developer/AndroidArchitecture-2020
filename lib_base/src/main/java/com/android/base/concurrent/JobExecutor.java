package com.android.base.concurrent;

import java.util.concurrent.Executor;


/**
 * Smart 执行器
 *
 * @see <a href="https://github.com/litesuits/android-lite-go">android-lite-go</a>
 */
public class JobExecutor {

    private static SmartExecutor smartExecutor;

    /**
     * 任务数量超出[最大并发数]后，自动进入[等待队列]，等待当前执行任务完成后按策略进入执行状态
     * @param policy SchedulePolicy.LastInFirstRun 后进先执行， SchedulePolicy.FirstInFistRun 先进先执行
     */
    public static void setSchedulePolicy(SchedulePolicy policy) {
        if (smartExecutor != null) {
            smartExecutor.setSchedulePolicy(policy);
        }
    }

    /**
     * 后续添加新任务数量超出[等待队列]大小时，执行过载策略：
     * @param policy DiscardNewTaskInQueue 抛弃队列内最新任务
     *               DiscardOldTaskInQueue 抛弃队列内最旧任务
     *               DiscardCurrentTask 抛弃当前正在执行的任务
     *               当等待队列满载后 CallerRuns 切换到 Caller 线程中
     *               ThrowExecption 当等待队列满载后 抛异常
     */
    public static void setOverloadPolicy(OverloadPolicy policy) {
        if (smartExecutor != null) {
            smartExecutor.setOverloadPolicy(policy);
        }
    }

    /**
     * 开发者均衡性能和业务场景，自己调整同一时段的最大并发数量
     * @param coreSize number of concurrent threads at the same time, recommended core size is CPU count
     */
    public static void setCoreSize(int coreSize) {
        if (smartExecutor != null) {
            smartExecutor.setCoreSize(coreSize);
        }
    }

    /**
     * 开发者均衡性能和业务场景，自己调整最大排队线程数量
     * @param queueSize adjust maximum number of waiting queue size by yourself or based on phone performance
     */
    public static void setQueueSize(int queueSize) {
        if (smartExecutor != null) {
            smartExecutor.setQueueSize(queueSize);
        }
    }

    static {
        // 智能并发调度控制器：设置[最大并发数]，和[等待队列]大小
        smartExecutor = new SmartExecutor();
        // 任务数量超出[最大并发数]后，自动进入[等待队列]，等待当前执行任务完成后按策略进入执行状态：后进先执行。
        smartExecutor.setSchedulePolicy(SchedulePolicy.LastInFirstRun);
        // 后续添加新任务数量超出[等待队列]大小时，执行过载策略：抛弃队列内最旧任务。
        smartExecutor.setOverloadPolicy(OverloadPolicy.DiscardOldTaskInQueue);
    }

    public static Executor getJobExecutor() {
        return smartExecutor;
    }

}
