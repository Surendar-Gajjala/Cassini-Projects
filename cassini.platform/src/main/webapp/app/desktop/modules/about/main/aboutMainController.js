/**
 * Created by Nageshreddy on 15-11-2019.
 */

define(
    [
        'app/assets/bower_components/cassini-platform/app/desktop/modules/about/about.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/appDetailsService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/licenseService'
    ],
    function (module) {
        module.controller('AboutMainController', AboutMainController);

        function AboutMainController($scope, $rootScope, $window, $interval, $state, $translate, ItemService, $timeout,
                                     AppDetailsService, $sce, LoginService, LicenseService) {

            var parsed = angular.element("<div></div>");
            $rootScope.viewInfo.icon = "fa flaticon-debit-card";
            $rootScope.viewInfo.title = parsed.html($translate.instant("ABOUT")).html();
            $rootScope.viewInfo.showDetails = false;

            var vm = this;
            var settingsUpdateSuccessMessage = parsed.html($translate.instant("SETTINGS_UPDATE_SUCCESS")).html();
            var loginAttemptsValidateMessage = parsed.html($translate.instant("LOGIN_ATTEMPTS_VALIDATION")).html();
            vm.listDetails = [];
            vm.loading = false;
            vm.updateDetails = updateDetails;
            vm.back = back;
            vm.validate = validate;

            function validate(key) {
                return (key == 12 || key == 1 || key == 2 || key == 3 || key == 4);
            }

            function back() {
                $window.history.back();
            }


            function updateSettings(details, msg) {
                AppDetailsService.updateAppDetails(details).then(
                    function (data) {
                        $rootScope.showSuccessMessage(msg);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    })
            }

            vm.updateDefaultLanguage = updateDefaultLanguage;
            function updateDefaultLanguage(details) {
                details.editMode = true;
                details.editMode = false;

                if (details.optionKey == 9) {
                    if (details.value == 'English') {
                        $rootScope.AppDefaultLanguage = 'en';
                    }
                    if (details.value == 'German') {
                        $rootScope.AppDefaultLanguage = 'de';
                    }
                }


                if (details.value == vm.previousLang) {
                    updateSettings(details, parsed.html($translate.instant("SETTINGS_UPDATE_SUCCESS")).html());
                } else {
                    $window.localStorage.removeItem("language");
                    var language = $window.localStorage.getItem("language");
                    updateSettings(details, parsed.html($translate.instant("DEFAULT_LANGUAGE_UPDATE_SUCCESS_MESSAGE")).html());
                    if ($rootScope.AppDefaultLanguage != null || $rootScope.AppDefaultLanguage != undefined || $rootScope.AppDefaultLanguage != "") {

                        if (language != null) {
                            $window.localStorage.setItem("language", language);
                            $translate.use(language);
                            changeLanguage(language);
                        }
                        else {
                            $window.localStorage.setItem("language", $rootScope.AppDefaultLanguage);
                            $translate.use($rootScope.AppDefaultLanguage);
                            changeLanguage($rootScope.AppDefaultLanguage);
                        }

                    }

                }


            }

            function updateDetails(details) {
                details.editMode = true;
                $rootScope.AppDefaultLanguage = null;

                if (details.optionKey == 11) {
                    if (details.value == null || details.value == "" || !validateNumbers(details)) {
                        details.editMode = true;
                        $rootScope.showWarningMessage(loginAttemptsValidateMessage);
                    }
                    else {
                        details.editMode = false;
                        AppDetailsService.updateAppDetails(details).then(
                            function (data) {
                                details = data;
                                if (details.optionKey == 11) {
                                    vm.previousAttempts = details.value;
                                    details.editMode = false;
                                }
                                if (details.optionKey == 6) {
                                    $rootScope.showAppLanguage = (details.value.toLowerCase() === 'true');

                                }
                                angular.forEach(vm.listDetails, function (option) {
                                    if (option.optionKey == 6) {
                                        $rootScope.showAppLanguage = (option.value.toLowerCase() === 'true');

                                    }

                                    if (option.optionKey == 8) {
                                        $rootScope.AppForgeEnable = option.value;

                                    }

                                    if (option.optionKey == 11) {
                                        $rootScope.AppLoginAttempts = option.value;

                                    }
                                });
                                $rootScope.showSuccessMessage(settingsUpdateSuccessMessage);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }


                }
                else {
                    details.editMode = false;
                    AppDetailsService.updateAppDetails(details).then(
                        function (data) {
                            details = data;
                            if (details.optionKey == 11) {
                                vm.previousAttempts = details.value;
                                details.editMode = false;
                            }

                            angular.forEach(vm.listDetails, function (option) {
                                if (option.optionKey == 6) {
                                    $rootScope.showAppLanguage = (option.value.toLowerCase() === 'true');

                                }


                                if (option.optionKey == 8) {
                                    $rootScope.AppForgeEnable = option.value;

                                }

                                if (option.optionKey == 11) {
                                    $rootScope.AppLoginAttempts = option.value;

                                }
                            });
                            $rootScope.showSuccessMessage(settingsUpdateSuccessMessage);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }


            }

            function changeLanguage(langKey) {
                LoginService.changeLanguage(langKey).then(
                    function (data) {
                        $window.localStorage.setItem("language", langKey);
                        $translate.use(langKey)
                            .then(function (languageId) {
                                performLogout();

                            });
                    }, function (error) {
                        $rootScope.showErrorMessage("Language not Changes");
                    }
                )

            }

            function performLogout() {
                settingsUpdateSuccessMessage = parsed.html($translate.instant("SETTINGS_UPDATE_SUCCESS")).html();
                $timeout(function () {
                    LoginService.logout().then(
                        function (success) {
                            $rootScope.$broadcast("app..notifications.logout");
                            $state.go('login', {}, {reload: true});
                        },
                        function (error) {
                            console.error(error);
                        }
                    );
                }, 5000);
            }

            function loadSystemInfo() {
                $rootScope.showBusyIndicator();
                vm.fileSystemSpace = null;
                AppDetailsService.getSystemInfo().then(
                    function (data) {
                        vm.fileSystemSpace = data.fileSystemSize;
                        vm.adminInfo = data.adminInfo;
                        loadSystemInformation();

                    })
            }

            vm.changeLoginAttempts = changeLoginAttempts;
            function changeLoginAttempts(details) {
                details.editMode = true;
                details.value = details.value;
            }

            vm.cancelChanges = cancelChanges;
            function cancelChanges(details) {
                details.editMode = false;
                details.value = vm.previousAttempts;
            }

            vm.validateNumbers = validateNumbers;
            function validateNumbers(details) {
                var valid = true;
                var regex = /^[1-9][0-9]*$/;
                if (!regex.test(details.value)) {
                    valid = false;
                }
                else {
                    valid = true;
                }
                return valid;
            }


            vm.previousAttempts = null;
            vm.previousLang = null;
            vm.systemUpTime = null;
            function loadDetails() {
                vm.loading = true;
                AppDetailsService.getAppDetails().then(
                    function (data) {
                        vm.listDetails = data;
                        angular.forEach(vm.listDetails, function (det) {
                            det.editMode = false;
                            if (det.optionKey == 11) {
                                vm.previousAttempts = det.value;
                            }
                            if (det.optionKey == 9) {
                                vm.previousLang = det.value;
                            }
                            if (det.optionKey == 1) {
                                vm.systemUpTime = det.systemUpTime;
                            }
                            if (det.optionKey == 4) {
                                var translatedName = parsed.html($translate.instant("RELEASE_DATE")).html();
                                det.name = translatedName;
                            } else {
                                var translatedName = parsed.html($translate.instant(det.optionName)).html();
                                det.name = translatedName;
                            }
                            $rootScope.hideBusyIndicator();

                        });
                        vm.loading = false;
                    }
                )
            }

            function loadLicenceDetails() {
                LicenseService.getLicense().then(
                    function (data) {
                        vm.licenceDetails = data;
                    }
                )
            }

            var usedSpace = parsed.html($translate.instant("DATA_USED_SPACE")).html();
            var avalableSpace = parsed.html($translate.instant("DATA_AVAILABLE_SPACE")).html();
            var totalSpace = parsed.html($translate.instant("DATA_TOTAL_SPACE")).html();
            vm.fileSystemSpace = null;
            function loadSystemInformation() {
                AppDetailsService.systemInformation().then(
                    function (data) {
                        $scope.systemInformation = data;
                        new Chart(document.getElementById("pie-chart"), {
                            type: 'pie',
                            data: {
                                labels: [usedSpace + " : " + $scope.systemInformation.usedStorageReadableFormat + " [ " + $scope.systemInformation.usedStoragePercentage + " ]",
                                    "File System Used Space" + " : " + vm.fileSystemSpace],
                                datasets: [{
                                    backgroundColor: ["#8e5ea2", "#3cba9f"],
                                    data: [$scope.systemInformation.usedStorage, $scope.systemInformation.availableStorage]
                                }]
                            },
                            options: {
                                title: {
                                    display: true,
                                    text: totalSpace + ' :' + $scope.systemInformation.totalStorageReadableFormat + " [ " + $scope.systemInformation.totalStoragePercentage + " ]"
                                }
                            }
                        });
                        loadDetails();
                        loadLicenceDetails();
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                    })
            }

            vm.systemInformation = systemInformation;
            $rootScope.systemInfo = false;
            function systemInformation() {
                $rootScope.systemInfo = $rootScope.systemInfo ? false : true;
            }


            vm.aboutInformation = aboutInformation;
            $rootScope.aboutInfo = false;
            function aboutInformation() {
                $rootScope.aboutInfo = $rootScope.aboutInfo ? false : true;
            }


            vm.applicationInformation = applicationInformation;
            $rootScope.appInfo = true;
            function applicationInformation() {
                $rootScope.appInfo = $rootScope.appInfo ? false : true;
            }

            (function () {
                loadSystemInfo();
            })();
        }
    }
)
;