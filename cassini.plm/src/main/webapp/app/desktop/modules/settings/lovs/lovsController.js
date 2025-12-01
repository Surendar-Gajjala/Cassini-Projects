define(
    [
        'app/desktop/modules/settings/settings.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/shared/services/core/itemTypeService'
    ],
    function (module) {
        module.controller('LovsController', LovsController);

        function LovsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                LovService, $translate, ItemTypeService) {
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
            vm.editLov = editLov;
            vm.editLovValue = editLovValue;

            var parsed = angular.element("<div></div>");

            $scope.deleteValueTitle = parsed.html($translate.instant("DELETE_VALUE")).html();
            $scope.lovCancel = parsed.html($translate.instant("CANCEL_CHANGES")).html();
            $scope.lovAlreadyInUse = parsed.html($translate.instant("LOV_ALREADY_IN_USE")).html();

            $scope.saveChangesTitle = parsed.html($translate.instant("SAVE_CHANGES")).html();
            $scope.deleteLovTitle = parsed.html($translate.instant("DELETE_LOV")).html();
            $scope.addNewValueTitle = parsed.html($translate.instant("ADD_NEW_VALUE")).html();
            var the_notAllowed = parsed.html($translate.instant("THE_NOT_ALLOWED")).html();
            var specialCharacterNotAllowed = parsed.html($translate.instant("SPECIAL_CHARACTER_NOT_ALLOWED")).html();
            var commaNotAllowed = parsed.html($translate.instant("COMMA_NOT_ALLOWED")).html();
            var valueAlreadyExist = parsed.html($translate.instant("VALUE_ALREADY_EXIST")).html();
            var nameValidation = parsed.html($translate.instant("LOV_NAME_VALIDATION")).html();
            var lovValuesValidation = parsed.html($translate.instant("LOV_VALUES_VALIDATION")).html();

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

                $timeout(function () {
                    $('.lov-header input').focus();
                });
            }

            var newValue = $translate.instant("NEW_VALUE");

            function addLovValue(lov, index) {
                lov.valueObjects.push({string: newValue, newString: newValue, editMode: true, newMode: true});
                var body = $("#lov-body" + index);
                body.animate({scrollTop: body.height() + 50}, 300);

                $timeout(function () {
                    $('.lov-value input').focus();
                });
            }

            var deleteLovSuccessMessage = parsed.html($translate.instant("DELETE_LOV_SUCCESS_MESSAGE")).html();
            var deleteLovFailMessage = parsed.html($translate.instant("DELETE_LOV_FAIL_MESSAGE")).html();
            var deleteLovUsedMessage = parsed.html($translate.instant("DELETE_LOV_USED_MESSAGE")).html();

            function deleteLov(lov) {
                ItemTypeService.getItemTypeByLovId(lov).then(
                    function (data) {
                        if (data.itemTypes.length == 0 && data.objectTypes.length == 0) {
                            LovService.getLovById(lov.id).then(
                                function (data) {
                                    if (data.length == 0) {

                                        LovService.deleteLov(lov.id).then(
                                            function (data) {
                                                vm.lovs.remove(lov);
                                                $rootScope.showSuccessMessage(deleteLovSuccessMessage);
                                            },
                                            function (error) {
                                                $rootScope.showErrorMessage(error.message);
                                            }
                                        );
                                    } else {
                                        var values = [];
                                        angular.forEach(data, function (attribute) {
                                            values.push(attribute.name);
                                        })
                                        if (data.length > 1) {
                                            $rootScope.showWarningMessage(deleteLovFailMessage + "s '" + values + "'");
                                        } else {
                                            $rootScope.showWarningMessage(deleteLovFailMessage + " '" + values + "'");
                                        }
                                    }
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            );
                        } else {
                            var values = [];
                            if (data.itemTypes.length > 0) {
                                values.push("Item Types");
                            }

                            if (data.objectTypes.length > 0) {
                                values.push("Requirements");
                            }

                            $rootScope.showWarningMessage(deleteLovUsedMessage + " '" + values + "'");
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function promptDeleteLov(index) {
                var width = $('#lov' + index).width();
                var height = $('#lov' + index).height();
                $('#lov' + index).css({position: 'relative'});
                $('#lovMask' + index).width(width + 2);
                $('#lovMask' + index).height(height + 2);
                $('#lovMask' + index).css({display: 'table'});
            }

            function promptDeleteLovValue(value) {
                var index = vm.lovs.map(function (e) {
                    return e.id;
                }).indexOf(value.lovId);
                var width = $('#lov' + index).width();
                var height = $('#lov' + index).height();
                $('#lov' + index).css({position: 'relative'});
                $('#lovValueMask' + index).width(width + 2);
                $('#lovValueMask' + index).height(height + 2);
                $('#lovValueMask' + index).css({display: 'table'});
            }

            function applyChanges(lov) {
                if (lov.newName == '' || lov.newName == null) {
                    lov.editTitle = true;
                    $rootScope.showWarningMessage(nameValidation)
                } else {
                    lov.values = [];
                    angular.forEach(lov.valueObjects, function (obj) {
                        if (obj.newString != newValue && obj.newString != "" && obj.newString != null && obj.newString != undefined) {
                            lov.values.push(obj.string);
                        }
                    });

                    saveLov(lov);
                }
            }

            function cancelChanges(lov, value) {
                if (value.newMode == true) {
                    lov.valueObjects.remove(value);
                }
            }

            vm.applyChangesList = applyChangesList;

            function applyChangesList(value, lov) {
                if (validateLovNames(value, lov)) {
                    lov.values = [];
                    if (newValue.includes("_")) {
                        $rootScope.showWarningMessage(the_notAllowed);
                    }

                    angular.forEach(lov.valueObjects, function (obj) {
                        obj.lovId = lov.id;
                        if (obj.newString != newValue && obj.newString != "" && obj.newString != null && obj.newString != undefined) {
                            lov.values.push(obj.string);
                        }
                    });

                    saveLov(lov);
                }

            }

            var enterListValue = parsed.html($translate.instant("ENTER_LIST_VALUE")).html();
            var valueCannotBe = parsed.html($translate.instant("VALUE_CANNOT_BE")).html();

            function validateLovNames(value, lov) {
                var valid = true;
                if (value.newString.includes("_")) {
                    valid = false;
                    value.editMode = true;
                    value.newMode = false;
                    $rootScope.showWarningMessage(specialCharacterNotAllowed);
                } else if (value.newString.includes(",")) {
                    valid = false;
                    value.editMode = true;
                    value.newMode = false;
                    $rootScope.showWarningMessage(commaNotAllowed);
                } else if (value.newString == "" || value.newString == null || value.newString == undefined) {
                    valid = false;
                    value.editMode = true;
                    $rootScope.showWarningMessage(enterListValue);
                } else if (value.newString == newValue) {
                    valid = false;
                    value.editMode = true;
                    $rootScope.showWarningMessage(valueCannotBe + " [ " + newValue + " ]");
                } else if (!validateNames(value, lov)) {
                    valid = false;
                }

                return valid;
            }

            function validateNames(value, lov) {
                var valid = true;

                var count = 0;
                angular.forEach(lov.valueObjects, function (obj) {
                    if (obj.newString.toUpperCase() === value.newString.toUpperCase() && obj.hasOwnProperty('lovId')) {
                        count++;
                    }
                });

                if (count > 1) {
                    valid = false;
                    value.editMode = true;
                    value.newMode = false;
                    $rootScope.showWarningMessage(valueAlreadyExist);
                }

                return valid;
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

            var lovSavedMessage = parsed.html($translate.instant("LOV_SAVED_MESSAGE")).html();
            var lovUpdatedMessage = parsed.html($translate.instant("LOV_UPDATED_MESSAGE")).html();
            var newValueValidation = parsed.html($translate.instant("VALUE_VALIDATION")).html();

            function saveLov(lov) {
                var name = lov.name;
                lov.name = lov.newName;
                var promise = null;
                /*if (lov.values.length > 0) {
                    lov.defaultValue = lov.values[0];
                 }*/
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
                        $rootScope.showSuccessMessage(lovSavedMessage);
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
                ItemTypeService.getAllListOfValues().then(
                    function (data) {
                        vm.lovs = data;
                        angular.forEach(vm.lovs, function (lov) {
                            lov.newName = lov.name;
                            lov.editTitle = false;
                            lov.showBusy = false;
                            lov.valueObjects = [];
                            angular.forEach(lov.values, function (value) {
                                lov.valueObjects.push({
                                    lovId: lov.id,
                                    string: value,
                                    newString: value,
                                    editMode: false
                                });
                            })
                        });
                        $timeout(function () {
                            resizeScreen();
                            $rootScope.hideBusyIndicator();
                        }, 500);
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
                /*LovService.getAllLovs().then(
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
                 );*/
            }

            var lovValueDeletedMessage = parsed.html($translate.instant("LOV_VALUE_DELETED_MESSAGE")).html();
            var lovValueUsed = parsed.html($translate.instant("LOV_VALUE_USED_MSG")).html();

            function deleteLovValue(lov) {
                if (vm.toDeleteValue != null) {
                    $rootScope.showBusyIndicator();
                    ItemTypeService.getLovValueUsedCount(lov.id, vm.toDeleteValue.string).then(
                        function (data) {
                            if (data == 0) {
                                lov.showBusy = true;
                                lov.valueObjects.remove(vm.toDeleteValue);
                                lov.values = [];
                                angular.forEach(lov.valueObjects, function (obj) {
                                    if (obj.newString != newValue && obj.newString != "" && obj.newString != null && obj.newString != undefined) {
                                        lov.values.push(obj.string);
                                    }
                                });

                                LovService.updateLov(lov).then(
                                    function (data) {
                                        lov.id = data.id;
                                        hideMask(vm.lovs.indexOf(lov));
                                        lov.showBusy = false;
                                        $rootScope.showSuccessMessage(lovValueDeletedMessage);
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                        $rootScope.hideBusyIndicator();
                                    }
                                );
                            } else {
                                $rootScope.showErrorMessage(lovValueUsed.format(vm.toDeleteValue.string));
                            }
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            vm.obj = null;
            vm.forLovIndex = forLovIndex;
            function forLovIndex(lov, index) {
                vm.obj = lov;
                // vm.lovIndex = index;
            }

            function resizeScreen() {
                var viewContent = $('.view-content').outerHeight();
                $('.stickyheader').height(viewContent - 65);
            }


            function editLovValue(value, lov) {
                if (!lov.usedLov) {
                    value.editMode = true;
                    vm.toDeleteValue = null;
                } else {
                    $rootScope.showBusyIndicator();
                    ItemTypeService.getLovValueUsedCount(lov.id, value.string).then(
                        function (data) {
                            if (data == 0) {
                                value.editMode = true;
                            } else {
                                $rootScope.showErrorMessage(lovValueUsed);
                            }
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function editLov(lov) {
                if (!lov.usedLov) {
                    lov.editTitle = true;
                }
            }

            $scope.updateLovSequence = updateLovSequence;
            function updateLovSequence(lov, actualValue, targetValue) {
                if (actualValue != null && targetValue != null) {
                    var targetIndex = lov.valueObjects.indexOf(targetValue);
                    var actualIndex = lov.valueObjects.indexOf(actualValue);

                    if (targetIndex != actualIndex) {
                        $rootScope.showBusyIndicator();
                        lov.valueObjects.splice(actualIndex, 1);
                        lov.valueObjects.splice(targetIndex, 0, actualValue);
                        lov.values = [];
                        angular.forEach(lov.valueObjects, function (obj) {
                            if (obj.newString != newValue && obj.newString != "" && obj.newString != null && obj.newString != undefined) {
                                lov.values.push(obj.string);
                            }
                        });

                        LovService.updateLov(lov).then(
                            function (data) {
                                lov.id = data.id;
                                lov.showBusy = false;
                                lov.editTitle = false;
                                $rootScope.showSuccessMessage(lovUpdatedMessage);
                                $rootScope.hideBusyIndicator();
                            },
                            function (error) {
                                lov.editTitle = true;
                                lov.name = name;
                                $rootScope.showErrorMessage(error.message);
                                lov.showBusy = false;
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                }
            }

            (function () {
                loadLovs();
                $(window).resize(function () {
                    resizeScreen();
                });

                $scope.$on("settings.new.lov", function (evnt, args) {
                    addLov();
                });
            })();
        }
    }
);