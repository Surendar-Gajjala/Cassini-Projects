package com.cassinisys.is;

import com.cassinisys.is.model.procm.ISMaterialItem;
import com.cassinisys.is.model.procm.ISMaterialType;
import com.cassinisys.is.repo.procm.MaterialItemRepository;
import com.cassinisys.is.repo.procm.MaterialTypeRepository;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.repo.core.AutoNumberRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.io.FileReader;
import java.util.Date;
import java.util.List;

public class Importer extends BaseTest {

    @Autowired
    private MaterialTypeRepository typeRepository;

    @Autowired
    private MaterialItemRepository itemRepository;

    @Autowired
    private AutoNumberRepository autoNumberRepository;

    @Test
    @Rollback(false)
    public void importNfrData() throws Exception {
        String file = "C:\\Temp\\nfr_item_master.csv";
        CSVParser parser = new CSVParser(new FileReader(file), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        for (CSVRecord record : records) {
            String itemName = record.get(0);
            String itemType = record.get(2);
            String itemUnits = record.get(3);
            AutoNumber autoNumber = autoNumberRepository.findByName("Default Material Item Number Source");

            ISMaterialType type1 = typeRepository.findByName(itemType);
            ISMaterialType type = null;
            if(type == null) {
                type = new ISMaterialType();
                type.setName(itemType);
                type.setDescription(itemType);
                type.setCreatedBy(getPersonId("Administrator"));
                type.setModifiedBy(getPersonId("Administrator"));
                type.setCreatedDate(new Date());
                type.setModifiedDate(new Date());
                type.setMaterialNumberSource(autoNumber);

                type = typeRepository.save(type);
            }
            else {
                type = type1;
            }

            ISMaterialItem item = new ISMaterialItem();
            item.setItemNumber(autoNumber.next());
            item.setItemName(itemName);
            item.setItemType(type);
            item.setDescription(itemName);
            item.setUnits(itemUnits);
            item.setCreatedBy(getPersonId("Administrator"));
            item.setModifiedBy(getPersonId("Administrator"));
            item.setCreatedDate(new Date());
            item.setModifiedDate(new Date());

            item = itemRepository.save(item);
        }
    }
}
