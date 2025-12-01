define(
    [
        'app/desktop/modules/customObject/customObject.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/customObjectService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/customObjectTypeService',
        'app/desktop/directives/all-view-attributes/allViewAttributesDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/desktop/directives/all-view-icons/allViewIconsDirective',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective',
        'app/shared/services/core/supplierService'
    ],
    function (module) {
        module.controller('CustomObjectsController', CustomObjectsController);

        function CustomObjectsController($scope, $rootScope, $translate, $timeout, $state, $window, $stateParams, $cookies, $sce, $application,
                                         DialogService, ObjectTypeAttributeService, CustomObjectTypeService, CommonService,
                                         CustomObjectService, SupplierService) {

            $rootScope.viewInfo.icon = "fa fa-calendar";
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            var vm = this;

            var typeId = null;
            vm.customObjectType = {
                name: "Custom Object"
            };
            vm.loading = true;
            var currencyMap = new Hashtable();
            var parsed = angular.element("<div></div>");
            $scope.createNcrTitle = parsed.html($translate.instant("CREATE_NEW_NCR")).html();
            var createButton = parsed.html($translate.instant("CREATE")).html();
            $scope.cannotDeleteApprovedNcr = parsed.html($translate.instant("CANNOT_DELETE_APPROVED_NCR")).html();
            vm.RemoveColumnTitle = parsed.html($translate.instant("REMOVE_ATTRIBUTE_COLUMN")).html();
            vm.showAttributeTitle = parsed.html($translate.instant("SHOW_ATTRIBUTES")).html();

            if ($stateParams.customMode != null) {
                $rootScope.customTitle = null;
                $scope.customObjectTitle = $stateParams.customMode;
                $rootScope.customTitle = $scope.customObjectTitle;
            }
            vm.newCustomObject = newCustomObject;
            vm.selectedAttributes = [];

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

            vm.customFilter = {
                number: null,
                name: null,
                type: '',
                customType: null,
                description: null,
                searchQuery: null,
                object: '',
                bomObject: '',
                related: false,
                supplier: ''
            };

            function loadCustomObjectTypeByName() {
                vm.loading = true;
                var typeName = $window.localStorage.getItem("route-param");
                if (typeName != null && typeName != "") {
                    CustomObjectTypeService.getCustomObjectByName(typeName).then(
                        function (data) {
                            typeId = data.customObjectTypeId;
                            vm.customFilter.customType = typeId;
                            vm.customObjectType = data.customObjectType;
                            if ($stateParams.customMode != null) {
                                $rootScope.customTitle = null;
                                $scope.customObjectTitle = data.name;
                                $rootScope.customTitle = $scope.customObjectTitle;
                            }
                            loadCustomObjects();
                            $rootScope.hideBusyIndicator();
                            //loadCustomObjectType();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }

            }

            vm.customObjects = angular.copy(pagedResults);
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;

            function newCustomObject() {
                var options = {
                    title: 'New ' + vm.customObjectType.name,
                    template: 'app/desktop/modules/customObject/new/newCustomObjectView.jsp',
                    controller: 'NewCustomObjectController as newCustomObjectsVm',
                    resolve: 'app/desktop/modules/customObject/new/newCustomObjectController',
                    width: 650,
                    showMask: true,
                    data: {
                        selectType: typeId,
                        customeObjectTypeName: vm.customObjectType.name,
                        creationType: "customObject"
                    },
                    buttons: [
                        {text: createButton, broadcast: 'app.customObject.new'}
                    ],
                    callback: function () {
                        $timeout(function () {
                            loadCustomObjects();
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function loadCustomObjects() {
                CustomObjectService.getCustomObjects(vm.pageable, vm.customFilter).then(
                    function (data) {
                        vm.customObjects = data;
                        vm.loading = false;
                        if (vm.customObjectType.name == "Supplier Performance Rating" || vm.customObjectType.name == "CPI Form" || vm.customObjectType.name == "4MChange-Supplier") {
                            SupplierService.getSupplierReferences(vm.customObjects.content, 'supplier')
                        }
                        CommonService.getPersonReferences(vm.customObjects.content, 'createdBy');
                        CommonService.getPersonReferences(vm.customObjects.content, 'modifiedBy');
                        loadAttributeDefs();
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.showCustomObject = showCustomObject;

            function showCustomObject(object) {
                $state.go('app.customobjects.details', {customId: object.id, tab: 'details.basic'});
            }

            function loadAttributeDefs() {
                vm.selectAttributes = [];
                CustomObjectTypeService.getCustomObjectAttributesWithHierarchy(typeId).then(
                    function (data) {
                        angular.forEach(data, function (attr) {
                            if (attr.showInTable == true) {
                                vm.selectAttributes.push(attr);
                            }
                        })
                        loadAttributeValues();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.selectedObjectAttributes = [];
            vm.objectIds = [];
            vm.attributeIds = [];
            function loadAttributeValues() {
                angular.forEach(vm.customObjects.content, function (customObject) {
                    vm.objectIds.push(customObject.id);
                });
                if (vm.selectedAttributes != null && vm.selectedAttributes.length > 0) {
                    angular.forEach(vm.selectedAttributes, function (attributes) {
                        vm.selectAttributes.push(attributes);
                    });
                }
                if (vm.selectAttributes.length > 0) {
                    angular.forEach(vm.selectAttributes, function (selectedAttribute) {
                        if (selectedAttribute.id != null && selectedAttribute.id != "" && selectedAttribute.id != 0) {
                            vm.attributeIds.push(selectedAttribute.id);
                        }
                    });
                    $rootScope.getObjectAttributeValues(vm.objectIds, vm.attributeIds, vm.selectAttributes, vm.customObjects.content);
                }
            }

            vm.openAttachment = openAttachment;
            function openAttachment(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/download".
                    format(window.location.protocol, window.location.host,
                    attachment.id);
                $rootScope.downloadFileFromIframe(url);
            }


            function nextPage() {
                if (vm.customObjects.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadCustomObjects();
                }
            }

            function previousPage() {
                if (vm.customObjects.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadCustomObjects();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.page = 0;
                vm.pageable.size = page;
                loadCustomObjects();
            }

            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.customFilter.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                } else {
                    resetPage();
                }
                loadCustomObjects();
                loadAttributeDefs();
            }

            function resetPage() {
                vm.filterSearch = false;
                vm.customObjects = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.customFilter.searchQuery = null;
                $scope.freeTextQuery = null;
                if (document.getElementById("freeTextSearchInput") != null)
                    document.getElementById("freeTextSearchInput").placeholder = "";
                $rootScope.showBusyIndicator($('.view-container'));
            }

            vm.removeAttribute = removeAttribute;
            function removeAttribute(att) {
                vm.selectAttributes.remove(att);
                vm.selectedAttributes.remove(att);
                $window.localStorage.setItem(typeId, JSON.stringify(vm.selectedAttributes));
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem(typeId));
                } catch (e) {
                    return false;
                }
                return true;
            }

            /*function loadCustomObjectType() {
             vm.loading = true;
             vm.customFilter.customType = typeId;
             CustomObjectTypeService.getCustomObjectType(typeId).then(
             function (data) {
             vm.customObjectType = data;
             if ($stateParams.customMode != null) {
             $rootScope.customTitle = null;
             $scope.customObjectTitle = data.name;
             $rootScope.customTitle = $scope.customObjectTitle;
             }

             loadCustomObjects();
             $rootScope.hideBusyIndicator();
             }, function (error) {
             $rootScope.showErrorMessage(error.message);
             $rootScope.hideBusyIndicator();
             }
             )
             }*/

            var deleteObjectTitle = parsed.html($translate.instant("DELETE_OBJECT_TITLE_MSG")).html();
            var customeObjectDeleteMsg = parsed.html($translate.instant("CUSTOM_OBJECT_DELETE_SUCCESS_MESSAGE")).html();
            var deleteObject = parsed.html($translate.instant("OBJECT_DELETE_MSG")).html();
            var selectedAttributesMessage = parsed.html($translate.instant("SELECTED_ATTRIBUTES_MESSAGE")).html();
            vm.deleteCustomObject = deleteCustomObject;
            function deleteCustomObject(object) {
                var options = {
                    title: deleteObject,
                    message: deleteObjectTitle + " [ " + object.number + " ] " + "?",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        if (object.id != null && object.id != undefined) {
                            CustomObjectService.deleteCustomObject(object.id).then(
                                function (data) {
                                    var index = vm.customObjects.content.indexOf(object);
                                    vm.customObjects.content.splice(index, 1);
                                    vm.customObjects.totalElements--;
                                    $rootScope.showSuccessMessage(customeObjectDeleteMsg);
                                },
                                function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    }
                });
            }

            var attributeTitle = $translate.instant("ATTRIBUTES");
            var addButton = parsed.html($translate.instant("ADD")).html();
            vm.showAttributes = showAttributes;
            function showAttributes() {
                var selectedAttribute = angular.copy(vm.selectedAttributes);
                var options = {
                    title: attributeTitle,
                    template: 'app/desktop/modules/shared/attributes/attributesView.jsp',
                    resolve: 'app/desktop/modules/shared/attributes/attributesController',
                    controller: 'AttributesController as attributesVm',
                    width: 500,
                    showMask: true,
                    data: {
                        selectedAttributes: selectedAttribute,
                        type: typeId,
                        objectType: "CUSTOMOBJECT",
                        selectionType: "DISPLAY"
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.select.attributes'}
                    ],
                    callback: function (result) {
                        vm.selectedAttributes = result;
                        $window.localStorage.setItem(typeId, JSON.stringify(vm.selectedAttributes));
                        loadAttributeDefs();
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesMessage);
                        }
                    }
                };

                $rootScope.showSidePanel(options);
            }

            var searchButton = $translate.instant("SEARCH");
            vm.itemsSearch = itemsSearch;
            vm.searchItemType = parsed.html($translate.instant("OBJECT_SEARCH")).html();

            function itemsSearch() {
                var options = {
                    title: "Custom Object Search",
                    template: 'app/desktop/modules/customObject/all/customSearchDialogueView.jsp',
                    controller: 'CustomSearchDialogueController as searchDialogueVm',
                    resolve: 'app/desktop/modules/customObject/all/customSearchDialogueController',
                    width: 750,
                    showMask: true,
                    data: {
                        itemsMode: vm.itemsMode,
                        selectedType: "CUSTOMOBJECTTYPE",
                        objectTypeId: typeId
                    },
                    buttons: [
                        {text: searchButton, broadcast: 'app.customObjects.search'}
                    ],
                    callback: function (filter) {
                        if (filter.length > 0) {
                            vm.resetPage();
                            vm.advancedItemSearchFilters = filter;
                            vm.filters = [];
                            angular.forEach(filter, function (item) {
                                var filter = {
                                    field: item.field.field,
                                    operator: item.operator.name,
                                    value: item.value,
                                    attributeId: item.attributeId
                                };
                                vm.filters.push(filter);
                            });
                            $timeout(function () {
                                advancedCustomObjectSearch(vm.filters);
                            }, 200);
                        } else {
                            vm.resetPage();
                            $rootScope.showBusyIndicator($('.view-container'));
                            searchItem(filter);
                        }
                    }
                };
                vm.mode = null;
                $rootScope.searchType = null;
                $rootScope.showSidePanel(options);
            }

            var person = null
            $rootScope.localStorageLogin = JSON.parse(localStorage.getItem('local_storage_login'));
            if ($rootScope.localStorageLogin.login.person.id != null) {
                person = $rootScope.localStorageLogin.login.person.id;
            }

            vm.search = {
                name: null,
                description: null,
                searchType: null,
                query: null,
                objectType: 'ITEM',
                owner: person,
                type: 'PRIVATE'
            };

            var rootNode = null;
            var searchMode = null;
            var simpleFilters = null;
            var advancedFilters = null;

            function searchItem(filters) {
                vm.search.searchType = "simple";
                searchMode = "simple";
                simpleFilters = filters;
                vm.search.query = angular.toJson(filters);
                if (vm.itemMode != null && vm.itemMode != "") filters.itemClass = vm.itemsMode;
                $rootScope.itemSimpleSearch = filters;
                $rootScope.itemAdvanceSearch = null;
                $stateParams.itemMode = null;
                $rootScope.showBusyIndicator($('.view-container'));
                CustomObjectService.searchCustomObject(vm.pageable, filters).then(
                    function (data) {
                        vm.customObjects = data;
                        vm.clear = true;
                        vm.loading = false;
                        vm.filterSearch = true;
                        $rootScope.showSearch = true;
                        $rootScope.searchModeType = true;
                        loadAttributeDefs();
                        if (document.getElementById("freeTextSearchInput") != null)
                            document.getElementById("freeTextSearchInput").placeholder = "Simple Search";
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function advancedCustomObjectSearch(filters) {
                vm.search.searchType = "advanced";
                searchMode = "advanced";
                advancedFilters = filters;
                vm.search.query = angular.toJson(filters);
                if (vm.itemMode != null && vm.itemMode != "") {
                    angular.forEach(filters, function (filter) {
                        filter.itemClass = vm.itemsMode;
                    });
                }
                $rootScope.itemAdvanceSearch = filters;
                $rootScope.itemSimpleSearch = null;
                $stateParams.itemMode = null;
                $rootScope.showBusyIndicator($('.view-container'));
                CustomObjectService.advancedSearchCustomObject(vm.pageable, filters, typeId).then(
                    function (data) {
                        vm.customObjects = data;
                        vm.filterSearch = true;
                        vm.clear = true;
                        vm.loading = false;
                        $rootScope.showSearch = true;
                        $rootScope.searchModeType = true;
                        loadAttributeDefs();
                        if (document.getElementById("freeTextSearchInput") != null)
                            document.getElementById("freeTextSearchInput").placeholder = "Advanced Search";
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {
                angular.forEach($application.currencies, function (data) {
                    currencyMap.put(data.id, $sce.trustAsHtml(data.symbol));
                });

                vm.filterSearch = false;
                var setAttributes = null;
                if (validateJSON()) {
                    setAttributes = JSON.parse($window.localStorage.getItem(typeId));
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
                                $window.localStorage.setItem(typeId, "");
                                vm.selectedAttributes = setAttributes
                            } else {
                                vm.selectedAttributes = setAttributes;
                            }
                            loadCustomObjectTypeByName();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    loadCustomObjectTypeByName();
                }

            })();
        }
    }
);


