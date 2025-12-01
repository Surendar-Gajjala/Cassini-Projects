define(
    [
        'app/desktop/modules/rm/rm.module',
        'app/desktop/modules/rm/directive/requirementDirective',
        'app/shared/services/core/requirementsTypeService',
        'app/shared/services/core/specificationsService',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService'
    ],

    function (module) {
        module.controller('NewRequirementsController', NewRequirementsController);

        function NewRequirementsController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies,
                                           ObjectAttributeService, AttachmentService, ItemService,
                                           AttributeAttachmentService, SpecificationsService, RequirementsTypeService, ObjectTypeAttributeService, LoginService) {


            var parse = angular.element("<div></div>");
            var vm = this;
            vm.valid = true;
            var specId = $stateParams.specId;
            var item = $scope.data.specSection;
            vm.requirementRequiredProperties = [];
            vm.requirementProperties = [];
            vm.onSelectType = onSelectType;

            vm.newSpecRequirement = {
                id: null,
                specification: null,
                parent: null,
                type: 'REQUIREMENT',
                requirement: {
                    id: null,
                    objectNumber: null,
                    name: null,
                    description: null,
                    specification: null,
                    assignedTo: null,
                    plannedFinishDate: null,
                    actualFinishDate: null,
                    workflowDefinition: null

                }
            };

            vm.persons = [];
            var reqCreatedMsg = parse.html($translate.instant("REQUIREMENT_CREATE_MSG")).html();
            var typeValidation = parse.html($translate.instant("SPECIFICATION_TYPE_VALIDATION")).html();
            var nameValidation = parse.html($translate.instant("SPECIFICATION_NAME_VALIDATION")).html();
            var descriptionValidation = parse.html($translate.instant("DESCRIPTION_VALIDATION")).html();
            var pleaseEnter = parse.html($translate.instant("PLEASE_ENTER")).html();
            var itemWorkflowValidation = parse.html($translate.instant("ITEM_WORKFLOW_VALIDATION")).html();

            function createRequirement() {
                if (validate() && validateRequiredProperties() && validateRequiredAttributes()) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    vm.newSpecRequirement.specification = specId;
                    vm.newSpecRequirement.requirement.specification = specId;
                    if (item != null) {
                        vm.newSpecRequirement.parent = item.id;
                    }
                    if (vm.newSpecRequirement.requirement.workflowDefinition != null) {
                        vm.newSpecRequirement.requirement.workflowDefId = vm.newSpecRequirement.requirement.workflowDefinition.id;
                    }
                    SpecificationsService.createRequirement(vm.newSpecRequirement).then(
                        function (data) {
                            vm.newSpecRequirement = data;
                            saveObjectAttributes().then(
                                function (object) {
                                    saveAttributes().then(
                                        function (att) {
                                            vm.newSpecRequirement = {
                                                id: null,
                                                specification: null,
                                                parent: null,
                                                type: 'REQUIREMENT',
                                                requirement: {
                                                    id: null,
                                                    objectNumber: null,
                                                    name: null,
                                                    description: null
                                                }
                                            };
                                            vm.attributes = [];
                                            vm.requiredAttributes = [];
                                            vm.requirementProperties = [];
                                            vm.requirementRequiredProperties = [];
                                            $rootScope.hideBusyIndicator();
                                            loadObjectTypeAttributes();
                                            $rootScope.showSuccessMessage(reqCreatedMsg);
                                            $scope.callback(data);
                                        }
                                    )
                                }
                            )

                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            /*------Save ObjectAttributes ------*/
            function saveObjectAttributes() {
                var defered = $q.defer();
                var propertyImages = new Hashtable();
                vm.propertyImageAttributes = [];
                var objectAttrs = vm.requirementProperties.concat(vm.requirementRequiredProperties);
                if (objectAttrs.length > 0) {
                    angular.forEach(objectAttrs, function (attribute) {
                        attribute.id.objectId = vm.newSpecRequirement.requirement.id;
                        /* if (attribute.timeValue != null) {
                         attribute.timeValue = moment(attribute.timeValue).format("HH:mm:ss");
                         }*/
                        /*  if (attribute.attributeDef.dataType == "LIST" && !attribute.attributeDef.listMultiple && attribute.listValue.length > 0) {
                         var listValue = attribute.listValue;
                         attribute.listValue = [];
                         attribute.listValue.push(listValue);
                         }*/
                        if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                            propertyImages.put(attribute.id.attributeDef, attribute.imageValue);
                            vm.propertyImageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }

                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length > 0) {
                            attribute.attachmentValues = addSpecPropertyAttachment(attribute);
                        }
                    });
                    $timeout(function () {
                        ObjectAttributeService.saveItemObjectAttributes(vm.newSpecRequirement.requirement.id, objectAttrs).then(
                            function (data) {
                                if (vm.propertyImageAttributes.length > 0) {
                                    angular.forEach(vm.propertyImageAttributes, function (propImgAtt) {
                                        ObjectAttributeService.uploadObjectAttributeImage(propImgAtt.id.objectId, propImgAtt.id.attributeDef, propertyImages.get(propImgAtt.id.attributeDef)).then(
                                            function (data) {
                                                defered.resolve();
                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
                                             }
                                        )
                                    })
                                } else {
                                    defered.resolve();
                                }
                                defered.resolve();
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                                defered.reject();
                            }
                        )
                    }, 2000);
                } else {
                    defered.resolve();
                }
                return defered.promise;
            }

            function addSpecPropertyAttachment(attribute) {
                var propertyAttachmentIds = [];
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'REQUIREMENT', attachmentFile).then(
                        function (data) {
                            propertyAttachmentIds.push(data[0].id);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                         }
                    )
                })
                return propertyAttachmentIds;
            }

            /*-----Save Attributes ------*/
            function saveAttributes() {
                var defered = $q.defer();
                vm.imageAttributes = [];
                var images = new Hashtable();
                angular.forEach(vm.requiredAttributes, function (reqatt) {
                    vm.attributes.push(reqatt);
                })
                if (vm.attributes.length > 0) {
                    angular.forEach(vm.attributes, function (attribute) {
                        attribute.id.objectId = vm.newSpecRequirement.requirement.id;
                        /* if (attribute.attributeDef.dataType == "LIST" && !attribute.attributeDef.listMultiple && attribute.listValue.length > 0) {
                         var listValue = attribute.listValue;
                         attribute.listValue = [];
                         attribute.listValue.push(listValue);
                         }*/

                        if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                            images.put(attribute.id.attributeDef, attribute.imageValue);
                            vm.imageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length != null) {
                            attribute.attachmentValues = addAttachment(attribute)
                        }
                    });
                    $timeout(function () {
                        SpecificationsService.saveAttributes(vm.newSpecRequirement.requirement.id, vm.attributes).then(
                            function (data) {

                                if (vm.imageAttributes.length > 0) {
                                    angular.forEach(vm.imageAttributes, function (imgAtt) {
                                        SpecificationsService.saveImageValue(imgAtt.id.objectId, imgAtt.id.attributeDef, images.get(imgAtt.id.attributeDef)).then(
                                            function (data) {
                                                vm.newSpecRequirement = {
                                                    id: null,
                                                    specification: null,
                                                    parent: null,
                                                    type: 'REQUIREMENT',
                                                    requirement: {
                                                        id: null,
                                                        objectNumber: null,
                                                        name: null,
                                                        description: null
                                                    }
                                                };
                                                defered.resolve();
                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
                                                $rootScope.hideBusyIndicator();
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

            function addAttachment(attribute) {
                var attachmentIds = [];
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'REQUIREMENT', attachmentFile).then(
                        function (data) {
                            attachmentIds.push(data[0].id);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                         })
                })
                return attachmentIds;
            }

            function onSelectType(itemType) {
                if (itemType != null && itemType != undefined) {
                    vm.newSpecRequirement.requirement.type = itemType;
                    loadWorkflows();
                    loadAttributeDefs();
                }
            }

            function loadAttributeDefs() {
                vm.requiredAttributes = [];
                vm.attributes = [];
                RequirementsTypeService.getTypeAttributesWithHierarchy(vm.newSpecRequirement.requirement.type.id).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newSpecRequirement.id,
                                    attributeDef: attribute.id

                                },
                                attributeDef: attribute,
                                listValue: null,
                                mlistValue: [],
                                newListValue: null,
                                listValueEditMode: false,
                                booleanValue: false,
                                refValue: null,
                                timestampValue: null,
                                ref: null,
                                imageValue: null,
                                attachmentValues: []
                            };

                            if (attribute.dataType == "LIST") {
                                if (attribute.listMultiple == true) {
                                    att.mlistValue.push(attribute.defaultListValue);
                                }
                                else {
                                    att.listValue = attribute.defaultListValue;
                                }

                            }

                            if (attribute.dataType == "TEXT") {
                                att.stringValue = attribute.defaultTextValue;
                            }
                            if (attribute.dataType == "TIMESTAMP") {
                                att.timestampValue = moment(new Date()).format("DD/MM/YYYY, HH:mm:ss");
                            }
                            if (attribute.validations != null && attribute.validations != "") {
                                attribute.newValidations = JSON.parse(attribute.validations);
                            }
                            if (attribute.lov != null) {
                                att.lovValues = attribute.lov.values;
                            }
                            if (attribute.required == false) {
                                vm.attributes.push(att);
                            } else {
                                vm.requiredAttributes.push(att);
                            }

                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function validate() {
                var valid = true;
                if (vm.newSpecRequirement.requirement.type == null || vm.newSpecRequirement.requirement.type == undefined ||
                    vm.newSpecRequirement.requirement.type == "") {
                    $rootScope.showWarningMessage(typeValidation);
                    valid = false;
                }
                else if (vm.newSpecRequirement.requirement.name == null || vm.newSpecRequirement.requirement.name == undefined ||
                    vm.newSpecRequirement.requirement.name == "") {
                    $rootScope.showWarningMessage(nameValidation);
                    valid = false;
                } else if (vm.newSpecRequirement.requirement.description == null || vm.newSpecRequirement.requirement.description == undefined ||
                    vm.newSpecRequirement.requirement.description == "") {
                    $rootScope.showWarningMessage(descriptionValidation);
                    valid = false;
                } else if (!$rootScope.checkAttributeValidations(vm.attributes)) {
                    valid = false;
                } else if (!$rootScope.checkAttributeValidations(vm.requiredAttributes)) {
                    valid = false;
                }
                return valid;
            }

            function loadObjectTypeAttributes() {
                vm.requirementProperties = [];
                vm.requirementRequiredProperties = [];
                ObjectTypeAttributeService.getObjectTypeAttributesByType("REQUIREMENT").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newSpecRequirement.requirement.id,
                                    attributeDef: attribute.id

                                },
                                attributeDef: attribute,
                                listValue: null,
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

                            if (attribute.dataType == "LIST") {
                                if (attribute.listMultiple == true) {
                                    att.mlistValue.push(attribute.defaultListValue);
                                }
                                else {
                                    att.listValue = attribute.defaultListValue;
                                }

                            }

                            if (attribute.dataType == "TEXT") {
                                att.stringValue = attribute.defaultTextValue;
                            }
                            if (attribute.dataType == "TIMESTAMP") {
                                att.timestampValue = moment(new Date()).format("DD/MM/YYYY, HH:mm:ss");
                            }
                            if (attribute.lov != null) {
                                att.lovValues = attribute.lov.values;
                            }

                            if (attribute.dataType == "RICHTEXT") {
                                $timeout(function () {
                                    $('.note-current-fontname').text('Arial');
                                }, 1000);

                            }

                            if (attribute.required == false) {
                                vm.requirementProperties.push(att);
                            } else {
                                vm.requirementRequiredProperties.push(att);
                            }
                        });
                    }
                )
            }

            /*---------Validation for Required Attributes ------------*/

            function validateRequiredAttributes() {
                var valid = true;

                if (vm.requiredAttributes.length > 0) {
                    angular.forEach(vm.requiredAttributes, function (attribute) {
                        if (valid) {
                            if ($rootScope.checkAttribute(attribute)) {
                                valid = true;
                            } else {
                                valid = false;
                                $rootScope.showWarningMessage(pleaseEnter + " " + attribute.attributeDef.name);
                            }
                        }
                    })
                }
                return valid;
            }

            /*-------Validation for Required Properties -----------------*/
            function validateRequiredProperties() {
                var valid = true;

                if (vm.requirementRequiredProperties.length > 0) {
                    angular.forEach(vm.requirementRequiredProperties, function (attribute) {
                        if (valid) {
                            if ($rootScope.checkAttribute(attribute)) {
                                valid = true;
                            } else {
                                valid = false;
                                $rootScope.showWarningMessage(pleaseEnter + " " + attribute.attributeDef.name);
                            }
                        }
                    })
                }

                return valid;
            }

            function loadPersons() {
                LoginService.getAllLogins().then(
                    function (data) {
                        angular.forEach(data, function (login) {
                            if (login.isActive == true && login.external == false) {
                                vm.persons.push(login.person);
                            }
                        });
                    }
                )
            }

            function loadWorkflows() {
                RequirementsTypeService.getWorkflows(vm.newSpecRequirement.requirement.type.id, 'REQUIREMENTS').then(
                    function (data) {
                        vm.workflows = data;
                    }
                )
            }

            (function () {
                //if ($application.homeLoaded == true) {
                loadObjectTypeAttributes();
                loadPersons();
                $rootScope.$on('app.spec.requirement.new', createRequirement);
                //}
            })();
        }
    }
)
;
