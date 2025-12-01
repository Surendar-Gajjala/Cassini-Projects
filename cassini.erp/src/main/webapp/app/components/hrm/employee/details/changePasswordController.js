define(['app/app.modules',
        'app/components/login/loginFactory'
    ],
    function ($app) {
        $app.controller('ChangePasswordController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$cookies',
                'loginFactory',

                function ($scope, $rootScope, $timeout, $interval, $state, $cookies,
                          loginFactory) {

                    $scope.password = {
                        oldPassword: "",
                        newPassword: "",
                        confirmPassword: ""
                    };
                    $scope.error = {
                        hasError: false,
                        errorMessage: ""
                    };

                    function validatePassword() {
                        var valid = true;
                        if($scope.password.oldPassword == $scope.password.newPassword) {
                            $scope.error.errorMessage = "Old and new passwords cannot be the same.";
                            $scope.error.hasError = true;
                            valid = false;
                        }
                        else if($scope.password.newPassword != $scope.password.confirmPassword) {
                            $scope.error.errorMessage = "New password and confirm password are not the same";
                            $scope.error.hasError = true;
                            valid = false;
                        }

                        return valid;
                    }

                    $scope.submitChangePassword = function() {
                        $scope.error.hasError = false;

                        if(validatePassword()) {
                            loginFactory.changePassword($scope.password.oldPassword, $scope.password.newPassword).then(
                                function(data) {
                                    return loginFactory.logout().then(
                                        function(data) {
                                            $rootScope.performLogout();
                                        }
                                    );
                                },
                                function(error) {
                                    $scope.error.errorMessage = error.message;
                                    $scope.error.hasError = true;
                                }
                            )
                        }
                    };
                }
            ]
        );
    }
);