package com.tongji.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 通用异步任务线程池配置。
 *
 * <p>提供名为 {@code taskExecutor} 的执行器，供项目中的异步后台任务复用；
 * 队列满时由提交任务的线程执行，避免任务被静默丢弃。关闭应用时最多等待 60 秒完成存量任务。</p>
 */
@Configuration
public class ThreadPoolConfig {

    /** @return 已完成容量、拒绝策略与优雅停机配置的任务执行器 */
    @Bean(name = "taskExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(200);
        executor.setKeepAliveSeconds(30);
        executor.setThreadNamePrefix("NoteExecutor-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.initialize();
        return executor;
    }
}
