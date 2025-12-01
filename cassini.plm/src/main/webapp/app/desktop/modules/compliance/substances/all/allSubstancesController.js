define(
    [
        'app/desktop/modules/compliance/compliance.module',
        'app/shared/services/core/substanceService',
        'app/desktop/directives/all-view-attributes/allViewAttributesDirective',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('AllSubstanceController', AllSubstanceController);

        function AllSubstanceController($scope, $rootScope, $translate, $timeout, $state, $window, DialogService, $application, $stateParams, $cookies, $sce,
                                        ObjectTypeAttributeService, CommonService, SubstanceService, RecentlyVisitedService, ItemService, ECOService, WorkflowDefinitionService,
                                        AttributeAttachmentService, MfrService, MfrPartsService, ProjectService, SpecificationsService) {

            var vm = this;
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            vm.loading = false;
            vm.newSubstance = newSubstance;
            vm.substances = [];
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
                number: null,
                type: '',
                name: null,
                description: null,
                searchQuery: null,
                declarationPart: ''
            };
            $scope.freeTextQuery = null;

            vm.substances = angular.copy(pagedResults);

            var parsed = angular.element("<div></div>");
            var create = parsed.html($translate.instant("CREATE")).html();
            var newSubstanceHeading = parsed.html($translate.instant("NEW_SUBSTANCE")).html();

            function newSubstance() {
                var options = {
                    title: newSubstanceHeading,
                    template: 'app/desktop/modules/compliance/substances/new/newSubstanceView.jsp',
                    controller: 'NewSubstanceController as newSubstanceVm',
                    resolve: 'app/desktop/modules/compliance/substances/new/newSubstanceController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: create, broadcast: 'app.substance.new'}
                    ],
                    callback: function (substance) {
                        $timeout(function () {
                            loadSubstances();
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }


            function nextPage() {
                if (vm.substances.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadSubstances();
                }
            }

            function previousPage() {
                if (vm.substances.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadSubstances();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.size = page;
                vm.pageable.page = 0;
                loadSubstances();
            }

            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.filters.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    loadSubstances();
                } else {
                    resetPage();
                }
            }

            function resetPage() {
                vm.substances = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                $rootScope.showBusyIndicator($('.view-container'));
                loadSubstances();
            }

            function loadSubstances() {
                vm.loading = true;
                SubstanceService.getAllSubstances(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.substances = data;
                        vm.loading = false;
                        CommonService.getPersonReferences(vm.substances.content, 'modifiedBy');
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
                angular.forEach(vm.substances.content, function (item) {
                    vm.objectIds.push(item.id);
                });
                if (vm.selectedAttributes.length > 0) {
                    angular.forEach(vm.selectedAttributes, function (selectedAttribute) {
                        if (selectedAttribute.id != null && selectedAttribute.id != "" && selectedAttribute.id != 0) {
                            vm.attributeIds.push(selectedAttribute.id);
                        }
                    });
                    $rootScope.getObjectAttributeValues(vm.objectIds, vm.attributeIds, vm.selectedAttributes, vm.substances.content);
                }
            }

            vm.showSubstance = showSubstance;
            function showSubstance(substance) {
                vm.recentlyVisited = {};
                vm.recentlyVisited.objectId = substance.id;
                vm.recentlyVisited.objectType = "PGCSUBSTANCE";
                vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                    function (data) {
                        $state.go('app.compliance.substance.details', {
                            substanceId: substance.id,
                            tab: 'details.basic'
                        });
                    }, function (error) {
                        $state.go('app.compliance.substance.details', {
                            substanceId: substance.id,
                            tab: 'details.basic'
                        });
                    }
                )
            }

            var deleteSubstances = parsed.html($translate.instant("DELETE_SUBSTANCE")).html();
            var deleteDialogMessage = parsed.html($translate.instant("DELETE_TYPE_DIALOG_MESSAGE")).html();
            var substanceDeleteMsg = parsed.html($translate.instant("SUBSTANCE_DELETE_MSG")).html();


            vm.deleteSubstance = deleteSubstance;
            function deleteSubstance(substance) {
                var options = {
                    title: deleteSubstances,
                    message: deleteDialogMessage + " [ " + substance.number + " ] " + "?",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        if (substance.id != null && substance.id != undefined) {
                            SubstanceService.deleteSubstance(substance.id).then(
                                function (data) {
                                    var index = vm.substances.content.indexOf(substance);
                                    vm.substances.content.splice(index, 1);
                                    vm.substances.totalElements--;
                                    $rootScope.showSuccessMessage(substanceDeleteMsg);
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
                        type: "PGCSUBSTANCETYPE",
                        objectType: "PGCOBJECT"
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.select.attributes'}
                    ],
                    callback: function (result) {
                        vm.selectedAttributes = result;
                        $window.localStorage.setItem("substanceAttributes", JSON.stringify(vm.selectedAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesAdded);
                        }
                        loadSubstances();
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
                    JSON.parse($window.localStorage.getItem("substanceAttributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }


            (function () {
                var setAttributes = null;
                if (validateJSON()) {
                    setAttributes = JSON.parse($window.localStorage.getItem("substanceAttributes"));
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
                                $window.localStorage.setItem("substanceAttributes", "");
                                vm.selectedAttributes = setAttributes
                            } else {
                                vm.selectedAttributes = setAttributes;
                            }
                            loadSubstances();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                } else {
                    loadSubstances();
                }
            })();

        }
    }
);
