package com.cassinisys.plm.migration;

import com.cassinisys.plm.BaseTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * Created by reddy on 6/23/17.
 */
public class Exporter extends BaseTest {
    String outputDir = "/Users/reddy/temp/plm";
    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private ApplicationContext context;
    @Value("classpath:platform.map")
    private Resource platformMapResource;
    @Value("classpath:plm.map")
    private Resource plmMapResource;

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void export() throws Exception {
        //Set pretty printing of json
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        processMapFile(new InputStreamReader(platformMapResource.getInputStream()));
        processMapFile(new InputStreamReader(plmMapResource.getInputStream()));
    }

    private void processMapFile(InputStreamReader reader) throws Exception {
        BufferedReader b = new BufferedReader(reader);
        String readLine = "";

        while ((readLine = b.readLine()) != null) {
            readLine = readLine.trim();
            if (!readLine.isEmpty()) {
                String[] arr = readLine.split(":");
                String model = arr[0];
                String repo = arr[1];
                JpaRepository jpaRepo = (JpaRepository) context.getBean(repo);

                try {
                    String json = objectMapper.writeValueAsString(jpaRepo.findAll());
                    FileUtils.writeStringToFile(new File(outputDir + "/" + model + ".json"), json);
                } catch (Exception e) {
                    System.out.println(e.getCause().getMessage());
                }
            }
        }
        b.close();
    }
}
