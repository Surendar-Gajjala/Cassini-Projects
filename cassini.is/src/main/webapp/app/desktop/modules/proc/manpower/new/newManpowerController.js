define(
    [
        'app/desktop/modules/proc/proc.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/itemTypeService',
        'app/desktop/modules/proc/manpower/directive/manpowerDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController'
    ],
    function (module) {
        module.controller('NewManpowerController', NewManpowerController);

        function NewManpowerController($scope, $q, $rootScope, $timeout, $state, ItemService, ItemTypeService, $stateParams, $cookies,
                                       AutonumberService, ObjectAttributeService, AttributeAttachmentService, LoginService, ClassificationService, ObjectAttributeService) {

            var vm = this;
            var manpowerItems = $scope.data.manpower;
            vm.itemType = null;

            vm.newManpower = {
                person: null,
                personType: null,
                itemType: null,
                itemNumber: null,
                description: null,
                picture: null

            };

            vm.pageable = {
                page: 1,
                size: 10,
                sort: {
                    label: "loginTime",
                    field: "loginTime",
                    order: "desc"
                }
            };

            vm.attributes = [];
            vm.requiredAttributes = [];
            vm.persons = [];
            vm.attribute = null;
            vm.showNew = false;
            vm.manpower = null;
            vm.personImage = null;
            vm.personImagePath = null;

            vm.onSelectType = onSelectType;
            vm.createItem = createItem;
            vm.cancelItem = cancelItem;
            vm.autoNumber = autoNumber;
            vm.addToListValue = addToListValue;
            vm.cancelToListValue = cancelToListValue;
            vm.newPerson = newPerson;
            vm.existingPerson = existingPerson;
            vm.mode = null;

            function existingPerson(mode) {
                if (vm.itemType == null || vm.itemType == undefined) {
                    $rootScope.showErrorMessage("Please select Manpower Type");
                }
                else {
                    vm.mode = 'EXIST';
                    vm.persons = [];
                    ItemService.getExistingPersons(vm.itemType.id).then(
                        function (data) {
                            vm.persons = data;
                        }
                    )
                }
            }

            function newPerson(mode) {
                if (vm.itemType == null || vm.itemType == undefined) {
                    $rootScope.showErrorMessage("Please select Manpower Type");
                }
                else {
                    vm.mode = 'NEW';
                    vm.persons = [];
                    ItemService.getExistingPersons(vm.itemType.id).then(
                        function (data) {
                            vm.persons = data;
                        }
                    )
                }
                image();
            }

            function anotherItem() {
                create().then(
                    function () {
                        vm.mode = null;
                        vm.newManpower = {
                            firstName: null,
                            lastName: null,
                            phoneMobile: null,
                            email: null,
                            personType: null,
                            itemType: null,
                            itemNumber: null,
                            description: null,
                            image: null,
                            person: null
                        };
                        $rootScope.hideBusyIndicator();
                        $rootScope.hideSidePanel();
                        $rootScope.showSuccessMessage("Manpower (" + vm.manpower.itemNumber + ") created successfully");
                        //$scope.callback();
                        $rootScope.$broadcast('app.manpower.all');
                    }
                );
            }

            function createItem() {
                create().then(
                    function () {
                        $rootScope.hideSidePanel();
                        //$scope.callback();
                        $rootScope.$broadcast('app.manpower.all');
                    }
                );
            }

            function validate() {
                var valid = true;

                if (vm.newManpower.itemType == null || vm.newManpower.itemType == undefined || vm.newManpower.itemType == "") {
                    valid = false;
                    $rootScope.showErrorMessage('Manpower Type cannot be empty');
                }
                else if (vm.newManpower.itemNumber == null || vm.newManpower.itemNumber == undefined || vm.newManpower.itemNumber == "") {
                    valid = false;
                    $rootScope.showErrorMessage('Manpower Number cannot be empty');
                }
                else if (vm.mode == null) {
                    valid = false;
                    $rootScope.showErrorMessage("Please select the type of Person Existed/New");
                }
                else if (vm.mode == "NEW" && (vm.newManpower.firstName == null || vm.newManpower.firstName == undefined || vm.newManpower.firstName == "")) {
                    valid = false;
                    $rootScope.showErrorMessage('Manpower First Name cannot be empty');
                }
                else if (vm.mode == "EXIST" && vm.manpower == null) {
                    $rootScope.showErrorMessage("Please select person");
                    valid = false;
                }
                if (vm.mode == "NEW" && valid == true) {
                    vm.newManpower.personType = 1;
                }
                /* if (vm.mode == "NEW" && (vm.personImage == null || !validatePersonImage())) {
                 valid = false;
                 $rootScope.showErrorMessage('Please upload photo');
                 }*/
                return valid;
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
                if (vm.newManpowerAttributes != null && vm.newManpowerAttributes != undefined) {
                    vm.objectAttributes = vm.objectAttributes.concat(vm.newManpowerAttributes);
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

            function saveObjectAttributes() {
                var defered = $q.defer();
                if (vm.newManpowerAttributes.length > 0) {
                    angular.forEach(vm.newManpowerAttributes, function (att) {
                        if (att.dateValue == "") {
                            att.dateValue = null;
                        }
                    });
                    ObjectAttributeService.saveItemObjectAttributes(vm.manpower.id, vm.newManpowerAttributes).then(
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
                        });
                } else {
                    defered.resolve();
                }
                $rootScope.$broadcast('app.manpower.all');
                return defered.promise;
            }

            function intializationForAttributesSave() {
                var defered = $q.defer();
                var attrCount = 0;
                vm.propertyImageAttributes = [];
                vm.propertyImages = new Hashtable();
                vm.imageAttributes = [];
                vm.images = new Hashtable();
                vm.requiredAttributes = [];

                if (vm.newManpowerAttributes.length == 0) {
                    defered.resolve();
                }
                else {
                    angular.forEach(vm.newManpowerAttributes, function (attribute) {
                        attribute.id.objectId = vm.manpower.id;
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
                                    if (attrCount == vm.newManpowerAttributes.length) {
                                        saveObjectAttributes().then(
                                            function (data) {
                                                vm.newManpowerAttributes = [];
                                                //loadObjectAttributeDefs();
                                                defered.resolve();
                                            }, function (error) {
                                                defered.reject();
                                            }
                                        )
                                    }
                                });
                        } else {
                            attrCount++;
                            if (attrCount == vm.newManpowerAttributes.length) {
                                saveObjectAttributes().then(
                                    function (data) {
                                        vm.newManpowerAttributes = [];
                                        //loadObjectAttributeDefs();
                                        defered.resolve();
                                    }, function (error) {
                                        defered.reject();
                                    }
                                )
                            }
                        }
                    });
                }
                return defered.promise;
            }

            function create() {
                var defered = $q.defer();
                $rootScope.closeNotification();
                if (validate()) {
                    attributesValidate().then(
                        function (success) {
                            if (vm.mode == "EXIST") {
                                vm.newManpower = {
                                    person: vm.manpower,
                                    personType: vm.manpower.personType,
                                    itemType: vm.newManpower.itemType,
                                    itemNumber: vm.newManpower.itemNumber,
                                    description: vm.newManpower.description
                                };
                            }
                            else {
                                vm.newManpower = {
                                    person: vm.newManpower,
                                    itemType: vm.newManpower.itemType,
                                    itemNumber: vm.newManpower.itemNumber,
                                    description: vm.newManpower.description,
                                    image: null,
                                    firstName: vm.newManpower.firstName,
                                    lastName: vm.newManpower.lastName,
                                    phoneMobile: vm.newManpower.phoneMobile,
                                    email: vm.newManpower.email,
                                    personType: 1
                                };
                            }

                            vm.newManpower.objectType = "MANPOWER";
                            ItemService.createManpowerItem(vm.newManpower).then(
                                function (data) {
                                    vm.manpower = data;
                                    manpowerItems.content.push(vm.newManpower);
                                    vm.newManpower.id = data.id;
                                    ItemService.uploadImage(vm.manpower.person.id, vm.personImage).then(
                                        function (data) {
                                            intializationForAttributesSave().then(
                                                function (success) {
                                                    defered.resolve();
                                                }, function (error) {
                                                    defered.reject();
                                                }
                                            )
                                        },
                                        function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    )

                                },
                                function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    defered.reject();
                                }
                            )
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                            defered.reject();
                        });
                }
                return defered.promise;
            }

            function cancelItem() {
                vm.newManpower = null;
                $state.go('app.proc.manpower.all');
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
                    return true
                } else {
                    return false;
                }

            }

            function saveAttributes() {
                var defered = $q.defer();
                if (vm.attributes.length > 0) {
                    angular.forEach(vm.attributes, function (att) {
                        if (att.dateValue == "") {
                            att.dateValue = null;
                        }
                    });
                    ItemService.saveManpowerItemAttributes(vm.manpower.id, vm.attributes).then(
                        function (data) {
                            if (vm.imageAttributes.length > 0) {
                                angular.forEach(vm.imageAttributes, function (imgAtt) {
                                    ObjectAttributeService.uploadObjectAttributeImage(imgAtt.id.objectId, imgAtt.id.attributeDef, vm.images.get(imgAtt.id.attributeDef)).then(
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
                        }
                    )
                }

                else {
                    defered.resolve();
                }
                return defered.promise;
            }

            function addAttachment(attribute) {
                var defered = $q.defer();
                vm.propertyAttachmentIds = [];
                var counter = 0;
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'MANPOWER', attachmentFile).then(
                        function (data) {
                            vm.propertyAttachmentIds.push(data[0].id);
                            counter++;
                            if (counter == attribute.attachmentValues.length) {
                                defered.resolve(true);
                            }
                        }
                    )
                });
                return defered.promise;
            }

            function onSelectType(itemType) {
                vm.itemType = itemType;
                if (itemType != null && itemType != undefined) {
                    vm.newManpower.itemType = itemType;
                    if (vm.mode != null && vm.mode != undefined) {
                        if (vm.mode == 'NEW') {
                            newPerson('NEW');
                        }
                        else {
                            existingPerson('EXIST');
                        }
                    }
                    autoNumber();
                }
            }

            function autoNumber() {
                if (vm.newManpower.itemType != null && vm.newManpower.itemType.manpowerNumberSource != null) {
                    var source = vm.newManpower.itemType.manpowerNumberSource;
                    AutonumberService.getNextNumber(source.id).then(
                        function (data) {
                            vm.newManpower.itemNumber = data;
                            loadAttributeDefs();
                        }
                    )
                }
            }

            function loadAttributeDefs() {
                vm.requiredAttributes = [];
                vm.attributes = [];
                ItemTypeService.getManpowerAttributesWithHierarchy(vm.newManpower.itemType.id).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newManpower.id,
                                    attributeDef: attribute.id

                                },
                                attributeDef: attribute,
                                listValue: null,
                                newListValue: null,
                                listValueEditMode: false,
                                timestampValue: moment(new Date()).format("DD/MM/YYYY, HH:mm:ss"),
                                booleanValue: false,
                                dateValue: null,
                                timeValue: null,
                                imageValue: null,
                                integerValue: null,
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

                        });
                    }
                )
            }

            function loadObjectAttributeDefs() {
                vm.newManpowerAttributes = [];
                ItemService.getAllTypeAttributes("MANPOWER").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newManpower.id,
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
                            vm.newManpowerAttributes.push(att);
                        });
                    });
            }

            function addToListValue(attribute) {
                if (attribute.listValue == undefined) {
                    attribute.listValue = [];
                }

                attribute.listValue.push(attribute.newListValue);
            }

            function cancelToListValue(attribute) {
                attribute.listValueEditMode = false;
            }

            function unCheck() {
                document.getElementById("radio1").checked = false;
                document.getElementById("radio2").checked = false;
            }

            $scope.$on('app.manpower.new', function loadCreateMethod() {
                anotherItem();
            });

            function image() {
                document.getElementById("image1").onchange = function () {
                    var file = document.getElementById("image1");
                    var reader = new FileReader();
                    if (vm.personImagePath == null) {
                        reader.onload = function (e) {
                            $('#personImage')
                                .attr('src', e.target.result)
                                .width(100)
                                .height(100);
                        };
                        vm.personImage = file.files[0];
                        reader.readAsDataURL(file.files[0]);
                    } else {
                        reader.onload = function (e) {
                            $('#changeImage')
                                .attr('src', e.target.result)
                                .width(100)
                                .height(100);
                        };
                        vm.personImage = file.files[0];
                        reader.readAsDataURL(file.files[0]);
                    }
                };
            }

            function validatePersonImage() {
                var valid = true;
                var typeIndex = vm.personImage.name.lastIndexOf(".");
                var imageType = vm.personImage.name.substring(typeIndex).toLowerCase();
                if (imageType == ".jpg" || imageType == ".jpeg" || imageType == ".png") {
                    if (vm.personImage.size >= 2097152) {
                        valid = false;
                        vm.personImage = null;
                        vm.personImagePath = null;
                        $rootScope.showWarningMessage("Please upload photo with in 1MB Size");
                    }

                    if (vm.personImage.size < 2097152) {
                        valid = true;
                        vm.personImageType = imageType;
                    }
                } else {
                    valid = false;
                    vm.personImage = null;
                    vm.personImagePath = null;
                    $rootScope.showWarningMessage("Invalid File format");
                }

                return valid;
            }

            (function () {
                if ($application.homeLoaded == true) {
                    $timeout(function () {
                        loadObjectAttributeDefs();
                    }, 500);
                }
            })();
        }
    }
);
