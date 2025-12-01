package com.cassinisys.tm;

import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.tm.model.TMAccommodation;
import com.cassinisys.tm.model.TMAccommodationSuit;
import com.cassinisys.tm.model.TMBed;
import com.cassinisys.tm.repo.AccommodationRepository;
import com.cassinisys.tm.repo.AccommodationSuitRepository;
import com.cassinisys.tm.repo.BedRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.io.FileReader;
import java.text.MessageFormat;
import java.util.List;

/**
 * Created by reddy on 9/1/16.
 */
public class ImportAccommodation extends BaseTest {

    @Autowired
    private AccommodationRepository accommodationRepository;

    @Autowired
    private AccommodationSuitRepository suitRepository;

    @Autowired
    private BedRepository bedRepository;

    @Autowired
    private PersonRepository personRepository;


    @Test
    public void getCounts() throws Exception {
        System.out.println("Accommodations: " + accommodationRepository.count());
        System.out.println("Suites: " + suitRepository.count());
        System.out.println("Beds: " + bedRepository.count());
    }

    @Test
    @Rollback(false)
    public void importAccommodation() throws Exception {
        String fileName = "/Users/reddy/Downloads/accommodation.csv";
        CSVParser parser = new CSVParser(new FileReader(fileName),
                CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        long totalCount = records.size();

        System.out.println();
        for (CSVRecord csvRecord : records) {
            if (csvRecord.getRecordNumber() == 1) continue;

            String msg = MessageFormat.format("Processing {0} of {1}", csvRecord.getRecordNumber() - 1, totalCount - 1);
            System.out.println(msg);

            String aName = csvRecord.get(0);
            String sName = csvRecord.get(1);
            String bName = csvRecord.get(2);
            String name = "";//csvRecord.get(3);


            TMAccommodation accommodation = accommodationRepository.findByName(aName);
            if(accommodation == null) {
                accommodation = new TMAccommodation();
                accommodation.setName(aName);
                accommodation.setDescription(aName);
                accommodation = accommodationRepository.save(accommodation);
            }

            TMAccommodationSuit suite = suitRepository.findByAccommodationAndName(accommodation.getId(), sName);
            if(suite == null) {
                suite = new TMAccommodationSuit();
                suite.setAccommodation(accommodation.getId());
                suite.setName(sName);
                suite.setDescription(sName);
                suite = suitRepository.save(suite);
            }

            if(bName != null && !bName.trim().isEmpty()) {
                TMBed bed = bedRepository.findBySuiteAndName(suite.getSuitId(), bName);
                if(bed == null) {
                    bed = new TMBed();
                    bed.setSuite(suite.getSuitId());
                    bed.setName(bName);
                    bed = bedRepository.save(bed);
                }

                if(name != null && !name.trim().isEmpty()) {
                    if(name.indexOf(',') != -1) {
                        String[] arr = name.split(",");
                        String fName = arr[0].trim();
                        String lName = arr[1].trim();

                        try {
                            Person person = personRepository.findByFirstNameAndLastName(fName, lName);
                            if(person != null) {
                                bed.setAssignedTo(person.getId());
                                bedRepository.save(bed);
                            }
                        } catch (Exception e) {

                        }
                    }
                }
            }

        }
    }
}
