/**
 * Created by Hello on 10/19/2020.
 */
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
        'app/shared/services/core/qualityTypeService',
        'app/shared/services/core/mesObjectTypeService',
        'app/shared/services/core/manpowerService',
        //'app/desktop/directives/person-data/personDataDirectiveController',
        'app/shared/services/core/classificationService'
    ],
    function (module) {

        module.controller('NewManpowerController', NewManpowerController);

        function NewManpowerController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce, ManpowerService, LoginService,
                                       ObjectTypeAttributeService, QualityTypeService, AutonumberService, MESObjectTypeService, ClassificationService) {

            var vm = this;

            var parsed = angular.element("<div></div>");
            vm.selectedManpowerType = null;
            vm.attributes = [];
            vm.validattributes = [];

            vm.newManpower = {
                id: null,
                type: null,
                number: null,
                name: null,
                description: null,
                person: null,
                personDetails: {
                    id: null,
                    firstName: null,
                    lastName: null,
                    phoneMobile: null,
                    email: null,
                    personType: 1
                },
                newPerson: true
            };

            vm.autoNumber = autoNumber;
            vm.onSelectType = onSelectType;
            function onSelectType(manpowerType) {
                if (manpowerType != null && manpowerType != undefined) {
                    vm.selectedManpowerType = manpowerType;
                    vm.newManpower.type = manpowerType;
                    autoNumber();
                }
            }

            function autoNumber() {
                if (vm.selectedManpowerType != null) {
                    var source = vm.selectedManpowerType.autoNumberSource;
                    AutonumberService.getNextNumberByName(source.name).then(
                        function (data) {
                            vm.newManpower.number = data;
                            loadManpowerTypeAttributes();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            var attributeRequired = parsed.html($translate.instant("ATTRIBUTE_REQUIRED")).html();
            var itemTypeValidation = parsed.html($translate.instant("ITEM_TYPE_VALIDATION")).html();
            var itemNumberValidation = parsed.html($translate.instant("ITEM_NUMBER_VALIDATION")).html();
            var itemNameValidation = parsed.html($translate.instant("ITEM_NAME_VALIDATION")).html();
            var personValidation = parsed.html($translate.instant("SELECT_ANY_PERSON")).html();
            var firstNameValidation = parsed.html($translate.instant("FIRST_NAME_VALIDATION")).html();
            var emailValidation = parsed.html($translate.instant("EMAIL_VALIDATION")).html();
            var emailCannotEmpty = parsed.html($translate.instant("ENTER_VALID_EMAIL")).html();
            $scope.selectPersons = parsed.html($translate.instant("SELECT_PERSONS")).html();

            function validate() {
                var valid = true;
                if (vm.newManpower.type == null || vm.newManpower.type == undefined ||
                    vm.newManpower.type == "") {
                    $rootScope.showErrorMessage(itemTypeValidation);
                    valid = false;
                }
                else if (vm.newManpower.number == null || vm.newManpower.number == undefined ||
                    vm.newManpower.number == "") {
                    $rootScope.showErrorMessage(itemNumberValidation);
                    valid = false;
                }
                else if (vm.newManpower.name == null || vm.newManpower.name == undefined ||
                    vm.newManpower.name == "") {
                    $rootScope.showErrorMessage(itemNameValidation);
                    valid = false;
                }
                // else if (!vm.newManpower.newPerson && (vm.newManpower.person == null || vm.newManpower.person == undefined || vm.newManpower.person == "")) {
                //     $rootScope.showErrorMessage(personValidation);
                //     valid = false;
                // } else if (vm.newManpower.newPerson && (vm.newManpower.personDetails.firstName == null || vm.newManpower.personDetails.firstName == "" || vm.newManpower.personDetails.firstName == undefined)) {
                //     valid = false;
                //     $rootScope.showWarningMessage(firstNameValidation);
                // } else if (vm.newManpower.newPerson && (vm.newManpower.personDetails.email == null || vm.newManpower.personDetails.email == "" || vm.newManpower.personDetails.email == undefined)) {
                //     valid = false;
                //     $rootScope.showWarningMessage(emailValidation);
                // } else if (vm.newManpower.newPerson && vm.newManpower.personDetails.email != null && !validateEmail()) {
                //     valid = false;
                //     $rootScope.showWarningMessage(emailCannotEmpty);
                // } 
                else if (vm.attributes.length > 0 && !validateAttributes()) {
                    valid = false;
                } else if (!$rootScope.checkAttributeValidations(vm.attributes)) {
                    valid = false;
                }

                return valid;
            }

            function validateEmail() {
                var valid = true;
                var atpos = vm.newManpower.personDetails.email.indexOf("@");
                var dotpos = vm.newManpower.personDetails.email.lastIndexOf(".");
                if (vm.newManpower.personDetails.email != null && vm.newManpower.personDetails.email != undefined && vm.newManpower.personDetails.email != "") {
                    if (atpos < 1 || ( dotpos - atpos < 2 )) {
                        valid = false;
                    }
                }
                return valid
            }

            var insertedSuccefully = parsed.html($translate.instant("MANPOWER_ADDED_SUCCESS")).html();


            function create() {
                if (validate()) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
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
                    vm.newManpower.mesObjectAttributes = vm.attributes;
                    ManpowerService.createManpower(vm.newManpower).then(
                        function (data) {
                            vm.newManpower = data;
                            saveAttributes().then(
                                function (attributes) {
                                    $scope.callback(vm.newManpower);
                                    $rootScope.hideBusyIndicator();
                                    $rootScope.showSuccessMessage(insertedSuccefully);
                                    vm.selectedManpowerType = null;
                                    vm.newManpower = {
                                        id: null,
                                        type: null,
                                        number: null,
                                        name: null,
                                        description: null,
                                        person: null,
                                        personDetails: {
                                            id: null,
                                            firstName: null,
                                            lastName: null,
                                            phoneMobile: null,
                                            email: null,
                                            personType: 1
                                        },
                                        newPerson: true
                                    };
                                }
                            )

                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

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

            function saveAttributes() {
                var defered = $q.defer();
                if (vm.imageAttributes.length > 0 || vm.attachmentAttributes.length > 0) {
                    angular.forEach(vm.imageAttributes, function (imgAtt) {
                            ClassificationService.updateAttributeImageValue("MESOBJECTTYPE", vm.newManpower.id, imgAtt.id.attributeDef, vm.images.get(imgAtt.id.attributeDef)).then(
                                function (data) {
                                    defered.resolve();
                                }
                                , function (error) {
                                    defered.resolve();
                                }
                            )
                        }
                    );
                    angular.forEach(vm.attachmentAttributes, function (imgAtt) {
                            ClassificationService.updateAttributeAttachmentValues("MESOBJECTTYPE", vm.newManpower.id, imgAtt.id.attributeDef, vm.attachments.get(imgAtt.id.attributeDef)).then(
                                function (data) {
                                    defered.resolve();
                                }
                                , function (error) {
                                    defered.resolve();
                                }
                            )
                        }
                    )
                } else {
                    defered.resolve();
                }
                return defered.promise;
            }

            function loadManpowerTypeAttributes() {
                vm.attributes = [];
                MESObjectTypeService.getMesObjectAttributesWithHierarchy(vm.newManpower.type.id).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newManpower.id,
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
                            if (attribute.dataType == "LIST" && !attribute.listMultiple && attribute.defaultListValue != null) {
                                att.listValue = attribute.defaultListValue;
                            }
                            if (attribute.dataType == "LIST" && attribute.listMultiple && attribute.defaultListValue != null) {
                                att.mlistValue.push(attribute.defaultListValue);
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

            vm.onChangePerson = onChangePerson;
            function onChangePerson() {
                vm.newManpower.personDetails = {
                    id: null,
                    firstName: null,
                    lastName: null,
                    phoneMobile: null,
                    email: null,
                    personType: 1
                }
            }

            (function () {
                loadPersons();
                $rootScope.$on('app.manpower.new', create)
            })();
        }
    }
);