package com.cassinisys.plm.service.analytics.items;

import com.cassinisys.plm.model.analytics.items.*;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.repo.cm.AffectedItemRepository;
import com.cassinisys.plm.repo.plm.ItemRepository;
import com.cassinisys.plm.repo.plm.ItemRevisionRepository;
import com.cassinisys.plm.repo.plm.ItemTypeRepository;
import com.cassinisys.plm.repo.pqm.PRProblemItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by subramanyam on 17-07-2020.
 */
@Service
public class ItemAnalyticsService {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemTypeRepository itemTypeRepository;
    @Autowired
    private PRProblemItemRepository prProblemItemRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private AffectedItemRepository affectedItemRepository;

    @Transactional(readOnly = true)
    public ItemDashboardCountsDto getDashboardCounts() {
        ItemDashboardCountsDto typeDto = new ItemDashboardCountsDto();

        typeDto.getItemsByClass().add(itemRepository.getItemCountByItemClass(ItemClass.PRODUCT));
        typeDto.getItemsByClass().add(itemRepository.getItemCountByItemClass(ItemClass.ASSEMBLY));
        typeDto.getItemsByClass().add(itemRepository.getItemCountByItemClass(ItemClass.PART));
        typeDto.getItemsByClass().add(itemRepository.getItemCountByItemClass(ItemClass.DOCUMENT));
        typeDto.getItemsByClass().add(itemRepository.getItemCountByItemClass(ItemClass.OTHER));

        typeDto.getProductItemsByStatus().add(itemRepository.getPendingProductItems(ItemClass.PRODUCT));
        typeDto.getProductItemsByStatus().add(itemRepository.getReleasedProductItems(ItemClass.PRODUCT));
        typeDto.getProductItemsByStatus().add(itemRepository.getRejectedProductItems(ItemClass.PRODUCT));

        typeDto.getAssemblyItemsByStatus().add(itemRepository.getPendingProductItems(ItemClass.ASSEMBLY));
        typeDto.getAssemblyItemsByStatus().add(itemRepository.getReleasedProductItems(ItemClass.ASSEMBLY));
        typeDto.getAssemblyItemsByStatus().add(itemRepository.getRejectedProductItems(ItemClass.ASSEMBLY));

        typeDto.getPartItemsByStatus().add(itemRepository.getPendingProductItems(ItemClass.PART));
        typeDto.getPartItemsByStatus().add(itemRepository.getReleasedProductItems(ItemClass.PART));
        typeDto.getPartItemsByStatus().add(itemRepository.getRejectedProductItems(ItemClass.PART));

        typeDto.getDocumentItemsByStatus().add(itemRepository.getPendingProductItems(ItemClass.DOCUMENT));
        typeDto.getDocumentItemsByStatus().add(itemRepository.getReleasedProductItems(ItemClass.DOCUMENT));
        typeDto.getDocumentItemsByStatus().add(itemRepository.getRejectedProductItems(ItemClass.DOCUMENT));

        typeDto.getOtherItemsByStatus().add(itemRepository.getPendingProductItems(ItemClass.OTHER));
        typeDto.getOtherItemsByStatus().add(itemRepository.getReleasedProductItems(ItemClass.OTHER));
        typeDto.getOtherItemsByStatus().add(itemRepository.getRejectedProductItems(ItemClass.OTHER));

        typeDto.getItemsByConfigurations().add(itemRepository.getNormalItemsCount());
        typeDto.getItemsByConfigurations().add(itemRepository.getConfigurableItemsCount());
        typeDto.getItemsByConfigurations().add(itemRepository.getConfiguredItemsCount());

        return typeDto;
    }

    @Transactional(readOnly = true)
    public ItemDashboardCountsDto getItemsByClass() {
        ItemDashboardCountsDto typeDto = new ItemDashboardCountsDto();

        typeDto.getItemsByClass().add(itemRepository.getItemCountByItemClass(ItemClass.PRODUCT));
        typeDto.getItemsByClass().add(itemRepository.getItemCountByItemClass(ItemClass.ASSEMBLY));
        typeDto.getItemsByClass().add(itemRepository.getItemCountByItemClass(ItemClass.PART));
        typeDto.getItemsByClass().add(itemRepository.getItemCountByItemClass(ItemClass.DOCUMENT));
        typeDto.getItemsByClass().add(itemRepository.getItemCountByItemClass(ItemClass.OTHER));
        return typeDto;
    }

