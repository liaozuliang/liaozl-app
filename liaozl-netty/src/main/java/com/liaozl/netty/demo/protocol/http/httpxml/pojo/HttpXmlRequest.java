package com.liaozl.netty.demo.protocol.http.httpxml.pojo;

import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @author liaozuliang
 * @date 2016-12-13
 */
public class HttpXmlRequest {

    private FullHttpRequest request;
    private Object body;

    public HttpXmlRequest(FullHttpRequest request, Object body) {
        this.request = request;
        this.body = body;
    }

    public FullHttpRequest getRequest() {
        return request;
    }

    public void setRequest(FullHttpRequest request) {
        this.request = request;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
