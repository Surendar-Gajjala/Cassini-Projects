define(['app/assets/bower_components/cassini-platform/app/desktop/modules/admin/admin.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/sessionService'],
    function(module){

        module.controller('SessionDetailsController',SessionDetailsController);

        function SessionDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $uibModal,
                                 SessionService) {
            var vm = this;

            $rootScope.iconClass = "fa fa-lock";
            $rootScope.viewTitle = "Session (" + $stateParams.sessionId + ")";

            vm.session = {
                loginTime: "&nbsp;",
                logoutTime: "&nbsp;"
            };
            vm.history = [];

            SessionService.getSession($stateParams.sessionId).then (
                function(session) {
                    if(session.logoutTime == null || session.logoutTime == "") {
                        session.logoutTime = "-";
                    }
                    vm.session = session;
                    return SessionService.getSession($stateParams.sessionId);
                },

                function(error) {
                    console.error(error);
                }
            ).then (
                function(history) {
                    $scope.history = history;
                },

                function(error) {
                    console.error(error);
                }
            );
        }


            (function() {

            })();

    }
)