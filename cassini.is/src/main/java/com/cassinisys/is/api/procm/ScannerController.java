package com.cassinisys.is.api.procm;
/**
 * The Class is for ScannerController
 **/

import com.cassinisys.is.model.procm.ISMachineItem;
import com.cassinisys.is.model.procm.ISManpowerItem;
import com.cassinisys.is.model.procm.ISMaterialItem;
import com.cassinisys.is.service.procm.ItemService;
import com.cassinisys.platform.api.core.BaseController;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;

@RestController
@RequestMapping("is/scanner")
@Api(name = "Barcode", description = "Barcode endpoint", group = "IS")
public class ScannerController extends BaseController {

    @Autowired
    private ItemService itemService;

    /**
     * The method used to generateItemBarcode
     **/
    @RequestMapping(value = "/barcode/materialItems/{id}", method = RequestMethod.GET)
    public void generateMaterialItemBarcode(@PathVariable("id") Integer id,
                                            HttpServletResponse response) {
        ISMaterialItem item = itemService.get(id);
        if (item != null) {
            BitMatrix bitMatrix;
            try {
                response.setContentType("image/png");
                OutputStream out = response.getOutputStream();
                String msg = "{0};{1};{2};{3}";
                msg = MessageFormat.format(msg, item.getId(), item.getItemNumber(), item.getDescription());
                bitMatrix = new Code128Writer().encode(msg, BarcodeFormat.CODE_128, 150, 80, null);
                MatrixToImageWriter.writeToStream(bitMatrix, "png", out);
                out.flush();
                ;
            } catch (WriterException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping(value = "/barcode/machineItems/{id}", method = RequestMethod.GET)
    public void generateMachineItemBarcode(@PathVariable("id") Integer id,
                                           HttpServletResponse response) {
        ISMachineItem item = itemService.getMachine(id);
        if (item != null) {
            BitMatrix bitMatrix;
            try {
                response.setContentType("image/png");
                OutputStream out = response.getOutputStream();
                String msg = "{0};{1};{2};{3}";
                msg = MessageFormat.format(msg, item.getId(), item.getItemNumber(), item.getDescription());
                bitMatrix = new Code128Writer().encode(msg, BarcodeFormat.CODE_128, 150, 80, null);
                MatrixToImageWriter.writeToStream(bitMatrix, "png", out);
                out.flush();
                ;
            } catch (WriterException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping(value = "/barcode/manpowerItems/{id}", method = RequestMethod.GET)
    public void generateManpowerItemBarcode(@PathVariable("id") Integer id,
                                            HttpServletResponse response) {
        ISManpowerItem item = itemService.getManpower(id);
        if (item != null) {
            BitMatrix bitMatrix;
            try {
                response.setContentType("image/png");
                OutputStream out = response.getOutputStream();
                String msg = "{0};{1};{2};{3}";
                msg = MessageFormat.format(msg, item.getId(), item.getItemNumber(), item.getDescription());
                bitMatrix = new Code128Writer().encode(msg, BarcodeFormat.CODE_128, 150, 80, null);
                MatrixToImageWriter.writeToStream(bitMatrix, "png", out);
                out.flush();
                ;
            } catch (WriterException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The method used to generateItemQrcode
     **/
    @RequestMapping(value = "/qrcode/materialItems/{id}", method = RequestMethod.GET)
    public void generateMaterialItemQRCode(@PathVariable("id") Integer id,
                                           HttpServletResponse response) {
        ISMaterialItem item = itemService.get(id);
        if (item != null) {
            BitMatrix bitMatrix;
            Writer writer = new QRCodeWriter();
            try {
                response.setContentType("image/png");
                OutputStream out = response.getOutputStream();
                String msg = "{0};{1};{2};{3}";
                msg = MessageFormat.format(msg, item.getId(), item.getItemNumber(), item.getDescription());
                bitMatrix = writer.encode(msg, BarcodeFormat.QR_CODE, 200, 200);
                MatrixToImageWriter.writeToStream(bitMatrix, "png", out);
                out.flush();
            } catch (WriterException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping(value = "/qrcode/machineItems/{id}", method = RequestMethod.GET)
    public void generateMachineItemQRCode(@PathVariable("id") Integer id,
                                          HttpServletResponse response) {
        ISMachineItem item = itemService.getMachine(id);
        if (item != null) {
            BitMatrix bitMatrix;
            Writer writer = new QRCodeWriter();
            try {
                response.setContentType("image/png");
                OutputStream out = response.getOutputStream();
                String msg = "{0};{1};{2};{3}";
                msg = MessageFormat.format(msg, item.getId(), item.getItemNumber(), item.getDescription());
                bitMatrix = writer.encode(msg, BarcodeFormat.QR_CODE, 200, 200);
                MatrixToImageWriter.writeToStream(bitMatrix, "png", out);
                out.flush();
            } catch (WriterException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping(value = "/qrcode/manpowerItems/{id}", method = RequestMethod.GET)
    public void generateManpowerItemQRCode(@PathVariable("id") Integer id,
                                           HttpServletResponse response) {
        ISManpowerItem item = itemService.getManpower(id);
        if (item != null) {
            BitMatrix bitMatrix;
            Writer writer = new QRCodeWriter();
            try {
                response.setContentType("image/png");
                OutputStream out = response.getOutputStream();
                String msg = "{0};{1};{2};{3}";
                msg = MessageFormat.format(msg, item.getId(), item.getItemNumber(), item.getDescription());
                bitMatrix = writer.encode(msg, BarcodeFormat.QR_CODE, 200, 200);
                MatrixToImageWriter.writeToStream(bitMatrix, "png", out);
                out.flush();
            } catch (WriterException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
