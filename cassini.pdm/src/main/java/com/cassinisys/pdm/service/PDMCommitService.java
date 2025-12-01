package com.cassinisys.pdm.service;

import com.cassinisys.pdm.model.PDMCommit;
import com.cassinisys.pdm.model.PDMFile;
import com.cassinisys.pdm.repo.CommitRepository;
import com.cassinisys.pdm.repo.PDMFileRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.util.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;

@Service
@Transactional
public class PDMCommitService implements CrudService<PDMCommit, Integer> {

    @Autowired
    private PDMFileRepository fileRepository;

    @Autowired
    private CommitRepository commitRepository;

    @Override
    public PDMCommit create(PDMCommit pdmCommit) {
        pdmCommit.setId(null);
        pdmCommit.setSha(getSha(10));
        return commitRepository.save(pdmCommit);
    }

    private String getSha(int len)
    {
        String AB = "0123456789abcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }

    @Override
    public PDMCommit update(PDMCommit pdmCommit) {
        return commitRepository.save(pdmCommit);
    }

    @Override
    public void delete(Integer integer) {
        commitRepository.delete(integer);
    }

    @Override
    public PDMCommit get(Integer integer) {
        return commitRepository.getOne(integer);
    }

    @Override
    public List<PDMCommit> getAll() {
        return commitRepository.findAllByOrderByCreatedDateDesc();
    }

    public Page<PDMCommit> getAllPaged(Pageable pageable) {
        return commitRepository.findAllByOrderByCreatedDateDesc(pageable);
    }

    public List<PDMFile> getAllFilesByCommit(Integer commit) {
        return fileRepository.findAllByCommitOrderByCreatedDateDesc(commit);
    }
}
