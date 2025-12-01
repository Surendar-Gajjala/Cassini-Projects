define(
    [
        'app/desktop/modules/stores/store.module',
        'app/shared/services/core/itemService',
        'app/shared/services/core/itemTypeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService'

    ],
    function (module) {
        module.controller('AttributeSearchController', AttributeSearchController);

        function AttributeSearchController($scope, $rootScope, $stateParams, $timeout, $window, $state,
                                           ObjectAttributeService, ItemService, ItemTypeService, ObjectTypeAttributeService) {
            var vm = this;
            vm.attributeSearch = {
                objectTypeAttribute: null,
                text: null,
                integer: null,
                list: null,
                currency: null,
                aDouble: null,
                date: null,
                time: null,
                aBoolean: null,
                booleanSearch: false
            }

            var objectType = $scope.data.objectType;
            var type = $scope.data.type;

            vm.receiveAttributes = [];
            vm.searchAttributes = [];
            vm.allAttributes = [];
            vm.removeAttribute = removeAttribute;
            vm.selectAttribute = selectAttribute;

            function search() {
                if (vm.receiveAttributes.length > 0) {
                    if (validate()) {
                        $rootScope.showBusyIndicator($("#rightSidePanel"));
                        $scope.callback(vm.receiveAttributes);
                        vm.allAttributes = [];
                        vm.receiveAttributes = [];
                    }
                } else {
                    $rootScope.showWarningMessage("Please select atleast one attribute");
                }
            }

            function validate() {
                var valid = true;

                angular.forEach(vm.receiveAttributes, function (att) {
                    if (valid) {
                        if (att.objectTypeAttribute.dataType == "TEXT" && (att.text == null || att.text == "" || att.text == undefined)) {
                            valid = false;
                            $rootScope.showWarningMessage("Please enter value for " + att.objectTypeAttribute.name);
                        } else if (att.objectTypeAttribute.dataType == "INTEGER" && (att.integer == null || att.integer == "" || att.integer == undefined)) {
                            valid = false;
                            $rootScope.showWarningMessage("Please enter value for " + att.objectTypeAttribute.name);
                        } else if (att.objectTypeAttribute.dataType == "DOUBLE" && (att.aDouble == null || att.aDouble == "" || att.aDouble == undefined)) {
                            valid = false;
                            $rootScope.showWarningMessage("Please enter value for " + att.objectTypeAttribute.name);
                        } else if (att.objectTypeAttribute.dataType == "DATE" && (att.date == null || att.date == "" || att.date == undefined)) {
                            valid = false;
                            $rootScope.showWarningMessage("Please select date for " + att.objectTypeAttribute.name);
                        } else if (att.objectTypeAttribute.dataType == "CURRENCY" && (att.currency == null || att.currency == "" || att.currency == undefined)) {
                            valid = false;
                            $rootScope.showWarningMessage("Please enter value for " + att.objectTypeAttribute.name);
                        } else if (att.objectTypeAttribute.dataType == "LIST" && (att.list == null || att.list == "" || att.list == undefined)) {
                            valid = false;
                            $rootScope.showWarningMessage("Please select value for " + att.objectTypeAttribute.name);
                        }
                    }
                });

                return valid;
            }

            function loadAttributes() {
                if (type != null) {
                    ObjectTypeAttributeService.getObjectTypeAttributesByType(type).then(
                        function (data) {
                            angular.forEach(data, function (attribute) {
                                if (attribute.dataType == "TEXT" || attribute.dataType == "INTEGER" || attribute.dataType == "DOUBLE"
                                    || attribute.dataType == "DATE" || attribute.dataType == "LIST" || attribute.dataType == "CURRENCY" || attribute.dataType == "BOOLEAN") {
                                    vm.allAttributes.push(attribute);
                                }
                            });
                            if (objectType == 'RECEIVE') {
                                ItemTypeService.getReceiveTypeReferences(vm.allAttributes, 'itemType');
                            } else if (objectType == 'ISSUE') {
                                ItemTypeService.getIssueTypeReferences(vm.allAttributes, 'itemType');
                            } else if (objectType == 'MATERIAL') {
                                ItemTypeService.getMaterialTypeReferences(vm.allAttributes, 'itemType');
                            } else if (objectType == 'MACHINE') {
                                ItemTypeService.getMachineTypeReferences(vm.allAttributes, 'itemType');
                            } else if (objectType == 'MANPOWER') {
                                ItemTypeService.getManpowerTypeReferences(vm.allAttributes, 'itemType');
                            }
                        }
                    )
                }
                ItemService.getAllTypeAttributes(objectType).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            if (attribute.dataType == "TEXT" || attribute.dataType == "INTEGER" || attribute.dataType == "DOUBLE"
                                || attribute.dataType == "DATE" || attribute.dataType == "LIST" || attribute.dataType == "CURRENCY" || attribute.dataType == "BOOLEAN") {
                                vm.allAttributes.push(attribute);
                            }
                        })
                    }
                );

                $timeout(function () {
                    var sidePanelHeight = $('.app-side-panel').height();

                    var content = $('#attribute-search').height(sidePanelHeight - 100);
                }, 1000);

            }

            function selectAttribute(attribute) {

                var emptyAttribute = angular.copy(vm.attributeSearch);
                emptyAttribute.objectTypeAttribute = attribute;
                if (attribute.lov != null) {
                    emptyAttribute.objectTypeAttribute.lovValues = attribute.lov.values;
                }
                vm.receiveAttributes.push(emptyAttribute);

                vm.allAttributes.splice(vm.allAttributes.indexOf(attribute), 1);
            }

            function removeAttribute(attribute) {
                angular.forEach(vm.receiveAttributes, function (att) {
                    if (att.objectTypeAttribute.id == attribute.id) {
                        vm.receiveAttributes.splice(vm.receiveAttributes.indexOf(att), 1);
                    }
                })
                vm.allAttributes.push(attribute);
            }

            function loadCurrencies() {
                ObjectAttributeService.getCurrencies().then(
                    function (data) {
                        vm.currencies = data;
                    });
            }

            (function () {
                loadAttributes();
                loadCurrencies();
                $rootScope.$on('app.store.attribute.search', search);
            })();
        }
    }
)
;