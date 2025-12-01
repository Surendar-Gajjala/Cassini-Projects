define(
    [
        'app/desktop/desktop.app',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/desktop/modules/pm/pm.module',
        'app/shared/services/core/projectService',
        'app/shared/services/core/activityService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/glossaryService',
        'app/shared/services/core/specificationsService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],

    function (module) {
        module.directive('deliverablesView', DeliverablesDirectiveController);

        function DeliverablesDirectiveController($state, $window, $injector,
                                                 $rootScope, $translate, GlossaryService, $application,
                                                 ActivityService, ProjectService, $stateParams, ItemService, DialogService, SpecificationsService) {
            return {
                templateUrl: 'app/desktop/modules/directives/deliverablesDirectiveView.jsp',
                restrict: 'E',
                replace: false,
                scope: {
                    'objectType': '@',
                    'activityId': '=',
                    'taskId': '=',
                    'projectId': '=',
                    'hasPermission': '='
                },
                link: function ($scope, $elem, attrs) {
                    var revisionIds = [];
                    var revisions = [];
                    var itemIds = [];

                    $scope.nextPage = nextPage;
                    $scope.previousPage = previousPage;
                    $scope.deleteDeliverable = deleteDeliverable;
                    $scope.showItem = showItem;
                    $scope.glossaries = [];
                    $scope.deleteGlossaryDeliverable = deleteGlossaryDeliverable;
                    $scope.openGlossaryDetails = openGlossaryDetails;

                    $scope.loading = true;
                    $scope.specification = false;
                    $scope.requirement = false;
                    $scope.showGlossaryDeliverable = false;
                    $scope.showItemDeliverable = true;
                    $scope.loginPersonDetails = $rootScope.loginPersonDetails;

                    var parsed = angular.element("<div></div>");
                    var deliverable = parsed.html($translate.instant("ADD_DELIVERABLE")).html();
                    var deleteMessage = parsed.html($translate.instant("DELETE_DELIVERABLE_SUCCESS")).html();
                    var deleteDialogueMsg = parsed.html($translate.instant("DELETE_DELIVERABLE_VALIDATE")).html();
                    var deleteDialogueTitle = parsed.html($translate.instant("DELETE_DELIVERABLE")).html();
                    $rootScope.searchTitle = parsed.html($translate.instant("CLEAR_SEARCH")).html();
                    var itemDelete = parsed.html($translate.instant("ITEMDELETE")).html();
                    var glossaryDelete = parsed.html($translate.instant("ITEMDELETE")).html();
                    $scope.deleteContextMsg = parsed.html($translate.instant("PROJECT_DELIVERABLE_CONTEXT_MSG")).html();
                    var deliverableAddedToClipboard = parsed.html($translate.instant("DELIVERABLES_ADDED_TO_CLIPBOARD")).html();
                    var deliverableCopiedMessage = parsed.html($translate.instant("DELIVERABLES_COPIED_MESSAGE")).html();
                    var deliverableAlreadyExist = parsed.html($translate.instant("DELIVERABLES_ALREADY_EXIST")).html();
                    var undoSuccessful = parsed.html($translate.instant("UNDO_SUCCESSFUL")).html();
                    $scope.cannotDeleteFinishedDeliverable = parsed.html($translate.instant("CANNOT_DELETE_FINISHED_DELIVERABLE")).html();


                    var pageable = {
                        page: 0,
                        size: 20,
                        sort: {
                            field: "modifiedDate",
                            order: "DESC"
                        }
                    };

                    var pagedResults = {
                        content: [],
                        last: true,
                        totalPages: 0,
                        totalElements: 0,
                        size: pageable.size,
                        number: 0,
                        sort: null,
                        first: true,
                        numberOfElements: 0
                    };

                    $scope.items = angular.copy(pagedResults);

                    function nextPage() {
                        if ($scope.items.last != true) {
                            pageable.page++;
                        }
                    }

                    function previousPage() {
                        if ($scope.items.first != true) {
                            pageable.page--;
                        }
                    }

                    function loadDeliverables() {
                        $rootScope.showBusyIndicator();
                        $scope.loading = true;
                        $scope.specification = false;
                        $scope.requirement = false;
                        $scope.showGlossaryDeliverable = false;
                        $scope.showItemDeliverable = true;
                        revisionIds = [];
                        if ($scope.objectType == 'PROJECT') {
                            $scope.deliverableType = 'PROJECT';
                            ProjectService.getProjectAndGlossaryDeliverables($scope.projectId).then(
                                function (data) {
                                    $scope.itemDeliverables = data.itemDeliverables;
                                    $scope.loading = false;
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                        if ($scope.objectType == 'ACTIVITY') {
                            $scope.deliverableType = 'PROJECTACTIVITY';
                            ActivityService.getAllActivityDeliverables($scope.activityId).then(
                                function (data) {
                                    $scope.itemDeliverables = data.itemDeliverables;
                                    $scope.glossaries = data.glossaryDeliverables;
                                    $scope.showItemDeliverable = true;
                                    $scope.loading = false;
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                        if ($scope.objectType == 'TASK') {
                            $scope.deliverableType = 'PROJECTTASK';
                            ActivityService.getAllTaskDeliverables($scope.activityId, $scope.taskId).then(
                                function (data) {
                                    $scope.itemDeliverables = data.itemDeliverables;
                                    $scope.glossaries = data.glossaryDeliverables;
                                    $scope.specification = false;
                                    $scope.requirement = false;
                                    $scope.glossaryItem = false;
                                    $scope.showItemDeliverable = true;
                                    $scope.loading = false;
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    }

                    function checkDeliverables() {
                        if ($rootScope.clipBoardDeliverables != undefined) {
                            if ($rootScope.clipBoardDeliverables.itemIds.length > 0 || $rootScope.clipBoardDeliverables.specIds.length > 0 ||
                                $rootScope.clipBoardDeliverables.requirementIds.length > 0 || $rootScope.clipBoardDeliverables.glossaryIds.length > 0) {
                                $rootScope.objectDeliverables = true;
                            } else {
                                $rootScope.objectDeliverables = false;
                            }
                        } else {
                            $rootScope.objectDeliverables = false;
                        }
                        $scope.deliverablesAdded = $rootScope.objectDeliverables;
                    }

                    var deliverableFinishedSuccess = parsed.html($translate.instant("DELIVERABLE_FINISHED_SUCCESS_MES")).html();
                    $scope.finishDeliverable = finishDeliverable;
                    function finishDeliverable(deliverable) {
                        if ($scope.objectType == 'PROJECT') {
                            if ($rootScope.loginPersonDetails.person.id == deliverable.ownerId) {
                                ProjectService.finishProjectDeliverable(deliverable).then(
                                    function (data) {
                                        $rootScope.showSuccessMessage(deliverableFinishedSuccess);
                                        loadDeliverables();
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                    }
                                )
                            } else {
                                $rootScope.showErrorMessage("You cannot finish this deliverable");
                            }
                        }
                        if ($scope.objectType == 'ACTIVITY') {
                            if ($rootScope.loginPersonDetails.person.id == deliverable.ownerId) {
                                ActivityService.finishActivityAndTaskDeliverable('ACTIVITY', deliverable).then(
                                    function (data) {
                                        loadDeliverables();
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                    }
                                )
                            } else {
                                $rootScope.showErrorMessage("You cannot finish this deliverable");
                            }
                        }
                        if ($scope.objectType == 'TASK') {
                            if ($rootScope.loginPersonDetails.person.id == deliverable.ownerId) {
                                ActivityService.finishActivityAndTaskDeliverable('TASK', deliverable).then(
                                    function (data) {
                                        loadDeliverables();
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                    }
                                )
                            } else {
                                $rootScope.showErrorMessage("You cannot finish this deliverable");
                            }
                        }
                    }

                    function loadGlossaryDeliverables() {
                        $scope.specification = false;
                        $scope.requirement = false;
                        $scope.showItemDeliverable = false;
                        $scope.showGlossaryDeliverable = true;
                        revisionIds = [];
                        if ($scope.objectType == 'PROJECT') {
                            ProjectService.getProjectAndGlossaryDeliverables($scope.projectId).then(
                                function (data) {
                                    $scope.glossaries = data.glossaryDeliverables;
                                    $scope.loading = false;
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                        if ($scope.objectType == 'ACTIVITY') {
                            ActivityService.getAllActivityDeliverables($scope.activityId).then(
                                function (data) {
                                    $scope.itemDeliverables = data.itemDeliverables;
                                    $scope.glossaries = data.glossaryDeliverables;
                                    $scope.showGlossaryDeliverable = true;
                                    $scope.loading = false;
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                        if ($scope.objectType == 'TASK') {
                            ActivityService.getAllTaskDeliverables($scope.activityId, $scope.taskId).then(
                                function (data) {
                                    $scope.itemDeliverables = data.itemDeliverables;
                                    $scope.glossaries = data.glossaryDeliverables;
                                    $scope.specification = false;
                                    $scope.requirement = false;
                                    $scope.glossaryItem = true;
                                    $scope.showItemDeliverable = false;
                                    $scope.loading = false;
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    }

                    /*------------- Specifications And Requirements Deliverables --------------*/
                    $scope.loadSpecificationDeliverables = loadSpecificationDeliverables;
                    $scope.loadRequiremntsDeliverables = loadRequiremntsDeliverables;
                    $scope.showSpecification = showSpecification;
                    $scope.showRequirements = showRequirements;
                    $scope.deleteSpecificationDeliverable = deleteSpecificationDeliverable;
                    $scope.deleteRequirementDeliverable = deleteRequirementDeliverable;
                    $scope.specDeliverables = [];
                    $scope.reqDeliverables = [];
                    $scope.specification = false;
                    $scope.requirement = false;

                    function showSpecification() {
                        $scope.specification = true;
                        $scope.requirement = false;
                        $scope.showItemDeliverable = false;
                        $scope.showGlossaryDeliverable = false;
                        loadSpecificationDeliverables();
                    }

                    function showGlossaryItems() {
                        $scope.specification = false;
                        $scope.requirement = false;
                        $scope.showItemDeliverable = false;
                        $scope.showGlossaryDeliverable = true;
                        loadGlossaryDeliverables();
                    }

                    function showRequirements() {
                        $scope.requirement = true;
                        $scope.specification = false;
                        $scope.showItemDeliverable = false;
                        $scope.showGlossaryDeliverable = false;
                        loadRequiremntsDeliverables();
                    }

                    function loadSpecificationDeliverables() {
                        $scope.specDeliverables = [];
                        if ($scope.objectType == 'PROJECT') {
                            SpecificationsService.getSpecificationDeliverables($scope.projectId).then(
                                function (data) {
                                    $scope.specDeliverables = data.specificationDeliverables;
                                    $scope.loading = false;
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                        if ($scope.objectType == 'ACTIVITY') {
                            ActivityService.getAllSpecDeliverables($scope.activityId).then(
                                function (data) {
                                    $scope.specDeliverables = data.specificationDeliverables;
                                    $scope.loading = false;
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                        if ($scope.objectType == 'TASK') {
                            ActivityService.getAllTaskDeliverables($scope.activityId, $scope.taskId).then(
                                function (data) {
                                    $scope.specDeliverables = data.specificationDeliverables;
                                    $scope.specification = true;
                                    $scope.loading = false;
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    }

                    function loadRequiremntsDeliverables() {
                        $scope.reqDeliverables = [];
                        if ($scope.objectType == 'PROJECT') {
                            SpecificationsService.getRequirementDeliverables($scope.projectId).then(
                                function (data) {
                                    $scope.reqDeliverables = data.requirementDeliverables;
                                    $scope.loading = false;
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                        if ($scope.objectType == 'ACTIVITY') {
                            ActivityService.getAllReqDeliverables($scope.activityId).then(
                                function (data) {
                                    $scope.reqDeliverables = data.requirementDeliverables;
                                    $scope.loading = false;
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                        if ($scope.objectType == 'TASK') {
                            ActivityService.getAllTaskDeliverables($scope.activityId, $scope.taskId).then(
                                function (data) {
                                    $scope.reqDeliverables = data.requirementDeliverables;
                                    $scope.requirement = true;
                                    $scope.loading = false;
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    }

                    function deleteSpecificationDeliverable(spec) {
                        var options = {
                            title: deleteDialogueTitle,
                            message: deleteDialogueMsg + " " + spec.specification.name + " " + glossaryDelete + "?",
                            okButtonClass: 'btn-danger'
                        };
                        if ($scope.objectType == 'PROJECT') {
                            DialogService.confirm(options, function (yes) {
                                if (yes == true) {
                                    SpecificationsService.deleteSpecificationDeliverable($scope.projectId, spec.specification.id).then(
                                        function (data) {
                                            $rootScope.showSuccessMessage(deleteMessage);
                                            $scope.specification = true;
                                            $rootScope.loadProjectCounts();
                                            loadSpecificationDeliverables();
                                        }
                                    )
                                }
                            });
                        }
                        if ($scope.objectType == 'ACTIVITY') {
                            DialogService.confirm(options, function (yes) {
                                if (yes == true) {
                                    SpecificationsService.deleteSpecificationDeliverable($scope.activityId, spec.specification.id).then(
                                        function (data) {
                                            $rootScope.showSuccessMessage(deleteMessage);
                                            $rootScope.loadActivityCount();
                                            loadSpecificationDeliverables();
                                            $scope.specification = true;
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    )
                                }
                            });
                        }
                        if ($scope.objectType == 'TASK') {
                            DialogService.confirm(options, function (yes) {
                                if (yes == true) {
                                    SpecificationsService.deleteSpecificationDeliverable($scope.taskId, spec.specification.id).then(
                                        function (data) {
                                            $rootScope.showSuccessMessage(deleteMessage);
                                            $rootScope.loadTaskCount();
                                            loadSpecificationDeliverables();
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    )
                                }
                            });
                        }
                    }

                    function deleteRequirementDeliverable(req) {
                        var options = {
                            title: deleteDialogueTitle,
                            message: deleteDialogueMsg + " " + req.requirement.name + " " + glossaryDelete + "?",
                            okButtonClass: 'btn-danger'
                        };
                        if ($scope.objectType == 'PROJECT') {
                            DialogService.confirm(options, function (yes) {
                                if (yes == true) {
                                    SpecificationsService.deleteRequirementDeliverable($scope.projectId, req.requirement.id).then(
                                        function (data) {
                                            $rootScope.showSuccessMessage(deleteMessage);
                                            $scope.requirement = true;
                                            $rootScope.loadProjectCounts();
                                            loadRequiremntsDeliverables();
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    )
                                }
                            });
                        }
                        if ($scope.objectType == 'ACTIVITY') {
                            DialogService.confirm(options, function (yes) {
                                if (yes == true) {
                                    SpecificationsService.deleteRequirementDeliverable($scope.activityId, req.requirement.id).then(
                                        function (data) {
                                            $rootScope.showSuccessMessage(deleteMessage);
                                            $rootScope.loadActivityCount();
                                            loadRequiremntsDeliverables();
                                            $scope.requirement = true;
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    )
                                }
                            });
                        }
                        if ($scope.objectType == 'TASK') {
                            DialogService.confirm(options, function (yes) {
                                if (yes == true) {
                                    SpecificationsService.deleteRequirementDeliverable($scope.activityId, req.requirement.id).then(
                                        function (data) {
                                            $rootScope.showSuccessMessage(deleteMessage);
                                            $rootScope.loadTaskCount();
                                            loadRequiremntsDeliverables();
                                            $scope.requirement = true;
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    )
                                }
                            });
                        }
                    }

                    $scope.showSpecificationDetails = showSpecificationDetails;
                    $scope.showRequirementDetails = showRequirementDetails;
                    function showSpecificationDetails(spec) {
                        $window.localStorage.setItem("lastSelectedProjectTab", JSON.stringify($scope.deliverablesTabId));
                        $state.go('app.rm.specifications.details', {specId: spec.specification.id});
                    }

                    function showRequirementDetails(req) {
                        $window.localStorage.setItem("lastSelectedProjectTab", JSON.stringify($scope.deliverablesTabId));
                        $state.go('app.rm.requirements.details', {requirementId: req.requirement.id});
                    }

                    function getRevisions() {
                        itemIds = [];
                        ItemService.getRevisionsByIds(revisionIds).then(
                            function (data) {
                                revisions = data;
                                angular.forEach(revisions, function (revision) {
                                    itemIds.push(revision.itemMaster);
                                });
                                getItems();

                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        );

                    }

                    function getItems() {
                        ItemService.getItemsByIds(itemIds).then(
                            function (data) {
                                $scope.items = data;
                                ItemService.getLatestRevisionReferences($scope.items, 'latestRevision');
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }

                    var addButton = parsed.html($translate.instant("ADD")).html();

                    $scope.newDeliverable = newDeliverable;
                    function newDeliverable() {
                        var options = {
                            title: deliverable,
                            showMask: true,
                            template: 'app/desktop/modules/pm/project/details/tabs/deliverables/new/newDeliverableDialogueView.jsp',
                            controller: 'NewDeliverableController as newDeliverableVm',
                            resolve: 'app/desktop/modules/pm/project/details/tabs/deliverables/new/newDeliverableDialogueController',
                            width: 1000,
                            buttons: [
                                {text: addButton, broadcast: 'app.deliverables.new'}
                            ],
                            callback: function (data) {
                                if ($scope.selectFilter == null || $scope.selectFilter == "") {
                                    if (data.type == "Item") {
                                        loadDeliverables();
                                    }
                                    if (data.type == "Terminology") {
                                        showGlossaryItems();
                                    }
                                    if (data.type == "Specification") {
                                        showSpecification();
                                    }
                                    if (data.type == "Requirement") {
                                        showRequirements();
                                    }
                                } else {
                                    if ($scope.selectFilter == "Item") {
                                        loadDeliverables();
                                    }
                                    if ($scope.selectFilter == "Terminology") {
                                        showGlossaryItems();
                                    }
                                    if ($scope.selectFilter == "Specification") {
                                        showSpecification();
                                    }
                                    if ($scope.selectFilter == "Requirement") {
                                        showRequirements();
                                    }
                                }
                                $rootScope.loadProjectCounts();
                            }
                        };

                        $rootScope.showSidePanel(options);
                    }

                    var selectActivityDeliverable = parsed.html($translate.instant("SELECT_DELIVERABLES")).html();
                    var deliverableItemAddedMessage = parsed.html($translate.instant("SELECT_DELIVERABLE")).html();

                    $scope.addDeliverable = addDeliverable;
                    function addDeliverable() {
                        var options = {
                            title: selectActivityDeliverable,
                            template: 'app/desktop/modules/pm/project/activity/details/deliverables/selectActivityDeliverableView.jsp',
                            controller: 'SelectActivityDeliverableController as selectActivityDeliverableVm',
                            resolve: 'app/desktop/modules/pm/project/activity/details/deliverables/selectActivityDeliverableController',
                            width: 700,
                            showMask: true,
                            data: {
                                selectedActivityId: $scope.activityId,
                                selectedTaskId: $scope.taskId,
                                deliverableMode: $scope.objectType
                            },
                            buttons: [
                                {text: addButton, broadcast: 'app.project.activity.deliverable.select'}
                            ],
                            callback: function (data) {
                                $rootScope.showSuccessMessage(deliverableItemAddedMessage);
                                $scope.selected = null;
                                if ($scope.selectFilter == null || $scope.selectFilter == "") {
                                    if (data.type == "Item") {
                                        loadDeliverables();
                                    }
                                    if (data.type == "Terminology") {
                                        loadGlossaryDeliverables();
                                    }
                                    if (data.type == "Specification") {
                                        showSpecification();
                                    }
                                    if (data.type == "Requirement") {
                                        showRequirements();
                                    }
                                } else {
                                    if ($scope.selectFilter == "Item") {
                                        loadDeliverables();
                                    }
                                    if ($scope.selectFilter == "Terminology") {
                                        loadGlossaryDeliverables();
                                    }
                                    if ($scope.selectFilter == "Specification") {
                                        showSpecification();
                                    }
                                    if ($scope.selectFilter == "Requirement") {
                                        showRequirements();
                                    }
                                }
                                if ($scope.objectType == 'ACTIVITY') $rootScope.loadActivityCount();
                                if ($scope.objectType == 'TASK') $rootScope.loadTaskCount();
                                $rootScope.hideSidePanel();
                            }
                        };
                        $rootScope.showSidePanel(options);
                    }

                    function deleteDeliverable(item) {
                        var options = {
                            title: deleteDialogueTitle,
                            message: deleteDialogueMsg + " [" + item.item.itemName + " ]" + " ?",
                            okButtonClass: 'btn-danger'
                        };
                        if ($scope.objectType == 'PROJECT') {
                            DialogService.confirm(options, function (yes) {
                                if (yes == true) {
                                    ProjectService.deleteProjectDeliverable($scope.projectId, item.id).then(
                                        function (data) {
                                            $rootScope.showSuccessMessage(deleteMessage);
                                            $rootScope.loadProjectCounts();
                                            loadDeliverables();
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    )
                                }
                            });
                        }
                        if ($scope.objectType == 'ACTIVITY') {
                            DialogService.confirm(options, function (yes) {
                                if (yes == true) {
                                    ActivityService.deleteActivityDeliverable($scope.activityId, item.id).then(
                                        function (data) {
                                            $rootScope.showSuccessMessage(deleteMessage);
                                            $rootScope.loadActivityCount();
                                            loadDeliverables();
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    )
                                }
                            })
                        }
                        if ($scope.objectType == 'TASK') {
                            DialogService.confirm(options, function (yes) {
                                if (yes == true) {
                                    ActivityService.deleteTaskDeliverable($scope.activityId, $scope.taskId, item.id).then(
                                        function (data) {
                                            $rootScope.showSuccessMessage(deleteMessage);
                                            $rootScope.loadTaskCount();
                                            loadDeliverables();
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    )
                                }
                            })
                        }
                    }

                    function deleteGlossaryDeliverable(glossary) {
                        var options = {
                            title: deleteDialogueTitle,
                            message: deleteDialogueMsg + " " + glossary.name + " " + glossaryDelete + "?",
                            okButtonClass: 'btn-danger'
                        };
                        if ($scope.objectType == 'PROJECT') {
                            DialogService.confirm(options, function (yes) {
                                if (yes == true) {
                                    GlossaryService.deleteGlossaryDeliverable($scope.projectId, glossary.id).then(
                                        function (data) {
                                            $rootScope.showSuccessMessage(deleteMessage);
                                            $scope.showGlossaryDeliverable = true;
                                            $rootScope.loadProjectCounts();
                                            loadGlossaryDeliverables();
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                            $rootScope.hideBusyIndicator();
                                        }
                                    )
                                }
                            });
                        }
                        if ($scope.objectType == 'ACTIVITY') {
                            DialogService.confirm(options, function (yes) {
                                if (yes == true) {
                                    GlossaryService.deleteGlossaryDeliverable(activityId, glossary.id).then(
                                        function (data) {
                                            $rootScope.showSuccessMessage(deleteMessage);
                                            $rootScope.loadActivityCount();
                                            loadGlossaryDeliverables();
                                            $scope.showGlossaryDeliverable = true;
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                            $rootScope.hideBusyIndicator();
                                        }
                                    )
                                }
                            });
                        }
                        if ($scope.objectType == 'TASK') {
                            DialogService.confirm(options, function (yes) {
                                if (yes == true) {
                                    GlossaryService.deleteGlossaryDeliverable($scope.taskId, glossary.id).then(
                                        function (data) {
                                            $rootScope.showSuccessMessage(deleteMessage);
                                            $rootScope.loadTaskCount();
                                            loadGlossaryDeliverables();
                                            $scope.showGlossaryDeliverable = true;
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                            $rootScope.hideBusyIndicator();
                                        }
                                    )
                                }
                            });
                        }
                    }

                    function showItem(item) {
                        $window.localStorage.setItem("lastSelectedProjectTab", JSON.stringify($scope.deliverablesTabId));
                        $state.go('app.items.details', {itemId: item.latestRevision});
                    }

                    function openGlossaryDetails(glossary) {
                        $window.localStorage.setItem("lastSelectedGlossaryTab", JSON.stringify('details.entries'));
                        $window.localStorage.setItem("lastSelectedProjectTab", JSON.stringify($scope.deliverablesTabId));
                        $state.go('app.rm.glossary.details', {glossaryId: glossary.id});
                    }

                    $scope.showActivity = showActivity;
                    function showActivity(delivarable) {
                        $window.localStorage.setItem("lastSelectedProjectTab", JSON.stringify($scope.deliverablesTabId));
                        $window.localStorage.setItem("lastSelectedActivityTab", JSON.stringify('details.basic'));
                        if (delivarable.objectType == "PROJECTACTIVITY") {
                            $state.go('app.pm.project.activity.details', {
                                activityId: delivarable.objectId,
                                tab: 'details.basic'
                            });
                        } else {
                            $state.go('app.pm.project.activity.task.details', {
                                activityId: delivarable.objectId,
                                taskId: delivarable.task,
                                tab: 'details.basic'
                            })
                        }

                    }

                    $scope.switchType = switchType;
                    $scope.selected = null;
                    $scope.selectFilter = null;
                    function switchType(selected) {
                        var seletedValue = selected;

                        if (seletedValue == 'Artikel') {
                            seletedValue = 'Items';
                        }

                        if (seletedValue == 'Terminologie') {
                            seletedValue = 'Terminology';
                        }

                        if (seletedValue == 'Spezifikationen') {
                            seletedValue = 'Specifications';
                        }

                        if (seletedValue == 'Anforderung') {
                            seletedValue = 'Requirement';
                        }
                        $scope.selectFilter = seletedValue;
                        switch (seletedValue) {
                            case 'Items':
                                loadDeliverables();
                                break;
                            case 'Terminology':
                                loadGlossaryDeliverables();
                                break;
                            case 'Specifications':
                                showSpecification();
                                break;

                            case 'Requirement':
                                showRequirements();
                                break;

                            default:
                                loadDeliverables();

                        }

                    }

                    $scope.selectItem = selectItem;
                    $scope.selectedItems = [];
                    function selectItem(item) {
                        if (item.selected) {
                            $scope.selectedItems.push(item);
                        } else {
                            $scope.selectedItems.splice($scope.selectedItems.indexOf(item), 1);
                        }

                        if ($scope.selectedItems.length > 0) {
                            $rootScope.showCopyDeliverablesToClipBoard = true;
                        } else {
                            $rootScope.showCopyDeliverablesToClipBoard = false;
                        }
                    }

                    $scope.selectSpecification = selectSpecification;
                    $scope.selectedSpecs = [];
                    function selectSpecification(item) {
                        if (item.selected) {
                            $scope.selectedSpecs.push(item);
                        } else {
                            $scope.selectedSpecs.splice($scope.selectedSpecs.indexOf(item), 1);
                        }

                        if ($scope.selectedSpecs.length > 0) {
                            $rootScope.showCopyDeliverablesToClipBoard = true;
                        } else {
                            $rootScope.showCopyDeliverablesToClipBoard = false;
                        }
                    }

                    $scope.selectRequirement = selectRequirement;
                    $scope.selectedReqs = [];
                    function selectRequirement(item) {
                        if (item.selected) {
                            $scope.selectedReqs.push(item);
                        } else {
                            $scope.selectedReqs.splice($scope.selectedReqs.indexOf(item), 1);
                        }

                        if ($scope.selectedReqs.length > 0) {
                            $rootScope.showCopyDeliverablesToClipBoard = true;
                        } else {
                            $rootScope.showCopyDeliverablesToClipBoard = false;
                        }
                    }

                    $scope.selectGlossary = selectGlossary;
                    $scope.selectedGlossaries = [];
                    function selectGlossary(item) {
                        if (item.selected) {
                            $scope.selectedGlossaries.push(item);
                        } else {
                            $scope.selectedGlossaries.splice($scope.selectedGlossaries.indexOf(item), 1);
                        }

                        if ($scope.selectedGlossaries.length > 0) {
                            $rootScope.showCopyDeliverablesToClipBoard = true;
                        } else {
                            $rootScope.showCopyDeliverablesToClipBoard = false;
                        }
                    }

                    $rootScope.hidePasteFromClipBoard = false;
                    $rootScope.copyDelivarablesToClipBoard = copyDelivarablesToClipBoard;
                    $rootScope.clearAndCopyDelivarablesToClipBoard = clearAndCopyDelivarablesToClipBoard;
                    function copyDelivarablesToClipBoard() {
                        angular.forEach($scope.selectedItems, function (selectedItem) {
                            selectedItem.selected = false;
                            $application.clipboard.deliverables.itemIds.push(selectedItem.itemRevision);
                        });
                        angular.forEach($scope.selectedGlossaries, function (selectedGlossary) {
                            selectedGlossary.selected = false;
                            $application.clipboard.deliverables.glossaryIds.push(selectedGlossary.glossary.id);
                        });
                        angular.forEach($scope.selectedSpecs, function (selectedSpec) {
                            selectedSpec.selected = false;
                            $application.clipboard.deliverables.specIds.push(selectedSpec.specification.id);
                        });
                        angular.forEach($scope.selectedReqs, function (selectedReq) {
                            selectedReq.selected = false;
                            $application.clipboard.deliverables.requirementIds.push(selectedReq.requirement.id);
                        });

                        $rootScope.hidePasteFromClipBoard = true;
                        $rootScope.showCopyDeliverablesToClipBoard = false;
                        $rootScope.clipBoardDeliverables = $application.clipboard.deliverables;
                        $rootScope.objectDeliverables = true;
                        $scope.deliverablesAdded = true;
                        $rootScope.showSuccessMessage(deliverableAddedToClipboard);
                        $scope.selectedItems = [];
                        $scope.selectedGlossaries = [];
                        $scope.selectedSpecs = [];
                        $scope.selectedReqs = [];
                    }

                    function clearAndCopyDelivarablesToClipBoard() {
                        $application.clipboard.deliverables.itemIds = [];
                        $application.clipboard.deliverables.specIds = [];
                        $application.clipboard.deliverables.requirementIds = [];
                        $application.clipboard.deliverables.glossaryIds = [];
                        copyDelivarablesToClipBoard();
                    }

                    $scope.copiedDeliverables = null;
                    $scope.pasteDeliverablesFromClipboard = pasteDeliverablesFromClipboard;
                    function pasteDeliverablesFromClipboard() {
                        $rootScope.showBusyIndicator($('.view-content'));
                        if ($scope.objectType == 'PROJECT') {
                            ProjectService.pasteDeliverablesToProject($scope.projectId, $application.clipboard.deliverables).then(
                                function (data) {
                                    $scope.copiedDeliverables = data;
                                    var copiedCount = 0;
                                    if ($scope.copiedDeliverables.itemIds.length > 0) {
                                        copiedCount = copiedCount + $scope.copiedDeliverables.itemIds.length;
                                    }
                                    if ($scope.copiedDeliverables.specIds.length > 0) {
                                        copiedCount = copiedCount + $scope.copiedDeliverables.specIds.length;
                                    }
                                    if ($scope.copiedDeliverables.glossaryIds.length > 0) {
                                        copiedCount = copiedCount + $scope.copiedDeliverables.glossaryIds.length;
                                    }
                                    if ($scope.copiedDeliverables.requirementIds.length > 0) {
                                        copiedCount = copiedCount + $scope.copiedDeliverables.requirementIds.length;
                                    }

                                    $rootScope.clipBoardProjectDeliverables = $application.clipboard.deliverables;
                                    $rootScope.showCopyDeliverablesToClipBoard = false;
                                    $rootScope.loadProjectCounts();
                                    switchType($scope.selectFilter);
                                    $rootScope.hideBusyIndicator();
                                    if (copiedCount == 0) {
                                        $rootScope.showWarningMessage(deliverableAlreadyExist);
                                    } else {
                                        $rootScope.showSuccessMessage(deliverableCopiedMessage, true, "PROJECTDELIVERABLE");
                                    }
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                        if ($scope.objectType == 'ACTIVITY') {
                            ActivityService.pasteDeliverablesToActivity($scope.activityId, $application.clipboard.deliverables).then(
                                function (data) {
                                    $scope.copiedDeliverables = data;
                                    var copiedCount = 0;
                                    if ($scope.copiedDeliverables.itemIds.length > 0) {
                                        copiedCount = copiedCount + $scope.copiedDeliverables.itemIds.length;
                                    }
                                    if ($scope.copiedDeliverables.specIds.length > 0) {
                                        copiedCount = copiedCount + $scope.copiedDeliverables.specIds.length;
                                    }
                                    if ($scope.copiedDeliverables.glossaryIds.length > 0) {
                                        copiedCount = copiedCount + $scope.copiedDeliverables.glossaryIds.length;
                                    }
                                    if ($scope.copiedDeliverables.requirementIds.length > 0) {
                                        copiedCount = copiedCount + $scope.copiedDeliverables.requirementIds.length;
                                    }
                                    $rootScope.clipBoardActivityDeliverables = $application.clipboard.deliverables;
                                    $rootScope.showCopyDeliverablesToClipBoard = false;
                                    $rootScope.loadActivityCount();
                                    switchType($scope.selectFilter);
                                    $rootScope.hideBusyIndicator();
                                    if (copiedCount == 0) {
                                        $rootScope.showWarningMessage(deliverableAlreadyExist);
                                    } else {
                                        $rootScope.showSuccessMessage(deliverableCopiedMessage, true, "ACTIVITYDELIVERABLE");
                                    }

                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                        if ($scope.objectType == 'TASK') {
                            $rootScope.showBusyIndicator($('.view-content'));
                            ActivityService.pasteDeliverablesToTask($stateParams.taskId, $application.clipboard.deliverables).then(
                                function (data) {
                                    $scope.copiedDeliverables = data;
                                    var copiedCount = 0;
                                    if ($scope.copiedDeliverables.itemIds.length > 0) {
                                        copiedCount = copiedCount + $scope.copiedDeliverables.itemIds.length;
                                    }
                                    if ($scope.copiedDeliverables.specIds.length > 0) {
                                        copiedCount = copiedCount + $scope.copiedDeliverables.specIds.length;
                                    }
                                    if ($scope.copiedDeliverables.glossaryIds.length > 0) {
                                        copiedCount = copiedCount + $scope.copiedDeliverables.glossaryIds.length;
                                    }
                                    if ($scope.copiedDeliverables.requirementIds.length > 0) {
                                        copiedCount = copiedCount + $scope.copiedDeliverables.requirementIds.length;
                                    }
                                    $rootScope.clipBoardTaskDeliverables = $application.clipboard.deliverables;
                                    $rootScope.showCopyDeliverablesToClipBoard = false;
                                    switchType($scope.selectFilter);
                                    $rootScope.loadTaskCount();
                                    $rootScope.hideBusyIndicator();
                                    if (copiedCount == 0) {
                                        $rootScope.showWarningMessage(deliverableAlreadyExist);
                                    } else {
                                        $rootScope.showSuccessMessage(deliverableCopiedMessage, true, "TASKDELIVERABLE");
                                    }
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    }

                    $rootScope.undoProjectDeliverables = undoProjectDeliverables;
                    function undoProjectDeliverables() {
                        $rootScope.closeNotification();
                        $rootScope.showBusyIndicator($('.view-content'));
                        ProjectService.undoProjectDeliverables($scope.projectId, $scope.copiedDeliverables).then(
                            function (data) {
                                $rootScope.loadProjectCounts();
                                switchType($scope.selectFilter);
                                $rootScope.hideBusyIndicator();
                                $rootScope.showSuccessMessage(undoSuccessful);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }

                    $rootScope.undoActivityDeliverables = undoActivityDeliverables;
                    function undoActivityDeliverables() {
                        $rootScope.closeNotification();
                        $rootScope.showBusyIndicator($('.view-content'));
                        ActivityService.undoActivityDeliverables($scope.activityId, $scope.copiedDeliverables).then(
                            function (data) {
                                $rootScope.loadActivityCount();
                                switchType($scope.selectFilter);
                                $rootScope.hideBusyIndicator();
                                $rootScope.showSuccessMessage(undoSuccessful);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }

                    $rootScope.undoTaskDeliverables = undoTaskDeliverables;
                    function undoTaskDeliverables() {
                        $rootScope.closeNotification();
                        $rootScope.showBusyIndicator($('.view-content'));
                        ActivityService.undoTaskDeliverables($scope.taskId, $scope.copiedDeliverables).then(
                            function (data) {
                                $rootScope.loadTaskCount();
                                switchType($scope.selectFilter);
                                $rootScope.hideBusyIndicator();
                                $rootScope.showSuccessMessage(undoSuccessful);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }

                    $scope.performCustomTableAction = performCustomTableAction;
                    function performCustomTableAction(action) {
                        var service = $injector.get(action.service);
                        if (service != null && service !== undefined) {
                            var method = service[action.method];
                            if (method != null && method !== undefined && typeof method === "function") {
                                if ($rootScope.pluginTableObjectRevision != null && $rootScope.pluginTableObjectRevision != undefined) method($rootScope.pluginTableObject, $rootScope.pluginTableObjectRevision);
                                else method($rootScope.pluginTableObject);
                            }
                        }
                    }

                    $scope.resizeDropdown = resizeDropdown;
                    function resizeDropdown(deliverable) {
                        var dropdown = $('#deliverable-dropdown-' + deliverable.id);
                        if (dropdown != null) {
                            var top = $('#deliverable-dropdown-' + deliverable.id).position().top;
                            var tableBottom = $(window).height();
                            var height = $('#deliverable-dropdown-' + deliverable.id).outerHeight();
                            if (tableBottom < (top + height)) {
                                $('#deliverable-dropdown-' + deliverable.id).css("top", (top - height - 30) + "px");
                            }
                        }
                    }

                    (function () {
                        $scope.$on('app.object.deliverables', function (event, data) {
                            $scope.deliverablesTabId = data.tabId;
                            loadDeliverables();
                            checkDeliverables();
                        });
                        $scope.$on('app.pm.deliverables', newDeliverable);
                    })();
                }
            }
        }
    }
);
