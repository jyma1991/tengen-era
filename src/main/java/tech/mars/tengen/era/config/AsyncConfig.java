package tech.mars.tengen.era.config;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import tech.mars.tengen.era.utils.GsonUtils;


/**
 * 类AsyncConfig.java的实现描述：把@Async默认线程池SimpleAsyncTaskExecutor改为ThreadPoolTaskExecutor线程池
 * 
 * <pre>
 * org.springframework.scheduling.annotation.Async默认异步配置使用的是SimpleAsyncTaskExecutor线程池
 * 该线程池默认来一个任务创建一个线程，在压测情况下，
 * 会有大量写库请求进入系统，这时就会不断创建大量线程，极有可能压爆服务器内存
 * </pre>
 * 
 * @author chenhongqiao 2019年3月22日 下午4:49:54
 */
@Slf4j
@Configuration
public class AsyncConfig implements AsyncConfigurer {

    @Value("${async.thread.pool.corePoolSize:10}")
    private int corePoolSize;

    @Value("${async.thread.pool.maxPoolSize:100}")
    private int maxPoolSize;

    @Value("${async.thread.pool.keepAliveSeconds:120}")
    private int keepAliveSeconds;

    @Value("${async.thread.pool.queueCapacity:10000}")
    private int queueCapacity;

    /**
     * 自定义线程池
     */
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("ThreadPoolTaskExecutor");
        // 核心线程数
        executor.setCorePoolSize(corePoolSize);
        // 最大线程数
        executor.setMaxPoolSize(maxPoolSize);
        // 线程池维护线程所允许的空闲时间(单位秒)
        executor.setKeepAliveSeconds(keepAliveSeconds);
        // 队列最大长度
        executor.setQueueCapacity(queueCapacity);
        //Reject策略预定义有四种： 
        //(1)ThreadPoolExecutor.AbortPolicy策略，是默认的策略,处理程序遭到拒绝将抛出运行时 RejectedExecutionException。 
        //(2)ThreadPoolExecutor.CallerRunsPolicy策略 ,调用者的线程会执行该任务,如果执行器已关闭,则丢弃. 
        //(3)ThreadPoolExecutor.DiscardPolicy策略，不能执行的任务将被丢弃. 
        //(4)ThreadPoolExecutor.DiscardOldestPolicy策略，如果执行程序尚未关闭，则位于工作队列头部的任务将被删除，然后重试执行程序（如果再次失败，则重复此过程）.
        // 线程池对拒绝任务(无线程可用)的处理策略 ThreadPoolExecutor.CallerRunsPolicy策略 ,调用者的线程会执行该任务,如果执行器已关闭,则丢弃.
        // 设置拒绝策略，使用预定义的异常处理类
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncUncaughtExceptionHandler() {
            @Override
            public void handleUncaughtException(Throwable e, Method method, Object... params) {
                log.error("【{}】方法，参数：{}，@Async的ThreadPoolTaskExecutor线程池执行任务发生未知异常.{}", method.getName(),
                        GsonUtils.toJson(params), e);
            }
        };
    }

    @Bean(name = "commonExecutor")
    public ThreadPoolTaskExecutor commonExecutor(){
        ThreadPoolTaskExecutor threadPoolTaskExecutor=new ThreadPoolTaskExecutor();
        //线程核心数目
        threadPoolTaskExecutor.setCorePoolSize(4);
        threadPoolTaskExecutor.setAllowCoreThreadTimeOut(true);
        //最大线程数
        threadPoolTaskExecutor.setMaxPoolSize(8);
        //配置队列大小
        threadPoolTaskExecutor.setQueueCapacity(10000);
        //配置线程池前缀
        threadPoolTaskExecutor.setThreadNamePrefix("commonExecutor-");
        //配置拒绝策略
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        //数据初始化
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }
}
