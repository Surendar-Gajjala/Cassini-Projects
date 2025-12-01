define(
    [
        'app/desktop/modules/request/request.module',
        'split-pane',
        'app/desktop/modules/bom/directive/bomTreeDirective',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/bomService',
        'app/shared/services/core/requestService'
    ],
    function (module) {
        module.controller('NewRequestController', NewRequestController);

        function NewRequestController($scope, $rootScope, $translate, $window, $timeout, $application, $state,
                                      $stateParams, $filter, CommonService, DialogService, BomService, RequestService) {
            if ($application.homeLoaded == false) {
                return;
            }

            $rootScope.viewInfo.icon = "fa flaticon-stamp13";
            $rootScope.viewInfo.title = "New Request";

            var vm = this;

            vm.loading = true;
            vm.clear = false;
            vm.showSearchBox = false;

            vm.requisitionItems = [];

            vm.back = back;
            vm.onSelectType = onSelectType;
            vm.preventClick = preventClick;
            vm.showRequisitionInfoPanel = showRequisitionInfoPanel;
            vm.performSearch = performSearch;
            vm.addItemToRequisition = addItemToRequisition;
            vm.removeReqItem = removeReqItem;
            vm.createRequisition = createRequisition;
            vm.validateQuantity = validateQuantity;
            vm.onSelectBom = onSelectBom;
            vm.onSelectInstance = onSelectInstance;
            $scope.unitsTitle = "Select Unit";

            vm.searchFilter = {
                searchQuery: null,
                bom: '',
                section: '',
                subsystem: '',
                unit: ''
            };

            var pageable = {
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
                size: pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };

            vm.searchResults = angular.copy(pagedResults);

            vm.requisition = {
                id: null,
                reqNumber: null,
                bomInstance: null,
                requestedBy: null,
                requestedDate: null,
                status: null,
                notes: null,
                requestItems: [],
                section: null,
                subsystem: null,
                unit: null,
                versity: false
            };

            vm.emptyRequisitionItem = {
                id: null,
                request: null,
                item: null,
                quantity: 0,
                fractionalQuantity: 0.0,
                failureQty: 0,
                fractionalFailureQty: 0.0,
                replaceQty: 0,
                fractionalReplaceQty: 0.0
            };

            vm.selectedBom = null;
            vm.bomInstances = [];
            vm.bomInstanceTitle = "Select Instance";
            vm.selectBomTitle = "Select BOM";

            function back() {
                window.history.back();
            }

            vm.lastSelectedInstance = null;

            function onSelectType(bom) {
                if (bom != undefined) {
                    if (bom.objectType == "BOM") {
                        $rootScope.showWarningMessage("Invalid selection.Please select instance");
                    } else {
                        if (vm.lastSelectedInstance != null && vm.lastSelectedInstance.id != bom.id) {
                            vm.requisitionItems = [];
                        }
                        vm.requisition.bomInstance = bom;
                        vm.lastSelectedInstance = bom;
                        vm.showSearchBox = true;
                    }
                }
            }

            function preventClick(event) {
                event.stopPropagation();
                event.preventDefault();
            }

            function showRequisitionInfoPanel(show) {
                if (show) {
                    $('#inwardInfoPanel').show('slide', {direction: 'left'}, 600);
                    vm.sidePanel = true;
                }
                else {
                    $('#inwardInfoPanel').hide('slide', {direction: 'left'}, 600);
                    vm.sidePanel = false;
                }
            }

            function initSearchBox() {
                $(document).click(function () {
                    $('#newRequestSearchResults').hide();
                });
                $(document).on('keydown', function (evt) {
                    if (evt.keyCode == 27) {
                        $('#newRequestSearchResults').hide();
                    }
                });

                $timeout(function () {
                    $('#newRequestSearchResults').click(function (event) {
                        event.stopPropagation();
                        event.preventDefault();
                    });
                }, 1000);
            }

            vm.previousPage = previousPage;
            vm.nextPage = nextPage;

            function previousPage() {
                pageable.page--;
                searchRequestItems();
            }

            function nextPage() {
                pageable.page++;
                searchRequestItems();
            }

            function performSearch() {
                if (vm.searchFilter.searchQuery == "") {
                    $('#newRequestSearchResults').hide();
                }
                if (vm.searchFilter.searchQuery != "") {
                    if (vm.searchFilter.searchQuery.length > 2) {
                        vm.searchResults.content = [];
                        $('#newRequestSearchResults').show();
                        var height = $('.view-content').outerHeight();
                        $('#newRequestSearchResults').height(height);
                        showRequisitionInfoPanel(false);
                        pageable.page = 0;
                        vm.searchFilter.bom = vm.requisition.bomInstance.id;
                        searchRequestItems();
                    }
                }
            }

            function checkIfAlreadyAdded(item) {
                var found = false;
                angular.forEach(vm.requisitionItems, function (reqItem) {
                    if (reqItem.item.id == item.id) {
                        found = true;
                    }
                });

                return found;
            }

            function searchRequestItems() {
                $rootScope.showBusyIndicator($("#newRequestSearchResults"));
                BomService.searchBomInstanceItemsBySection(vm.searchFilter, pageable).then(
                    function (data) {
                        vm.searchResultsData = angular.copy(data);
                        vm.searchResults = angular.copy(data);
                        vm.searchResults.content = [];
                        angular.forEach(vm.searchResultsData.content, function (item) {
                            if (!checkIfAlreadyAdded(item)) {
                                vm.searchResults.content.push(item);
                            }
                        });
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function addItemToRequisition(item) {
                var newRequisitionItem = angular.copy(vm.emptyRequisitionItem);
                newRequisitionItem.item = item;

                vm.searchResults.content.splice(vm.searchResults.content.indexOf(item), 1);

                vm.requisitionItems.push(newRequisitionItem);
            }

            function removeReqItem(item) {
                vm.requisitionItems.splice(vm.requisitionItems.indexOf(item), 1);
            }

            function createRequisition() {
                if (validate() && validateRequisitionItems()) {
                    $rootScope.showBusyIndicator($("#newRequestView"));
                    vm.requisition.requestItems = vm.requisitionItems;
                    vm.requisition.unitIds = vm.unitIds;
                    vm.requisition.versity = vm.selectedSection.typeRef.versity;
                    RequestService.createRequest(vm.requisition).then(
                        function (data) {
                            RequestService.createRequestItems(data.id, vm.requisitionItems).then(
                                function (data) {
                                    $rootScope.hideBusyIndicator();
                                    $state.go('app.requests.all');
                                    $rootScope.showSuccessMessage("Request created successfully");
                                }, function (error) {
                                    $rootScope.hideBusyIndicator();
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )

                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function validate() {
                var valid = true;
                if (vm.selectedBom == null || vm.selectedBom == "" || vm.selectedBom == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please select BOM");
                } else if (vm.requisition.bomInstance == null || vm.requisition.bomInstance == "" || vm.requisition.bomInstance == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please select Instance");
                }
                else if (vm.selectedSection == null || vm.selectedSection == "" || vm.selectedSection == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please select Section");
                }

                return valid;
            }

            function validateRequisitionItems() {
                var valid = true;

                if (vm.requisitionItems.length === 0) {
                    valid = false;
                    $rootScope.showWarningMessage("At least one item should be in request");
                } else {
                    vm.validItems = [];
                    angular.forEach(vm.requisitionItems, function (reqItem) {
                        if (valid) {
                            if (reqItem.item.item.itemMaster.itemType.hasLots) {

                                if (reqItem.item.canRequestMore > 0 && (reqItem.fractionalQuantity == 0 || reqItem.fractionalQuantity == null || reqItem.fractionalQuantity == "" || reqItem.fractionalQuantity == undefined)) {
                                    valid = false;
                                    $rootScope.showWarningMessage("Please enter request quantity for : " + reqItem.item.item.itemMaster.itemName);

                                } else if (reqItem.item.canRequestMore > 0 && (reqItem.item.item.itemMaster.itemType.hasLots && (reqItem.fractionalQuantity < 0))) {
                                    valid = false;
                                    $rootScope.showWarningMessage("Please enter positive request quantity for : " + reqItem.item.item.itemMaster.itemName);

                                } else if (reqItem.item.canRequestMore > 0 && reqItem.item.item.itemMaster.itemType.hasLots && (reqItem.fractionalQuantity > 0
                                    && reqItem.fractionalQuantity > reqItem.item.canRequestMore)) {
                                    valid = false;
                                    $rootScope.showWarningMessage("Request quantity cannot be greater than  " + reqItem.item.canRequestMore + " for " + reqItem.item.item.itemMaster.itemName);

                                } else if (reqItem.item.canRequestMore > 0) {
                                    vm.validItems.push(reqItem);
                                }

                            } else {

                                if (reqItem.item.canRequestMore > 0 && (reqItem.quantity == 0 || reqItem.quantity == null || reqItem.quantity == "" || reqItem.quantity == undefined)) {
                                    valid = false;
                                    $rootScope.showWarningMessage("Please enter request quantity for : " + reqItem.item.item.itemMaster.itemName);
                                } else if (reqItem.item.canRequestMore > 0 && !reqItem.item.item.itemMaster.itemType.hasLots && (reqItem.quantity < 0)) {
                                    valid = false;
                                    $rootScope.showWarningMessage("Please enter positive request quantity for : " + reqItem.item.item.itemMaster.itemName);

                                } else if (reqItem.item.canRequestMore > 0 && !reqItem.item.item.itemMaster.itemType.hasLots && (reqItem.quantity > 0
                                    && reqItem.quantity > reqItem.item.canRequestMore)) {
                                    valid = false;
                                    $rootScope.showWarningMessage("Request quantity cannot be greater than " + reqItem.item.canRequestMore + " for : " + reqItem.item.item.itemMaster.itemName);

                                } /*else if ((reqItem.item.requestedQuantity + reqItem.quantity) > reqItem.item.quantity) {
                                 valid = false;
                                 $rootScope.showWarningMessage(reqItem.item.item.itemMaster.itemName + " Request Quantity cannot be greater than "
                                 + (reqItem.item.quantity - reqItem.item.requestedQuantity));
                                 }*/ else if (reqItem.item.canRequestMore > 0) {
                                    vm.validItems.push(reqItem);
                                }

                            }
                        }
                    })
                }

                if (valid && vm.validItems.length == 0) {
                    valid = false;
                    $rootScope.showWarningMessage("At least one item should be in request");
                }

                return valid;
            }

            function validateQuantity(reqItem) {
                if (reqItem.item.item.itemMaster.itemType.hasLots) {
                    if (reqItem.fractionalQuantity == undefined || reqItem.fractionalQuantity < 0) {
                        reqItem.fractionalQuantity = 0;
                        $rootScope.showWarningMessage("Please enter positive quantity");
                    } else {
                        if ((reqItem.item.fractionalRequestedQuantity + reqItem.fractionalQuantity) > reqItem.item.fractionalQuantity) {
                            $rootScope.showWarningMessage("Request Quantity cannot be greater than "
                                + (reqItem.item.fractionalQuantity - reqItem.item.fractionalRequestedQuantity));
                            reqItem.fractionalQuantity = 0;
                        }
                    }

                } else {
                    if (reqItem.quantity == undefined || reqItem.quantity < 0) {
                        reqItem.quantity = 0;
                        $rootScope.showWarningMessage("Please enter positive quantity");
                    } else {
                        if ((reqItem.item.requestedQuantity + reqItem.quantity) > reqItem.item.quantity) {
                            $rootScope.showWarningMessage("Request Quantity cannot be greater than "
                                + (reqItem.item.quantity - reqItem.item.requestedQuantity));
                            reqItem.quantity = 0;
                        }
                    }
                }
            }

            function loadBoms() {
                BomService.getAllBoms().then(
                    function (data) {
                        vm.boms = data;
                        if (vm.boms.length == 0) {
                            vm.selectBomTitle = "No BOM";
                        }
                    }
                )
            }

            function onSelectBom(bom) {
                BomService.getBomInstances(bom.id).then(
                    function (data) {
                        vm.bomInstances = data;
                        vm.bomInstances = $filter('orderBy')(vm.bomInstances, 'item.instanceName');
                        if (vm.bomInstances.length == 0) {
                            vm.bomInstanceTitle = "No Instances";
                        } else {
                            vm.bomInstanceTitle = "Select Instance";
                        }
                        vm.requisition.bomInstance = null;
                        vm.showSearchBox = false;
                        vm.requisitionItems = [];
                        $scope.$evalAsync();
                    }
                )
            }

            function onSelectInstance(bomInstance) {
                $rootScope.showBusyIndicator($('.view-container'));
                vm.sectionItems = [];
                vm.requisitionItems = [];
                vm.selectedSubsystem = null;
                vm.selectedUnit = null;
                vm.searchFilter.subsystem = '';
                vm.searchFilter.unit = '';
                vm.requisition.subsystem = null;
                vm.requisition.unit = null;
                vm.searchFilter.section = '';
                vm.searchFilter.subsystem = '';
                vm.searchFilter.unit = '';
                vm.requisition.section = null;
                vm.requisition.subsystem = null;
                vm.requisition.unit = null;
                vm.selectedSection = null;
                vm.selectedSubsystem = null;
                vm.selectedUnit = null;

                var admin = $rootScope.hasPermission('permission.admin.all');
                var versity = $rootScope.hasPermission('permission.versity.group');

                BomService.getSectionsByInstance(vm.requisition.bomInstance.id, admin, versity).then(
                    function (data) {
                        vm.instanceSections = data;
                        angular.forEach(vm.instanceSections, function (item) {
                            item.selected = false;
                        });
                        vm.showSearchBox = false;
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.closeRequestSectionsView = closeRequestSectionsView;
            vm.showRequestSectionsView = showRequestSectionsView;

            function showRequestSectionsView() {
                var modal = document.getElementById("requestSection-view");
                modal.style.display = "block";
            }

            function closeRequestSectionsView() {
                var modal = document.getElementById("requestSection-view");
                modal.style.display = "none";
                vm.showSearchBox = true;
            }

            vm.getSections = getSections;
            function getSections() {
                var admin = $rootScope.hasPermission('permission.admin.all');
                var versity = $rootScope.hasPermission('permission.versity.group');

                BomService.getSectionsByInstance(vm.requisition.bomInstance.id, admin, versity).then(
                    function (data) {
                        vm.instanceSections = data;
                        angular.forEach(vm.instanceSections, function (item) {
                            item.selected = false;
                        });
                        showRequestSectionsView();
                        showRequisitionInfoPanel(false);
                        vm.showSearchBox = false;
                    }
                )
            }

            vm.removeAll = removeAll;
            function removeAll() {
                vm.requisitionItems = [];
            }

            vm.sectionItems = [];
            vm.selectSection = selectSection;
            vm.selectSubsystem = selectSubsystem;
            vm.selectUnit = selectUnit;
            vm.selectedSection = null;
            vm.selectedSubsystem = null;
            vm.selectedUnit = null;
            function selectSection(instanceSection) {
                vm.sectionItems = [];
                vm.requisitionItems = [];
                vm.searchFilter.section = instanceSection.id;
                vm.selectedSection = instanceSection;
                vm.requisition.section = instanceSection.id;
                vm.selectedSubsystem = null;
                vm.selectedUnit = null;
                vm.searchFilter.subsystem = '';
                vm.searchFilter.unit = '';
                vm.requisition.subsystem = null;
                vm.requisition.unit = null;
                $rootScope.showBusyIndicator($('.view-container'));
                BomService.getReqItemsBySection(vm.requisition.bomInstance.id, instanceSection).then(
                    //BomService.getChildrenByItem(instanceSection.id).then(
                    function (data) {
                        vm.subsystems = data.children;
                        vm.sectionItems = data.bomInstanceItems;
                        vm.requisitionItems = [];
                        angular.forEach(vm.sectionItems, function (item) {
                            var newRequisitionItem = angular.copy(vm.emptyRequisitionItem);
                            newRequisitionItem.item = item;

                            vm.requisitionItems.push(newRequisitionItem);
                        });
                        vm.showSearchBox = false;
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function selectSubsystem(subsystem) {
                vm.sectionItems = [];
                vm.requisitionItems = [];
                vm.searchFilter.subsystem = subsystem.id;
                vm.selectedSubsystem = subsystem;
                vm.requisition.subsystem = subsystem.id;
                vm.selectedUnit = null;
                vm.searchFilter.unit = '';
                vm.requisition.unit = null;
                vm.showUnitsDropDown = false;
                $('#unitDropDown').hide();
                $rootScope.showBusyIndicator($('.view-container'));
                BomService.getReqItemsBySection(vm.requisition.bomInstance.id, subsystem).then(
                    function (data) {
                        vm.unitIds = [];
                        vm.units = data.children;
                        angular.forEach(vm.units, function (unit) {
                            unit.selected = false;
                        })
                        vm.sectionItems = data.bomInstanceItems;
                        vm.requisitionItems = [];
                        angular.forEach(vm.sectionItems, function (item) {
                            var newRequisitionItem = angular.copy(vm.emptyRequisitionItem);
                            newRequisitionItem.item = item;

                            vm.requisitionItems.push(newRequisitionItem);
                        });
                        vm.showSearchBox = true;
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.showUnits = showUnits;
            function showUnits() {
                vm.showUnitsDropDown = !vm.showUnitsDropDown;
                if (this.showUnitsDropDown) {
                    $('#unitDropDown').show();
                } else {
                    $('#unitDropDown').hide();
                }
            }


            vm.unitIds = [];
            function selectUnit(unit) {
                $rootScope.showBusyIndicator($('.view-content'));
                if (unit.selected) {
                    vm.unitIds.push(unit.id);
                } else {
                    vm.unitIds.splice(vm.unitIds.indexOf(unit.id), 1);
                }
                if (vm.unitIds.length > 0) {
                    loadUnitParts();
                } else {
                    selectSubsystem(vm.selectedSubsystem);
                }
            }

            function loadUnitParts() {
                vm.sectionItems = [];
                vm.requisitionItems = [];
                BomService.getReqItemsByUnits(vm.requisition.bomInstance.id, vm.unitIds).then(
                    function (data) {
                        vm.sectionItems = data.bomInstanceItems;
                        vm.requisitionItems = [];
                        angular.forEach(vm.sectionItems, function (item) {
                            var newRequisitionItem = angular.copy(vm.emptyRequisitionItem);
                            newRequisitionItem.item = item;

                            vm.requisitionItems.push(newRequisitionItem);
                        });
                        vm.showSearchBox = false;
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.addItems = addItems;
            function addItems() {
                $rootScope.showBusyIndicator();
                vm.requisitionItems = [];
                angular.forEach(vm.sectionItems, function (item) {
                    var newRequisitionItem = angular.copy(vm.emptyRequisitionItem);
                    newRequisitionItem.item = item;

                    vm.requisitionItems.push(newRequisitionItem);
                });
                $rootScope.hideBusyIndicator();

                /*var count = 0;
                 angular.forEach(selectedSectionItems, function (item) {
                 count++;
                 if (!checkIfAlreadyAdded(item)) {
                 vm.sectionItems.push(item);
                 }
                 if (count == vm.selectedSectionItems.length) {
                 if (vm.sectionItems.length > 0) {
                 angular.forEach(vm.sectionItems, function (item) {
                 var newRequisitionItem = angular.copy(vm.emptyRequisitionItem);
                 newRequisitionItem.item = item;

                 vm.requisitionItems.push(newRequisitionItem);
                 });
                 closeRequestSectionsView();
                 $rootScope.hideBusyIndicator();
                 } else {
                 $rootScope.hideBusyIndicator();
                 $rootScope.showWarningMessage("No Items in selected Section");
                 }
                 }
                 });*/
            }

            vm.unCheckAll = unCheckAll;
            function unCheckAll() {
                angular.forEach(vm.requisitionItems, function (reqItem) {
                    if (reqItem.item.item.itemMaster.itemType.hasLots) {
                        reqItem.fractionalQuantity = 0;
                    } else {
                        reqItem.quantity = 0;
                    }
                })
            }

            vm.requestAll = requestAll;
            function requestAll() {
                angular.forEach(vm.requisitionItems, function (reqItem) {
                    if (reqItem.item.item.itemMaster.itemType.hasLots) {
                        reqItem.fractionalQuantity = reqItem.item.canRequestMore;
                    } else {
                        reqItem.quantity = reqItem.item.canRequestMore;
                    }
                })
            }

            (function () {
                $scope.$on('$viewContentLoaded', function () {
                    loadBoms();
                    initSearchBox();
                    vm.sidePanel = true;
                    showRequisitionInfoPanel(vm.sidePanel)
                });
            })();
        }
    }
);