define(
    [
        'app/desktop/modules/item/item.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/personGrpService',
        'app/desktop/directives/person-avatar/personAvatarDirective'
    ],
    function (module) {
        module.controller('UserDetailsController', UserDetailsController);

        function UserDetailsController($scope, $rootScope, $injector, $sce, $translate, $cookieStore, $window, $timeout, $application,
                                       $state, $stateParams, $cookies, LoginService, PersonGroupService, CommonService) {

            $rootScope.viewInfo.showDetails = false;

            var vm = this;

            var parsed = angular.element("<div></div>");
            var userId = $stateParams.userId;

            vm.details = 'activity';
            vm.loginDetails = {
                isActive: true,
                isSuperUser: false,
                external: false
            };

            vm.showUserActivity = showUserActivity;
            function showUserActivity() {
                vm.details = 'activity';
                $state.go('app.userdetails.activity', {userId: userId})
            }

            vm.showAccountInfo = showAccountInfo;
            function showAccountInfo() {
                vm.details = 'account';
                $state.go('app.userdetails.account', {userId: userId})
            }

            vm.changePassword = changePassword;
            function changePassword() {
                vm.details = 'password';
                $state.go('app.userdetails.password', {userId: userId})
            }

            vm.showUserRoles = showUserRoles;
            function showUserRoles() {
                vm.details = 'roles';
                $state.go('app.userdetails.roles', {userId: userId})
            }

            vm.showUserSettings = showUserSettings;
            function showUserSettings() {
                vm.details = 'settings';
                $state.go('app.userdetails.settings', {userId: userId})
            }

            vm.gotoAdmin = gotoAdmin;
            function gotoAdmin() {
                $state.go('app.newadmin.users');
            }

            vm.showPhotoPreview = showPhotoPreview;
            function showPhotoPreview() {
                var modal = document.getElementById('item-thumbnail-basic' + vm.loginDetails.id);
                modal.style.display = "block";

                var span = document.getElementById("thumbnail-close-basic" + vm.loginDetails.id);
                $("#thumbnail-image-basic" + vm.loginDetails.id).width($('#thumbnail-view-basic' + vm.loginDetails.id).outerWidth());
                $("#thumbnail-image-basic" + vm.loginDetails.id).height($('#thumbnail-view-basic' + vm.loginDetails.id).outerHeight());
                $(".split-pane-divider").css('z-index', 0);
                span.onclick = function () {
                    modal.style.display = "none";
                }
                $scope.$evalAsync();
            }

            $rootScope.loginDetails = vm.loginDetails;
            function loadLogin() {
                LoginService.getLoginByPerson(userId).then(
                    function (data) {
                        vm.loginDetails = data;
                        $rootScope.loginDetails = data;
                        if (vm.loginDetails.id == $application.login.id) {
                            $application.loadProfileImage = false;
                            $timeout(function () {
                                $application.loadProfileImage = true;
                            }, 100);
                        }
                        var firstLetter = "";
                        var lastLetter = "";
                        if (vm.loginDetails.person.firstName != null && vm.loginDetails.person.firstName != "") {
                            firstLetter = vm.loginDetails.person.firstName.substring(0, 1).toUpperCase();
                        }
                        if (vm.loginDetails.person.lastName != null && vm.loginDetails.person.lastName != "") {
                            lastLetter = vm.loginDetails.person.lastName.substring(0, 1).toUpperCase();
                        }
                        vm.loginDetails.imageWord = firstLetter + "" + lastLetter;
                        PersonGroupService.getLoginPersonGroupReferences([vm.loginDetails], 'defaultGroup');
                        if (vm.loginDetails.person.hasImage) {
                            vm.personImage = "api/common/persons/" + vm.loginDetails.person.id + "/image/download?" + new Date().getTime();
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.image = null;
            $scope.saveUserImage = saveUserImage;
            function saveUserImage(image) {
                if (image == null || image == "") {
                    $rootScope.showWarningMessage("Select image");

                } else {
                    vm.image = image;
                    if (validateImage()) {
                        $rootScope.showBusyIndicator();
                        CommonService.uploadPersonImage(userId, image).then(
                            function (data) {
                                document.getElementById('userImage').value = "";
                                $rootScope.showSuccessMessage("Image uploaded successfully");
                                loadLogin();
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        );
                    }
                }
            }

            var imageValidation = parsed.html($translate.instant("IMAGE_VALIDATION")).html();

            function validateImage() {
                var valid = true;
                if (vm.image != null) {
                    var fup = document.getElementById('userImage');
                    var fileName = fup.value;
                    var ext = fileName.substring(fileName.lastIndexOf('.') + 1);
                    ext = ext.toLowerCase();
                    if (['jpeg', 'jpg', 'png', 'gif'].indexOf(ext) !== -1) {
                        return true;
                    } else {
                        $rootScope.showWarningMessage(imageValidation);
                        fup.focus();
                        valid = false;
                    }
                }

                return valid;
            }

            vm.removeProfilePicture = removeProfilePicture;
            function removeProfilePicture() {
                $rootScope.showBusyIndicator();
                CommonService.deletePersonImage(userId).then(
                    function (data) {
                        if (vm.loginDetails.id == $application.login.id) {
                            $application.loadProfileImage = false;
                            $timeout(function () {
                                $application.loadProfileImage = true;
                            }, 100);
                        }
                        vm.loginDetails.person.hasImage = false;
                        $rootScope.showSuccessMessage("Profile image removed successfully");
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            (function () {
                loadLogin();
                var url = $state.current.url;

                if (url === '/activity') {
                    vm.details = 'activity';
                    showUserActivity();
                }
                else if (url === '/account') {
                    vm.details = 'account';
                    showAccountInfo();
                }
                else if (url === '/password') {
                    vm.details = 'password';
                    changePassword();
                }
                else if (url === '/roles') {
                    vm.details = 'roles';
                    showUserRoles();
                }
                else if (url === '/settings') {
                    vm.details = 'settings';
                    showUserSettings();
                }

                $('#contentpanel').css({'overflow-y': 'auto'});

                $scope.$on("$destroy", function () {
                    $('#contentpanel').css({'overflow-y': 'hidden'});
                });

            })();
        }
    }
);