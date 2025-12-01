define(['app/desktop/modules/mfr/mfrparts/mfrparts.module',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/recentlyVisitedService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/desktop/directives/all-view-attributes/allViewAttributesDirective',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective',
        'app/desktop/directives/all-view-icons/allViewIconsDirective',
        'app/desktop/modules/classification/workflowDirective/workflowManufacturerPartDirective'
    ],
    function (module) {
        module.controller('AllMfrPartsController', AllMfrPartsController);

        function AllMfrPartsController($scope, $rootScope, $timeout, $state, $sce, $stateParams, $window, $cookies, $cookieStore, $translate,
                                       $uibModal, CommonService, DialogService, MfrPartsService, MfrService, RecentlyVisitedService, ObjectTypeAttributeService) {
            var vm = this;
            var parsed = angular.element("<div></div>");
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;

            vm.loading = true;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;
            vm.mode = null;
            vm.mode = $rootScope.searchType;
            vm.selectedMfrPartType = null;
            vm.mfrPartTitle = parsed.html($translate.instant("CREATE_MFR_PARTS")).html();
            vm.savedSearchItems = parsed.html($translate.instant("ALL_VIEW_SAVED_SEARCHES")).html();
            vm.saveSearchMfrPart = parsed.html($translate.instant("SAVED_SEARCH_MFR_PART")).html();
            vm.share = parsed.html($translate.instant("SHARE")).html();
            vm.partAttributesTitle = parsed.html($translate.instant("MFR_PART_ATTRIBUTES")).html();
            $rootScope.localStorageLogin = JSON.parse(localStorage.getItem('local_storage_login'));

            if ($rootScope.localStorageLogin.login.person.id != null) {
                vm.person = $rootScope.localStorageLogin.login.person.id;
            }
            vm.pageable = {
                page: 0,
                size: 20,
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
                size: vm.pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };

            vm.filters = {
                partNumber: null,
                partName: null,
                description: null,
                mfrPartType: '',
                type:'',
                manufacturer: '',
                freeTextSearch: true,
                searchQuery: null
            };
            $scope.freeTextQuery = null;

            vm.mfrParts = angular.copy(pagedResults);

            $rootScope.showSearch = false;
            function nextPage() {
                if (vm.mfrParts.last != true) {
                    $rootScope.showBusyIndicator($('body'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadMfrParts();
                }
            }

            function previousPage() {
                if (vm.mfrParts.first != true) {
                    $rootScope.showBusyIndicator($('body'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadMfrParts();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.page = 0;
                vm.pageable.size = page;
                loadMfrParts();
            }

            vm.search = {
                name: null,
                description: null,
                searchType: null,
                query: null,
                objectType: 'SAVEDSEARCH',
                searchObjectType: 'MANUFACTURERPART',
                type: 'PRIVATE',
                owner: vm.person
            };

            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                vm.searchText = freeText;
                $rootScope.showBusyIndicator($('body'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.filters.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    $rootScope.showSearch = true;
                    vm.search.searchType = "freetext";
                    vm.search.query = freeText;
                    vm.search.query = angular.toJson(freeText);
                    loadMfrParts();
                } else {
                    resetPage();
                    $rootScope.showSearch = false;
                }
            }

            function resetPage() {
                vm.mfrParts = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                $rootScope.showBusyIndicator($('body'));
                loadMfrParts();
            }

            $scope.mfrPartsInspectionReportPopover = {
                templateUrl: 'app/desktop/modules/mfr/mfrparts/all/mfrPartsInspectionReportPopoverTemplate.jsp'
            };

            function loadMfrParts() {
                $rootScope.showBusyIndicator();
                vm.loading = true;
                MfrPartsService.getMfrParts(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.mfrParts = data;
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                        loadMfrPartAttributeValues();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }


            vm.showMfrDetails = showMfrDetails;
            function showMfrDetails(mfr) {
                $state.go('app.mfr.details', {manufacturerId: mfr.manufacturer});
            }


            vm.showMfrPartDetails = showMfrPartDetails;
            function showMfrPartDetails(part) {
                vm.recentlyVisited = {};
                vm.recentlyVisited.objectId = part.id;
                vm.recentlyVisited.objectType = part.objectType;
                vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                    function (data) {
                        $state.go('app.mfr.mfrparts.details', {mfrId: part.manufacturer, manufacturePartId: part.id});
                    }, function (error) {
                        $state.go('app.mfr.mfrparts.details', {mfrId: part.manufacturer, manufacturePartId: part.id});
                    }
                );
            }

            vm.createParts = createParts;
            var mfrPartTitle = parsed.html($translate.instant("NEW_MANUFACTURER_PART_TITLE")).html();
            var createButton = parsed.html($translate.instant("CREATE")).html();

            function createParts() {
                var options = {
                    title: mfrPartTitle,
                    template: 'app/desktop/modules/mfr/all/newManufacturerPartView.jsp',
                    resolve: 'app/desktop/modules/mfr/all/newManufacturerPartController',
                    controller: 'NewManufacturerPartController as newManufacturerPartVm',
                    width: 600,
                    showMask: true,
                    data: {},
                    buttons: [
                        {text: createButton, broadcast: 'app.mfr.mfrPart.new'}
                    ],
                    callback: function (result) {
                        $timeout(function () 
                        {
                            loadMfrParts();
                        }, 500);

                    }
                };

                $rootScope.showSidePanel(options);
            }


            var MfrAttributeTitle = parsed.html($translate.instant("ATTRIBUTES")).html();
            var selectedAttributesAdded = parsed.html($translate.instant("SELECTED_ATTRIBUTES_MESSAGE")).html();
            var addButton = parsed.html($translate.instant("ADD")).html();
            vm.showPartAttributes = showPartAttributes;
            function showPartAttributes() {
                var options = {
                    title: MfrAttributeTitle,
                    template: 'app/desktop/modules/shared/attributes/attributesView.jsp',
                    resolve: 'app/desktop/modules/shared/attributes/attributesController',
                    controller: 'AttributesController as attributesVm',
                    width: 500,
                    showMask: true,
                    data: {
                        selectedAttributes: vm.selectedMfrPartAttributes,
                        type: "MANUFACTURERPARTTYPE",
                        objectType: "MANUFACTURERPART"
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.select.attributes'}
                    ],
                    callback: function (result) {
                        vm.selectedMfrPartAttributes = result;
                        $window.localStorage.setItem("mfrPartAttributes", JSON.stringify(vm.selectedMfrPartAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesAdded);
                        }
                        loadMfrParts();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function loadMfrs() {
                MfrService.getAllManufacturers().then(
                    function (data) {
                        vm.loading = false;
                        vm.manufacturers = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );
            }

            vm.objectIds = [];
            vm.mfrPartsIds = [];
            vm.attributeIds = [];
            vm.selectedMfrPartAttributes = [];
            function loadMfrPartAttributeValues() {
                angular.forEach(vm.mfrParts.content, function (part) {
                    vm.mfrPartsIds.push(part.id);
                    if (part.thumbnail != null) {
                        part.imagePath = "api/plm/mfr/parts/" + part.id + "/image/download?" + new Date().getTime();
                    }
                });
                if (vm.selectedMfrPartAttributes != null && vm.selectedMfrPartAttributes.length > 0) {
                    angular.forEach(vm.selectedMfrPartAttributes, function (selectedAttribute) {
                        if (selectedAttribute.id != null && selectedAttribute.id != "" && selectedAttribute.id != 0) {
                            vm.attributeIds.push(selectedAttribute.id);
                        }
                    });
                    $rootScope.getObjectAttributeValues(vm.mfrPartsIds, vm.attributeIds, vm.selectedMfrPartAttributes, vm.mfrParts.content);
                }
            }

            vm.removeAttribute = removeAttribute;
            function removeAttribute(att) {
                vm.selectedMfrPartAttributes.remove(att);
                $window.localStorage.setItem("mfrPartAttributes", JSON.stringify(vm.selectedMfrPartAttributes));
            }

            vm.clearTypeSelection = clearTypeSelection;
            function clearTypeSelection() {
                $('.view-content').click();
                vm.pageable.page = 0;
                vm.selectedMfrPartType = null;
                vm.filters.type = '';
                loadMfrParts();
            }

            vm.onSelectType = onSelectType;
            function onSelectType(mfrPartType) {
                vm.pageable.page = 0; 
                vm.selectedMfrPartType = mfrPartType;
                vm.filters.type = mfrPartType.id;
                vm.filters.freeTextSearch = false;
                loadMfrParts();
            }

            vm.showSaveSearch = showSaveSearch;
            function showSaveSearch() {
                var options = {
                    title: searchTitle,
                    template: 'app/desktop/modules/item/newSearchView.jsp',
                    controller: 'NewSearchController as newSearchVm',
                    resolve: 'app/desktop/modules/item/newSearchController',
                    width: 600,
                    data: {
                        search: vm.search
                    },
                    showMask: true,
                    buttons: [
                        {text: createButton, broadcast: 'app.search.new'}
                    ],
                    callback: function () {

                    }
                };

                $rootScope.showSidePanel(options);
            }

            var searchTitle = $translate.instant("NEW_SEARCH");
            $scope.saved = parsed.html($translate.instant("SAVED_SEARCH_TITLE")).html();
            vm.showPartSavedSearches = showPartSavedSearches;
            function showPartSavedSearches() {
                var options = {
                    title: $scope.saved,
                    template: 'app/desktop/modules/item/showSavedSearches.jsp',
                    controller: 'SavedSearchesController as searchVm',
                    resolve: 'app/desktop/modules/item/savedSearchesController',
                    width: 600,
                    showMask: true,
                    data: {
                        type: "MANUFACTURERPART"
                    },
                    callback: function (result) {
                        var query = angular.fromJson(result.query);
                        if (result.searchType == 'freetext') {
                            freeTextSearch(query);
                        }
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function loadSavedSearch(searchType, query) {
                if (searchType == 'freetext') {
                    freeTextSearch(query);
                }
            }

            vm.showShareButton = false;
            vm.selectCheck = selectCheck;
            vm.selectAll = selectAll;
            vm.selectedParts = [];
            function selectAll() {
                vm.selectedParts = [];
                if (vm.flag) {
                    angular.forEach(vm.mfrParts.content, function (part) {
                            vm.selectedParts.push(part);
                            part.selected = true;
                            vm.showShareButton = true;
                        }
                    )
                } else {
                    vm.selectedParts = [];
                    vm.showShareButton = false;
                    angular.forEach(vm.mfrParts.content, function (part) {
                        part.selected = false;
                    })
                }

            }

            function selectCheck(part) {
                if (part.selected == false) {
                    var index = vm.selectedParts.indexOf(part);
                    if (index != -1) {
                        vm.selectedParts.splice(index, 1);
                        if (vm.selectedParts.length == 0) {
                            vm.showShareButton = false;
                        }
                        if (vm.mfrParts.content.length == vm.selectedParts.length) {
                            vm.flag = true;
                        } else {
                            vm.flag = false;
                        }
                    }
                }
                else {
                    vm.selectedParts.push(part);
                    if (vm.mfrParts.content.length == vm.selectedParts.length) {
                        vm.flag = true;
                    } else {
                        vm.flag = false;
                    }
                    vm.selection = 'selected';
                    vm.showShareButton = true;
                }
            }

            var shareItem = parsed.html($translate.instant("SHARE_MFR_PART")).html();
            var shareButton = parsed.html($translate.instant("SHARE")).html();
            vm.shareSelectedParts = shareSelectedParts;
            function shareSelectedParts() {
                var options = {
                    title: shareItem,
                    template: 'app/desktop/modules/shared/share/shareView.jsp',
                    controller: 'ShareController as shareVm',
                    resolve: 'app/desktop/modules/shared/share/shareController',
                    width: 600,
                    showMask: true,
                    data: {
                        selectedShareItems: vm.selectedParts,
                        itemsSharedType: 'partsSelection',
                        objectType: "MANUFACTURERPART"
                    },
                    buttons: [
                        {text: shareButton, broadcast: 'app.shareSelectedItems.item'}
                    ],
                    callback: function (data) {
                        angular.forEach(vm.selectedParts, function (selectedItem) {
                            selectedItem.selected = false;
                        });
                        vm.showShareButton = false;
                        vm.selectedParts = [];
                        vm.flag = false;
                    }
                };
                $rootScope.showSidePanel(options);
            }

            vm.showImage = showImage;
            function showImage(mfrPart) {
                var modal = document.getElementById('item-thumbnail' + mfrPart.id);
                modal.style.display = "block";

                var span = document.getElementById("thumbnail-close" + mfrPart.id);
                $("#thumbnail-image" + mfrPart.id).width($('#thumbnail-view' + mfrPart.id).outerWidth());
                $("#thumbnail-image" + mfrPart.id).height($('#thumbnail-view' + mfrPart.id).outerHeight());
               // $(".split-pane-divider").css('z-index', 0);
                span.onclick = function () {
                    modal.style.display = "none";
                }
                $scope.$evalAsync();
            }

            $scope.deletePartTitle = parsed.html($translate.instant("PART_DIALOG_TITLE")).html();
            $scope.deleteDialogMessage = parsed.html($translate.instant("PART_DIALOG_MESSAGE")).html();
            var deletePartMessage = $translate.instant("PART_DELETE_MESSAGE");
            var partDelete = parsed.html($translate.instant("ITEMDELETE")).html();
            $scope.PartUsedMessage = parsed.html($translate.instant("PART_USED_MESSAGE")).html();

            vm.deleteMfrpart = deleteMfrpart;
            function deleteMfrpart(mfrpart) {
                MfrPartsService.getMfrPartUsedCount(mfrpart.manufacturer,mfrpart.id).then(
                    function (data) {
                        if (data == 0) {
                            var options = {
                                title: $scope.deletePartTitle,
                                message: $scope.deleteDialogMessage + " [ " + mfrpart.partName + " ] " + partDelete + "?",
                                okButtonClass: 'btn-danger'
                            };
                            DialogService.confirm(options, function (yes) {
                                if (yes == true) {
                                    MfrPartsService.deleteManufacturepart(mfrpart.manufacturer, mfrpart.id).then(
                                        function (data) {
                                            var index = vm.mfrParts.content.indexOf(mfrpart);
                                            vm.mfrParts.content.splice(index, 1);
                                            $rootScope.showSuccessMessage(deletePartMessage);
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                            $rootScope.hideBusyIndicator();
                                        }
                                    )
                                }
                            });
                        } else {

                            $rootScope.showErrorMessage($scope.PartUsedMessage);

                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("mfrPartAttributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            (function () {
                var setAttributes = null;
                if (validateJSON()) {
                    setAttributes = JSON.parse($window.localStorage.getItem("mfrPartAttributes"));
                } else {
                    setAttributes = null;
                }
                if (setAttributes != null && setAttributes != undefined) {
                    angular.forEach(setAttributes, function (setAtt) {
                        vm.objectIds.push(setAtt.id);
                    })
                    ObjectTypeAttributeService.getObjectTypeAttributesByIds(vm.objectIds).then(
                        function (data) {
                            if (data.length == 0) {
                                setAttributes = null;
                                $window.localStorage.setItem("mfrPartAttributes", "");
                                vm.selectedMfrPartAttributes = setAttributes
                            } else {
                                vm.selectedMfrPartAttributes = setAttributes;
                            }
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
                ObjectTypeAttributeService.getObjectTypeAttributesByType('MANUFACTURERPARTTYPE').then(
                    function (data) {
                        vm.typeAttributes = data;
                        angular.forEach(vm.typeAttributes, function (at1) {
                            vm.attributeIds.push(at1.id);
                        });
                        if (vm.mode == null || vm.mode == undefined || vm.mode == "") {
                            loadMfrParts();
                            loadMfrs();
                        } else {
                            vm.query = JSON.parse($rootScope.searchQuery);
                            $rootScope.searchQuery = null;
                            loadSavedSearch(vm.mode, vm.query);
                        }
                    });

            })();
        }
    }
)
;
