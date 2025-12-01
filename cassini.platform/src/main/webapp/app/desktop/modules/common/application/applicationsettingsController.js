define(
    [
        'app/desktop/modules/settings/settings.module',
        'split-pane',
        'jquery.easyui',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/preferenceService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/forgeService'
    ],
    function (module) {
        module.controller('ApplicationSettingsController', ApplicationSettingsController);

        function ApplicationSettingsController($q, $scope, $rootScope, $timeout, $state, DialogService, $cookies,
                                               CommonService, $translate, PreferenceService, ForgeService) {

            var vm = this;
            var parsed = angular.element("<div></div>");

            var context = 'APPLICATION';
            vm.update = false;
            vm.fileEdit = false;
            vm.fileType = false;
            vm.fileSize = false;
            vm.recurringItem = false;
            vm.editfileValue = editfileValue;
            vm.fileTypeUpdate = false;
            vm.fileSizeUpdate = false;
            $scope.changeApproval = {
                id: null,
                context: context,
                preferenceKey: 'APPLICATION.CHANGE_APPROVAL',
                booleanValue: false
            };
            var newHoliday = {
                id: null,
                name: "",
                date: null,
                editMode: true,
                showValues: false,
                isNew: true
            };

            vm.deleteTitle = parsed.html($translate.instant("DELETE")).html();
            vm.editTitle = parsed.html($translate.instant("EDIT")).html();
            var forgeSettingsSaved = parsed.html($translate.instant("FORGE_SETTINGS_SAVED")).html();
            var applicationSettingsSaved = parsed.html($translate.instant("APPLICATION_SETTINGS_SAVED")).html();
            var applicationSettingsUpdated = parsed.html($translate.instant("APPLICATION_SETTINGS_UPDATED")).html();
            var valueValidation = parsed.html($translate.instant("VALUE_VALIDATION")).html();
            var recurringValidation = parsed.html($translate.instant("ENTER_RECURRING_POSITIVE_NUMBER")).html();
            var fileSizeValidation = parsed.html($translate.instant("FILE_SIZE_VALIDATION")).html();
            vm.addFileType = parsed.html($translate.instant("ADD_FILE_TYPE")).html();
            var holidayDeleteTitle = parsed.html($translate.instant("HOLIDAY_DIALOG_TITLE")).html();
            var holidayDeleteMessage = parsed.html($translate.instant("HOLIDAY_DIALOG_MESSAGE")).html();
            var holidaysUpdated = parsed.html($translate.instant("HOLIDAYS_LIST_UPDATED")).html();
            var workingDaysUpdated = parsed.html($translate.instant("SAVED_WORKING_DAYS")).html();
            var nameValidation = parsed.html($translate.instant("ITEM_NAME_VALIDATION")).html();
            var nameExists = parsed.html($translate.instant("DUPLICATE_MESSAGE")).html();
            var dateExists = parsed.html($translate.instant("DATE_EXIST")).html();
            var dateValidation = parsed.html($translate.instant("HOLIDAY_DATE_VALIDATION")).html();
            var holidayDeleteSuccessMessage = parsed.html($translate.instant("HOLIDAY_DELETED_SUCCESS_MESSAGE")).html();
            $scope.edit = parsed.html($translate.instant("EDIT")).html();

            vm.application = {
                fileSize: null,
                fileType: null,
                recurringItem: null
            };

            var holiday = {
                name: "",
                date: null
            };

            vm.applicationForge = {
                forgeActive: {
                    id: null,
                    context: 'APPLICATION',
                    preferenceKey: 'APPLICATION.FORGE_ACTIVE',
                    booleanValue: ''
                },
                forgeClientId: {
                    id: null,
                    context: 'APPLICATION',
                    preferenceKey: 'APPLICATION.FORGE_CLIENT_ID',
                    stringValue: ''
                },
                forgeClientSecretKey: {
                    id: null,
                    context: 'APPLICATION',
                    preferenceKey: 'APPLICATION.FORGE_CLIENT_SECRET_KEY',
                    stringValue: ''
                },
                forgeBucketName: {
                    id: null,
                    context: 'APPLICATION',
                    preferenceKey: 'APPLICATION.CLIENT_BUCKET_NAME',
                    stringValue: ''
                }
            };

            vm.oldSettings = {
                fileSize: null,
                fileType: null,
                recurringItem: null
            };

            vm.oldSettingsHolidays = {
                workingDays: null,
                holidayList: []
            };

            vm.holidays = {
                workingDays: null,
                holidayList: []
            };

            vm.oldSettingsForge = angular.copy(vm.applicationForge);

            vm.ids = {
                fileSizeId: null,
                fileTypeId: null,
                recurringItem: null
            };


            function loadApplicationSettings() {
                CommonService.getPreferenceByContext(context).then(
                    function (data) {
                        if (data.length > 0) {
                            vm.applicationSettings = data;
                            angular.forEach(vm.applicationSettings, function (app) {
                                switch (app.preferenceKey) {
                                    case 'APPLICATION.FILESIZE':
                                        vm.fileSizeUpdate = true;
                                        vm.ids.fileSizeId = app.id;
                                        vm.oldSettings.fileSize = app.integerValue;
                                        vm.application.fileSize = app.integerValue;
                                        if (vm.application.fileSize == null || vm.application.fileSize == 50) {
                                            $("#fileSize").prop('checked', false);
                                            $("#timeVal").attr('readOnly', true);
                                        } else {
                                            $("#fileSize").prop('checked', true);
                                            $("#timeVal").attr('readOnly', false);
                                        }
                                        break;
                                    case 'APPLICATION.FILETYPE':
                                        vm.fileTypeUpdate = true;
                                        vm.ids.fileTypeId = app.id;
                                        vm.oldSettings.fileType = app.stringValue;
                                        vm.application.fileType = app.stringValue;
                                        break;
                                    case 'APPLICATION.CHANGE_APPROVAL':
                                        $scope.changeApproval = app;
                                        break;
                                    case 'APPLICATION.RECURRING_ITEM':
                                        vm.ids.recurringItem = app.id;
                                        vm.oldSettings.recurringItem = app.integerValue;
                                        vm.application.recurringItem = app.integerValue;
                                        break;
                                    case 'APPLICATION.FORGE_ACTIVE':
                                        vm.oldSettingsForge.forgeActive = angular.copy(app);
                                        vm.applicationForge.forgeActive = app;
                                        break;
                                    case 'APPLICATION.FORGE_CLIENT_ID':
                                        vm.oldSettingsForge.forgeClientId = angular.copy(app);
                                        ;
                                        vm.applicationForge.forgeClientId = app;
                                        break;
                                    case 'APPLICATION.FORGE_CLIENT_SECRET_KEY':
                                        vm.oldSettingsForge.forgeClientSecretKey = angular.copy(app);
                                        ;
                                        vm.applicationForge.forgeClientSecretKey = app;
                                        break;
                                    case 'APPLICATION.CLIENT_BUCKET_NAME':
                                        vm.oldSettingsForge.forgeBucketName = angular.copy(app);
                                        ;
                                        vm.applicationForge.forgeBucketName = app;
                                        break;
                                    case 'APPLICATION.WORKING_DAYS':
                                        vm.oldSettingsHolidays.workingDays = app.integerValue;
                                        vm.holidays.workingDays = app.integerValue;
                                        break;
                                    case 'APPLICATION.HOLIDAY_LIST':
                                        vm.oldSettingsHolidays.holidayList = JSON.parse(angular.copy(app.jsonValue));
                                        vm.holidays.holidayList = JSON.parse(app.jsonValue);
                                        loadHolidays();
                                        break;
                                    default:
                                        break;
                                }
                            });
                            watchChangeApproval();
                        }
                        $timeout(function () {
                            $("#application-settings").height($(".miscellaneous-right").outerHeight() - 20);
                        }, 200);
                    },
                    function (error) {
                        console.log(error);
                    }
                )
            }

            function loadHolidays() {
                angular.forEach(vm.holidays.holidayList, function (holiday) {
                    holiday.newName = holiday.name;
                    holiday.newDate = holiday.date;
                    holiday.editMode = false;
                    holiday.showValues = true;
                    holiday.isNew = false;
                });
            }

            vm.showEditMode = showEditMode;
            function showEditMode(holiday) {
                holiday.newName = holiday.name;
                holiday.newDate = holiday.date;
                holiday.editMode = true;
                holiday.showValues = false;
                holiday.isNew = false;
            }

            vm.hideEditMode = hideEditMode;
            function hideEditMode(holiday) {
                var index = vm.holidays.holidayList.indexOf(holiday);
                vm.holidays.holidayList.splice(index, 1);
            }

            vm.cancelEditMode = cancelEditMode;
            function cancelEditMode(holiday) {
                holiday.newName = holiday.name;
                holiday.newDate = holiday.date;
                holiday.editMode = false;
                holiday.showValues = true;
                holiday.isNew = false;
            }

            vm.addHoliday = addHoliday;
            function addHoliday() {
                var holiday = angular.copy(newHoliday);
                vm.holidays.holidayList.unshift(holiday);
            }

            vm.delete = false;
            vm.deleteholiday = deleteholiday;
            function deleteholiday(holiday) {
                var options = {
                    title: holidayDeleteTitle,
                    message: holidayDeleteMessage + " [ " + holiday.name + " ] " + "?",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        var index = vm.holidays.holidayList.indexOf(holiday);
                        if (index != -1) {
                            vm.holidays.holidayList.splice(index, 1);
                        }
                        unSetWorkTime(holiday);
                        vm.delete = true;
                        saveHoliday();
                    }
                });

            }

            function unSetWorkTime(holiday) {
                var parts = holiday.date.split('/');
                gantt.unsetWorkTime({date: new Date(parts[2], parts[1] - 1, parts[0]), hours: false});
            }

            function editfileValue() {
                vm.fileEdit = true;
            }

            vm.addfileType = addfileType;
            function addfileType() {
                vm.fileType = true;
                vm.application.newfileType = vm.application.fileType;
            }

            vm.addfileSize = addfileSize;
            function addfileSize() {
                vm.fileSize = true;
                vm.application.newfileSize = vm.application.fileSize;
            }

            vm.addRecurringItem = addRecurringItem;
            function addRecurringItem() {
                vm.recurringItem = true;
                vm.application.newRecurringItem = vm.application.recurringItem;
            }

            vm.cancelFileType = cancelFileType;
            function cancelFileType() {
                vm.fileType = false;
                vm.application.fileType = vm.application.newfileType;
            }

            vm.cancelFileSize = cancelFileSize;
            function cancelFileSize() {
                vm.fileSize = false;
                vm.application.fileSize = vm.application.newfileSize;
            }

            vm.cancelRecurringItem = cancelRecurringItem;
            function cancelRecurringItem() {
                vm.recurringItem = false;
                vm.application.recurringItem = vm.oldSettings.recurringItem;
            }

            vm.forgeEdit = false;
            vm.editForgeDetails = editForgeDetails;
            function editForgeDetails() {
                vm.forgeEdit = true;
                vm.newApplicationForge = vm.applicationForge;
            }

            vm.cancelEditForgeDetails = cancelEditForgeDetails;
            function cancelEditForgeDetails() {
                vm.forgeEdit = false;
                vm.applicationForge = angular.copy(vm.oldSettingsForge);
            }

            function checkNullValue(val) {
                if (vm.applicationForge.forgeActive.booleanValue) {
                    if (val != null && val != '' && val != undefined) return true;
                    else return false;
                } else {
                    if (val != null && val != undefined) return true;
                    else return false;
                }
            }

            vm.submitForgeDetails = submitForgeDetails;
            function submitForgeDetails() {
                var objs = [];
                objs.push(vm.applicationForge.forgeActive);
                if (checkNullValue(vm.applicationForge.forgeClientId.stringValue)) objs.push(vm.applicationForge.forgeClientId);
                if (checkNullValue(vm.applicationForge.forgeClientSecretKey.stringValue)) objs.push(vm.applicationForge.forgeClientSecretKey);
                if (vm.applicationForge.forgeActive.booleanValue && checkNullValue(vm.applicationForge.forgeClientId.stringValue) && checkNullValue(vm.applicationForge.forgeClientSecretKey.stringValue)) {
                    $rootScope.showBusyIndicator($('.view-content'));
                    ForgeService.checkForgeAuthentication(vm.applicationForge.forgeClientId.stringValue, vm.applicationForge.forgeClientSecretKey.stringValue).then(
                        function (val) {
                            if (val) {
                                CommonService.saveForgeDetails(objs).then(
                                    function (data) {
                                        vm.forgeEdit = false;
                                        loadApplicationSettings();
                                        $rootScope.showSuccessMessage(forgeSettingsSaved);
                                        $rootScope.hideBusyIndicator();
                                    }, function (error) {
                                        console.log(error);
                                        $rootScope.hideBusyIndicator();
                                    }
                                )
                            } else {
                                $rootScope.showErrorMessage("Please provide valid client id and secret key");
                                $rootScope.hideBusyIndicator();
                            }
                        }, function (err) {
                            $rootScope.showErrorMessage(err.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                } else if (!vm.applicationForge.forgeActive.booleanValue && checkNullValue(vm.applicationForge.forgeClientId.stringValue) && checkNullValue(vm.applicationForge.forgeClientSecretKey.stringValue)) {
                    $rootScope.showBusyIndicator($('.view-content'));
                    CommonService.saveForgeDetails(objs).then(
                        function (data) {
                            vm.forgeEdit = false;
                            loadApplicationSettings();
                            $rootScope.showSuccessMessage(forgeSettingsSaved);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            console.log(error);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    $rootScope.showErrorMessage("Please enter client id and secret key");
                    $rootScope.hideBusyIndicator();
                }

            }

            vm.updateRecurringItem = updateRecurringItem;
            function updateRecurringItem() {
                if (validate(vm.application.recurringItem)) {
                    CommonService.updateRecurringItem(vm.ids.recurringItem, vm.application.recurringItem).then(
                        function (data) {
                            vm.recurringItem = false;
                            loadApplicationSettings();
                            $rootScope.showSuccessMessage(applicationSettingsUpdated);
                        }, function (error) {
                            console.log(error);
                        }
                    )
                } else {
                    $rootScope.showWarningMessage(recurringValidation);
                }
            }

            vm.SubmitFileSize = SubmitFileSize;
            function SubmitFileSize() {
                if (vm.fileSizeUpdate == false) {
                    if (validate(vm.application.fileSize)) {
                        if (vm.application.fileSize != null) {
                            CommonService.saveFileSize(context, vm.application.fileSize).then(
                                function (data) {
                                    vm.fileSize = false;
                                    vm.fileSizeUpdate = true;
                                    loadApplicationSettings();
                                    $rootScope.showSuccessMessage(applicationSettingsSaved);
                                }, function (error) {
                                    console.log(error);
                                }
                            )
                        } else {
                            $rootScope.showWarningMessage(valueValidation);
                        }
                    }
                } else {
                    if (validate(vm.application.fileSize)) {
                        if (vm.application.fileSize != null && vm.ids.fileSizeId != null) {
                            CommonService.updateFileSize(vm.ids.fileSizeId, vm.application.fileSize).then(
                                function (data) {
                                    vm.fileSize = false;
                                    loadApplicationSettings();
                                    $rootScope.showSuccessMessage(applicationSettingsUpdated);
                                }, function (error) {
                                    console.log(error);
                                }
                            )
                        } else {
                            $rootScope.showWarningMessage(valueValidation);
                        }
                    }
                }
            }

            function validate(number) {
                var valid = true;
                if (number <= 0 || !Number.isInteger(number)) {
                    $rootScope.showWarningMessage(fileSizeValidation);
                    valid = false;
                }
                return valid;
            }

            vm.SubmitFileType = SubmitFileType;
            function SubmitFileType() {
                if (vm.application.fileType == "") vm.application.fileType = "NONE";
                if (vm.fileTypeUpdate == false) {
                    if (vm.application.fileType != null) {
                        CommonService.saveFileType(context, vm.application.fileType).then(
                            function (data) {
                                vm.fileType = false;
                                vm.fileTypeUpdate = true;
                                loadApplicationSettings();
                                $rootScope.showSuccessMessage(applicationSettingsSaved);
                            }, function (error) {
                                console.log(error);
                            }
                        )
                    } else {
                        $rootScope.showWarningMessage(valueValidation);
                    }
                } else {
                    if (vm.application.fileType != null && vm.ids.fileTypeId != null) {
                        CommonService.updateFileType(vm.ids.fileTypeId, vm.application.fileType).then(
                            function (data) {
                                vm.fileType = false;
                                loadApplicationSettings();
                                $rootScope.showSuccessMessage(applicationSettingsUpdated);
                            }, function (error) {
                                console.log(error);
                            }
                        )
                    } else {
                        $rootScope.showWarningMessage(valueValidation);
                    }
                }
            }

            function watchChangeApproval() {
                $scope.$watch('changeApproval.booleanValue', function (newVal, oldVal) {
                    if (angular.equals(newVal, oldVal)) {

                    } else {
                        $scope.changeApproval.booleanValue = newVal;
                        updatePreference();
                    }
                    $scope.$evalAsync();
                }, true);
            }

            vm.saveWorkingDays = saveWorkingDays;
            function saveWorkingDays() {
                CommonService.saveWorkingDays(vm.holidays.workingDays).then(
                    function (data) {
                        loadApplicationSettings();
                        $rootScope.showSuccessMessage(workingDaysUpdated);
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.saveHoliday = saveHoliday;
            function saveHoliday() {
                if (validateHoliday()) {
                    CommonService.saveHolidays(JSON.stringify(vm.holidayList)).then(
                        function (data) {
                            loadApplicationSettings();
                            if (vm.delete == true) $rootScope.showSuccessMessage(holidayDeleteSuccessMessage);
                            if (vm.delete != true) $rootScope.showSuccessMessage(holidaysUpdated);
                            vm.delete = false;
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function validateHoliday() {
                vm.holidayList = [];
                var holidayVal = {};
                var valid = true;
                var holidays = angular.copy(vm.holidays.holidayList);
                angular.forEach(holidays, function (val, index) {
                    if (valid) {
                        holidayVal = angular.copy(holiday);
                        if (val.newName != null && val.newName != "" && val.newName != undefined && valid) {
                            holidayVal.name = val.newName;
                        } else {
                            valid = false;
                            $rootScope.showWarningMessage(nameValidation);
                        }
                        if (val.newDate != null && val.newDate != "" && val.newName != undefined && valid) {
                            holidayVal.date = val.newDate;
                        } else if (valid) {
                            valid = false;
                            $rootScope.showWarningMessage(dateValidation);
                        }
                        if (val.isNew && valid) {
                            angular.forEach(holidays, function (val1, index1) {
                                if (index != index1) {
                                    if (val.newName == val1.newName) {
                                        valid = false;
                                        $rootScope.showWarningMessage(nameExists);
                                    }
                                    if (val.newDate == val1.newDate) {
                                        valid = false;
                                        $rootScope.showWarningMessage(dateExists);
                                    }
                                }
                            });
                        }
                        vm.holidayList.push(holidayVal);
                    }
                });
                return valid;
            }


            function updatePreference() {
                $rootScope.showBusyIndicator($('.view-content'));
                if ($scope.changeApproval.id == null || $scope.changeApproval.id == "") {
                    PreferenceService.createPreference($scope.changeApproval).then(
                        function (data) {
                            $scope.changeApproval = data;
                            $rootScope.showSuccessMessage(applicationSettingsUpdated);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    PreferenceService.updatePreference($scope.changeApproval).then(
                        function (data) {
                            $scope.changeApproval = data;
                            $rootScope.showSuccessMessage(applicationSettingsUpdated);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            $(document).on('change', '[id="fileSize"]', function () {
                var checkbox = $(this);
                if (checkbox.is(':checked')) {
                    document.getElementById("sizeVal").readOnly = false;
                } else {
                    vm.application.fileSize = 50;
                    document.getElementById("sizeVal").readOnly = true;
                }
            });

            function resizeScreen() {
                $("#application-settings").height($(".miscellaneous-right").outerHeight() - 80);
            }

            (function () {
                loadApplicationSettings();
                $(window).resize(function () {
                    resizeScreen();
                })
            })();
        }
    }
)
;