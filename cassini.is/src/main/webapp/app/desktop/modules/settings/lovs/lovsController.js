define(
    [
        'app/desktop/modules/settings/settings.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',

    ],
    function (module) {
        module.controller('LovsController', LovsController);

        function LovsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                LovService, $translate) {
            var vm = this;

            vm.lovs = [];
            vm.toDeleteValue = null;

            vm.addLov = addLov;
            vm.deleteLov = deleteLov;
            vm.saveLov = saveLov;
            vm.addLovValue = addLovValue;
            vm.promptDeleteLov = promptDeleteLov;
            vm.promptDeleteLovValue = promptDeleteLovValue;
            vm.deleteLovValue = deleteLovValue;
            vm.applyChanges = applyChanges;
            vm.cancelChanges = cancelChanges;
            vm.hideMask = hideMask;
            vm.cancelLovChange = cancelLovChange;

            var parsed = angular.element("<div></div>");

            $scope.deleteValueTitle = parsed.html($translate.instant("DELETE_VALUE")).html();
            $scope.lovCancel = parsed.html($translate.instant("CANCEL_CHANGES")).html();

            $scope.saveChangesTitle = parsed.html($translate.instant("SAVE_CHANGES")).html();
            $scope.deleteLovTitle = parsed.html($translate.instant("DELETE_LOV")).html();
            $scope.addNewValueTitle = parsed.html($translate.instant("ADD_NEW_VALUE")).html();
            var the_notAllowed = parsed.html($translate.instant("THE_NOT_ALLOWED")).html();
            var specialCharacterNotAllowed = parsed.html($translate.instant("SPECIAL_CHARACTER_NOT_ALLOWED")).html();
            var valueAlreadyExist = parsed.html($translate.instant("VALUE_ALREADY_EXIST")).html();

            function addLov() {
                var newLov = {
                    name: 'New Lov',
                    newName: 'New Lov',
                    description: "",
                    defaultValue: "",
                    valueObjects: [],
                    values: [],
                    editTitle: true,
                    showBusy: false
                };

                vm.lovs.unshift(newLov);
            }

            var newValue = $translate.instant("NEW_VALUE");

            function addLovValue(lov, index) {
                lov.valueObjects.push({string: newValue, newString: newValue, editMode: true, newMode: true});
                var body = $("#lov-body" + index);
                body.animate({scrollTop: body.height() + 50}, 300);
            }



            function deleteLov(lov) {
                LovService.deleteLov(lov.id).then(
                    function (data) {
                        vm.lovs.remove(lov);
                        $rootScope.showSuccessMessage("Lov deleted successfully");
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            function promptDeleteLov(index) {
                var width = $('#lov' + index).width();
                var height = $('#lov' + index).height();

                $('#lovMask' + index).width(width + 2);
                $('#lovMask' + index).height(height + 2);
                $('#lovMask' + index).css({display: 'table'});
            }

            function promptDeleteLovValue(index) {
                var width = $('#lov' + index).width();
                var height = $('#lov' + index).height();

                $('#lovValueMask' + index).width(width + 2);
                $('#lovValueMask' + index).height(height + 2);
                $('#lovValueMask' + index).css({display: 'table'});
            }

            function applyChanges(lov) {
                lov.values = [];
                angular.forEach(lov.valueObjects, function (obj) {
                    lov.values.push(obj.string);
                });

                saveLov(lov);
            }

            function cancelChanges(lov, value) {
                if (value.newMode == true) {
                    lov.valueObjects.remove(value);
                }
            }

            function validateLovNames(value, lov) {
                var valid = true;
                if (value.newString.includes("_")) {
                    valid = false;
                    value.editMode = true;
                    value.newMode = false;
                    $rootScope.showWarningMessage("specialCharacterNotAllowed");
                }
                else {
                    angular.forEach(lov.values, function (obj) {
                        if (obj.toUpperCase() === value.newString.toUpperCase()) {
                            valid = false;
                            value.editMode = true;
                            value.newMode = false;
                            $rootScope.showWarningMessage("Duplicate lov's are not allowed ");
                        }

                    });
                }
                return valid;
            }

            vm.applyChangesList = applyChangesList;

            function applyChangesList(value, lov) {

                if (validateLovNames(value, lov)) {
                    lov.values = [];
                    if (newValue.includes("_")) {
                        $rootScope.showWarningMessage(the_notAllowed);
                    }

                    angular.forEach(lov.valueObjects, function (obj) {
                        lov.values.push(obj.string);

                    });

                    saveLov(lov);
                }

            }

            vm.cancelChangesList = cancelChangesList;
            function cancelChangesList(lov, value) {
                lov.valueObjects.remove(value);
                loadLovs();
            }

            function cancelLovChange(lov) {
                lov.newName = lov.name;
                if (lov.id == null) {
                    var index = vm.lovs.indexOf(lov);
                    vm.lovs.splice(index, 1);
                }
            }



            function saveLov(lov) {
                var name = lov.name;
                lov.name = lov.newName;
                var promise = null;
                if (lov.id == null) {
                    promise = LovService.createLov(lov);
                }
                else {
                    promise = LovService.updateLov(lov);
                }

                promise.then(
                    function (data) {
                        lov.id = data.id;
                        hideMask(vm.lovs.indexOf(lov));
                        lov.showBusy = false;
                        lov.editTitle = false;
                        $rootScope.showSuccessMessage("Lov Saved Successfully");
                    },
                    function (error) {
                        lov.editTitle = true;
                        lov.name = name;
                        $rootScope.showErrorMessage(error.message);
                        hideMask(vm.lovs.indexOf(lov));
                        lov.showBusy = false;
                    }
                )
            }

            function hideMask(index) {
                $('#lovMask' + index).hide();
                $('#lovValueMask' + index).hide();
            }

            function loadLovs() {
                LovService.getAllLovs().then(
                    function (data) {
                        vm.lovs = data;
                        angular.forEach(vm.lovs, function (lov) {
                            lov.newName = lov.name;
                            lov.editTitle = false;
                            lov.showBusy = false;
                            lov.valueObjects = [];
                            angular.forEach(lov.values, function (value) {
                                lov.valueObjects.push({string: value, newString: value, editMode: false});
                            })
                        });
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }



            function deleteLovValue(lov) {
                if (vm.toDeleteValue != null) {
                    lov.showBusy = true;
                    lov.valueObjects.remove(vm.toDeleteValue);
                    lov.values = [];
                    angular.forEach(lov.valueObjects, function (obj) {
                        lov.values.push(obj.string);
                    });

                    LovService.updateLov(lov).then(
                        function (data) {
                            lov.id = data.id;
                            hideMask(vm.lovs.indexOf(lov));
                            lov.showBusy = false;
                            $rootScope.showSuccessMessage("lov deleted successfully");
                            loadLovs();
                        }
                    );
                }
            }

            vm.obj = null;
            vm.forLovIndex = forLovIndex;
            function forLovIndex(lov, index) {
                vm.obj = lov;
                // vm.lovIndex = index;
            }

            (function () {
                loadLovs();
            })();
        }
    }
);