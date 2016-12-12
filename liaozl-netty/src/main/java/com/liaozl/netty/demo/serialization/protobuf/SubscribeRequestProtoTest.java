package com.liaozl.netty.demo.serialization.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;

/**
 * @author liaozuliang
 * @date 2016-12-12
 */
public class SubscribeRequestProtoTest {

    private static byte[] encode(SubscribeRequestProto.SubscribeRequest request) {
        return request.toByteArray();
    }

    private static SubscribeRequestProto.SubscribeRequest decode(byte[] body) throws InvalidProtocolBufferException {
        return SubscribeRequestProto.SubscribeRequest.parseFrom(body);
    }

    private static SubscribeRequestProto.SubscribeRequest createSubscribeRequest() {
        SubscribeRequestProto.SubscribeRequest.Builder builder = SubscribeRequestProto.SubscribeRequest.newBuilder();

        builder.setSubReqID(1);
        builder.setUserName("liaozl");
        builder.setProductName("Netty Book");
        builder.setPhoneNumber("13430474568");
        builder.setAddress("北京");

        return builder.build();
    }

    public static void main(String[] args) throws InvalidProtocolBufferException {
        SubscribeRequestProto.SubscribeRequest request = createSubscribeRequest();
        System.out.println("before encode: " + request.toString());

        SubscribeRequestProto.SubscribeRequest request2 = decode(encode(request));
        System.out.println("after decode: " + request2.toString());

        System.out.println("Assert equal: " + request.equals(request2));
    }
}
