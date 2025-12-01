define(
    [
        'app/desktop/modules/col/col.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/desktop/modules/col/communication/messages/messagesController',
        'app/desktop/modules/col/communication/email/emailController'
    ],
    function (module) {
        module.controller('CommunicationMainController', CommunicationMainController);

        function CommunicationMainController($scope, $rootScope, $timeout, $state, $stateParams, $cookies) {

            var vm = this;

            $rootScope.viewInfo.icon = "fa fa-comments";
            $rootScope.viewInfo.title = "Communication";

            vm.activeTab = 0;
            vm.communicationTabActivated = communicationTabActivated;

            vm.tabs = {
                email: {
                    id: 'communication.email',
                    heading: 'Email',
                    template: 'app/desktop/modules/col/communication/email/emailView.jsp',
                    active: true
                },
                messaging: {
                    id: 'communication.messaging',
                    heading: 'Messaging',
                    template: 'app/desktop/modules/col/communication/messages/messagesView.jsp',
                    active: false
                }
            };

            function activateTab(tab) {
                for (var t in vm.tabs) {
                    if (vm.tabs.hasOwnProperty(t)) {
                        vm.tabs[t].active = (t != undefined && t == tab);
                    }
                }
            }

            function communicationTabActivated(tabId) {
                $scope.$broadcast('app.project.communication', {tabId: tabId});
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

            (function () {
                if ($application.homeLoaded == true) {
                }

            })();
        }
    }
);