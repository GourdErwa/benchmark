package com.gourd.erwa.lock;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 运行条件设定
 *
 * @author wei.Li
 */
class RunCondition {

    @JSONField(ordinal = 1)
    private int fullNum = 10_0000;
    @JSONField(ordinal = 2)
    private int readThreadNum = 10;
    @JSONField(ordinal = 3)
    private int writeThreadNum = 10;
    @JSONField(ordinal = 4)
    private int roundNum = 10;

    RunCondition(int fullNum, int readThreadNum, int writeThreadNum, int roundNum) {
        this.fullNum = fullNum;
        this.readThreadNum = readThreadNum;
        this.writeThreadNum = writeThreadNum;
        this.roundNum = roundNum;
    }

    //get 方法必须为 public ，FastJSON JSON时 调用
    public int getFullNum() {
        return fullNum;
    }

    public int getReadThreadNum() {
        return readThreadNum;
    }

    public int getWriteThreadNum() {
        return writeThreadNum;
    }

    public int getRoundNum() {
        return roundNum;
    }

    @Override
    public String toString() {

        return String.format(
                "fullNum[%d]readThreadNum[%d]writeThreadNum[%d]roundNum[%d]",
                fullNum, readThreadNum, writeThreadNum, roundNum
        );

    }

    static class Builder {

        private int fullNum = 10_000;
        private int readThreadNum = 5;
        private int writeThreadNum = 5;
        private int roundNum = 10;

        private Builder() {
        }

        static Builder create() {
            return new Builder();
        }

        Builder setFullNum(int fullNum) {
            this.fullNum = fullNum;
            return this;
        }

        Builder setReadThreadNum(int readThreadNum) {
            this.readThreadNum = readThreadNum;
            return this;
        }

        Builder setWriteThreadNum(int writeThreadNum) {
            this.writeThreadNum = writeThreadNum;
            return this;
        }

        Builder setRoundNum(int roundNum) {
            this.roundNum = roundNum;
            return this;
        }

        RunCondition createRunCondition() {
            return new RunCondition(fullNum, readThreadNum, writeThreadNum, roundNum);
        }
    }
}
