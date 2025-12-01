define(['app/desktop/modules/settings/settings.module',
        'split-pane',
        'jquery.easyui',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/bomGroupService'
    ],
    function (module) {
        module.controller('BomGroupController', BomGroupController);

        function BomGroupController($q, $scope, $rootScope, $timeout, $state, DialogService, $stateParams, $cookies,
                                    CommonService, BomGroupService) {
            var vm = this;

            vm.bomGroups = [];

            vm.addBomGroupType = addBomGroupType;
            vm.acceptChanges = acceptChanges;
            vm.showEditMode = showEditMode;
            vm.hideEditMode = hideEditMode;
            vm.deleteBomGroupType = deleteBomGroupType;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;

            vm.bomGroupTypes = ['SECTION', 'SUBSYSTEM', 'UNIT'];

            var pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };

            vm.bomGroups = angular.copy(pagedResults);

            var addBomGroup = {
                id: null,
                name: null,
                code: null,
                type: null,
                versity: false,
                newName: null,
                newCode: null,
                newType: null,
                newVersity: false,
                editMode: true
            };

            function addBomGroupType() {
                var bomGroup = angular.copy(addBomGroup);
                vm.bomGroups.content.unshift(bomGroup);
            }

            function acceptChanges(bomGroup) {
                if (bomGroup.newType != null && bomGroup.newType != "") {
                    bomGroup.type = bomGroup.newType;
                }
                if (bomGroup.newName != null && bomGroup.newName != "") {
                    bomGroup.name = bomGroup.newName;
                }
                if (bomGroup.newCode != null && bomGroup.newCode != "") {
                    bomGroup.code = bomGroup.newCode.toUpperCase();
                }

                bomGroup.versity = bomGroup.newVersity;

                if (validateBomGroup(bomGroup)) {
                    if (bomGroup.id == null || bomGroup.id == undefined) {
                        BomGroupService.createBomGroup(bomGroup).then(
                            function (data) {
                                bomGroup.id = data.id;
                                bomGroup.editMode = false;
                                vm.bomGroups.numberOfElements++;
                                vm.bomGroups.totalElements++;
                                $rootScope.showSuccessMessage("Type created successfully");
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    } else {
                        BomGroupService.updateBomGroup(bomGroup).then(
                            function (data) {
                                bomGroup.editMode = false;
                                $rootScope.showSuccessMessage("Type updated successfully");
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                }
            }

            function validateBomGroup(bomGroup) {
                var valid = true;
                if (bomGroup.type == null || bomGroup.type == "" || bomGroup.type == undefined) {
                    $rootScope.showWarningMessage("Please select Type");
                    valid = false;
                } else if (bomGroup.name == null || bomGroup.name == "" || bomGroup.name == undefined) {
                    $rootScope.showWarningMessage("Please enter Name");
                    valid = false;
                } else if (bomGroup.code == null || bomGroup.code == "" || bomGroup.code == undefined) {
                    $rootScope.showWarningMessage("Please enter Code");
                    valid = false;
                } else if (bomGroup.code != null && !validCodeLength(bomGroup)) {
                    valid = false;
                }
                return valid;
            }

            function validCodeLength(bomGroup) {
                var valid = true;

                if (bomGroup.type == "SECTION" || bomGroup.type == "SUBSYSTEM") {
                    if (bomGroup.code.length != 1) {
                        valid = false;
                        $rootScope.showWarningMessage(bomGroup.type + " Code accepts 1 character (or) digit only")
                    }
                } else if (bomGroup.type == "UNIT") {
                    if (bomGroup.code.length != 2) {
                        valid = false;
                        $rootScope.showWarningMessage(bomGroup.type + " Code accepts 2 characters (or) digits only")
                    }
                }

                return valid;
            }

            function showEditMode(bomGroup) {

                BomGroupService.findItemsByBomGroupId(bomGroup.id).then(
                    function (data) {
                        if (data.length == 0) {
                            bomGroup.newName = bomGroup.name;
                            bomGroup.newCode = bomGroup.code;
                            bomGroup.newType = bomGroup.type;
                            bomGroup.newVersity = bomGroup.versity;
                            bomGroup.editMode = true;
                        } else {
                            $rootScope.showWarningMessage("This Type has items, Cannot edit this Type");
                        }
                    }
                )
            }

            function hideEditMode(bomGroup) {
                if (bomGroup.id == null) {
                    var index = vm.bomGroups.content.indexOf(bomGroup);
                    if (index != -1) {
                        vm.bomGroups.content.splice(index, 1);
                    }
                }
                else {
                    BomGroupService.getBomGroup(bomGroup).then(
                        function (data) {
                            bomGroup.name = data.name;
                            bomGroup.code = data.code;
                            bomGroup.type = data.type;
                            bomGroup.versity = data.versity;
                            bomGroup.editMode = false;
                        }
                    );
                }
            }

            function deleteBomGroupType(bomGroup) {
                BomGroupService.findItemsByBomGroupId(bomGroup.id).then(
                    function (data) {
                        if (data.length == 0) {
                            var options = {
                                title: "Delete Type",
                                message: "Please confirm to delete this Type",
                                okButtonClass: 'btn-danger',
                                okButtonText: "Yes",
                                cancelButtonText: "No"
                            };
                            DialogService.confirm(options, function (yes) {
                                if (yes == true) {
                                    BomGroupService.deleteBomGroup(bomGroup.id).then(
                                        function (data) {
                                            var index = vm.bomGroups.content.indexOf(bomGroup);
                                            vm.bomGroups.content.splice(index, 1);
                                            vm.bomGroups.numberOfElements--;
                                            vm.bomGroups.totalElements--;
                                            $rootScope.showSuccessMessage("Type deleted successfully");
                                        },
                                        function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    );
                                }
                            });
                        } else {
                            $rootScope.showWarningMessage("This Type has items, Cannot delete this Type");
                        }
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function nextPage() {
                pageable.page++;
                loadBomGroups();
            }

            function previousPage() {
                pageable.page--;
                loadBomGroups();
            }

            function loadBomGroups() {
                vm.loading = false;
                BomGroupService.getAllBomGroups(pageable).then(
                    function (data) {
                        vm.bomGroups = data;
                    }
                )
            }

            (function () {
                loadBomGroups();
            })();
        }
    }
)
;