define(['app/desktop/modules/documents/document.module'
    ],
    function (module) {
        module.controller('DocumentMainController', DocumentMainController);

        function DocumentMainController($scope, $rootScope, $timeout, $state, $cookies) {
            $rootScope.setViewType('APP');
            $rootScope.viewInfo.icon = "fa fa-files-o";
            $rootScope.viewInfo.title = "Documents";
            var vm = this;
            (function () {
                if ($application.homeLoaded == true) {

                }
            })();
        }
    }
);