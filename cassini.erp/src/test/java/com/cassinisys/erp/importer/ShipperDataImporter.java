package com.cassinisys.erp.importer;

import com.cassinisys.erp.model.common.ERPAddress;
import com.cassinisys.erp.model.common.ERPCountry;
import com.cassinisys.erp.model.common.ERPState;
import com.cassinisys.erp.model.crm.ERPShipper;
import com.cassinisys.erp.repo.common.AddressTypeRepository;
import com.cassinisys.erp.repo.common.CountryRepository;
import com.cassinisys.erp.repo.common.StateRepository;
import com.cassinisys.erp.repo.crm.ShipperRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;

import java.io.FileReader;
import java.text.MessageFormat;
import java.util.List;

/**
 * Created by reddy on 10/13/15.
 */
@Component
public class ShipperDataImporter {
    private String DATA_FILE = "/Users/reddy/MyHome/CassiniSys/Customers/DPPL/Data/shippers.csv";

    @Autowired
    private ShipperRepository shipperRepository;

    @Autowired
    private AddressTypeRepository addressTypeRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private StateRepository stateRepository;

    @Test
    @Rollback(false)
    public void importShippers() throws Exception {
        CSVParser parser = new CSVParser(new FileReader(DATA_FILE), CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        long totalCount = records.size();


        System.out.println();
        for (CSVRecord csvRecord : records) {
            if (csvRecord.getRecordNumber() == 1) continue;

            String msg = MessageFormat.format("Processing {0} of {1}", csvRecord.getRecordNumber() - 1, totalCount - 1);
            System.out.println(msg);

            ERPAddress address = getAddress(csvRecord);

            ERPShipper shipper = new ERPShipper();
            shipper.setName(csvRecord.get(0));
            shipper.setOfficePhone(csvRecord.get(1));
            shipper.setOfficeFax(csvRecord.get(2));
            shipper.setOfficeEmail(csvRecord.get(3));
            shipper.setAddress(address);

            shipperRepository.save(shipper);
        }
        System.out.println();
    }

    private ERPAddress getAddress(CSVRecord csvRecord) {
        ERPAddress address = new ERPAddress();
        address.setAddressType(addressTypeRepository.findByName("Office"));

        ERPCountry country = countryRepository.findByName("India");

        String stateName = csvRecord.get(6);
        ERPState state = stateRepository.findByNameAndCountry(stateName, country);

        address.setAddressText(csvRecord.get(4));
        address.setCity(csvRecord.get(5));
        address.setState(state);
        address.setCountry(country);
        address.setPincode(csvRecord.get(8));


        return address;
    }

}
