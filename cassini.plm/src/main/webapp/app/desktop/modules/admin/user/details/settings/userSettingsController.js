define(
    [
        'app/desktop/modules/item/item.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/preferenceService'
    ],
    function (module) {
        module.controller('UserSettingsController', UserSettingsController);

        function UserSettingsController($scope, $rootScope, $injector, $sce, $translate, $cookieStore, $window, $timeout, $application,
                                        $state, $stateParams, $cookies, PreferenceService, LoginService, $i18n) {

            var vm = this;
            var parsed = angular.element("<div></div>");
            $scope.personalPin = $i18n.getValue("PERSONAL_PIN");
            $scope.select = $i18n.getValue("SELECT");

            vm.loading = true;
            vm.loginId = $stateParams.userId;
            vm.preferences = {
                id: null,
                context: null,
                preferenceKey: null,
                stringValue: null,
                jsonValue: null
            };

            vm.loadUserPreference = loadUserPreference;
            vm.themes = ["Default", "Blue", "Red", "Pink", "Purple", "Green", "Grey", "Black", "Violet"];

            vm.dateFormats = [
                {dateFormat: "dd/MM/yyyy, HH:mm:ss", shortDateFormat: "dd/MM/yyyy"},
                {dateFormat: "dd MM yyyy HH:mm:ss", shortDateFormat: "dd MM yyyy"},
                {dateFormat: "dd.MM.yyyy, HH:mm:ss", shortDateFormat: "dd.MM.yyyy"},
                {dateFormat: "MM-dd-yyyy, HH:mm:ss", shortDateFormat: "MM-dd-yyyy"}
            ];

            function loadUserPreference() {
                LoginService.getUserPreference($rootScope.loginDetails.id).then(
                    function (data) {
                        vm.userPreferences = data;
                        if (vm.userPreferences.userTheme == null || vm.userPreferences.userTheme == "") {
                            vm.userPreferences.userTheme = "Default";
                        }
                        if (vm.userPreferences.userDateFormat != null && vm.userPreferences.userDateFormat != "") {
                            $rootScope.applicationDateFormat = vm.userPreferences.userDateFormat;

                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
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

            var settingsSaved = parsed.html($translate.instant("SETTINGS_SAVED")).html();
            $scope.updateApprovalPassword = parsed.html($translate.instant("UPDATE_CHANGE_APPROVAL_PASSWORD")).html();
            $scope.addLastName = parsed.html($translate.instant("ADD_LAST_NAME")).html();
            $scope.addPhoneNumber = parsed.html($translate.instant("ADD_PHONE_NUMBER")).html();
            var settingsSavedSuccessfully = parsed.html($translate.instant("SETTINGS_SAVED_MESSAGE")).html();

            vm.updatePreference = updatePreference;

            function updatePreference() {
                if (vm.userPreferences.userTheme == 'Default') {
                    vm.userPreferences.userTheme = "";
                }
                LoginService.updateUserPreference(vm.userPreferences).then(
                    function (data) {
                        $rootScope.showSuccessMessage(settingsSavedSuccessfully);
                        $rootScope.hideBusyIndicator();
                        performLogout();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );
            }

            function performLogout() {
                $timeout(function () {
                    LoginService.logout().then(
                        function (success) {
                            $rootScope.$broadcast("app.notifications.logout");
                            $state.go('login', {}, {reload: true});
                            $timeout(function () {
                                window.location.reload();
                            }, 100);
                        },
                        function (error) {
                            console.error(error);
                        }
                    );
                }, 2000);
            }


            var defaultPagaMessage = parsed.html($translate.instant("DEFAULT_PAGE_MESSAGE")).html();
            vm.resetPreferredPage = resetPreferredPage;
            function resetPreferredPage() {
                vm.userPreferences.preferredPage = null;
                vm.userPreferences.login = $rootScope.loginPersonDetails.id;
                LoginService.savePreferredPage(vm.userPreferences).then(
                    function (data) {
                        loadUserPreference();
                        $rootScope.showSuccessMessage(defaultPagaMessage);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            function loadChangeApprovalPassword() {
                PreferenceService.getUserChangeApprovalPassword($stateParams.userId).then(
                    function (data) {
                        if (data != null && data != "") {
                            vm.preference = data;
                        } else {
                            vm.preference = {
                                id: null,
                                jsonValue: null,
                                stringValue: null
                            }
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.updatingApproval = false;
            vm.createChangeApprovalPassword = createChangeApprovalPassword;
            function createChangeApprovalPassword() {
                vm.preference.context = "USER";
                vm.preference.preferenceKey = $stateParams.userId + "_CHANGE_APPROVAL";
                vm.preference.stringValue = vm.preference.jsonValue;
                PreferenceService.createChangeApprovalPassword(vm.preference).then(
                    function (data) {
                        vm.preference = data;
                        loadUserPreference();
                        $rootScope.showSuccessMessage(settingsSaved);
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        vm.updatingApproval = false;
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.onSelectDateFormat = onSelectDateFormat;
            function onSelectDateFormat(dateFormat) {
                vm.userPreferences.shortDateFormat = dateFormat.shortDateFormat;
            }

            (function () {
                loadUserPreference();
                loadChangeApprovalPassword();
            })();
        }
    }
);