define(
    [
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogs.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/confirm/confirmDialogController'
    ],
    function (mdoule) {
        mdoule.factory('DialogService', DialogService);

        function DialogService($uibModal, $translate) {

            var parsed = angular.element("<div></div>");

            function confirmLogout(callback) {
                var confirmLogoutDialogMessage = parsed.html($translate.instant("LOGOUT_DIALOG_MESSAGE")).html();
                var confirmLogoutTitle = $translate.instant("LOGOUT");
                var modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/confirm/confirmDialog.jsp',
                    controller: 'ConfirmDialogController as confirmVm',
                    size: 'md',
                    resolve: {
                        modalData: function () {
                            return {
                                title: confirmLogoutTitle,
                                message: confirmLogoutDialogMessage,
                                okButtonClass: "btn-danger"
                            };
                        }
                    }
                });

                modalInstance.result.then(
                    function (result) {
                        callback(result);
                    }
                );
            }

            function confirmChangePasswordLogout(callback) {
                var changePasswordTitle = parsed.html($translate.instant("LOGOUT")).html();
                var changePasswordMessage = parsed.html($translate.instant("CHANGE_PASSWORD_MESSAGE")).html();

                var modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/confirm/confirmDialog.jsp',
                    controller: 'ConfirmDialogController as confirmVm',
                    size: 'md',
                    resolve: {
                        modalData: function () {
                            return {
                                title: changePasswordTitle,
                                message: changePasswordMessage,
                                okButtonClass: "btn-info"
                            };
                        }
                    }
                });

                modalInstance.result.then(
                    function (result) {
                        callback(result);
                    }
                );
            }

            function confirm(options, callback) {
                var modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/confirm/confirmDialog.jsp',
                    controller: 'ConfirmDialogController as confirmVm',
                    size: 'md',
                    resolve: {
                        modalData: function () {
                            return {
                                title: options.title,
                                message: options.message,
                                okButtonClass: options.okButtonClass,
                                okButtonText: options.okButtonText,
                                cancelButtonClass: options.cancelButtonClass,
                                cancelButtonText: options.cancelButtonText
                            };
                        }
                    }
                });

                modalInstance.result.then(
                    function (result) {
                        callback(result);
                    }, function () {
                        callback(false);
                    }
                );
            }

            return {
                confirm: confirm,
                confirmLogout: confirmLogout,
                confirmChangePasswordLogout: confirmChangePasswordLogout
            }
        }
    }
);