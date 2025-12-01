package com.cassinisys.plm.service.exim.importer;

import com.cassinisys.platform.model.core.Lov;
import com.cassinisys.platform.repo.core.LovRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Component
@Transactional
public class LovsImporter extends AbstractImporter {
    @Autowired
    private LovRepository lovRepository;

    @Override
    protected void importData(byte[] bytes) {
        try {
            List<Lov> lovs = getObjectMapper().readValue(bytes, new TypeReference<List<Lov>>() {
            });
            for (Lov lov : lovs) {
                Lov found = lovRepository.findByName(lov.getName());
                if (found == null) {
                    lov.setId(null);
                    lovRepository.save(lov);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
