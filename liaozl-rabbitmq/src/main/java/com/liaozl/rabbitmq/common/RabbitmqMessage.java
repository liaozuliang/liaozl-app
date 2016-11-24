package com.liaozl.rabbitmq.common;

import java.util.Date;

/**
 * @author liaozuliang
 * @date 2016-09-29
 */
public class RabbitmqMessage {

    private Integer msgId;                      //消息Id
    private String exchange;                    //消息exchange
    private String routingKey;                  //消息routingKey
    private RabbitmqMessageStatusEnum status;   //消息状态
    private String msgBody;                     //消息内容
    private String errorLog;                    //发送失败的错误日志
    private Date createTime;                    //创建时间
    private Date sendTime;                      //发送时间

    public Integer getMsgId() {
        return msgId;
    }

    public void setMsgId(Integer msgId) {
        this.msgId = msgId;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public RabbitmqMessageStatusEnum getStatus() {
        return status;
    }

    public void setStatus(RabbitmqMessageStatusEnum status) {
        this.status = status;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }

    public String getErrorLog() {
        return errorLog;
    }

    public void setErrorLog(String errorLog) {
        this.errorLog = errorLog;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    @Override
    public String toString() {
        return "RabbitmqMessage{" +
                "msgId=" + msgId +
                ", exchange='" + exchange + '\'' +
                ", routingKey='" + routingKey + '\'' +
                ", status=" + status +
                ", msgBody='" + msgBody + '\'' +
                ", errorLog='" + errorLog + '\'' +
                ", createTime=" + createTime +
                ", sendTime=" + sendTime +
                '}';
    }
}
