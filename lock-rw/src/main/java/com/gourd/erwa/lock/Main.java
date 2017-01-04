package com.gourd.erwa.lock;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


/**
 * @author wei.Li
 */
class Main {

    //结果集收集目录
    private static final String OUT_DATA_PATH = "/lw/temp/benchmark/lock-rw/";

    private static final Map<EventContainerType, List<Future<?>>> JOB = new HashMap<>();
    //事件数量
    private static final int FULL_NUM = 10_0000;
    //读线程数量
    private static final int READ_THREAD_NUM = 5;
    //写线程数量
    private static final int WRITE_THREAD_NUM = 5;
    //循环次数
    private static final int ROUND_NUM = 20;
    //运行模拟条件
    private static final RunCondition RUN_CONDITION = RunCondition.Builder.create()
            .setFullNum(FULL_NUM)
            .setReadThreadNum(READ_THREAD_NUM)
            .setWriteThreadNum(WRITE_THREAD_NUM)
            .setRoundNum(ROUND_NUM)
            .createRunCondition();
    //固定大小线程池
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(READ_THREAD_NUM + WRITE_THREAD_NUM);

    private static final Object LOCK = new Object();
    //private static final EventContainerType[] TYPES = new EventContainerType[]{EventContainerType.OPTIMISTIC_STAMPED};
    //待测试类型
    private static final EventContainerType[] TYPES = EventContainerType.values();

    public static void main(String[] args) throws InterruptedException {


        EXECUTOR_SERVICE.submit(() -> {
            //预热线程池
        }).cancel(true);

        //结果集数据
        final List<Part> data = new ArrayList<>();

        //遍历所有实现读写操作的数据类型
        for (EventContainerType type : TYPES) {

            final List<Long> times = new ArrayList<>(ROUND_NUM);

            System.out.println("---------------------- " + type + " -------------------------");


            //每个类型 循环测试次数
            for (int m = 0; m < ROUND_NUM; m++) {

                final EventContainer implInstance = type.getImplInstance();
                //提交线程返回值，用于停止任务
                final List<Future<?>> futures = new ArrayList<>();
                //提交读线程
                for (int i = 0; i < READ_THREAD_NUM; i++) {
                    futures.add(EXECUTOR_SERVICE.submit(new Operation.Reader(implInstance, RUN_CONDITION)));
                }
                //提交写线程
                for (int i = 0; i < WRITE_THREAD_NUM; i++) {
                    futures.add(EXECUTOR_SERVICE.submit(new Operation.Writer(implInstance, RUN_CONDITION)));
                }

                JOB.put(type, futures);
                final long start = System.currentTimeMillis();
                synchronized (LOCK) {
                    LOCK.wait();
                }
                final long diff = System.currentTimeMillis() - start;
                times.add(diff);
                System.out.println("RoundNum = " + m + " \t Time = " + diff + " ms");
                TimeUnit.SECONDS.sleep(3L);
            }

            data.add(new Part(type, times));
        }

        final Result result = new Result(RUN_CONDITION, data);

        System.out.println();
        final String x = JSON.toJSONString(result, SerializerFeature.PrettyFormat);

        System.out.println(x);
        //写出到 JSON 文件
        final File file = new File(OUT_DATA_PATH + "/" + RUN_CONDITION + ".json");

        try {
            file.getParentFile().mkdirs();
            Files.write(x, file, Charset.defaultCharset());
            System.out.println("OUT_DATA_PATH :" + file.getPath());

        } catch (IOException e) {
            e.printStackTrace();
        }

        EXECUTOR_SERVICE.shutdown();
    }


    static void stop(EventContainerType containers) {

        final List<Future<?>> remove = JOB.remove(containers);
        if (remove != null) {
            remove.forEach((Future<?> future) -> future.cancel(true));
            synchronized (LOCK) {
                LOCK.notify();
            }
        }
    }
}
