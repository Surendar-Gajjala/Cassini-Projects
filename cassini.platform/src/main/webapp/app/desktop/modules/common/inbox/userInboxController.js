
define(['app/assets/bower_components/cassini-platform/app/desktop/modules/common/common.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/inboxService'
    ],
    function(module) {
        module.controller('UserInboxController', UserInboxController);

        function UserInboxController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,InboxService) {

            $scope.messages = [];

           function userAllMessages(){
               InboxService.getUserAllMessages().
                    then(function(data){
                        $scope.messages = data;
                  });
            };

            (function() {
                userAllMessages();
            })();
        }
    }
);
