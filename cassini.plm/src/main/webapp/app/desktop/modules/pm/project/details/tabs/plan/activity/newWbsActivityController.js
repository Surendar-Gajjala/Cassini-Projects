define(
    [
        'app/desktop/modules/pm/pm.module',
        'app/shared/services/core/projectService',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/shared/services/core/activityService',
        'app/shared/services/core/templateActivityService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
    ],

    function (module) {
        module.controller('NewWbsActivityController', NewWbsActivityController);

        function NewWbsActivityController($scope, $rootScope, $timeout, $q, $translate, $stateParams, $state, ProjectService,
                                          ItemService, CommonService, ActivityService, TemplateActivityService, LoginService, ObjectAttributeService, AttachmentService,
                                          AttributeAttachmentService, ObjectTypeAttributeService) {

            var vm = this;
            var parsed = angular.element("<div></div>");
            var activityCreatedMessage = parsed.html($translate.instant("ACTIVITY_CREATED_MESSAGE")).html();
            var activityUpdatedMessage = parsed.html($translate.instant("ACTIVITY_UPDATED_MESSAGE")).html();
            var nameValidation = parsed.html($translate.instant("NAME_CANNOT_BE_EMPTY")).html();
            var assignedToValidation = parsed.html($translate.instant("ASSIGNED_TO_VALIDATION")).html();
            var plannedStartDateValidation = parsed.html($translate.instant("PLANNED_START_DATE_VALIDATION")).html();
            var plannedFinishDateValidation = parsed.html($translate.instant("PLANNED_FINISH_DATE_VALIDATION")).html();
            var startDateValidation = parsed.html($translate.instant("START_DATE_VALIDATION")).html();
            var finishDateValidation = parsed.html($translate.instant("FINISH_DATE_VALIDATION")).html();

            var activityStartBeforeProject = parsed.html($translate.instant("ACTIVITY_PLANSTARTBEFORE_MSG")).html();
            var activityStartAfterProject = parsed.html($translate.instant("ACTIVITY_PLANSTARTAFTER_MSG")).html();
            var activityFinishBeforeProject = parsed.html($translate.instant("ACTIVITY_PLANFINISHBEFOREMSG")).html();
            var activityFinishAfterProject = parsed.html($translate.instant("ACTIVITY_PLANFINISHAFTERMSG")).html();
            var activityExistOnWbs = parsed.html($translate.instant("ACTIVITY_NAME_EXIST_WBS")).html();
            var pleaseEnter = parsed.html($translate.instant("PLEASE_ENTER")).html();

            vm.lastSelectedPerson = null;

            function createActivity() {
                if (validate() && validateRequiredProperties()) {
                    $rootScope.showBusyIndicator($("#rightSidePanel"));
                    vm.lastSelectedPerson = vm.newActivity.assignedTo;
                    vm.newActivity.assignedTo = vm.newActivity.assignedTo.id;
                    ActivityService.createActivity(vm.newActivity).then(
                        function (data) {
                            vm.newActivity.id = data.id;
                            saveObjectAttributes().then(
                                function (object) {
                                    $scope.callback();
                                    vm.newActivity = {
                                        id: null,
                                        name: null,
                                        description: null,
                                        wbs: vm.wbs.id,
                                        assignedTo: null,
                                        sequenceNumber: null,
                                        predecessors: [],
                                        plannedStartDate: null,
                                        plannedFinishDate: null,
                                        actualStartDate: null,
                                        actualFinishDate: null,
                                        status: 'PENDING',
                                        percentComplete: 0
                                    };
                                    vm.activityProperties = [];
                                    vm.activityRequiredProperties = [];
                                    loadObjectTypeAttributes();
                                    $rootScope.showSuccessMessage(activityCreatedMessage);
                                    $rootScope.hideBusyIndicator();

                                }
                            )

                        }, function (error) {
                            vm.newActivity.assignedTo = vm.lastSelectedPerson;
                            $rootScope.hideBusyIndicator();
                            $rootScope.showWarningMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function editActivity() {
                if (validate()) {
                    vm.newActivity.assignedTo = vm.newActivity.assignedTo.id;
                    ActivityService.updateActivity(vm.newActivity).then(
                        function (data) {
                            $scope.callback();
                            $rootScope.showSuccessMessage(activityUpdatedMessage);
                        }, function (error) {
                            vm.newActivity.name = vm.activity.name;
                            vm.newActivity.assignedTo = vm.activity.person;
                            $rootScope.showWarningMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function validate() {
                var valid = true;
                if (vm.newActivity.name == null || vm.newActivity.name == "" || vm.newActivity.name == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(nameValidation);
                } else if (vm.newActivity.assignedTo == null || vm.newActivity.assignedTo == "") {
                    valid = false;
                    $rootScope.showWarningMessage(assignedToValidation)
                } else if (vm.newActivity.plannedStartDate == null || vm.newActivity.plannedStartDate == "") {
                    valid = false;
                    $rootScope.showWarningMessage(plannedStartDateValidation)
                } else if (vm.newActivity.plannedStartDate != null && vm.newActivity.plannedStartDate != "") {
                    var today = moment(new Date());
                    var todayStr = today.format($rootScope.applicationDateSelectFormat);
                    var todayDate = moment(todayStr, $rootScope.applicationDateSelectFormat);
                    var projectFinishDate = moment(vm.project.plannedFinishDate, $rootScope.applicationDateSelectFormat);
                    var projectStartDate = moment(vm.project.plannedStartDate, $rootScope.applicationDateSelectFormat);

                    if ($rootScope.dateValidation(vm.newActivity.plannedStartDate)) {
                        vm.newActivity.plannedStartDate = $rootScope.convertDate(vm.newActivity.plannedStartDate);
                        var plannedStartDate = moment(vm.newActivity.plannedStartDate, $rootScope.applicationDateSelectFormat);

                        var val2 = plannedStartDate.isAfter(projectFinishDate);
                        var validStartDate = plannedStartDate.isBefore(projectStartDate);
                        //vm.newActivity.plannedFinishDate = $rootScope.convertDate(vm.newActivity.plannedFinishDate);
                        if (vm.newActivity.plannedFinishDate == null || vm.newActivity.plannedFinishDate == "" || vm.newActivity.plannedFinishDate == undefined) {
                            valid = false;
                            $rootScope.showWarningMessage(plannedFinishDateValidation);
                        }

                        if (val2) {
                            valid = false;
                            $rootScope.showWarningMessage(activityStartBeforeProject + " : " + vm.project.plannedFinishDate);
                        }

                        if (validStartDate) {
                            valid = false;
                            $rootScope.showWarningMessage(activityStartAfterProject + " : " + vm.project.plannedStartDate);
                        }
                    } else {
                        valid = false;
                    }

                    /*if (!val1) {
                     valid = false;
                     $rootScope.showWarningMessage(startDateValidation);
                     } else*/

                }

                if (vm.newActivity.plannedStartDate != null && vm.newActivity.plannedFinishDate != null && vm.newActivity.plannedStartDate != "" && vm.newActivity.plannedFinishDate != "") {
                    if ($rootScope.dateValidation(vm.newActivity.plannedFinishDate)) {
                        vm.newActivity.plannedFinishDate = $rootScope.convertDate(vm.newActivity.plannedFinishDate);
                        var plannedFinishDate = moment(vm.newActivity.plannedFinishDate, $rootScope.applicationDateSelectFormat);
                        var plannedStartDate = moment(vm.newActivity.plannedStartDate, $rootScope.applicationDateSelectFormat);
                        var projectPlannedFinishDate = moment(vm.project.plannedFinishDate, $rootScope.applicationDateSelectFormat);
                        var projectPlannedStartDate = moment(vm.project.plannedStartDate, $rootScope.applicationDateSelectFormat);
                        var val = plannedFinishDate.isSame(plannedStartDate) || plannedFinishDate.isAfter(plannedStartDate);
                        var val3 = plannedFinishDate.isAfter(projectPlannedFinishDate);
                        var startDateValid = plannedFinishDate.isBefore(projectPlannedStartDate);
                        if (!val) {
                            valid = false;
                            $rootScope.showWarningMessage(finishDateValidation);
                        }

                        if (val3) {
                            valid = false;
                            $rootScope.showWarningMessage(activityFinishBeforeProject + " :" + vm.project.plannedFinishDate);
                        }

                        if (startDateValid) {
                            valid = false;
                            $rootScope.showWarningMessage(activityFinishAfterProject + " :" + vm.project.plannedStartDate);
                        }
                    } else {
                        valid = false;
                    }

                }

                return valid;
            }

            function createTemplateWbsActivity() {
                if (validateTemplateActivity()) {
                    TemplateActivityService.getActivityByNameAndWbs(vm.newActivity.name, vm.newActivity.wbs).then(
                        function (activityName) {
                            if (activityName != null && activityName != "") {
                                $rootScope.showWarningMessage(vm.newActivity.name + " : " + activityExistOnWbs);
                            } else {
                                $rootScope.showBusyIndicator($("#rightSidePanel"));
                                TemplateActivityService.createTemplateActivity(vm.newActivity).then(
                                    function (data) {
                                        vm.activity = data;
                                        vm.newActivity = {
                                            id: null,
                                            name: null,
                                            description: null,
                                            wbs: vm.wbs.id
                                        };
                                        $scope.callback();
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                        $rootScope.hideBusyIndicator();
                                    }
                                )
                            }
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function editTemplateActivity() {
                if (validateTemplateActivity()) {
                    $rootScope.showBusyIndicator($("#rightSidePanel"));
                    TemplateActivityService.updateTemplateActivity(vm.newActivity).then(
                        function (data) {
                            vm.activity = data;
                            $scope.callback();
                            $rootScope.hideSidePanel();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function validateTemplateActivity() {
                var valid = true;
                if (vm.newActivity.name == null || vm.newActivity.name == "" || vm.newActivity.name == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(nameValidation);
                }

                return valid;
            }

            vm.persons = [];
            function loadProjectPersons() {
                vm.persons = [];
                ProjectService.getAllProjectMembers(vm.project.id).then(
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
                var objectAttrs = vm.activityProperties.concat(vm.activityRequiredProperties);
                if (objectAttrs.length > 0) {
                    angular.forEach(objectAttrs, function (attribute) {
                        attribute.id.objectId = vm.newActivity.id;
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
                        ObjectAttributeService.saveItemObjectAttributes(vm.newActivity.id, objectAttrs).then(
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
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'PROJECTACTIVITY', attachmentFile).then(
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

                if (vm.activityRequiredProperties.length > 0) {
                    angular.forEach(vm.activityRequiredProperties, function (attribute) {
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
            vm.activityRequiredProperties = [];
            vm.activityProperties = [];
            function loadObjectTypeAttributes() {
                ObjectTypeAttributeService.getObjectTypeAttributesByType("PROJECTACTIVITY").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newActivity.id,
                                    attributeDef: attribute.id

                                },
                                attributeDef: attribute,
                                listValue: null,
                                newListValue: null,
                                mlistValue: [],
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
                                vm.activityProperties.push(att);
                            } else {
                                vm.activityRequiredProperties.push(att);
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {
                vm.wbs = $scope.data.activityWbsData;
                vm.mode = $scope.data.activityMode;
                vm.activity = $scope.data.activityData;
                vm.project = $scope.data.projectData;
                vm.templateId = $scope.data.templateData;

                /*---------- For Project Activity -------------*/

                if (vm.mode == "Edit" && vm.activity != null) {
                    vm.newActivity = {
                        id: vm.activity.id,
                        name: vm.activity.name,
                        description: vm.activity.description,
                        wbs: vm.activity.wbs,
                        assignedTo: vm.activity.person,
                        sequenceNumber: vm.activity.sequenceNumber,
                        predecessors: vm.activity.predecessors,
                        plannedStartDate: vm.activity.plannedStartDate,
                        plannedFinishDate: vm.activity.plannedFinishDate,
                        actualStartDate: vm.activity.actualStartDate,
                        actualFinishDate: vm.activity.actualFinishDate,
                        status: vm.activity.status,
                        percentComplete: 0,
                        createdDate: vm.activity.createdDate,
                        createdBy: vm.activity.createdBy
                    };
                    loadProjectPersons();
                } else if (vm.mode == "New") {
                    vm.newActivity = {
                        id: null,
                        name: null,
                        description: null,
                        wbs: vm.wbs.id,
                        assignedTo: null,
                        sequenceNumber: null,
                        predecessors: [],
                        plannedStartDate: null,
                        plannedFinishDate: null,
                        actualStartDate: null,
                        actualFinishDate: null,
                        status: 'PENDING',
                        percentComplete: 0
                    };
                    loadObjectTypeAttributes();
                    loadProjectPersons();
                }

                $rootScope.$on('app.project.plan.activity.new', createActivity);
                $rootScope.$on('app.project.plan.activity.edit', editActivity);

                /*------------- For Template Activity -----------------*/

                if (vm.mode == "TemplateActivityEdit" && vm.activity != null) {
                    vm.newActivity = {
                        id: vm.activity.id,
                        name: vm.activity.name,
                        description: vm.activity.description,
                        wbs: vm.activity.wbs,
                        createdDate: vm.activity.createdDate,
                        createdBy: vm.activity.createdBy
                    };
                } else if (vm.mode == "TemplateActivityNew") {
                    vm.newActivity = {
                        id: null,
                        name: null,
                        description: null,
                        wbs: vm.wbs.id
                    };

                }

                $rootScope.$on('app.template.activity.new', createTemplateWbsActivity);
                $rootScope.$on('app.template.activity.edit', editTemplateActivity);
            })();
        }
    }
);