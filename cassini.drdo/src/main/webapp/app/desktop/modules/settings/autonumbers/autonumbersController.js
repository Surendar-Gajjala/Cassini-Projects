define(['app/desktop/modules/settings/settings.module',
        'split-pane',
        'jquery.easyui',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/shared/services/core/itemTypeService'
    ],
    function (module) {
        module.controller('AutonumbersController', AutonumbersController);

        function AutonumbersController($q, $scope, $rootScope, $timeout, $state, DialogService, $stateParams, $cookies,
                                       CommonService, AutonumberService, ItemTypeService) {
            var vm = this;

            vm.autonumbers = [];

            vm.addAutonumber = addAutonumber;
            vm.acceptChanges = acceptChanges;
            vm.showEditMode = showEditMode;
            vm.hideEditMode = hideEditMode;
            vm.deleteAutonumber = deleteAutonumber;
            vm.getAutonumberExamples = getAutonumberExamples;

            var newAutonumber = {
                id: null,
                name: "",
                description: "",
                numbers: 5,
                start: 1,
                increment: 1,
                padwith: "0",
                prefix: "",
                suffix: "",
                newName: "",
                newDescription: "",
                newNumber: 5,
                newStart: 1,
                newIncrement: 1,
                newPadwith: "0",
                newPrefix: "",
                newSuffix: "",
                editMode: true,
                showValues: false
            };

            function addAutonumber() {
                var autoNo = angular.copy(newAutonumber);
                vm.autonumbers.unshift(autoNo);
            }

            function acceptChanges(autonumber) {
                if (autonumber.newName != null && autonumber.newName != "") {
                    autonumber.name = autonumber.newName;
                }
                if (autonumber.newDescription != null && autonumber.newDescription != "") {
                    autonumber.description = autonumber.newDescription;
                }
                autonumber.numbers = autonumber.newNumber;
                autonumber.start = autonumber.newStart;
                autonumber.increment = autonumber.newIncrement;
                autonumber.padwith = autonumber.newPadwith;
                if (autonumber.newPrefix != null && autonumber.newPrefix != "") {
                    autonumber.prefix = autonumber.newPrefix;
                }
                autonumber.suffix = autonumber.newSuffix;
                var promise = null;
                if (autonumber.id == null) {
                    if (validateAutoNumber(autonumber)) {
                        vm.autonumberName = [];
                        AutonumberService.getAutonumberName(autonumber.name).then(
                            function (data) {
                                vm.autonumberName = data;
                                AutonumberService.getAutonumberPrefix(autonumber.prefix).then(
                                    function (data) {
                                        vm.autonumberPrefix = data;
                                        if (vm.autonumberPrefix == "") {
                                            if (vm.autonumberName == "") {
                                                promise = AutonumberService.createAutonumber(autonumber).then(
                                                    function (data) {
                                                        autonumber.id = data.id;
                                                        autonumber.newName = autonumber.name;
                                                        autonumber.newDescription = autonumber.description;
                                                        autonumber.newNumber = autonumber.numbers;
                                                        autonumber.newStart = autonumber.start;
                                                        autonumber.newIncrement = autonumber.increment;
                                                        autonumber.newPadwith = autonumber.padwith;
                                                        autonumber.newPrefix = autonumber.prefix;
                                                        autonumber.newSuffix = autonumber.suffix;
                                                        autonumber.editMode = false;
                                                        autonumber.showValues = true;
                                                        $rootScope.showSuccessMessage("Auto Number created successfully");
                                                    },
                                                    function (error) {
                                                        $rootScope.showErrorMessage(error.message);
                                                    }
                                                )
                                            } else {
                                                $rootScope.showWarningMessage(autonumber.name + " : Auto Number Name already exist");
                                                autonumber.showValues = false;

                                            }
                                        } else {
                                            $rootScope.showWarningMessage(autonumber.prefix + " : Prefix already exist");
                                            autonumber.showValues = false;
                                        }
                                    }
                                )
                            }
                        )
                    }
                }
                else {
                    promise = AutonumberService.updateAutonumber(autonumber.id, autonumber).then(
                        function (data) {
                            autonumber.id = data.id;
                            autonumber.newName = autonumber.name;
                            autonumber.newDescription = autonumber.description;
                            autonumber.newNumber = autonumber.numbers;
                            autonumber.newStart = autonumber.start;
                            autonumber.newIncrement = autonumber.increment;
                            autonumber.newPadwith = autonumber.padwith;
                            autonumber.newPrefix = autonumber.prefix;
                            autonumber.newSuffix = autonumber.suffix;
                            autonumber.editMode = false;
                            autonumber.showValues = true;
                            $rootScope.showSuccessMessage("Auto Number updated successfully");
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                        })

                }
            }

            function validateAutoNumber(autoNo) {
                var valid = true;
                if (autoNo.name == null || autoNo.name == "") {
                    $rootScope.showWarningMessage("Please enter Name");
                    valid = false;
                } else if (autoNo.numbers == null || autoNo.numbers == "") {
                    $rootScope.showWarningMessage("Please enter Numbers");
                    valid = false;
                } else if (autoNo.start == null || autoNo.start == "") {
                    $rootScope.showWarningMessage("Please enter Start Number");
                    valid = false;
                } else if (autoNo.increment == null || autoNo.increment == "") {
                    $rootScope.showWarningMessage("Please enter Increment Number");
                    valid = false;
                } else if (autoNo.prefix == null || autoNo.prefix == "") {
                    $rootScope.showWarningMessage("Please enter Prefix");
                    valid = false;
                }
                return valid;
            }

            function showEditMode(autonumber) {

                ItemTypeService.findItemsByAutoNumberId(autonumber.id).then(
                    function (data) {
                        if (data.length == 0) {
                            autonumber.newName = autonumber.name;
                            autonumber.newDescription = autonumber.description;
                            autonumber.newNumber = autonumber.numbers;
                            autonumber.newStart = autonumber.start;
                            autonumber.newIncrement = autonumber.increment;
                            autonumber.newPadwith = autonumber.padwith;
                            autonumber.newPrefix = autonumber.prefix;
                            autonumber.newSuffix = autonumber.suffix;
                            autonumber.editMode = true;
                            autonumber.showValues = false;
                        } else {
                            $rootScope.showWarningMessage("This Auto Number has items, Cannot edit this Auto Number");
                        }
                    }
                )
            }

            function hideEditMode(autonumber) {
                if (autonumber.id == null) {
                    var index = vm.autonumbers.indexOf(autonumber);
                    if (index != -1) {
                        vm.autonumbers.splice(index, 1);
                    }
                }
                else {
                    AutonumberService.getAutonumber(autonumber.id).then(
                        function (data) {
                            autonumber.prefix = data.prefix;
                            autonumber.suffix = data.suffix;
                            autonumber.numbers = data.numbers;
                            autonumber.name = data.name;
                            autonumber.descritpion = data.description;
                            autonumber.start = data.start;
                            autonumber.increment = data.increment;
                            autonumber.editMode = false;
                        }
                    )

                    $timeout(function () {
                        autonumber.showValues = true;
                    }, 1000);
                }
            }

            function deleteAutonumber(auto) {
                ItemTypeService.findItemsByAutoNumberId(auto.id).then(
                    function (data) {
                        if (data.length == 0) {
                            var options = {
                                title: "Delete Auto Number",
                                message: "Please confirm to delete this Auto Number",
                                okButtonClass: 'btn-danger',
                                okButtonText: "Yes",
                                cancelButtonText: "No"
                            };
                            DialogService.confirm(options, function (yes) {
                                if (yes == true) {
                                    AutonumberService.deleteAutonumber(auto.id).then(
                                        function (data) {
                                            var index = vm.autonumbers.indexOf(auto);
                                            vm.autonumbers.splice(index, 1);
                                            $rootScope.showSuccessMessage("Auto Number deleted successfully");
                                        },
                                        function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    );
                                }
                            });
                        } else {
                            $rootScope.showWarningMessage("This Auto Number has items, Cannot delete this Auto Number");
                        }
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function getAutonumberExamples(autonumber) {
                var example = "";
                var count = 2;
                for (var i = 0; i < count; i++) {
                    if (autonumber.prefix != null && autonumber.prefix.length > 0) {
                        example += autonumber.prefix;
                    }
                    var t = "";
                    if (i == 0) {
                        t = "" + autonumber.start;
                    } else {
                        t = "" + (autonumber.start + (i * autonumber.increment));
                    }
                    example += getNumberPart(autonumber.numbers, autonumber.padwith, t);
                    example += t;
                    if (autonumber.suffix != null && autonumber.suffix != undefined)
                        example += autonumber.suffix;
                    if (i != count - 1) {
                        example += ", "
                    }
                }
                return example;
            }

            function getNumberPart(numbers, padwith, number) {
                var p = "";

                var t = "" + number;
                var n = numbers - t.length;

                p += getPadding(padwith, n);

                return p;
            }

            function getPadding(padWith, times) {
                var padding = "";

                for (var i = 0; i < times; i++) {
                    padding += padWith;
                }

                return padding;
            }


            function loadAutoNumbers() {
                AutonumberService.getAutonumbers().then(
                    function (data) {
                        vm.autonumbers = data;
                        angular.forEach(vm.autonumbers, function (autonumber) {
                            autonumber.newName = autonumber.name;
                            autonumber.newDescription = autonumber.description;
                            autonumber.newNumber = autonumber.numbers;
                            autonumber.newStart = autonumber.start;
                            autonumber.newIncrement = autonumber.increment;
                            autonumber.newPadwith = autonumber.padwith;
                            autonumber.newPrefix = autonumber.prefix;
                            autonumber.newSuffix = autonumber.suffix;
                        })
                        addFlags();
                    },
                    function (error) {

                    }
                );
            }

            function addFlags() {
                angular.forEach(vm.autonumbers, function (autonumber) {
                    autonumber.editMode = false;
                    autonumber.showValues = true;
                });
            }

            (function () {
                loadAutoNumbers();
            })();
        }
    }
)
;