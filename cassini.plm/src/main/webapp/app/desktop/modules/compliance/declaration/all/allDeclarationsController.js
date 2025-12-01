define(
    [
        'app/desktop/modules/compliance/compliance.module',
        'app/shared/services/core/declarationService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/workflowDefinitionService',
        'app/shared/services/core/projectService',
        'app/shared/services/core/declarationService',
        'app/shared/services/core/supplierService',
        'app/desktop/directives/all-view-attributes/allViewAttributesDirective',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('AllDeclarationController', AllDeclarationController);

        function AllDeclarationController($scope, $rootScope, $translate, $timeout, $state, $window, DialogService, $application, $stateParams, $cookies, $sce, DeclarationService, ObjectTypeAttributeService,
                                          ItemService, ECOService, WorkflowDefinitionService, MfrService, MfrPartsService, AttributeAttachmentService, CommonService, ProjectService,
                                          RecentlyVisitedService, SupplierService) {

            var vm = this;
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            vm.loading = false;
            vm.newDeclaration = newDeclaration;
            vm.deleteDeclaration = deleteDeclaration;
            vm.declaration = [];
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
                searchQuery: null
            };
            $scope.freeTextQuery = null;

            vm.declarations = angular.copy(pagedResults);


            var parsed = angular.element("<div></div>");
            var create = parsed.html($translate.instant("CREATE")).html();
            var newDeclarationHeading = parsed.html($translate.instant("NEW_DECLARATION")).html();

            function newDeclaration() {
                var options = {
                    title: newDeclarationHeading,
                    template: 'app/desktop/modules/compliance/declaration/new/newDeclarationView.jsp',
                    controller: 'NewDeclarationController as newDeclarationVm',
                    resolve: 'app/desktop/modules/compliance/declaration/new/newDeclarationController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: create, broadcast: 'app.declaration.new'}
                    ],
                    callback: function (declaration) {
                        $timeout(function () {
                            loadDeclarations();
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function nextPage() {
                if (vm.declarations.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadDeclarations();
                }
            }

            function previousPage() {
                if (vm.declarations.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadDeclarations();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.size = page;
                vm.pageable.page = 0;
                loadDeclarations();
            }

            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.filters.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    loadDeclarations();
                } else {
                    resetPage();
                }
            }

            function resetPage() {
                vm.declarations = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                $rootScope.showBusyIndicator($('.view-container'));
                loadDeclarations();
            }

            function loadDeclarations() {
                vm.loading = true;
                DeclarationService.getAllDeclarations(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.declarations = data;
                        angular.forEach(vm.declarations.content, function (declaration) {
                            declaration.modifiedDatede = null;
                            if (declaration.modifiedDate != null) {
                                declaration.modifiedDatede = moment(declaration.modifiedDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                            }
                            if (declaration.image != null) {
                                declaration.imagePath = "api/compliance/declarations/" + declaration.id + "/image/download?" + new Date().getTime();
                            }
                        });
                        CommonService.getPersonReferences(vm.declarations.content, 'modifiedBy');
                        loadAttributeValues();
                        $rootScope.hideBusyIndicator();
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )

                vm.loading = false;
            }

            vm.objectIds = [];
            vm.selectedAttributes = [];
            function loadAttributeValues() {
                vm.objectIds = [];
                vm.attributeIds = [];
                angular.forEach(vm.declarations.content, function (item) {
                    vm.objectIds.push(item.id);
                });
                if (vm.selectedAttributes.length > 0) {
                    angular.forEach(vm.selectedAttributes, function (selectedAttribute) {
                        if (selectedAttribute.id != null && selectedAttribute.id != "" && selectedAttribute.id != 0) {
                            vm.attributeIds.push(selectedAttribute.id);
                        }
                    });
                    $rootScope.getObjectAttributeValues(vm.objectIds, vm.attributeIds, vm.selectedAttributes, vm.declarations.content);
                }

            }


            vm.showDeclaration = showDeclaration;
            function showDeclaration(declaration) {
                vm.recentlyVisited = {};
                vm.recentlyVisited.objectId = declaration.id;
                vm.recentlyVisited.objectType = declaration.objectType;
                vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                    function (data) {
                        $state.go('app.compliance.declaration.details', {
                            declarationId: declaration.id,
                            tab: 'details.basic'
                        });
                    }, function (error) {
                        $state.go('app.compliance.declaration.details', {
                            declarationId: declaration.id,
                            tab: 'details.basic'
                        });
                    }
                )
            }

            var deleteDeclarationt = parsed.html($translate.instant("DELETE_DECLARATION")).html();
            var deleteDialogMessage = parsed.html($translate.instant("DELETE_TYPE_DIALOG_MESSAGE")).html();
            var DeclarationDeleteSuccess = parsed.html($translate.instant("DECLARATION_DELETE_SUCCESS")).html();


            function deleteDeclaration(declaration) {
                var options = {
                    title: deleteDeclarationt,
                    message: deleteDialogMessage + " [ " + declaration.number + " ] " + "?",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        if (declaration.id != null && declaration.id != undefined) {
                            DeclarationService.deleteDeclaration(declaration.id).then(
                                function (data) {
                                    $rootScope.showSuccessMessage(DeclarationDeleteSuccess);
                                    loadDeclarations();
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
                        type: "PGCDECLARATIONTYPE",
                        objectType: "PGCOBJECT"
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.select.attributes'}
                    ],
                    callback: function (result) {
                        vm.selectedAttributes = result;
                        $window.localStorage.setItem("declarationAttributes", JSON.stringify(vm.selectedAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesAdded);
                        }
                        loadDeclarations();
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
                    JSON.parse($window.localStorage.getItem("declarationAttributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }


            (function () {
                var setAttributes = null;
                if (validateJSON()) {
                    setAttributes = JSON.parse($window.localStorage.getItem("declarationAttributes"));
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
                                $window.localStorage.setItem("declarationAttributes", "");
                                vm.selectedAttributes = setAttributes
                            } else {
                                vm.selectedAttributes = setAttributes;
                            }
                            loadDeclarations();
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                } else {
                    loadDeclarations();
                }
            })();

        }
    }
);