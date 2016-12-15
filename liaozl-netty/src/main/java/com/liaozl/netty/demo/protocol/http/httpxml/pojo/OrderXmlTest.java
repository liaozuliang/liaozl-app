package com.liaozl.netty.demo.protocol.http.httpxml.pojo;

import com.liaozl.utils.xml.XmlPojoUtil;

import java.util.Arrays;
import java.util.List;


public class OrderXmlTest {

    public static void main(String[] args) {
        List<Class> classList = Arrays.asList(new Class[]{Order.class});
        String xmlStr = XmlPojoUtil.pojoToXml(OrderFactory.getOrder(1), classList);
        System.out.println(xmlStr);

        Order order = XmlPojoUtil.xmlToPojo(xmlStr, classList);
        System.out.println(order);
    }

}
