package com.cassinisys.is;

import com.cassinisys.is.config.ISConfig;
import com.cassinisys.is.model.procm.ISMaterialItem;
import com.cassinisys.is.model.procm.ISMaterialType;
import com.cassinisys.is.repo.procm.MaterialItemRepository;
import com.cassinisys.is.service.procm.MaterialTypeService;
import com.cassinisys.platform.model.core.Lov;
import com.cassinisys.platform.service.core.LovService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nageshreddy on 15-02-2018.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ISConfig.class})
@WebAppConfiguration
@Transactional
public class ItemImporter extends BaseTest {

    Map<Integer, ISMaterialType> isMaterialTypeMap = new HashMap();
    @Autowired
    private LovService lovService;
    @Autowired
    private MaterialTypeService materialTypeService;

    @Autowired
    private MaterialItemRepository materialItemRepository;

    @Test
    @Rollback(false)
    public void importUnits() throws Exception {

        String FILE_NAME = "Item_Unit.csv";
        String UNITS_FILE = FILE_PATH+FILE_NAME;
        CSVParser parser = new CSVParser(new FileReader(UNITS_FILE), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        ArrayList<String> stringArrayList = new ArrayList();
        for (CSVRecord csvRecord : records) {
            if (csvRecord.getRecordNumber() == 1) continue;
            stringArrayList.add(csvRecord.get(1));
        }
        Lov lov = new Lov();
        lov.setName("Units");
        lov.setType("units");
        lov.setDescription("units");
        lov.setDefaultValue("Nos");
        String[] stockArr = new String[stringArrayList.size()];
        stockArr = stringArrayList.toArray(stockArr);
        lov.setValues(stockArr);

        lovService.create(lov);
    }

    @Test
    @Rollback(false)
    public void importItemsAndItemTypes() throws Exception {
        String FILE_NAME = "ItemGroup_Master.csv";
        String ITEM_GROUP_FILE = FILE_PATH+FILE_NAME;
        CSVParser parser = new CSVParser(new FileReader(ITEM_GROUP_FILE), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        for (CSVRecord csvRecord : records) {
            if (csvRecord.getRecordNumber() == 1) continue;

            ISMaterialType materialType = new ISMaterialType();
            materialType.setId(Integer.parseInt(csvRecord.get(1)));
            materialType.setName(csvRecord.get(2));
            materialType.setCreatedDate(getDateFormat(csvRecord.get(4), "dd-MM-yyyy"));
            materialType.setCreatedBy(getPersonId(csvRecord.get(5)));
            materialType.setModifiedDate(getDateFormat(csvRecord.get(7), "dd-MM-yyyy"));
            materialType.setModifiedBy(getPersonId(csvRecord.get(8)));

            ISMaterialType materialType1 = materialTypeService.create(materialType);
            isMaterialTypeMap.put(Integer.parseInt(csvRecord.get(1)), materialType1);
        }
        importItems(isMaterialTypeMap);
    }


    private void importItems(Map<Integer, ISMaterialType> isMaterialTypeMap) throws Exception {
        String ITEMS_FILE = FILE_PATH+"Item_Master.csv";
        CSVParser parser = new CSVParser(new FileReader(ITEMS_FILE), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        for (CSVRecord csvRecord : records) {
            if (csvRecord.getRecordNumber() == 1) continue;

            ISMaterialItem materialItem = new ISMaterialItem();

            materialItem.setItemName(csvRecord.get(2));
            materialItem.setDescription(csvRecord.get(3));
            materialItem.setItemNumber(csvRecord.get(2));

            ISMaterialType materialType2 = isMaterialTypeMap.get(Integer.parseInt(csvRecord.get(1)));
            materialItem.setItemType(materialType2);
            materialItem.setUnits(csvRecord.get(4));
//            materialItem.setCreatedDate(getDateFormat(csvRecord.get(5), "MM/dd/yyyy hh:mm:ss a"));
            materialItem.setCreatedBy(getPersonId(csvRecord.get(6)));
//            materialItem.setModifiedDate(getDateFormat(csvRecord.get(8), "MM/dd/yyyy hh:mm:ss a"));
            materialItem.setModifiedBy(getPersonId(csvRecord.get(9)));

            materialItemRepository.save(materialItem);
        }
    }

}
