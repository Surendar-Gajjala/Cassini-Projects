package com.cassinisys.is;

import com.cassinisys.is.model.pm.ISProject;
import com.cassinisys.is.service.pm.ProjectService;
import com.cassinisys.platform.model.core.*;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.cassinisys.platform.service.core.LovService;
import com.cassinisys.platform.service.core.ObjectAttributeService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nageshreddy on 19-02-2018.
 */
public class projectImporter extends BaseTest {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;

    @Autowired
    private ObjectAttributeService objectAttributeService;

    @Autowired
    private LovService lovService;

    @Test
    @Rollback(false)
    public void importPlanHeads() throws Exception {

        String FILE_NAME = "plan_heads.csv";
        String PLAN_FILE = FILE_PATH+FILE_NAME;
        CSVParser parser = new CSVParser(new FileReader(PLAN_FILE), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        ArrayList<String> stringArrayList = new ArrayList();
        for (CSVRecord csvRecord : records) {
            if (csvRecord.getRecordNumber() == 1) continue;
            stringArrayList.add(csvRecord.get(1));
        }
        Lov lov = new Lov();
        lov.setName("Project Type");
        lov.setType("Project Type");
        lov.setDescription("Project Type");
        lov.setDefaultValue("New Lines");
        String[] typeArr = new String[stringArrayList.size()];
        typeArr = stringArrayList.toArray(typeArr);
        lov.setValues(typeArr);

        lovService.create(lov);
    }


    @Test
    @Rollback(false)
    public void importProjects() throws Exception {

        String FILE_NAME = "projects.csv";
        String UNITS_FILE = FILE_PATH + FILE_NAME;
        CSVParser parser = new CSVParser(new FileReader(UNITS_FILE), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        ArrayList<ObjectAttribute> attributes = new ArrayList();
        for (CSVRecord csvRecord : records) {
            if (csvRecord.getRecordNumber() == 1) continue;
            ISProject project = new ISProject();
            project.setName(csvRecord.get(1));
            project.setDescription(csvRecord.get(1));
            project.setProjectOwner(1);
            ISProject project1 = projectService.create(project);
            List<ObjectTypeAttribute> typeAttributes =
                    objectTypeAttributeRepository.findByObjectType(ObjectType.valueOf("PROJECT"));

            for (ObjectTypeAttribute typeAttribute : typeAttributes) {
                ObjectAttribute objectAttribute = new ObjectAttribute();
                objectAttribute.setId(new ObjectAttributeId(project1.getId(), typeAttribute.getId()));
                if (typeAttribute.getName().equalsIgnoreCase("Plan head")) {
                    objectAttribute.setStringValue(csvRecord.get(0));
                } else if (typeAttribute.getName().equalsIgnoreCase("Division")) {
                    objectAttribute.setStringValue(csvRecord.get(2));
                } else if (typeAttribute.getName().equalsIgnoreCase("Estimate cost")) {
                    objectAttribute.setStringValue(csvRecord.get(3));
                }
                attributes.add(objectAttribute);
            }

            objectAttributeService.saveObjectAttributes(attributes);
        }
    }
}
