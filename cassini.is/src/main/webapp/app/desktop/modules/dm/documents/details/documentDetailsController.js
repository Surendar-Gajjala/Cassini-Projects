define(['app/desktop/modules/dm/dm.module',
        'app/shared/services/pm/project/documentService',
        'app/desktop/modules/col/comments/commentsDirective'
    ],
    function (module) {
        module.controller('DocumentDetailsController', DocumentDetailsController);

        function DocumentDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window,
                                           DocumentService) {
            $rootScope.viewInfo.icon = "fa fa-files-o";
            $rootScope.viewInfo.title = "Document Details";
            var vm = this;

            vm.loading = true;
            vm.document = null;
            vm.documentId = $stateParams.documentId;
            vm.back = back;

            function back() {
                $window.history.back();
            }

            function loadDocumentDetails() {
                DocumentService.getDocument($stateParams.projectId, $stateParams.folderId,
                    $stateParams.documentId).then(
                    function (data) {
                        vm.document = data;
                        vm.loading = false;
                    }
                );
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadDocumentDetails();
                }
            })();
        }
    }
);