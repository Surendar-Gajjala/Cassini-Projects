define(
    [
        'app/desktop/modules/item/item.module',
        'app/desktop/modules/classification/directive/classificationTreeDirective',
        'app/desktop/modules/classification/directive/classificationTreeController',
        'app/desktop/modules/change/eco/directive/changeDirective',
        'app/desktop/modules/pqm/directives/qualityTypeDirective',
        'app/desktop/modules/directives/pmObjectTypeDirective',
        'app/desktop/modules/directives/mesObjectTypeDirective',
        'app/desktop/modules/mfr/directive/manufacturerDirective',
        'app/desktop/modules/mfr/mfrparts/directive/manufacturerPartDirective',
        'app/desktop/modules/mfr/supplier/directive/supplierDirective',
        'app/desktop/modules/directives/pgcObjectTypeDirective',
        'app/desktop/modules/securityPermission/securityPermissionService',
        'app/desktop/modules/directives/customObjectTypeDirective',
        'app/shared/services/core/itemTypeService'
    ],
    function (module) {
        module.controller('NewPermissionController', NewPermissionController);

        function NewPermissionController($scope, $rootScope, $sce, $i18n, $translate, $timeout, SecurityPermissionService, ItemTypeService) {

            var vm = this;
            var parsed = angular.element("<div></div>");
            vm.enterValueTitle = $sce.trustAsHtml(parsed.html($translate.instant("ENTER_VALUE_TITLE")).html());
            vm.enterNameTitle = $sce.trustAsHtml(parsed.html($translate.instant("ITEM_NAME_VALIDATION")).html());
            vm.enterObjectTypeeTitle = $sce.trustAsHtml(parsed.html($translate.instant("OBJECT_TYPE_VALIDATION")).html());
            vm.enterPrivilegeTitle = $sce.trustAsHtml(parsed.html($translate.instant("PRIVILEGE_VALIDATION")).html());
            var updateSuccessMessage = $sce.trustAsHtml(parsed.html($translate.instant("UPDATED_PERMISSION_SUCCESS_MESSAGE")).html());
            var savedSuccessMessage = $sce.trustAsHtml(parsed.html($translate.instant("SAVED_PERMISSION_SUCCESS_MESSAGE")).html());
            vm.loadSubType = loadSubType;
            vm.onSelectType = onSelectType;
            vm.objectTypes = [];
            vm.privileges = [];
            vm.subTypes = [];
            vm.attributeGroups = [];
            vm.groupAttributes = [];
            vm.typeValue = null;
            vm.subTypeId = null;
            vm.mode = $scope.data.mode;

            $scope.select = $i18n.getValue("SELECT");

            function loadObjectTypesAndPrivileges() {
                SecurityPermissionService.getObjectTypes().then(
                    function (data) {
                        vm.objectTypes = data.objectTypes;
                        vm.privileges = data.privileges;
                        vm.loading = false;
                        loadCriteria();
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function getObject(id) {
                $rootScope.showBusyIndicator($('#rightSidePanel'));
                SecurityPermissionService.getObjectType(id).then(
                    function (data) {
                        getTypeAttributes(data);
                        $timeout(function () {
                            getEditGroupAttributes(vm.permission.attributeGroup);
                            if (vm.permission.attribute != null) {
                                vm.attribute = vm.permission.attribute.split(",");
                            }
                            $rootScope.hideBusyIndicator();
                        }, 3000);
                    }, function (error) {
                        $rootScope.showErrorMessage(error);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadSubType(objectType) {
                resetValues();
                var typeValue = objectType.toUpperCase();
                vm.typeValue = $rootScope.subTypeNames[typeValue];
                return vm.typeValue;
            }

            function loadSubTypeOnEdit(objectType) {
                var typeValue = objectType.toUpperCase();
                vm.typeValue = $rootScope.subTypeNames[typeValue];
                return vm.typeValue;
            }

            function resetValues() {
                vm.typeValue = null;
                vm.attributeGroups = [];
                vm.groupAttributes = [];
                vm.permission.subType = null;
                vm.permission.privilege = null;
                vm.permission.attribute = null;
                vm.permission.attributeGroup = null;
            }

            function onSelectType(type) {
                vm.permission.subType = type.name;
                vm.attributeGroups = [];
                vm.groupAttributes = [];
                vm.permission.attributeGroup = null;
                vm.permission.attribute = null;
                vm.permission.subTypeId = type.id;
                getTypeAttributes(type);
                $rootScope.hideBusyIndicator();
            }

            function getTypeAttributes(type) {
                SecurityPermissionService.getTypeAttributes(type.id, type.objectType).then(
                    function (data) {
                        $timeout(function () {
                            setAttributes(data);
                        }, 1000);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.getGroupAttributes = getGroupAttributes;
            function getGroupAttributes(group) {
                vm.permission.attribute = [];
                if (group != null) vm.groupAttributes = vm.groupAttributeMap.get(group).objectProperties;
            }

            function getEditGroupAttributes(group) {
                if (group != null) vm.groupAttributes = vm.groupAttributeMap.get(group).objectProperties;
            }

            function setAttributes(attributes) {
                vm.groupAttributeMap = new Hashtable();
                angular.forEach(attributes, function (attribute) {
                    if (attribute.attributeGroup != null) {
                        var groupAttribute = vm.groupAttributeMap.get(attribute.attributeGroup);
                        if (vm.attributeGroups.indexOf(attribute.attributeGroup) == -1) {
                            vm.attributeGroups.push(attribute.attributeGroup);
                        }
                        if (groupAttribute == null) {
                            var groupAttributes = {
                                groupName: attribute.attributeGroup,
                                objectProperties: [attribute.name]
                            };
                            vm.groupAttributeMap.put(attribute.attributeGroup, groupAttributes);
                        } else {
                            groupAttribute.objectProperties.push(attribute.name);
                            vm.groupAttributeMap.put(attribute.attributeGroup, groupAttribute)
                        }
                    } else {
                        vm.groupAttributes.push(attribute.name);
                    }
                });
            }

            function createPermission() {
                if (vm.predicates == null && vm.predicates.length == 0) {
                    vm.permission.criteria = null;
                } else {
                    vm.permission.criteria = createCriteria();
                }
                if (validate() && vm.isValid) {
                    vm.permission.attribute = vm.attribute.length > 0 ? vm.attribute.toString() : null;
                    $rootScope.showBusyIndicator($('.view-container'));
                    SecurityPermissionService.createSecutiryPermission(vm.permission).then(
                        function (data) {
                            $scope.$off('app.securityPermission.new', createPermission);
                            $rootScope.showSuccessMessage(savedSuccessMessage);
                            $rootScope.hideBusyIndicator();
                            $scope.callback();
                        }, function (error) {
                            $scope.$off('app.securityPermission.new', createPermission);
                            $rootScope.showWarningMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                } else {
                    $rootScope.hideBusyIndicator();
                }
            }

            function updatePermission() {
                if (vm.permission.attributeGroup != null && vm.permission.attributeGroup.trim() == "") vm.permission.attributeGroup = null;
                if (vm.predicates == null && vm.predicates.length == 0) {
                    vm.permission.criteria = null;
                } else {
                    vm.permission.criteria = createCriteria();
                }
                if (validate() && vm.isValid) {
                    vm.permission.attribute = vm.attribute.length > 0 ? vm.attribute.toString() : null;
                    $rootScope.showBusyIndicator($('.view-container'));
                    SecurityPermissionService.updateSecutiryPermission(vm.permission).then(
                        function (data) {
                            $scope.$off('app.securityPermission.update', updatePermission);
                            $rootScope.showSuccessMessage(updateSuccessMessage);
                            $rootScope.hideBusyIndicator();
                            $scope.callback();
                        }, function (error) {
                            $scope.$off('app.securityPermission.update', updatePermission);
                            $rootScope.showWarningMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                }
                else {
                    $rootScope.hideBusyIndicator();
                }

            }

            function validate() {
                var valid = true;
                if (vm.permission.name == null || vm.permission.name == undefined || vm.permission.name == "") {
                    $rootScope.showWarningMessage(vm.enterNameTitle);
                    valid = false;
                }
                else if (vm.permission.objectType == null || vm.permission.objectType == undefined || vm.permission.objectType == "") {
                    $rootScope.showWarningMessage(vm.enterObjectTypeeTitle);
                    valid = false;
                }
                else if (vm.permission.privilege == null || vm.permission.privilege == undefined || vm.permission.privilege == "") {
                    $rootScope.showWarningMessage(vm.enterPrivilegeTitle);
                    valid = false;
                }
                return valid;
            }

            /* Criteria related code */

            vm.predicates = [];
            vm.predicates1 = [];
            var finalList = [];
            vm.lhsList = [];
            vm.newRowList = [];
            vm.operators = [];
            vm.selectTitle = $i18n.getValue("SELECT");
            vm.criteriaSaved = $i18n.getValue("CRITERIA_SAVED");
            var addCriteria = $i18n.getValue("ADD_CRITERIA");
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
            var commonTextOperators = [
                {"name": "==", "type": "operator"},
                {"name": "!=", "type": "operator"},
                {"name": "contains", "type": "method"},
                {"name": "contentEquals", "type": "method"},
                {"name": "endsWith", "type": "method"},
                {"name": "isEmpty", "type": "method"},
                {"name": "isNotEmpty", "type": "method"},
                {"name": "startsWith", "type": "method"}
            ];
            var commonNumberOperators = [
                {"name": "==", "type": "operator"},
                {"name": "!=", "type": "operator"},
                {"name": ">", "type": "operator"},
                {"name": ">=", "type": "operator"},
                {"name": "<", "type": "operator"},
                {"name": "<=", "type": "operator"},
                {"name": "isEmpty", "type": "method"},
                {"name": "isNotEmpty", "type": "method"}
            ];
            var commonDateOperators = [
                {"name": "equals", "type": "method"},
                {"name": "before", "type": "method"},
                {"name": "after", "type": "method"},
                {"name": "isEmpty", "type": "method"},
                {"name": "isNotEmpty", "type": "method"}
            ];
            var equalsAndNotEqualsOperator = [
                {"name": "==", "type": "operator"},
                {"name": "!=", "type": "operator"},
                {"name": "isEmpty", "type": "method"},
                {"name": "isNotEmpty", "type": "method"}
            ];
            var defaultOperators = [
                {"name": "isEmpty", "type": "method"},
                {"name": "isNotEmpty", "type": "method"}
            ];
            vm.permissionRow = {
                name: null,
                description: null,
                objectType: null,
                subType: null,
                subTypeId: null,
                attributeGroup: null,
                attribute: null,
                privilege: null,
                module: null,
                criteria: null,
                privilegeType: "GRANTED"
            };
            vm.attribute = {};
            vm.dataType = null;
            vm.values = ["true", "false"];
            vm.onAddItem = onAddItem;
            vm.checkProperty = checkProperty;
            vm.checkAttribute = checkAttribute;
            vm.deleteItem = deleteItem;

            function validatePredicate() {
                vm.isValid = true;
                angular.forEach(vm.predicates, function (predicate) {
                    if (!predicate.isPerson) {
                        if ((predicate.operator == undefined || predicate.operator == null) && vm.isValid) {
                            $rootScope.showWarningMessage(addCriteria);
                            vm.isValid = false;
                        } else if (!(predicate.operator.name == 'isEmpty' || predicate.operator.name == 'isNotEmpty') && (predicate.rhs == undefined || predicate.rhs == null) && vm.isValid) {
                            $rootScope.showWarningMessage(addCriteria);
                            vm.isValid = false;
                        }
                    } else if (predicate.isPerson) {
                        if ((predicate.operator == undefined || predicate.operator == null) && vm.isValid) {
                            $rootScope.showWarningMessage(addCriteria);
                            vm.isValid = false;
                        } else if (!(predicate.operator.name == 'isEmpty' || predicate.operator.name == 'isNotEmpty') && (predicate.person == undefined || predicate.person == null) && vm.isValid) {
                            $rootScope.showWarningMessage(addCriteria);
                            vm.isValid = false;
                        }
                    }

                });
                return vm.isValid;
            }

            function createCriteria() {
                var finalList = [];
                if (validatePredicate(vm.predicates)) {
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
                                if (item.attribute.dataType != undefined) {
                                    lhs.name = item.attribute.name;
                                    lhs.dataType = item.attribute.dataType;
                                    if (lhs.dataType == 'OBJECT') lhs.objectType = item.attribute.refType.toLowerCase()
                                } else {
                                    lhs.name = item.attribute.name;
                                    lhs.dataType = item.attribute.type;
                                    lhs.objectType = item.attribute.objectType;
                                }

                            }
                            vm.lhss.push(lhs);
                        });
                        criteriaList.lhs = setLhs();
                        criteriaList.operator = predicate.operator;
                        criteriaList.person = predicate.person;
                        criteriaList.rhs = predicate.rhs;
                        if (index != vm.predicates.length - 1) criteriaList.mergeType = "and";
                        finalList.push(criteriaList);
                    });
                }
                return JSON.stringify(finalList);
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
                                dataTypeOperators(criteria.selectedDataType);
                                criteria.dataType = vm.dataType;
                                criteria.operators = vm.operators;
                            }

                            if (val.type == "PROPERTY") {
                                newRow.type = 'Properties';
                                if (i == 0) {
                                    newRow.properties = $rootScope.entityProerties.get($rootScope.typeNames[vm.permission.objectType]);
                                } else {
                                    var parentType = vm.lhsList[i - 1];
                                    if (parentType.objectType != undefined) newRow.properties = $rootScope.entityProerties.get(parentType.objectType);
                                    else newRow.properties = $rootScope.entityProerties.get($rootScope.typeNames[vm.lhsList[i - 1].name]);
                                    if (parentType.type == 'ATTRIBUTE') newRow.properties = $rootScope.entityProerties.get($rootScope.typeNames[parentType.objectType]);
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
                                criteria.isEnum = false;
                                criteria.isPerson = false;
                                newRow.type = 'Attributes';
                                if (i == 0) newRow.attributes = loadAttributes(vm.permission.objectType);
                                else newRow.attributes = loadAttributes(vm.lhsList[i - 1].objectType);
                                newRow.attribute = {
                                    "name": val.name,
                                    "type": val.dataType,
                                    "objectType": val.objectType
                                };
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


            function dataTypeOperators(dataType) {
                vm.operators = [];
                vm.dataType = null;
                switch (dataType) {
                    case 'string':
                        vm.dataType = 'string';
                        vm.operators = commonTextOperators;
                        break;
                    case 'integer':
                        vm.dataType = 'integer';
                        vm.operators = commonNumberOperators;
                        break;
                    case 'date':
                        vm.dataType = 'date';
                        vm.operators = commonDateOperators;
                        break;
                    case 'object':
                        vm.dataType = 'object';
                        vm.operators = equalsAndNotEqualsOperator;
                        break;
                    case 'boolean':
                        vm.dataType = 'boolean';
                        vm.operators = [{"name": "==", "type": "operator"}];
                        break;
                    case 'double':
                        vm.dataType = 'double';
                        vm.operators = commonNumberOperators;
                        break;
                    case 'long':
                        vm.dataType = 'long';
                        vm.operators = commonNumberOperators;
                        break;
                    case 'float':
                        vm.dataType = 'float';
                        vm.operators = commonNumberOperators;
                        break;
                    case 'text':
                        vm.dataType = 'text';
                        vm.operators = commonTextOperators;
                        break;
                    case 'longtext':
                        vm.dataType = 'longtext';
                        vm.operators = commonTextOperators;
                        break;
                    case 'richtext':
                        vm.dataType = 'richtext';
                        vm.operators = commonTextOperators;
                        break;
                    case 'list':
                        vm.dataType = 'list';
                        vm.operators = equalsAndNotEqualsOperator;
                        break;
                    case 'time':
                        vm.dataType = 'time';
                        vm.operators = commonDateOperators;
                        break;
                    case 'timestamp':
                        vm.dataType = 'timestamp';
                        vm.operators = commonDateOperators;
                        break;
                    case 'currency':
                        vm.dataType = 'currency';
                        vm.operators = commonNumberOperators;
                        break;
                    case 'hyperlink':
                        vm.dataType = 'hyperlink';
                        vm.operators = commonTextOperators;
                        break;
                    case 'enum':
                        vm.dataType = 'enum';
                        vm.operators = equalsAndNotEqualsOperator;
                        break;
                    case 'person':
                        vm.dataType = 'person';
                        vm.operators = equalsAndNotEqualsOperator;
                        break;
                    default:
                        vm.dataType = dataType;
                        vm.operators = defaultOperators;
                }
            }

            function onAddItem() {
                vm.className = $rootScope.typeNames[vm.permission.objectType];
                var criteria = angular.copy(vm.criteria);
                var newRow = angular.copy(vm.criteriaList);
                newRow.properties = [];
                var objectTypeList = [];
                angular.forEach($rootScope.entityProerties.get(vm.className), function (objectType) {
                    objectTypeList.push(objectType.name);
                });
                newRow.properties = $rootScope.entityProerties.get(vm.className);
                if (vm.permission.subType != null) {
                    var subTypeClass = $rootScope.typeNames[(vm.permission.subType).toLowerCase()];
                    var subTypeList = $rootScope.entityProerties.get(subTypeClass);
                    angular.forEach(subTypeList, function (subType) {
                        if (objectTypeList.indexOf(subType.name) == -1) {
                            newRow.properties.push(subType);
                        }
                    });
                }
                newRow.attributes = loadAttributes(vm.permission.objectType);
                criteria.newRow.push(newRow);
                vm.predicates.push(criteria);
            }

            function checkProperty(criteria, property, index) {
                criteria.isEnum = false;
                criteria.isPerson = false;
                var dataTypeUpperCaseValue = property.type;
                dataTypeOperators(dataTypeUpperCaseValue.toLowerCase());
                criteria.dataType = vm.dataType;
                criteria.operators = vm.operators;
                criteria.selectedDataType = vm.dataType;
                if (property.type == 'Object') {
                    var newRow = angular.copy(vm.criteriaList);
                    newRow.properties = $rootScope.entityProerties.get(property.objectType);
                    criteria.newRow.push(newRow);
                }
                if (property.type == 'Enum') {
                    criteria.isEnum = true;
                    criteria.enums = $rootScope.enums.get(property.objectType);
                }
                if (property.type == 'Person') {
                    criteria.isPerson = true;
                    criteria.persons = $rootScope.permissionPersons;
                }
            }

            function checkAttribute(criteria, attribute, index) {
                criteria.isEnum = false;
                criteria.isPerson = false;
                var dataTypeUpperCaseValue = attribute.dataType;
                dataTypeOperators(dataTypeUpperCaseValue.toLowerCase());
                criteria.dataType = vm.dataType;
                criteria.operators = vm.operators;
                criteria.selectedDataType = vm.dataType;
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

            function deleteItem(index) {
                vm.predicates.splice(index, 1);
            }

            (function () {
                if (vm.mode == 'edit') {
                    vm.permission = angular.copy(vm.permissionRow);
                    vm.permission = $scope.data.permission;
                    if (vm.permission.subTypeId != null) {
                        getObject(vm.permission.subTypeId);
                    }
                    $timeout(function () {
                        if (vm.permission.subType != null) loadSubTypeOnEdit(vm.permission.objectType);
                        else $rootScope.hideBusyIndicator();
                    }, 2000);
                    $scope.$on('app.securityPermission.update', updatePermission);
                } else {
                    vm.permission = angular.copy(vm.permissionRow);
                    $scope.$on('app.securityPermission.new', createPermission);
                }
                loadObjectTypesAndPrivileges();
            })();
        }
    }
)
;