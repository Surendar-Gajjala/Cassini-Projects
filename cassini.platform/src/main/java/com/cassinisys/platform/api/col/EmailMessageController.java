package com.cassinisys.platform.api.col;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.col.EmailMessage;
import com.cassinisys.platform.service.col.EmailMessageService;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/col/email")
@Api(tags = "PLATFORM.COMMON",description = "Common endpoints")
public class EmailMessageController extends BaseController {
    @Autowired
    private EmailMessageService emailMessageService;
    @Autowired
    private PageRequestConverter pageRequestConverter;


    @RequestMapping(value = "/{objectId}", method = RequestMethod.GET)
    public List<EmailMessage> getObjectEmailMessages(@PathVariable("objectId") Integer objectId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return emailMessageService.getEmailMessages(objectId, pageable);
    }

    @RequestMapping(value = "/{objectId}/{messageId}", method = RequestMethod.GET)
    public void downloadAttachment(@PathVariable("objectId")Integer objectId,
                                   @PathVariable("messageId")Integer messageId,
                                   @RequestParam("fileName")String fileName,
                                   HttpServletResponse response) {
        emailMessageService.downloadAttachment(objectId, messageId, fileName, response);
    }

}
