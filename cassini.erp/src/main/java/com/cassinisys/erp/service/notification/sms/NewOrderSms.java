package com.cassinisys.erp.service.notification.sms;

/**
 * Created by reddy on 9/2/15.
 */
public class NewOrderSms extends SMSObject {


    public NewOrderSms() {
        super.setTemplateId("d905ac300a066d9c7ab944eb");
    }

    public NewOrderSms(String orderNumber, String customerName) {
        this();

        super.getPlaceholders().put("orderNumber", orderNumber);
        super.getPlaceholders().put("customerName", customerName);
    }

    public void setOrderNumber(String orderNumber) {
        super.getPlaceholders().put("orderNumber", orderNumber);
    }

    public void setCustomerName(String customerName) {
        super.getPlaceholders().put("customerName", customerName);
    }
}
