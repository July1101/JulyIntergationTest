package com.thg.report.enmu;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/5/19 20:52
 **/
public enum CasePace {
    /* 序列化 */
    DESERIALIZE(1),
    /* 数据预清理 */
    PRE_CLEAR(2),
    /* 数据初始化 */
    INIT(3),
    /* MOCK */
    MOCK(4),
    /* 测试执行 */
    ACTION(5),
    /* 数据校验 */
    CHECK(6),
    /* 数据后清理 */
    AFTER_CLEAR(7);

    CasePace(int value) {
        this.value = value;
    }

    private int value;

    public int getValue(){
        return value;
    }

}
