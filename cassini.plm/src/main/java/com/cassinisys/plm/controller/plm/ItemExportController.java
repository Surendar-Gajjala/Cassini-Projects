package com.cassinisys.plm.controller.plm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.common.Export;
import com.cassinisys.plm.service.plm.ItemExportService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by lakshmi on 08-10-2017.
 */
@RestController
@RequestMapping("/itemexcel/exports")
@Api(tags = "PLM.ITEMS",description = "Items Related")
public class ItemExportController extends BaseController {

    @Autowired
    private ItemExportService exportService;

    @RequestMapping(method = RequestMethod.GET, value = "/csvFile/{fileId}/download")
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
