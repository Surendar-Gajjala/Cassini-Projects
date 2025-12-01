define(
    [
        'app/desktop/modules/rm/rm.module',
        'app/shared/services/core/specificationsService',
        'app/shared/services/core/reqIFService',
        'app/desktop/modules/rm/specification/details/tabs/basic/specBasicInfoController',
        'app/desktop/modules/rm/specification/details/tabs/items/specItemsController',
        'app/desktop/modules/rm/specification/details/tabs/items/specElementController',
        'app/desktop/modules/rm/specification/details/tabs/files/specFilesController',
        'app/desktop/modules/rm/specification/details/tabs/workflow/specWorkflowController',
        'app/desktop/modules/rm/specification/details/tabs/permissions/permissionsController',
        'app/desktop/modules/rm/specification/details/tabs/attributes/specAttributesController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController'
    ],
    function (module) {
        module.controller('SpecificationDetailsController', SpecificationDetailsController);

        function SpecificationDetailsController($scope, $rootScope, $window, $timeout, $state, $stateParams, $cookies, $translate,
                                                CommonService, SpecificationsService, ReqIFService, CommentsService, $application) {

            $rootScope.viewInfo.icon = "fa flaticon-debit-card";
            $rootScope.viewInfo.title = "Specification Details";
            $rootScope.viewInfo.showDetails = true;
            var vm = this;

            var specId = $stateParams.specId;
            var lastSelectedTab = null;
            $rootScope.selectedSpecification = null;
            vm.back = back;
            vm.downloadFilesAsZip = downloadFilesAsZip;
            vm.active = 0;
            var parsed = angular.element("<div></div>");
            var basic = parsed.html($translate.instant("DETAILS_TAB_BASIC")).html();
            var fileTabName = parsed.html($translate.instant("FILES")).html();
            var reqTabName = parsed.html($translate.instant("REQUIREMENT_TAB")).html();
            var permisionTabName = parsed.html($translate.instant("PERMISSIONS")).html();
            var specificationImported = parsed.html($translate.instant("SPECIFICATION_IMPORTED")).html();
            var reqIfImported = parsed.html($translate.instant("REQIF_IMPORT_SUCCESS")).html();
            vm.reqAttributesTitle = parsed.html($translate.instant("REQ_ATTRIBUTES")).html();
            vm.specRevisionHistory = parsed.html($translate.instant("SPEC_REVISION_HISTORY")).html();
            vm.promoteSpec = parsed.html($translate.instant("PROMOTE_SPEC_LIFECYCLE_PHASE")).html();
            vm.demoteSpec = parsed.html($translate.instant("DEMOTE_SPEC_LIFECYCLE_PHASE")).html();
            vm.reviseItem = parsed.html($translate.instant("REVISE_SPECIFICATION")).html();
            vm.changeWorkflowTitle = parsed.html($translate.instant("CHANGE_WORKFLOW")).html();
            vm.addWorkflowTitle = parsed.html($translate.instant("ADD_WORKFLOW")).html();


            $rootScope.showCopySpecFilesToClipBoard = false;
            $rootScope.clipBoardSpecFiles = [];
            $rootScope.clipBoardSpecFiles = $application.clipboard.files;

            $rootScope.showRequirementsCopyToClipBoard = false;
            $rootScope.clipBoardRequirements = [];
            $rootScope.clipBoardRequirements = $application.clipboard.deliverables.requirementIds;

            vm.specDetailsTabActivated = specDetailsTabActivated;
            $scope.importExcel = importExcel;

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: basic,
                    template: 'app/desktop/modules/rm/specification/details/tabs/basic/specBasicInfoView.jsp',
                    index: 0,
                    active: true,
                    activated: true
                }
                , attributes: {
                    id: 'details.attributes',
                    heading: 'Attributes',
                    template: 'app/desktop/modules/rm/specification/details/tabs/attributes/specAttributesView.jsp',
                    index: 1,
                    active: false,
                    activated: false
                },
                items: {
                    id: 'details.sections',
                    heading: reqTabName,
                    template: 'app/desktop/modules/rm/specification/details/tabs/items/specItemsView.jsp',
                    index: 2,
                    active: false,
                    activated: false
                },

                /*        items: {
                 id: 'details.sections',
                 heading: reqTabName,
                 template: 'app/desktop/modules/rm/specification/details/tabs/items/specElementView.jsp',
                 index: 2,
                 active: false,
                 activated: false
                 },*/
                files: {
                    id: 'details.files',
                    heading: fileTabName,
                    template: 'app/desktop/modules/rm/specification/details/tabs/files/specFilesView.jsp',
                    index: 3,
                    active: false,
                    activated: false
                },
                workflow: {
                    id: 'details.workflow',
                    heading: 'Workflow',
                    template: 'app/desktop/modules/rm/specification/details/tabs/workflow/specWorkflowView.jsp',
                    index: 4,
                    active: false,
                    activated: false
                },
                permissions: {
                    id: 'details.permissions',
                    heading: permisionTabName,
                    template: 'app/desktop/modules/rm/specification/details/tabs/permissions/permissionsView.jsp',
                    index: 5,
                    active: false,
                    activated: false
                }
            };

            var tabId = $stateParams.tab;

            vm.onClear = onClear;
            function onClear() {
                $scope.$broadcast('app.activity.tabActivated', {tabId: 'details.files'});
            }

            vm.changeWorkflow = changeWorkflow;
            function changeWorkflow() {
                $scope.$broadcast('app.change.workflow');
            }

            vm.addWorkflow = addWorkflow;
            function addWorkflow() {
                $scope.$broadcast('app.add.workflow');
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
                    $scope.$broadcast('app.spec.loadFiles', {name: freeText});
                }
                else {
                    $rootScope.freeTextQuerys = null;
                    $scope.$broadcast('app.spec.tabactivated', {tabId: 'details.files'});
                }
            }


            $rootScope.loadSpecification = loadSpecification;
            $rootScope.selectedSpecification = null;
            $rootScope.specName = null;
            $rootScope.loadSpecCounts = loadSpecCounts;

            function loadSpecCounts() {
                SpecificationsService.getSpecificationDetails(specId).then(
                    function (data) {
                        vm.specDetails = data;
                        var filesTab = document.getElementById("files");
                        var requirements = document.getElementById("requirements-tab");

                        filesTab.lastElementChild.innerHTML = vm.tabs.files.heading +
                            "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.specDetails.files);
                        requirements.lastElementChild.innerHTML = vm.tabs.items.heading +
                            "<span class='label label-default' style='margin-top:20px;background-color: #e4dddd;height: 20px;margin-left: 5px;'>{0}</span>".format(vm.specDetails.requirements);

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadSpecification() {
                SpecificationsService.findById(specId).then(
                    function (data) {
                        $rootScope.selectedSpecification = data;
                        if ($rootScope.selectedSpecification.createdDate)
                            $rootScope.selectedSpecification.createdDatede = moment($rootScope.selectedSpecification.createdDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                        if ($rootScope.selectedSpecification.modifiedDate)
                            $rootScope.selectedSpecification.modifiedDatede = moment($rootScope.selectedSpecification.modifiedDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                        $rootScope.specPermission = data.specPermission;
                        $rootScope.specName = $rootScope.selectedSpecification.name;
                        CommonService.getPersonReferences([$rootScope.selectedSpecification], 'createdBy');
                        CommonService.getPersonReferences([$rootScope.selectedSpecification], 'modifiedBy');

                        $rootScope.viewInfo.title = "<div class='item-number'>" +
                            "{0}</div> <span class='item-rev'>Rev {1}</span>".
                                format($rootScope.selectedSpecification.objectNumber,
                                $rootScope.selectedSpecification.revision);
                        $rootScope.viewInfo.description = $rootScope.selectedSpecification.name;
                        loadSubscribe();
                        setLifecycles();
                        loadSpecCounts();
                    }
                )
            }

            /*----------------------Specification Subscription ------------------*/

            var parsed = angular.element("<div></div>");
            var unsubscribeMsg = parsed.html($translate.instant("SPEC_UN_SUBSCRIBE_MSG")).html();
            var subscribeMsg = parsed.html($translate.instant("SPEC_SUBSCRIBE_MSG")).html();
            var subscribeTitle = parsed.html($translate.instant("SUBSCRIBE_TITLE")).html();
            var unsubscribeTitle = parsed.html($translate.instant("UN_SUBSCRIBE_TITLE")).html();

            vm.subscribeSpecification = subscribeSpecification;

            function subscribeSpecification(item) {
                SpecificationsService.subscribeSpecificationAndRequirement(item.id).then(
                    function (data) {
                        vm.subscribe = data;
                        loadSubscribe();
                        if (vm.subscribe.subscribe) {
                            $rootScope.showSuccessMessage(subscribeMsg);
                        } else {
                            $rootScope.showSuccessMessage(unsubscribeMsg);
                        }
                    }
                )
            }

            function loadSubscribe() {
                var session = JSON.parse(localStorage.getItem('local_storage_login'));
                $rootScope.loginPersonDetails = session.login;
                SpecificationsService.getSubscribeByPerson($rootScope.selectedSpecification.id, $rootScope.loginPersonDetails.person.id).then(
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
                    }
                )
            }

            function back() {
                window.history.back();
            }

            function setLifecycles() {
                var phases = [];
                var currentPhase = $rootScope.selectedSpecification.lifecyclePhase.phase;
                var defs = $rootScope.selectedSpecification.type.lifecycle.phases;
                defs.sort(function (a, b) {
                    return a.id - b.id;
                });
                angular.forEach(defs, function (def) {
                    phases.push({
                        name: def.phase,
                        finished: false,
                        current: (def.phase == currentPhase)
                    })
                });

                var index = -1;
                for (var i = 0; i < phases.length; i++) {
                    if (phases[i].current == true) {
                        index = i;
                    }
                }

                if (index > 0) {
                    for (i = 0; i < index; i++) {
                        phases[i].finished = true;
                    }
                }

                $rootScope.setLifecyclePhases(phases);
            }

            function activateTab(tab) {
                for (var t in vm.tabs) {
                    vm.tabs[t].active = (vm.tabs[t].id == tab.id);
                }
            }

            function specDetailsTabActivated(tabId) {
                $state.transitionTo('app.rm.specifications.details', {
                    specId: specId,
                    tab: tabId
                }, {notify: false});
                var tab = getTabById(tabId);
                vm.active = tab.index;
                if (tab != null) {
                    tab.activated = true;
                    $rootScope.selectedTab = tab;
                    $scope.$broadcast('app.spec.tabactivated', {tabId: tabId});

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

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("lastSelectedSpecificationTab"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            vm.exportSpecification = exportSpecification;

            function exportSpecification(format) {
                if (format === 'reqif') {
                    var url = "{0}//{1}/api/rm/specifications/{2}/export?format=reqif".
                        format(window.location.protocol, window.location.host, specId);

                    window.open(url);
                    $timeout(function () {
                        window.close();
                    }, 2000);
                }
                else if (format == 'excel') {
                    var url = "{0}//{1}/api/rm/specifications/{2}/export?format=excel".
                        format(window.location.protocol, window.location.host, specId);
                    //launchUrl(url);
                    window.open(url);
                    $timeout(function () {
                        window.close();
                    }, 2000);
                }
            }

            $scope.importReqIF = importReqIF;
            function importReqIF() {
                $rootScope.showBusyIndicator($('.view-container'));
                var fileElem = document.getElementById("fileReqIf");
                var file = fileElem.files[0];
                SpecificationsService.importFile(specId, file, "reqif").then(
                    function (data) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showSuccessMessage(reqIfImported);
                        $scope.$broadcast('app.spec.imported', {});

                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            /*--------- Show Revision history ----------*/
            vm.showRevisionHistory = showRevisionHistory;
            var revisionHistoryTitle = parsed.html($translate.instant("REVISION_HISTORY_TITLE")).html();

            function showRevisionHistory() {
                var options = {
                    title: revisionHistoryTitle,
                    template: 'app/desktop/modules/rm/specification/details/specificationRevisionHistoryView.jsp',
                    controller: 'SpecificationRevisionHistoryController as specificationRevisionHistoryVm',
                    resolve: 'app/desktop/modules/rm/specification/details/specificationRevisionHistoryController',
                    data: {
                        specId: specId
                    },
                    width: 700
                };

                $rootScope.showSidePanel(options);
            }

            function importExcel() {
                $rootScope.showBusyIndicator($('.view-container'));
                var fileElem = document.getElementById("fileExcel");
                var file = fileElem.files[0];
                SpecificationsService.importExcel(specId, file).then(
                    function (data) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showSuccessMessage(specificationImported);
                    },
                    function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadCommentsCount() {
                CommentsService.getAllCommentsCount('SPECIFICATION', specId).then(
                    function (data) {
                        $rootScope.showComments('SPECIFICATION', specId, data);
                    }
                )
            }

            function downloadFilesAsZip() {
                $rootScope.showBusyIndicator($('.view-container'));
                var url = "{0}//{1}/api/rm/specifications/{2}/files/zip".
                    format(window.location.protocol, window.location.host, specId);

                //launchUrl(url);
                window.open(url);
                $rootScope.hideBusyIndicator();
            }

            (function () {
                if (validateJSON()) {
                    lastSelectedTab = JSON.parse($window.localStorage.getItem("lastSelectedSpecificationTab"));
                }
                $window.localStorage.setItem("lastSelectedSpecificationTab", "");

                if (lastSelectedTab != null && lastSelectedTab != undefined) {
                    specDetailsTabActivated(lastSelectedTab);

                    $timeout(function () {
                        $scope.$broadcast('app.spec.tabactivated', {tabId: lastSelectedTab});
                    }, 1000)
                } else if (tabId != null && tabId != undefined) {
                    specDetailsTabActivated(tabId);

                    $timeout(function () {
                        $scope.$broadcast('app.spec.tabactivated', {tabId: tabId});
                    }, 1000)
                }
                loadSpecification();
                $window.localStorage.setItem("lastSelectedSpecificationTab", "");
                loadCommentsCount();
            })();
        }
    }
);