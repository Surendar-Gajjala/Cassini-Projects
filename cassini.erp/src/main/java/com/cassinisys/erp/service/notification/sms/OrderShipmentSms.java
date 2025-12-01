package com.cassinisys.erp.service.notification.sms;

/**
 * Created by reddy on 15/03/16.
 */
public class OrderShipmentSms extends SMSObject{
    public OrderShipmentSms() {
        super.setTemplateId("453f8c993392095033b36cdf");
    }

    public OrderShipmentSms(String poNumber, String shipper, String lrNumber, String shippedDate, String contents) {
        this();

        super.getPlaceholders().put("poNumber", poNumber);
        super.getPlaceholders().put("shipper", shipper);
        super.getPlaceholders().put("lrNumber", lrNumber);
        super.getPlaceholders().put("shippedDate", shippedDate);
        super.getPlaceholders().put("contents", contents);
    }
}
