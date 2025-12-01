define(
    [
        'app/desktop/modules/change/change.module',
        'app/shared/services/core/mesObjectTypeService',
        'app/desktop/directives/all-view-attributes/allViewAttributesDirective',
        'app/shared/services/core/machineService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/specificationsService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('AllMachineController', AllMachineController);

        function AllMachineController($scope, $rootScope, $translate, $timeout, $state, $window, DialogService, $application, $stateParams, $cookies, $sce, MESObjectTypeService,
                                      ObjectTypeAttributeService, MachineService, CommonService, RecentlyVisitedService, ItemService, ECOService, WorkflowDefinitionService,
                                      AttributeAttachmentService, MfrService, MfrPartsService, ProjectService, SpecificationsService) {

            var vm = this;
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            vm.loading = false;
            vm.newMachine = newMachine;
            vm.deleteMachine = deleteMachine;
            vm.machine = [];
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;

            vm.searchText = null;
            vm.filterSearch = null;


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
                searchQuery: null,
                workOrder:''
            };
            $scope.freeTextQuery = null;

            vm.machines = angular.copy(pagedResults);


            var parsed = angular.element("<div></div>");
            var create = parsed.html($translate.instant("CREATE")).html();
            var newMachinetHeading = parsed.html($translate.instant("NEW_MACHINE_TYPE")).html();

            function newMachine() {
                var options = {
                    title: newMachinetHeading,
                    template: 'app/desktop/modules/mes/machine/new/newMachineView.jsp',
                    controller: 'NewMachineController as newMachineVm',
                    resolve: 'app/desktop/modules/mes/machine/new/newMachineController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: create, broadcast: 'app.machine.new'}
                    ],
                    callback: function (machine) {
                        $timeout(function () {
                            loadMachines();
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            $scope.operationFilePopover = {
                templateUrl: 'app/desktop/modules/mes/machine/all/machineFilePopoverTemplate.jsp'
            };

            function nextPage() {
                if (vm.machines.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadMachines();
                }
            }

            function previousPage() {
                if (vm.machines.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadMachines();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.size = page;
                vm.pageable.page = 0;
                loadMachines();
            }

            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.filters.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    loadMachines();
                } else {
                    resetPage();
                }
            }

            function resetPage() {
                vm.machines = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                $rootScope.showBusyIndicator($('.view-container'));
                loadMachines();
            }

            function loadMachines() {
                vm.loading = true;
                MachineService.getAllMachines(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.machines = data;
                        angular.forEach(vm.machines.content, function (machine) {
                            machine.modifiedDatede = null;
                            if (machine.modifiedDate != null) {
                                machine.modifiedDatede = moment(machine.modifiedDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                            }
                            if (machine.image != null) {
                                machine.imagePath = "api/mes/machines/" + machine.id + "/image/download?" + new Date().getTime();
                            }
                        });
                        CommonService.getPersonReferences(vm.machines.content, 'modifiedBy');
                        loadAttributeValues();
                        $rootScope.hideBusyIndicator();
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }


            vm.objectIds = [];
            vm.selectedAttributes = [];
            function loadAttributeValues() {
                vm.objectIds = [];
                vm.attributeIds = [];
                angular.forEach(vm.machines.content, function (item) {
                    vm.objectIds.push(item.id);
                });
                angular.forEach(vm.selectedAttributes, function (selectedAttribute) {
                    if (selectedAttribute.id != null && selectedAttribute.id != "" && selectedAttribute.id != 0) {
                        vm.attributeIds.push(selectedAttribute.id);
                    }
                });
                $rootScope.getObjectAttributeValues(vm.objectIds, vm.attributeIds, vm.selectedAttributes, vm.machines.content);
            }


            vm.showMachine = showMachine;
            function showMachine(machine) {
                vm.recentlyVisited = {};
                vm.recentlyVisited.objectId = machine.id;
                vm.recentlyVisited.objectType = machine.objectType;
                vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                    function (data) {
                        $state.go('app.mes.masterData.machine.details', {machineId: machine.id, tab: 'details.basic'});
                    }, function (error) {
                        $state.go('app.mes.masterData.machine.details', {machineId: machine.id, tab: 'details.basic'});
                    }
                )
            }


            vm.showWorkCenter = showWorkCenter;
            function showWorkCenter(machine) {
                vm.recentlyVisited = {};
                vm.recentlyVisited.objectId = machine.workCenterId;
                vm.recentlyVisited.objectType = 'WORKCENTER';
                vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                    function (data) {
                        $state.go('app.mes.masterData.workcenter.details', {
                            workcenterId: machine.workCenterId,
                            tab: 'details.basic'
                        });
                    }, function (error) {
                        $state.go('app.mes.masterData.workcenter.details', {
                            workcenterId: machine.workCenterId,
                            tab: 'details.basic'
                        });
                    }
                )
            }


            var deleteMachineTitle = parsed.html($translate.instant("DELETE_MACHINE")).html();
            var deleteDialogMessage = parsed.html($translate.instant("DELETE_TYPE_DIALOG_MESSAGE")).html();
            var MachineDeleteMsg = parsed.html($translate.instant("MACHINE_DELETE_MSG")).html();


            function deleteMachine(machine) {
                var options = {
                    title: deleteMachineTitle,
                    message: deleteDialogMessage + " [ " + machine.number + " ] " + "?",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        if (machine.id != null && machine.id != undefined) {
                            MachineService.deleteMachine(machine.id).then(
                                function (data) {
                                    $rootScope.showSuccessMessage(MachineDeleteMsg);
                                    loadMachines();
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    }
                });
            }

            var currencyMap = new Hashtable();
            var plantsAttributeTitle = parsed.html($translate.instant("ATTRIBUTES")).html();
            var selectedAttributesAdded = parsed.html($translate.instant("SELECTED_ATTRIBUTES_MESSAGE")).html();
            var addButton = parsed.html($translate.instant("ADD")).html();

            vm.showTypeAttributes = showTypeAttributes;
            function showTypeAttributes() {
                var options = {
                    title: plantsAttributeTitle,
                    template: 'app/desktop/modules/shared/attributes/attributesView.jsp',
                    resolve: 'app/desktop/modules/shared/attributes/attributesController',
                    controller: 'AttributesController as attributesVm',
                    width: 500,
                    showMask: true,
                    data: {
                        selectedAttributes: vm.selectedAttributes,
                        type: "MACHINETYPE",
                        objectType: "MACHINE"
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.select.attributes'}
                    ],
                    callback: function (result) {
                        vm.selectedAttributes = result;
                        $window.localStorage.setItem("machineAttributes", JSON.stringify(vm.selectedAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesAdded);
                        }
                        loadMachines();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.removeAttribute = removeAttribute;
            function removeAttribute(att) {
                vm.selectedAttributes.remove(att);
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("machineAttributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            vm.showImage = showImage;
            function showImage(machine) {
                var modal = document.getElementById('item-thumbnail' + machine.id);
                modal.style.display = "block";

                var span = document.getElementById("thumbnail-close" + machine.id);
                $("#thumbnail-image" + machine.id).width($('#thumbnail-view' + machine.id).outerWidth());
                $("#thumbnail-image" + machine.id).height($('#thumbnail-view' + machine.id).outerHeight());

                span.onclick = function () {
                    modal.style.display = "none";
                }
                $scope.$evalAsync();
            }

            (function () {
                angular.forEach($application.currencies, function (data) {
                    currencyMap.put(data.id, $sce.trustAsHtml(data.symbol));
                })
                var setAttributes = null;
                if (validateJSON()) {
                    setAttributes = JSON.parse($window.localStorage.getItem("machineAttributes"));
                } else {
                    setAttributes = null;
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
                                $window.localStorage.setItem("machineAttributes", "");
                                vm.selectedAttributes = setAttributes
                            } else {
                                vm.selectedAttributes = setAttributes;
                            }
                            loadMachines();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    loadMachines();
                }
            })();

        }
    }
);