    @Transactional(readOnly = true)
    public ItemClassDto getItemsByItemClass() {
        ItemClassDto typeDto = new ItemClassDto();

        typeDto.setProducts(itemRepository.getItemCountByItemClass(ItemClass.PRODUCT));
        typeDto.setAssemblies(itemRepository.getItemCountByItemClass(ItemClass.ASSEMBLY));
        typeDto.setParts(itemRepository.getItemCountByItemClass(ItemClass.PART));
        typeDto.setDocuments(itemRepository.getItemCountByItemClass(ItemClass.DOCUMENT));
        typeDto.setOthers(itemRepository.getItemCountByItemClass(ItemClass.OTHER));
        return typeDto;
    }

    @Transactional(readOnly = true)
    public ItemConfigurationCounts getItemsByConfigurations() {
        ItemConfigurationCounts typeDto = new ItemConfigurationCounts();

        typeDto.getItemsByConfigurations().add(itemRepository.getNormalItemsCount());
        typeDto.getItemsByConfigurations().add(itemRepository.getConfigurableItemsCount());
        typeDto.getItemsByConfigurations().add(itemRepository.getConfiguredItemsCount());
        return typeDto;
    }

    @Transactional(readOnly = true)
    public ItemDashboardCountsDto getProductItemsByStatus() {
        ItemDashboardCountsDto typeDto = new ItemDashboardCountsDto();

        typeDto.getProductItemsByStatus().add(itemRepository.getPendingProductItems(ItemClass.PRODUCT));
        typeDto.getProductItemsByStatus().add(itemRepository.getReleasedProductItems(ItemClass.PRODUCT));
        typeDto.getProductItemsByStatus().add(itemRepository.getRejectedProductItems(ItemClass.PRODUCT));
        return typeDto;
    }

    @Transactional(readOnly = true)
    public ItemDashboardCountsDto getAssemblyItemsByStatus() {
        ItemDashboardCountsDto typeDto = new ItemDashboardCountsDto();

        typeDto.getAssemblyItemsByStatus().add(itemRepository.getPendingProductItems(ItemClass.ASSEMBLY));
        typeDto.getAssemblyItemsByStatus().add(itemRepository.getReleasedProductItems(ItemClass.ASSEMBLY));
        typeDto.getAssemblyItemsByStatus().add(itemRepository.getRejectedProductItems(ItemClass.ASSEMBLY));
        return typeDto;
    }

    @Transactional(readOnly = true)
    public ItemDashboardCountsDto getPartItemsByStatus() {
        ItemDashboardCountsDto typeDto = new ItemDashboardCountsDto();

        typeDto.getPartItemsByStatus().add(itemRepository.getPendingProductItems(ItemClass.PART));
        typeDto.getPartItemsByStatus().add(itemRepository.getReleasedProductItems(ItemClass.PART));
        typeDto.getPartItemsByStatus().add(itemRepository.getRejectedProductItems(ItemClass.PART));
        return typeDto;
    }

    @Transactional(readOnly = true)
    public ItemDashboardCountsDto getDocumentItemsByStatus() {
        ItemDashboardCountsDto typeDto = new ItemDashboardCountsDto();

        typeDto.getDocumentItemsByStatus().add(itemRepository.getPendingProductItems(ItemClass.DOCUMENT));
        typeDto.getDocumentItemsByStatus().add(itemRepository.getReleasedProductItems(ItemClass.DOCUMENT));
        typeDto.getDocumentItemsByStatus().add(itemRepository.getRejectedProductItems(ItemClass.DOCUMENT));
        return typeDto;
    }

    @Transactional(readOnly = true)
    public ItemDashboardCountsDto getOtherItemsByStatus() {
        ItemDashboardCountsDto typeDto = new ItemDashboardCountsDto();

        typeDto.getOtherItemsByStatus().add(itemRepository.getPendingProductItems(ItemClass.OTHER));
        typeDto.getOtherItemsByStatus().add(itemRepository.getReleasedProductItems(ItemClass.OTHER));
        typeDto.getOtherItemsByStatus().add(itemRepository.getRejectedProductItems(ItemClass.OTHER));
        return typeDto;
    }

