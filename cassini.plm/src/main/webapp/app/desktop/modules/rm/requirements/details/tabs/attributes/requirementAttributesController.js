define(
    [
        'app/desktop/modules/rm/requirements/requirement.module',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'
    ],
    function (module) {
        module.controller('RequirementAttributesController', RequirementAttributesController);

        function RequirementAttributesController($scope, $rootScope, $sce, $timeout, $state, $stateParams, $window, $translate, $cookies) {
            var vm = this;

            vm.reqId = $stateParams.requirementId;

            (function () {
                $scope.$on('app.requirement.tabActivated', function (event, data) {
                    if (data.tabId == 'details.attributes') {
                        $scope.$broadcast('app.attributes.tabActivated', {load: true});
                    }
                });
            })();
        }
    }
);

