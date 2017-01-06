package com.gourd.erwa.lock;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * 结果集收集
 *
 * @author wei.Li
 */
public class Result {

    @JSONField(ordinal = 1)
    private RunCondition runCondition;
    @JSONField(ordinal = 2)
    private List<Part> data;

    Result(RunCondition runCondition, List<Part> data) {
        this.runCondition = runCondition;
        this.data = data;
    }

    public RunCondition getRunCondition() {
        return runCondition;
    }

    public List<Part> getData() {
        return data;
    }
}

class Part {

    //测试数据类型
    @JSONField(ordinal = 1)
    private String type;
    //时间点平均时间
    @JSONField(ordinal = 2)
    private double avgTime;
    //测试时间点
    @JSONField(ordinal = 3)
    private List<Long> times;

    Part(EventContainerType type, List<Long> times) {
        this.type = type.getDescribe();
        this.times = times;
        final int size = times.size();
        //去掉最小值最大值求平均数，size == 0 异常不处理
        this.avgTime = size == 1 ? times.get(0) : times.stream().sorted().skip(0).limit(size - 1).mapToLong(value -> value).average().orElse(-1D);
    }

    public String getType() {
        return type;
    }

    public List<Long> getTimes() {
        return times;
    }

    public double getAvgTime() {
        return avgTime;
    }
}