    @Transactional(readOnly = true)
    public ItemsByLifecycleCounts getItemsByLifeCyclePhases() {
        ItemsByLifecycleCounts itemsByLifecycleCounts = new ItemsByLifecycleCounts();
        List<String> phases = new LinkedList<>();
        List<PLMLifeCycle> plmLifeCycles = itemTypeRepository.getUniqueLifeCyclesByItemClass(ItemClass.PRODUCT);
        for (PLMLifeCycle plmLifeCycle : plmLifeCycles) {
            for (PLMLifeCyclePhase lifeCyclePhase : plmLifeCycle.getPhases()) {
                if (phases.indexOf(lifeCyclePhase.getPhase()) == -1) {
                    phases.add(lifeCyclePhase.getPhase());
                }
            }
        }
        itemsByLifecycleCounts.setProductLifeCycles(phases);
        phases.forEach(phase -> {
            itemsByLifecycleCounts.getProductLifeCycleCounts().add(itemRepository.getItemsByClassAndPhase(ItemClass.PRODUCT, phase));
        });

        phases = new LinkedList<>();
        plmLifeCycles = itemTypeRepository.getUniqueLifeCyclesByItemClass(ItemClass.ASSEMBLY);
        for (PLMLifeCycle plmLifeCycle : plmLifeCycles) {
            for (PLMLifeCyclePhase lifeCyclePhase : plmLifeCycle.getPhases()) {
                if (phases.indexOf(lifeCyclePhase.getPhase()) == -1) {
                    phases.add(lifeCyclePhase.getPhase());
                }
            }
        }
        itemsByLifecycleCounts.setAssemblyLifeCycles(phases);
        phases.forEach(phase -> {
            itemsByLifecycleCounts.getAssemblyLifeCycleCounts().add(itemRepository.getItemsByClassAndPhase(ItemClass.ASSEMBLY, phase));
        });

        phases = new LinkedList<>();
        plmLifeCycles = itemTypeRepository.getUniqueLifeCyclesByItemClass(ItemClass.PART);
        for (PLMLifeCycle plmLifeCycle : plmLifeCycles) {
            for (PLMLifeCyclePhase lifeCyclePhase : plmLifeCycle.getPhases()) {
                if (phases.indexOf(lifeCyclePhase.getPhase()) == -1) {
                    phases.add(lifeCyclePhase.getPhase());
                }
            }
        }
        itemsByLifecycleCounts.setPartLifeCycles(phases);
        phases.forEach(phase -> {
            itemsByLifecycleCounts.getPartLifeCycleCounts().add(itemRepository.getItemsByClassAndPhase(ItemClass.PART, phase));
        });

        phases = new LinkedList<>();
        plmLifeCycles = itemTypeRepository.getUniqueLifeCyclesByItemClass(ItemClass.DOCUMENT);
        for (PLMLifeCycle plmLifeCycle : plmLifeCycles) {
            for (PLMLifeCyclePhase lifeCyclePhase : plmLifeCycle.getPhases()) {
                if (phases.indexOf(lifeCyclePhase.getPhase()) == -1) {
                    phases.add(lifeCyclePhase.getPhase());
                }
            }
        }
        itemsByLifecycleCounts.setDocumentLifeCycles(phases);
        phases.forEach(phase -> {
            itemsByLifecycleCounts.getDocumentLifeCycleCounts().add(itemRepository.getItemsByClassAndPhase(ItemClass.DOCUMENT, phase));
        });

        phases = new LinkedList<>();
        plmLifeCycles = itemTypeRepository.getUniqueLifeCyclesByItemClass(ItemClass.OTHER);
        for (PLMLifeCycle plmLifeCycle : plmLifeCycles) {
            for (PLMLifeCyclePhase lifeCyclePhase : plmLifeCycle.getPhases()) {
                if (phases.indexOf(lifeCyclePhase.getPhase()) == -1) {
                    phases.add(lifeCyclePhase.getPhase());
                }
            }
        }
        itemsByLifecycleCounts.setOtherLifeCycles(phases);
        phases.forEach(phase -> {
            itemsByLifecycleCounts.getOtherLifeCycleCounts().add(itemRepository.getItemsByClassAndPhase(ItemClass.OTHER, phase));
        });

        return itemsByLifecycleCounts;
    }

