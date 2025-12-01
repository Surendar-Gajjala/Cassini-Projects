define(
    [
        'app/desktop/modules/customObject/customObject.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/customObjectService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('CustomObjectRelatedController', CustomObjectRelatedController);

        function CustomObjectRelatedController($scope, $rootScope, $timeout, $state, $stateParams, $q, $translate, $application,
                                               CustomObjectService, DialogService) {
            var vm = this;
            vm.customId = $stateParams.customId;
            var parsed = angular.element("<div></div>");

            vm.addRelatedObjects = addRelatedObjects;
            vm.cancelChanges = cancelChanges;
            vm.onCancel = onCancel;
            vm.saveAll = saveAll;
            vm.removeAll = removeAll;
            vm.selectedRelatedObjects = [];
            var relatedObjectTitleMsg = parsed.html($translate.instant("RELATED_OBJECTS_ADD_TITLE_MSG")).html();
            var add = parsed.html($translate.instant("ADD")).html();
            function addRelatedObjects() {
                var options = {
                    title: relatedObjectTitleMsg,
                    template: 'app/desktop/modules/customObject/details/tabs/bom/customObjectSelectionView.jsp',
                    controller: 'CustomObjectSelectionController as customObjectSelectionVm',
                    resolve: 'app/desktop/modules/customObject/details/tabs/bom/customObjectSelectionController',
                    data: {
                        selectedRelated: true
                    },
                    width: 700,
                    showMask: true,
                    buttons: [
                        {text: add, broadcast: 'add.customobject.bom.objects'}
                    ],
                    callback: function (result) {
                        vm.objects = result;
                        vm.itemFlag = true;
                        var selectCustomRelatedObject = {
                            parent: vm.customId,
                            related: null,
                            notes: null
                        };
                        angular.forEach(vm.objects, function (relatedObject) {
                            var customRelated = angular.copy(selectCustomRelatedObject);
                            customRelated.editMode = true;
                            customRelated.related = relatedObject;
                            vm.selectedRelatedObjects.unshift(customRelated);
                            vm.relatedCustomObjects.unshift(customRelated);
                        });
                        $rootScope.hideSidePanel();
                    }
                };
                $rootScope.showSidePanel(options);
            }


            vm.onCancel = onCancel;
            function onCancel(item) {
                vm.relatedCustomObjects.splice(vm.relatedCustomObjects.indexOf(item), 1);
                vm.selectedRelatedObjects.splice(vm.selectedRelatedObjects.indexOf(item), 1);
            }


            var relatedObjectsAddedMsg = parsed.html($translate.instant("RELATED_OBJECTS_ADDED_MSG")).html();
            var relatedObjectAddedMsg = parsed.html($translate.instant("RELATED_OBJECT_ADDED_MSG")).html();
            var relatedObjectsUpdatedMsg = parsed.html($translate.instant("RELATED_OBJECTS_UPDATE_MSG")).html();
            vm.createRelatedCustomObject = createRelatedCustomObject;
            function createRelatedCustomObject(object) {
                $rootScope.showBusyIndicator($('.view-container'));
                if (object.id == null) {
                    CustomObjectService.createRelatedCustomObject(vm.customId, object).then(
                        function (data) {
                            object.id = data.id;
                            object.editMode = false;
                            vm.selectedRelatedObjects.splice(vm.selectedRelatedObjects.indexOf(object), 1);
                            $rootScope.showSuccessMessage(relatedObjectAddedMsg);
                            $rootScope.hideBusyIndicator();
                            $rootScope.loadCustomObjectTabCount();
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    CustomObjectService.updateRelatedCustomObject(vm.customId, object).then(
                        function (data) {
                            object.id = data.id;
                            object.editMode = false;
                            $rootScope.hideBusyIndicator();
                            $rootScope.showSuccessMessage(relatedObjectsUpdatedMsg);
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function saveAll() {
                $rootScope.showBusyIndicator($('.view-container'));
                CustomObjectService.createRelatedMultipleCustomObject(vm.customId, vm.selectedRelatedObjects).then(
                    function (data) {
                        loadRelatedCustomObject();
                        vm.selectedRelatedObjects = [];
                        $rootScope.showSuccessMessage(relatedObjectsAddedMsg);
                        $rootScope.hideBusyIndicator();
                        $rootScope.loadCustomObjectTabCount();
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function removeAll() {
                angular.forEach(vm.selectedRelatedObjects, function (part) {
                    vm.relatedCustomObjects.splice(vm.relatedCustomObjects.indexOf(part), 1);
                });
                vm.selectedRelatedObjects = [];
            }

            vm.editRelatedCustomObject = editRelatedCustomObject;
            function editRelatedCustomObject(object) {
                vm.itemFlag = false;
                object.editMode = true;
                $scope.notes = object.notes;
            }

            vm.cancelChanges = cancelChanges;
            function cancelChanges(object) {
                object.editMode = false;
                object.notes = $scope.notes;
            }

            var deleteRelatedCustomObjectDialogTitle = parsed.html($translate.instant("DELETE_RELATED_CUSTOM_DIALOG_TITLE")).html();
            var deleteRelatedCustomObjectDialogMessage = parsed.html($translate.instant("DELETE_RELATED_CUSTOM_DIALOG_MESSAGE")).html();
            var deleteCustomRelatedObjectMessage = parsed.html($translate.instant("DELETE_RELATED_CUSTOM_MESSAGE")).html();
            var itemDelete = parsed.html($translate.instant("ITEMDELETE")).html();

            vm.deleteCustomObject = deleteCustomObject;
            function deleteCustomObject(object) {
                var options = {
                    title: deleteRelatedCustomObjectDialogTitle,
                    message: deleteRelatedCustomObjectDialogMessage + " [ " + object.related.number + " ] " + itemDelete + "?",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        CustomObjectService.deleteRelatedCustomObject(vm.customId, object.id).then(
                            function (data) {
                                loadRelatedCustomObject();
                                var index = vm.relatedCustomObjects.indexOf(object);
                                vm.relatedCustomObjects.splice(index, 1);
                                $rootScope.showSuccessMessage(deleteCustomRelatedObjectMessage);
                                $rootScope.loadCustomObjectTabCount();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                });
            }

            vm.showCustomObject = showCustomObject;

            function showCustomObject(object) {
                $state.go('app.customobjects.details', {customId: object.related.id, tab: 'details.basic'});
            }


            function loadRelatedCustomObject() {
                $rootScope.showBusyIndicator($('.view-container'));
                vm.loading = true;
                CustomObjectService.getAllRelatedCustomObjects(vm.customId).then(
                    function (data) {
                        vm.relatedCustomObjects = data;
                        angular.forEach(vm.relatedCustomObjects, function (customRelated) {
                            customRelated.editMode = false;
                        });
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                $scope.$on('app.customObj.tabActivated', function (event, data) {
                    if (data.tabId == 'details.related') {
                        loadRelatedCustomObject();
                    }
                });
            })();
        }
    }
);
