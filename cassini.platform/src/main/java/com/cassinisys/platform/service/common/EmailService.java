package com.cassinisys.platform.service.common;

import com.cassinisys.platform.util.EmailDetails;
import com.cassinisys.platform.util.HTMLTemplateUtils;
import com.cassinisys.platform.util.MailSendUtilClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * Created by lakshmi on 7/10/2016.
 */
@Service
public class EmailService {


    @Autowired
    private MailSendUtilClient mailSendUtilClient;

    @Autowired
    private HTMLTemplateUtils templateUtils;

    @Transactional
    public String sendMailService(EmailDetails emailDetails,MultipartHttpServletRequest request){
        String status="failed";
        try{

            if(emailDetails!=null){

                if(emailDetails.getTemplateUrl()!=null){
                    String strTemplate = HTMLTemplateUtils.getTemplateAsString(emailDetails.getTemplateUrl(),request.getServletContext());
                    emailDetails.setText(strTemplate);

                }

            mailSendUtilClient.sendEmailToMultiple(emailDetails,request.getFileMap());
            status="success";
            }

        }catch (Exception exp){
            status="failed";
        }

        return status;
    }

}