    @Transactional(readOnly = true)
    public ItemsByLifecycleCounts getProductItemsByLifecyclePhases() {
        ItemsByLifecycleCounts itemsByLifecycleCounts = new ItemsByLifecycleCounts();
        List<String> phases = new LinkedList<>();
        List<PLMLifeCycle> plmLifeCycles = itemTypeRepository.getUniqueLifeCyclesByItemClass(ItemClass.PRODUCT);
        for (PLMLifeCycle plmLifeCycle : plmLifeCycles) {
            for (PLMLifeCyclePhase lifeCyclePhase : plmLifeCycle.getPhases()) {
                if (phases.indexOf(lifeCyclePhase.getPhase()) == -1) {
                    phases.add(lifeCyclePhase.getPhase());
                }
            }
        }
        itemsByLifecycleCounts.setProductLifeCycles(phases);
        phases.forEach(phase -> {
            itemsByLifecycleCounts.getPartLifeCycleCounts().add(itemRepository.getItemsByClassAndPhase(ItemClass.PRODUCT, phase));
        });

        return itemsByLifecycleCounts;
    }

    @Transactional
    public ItemsByLifecycleCounts getAssemblyItemsByLifecyclePhases() {
        ItemsByLifecycleCounts itemsByLifecycleCounts = new ItemsByLifecycleCounts();
        List<String> phases = new LinkedList<>();
        List<PLMLifeCycle> plmLifeCycles = itemTypeRepository.getUniqueLifeCyclesByItemClass(ItemClass.ASSEMBLY);
        for (PLMLifeCycle plmLifeCycle : plmLifeCycles) {
            for (PLMLifeCyclePhase lifeCyclePhase : plmLifeCycle.getPhases()) {
                if (phases.indexOf(lifeCyclePhase.getPhase()) == -1) {
                    phases.add(lifeCyclePhase.getPhase());
                }
            }
        }
        itemsByLifecycleCounts.setProductLifeCycles(phases);
        phases.forEach(phase -> {
            itemsByLifecycleCounts.getPartLifeCycleCounts().add(itemRepository.getItemsByClassAndPhase(ItemClass.ASSEMBLY, phase));
        });

        return itemsByLifecycleCounts;
    }

    @Transactional
    public ItemsByLifecycleCounts getPartItemsByLifecyclePhases() {
        ItemsByLifecycleCounts itemsByLifecycleCounts = new ItemsByLifecycleCounts();
        List<String> phases = new LinkedList<>();
        List<PLMLifeCycle> plmLifeCycles = itemTypeRepository.getUniqueLifeCyclesByItemClass(ItemClass.PART);
        for (PLMLifeCycle plmLifeCycle : plmLifeCycles) {
            for (PLMLifeCyclePhase lifeCyclePhase : plmLifeCycle.getPhases()) {
                if (phases.indexOf(lifeCyclePhase.getPhase()) == -1) {
                    phases.add(lifeCyclePhase.getPhase());
                }
            }
        }
        itemsByLifecycleCounts.setProductLifeCycles(phases);
        phases.forEach(phase -> {
            itemsByLifecycleCounts.getPartLifeCycleCounts().add(itemRepository.getItemsByClassAndPhase(ItemClass.PART, phase));
        });

        return itemsByLifecycleCounts;
    }

