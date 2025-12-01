package com.cassinisys.plm.controller.pdm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.plm.model.pdm.PDMGitHubAccount;
import com.cassinisys.plm.model.pdm.PDMGitHubItemRepository;
import com.cassinisys.plm.model.pdm.PDMGitHubItemRevisionRelease;
import com.cassinisys.plm.model.pdm.PDMGitHubRepository;
import com.cassinisys.plm.service.pdm.PDMGitHubService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pdm/github")
@Api(tags = "PLM.PDM",description = "PDM Related")
public class GitHubController extends BaseController {
    @Autowired
    private PDMGitHubService pdmGitHubService;

    @RequestMapping(value = "/accounts", method = RequestMethod.POST)
    public PDMGitHubAccount createGitHubAccount(@RequestBody PDMGitHubAccount account) {
        return pdmGitHubService.createGitHubAccount(account);
    }

    @RequestMapping(value = "/accounts")
    public List<PDMGitHubAccount> getAllGitHubAccounts() {
        return pdmGitHubService.getAllGitHubAccounts();
    }

    @RequestMapping(value = "/accounts/{id}")
    public PDMGitHubAccount getGitHubAccount(@PathVariable Integer id) {
        return pdmGitHubService.getGitHubAccount(id);
    }

    @RequestMapping(value = "/accounts/{id}", method = RequestMethod.PUT)
    public PDMGitHubAccount updateGitHubAccount(@PathVariable Integer id,
                                                @RequestBody PDMGitHubAccount account) {
        return pdmGitHubService.updateGitHubAccount(account);
    }

    @RequestMapping(value = "/accounts/{id}", method = RequestMethod.DELETE)
    public void deleteGitHubAccount(@PathVariable Integer id) {
        pdmGitHubService.deleteGitHubAccount(id);
    }

    @RequestMapping(value = "/accounts/{id}/repositories/fromgithub")
    public List<PDMGitHubRepository> getRepositoriesFromGitHub(@PathVariable Integer id) {
        return pdmGitHubService.getRepositoriesFromGitHub(id);
    }


    @RequestMapping(value = "/accounts/{id}/repositories")
    public List<PDMGitHubRepository> getAllGitHubRepositories(@PathVariable Integer id) {
        return pdmGitHubService.getGetAllRepositories();
    }

    @RequestMapping(value = "/accounts/{id}/repositories", method = RequestMethod.POST)
    public PDMGitHubRepository addGitHubRepository(@PathVariable Integer id,
                                                   @RequestBody PDMGitHubRepository repo) {
        return pdmGitHubService.createGitHubRepository(repo);
    }

    @RequestMapping(value = "/accounts/{accountId}/repositories/{repoId}")
    public PDMGitHubRepository getGitHubRepository(@PathVariable Integer accountId,
                                                   @PathVariable Integer repoId) {
        return pdmGitHubService.getGitHubRepository(repoId);
    }

    @RequestMapping(value = "/accounts/{accountId}/repositories/{repoId}", method = RequestMethod.DELETE)
    public void deleteGitHubRepository(@PathVariable Integer accountId,
                                        @PathVariable Integer repoId) {
        pdmGitHubService.deleteGitHubRepository(repoId);
    }

    @RequestMapping(value = "/accounts/{accountId}/repositories/{repoId}/item/{itemId}", method = RequestMethod.POST)
    public PDMGitHubItemRepository setGitHubItemRepository(@PathVariable Integer accountId,
                                  @PathVariable Integer repoId,
                                  @PathVariable Integer itemId) {
        return pdmGitHubService.setGitHubItemRepository(itemId, repoId);
    }

    @RequestMapping(value = "/accounts/{accountId}/repositories/{repoId}/item/{itemId}")
    public PDMGitHubItemRepository getGitHubItemRepository(@PathVariable Integer accountId,
                                                           @PathVariable Integer repoId,
                                                           @PathVariable Integer itemId) {
        return pdmGitHubService.getGitHubItemRepository(itemId);
    }

    @RequestMapping(value = "/accounts/{accountId}/repositories/{repoId}/releases")
    public List<PDMGitHubItemRevisionRelease> getGitHubRepositoryReleases(@PathVariable Integer accountId,
                                                                          @PathVariable Integer repoId) {
        return pdmGitHubService.getRepositoryReleasesFromGitHub(repoId);
    }

    @RequestMapping(value = "/accounts/{accountId}/repositories/{repoId}/item/{itemRevisionId}/release")
    public PDMGitHubItemRevisionRelease getGitHubItemRevisionRelease(@PathVariable Integer accountId,
                                                                     @PathVariable Integer repoId,
                                                                     @PathVariable Integer itemRevisionId) {
        return pdmGitHubService.getGitHubItemRevisionRelease(itemRevisionId);
    }

    @RequestMapping(value = "/accounts/{accountId}/repositories/{repoId}/item/{itemRevisionId}/release", method =  RequestMethod.POST)
    public PDMGitHubItemRevisionRelease setGitHubItemRevisionRelease(@PathVariable Integer accountId,
                                                                     @PathVariable Integer repoId,
                                                                     @PathVariable Integer itemRevisionId,
                                                                     @RequestBody PDMGitHubItemRevisionRelease release) {
        return pdmGitHubService.updateGitHubItemRevisionRelease(release);
    }

    @RequestMapping(value = "/accounts/{accountId}/repositories/{repoId}/item/{itemRevisionId}/release", method =  RequestMethod.DELETE)
    public void deleteGitHubItemRevisionRelease(@PathVariable Integer accountId,
                                                                     @PathVariable Integer repoId,
                                                                     @PathVariable Integer itemRevisionId) {
        pdmGitHubService.deleteGitHubItemRevisionRelease(itemRevisionId);
    }
}
