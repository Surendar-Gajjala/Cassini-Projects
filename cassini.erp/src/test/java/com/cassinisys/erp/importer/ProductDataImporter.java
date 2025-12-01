package com.cassinisys.erp.importer;

import com.cassinisys.erp.BaseTest;
import com.cassinisys.erp.model.hrm.ERPBusinessUnit;
import com.cassinisys.erp.model.production.ERPProduct;
import com.cassinisys.erp.model.production.ERPProductCategory;
import com.cassinisys.erp.model.production.ERPProductType;
import com.cassinisys.erp.model.security.ERPLogin;
import com.cassinisys.erp.repo.hrm.BusinessUnitRepository;
import com.cassinisys.erp.repo.production.ProductCategoryRepository;
import com.cassinisys.erp.repo.production.ProductRepository;
import com.cassinisys.erp.repo.production.ProductTypeRepository;
import com.cassinisys.erp.repo.security.LoginRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by reddy on 8/4/15.
 */
@Component
public class ProductDataImporter extends BaseTest {
    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BusinessUnitRepository businessUnitRepository;

    private ERPLogin login = null;


    @Test
    @Rollback(false)
    public void importProducts() throws Exception {
        System.out.println();

        login = loginRepository.findByLoginName("admin");
        importBooksData();

        System.out.println();
    }


