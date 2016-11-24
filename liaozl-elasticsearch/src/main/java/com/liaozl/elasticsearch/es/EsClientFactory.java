package com.liaozl.elasticsearch.es;

import org.apache.log4j.Logger;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.net.InetAddress;

/**
 * @author liaozuliang
 * @date 2016-10-18
 */
public class EsClientFactory {

    private static final Logger logger = Logger.getLogger(EsClientFactory.class);

    private static final String ES_HOST = "192.168.10.32";
    private static final int ES_PORT = 9300;

    public static Client getEsClient() {
        try {
            Client client = TransportClient.builder().build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ES_HOST), ES_PORT));
            return client;
        } catch (Exception e) {
            logger.error("get elasticsearch client error: ", e);
            new RuntimeException("获取elasticsearch连接出错");
        }

        return null;
    }
}
