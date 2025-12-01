define(['app/desktop/modules/pm/pm.module',
        'app/shared/services/pm/project/projectService',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/mailServerService'
    ],
    function (module) {
        module.controller('NewProjectController', NewProjectController);

        function NewProjectController($scope, $rootScope, $timeout, $sce, $state, $cookies, ItemService, ProjectService, CommonService,
                                      AttributeAttachmentService, $q, MailServerService, ObjectAttributeService, ObjectTypeAttributeService) {
            var vm = this;

            vm.attributes = [];

            vm.project = {
                id: null,
                name: null,
                description: "",
                portfolioObject: null,
                portfolio: null,
                plannedStartDate: null,
                plannedFinishDate: null,
                owner: null
            };
            vm.requiredAttributes = [];
            vm.create = create;
            vm.creating = false;
            vm.valid = true;
            vm.error = "";
            vm.persons = [];
            vm.portfolios = [];
            vm.showReceiverPassword = showReceiverPassword;
            vm.showSenderPassword = showSenderPassword;
            var validateEmail = validateEmail;

            var pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "ASC"
                }
            };

            var projects = [];
            var projectMap = new Hashtable();

            $scope.trustAsHtml = function (value) {
                return $sce.trustAsHtml(value);
            };

            vm.objectMailSettings = {
                objectId: null,
                mailServerObject: null,
                mailServer: null,
                receiverUser: null,
                receiverEmail: null,
                receiverPassword: null,
                senderUser: null,
                senderEmail: null,
                senderPassword: null
            }

            vm.objectMailSettingsChanged = mailServerChanged;

            function mailServerChanged(mailServerObject) {
                vm.objectMailSettings.mailServerObject = null;
                vm.objectMailSettings.mailServerObject = mailServerObject;
            }

            function validateObjectSettings() {
                var defered = $q.defer();
                if (vm.objectMailSettings.mailServerObject != null) {
                    if (vm.objectMailSettings.receiverUser == null || vm.objectMailSettings.receiverUser == undefined || vm.objectMailSettings.receiverUser == "") {
                        defered.reject();
                        $rootScope.showWarningMessage("Receiver User cannot be empty");
                    } else if (vm.objectMailSettings.receiverUser != null) {
                        if (vm.objectMailSettings.receiverEmail == null || vm.objectMailSettings.receiverEmail == undefined || vm.objectMailSettings.receiverEmail == "") {
                            defered.reject();
                            $rootScope.showWarningMessage("Receiver Email cannot be empty");
                        } else if (vm.objectMailSettings.receiverEmail != null) {
                            validateEmail(vm.objectMailSettings.receiverEmail).then(
                                function (success) {
                                    if (vm.objectMailSettings.receiverPassword == null || vm.objectMailSettings.receiverPassword == undefined || vm.objectMailSettings.receiverPassword == "") {
                                        defered.reject();
                                        $rootScope.showWarningMessage("Receiver Password cannot be empty");
                                    } else if (vm.objectMailSettings.receiverPassword != null) {
                                        if (vm.objectMailSettings.senderUser == null || vm.objectMailSettings.senderUser == undefined || vm.objectMailSettings.senderUser == "") {
                                            defered.reject();
                                            $rootScope.showWarningMessage("Sender User cannot be empty");
                                        } else if (vm.objectMailSettings.senderUser != null) {
                                            if (vm.objectMailSettings.senderEmail == null || vm.objectMailSettings.senderEmail == undefined || vm.objectMailSettings.senderEmail == "") {
                                                defered.reject();
                                                $rootScope.showWarningMessage("Sender Email cannot be empty");
                                            } else if (vm.objectMailSettings.senderEmail != null) {
                                                validateEmail(vm.objectMailSettings.senderEmail).then(
                                                    function (success) {
                                                        if (vm.objectMailSettings.senderPassword == null || vm.objectMailSettings.senderPassword == undefined || vm.objectMailSettings.senderPassword == "") {
                                                            defered.reject();
                                                            $rootScope.showWarningMessage("Sender Password cannot be empty");
                                                        } else {
                                                            defered.resolve();
                                                        }
                                                    }, function (error) {
                                                        defered.reject();
                                                    });
                                            }
                                        }
                                    }
                                }, function (error) {
                                    defered.reject();
                                });
                        }
                    }
                }
                return defered.promise;
            }

            function loadPersons() {
                CommonService.findAllPersons().then(
                    function (data) {
                        angular.forEach(data, function (person) {
                            if (person.objectType == 'PERSON') {
                                vm.persons.push(person);
                            }
                        });
                    });
                loadMailServers();
            }

            function loadMailServers() {
                vm.mailServers = [];
                MailServerService.getAllMailServers().then(
                    function (success) {
                        vm.mailServers = success;
                    }, function (error) {

                    });
            }

            function loadPortfolios() {
                ProjectService.getAllPortfolios().then(
                    function (data) {
                        vm.portfolios = data;
                    })
            }

            function loadProjects() {
                ProjectService.getProjects().then(
                    function (data) {
                        projects = data;
                        angular.forEach(projects, function (project) {
                            projectMap.put(project.name, project);
                        });
                    }
                )
            }

            function validate() {
                var defered = $q.defer();

                if (vm.project.name == null || vm.project.name == undefined || vm.project.name == "") {
                    defered.reject();
                    $rootScope.showErrorMessage("Name cannot be empty");
                } else if (projectMap.get(vm.project.name) != null) {
                    defered.reject();
                    $rootScope.showErrorMessage("{0} Name already exists".format(vm.project.name));
                } else if (vm.project.portfolioObject == null || vm.project.portfolioObject == undefined || vm.project.portfolioObject == "") {
                    defered.reject();
                    $rootScope.showErrorMessage("Portfolio cannot be empty");
                } else if (vm.project.owner == null || vm.project.owner == undefined || vm.project.owner == "") {
                    defered.reject();
                    $rootScope.showErrorMessage("Project Owner cannot be empty");
                } else if (vm.project.plannedStartDate == null ||
                    vm.project.plannedStartDate == undefined || vm.project.plannedStartDate == "") {
                    defered.reject();
                    $rootScope.showErrorMessage("Planned Start Date cannot be empty");
                } else if (vm.project.plannedFinishDate == null ||
                    vm.project.plannedFinishDate == undefined || vm.project.plannedFinishDate == "") {
                    defered.reject();
                    $rootScope.showErrorMessage("Planned Finish Date cannot be empty");
                } else if (vm.project.plannedStartDate != null) {
                    var today = moment(new Date());
                    var todayStr = today.format('DD/MM/YYYY');
                    var todayDate = moment(todayStr, 'DD/MM/YYYY');
                    var plannedStartDate = moment(vm.project.plannedStartDate, 'DD/MM/YYYY');
                    var val1 = plannedStartDate.isSame(todayDate) || plannedStartDate.isAfter(todayDate);
                    if (!val1) {
                        defered.reject();
                        $rootScope.showErrorMessage("Start Date should be on (or) after Today's Date");
                    } else if (vm.project.plannedFinishDate == null || vm.project.plannedFinishDate == undefined || vm.project.plannedFinishDate == "") {
                        defered.reject();
                        $rootScope.showErrorMessage("Planned Finish Date cannot be empty");
                    } else if (vm.project.plannedStartDate != null && vm.project.plannedFinishDate != null) {
                        var plannedFinishDate = moment(vm.project.plannedFinishDate, 'DD/MM/YYYY');
                        var plannedStartDate = moment(vm.project.plannedStartDate, 'DD/MM/YYYY');
                        var val = plannedFinishDate.isSame(plannedStartDate) || plannedFinishDate.isAfter(plannedStartDate);
                        if (!val) {
                            defered.reject();
                            $rootScope.showErrorMessage("Planned Finish Date should be after Planned Start Date");
                        } else if (vm.objectMailSettings.mailServerObject != null) {
                            validateObjectSettings().then(
                                function (success) {

                                    defered.resolve();
                                }, function (error) {

                                }
                            )
                        } else {
                            defered.resolve();
                        }
                    }
                }

                return defered.promise;
            }

            function createObjectSettings() {
                vm.objectMailSettings.objectId = vm.project.id;
                vm.objectMailSettings.mailServer = vm.objectMailSettings.mailServerObject.id;
                MailServerService.createObjectMailSettings(vm.objectMailSettings).then(
                    function (success) {
                        vm.creating = false;
                        $rootScope.hideSidePanel('right');
                        $rootScope.hideBusyIndicator();
                        $rootScope.showSuccessMessage("Project (" + vm.project.name + ") created successfully");
                        $scope.callback(vm.project);
                    }, function (error) {

                    });
            }

            function create() {
                $rootScope.closeNotification();
                validate().then(
                    function (success) {
                        attributesValidate().then(
                            function (success) {
                                if (vm.project.owner != null) {
                                    vm.project.projectOwner = vm.project.owner.id;
                                }
                                if (vm.project.portfolioObject != null) {
                                    vm.project.portfolio = vm.project.portfolioObject.id;
                                }
                                ProjectService.createProject(vm.project).then(
                                    function (data) {
                                        vm.project = data;
                                        intializationForAttributesSave().then(
                                            function (success) {
                                                if (vm.objectMailSettings.mailServerObject != null) {
                                                    createObjectSettings();
                                                } else {
                                                    vm.creating = false;
                                                    $rootScope.hideSidePanel('right');
                                                    $rootScope.hideBusyIndicator();
                                                    $rootScope.showSuccessMessage("Project (" + vm.project.name + ") created successfully");
                                                    $scope.callback(vm.project);
                                                }
                                            }, function (error) {

                                            }
                                        )
                                    }, function (error) {
                                    }
                                )
                            }, function (error) {

                            });
                    }, function (error) {

                    });
            }

            /* ............. start attributes methods block ............. */

            function addAttachment(attribute) {
                var defered = $q.defer();
                vm.propertyAttachmentIds = [];
                var counter = 0;
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'PROJECT', attachmentFile).then(
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

            function saveObjectAttributes() {
                var defered = $q.defer();
                if (vm.newProjectAttributes.length > 0) {
                    angular.forEach(vm.newProjectAttributes, function (att) {
                        if (att.dateValue == "") {
                            att.dateValue = null;
                        }
                    });
                    ObjectAttributeService.saveItemObjectAttributes(vm.project.id, vm.newProjectAttributes).then(
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
                        }, function (error) {
                            defered.reject();
                        }
                    )
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
                if (vm.newProjectAttributes != null && vm.newProjectAttributes != undefined) {
                    vm.objectAttributes = vm.objectAttributes.concat(vm.newProjectAttributes);
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
                vm.newProjectAttributes = [];
                ObjectTypeAttributeService.getObjectTypeAttributesByType("PROJECT").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.project.id,
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

                            vm.newProjectAttributes.push(att);
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
                var defered = $q.defer();
                var attrCount = 0;
                vm.propertyImageAttributes = [];
                vm.propertyImages = new Hashtable();
                vm.imageAttributes = [];
                vm.images = new Hashtable();
                vm.requiredAttributes = [];
                if (vm.newProjectAttributes.length == 0) {
                    defered.resolve();
                } else {
                    angular.forEach(vm.newProjectAttributes, function (attribute) {
                        attribute.id.objectId = vm.project.id;
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
                                    if (attrCount == vm.newProjectAttributes.length) {
                                        saveObjectAttributes().then(
                                            function (data) {
                                                vm.newProjectAttributes = [];
                                                loadObjectAttributeDefs();
                                                defered.resolve();
                                            }, function (error) {
                                                defered.reject();
                                            }
                                        )
                                    }
                                });
                        } else {
                            attrCount++;
                            if (attrCount == vm.newProjectAttributes.length) {
                                saveObjectAttributes().then(
                                    function (data) {
                                        vm.newProjectAttributes = [];
                                        loadObjectAttributeDefs();
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

            function showReceiverPassword() {
                var eyeIcon = document.getElementById("showPassword");
                var receiverPassword = document.getElementById("receiverPassword");
                if (eyeIcon.attributes.class.nodeValue == "fa fa-fw fa-eye") {
                    eyeIcon.attributes.class.nodeValue = "fa fa-fw fa-eye-slash";
                } else {
                    eyeIcon.attributes.class.nodeValue = "fa fa-fw fa-eye";
                }

                if (receiverPassword.attributes.type.nodeValue == "password") {
                    receiverPassword.attributes.type.nodeValue = "text";
                } else {
                    receiverPassword.attributes.type.nodeValue = "password";
                }
            }

            function showSenderPassword() {
                var eyeIcon = document.getElementById("showPassword1");
                var receiverPassword = document.getElementById("senderPassword");
                if (eyeIcon.attributes.class.nodeValue == "fa fa-fw fa-eye") {
                    eyeIcon.attributes.class.nodeValue = "fa fa-fw fa-eye-slash";
                } else {
                    eyeIcon.attributes.class.nodeValue = "fa fa-fw fa-eye";
                }

                if (receiverPassword.attributes.type.nodeValue == "password") {
                    receiverPassword.attributes.type.nodeValue = "text";
                } else {
                    receiverPassword.attributes.type.nodeValue = "password";
                }
            }

            function validateEmail(email) {
                var defered = $q.defer();
                var atpos = email.indexOf("@");
                var dotpos = email.lastIndexOf(".");
                if (email != null && email != undefined && email != "") {
                    if (atpos < 1 || ( dotpos - atpos < 2 )) {
                        defered.reject();
                        $rootScope.showWarningMessage(" Invalid email format");
                    } else {
                        defered.resolve();
                    }
                }
                return defered.promise;
            }

            /* ............. attributes methods end block ............. */

            $scope.$on('app.project.new', function () {
                create();
            });

            (function () {
                if ($application.homeLoaded == true) {
                    loadPersons();
                    loadPortfolios();
                    loadProjects();
                    loadObjectAttributeDefs();
                }
            })();
        }
    }
);