package com.cassinisys.platform.api.common;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.service.common.EmailService;
import com.cassinisys.platform.util.EmailDetails;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * Created by lakshmi on 7/10/2016.
 */

@RestController
@RequestMapping("/common/emails")
@Api(tags = "PLATFORM.COMMON",description = "Common endpoints")
public class EmailController extends BaseController {


    @Autowired
    private EmailService emailService;

    @RequestMapping(method = RequestMethod.POST)
    public String sendMail(@RequestBody EmailDetails emailDetails,MultipartHttpServletRequest request) {
          return emailService.sendMailService(emailDetails,request);
    }


}