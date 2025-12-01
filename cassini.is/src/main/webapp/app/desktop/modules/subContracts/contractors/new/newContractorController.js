/**
 * Created by swapna on 21/01/19.
 */
define(['app/desktop/modules/subContracts/contracts.module',
        'app/shared/services/core/subContractService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService'
    ],
    function (module) {
        module.controller('NewContractorController', NewContractorController);

        function NewContractorController($scope, $rootScope, $timeout, $state, $cookies, $q,
                                         SubContractService, ObjectTypeAttributeService, AttributeAttachmentService, ObjectAttributeService, LoginService) {

            var vm = this;
            vm.newContractor = {
                name: null,
                contact: null,
                active: true
            };
            vm.logins = [];
            vm.attributes = [];
            vm.requiredAttributes = [];

            function validateContractor() {
                var flag = true;
                if (vm.newContractor.name == null || vm.newContractor.name == "" || vm.newContractor.name == undefined || vm.newContractor.name == " ") {
                    $rootScope.showErrorMessage("Name cannot be empty");
                    flag = false;
                }
                else if (vm.newContractor.contact == null) {
                    $rootScope.showErrorMessage("Please select contact person");
                    flag = false;
                }
                return flag;
            }

            function create() {
                if (validateContractor()) {
                    attributesValidate().then(
                        function (success) {
                            vm.newContractor.contact = vm.newContractor.contact.person.id;
                            $rootScope.showBusyIndicator();
                            SubContractService.createContractor(vm.newContractor).then(
                                function (data) {
                                    vm.newContractor = data;
                                    intializationForAttributesSave();
                                    $rootScope.hideBusyIndicator();
                                },
                                function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }, function (error) {

                        });
                }
            }

            /* ............. start attributes methods block ............. */

            function addAttachment(attribute) {
                var defered = $q.defer();
                vm.propertyAttachmentIds = [];
                var counter = 0;
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'CONTRACTOR', attachmentFile).then(
                        function (data) {
                            vm.propertyAttachmentIds.push(data[0].id);
                            counter++;
                            if (counter == attribute.attachmentValues.length) {
                                defered.resolve(true);
                            }
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                });
                return defered.promise;
            }

            function saveObjectAttributes() {
                var defered = $q.defer();
                var countloop = 0;
                if (vm.newContractorAttributes.length > 0) {
                    angular.forEach(vm.newContractorAttributes, function (att) {
                        if (att.dateValue == "") {
                            att.dateValue = null;
                        }
                        countloop++;
                        if (countloop == vm.newContractorAttributes.length) {
                            ObjectAttributeService.saveItemObjectAttributes(vm.newContractor.id, vm.newContractorAttributes).then(
                                function (data) {
                                    var secCount = 0;
                                    if (vm.propertyImageAttributes.length > 0) {
                                        angular.forEach(vm.propertyImageAttributes, function (propImgAtt) {
                                            ObjectAttributeService.uploadObjectAttributeImage(propImgAtt.id.objectId, propImgAtt.id.attributeDef, vm.propertyImages.get(propImgAtt.id.attributeDef)).then(
                                                function (data) {
                                                });
                                            secCount++;
                                            if (secCount == vm.propertyImageAttributes.length) {
                                                defered.resolve();
                                            }
                                        });
                                    } else {
                                        defered.resolve();
                                    }
                                },
                                function (error) {
                                    defered.reject();
                                }
                            )
                        }
                    });

                } else {
                    defered.resolve();
                }
                return defered.promise;
            }

            function attributesValidate() {
                var defered = $q.defer();
                $rootScope.closeNotification();
                vm.objectAttributes = [];
                vm.validObjectAttributes = [];
                vm.validattributes = [];
                angular.forEach(vm.requiredAttributes, function (attribute) {
                    if (attribute.attributeDef.required == true && attribute.attributeDef.dataType != 'BOOLEAN' &&
                        attribute.attributeDef.dataType != 'TIMESTAMP') {
                        if (checkAttribute(attribute)) {
                            vm.validattributes.push(attribute);
                        }
                        else {
                            $rootScope.showErrorMessage(attribute.attributeDef.name + ": attribute is required");
                            $rootScope.hideBusyIndicator();
                        }
                    } else {
                        vm.validattributes.push(attribute);
                    }
                });
                vm.objectAttributes = [];
                if (vm.newContractorAttributes != null && vm.newContractorAttributes != undefined) {
                    vm.objectAttributes = vm.objectAttributes.concat(vm.newContractorAttributes);
                }
                angular.forEach(vm.objectAttributes, function (attribute) {
                    if (attribute.attributeDef.required == true && attribute.attributeDef.dataType != 'BOOLEAN' &&
                        attribute.attributeDef.dataType != 'TIMESTAMP') {
                        if (checkAttribute(attribute)) {
                            vm.validObjectAttributes.push(attribute);
                        }
                        else {
                            $rootScope.showErrorMessage(attribute.attributeDef.name + ": attribute is required");
                            $rootScope.hideBusyIndicator();
                        }
                    } else {
                        vm.validObjectAttributes.push(attribute);
                    }
                });
                if (vm.requiredAttributes.length == vm.validattributes.length && vm.objectAttributes.length == vm.validObjectAttributes.length) {
                    defered.resolve();
                } else {
                    defered.reject();
                }
                return defered.promise;
            }

            function loadObjectAttributeDefs() {
                vm.newContractorAttributes = [];
                ObjectTypeAttributeService.getObjectTypeAttributesByType("CONTRACTOR").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newContractor.id,
                                    attributeDef: attribute.id
                                },
                                attributeDef: attribute,
                                listValue: null,
                                newListValue: null,
                                timeValue: null,
                                timestampValue: moment(new Date()).format("DD/MM/YYYY, HH:mm:ss"),
                                listValueEditMode: false,
                                booleanValue: false,
                                dateValue: null,
                                imageValue: null,
                                refValue: null,
                                ref: null,
                                attachmentValues: []
                            };
                            if (attribute.lov != null) {
                                att.lovValues = attribute.lov.values;
                            }
                            if (attribute.required == false) {
                                vm.attributes.push(att);
                            } else {
                                vm.requiredAttributes.push(att);
                            }

                            vm.newContractorAttributes.push(att);
                        });
                    }, function (error) {

                    });
            }

            function checkAttribute(attribute) {
                if ((attribute.stringValue != null && attribute.stringValue != undefined && attribute.stringValue != "") ||
                    (attribute.integerValue != null && attribute.integerValue != undefined && attribute.integerValue != "") ||
                    (attribute.doubleValue != null && attribute.doubleValue != undefined && attribute.doubleValue != "") ||
                    (attribute.dateValue != null && attribute.dateValue != undefined && attribute.dateValue != "") ||
                    (attribute.imageValue != null && attribute.imageValue != undefined && attribute.imageValue != "") ||
                    (attribute.currencyValue != null && attribute.currencyValue != undefined && attribute.currencyValue != "") ||
                    (attribute.timeValue != null && attribute.timeValue != undefined && attribute.timeValue != "") ||
                    (attribute.attachmentValues.length != 0) ||
                    (attribute.refValue != null && attribute.refValue != undefined && attribute.refValue != "") ||
                    (attribute.listValue != null && attribute.listValue != undefined && attribute.listValue != "")) {
                    return true;
                } else {
                    return false;
                }
            }

            function intializationForAttributesSave() {
                var attrCount = 0;
                vm.propertyImageAttributes = [];
                vm.propertyImages = new Hashtable();
                vm.imageAttributes = [];
                vm.images = new Hashtable();
                vm.requiredAttributes = [];

                if (vm.newContractorAttributes.length == 0) {
                    $rootScope.hideBusyIndicator();
                    $rootScope.showSuccessMessage("Contractor (" + vm.newContractor.name + ") created successfully");
                    $rootScope.hideSidePanel('right');
                    $rootScope.$broadcast('app.contractors.all');
                }
                else {
                    angular.forEach(vm.newContractorAttributes, function (attribute) {
                        attribute.id.objectId = vm.newContractor.id;
                        if (attribute.attributeDef.dataType == "IMAGE" && attribute.imageValue != null) {
                            vm.propertyImages.put(attribute.attributeDef.id, attribute.imageValue);
                            vm.propertyImageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length > 0) {
                            addAttachment(attribute).then(
                                function (data) {
                                    attribute.attachmentValues = vm.propertyAttachmentIds;
                                    attrCount++;
                                    if (attrCount == vm.newContractorAttributes.length) {
                                        saveObjectAttributes().then(
                                            function (data) {
                                                vm.newContractorAttributes = [];
                                                loadObjectAttributeDefs();
                                                $rootScope.hideBusyIndicator();
                                                $rootScope.hideSidePanel('right');
                                                $rootScope.showSuccessMessage("Contractor (" + vm.newContractor.name + ") created successfully");
                                                $rootScope.$broadcast('app.contractors.all');
                                            }
                                        )
                                    }
                                });
                        } else {
                            attrCount++;
                            if (attrCount == vm.newContractorAttributes.length) {
                                saveObjectAttributes().then(
                                    function (data) {
                                        vm.newContractorAttributes = [];
                                        loadObjectAttributeDefs();
                                        $rootScope.hideBusyIndicator();
                                        $rootScope.hideSidePanel('right');
                                        $rootScope.$broadcast('app.contractors.all');
                                        $rootScope.showSuccessMessage("Contractor (" + vm.newContractor.name + ") created successfully");
                                    }
                                )
                            }
                        }
                    });
                }
            }

            /* ............. attributes methods end block ............. */

            function loadLogins() {
                LoginService.getActiveLogins().then(
                    function (data) {
                        vm.logins = data;
                    }
                )
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadObjectAttributeDefs();
                    loadLogins();
                    $scope.$on('app.contractor.new', function () {
                        create();
                    });
                }
            })();
        }
    }
);