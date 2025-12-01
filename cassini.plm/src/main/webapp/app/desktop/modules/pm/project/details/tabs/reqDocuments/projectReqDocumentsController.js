define(['app/desktop/modules/pm/pm.module',
        'app/shared/services/core/projectService',
        'app/shared/services/core/activityService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'

    ],
    function (module) {
        module.controller('ProjectReqDocumentController', ProjectReqDocumentController);

        function ProjectReqDocumentController($scope, $rootScope, $timeout, $state, $translate, $stateParams, $cookies, ProjectService, DialogService, CommonService) {

            var vm = this;

            vm.loading = true;
            vm.projectId = $stateParams.projectId;
            vm.projectReqDocuments = [];
            vm.addRequirementDocuments = addRequirementDocuments;
            vm.deleteProjectReqDocument = deleteProjectReqDocument;

            var parsed = angular.element("<div></div>");
            var selectPerson = parsed.html($translate.instant("SELECT_PERSON")).html();
            var addButton = parsed.html($translate.instant("ADD")).html();
            var removeReqDocTitle = parsed.html($translate.instant("REMOVE_REQ_DOCUMENT")).html();
            var removeReqDocDialogMsg = parsed.html($translate.instant("REMOVE_REQ_DOC_MSG")).html();
            var reqDocRemoved = parsed.html($translate.instant("REQ_DCO_REMOVED")).html();


            function loadProjectRequirementDocuments() {
                vm.projectReqDocuments = [];
                $rootScope.showBusyIndicator();
                vm.loading = true;
                ProjectService.getProjectReqDocuments($stateParams.projectId).then(
                    function (data) {
                        vm.projectReqDocuments = data;
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function addRequirementDocuments() {
                var options = {
                    title: "Add Requirement Documents",
                    showMask: true,
                    template: 'app/desktop/modules/pm/project/details/tabs/reqDocuments/reqDocumentsSelectionView.jsp',
                    controller: 'ReqDocumentsSelectionController as reqDocumentsSelectionVm',
                    resolve: 'app/desktop/modules/pm/project/details/tabs/reqDocuments/reqDocumentsSelectionController',
                    width: 700,
                    data: {
                        selectedProjectId: vm.projectId,
                        selectedProject: $rootScope.projectInfo
                    },
                    buttons: [
                        {text: addButton, broadcast: 'app.project.reqDocument.add'}
                    ],
                    callback: function () {
                        loadProjectRequirementDocuments();
                        $rootScope.loadProjectCounts();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function deleteProjectReqDocument(reqDoc) {
                var options = {
                    title: removeReqDocTitle,
                    message: removeReqDocDialogMsg,
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        ProjectService.deleteProjectReqDocument(vm.projectId, reqDoc.id).then(
                            function (data) {
                                $rootScope.showSuccessMessage(reqDocRemoved);
                                loadProjectRequirementDocuments();
                                $rootScope.loadProjectCounts();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                });
            }

            vm.showReqDocument = showReqDocument;
            function showReqDocument(projectReqDocument) {
                $state.go('app.req.document.details', {reqId: projectReqDocument.reqDocument.id, tab: 'details.basic'});
            }

            (function () {
                $scope.$on('app.project.tabactivated', function (event, data) {
                    if (data.tabId == 'details.reqDocuments') {
                        loadProjectRequirementDocuments();
                    }
                });
            })();

        }
    }
);

