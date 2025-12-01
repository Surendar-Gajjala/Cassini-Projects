define(
    [
        'app/desktop/modules/pm/project/activity/activity.module',
        'app/shared/services/core/activityService',
        'app/shared/services/core/projectService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',

    ],
    function (module) {
        module.controller('NewActivityTaskController', NewActivityTaskController);

        function NewActivityTaskController($scope, $stateParams, $q, $rootScope, $translate, $timeout, ActivityService, ObjectAttributeService, AttachmentService,
                                           AttributeAttachmentService, ObjectTypeAttributeService, ProjectService, LoginService) {

            var vm = this;

            var activityId = $stateParams.activityId;

            vm.newTask = {
                id: null,
                activity: activityId,
                name: null,
                description: null,
                assignedTo: null,
                required: false,
                percentComplete: 0,
                status: 'PENDING'
            };

            var parsed = angular.element("<div></div>");

            var activityAssignMsg = parsed.html($translate.instant("ACTIVITY_ASSIGN_MSG")).html();
            var activity = parsed.html($translate.instant("ACTIVITY")).html();
            var taskNameExist = parsed.html($translate.instant("TASK_NAME_EXIST")).html();
            var enterTaskName = parsed.html($translate.instant("ENTER_TASK_NAME")).html();
            var assignedTo = parsed.html($translate.instant("ASSIGNED_TO_VALIDATION")).html();
            var pleaseEnter = parsed.html($translate.instant("PLEASE_ENTER")).html();

            function createTask() {
                if (validate() && validateRequiredProperties()) {
                    if (vm.activity.assignedTo != null) {
                        vm.newTask.assignedTo = vm.newTask.assignedTo.id;
                        ActivityService.getActivityTaskByName(vm.activity.id, vm.newTask.name).then(
                            function (activityName) {
                                if (activityName == "") {
                                    ActivityService.createActivityTask(activityId, vm.newTask).then(
                                        function (data) {
                                            vm.activityTask = data;

                                            saveObjectAttributes().then(
                                                function (object) {

                                                    vm.newTask = {
                                                        id: null,
                                                        activity: activityId,
                                                        name: null,
                                                        description: null,
                                                        assignedTo: null,
                                                        required: false,
                                                        percentComplete: 0,
                                                        status: 'PENDING'
                                                    };
                                                    $scope.callback();
                                                    vm.taskProperties = [];
                                                    vm.taskRequiredProperties = [];
                                                    loadObjectTypeAttributes();

                                                }
                                            )

                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                            $rootScope.hideBusyIndicator();
                                        }
                                    )
                                } else {
                                    $rootScope.showWarningMessage(vm.newTask.name + " : " + taskNameExist);
                                }
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    } else {
                        $rootScope.showWarningMessage(activityAssignMsg + "(" + vm.activity.name + ")" + activity);
                    }

                }

            }

            function validate() {
                var valid = true;
                if (vm.newTask.name == null || vm.newTask.name == "") {
                    valid = false;
                    $rootScope.showWarningMessage(enterTaskName);
                } else if (vm.newTask.assignedTo == null || vm.newTask.assignedTo == "") {
                    valid = false;
                    $rootScope.showWarningMessage(assignedTo)

                }

                return valid;
            }

            function loadActivity() {
                ActivityService.getActivity(activityId).then(
                    function (data) {
                        vm.activity = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.persons = [];
            function loadPersons() {
                ProjectService.getAllProjectMembers($rootScope.projectId).then(
                    function (data) {
                        vm.persons = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            /*------Save ObjectAttributes ------*/
            function saveObjectAttributes() {
                var defered = $q.defer();
                var propertyImages = new Hashtable();
                vm.propertyImageAttributes = [];
                var objectAttrs = vm.taskProperties.concat(vm.taskRequiredProperties);
                if (objectAttrs.length > 0) {
                    angular.forEach(objectAttrs, function (attribute) {
                        attribute.id.objectId = vm.activityTask.id;
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
                        ObjectAttributeService.saveItemObjectAttributes(vm.activityTask.id, objectAttrs).then(
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
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'PROJECTTASK', attachmentFile).then(
                        function (data) {
                            propertyAttachmentIds.push(data[0].id);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                })
                return propertyAttachmentIds;
            }

            /*-------Validation for Required Properties -----------------*/
            function validateRequiredProperties() {
                var valid = true;

                if (vm.taskRequiredProperties.length > 0) {
                    angular.forEach(vm.taskRequiredProperties, function (attribute) {
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

            /*----- Load ObjectType Attributes -------*/
            vm.taskRequiredProperties = [];
            vm.taskProperties = [];
            function loadObjectTypeAttributes() {
                ObjectTypeAttributeService.getObjectTypeAttributesByType("PROJECTTASK").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newTask.id,
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
                            if (attribute.dataType == "TEXT") {
                                att.stringValue = attribute.defaultTextValue;
                            }
                            if (attribute.dataType == "LIST" && !attribute.listMultiple) {
                                att.listValue = attribute.defaultListValue;
                            }
                            if (attribute.dataType == "LIST" && attribute.listMultiple) {
                                att.mlistValue.push(attribute.defaultListValue);
                            }
                            if (attribute.lov != null) {
                                att.lovValues = attribute.lov.values;
                            }
                            if (attribute.dataType == "RICHTEXT") {
                                $timeout(function () {
                                    $(".note-editor").show();
                                    $('.note-current-fontname').text('Arial');
                                }, 1000);

                            }
                            if (attribute.required == false) {
                                vm.taskProperties.push(att);
                            } else {
                                vm.taskRequiredProperties.push(att);
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {
                loadActivity();
                loadPersons();
                loadObjectTypeAttributes();
                $scope.$on('app.project.activity.task.new', createTask);
            })();
        }
    }
);