package com.hhl.springbootlearn.demo1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author hhl
 * @version 1.0
 * @description
 * @date 2024/9/11 21:11
 */

@Slf4j
@Component
public class PostConstructTest {
    // 创建一个阻塞队列，大小为 50
    private static final BlockingQueue<Runnable> taskQueue = new ArrayBlockingQueue<>(50);

    // 创建一个固定大小的线程池
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(10);
    @PostConstruct
    public void postConstruct() {
        log.info("启动时自动执行  @PostConstruct 注解方法");
        // 启动一个线程，不断从阻塞队列中取出任务并提交给线程池执行
        Thread taskProcessor = new Thread(() -> {
            try {
                while (true) {
                    // 从队列中取出任务，如果没有任务会阻塞
                    Runnable task = taskQueue.take();
                    // 提交任务到线程池中执行
                    threadPool.submit(task);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        taskProcessor.start();

        // 模拟异步任务提交到阻塞队列
        for (int i = 0; i < 100; i++) {
            final int taskId = i;
            try {
                taskQueue.put(() -> {
                    System.out.println("Executing task " + taskId + " in thread: " + Thread.currentThread().getName());
                    try {
                        // 模拟任务执行时间
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            Thread.sleep(15000); // 等待一定时间以确保任务都提交到线程池
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 等待所有任务处理完毕后关闭线程池
        threadPool.shutdown();
        taskProcessor.interrupt(); // 中断 taskProcessor 线程
    }
}
