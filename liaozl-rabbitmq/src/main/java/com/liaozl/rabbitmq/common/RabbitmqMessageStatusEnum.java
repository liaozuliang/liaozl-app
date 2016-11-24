package com.liaozl.rabbitmq.common;

/**
 * 消息状态
 * @author liaozuliang
 * @date 2016-09-29
 */
public enum RabbitmqMessageStatusEnum {

    NOT_SEND("未发送"),
    SEND_SUCCESS("发送成功"),
    SEND_FAIL("发送失败");

    private String desc;

    private RabbitmqMessageStatusEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
