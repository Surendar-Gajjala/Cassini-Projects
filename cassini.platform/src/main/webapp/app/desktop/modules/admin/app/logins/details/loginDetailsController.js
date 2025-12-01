define(['app/assets/bower_components/cassini-platform/app/desktop/modules/admin/admin.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/logins/details/changePasswordController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/preferenceService'
    ],
    function (module) {
        module.controller('LoginDetailsController', LoginDetailsController);

        function LoginDetailsController($scope, $rootScope, $timeout, $window, $state, $stateParams, $uibModal,
                                        LoginService, CommonService, $translate, PreferenceService) {


            var vm = this;
            var parsed = angular.element("<div></div>");
            vm.backTitle = parsed.html($translate.instant("BACK")).html();
            vm.saveTitle = parsed.html($translate.instant("SAVE")).html();
            vm.changePasswordTitle = parsed.html($translate.instant("CHANGE_PASSWORD")).html();
            $rootScope.viewInfo.icon = "fa fa-key";
            $rootScope.viewInfo.title = parsed.html($translate.instant("LOGIN_DETAILS")).html();
            $rootScope.viewInfo.showDetails = false;

            vm.login = null;

            vm.loading = true;
            vm.loginId = $stateParams.loginId;
            vm.userPreferences = null;
            vm.preference = {
                id: null,
                context: null,
                preferenceKey: null,
                stringValue: null,
                jsonValue: null
            };

            vm.updateLogin = updateLogin;
            vm.changePassword = changePassword;
            vm.back = back;
            vm.loadUserPreference = loadUserPreference;
            vm.themes = ["Default", "Blue", "Red", "Pink", "Purple", "Green", "Grey", "Black", "Violet"];
            function loadUserPreference() {
                var loginId = $stateParams.loginId;
                LoginService.getUserPreference(loginId).then(
                    function (data) {
                        vm.userPreferences = data;
                        if (vm.userPreferences.userTheme) vm.userPreferences.userTheme = data.userTheme;
                        else vm.userPreferences.userTheme = "Default";
                    }
                );
            }


            function changeTheme(el) {
                var theme = el;
                if (theme != null) {
                    if (theme == "Default") {
                        $("#footer").css('color', 'white');
                        $('#' + el).css('display', 'none');
                        $("#footer").css('background-color', "#00253f");
                        $(".headerbar").css('background-color', "#00253f");
                    } else if (theme == "Violet") {
                        $("#footer").css('color', 'black');
                        $('#' + el).css('display', 'block');
                        $("#footer").css('background-color', el);
                        $(".headerbar").css('background-color', el);
                    } else if (theme == "Pink") {
                        $("#footer").css('color', 'black');
                        $('#' + el).css('display', 'block');
                        $("#footer").css('background-color', el);
                        $(".headerbar").css('background-color', el);
                    } else {
                        $("#footer").css('color', 'white');
                        $('#' + el).css('display', 'block');
                        $("#footer").css('background-color', el);
                        $(".headerbar").css('background-color', el);
                    }
                } else {
                    $('#' + el).css('display', 'none');
                    $("#footer").css('background-color', "#00253f");
                    $(".headerbar").css('background-color', "#00253f");
                }
            }

            function loadLogin() {
                vm.loading = true;
                loadUserPreference();
                var loginId = $stateParams.loginId;
                LoginService.getLogin(loginId).then(
                    function (data) {
                        vm.loading = false;
                        vm.login = data;

                    }
                );
            }

            var loginSavedMessage = parsed.html($translate.instant("LOGIN_DETAILS_SUCCESS")).html();
            var emailValidation = parsed.html($translate.instant("EMAIL_VALIDATION")).html();
            var nameValidation = parsed.html($translate.instant("FIRST_NAME_VALIDATION")).html();
            var themeSuccessMessage = parsed.html($translate.instant("THEME_SUCCESS_MESSAGE")).html();
            var changeApprovalUpdated = parsed.html($translate.instant("CHANGE_APPROVAL_PASSWORD_UPDATED")).html();
            var changeApprovalValidation = parsed.html($translate.instant("CHANGE_APPROVAL_PASSWORD_VALIDATION")).html();
            $scope.updateApprovalPassword = parsed.html($translate.instant("UPDATE_CHANGE_APPROVAL_PASSWORD")).html();
            $scope.addLastName = parsed.html($translate.instant("ADD_LAST_NAME")).html();
            $scope.addPhoneNumber = parsed.html($translate.instant("ADD_PHONE_NUMBER")).html();

            function updateLogin() {
                if (validate()) {
                    CommonService.updatePerson(vm.login.person).then(
                        function (data) {
                            vm.login.person = data;
                            $rootScope.personInfo = data;
                            return LoginService.updateLogin(vm.login);
                        }
                    ).then(
                        function (data) {
                            $rootScope.showSuccessMessage(loginSavedMessage);
                            $rootScope.$broadcast("app.main.groups");
                        }
                    );

                }
            }

            vm.updateUserPreference = updateUserPreference;

            function updateUserPreference() {
                LoginService.updateUserPreference(vm.userPreferences).then(
                    function (data) {
                        //changeTheme(vm.userPreferences.userTheme);
                        loadUserPreference();
                        $rootScope.showSuccessMessage(themeSuccessMessage);
                    }
                );
            }

            vm.updateDateFormat = updateDateFormat;

            function updateDateFormat() {
                LoginService.updateUserPreference(vm.userPreferences).then(
                    function (data) {
                        $rootScope.applicationDateFormat = vm.userPreferences.userDateFormat;
                        $rootScope.showSuccessMessage("Date format updated successfully");
                    }
                );
            }

            var defaultPagaMessage = parsed.html($translate.instant("DEFAULT_PAGE_MESSAGE")).html();
            vm.resetPreferredPage = resetPreferredPage;
            function resetPreferredPage() {
                vm.userPreferences.preferredPage = null;
                vm.userPreferences.login = $rootScope.loginPersonDetails.id;
                LoginService.savePreferredPage(vm.userPreferences).then(
                    function (data) {
                        $rootScope.showSuccessMessage(defaultPagaMessage);
                    }, function (error) {
                        console.log(error);
                    }
                );
            }

            function changePassword() {
                var modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/logins/details/changePasswordView.jsp',
                    controller: 'ChangePasswordController as chPassVm',
                    size: 'lg'
                });

                modalInstance.result.then(
                    function (result) {

                    });
            }

            function back() {
                $window.history.back();
            }

            function validate() {
                var valid = true;
                if (vm.login.person.email == null || vm.login.person.email == ""
                    || vm.login.person.email == undefined) {
                    valid = false;
                    loadLogin();
                    $rootScope.showWarningMessage(emailValidation);

                } else if (vm.login.person.firstName == null || vm.login.person.firstName == ""
                    || vm.login.person.firstName == undefined) {
                    valid = false;
                    loadLogin();
                    $rootScope.showWarningMessage(nameValidation);
                }
                return valid;
            }

            function loadChangeApprovalPassword() {
                PreferenceService.getUserChangeApprovalPassword($stateParams.loginId).then(
                    function (data) {
                        if (data != null && data != "") {
                            vm.preference = data;
                            vm.preference.jsonValue = null;
                            vm.preference.stringValue = "*******";
                        }
                        loadChangeApprovalPreference();
                    }
                )
            }

            function loadChangeApprovalPreference() {
                var key = "APPLICATION.CHANGE_APPROVAL";
                PreferenceService.getPreferenceByKey(key).then(
                    function (data) {
                        if (data == null || data == "") {
                            vm.changeApprovalPreference = {
                                booleanValue: false
                            }
                        } else {
                            vm.changeApprovalPreference = data;
                        }
                    }
                )
            }

            vm.updatingApproval = false;
            vm.createChangeApprovalPassword = createChangeApprovalPassword;
            function createChangeApprovalPassword() {
                if (vm.preference.jsonValue != null && vm.preference.jsonValue != "") {
                    vm.updatingApproval = true;
                    $rootScope.showBusyIndicator($('.view-content'));
                    vm.preference.context = "USER";
                    vm.preference.preferenceKey = $stateParams.loginId + "_CHANGE_APPROVAL";
                    vm.preference.stringValue = vm.preference.jsonValue;
                    PreferenceService.createChangeApprovalPassword(vm.preference).then(
                        function (data) {
                            vm.preference = data;
                            vm.preference.jsonValue = null;
                            vm.preference.stringValue = "*******";
                            vm.updatingApproval = false;
                            $rootScope.showSuccessMessage(changeApprovalUpdated);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            vm.updatingApproval = false;
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    vm.updatingApproval = false;
                    $rootScope.showWarningMessage(changeApprovalValidation);
                    $('#changePassword').editable();
                }
            }


            (function () {
                //if ($application.homeLoaded == true) {
                //  alert('before load');
                loadLogin();
                loadChangeApprovalPassword();
                //}
            })();
        }
    }
);