define(
    [
        'app/assets/bower_components/cassini-platform/app/desktop/modules/login/login.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/app/application',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/js/utils/uiUtils',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commentsService',
    ],
    function (module) {
        module.controller('NewPasswordController', NewPasswordController);

        function NewPasswordController($scope, $rootScope, $state, $stateParams, $timeout, $sce, $cookies, $translate,
                                       $application, LoginService, CommonService) {
            var vm = this;

            vm.error = null;
            vm.processing = false;
            vm.loginName = null;
            vm.newPwd = null;
            vm.verifyPwd = null;
            vm.cancel = cancel;

            vm.setNewPassword = setNewPassword;
            vm.showUserPassword = showUserPassword;
            var parsed = angular.element("<div></div>");
            var passwordLength = parsed.html($translate.instant("PASSWORD_LENGTH")).html();
            var passwordLowerCase = parsed.html($translate.instant("PASSWORD_LOWERCASE")).html();
            var passwordUpperCase = parsed.html($translate.instant("PASSWORD_UPPERCASE")).html();
            var passwordSpecialChar = parsed.html($translate.instant("PASSWORD_SPECIAL")).html();
            var passwordNumber = parsed.html($translate.instant("PASSWORD_NUMBER")).html();
            var newPasswordValid = parsed.html($translate.instant("NEW_PASSWORD_VALIDATION")).html();
            var verifyPasswordValid = parsed.html($translate.instant("VERIFY_PASSWORD_VALIDATION")).html();
            var newAndVerifyValid = parsed.html($translate.instant("NEW_VERIFY_PASSWORD")).html();
            vm.passwordInformation = parsed.html($translate.instant("PASSWORD_INFORMATION")).html();
            vm.invalidPassword = parsed.html($translate.instant("INVALID_PASSWORD")).html();
            vm.invalidUsername = parsed.html($translate.instant("INVALID_USERNAME")).html();
            vm.verifyPass = parsed.html($translate.instant("VERIFY_PASSWORD")).html();
            var showPassword = parsed.html($translate.instant("SHOW_PASSWORD")).html();
            var hidePassword = parsed.html($translate.instant("HIDE_PASSWORD")).html();

            var passwordStrength = /^[\s\S]{8,32}$/,
                upper = /[A-Z]/,
                lower = /[a-z]/,
                number = /[0-9]/,
                special = /[ !"#$%&'()*+,\-./:;<=>?@[\\\]^_`{|}~]/;

            vm.loginDTO = {
                loginName: null,
                newPassword: null,
                otp: null
            };
            function setNewPassword() {
                vm.error = null;
                vm.processing = true;

                if (validate()) {
                    vm.loginDTO.loginName = $scope.$parent.$parent.loginName;
                    vm.loginDTO.newPassword = vm.newPwd;
                    vm.loginDTO.otp = $scope.$parent.$parent.verifiedOtp;
                    LoginService.setNewPassword(vm.loginDTO).then(
                        function (data) {
                            vm.processing = false;
                            $scope.$parent.showLoginView();
                        },
                        function (error) {
                            vm.processing = false;
                            vm.error = error.message;
                        }
                    );
                }
                else {
                    vm.processing = false;
                }
            }

            vm.title = showPassword;
            function showUserPassword() {
                var eyeIcon = document.getElementById("showPassword");
                var newPassword = document.getElementById("newPwd");
                if (eyeIcon.attributes.class.nodeValue == "fa fa-fw fa-eye-slash") {
                    eyeIcon.attributes.class.nodeValue = "fa fa-fw fa-eye";
                    vm.title = hidePassword;
                } else {
                    eyeIcon.attributes.class.nodeValue = "fa fa-fw fa-eye-slash";
                    vm.title = showPassword;
                }

                if (newPassword.attributes.type.nodeValue == "password") {
                    newPassword.attributes.type.nodeValue = "text";
                } else {
                    newPassword.attributes.type.nodeValue = "password";
                }
            }

            function cancel() {
                $rootScope.view = 'login';
                vm.newPwd = null;
                vm.verifyPwd = null;
            }

            $rootScope.passwordStrengthValid = passwordStrengthValid;
            function passwordStrengthValid() {
                vm.validPassword = false;
                var password = document.getElementsByName('password')[0].value;
                var meter = document.getElementById('password-strength-meter');
                var score = null;
                if (vm.properties != null) {
                    loadPasswordMessage();
                    var minLength = parseInt(vm.properties.minLen);
                    var specialChar = vm.properties.specialChar;
                    var cases = vm.properties.cases;
                    score = evalPasswordScore(minLength, specialChar, cases, password);
                } else {
                    score = 4;
                    vm.passwordInformation = vm.password;
                }
                if (score == 4) {
                    vm.validPassword = true;
                }
                meter.value = score;
            }

            vm.passwordScore = null;
            function evalPasswordScore(minLength, specialChar, cases, password) {
                var len = password.length;
                var score = 0;
                if (minLength == null && (specialChar == null || specialChar == 'No') && (cases == null || cases == 'No')) {
                    score = 4;
                } else if (minLength != null && (specialChar == 'No' || specialChar == null) && (cases == 'No' || cases == null)) {
                    if (len >= minLength) score = 4;
                } else if (minLength != null && specialChar == 'Nb' && (cases == 'No' || cases == null)) {
                    if (len >= minLength) {
                        score = 2;
                    }
                    if (number.test(password)) {
                        score += 2;
                    }
                } else if (minLength != null && specialChar == 'Nbs' && (cases == 'No' || cases == null)) {
                    if (len >= minLength) {
                        score = 2;
                    }
                    if (number.test(password) && special.test(password)) {
                        score += 2;
                    }
                } else if (minLength != null && (specialChar == 'No' || specialChar == null) && (cases == 'Yes' || cases == null)) {
                    if (len >= minLength) {
                        score = 2;
                    }
                    if (upper.test(password) && lower.test(password)) {
                        score += 2;
                    }
                } else if (minLength != null && specialChar == 'Nb' && (cases == 'Yes' || cases == null)) {
                    if (len >= minLength) {
                        score = 2;
                    }
                    if (number.test(password)) {
                        score += 1;
                    }
                    if (upper.test(password) && lower.test(password)) {
                        score += 1;
                    }
                } else if (minLength != null && specialChar == 'Nbs' && (cases == 'Yes' || cases == null)) {
                    if (len >= minLength) {
                        score = 2;
                    }
                    if (number.test(password) && special.test(password)) {
                        score += 1;
                    }
                    if (upper.test(password) && lower.test(password)) {
                        score += 1;
                    }
                } else if (minLength == null && specialChar == 'Nb' && (cases == 'No' || cases == null)) {
                    if (number.test(password)) {
                        score = 4;
                    }
                } else if (minLength == null && specialChar == 'Nbs' && (cases == 'No' || cases == null)) {
                    if (number.test(password) && special.test(password)) {
                        score = 4;
                    }
                } else if (minLength == null && (specialChar == 'No' || specialChar == null) && (cases == 'Yes' || cases == null)) {
                    if (upper.test(password) && lower.test(password)) {
                        score = 4;
                    }
                } else if (minLength == null && specialChar == 'Nb' && (cases == 'Yes' || cases == null)) {
                    if (number.test(password)) {
                        score = 2;
                    }
                    if (upper.test(password) && lower.test(password)) {
                        score += 2;
                    }
                } else if (minLength == null && specialChar == 'Nbs' && (cases == 'Yes' || cases == null)) {
                    if (number.test(password) && special.test(password)) {
                        score = 2;
                    }
                    if (upper.test(password) && lower.test(password)) {
                        score += 2;
                    }
                }
                vm.passwordScore = score;
                return score;
            }

            //Check Password has Special Characters
            vm.passwordSafeCharacters == false;
            vm.safeCharsPasswordCheck = safeCharsPasswordCheck;
            function safeCharsPasswordCheck() {
                var userPassword = document.getElementById("newPwd").value;
                var regex = /^[a-zA-Z0-9!*:,'_~( )@\*\)\(_\-\s\u00E4\u00F6\u00FC\u00C4\u00D6\u00DC\u00df]+$/g;
                // var match = getValue().match(regex);
                if (userPassword.match(regex) == null || userPassword.includes(" ")) {
                    return false;
                }
                else {
                    vm.passwordSafeCharacters = false;
                    return true;
                }

            }

            function validate() {
                var valid = true;

                if (vm.newPwd == null || vm.newPwd.trim() == "") {
                    vm.error = newPasswordValid;
                    valid = false;
                }
                else if (vm.verifyPwd == null || vm.verifyPwd.trim() == "") {
                    vm.error = verifyPasswordValid;
                    valid = false;
                }
                else if (vm.newPwd != vm.verifyPwd) {
                    vm.error = newAndVerifyValid;
                    valid = false;
                }

                if (vm.validPassword == false) {
                    vm.error = vm.newPasswordInfo;
                    valid = false;
                }

                /* if (vm.newPwd != null) {
                 if (vm.newPwd.length < 8) {
                 vm.error = passwordLength;
                 valid = false;
                 } else if (!upper.test(vm.newPwd)) {
                 vm.error = passwordUpperCase;
                 valid = false;
                 } else if (!lower.test(vm.newPwd)) {
                 vm.error = passwordLowerCase;
                 valid = false;
                 } else if (!number.test(vm.newPwd)) {
                 vm.error = passwordNumber;
                 valid = false;
                 } else if (!special.test(vm.newPwd)) {
                 vm.error = passwordSpecialChar;
                 valid = false;
                 }
                 }*/

                return valid;
            }

            vm.properties = null;
            function loadPasswordStrength() {
                vm.properties = null;
                var context = 'SYSTEM';
                CommonService.getPreferenceByContext(context).then(
                    function (data) {
                        angular.forEach(data, function (prop) {
                            if (prop.preferenceKey == 'SYSTEM.PASSWORD') {
                                vm.properties = JSON.parse(prop.jsonValue);
                                loadPasswordMessage();
                            }
                        });
                    }, function (error) {
                        console.log(error);
                    }
                )
            }

            function loadPasswordMessage() {
                vm.passwordMinLength = parsed.html($translate.instant("PASSWORD_MINIMUM_LENGTH")).html();
                vm.passwordNumbersOnly = parsed.html($translate.instant("PASSWORD_NUMBERS_ONLY")).html();
                vm.passwordNumbersAndSpecialChar = parsed.html($translate.instant("PASSWORD_NUMBERS_AND_SPECIAL_CHARACTERS")).html();
                vm.passwordCaseSensitivie = parsed.html($translate.instant("PASSWORD_CASE_SENSITIVIE")).html();
                vm.password = parsed.html($translate.instant("PASSWORD")).html();
                var characters = parsed.html($translate.instant("CHARACTERS")).html();
                vm.newPasswordInfo = '';
                vm.passwordInformation = "";
                if (vm.properties.minLen != null) {
                    vm.passwordInformation += "\u2022 " + vm.passwordMinLength + vm.properties.minLen + " " + characters;
                }
                if (vm.properties.specialChar != null) {
                    if (vm.properties.specialChar == 'Nb') {
                        vm.passwordInformation += "<br>";
                        vm.passwordInformation += "\u2022 " + vm.passwordNumbersOnly;
                    }
                    if (vm.properties.specialChar == 'Nbs') {
                        vm.passwordInformation += "<br>";
                        vm.passwordInformation += "\u2022 " + vm.passwordNumbersAndSpecialChar;
                    }
                }
                if (vm.properties.cases != null) {
                    if (vm.properties.cases == 'Yes') {
                        vm.passwordInformation += "<br>";
                        vm.passwordInformation += "\u2022 " + vm.passwordCaseSensitivie;
                    }
                }
                vm.newPasswordInfo = $sce.trustAsHtml(vm.passwordInformation)
            }


            (function () {
                loadPasswordStrength();
            })();

        }
    }
);