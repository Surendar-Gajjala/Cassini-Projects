define(
    [
        'app/desktop/modules/rm/rm.module',
        'moment',
        'moment-timezone-with-data',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/shared/services/core/specificationsService',
        'app/shared/services/core/requirementsTypeService',
        'app/shared/services/core/itemService',
        'app/desktop/modules/rm/directive/specificationDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController'
    ],
    function (module) {

        module.controller('NewSpecificationController', NewSpecificationController);

        function NewSpecificationController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies,
                                            AutonumberService, ObjectAttributeService, AttachmentService, ItemService,
                                            AttributeAttachmentService, SpecificationsService, RequirementsTypeService, ObjectTypeAttributeService) {

            var vm = this;
            vm.onSelectType = onSelectType;
            var parse = angular.element("<div></div>");
            vm.specRequiredProperties = [];
            vm.specProperties = [];

            vm.newSpecification = {
                id: null,
                type: null,
                objectNumber: null,
                name: null,
                description: null,
                workflowDefinition: null
            };

            var newSpecificationTitle = parse.html($translate.instant("NEW_SPECIFICATION")).html();
            var specCreatedMsg = parse.html($translate.instant("SPECIFICATION_CREATE_MSG")).html();
            var typeValidation = parse.html($translate.instant("SPECIFICATION_TYPE_VALIDATION")).html();
            var numberValidation = parse.html($translate.instant("SPECIFICATION_NUMBER__VALIDATION")).html();
            var nameValidation = parse.html($translate.instant("SPECIFICATION_NAME_VALIDATION")).html();
            var descriptionValidation = parse.html($translate.instant("DESCRIPTION_VALIDATION")).html();
            var pleaseEnter = parse.html($translate.instant("PLEASE_ENTER")).html();
            var itemWorkflowValidation = parse.html($translate.instant("ITEM_WORKFLOW_VALIDATION")).html();

            function onSelectType(itemType) {
                if (itemType != null && itemType != undefined) {
                    vm.newSpecification.type = itemType;
                    autoNumber();
                }
            }

            function autoNumber() {
                if (vm.newSpecification.type != null) {
                    AutonumberService.getNextNumberByName(vm.newSpecification.type.numberSource.id).then(
                        function (data) {
                            vm.newSpecification.objectNumber = data;
                            loadWorkflows();
                            loadAttributeDefs();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function loadAttributeDefs() {
                vm.requiredAttributes = [];
                vm.attributes = [];
                RequirementsTypeService.getTypeAttributesWithHierarchy(vm.newSpecification.type.id).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newSpecification.id,
                                    attributeDef: attribute.id

                                },
                                attributeDef: attribute,
                                listValue: null,
                                newListValue: null,
                                mlistValue: [],
                                listValueEditMode: false,
                                booleanValue: false,
                                refValue: null,
                                timestampValue: null,
                                ref: null,
                                imageValue: null,
                                attachmentValues: []
                            };
                            if (attribute.dataType == "LIST" && !attribute.listMultiple) {
                                att.listValue = attribute.defaultListValue;
                            }
                            if (attribute.dataType == "LIST" && attribute.listMultiple) {
                                att.mlistValue.push(attribute.defaultListValue);
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
                            if (attribute.validations != null && attribute.validations != "") {
                                attribute.newValidations = JSON.parse(attribute.validations);
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

            /*----------- To create Specifications one by one  -------------------*/

            function create() {
                if (validate() && validateRequiredProperties() && validateRequiredAttributes()) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    if (vm.newSpecification.workflowDefinition != null) {
                        vm.newSpecification.workflowDefId = vm.newSpecification.workflowDefinition.id;
                    }
                    SpecificationsService.createSpecification(vm.newSpecification).then(
                        function (data) {
                            vm.newSpecification = data;
                            var newSpec = data;
                            saveObjectAttributes().then(
                                function (object) {
                                    saveAttributes().then(
                                        function (att) {
                                            vm.newSpecification = {
                                                id: null,
                                                type: null,
                                                objectNumber: null,
                                                name: null,
                                                description: null
                                            };
                                            vm.attributes = [];
                                            vm.requiredAttributes = [];
                                            vm.specProperties = [];
                                            vm.specRequiredProperties = [];
                                            $scope.callback(newSpec);
                                            loadObjectTypeAttributes();
                                            $rootScope.hideBusyIndicator();
                                            $rootScope.showSuccessMessage(specCreatedMsg);
                                        }
                                    )
                                }
                            )
                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            /*------------Validation for Specification ----------------*/

            function validate() {
                var valid = true;
                if (vm.newSpecification.type == null || vm.newSpecification.type == undefined ||
                    vm.newSpecification.type == "") {
                    $rootScope.showErrorMessage(typeValidation);
                    valid = false;
                }
                else if (vm.newSpecification.objectNumber == null || vm.newSpecification.objectNumber == undefined ||
                    vm.newSpecification.objectNumber == "") {
                    $rootScope.showErrorMessage(numberValidation);
                    valid = false;
                }
                else if (vm.newSpecification.name == null || vm.newSpecification.name == undefined ||
                    vm.newSpecification.name == "") {
                    $rootScope.showErrorMessage(nameValidation);
                    valid = false;
                } else if (!$rootScope.checkAttributeValidations(vm.attributes)) {
                    valid = false;
                } else if (!$rootScope.checkAttributeValidations(vm.requiredAttributes)) {
                    valid = false;
                }
                /*  else if (vm.newSpecification.workflowDefinition == null || vm.newSpecification.workflowDefinition == undefined ||
                 vm.newSpecification.workflowDefinition == "") {
                 $rootScope.showErrorMessage(itemWorkflowValidation);
                 valid = false;
                 }*/
                /* else if (vm.attributes != null || vm.attributes != "" || vm.attributes != undefined) {
                 angular.forEach(vm.attributes, function (obj) {
                 if (obj.attributeDef.dataType == 'DATE') {
                 if ($rootScope.dateValidation(obj.dateValue)) {
                 obj.dateValue = $rootScope.convertDate(obj.dateValue);
                 } else {
                 valid = false;
                 $rootScope.hideBusyIndicator();
                 }
                 }
                 });
                 }*/

                return valid;
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

                if (vm.specRequiredProperties.length > 0) {
                    angular.forEach(vm.specRequiredProperties, function (attribute) {
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

            /*------Save ObjectAttributes ------*/
            function saveObjectAttributes() {
                var defered = $q.defer();
                var propertyImages = new Hashtable();
                vm.propertyImageAttributes = [];
                var objectAttrs = vm.specProperties.concat(vm.specRequiredProperties);
                if (objectAttrs.length > 0) {
                    angular.forEach(objectAttrs, function (attribute) {
                        attribute.id.objectId = vm.newSpecification.id;
                        if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                            propertyImages.put(attribute.id.attributeDef, attribute.imageValue);
                            vm.propertyImageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'BOOLEAN' && attribute.booleanValue == null) {
                            attribute.booleanValue = false;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length > 0) {
                            attribute.attachmentValues = addSpecPropertyAttachment(attribute);
                        }
                    });
                    $timeout(function () {
                        ObjectAttributeService.saveItemObjectAttributes(vm.newSpecification.id, objectAttrs).then(
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
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'SPECIFICATION', attachmentFile).then(
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
                        attribute.id.objectId = vm.newSpecification.id;

                        /*   if (attribute.attributeDef.dataType == "LIST" && attribute.attributeDef.listMultiple == true && attribute.mlistValue.length > 0) {
                         var mlistValue = attribute.mlistValue;
                         attribute.mlistValue = [];
                         attribute.mlistValue.push(mlistValue);
                         }*/

                        if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                            images.put(attribute.id.attributeDef, attribute.imageValue);
                            vm.imageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'BOOLEAN' && attribute.booleanValue != true) {
                            attribute.booleanValue = false;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length != null) {
                            attribute.attachmentValues = addAttachment(attribute)
                        }
                    });
                    $timeout(function () {
                        SpecificationsService.saveAttributes(vm.newSpecification.id, vm.attributes).then(
                            function (data) {

                                if (vm.imageAttributes.length > 0) {
                                    angular.forEach(vm.imageAttributes, function (imgAtt) {
                                        SpecificationsService.saveImageValue(imgAtt.id.objectId, imgAtt.id.attributeDef, images.get(imgAtt.id.attributeDef)).then(
                                            function (data) {
                                                vm.newSpecification = {
                                                    id: null,
                                                    type: null,
                                                    objectNumber: null,
                                                    name: null,
                                                    description: null
                                                };
                                                defered.resolve();
                                            }
                                        )
                                    })
                                } else {
                                    defered.resolve();
                                }

                            }, function (error) {
                                defered.reject();
                                $rootScope.showErrorMessage(error.message);
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
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'SPECIFICATION', attachmentFile).then(
                        function (data) {
                            attachmentIds.push(data[0].id);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                         })
                })
                return attachmentIds;
            }

            /*----- Load ObjectType Attributes -------*/

            function loadObjectTypeAttributes() {
                ObjectTypeAttributeService.getObjectTypeAttributesByType("SPECIFICATION").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newSpecification.id,
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
                            if (attribute.dataType == "TIMESTAMP") {
                                att.timestampValue = moment(new Date()).format("DD/MM/YYYY, HH:mm:ss");
                            }

                            if (attribute.dataType == "LIST" && !attribute.listMultiple) {
                                att.listValue = attribute.defaultListValue;
                            }
                            if (attribute.dataType == "LIST" && attribute.listMultiple) {
                                att.mlistValue.push(attribute.defaultListValue);
                            }

                            if (attribute.dataType == "TEXT") {
                                att.stringValue = attribute.defaultTextValue;
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
                                vm.specProperties.push(att);
                            } else {
                                vm.specRequiredProperties.push(att);
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadWorkflows() {
                SpecificationsService.getWorkflows(vm.newSpecification.type.id, 'REQUIREMENTS').then(
                    function (data) {
                        vm.workflows = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                     }
                )
            }

            (function () {
                //if ($application.homeLoaded == true) {
                /* $(".summernote").summernote('code', '<p style="font-family: Arial;"><br></p>')*/
                /* $("div.note-btn-group.btn-group.note-fontname > div > div > li:nth-child(1) > a").trigger('click');*/
                loadObjectTypeAttributes();
                $rootScope.$on('app.rm.specifications.new', create);
                //}
            })();
        }
    }
)
;