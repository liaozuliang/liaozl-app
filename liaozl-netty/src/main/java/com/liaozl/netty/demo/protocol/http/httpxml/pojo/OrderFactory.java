package com.liaozl.netty.demo.protocol.http.httpxml.pojo;

import java.util.Arrays;

/**
 * @author liaozuliang
 * @date 2016-12-13
 */
public class OrderFactory {

    public static Order getOrder(long orderNumber) {
        Order order = new Order();

        order.setOrderNumber(orderNumber);
        order.setShipping(Shipping.DOMESTIC_EXPRESS);
        order.setTotal(1000f);

        Customer customer = new Customer();
        customer.setCustomerNumber(1);
        customer.setFirstName("廖");
        customer.setLastName("祖亮");
        customer.setMiddleNames(Arrays.asList(new String[]{"廖祖亮", "Liaozuliang"}));

        order.setCustomer(customer);

        Address billTo = new Address();
        billTo.setCountry("中国");
        billTo.setState("江西省");
        billTo.setCity("赣州市");
        billTo.setStreet1("上犹县");
        billTo.setStreet2("水南大桥");
        billTo.setPostCode("341200");

        order.setBillTo(billTo);

        Address shipTo = new Address();
        shipTo.setCountry("中国");
        shipTo.setState("北京");
        shipTo.setCity("北京");
        shipTo.setStreet1("中关村");
        shipTo.setStreet2("和平路");
        shipTo.setPostCode("120005");

        order.setShipTo(shipTo);

        return order;
    }
}
