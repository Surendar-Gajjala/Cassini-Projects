define(
    [
        'app/desktop/modules/change/change.module',
        'app/shared/services/core/equipmentService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/workflowDefinitionService',
        'app/shared/services/core/projectService',
        'app/shared/services/core/specificationsService',
        'app/desktop/directives/all-view-attributes/allViewAttributesDirective',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('AllEquipmentController', AllEquipmentController);

        function AllEquipmentController($scope, $rootScope, $translate, $timeout, $state, $window, DialogService, $application, $stateParams, $cookies, $sce, EquipmentService, ObjectTypeAttributeService,
                                        ItemService, ECOService, WorkflowDefinitionService, MfrService, MfrPartsService, AttributeAttachmentService, CommonService, ProjectService, SpecificationsService,
                                        RecentlyVisitedService) {

            var vm = this;
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            vm.loading = false;
            vm.newEquipment = newEquipment;
            vm.deleteEquipment = deleteEquipment;
            vm.equipment = [];
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
                workOrder: ''
            };
            $scope.freeTextQuery = null;

            vm.equipments = angular.copy(pagedResults);


            var parsed = angular.element("<div></div>");
            var create = parsed.html($translate.instant("CREATE")).html();
            var newEquipmentHeading = parsed.html($translate.instant("NEW_EQUIPMENT_TYPE")).html();
            $scope.newEquipment = parsed.html($translate.instant("NEW_EQUIPMENT_TYPE")).html();

            function newEquipment() {
                var options = {
                    title: newEquipmentHeading,
                    template: 'app/desktop/modules/mes/equipment/new/newEquipmentView.jsp',
                    controller: 'NewEquipmentController as newEquipmentVm',
                    resolve: 'app/desktop/modules/mes/equipment/new/newEquipmentController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: create, broadcast: 'app.equipment.new'}
                    ],
                    callback: function (equipment) {
                        $timeout(function () {
                            loadEquipments();
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function nextPage() {
                if (vm.equipments.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadEquipments();
                }
            }

            $scope.operationFilePopover = {
                templateUrl: 'app/desktop/modules/mes/equipment/all/equipmentFilePopoverTemplate.jsp'
            };

            function previousPage() {
                if (vm.equipments.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadEquipments();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.size = page;
                vm.pageable.page = 0;
                loadEquipments();
            }

            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.filters.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    loadEquipments();
                } else {
                    resetPage();
                }
            }

            function resetPage() {
                vm.equipments = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                $rootScope.showBusyIndicator($('.view-container'));
                loadEquipments();
            }

            function loadEquipments() {
                vm.loading = true;
                EquipmentService.getAllEquipments(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.equipments = data;
                        angular.forEach(vm.equipments.content, function (equipment) {
                            equipment.modifiedDatede = null;
                            if (equipment.modifiedDate != null) {
                                equipment.modifiedDatede = moment(equipment.modifiedDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                            }
                            if (equipment.image != null) {
                                equipment.imagePath = "api/mes/equipments/" + equipment.id + "/image/download?" + new Date().getTime();
                            }
                        });
                        CommonService.getPersonReferences(vm.equipments.content, 'modifiedBy');
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
                angular.forEach(vm.equipments.content, function (item) {
                    vm.objectIds.push(item.id);
                });
                angular.forEach(vm.selectedAttributes, function (selectedAttribute) {
                    if (selectedAttribute.id != null && selectedAttribute.id != "" && selectedAttribute.id != 0) {
                        vm.attributeIds.push(selectedAttribute.id);
                    }
                });
                $rootScope.getObjectAttributeValues(vm.objectIds, vm.attributeIds, vm.selectedAttributes, vm.equipments.content);
            }


            vm.showEquipment = showEquipment;
            function showEquipment(equipment) {
                vm.recentlyVisited = {};
                vm.recentlyVisited.objectId = equipment.id;
                vm.recentlyVisited.objectType = equipment.objectType;
                vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                    function (data) {
                        $state.go('app.mes.masterData.equipment.details', {
                            equipmentId: equipment.id,
                            tab: 'details.basic'
                        });
                    }, function (error) {
                        $state.go('app.mes.masterData.equipment.details', {
                            equipmentId: equipment.id,
                            tab: 'details.basic'
                        });
                    }
                )
            }

            var deleteEquipmentt = parsed.html($translate.instant("DELETE_EQUIPMENT")).html();
            var deleteDialogMessage = parsed.html($translate.instant("DELETE_TYPE_DIALOG_MESSAGE")).html();
            var EquipmentDeleteMsg = parsed.html($translate.instant("EQUIPMENT_DELETE_MSG")).html();


            function deleteEquipment(equipment) {
                var options = {
                    title: deleteEquipmentt,
                    message: deleteDialogMessage + " [ " + equipment.name + " ] " + "?",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        if (equipment.id != null && equipment.id != undefined) {
                            EquipmentService.deleteEquipment(equipment.id).then(
                                function (data) {
                                    $rootScope.showSuccessMessage(EquipmentDeleteMsg);
                                    loadEquipments();
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
                        type: "EQUIPMENTTYPE",
                        objectType: "EQUIPMENT"
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.select.attributes'}
                    ],
                    callback: function (result) {
                        vm.selectedAttributes = result;
                        $window.localStorage.setItem("equipmentAttributes", JSON.stringify(vm.selectedAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesAdded);
                        }
                        loadEquipments();
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
                    JSON.parse($window.localStorage.getItem("equipmentAttributes"));
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
                    setAttributes = JSON.parse($window.localStorage.getItem("equipmentAttributes"));
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
                                $window.localStorage.setItem("equipmentAttributes", "");
                                vm.selectedAttributes = setAttributes
                            } else {
                                vm.selectedAttributes = setAttributes;
                            }
                            loadEquipments();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                } else {
                    loadEquipments();
                }
            })();

        }
    }
);