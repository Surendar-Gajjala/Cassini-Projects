define(['app/desktop/modules/item/item.module',
        'jquery.easyui',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/groupService',
        'app/shared/services/core/itemTypeService',
        'app/desktop/modules/securityPermission/securityPermissionService'
    ],
    function (module) {
        module.controller('NewCriteriaController', NewCriteriaController);

        function NewCriteriaController($scope, $http, $i18n, SecurityPermissionService, ItemTypeService, $rootScope, $timeout, $state, $stateParams, $cookies, $translate) {
            var vm = this;
            vm.permission = $scope.data.permission;
            vm.className = $scope.data.className;
            vm.mode = $scope.data.mode;

            vm.predicates = [];
            vm.predicates1 = [];
            vm.finalList = [];
            vm.lhsList = [];
            vm.newRowList = [];
            vm.operators = [];
            vm.selectTitle = $i18n.getValue("SELECT");
            vm.criteriaSaved = $i18n.getValue("CRITERIA_SAVED");
            vm.types = [];
            vm.criteriaList = {
                lhs: [],
                operator: null,
                rhs: null,
                person: null,
                mergeType: null
            };
            vm.criteria = {newRow: [], enums: [], persons: []};
            vm.lhs = {
                "type": null,
                "dataType": null,
                "name": null
            };
            vm.property = {
                "name": null,
                "type": null,
                "objectType": null
            };
            vm.attribute = {};
            vm.deleteItem = deleteItem;
            function deleteItem(index) {
                vm.predicates.splice(index, 1);
            }

            vm.dataType = null;
            vm.values = ["true", "false"];
            function dataTypeOperators(dataType) {
                vm.operators = [];
                vm.dataType = null;
                switch (dataType) {
                    case 'string':
                        vm.dataType = 'string';
                        vm.operators = ["==", "!=", "contains", "contentEquals", "endsWith", "isEmpty"];
                        break;
                    case 'integer':
                        vm.dataType = 'integer';
                        vm.operators = ["==", "!=", ">", ">=", "<", "<="];
                        break;
                    case 'date':
                        vm.dataType = 'date';
                        vm.operators = ["compareTo", "equals", "before", "after"];
                        break;
                    case 'string[]':
                        vm.dataType = 'string[]';
                        vm.operators = ["=="];
                        break;
                    case 'object':
                        vm.dataType = 'object';
                        vm.operators = ["=="];
                        break;
                    case 'boolean':
                        vm.dataType = 'boolean';
                        vm.operators = ["equals"];
                        break;
                    case 'byte[]':
                        vm.dataType = 'byte[]';
                        vm.operators = ["=="];
                        break;
                    case 'integer[]':
                        vm.dataType = 'integer[]';
                        vm.operators = ["=="];
                        break;
                    case 'double':
                        vm.dataType = 'double';
                        vm.operators = ["==", "!=", ">", ">=", "<", "<="];
                        break;
                    case 'long':
                        vm.dataType = 'long';
                        vm.operators = ["==", "!=", ">", ">=", "<", "<="];
                        break;
                    case 'float':
                        vm.dataType = 'float';
                        vm.operators = ["==", "!=", ">", ">=", "<", "<="];
                        break;
                    case 'byte':
                        vm.dataType = 'byte';
                        vm.operators = ["=="];
                        break;
                    case 'text':
                        vm.dataType = 'text';
                        vm.operators = ["==", "!=", "contains", "contentEquals", "endsWith", "isEmpty"];
                        break;
                    case 'longtext':
                        vm.dataType = 'longtext';
                        vm.operators = ["==", "!=", "contains", "contentEquals", "endsWith", "isEmpty"];
                        break;
                    case 'richtext':
                        vm.dataType = 'richtext';
                        vm.operators = ["==", "!=", "contains", "contentEquals", "endsWith", "isEmpty"];
                        break;
                    case 'list':
                        vm.dataType = 'list';
                        vm.operators = ["=="];
                        break;
                    case 'time':
                        vm.dataType = 'time';
                        vm.operators = ["compareTo", "equals", "before", "after"];
                        break;
                    case 'timestamp':
                        vm.dataType = 'timestamp';
                        vm.operators = ["compareTo", "equals", "before", "after"];
                        break;
                    case 'currency':
                        vm.dataType = 'currency';
                        vm.operators = ["==", "!=", "contains", "contentEquals", "endsWith", "isEmpty"];
                        break;
                    case 'image':
                        vm.dataType = 'image';
                        vm.operators = ["=="];
                        break;
                    case 'attachment':
                        vm.dataType = 'attachment';
                        vm.operators = ["=="];
                        break;
                    case 'hyperlink':
                        vm.dataType = 'hyperlink';
                        vm.operators = ["==", "!=", "contains", "contentEquals", "endsWith", "isEmpty"];
                        break;
                    case 'formula':
                        vm.dataType = 'formula';
                        vm.operators = ["==", "!=", "contains", "contentEquals", "endsWith", "isEmpty"];
                        break;
                    default:

                }
            };

            vm.onAddItem = onAddItem;
            function onAddItem() {
                var criteria = angular.copy(vm.criteria);
                var newRow = angular.copy(vm.criteriaList);
                newRow.properties = [];
                var objectTypeList = [];
                angular.forEach($rootScope.entityProerties.get(vm.className), function(objectType){
                    objectTypeList.push(objectType.name);
                });
                newRow.properties = $rootScope.entityProerties.get(vm.className);
                if(vm.permission.subType != null){
                    var subTypeClass = $rootScope.typeNames[(vm.permission.subType).toLowerCase()];
                    var subTypeList = $rootScope.entityProerties.get(subTypeClass);
                    angular.forEach(subTypeList, function(subType){
                        if(objectTypeList.indexOf(subType.name) == -1) {
                            newRow.properties.push(subType);
                        }
                    });
                }
                newRow.attributes = loadAttributes(vm.permission.objectType);
                criteria.newRow.push(newRow);
                vm.predicates.push(criteria);
            }

            vm.checkProperty = checkProperty;
            function checkProperty(criteria, property, index) {
                criteria.isEnum = false;
                vm.enums = [];
                var dataTypeUpperCaseValue = property.type;
                dataTypeOperators(dataTypeUpperCaseValue.toLowerCase());
                if (property.type == 'Object') {
                    var newRow = angular.copy(vm.criteriaList);
                    newRow.properties = $rootScope.entityProerties.get(property.objectType);
                    criteria.newRow.push(newRow);
                }
                if (property.type == 'Enum') {
                    criteria.isEnum = true;
                    criteria.enums = $rootScope.enums.get(property.objectType);
                }
            }

            vm.checkAttribute = checkAttribute;
            function checkAttribute(criteria, attribute, index) {
                criteria.isEnum = false;
                var dataTypeUpperCaseValue = attribute.dataType;
                dataTypeOperators(dataTypeUpperCaseValue.toLowerCase());
                if (attribute.dataType == 'OBJECT') {
                    var newRow = angular.copy(vm.criteriaList);
                    newRow.properties = $rootScope.entityProerties.get($rootScope.typeNames[attribute.refType.toLowerCase()]);
                    newRow.attributes = loadAttributes(attribute.refType.toLowerCase());
                    criteria.newRow.push(newRow);
                }
            }

            function loadAttributes(objectType) {
                var attributes = [];
                var typeValue = $rootScope.loadSubTypes(objectType);
                if (typeValue != null && typeValue != undefined && typeValue != "") {
                    ItemTypeService.getAllTypeAttributes(typeValue).then(
                        function (data) {
                            angular.forEach(data, function (value) {
                                attributes.push(value);
                            });
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    );
                }
                return attributes;
            }


            function createCriteria() {
                vm.finalList = [];
                angular.forEach(vm.predicates, function (predicate, index) {
                    vm.lhss = [];
                    var criteriaList = angular.copy(vm.criteriaList);
                    angular.forEach(predicate.newRow, function (item) {
                        var lhs = angular.copy(vm.lhs);
                        if (item.type == 'Properties') {
                            lhs.type = 'PROPERTY';
                            lhs.dataType = item.property.type;
                            lhs.name = item.property.name;
                            lhs.objectType = item.property.objectType;
                        }
                        if (item.type == 'Attributes') {
                            lhs.type = 'ATTRIBUTE';
                            lhs.dataType = item.attribute.dataType;
                            lhs.name = item.attribute.name;
                        }
                        vm.lhss.push(lhs);
                    });
                    criteriaList.lhs = setLhs();
                    criteriaList.operator = predicate.operator;
                    criteriaList.rhs = predicate.rhs;
                    if (index != vm.predicates.length - 1) criteriaList.mergeType = "and";
                    vm.finalList.push(criteriaList);
                });
                SecurityPermissionService.saveCriteria(vm.permission.id, JSON.stringify(vm.finalList)).then(
                    function (data) {
                        $rootScope.showSuccessMessage(vm.criteriaSaved);
                        $scope.$off('app.securityPermission.criteria', createCriteria);
                        $scope.callback();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            function setLhs() {
                var templhs = null;
                for (var i = vm.lhss.length; i >= 0; i--) {
                    if (templhs == null) {
                        templhs = vm.lhss[i];
                    } else {
                        vm.lhss[i].object = templhs;
                        templhs = vm.lhss[i];
                    }
                }
                return templhs;
            }

            function loadCriteria() {
                if (vm.permission.criteria != null || vm.permission.criteria != undefined || vm.permission.criteria != "") {
                    angular.forEach(JSON.parse(vm.permission.criteria), function (value, index) {
                        var criteria = angular.copy(vm.criteria);
                        criteria.operator = value.operator;
                        criteria.rhs = value.rhs;
                        vm.lhsList = [];
                        getCriteria(value.lhs);
                        angular.forEach(vm.lhsList, function (val, i) {
                            var newRow = {};
                            if (val.dataType != null && val.dataType != "" && val.dataType != undefined) {
                                var dataType = val.dataType;
                                criteria.selectedDataType = dataType.toLowerCase();
                            }

                            if (val.type == "PROPERTY") {
                                newRow.type = 'Properties';
                                if (i == 0) {
                                    newRow.properties = $rootScope.entityProerties.get(vm.className);
                                } else {
                                    if (vm.lhsList[i - 1].objectType != undefined) newRow.properties = $rootScope.entityProerties.get(vm.lhsList[i - 1].objectType);
                                    else newRow.properties = $rootScope.entityProerties.get($rootScope.typeNames[vm.lhsList[i - 1].name]);
                                }
                                newRow.property = {
                                    "name": val.name,
                                    "type": val.dataType,
                                    "objectType": val.objectType
                                };
                                if (val.dataType == "Enum") {
                                    criteria.isEnum = true;
                                    criteria.enums = $rootScope.enums.get(val.objectType);
                                } else if (val.dataType == "Person") {
                                    criteria.isPerson = true;
                                    criteria.person = value.person;
                                    criteria.persons = $rootScope.permissionPersons;
                                } else {
                                    criteria.isEnum = false;
                                    criteria.isPerson = false;
                                }
                                criteria.newRow.push(newRow);
                            }
                            if (val.type == "ATTRIBUTE") {
                                newRow.type = 'Attributes';
                                criteria.isEnum = false;
                                criteria.isPerson = false;
                                if (i == 0) newRow.attributes = loadAttributes(vm.permission.objectType);
                                else newRow.attributes = loadAttributes(vm.lhsList[i - 1].objectType);
                                newRow.attribute = {"name": val.name};
                                if (val.dataType == "Person") {
                                    criteria.isPerson = true;
                                    criteria.person = value.person;
                                    criteria.persons = $rootScope.permissionPersons;
                                }
                                criteria.newRow.push(newRow);
                            }
                        });
                        vm.predicates.push(criteria);
                    });
                }
                if (vm.permission.privilege == "create") vm.types = ['Properties'];
                else vm.types = ['Properties', 'Attributes'];
            }

            function getCriteria(value) {
                var lhs = angular.copy(vm.lhs);
                lhs.name = value.name;
                lhs.dataType = value.dataType;
                lhs.type = value.type;
                lhs.objectType = value.objectType;
                vm.lhsList.push(lhs);
                if (value.object != undefined) getCriteria(value.object);
            }

            (function () {
                $scope.$on('app.securityPermission.criteria', createCriteria);
                loadCriteria();
            })();
        }
    }
);
