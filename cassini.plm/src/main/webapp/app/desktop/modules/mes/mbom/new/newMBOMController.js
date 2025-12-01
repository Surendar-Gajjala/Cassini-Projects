define(
    [
        'app/desktop/modules/mes/mes.module',
        'moment',
        'moment-timezone-with-data',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/desktop/modules/directives/changeTypeDirective',
        'app/desktop/modules/directives/mesObjectTypeDirective',
        'app/shared/services/core/workflowDefinitionService',
        'app/shared/services/core/mbomService',
        'app/shared/services/core/mesObjectTypeService',
        'app/shared/services/core/classificationService',
        'app/shared/services/core/qualityTypeService',
        'app/shared/services/core/itemService'
    ],
    function (module) {

        module.controller('NewMBOMController', NewMBOMController);

        function NewMBOMController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce, CommonService, MESObjectTypeService,
                                   AutonumberService, LoginService, WorkflowDefinitionService, ObjectTypeAttributeService, MBOMService, ClassificationService,
                                   QualityTypeService, ItemService) {

            var vm = this;
            var parsed = angular.element("<div></div>");
            $scope.selectManufacturer = parsed.html($translate.instant("SELECT")).html();
            $scope.selectWorkflowTitle = parsed.html($translate.instant("SELECT_WORKFLOW")).html();
            var mbomTypeValidation = parsed.html($translate.instant("TYPE_VALIDATION")).html();
            var mbomNumberValidation = parsed.html($translate.instant("PLEASE_ENTER_NUMBER")).html();
            var mbomNameValidation = parsed.html($translate.instant("PLEASE_ENTER_NAME")).html();
            var pleaseSelectItem = parsed.html($translate.instant("PLEASE_SELECT_ITEM")).html();
            var pleaseSelectItemRevision = parsed.html($translate.instant("PLEASE_SELECT_ITEM_REVISION")).html();

            var mbomCreatedMsg = parsed.html($translate.instant("MBOM_CREATED_MESSAGE")).html();

            vm.newMBOM = {
                id: null,
                type: null,
                item: null,
                number: null,
                name: null,
                description: null,
                workflow: null,
                itemRevision: null
            };
            vm.onSelectType = onSelectType;

            function onSelectType(itemType) {
                if (itemType != null && itemType != undefined) {
                    vm.newMBOM.type = itemType;
                    autoNumber();
                }
            }

            function autoNumber() {
                if (vm.newMBOM.type != null && vm.newMBOM.type.autoNumberSource != null) {
                    var source = vm.newMBOM.type.autoNumberSource;
                    AutonumberService.getNextNumberByName(source.name).then(
                        function (data) {
                            vm.newMBOM.number = data;
                            loadMBOMTypeAttributes();
                            loadWorkflows();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function loadWorkflows() {
                MBOMService.getMESTypeWorkflows(vm.newMBOM.type.id, 'MANUFACTURING').then(
                    function (data) {
                        vm.workflows = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }


            function loadMBOMTypeAttributes() {
                vm.attributes = [];
                MESObjectTypeService.getMesObjectAttributesWithHierarchy(vm.newMBOM.type.id).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newMBOM.id,
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


            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            vm.filters = {
                partNumber: null,
                partName: null,
                description: null,
                freeTextSearch: true,
                searchQuery: null
            };

            vm.selectedItem = null;
            vm.onSelectItem = onSelectItem;
            function onSelectItem(item) {
                vm.selectedItem = item;
                vm.newMBOM.itemRevision = null;
                if (item.itemRevisions.length == 1) {
                    vm.newMBOM.itemRevision = item.itemRevisions[0].id;
                }
            }

            function create() {
                if (validate()) {
                    vm.imageAttributes = [];
                    vm.attachmentAttributes = [];
                    vm.images = new Hashtable();
                    vm.attachments = new Hashtable();
                    angular.forEach(vm.attributes, function (attribute) {
                        if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                            vm.images.put(attribute.id.attributeDef, attribute.imageValue);
                            vm.imageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length > 0) {
                            vm.attachments.put(attribute.id.attributeDef, attribute.attachmentValues);
                            vm.attachmentAttributes.push(attribute);
                            attribute.attachmentValues = [];
                        }
                    });
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    MBOMService.createMBOM(vm.newMBOM).then(
                        function (data) {
                            vm.newMBOM = data;
                            saveAttributes().then(
                                function (attributes) {
                                    $scope.callback(vm.newMBOM);
                                    $rootScope.hideBusyIndicator();
                                    $rootScope.hideSidePanel();
                                    $rootScope.showSuccessMessage(mbomCreatedMsg);
                                    vm.newMBOM = {
                                        id: null,
                                        type: null,
                                        item: null,
                                        number: null,
                                        name: null,
                                        description: null,
                                        workflow: null,
                                        itemRevision: null
                                    };
                                }
                            )
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                }


                function validate() {
                    var valid = true;
                    if (vm.newMBOM.type == null || vm.newMBOM.type == undefined || vm.newMBOM.type == "") {
                        $rootScope.showWarningMessage(mbomTypeValidation);
                        valid = false;
                    } else if (vm.newMBOM.number == null || vm.newMBOM.number == undefined || vm.newMBOM.number == "") {
                        $rootScope.showWarningMessage(mbomNumberValidation);
                        valid = false;
                    } else if (vm.newMBOM.name == null || vm.newMBOM.name == undefined || vm.newMBOM.name == "") {
                        $rootScope.showWarningMessage(mbomNameValidation);
                        valid = false;
                    } else if (vm.newMBOM.item == null || vm.newMBOM.item == undefined || vm.newMBOM.item == "") {
                        $rootScope.showWarningMessage(pleaseSelectItem);
                        valid = false;
                    } else if (vm.newMBOM.itemRevision == null || vm.newMBOM.itemRevision == undefined || vm.newMBOM.itemRevision == "") {
                        $rootScope.showWarningMessage(pleaseSelectItemRevision);
                        valid = false;
                    } else if (vm.attributes.length > 0 && !validateAttributes()) {
                        valid = false;
                    } else if (vm.mbomRequiredProperties.length > 0 && !validateCustomAttributes()) {
                        valid = false;
                    } else if (!$rootScope.checkAttributeValidations(vm.attributes)) {
                        valid = false;
                    }
                    return valid;
                }
            }

            var attributeRequired = $translate.instant("ATTRIBUTE_REQUIRED");

            function validateAttributes() {
                var valid = true;
                angular.forEach(vm.attributes, function (attribute) {
                    if (valid) {
                        if (attribute.attributeDef.required == true && attribute.attributeDef.dataType != 'BOOLEAN' &&
                            attribute.attributeDef.dataType != 'TIMESTAMP') {
                            if (!$rootScope.checkAttribute(attribute)) {
                                valid = false;
                                $rootScope.showWarningMessage(attribute.attributeDef.name + ":" + attributeRequired);
                            }
                        }
                    }
                });
                return valid;
            }

            function validateCustomAttributes() {
                var valid = true;
                angular.forEach(vm.mbomRequiredProperties, function (attribute) {
                    if (valid) {
                        if (attribute.attributeDef.required == true && attribute.attributeDef.dataType != 'BOOLEAN' &&
                            attribute.attributeDef.dataType != 'TIMESTAMP') {
                            if (!$rootScope.checkAttribute(attribute)) {
                                valid = false;
                                $rootScope.showWarningMessage(attribute.attributeDef.name + ":" + attributeRequired);
                            }
                        }
                    }
                });
                return valid;
            }

            function saveAttributes() {
                var defered = $q.defer();
                angular.forEach(vm.attributes, function (attribute) {
                    attribute.id.objectId = vm.newMBOM.id;
                });
                MBOMService.saveMBOMAttributes(vm.attributes).then(
                    function (data) {
                        if (vm.imageAttributes.length > 0 || vm.attachmentAttributes.length > 0) {
                            angular.forEach(vm.imageAttributes, function (imgAtt) {
                                    ClassificationService.updateAttributeImageValue("MESOBJECTTYPE", vm.newMBOM.id, imgAtt.id.attributeDef, vm.images.get(imgAtt.id.attributeDef)).then(
                                        function (data) {
                                            defered.resolve();
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    )
                                }
                            )

                            angular.forEach(vm.attachmentAttributes, function (imgAtt) {
                                    ClassificationService.updateAttributeAttachmentValues("MESOBJECTTYPE", vm.newMBOM.id, imgAtt.id.attributeDef, vm.attachments.get(imgAtt.id.attributeDef)).then(
                                        function (data) {
                                            defered.resolve();
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    )
                                }
                            )
                        } else {
                            defered.resolve();
                        }
                    }
                )
                return defered.promise;
            }

            function loadReleasedItems() {
                ItemService.getReleasedItemsByHasBom(true).then(
                    function (data) {
                        vm.items = data;
                    }
                )
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

            function loadMBOMCustomProperties() {
                vm.mbomProperties = [];
                vm.mbomRequiredProperties = [];
                ObjectTypeAttributeService.getObjectTypeAttributesByType("MBOM").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newMBOM.id,
                                    attributeDef: attribute.id

                                },
                                attributeDef: attribute,
                                listValue: null,
                                stringValue: null,
                                mlistValue: [],
                                newListValue: null,
                                timeValue: null,
                                timestampValue: null,
                                listValueEditMode: false,
                                booleanValue: false,
                                dateValue: null,
                                imageValue: null,
                                refValue: null,
                                ref: null,
                                attachmentValues: []
                            };
                            if (attribute.dataType == "TIMESTAMP") {
                                att.timestampValue = moment(new Date()).format("DD/MM/YYYY, HH:mm:ss");
                            }
                            if (attribute.dataType == "TEXT") {
                                att.stringValue = attribute.defaultTextValue;
                            }
                            if (attribute.dataType == "LIST" && !attribute.listMultiple && attribute.defaultListValue != null) {
                                att.listValue = attribute.defaultListValue;
                            }
                            if (attribute.dataType == "LIST" && attribute.listMultiple && attribute.defaultListValue != null) {
                                att.mlistValue.push(attribute.defaultListValue);
                            }
                            if (attribute.lov != null) {
                                att.lovValues = attribute.lov.values;
                            }
                            if (attribute.required == false) {
                                vm.mbomProperties.push(att);
                            } else {
                                vm.mbomRequiredProperties.push(att);
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {
                loadReleasedItems();
                loadPersons();
                loadMBOMCustomProperties();
                $rootScope.$on('app.mbom.new', create);
            })();


        }

    });    