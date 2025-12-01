define(
    [
        'app/desktop/modules/compliance/compliance.module',
        'app/shared/services/core/specificationService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/workflowDefinitionService',
        'app/shared/services/core/projectService',
        'app/shared/services/core/specificationsService',
        'app/desktop/directives/all-view-attributes/allViewAttributesDirective',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('AllSpecificationController', AllSpecificationController);

        function AllSpecificationController($scope, $rootScope, $translate, $timeout, $state, $window, DialogService, $application, $stateParams, $cookies, $sce, SpecificationService, ObjectTypeAttributeService,
                                            ItemService, ECOService, WorkflowDefinitionService, MfrService, MfrPartsService, AttributeAttachmentService, CommonService, ProjectService, SpecificationsService,
                                            RecentlyVisitedService) {

            var vm = this;
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            vm.loading = false;
            vm.newSpecification = newSpecification;
            vm.deleteSpecification = deleteSpecification;
            vm.specification = [];
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
                type: '',
                name: null,
                number: null,
                declaration: '',
                item: ''
            };
            $scope.freeTextQuery = null;

            vm.specifications = angular.copy(pagedResults);


            var parsed = angular.element("<div></div>");
            var create = parsed.html($translate.instant("CREATE")).html();
            var newSpecificationHeading = parsed.html($translate.instant("NEW_SPECIFICATION")).html();

            function newSpecification() {
                var options = {
                    title: newSpecificationHeading,
                    template: 'app/desktop/modules/compliance/specifications/new/newSpecificationView.jsp',
                    controller: 'NewSpecificationController as newSpecificationVm',
                    resolve: 'app/desktop/modules/compliance/specifications/new/newSpecificationController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: create, broadcast: 'app.specification.new'}
                    ],
                    callback: function (result) {
                        $timeout(function () {
                            loadSpecifications();
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function nextPage() {
                if (vm.specifications.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadSpecifications();
                }
            }

            function previousPage() {
                if (vm.specifications.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadSpecifications();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.size = page;
                vm.pageable.page = 0;
                loadSpecifications();
            }

            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.filters.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    loadSpecifications();
                } else {
                    resetPage();
                }
            }

            function resetPage() {
                vm.specifications = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                $rootScope.showBusyIndicator($('.view-container'));
                loadSpecifications();
            }

            function loadSpecifications() {
                vm.loading = true;
                SpecificationService.getAllSpecifications(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.specifications = data;
                        CommonService.getPersonReferences(vm.specifications.content, 'modifiedBy');
                        loadAttributeValues();
                        $rootScope.hideBusyIndicator();
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );

                vm.loading = false;
            }

            vm.objectIds = [];
            vm.selectedAttributes = [];
            function loadAttributeValues() {
                vm.objectIds = [];
                vm.attributeIds = [];
                angular.forEach(vm.specifications.content, function (item) {
                    vm.objectIds.push(item.id);
                });
                if (vm.selectedAttributes.length > 0) {
                    angular.forEach(vm.selectedAttributes, function (selectedAttribute) {
                        if (selectedAttribute.id != null && selectedAttribute.id != "" && selectedAttribute.id != 0) {
                            vm.attributeIds.push(selectedAttribute.id);
                        }
                    });
                    $rootScope.getObjectAttributeValues(vm.objectIds, vm.attributeIds, vm.selectedAttributes, vm.specifications.content);
                }

            }


            vm.showSpecification = showSpecification;
            function showSpecification(specification) {
                vm.recentlyVisited = {};
                vm.recentlyVisited.objectId = specification.id;
                vm.recentlyVisited.objectType = specification.objectType;
                vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                    function (data) {
                        $state.go('app.compliance.specification.details', {
                            specificationId: specification.id,
                            tab: 'details.basic'
                        });
                    }, function (error) {
                        $state.go('app.compliance.specification.details', {
                            specificationId: specification.id,
                            tab: 'details.basic'
                        });
                    }
                )
            }

            var deleteSpecificationTitle = parsed.html($translate.instant("DELETE_SPECIFICATION")).html();
            var deleteDialogMessage = parsed.html($translate.instant("DELETE_TYPE_DIALOG_MESSAGE")).html();
            var SpecificationDeleteMsg = parsed.html($translate.instant("SPECIFICATION_DELETE_MSG")).html();


            function deleteSpecification(specification) {
                var options = {
                    title: deleteSpecificationTitle,
                    message: deleteDialogMessage + " [ " + specification.number + " ] " + "?",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        if (specification.id != null && specification.id != undefined) {
                            SpecificationService.deleteSpecification(specification.id).then(
                                function (data) {
                                    $rootScope.showSuccessMessage(SpecificationDeleteMsg);
                                    loadSpecifications();
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
                        type: "PGCSPECIFICATIONTYPE",
                        objectType: "PGCOBJECT"
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.select.attributes'}
                    ],
                    callback: function (result) {
                        vm.selectedAttributes = result;
                        $window.localStorage.setItem("specificationAttributes", JSON.stringify(vm.selectedAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesAdded);
                        }
                        loadSpecifications();
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
                    JSON.parse($window.localStorage.getItem("specificationAttributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }


            (function () {
                var setAttributes = null;
                if (validateJSON()) {
                    setAttributes = JSON.parse($window.localStorage.getItem("specificationAttributes"));
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
                                $window.localStorage.setItem("specificationAttributes", "");
                                vm.selectedAttributes = setAttributes
                            } else {
                                vm.selectedAttributes = setAttributes;
                            }
                            loadSpecifications();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                } else {
                    loadSpecifications();
                }
            })();

        }
    }
);