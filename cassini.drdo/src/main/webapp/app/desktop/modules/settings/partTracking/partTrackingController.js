/**
 * Created by Nageshreddy on 08-10-2018.
 */
define(
    [
        'app/desktop/modules/settings/settings.module',
        'app/shared/services/core/listService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives'
    ],
    function (module) {
        module.controller('PartTrackingController', PartTrackingController);

        function PartTrackingController($scope, $rootScope, $timeout, $state, $stateParams, $cookies,
                                        ListService, $translate) {
            if ($application.homeLoaded == false) {
                return;
            }
            var vm = this;

            vm.lists = [];
            vm.toDeleteValue = null;

            vm.addPartTracking = addPartTracking;
            vm.deletePartTracking = deletePartTracking;
            vm.savePartTracking = savePartTracking;
            vm.addPartTrackingValue = addPartTrackingValue;
            vm.promptDeletePartTracking = promptDeletePartTracking;
            vm.promptDeletePartTrackingValue = promptDeletePartTrackingValue;
            vm.deletePartTrackingValue = deletePartTrackingValue;
            vm.applyChanges = applyChanges;
            vm.cancelChanges = cancelChanges;
            vm.hideMask = hideMask;
            vm.cancelPartTrackingChange = cancelPartTrackingChange;

            function addPartTracking() {
                var newPartTracking = {
                    name: 'New Part Tracking',
                    newName: 'New Part Tracking',
                    description: "",
                    defaultValue: "",
                    trackingSteps: [],
                    steps: [],
                    editTitle: true,
                    showBusy: false
                };

                vm.lists.push(newPartTracking);
            }

            function addPartTrackingValue(list, index) {
                var partTrackingStep = {
                    serialNo: list.steps.length + 1,
                    status: "New Status",
                    newStatus: "New Status",
                    description: 'New Part Tracking Value',
                    newDescription: 'New Part Tracking Value',
                    percentage: 0,
                    newPercentage: 0,
                    partTracking: list.id,
                    bdl: true,
                    newBdl: true,
                    ssqag: true,
                    newSsqag: true,
                    cas: true,
                    newCas: true,
                    attachment: false,
                    newAttachment: false,
                    scan: false,
                    newScan: false,
                    editMode: true,
                    newMode: true,
                    showBusy: false
                };
                list.steps.push(partTrackingStep);
                var body = $("#list-body" + index);
                body.animate({scrollTop: body.height() + 50}, 300);

            }

            function deletePartTracking(list) {
                ListService.deleteList(list.id).then(
                    function (data) {
                        vm.lists.remove(list);
                        $rootScope.showSuccessMessage('Part Tracking deleted successfully');
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            function promptDeletePartTracking(index) {
                var width = $('#list' + index).width();
                var height = $('#list' + index).height();

                $('#listMask' + index).width(width + 2);
                $('#listMask' + index).height(height + 2);
                $('#listMask' + index).css({display: 'table'});
            }

            function promptDeletePartTrackingValue(index) {
                var width = $('#list' + index).width();
                var height = $('#list' + index).height();

                $('#listValueMask' + index).width(width + 2);
                $('#listValueMask' + index).height(height + 2);
                $('#listValueMask' + index).css({display: 'table'});
            }

            function validate(list, value) {

                var valid = true;
                if (value != undefined) {
                    if (value.newStatus != 'New Status') {
                        angular.forEach(list.steps, function (obj) {
                            if ((obj.id != null && obj.id != undefined) &&
                                (obj.id != value.id && obj.status == value.newStatus)) {
                                $rootScope.showErrorMessage("Status is already exist");
                                valid = false;
                            }
                        });

                        if (value.newPercentage == 0 || value.newPercentage == null || value.newPercentage == undefined) {
                            $rootScope.showErrorMessage("Please enter percentage");
                            valid = false;
                        } else if (value.newPercentage < 0) {
                            $rootScope.showErrorMessage("Please enter valid percentage");
                            valid = false;
                        }
                    } else {
                        valid = false;
                        $rootScope.showErrorMessage("Please enter status");
                    }
                }
                return valid;
            }

            function applyChanges(list, value) {
                if (validate(list, value)) {
                    if (value != undefined) {
                        value.editMode = false;
                        value.description = value.newDescription;
                        value.percentage = value.newPercentage;
                        value.bdl = value.newBdl;
                        value.ssqag = value.newSsqag;
                        value.cas = value.newCas;
                        value.scan = value.newScan;
                        value.attachment = value.newAttachment;
                        value.newMode = false;
                        value.status = value.newStatus;
                    }
                    list.trackingSteps = [];
                    angular.forEach(list.steps, function (obj) {
                        if (obj.newMode != true) {
                            list.trackingSteps.push(obj);
                        }
                    });

                    savePartTracking(list);
                }
            }

            function savePartTracking(list) {
                var name = list.name;
                list.name = list.newName;
                var promise = null;
                if (list.id == null) {
                    promise = ListService.createList(list);
                }
                else {
                    promise = ListService.updateList(list);
                }

                promise.then(
                    function (data) {
                        list.id = data.id;
                        hideMask(vm.lists.indexOf(list));
                        list.showBusy = false;
                        list.editTitle = false;
                        angular.forEach(data.trackingSteps, function (partTracking) {
                            angular.forEach(list.steps, function (step) {
                                if (step.status == partTracking.status) {
                                    step.id = partTracking.id;
                                }
                            });
                        });
                    },
                    function (error) {
                        list.editTitle = true;
                        list.name = name;
                        $rootScope.showErrorMessage(error.message);
                        hideMask(vm.lists.indexOf(list));
                        list.showBusy = false;
                    }
                )
            }

            function cancelChanges(list, value) {
                value.editMode = false;
                value.newDescription = value.description;
                value.newPercentage = value.percentage;
                value.newStatus = value.status;
                value.newBdl = value.bdl;
                value.newSsqag = value.ssqag;
                value.newCas = value.cas;
                value.newScan = value.scan;
                value.newAttachment = value.attachment;
                if (value.newMode == true) {
                    list.steps.remove(value);
                }
            }

            function cancelPartTrackingChange(list) {
                list.newName = list.name;
                if (list.id == null) {
                    var index = vm.lists.indexOf(list);
                    vm.lists.splice(index, 1);
                }
            }

            function hideMask(index) {
                $('#listMask' + index).hide();
                $('#listValueMask' + index).hide();
            }

            function loadPartTrackings() {
                ListService.getLists().then(
                    function (data) {
                        vm.lists = data;
                        angular.forEach(vm.lists, function (list) {
                            list.newName = list.name;
                            list.editTitle = false;
                            list.showBusy = false;
                            list.steps = [];
                            angular.forEach(list.trackingSteps, function (step) {
                                step.newDescription = step.description;
                                step.newPercentage = step.percentage;
                                step.newStatus = step.status;
                                step.newBdl = step.bdl;
                                step.newSsqag = step.ssqag;
                                step.newCas = step.cas;
                                step.newScan = step.scan;
                                step.newAttachment = step.attachment;
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

            function deletePartTrackingValue(list, index) {
                if (vm.toDeleteValue != null) {
                    list.showBusy = true;
                    list.steps.remove(vm.toDeleteValue);
                    applyChanges(list);
                    $('#listValueMask' + index).css({display: 'none'});
                }
            }

            (function () {
                loadPartTrackings();
            })();
        }
    }
);