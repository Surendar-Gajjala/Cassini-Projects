define([
    'app/desktop/modules/pqm/pqm.module',
    'app/desktop/modules/directives/timelineDirective'
    
], function(module) {
        module.controller('PPAPTimelineController', PPAPTimelineController);
        function PPAPTimelineController($scope, $rootScope, $sce, $timeout, $state, $application, $translate, $stateParams, $cookies, $window, ItemTypeService, CommonService,){
            var vm = this;

            vm.loading = true;
            vm.ppapId = $stateParams.ppapId;
          

            (function () {
                $scope.$on('app.ppap.tabActivated', function (event, data) {

                    if (data.tabId == 'details.timelineHistory') {
                        $scope.$broadcast('app.object.timeline', {});
                    }

                });
            })();

           
        }
});