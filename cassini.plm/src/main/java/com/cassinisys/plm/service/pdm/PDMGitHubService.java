package com.cassinisys.plm.service.pdm;

import com.cassinisys.plm.model.pdm.*;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.repo.pdm.*;
import com.cassinisys.plm.repo.plm.ItemRepository;
import org.kohsuke.github.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PDMGitHubService {
    @Autowired
    private PDMGitHubAccountRepository pdmGitHubAccountRepository;

    @Autowired
    private PDMGitHubRepositoryRepository pdmGitHubRepositoryRepository;

    @Autowired
    private PDMGitHubItemRepositoryRepository pdmGitHubItemRepositoryRepository;

    @Autowired
    private PDMGitHubItemRevisionReleaseRepository pdmGitHubItemRevisionReleaseRepository;

    @Autowired
    private PDMGitHubItemRevisionReleaseAssetRepository pdmGitHubItemRevisionReleaseAssetRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MessageSource messageSource;

    public PDMGitHubAccount createGitHubAccount(PDMGitHubAccount account) {
        PDMGitHubAccount exists = pdmGitHubAccountRepository.findByName(account.getName());
        if (exists != null) {
            throw new PDMException(messageSource.getMessage("github_account_already_exists",
                    new Object[]{account.getName()}, LocaleContextHolder.getLocale()));
        }
        GitHub gitHub = getGitHubClient(account);
        return pdmGitHubAccountRepository.save(account);
    }

    public List<PDMGitHubAccount> getAllGitHubAccounts() {
        return pdmGitHubAccountRepository.findAll();
    }

    public PDMGitHubAccount getGitHubAccount(Integer accountId) {
        return pdmGitHubAccountRepository.findOne(accountId);
    }

    public PDMGitHubAccount updateGitHubAccount(PDMGitHubAccount account) {
        GitHub gitHub = getGitHubClient(account);
        return pdmGitHubAccountRepository.save(account);
    }

    public void deleteGitHubAccount(Integer id) {
        pdmGitHubAccountRepository.delete(id);
    }

    public PDMGitHubRepository createGitHubRepository(PDMGitHubRepository repository) {
        PDMGitHubAccount account = repository.getAccount();
        PDMGitHubRepository exists = pdmGitHubRepositoryRepository.findByAccountAndName(account, repository.getName());
        if (exists != null) {
            throw new PDMException(messageSource.getMessage("github_repository_already_exists",
                    new Object[]{repository.getName()}, LocaleContextHolder.getLocale()));
        }

        return pdmGitHubRepositoryRepository.save(repository);
    }

    public List<PDMGitHubRepository> getRepositoriesFromGitHub(Integer accountId) {
        List<PDMGitHubRepository> repositories = new ArrayList<>();
        try {
            PDMGitHubAccount account = pdmGitHubAccountRepository.findOne(accountId);
            if (account != null) {
                GitHub gitHub = getGitHubClient(account);
                if (gitHub != null) {
                    GHOrganization organization = gitHub.getOrganization(account.getOrganization());
                    PagedIterable<GHRepository> repos = organization.listRepositories(100);
                    repos.forEach(repo -> {
                        PDMGitHubRepository gitHubRepository = new PDMGitHubRepository();
                        gitHubRepository.setAccount(account);
                        gitHubRepository.setName(repo.getName());
                        gitHubRepository.setDescription(repo.getDescription());

                        PDMGitHubRepository exists = pdmGitHubRepositoryRepository.findByAccountAndName(account, repo.getName());
                        if (exists != null) {
                            gitHubRepository.setId(exists.getId());
                        }
                        gitHubRepository.setSelected(exists != null);
                        try {
                            gitHubRepository.setReleases(repo.listReleases().toList().size());
                            gitHubRepository.setForks(repo.getForksCount());
                            gitHubRepository.setOpenIssues(repo.getOpenIssueCount());
                            gitHubRepository.setUpdatedAt(repo.getUpdatedAt());
                        } catch (IOException e) {
                            throw new PDMException(e.getMessage());
                        }
                        repositories.add(gitHubRepository);
                    });
                }
            }
        } catch (IOException e) {
            throw new PDMException(e.getMessage());
        }
        return repositories;
    }

    public List<PDMGitHubRepository> getGetAllRepositories() {
        return pdmGitHubRepositoryRepository.findAll();
    }

    public List<PDMGitHubRepository> getGetRepositoriesByAccount(Integer accountId) {
        PDMGitHubAccount account = getGitHubAccount(accountId);
        return pdmGitHubRepositoryRepository.findAllByAccount(account);
    }

    public PDMGitHubRepository getGitHubRepository(Integer id) {
        return pdmGitHubRepositoryRepository.findOne(id);
    }

    public PDMGitHubRepository updateGitHubRepository(PDMGitHubRepository repository) {
        return pdmGitHubRepositoryRepository.save(repository);
    }

    public void deleteGitHubRepository(Integer id) {
        pdmGitHubRepositoryRepository.delete(id);
    }

    public PDMGitHubItemRepository createGitHubItemRepository(PDMGitHubItemRepository repository) {
        return pdmGitHubItemRepositoryRepository.save(repository);
    }

    public PDMGitHubItemRepository getGitHubItemRepository(Integer itemId) {
        return pdmGitHubItemRepositoryRepository.findOne(itemId);
    }

    public PDMGitHubItemRepository updateGitHubItemRepository(PDMGitHubItemRepository repository) {
        return pdmGitHubItemRepositoryRepository.save(repository);
    }

    public void deleteGitHubItemRepository(Integer itemId) {
        pdmGitHubItemRepositoryRepository.delete(itemId);
    }

    public List<PDMGitHubItemRevisionRelease> getRepositoryReleasesFromGitHub(Integer repoId) {
        PDMGitHubRepository repo = pdmGitHubRepositoryRepository.findOne(repoId);
        List<PDMGitHubItemRevisionRelease> releases = new ArrayList<>();
        GitHub github = getGitHubClient(repo.getAccount());
        if (github != null) {
            PDMGitHubAccount account = repo.getAccount();
            try {
                GHRepository grepo = github.getRepository(account.getOrganization() + "/" + repo.getName());
                PagedIterable<GHRelease> ghreleases = grepo.listReleases();
                ghreleases.forEach(ghrelease -> {
                    PDMGitHubItemRevisionRelease release = new PDMGitHubItemRevisionRelease();
                    release.setReleaseId(ghrelease.getId());
                    release.setReleaseName(ghrelease.getName());
                    release.setReleaseDate(ghrelease.getPublished_at());
                    release.setComments(ghrelease.getBody());
                    releases.add(release);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return releases;
    }

    public PDMGitHubItemRevisionRelease createGitHubItemRevisionRelease(PDMGitHubItemRevisionRelease revisionRelease) {
        return pdmGitHubItemRevisionReleaseRepository.save(revisionRelease);
    }

    public PDMGitHubItemRevisionRelease getGitHubItemRevisionRelease(Integer itemRevisionId) {
        return pdmGitHubItemRevisionReleaseRepository.findOne(itemRevisionId);
    }

    public PDMGitHubItemRevisionRelease updateGitHubItemRevisionRelease(PDMGitHubItemRevisionRelease revisionRelease) {
        return pdmGitHubItemRevisionReleaseRepository.save(revisionRelease);
    }

    public void deleteGitHubItemRevisionRelease(Integer itemRevisionId) {
        pdmGitHubItemRevisionReleaseRepository.delete(itemRevisionId);
    }

    public PDMGitHubItemRepository setGitHubItemRepository(Integer itemId, Integer repoId) {
        PLMItem item = itemRepository.findOne(itemId);
        if (item != null) {
            PDMGitHubRepository repo = pdmGitHubRepositoryRepository.findOne(repoId);
            PDMGitHubItemRepository itemRepo = pdmGitHubItemRepositoryRepository.findByItemAndRepository(itemId, repo);
            if (itemRepo == null) {
                itemRepo = new PDMGitHubItemRepository();
                itemRepo.setItem(itemId);
                itemRepo.setRepository(pdmGitHubRepositoryRepository.findOne(repoId));
                itemRepo = pdmGitHubItemRepositoryRepository.save(itemRepo);
            }
            return itemRepo;
        }

        return null;
    }


    public List<GitHubReleaseDTO> getGitHubRepositoryReleases(Integer repoId) {
        List<GitHubReleaseDTO> releases = new ArrayList<>();
        PDMGitHubRepository githubRepo = pdmGitHubRepositoryRepository.findOne(repoId);
        try {
            GitHub github = new GitHubBuilder().withOAuthToken(githubRepo.getAccount().getOauthToken()).build();
            String path = githubRepo.getAccount().getOrganization() + "/" + githubRepo.getName();
            GHRepository repo = github.getRepository(path);
            PagedIterable<GHRelease> list = repo.listReleases();
            list.forEach(release ->
                            releases.add(new GitHubReleaseDTO(release.getName(), release.getBody(), release.getPublished_at()))
            );
        } catch (IOException e) {
            throw new PDMException(e.getMessage());
        }

        return releases;
    }


    private GitHub getGitHubClient(PDMGitHubAccount account) {
        GitHub gitHub = null;

        try {
            if (account.getAuthType() == GitHubAuthType.TOKEN) {
                gitHub = new GitHubBuilder().withOAuthToken(account.getOauthToken()).build();
            } else {
                gitHub = new GitHubBuilder().withPassword(account.getUsername(), account.getPassword()).build();
            }
        } catch (IOException e) {
            throw new PDMException(e.getMessage());
        }


        try {
            GHOrganization org = gitHub.getOrganization(account.getOrganization());
        } catch (IOException e) {
            throw new PDMException("Organization not found for this user");
        }

        return gitHub;
    }
}