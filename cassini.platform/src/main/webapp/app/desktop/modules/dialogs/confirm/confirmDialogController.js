define(
    ['app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogs.module'],

    function (module) {
        module.controller('ConfirmDialogController', ConfirmDialogController);

        function ConfirmDialogController($uibModalInstance, modalData, $translate) {
            var vm = this;

            var cancel = $translate.instant("CANCEL");
            var ok = $translate.instant("OK");

            vm.title = (modalData != undefined && modalData != null &&
            modalData.title != undefined && modalData.title != null) ?
                modalData.title : "Dialog Title";

            vm.message = (modalData != undefined && modalData != null &&
            modalData.message != undefined && modalData.message != null) ?
                modalData.message : "";

            vm.okButtonText = (modalData != undefined && modalData != null &&
            modalData.okButtonText != undefined && modalData.okButtonText != null) ?
                modalData.okButtonText : ok;

            vm.cancelButtonText = (modalData != undefined && modalData != null &&
            modalData.cancelButtonText != undefined && modalData.cancelButtonText != null) ?
                modalData.cancelButtonText : cancel;

            vm.okButtonClass = (modalData != undefined && modalData != null &&
            modalData.okButtonClass != undefined && modalData.okButtonClass != null) ?
                modalData.okButtonClass : "btn-primary";

            vm.cancelButtonClass = (modalData != undefined && modalData != null &&
            modalData.cancelButtonClass != undefined && modalData.cancelButtonClass != null) ?
                modalData.cancelButtonClass : "btn-default";

            vm.onOk = onOk;
            vm.onCancel = onCancel;


            function onOk() {
                $uibModalInstance.close(true);
            }

            function onCancel() {
                $uibModalInstance.dismiss('cancel');
            }

        }
    }
);