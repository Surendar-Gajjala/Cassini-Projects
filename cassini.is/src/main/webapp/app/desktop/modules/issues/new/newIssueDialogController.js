define(['app/desktop/modules/issues/issues.module',
        'app/shared/services/issue/issueService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/pm/project/projectService',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService'
    ],
    function (module) {
        module.controller('NewIssueDialogController', NewIssueDialogController);

        function NewIssueDialogController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $sce, $q,
                                          IssueService, CommonService, ProjectService, ItemService, ObjectAttributeService, AttributeAttachmentService) {
            var vm = this;

            vm.valid = true;
            vm.error = "";

            vm.priorities = ['LOW', 'MEDIUM', 'HIGH'];
            vm.newIssue = {
                title: null,
                type: null,
                description: null,
                priority: vm.priorities[0],
                assignedTo: null,
                targetObjectType: $scope.data.targetObjectType,
                targetObjectId: $scope.data.targetObjectId
            };
            if ($scope.data.targetObjectType == 'TASK') {
                vm.newIssue.status = "ASSIGNED";
                vm.newIssue.task = $scope.data.targetObjectId;
            }
            vm.types = null;
            vm.requiredAttributes = [];
            vm.create = create;

            vm.persons = [];
            vm.projectPersons = [];
            var issueMap = $scope.data.issueMap;

            $scope.trustAsHtml = function (value) {
                return $sce.trustAsHtml(value);
            };

            function create() {
                $rootScope.closeNotification();
                if (validate()) {
                    if (vm.newIssue.assignedTo != null) {
                        vm.newIssue.assignedTo = vm.newIssue.assignedTo.id;
                    }
                    vm.newIssue.type = vm.newIssue.type.id;
                    attributesValidate().then(
                        function (success) {
                            IssueService.createIssue(vm.newIssue).then(
                                function (data) {
                                    vm.newIssue = data;
                                    intializationForAttributesSave().then(
                                        function (success) {
                                            $rootScope.showSuccessMessage("Problem (" + vm.newIssue.title + ") created successfully");
                                            $rootScope.hideBusyIndicator();
                                            $rootScope.problemId = data.id;
                                            $rootScope.hideSidePanel();
                                            if (vm.newIssue.targetObjectType == 'TASK') {
                                                $rootScope.$broadcast('app.task.problems');
                                            }
                                            else {
                                                $rootScope.$broadcast('app.problems.all');
                                            }
                                        }, function (error) {

                                        });
                                }, function (error) {

                                });
                        }, function (error) {

                        });
                }
            }

            function validate() {
                vm.valid = true;

                if (vm.newIssue.type == null) {
                    vm.valid = false;
                    $rootScope.showErrorMessage("Problem Type cannot be empty");
                } else if (vm.newIssue.title == null || vm.newIssue.title.trim() == "") {
                    vm.valid = false;
                    $rootScope.showErrorMessage("Problem Title cannot be empty");
                } else if (issueMap.get(vm.newIssue.title) != null) {
                    vm.valid = false;
                    $rootScope.showErrorMessage("{0} Title already exists".format(vm.newIssue.title));
                }
                else if (vm.newIssue.priority == null || vm.newIssue.priority == "") {
                    vm.valid = false;
                    $rootScope.showErrorMessage("Problem Priority cannot be empty");
                } else if (vm.newIssue.assignedTo == null) {
                    vm.valid = false;
                    $rootScope.showErrorMessage("AssignedTo cannot be empty");
                }
                /*else if (vm.newIssue.description == null || vm.newIssue.description == "") {
                 vm.valid = false;
                 $rootScope.showErrorMessage("Description cannot be empty");
                 } */
                return vm.valid;
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
                            $rootScope.showWarningMessage(attribute.attributeDef.name + ": attribute is required");
                            $rootScope.hideBusyIndicator();
                        }
                    } else {
                        vm.validattributes.push(attribute);
                    }
                });
                vm.objectAttributes = [];
                if (vm.newProblemAttributes != null && vm.newProblemAttributes != undefined) {
                    vm.objectAttributes = vm.objectAttributes.concat(vm.newProblemAttributes);
                }
                angular.forEach(vm.objectAttributes, function (attribute) {
                    if (attribute.attributeDef.required == true && attribute.attributeDef.dataType != 'BOOLEAN' &&
                        attribute.attributeDef.dataType != 'TIMESTAMP') {
                        if (checkAttribute(attribute)) {
                            vm.validObjectAttributes.push(attribute);
                        }
                        else {
                            $rootScope.showWarningMessage(attribute.attributeDef.name + ": attribute is required");
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

            function loadIssueTypes() {
                IssueService.getIssueTypes().then(function (data) {
                    vm.types = data;
                })
            }

            function loadProjectPersons() {
                ProjectService.getProjectPersons($stateParams.projectId).then(
                    function (data) {
                        vm.projectPersons = data;
                        angular.forEach(vm.projectPersons, function (obj) {
                            loadPersonsById(obj.person);
                        })
                    }
                )
            }

            function loadPersonsById(person) {
                vm.loading = false;
                CommonService.getPerson(person).then(
                    function (data) {
                        vm.persons.push(data);
                    }
                )
            }

            function loadObjectAttributeDefs() {
                vm.newProblemAttributes = [];
                ItemService.getAllTypeAttributes("ISSUE").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newIssue.id,
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
                            vm.newProblemAttributes.push(att);
                        });
                    }
                );
            }

            function intializationForAttributesSave() {
                var defered = $q.defer();
                var attrCount = 0;
                vm.propertyImageAttributes = [];
                vm.propertyImages = new Hashtable();
                vm.imageAttributes = [];
                vm.images = new Hashtable();
                vm.requiredAttributes = [];

                if (vm.newProblemAttributes.length == 0) {
                    defered.resolve();
                }
                else {
                    angular.forEach(vm.newProblemAttributes, function (attribute) {
                        attribute.id.objectId = vm.newIssue.id;
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
                                    if (attrCount == vm.newProblemAttributes.length) {
                                        saveObjectAttributes().then(
                                            function (success) {
                                                vm.newProblemAttributes = [];
                                                loadObjectAttributeDefs();
                                                defered.resolve();
                                            }, function (erro) {
                                                defered.reject();
                                            }
                                        )
                                    }
                                });
                        } else {
                            attrCount++;
                            if (attrCount == vm.newProblemAttributes.length) {
                                saveObjectAttributes().then(
                                    function (data) {
                                        vm.newProblemAttributes = [];
                                        loadObjectAttributeDefs();
                                        defered.resolve();
                                    }
                                )
                            }
                        }
                    });
                }
                return defered.promise;
            }

            function saveObjectAttributes() {
                var defered = $q.defer();
                if (vm.newProblemAttributes.length > 0) {
                    angular.forEach(vm.newProblemAttributes, function (att) {
                        if (att.dateValue == "") {
                            att.dateValue = null;
                        }
                    });
                    ObjectAttributeService.saveItemObjectAttributes(vm.newIssue.id, vm.newProblemAttributes).then(
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

            function addAttachment(attribute) {
                var defered = $q.defer();
                vm.propertyAttachmentIds = [];
                var counter = 0;
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'TASK', attachmentFile).then(
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

            (function () {
                if ($application.homeLoaded == true) {
                    loadIssueTypes();
                    loadProjectPersons();
                    loadObjectAttributeDefs();
                    $scope.$on('app.issue.new', create);
                }
            })();
        }
    }
);