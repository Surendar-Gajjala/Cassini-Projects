/**
 * Created by Nageshreddy on 13-11-2018.
 */
define(
    [
        'app/desktop/modules/settings/settings.module',
        'app/shared/services/core/groupLocationService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives'
    ],
    function (module) {
        module.controller('GroupLocationsController', GroupLocationsController);

        function GroupLocationsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, GroupLocationService) {
            var vm = this;

            vm.groupLocations = [];
            vm.toDeleteValue = null;

            vm.addGroupLocation = addGroupLocation;
            vm.deleteGroupLocation = deleteGroupLocation;
            vm.saveGroupLocation = saveGroupLocation;
            vm.addLocation = addLocation;
            vm.promptDeleteGroupLocation = promptDeleteGroupLocation;
            vm.promptDeleteLocation = promptDeleteLocation;
            vm.deleteLocation = deleteLocation;
            vm.applyChanges = applyChanges;
            vm.cancelChanges = cancelChanges;
            vm.hideMask = hideMask;
            vm.cancelGroupLocationChange = cancelGroupLocationChange;

            function addGroupLocation() {
                var newGroupLocation = {
                    name: 'New Group Location',
                    newName: 'New Group Location',
                    locationObjects: [],
                    locations: [],
                    editTitle: true,
                    showBusy: false
                };

                vm.groupLocations.unshift(newGroupLocation);
            }

            function addLocation(group, index) {
                var location = {
                    name: 'New Location',
                    newName: 'New Location',
                    groupId: group.id,
                    editMode: true,
                    newMode: true,
                    showBusy: false
                };
                group.locationObjects.push(location);
                var body = $("#group-body" + index);
                body.animate({scrollTop: body.height() + 50}, 300);

            }

            function deleteGroupLocation(group) {
                GroupLocationService.deleteGroup(group.id).then(
                    function (data) {
                        vm.groupLocations.remove(group);
                        $rootScope.showSuccessMessage('Group Location deleted successfully');
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            function promptDeleteGroupLocation(index) {
                var width = $('#groupLocation' + index).width();
                var height = $('#groupLocation' + index).height();

                $('#groupLocationMask' + index).width(width + 2);
                $('#groupLocationMask' + index).height(height + 2);
                $('#groupLocationMask' + index).css({display: 'table'});
            }

            function promptDeleteLocation(index) {
                var width = $('#groupLocation' + index).width();
                var height = $('#groupLocation' + index).height();

                $('#groupLocationValueMask' + index).width(width + 2);
                $('#groupLocationValueMask' + index).height(height + 2);
                $('#groupLocationValueMask' + index).css({display: 'table'});
            }

            function applyChanges(group, value) {
                if (value != undefined) {
                    value.editMode = false;
                    value.newMode = false;
                    value.name = value.newName;
                }
                group.locations = [];
                angular.forEach(group.locationObjects, function (obj) {
                    if (obj.newMode != true) {
                        group.locations.push(obj);
                    }
                });

                saveGroupLocation(group);
            }

            function saveGroupLocation(group) {
                var name = group.name;
                group.name = group.newName;
                var promise = null;
                if (group.id == null) {
                    promise = GroupLocationService.createGroup(group);
                }
                else {
                    promise = GroupLocationService.updateGroup(group);
                }

                promise.then(
                    function (data) {
                        group.id = data.id;
                        hideMask(vm.groupLocations.indexOf(group));
                        group.showBusy = false;
                        group.editTitle = false;
                        angular.forEach(data.locations, function (location) {
                            angular.forEach(group.locationObjects, function (step) {
                                if (step.name == location.name) {
                                    step.id = location.id;
                                }
                            });
                        });
                    },
                    function (error) {
                        group.editTitle = true;
                        group.name = name;
                        $rootScope.showErrorMessage(error.message);
                        hideMask(vm.groupLocations.indexOf(group));
                        group.showBusy = false;
                    }
                )
            }

            function cancelChanges(group, value) {
                value.editMode = false;
                location.newName = location.name;
                if (value.newMode == true) {
                    group.locationObjects.remove(value);
                }
            }

            function cancelGroupLocationChange(group) {
                group.newName = group.name;
                if (group.id == null) {
                    var index = vm.groupLocations.indexOf(group);
                    vm.groupLocations.splice(index, 1);
                }
            }

            function hideMask(index) {
                $('#groupLocationMask' + index).hide();
                $('#groupLocationValueMask' + index).hide();
            }

            function loadGroupLocations() {
                GroupLocationService.getGroups().then(
                    function (data) {
                        vm.groupLocations = data;
                        angular.forEach(vm.groupLocations, function (group) {
                            group.newName = group.name;
                            group.editTitle = false;
                            group.showBusy = false;
                            group.locationObjects = [];
                            angular.forEach(group.locations, function (location) {
                                location.newName = location.name;
                                location.editMode = false;
                                location.newMode = false;
                                group.locationObjects.push(location);
                            })
                        });
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            function deleteLocation(group) {
                if (vm.toDeleteValue != null) {
                    group.showBusy = true;
                    group.locationObjects.remove(vm.toDeleteValue);
                    applyChanges(group);
                }
            }


            (function () {
                loadGroupLocations();
            })();
        }
    }
);