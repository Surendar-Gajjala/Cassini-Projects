define(
    [
        'app/desktop/modules/settings/settings.module',
        'split-pane',
        'jquery.easyui',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/preferenceService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/licenseService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/personGrpService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/appDetailsService',
        'app/shared/services/core/lifecycleService'
    ],
    function (module) {
        module.controller('SystemsettingsController', SystemsettingsController);

        function SystemsettingsController($q, $scope, $rootScope, $timeout, $state, DialogService, $stateParams, $cookies, LifecycleService,
                                          AppDetailsService, CommonService, $translate, LoginService, PreferenceService, LicenseService, LovService, AutonumberService, PersonGroupService) {

            var vm = this;
            var parsed = angular.element("<div></div>");

            vm.onSubmit = onSubmit;
            vm.onCancel = onCancel;
            vm.passwordUpdate = false;
            vm.logoutUpdate = false;
            vm.logoUpdate = false;
            vm.theamUpdate = false;
            vm.formatUpdate = false;
            vm.ipAddress = [];

            var defaultTheam = "Default";

            var characters = parsed.html($translate.instant("CHARACTERS")).html();
            var no = parsed.html($translate.instant("NO")).html();
            var numbersOnly = parsed.html($translate.instant("PASSWORD_NUMBERS_ONLY")).html();
            var numbersandspecialcharactersTitle = parsed.html($translate.instant("PASSWORD_NUMBERS_AND_SPECIAL_CHARACTERS")).html();
            var drcUpdated = parsed.html($translate.instant("DRC_UPDATED")).html();
            var defaultValueUpdated = parsed.html($translate.instant("DEFAULT_VALUE_UPDATED")).html();
            var selectPasscodeExpiration = parsed.html($translate.instant("P_S_TWO_FACTOR_EXPIRATION")).html();
            var twoFactorPreferenceUpdated = parsed.html($translate.instant("TWO_FACTOR_PREFERENCE_UPDATED")).html();
            $scope.selectConfigurationTitle = parsed.html($translate.instant("SELECT_CONFIGURATION")).html();
            var context = 'SYSTEM';

            vm.passcodeExpirations = [15, 30, 45, 60];

            $scope.data = {
                minLen: [
                    {id: '5', value: '5' + ' ' + characters},
                    {id: '8', value: '8' + ' ' + characters},
                    {id: '10', value: '10' + ' ' + characters},
                    {id: '12', value: '12' + ' ' + characters},
                    {id: '15', value: '15' + ' ' + characters}
                ],
                selectedOption: null,
                specialChar: [
                    {id: 'No', value: no},
                    {id: 'Nb', value: numbersOnly},
                    {id: 'Nbs', value: numbersandspecialcharactersTitle}
                ]
            };
            vm.colors = ['blue', 'red', 'green'];
            vm.system = {
                password: {
                    minLen: null,
                    specialChar: null,
                    cases: null
                },
                logoutTime: null,
                theme: null
            };
            vm.licenseKey = null;
            vm.oldLicenseKey = null;
            vm.changeLicenseKey = false;

            vm.preference = {
                id: null,
                context: null,
                preferenceKey: null,
                stringValue: null
            };
            vm.ids = {
                passwordId: null,
                logoutTimeId: null,
                themeId: null
            }

            vm.oldSettings = {
                password: {
                    minLen: null,
                    specialChar: null,
                    cases: null
                },
                logoutTime: null,
                theme: null
            };
            vm.emailSettingsPreference = {
                id: null,
                context: "SYSTEM",
                preferenceKey: "SYSTEM.EMAIL_SETTINGS",
                jsonValue: null
            };
            vm.supplierAuditEmailReminder = {
                id: null,
                context: "SYSTEM",
                preferenceKey: "SUPPLIER_AUDIT_EMAIL_REMINDER",
                booleanValue: false
            };
            vm.emailSettings = {
                userName: "",
                password: "",
                host: null,
                port: null,
                sslTrust: null
            };

            var newAddress = {
                description: "",
                address: null,
                editMode: true,
                showValues: false,
                isNew: true
            };

            var address = {
                description: "",
                address: null,
                addressActive: false
            };
            vm.addressActive = false;
            vm.defaultRevisionConfiguration = null;
            vm.defaultRevisionConfigurationPreference = {
                id: null,
                context: "SYSTEM",
                preferenceKey: "SYSTEM.DEFAULT.REVISION.CONFIGURATION",
                jsonValue: null
            };
            vm.twoFactorAuthentication = {
                id: null,
                context: "SYSTEM",
                preferenceKey: "TWO_FACTOR_AUTHENTICATION_ENABLED",
                booleanValue: false,
                integerValue: null
            };
            vm.bomRules = [
                {
                    key: 'bom.latest',
                    label: 'Latest Revision'
                },
                {
                    key: 'bom.latest.released',
                    label: 'Latest Released Revision'
                },
                {
                    key: 'bom.as.released',
                    label: 'As Released Revision'
                }
            ];

            vm.themes = ["Default", "Blue", "Red", "Pink", "Purple", "Green", "Grey", "Black", "Violet"];
            vm.logo = false;

            vm.addLogo = false;
            $scope.trustAsHtml = function (value) {
                return $sce.trustAsHtml(value);
            };
            var systemSettingsSaved = parsed.html($translate.instant("SYSTEM_SETTINGS_SAVED")).html();
            var systemSettingsUpdated = parsed.html($translate.instant("SYSTEM_SETTINGS_UPDATED")).html();
            var passwordStrengthUpdated = parsed.html($translate.instant("PASSWORD_STRENGTH_UPDATED")).html();
            var passwordStrengthSaved = parsed.html($translate.instant("PASSWORD_STRENGTH_SAVE")).html();
            var logoutSaved = parsed.html($translate.instant("LOGOUT_TIME_SAVED")).html();
            var logoutUpdated = parsed.html($translate.instant("LOGOUT_TIME_UPDATED")).html();
            var logoSaved = parsed.html($translate.instant("LOGO_SAVED")).html();
            var logoUpdated = parsed.html($translate.instant("LOGO_UPDATED")).html();
            var logoDeleted = parsed.html($translate.instant("LOGO_DELETED")).html();
            var themeSaved = parsed.html($translate.instant("THEME_SAVED")).html();
            var themeUpdated = parsed.html($translate.instant("THEME_UPDATED")).html();
            var valueValidation = parsed.html($translate.instant("VALUE_VALIDATION")).html();
            var timeValidation = parsed.html($translate.instant("TIME_VALIDATION")).html();
            var usernameMsg = parsed.html($translate.instant("USERNAME_MESSAGE")).html();
            var passwordMsg = parsed.html($translate.instant("PASSWORD_MESSAGE")).html();
            var emailValidation = parsed.html($translate.instant("P_ENTER_EMAIL")).html();
            var emailCannotEmpty = parsed.html($translate.instant("ENTER_VALID_EMAIL")).html();
            var hostMsg = parsed.html($translate.instant("HOST_MSG")).html();
            var portMsg = parsed.html($translate.instant("PORT_MSG")).html();
            var sslTrustMsg = parsed.html($translate.instant("SSL_TRUST_MSG")).html();
            var emailSettingsSaved = parsed.html($translate.instant("EMAIL_SETTINGS_SAVED_MSG")).html();
            var auditReminderUpdated = parsed.html($translate.instant("AUDIT_REMINDER_UPDATED")).html();
            vm.editImage = parsed.html($translate.instant("EDIT_IMAGE")).html();
            vm.deleteImg = parsed.html($translate.instant("DELETE_IMAGE")).html();
            vm.licenseSaveSuccessfully = parsed.html($translate.instant("LICENSE_SAVE_SUCCESSFULLY")).html();
            vm.reset = parsed.html($translate.instant("RESET")).html();
            $scope.enterEmail = parsed.html($translate.instant("ENTER_EMAIL")).html();
            $scope.enterPassword = parsed.html($translate.instant("ENTER_M_PASSWORD")).html();
            $scope.enterHost = parsed.html($translate.instant("ENTER_HOST")).html();
            $scope.enterPort = parsed.html($translate.instant("ENTER_PORT")).html();
            $scope.enterSslTrust = parsed.html($translate.instant("ENTER_SSL_TRUST")).html();
            var dateFormatSavedMsg = parsed.html($translate.instant("DATE_FORMAT_SAVED_MESSAGE")).html();

            vm.userPreferences = null;
            function loadDefaultValueSettings() {
                PreferenceService.getPreferencesByContext("DEFAULT").then(
                    function (data) {
                        vm.defaultPreferences = data;
                    }
                )
            }

            function loadPersonGroups() {
                PersonGroupService.getAllPersonGroups().then(
                    function (data) {
                        vm.personGroups = data;
                    }
                )
            }

            function loadAutoNumbers() {
                AutonumberService.getAutonumbers().then(
                    function (data) {
                        vm.autoNumbers = data;
                    }
                )
            }

            function loadSettings() {
                CommonService.getPreferenceByContext(context).then(
                    function (data) {
                        if (data.length > 0) {

                            vm.systemSettings = data;
                            angular.forEach(vm.systemSettings, function (settings) {
                                    switch (settings.preferenceKey) {
                                        case 'SYSTEM.PASSWORD' :
                                            vm.passwordUpdate = true;
                                            vm.passwordId = settings.id;
                                            var passwordProperties = JSON.parse(settings.jsonValue);
                                            if (passwordProperties != null) {
                                                if (passwordProperties.minLen != null) {
                                                    vm.oldSettings.password.minLen = passwordProperties.minLen;
                                                    var result = $scope.data.minLen.filter(function (val) {
                                                        return val.id == passwordProperties.minLen;
                                                    });
                                                    $scope.data.selectedOption = result[0];
                                                }
                                                if (passwordProperties.specialChar != null) {
                                                    vm.oldSettings.password.specialChar = passwordProperties.specialChar;
                                                    vm.system.password.specialChar = passwordProperties.specialChar;
                                                }
                                                if (passwordProperties.cases != null) {
                                                    vm.oldSettings.password.cases = passwordProperties.cases;
                                                    vm.system.password.cases = passwordProperties.cases;
                                                }
                                            }
                                            break;
                                        case  'SYSTEM.LOGOUTTIME' :
                                            vm.logoutUpdate = true;
                                            vm.logoutTimeId = settings.id;
                                            vm.oldSettings.logoutTime = settings.integerValue;
                                            vm.system.logoutTime = settings.integerValue;
                                            /*   if (vm.system.logoutTime == null || vm.system.logoutTime == 30) {
                                             $("#time").prop('checked', false);
                                             document.getElementById("timeVal").readOnly = true;
                                             } else {
                                             $("#time").prop('checked', true);
                                             document.getElementById("timeVal").readOnly = false;
                                             }*/
                                            break;
                                        case  'SYSTEM.THEME' :
                                            vm.theamUpdate = true;
                                            vm.theme = settings.id;
                                            if (settings.stringValue != null) {
                                                vm.system.theme = settings.stringValue;
                                            }

                                            break;
                                        case  'SYSTEM.DATE.FORMAT' :
                                            vm.formatUpdate = true;
                                            vm.preference.context = context;
                                            vm.preference.preferenceKey = settings.preferenceKey;
                                            vm.preference.id = settings.id;
                                            if (settings.stringValue != null) {
                                                vm.preference.stringValue = settings.stringValue;
                                            }

                                            /* if (vm.userPreferences.userTheme == null || vm.userPreferences.userTheme == "") {
                                             changeTheme(vm.system.theme);
                                             }*/
                                            else {
                                                changeTheme(defaultTheam);
                                            }

                                            break;
                                        case  'SYSTEM.LOGO' :
                                            vm.logoId = settings.id;
                                            vm.logoUpdate = true;
                                            if (settings.customLogo != null) {

                                                vm.logo = true;
                                                vm.customImagePath = "api/common/persons/" + settings.id + "/customImageAttribute/download?" + new Date().getTime();
                                                $rootScope.companyImage = vm.customImagePath;
                                            }
                                            break;
                                        case  "SYSTEM.EMAIL_SETTINGS" :
                                            if (settings.jsonValue != null && settings.jsonValue != "") {
                                                vm.emailSettingsPreference = settings;
                                                vm.emailSettings = JSON.parse(settings.jsonValue);
                                            } else {
                                                vm.emailSettingsPreference = {
                                                    id: null,
                                                    context: "SYSTEM",
                                                    preferenceKey: "SYSTEM.EMAIL_SETTINGS",
                                                    jsonValue: null
                                                };
                                                vm.emailSettings = {
                                                    userName: "",
                                                    password: "",
                                                    host: null,
                                                    port: null,
                                                    sslTrust: null
                                                }
                                            }
                                            break;
                                        case  "SUPPLIER_AUDIT_EMAIL_REMINDER" :
                                            vm.supplierAuditEmailReminder = settings;
                                            break;
                                        case  "SYSTEM.CASSINI.LICENSE" :
                                            vm.licenseKey = settings.stringValue;
                                            vm.oldLicenseKey = angular.copy(settings.stringValue);
                                            vm.changeLicenseKey = false;
                                            break;
                                        case  "TWO_FACTOR_AUTHENTICATION_ENABLED" :
                                            vm.twoFactorAuthentication = settings;
                                            break;
                                        case  "SYSTEM.DEFAULT.REVISION.CONFIGURATION" :
                                            if (settings.jsonValue != null && settings.jsonValue != "") {
                                                vm.defaultRevisionConfigurationPreference = settings;
                                                vm.defaultRevisionConfiguration = JSON.parse(settings.jsonValue);
                                            } else {
                                                vm.defaultRevisionConfigurationPreference = {
                                                    id: null,
                                                    context: "SYSTEM",
                                                    preferenceKey: "SYSTEM.DEFAULT.REVISION.CONFIGURATION",
                                                    jsonValue: null
                                                };
                                                vm.defaultRevisionConfiguration = null;
                                            }
                                            break;
                                        default :
                                            break;
                                    }
                                }
                            )
                        }
                        $timeout(function () {
                            $("#system-settings").height($(".miscellaneous-right").outerHeight() - 20);
                        }, 200);
                    }, function (error) {
                        console.log(error);
                    }
                )
            }

            $rootScope.changeTheme = changeTheme;

            //$rootScope.setDefaultTheme = setDefaultTheme;
            function loadDetails() {
                vm.loading = true;
                AppDetailsService.getAppDetails().then(
                    function (data) {
                        vm.listDetails = data;
                        angular.forEach(vm.listDetails, function (det) {
                            det.editMode = false;
                            if (det.optionKey == 13 && det.value != null) {
                                vm.ipAddress = JSON.parse(det.value);
                                loadAddress();
                            }
                            $rootScope.hideBusyIndicator();

                        });
                        vm.loading = false;
                    }
                )
            }

            function loadAddress() {
                angular.forEach(vm.ipAddress, function (address) {
                    address.newDescription = address.description;
                    address.newAddress = address.address;
                    address.editMode = false;
                    address.showValues = true;
                    address.isNew = false;
                    vm.addressActive = address.addressActive;
                });
            }

            var holidayDeleteTitle = parsed.html($translate.instant("ADDRESS_DIALOG_TITLE")).html();
            var holidayDeleteMessage = parsed.html($translate.instant("ADDRESS_DIALOG_MESSAGE")).html();

            vm.delete = false;
            vm.deleteAddress = deleteAddress;
            function deleteAddress(holiday) {
                var options = {
                    title: holidayDeleteTitle,
                    message: holidayDeleteMessage + " [ " + holiday.description + " ] " + "?",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        var index = vm.ipAddress.indexOf(holiday);
                        if (index != -1) {
                            vm.ipAddress.splice(index, 1);
                        }
                        vm.delete = true;
                        saveAddress();
                    }
                });

            }

            vm.showEditMode = showEditMode;
            function showEditMode(address) {
                address.newAddress = address.address;
                address.newDescription = address.description;
                address.editMode = true;
                address.showValues = false;
                address.isNew = false;
            }

            vm.hideEditMode = hideEditMode;
            function hideEditMode(address) {
                var index = vm.ipAddress.indexOf(address);
                vm.ipAddress.splice(index, 1);
            }

            vm.cancelEditMode = cancelEditMode;
            function cancelEditMode(address) {
                address.newAddress = address.address;
                address.newDescription = address.description;
                address.editMode = false;
                address.showValues = true;
                address.isNew = false;
            }

            vm.addAddress = addAddress;
            function addAddress() {
                var address = angular.copy(newAddress);
                vm.ipAddress.unshift(address);
            }

            var addressDeleteSuccessMessage = parsed.html($translate.instant("ADDRESS_DELETE")).html();
            var addresssUpdated = parsed.html($translate.instant("ADDRESS_LIST_UPDATED")).html();

            vm.saveAddress = saveAddress;
            function saveAddress() {
                if (validateAddress()) {
                    AppDetailsService.saveIpAddress(JSON.stringify(vm.addressList)).then(
                        function (data) {
                            loadDetails();
                            if (vm.delete == true) $rootScope.showSuccessMessage(addressDeleteSuccessMessage);
                            if (vm.delete != true) $rootScope.showSuccessMessage(addresssUpdated);
                            vm.delete = false;
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            var nameValidation = parsed.html($translate.instant("DESCRIPTION_VALIDATION")).html();
            var addressValidation = parsed.html($translate.instant("ADDRESS_VALIDATION")).html();
            var dateValidation = parsed.html($translate.instant("VALID_IP_ADDRESS")).html();
            var nameExists = parsed.html($translate.instant("DUPLICATE_MESSAGE")).html();

            var dateExists = parsed.html($translate.instant("DATE_EXIST")).html();

            function checkIPAddress(address) {
                return /^(([1-9]?\d|1\d\d|2[0-5][0-5]|2[0-4]\d)\.){3}([1-9]?\d|1\d\d|2[0-5][0-5]|2[0-4]\d)$/.test(address);
            }

            function validateAddress() {
                vm.addressList = [];
                var addressVal = {};
                var valid = true;
                var addresses = angular.copy(vm.ipAddress);
                angular.forEach(addresses, function (val, index) {
                    if (valid) {
                        addressVal = angular.copy(address);
                        if (val.newDescription != null && val.newDescription != "" && val.newDescription != undefined && valid) {
                            addressVal.description = val.newDescription;
                        } else {
                            valid = false;
                            $rootScope.showWarningMessage(nameValidation);
                        }
                        if (val.newAddress != null && val.newAddress != "" && val.newAddress != undefined && valid) {
                            if (checkIPAddress(val.newAddress)) {
                                addressVal.address = val.newAddress;
                            } else {
                                valid = false;
                                $rootScope.showWarningMessage(dateValidation);
                            }
                        } else if (valid) {
                            valid = false;
                            $rootScope.showWarningMessage(addressValidation);
                        }
                        if (val.isNew && valid) {
                            angular.forEach(addresses, function (val1, index1) {
                                if (index != index1) {
                                    if (val.newDescription == val1.newDescription) {
                                        valid = false;
                                        $rootScope.showWarningMessage(nameExists);
                                    }
                                    if (val.newAddress == val1.newAddress) {
                                        valid = false;
                                        $rootScope.showWarningMessage(dateExists);
                                    }
                                }
                            });
                        }
                        addressVal.addressActive = vm.addressActive;
                        vm.addressList.push(addressVal);
                    }
                });
                return valid;
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


            $(document).on('change', '[id="time"]', function () {
                var checkbox = $(this); // Selected or current checkbox
                if (checkbox.is(':checked')) {
                    document.getElementById("timeVal").readOnly = false;
                } else {
                    vm.system.logoutTime = 30;
                    document.getElementById("timeVal").readOnly = true;
                }
            });
            vm.changeSystemLicenseKey = changeSystemLicenseKey;
            function changeSystemLicenseKey() {
                vm.oldLicenseKey = vm.licenseKey;
                vm.licenseKey = "";
                vm.changeLicenseKey = true;

            };
            vm.licenseDto = {licenseKey: null};
            vm.saveLicenseKey = saveLicenseKey;
            function saveLicenseKey() {
                if (vm.licenseKey != null && vm.licenseKey != "") {
                    vm.licenseDto.licenseKey = vm.licenseKey;
                    checkActiveUserLicenses(vm.licenseDto);
                }
                else {
                    $rootScope.showErrorMessage("Please enter license Key");
                }
            }


            function checkActiveUserLicenses(licenseDto) {
                LicenseService.checkActiveUserLicenses(licenseDto).then(
                    function (data) {
                        if (!data.valid) {
                            LicenseService.saveLicense(vm.licenseKey).then(
                                function () {
                                    vm.changeLicenseKey = false;
                                    $rootScope.showSuccessMessage(vm.licenseSaveSuccessfully);
                                },
                                function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        } else {
                            $rootScope.showWarningMessage("Active users are more than you uploaded licenses, Please go admin module and keep active " + data.licenses + " users");
                        }
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.cancelLicenseKey = cancelLicenseKey;
            function cancelLicenseKey() {
                vm.licenseKey = angular.copy(vm.oldLicenseKey);
                vm.changeLicenseKey = false;
            }

            vm.changeMinLen = changeMinLen;
            function changeMinLen() {
                vm.system.password.minLen = $scope.data.selectedOption.id;
            }

            vm.updateUserPreference = updateUserPreference;
            function updateUserPreference() {
                LoginService.updateUserPreference(vm.userPreferences).then(
                    function (data) {
                        changeTheme(vm.userPreferences.userTheme);
                        loadUserPreference();
                        $rootScope.showSuccessMessage(themeUpdated);
                    }
                );
            }

            vm.updateDateFormat = updateDateFormat;

            function updateDateFormat() {
                LoginService.updateUserPreference(vm.userPreferences).then(
                    function (data) {
                        $rootScope.applicationDateFormat = vm.userPreferences.userDateFormat;
                        $rootScope.showSuccessMessage("Date format updated successfully");
                        loadUserPreference();
                    }
                );
            }

            vm.update11 = false;
            function onSubmit() {

            }

            function loadPasswordStrength() {
                var context = 'SYSTEM';
                CommonService.getPreferenceByContext(context).then(
                    function (data) {
                        angular.forEach(data, function (prop) {
                            if (prop.preferenceKey == 'SYSTEM.PASSWORD') {
                                $rootScope.passwordProperties = JSON.parse(prop.jsonValue);
                            }
                        });
                    }, function (error) {
                        console.log(error);
                    }
                )
            }

            vm.savePasswordStrength = savePasswordStrength;
            function savePasswordStrength() {
                if (vm.system.password != null && vm.system.password != "" && vm.system.password != undefined) {
                    if (vm.system.password.minLen == null) {
                        vm.system.password.minLen = vm.oldSettings.password.minLen;
                    }
                    if (vm.system.password.specialChar == null) {
                        vm.system.password.specialChar = vm.oldSettings.password.specialChar;
                    }
                    if (vm.system.password.cases == null) {
                        vm.system.password.cases = vm.oldSettings.password.cases;
                    }
                    if (vm.passwordUpdate == false) {
                        if (vm.system.password != null && vm.system.password != "" && vm.system.password != undefined) {
                            CommonService.savePassword(context, JSON.stringify(vm.system.password)).then(
                                function (data) {
                                    loadSettings();
                                    loadPasswordStrength();
                                    $rootScope.showSuccessMessage(passwordStrengthSaved);
                                }
                            )
                        }
                    } else {
                        if (vm.system.password != null && vm.system.password != "" && vm.system.password != undefined) {
                            CommonService.updatePassword(vm.passwordId, JSON.stringify(vm.system.password)).then(
                                function (data) {
                                    loadSettings();
                                    loadPasswordStrength();
                                    $rootScope.showSuccessMessage(passwordStrengthUpdated);
                                }
                            )
                        }
                    }
                }

            }

            vm.submitLogOutTime = submitLogOutTime;
            function submitLogOutTime() {
                if (vm.logoutUpdate == false) {
                    if (validate(vm.system.logoutTime)) {
                        if (vm.system.logoutTime != null && vm.system.logoutTime != "" && vm.system.logoutTime != undefined) {
                            CommonService.saveLogoutTime(context, vm.system.logoutTime).then(
                                function (data) {
                                    vm.system.logoutTime = data.integerValue;
                                    vm.changeTime = false;
                                    $rootScope.sessionTime = vm.system.logoutTime;
                                    loadSettings();
                                    $rootScope.showSuccessMessage(logoutSaved);
                                }
                            )
                        } else {
                            $rootScope.showWarningMessage(valueValidation);
                        }
                    }
                } else {
                    if (validate(vm.system.logoutTime)) {
                        if (vm.system.logoutTime != null && vm.system.logoutTime != "" && vm.system.logoutTime != undefined) {
                            CommonService.updateLogoutTime(vm.logoutTimeId, vm.system.logoutTime).then(
                                function (data) {
                                    vm.system.logoutTime = data.integerValue;
                                    vm.changeTime = false;
                                    $rootScope.sessionTime = vm.system.logoutTime;
                                    loadSettings();
                                    $rootScope.showSuccessMessage(logoutUpdated);
                                });
                        } else {
                            $rootScope.showWarningMessage(valueValidation);
                        }
                    }
                }
            }

            function validate(number) {
                var valid = true;
                if (number <= 0 || !Number.isInteger(number)) {
                    $rootScope.showWarningMessage(timeValidation);
                    valid = false;
                }
                return valid;
            }

            vm.submitLogo = submitLogo;
            function submitLogo() {
                if (vm.logoUpdate == false) {
                    if (vm.system.customLogo != null && vm.system.customLogo != "" && vm.system.customLogo != undefined) {
                        if (validateCustomImage()) {
                            CommonService.saveCustomLogo(vm.system.customLogo).then(
                                function (data) {
                                    vm.logo = false;
                                    vm.addLogo = false;
                                    loadSettings();
                                    $rootScope.showSuccessMessage(logoSaved);
                                }
                            )
                        }
                    } else {
                        $rootScope.showWarningMessage(uploadValidation);
                    }
                } else {
                    if (vm.system.customLogo != null && vm.system.customLogo != "" && vm.system.customLogo != undefined) {
                        if (validateCustomImage()) {
                            CommonService.updateCustomLogo(vm.logoId, vm.system.customLogo).then(
                                function (data) {
                                    $rootScope.showSuccessMessage(logoUpdated);
                                    vm.logo = false;
                                    vm.addLogo = false;
                                    loadSettings();
                                }
                            )
                        }
                    } else {
                        $rootScope.showWarningMessage(uploadValidation);
                    }
                }
            }

            vm.submitTheme = submitTheme;
            function submitTheme() {
                if (vm.theamUpdate == false) {
                    if (vm.system.theme != null && vm.system.theme != "" && vm.system.theme != undefined) {
                        CommonService.saveTheme(context, vm.system.theme).then(
                            function (data) {
                                loadSettings();
                                $rootScope.showSuccessMessage("Date format updated successfully");
                            }
                        )
                    }
                } else {
                    if (vm.system.theme != null && vm.system.theme != "" && vm.system.theme != undefined) {
                        CommonService.updateTheme(vm.theme, vm.system.theme).then(
                            function (data) {
                                loadSettings();
                                $rootScope.showSuccessMessage(themeUpdated);

                            }
                        )

                    }
                }
            }

            vm.submitDateFormat = submitDateFormat;
            function submitDateFormat() {
                CommonService.updateSystemDateFormat(vm.preference).then(
                    function (data) {
                        performLogout();
                        $rootScope.showSuccessMessage(dateFormatSavedMsg);

                    }
                )
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


            var imageValidation = parsed.html($translate.instant("IMAGE_VALIDATION")).html();
            var imageSizeValidation = parsed.html($translate.instant("COMPANY_LOGO_VALIDATION")).html();
            var uploadValidation = parsed.html($translate.instant("UPLOAD_VALIDATION")).html();

            function validateCustomImage() {
                var valid = true;
                var fup = document.getElementById('image');
                var fileName = fup.value;
                var ext = fileName.substring(fileName.lastIndexOf('.') + 1);
                if (ext != "JPEG" && ext != "jpeg" && ext != "jpg" && ext != "JPG" && ext != "PNG" && ext != "png" && ext != "GIF" && ext != "gif") {
                    $rootScope.showWarningMessage(imageValidation);
                    valid = false;
                }
                else if (fup.files[0].size > 2097152) {
                    $rootScope.showWarningMessage(imageSizeValidation);
                    valid = false;
                }

                return valid;
            }

            vm.customImage = null;
            vm.customImagePath = null;
            vm.showPreview = false;


            function addLogoImage() {
                if (vm.addLogo == true) {
                    document.getElementById("image").onchange = function () {
                        vm.showPreview = true;
                        var file = document.getElementById("image");
                        var reader = new FileReader();
                        reader.onload = function (e) {
                            $('#customImage')
                                .attr('src', e.target.result)
                                .width(58)
                                .height(50);
                        };
                        $('#customImage').show();
                        vm.customImage = file.files[0];
                        reader.readAsDataURL(file.files[0]);
                    };
                }

            }

            vm.changeTime1 = false;
            vm.changeLogOutTime = changeLogOutTime;
            function changeLogOutTime() {
                vm.changeTime1 = !vm.changeTime1;
            }

            function onCancel() {
                loadSettings();
            }

            vm.addImage = addImage;
            function addImage() {
                vm.addLogo = true;
                addLogoImage();
            }

            vm.changeTime = false;
            vm.changeSystemTime = changeSystemTime;
            function changeSystemTime() {
                vm.changeTime = true;
                vm.system.newLogoutTime = vm.system.logoutTime;
            }

            vm.cancelLogo = cancelLogo;
            function cancelLogo() {
                vm.addLogo = false;
                if (vm.customImagePath == null) $('#customImage').hide();
                loadSettings();
            }

            vm.cancelTime = cancelTime;
            function cancelTime() {
                vm.changeTime = false;
                vm.system.logoutTime = vm.system.newLogoutTime;
            }

            vm.deleteImage = deleteImage;
            function deleteImage() {
                vm.system.customLogo = null;
                CommonService.deleteCustomLogo(vm.logoId, vm.system.customLogo).then(
                    function (data) {
                        $rootScope.showSuccessMessage(logoDeleted);
                        $rootScope.companyImage = null;
                        vm.customImagePath = null;
                        vm.logo = false;
                        vm.addLogo = false;
                        loadSettings();
                    }
                )
            }

            var session = JSON.parse(localStorage.getItem('local_storage_login'));
            $rootScope.loginPersonDetails = session.login;

            function loadUserPreference() {
                LoginService.getUserPreference($rootScope.loginPersonDetails.id).then(
                    function (data) {
                        vm.userPreferences = data;
                    }
                );
            }

            vm.editMailSettings = editMailSettings;
            function editMailSettings() {
                vm.oldMailSettings = angular.copy(vm.emailSettings);
                vm.editMailValue = true;
            }

            vm.editMailValue = false;
            vm.saveEmailSettings = saveEmailSettings;
            function saveEmailSettings() {
                if (validateEmailSettings()) {
                    $rootScope.showBusyIndicator();
                    vm.emailSettingsPreference.jsonValue = JSON.stringify(vm.emailSettings);
                    PreferenceService.createPreference(vm.emailSettingsPreference).then(
                        function (data) {
                            vm.emailSettingsPreference = data;
                            vm.emailSettings = JSON.parse(vm.emailSettingsPreference.jsonValue);
                            $rootScope.showSuccessMessage(emailSettingsSaved);
                            $rootScope.hideBusyIndicator();
                            vm.editMailValue = false;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            vm.saveAuditEmailReminder = saveAuditEmailReminder;
            function saveAuditEmailReminder() {
                $rootScope.showBusyIndicator();
                PreferenceService.createPreference(vm.supplierAuditEmailReminder).then(
                    function (data) {
                        vm.supplierAuditEmailReminder = data;
                        $rootScope.showSuccessMessage(auditReminderUpdated);
                        $rootScope.hideBusyIndicator();
                        vm.editMailValue = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function validateEmailSettings() {
                var valid = true;
                if (vm.emailSettings.userName == null || vm.emailSettings.userName == "" || vm.emailSettings.userName == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(emailValidation);
                } else if (!validateEmail()) {
                    valid = false;
                    $rootScope.showWarningMessage(emailCannotEmpty);
                } else if (vm.emailSettings.password == null || vm.emailSettings.password == "" || vm.emailSettings.password == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(passwordMsg);
                } else if (vm.emailSettings.host == null || vm.emailSettings.host == "" || vm.emailSettings.host == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(hostMsg);
                } else if (vm.emailSettings.port == null || vm.emailSettings.port == "" || vm.emailSettings.port == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(portMsg);
                } else if (vm.emailSettings.sslTrust == null || vm.emailSettings.sslTrust == "" || vm.emailSettings.sslTrust == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(sslTrustMsg);
                }
                return valid;
            }

            function validateEmail() {
                var valid = true;
                var atpos = vm.emailSettings.userName.indexOf("@");
                var dotpos = vm.emailSettings.userName.lastIndexOf(".");
                if (vm.emailSettings.userName != null && vm.emailSettings.userName != undefined && vm.emailSettings.userName != "") {
                    if (atpos < 1 || ( dotpos - atpos < 2 )) {
                        valid = false;
                    }
                }
                return valid
            }

            vm.saveRevisionConfiguration = saveRevisionConfiguration;
            function saveRevisionConfiguration() {
                $rootScope.showBusyIndicator();
                vm.defaultRevisionConfigurationPreference.jsonValue = JSON.stringify(vm.defaultRevisionConfiguration);
                PreferenceService.createPreference(vm.defaultRevisionConfigurationPreference).then(
                    function (data) {
                        if (data != "" && data.jsonValue != null && data.jsonValue != "") {
                            window.$application.defaultRevisionConfiguration = JSON.parse(data.jsonValue);
                        }
                        loadSettings();
                        $rootScope.showSuccessMessage(defaultValueUpdated);
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.saveDefaultValue = saveDefaultValue;
            function saveDefaultValue(preference) {
                $rootScope.showBusyIndicator();
                preference.jsonValue = JSON.stringify(preference.defaultValue);
                PreferenceService.createPreference(preference).then(
                    function (data) {
                        loadDefaultValueSettings();
                        $rootScope.showSuccessMessage(defaultValueUpdated);
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }


            function resizeScreen() {
                $("#system-settings").height($(".miscellaneous-right").outerHeight() - 80);
            }

            function loadLovs() {
                LovService.getAllLovs().then(
                    function (data) {
                        vm.listOfValues = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            function loadLifecycles() {
                LifecycleService.getLifecycles().then(
                    function (data) {
                        vm.lifecycles = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }


            vm.saveTwoFactorAuthentication = saveTwoFactorAuthentication;
            function saveTwoFactorAuthentication() {
                if (validateTwoFactor()) {
                    $rootScope.showBusyIndicator();
                    PreferenceService.createPreference(vm.twoFactorAuthentication).then(
                        function (data) {
                            vm.twoFactorAuthentication = data;
                            $rootScope.showSuccessMessage(twoFactorPreferenceUpdated);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function validateTwoFactor() {
                var valid = true;
                if (vm.twoFactorAuthentication.booleanValue && (vm.twoFactorAuthentication.integerValue == null || vm.twoFactorAuthentication.integerValue == "")) {
                    valid = false;
                    $rootScope.showWarningMessage(selectPasscodeExpiration);
                }
                return valid;
            }


            (function () {
                $timeout(function () {
                    loadUserPreference();
                    loadSettings();
                    loadDetails();
                    loadDefaultValueSettings();
                    loadPersonGroups();
                    loadAutoNumbers();
                    loadLovs();
                    loadLifecycles();
                }, 500);
                $(window).resize(function () {
                    resizeScreen();
                })
            })();
        }
    }
)
;