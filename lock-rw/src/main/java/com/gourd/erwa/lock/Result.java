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
    private EventContainerType type;
    //时间点平均时间
    @JSONField(ordinal = 2)
    private double avgTime;
    //测试时间点
    @JSONField(ordinal = 3)
    private List<Long> times;

    Part(EventContainerType type, List<Long> times) {
        this.type = type;
        this.times = times;
        this.avgTime = times.stream().mapToLong(value -> value).average().orElse(-1D);
    }

    public EventContainerType getType() {
        return type;
    }

    public List<Long> getTimes() {
        return times;
    }

    public double getAvgTime() {
        return avgTime;
    }
}
