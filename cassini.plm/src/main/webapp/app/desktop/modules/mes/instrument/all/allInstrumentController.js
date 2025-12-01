define(
    [
        'app/desktop/modules/change/change.module',
        'app/shared/services/core/mesObjectTypeService',
        'app/desktop/directives/all-view-attributes/allViewAttributesDirective',
        'app/shared/services/core/instrumentService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('AllInstrumentController', AllInstrumentController);

        function AllInstrumentController($scope, $rootScope, $translate, $timeout, $state, $window, DialogService, $application, $stateParams, $cookies, $sce, MESObjectTypeService,
                                         ObjectTypeAttributeService, InstrumentService, CommonService, RecentlyVisitedService, ItemService, ECOService, WorkflowDefinitionService,
                                         AttributeAttachmentService, MfrService, MfrPartsService, ProjectService, SpecificationsService) {

            var vm = this;
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            vm.loading = false;
            vm.newInstrument = newInstrument;
            vm.deleteInstrument = deleteInstrument;
            vm.instrument = [];
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

            vm.instruments = angular.copy(pagedResults);


            var parsed = angular.element("<div></div>");
            var create = parsed.html($translate.instant("CREATE")).html();
            var newInstrumentHeading = parsed.html($translate.instant("NEW_INSTRUMENT_TYPE")).html();
            $scope.newInstrument = parsed.html($translate.instant("NEW_INSTRUMENT_TYPE")).html();

            function newInstrument() {
                var options = {
                    title: newInstrumentHeading,
                    template: 'app/desktop/modules/mes/instrument/new/newInstrumentView.jsp',
                    controller: 'NewInstrumentController as newInstrumentVm',
                    resolve: 'app/desktop/modules/mes/instrument/new/newInstrumentController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: create, broadcast: 'app.instrument.new'}
                    ],
                    callback: function (instrument) {
                        $timeout(function () {
                            loadInstruments();
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            $scope.operationFilePopover = {
                templateUrl: 'app/desktop/modules/mes/instrument/all/instrumentFilePopoverTemplate.jsp'
            };

            function nextPage() {
                if (vm.instruments.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadInstruments();
                }
            }

            function previousPage() {
                if (vm.instruments.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadInstruments();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.size = page;
                vm.pageable.page = 0;
                loadInstruments();
            }

            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.filters.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    loadInstruments();
                } else {
                    resetPage();
                }
            }

            function resetPage() {
                vm.instruments = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                $rootScope.showBusyIndicator($('.view-container'));
                loadInstruments();
            }

            function loadInstruments() {
                vm.loading = true;
                InstrumentService.getAllInstruments(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.instruments = data;
                        angular.forEach(vm.instruments.content, function (instrument) {
                            instrument.modifiedDatede = null;
                            if (instrument.modifiedDate != null) {
                                instrument.modifiedDatede = moment(instrument.modifiedDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                            }
                            if (instrument.image != null) {
                                instrument.imagePath = "api/mes/instruments/" + instrument.id + "/image/download?" + new Date().getTime();
                            }
                        });
                        CommonService.getPersonReferences(vm.instruments.content, 'modifiedBy');
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
                angular.forEach(vm.instruments.content, function (item) {
                    vm.objectIds.push(item.id);
                });
                angular.forEach(vm.selectedAttributes, function (selectedAttribute) {
                    if (selectedAttribute.id != null && selectedAttribute.id != "" && selectedAttribute.id != 0) {
                        vm.attributeIds.push(selectedAttribute.id);
                    }
                });
                $rootScope.getObjectAttributeValues(vm.objectIds, vm.attributeIds, vm.selectedAttributes, vm.instruments.content);
            }


            vm.showInstrument = showInstrument;
            function showInstrument(instrument) {
                vm.recentlyVisited = {};
                vm.recentlyVisited.objectId = instrument.id;
                vm.recentlyVisited.objectType = instrument.objectType;
                vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                    function (data) {
                        $state.go('app.mes.masterData.instrument.details', {
                            instrumentId: instrument.id,
                            tab: 'details.basic'
                        });
                    }, function (error) {
                        $state.go('app.mes.masterData.instrument.details', {
                            instrumentId: instrument.id,
                            tab: 'details.basic'
                        });
                    }
                )
            }


            var deleteInstrumentTitle = parsed.html($translate.instant("DELETE_INSTRUMENT")).html();
            var deleteDialogMessage = parsed.html($translate.instant("DELETE_TYPE_DIALOG_MESSAGE")).html();
            var InstrumentDeleteMsg = parsed.html($translate.instant("INSTRUMENT_DELETE_MSG")).html();


            function deleteInstrument(instrument) {
                var options = {
                    title: deleteInstrumentTitle,
                    message: deleteDialogMessage + " [ " + instrument.name + " ] " + "?",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        if (instrument.id != null && instrument.id != undefined) {
                            InstrumentService.deleteInstrument(instrument.id).then(
                                function (data) {
                                    $rootScope.showSuccessMessage(InstrumentDeleteMsg);
                                    loadInstruments();
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
                        type: "INSTRUMENTTYPE",
                        objectType: "INSTRUMENT"
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.select.attributes'}
                    ],
                    callback: function (result) {
                        vm.selectedAttributes = result;
                        $window.localStorage.setItem("instrumentAttributes", JSON.stringify(vm.selectedAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesAdded);
                        }
                        loadInstruments();
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
                    JSON.parse($window.localStorage.getItem("instrumentAttributes"));
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
                    setAttributes = JSON.parse($window.localStorage.getItem("instrumentAttributes"));
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
                                $window.localStorage.setItem("instrumentAttributes", "");
                                vm.selectedAttributes = setAttributes
                            } else {
                                vm.selectedAttributes = setAttributes;
                            }
                            loadInstruments();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                } else {
                    loadInstruments();
                }
            })();
        }
    }
);