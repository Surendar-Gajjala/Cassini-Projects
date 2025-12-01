define(
    [
        'app/desktop/modules/mro/mro.module',
        'app/shared/services/core/meterService',
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
        module.controller('AllMetersController', AllMetersController);

        function AllMetersController($scope, $rootScope, $translate, $timeout, $state, $window, DialogService, $application, $stateParams, $cookies, $sce, MeterService, ObjectTypeAttributeService,
                                     ItemService, ECOService, WorkflowDefinitionService, MfrService, MfrPartsService, AttributeAttachmentService, CommonService, ProjectService, SpecificationsService,
                                     RecentlyVisitedService) {

            var vm = this;
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            vm.loading = false;
            vm.newMeter = newMeter;
            vm.deleteMeter = deleteMeter;
            vm.Meter = [];
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
                asset: ''
            };
            $scope.freeTextQuery = null;

            vm.meters = angular.copy(pagedResults);

            var parsed = angular.element("<div></div>");
            var create = parsed.html($translate.instant("CREATE")).html();
            var newMeterHeading = parsed.html($translate.instant("NEW_METER")).html();
            $scope.cannotDeleteAddedMeter = parsed.html($translate.instant("CANNOT_DELETE_ADDED_METER")).html();
            $scope.newMeter = parsed.html($translate.instant("NEW_METER")).html();

            function newMeter() {
                var options = {
                    title: newMeterHeading,
                    template: 'app/desktop/modules/mro/meter/new/newMeterView.jsp',
                    controller: 'NewMeterController as newMeterVm',
                    resolve: 'app/desktop/modules/mro/meter/new/newMeterController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: create, broadcast: 'app.meter.new'}
                    ],
                    callback: function (meter) {
                        $timeout(function () {
                            loadMeters();
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function nextPage() {
                if (vm.meters.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadMeters();
                }
            }

            function previousPage() {
                if (vm.meters.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadMeters();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.size = page;
                vm.pageable.page = 0;
                loadMeters();
            }

            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.filters.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    loadMeters();
                } else {
                    resetPage();
                }
            }

            function resetPage() {
                vm.meters = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                $rootScope.showBusyIndicator($('.view-container'));
                loadMeters();
            }

            function loadMeters() {
                vm.loading = true;
                MeterService.getAllMeters(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.meters = data;
                        angular.forEach(vm.meters.content, function (meter) {
                            meter.modifiedDatede = null;
                            if (meter.modifiedDate != null) {
                                meter.modifiedDatede = moment(meter.modifiedDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                            }
                        });
                        CommonService.getPersonReferences(vm.meters.content, 'modifiedBy');
                        loadAttributeValues();
                        $rootScope.hideBusyIndicator();
                        vm.loading = false;
                    },
                    function (error) {
                        vm.loading = false;
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.objectIds = [];
            vm.selectedAttributes = [];
            function loadAttributeValues() {
                vm.objectIds = [];
                vm.attributeIds = [];
                angular.forEach(vm.meters.content, function (item) {
                    vm.objectIds.push(item.id);
                });
                angular.forEach(vm.selectedAttributes, function (selectedAttribute) {
                    if (selectedAttribute.id != null && selectedAttribute.id != "" && selectedAttribute.id != 0) {
                        vm.attributeIds.push(selectedAttribute.id);
                    }
                });
                $rootScope.getObjectAttributeValues(vm.objectIds, vm.attributeIds, vm.selectedAttributes, vm.meters.content);

            }


            vm.showMeter = showMeter;
            function showMeter(meter) {
                $state.go('app.mro.meter.details', {meterId: meter.id, tab: 'details.basic'});
                /*vm.recentlyVisited = {};
                 vm.recentlyVisited.objectId = meter.id;
                 vm.recentlyVisited.objectType = meter.objectType;
                 vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                 RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                 function (data) {
                 $state.go('app.mes.meter.details', {meterId: meter.id, tab: 'details.basic'});
                 }, function (error) {
                 $state.go('app.mes.meter.details', {meterId: meter.id, tab: 'details.basic'});
                 }
                 )*/
            }

            var deleteMeterTitle = parsed.html($translate.instant("DELETE_METER")).html();
            var deleteDialogMessage = parsed.html($translate.instant("DELETE_TYPE_DIALOG_MESSAGE")).html();
            var meterDeleteMsg = parsed.html($translate.instant("METER_DELETE_MSG")).html();


            function deleteMeter(meter) {
                var options = {
                    title: deleteMeterTitle,
                    message: deleteDialogMessage + " [ " + meter.name + " ] " + "?",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        if (meter.id != null && meter.id != undefined) {
                            MeterService.deleteMeter(meter.id).then(
                                function (data) {
                                    $rootScope.showSuccessMessage(meterDeleteMsg);
                                    loadMeters();
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
            var attributeTitle = parsed.html($translate.instant("ATTRIBUTES")).html();
            var selectedAttributesAdded = parsed.html($translate.instant("SELECTED_ATTRIBUTES_MESSAGE")).html();
            var addButton = parsed.html($translate.instant("ADD")).html();

            vm.showTypeAttributes = showTypeAttributes;
            function showTypeAttributes() {
                var options = {
                    title: attributeTitle,
                    template: 'app/desktop/modules/shared/attributes/attributesView.jsp',
                    resolve: 'app/desktop/modules/shared/attributes/attributesController',
                    controller: 'AttributesController as attributesVm',
                    width: 500,
                    showMask: true,
                    data: {
                        selectedAttributes: vm.selectedAttributes,
                        type: "METERTYPE",
                        objectType: "MROMETER"
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.select.attributes'}
                    ],
                    callback: function (result) {
                        vm.selectedAttributes = result;
                        $window.localStorage.setItem("meterAttributes", JSON.stringify(vm.selectedAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesAdded);
                        }
                        loadMeters();
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
                    JSON.parse($window.localStorage.getItem("meterAttributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            (function () {
                angular.forEach($application.currencies, function (data) {
                    currencyMap.put(data.id, $sce.trustAsHtml(data.symbol));
                })
                var setAttributes = null;
                if (validateJSON()) {
                    setAttributes = JSON.parse($window.localStorage.getItem("meterAttributes"));
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
                                $window.localStorage.setItem("meterAttributes", "");
                                vm.selectedAttributes = setAttributes
                            } else {
                                vm.selectedAttributes = setAttributes;
                            }
                            loadMeters();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    loadMeters();
                }
            })();

        }
    }
);