    private void importBooksData() throws Exception {
        String dataPath = "/Users/reddy/Downloads/mantena/books.csv";
        String imagesPath = "/Users/reddy/Downloads/mantena/images";

        /*
        String dataPath = "/Users/reddy/MyHome/CassiniSys/Customers/DPPL/Data/books/books.csv";
        String imagesPath = "/Users/reddy/MyHome/CassiniSys/Customers/DPPL/Data/books/images";
        */

        File dataFile = new File(dataPath);
        File imagesDir = new File(imagesPath);

        Map<String, ERPProductCategory> catMap = new HashMap<>();
        ERPProductType type = productTypeRepository.findByName("BooK");
        if(type == null) {
            type = new ERPProductType();
            type.setName("Book");
            type.setDescription("Book");
            type = productTypeRepository.save(type);
        }

        //productCategoryRepository.deleteAll();
        //productRepository.deleteAll();

        CSVParser parser = new CSVParser(new FileReader(dataFile), CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        long totalCount = records.size();

        for (CSVRecord csvRecord : records) {
            if(csvRecord.getRecordNumber() > 1) {

                String msg = MessageFormat.format("Processing {0} of {1}", csvRecord.getRecordNumber() - 1, totalCount - 1);
                System.out.println(msg);

                String buName = csvRecord.get(1);

                ERPBusinessUnit buObject = createBusinessUnit(buName);

                ERPProductCategory buCat = catMap.get(buName);
                if(buCat == null) {
                    buCat = productCategoryRepository.findByName(buName);
                    if(buCat == null) {
                        buCat = new ERPProductCategory();
                        buCat.setName(buName);
                        buCat.setDescription(buName);
                        buCat.setCode("");
                        buCat.setType(type.getId());
                        buCat = productCategoryRepository.save(buCat);
                        buCat.setBusinessUnit(buObject);
                    }
                }

                String catName = csvRecord.get(2);

                ERPProductCategory cat = catMap.get(catName);
                if(cat == null) {
                    cat = productCategoryRepository.findByName(catName);
                    if(cat == null) {
                        cat = new ERPProductCategory();
                        cat.setParent(buCat.getId());
                        cat.setName(catName);
                        cat.setDescription(catName);
                        cat.setCode("");
                        cat.setType(type.getId());
                        cat = productCategoryRepository.save(cat);
                        cat.setBusinessUnit(buObject);
                    }
                    catMap.put(catName, cat);
                }

                String bookName = csvRecord.get(3);
                String s = csvRecord.get(4);
                Double price = 0.0;

                String id = csvRecord.get(0);

                try {
                    price = Double.parseDouble(s.trim());
                } catch (NumberFormatException e) {
                }

                String sku = csvRecord.get(5);


                ERPProduct prod = new ERPProduct();
                prod.setCategory(cat);
                prod.setSku(sku);
                prod.setName(bookName);
                prod.setDescription(bookName);
                prod.setUnitPrice(price);
                prod.setType(type.getId());
                prod.setCreatedBy(login.getPerson().getId());
                prod.setModifiedBy(login.getPerson().getId());
                prod.setBusinessUnit(buObject);


                File imgFile = new File(imagesDir, id+".jpg");
                if(imgFile.exists()) {
                    byte[] picData = IOUtils.toByteArray(new FileInputStream(imgFile));
                    prod.setPicture(picData);
                }

                productRepository.save(prod);
            }
        }
    }

    @Test
    @Rollback(false)
    public void importProductData() throws Exception {
        String dataPath = "E:/sspackages.csv";

        login = loginRepository.findByLoginName("admin");
        File dataFile = new File(dataPath);

        Map<String, ERPProductCategory> catMap = new HashMap<>();

        CSVParser parser = new CSVParser(new FileReader(dataFile), CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        long totalCount = records.size();

        for (CSVRecord csvRecord : records) {
            if(csvRecord.getRecordNumber() > 1) {

                String msg = MessageFormat.format("Processing {0} of {1}", csvRecord.getRecordNumber() - 1, totalCount - 1);
                System.out.println(msg);

                String pType = csvRecord.get(0);
                ERPProductType type = productTypeRepository.findByName(pType);
                if(type == null) {
                    type = new ERPProductType();
                    type.setName(pType);
                    type.setDescription(pType);
                    type = productTypeRepository.save(type);
                }

                String buName = csvRecord.get(5);
                ERPBusinessUnit buObject = createBusinessUnit(buName);

                ERPProductCategory buCat = catMap.get(buName);
                if(buCat == null) {
                    buCat = productCategoryRepository.findByName(buName);
                    if(buCat == null) {
                        buCat = new ERPProductCategory();
                        buCat.setName(buName);
                        buCat.setDescription(buName);
                        buCat.setCode("");
                        buCat.setType(type.getId());
                        buCat = productCategoryRepository.save(buCat);
                        buCat.setBusinessUnit(buObject);
                    }
                }

                String catName = csvRecord.get(1);

                ERPProductCategory cat = catMap.get(catName);
                if(cat == null) {
                    cat = productCategoryRepository.findByName(catName);
                    if(cat == null) {
                        cat = new ERPProductCategory();
                        cat.setParent(buCat.getId());
                        cat.setName(catName);
                        cat.setDescription(catName);
                        cat.setCode("");
                        cat.setType(type.getId());
                        cat = productCategoryRepository.save(cat);
                        cat.setBusinessUnit(buObject);
                    }
                    catMap.put(catName, cat);
                }

                String name = csvRecord.get(3);
                Double price = 0.0;
                /* String s = csvRecord.get(4);



                try {
                    price = Double.parseDouble(s.trim());
                } catch (NumberFormatException e) {
                }*/

                String sku = csvRecord.get(2);


                ERPProduct prod = new ERPProduct();
                prod.setCategory(cat);
                prod.setSku(sku);
                prod.setName(name);
                prod.setDescription(name);
                prod.setUnitPrice(price);
                prod.setType(type.getId());
                prod.setCreatedBy(login.getPerson().getId());
                prod.setModifiedBy(login.getPerson().getId());
                prod.setBusinessUnit(buObject);


               /* File imgFile = new File(imagesDir, id+".jpg");
                if(imgFile.exists()) {
                    byte[] picData = IOUtils.toByteArray(new FileInputStream(imgFile));
                    prod.setPicture(picData);
                }*/

                productRepository.save(prod);
            }
        }
    }

    private ERPBusinessUnit createBusinessUnit(String name) {
        ERPBusinessUnit buObject = businessUnitRepository.findByName(name);
        if(buObject == null) {
            buObject = new ERPBusinessUnit();

            buObject.setName(name);
            buObject.setDescription(name);
            buObject.setCreatedBy(login.getPerson().getId());
            buObject.setModifiedBy(login.getPerson().getId());

            buObject = businessUnitRepository.save(buObject);
        }

        return buObject;
    }
}
