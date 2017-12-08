package com.example.tahasaber.twsila.DataClasses;

/**
 * Created by mohamed on 20/06/17.
 */

public class MessageDataClass {
    private String msgBody;
    private String msgPublisher;
    private String msgPublisherId;

    public MessageDataClass() {
    }

    public MessageDataClass(String msgBody, String msgPublisher, String msgPublisherId) {
        this.msgBody = msgBody;
        this.msgPublisher = msgPublisher;
        this.msgPublisherId = msgPublisherId;

    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }

    public void setMsgPublisher(String msgPublisher) {
        this.msgPublisher = msgPublisher;
    }


    public String getMsgBody() {
        return msgBody;
    }

    public String getMsgPublisher() {
        return msgPublisher;
    }

    public String getMsgPublisherId() {
        return msgPublisherId;
    }

    public void setMsgPublisherId(String msgPublisherId) {
        this.msgPublisherId = msgPublisherId;
    }
}
