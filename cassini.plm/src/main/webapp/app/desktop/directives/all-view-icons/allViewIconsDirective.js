define(
    [
        'app/desktop/modules/item/item.module',
        'app/shared/services/core/itemFileService',
        'app/shared/services/core/objectFileService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/customObjectFilesService'

    ],
    function (module) {
        module.directive('allViewIcons', AllViewIconsDirective);

        function AllViewIconsDirective($rootScope, $state, $timeout, $translate, ItemFileService, CustomObjectFileService, CommonService, ObjectFileService) {

            return {
                templateUrl: 'app/desktop/directives/all-view-icons/allViewIconsDirective.jsp',
                restrict: 'E',
                scope: {
                    object: "="
                },

                link: function ($scope, element, attrs) {
                    var parsed = angular.element("<div></div>");
                    $scope.showFileTitle = parsed.html($translate.instant("SHOW_FILE_TITLE")).html();
                    $scope.showObjectFileTitle = parsed.html($translate.instant("SHOW_OBJECT_FILE_TITLE")).html();
                    $scope.objectHasFiles = parsed.html($translate.instant("OBJECT_HAS_FILES")).html();
                    $scope.showBomTitle = parsed.html($translate.instant("SHOW_BOM_TITLE")).html();
                    $scope.objectHasBom = parsed.html($translate.instant("OBJECT_HAS_BOM")).html();
                    $scope.showPartTitle = parsed.html($translate.instant("SHOW_PART_TITLE")).html();
                    $scope.showChangesTitle = parsed.html($translate.instant("SHOW_CHANGES_TITLE")).html();
                    $scope.showVarianceTitle = parsed.html($translate.instant("SHOW_VARIANCE_TITLE")).html();
                    $scope.showQualityTitle = parsed.html($translate.instant("SHOW_QUALITY_TITLE")).html();
                    $scope.configurableItem = parsed.html($translate.instant("CONFIGURABLE_ITEM")).html();
                    $scope.configuredItem = parsed.html($translate.instant("CONFIGURED_ITEM")).html();
                    $scope.showSubscribers = parsed.html($translate.instant("CLICK_TO_SHOW_SUBSCRIBERS")).html();
                    $scope.hasAlternateParts = parsed.html($translate.instant("HAS_ALTERNATE_PARTS")).html();
                    $scope.showInspectionReports = parsed.html($translate.instant("CLICK_TO_SHOW_INSPECTIONREPORTS")).html();
                    $scope.showMbomFiles = parsed.html($translate.instant("CLICK_TO_SHOW_MBOMFILES")).html();
                    $scope.showBopFiles = parsed.html($translate.instant("CLICK_TO_SHOW_BOPFILES")).html();
                    $scope.showOperationFiles = parsed.html($translate.instant("CLICK_TO_SHOW_OPERATION_FILES")).html();
                    $scope.showShiftFiles = parsed.html($translate.instant("CLICK_TO_SHOW_SHIFT_FILES")).html();
                    $scope.showMaterialFiles = parsed.html($translate.instant("CLICK_TO_SHOW_MATERIAL_FILES")).html();
                    $scope.showJigFixtureFiles = parsed.html($translate.instant("CLICK_TO_SHOW_JIGFIXTURE_FILES")).html();
                    $scope.showToolFiles = parsed.html($translate.instant("CLICK_TO_SHOW_TOOL_FILES")).html();
                    $scope.showInstrumentFiles = parsed.html($translate.instant("CLICK_TO_SHOW_INSTRUMENT_FILES")).html();
                    $scope.showEquipmentFiles = parsed.html($translate.instant("CLICK_TO_SHOW_EQUIPMENT_FILES")).html();
                    $scope.showMachineFiles = parsed.html($translate.instant("CLICK_TO_SHOW_MACHINE_FILES")).html();
                    $scope.showWorkCenterFiles = parsed.html($translate.instant("CLICK_TO_SHOW_WORKCENTER_FILES")).html();
                    $scope.showAssemblyLineFiles = parsed.html($translate.instant("CLICK_TO_SHOW_ASSEMBLYLINE_FILES")).html();
                    $scope.showPlantFiles = parsed.html($translate.instant("CLICK_TO_SHOW_PLANT_FILES")).html();
        
                    $scope.itemFilePopover = {
                        templateUrl: 'app/desktop/modules/item/all/itemFilePopoverTemplate.jsp'
                    };

                    $scope.subscribesPopover = {
                        templateUrl: 'app/desktop/modules/item/all/subscribesPopoverTemplate.jsp'
                    };
                    $scope.mfrPartsInspectionReportPopover = {
                        templateUrl: 'app/desktop/modules/mfr/mfrparts/all/mfrPartsInspectionReportPopoverTemplate.jsp'
                    };

                    $scope.mbomFilesPopover = {
                        templateUrl: 'app/desktop/modules/mes/mbom/all/mbomFilePopoverTemplate.jsp'
                    };

                    $scope.bopFilePopover = {
                        templateUrl: 'app/desktop/modules/mes/bop/all/bopFileTemplatePopover.jsp'
                    };

                    $scope.operationFilePopover = {
                        templateUrl: 'app/desktop/modules/mes/operations/all/operationFilePopoverTemplate.jsp'
                    };
                    $scope.shiftFilePopover = {
                        templateUrl: 'app/desktop/modules/mes/shift/all/shiftFilePopoverTemplate.jsp'
                    };
                    $scope.materialFilePopover = {
                        templateUrl: 'app/desktop/modules/mes/material/all/materialFilePopoverTemplate.jsp'
                    };
                    $scope.jigFixtureFilePopover = {
                        templateUrl: 'app/desktop/modules/mes/jigsAndFixtures/all/jigFixtureFilePopoverTemplate.jsp'
                    };
                    $scope.plantFilePopover = {
                        templateUrl: 'app/desktop/modules/mes/plant/all/plantFilePopoverTempalate.jsp'
                    };
                    $scope.toolFilePopover = {
                        templateUrl: 'app/desktop/modules/mes/tool/all/toolFilePopoverTemplate.jsp'
                    };
                    $scope.instrumentFilePopover = {
                        templateUrl: 'app/desktop/modules/mes/instrument/all/instrumentFilePopoverTemplate.jsp'
                    };
                    $scope.equipmentFilePopover = {
                        templateUrl: 'app/desktop/modules/mes/equipment/all/equipmentFilePopoverTemplate.jsp'
                    };
                    $scope.machineFilePopover = {
                        templateUrl: 'app/desktop/modules/mes/machine/all/machineFilePopoverTemplate.jsp'
                    };
                    $scope.workCenterFilePopover = {
                        templateUrl: 'app/desktop/modules/mes/workCenter/all/workcenterFilePopoverTemplate.jsp'
                    };
                    $scope.assemblyLineFilePopover = {
                        templateUrl: 'app/desktop/modules/mes/assemblyLine/all/assemblyLineFilePopoverTemplate.jsp'
                    };
                


                   /* $scope.mbomFilesPopover = {
                        templateUrl: 'app/desktop/modules/mes/bop/all/bopFilePopoverTemplate.jsp'
                    };*/

                    if ($scope.object != null && $scope.object != "") {
                        angular.forEach($scope.object.itemFiles, function (obj) {
                            obj.level = 0;
                            obj.expanded = false;
                            obj.folderChildren = [];
                            obj.parentFolder = null;
                        });
                        angular.forEach($scope.object.mbomFiles, function (obj) {
                            obj.level = 0;
                            obj.expanded = false;
                            obj.folderChildren = [];
                            obj.parentFolder = null;
                        });
                    }
                    $scope.showItemBom = showItemBom;
                    function showItemBom(item) {
                        if (item.objectType == "ITEM") {
                            $state.go('app.items.details', {itemId: item.latestRevision, tab: 'details.bom'});
                        } else if (item.objectType == "CUSTOMOBJECT") {
                            $state.go('app.customobjects.details', {customId: item.id, tab: 'details.bom'});
                        }
                    }

                    $scope.showItemChanges = showItemChanges;
                    function showItemChanges(item) {
                        $state.go('app.items.details', {itemId: item.latestRevision, tab: 'details.changes'});
                    }

                    $scope.showItemVariance = showItemVariance;
                    function showItemVariance(item) {
                        $state.go('app.items.details', {itemId: item.latestRevision, tab: 'details.variance'});
                    }

                    $scope.showItemQuality = showItemQuality;
                    function showItemQuality(item) {
                        $state.go('app.items.details', {itemId: item.latestRevision, tab: 'details.quality'});
                    }

                    $scope.showItemMfrParts = showItemMfrParts;
                    function showItemMfrParts(item) {
                        $state.go('app.items.details', {itemId: item.latestRevision, tab: 'details.mfr'});
                    }

                    $scope.showItemRelatedItems = showItemRelatedItems;
                    function showItemRelatedItems(item) {
                        $state.go('app.items.details', {itemId: item.latestRevision, tab: 'details.relatedItems'});
                    }

                    $scope.filePopover = filePopover;
                    function filePopover(item) {
                        item.openPopover = true;
                    }

                    $scope.subscribePopover = subscribePopover;
                    function subscribePopover(item) {
                        item.openPopover = true;
                    }

                    $scope.downloadFile = downloadFile;
                    function downloadFile(file) {
                        if (file.objectType == 'MFRPARTINSPECTIONREPORT') {

                            ObjectFileService.updateObjectFileDownloadHistory(file.object, file.objectType, file.id).then(
                                function (data) {
                                    var url = "{0}//{1}/api/plm/objects/{2}/{3}/files/{4}/download".
                                        format(window.location.protocol, window.location.host,
                                        file.object, file.objectType, file.id);

                                    $rootScope.downloadFileFromIframe(url);
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                        else if (file.objectType == 'FILE'  && file.parentObject == 'MBOM') {
                            var objectId = null;
                            if(file.parentFolder != null){
                               objectId = file.parentFolder.object;
                            } else{
                                objectId = file.object; 
                            }
                            ObjectFileService.updateObjectFileDownloadHistory(objectId, file.objectType, file.id).then(
                                function (data) {
                                    var url = "{0}//{1}/api/plm/objects/{2}/{3}/files/{4}/download".
                                        format(window.location.protocol, window.location.host,
                                            objectId, "MBOMREVISION", file.id);

                                    $rootScope.downloadFileFromIframe(url);
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                        else  if (file.objectType == 'FILE' && file.parentObject == 'BOPREVISION') {
                            ObjectFileService.updateObjectFileDownloadHistory(file.object, file.parentObject, file.id).then(
                                function (data) {
                                    var url = "{0}//{1}/api/plm/objects/{2}/{3}/files/{4}/download".
                                        format(window.location.protocol, window.location.host,
                                        file.object, file.parentObject, file.id);

                                    $rootScope.downloadFileFromIframe(url);
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }

                        else  if (file.objectType == 'FILE' && file.parentObject == 'MESOBJECT') { 

                            ObjectFileService.updateObjectFileDownloadHistory(file.object, file.parentObject, file.id).then(
                                function (data) {
                                    var url = "{0}//{1}/api/plm/objects/{2}/{3}/files/{4}/download".
                                        format(window.location.protocol, window.location.host,
                                        file.object, "MESOBJECT", file.id);

                                    $rootScope.downloadFileFromIframe(url);
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }

                        else if (file.objectType != 'CUSTOMFILE') {
                            ObjectFileService.updateObjectFileDownloadHistory(file.object, $scope.object.objectType, file.id).then(
                                function (data) {
                                    var url = "{0}//{1}/api/plm/objects/{2}/{3}/files/{4}/download".
                                        format(window.location.protocol, window.location.host,
                                        file.object, $scope.object.objectType, file.id);

                                    $rootScope.downloadFileFromIframe(url);
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        } else if (file.objectType == 'CUSTOMFILE') {
                            var url = "{0}//{1}/api/customObjects/{2}/files/{3}/download".
                                format(window.location.protocol, window.location.host,
                                file.object, file.id);
                            $rootScope.downloadFileFromIframe(url);
                            CustomObjectFileService.updateCustomObjectFileDownloadHistory(file.object, file.id).then(
                                function (data) {

                                }
                            )
                        }
                    }


                    $scope.toggleNode = toggleNode;
                    function toggleNode(folder) {
                        if (folder.expanded == null || folder.expanded == undefined) {
                            folder.expanded = false;
                        }
                        folder.expanded = !folder.expanded;
                        if ($scope.object.objectType == "ITEM" || $scope.object.objectType == "CUSTOMOBJECT" || $scope.object.objectType == "BOPREVISION") {
                            var index = $scope.object.itemFiles.indexOf(folder);
                        }
                        if ($scope.object.objectType == "MBOM") {
                            var index = $scope.object.mbomFiles.indexOf(folder);
                        }
                        else if ($scope.object.objectType == "SHIFT") {
                            var index = $scope.object.shiftFiles.indexOf(folder);
                        }
                        else if ($scope.object.objectType == "MATERIAL") {
                            var index = $scope.object.materialFiles.indexOf(folder);
                        }
                        else if ($scope.object.objectType == "JIGFIXTURE") {
                            var index = $scope.object.jigsFixtureFiles.indexOf(folder);
                        }
                        else if ($scope.object.objectType == "TOOL") {
                            var index = $scope.object.toolFiles.indexOf(folder);
                        }
                        else if ($scope.object.objectType == "INSTRUMENT") {
                            var index = $scope.object.instrumentFiles.indexOf(folder);
                        }
                        else if ($scope.object.objectType == "EQUIPMENT") {
                            var index = $scope.object.equipmentFiles.indexOf(folder);
                        }
                        else if ($scope.object.objectType == "MACHINE") {
                            var index = $scope.object.machineFiles.indexOf(folder);
                        }
                        else if ($scope.object.objectType == "WORKCENTER") {
                            var index = $scope.object.workcenterFiles.indexOf(folder);
                        }
                        else if ($scope.object.objectType == "ASSEMBLYLINE") {
                            var index = $scope.object.assemblyLineFiles.indexOf(folder);
                        }
                        else if ($scope.object.objectType == "PLANT") {
                            var index = $scope.object.plantFiles.indexOf(folder);
                        }
                        else if ($scope.object.objectType == "OPERATION") {
                            var index = $scope.object.operationFiles.indexOf(folder);
                        }
                    
                        else if ($scope.object.objectType == "MANUFACTURERPART") {
                            var index = $scope.object.inspectionReportFiles.indexOf(folder);
                        }
                        if (folder.expanded == false) {
                            removeChildren(folder);
                        } else {
                            var promise = null;
                            var objectId = $scope.object.id;
                            if ($scope.object.objectType != undefined && ($scope.object.objectType == "ITEM" || $scope.object.objectType == "MBOM" ||  $scope.object.objectType == "BOPREVISION")) {
                                if($scope.object.objectType == "ITEM"){
                                    objectId = $scope.object.latestRevision;
                                }
                                promise = ObjectFileService.getFolderChildren(objectId, $scope.object.objectType, folder.id, false)
                            } else if (folder.objectType == 'CUSTOMFILE') {
                                promise = CustomObjectFileService.getFolderChildren($scope.object.id, folder.id, false)
                            } else if ($scope.object.objectType == "MANUFACTURERPART") {
                                promise = ObjectFileService.getFolderChildren($scope.object.id, $scope.object.objectType, folder.id, false)
                            }
                            else if ($scope.object.objectType == "SHIFT") {
                                promise = ObjectFileService.getFolderChildren($scope.object.id, 'MESOBJECT', folder.id, false)
                            }
                            else if ($scope.object.objectType == "MATERIAL") {
                                promise = ObjectFileService.getFolderChildren($scope.object.id, 'MESOBJECT', folder.id, false)
                            }
                            else if ($scope.object.objectType == "JIGFIXTURE") {
                                promise = ObjectFileService.getFolderChildren($scope.object.id, 'MESOBJECT', folder.id, false)
                            }
                            else if ($scope.object.objectType == "TOOL") {
                                promise = ObjectFileService.getFolderChildren($scope.object.id, 'MESOBJECT', folder.id, false)
                            }
                            else if ($scope.object.objectType == "INSTRUMENT") {
                                promise = ObjectFileService.getFolderChildren($scope.object.id, 'MESOBJECT', folder.id, false)
                            }
                            else if ($scope.object.objectType == "EQUIPMENT") {
                                promise = ObjectFileService.getFolderChildren($scope.object.id, 'MESOBJECT', folder.id, false)
                            }
                            else if ($scope.object.objectType == "MACHINE") {
                                promise = ObjectFileService.getFolderChildren($scope.object.id, 'MESOBJECT', folder.id, false)
                            }
                            else if ($scope.object.objectType == "WORKCENTER") {
                                promise = ObjectFileService.getFolderChildren($scope.object.id, 'MESOBJECT', folder.id, false)
                            }
                            else if ($scope.object.objectType == "ASSEMBLYLINE") {
                                promise = ObjectFileService.getFolderChildren($scope.object.id, 'MESOBJECT', folder.id, false)
                            }
                            else if ($scope.object.objectType == "PLANT") {
                                promise = ObjectFileService.getFolderChildren($scope.object.id, 'MESOBJECT', folder.id, false)
                            }
                            else if ($scope.object.objectType == "OPERATION") {
                                promise = ObjectFileService.getFolderChildren($scope.object.id, 'MESOBJECT', folder.id, false)
                            }
                            if (promise != null) {
                                promise.then(
                                    function (data) {
                                        var childrenData = [];
                                        folder.folderChildren = [];
                                        if (folder.objectType == 'CUSTOMFILE') {
                                            childrenData = data;
                                        } else {
                                            childrenData = data.objectFiles;
                                        }
                                        folder.count = childrenData.length;
                                        angular.forEach(childrenData, function (item) {
                                            item.editMode = false;
                                            item.expanded = false;
                                            item.level = folder.level + 1;
                                            item.parentFolder = folder;
                                            item.folderChildren = [];
                                            if (item.createdDate) {
                                                item.createdDatede = moment(item.createdDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                                            }
                                            if (item.modifiedDate) {
                                                item.modifiedDatede = moment(item.modifiedDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                                            }
                                            folder.folderChildren.push(item);
                                        });

                                        angular.forEach(folder.folderChildren, function (item) {
                                            index = index + 1;
                                            if (item.createdDate) {
                                                item.createdDatede = moment(item.createdDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                                            }
                                            if (item.modifiedDate) {
                                                item.modifiedDatede = moment(item.modifiedDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                                            }
                                            if ($scope.object.objectType == "ITEM" || $scope.object.objectType == "CUSTOMOBJECT" || $scope.object.objectType == "BOPREVISION") {
                                                $scope.object.itemFiles.splice(index, 0, item);
                                            }
                                            if ($scope.object.objectType == "MBOM") {
                                                $scope.object.mbomFiles.splice(index, 0, item);
                                            }
                                            if ($scope.object.objectType == "SHIFT") {
                                                $scope.object.shiftFiles.splice(index, 0, item);
                                            }
                                            if ($scope.object.objectType == "MATERIAL") {
                                                $scope.object.materialFiles.splice(index, 0, item);
                                            }
                                            if ($scope.object.objectType == "JIGFIXTURE") {
                                                $scope.object.jigsFixtureFiles.splice(index, 0, item);
                                            }
                                            if ($scope.object.objectType == "TOOL") {
                                                $scope.object.toolFiles.splice(index, 0, item);
                                            }
                                            if ($scope.object.objectType == "INSTRUMENT") {
                                                $scope.object.instrumentFiles.splice(index, 0, item);
                                            }
                                            if ($scope.object.objectType == "EQUIPMENT") {
                                                $scope.object.equipmentFiles.splice(index, 0, item);
                                            }
                                            if ($scope.object.objectType == "MACHINE") {
                                                $scope.object.machineFiles.splice(index, 0, item);
                                            }
                                            if ($scope.object.objectType == "WORKCENTER") {
                                                $scope.object.workcenterFiles.splice(index, 0, item);
                                            }
                                            if ($scope.object.objectType == "ASSEMBLYLINE") {
                                                $scope.object.assemblyLineFiles.splice(index, 0, item);
                                            }
                                            if ($scope.object.objectType == "PLANT") {
                                                $scope.object.plantFiles.splice(index, 0, item);
                                            }
                                            if ($scope.object.objectType == "OPERATION") {
                                                $scope.object.operationFiles.splice(index, 0, item);
                                            }
                                            if ($scope.object.objectType == "MANUFACTURERPART") {
                                                $scope.object.inspectionReportFiles.splice(index, 0, item);
                                            }
                                        });
                                        CommonService.getMultiplePersonReferences(folder.folderChildren, ['createdBy', 'modifiedBy', 'lockedBy']);

                                        $scope.$evalAsync();

                                    }
                                )
                            }
                        }
                    }

                    function removeChildren(folder) {
                        if (folder != null && folder.folderChildren != null && folder.folderChildren != undefined) {
                            angular.forEach(folder.folderChildren, function (item) {
                                removeChildren(item);
                            });
                            if ($scope.object.objectType == "ITEM" || $scope.object.objectType == "CUSTOMOBJECT" || $scope.object.objectType == "BOPREVISION") {
                                var index = $scope.object.itemFiles.indexOf(folder);
                                $scope.object.itemFiles.splice(index + 1, folder.folderChildren.length);
                            } else if ($scope.object.objectType == "MANUFACTURERPART") {
                                var index = $scope.object.inspectionReportFiles.indexOf(folder);
                                $scope.object.inspectionReportFiles.splice(index + 1, folder.folderChildren.length);
                            } if ($scope.object.objectType == "MBOM") {
                                var index = $scope.object.mbomFiles.indexOf(folder);
                                $scope.object.  mbomFiles.splice(index + 1, folder.folderChildren.length);
                            }
                            if ($scope.object.objectType == "PLANT") {
                                var index = $scope.object.plantFiles.indexOf(folder);
                                $scope.object.plantFiles.splice(index + 1, folder.folderChildren.length);
                            }
                            if ($scope.object.objectType == "ASSEMBLYLINE") {
                                var index = $scope.object.assemblyLineFiles.indexOf(folder);
                                $scope.object.assemblyLineFiles.splice(index + 1, folder.folderChildren.length);
                            }
                            if ($scope.object.objectType == "WORKCENTER") {
                                var index = $scope.object.workcenterFiles.indexOf(folder);
                                $scope.object.workcenterFiles.splice(index + 1, folder.folderChildren.length);
                            }
                            if ($scope.object.objectType == "MACHINE") {
                                var index = $scope.object.machineFiles.indexOf(folder);
                                $scope.object.machineFiles.splice(index + 1, folder.folderChildren.length);
                            }
                            if ($scope.object.objectType == "EQUIPMENT") {
                                var index = $scope.object.equipmentFiles.indexOf(folder);
                                $scope.object.equipmentFiles.splice(index + 1, folder.folderChildren.length);
                            }
                            if ($scope.object.objectType == "INSTRUMENT") {
                                var index = $scope.object.instrumentFiles.indexOf(folder);
                                $scope.object.instrumentFiles.splice(index + 1, folder.folderChildren.length);
                            }
                            if ($scope.object.objectType == "TOOL") {
                                var index = $scope.object.toolFiles.indexOf(folder);
                                $scope.object.toolFiles.splice(index + 1, folder.folderChildren.length);
                            }
                            if ($scope.object.objectType == "JIGFIXTURE") {
                                var index = $scope.object.jigsFixtureFiles.indexOf(folder);
                                $scope.object.jigsFixtureFiles.splice(index + 1, folder.folderChildren.length);
                            }
                            if ($scope.object.objectType == "MATERIAL") {
                                var index = $scope.object.materialFiles.indexOf(folder);
                                $scope.object.materialFiles.splice(index + 1, folder.folderChildren.length);
                            }
                            if ($scope.object.objectType == "SHIFT") {
                                var index = $scope.object.shiftFiles.indexOf(folder);
                                $scope.object.shiftFiles.splice(index + 1, folder.folderChildren.length);
                            }
                            if ($scope.object.objectType == "OPERATION") {
                                var index = $scope.object.operationFiles.indexOf(folder);
                                $scope.object.operationFiles.splice(index + 1, folder.folderChildren.length);
                            }
                            
                            folder.folderChildren = [];
                            folder.expanded = false;
                        }

                        $scope.$evalAsync();
                    }

                    (function () {
                        $scope.$on('app.all.view.attributes', function (event, data) {

                        });
                    })();
                }
            }

        }
    }
); 