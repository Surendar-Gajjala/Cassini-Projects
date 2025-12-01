define(['app/desktop/modules/col/col.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/communication/communicationService'],
    function (module) {
        module.controller('NewDiscussionDialogController', NewDiscussionDialogController);

        function NewDiscussionDialogController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $sce,
                                               $uibModalInstance, CommonService, CommunicationService, modalData) {

            $rootScope.viewInfo.icon = "fa fa-comments";
            var vm = this;

            vm.create = create;
            vm.cancel = cancel;

            function create() {
                var data = {
                    "name": vm.name,
                    "description": vm.description,
                    "moderator": window.$application.login.person.id,
                    "ctxObjectType": "PROJECT",
                    "ctxObjectId": $state.params.projectId
                };

                CommunicationService.createDiscussionGroup(data).then(
                    function (result) {
                        $uibModalInstance.close(result);
                    }
                )
            }

            function cancel() {
                $uibModalInstance.dismiss('cancel');
            }

            (function () {
                if ($application.homeLoaded == true) {

                }
            })();
        }
    }
);