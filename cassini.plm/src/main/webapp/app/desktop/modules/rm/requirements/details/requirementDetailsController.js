define(
    [
        'app/desktop/modules/rm/requirements/requirement.module',
        'app/shared/services/core/specificationsService',
        'app/desktop/modules/rm/requirements/details/tabs/basic/requirementsBasicInfoController',
        'app/desktop/modules/rm/requirements/details/tabs/attributes/requirementAttributesController',
        'app/desktop/modules/rm/requirements/details/tabs/files/requirementsFilesController',
        'app/desktop/modules/rm/requirements/details/tabs/deliverables/requirementsDeliverableController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commentsService',
        'app/desktop/modules/rm/requirements/details/tabs/workflow/requirementWorkflowController'

    ],
    function (module) {
        module.controller('RequirementDetailsController', RequirementDetailsController);

        function RequirementDetailsController($scope, $stateParams, $rootScope, $translate, $timeout, $sce, $state,
                                              $window, SpecificationsService, CommonService, CommentsService) {
            $rootScope.viewInfo.showDetails = true;
            var vm = this;
            vm.reqDetailsTabActivated = reqDetailsTabActivated;
            vm.downloadFilesAsZip = downloadFilesAsZip;
            vm.back = back;
            var reqId = $stateParams.requirementId;
            vm.newDeliverable = newDeliverable;
            var lastSelectedTab = null;

            function back() {
                window.history.back();
            }

            vm.active = 0;

            var parsed = angular.element("<div></div>");
            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var task = parsed.html($translate.instant("TASKS")).html();
            var attributes = parsed.html($translate.instant("ATTRIBUTES")).html();
            var files = parsed.html($translate.instant("DETAILS_TAB_FILES")).html();
            var deliverables = parsed.html($translate.instant("DETAILS_TAB_DELIVERABLES")).html();
            var assignedTo = parsed.html($translate.instant("ASSIGNED_TO")).html();
            var referenceItems = parsed.html($translate.instant("REFERENCE_ITEMS")).html();
            vm.clickToFinish = parsed.html($translate.instant("CLICK_TO_FINISH")).html();
            vm.reqVersionHistory = parsed.html($translate.instant("REQ_VERSION_HISTORY")).html();
            vm.changeWorkflowTitle = parsed.html($translate.instant("CHANGE_WORKFLOW")).html();
            vm.addWorkflowTitle = parsed.html($translate.instant("ADD_WORKFLOW")).html();


            $rootScope.showCopyReqFilesToClipBoard = false;
            $rootScope.clipBoardReqFiles = [];
            $rootScope.clipBoardReqFiles = $application.clipboard.files;

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: basic,
                    index: 0,
                    template: 'app/desktop/modules/rm/requirements/details/tabs/basic/requirementsBasicInfoView.jsp',
                    active: true,
                    activated: true
                },
                attributes: {
                    id: 'details.attributes',
                    heading: 'Attributes',
                    index: 1,
                    template: 'app/desktop/modules/rm/requirements/details/tabs/attributes/requirementAttributesView.jsp',
                    active: false,
                    activated: false
                },
                files: {
                    id: 'details.files',
                    heading: files,
                    index: 2,
                    template: 'app/desktop/modules/rm/requirements/details/tabs/files/requirementsFilesView.jsp',
                    active: false,
                    activated: false
                },
                deliverables: {
                    id: 'details.deliverables',
                    heading: deliverables,
                    index: 3,
                    template: 'app/desktop/modules/rm/requirements/details/tabs/deliverables/requirementsDeliverableView.jsp',
                    active: false,
                    activated: false
                },
                workflow: {
                    id: 'details.workflow',
                    heading: 'Workflow',
                    template: 'app/desktop/modules/rm/requirements/details/tabs/workflow/requirementWorkflowView.jsp',
                    index: 4,
                    active: false,
                    activated: false
                }
            };

            function newDeliverable() {
                $scope.$broadcast('app.rm.deliverables');
            }

            function activateTab(tab) {
                for (var t in vm.tabs) {
                    vm.tabs[t].active = (vm.tabs[t].id == tab.id);
                }
            }

            vm.onClear = onClear;
            function onClear() {
                $scope.$broadcast('app.activity.tabActivated', {tabId: 'details.files'});
            }

            vm.showAllRequirements = showAllRequirements;
            function showAllRequirements() {
                $state.go('app.rm.specifications.details', {
                    specId: $rootScope.selectedSpecification.id,
                    tab: 'details.sections'
                });
            }

            vm.freeTextSearch = freeTextSearch;
            var searchMode = null;
            var freeTextQuery = null;
            $rootScope.freeTextQuerys = null;
            function freeTextSearch(freeText) {
                searchMode = "freetext";
                $rootScope.freeTextQuerys = freeText;
                freeTextQuery = null;
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    $scope.$broadcast('app.requirement.loadFiles', {name: freeText});
                }
                else {
                    $rootScope.freeTextQuerys = null;
                    $scope.$broadcast('app.requirement.tabActivated', {tabId: 'details.files'});
                }
            }

            var tabId = $stateParams.tab;

            function reqDetailsTabActivated(tabId) {
                $state.transitionTo('app.rm.requirements.details', {
                    requirementId: reqId,
                    tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    tab.activated = true;
                    tab.active = true;
                    $scope.$broadcast('app.requirement.tabActivated', {tabId: tabId});
                }
                if (tab != null) {
                    activateTab(tab);
                }
            }

            function getTabById(tabId) {
                var tab = null;
                for (var t in vm.tabs) {
                    if (vm.tabs.hasOwnProperty(t) && vm.tabs[t].id == tabId) {
                        tab = vm.tabs[t];
                    }
                }

                return tab;
            }

            $rootScope.loadRequirement = loadRequirement;
            $rootScope.selectedRequirement = null;
            $rootScope.reqName = null;
            $rootScope.description = null;
            function loadRequirement() {
                SpecificationsService.getRequirement(reqId).then(
                    function (data) {
                        $rootScope.selectedRequirement = data;
                        if ($rootScope.selectedRequirement.actualFinishDate)
                            $rootScope.selectedRequirement.actualFinishDatede = moment($rootScope.selectedRequirement.actualFinishDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                        if ($rootScope.selectedRequirement.createdDate)
                            $rootScope.selectedRequirement.createdDatede = moment($rootScope.selectedRequirement.createdDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                        if ($rootScope.selectedRequirement.modifiedDate)
                            $rootScope.selectedRequirement.modifiedDatede = moment($rootScope.selectedRequirement.modifiedDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                        if ($rootScope.selectedRequirement.plannedFinishDate)
                            $rootScope.selectedRequirement.plannedFinishDatede = moment($rootScope.selectedRequirement.plannedFinishDate, "DD/MM/YYYY").format("DD.MM.YYYY");
                        $rootScope.reqName = $rootScope.selectedRequirement.name;
                        $rootScope.description = $rootScope.selectedRequirement.description;
                        CommonService.getPersonReferences([$rootScope.selectedRequirement], 'createdBy');
                        CommonService.getPersonReferences([$rootScope.selectedRequirement], 'modifiedBy');

                        $rootScope.viewInfo.title = "<div class='item-number'>" +
                            "{0}</div> <span class='item-rev'>Version {1}</span>".
                                format($rootScope.selectedRequirement.objectNumber,
                                $rootScope.selectedRequirement.version);
                        $rootScope.viewInfo.description = $rootScope.selectedRequirement.name;
                        loadRequirementCounts();
                        loadCommentsCount();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            $rootScope.loadRequirementCounts = loadRequirementCounts;

            function loadRequirementCounts() {
                SpecificationsService.getReqDetails(reqId).then(
                    function (data) {
                        vm.reqDetails = data;
                        var filesTab = document.getElementById("files");
                        var deliverables = document.getElementById("deliverables");

                        filesTab.lastElementChild.innerHTML = vm.tabs.files.heading +
                            "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.reqDetails.files);
                        deliverables.lastElementChild.innerHTML = vm.tabs.deliverables.heading +
                            "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.reqDetails.deliverables);
                        loadSubscribe();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.changeWorkflow = changeWorkflow;
            function changeWorkflow() {
                $scope.$broadcast('app.change.workflow');
            }

            vm.addWorkflow = addWorkflow;
            function addWorkflow() {
                $scope.$broadcast('app.add.workflow');
            }

            /*----------------------Specification Subscription ------------------*/

            var unsubscribeMsg = parsed.html($translate.instant("REQ_UN_SUBSCRIBE_MSG")).html();
            var subscribeMsg = parsed.html($translate.instant("REQ_SUBSCRIBE_MSG")).html();
            var subscribeTitle = parsed.html($translate.instant("SUBSCRIBE_TITLE")).html();
            var unsubscribeTitle = parsed.html($translate.instant("UN_SUBSCRIBE_TITLE")).html();

            vm.subscribeRequirement = subscribeRequirement;

            function subscribeRequirement(item) {
                SpecificationsService.subscribeSpecificationAndRequirement(item.id).then(
                    function (data) {
                        loadSubscribe();
                        vm.subscribe = data;
                        if (vm.subscribe.subscribe) {
                            $rootScope.showSuccessMessage(subscribeMsg);
                        } else {
                            $rootScope.showSuccessMessage(unsubscribeMsg);
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadSubscribe() {
                var session = JSON.parse(localStorage.getItem('local_storage_login'));
                $rootScope.loginPersonDetails = session.login;
                SpecificationsService.getSubscribeByPerson($rootScope.selectedRequirement.id, $rootScope.loginPersonDetails.person.id).then(
                    function (data) {
                        vm.subscribe = data;
                        if (vm.subscribe == null) {
                            $scope.subscribeButtonTitle = subscribeTitle;
                        } else {
                            if (vm.subscribe.subscribe) {
                                $scope.subscribeButtonTitle = unsubscribeTitle;
                            } else {
                                $scope.subscribeButtonTitle = subscribeTitle;
                            }
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            /*--------- Show Revision history ----------*/
            vm.showVersionHistory = showVersionHistory;
            var versionHistoryTitle = parsed.html($translate.instant("VERSION_HISTORY")).html();

            function showVersionHistory() {
                var options = {
                    title: versionHistoryTitle,
                    template: 'app/desktop/modules/rm/requirements/details/requirementRevisionHistoryView.jsp',
                    controller: 'RequirementRevisionHistoryController as requirementRevisionHistoryVm',
                    resolve: 'app/desktop/modules/rm/requirements/details/requirementRevisionHistoryController',
                    data: {
                        reqId: reqId
                    },
                    width: 700
                };

                $rootScope.showSidePanel(options);
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("lastSelectedTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('REQUIREMENT', reqId).then(
                    function (data) {
                        $rootScope.showComments('REQUIREMENT', reqId, data);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function downloadFilesAsZip() {
                $rootScope.showBusyIndicator($('.view-container'));
                var url = "{0}//{1}/api/rm/requirements/{2}/files/zip".
                    format(window.location.protocol, window.location.host, reqId);

                //launchUrl(url);
                window.open(url);
                $rootScope.hideBusyIndicator();
            }

            (function () {
                //if ($application.homeLoaded == true) {
                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedTab"));
                }

                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    reqDetailsTabActivated(lastSelectedTab);

                    $timeout(function () {
                        $scope.$broadcast('app.requirement.tabActivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    reqDetailsTabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.requirement.tabActivated', {tabId: tabId});
                    }, 1000)
                }

                loadRequirement();
                //}
            })();
        }
    }
)
;