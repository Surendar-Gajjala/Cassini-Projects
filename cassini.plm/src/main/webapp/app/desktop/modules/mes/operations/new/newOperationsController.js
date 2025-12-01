define(
    [
        'app/desktop/modules/mes/mes.module',
        'moment',
        'moment-timezone-with-data',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/desktop/modules/directives/mesObjectTypeDirective',
        'app/shared/services/core/mesObjectTypeService',
        'app/shared/services/core/operationService',
        'app/shared/services/core/workCenterService'
    ],
    function (module) {

        module.controller('NewOperationsController', NewOperationsController);

        function NewOperationsController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce, OperationService, LoginService, AutonumberService, AttributeAttachmentService, MESObjectTypeService, WorkCenterService) {

            var vm = this;

            var parsed = angular.element("<div></div>");
            vm.selectedOperationType = null;
            vm.attributes = [];
            vm.validattributes = [];

            var itemTypeValidation = parsed.html($translate.instant("ITEM_TYPE_VALIDATION")).html();
            var itemNumberValidation = parsed.html($translate.instant("ITEM_NUMBER_VALIDATION")).html();
            var itemNameValidation = parsed.html($translate.instant("ITEM_NAME_VALIDATION")).html();
            var OperationManagerValidation = parsed.html($translate.instant("OPERATION_MANAGER_VALIDATION")).html();
            var pleaseEnter = parsed.html($translate.instant("PLEASE_ENTER")).html();
            vm.selectWorkCenterTitle = parsed.html($translate.instant("SELECT_WORKCENTER")).html();
            var workCenterValidation = parsed.html($translate.instant("WORKCENTER_VALIDATION")).html();

            vm.newOperation = {
                id: null,
                type: null,
                number: null,
                name: null,
                description: null,
                workCenter: null,
            }


            vm.autoNumber = autoNumber;
            vm.onSelectType = onSelectType;
            function onSelectType(operationType) {
                if (operationType != null && operationType != undefined) {
                    vm.selectedOperationType = operationType;
                    vm.newOperation.type = operationType;
                    autoNumber();
                }
            }

            function autoNumber() {
                if (vm.selectedOperationType != null) {
                    var source = vm.selectedOperationType.autoNumberSource;
                    AutonumberService.getNextNumberByName(source.name).then(
                        function (data) {
                            vm.newOperation.number = data;
                            loadOperationTypeAttributes();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }


            function createNewOperation() {
                create().then(function () {
                    vm.newOperation = {
                        id: null,
                        type: null,
                        number: null,
                        name: null,
                        description: null,
                    };
                    $scope.callback();
                    $rootScope.hideBusyIndicator();
                })

            }


            function validate() {
                console.log(vm.newOperation)
                var valid = true;
                if (vm.newOperation.type == null || vm.newOperation.type == undefined ||
                    vm.newOperation.type == "") {
                    $rootScope.showWarningMessage(itemTypeValidation);
                    valid = false;
                }

                else if (vm.newOperation.number == null || vm.newOperation.number == undefined ||
                    vm.newOperation.number == "") {
                    $rootScope.showWarningMessage(itemNumberValidation);
                    valid = false;
                }
                else if (vm.newOperation.name == null || vm.newOperation.name == undefined ||
                    vm.newOperation.name == "") {
                    $rootScope.showWarningMessage(itemNameValidation);
                    valid = false;
                }


                else if (!$rootScope.checkAttributeValidations(vm.attributes)) {
                    valid = false;
                }


                return valid;
            }

            function validateRequiredAttributes() {
                var valid = true;
                if (vm.attributes.length > 0) {
                    angular.forEach(vm.attributes, function (attribute) {
                        if (valid) {
                            if ($rootScope.checkAttribute(attribute)) {
                                valid = true;
                            } else {
                                valid = false;
                                $rootScope.showWarningMessage("Please enter" + " " + attribute.attributeDef.name);
                            }
                        }
                    })
                }
                return valid;
            }

            var insertedSuccefully = parsed.html($translate.instant("OPERATION_ADDED_SUCCESS")).html();

            function create() {
                var dfd = $q.defer();
                vm.validattributes = [];
                if (validate() && validateRequiredAttributes()) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    OperationService.createOperation(vm.newOperation).then(
                        function (data) {
                            vm.newOperation = data;
                            saveAttributes().then(
                                function (att) {
                                    vm.newOperation = {
                                        id: null,
                                        type: null,
                                        number: null,
                                        name: null,
                                        description: null,
                                    };
                                    vm.selectedOperationType = null;
                                    vm.attributes = [];
                                    $scope.callback();
                                    $rootScope.hideBusyIndicator();
                                    $rootScope.showSuccessMessage(insertedSuccefully);
                                }
                            )
                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }

                return dfd.promise;
            }


            function addOperationPropertyAttachment(attribute) {
                var propertyAttachmentIds = [];
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'OPERATIONTYPE', attachmentFile).then(
                        function (data) {
                            propertyAttachmentIds.push(data[0].id);
                        }
                    )
                })
                return propertyAttachmentIds;
            }


            function saveAttributes() {
                var defered = $q.defer();
                vm.imageAttributes = [];
                var images = new Hashtable();
                if (vm.attributes.length > 0) {
                    angular.forEach(vm.attributes, function (attribute) {
                        attribute.id.objectId = vm.newOperation.id;

                        if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                            images.put(attribute.id.attributeDef, attribute.imageValue);
                            vm.imageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'BOOLEAN' && attribute.booleanValue != true) {
                            attribute.booleanValue = false;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length != null) {
                            attribute.attachmentValues = addOperationPropertyAttachment(attribute)
                        }
                    });
                    $timeout(function () {
                        OperationService.saveOperationAttributes(vm.newOperation.id, vm.attributes).then(
                            function (data) {

                                if (vm.imageAttributes.length > 0) {
                                    angular.forEach(vm.imageAttributes, function (imgAtt) {
                                        OperationService.uploadImageAttribute(imgAtt.id.objectId, imgAtt.id.attributeDef, images.get(imgAtt.id.attributeDef)).then(
                                            function (data) {

                                                defered.resolve();
                                            }
                                        )
                                    })
                                } else {
                                    defered.resolve();
                                }

                            }, function () {
                                defered.reject();
                            })
                    }, 2000)
                } else {
                    defered.resolve();
                }
                return defered.promise;
            }


            function loadOperationTypeAttributes() {
                vm.attributes = [];
                MESObjectTypeService.getMesObjectAttributesWithHierarchy(vm.newOperation.type.id).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newOperation.id,
                                    attributeDef: attribute.id

                                },
                                attributeDef: attribute,
                                listValue: null,
                                mlistValue: [],
                                newListValue: null,
                                listValueEditMode: false,
                                booleanValue: false,
                                integerValue: null,
                                stringValue: null,
                                doubleValue: null,
                                imageValue: null,
                                refValue: null,
                                timestampValue: null,
                                ref: null,
                                attachmentValues: []
                            };
                            if (attribute.lov != null) {
                                att.lovValues = attribute.lov.values;
                            }
                            if (attribute.dataType == "TEXT") {
                                att.stringValue = attribute.defaultTextValue;
                            }
                            if (attribute.dataType == "LIST") {
                                att.listValue = attribute.defaultListValue;
                            }
                            if (attribute.dataType == "TIMESTAMP") {
                                att.timestampValue = moment(new Date()).format("DD/MM/YYYY, HH:mm:ss")
                            }
                            if (attribute.validations != null && attribute.validations != "") {
                                attribute.newValidations = JSON.parse(attribute.validations);
                            }
                            vm.attributes.push(att);
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );
            }

            function loadPersons() {
                vm.persons = [];
                LoginService.getAllLogins().then(
                    function (data) {
                        angular.forEach(data, function (login) {
                            if (login.isActive == true && login.external == false) {
                                vm.persons.push(login.person);
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }


            (function () {
                $rootScope.hideBusyIndicator();
                loadPersons();
                $rootScope.$on('app.operation.new', createNewOperation);

            })();
        }
    }
);