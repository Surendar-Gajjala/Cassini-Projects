define(['app/desktop/modules/settings/settings.module',
        'split-pane',
        'jquery.easyui',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/shared/services/core/itemService'
    ],
    function (module) {
        module.controller('AutonumbersController', AutonumbersController);

        function AutonumbersController($q, $scope, $rootScope, $timeout, $state, DialogService, $stateParams, $cookies,
                                       CommonService, AutonumberService, ItemService) {
            var vm = this;

            vm.autonumbers = [];

            vm.addAutonumber = addAutonumber;
            vm.acceptChanges = acceptChanges;
            vm.showEditMode = showEditMode;
            vm.hideEditMode = hideEditMode;
            vm.deleteAutonumber = deleteAutonumber;
            vm.getAutonumberExamples = getAutonumberExamples;

            function addAutonumber() {
                vm.newAutonumber = {
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

                vm.autonumbers.unshift(vm.newAutonumber);
            }

            function acceptChanges(autonumber) {
                autonumber.name = autonumber.newName;
                autonumber.description = autonumber.newDescription;
                autonumber.numbers = autonumber.newNumber;
                autonumber.start = autonumber.newStart;
                autonumber.increment = autonumber.newIncrement;
                autonumber.padwith = autonumber.newPadwith;
                autonumber.prefix = autonumber.newPrefix;
                autonumber.suffix = autonumber.newSuffix;
                if (autonumber.id == null) {
                    if (validateAutoNumber(autonumber)) {
                        vm.autonumberName = [];
                        AutonumberService.getAutonumberName(autonumber.name).then(
                            function (data) {
                                vm.autonumberName = data;
                                if (vm.autonumberName == "") {
                                    AutonumberService.createAutonumber(autonumber).then(
                                        function (data) {
                                            autonumber.editMode = false;
                                            $timeout(function () {
                                                loadAutoNumbers();
                                            }, 100);
                                            $rootScope.showSuccessMessage("Auto Number created successfully");
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    );

                                } else {
                                    $rootScope.showWarningMessage("Auto Number Name already exists");
                                    autonumber.showValues = false;

                                }
                            }
                        )
                    }
                }
                else {
                    if (validate(autonumber)) {
                        AutonumberService.updateAutonumber(autonumber.id, autonumber).then(
                            function (data) {
                                autonumber.id = data.id;
                                autonumber.editMode = false;
                                $timeout(function () {
                                    autonumber.showValues = true;
                                }, 500);
                                $rootScope.showSuccessMessage("Auto Number updated successfully");
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                }
            }

            function validateAutoNumber() {
                var valid = true;
                if (vm.newAutonumber.name == null || vm.newAutonumber.name == "") {
                    $rootScope.showErrorMessage("Please enter Name");
                    valid = false;
                } else if (vm.newAutonumber.description == null || vm.newAutonumber.description == "") {
                    $rootScope.showErrorMessage("Please enter Description");
                    valid = false;
                } else if (vm.newAutonumber.prefix == null || vm.newAutonumber.prefix == "") {
                    $rootScope.showErrorMessage("Please enter Prefix");
                    valid = false;
                } else if (vm.newAutonumber.numbers == null || vm.newAutonumber.numbers == "") {
                    $rootScope.showErrorMessage("Please enter Numbers");
                    valid = false;
                }
                else if (vm.newAutonumber.start == null || vm.newAutonumber.start == "") {
                    $rootScope.showErrorMessage("Please enter Starting Number");
                    valid = false;
                }
                else if (vm.newAutonumber.padwith == null || vm.newAutonumber.padwith == "") {
                    $rootScope.showErrorMessage("Please enter Padwith");
                    valid = false;
                }
                else if (vm.newAutonumber.increment == null || vm.newAutonumber.increment == "") {
                    $rootScope.showErrorMessage("Please enter Increment value");
                    valid = false;
                }
                return valid;
            }

            function validate(autoNumber) {
                var valid = true;
                if (autoNumber.name == null || autoNumber.name == "") {
                    $rootScope.showErrorMessage("Please enter Name");
                    valid = false;
                }
                else if (autoNumber.description == null || autoNumber.description == "") {
                    $rootScope.showErrorMessage("Please enter Description");
                    valid = false;
                }
                else if (autoNumber.prefix == null || autoNumber.prefix == "") {
                    $rootScope.showErrorMessage("Please enter Prefix");
                    valid = false;
                }
                else if (autoNumber.numbers == null || autoNumber.numbers == "") {
                    $rootScope.showErrorMessage("Please enter Numbers");
                    valid = false;
                }
                else if (autoNumber.start == null || autoNumber.start == "") {
                    $rootScope.showErrorMessage("Please enter Starting Number");
                    valid = false;
                }
                else if (autoNumber.padwith == null || autoNumber.padwith == "") {
                    $rootScope.showErrorMessage("Please enter Padwith");
                    valid = false;
                }
                else if (autoNumber.increment == null || autoNumber.increment == "") {
                    $rootScope.showErrorMessage("Please enter Increment value");
                    valid = false;
                }
                return valid;
            }

            function showEditMode(autonumber) {

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
            }

            function hideEditMode(autonumber) {
                if (autonumber.id == null) {
                    var index = vm.autonumbers.indexOf(autonumber);
                    if (index != -1) {
                        vm.autonumbers.splice(index, 1);
                    }
                }
                else {
                    autonumber.editMode = false;
                    loadAutoNumbers();
                    $timeout(function () {
                        autonumber.showValues = true;
                    }, 500);
                }
            }

            function deleteAutonumber(index) {
                ItemService.findItemByAutoNumId(vm.autonumbers[index].id).then(
                    function (data) {
                        var options = {
                            title: 'Delete AutoNumber',
                            message: 'Are you sure you want to delete this AutoNumber?',
                            okButtonClass: 'btn-danger',
                            okButtonText: "Yes",
                            cancelButtonText: "No"
                        };
                        DialogService.confirm(options, function (yes) {
                            if (yes == true) {
                                var auto = vm.autonumbers[index];
                                AutonumberService.deleteAutonumber(auto.id).then(
                                    function (data) {
                                        vm.autonumbers.splice(index, 1);
                                        $rootScope.showSuccessMessage("AutoNumber deleted successfully");
                                    },
                                    function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                    }
                                );
                            }
                        });

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
                    }
                    else {
                        t = "" + (autonumber.start + (i * autonumber.increment));
                    }

                    example += getNumberPart(autonumber.numbers, autonumber.padwith, t);
                    example += t;
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
                        vm.autoNums = data;

                        var autoNumberIds = [];

                        angular.forEach(vm.autoNums, function (autonumber) {
                            autoNumberIds.push(autonumber.id);
                        });

                        if (autoNumberIds.length == vm.autoNums.length) {
                            ItemService.getItemsByAutoNumberIds(autoNumberIds).then(
                                function (data) {
                                    vm.autonumbers = data;
                                    angular.forEach(vm.autonumbers, function (autonumber) {
                                        autonumber.newName = autonumber.name;
                                        autonumber.newDescription = autonumber.description;
                                        autonumber.newNumber = autonumber.number;
                                        autonumber.newStart = autonumber.start;
                                        autonumber.newIncrement = autonumber.increment;
                                        autonumber.newPadwith = autonumber.padwith;
                                        autonumber.newPrefix = autonumber.prefix;
                                        autonumber.newSuffix = autonumber.suffix;
                                    })
                                    addFlags();
                                }
                            )
                        }

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
);