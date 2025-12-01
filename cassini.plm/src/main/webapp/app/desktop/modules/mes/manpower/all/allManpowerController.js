/**
 * Created by Hello on 10/19/2020.
 */
define(
    [
        'app/desktop/modules/change/change.module',
        'app/shared/services/core/manpowerService',
        'app/desktop/directives/all-view-attributes/allViewAttributesDirective',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('AllManpowerController', AllManpowerController);

        function AllManpowerController($scope, $rootScope, $translate, $timeout, $state, $window, DialogService, $application, $stateParams, $cookies, $sce,
                                       ManpowerService, RecentlyVisitedService, ObjectTypeAttributeService, ItemService, ECOService, WorkflowDefinitionService,
                                       AttributeAttachmentService, MfrService, MfrPartsService, ProjectService, SpecificationsService, CommonService) {

            var vm = this;
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            vm.loading = false;
            vm.newManpower = newManpower;
            vm.manpowers = [];
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
                name: null,
                type: '',
                number: null,
                searchQuery: null
            };
            $scope.freeTextQuery = null;

            vm.manpowers = angular.copy(pagedResults);

            var parsed = angular.element("<div></div>");
            var create = parsed.html($translate.instant("CREATE")).html();
            var newManpowerHeading = parsed.html($translate.instant("NEW_MANPOWER_TYPE")).html();
            $scope.newManpower = parsed.html($translate.instant("NEW_MANPOWER_TYPE")).html();


            function newManpower() {
                var options = {
                    title: newManpowerHeading,
                    template: 'app/desktop/modules/mes/manpower/new/newManpowerView.jsp',
                    controller: 'NewManpowerController as newManpowerVm',
                    resolve: 'app/desktop/modules/mes/manpower/new/newManpowerController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: create, broadcast: 'app.manpower.new'}
                    ],
                    callback: function (manpower) {
                        $timeout(function () {
                            loadManpowers();
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }


            function nextPage() {
                if (vm.manpowers.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadManpowers();
                }
            }

            function previousPage() {
                if (vm.manpowers.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadManpowers();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.size = page;
                vm.pageable.page = 0;
                loadManpowers();
            }

            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.filters.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    loadManpowers();
                } else {
                    resetPage();
                }
            }

            function resetPage() {
                vm.manpowers = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                $rootScope.showBusyIndicator($('.view-container'));
                loadManpowers();
            }

            function loadManpowers() {
                vm.loading = true;
                $rootScope.showBusyIndicator();
                ManpowerService.getAllManpower(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.manpowers = data;
                        vm.loading = false;
                        CommonService.getPersonReferences(vm.manpowers.content, 'modifiedBy');
                        loadAttributeValues();
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )

            }

            vm.objectIds = [];
            vm.attributeIds = [];
            vm.selectedAttributes = [];
            function loadAttributeValues() {
                vm.objectIds = [];
                vm.attributeIds = [];
                angular.forEach(vm.manpowers.content, function (item) {
                    vm.objectIds.push(item.id);
                });
                angular.forEach(vm.selectedAttributes, function (selectedAttribute) {
                    if (selectedAttribute.id != null && selectedAttribute.id != "" && selectedAttribute.id != 0) {
                        vm.attributeIds.push(selectedAttribute.id);
                    }
                });
                $rootScope.getObjectAttributeValues(vm.objectIds, vm.attributeIds, vm.selectedAttributes, vm.manpowers.content);
            }


            vm.showManpower = showManpower;
            function showManpower(manpower) {
                vm.recentlyVisited = {};
                vm.recentlyVisited.objectId = manpower.id;
                vm.recentlyVisited.objectType = manpower.objectType;
                vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                    function (data) {
                        $state.go('app.mes.masterData.manpower.details', {
                            manpowerId: manpower.id,
                            tab: 'details.basic'
                        });
                    }, function (error) {
                        $state.go('app.mes.masterData.manpower.details', {
                            manpowerId: manpower.id,
                            tab: 'details.basic'
                        });
                    }
                )
            }

            var deleteManpowers = parsed.html($translate.instant("DELETE_MANPOWER")).html();
            var deleteDialogMessage = parsed.html($translate.instant("DELETE_TYPE_DIALOG_MESSAGE")).html();
            var ManpowerDeleteMsg = parsed.html($translate.instant("MANPOWER_DELETE_MSG")).html();

            vm.deleteManpower = deleteManpower;

            function deleteManpower(manpower) {
                var options = {
                    title: deleteManpowers,
                    message: deleteDialogMessage + " [ " + manpower.name + " ] " + "?",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        if (manpower.id != null && manpower.id != undefined) {
                            ManpowerService.deleteManpower(manpower.id).then(
                                function (data) {
                                    $rootScope.showSuccessMessage(ManpowerDeleteMsg);
                                    loadManpowers();
                                },
                                function (error) {
                                    $rootScope.showErrorMessage(error.message);
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
                        type: "MANPOWERTYPE",
                        objectType: "MANPOWER"
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.select.attributes'}
                    ],
                    callback: function (result) {
                        vm.selectedAttributes = result;
                        $window.localStorage.setItem("manpowerAttributes", JSON.stringify(vm.selectedAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesAdded);
                        }
                        loadManpowers();
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
                    JSON.parse($window.localStorage.getItem("manpowerAttributes"));
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
                    setAttributes = JSON.parse($window.localStorage.getItem("manpowerAttributes"));
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
                                $window.localStorage.setItem("manpowerAttributes", "");
                                vm.selectedAttributes = setAttributes
                            } else {
                                vm.selectedAttributes = setAttributes;
                            }
                            loadManpowers();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    loadManpowers();
                }
            })();

        }
    }
);