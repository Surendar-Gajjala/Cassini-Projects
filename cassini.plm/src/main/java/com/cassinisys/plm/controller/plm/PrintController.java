package com.cassinisys.plm.controller.plm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.plm.model.dto.Print;
import com.cassinisys.plm.service.print.PrintService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/*
* Created by Suresh Cassini on 18-09-2020.
* */
@RestController
@Api(tags = "PLM.ITEMS",description = "Items Related")
@RequestMapping("/plm/print")
public class PrintController extends BaseController {

    @Autowired
    private PrintService printService;


    @RequestMapping(method = RequestMethod.GET, value = "/file/{fileName}/preview")
    public void previewFile(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        printService.previewFile(fileName, response);

    }

    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String printFile(@RequestParam("fileType") String fileType, @RequestBody Print print,
                            HttpServletResponse response) {
        String fileName = printService.print(fileType, print, response);
        return fileName;
    }


}