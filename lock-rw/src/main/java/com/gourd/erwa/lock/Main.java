package com.gourd.erwa.lock;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * @author wei.Li
 */
class Main {

    //结果集收集目录
    private static final String OUT_DATA_PATH = "/lw/workfile/intellij_work/benchmark/lock-rw/example-data/";

    //事件数量
    private static final int FULL_NUM = 100_000;
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
    private static final ThreadPoolExecutor POOL_EXECUTOR = new ScheduledThreadPoolExecutor(READ_THREAD_NUM + WRITE_THREAD_NUM);

    //private static final EventContainerType[] TYPES = new EventContainerType[]{EventContainerType.NO_THREAD_SAFE};
    //待测试类型
    private static final EventContainerType[] TYPES = EventContainerType.values();

    public static void main(String[] args) throws Exception {

        preheatEnvironment();

        //结果集数据
        final List<Part> data = new ArrayList<>();

        //遍历所有实现读写操作的数据类型
        for (EventContainerType type : TYPES) {

            final List<Long> times = new ArrayList<>(ROUND_NUM);

            System.out.println("----------------------> " + type + " <-------------------------");

            //每个类型 循环测试次数
            for (int m = 0; m < ROUND_NUM; m++) {

                final EventContainer implInstance = type.getImplInstance();

                final Collection<Operation> calls = new ArrayList<>(READ_THREAD_NUM + WRITE_THREAD_NUM);

                //提交读线程
                for (int i = 0; i < READ_THREAD_NUM; i++) {
                    calls.add(new Operation.Reader(implInstance, RUN_CONDITION));
                }
                //提交写线程
                for (int i = 0; i < WRITE_THREAD_NUM; i++) {
                    calls.add(new Operation.Writer(implInstance, RUN_CONDITION));
                }

                final long start = System.currentTimeMillis();

                printThreadPoolState("invokeAll Before:");

                // 使用 invokeAny 提交，以阻塞方式运行，任何一个任务完成后即可取消全部任务，
                POOL_EXECUTOR.invokeAny(calls);

                printThreadPoolState("invokeAll After:");

                final long time = System.currentTimeMillis() - start;

                times.add(time);
                System.out.println("------------> RoundNum = " + m + " \t Time = " + time + " ms");
                TimeUnit.SECONDS.sleep(3L);
                System.out.println();
            }

            data.add(new Part(type, times));
        }

        data.sort((o1, o2) -> (int) (o1.getAvgTime() - o2.getAvgTime()));
        final Result result = new Result(RUN_CONDITION, data);


        final String x = JSON.toJSONString(result, SerializerFeature.PrettyFormat);
        System.out.println();
        System.out.println("----------------------> OUT JSON STRING <-------------------------");
        System.out.println();
        System.out.println(x);
        System.out.println();

        System.out.println("----------------------> OUT JSON FILE <-------------------------");
        //写出到 JSON 文件
        final File file = new File(OUT_DATA_PATH + "/" + RUN_CONDITION + ".json");

        try {
            file.getParentFile().mkdirs();
            Files.write(x, file, Charset.defaultCharset());
            System.out.println("OUT_DATA_PATH :" + file.getPath());

        } catch (IOException e) {
            e.printStackTrace();
        }

        POOL_EXECUTOR.shutdown();
    }

    /**
     * 预热线程池
     */
    private static void preheatEnvironment() {

        try {

            final int corePoolSize = POOL_EXECUTOR.getCorePoolSize();
            final List<Callable<Boolean>> callableArrayList = new ArrayList<>(corePoolSize);
            for (int i = 0; i < corePoolSize; i++) {
                callableArrayList.add(() -> true);
            }
            try {
                POOL_EXECUTOR.invokeAny(callableArrayList);
                TimeUnit.SECONDS.sleep(10L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void printThreadPoolState(String pre) {
        System.out.println(pre + "\t" + POOL_EXECUTOR);
    }

}
