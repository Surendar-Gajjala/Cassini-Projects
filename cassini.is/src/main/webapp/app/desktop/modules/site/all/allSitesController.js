define(['app/desktop/modules/site/sites.module',
        'app/desktop/modules/site/all/newSiteDialogController',
        'app/shared/services/pm/project/projectSiteService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/storeService',
        'app/shared/services/tm/taskService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService'

    ],
    function (module) {
        module.controller('SitesAllController', SitesAllController);

        function SitesAllController($scope, $rootScope, $stateParams, $timeout, $state, $cookies, TaskService,
                                    CommonService, $uibModal, StoreService, ProjectSiteService, DialogService, $window,
                                    ItemService, AttributeAttachmentService, ObjectTypeAttributeService, ObjectAttributeService, $sce) {

            var vm = this;

            $rootScope.viewInfo.icon = "fa fa-map-marker";
            $rootScope.viewInfo.title = "Sites";

            vm.loading = true;
            vm.showValues = true;

            vm.showSite = showSite;
            vm.newSite = newSite;
            vm.loadSites = loadSites;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.deleteSite = deleteSite;
            vm.editSite = editSite;
            vm.cancelChanges = cancelChanges;
            vm.applyChanges = applyChanges;
            vm.selectStore = selectStore;
            vm.removeStore = removeStore;
            vm.resetPage = resetPage;
            vm.freeTextSearch = freeTextSearch;
            vm.clearFilter = clearFilter;
            vm.project = $stateParams.projectId;
            vm.sitesMap = new Hashtable();
            vm.requiredSiteAttributes = [];
            var currencyMap = new Hashtable();

            vm.stores = [];
            vm.siteStores = [];
            vm.siteList = [];
            vm.tasks = [];

            vm.emptyFilters = {
                searchQuery: null,
                site: null,
                store: null
            };

            var pageable = {
                page: 0,
                size: 10,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };
            vm.ecos = angular.copy(pagedResults);
            vm.filters = angular.copy(vm.emptyFilters);
            $scope.freeTextQuery = null;
            vm.showImage = showImage;
            vm.showRequiredImage = showRequiredImage;
            vm.openAttachment = openAttachment;

            function openAttachment(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/download".
                    format(window.location.protocol, window.location.host,
                    attachment.id);
                launchUrl(url);
            }

            function showImage(attribute) {
                var modal = document.getElementById('siteModal');
                var modalImg = document.getElementById('siteImg');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            function showRequiredImage(attribute) {
                var modal = document.getElementById('siteModal1');
                var modalImg = document.getElementById('siteImg1');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage1")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            function showSite(site) {
                $rootScope.siteId = site.id;
                $state.go('app.pm.project.sites.details', {siteId: site.id});
            }

            function newSite() {
                var options = {
                    title: 'New Site',
                    showMask: true,
                    template: 'app/desktop/modules/site/all/newSiteDialog.jsp',
                    controller: 'NewSiteDialogController as newSiteVm',
                    resolve: 'app/desktop/modules/site/all/newSiteDialogController',
                    width: 500,
                    data: {
                        siteList: vm.siteList.content
                    },
                    buttons: [
                        {text: 'Create', broadcast: 'app.site.new'}
                    ],
                    callback: function () {
                        loadSites();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function resetPage() {
                pageable.page = 0;
            }

            function freeTextSearch(freeText) {
                $scope.freeTextQuery = freeText;
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    vm.filters.searchQuery = freeText;
                    ProjectSiteService.freeTextSearch($stateParams.projectId, pageable, vm.filters).then(
                        function (data) {
                            vm.siteList = data;
                            angular.forEach(vm.siteList.content, function (site) {
                                site.editMode = false;
                                site.showValues = true;
                                site.newDescription = site.description;
                            });
                            vm.clear = true;
                        }
                    );
                } else {
                    resetPage();
                    loadSites();
                }
            }

            function loadSites() {
                ProjectSiteService.getPagedSitesByProject(vm.project, pageable).then(
                    function (data) {
                        vm.loading = false;
                        vm.siteList = data;
                        angular.forEach(vm.siteList.content, function (site) {
                            site.editMode = false;
                            site.showValues = true;
                            site.newDescription = site.description;
                        });
                        vm.showValues = true;
                        loadSiteAttributeValues();
                    }
                );

            }

            function clearFilter() {
                loadSites();
                vm.clear = false;
            }

            function removeStore(item) {
                ProjectSiteService.deleteSiteStore(item.site, item.rowId).then(
                    function (data) {
                        $rootScope.showSuccessMessage("Store deleted successfully");
                        StoreService.getStore($stateParams.projectId, item.store).then(
                            function (data) {
                                vm.stores.push(data);
                            }
                        );
                        loadSiteStores();
                    }
                )
            }

            function selectStore(item) {
                item.storeObject = item;
            }

            function applyChanges(site) {
                var siteStores = [];
                site.editMode = false;
                site.description = site.newDescription;
                ProjectSiteService.updateSite(site.id, site).then(
                    function (data) {
                        site.id = data.id;
                        site.editMode = false;
                        $timeout(function () {
                            site.showValues = true;
                        }, 100);
                        $rootScope.showSuccessMessage(site.name + " : Site updated successfully");
                    }
                );

                loadStores();
            }

            function cancelChanges(site) {
                var promise = null;
                if (site.id == null || site.id == undefined) {
                    vm.siteList.splice(vm.siteList.content.indexOf(site), 1);
                }
                else {
                    site.newDescription = site.description;
                    promise = ProjectSiteService.updateSite(site.id, site);
                    site.editMode = false;
                    loadSites();
                    $timeout(function () {
                        site.showValues = true;
                    }, 500);
                }

            }

            function editSite(site) {
                angular.forEach(site.stores, function (siteStore) {
                    angular.forEach(vm.stores, function (store) {
                        if (siteStore.store == store.id) {
                            var index = vm.stores.indexOf(store);
                            vm.stores.splice(index, 1);
                        }
                    })
                });
                site.showValues = false;
                site.editMode = true;
                $timeout(function () {
                }, 500);
            }

            function isSiteExistsInAnyTasks(site) {
                var exists = false;
                angular.forEach(vm.siteIds, function (siteId) {
                    if (siteId == site.id) {
                        exists = true;
                    }
                });
                return exists;
            }

            function deleteSite(site) {
                if (!isSiteExistsInAnyTasks(site)) {
                    var options = {
                        title: 'Delete Site',
                        message: 'Are you sure you want to delete (' + site.name + ') Site?',
                        okButtonClass: 'btn-danger'
                    };
                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            ProjectSiteService.deleteProjectSite($stateParams.projectId, site.id).then(
                                function (data) {
                                    var index = vm.siteList.content.indexOf(site);
                                    vm.siteList.content.splice(index, 1);
                                    $rootScope.showSuccessMessage(site.name + " : Site deleted successfully");
                                }
                            )
                        }
                    });
                }
                else {
                    $rootScope.showErrorMessage("This Site is assigned to a Task! We cannot delete this Site");
                }
            }

            function loadStores() {
                StoreService.getUnAssignedStores($stateParams.projectId).then(
                    function (data) {
                        vm.stores = data;

                    }
                );
            }

            function nextPage() {
                if (vm.siteList.last != true) {
                    pageable.page++;
                    loadSites();
                }
            }

            function previousPage() {
                if (vm.siteList.first != true) {
                    pageable.page--;
                    loadSites();
                }
            }

            function loadTasks() {
                TaskService.getListProjectTasks($stateParams.projectId).then(
                    function (data) {
                        vm.tasks = data;
                        vm.siteIds = [];
                        angular.forEach(vm.tasks, function (task) {
                            vm.siteIds.push(task.site);
                        });
                    }
                )
            }

            vm.showSiteAttributes = showSiteAttributes;

            vm.siteAttributes = [];
            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("siteAttributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            function showSiteAttributes() {
                var options = {
                    title: 'Site Attributes',
                    showMask: true,
                    template: 'app/desktop/modules/home/attributes/allAttributesView.jsp',
                    controller: 'AllAttributesController as allAttributesVm',
                    resolve: 'app/desktop/modules/home/attributes/allAttributesController',
                    width: 600,
                    data: {
                        selectedAttributes: vm.siteAttributes,
                        attributesMode: 'SITE'
                    },
                    buttons: [
                        {text: 'Add', broadcast: 'app.items.attributes.select'}
                    ],
                    callback: function (result) {
                        vm.siteAttributes = result;
                        $window.localStorage.setItem("siteAttributes", JSON.stringify(vm.siteAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage("Attributes added successfully");
                        }
                        $rootScope.hideSidePanel();
                        loadRequiredSiteAttributes();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.removeAttribute = removeAttribute;
            function removeAttribute(att) {
                vm.siteAttributes.remove(att);
            }

            function loadRequiredSiteAttributes() {
                ItemService.getTypeAttributesRequiredTrue("SITE").then(
                    function (data) {
                        vm.requiredSiteAttributes = data;
                        loadSites();
                    }
                )
            }

            function loadSiteAttributeValues() {
                vm.itemIds = [];
                vm.attributeIds = [];
                vm.requiredAttributeIds = [];
                angular.forEach(vm.siteList.content, function (item) {
                    item.refValueString = null;
                    vm.itemIds.push(item.id);
                });
                angular.forEach(vm.siteAttributes, function (siteAttribute) {
                    if (siteAttribute.id != null && siteAttribute.id != "" && siteAttribute.id != 0) {
                        vm.attributeIds.push(siteAttribute.id);
                    }
                });

                angular.forEach(vm.requiredSiteAttributes, function (siteAttribute) {
                    if (siteAttribute.id != null && siteAttribute.id != "" && siteAttribute.id != 0) {
                        vm.requiredAttributeIds.push(siteAttribute.id);
                    }
                });

                if (vm.itemIds.length > 0 && vm.attributeIds.length > 0) {
                    ItemService.getAttributesByItemIdAndAttributeId(vm.itemIds, vm.attributeIds).then(
                        function (data) {
                            vm.selectedObjectAttributes = data;

                            var map = new Hashtable();
                            angular.forEach(vm.siteAttributes, function (att) {
                                if (att.id != null && att.id != "" && att.id != 0) {
                                    map.put(att.id, att);
                                }
                            });

                            angular.forEach(vm.siteList.content, function (item) {
                                var attributes = [];

                                var itemAttributes = vm.selectedObjectAttributes[item.id];
                                if (itemAttributes != null && itemAttributes != undefined) {
                                    attributes = attributes.concat(itemAttributes);
                                }
                                angular.forEach(attributes, function (attribute) {
                                    var selectatt = map.get(attribute.id.attributeDef);
                                    if (selectatt != null) {
                                        var attributeName = selectatt.name;
                                        if (selectatt.dataType == 'TEXT') {
                                            item[attributeName] = attribute.stringValue;
                                        } else if (selectatt.dataType == 'INTEGER') {
                                            item[attributeName] = attribute.integerValue;
                                        } else if (selectatt.dataType == 'BOOLEAN') {
                                            item[attributeName] = attribute.booleanValue;
                                        } else if (selectatt.dataType == 'DOUBLE') {
                                            item[attributeName] = attribute.doubleValue;
                                        } else if (selectatt.dataType == 'DATE') {
                                            item[attributeName] = attribute.dateValue;
                                        } else if (selectatt.dataType == 'LIST') {
                                            item[attributeName] = attribute.listValue;
                                        } else if (selectatt.dataType == 'TIME') {
                                            item[attributeName] = attribute.timeValue;
                                        } else if (selectatt.dataType == 'TIMESTAMP') {
                                            item[attributeName] = attribute.timestampValue;
                                        } else if (selectatt.dataType == 'CURRENCY') {
                                            item[attributeName] = attribute.currencyValue;
                                            if (attribute.currencyType != null) {
                                                item[attributeName + 'type'] = currencyMap.get(attribute.currencyType);
                                            }
                                        } else if (selectatt.dataType == 'ATTACHMENT') {
                                            var attachmentIds = [];
                                            if (attribute.attachmentValues.length > 0) {
                                                angular.forEach(attribute.attachmentValues, function (attachmentId) {
                                                    attachmentIds.push(attachmentId);
                                                });
                                                AttributeAttachmentService.getMultipleAttributeAttachments(attachmentIds).then(
                                                    function (data) {
                                                        vm.siteAttachments = data;
                                                        item[attributeName] = vm.siteAttachments;
                                                    }
                                                )
                                            }
                                        } else if (selectatt.dataType == 'IMAGE') {
                                            if (attribute.imageValue != null) {
                                                item[attributeName] = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();

                                            }
                                        } else if (selectatt.dataType == 'OBJECT') {
                                            if (attribute.refValue != null) {
                                                var objectSelector = $application.getObjectSelector(selectatt.refType);
                                                if (objectSelector != null && attribute.refValue != null) {
                                                    objectSelector.getDetails(attribute.refValue, item, attributeName);
                                                }
                                            }
                                        }
                                    }
                                })
                            })

                        }
                    );
                }

                if (vm.itemIds.length > 0 && vm.requiredAttributeIds.length > 0) {
                    ItemService.getAttributesByItemIdAndAttributeId(vm.itemIds, vm.requiredAttributeIds).then(
                        function (data) {
                            vm.requiredAttributes = data;

                            var map = new Hashtable();
                            angular.forEach(vm.requiredSiteAttributes, function (att) {
                                if (att.id != null && att.id != "" && att.id != 0) {
                                    map.put(att.id, att);
                                }
                            });

                            angular.forEach(vm.siteList.content, function (item) {
                                var attributes = [];

                                var itemAttributes = vm.requiredAttributes[item.id];
                                if (itemAttributes != null && itemAttributes != undefined) {
                                    attributes = attributes.concat(itemAttributes);
                                }
                                angular.forEach(attributes, function (attribute) {
                                    var selectatt = map.get(attribute.id.attributeDef);
                                    if (selectatt != null) {
                                        var attributeName = selectatt.name;
                                        if (selectatt.dataType == 'TEXT') {
                                            item[attributeName] = attribute.stringValue;
                                        } else if (selectatt.dataType == 'INTEGER') {
                                            item[attributeName] = attribute.integerValue;
                                        } else if (selectatt.dataType == 'BOOLEAN') {
                                            item[attributeName] = attribute.booleanValue;
                                        } else if (selectatt.dataType == 'DOUBLE') {
                                            item[attributeName] = attribute.doubleValue;
                                        } else if (selectatt.dataType == 'DATE') {
                                            item[attributeName] = attribute.dateValue;
                                        } else if (selectatt.dataType == 'LIST') {
                                            item[attributeName] = attribute.listValue;
                                        } else if (selectatt.dataType == 'TIME') {
                                            item[attributeName] = attribute.timeValue;
                                        } else if (selectatt.dataType == 'TIMESTAMP') {
                                            item[attributeName] = attribute.timestampValue;
                                        } else if (selectatt.dataType == 'CURRENCY') {
                                            item[attributeName] = attribute.currencyValue;
                                            if (attribute.currencyType != null) {
                                                item[attributeName + 'type'] = currencyMap.get(attribute.currencyType);
                                            }
                                        } else if (selectatt.dataType == 'ATTACHMENT') {
                                            var attachmentIds = [];
                                            if (attribute.attachmentValues.length > 0) {
                                                angular.forEach(attribute.attachmentValues, function (attachmentId) {
                                                    attachmentIds.push(attachmentId);
                                                });
                                                AttributeAttachmentService.getMultipleAttributeAttachments(attachmentIds).then(
                                                    function (data) {
                                                        vm.requiredMaterialAttachments = data;
                                                        item[attributeName] = vm.requiredMaterialAttachments;
                                                    }
                                                )
                                            }
                                        } else if (selectatt.dataType == 'IMAGE') {
                                            if (attribute.imageValue != null) {
                                                item[attributeName] = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();

                                            }
                                        } else if (selectatt.dataType == 'OBJECT') {
                                            if (attribute.refValue != null) {
                                                var objectSelector = $application.getObjectSelector(selectatt.refType);
                                                if (objectSelector != null && attribute.refValue != null) {
                                                    objectSelector.getDetails(attribute.refValue, item, attributeName);
                                                }
                                            }
                                        }
                                    }
                                })
                            })

                        }
                    );
                }

            }

            vm.objectIds = [];
            function checkJson() {
                if (validateJSON()) {
                    var setAttributes = JSON.parse($window.localStorage.getItem("siteAttributes"));
                } else {
                    var setAttributes = null;
                }

                if (setAttributes != null && setAttributes != undefined) {
                    angular.forEach(setAttributes, function (setAtt) {
                        if (setAtt.id != null && setAtt.id != "" && setAtt.id != 0) {
                            vm.objectIds.push(setAtt.id);
                        }
                    });
                    ObjectTypeAttributeService.getObjectTypeAttributesByIds(vm.objectIds).then(
                        function (data) {
                            if (data.length == 0) {
                                setAttributes = null;
                                $window.localStorage.setItem("siteAttributes", "");
                                vm.siteAttributes = setAttributes
                            } else {
                                vm.siteAttributes = setAttributes;
                            }

                            if (vm.mode == null || vm.mode == undefined || vm.mode == "") {
                                loadRequiredSiteAttributes();
                            }
                        }
                    )
                } else {
                    if (vm.mode == null || vm.mode == undefined || vm.mode == "") {
                        loadRequiredSiteAttributes();
                    }
                }
            }

            function loadCurrencies() {
                ObjectAttributeService.getCurrencies().then(
                    function (data) {
                        vm.currencies = data;
                        angular.forEach(vm.currencies, function (currency) {
                            currencyMap.put(currency.id, $sce.trustAsHtml(currency.symbol));
                        });
                    }
                );
            }

            (function () {
                loadCurrencies();
                if ($application.homeLoaded == true) {
                    checkJson();
                }
            })();
        }
    }
)
;