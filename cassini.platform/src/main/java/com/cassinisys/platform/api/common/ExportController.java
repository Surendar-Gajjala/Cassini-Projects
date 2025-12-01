package com.cassinisys.platform.api.common;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.common.Export;
import com.cassinisys.platform.service.common.ExportService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by lakshmi on 08-10-2017.
 */
@RestController
@RequestMapping("/common/exports")
@Api(tags = "PLATFORM.COMMON",description = "Common endpoints")
public class ExportController extends BaseController {


    @Autowired
    private ExportService exportService;


    @RequestMapping(value = "/html", method = RequestMethod.POST, produces = "text/html")
    @ResponseBody
    public String exportHtml(@RequestBody Export export,
                             HttpServletResponse response) {
        return exportService.getExportHtml(export);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/file/{fileId}/download")
    public void downloadExportFile(@PathVariable("fileId") String fileId, HttpServletResponse response) {
        exportService.downloadExportFile(fileId, response);

    }

    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String exportFile(@RequestParam("fileType") String fileType, @RequestBody Export export,
                                     HttpServletResponse response) {
        String fileName = exportService.exportFile(fileType, export, response);
        return fileName;
    }

}
