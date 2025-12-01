package com.cassinisys.plm.github;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.custom.CustomObject;
import com.cassinisys.platform.repo.core.ObjectRepository;
import com.cassinisys.platform.util.AES;
import com.cassinisys.plm.BaseTest;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.repo.wf.PLMWorkflowRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.kohsuke.github.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


public class TestGitHub extends BaseTest {
    @Autowired
    private ObjectRepository objectRepository;
    @Autowired
    private PLMWorkflowRepository plmWorkflowRepository;

    @Test
    public void testGitHub() throws Exception {
        GitHub github = new GitHubBuilder().withOAuthToken("f2a0d77a655d56fd6d5b36169c690c50eacdfa2d").build();

        GHOrganization organization = github.getOrganization("cassiniplm");
        System.out.println(organization);
        PagedIterable<GHRepository> iterable = organization.listRepositories(1000);
        iterable.forEach(System.out::println);

        GHRepository repo = github.getRepository("cassiniplm/cassini.portal");
        PagedIterable<GHRelease> releases = repo.listReleases();

        System.out.println("Releases: " + releases.toList().size());
        System.out.println("Forks: " + repo.getForksCount());
        System.out.println("Open Issues: " + repo.getOpenIssueCount());
        System.out.println("Subscribers: " + repo.getSubscribersCount());

        releases.forEach(release -> {
            System.out.println(release.getName() + ": " + release.getPublished_at());
            System.out.println(release.getTarballUrl());
        });
    }


    @Test
    public void testKeys() throws Exception {
        testCipher();
    }

    private void testCipher() throws Exception {
        String salt = RandomStringUtils.randomAlphanumeric(32);
        String origText = "{'license':'PLM-ENT-0001', 'expiration': ''}";

        String encryptedString = AES.encrypt(origText, salt) ;
        String decryptedString = AES.decrypt(encryptedString, salt) ;

        System.out.println(salt);
        System.out.println(origText);
        System.out.println(encryptedString);
        System.out.println(decryptedString);
    }

    @Test
    public void testCustomObject() throws Exception {
        List<Integer> ids = new ArrayList<>();
        List<PLMWorkflow> workflows = plmWorkflowRepository.findAll();
        for (PLMWorkflow workflow : workflows) {
            CassiniObject cassiniObject = objectRepository.findById(workflow.getAttachedTo());
            System.out.println(cassiniObject);
        }
    }
}
