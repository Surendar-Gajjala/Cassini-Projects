define(
    [
        'app/phone/modules/project/project.module'
    ],
    function(module) {
        module.controller('NewProjectController', NewProjectController);

        function NewProjectController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $application, $uibModalInstance) {
            var vm = this;

            (function() {
                if($application.homeLoaded == true) {





                }
            })();
        }
    }
);

