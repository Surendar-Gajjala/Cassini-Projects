package com.cassinisys.erp.sms;

import com.cassinisys.erp.service.notification.sms.InvoiceOrderSms;
import org.junit.Test;

/**
 * Created by reddy on 9/2/15.
 */
public class SmsTest {

    @Test
    public void testSms() throws Exception {
        //new NewOrderSms("D00005", "Alpha Public School").sendTo("+919701030235");
        //new NewLoginAccountSms("raghuram", "cassini").sendTo("+919701030235");

        new InvoiceOrderSms("PO-454545", "INV-S45345", "12000").sendTo("+919701030235");
    }
}
