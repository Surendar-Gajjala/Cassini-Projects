define(
    [
        'app/desktop/modules/customObject/customObject.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/customObjectService',
        'app/desktop/modules/directives/customObjectTypeDirective'
    ],

    function (module) {
        module.controller('CustomObjectSelectionController', CustomObjectSelectionController);

        function CustomObjectSelectionController($scope, $rootScope, $timeout, $translate, $stateParams, $state, CustomObjectService) {

            var vm = this;

            var bomObject = $scope.data.selectedCustomObjectBom;
            var related = $scope.data.selectedRelated;
            vm.customId = $stateParams.customId;
            vm.selectedItems = [];
            vm.selectCheck = selectCheck;
            vm.selectAll = selectAll;
            vm.onSelectType = onSelectType;
            vm.clearFilter = clearFilter;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            function nextPage() {
                if (vm.customObjects.last != true) {
                    vm.pageable.page++;
                    vm.selectAllCheck = false;
                    vm.selectedItems = [];
                    $rootScope.showBusyIndicator($("#rightSidePanel"));
                    loadCustomObjects();
                }
            }

            function previousPage() {
                if (vm.customObjects.first != true) {
                    vm.pageable.page--;
                    vm.selectAllCheck = false;
                    vm.selectedItems = [];
                    $rootScope.showBusyIndicator($("#rightSidePanel"));
                    loadCustomObjects();
                }
            }

            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            vm.customFilter = {
                number: null,
                name: null,
                type: '',
                customType: '',
                description: null,
                searchQuery: null,
                object: '',
                bomObject: '',
                related: false,
                typeName: null
            };

            function onSelectType(reqType) {
                if (reqType != null && reqType != undefined) {
                    $rootScope.showBusyIndicator($("#rightSidePanel"));
                    vm.customFilter.typeName = reqType.name;
                    vm.customFilter.type = reqType.id;
                    vm.selectedItems = [];
                    searchItems();
                }
            }

            vm.searchItems = searchItems;

            function searchItems() {
                vm.pageable.page = 0;
                loadCustomObjects();
                vm.clear = true;
            }

            function clearFilter() {
                vm.customFilter = {
                    number: null,
                    name: null,
                    type: '',
                    customType: '',
                    description: null,
                    searchQuery: null,
                    object: '',
                    bomObject: '',
                    typeName: null,
                    related: false
                };
                vm.selectedItems = [];
                vm.pageable.page = 0;
                loadCustomObjects();
                vm.clear = false;
            }

            vm.selectAllCheck = false;

            function selectAll() {
                vm.selectedItems = [];
                if (vm.selectAllCheck == false) {
                    angular.forEach(vm.customObjects.content, function (item) {
                        item.selected = false;
                    })
                    vm.selectedItems = [];

                } else {
                    vm.error = "";
                    angular.forEach(vm.customObjects.content, function (item) {
                        item.selected = true;
                        vm.selectedItems.push(item);
                    })
                    if (vm.selectedItems.length == vm.customObjects.content.length) {
                        vm.selectAllCheck = true;
                    }
                }
            }

            function selectCheck(item) {
                var flag = true;
                vm.error = "";
                angular.forEach(vm.selectedItems, function (selectedItem) {
                    if (selectedItem.id == item.id) {
                        flag = false;
                        var index = vm.selectedItems.indexOf(selectedItem);
                        vm.selectedItems.splice(index, 1);
                        vm.selectAllCheck = false;
                    }
                });
                if (flag) {
                    vm.selectedItems.push(item);
                    if (vm.selectedItems.length == vm.customObjects.content.length) {
                        vm.selectAllCheck = true;
                    }
                }
            }

            var parsed = angular.element("<div></div>");
            var atLeastCustomObjectValidation = parsed.html($translate.instant("ADD_AT_LEAST_ONE_CUSTOM_OBJECT")).html();
            var atLeastObjectValidation = parsed.html($translate.instant("ADD_AT_LEAST_ONE_RELATED_OBJECT")).html();
            vm.projectReqDocs = [];
            function create() {
                if (vm.selectedItems.length == 0) {
                    if (related == true) {
                        $rootScope.showWarningMessage(atLeastObjectValidation);
                    } else {
                        $rootScope.showWarningMessage(atLeastCustomObjectValidation);
                    }
                } else {
                    $scope.callback(vm.selectedItems);
                }
            }

            function loadCustomObjects() {
                vm.loading = false;
                if (bomObject != null && bomObject != undefined) {
                    vm.customFilter.bomObject = bomObject.id;
                }
                if (related == true) {
                    vm.customFilter.related = true;
                }
                vm.customFilter.object = vm.customId;
                CustomObjectService.getCustomObjects(vm.pageable, vm.customFilter).then(
                    function (data) {
                        vm.customObjects = data;
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


            (function () {
                $rootScope.$on('add.customobject.bom.objects', create);
                loadCustomObjects();
            })();
        }
    }
);