/**
 * Created by Nageshreddy on 10-04-2018.
 */
define(
    [
        'app/desktop/modules/settings/settings.module',
        'app/shared/services/core/failureListService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives'
    ],
    function (module) {
        module.controller('FailureListsController', FailureListsController);

        function FailureListsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                        FailureListService, $translate) {
            if ($application.homeLoaded == false) {
                return;
            }
            var vm = this;

            vm.fLists = [];
            vm.toDeleteValue = null;

            vm.addFailureList = addFailureList;
            vm.deleteFailureList = deleteFailureList;
            vm.saveFailureList = saveFailureList;
            vm.addFailureListValue = addFailureListValue;
            vm.promptDeleteFailureList = promptDeleteFailureList;
            vm.promptDeleteFailureListValue = promptDeleteFailureListValue;
            vm.deleteFailureListValue = deleteFailureListValue;
            vm.applyChanges = applyChanges;
            vm.cancelChanges = cancelChanges;
            vm.hideMask = hideMask;
            vm.cancelFailureListChange = cancelFailureListChange;
            vm.dataTypes = [
                'STRING',
                'INTEGER',
                'DATE',
                'LIST',
                'TEXT',
                'BOOLEAN',
                'FILES'
            ];

            function addFailureList() {
                var newFailureList = {
                    name: 'New Failure List',
                    newName: 'New Failure List',
                    description: "",
                    defaultValue: "",
                    failureSteps: [],
                    steps: [],
                    editTitle: true,
                    showBusy: false
                };

                vm.fLists.push(newFailureList);
            }

            function addFailureListValue(list, index) {
                var failureStep = {
                    serialNo: list.steps.length + 1,
                    status: "New Status",
                    newStatus: "New Status",
                    dataType: "",
                    newDataType: "",
                    failureList: list.id,
                    editMode: true,
                    newMode: true,
                    showBusy: false
                };
                list.steps.push(failureStep);
                var body = $("#list-body" + index);
                body.animate({scrollTop: body.height() + 50}, 300);

            }

            function deleteFailureList(list) {
                FailureListService.deleteList(list.id).then(
                    function (data) {
                        vm.fLists.remove(list);
                        $rootScope.showSuccessMessage('Failure list deleted successfully');
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            function promptDeleteFailureList(index) {
                var width = $('#list' + index).width();
                var height = $('#list' + index).height();

                $('#listMask' + index).width(width + 2);
                $('#listMask' + index).height(height + 2);
                $('#listMask' + index).css({display: 'table'});
            }

            function promptDeleteFailureListValue(index) {
                var width = $('#list' + index).width();
                var height = $('#list' + index).height();

                $('#listValueMask' + index).width(width + 2);
                $('#listValueMask' + index).height(height + 2);
                $('#listValueMask' + index).css({display: 'table'});
            }

            function applyChanges(list, value) {
                if (validateValue(value)) {
                    list.failureSteps = [];
                    angular.forEach(list.steps, function (obj) {
                        if (obj.newMode != true) {
                            list.failureSteps.push(obj);
                        }
                    });

                    saveFailureList(list);
                }
            }

            function validateValue(value) {
                var valid = true;
                if (value != undefined) {
                    if (value.newDataType == undefined || value.newDataType == "" || value.newDataType == null) {
                        valid = false;
                        $rootScope.showErrorMessage("Please select dataType");
                    }
                    if (value.newStatus == undefined || value.newStatus == "" || value.newStatus == null) {
                        valid = false;
                        $rootScope.showErrorMessage("Please enter Status");
                    }
                    if (valid) {
                        value.editMode = false;
                        value.newMode = false;
                        value.dataType = value.newDataType;
                        value.status = value.newStatus;
                    }
                }
                return valid;
            }

            function saveFailureList(list) {
                var name = list.name;
                list.name = list.newName;
                var promise = null;
                if (list.id == null) {
                    promise = FailureListService.createList(list);
                }
                else {
                    promise = FailureListService.updateList(list);
                }

                promise.then(
                    function (data) {
                        list.id = data.id;
                        hideMask(vm.fLists.indexOf(list));
                        list.showBusy = false;
                        list.editTitle = false;
                        angular.forEach(data.failureSteps, function (failureList) {
                            angular.forEach(list.steps, function (step) {
                                if (step.status == failureList.status) {
                                    step.id = failureList.id;
                                }
                            });
                        });
                    },
                    function (error) {
                        list.editTitle = true;
                        list.name = name;
                        $rootScope.showErrorMessage(error.message);
                        hideMask(vm.fLists.indexOf(list));
                        list.showBusy = false;
                    }
                )
            }

            function cancelChanges(list, value) {
                value.editMode = false;
                value.newStatus = value.status;
                if (value.newMode == true) {
                    list.steps.remove(value);
                }
            }

            function cancelFailureListChange(list) {
                list.newName = list.name;
                if (list.id == null) {
                    var index = vm.fLists.indexOf(list);
                    vm.fLists.splice(index, 1);
                }
            }

            function hideMask(index) {
                $('#listMask' + index).hide();
                $('#listValueMask' + index).hide();
            }

            function loadFailurelists() {
                FailureListService.getLists().then(
                    function (data) {
                        vm.fLists = data;
                        angular.forEach(vm.fLists, function (list) {
                            list.newName = list.name;
                            list.editTitle = false;
                            list.showBusy = false;
                            list.steps = [];
                            angular.forEach(list.failureSteps, function (step) {
                                step.newStatus = step.status;
                                step.newDataType = step.dataType;
                                step.editMode = false;
                                step.newMode = false;
                                list.steps.push(step);
                            })
                        });
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            function deleteFailureListValue(list) {
                if (vm.toDeleteValue != null) {
                    list.showBusy = true;
                    list.steps.remove(vm.toDeleteValue);
                    applyChanges(list);
                }
            }

            (function () {
                loadFailurelists();
            })();
        }
    }
);