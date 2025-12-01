package com.cassinisys.erp.service.notification.sms;

/**
 * Created by reddy on 15/03/16.
 */
public class InvoiceOrderSms extends SMSObject{
    public InvoiceOrderSms() {
        super.setTemplateId("e66756e17bee99297ce5257a");
    }

    public InvoiceOrderSms(String poNumber, String invoiceNumber, String invoiceAmount) {
        this();

        super.getPlaceholders().put("poNumber", poNumber);
        super.getPlaceholders().put("invoiceTo", poNumber);

        super.getPlaceholders().put("invoiceNumber", invoiceNumber);
        super.getPlaceholders().put("invoiceAmount", invoiceAmount);
    }
}
