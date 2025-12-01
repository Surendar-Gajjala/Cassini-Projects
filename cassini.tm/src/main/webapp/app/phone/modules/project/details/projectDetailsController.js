define(
    [
        'app/phone/modules/project/project.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/activity/activityStreamDirective',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/comments/commentsDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/phone/modules/project/details/tabs/basic/projectBasicInfoController',
        'app/phone/modules/project/details/tabs/tasks/projectTasksController'
    ],
    function (module) {
        module.controller('ProjectDetailsController', ProjectDetailsController);

        function ProjectDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies) {

            (function () {
            })();
        }
    }
);