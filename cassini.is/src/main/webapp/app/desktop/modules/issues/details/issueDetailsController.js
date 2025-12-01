define(['app/desktop/modules/tm/tm.module',
        'dropzone',
        'app/desktop/modules/issues/details/basic/problemBasicController',
        'app/desktop/modules/issues/details/media/problemMediaController',
        'app/shared/services/issue/issueService'

    ],
    function (module) {
        module.controller('IssueDetailsController', IssueDetailsController);

        function IssueDetailsController($scope, $rootScope, $timeout, $state, $stateParams, IssueService) {
            var vm = this;

            $rootScope.viewInfo.icon = "fa flaticon-marketing8";

            vm.loading = true;
            vm.back = back;
            vm.activeTab = 0;
            vm.projectId = $stateParams.projectId;
            vm.issueId = $stateParams.issueId;
            vm.problemDetailsTabActivated = problemDetailsTabActivated;
            vm.newTask = newTask;

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: 'Basic',
                    template: 'app/desktop/modules/issues/details/basic/problemBasicView.jsp',
                    active: true
                },
                media: {
                    id: 'details.media',
                    heading: 'Media',
                    template: 'app/desktop/modules/issues/details/media/problemMediaView.jsp',
                    active: false
                }
            };

            function back() {
                window.history.back();
            }

            function activateTab(tab) {
                for (var t in vm.tabs) {
                    if (vm.tabs.hasOwnProperty(t)) {
                        vm.tabs[t].active = (t != undefined && t == tab);
                    }
                }
            }

            function problemDetailsTabActivated(tabId) {
                var tab = getTabById(tabId);
                if (tab != null) {
                    activateTab(tab);
                }
            }

            function getTabById(tabId) {
                var tab = null;
                for (var t in vm.tabs) {
                    if (vm.tabs.hasOwnProperty(t) && vm.tabs[t].id == tabId) {
                        tab = t;
                    }
                }

                return tab;
            }

            function loadIssue() {
                IssueService.getIssue($stateParams.issueId).then(
                    function (data) {
                        vm.issue = data;
                        loadIssueDetailsCount();
                    }
                )
            }

            function newTask() {
                $rootScope.$broadcast('app.project.problem.newTask');
            }

            $rootScope.loadIssueDetailsCount = loadIssueDetailsCount;
            function loadIssueDetailsCount() {
                IssueService.getIssueDetailsCount($stateParams.issueId).then(
                    function (data) {
                        vm.issueCount = data;
                        var mediaTab = document.getElementById("media");

                        mediaTab.lastElementChild.innerHTML = vm.tabs.media.heading +
                            "<span class='label label-default' style='margin-top:20px;color: black;background-color: #e4dddd;height: 20px;margin-left: 5px;padding: 3px 7px;'>{0}</span>".format(vm.issueCount.media);

                    }
                )
            }

            (function () {
                loadIssue();
            })();
        }
    }
);