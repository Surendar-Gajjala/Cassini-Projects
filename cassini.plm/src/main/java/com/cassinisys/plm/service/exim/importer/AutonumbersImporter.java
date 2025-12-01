package com.cassinisys.plm.service.exim.importer;

import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.repo.core.AutoNumberRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Component
@Transactional
public class AutonumbersImporter extends AbstractImporter {
    @Autowired
    private AutoNumberRepository autoNumberRepository;

    @Override
    protected void importData(byte[] bytes) {
        try {
            List<AutoNumber> autoNumbers = getObjectMapper().readValue(bytes, new TypeReference<List<AutoNumber>>() {
            });
            for (AutoNumber autoNumber : autoNumbers) {
                AutoNumber found = autoNumberRepository.findByName(autoNumber.getName());
                if (found == null) {
                    autoNumber.setId(null);
                    autoNumberRepository.save(autoNumber);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