    @Transactional
    public ItemsByLifecycleCounts getDocumentItemsByLifecyclePhases() {
        ItemsByLifecycleCounts itemsByLifecycleCounts = new ItemsByLifecycleCounts();
        List<String> phases = new LinkedList<>();
        List<PLMLifeCycle> plmLifeCycles = itemTypeRepository.getUniqueLifeCyclesByItemClass(ItemClass.DOCUMENT);
        for (PLMLifeCycle plmLifeCycle : plmLifeCycles) {
            for (PLMLifeCyclePhase lifeCyclePhase : plmLifeCycle.getPhases()) {
                if (phases.indexOf(lifeCyclePhase.getPhase()) == -1) {
                    phases.add(lifeCyclePhase.getPhase());
                }
            }
        }
        itemsByLifecycleCounts.setProductLifeCycles(phases);
        phases.forEach(phase -> {
            itemsByLifecycleCounts.getPartLifeCycleCounts().add(itemRepository.getItemsByClassAndPhase(ItemClass.DOCUMENT, phase));
        });

        return itemsByLifecycleCounts;
    }

    @Transactional
    public ItemsByLifecycleCounts getOtherItemsByLifecyclePhases() {
        ItemsByLifecycleCounts itemsByLifecycleCounts = new ItemsByLifecycleCounts();
        List<String> phases = new LinkedList<>();
        List<PLMLifeCycle> plmLifeCycles = itemTypeRepository.getUniqueLifeCyclesByItemClass(ItemClass.OTHER);
        for (PLMLifeCycle plmLifeCycle : plmLifeCycles) {
            for (PLMLifeCyclePhase lifeCyclePhase : plmLifeCycle.getPhases()) {
                if (phases.indexOf(lifeCyclePhase.getPhase()) == -1) {
                    phases.add(lifeCyclePhase.getPhase());
                }
            }
        }
        itemsByLifecycleCounts.setProductLifeCycles(phases);
        phases.forEach(phase -> {
            itemsByLifecycleCounts.getPartLifeCycleCounts().add(itemRepository.getItemsByClassAndPhase(ItemClass.OTHER, phase));
        });

        return itemsByLifecycleCounts;
    }

    @Transactional(readOnly = true)
    public List<TopProblemItems> getTopProblemItems() {
        List<TopProblemItems> problemItems = new LinkedList<>();

        List<Object[]> objects = prProblemItemRepository.getTopProductItems();
        for (Object[] row : objects) {
            Integer itemId = (Integer) row[0];
            BigInteger count = (BigInteger) row[1];
            TopProblemItems productProblem = new TopProblemItems();
            PLMItem item = itemRepository.findOne(itemId);
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(item.getLatestRevision());
            productProblem.setItemId(itemRevision.getId());
            productProblem.setRevision(itemRevision.getRevision());
            productProblem.setItemName(item.getItemName());
            productProblem.setItemNumber(item.getItemNumber());
            productProblem.setCount(count);

            problemItems.add(productProblem);
        }

        return problemItems;
    }

    @Transactional(readOnly = true)
    public List<TopProblemItemTypes> getTopProblemItemTypes() {
        List<TopProblemItemTypes> problemItemTypes = new LinkedList<>();

        List<Object[]> objects = prProblemItemRepository.getTopProblemItemTypes();
        for (Object[] row : objects) {
            Integer itemId = (Integer) row[0];
            BigInteger count = (BigInteger) row[1];
            TopProblemItemTypes topProblemItemType = new TopProblemItemTypes();
            PLMItemType itemType = itemTypeRepository.findOne(itemId);
            topProblemItemType.setId(itemType.getId());
            topProblemItemType.setName(itemType.getName());
            topProblemItemType.setCount(count);

            problemItemTypes.add(topProblemItemType);
        }

        return problemItemTypes;
    }

    @Transactional(readOnly = true)
    public List<TopProblemItemTypes> getTopFrequentlyChangingItemTypes() {
        List<TopProblemItemTypes> problemItemTypes = new LinkedList<>();

        List<Object[]> objects = affectedItemRepository.getTopFrequentlyChangingItemTypes();
        for (Object[] row : objects) {
            Integer itemId = (Integer) row[0];
            BigInteger count = (BigInteger) row[1];
            TopProblemItemTypes topProblemItemType = new TopProblemItemTypes();
            PLMItemType itemType = itemTypeRepository.findOne(itemId);
            topProblemItemType.setId(itemType.getId());
            topProblemItemType.setName(itemType.getName());
            topProblemItemType.setCount(count);

            problemItemTypes.add(topProblemItemType);
        }

        return problemItemTypes;
    }
}
