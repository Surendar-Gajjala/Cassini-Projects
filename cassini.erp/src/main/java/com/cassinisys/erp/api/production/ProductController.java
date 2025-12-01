package com.cassinisys.erp.api.production;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.config.AutowiredLogger;
import com.cassinisys.erp.converters.PageRequestConverter;
import com.cassinisys.erp.model.api.criteria.ProductCriteria;
import com.cassinisys.erp.model.api.paging.ERPPageRequest;
import com.cassinisys.erp.model.production.ERPProduct;
import com.cassinisys.erp.service.production.ProductService;
import org.apache.commons.io.IOUtils;
import org.jsondoc.core.annotation.Api;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by reddy on 7/18/15.
 */
@RestController
@RequestMapping("production/products")
@Api(name="Products",description="Products endpoint",group="PRODUCTION")
public class ProductController extends BaseController {

    @AutowiredLogger
    private Logger LOGGER;

    @Autowired
    private ProductService productService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @Autowired
    private ServletContext servletContext;


    @RequestMapping (value="/category/{id}", method = RequestMethod.GET)
    public 
    Page<ERPProduct> getProductsByCategory(@PathVariable("id") Integer id, ERPPageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return productService.getByCategory(id, pageable);
    }

    @RequestMapping (value="/{id}", method = RequestMethod.GET)
    public  ERPProduct getProduct(@PathVariable("id") Integer id) {
        return productService.get(id);
    }

    @RequestMapping (value="/sku/{sku}", method = RequestMethod.GET)
    public  ERPProduct getProductBySku(@PathVariable("sku") String sku) {
        return productService.getBySku(sku);
    }

    @RequestMapping (method = RequestMethod.POST)
    public  ERPProduct createProduct(@RequestBody @Valid ERPProduct product) {
        return productService.create(product);
    }

    @RequestMapping (value="/{id}", method = RequestMethod.PUT)
    public  ERPProduct updateProduct(@PathVariable("id") Integer id,
                                                  @RequestBody @Valid ERPProduct product) {
        ERPProduct existing = productService.get(id);
        if(existing != null && existing.getPicture() != null) {
            product.setPicture(existing.getPicture());
        }
        return productService.update(product);
    }

    @RequestMapping (value="/{id}/picture", method = RequestMethod.GET)
    public void getProductPicture(@PathVariable("id") Integer id, HttpServletResponse response) {
        response.reset();
        ERPProduct prod = productService.get(id);
        if(prod != null) {
            InputStream is = null;

            if(prod.getPicture() != null) {
                response.setContentType(MediaType.IMAGE_JPEG_VALUE);
                response.setContentLength(prod.getPicture().length);
                is = new BufferedInputStream(new ByteArrayInputStream(prod.getPicture()));
            }
            else {
                response.setContentType(MediaType.IMAGE_PNG_VALUE);
                is = servletContext.getResourceAsStream("/app/assets/images/no-image.jpg");
            }

            try {
                IOUtils.copy(is, response.getOutputStream());
                response.flushBuffer();
            } catch (final IOException e) {
                LOGGER.error("Error returning product picture", e);
            }
        }
    }

    @RequestMapping (value = "/search", method = RequestMethod.GET)
    public 
    Page<ERPProduct> textSearch(ProductCriteria criteria, ERPPageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return productService.findAll(criteria, pageable);
    }

    @RequestMapping (method = RequestMethod.GET)
    public 
    Page<ERPProduct> getProducts(ProductCriteria criteria, ERPPageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return productService.findAll(criteria, pageable);
    }
}
