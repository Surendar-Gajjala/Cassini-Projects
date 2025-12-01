define(['app/assets/bower_components/cassini-platform/app/desktop/modules/common/common.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'split-pane',
        'jquery.easyui',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective'
    ],
    function (module) {
        module.controller('AutonumbersController', AutonumbersController);

        function AutonumbersController($q, $scope, $rootScope, $timeout, $state, DialogService, $stateParams, $cookies,
                                       CommonService, AutonumberService, $translate) {
            var vm = this;
            var parsed = angular.element("<div></div>");

            $rootScope.autonumbers = [];

            vm.addAutonumber = addAutonumber;
            vm.acceptChanges = acceptChanges;
            vm.showEditMode = showEditMode;
            vm.hideEditMode = hideEditMode;
            vm.deleteAutoNumber = deleteAutoNumber;
            vm.getAutonumberExamples = getAutonumberExamples;
            $rootScope.loadAutoNumbers = loadAutoNumbers;
            vm.pageSize = pageSize;
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
                $rootScope.autonumbers.content.unshift(autoNo);
            }


            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "id",
                    order: "DESC"
                }
            };

            function pageSize(page) {
                vm.pageable.page = 0;
                vm.pageable.size = page;
                loadAutoNumbers();
            }

            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: vm.pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };

            $rootScope.autonumbers = angular.copy(pagedResults);
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            function nextPage() {
                if ($rootScope.autonumbers.last != true) {
                    vm.pageable.page++;
                    loadAutoNumbers();
                }
            }

            function previousPage() {
                if ($rootScope.autonumbers.first != true) {
                    vm.pageable.page--;
                    loadAutoNumbers();
                }
            }

            var autonumberCreate = parsed.html($translate.instant("AUTONUMBER_CREATE_MSG")).html();
            var autonumberUpdate = parsed.html($translate.instant("AUTONUMBER_UPDATE_MSG")).html();
            var autonumberNameError = parsed.html($translate.instant("AUTONUMBER_NAME_ERROR_MSG")).html();
            var autonumberPrefixError = parsed.html($translate.instant("AUTONUMBER_PREFIX_ERROR_MSG")).html();
            vm.deleteTitle = parsed.html($translate.instant("DELETE")).html();
            vm.cancelTitle = parsed.html($translate.instant("CANCEL")).html();
            vm.saveTitle = parsed.html($translate.instant("SAVE")).html();
            vm.editTitle = parsed.html($translate.instant("EDIT")).html();

            function acceptChanges(autonumber) {
                if (autonumber.newName != null && autonumber.newName != "") {
                    autonumber.name = autonumber.newName;
                }
                autonumber.description = autonumber.newDescription;
                autonumber.numbers = autonumber.newNumber;
                if (autonumber.newStart > 0) {
                    autonumber.start = autonumber.newStart;
                }
                if (autonumber.newIncrement > 0) {
                    autonumber.increment = autonumber.newIncrement;
                }
                autonumber.padwith = autonumber.newPadwith;
                if (autonumber.newPrefix != null) {
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
                                /* AutonumberService.getAutonumberPrefix(autonumber.prefix).then(
                                 function (data) {
                                 vm.autonumberPrefix = data;
                                 if (vm.autonumberPrefix == "") {*/
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
                                            $rootScope.showSuccessMessage(autonumberCreate);
                                            loadAutoNumbers();
                                        },
                                        function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    )
                                } else {
                                    $rootScope.showWarningMessage(autonumber.name + ":" + autonumberNameError);
                                    autonumber.showValues = false;

                                }
                                /*    } else {
                                 $rootScope.showWarningMessage(autonumber.prefix + ":" + autonumberPrefixError);
                                 autonumber.showValues = false;
                                 }
                                 }
                                 )*/
                            }
                        )
                    }
                }
                else {
                    if (validateAutoNumber(autonumber)) {
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
                                $rootScope.showSuccessMessage(autonumberUpdate);
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                            })
                    }
                }
            }


            var nameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            var descriptionValidation = parsed.html($translate.instant("DESCRIPTION_VALIDATION")).html();
            var prefixVaildation = parsed.html($translate.instant("PREFIX_VALIDATION")).html();
            var deleteTitle = parsed.html($translate.instant("AUTONUMBER_TITLE_DELETE")).html();
            var deleteTitleMessage = parsed.html($translate.instant("AUTONUMBER_TITLE_MES_DELETE")).html();
            var deleteMessage = parsed.html($translate.instant("AUTONUMBER_DELETE_MESSAGE")).html();
            var numberValidation = parsed.html($translate.instant("NUMBER_VALIDATION")).html();
            var startVaildation = parsed.html($translate.instant("START_VALIDATION")).html();
            var incrementVaildation = parsed.html($translate.instant("INCREMENT_VALIDATION")).html();
            var positiveNumberValidation = parsed.html($translate.instant("POSITIVE_NUMBER_VALIDATION")).html();

            function validateAutoNumber(autoNo) {
                var valid = true;
                if (autoNo.name == null || autoNo.name == "") {
                    $rootScope.showWarningMessage(nameValidation);
                    valid = false;
                } else if (autoNo.numbers == null || autoNo.numbers == "") {
                    $rootScope.showWarningMessage(numberValidation);
                    valid = false;
                } else if (autoNo.numbers <= 0) {
                    $rootScope.showWarningMessage(positiveNumberValidation);
                    valid = false;
                } else if (autoNo.start == null || autoNo.start == "") {
                    $rootScope.showWarningMessage(startVaildation);
                    valid = false;
                } else if (autoNo.increment == null || autoNo.increment == "") {
                    $rootScope.showWarningMessage(incrementVaildation);
                    valid = false;
                }
                /* else if (autoNo.prefix == null || autoNo.prefix == "") {
                 $rootScope.showWarningMessage(prefixVaildation);
                 valid = false;
                 }*/
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
                    var index = $rootScope.autonumbers.content.indexOf(autonumber);
                    if (index != -1) {
                        $rootScope.autonumbers.content.splice(index, 1);
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

            function deleteAutoNumber(autoNumber) {
                $scope.$emit('app.autoNumber.delete', autoNumber);
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

            vm.filters = {
                searchQuery: null
            };

            function loadAutoNumbers() {
                AutonumberService.getAllAutonumbers(vm.pageable, vm.filters).then(
                    function (data) {
                        $rootScope.autonumbers = data;
                        angular.forEach($rootScope.autonumbers.content, function (autonumber) {
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
                        resizeScreen();
                        $rootScope.hideBusyIndicator();
                    },
                    function (error) {

                    }
                );
            }

            function addFlags() {
                angular.forEach($rootScope.autonumbers.content, function (autonumber) {
                    autonumber.editMode = false;
                    autonumber.showValues = true;
                });
            }

            function resizeScreen() {
                $timeout(function () {
                    var viewContent = $('.view-content').outerHeight();
                    var headerHeight = $('.autoNumber-height').outerHeight();
                    $('.headerSticky').height(viewContent - (headerHeight + 10));
                }, 500);
            }

            $rootScope.freeTextQuery = null;
            vm.freeTextSearch = freeTextSearch;
            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.freeTextQuery = freeText;
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.filters.searchQuery = freeText;
                    loadAutoNumbers();
                } else {
                    resetPage();
                }
            }

            vm.resetPage = resetPage;
            function resetPage() {
                $rootScope.autonumbers = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                $rootScope.freeTextQuery = null;
                loadAutoNumbers();
            }

            (function () {
                loadAutoNumbers();
                $(window).resize(function () {
                    resizeScreen();
                });
                $scope.$on("settings.new.autonumber", function (evnt, args) {
                    addAutonumber();
                });
            })();
        }
    }
);