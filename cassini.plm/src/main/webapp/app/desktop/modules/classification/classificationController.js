define(['app/desktop/modules/classification/classification.module',
        'split-pane',
        'jquery.easyui',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/customObjectTypeService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/classificationService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/workflowService',
        'app/shared/services/core/requirementsTypeService',
        'app/shared/services/core/qualityTypeService',
        'app/desktop/modules/classification/details/changeTypeDetailController',
        'app/desktop/modules/classification/details/itemTypeDetailController',
        'app/desktop/modules/classification/details/requirementTypeDetailController',
        'app/desktop/modules/classification/details/mfrTypeDetailController',
        'app/desktop/modules/classification/details/mfrPartTypeDetailController',
        'app/desktop/modules/classification/details/workflowTypeDetailController',
        'app/desktop/modules/classification/details/qualityTypeDetailsController',
        'app/desktop/modules/classification/details/mesObjectTypeDetailsController',
        'app/desktop/modules/classification/tabs/itemType/basic/itemTypeBasicController',
        'app/desktop/modules/classification/details/customObjectTypeDetailController',
        'app/desktop/modules/classification/details/pmObjectTypeDetailsController',
        'app/desktop/modules/classification/details/pgcObjectTypeDetailsController',
        'app/desktop/modules/classification/details/objectTypeAttributesController',
        'app/shared/services/core/varianceService',
        'app/shared/services/core/mesObjectTypeService',
        'app/shared/services/core/pgcObjectTypeService',
        'app/shared/services/core/pmObjectTypeService'
    ],
    function (module) {
        module.controller('ClassificationController', ClassificationController);

        function ClassificationController($q, $scope, $rootScope, $timeout, $state, DialogService, $stateParams, $cookies,
                                          CommonService, AutonumberService, ClassificationService, $translate, LovService,
                                          ItemTypeService, ItemService, ECOService, MfrService, MfrPartsService, PMObjectTypeService, PGCObjectTypeService,
                                          VarianceService, WorkflowService, RequirementsTypeService, CustomObjectTypeService, QualityTypeService, $interval, MESObjectTypeService) {

            $rootScope.viewInfo.icon = "fa flaticon-marketing8";
            $rootScope.viewInfo.title = $translate.instant("CLASSIFICATION_TITLE");
            $rootScope.viewInfo.showDetails = false;
            var vm = this;
            var parsed = angular.element("<div></div>");

            var itemTypeSuccessMessage = parsed.html($translate.instant("ITEM_TYPE_SUCCESS_MESSAGE")).html();
            var customObjectTypeSuccessMessage = parsed.html($translate.instant("CUSTOM_OBJECT_TYPE_SUCCESS_MESSAGE")).html();
            var changeTypeSuccessMessage = parsed.html($translate.instant("CHANGE_TYPE_SUCCESS_MESSAGE")).html();
            var mfrTypeSuccessMessage = parsed.html($translate.instant("MFR_TYPE_SUCCESS_MESSAGE")).html();
            var mfrPartTypeSuccessMessage = parsed.html($translate.instant("MFR_PART_TYPE_SUCCESS_MESSAGE")).html();
            var workflowTypeSuccessMessage = parsed.html($translate.instant("MFR_PART_TYPE_SUCCESS_MESSAGE")).html();
            var typeCannotBeEmpty = parsed.html($translate.instant("TYPE_CANNOT_BE_EMPTY")).html();

            $scope.searchTitle = parsed.html($translate.instant("SEARCH")).html();

            vm.autoNumbers = [];
            vm.type = null;

            vm.addType = addType;
            vm.deleteType = deleteType;
            vm.onSave = onSave;

            var pageable = {
                page: 0,
                size: 30,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            var nodeId = 0;
            var classificationTree = null;
            var rootNode = null;

            $rootScope.nameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            $rootScope.descriptionValidation = parsed.html($translate.instant("DESCRIPTION_VALIDATION")).html();
            $rootScope.dataTypeValidation = parsed.html($translate.instant("DATA_TYPE_VALIDATION")).html();
            $rootScope.listValueValidation = parsed.html($translate.instant("LIST_VALUE_VALIDATION")).html();
            $rootScope.referenceTypeValidation = parsed.html($translate.instant("REFERENCE_TYPE_VALIDATION")).html();
            $rootScope.formulaAttributeValidation = parsed.html($translate.instant("FORMULA_ATTRIBUTE_VALIDATION")).html();
            $rootScope.deleteAttributeDialogTitle = parsed.html($translate.instant("DELETE_ATTRIBUTE_DIALOG_TITLE")).html();
            $rootScope.deleteAttributeDialogMessage = parsed.html($translate.instant("DELETE_ATTRIBUTE_DIALOG_MESSAGE")).html();
            $rootScope.deleteAttributeMessage = parsed.html($translate.instant("DELETE_ATTRIBUTE_MESSAGE")).html();
            var classification = parsed.html($translate.instant("CLASSIFICATION_TITLE")).html();
            var Duplicate = $translate.instant("DUPLICATE_MESSAGE");

            function initClassificationTree() {
                classificationTree = $('#classificationTree').tree({
                    data: [
                        {
                            id: nodeId,
                            text: classification,
                            iconCls: 'classification-root',
                            attributes: {
                                typeObject: null,
                                nodeType: 'ROOT'
                            },
                            children: []
                        }
                    ],
                    onContextMenu: onContextMenu,
                    // onDblClick: onDblClick,
                    onAfterEdit: onAfterEdit,
                    onSelect: onSelectType
                });

                rootNode = classificationTree.tree('find', 0);

                $(document).click(function () {
                    $("#contextMenu").hide();
                });

                loadClassification();
            }

            vm.doubleClickType = null;
            // function onDblClick(node) {
            //     var data = classificationTree.tree('getData', node.target);
            //     if (data.id != 0 && (data.attributes.nodeType != null ||
            //         data.attributes.nodeType != undefined) &&
            //         node.attributes.root != false) {
            //         vm.doubleClickType = data.attributes.typeObject.name;

            //         if (vm.doubleClickType == 'Requirement' || vm.doubleClickType == 'Specification') {
            //             classificationTree.tree('cancelEdit', node.target);
            //         }
            //         else if (vm.doubleClickType == 'Anforderung' || vm.doubleClickType == 'Spezifikation') {
            //             classificationTree.tree('cancelEdit', node.target);
            //         }
            //         else if (!data.attributes.typeObject.usedType) {
            //           classificationTree.tree('beginEdit', node.target);
            //             $timeout(function () {
            //                 $('.tree-editor').focus().select();
            //             }, 1);
            //         }
            //     }

            // }

            var savedMessage = $translate.instant("SAVED_MESSAGE");
            vm.selectedNode = null;

            function onAfterEdit(node) {
                var promise = null;
                var deleted = false;
                var parent = classificationTree.tree('getParent', node.target);
                var parentData = classificationTree.tree('getData', parent.target);
                var data = classificationTree.tree('getData', node.target);
                if (data.attributes.typeObject == null) {
                    data.attributes.typeObject = {}
                }

                vm.selectedNode = data.attributes.nodeType;

                if (parentData.id != 0 && parentData.attributes.typeObject != undefined) {
                    data.attributes.typeObject.parentType = parentData.attributes.typeObject.id;
                    data.attributes.typeObject.parent = parentData.attributes.typeObject.id;
                }

                data.attributes.typeObject.name = node.text;

                if (data.attributes.typeObject.id == undefined || data.attributes.typeObject.id == null) {
                    if (isExists(parent, node)) {
                        classificationTree.tree('remove', node.target);
                        deleted = true;
                    }
                    else {
                        data.attributes.typeObject.itemNumberSource = vm.defaultPartNumberSource;
                        data.attributes.typeObject.revisionSequence = vm.defaultRevisionSequence;
                        data.attributes.typeObject.lifecycle = vm.defaultLifecycle;
                        if (data.attributes.nodeType == 'ITEMTYPE') {
                            if (parentData.id != 0 && parentData.attributes.typeObject != undefined) {
                                data.attributes.typeObject.parentType = parentData.attributes.typeObject.id;
                                data.attributes.typeObject.itemNumberSource = parentData.attributes.typeObject.itemNumberSource;
                                if (parentData.attributes.typeObject.itemClass != null && parentData.attributes.typeObject.itemClass != undefined) {
                                    data.attributes.typeObject.itemClass = parentData.attributes.typeObject.itemClass;
                                }
                                data.attributes.typeObject.softwareType = parentData.attributes.typeObject.softwareType;
                            }

                            if (parentData.id != 0 && parentData.attributes.root != undefined && parentData.attributes.root) {
                                data.attributes.typeObject.itemClass = "OTHER";
                            }

                            if (data.attributes.typeObject.name != "") {
                                data.attributes.typeObject.tabs = ['bom', 'whereUsed', 'changes', 'variance', 'quality', 'files', 'mfrParts', 'relatedItems', 'projects', 'specifications', 'requirements'];
                                promise = ClassificationService.createType('ITEMTYPE', data.attributes.typeObject);
                            } else {
                                $rootScope.showWarningMessage(typeCannotBeEmpty);
                                classificationTree.tree('remove', node.target);
                                deleted = true;
                            }
                        }
                        else if (data.attributes.nodeType == 'CUSTOMOBJECTTYPE') {
                            data.attributes.typeObject.numberSource = vm.defaultPartNumberSource;
                            if (parentData.id != 0 && parentData.attributes.typeObject != undefined) {
                                data.attributes.typeObject.numberSource = parentData.attributes.typeObject.numberSource;
                                data.attributes.typeObject.isRevisioned = parentData.attributes.typeObject.isRevisioned;
                                data.attributes.typeObject.hasLifecycle = parentData.attributes.typeObject.hasLifecycle;
                                data.attributes.typeObject.revisionSequence = parentData.attributes.typeObject.revisionSequence;
                                data.attributes.typeObject.lifecycle = parentData.attributes.typeObject.revisionSequence;
                            }
                            if (data.attributes.typeObject.name != "") {
                                data.attributes.typeObject.lifecycle = vm.defaultRevisionSequence;
                                data.attributes.typeObject.tabs = [];
                                //data.attributes.typeObject.tabs = ['bom', 'relatedObjects', 'whereUsed'];
                                var objectType = data.attributes.typeObject;
                                objectType.description = "";
                                promise = CustomObjectTypeService.createCustomObjectType(objectType);
                            } else {
                                $rootScope.showWarningMessage(typeCannotBeEmpty);
                                classificationTree.tree('remove', node.target);
                                deleted = true;
                            }
                        }
                        else if (data.attributes.nodeType == 'CHANGETYPE') {
                            if (data.attributes.typeObject.name != "") {
                                if (parentData.attributes.typeObject.objectType == "DEVIATIONTYPE") {
                                    data.attributes.typeObject['@type'] = "PLMDeviationType";
                                    data.attributes.typeObject.autoNumberSource = parentData.attributes.typeObject.autoNumberSource;
                                    data.attributes.typeObject.revisionSequence = parentData.attributes.typeObject.revisionSequence;
                                    promise = ClassificationService.createType('CHANGETYPE', data.attributes.typeObject);
                                } else if (parentData.attributes.typeObject.objectType == "WAIVERTYPE") {
                                    data.attributes.typeObject['@type'] = "PLMWaiverType";
                                    data.attributes.typeObject.autoNumberSource = parentData.attributes.typeObject.autoNumberSource;
                                    data.attributes.typeObject.revisionSequence = parentData.attributes.typeObject.revisionSequence;
                                    promise = ClassificationService.createType('CHANGETYPE', data.attributes.typeObject);
                                } else if (parentData.attributes.typeObject.objectType == "ECRTYPE") {
                                    data.attributes.typeObject['@type'] = "PLMECRType";
                                    data.attributes.typeObject.autoNumberSource = parentData.attributes.typeObject.autoNumberSource;
                                    data.attributes.typeObject.revisionSequence = parentData.attributes.typeObject.revisionSequence;
                                    data.attributes.typeObject.lifecycle = parentData.attributes.typeObject.lifecycle;
                                    data.attributes.typeObject.changeReasonTypes = parentData.attributes.typeObject.changeReasonTypes;
                                    promise = ClassificationService.createType('CHANGETYPE', data.attributes.typeObject);
                                } else if (parentData.attributes.typeObject.objectType == "ECOTYPE") {
                                    data.attributes.typeObject['@type'] = "PLMECOType";
                                    data.attributes.typeObject.autoNumberSource = parentData.attributes.typeObject.autoNumberSource;
                                    data.attributes.typeObject.revisionSequence = parentData.attributes.typeObject.revisionSequence;
                                    data.attributes.typeObject.lifecycle = parentData.attributes.typeObject.lifecycle;
                                    promise = ClassificationService.createType('CHANGETYPE', data.attributes.typeObject);
                                } else if (parentData.attributes.typeObject.objectType == "DCRTYPE") {
                                    data.attributes.typeObject['@type'] = "PLMDCRType";
                                    data.attributes.typeObject.autoNumberSource = parentData.attributes.typeObject.autoNumberSource;
                                    data.attributes.typeObject.revisionSequence = parentData.attributes.typeObject.revisionSequence;
                                    data.attributes.typeObject.lifecycle = parentData.attributes.typeObject.lifecycle;
                                    data.attributes.typeObject.changeReasonTypes = parentData.attributes.typeObject.changeReasonTypes;
                                    promise = ClassificationService.createType('CHANGETYPE', data.attributes.typeObject);
                                } else if (parentData.attributes.typeObject.objectType == "DCOTYPE") {
                                    data.attributes.typeObject['@type'] = "PLMDCOType";
                                    data.attributes.typeObject.autoNumberSource = parentData.attributes.typeObject.autoNumberSource;
                                    data.attributes.typeObject.revisionSequence = parentData.attributes.typeObject.revisionSequence;
                                    data.attributes.typeObject.lifecycle = parentData.attributes.typeObject.lifecycle;
                                    promise = ClassificationService.createType('CHANGETYPE', data.attributes.typeObject);
                                } else if (parentData.attributes.typeObject.objectType == "MCOTYPE") {
                                    data.attributes.typeObject['@type'] = "PLMMCOType";
                                    data.attributes.typeObject.autoNumberSource = parentData.attributes.typeObject.autoNumberSource;
                                    data.attributes.typeObject.revisionSequence = parentData.attributes.typeObject.revisionSequence;
                                    data.attributes.typeObject.lifecycle = parentData.attributes.typeObject.lifecycle;
                                    data.attributes.typeObject.mcoType = parentData.attributes.typeObject.mcoType;
                                    promise = ClassificationService.createType('CHANGETYPE', data.attributes.typeObject);
                                }

                            } else {
                                $rootScope.showWarningMessage(typeCannotBeEmpty);
                                classificationTree.tree('remove', node.target);
                                deleted = true;
                            }
                        }
                        /*  else if (data.attributes.nodeType == 'REQUIREMENTTYPE') {
                         if (data.attributes.typeObject.name != "") {
                         data.attributes.typeObject.numberSource = parentData.attributes.typeObject.numberSource;
                         data.attributes.typeObject.revisionSequence = parentData.attributes.typeObject.revisionSequence;
                         data.attributes.typeObject.lifecycle = parentData.attributes.typeObject.lifecycle;
                         promise = ClassificationService.createType('REQUIREMENTTYPE', data.attributes.typeObject);
                         } else {
                         $rootScope.showWarningMessage(typeCannotBeEmpty);
                         classificationTree.tree('remove', node.target);
                         deleted = true;
                         }
                         }*/

                        else if (data.attributes.nodeType == 'SPECIFICATIONTYPE') {
                            if (data.attributes.typeObject.name != "") {
                                data.attributes.typeObject.numberSource = parentData.attributes.typeObject.numberSource;
                                data.attributes.typeObject.revisionSequence = parentData.attributes.typeObject.revisionSequence;
                                data.attributes.typeObject.lifecycle = parentData.attributes.typeObject.lifecycle;
                                promise = ClassificationService.createType('SPECIFICATIONTYPE', data.attributes.typeObject);
                            } else {
                                $rootScope.showWarningMessage(typeCannotBeEmpty);
                                classificationTree.tree('remove', node.target);
                                deleted = true;
                            }
                        }

                        else if (data.attributes.nodeType == 'MANUFACTURERTYPE') {
                            if (data.attributes.typeObject.name != "") {
                                data.attributes.typeObject.lifecycle = vm.defaultMfrLifecycle;
                                promise = ClassificationService.createType('MANUFACTURERTYPE', data.attributes.typeObject);
                            } else {
                                $rootScope.showWarningMessage(typeCannotBeEmpty);
                                classificationTree.tree('remove', node.target);
                                deleted = true;
                            }
                        } else if (data.attributes.nodeType == 'MANUFACTURERPARTTYPE') {
                            if (data.attributes.typeObject.name != "") {
                                data.attributes.typeObject.lifecycle = vm.defaultMfrPartLifecycle;
                                promise = ClassificationService.createType('MANUFACTURERPARTTYPE', data.attributes.typeObject);
                            } else {
                                $rootScope.showWarningMessage(typeCannotBeEmpty);
                                classificationTree.tree('remove', node.target);
                                deleted = true;
                            }

                        } else if (data.attributes.nodeType == 'PLANTTYPE') {
                            if (data.attributes.typeObject.name != "") {
                                data.attributes.typeObject.autoNumberSource = vm.defaultPlantNumberSource;
                                promise = MESObjectTypeService.createPlantType(data.attributes.typeObject);
                            } else {
                                $rootScope.showWarningMessage(typeCannotBeEmpty);
                                classificationTree.tree('remove', node.target);
                                deleted = true;
                            }

                        } else if (data.attributes.nodeType == 'ASSEMBLYLINETYPE') {
                            if (data.attributes.typeObject.name != "") {
                                data.attributes.typeObject.autoNumberSource = parentData.attributes.typeObject.autoNumberSource;
                                promise = MESObjectTypeService.createAssemblyLineType(data.attributes.typeObject);
                            } else {
                                $rootScope.showWarningMessage(typeCannotBeEmpty);
                                classificationTree.tree('remove', node.target);
                                deleted = true;
                            }

                        } else if (data.attributes.nodeType == 'WORKCENTERTYPE') {
                            if (data.attributes.typeObject.name != "") {
                                data.attributes.typeObject.autoNumberSource = parentData.attributes.typeObject.autoNumberSource;
                                promise = MESObjectTypeService.createWorkCenterType(data.attributes.typeObject);
                            } else {
                                $rootScope.showWarningMessage(typeCannotBeEmpty);
                                classificationTree.tree('remove', node.target);
                                deleted = true;
                            }

                        } else if (data.attributes.nodeType == 'MACHINETYPE') {
                            if (data.attributes.typeObject.name != "") {
                                data.attributes.typeObject.autoNumberSource = vm.defaultMachineNumberSource;
                                promise = MESObjectTypeService.createMachineType(data.attributes.typeObject);
                            } else {
                                $rootScope.showWarningMessage(typeCannotBeEmpty);
                                classificationTree.tree('remove', node.target);
                                deleted = true;
                            }

                        } else if (data.attributes.nodeType == 'OPERATIONTYPE') {
                            if (data.attributes.typeObject.name != "") {
                                data.attributes.typeObject.autoNumberSource = vm.defaultOperationNumberSource;
                                promise = MESObjectTypeService.createOperationType(data.attributes.typeObject);
                            } else {
                                $rootScope.showWarningMessage(typeCannotBeEmpty);
                                classificationTree.tree('remove', node.target);
                                deleted = true;
                            }
                        }
                        else if (data.attributes.nodeType == 'TOOLTYPE') {
                            if (data.attributes.typeObject.name != "") {
                                data.attributes.typeObject.autoNumberSource = vm.defaultToolNumberSource;
                                promise = MESObjectTypeService.createToolType(data.attributes.typeObject);
                            } else {
                                $rootScope.showWarningMessage(typeCannotBeEmpty);
                                classificationTree.tree('remove', node.target);
                                deleted = true;
                            }
                        }
                        else if (data.attributes.nodeType == 'SUPPLIERTYPE') {
                            if (data.attributes.typeObject.name != "") {
                                data.attributes.typeObject.lifecycle = vm.defaultSupplierLifecycle;
                                if (parentData.id != 0 && parentData.attributes.typeObject != undefined) {
                                    data.attributes.typeObject.autoNumberSource = parentData.attributes.typeObject.autoNumberSource;
                                } else {
                                    data.attributes.typeObject.autoNumberSource = vm.defaultSupplierNumberSource;
                                }
                                promise = MESObjectTypeService.createSupplierType(data.attributes.typeObject);
                            } else {
                                $rootScope.showWarningMessage(typeCannotBeEmpty);
                                classificationTree.tree('remove', node.target);
                                deleted = true;
                            }
                        }
                        else if (data.attributes.nodeType == 'SPAREPARTTYPE') {
                            if (data.attributes.typeObject.name != "") {
                                data.attributes.typeObject.autoNumberSource = parentData.attributes.typeObject.autoNumberSource;
                                promise = MESObjectTypeService.createSparePartType(data.attributes.typeObject);
                            } else {
                                $rootScope.showWarningMessage(typeCannotBeEmpty);
                                classificationTree.tree('remove', node.target);
                                deleted = true;
                            }
                        }
                        else if (data.attributes.nodeType == 'ASSETTYPE') {
                            if (data.attributes.typeObject.name != "") {
                                data.attributes.typeObject.autoNumberSource = parentData.attributes.typeObject.autoNumberSource;
                                promise = MESObjectTypeService.createAssetType(data.attributes.typeObject);
                            } else {
                                $rootScope.showWarningMessage(typeCannotBeEmpty);
                                classificationTree.tree('remove', node.target);
                                deleted = true;
                            }
                        } else if (data.attributes.nodeType == 'METERTYPE') {
                            if (data.attributes.typeObject.name != "") {
                                data.attributes.typeObject.autoNumberSource = parentData.attributes.typeObject.autoNumberSource;
                                promise = MESObjectTypeService.createMeterType(data.attributes.typeObject);
                            } else {
                                $rootScope.showWarningMessage(typeCannotBeEmpty);
                                classificationTree.tree('remove', node.target);
                                deleted = true;
                            }
                        } else if (data.attributes.nodeType == 'WORKREQUESTTYPE') {
                            if (data.attributes.typeObject.name != "") {
                                data.attributes.typeObject.autoNumberSource = parentData.attributes.typeObject.autoNumberSource;
                                promise = MESObjectTypeService.createWorkRequestType(data.attributes.typeObject);
                            } else {
                                $rootScope.showWarningMessage(typeCannotBeEmpty);
                                classificationTree.tree('remove', node.target);
                                deleted = true;
                            }
                        }
                        else if (data.attributes.nodeType == 'WORKORDERTYPE') {
                            if (data.attributes.typeObject.name != "") {
                                data.attributes.typeObject.autoNumberSource = parentData.attributes.typeObject.autoNumberSource;
                                data.attributes.typeObject.type = parentData.attributes.typeObject.type;
                                promise = MESObjectTypeService.createWorkOrderType(data.attributes.typeObject);
                            } else {
                                $rootScope.showWarningMessage(typeCannotBeEmpty);
                                classificationTree.tree('remove', node.target);
                                deleted = true;
                            }
                        }
                        else if (data.attributes.nodeType == 'MATERIALTYPE') {
                            if (data.attributes.typeObject.name != "") {
                                data.attributes.typeObject.autoNumberSource = vm.defaultMaterialNumberSource;
                                promise = MESObjectTypeService.createMaterialType(data.attributes.typeObject);
                            } else {
                                $rootScope.showWarningMessage(typeCannotBeEmpty);
                                classificationTree.tree('remove', node.target);
                                deleted = true;
                            }
                        }
                        else if (data.attributes.nodeType == 'JIGFIXTURETYPE') {
                            if (data.attributes.typeObject.name != "") {
                                data.attributes.typeObject.autoNumberSource = vm.defaultJigsFixNumberSource;
                                data.attributes.typeObject.autoNumberSource = parentData.attributes.typeObject.autoNumberSource;
                                promise = MESObjectTypeService.createJigsFixtureType(data.attributes.typeObject);
                            } else {
                                $rootScope.showWarningMessage(typeCannotBeEmpty);
                                classificationTree.tree('remove', node.target);
                                deleted = true;
                            }
                        }
                        else if (data.attributes.nodeType == 'PGCSUBSTANCETYPE') {
                            if (data.attributes.typeObject.name != "") {
                                data.attributes.typeObject.autoNumberSource = parentData.attributes.typeObject.autoNumberSource;
                                promise = PGCObjectTypeService.createSubstanceType(data.attributes.typeObject);
                            } else {
                                $rootScope.showWarningMessage(typeCannotBeEmpty);
                                classificationTree.tree('remove', node.target);
                                deleted = true;
                            }
                        } else if (data.attributes.nodeType == 'PGCSUBSTANCEGROUPTYPE') {
                            if (data.attributes.typeObject.name != "") {
                                data.attributes.typeObject.autoNumberSource = parentData.attributes.typeObject.autoNumberSource;
                                promise = PGCObjectTypeService.createSubstanceGroupType(data.attributes.typeObject);
                            } else {
                                $rootScope.showWarningMessage(typeCannotBeEmpty);
                                classificationTree.tree('remove', node.target);
                                deleted = true;
                            }
                        } else if (data.attributes.nodeType == 'PGCSPECIFICATIONTYPE') {
                            if (data.attributes.typeObject.name != "") {
                                data.attributes.typeObject.autoNumberSource = parentData.attributes.typeObject.autoNumberSource;
                                promise = PGCObjectTypeService.createSpecificationType(data.attributes.typeObject);
                            } else {
                                $rootScope.showWarningMessage(typeCannotBeEmpty);
                                classificationTree.tree('remove', node.target);
                                deleted = true;
                            }
                        } else if (data.attributes.nodeType == 'PGCDECLARATIONTYPE') {
                            if (data.attributes.typeObject.name != "") {
                                data.attributes.typeObject.autoNumberSource = parentData.attributes.typeObject.autoNumberSource;
                                promise = PGCObjectTypeService.createDeclarationType(data.attributes.typeObject);
                            } else {
                                $rootScope.showWarningMessage(typeCannotBeEmpty);
                                classificationTree.tree('remove', node.target);
                                deleted = true;
                            }
                        } else if (data.attributes.nodeType == 'MANPOWERTYPE') {
                            if (data.attributes.typeObject.name != "") {
                                data.attributes.typeObject.autoNumberSource = vm.defaultManpowerNumberSource;
                                promise = MESObjectTypeService.createManpowerType(data.attributes.typeObject);
                            } else {
                                $rootScope.showWarningMessage(typeCannotBeEmpty);
                                classificationTree.tree('remove', node.target);
                                deleted = true;
                            }
                        } else if (data.attributes.nodeType == 'EQUIPMENTTYPE') {
                            if (data.attributes.typeObject.name != "") {
                                data.attributes.typeObject.autoNumberSource = vm.defaultEquipmentNumberSource;
                                promise = MESObjectTypeService.createEquipmentType(data.attributes.typeObject);
                            } else {
                                $rootScope.showWarningMessage(typeCannotBeEmpty);
                                classificationTree.tree('remove', node.target);
                                deleted = true;
                            }
                        } else if (data.attributes.nodeType == 'INSTRUMENTTYPE') {
                            if (data.attributes.typeObject.name != "") {
                                data.attributes.typeObject.autoNumberSource = vm.defaultInstrumentNumberSource;
                                promise = MESObjectTypeService.createInstrumentType(data.attributes.typeObject);
                            } else {
                                $rootScope.showWarningMessage(typeCannotBeEmpty);
                                classificationTree.tree('remove', node.target);
                                deleted = true;
                            }
                        }
                        else if (data.attributes.nodeType == 'MBOMTYPE') {
                            if (data.attributes.typeObject.name != "") {
                                data.attributes.typeObject.autoNumberSource = parentData.attributes.typeObject.autoNumberSource;
                                data.attributes.typeObject.lifecycle = parentData.attributes.typeObject.lifecycle;
                                data.attributes.typeObject.qualityType = parentData.attributes.typeObject.qualityType;
                                promise = MESObjectTypeService.createMBOMType(data.attributes.typeObject);
                            } else {
                                $rootScope.showWarningMessage(typeCannotBeEmpty);
                                classificationTree.tree('remove', node.target);
                                deleted = true;
                            }

                        }
                        else if (data.attributes.nodeType == 'BOPTYPE') {
                            if (data.attributes.typeObject.name != "") {
                                data.attributes.typeObject.autoNumberSource = parentData.attributes.typeObject.autoNumberSource;
                                data.attributes.typeObject.lifecycle = parentData.attributes.typeObject.lifecycle;
                                data.attributes.typeObject.qualityType = parentData.attributes.typeObject.qualityType;
                                promise = MESObjectTypeService.createBOPType(data.attributes.typeObject);
                            } else {
                                $rootScope.showWarningMessage(typeCannotBeEmpty);
                                classificationTree.tree('remove', node.target);
                                deleted = true;
                            }

                        }
                        else if (data.attributes.nodeType == 'PRODUCTIONORDERTYPE') {
                            if (data.attributes.typeObject.name != "") {
                                data.attributes.typeObject.autoNumberSource = vm.defaultProductionOrderNumberSource;
                                promise = MESObjectTypeService.createProductionOrderType(data.attributes.typeObject);
                            } else {
                                $rootScope.showWarningMessage(typeCannotBeEmpty);
                                classificationTree.tree('remove', node.target);
                                deleted = true;
                            }

                        } else if (data.attributes.nodeType == 'PMOBJECTTYPE') {
                            if (data.attributes.typeObject.name != "") {
                                data.attributes.typeObject.autoNumberSource = vm.defaultProjectNumberSource;
                                data.attributes.typeObject.type = parentData.attributes.typeObject.type;
                                if (data.attributes.typeObject.type == "PROJECT") {
                                    data.attributes.typeObject.tabs = ['team', 'plan', 'files', 'requirements', 'deliverables', 'referenceItems', 'workflow'];

                                }
                                else if (data.attributes.typeObject.type == "PROGRAM") {
                                    data.attributes.typeObject.autoNumberSource = vm.defaultProgramNumberSource;
                                    data.attributes.typeObject.tabs = ['resources', 'projects', 'files', 'workflow'];

                                }
                                else if (data.attributes.nodeType == "REQUIREMENTTYPE") {
                                    data.attributes.typeObject.autoNumberSource = vm.defaultRequirementNumberSource;
                                    data.attributes.typeObject.tabs = ['reviewers', 'items', 'files', 'workflow'];

                                }
                                promise = PMObjectTypeService.createPmType(data.attributes.typeObject);
                            } else {
                                $rootScope.showWarningMessage(typeCannotBeEmpty);
                                classificationTree.tree('remove', node.target);
                                deleted = true;
                            }

                        } else if (data.attributes.nodeType == 'REQUIREMENTTYPE') {
                            if (data.attributes.typeObject.name != "") {
                                data.attributes.typeObject.autoNumberSource = vm.defaultRequirementNumberSource;
                                data.attributes.typeObject.lifecycle = vm.defaultReqLifecycle;
                                data.attributes.typeObject.priorityList = vm.defaultReqRevisionSequence;
                                promise = PMObjectTypeService.createReqType(data.attributes.typeObject);
                            } else {
                                $rootScope.showWarningMessage(typeCannotBeEmpty);
                                classificationTree.tree('remove', node.target);
                                deleted = true;
                            }

                        } else if (data.attributes.nodeType == 'REQUIREMENTDOCUMENTTYPE') {
                            if (data.attributes.typeObject.name != "") {
                                data.attributes.typeObject.autoNumberSource = vm.defaultRequirementDocumentNumberSource;
                                data.attributes.typeObject.lifecycle = vm.defaultReqDocLifecycle;
                                data.attributes.typeObject.autoNumberSource = vm.defaultRequirementDocumentNumberSource;
                                data.attributes.typeObject.tabs = ['reviewers', 'requirements', 'files', 'workflow'];
                                promise = PMObjectTypeService.createReqDocType(data.attributes.typeObject);
                            } else {
                                $rootScope.showWarningMessage(typeCannotBeEmpty);
                                classificationTree.tree('remove', node.target);
                                deleted = true;
                            }

                        }
                        else if (data.attributes.nodeType == 'WORKFLOWTYPE') {
                            data.attributes.typeObject.numberSource = vm.defaultWorkflowNumberSource;
                            data.attributes.typeObject.lifecycle = vm.defaultWorkflowLifecycle;
                            data.attributes.typeObject.revisionSequence = vm.defaultRevisionSequence;
                            if (data.attributes.typeObject.name != "") {
                                promise = ClassificationService.createType('WORKFLOWTYPE', data.attributes.typeObject);
                            } else {
                                $rootScope.showWarningMessage(typeCannotBeEmpty);
                                classificationTree.tree('remove', node.target);
                                deleted = true;
                            }

                        } else if (data.attributes.nodeType == 'QUALITY_TYPE') {
                            data.attributes.typeObject.numberSource = parentData.attributes.typeObject.numberSource;
                            data.attributes.typeObject.lifecycle = parentData.attributes.typeObject.lifecycle;
                            data.attributes.typeObject.qualityType = parentData.attributes.typeObject.qualityType;
                            if (parentData.attributes.typeObject != null && parentData.attributes.typeObject != "" && parentData.attributes.typeObject != undefined) {
                                if (parentData.attributes.typeObject.qualityType == "PRODUCTINSPECTIONPLANTYPE" || parentData.attributes.typeObject.qualityType == "MATERIALINSPECTIONPLANTYPE") {
                                    data.attributes.typeObject.revisionSequence = parentData.attributes.typeObject.revisionSequence;
                                    data.attributes.typeObject.inspectionNumberSource = parentData.attributes.typeObject.inspectionNumberSource;
                                } else if (parentData.attributes.typeObject.qualityType == "PRTYPE") {
                                    data.attributes.typeObject.failureTypes = parentData.attributes.typeObject.failureTypes;
                                    data.attributes.typeObject.severities = parentData.attributes.typeObject.severities;
                                    data.attributes.typeObject.dispositions = parentData.attributes.typeObject.dispositions;
                                } else if (parentData.attributes.typeObject.qualityType == "NCRTYPE") {
                                    data.attributes.typeObject.failureTypes = parentData.attributes.typeObject.failureTypes;
                                    data.attributes.typeObject.severities = parentData.attributes.typeObject.severities;
                                    data.attributes.typeObject.dispositions = parentData.attributes.typeObject.dispositions;
                                }
                            }

                            if (data.attributes.typeObject.name != "") {
                                if (parentData.attributes.typeObject.qualityType == "PRODUCTINSPECTIONPLANTYPE") {
                                    data.attributes.typeObject.productType = parentData.attributes.typeObject.productType;
                                    promise = QualityTypeService.createProductInspectionPlanType(data.attributes.typeObject);
                                } else if (parentData.attributes.typeObject.qualityType == "MATERIALINSPECTIONPLANTYPE") {
                                    data.attributes.typeObject.partType = parentData.attributes.typeObject.partType;
                                    promise = QualityTypeService.createMaterialInspectionPlanType(data.attributes.typeObject);
                                } else if (parentData.attributes.typeObject.qualityType == "PRTYPE") {
                                    promise = QualityTypeService.createPrType(data.attributes.typeObject);
                                } else if (parentData.attributes.typeObject.qualityType == "NCRTYPE") {
                                    promise = QualityTypeService.createNcrType(data.attributes.typeObject);
                                } else if (parentData.attributes.typeObject.qualityType == "QCRTYPE") {
                                    promise = QualityTypeService.createQcrType(data.attributes.typeObject);
                                } else if (parentData.attributes.typeObject.qualityType == "PPAPTYPE") {
                                    promise = QualityTypeService.createPpapType(data.attributes.typeObject);
                                } else if (parentData.attributes.typeObject.qualityType == "SUPPLIERAUDITTYPE") {
                                    promise = QualityTypeService.createSupplierAuditType(data.attributes.typeObject);
                                }
                            } else {
                                $rootScope.showWarningMessage(typeCannotBeEmpty);
                                classificationTree.tree('remove', node.target);
                                deleted = true;
                            }

                        }
                    }
                }
                else {
                    if (data.attributes.nodeType == 'ITEMTYPE') {
                        if (data.attributes.typeObject.name != "") {
                            promise = ClassificationService.updateType('ITEMTYPE', data.attributes.typeObject);
                        } else {
                            $rootScope.showWarningMessage(typeCannotBeEmpty);
                            data.attributes.typeObject.name = vm.doubleClickType;
                            promise = ClassificationService.updateType('ITEMTYPE', data.attributes.typeObject);
                        }

                    }
                    else if (data.attributes.nodeType == 'CUSTOMOBJECTTYPE') {
                        if (data.attributes.typeObject.name != "") {
                            promise = CustomObjectTypeService.createCustomObjectType(data.attributes.typeObject);
                        } else {
                            $rootScope.showWarningMessage(typeCannotBeEmpty);
                            data.attributes.typeObject.name = vm.doubleClickType;
                            promise = CustomObjectTypeService.createCustomObjectType(data.attributes.typeObject);
                        }

                    }
                    else if (data.attributes.nodeType == 'CHANGETYPE') {
                        if (data.attributes.typeObject.name != "") {
                            promise = ClassificationService.updateType('CHANGETYPE', data.attributes.typeObject);
                        } else {
                            $rootScope.showWarningMessage(typeCannotBeEmpty);
                            data.attributes.typeObject.name = vm.doubleClickType;
                            promise = ClassificationService.updateType('CHANGETYPE', data.attributes.typeObject);
                        }

                    } else if (data.attributes.nodeType == 'MANUFACTURERTYPE') {
                        if (data.attributes.typeObject.name != "") {
                            promise = ClassificationService.updateType('MANUFACTURERTYPE', data.attributes.typeObject);
                        } else {
                            $rootScope.showWarningMessage(typeCannotBeEmpty);
                            data.attributes.typeObject.name = vm.doubleClickType;
                            promise = ClassificationService.updateType('MANUFACTURERTYPE', data.attributes.typeObject);
                        }

                    } else if (data.attributes.nodeType == 'MANUFACTURERPARTTYPE') {
                        if (data.attributes.typeObject.name != "") {
                            promise = ClassificationService.updateType('MANUFACTURERPARTTYPE', data.attributes.typeObject);
                        } else {
                            $rootScope.showWarningMessage(typeCannotBeEmpty);
                            data.attributes.typeObject.name = vm.doubleClickType;
                            promise = ClassificationService.updateType('MANUFACTURERPARTTYPE', data.attributes.typeObject);
                        }

                    }
                    else if (data.attributes.nodeType == 'OPERATIONTYPE') {
                        if (data.attributes.typeObject.name != "") {
                            promise = MESObjectTypeService.updateOperationType(data.attributes.typeObject);
                        } else {
                            $rootScope.showWarningMessage(typeCannotBeEmpty);
                            data.attributes.typeObject.name = vm.doubleClickType;
                            promise = MESObjectTypeService.updateOperationType(data.attributes.typeObject);
                        }

                    }
                    else if (data.attributes.nodeType == 'MANPOWERTYPE') {
                        if (data.attributes.typeObject.name != "") {
                            promise = MESObjectTypeService.updateManpowerType(data.attributes.typeObject);
                        } else {
                            $rootScope.showWarningMessage(typeCannotBeEmpty);
                            data.attributes.typeObject.name = vm.doubleClickType;
                            promise = MESObjectTypeService.updateManpowerType(data.attributes.typeObject);
                        }

                    } else if (data.attributes.nodeType == 'EQUIPMENTTYPE') {
                        if (data.attributes.typeObject.name != "") {
                            promise = MESObjectTypeService.updateManpowerType(data.attributes.typeObject);
                        } else {
                            $rootScope.showWarningMessage(typeCannotBeEmpty);
                            data.attributes.typeObject.name = vm.doubleClickType;
                            promise = MESObjectTypeService.updateManpowerType(data.attributes.typeObject);
                        }

                    } else if (data.attributes.nodeType == 'INSTRUMENTTYPE') {
                        if (data.attributes.typeObject.name != "") {
                            promise = MESObjectTypeService.updateManpowerType(data.attributes.typeObject);
                        } else {
                            $rootScope.showWarningMessage(typeCannotBeEmpty);
                            data.attributes.typeObject.name = vm.doubleClickType;
                            promise = MESObjectTypeService.updateManpowerType(data.attributes.typeObject);
                        }

                    }
                    else if (data.attributes.nodeType == 'PLANTTYPE') {
                        if (data.attributes.typeObject.name != "") {
                            promise = MESObjectTypeService.updatePlantType(data.attributes.typeObject);
                        } else {
                            $rootScope.showWarningMessage(typeCannotBeEmpty);
                            data.attributes.typeObject.name = vm.doubleClickType;
                            promise = MESObjectTypeService.updatePlantType(data.attributes.typeObject);
                        }

                    }
                    else if (data.attributes.nodeType == 'ASSEMBLYLINETYPE') {
                        if (data.attributes.typeObject.name != "") {
                            promise = MESObjectTypeService.updateAssemblyLineType(data.attributes.typeObject);
                        } else {
                            $rootScope.showWarningMessage(typeCannotBeEmpty);
                            data.attributes.typeObject.name = vm.doubleClickType;
                            promise = MESObjectTypeService.updateAssemblyLineType(data.attributes.typeObject);
                        }

                    }
                    else if (data.attributes.nodeType == 'MBOMTYPE') {
                        if (data.attributes.typeObject.name != "") {
                            promise = MESObjectTypeService.updateMBOMType(data.attributes.typeObject);
                        } else {
                            $rootScope.showWarningMessage(typeCannotBeEmpty);
                            data.attributes.typeObject.name = vm.doubleClickType;
                            promise = MESObjectTypeService.updateMBOMType(data.attributes.typeObject);
                        }

                    }
                    else if (data.attributes.nodeType == 'BOPTYPE') {
                        if (data.attributes.typeObject.name != "") {
                            promise = MESObjectTypeService.updateBOPType(data.attributes.typeObject);
                        } else {
                            $rootScope.showWarningMessage(typeCannotBeEmpty);
                            data.attributes.typeObject.name = vm.doubleClickType;
                            promise = MESObjectTypeService.updateBOPType(data.attributes.typeObject);
                        }

                    }
                    else if (data.attributes.nodeType == 'PRODUCTIONORDERTYPE') {
                        if (data.attributes.typeObject.name != "") {
                            promise = MESObjectTypeService.updateProductionOrderType(data.attributes.typeObject);
                        } else {
                            $rootScope.showWarningMessage(typeCannotBeEmpty);
                            data.attributes.typeObject.name = vm.doubleClickType;
                            promise = MESObjectTypeService.updateProductionOrderType(data.attributes.typeObject);
                        }

                    }
                    else if (data.attributes.nodeType == 'PMOBJECTTYPE') {
                        if (data.attributes.typeObject.name != "") {
                            promise = PMObjectTypeService.updatePmType(data.attributes.typeObject);
                        } else {
                            $rootScope.showWarningMessage(typeCannotBeEmpty);
                            data.attributes.typeObject.name = vm.doubleClickType;
                            promise = PMObjectTypeService.updatePmType(data.attributes.typeObject);
                        }

                    } else if (data.attributes.nodeType == 'REQUIREMENTTYPE') {
                        if (data.attributes.typeObject.name != "") {
                           // promise = ClassificationService.updateType('REQUIREMENTTYPE', data.attributes.typeObject);
                            promise = PMObjectTypeService.updateReqType(data.attributes.typeObject);
                        } else {
                            $rootScope.showWarningMessage(typeCannotBeEmpty);
                            data.attributes.typeObject.name = vm.doubleClickType;
                            promise = PMObjectTypeService.updateReqType(data.attributes.typeObject);
                        }

                    } else if (data.attributes.nodeType == 'REQUIREMENTDOCUMENTTYPE') {
                        if (data.attributes.typeObject.name != "") {
                            promise = PMObjectTypeService.updateReqDocType(data.attributes.typeObject);
                        } else {
                            $rootScope.showWarningMessage(typeCannotBeEmpty);
                            data.attributes.typeObject.name = vm.doubleClickType;
                            promise = PMObjectTypeService.updateReqDocType(data.attributes.typeObject);
                        }

                    }
                    else if (data.attributes.nodeType == 'MACHINETYPE') {
                        if (data.attributes.typeObject.name != "") {
                            promise = MESObjectTypeService.updateMachineType(data.attributes.typeObject);
                        } else {
                            $rootScope.showWarningMessage(typeCannotBeEmpty);
                            data.attributes.typeObject.name = vm.doubleClickType;
                            promise = MESObjectTypeService.updateMachineType(data.attributes.typeObject);
                        }

                    } else if (data.attributes.nodeType == 'TOOLTYPE') {
                        if (data.attributes.typeObject.name != "") {
                            promise = MESObjectTypeService.updateToolType(data.attributes.typeObject);
                        } else {
                            $rootScope.showWarningMessage(typeCannotBeEmpty);
                            data.attributes.typeObject.name = vm.doubleClickType;
                            promise = MESObjectTypeService.updateToolType(data.attributes.typeObject);
                        }

                    } else if (data.attributes.nodeType == 'SPAREPARTTYPE') {
                        if (data.attributes.typeObject.name != "") {
                            promise = MESObjectTypeService.updateSparePartType(data.attributes.typeObject);
                        } else {
                            $rootScope.showWarningMessage(typeCannotBeEmpty);
                            data.attributes.typeObject.name = vm.doubleClickType;
                            promise = MESObjectTypeService.updateSparePartType(data.attributes.typeObject);
                        }

                    } else if (data.attributes.nodeType == 'ASSETTYPE') {
                        if (data.attributes.typeObject.name != "") {
                            promise = MESObjectTypeService.updateAssetType(data.attributes.typeObject);
                        } else {
                            $rootScope.showWarningMessage(typeCannotBeEmpty);
                            data.attributes.typeObject.name = vm.doubleClickType;
                            promise = MESObjectTypeService.updateAssetType(data.attributes.typeObject);
                        }

                    } else if (data.attributes.nodeType == 'METERTYPE') {
                        if (data.attributes.typeObject.name != "") {
                            promise = MESObjectTypeService.updateMeterType(data.attributes.typeObject);
                        } else {
                            $rootScope.showWarningMessage(typeCannotBeEmpty);
                            data.attributes.typeObject.name = vm.doubleClickType;
                            promise = MESObjectTypeService.updateMeterType(data.attributes.typeObject);
                        }

                    } else if (data.attributes.nodeType == 'WORKREQUESTTYPE') {
                        if (data.attributes.typeObject.name != "") {
                            promise = MESObjectTypeService.updateWorkRequestType(data.attributes.typeObject);
                        } else {
                            $rootScope.showWarningMessage(typeCannotBeEmpty);
                            data.attributes.typeObject.name = vm.doubleClickType;
                            promise = MESObjectTypeService.updateWorkRequestType(data.attributes.typeObject);
                        }

                    } else if (data.attributes.nodeType == 'WORKORDERTYPE') {
                        if (data.attributes.typeObject.name != "") {
                            promise = MESObjectTypeService.updateWorkOrderType(data.attributes.typeObject);
                        } else {
                            $rootScope.showWarningMessage(typeCannotBeEmpty);
                            data.attributes.typeObject.name = vm.doubleClickType;
                            promise = MESObjectTypeService.updateWorkOrderType(data.attributes.typeObject);
                        }

                    } else if (data.attributes.nodeType == 'MATERIALTYPE') {
                        if (data.attributes.typeObject.name != "") {
                            promise = MESObjectTypeService.updateMaterialType(data.attributes.typeObject);
                        } else {
                            $rootScope.showWarningMessage(typeCannotBeEmpty);
                            data.attributes.typeObject.name = vm.doubleClickType;
                            promise = MESObjectTypeService.updateMaterialType(data.attributes.typeObject);
                        }

                    } else if (data.attributes.nodeType == 'JIGFIXTURERTYPE') {
                        if (data.attributes.typeObject.name != "") {
                            promise = MESObjectTypeService.updateJigsFixtureType(data.attributes.typeObject);
                        } else {
                            $rootScope.showWarningMessage(typeCannotBeEmpty);
                            data.attributes.typeObject.name = vm.doubleClickType;
                            promise = MESObjectTypeService.updateJigsFixtureType(data.attributes.typeObject);
                        }

                    }

                    else if (data.attributes.nodeType == 'WORKCENTERTYPE') {
                        if (data.attributes.typeObject.name != "") {
                            promise = MESObjectTypeService.updateWorkCenterType(data.attributes.typeObject);
                        } else {
                            $rootScope.showWarningMessage(typeCannotBeEmpty);
                            data.attributes.typeObject.name = vm.doubleClickType;
                            promise = MESObjectTypeService.updateWorkCenterType(data.attributes.typeObject);
                        }

                    }
                    else if (data.attributes.nodeType == 'WORKFLOWTYPE') {
                        if (data.attributes.typeObject.name != "") {
                            promise = ClassificationService.updateType('WORKFLOWTYPE', data.attributes.typeObject);
                        } else {
                            $rootScope.showWarningMessage(typeCannotBeEmpty);
                            data.attributes.typeObject.name = vm.doubleClickType;
                            promise = ClassificationService.updateType('WORKFLOWTYPE', data.attributes.typeObject);
                        }

                    }
                    else if (data.attributes.nodeType == 'REQUIREMENTTYPE') {
                        if (data.attributes.typeObject.name != "") {
                            promise = ClassificationService.createType('REQUIREMENTTYPE', data.attributes.typeObject);
                        } else {
                            $rootScope.showWarningMessage(typeCannotBeEmpty);
                            classificationTree.tree('remove', node.target);
                            deleted = true;
                        }
                    }

                    else if (data.attributes.nodeType == 'SPECIFICATIONTYPE') {
                        if (data.attributes.typeObject.name != "") {
                            promise = ClassificationService.createType('SPECIFICATIONTYPE', data.attributes.typeObject);
                        } else {
                            $rootScope.showWarningMessage(typeCannotBeEmpty);
                            classificationTree.tree('remove', node.target);
                            deleted = true;
                        }
                    } else if (data.attributes.nodeType == 'QUALITY_TYPE') {
                        if (data.attributes.typeObject.name != "") {
                            if (data.attributes.typeObject != null && data.attributes.typeObject != "" && data.attributes.typeObject != undefined) {
                                if (data.attributes.typeObject.qualityType == "PRODUCTINSPECTIONPLANTYPE") {
                                    promise = QualityTypeService.updateProductInspectionPlanType(data.attributes.typeObject);
                                } else if (data.attributes.typeObject.qualityType == "MATERIALINSPECTIONPLANTYPE") {
                                    promise = QualityTypeService.updateMaterialInspectionPlanType(data.attributes.typeObject);
                                } else if (data.attributes.typeObject.qualityType == "PRTYPE") {
                                    promise = QualityTypeService.updatePrType(data.attributes.typeObject);
                                } else if (data.attributes.typeObject.qualityType == "NCRTYPE") {
                                    promise = QualityTypeService.updateNcrType(data.attributes.typeObject);
                                } else if (data.attributes.typeObject.qualityType == "QCRTYPE") {
                                    promise = QualityTypeService.updateQcrType(data.attributes.typeObject);
                                } else if (data.attributes.typeObject.qualityType == "PPAPTYPE") {
                                    promise = QualityTypeService.updatePpapType(data.attributes.typeObject);
                                } else if (data.attributes.typeObject.qualityType == "SUPPLIERAUDITTYPE") {
                                    promise = QualityTypeService.updateSupplierAuditType(data.attributes.typeObject);
                                }
                            }

                        } else {
                            $rootScope.showWarningMessage(typeCannotBeEmpty);
                            classificationTree.tree('beginEdit', node.target);

                            $timeout(function () {
                                $('.tree-editor').focus().select();
                            }, 1);
                        }

                    }

                }
                if (promise != null) {
                    promise.then(
                        function (type) {
                            type.editable = true;
                            node.text = type.name;
                            node.add = $rootScope.hasPermission(type.objectType.toLowerCase(), 'create');
                            node.delete = $rootScope.hasPermission(type.objectType.toLowerCase(), 'delete');
                            $rootScope.showSuccessMessage(savedMessage);
                            vm.doubleClickType = null;
                            $rootScope.newItemTypeCreating = false;
                            if (data.attributes.nodeType == 'CUSTOMOBJECTTYPE') {
                                $rootScope.loadCustomObjectTypes();
                            }
                            data.attributes.typeObject = angular.copy(type);
                            classificationTree.tree('update', {target: node.target, attributes: data.attributes});
                            classificationTree.tree('select', node.target);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    );
                }
                else if (!deleted) {
                    classificationTree.tree('select', node.target);
                }

            }

            function updateItemInfo(itemType) {
                if (itemType.itemNumberSource == null) {
                    AutonumberService.getAutonumberByName('Default Item Number Source').then(
                        function (data) {
                            itemType.itemNumberSource = data;
                            if (vm.selectedNode == 'ITEMTYPE') {
                                return ClassificationService.updateType('ITEMTYPE', itemType);
                            } else if (vm.selectedNode == 'CHANGETYPE') {
                                return ClassificationService.updateType('CHANGETYPE', itemType);
                            }
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    ).then(
                        function (data) {
                            if (vm.selectedNode == 'ITEMTYPE') {
                                if (itemType.revisionSequence == null) {
                                    CommonService.getLovByName('Default Revision Sequence').then(
                                        function (data) {
                                            itemType.revisionSequence = data;
                                            //ClassificationService.updateType('ITEMTYPE', itemType);
                                        }
                                    )
                                }
                            }
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
                if (vm.selectedNode == 'ITEMTYPE') {
                    if (itemType.revisionSequence == null) {
                        CommonService.getLovByName('Default Revision Sequence').then(
                            function (data) {
                                itemType.revisionSequence = data;
                                //ClassificationService.updateType('ITEMTYPE', itemType);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        ).then(
                            function (data) {
                                if (itemType.lifecycle == null) {
                                    ItemTypeService.getLifeCycleByName('Default').then(
                                        function (data) {
                                            itemType.lifecycle = data;
                                            return ClassificationService.updateType('ITEMTYPE', itemType);
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    )
                                }
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                }
            }


            function setupTimeout(arg, data) {
                $timeout(function () {
                    vm.title = $('#' + nodeSelected.domId + ' .tree-title').outerWidth();
                    vm.indent = $('#' + nodeSelected.domId + ' .tree-indent').outerWidth();
                    vm.icon = $('#' + nodeSelected.domId + ' .tree-icon').outerWidth();
                    if (vm.title != null && vm.title > 400) {
                        // $('#' + node.domId + '.tree-node-selected').width(vm.title + (2 * vm.indent) + vm.icon);
                        $('.tree-node-selected').width(vm.title + (4 * vm.indent) + vm.icon);
                    }else {
                        $('.tree-node-selected').width(400);
                    }

                    $rootScope.$broadcast(arg, data);
                    $rootScope.type = data.typeObject;
                    vm.loading = false;
                }, 100);

            }

            function intializeNodeObjects() {
                $rootScope.selectedQualityTypeNode = null;
                $rootScope.selectedItemType = null;
                $rootScope.selectedChangeType = null;
                $rootScope.selectedItemTypeNodId = null;
                $rootScope.selectedChangeNodId = null;
                $rootScope.selectedQualityType = null;
                $rootScope.selectedWfType = null;
                $rootScope.selectedmfrType = null;
                $rootScope.selectedmfrTypeNodId = null;
                $rootScope.selectedCustomTypeNodId = null;
                $rootScope.selectedmfrPartType = null;
                $rootScope.selectedmfrPartTypeNodId = null;
                $rootScope.selectedSupplierType = null;
                $rootScope.selectedSupplierTypeNodId = null;
                $rootScope.selectedWfTypeNodeId = null;
            }

            vm.loading = false;
            vm.type = null;
            vm.objectTypeAttributes = false;
            $rootScope.type = null;
            $rootScope.selectedTypeObject = null;
            vm.selectedNodeType = null;
            vm.nodeSelected = null
            function onSelectType(node, flag) {
                nodeSelected = node;
                $("#contextMenu").hide();
                vm.loading = false;
                vm.type = null;
                intializeNodeObjects();
                $rootScope.type = null;
                var data = classificationTree.tree('getData', node.target);
                var typeObject = data.attributes.typeObject;
                vm.objectTypeAttributes = false;
                vm.selectedData = data;
                if (data.attributes.nodeType == 'ROOT' || typeObject == null || typeObject === undefined) {
                    $scope.$evalAsync();
                    if (data.attributes.nodeType != 'ROOT' && data.attributes.nodeType != 'CUSTOMNODE' && data.attributes.nodeType != 'SOURCING'
                        && data.attributes.nodeType != 'PMOBJECTTYPE' && data.attributes.nodeType != 'WORKORDERNODE' && data.attributes.nodeType != 'MASTERDATANODE' && data.attributes.nodeType != 'MCONODE') {
                        vm.objectTypeAttributes = true;
                        $rootScope.selectedObjectType = angular.copy(node);
                        setupTimeout('app.classification.object.attribute', {typeObject: node, nodeId: node.id});
                        $scope.$evalAsync();
                    }
                } else {
                    vm.loading = true;
                    vm.selectedNodeType = node;
                    $rootScope.closeNotification();
                    vm.type = data.attributes.nodeType;
                    $rootScope.selectedClassificationType = angular.copy(typeObject);
                    setupTimeout('app.objectType.selected', {typeObject: typeObject, nodeId: node.id});
                    if (vm.type == 'ITEMTYPE') {
                        $rootScope.selectedItemType = typeObject;
                        $rootScope.selectedItemTypeNodId = node.id;
                        setupTimeout('app.itemType.selected', {typeObject: typeObject, nodeId: node.id});
                    } else if (vm.type == 'CHANGETYPE') {
                        if (typeObject != undefined && typeObject != null) {
                            if (typeObject.objectType == 'ECOTYPE') {
                                data.attributes.typeObject['@type'] = "PLMECOType";
                                $rootScope.selectedChangeType = typeObject;
                                $rootScope.selectedChangeNodId = node.id;
                            }
                            if (typeObject.objectType == 'ECRTYPE') {
                                $rootScope.selectedChangeType = typeObject;
                                $rootScope.selectedChangeNodId = node.id;
                                data.attributes.typeObject['@type'] = "PLMECRType";
                            }
                            if (typeObject.objectType == 'DCOTYPE') {
                                $rootScope.selectedChangeType = typeObject;
                                $rootScope.selectedChangeNodId = node.id;
                                data.attributes.typeObject['@type'] = "PLMDCOType";
                            }
                            if (typeObject.objectType == 'DCRTYPE') {
                                $rootScope.selectedChangeType = typeObject;
                                $rootScope.selectedChangeNodId = node.id;
                                data.attributes.typeObject['@type'] = "PLMDCRType";
                            }
                            if (typeObject.objectType == 'DEVIATIONTYPE') {
                                $rootScope.selectedChangeType = typeObject;
                                $rootScope.selectedChangeNodId = node.id;
                                data.attributes.typeObject['@type'] = "PLMDeviationType";
                            }
                            if (typeObject.objectType == 'WAIVERTYPE') {
                                $rootScope.selectedChangeType = typeObject;
                                $rootScope.selectedChangeNodId = node.id;
                                data.attributes.typeObject['@type'] = "PLMWaiverType";
                            }
                            if (typeObject.objectType == 'MCOTYPE') {
                                $rootScope.selectedChangeType = typeObject;
                                $rootScope.selectedChangeNodId = node.id;
                                data.attributes.typeObject['@type'] = "PLMMCOType";
                            }
                            setupTimeout('app.changeType.selected', {typeObject: typeObject, nodeId: node.id});
                        }

                    }
                    else if (vm.type == 'MANUFACTURERTYPE') {
                        $rootScope.selectedmfrType = typeObject;
                        $rootScope.selectedmfrTypeNodId = node.id;
                        setupTimeout('app.mfrType.selected', {typeObject: typeObject, nodeId: node.id});
                    }
                    else if (vm.type == 'MANUFACTURERPARTTYPE') {
                        $rootScope.selectedmfrPartType = typeObject;
                        $rootScope.selectedmfrPartTypeNodId = node.id;
                        setupTimeout('app.mfrPartType.selected', {typeObject: typeObject, nodeId: node.id});
                    }
                    else if (vm.type == 'CUSTOMOBJECTTYPE') {
                        $rootScope.selectedCustomType = typeObject;
                        $rootScope.selectedCustomTypeNodId = node.id;
                        setupTimeout('app.customObjectType.selected', {typeObject: typeObject, nodeId: node.id});
                    }
                    else if (vm.type == 'WORKFLOWTYPE') {
                        $rootScope.selectedWfType = typeObject;
                        $rootScope.selectedWfTypeNodeId = node.id;
                        setupTimeout('app.workflowType.selected', {typeObject: typeObject, nodeId: node.id});
                    }
                    /* else if (vm.type == 'REQUIREMENTTYPE' || vm.type == 'SPECIFICATIONTYPE') {
                     setupTimeout('app.rmType.selected', {typeObject: typeObject, nodeId: node.id});
                     }*/
                    else if (vm.type == 'QUALITY_TYPE') {
                        $rootScope.selectedQualityType = typeObject;
                        $rootScope.selectedQualityTypeNode = node.id;
                        setupTimeout('app.qualityType.selected', {typeObject: typeObject, nodeId: node.id});
                    }
                    else if (vm.type == 'JIGFIXTURETYPE' || vm.type == 'TOOLTYPE' || vm.type == 'MANPOWERTYPE' || vm.type == 'MATERIALTYPE' || vm.type == 'PLANTTYPE' || vm.type == 'MACHINETYPE' ||
                        vm.type == 'WORKCENTERTYPE' || vm.type == 'OPERATIONTYPE' || vm.type == 'PRODUCTIONORDERTYPE' || vm.type == 'SPAREPARTTYPE' || vm.type == 'MBOMTYPE' || vm.type == 'BOPTYPE'
                        || vm.type == 'EQUIPMENTTYPE' || vm.type == 'INSTRUMENTTYPE' || vm.type == 'WORKREQUESTTYPE' || vm.type == 'WORKORDERTYPE'
                        || vm.type == 'ASSETTYPE' || vm.type == 'METERTYPE' || vm.type == 'SUPPLIERTYPE' || vm.type == 'ASSEMBLYLINETYPE') {
                        $rootScope.selectedProductionResType = typeObject;
                        $rootScope.selectedResourceTypeNode = node.id;
                        setupTimeout('app.mesObjectType.selected', {typeObject: typeObject, nodeId: node.id});
                    } else if (vm.type == 'PGCSUBSTANCETYPE' || vm.type == 'PGCSUBSTANCEGROUPTYPE' || vm.type == 'PGCSPECIFICATIONTYPE' || vm.type == 'PGCDECLARATIONTYPE') {
                        $rootScope.selectedPgcObjectType = typeObject;
                        $rootScope.selectedPgcObjectTypeNode = node.id;
                        setupTimeout('app.pgcObjectType.selected', {typeObject: typeObject, nodeId: node.id});
                    } else if (vm.type == 'PMOBJECTTYPE' || vm.type == 'REQUIREMENTTYPE' || vm.type == 'REQUIREMENTDOCUMENTTYPE') {
                        $rootScope.selectedObjectType = typeObject;
                        $rootScope.selectedObjectTypeNode = node.id;
                        setupTimeout('app.pmType.selected', {typeObject: typeObject, nodeId: node.id});
                    }

                    $scope.$evalAsync();
                    if (!$scope.$$phase) $scope.$apply();
                }

            }

            var addTypeText = parsed.html($translate.instant("ADD_TYPE")).html();

            function onContextMenu(e, node) {
                e.preventDefault();
                var $contextMenu = $("#contextMenu");
                $('#classificationTree').tree('select', node.target);
                var parent = classificationTree.tree('getData', node.target);
                if (node.attributes.typeObject != undefined && node.attributes.typeObject != null && node.attributes.nodeType == "ITEMTYPE") {
                    if (node.attributes.typeObject.parentType == null && node.attributes.typeObject.itemClass != 'OTHER') {
                        $('#addType').show();
                        $('#deleteType').hide();
                        if (node.add) $contextMenu.show();
                        else $contextMenu.hide();
                    } else if (node.attributes.typeObject.parentType != null && node.attributes.typeObject.itemClass != 'OTHER') {
                        $('#addType').show();
                        $('#deleteType').show();
                        if (node.add) $contextMenu.show();
                        else $contextMenu.hide();
                    } else if (node.attributes.typeObject.itemClass == 'OTHER') {
                        $('#addType').show();
                        $('#deleteType').show();
                        $contextMenu.show();
                    }
                }
                if (node.attributes.typeObject != undefined && node.attributes.typeObject != null && node.attributes.typeObject.parentType == null && node.attributes.nodeType == "CHANGETYPE") {
                    $('#addType').show();
                    $('#deleteType').hide();
                    if (node.add) $contextMenu.show();
                    else $contextMenu.hide();
                } else if (node.attributes.typeObject != undefined && node.attributes.typeObject != null && node.attributes.typeObject.parentType != null && node.attributes.nodeType == "CHANGETYPE") {
                    $('#addType').show();
                    $('#deleteType').show();
                    if (node.add) $contextMenu.show();
                    else $contextMenu.hide();
                }
                if (node.attributes.root != true && parent.attributes.nodeType == "WORKFLOWTYPE") {
                    $('#addType').hide();
                    $('#deleteType').show();
                    if (node.delete) $contextMenu.show();
                    else $contextMenu.hide();
                }
                if (node.attributes.root == true) {
                    $('#addType').show();
                    $('#deleteType').hide();
                    if (node.add) $contextMenu.show();
                    else $contextMenu.hide();
                }

                if (node.attributes.root != true && parent.attributes.nodeType == "CUSTOMOBJECTTYPE") {
                    $('#addType').show();
                    $('#deleteType').show();
                    if (node.delete) $contextMenu.show();
                    else $contextMenu.hide();
                }

                if (node.attributes.root == true && node.attributes.nodeType == "CHANGETYPE") {
                    $('#addType').hide();
                    $('#deleteType').hide();
                    $contextMenu.hide();
                }
                if (node.attributes.nodeType == 'RMOBJECTTYPE' && node.attributes.root == true) {
                    $contextMenu.hide();
                }

                if (node.attributes.root == undefined && parent.id === 0) {
                    $contextMenu.hide();
                }

                if (node.attributes.nodeType == "QUALITY_TYPE") {
                    if (node.attributes.root) {
                        $contextMenu.hide();
                    }

                    if (node.attributes.typeObject != undefined && node.attributes.typeObject != null && node.attributes.typeObject.parentType == null) {
                        $('#addType').show();
                        $('#deleteType').hide();
                        if (node.add) $contextMenu.show();
                        else $contextMenu.hide();
                    } else if (node.attributes.typeObject != undefined && node.attributes.typeObject != null && node.attributes.typeObject.parentType != null) {
                        $('#addType').show();
                        $('#deleteType').show();
                        if (node.add) $contextMenu.show();
                        else $contextMenu.hide();
                    }
                }
                if (node.attributes.nodeType == 'JIGFIXTURETYPE' || node.attributes.nodeType == 'TOOLTYPE' || node.attributes.nodeType == 'MANPOWERTYPE'
                    || node.attributes.nodeType == 'MATERIALTYPE' || node.attributes.nodeType == 'PLANTTYPE' || node.attributes.nodeType == 'SPAREPARTTYPE'
                    || node.attributes.nodeType == 'MACHINETYPE' || node.attributes.nodeType == 'WORKCENTERTYPE' || node.attributes.nodeType == 'OPERATIONTYPE'
                    || node.attributes.nodeType == 'PRODUCTIONORDERTYPE' || node.attributes.nodeType == 'MBOMTYPE' || node.attributes.nodeType == 'BOPTYPE' || node.attributes.nodeType == 'WORKREQUESTTYPE' || node.attributes.nodeType == 'WORKORDERTYPE'
                    || node.attributes.nodeType == 'ASSETTYPE' || node.attributes.nodeType == 'METERTYPE' || node.attributes.nodeType == 'INSTRUMENTTYPE'
                    || node.attributes.nodeType == 'EQUIPMENTTYPE' || node.attributes.nodeType == 'ASSEMBLYLINETYPE') {
                    if (node.attributes.root) {
                        $contextMenu.hide();
                    }
                    if (node.attributes.typeObject != undefined && node.attributes.typeObject != null && node.attributes.typeObject.parentType == null) {
                        $('#addType').show();
                        $('#deleteType').hide();
                        if (node.add) $contextMenu.show();
                        else $contextMenu.hide();
                    } else if (node.attributes.typeObject != undefined && node.attributes.typeObject != null && node.attributes.typeObject.parentType != null) {
                        $('#addType').show();
                        $('#deleteType').show();
                        if (node.add) $contextMenu.show();
                        else $contextMenu.hide();
                    }
                }
                if (node.attributes.nodeType == 'PGCSUBSTANCETYPE' || node.attributes.nodeType == 'PGCSUBSTANCEGROUPTYPE' || node.attributes.nodeType == 'PGCSPECIFICATIONTYPE'
                    || node.attributes.nodeType == 'PGCDECLARATIONTYPE' || node.attributes.nodeType == 'PGCOBJECTTYPE') {
                    if (node.attributes.root) {
                        $contextMenu.hide();
                    }
                    if (node.attributes.typeObject != undefined && node.attributes.typeObject != null && node.attributes.typeObject.parentType == null) {
                        $('#addType').show();
                        $('#deleteType').hide();
                        if (node.add) $contextMenu.show();
                        else $contextMenu.hide();
                    } else if (node.attributes.typeObject != undefined && node.attributes.typeObject != null && node.attributes.typeObject.parentType != null) {
                        $('#addType').show();
                        $('#deleteType').show();
                        if (node.add) $contextMenu.show();
                        else $contextMenu.hide();
                    }
                }

                if (node.attributes.nodeType == "MESOBJECTTYPE" || node.attributes.nodeType == "MROOBJECTTYPE") {
                    $('#addType').hide();
                    $('#deleteType').hide();
                    $contextMenu.hide();
                }

                if (node.attributes.typeObject != undefined && node.attributes.typeObject != null && node.attributes.typeObject.parentType == null &&
                    (node.attributes.nodeType == "MANUFACTURERTYPE" || node.attributes.nodeType == "MANUFACTURERPARTTYPE")) {
                    $('#addType').show();
                    $('#deleteType').show();
                    if (node.add) $contextMenu.show();
                    else $contextMenu.hide();
                }
                if (node.attributes.nodeType == "SOURCING") {
                    $('#addType').hide();
                    $('#deleteType').hide();
                    $contextMenu.hide();
                } else if (node.attributes.typeObject != undefined && node.attributes.typeObject != null && node.attributes.typeObject.parentType != null) {
                    $('#addType').show();
                    $('#deleteType').show();
                    if (node.add) $contextMenu.show();
                    else $contextMenu.hide();
                }
                $contextMenu.css({
                    left: e.pageX,
                    top: e.pageY
                });
                if (node.attributes.typeObject != undefined && node.attributes.typeObject != null && node.attributes.typeObject.parentType == null &&
                    (node.attributes.nodeType == "SUPPLIERTYPE" )) {
                    $('#addType').show();
                    $('#deleteType').show();
                    if (node.add) $contextMenu.show();
                    else $contextMenu.hide();
                }
                if (node.attributes.root == true && (node.attributes.nodeType == 'REQUIREMENTTYPE' || node.attributes.nodeType == 'REQUIREMENTDOCUMENTTYPE')) {
                    $('#addType').show();
                    $('#deleteType').hide();
                    if (node.add) $contextMenu.show();
                    else $contextMenu.hide();
                } else if (node.attributes.root != true && (node.attributes.nodeType == 'REQUIREMENTTYPE' || node.attributes.nodeType == 'REQUIREMENTDOCUMENTTYPE')) {
                    $('#addType').hide();
                    $('#deleteType').show();
                    if (node.delete) $contextMenu.show();
                    else $contextMenu.hide();
                }
                if (node.attributes.root == true && node.attributes.nodeType == 'PMOBJECTTYPE') {
                    $('#addType').show();
                    $('#deleteType').hide();
                    if (node.add) $contextMenu.show();
                    else $contextMenu.hide();
                } else if (node.attributes.root != true && node.attributes.nodeType == 'PMOBJECTTYPE') {
                    $('#addType').show();
                    if (node.attributes.typeObject.parent == null) {
                        $('#deleteType').hide();
                    } else {
                        $('#deleteType').show();
                    }
                    if (node.delete) $contextMenu.show();
                    else $contextMenu.hide();
                }
                $contextMenu.on("click", "a", function () {
                    $contextMenu.hide();
                });
            }

            var newItem = $translate.instant("NEW_ITEM");
            var newCustom = parsed.html($translate.instant("NEW_CUSTOM")).html();
            var newChange = parsed.html($translate.instant("NEW_CHANGE")).html();
            var newRequirement = parsed.html($translate.instant("NEW_REQUIREMENT")).html();
            var newSpec = parsed.html($translate.instant("NEW_SPECIFICATION")).html();

            var newManufacturer = $translate.instant("NEW_MANUFACTURER");
            var newMfrPart = $translate.instant("NEW_MANUFACTURER_PART");
            var newRoot = $translate.instant("NEW_ROOT");
            var newToolType = $translate.instant("NEW_TOOL_TYPE");
            var newWorkflow = $translate.instant("NEW_WORKFLOW");
            var newInspectionType = $translate.instant("NEW_INSPECTION_TYPE");
            var newProblemReportType = $translate.instant("NEW_PR_TYPE");
            var newNcrType = $translate.instant("NEW_NCR_TYPE");
            var newQcrType = $translate.instant("NEW_QCR_TYPE");
            var newPpapType = $translate.instant("NEW_PPAP_TYPE");
            var newSupplierAuditType = $translate.instant("NEW_SUPPLIER_AUDIT_TYPE");
            var newPlant = $translate.instant("NEW_PLANT_TYPE");
            var newAssemblyType = $translate.instant("NEW_ASSEMBLY_TYPE");
            var newWorkCenter = $translate.instant("NEW_WORKCENTER_TYPE");
            var newMachine = $translate.instant("NEW_MACHINE_TYPE");
            var newTool = $translate.instant("NEW_TOOL_TYPE");
            var newShift = $translate.instant("NEW_SHIFT_TYPE");
            var newManpower = $translate.instant("NEW_MANPOWER_TYPE");
            var newMaterial = $translate.instant("NEW_MATERIAL_TYPE");
            var newJigsFixture = $translate.instant("NEW_JIGS_FIXTURE_TYPE");
            var newOperation = $translate.instant("NEW_OPERATION_TYPE");
            var newProductionOrder = $translate.instant("NEW_PRODUCTION_ORDER");
            var newMbom = $translate.instant("NEW_MBOM");
            var newBop = $translate.instant("NEW_BOP");
            var newServiceOrder = $translate.instant("NEW_SERVICE_ORDER");
            var newEquipment = $translate.instant("NEW_EQUIPMENT_TYPE");
            var newInstrument = $translate.instant("NEW_INSTRUMENT_TYPE");
            var newType = $translate.instant("NEW_TYPE");
            var newSparePart = $translate.instant("NEW_SPARE_PART_TYPE");
            var newAsset = $translate.instant("NEW_ASSET_TYPE");
            var newMeter = $translate.instant("NEW_METER_TYPE");
            var newWorkRequest = $translate.instant("NEW_WORK_REQUEST_TYPE");
            var newWorkOrder = $translate.instant("NEW_WORK_ORDER_TYPE");
            var newProjectType = $translate.instant("NEW_PROJECT_TYPE");
            var newProgramType = $translate.instant("NEW_PROGRAM_TYPE");
            var newPhaseType = $translate.instant("NEW_PHASE_TYPE");
            var newActivityType = $translate.instant("NEW_ACTIVITY_TYPE");
            var newMilestoneType = $translate.instant("NEW_MILESTONE_TYPE");
            var newTaskType = $translate.instant("NEW_TASK_TYPE");

            $rootScope.newItemTypeCreating = false;

            function addType() {
                var selectedNode = classificationTree.tree('getSelected');
                $rootScope.newItemTypeCreating = true;
                if (selectedNode.attributes.nodeType == 'ITEMTYPE') {
                    addClsType('itemtype-node', newItem);
                }
                else if (selectedNode.attributes.nodeType == 'CUSTOMOBJECTTYPE') {
                    addClsType('custom-node', newCustom);
                }
                else if (selectedNode.attributes.nodeType == 'CHANGETYPE') {
                    addClsType('change-node', newChange);
                }
                /*  else if (selectedNode.attributes.nodeType == 'REQUIREMENTTYPE') {
                 addClsType('spec-node', newRequirement);
                 }
                 else if (selectedNode.attributes.nodeType == 'SPECIFICATIONTYPE') {
                 addClsType('spec-node', newSpec);
                 }*/
                else if (selectedNode.attributes.nodeType == 'MANUFACTURERTYPE') {
                    addClsType('mfr-node', newManufacturer);
                }
                else if (selectedNode.attributes.nodeType == 'SUPPLIERTYPE') {
                    addClsType('supplierType-node', 'New Supplier');
                }
                else if (selectedNode.attributes.nodeType == 'MANUFACTURERPARTTYPE') {
                    addClsType('mfrpart-node', newMfrPart);
                }
                else if (selectedNode.attributes.nodeType == 'OPERATIONTYPE') {
                    addClsType('operation-node', newOperation);
                }
                else if (selectedNode.attributes.nodeType == 'WORKFLOWTYPE') {
                    addClsType('workflow-node', newWorkflow);
                }
                else if (selectedNode.attributes.nodeType == 'QUALITY_TYPE') {
                    var nodeName = null;
                    if (selectedNode.attributes.typeObject.qualityType == "PRODUCTINSPECTIONPLANTYPE" || selectedNode.attributes.typeObject.qualityType == "MATERIALINSPECTIONPLANTYPE") {
                        nodeName = newInspectionType;
                    } else if (selectedNode.attributes.typeObject.qualityType == "PRTYPE") {
                        nodeName = newProblemReportType;
                    } else if (selectedNode.attributes.typeObject.qualityType == "NCRTYPE") {
                        nodeName = newNcrType;
                    } else if (selectedNode.attributes.typeObject.qualityType == "QCRTYPE") {
                        nodeName = newQcrType;
                    } else if (selectedNode.attributes.typeObject.qualityType == "PPAPTYPE") {
                        nodeName = newPpapType;
                    } else if (selectedNode.attributes.typeObject.qualityType == "SUPPLIERAUDITTYPE") {
                        nodeName = newSupplierAuditType;
                    }
                    addClsType('quality-node', nodeName);
                }
                else if (selectedNode.attributes.nodeType == "MATERIALTYPE") {
                    addClsType('material-node', newMaterial);
                } else if (selectedNode.attributes.nodeType == "TOOLTYPE") {
                    addClsType('tool-node', newTool);
                } else if (selectedNode.attributes.nodeType == "JIGFIXTURETYPE") {
                    addClsType('jigs-node', newJigsFixture);
                }
                else if (selectedNode.attributes.nodeType == 'PLANTTYPE') {
                    addClsType('plant-node', newPlant);
                }
                else if (selectedNode.attributes.nodeType == 'ASSEMBLYLINETYPE') {
                    addClsType('assemblyLine-node', newAssemblyType);
                }
                else if (selectedNode.attributes.nodeType == 'SPAREPARTTYPE') {
                    addClsType('sparepart-node', newSparePart);
                }
                else if (selectedNode.attributes.nodeType == 'ASSETTYPE') {
                    addClsType('asset-node', newAsset);
                }
                else if (selectedNode.attributes.nodeType == 'METERTYPE') {
                    addClsType('meter-node', newMeter);
                }
                else if (selectedNode.attributes.nodeType == 'WORKREQUESTTYPE') {
                    addClsType('workRequest-node', newWorkRequest);
                }
                else if (selectedNode.attributes.nodeType == 'WORKORDERTYPE') {
                    addClsType('workOrder-node', newWorkOrder);
                }
                else if (selectedNode.attributes.nodeType == 'WORKCENTERTYPE') {
                    addClsType('workCenter-node', newWorkCenter);
                }
                else if (selectedNode.attributes.nodeType == 'MACHINETYPE') {
                    addClsType('machine-node', newMachine);
                }
                else if (selectedNode.attributes.nodeType == 'PRODUCTIONORDERTYPE') {
                    addClsType('production-node', newProductionOrder);
                }
                else if (selectedNode.attributes.nodeType == 'MBOMTYPE') {
                    addClsType('mbom-node', newMbom);
                }
                else if (selectedNode.attributes.nodeType == 'BOPTYPE') {
                    addClsType('bop-node', newBop);
                }
                else if (selectedNode.attributes.nodeType == 'MANPOWERTYPE') {
                    addClsType('manpower-node', newManpower);
                }
                else if (selectedNode.attributes.nodeType == 'EQUIPMENTTYPE') {
                    addClsType('equipment-node', newEquipment);
                }
                else if (selectedNode.attributes.nodeType == 'INSTRUMENTTYPE') {
                    addClsType('instrument-node', newInstrument);
                }
                else if (selectedNode.attributes.nodeType == 'PMOBJECTTYPE') {
                    var nodeName = null;
                    if (selectedNode.attributes.typeObject.type == "PROJECT") {
                        nodeName = newProjectType;
                    } else if (selectedNode.attributes.typeObject.type == "PROGRAM") {
                        nodeName = newProgramType;
                    } else if (selectedNode.attributes.typeObject.type == "PROJECTPHASEELEMENT") {
                        nodeName = newPhaseType;
                    } else if (selectedNode.attributes.typeObject.type == "PROJECTACTIVITY") {
                        nodeName = newActivityType;
                    } else if (selectedNode.attributes.typeObject.type == "PROJECTTASK") {
                        nodeName = newTaskType;
                    } else if (selectedNode.attributes.typeObject.type == "PROJECTMILESTONE") {
                        nodeName = newMilestoneType;
                    }
                    addClsType('project-node', nodeName);
                }
                else if (selectedNode.attributes.nodeType == 'REQUIREMENTTYPE') {
                    addClsType('req-node', newType);
                }
                else if (selectedNode.attributes.nodeType == 'REQUIREMENTDOCUMENTTYPE') {
                    addClsType('spec-node', newType);
                }
                else if (selectedNode.attributes.nodeType == 'PGCSUBSTANCETYPE') {
                    addClsType('substance-node', newType);
                }
                else if (selectedNode.attributes.nodeType == 'PGCSUBSTANCEGROUPTYPE') {
                    addClsType('subGroup-node', newType);
                }
                else if (selectedNode.attributes.nodeType == 'PGCSPECIFICATIONTYPE') {
                    addClsType('specification-node', newType);
                }
                else if (selectedNode.attributes.nodeType == 'PGCDECLARATIONTYPE') {
                    addClsType('declaration-node', newType);
                }
            }

            function addClsType(icon, newText) {
                var selectedNode = classificationTree.tree('getSelected');

                if (selectedNode != null) {
                    var nodeid = ++nodeId;

                    classificationTree.tree('append', {
                        parent: selectedNode.target,
                        data: [
                            {
                                id: nodeid,
                                iconCls: icon,
                                text: newText,
                                attributes: {
                                    typeObject: null,
                                    nodeType: selectedNode.attributes.nodeType
                                }
                            }
                        ]
                    });

                    if (selectedNode.children.length != null) {
                        var newNode = classificationTree.tree('find', nodeid);
                        classificationTree.tree('expandTo', newNode.target);
                    }

                    var newNode = classificationTree.tree('find', nodeid);
                    if (newNode != null) {
                        classificationTree.tree('beginEdit', newNode.target);

                        $timeout(function () {
                            $('.tree-editor').focus().select();
                        }, 1);
                    }
                }
            }

            function deleteType() {
                $rootScope.closeNotification();
                var selectedNode = classificationTree.tree('getSelected');

                if (selectedNode.attributes.nodeType == 'ITEMTYPE') {
                    deleteClsType();
                }
                else if (selectedNode.attributes.nodeType == 'CUSTOMOBJECTTYPE') {
                    deleteClsType();
                }
                else if (selectedNode.attributes.nodeType == 'CHANGETYPE') {
                    deleteClsType();
                }
                /* else if (selectedNode.attributes.nodeType == 'REQUIREMENTTYPE') {
                 deleteClsType();
                 }
                 else if (selectedNode.attributes.nodeType == 'SPECIFICATIONTYPE') {
                 deleteClsType();
                 }*/
                else if (selectedNode.attributes.nodeType == 'MANUFACTURERTYPE') {
                    deleteClsType();
                } else if (selectedNode.attributes.nodeType == 'MANUFACTURERPARTTYPE') {
                    deleteClsType();
                } else if (selectedNode.attributes.nodeType == 'WORKFLOWTYPE') {
                    deleteClsType();
                } else if (selectedNode.attributes.nodeType == 'QUALITY_TYPE') {
                    deleteClsType();
                } else if (selectedNode.attributes.nodeType == 'MESOBJECTTYPE') {
                    deleteClsType();
                } else if (selectedNode.attributes.nodeType == 'PLANTTYPE' || selectedNode.attributes.nodeType == 'WORKCENTERTYPE' || selectedNode.attributes.nodeType == 'MATERIALTYPE'
                    || selectedNode.attributes.nodeType == 'MACHINETYPE' || selectedNode.attributes.nodeType == 'OPERATIONTYPE' || selectedNode.attributes.nodeType == 'MANPOWERTYPE'
                    || selectedNode.attributes.nodeType == 'PRODUCTIONORDERTYPE' || selectedNode.attributes.nodeType == 'MBOMTYPE' || selectedNode.attributes.nodeType == 'BOPTYPE' || selectedNode.attributes.nodeType == 'TOOLTYPE' || selectedNode.attributes.nodeType == 'INSTRUMENTTYPE'
                    || selectedNode.attributes.nodeType == 'JIGFIXTURETYPE' || selectedNode.attributes.nodeType == 'SPAREPARTTYPE' || selectedNode.attributes.nodeType == 'EQUIPMENTTYPE'
                    || selectedNode.attributes.nodeType == 'WORKORDERTYPE' || selectedNode.attributes.nodeType == 'WORKREQUESTTYPE' || selectedNode.attributes.nodeType == 'ASSETTYPE'
                    || selectedNode.attributes.nodeType == 'METERTYPE' || selectedNode.attributes.nodeType == 'ASSEMBLYLINETYPE') {
                    deleteClsType();
                } else if (selectedNode.attributes.nodeType == 'PMOBJECTTYPE') {
                    deleteClsType();
                } else if (selectedNode.attributes.nodeType == 'REQUIREMENTTYPE') {
                    deleteClsType();
                } else if (selectedNode.attributes.nodeType == 'REQUIREMENTDOCUMENTTYPE') {
                    deleteClsType();
                } else if (selectedNode.attributes.nodeType == 'PGCSUBSTANCETYPE' || selectedNode.attributes.nodeType == 'SUPPLIERTYPE'
                    || selectedNode.attributes.nodeType == 'PGCSUBSTANCEGROUPTYPE' || selectedNode.attributes.nodeType == 'PGCSPECIFICATIONTYPE' || selectedNode.attributes.nodeType == 'PGCDECLARATIONTYPE') {
                    deleteClsType();
                }
            }

            vm.selectedTypeObjects = [];
            function deleteClsType() {
                vm.selectedTypeObjects = [];
                vm.selectedNode = classificationTree.tree('getSelected');
                if (vm.selectedNode != null) {
                    var data = classificationTree.tree('getData', vm.selectedNode.target);
                    if (data != null && data.attributes.typeObject != null) {
                        if (vm.selectedNode.attributes.nodeType == 'ITEMTYPE') {
                            ItemService.getItemsByType(data.attributes.typeObject.id, pageable).then(
                                function (data) {
                                    vm.selectedTypeObjects = data;
                                    deleteTypeDialog();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            );
                        }
                        else if (vm.selectedNode.attributes.nodeType == 'CUSTOMOBJECTTYPE') {
                            vm.selectedTypeObjects = {content: []}
                            deleteTypeDialog();
                        }
                        else if (vm.selectedNode.attributes.nodeType == 'CHANGETYPE') {
                            ECOService.getECOsByType(data.attributes.typeObject.id, pageable).then(
                                function (data) {
                                    vm.selectedTypeObjects = data;
                                    deleteTypeDialog();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            );
                        } else if (vm.selectedNode.attributes.nodeType == 'MANUFACTURERTYPE') {
                            MfrService.getMfrsByType(data.attributes.typeObject.id, pageable).then(
                                function (data) {
                                    vm.selectedTypeObjects = data;
                                    deleteTypeDialog();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            );
                        } else if (vm.selectedNode.attributes.nodeType == 'MANUFACTURERPARTTYPE') {
                            MfrPartsService.getMfrPartsByType(data.attributes.typeObject.id, pageable).then(
                                function (data) {
                                    vm.selectedTypeObjects = data;
                                    deleteTypeDialog();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            );
                        } else if (vm.selectedNode.attributes.nodeType == 'WORKFLOWTYPE') {
                            WorkflowService.getWorkflowsByType(data.attributes.typeObject.id, pageable).then(
                                function (data) {
                                    vm.selectedTypeObjects = data;
                                    deleteTypeDialog();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            );
                        } else if (vm.selectedNode.attributes.nodeType == 'PLANTTYPE') {
                            vm.selectedMESObjectTypeNode = vm.selectedNode.attributes.nodeType;
                            MESObjectTypeService.getPlantsByType(data.attributes.typeObject.id, pageable).then(
                                function (data) {
                                    vm.selectedTypeObjects = data;
                                    deleteMESObjectTypeDialog();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            );
                        } else if (vm.selectedNode.attributes.nodeType == 'ASSEMBLYLINETYPE') {
                            vm.selectedMESObjectTypeNode = vm.selectedNode.attributes.nodeType;
                            MESObjectTypeService.getAssemblyLinesByType(data.attributes.typeObject.id, pageable).then(
                                function (data) {
                                    vm.selectedTypeObjects = data;
                                    deleteMESObjectTypeDialog();
                                }
                            );
                        } else if (vm.selectedNode.attributes.nodeType == 'WORKCENTERTYPE') {
                            vm.selectedMESObjectTypeNode = vm.selectedNode.attributes.nodeType;
                            MESObjectTypeService.getWorkCentersByType(data.attributes.typeObject.id, pageable).then(
                                function (data) {
                                    vm.selectedTypeObjects = data;
                                    deleteMESObjectTypeDialog();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            );
                        } else if (vm.selectedNode.attributes.nodeType == 'MACHINETYPE') {
                            vm.selectedMESObjectTypeNode = vm.selectedNode.attributes.nodeType;
                            MESObjectTypeService.getMachinesByType(data.attributes.typeObject.id, pageable).then(
                                function (data) {
                                    vm.selectedTypeObjects = data;
                                    deleteMESObjectTypeDialog();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            );
                        } else if (vm.selectedNode.attributes.nodeType == 'PRODUCTIONORDERTYPE') {
                            vm.selectedMESObjectTypeNode = vm.selectedNode.attributes.nodeType;
                            /*  MESObjectTypeService.getProductionOrdersByType(data.attributes.typeObject.id, pageable).then(
                             function (data) {
                             vm.selectedTypeObjects = data;
                             deleteMESObjectTypeDialog();
                             }
                             );*/
                        } else if (vm.selectedNode.attributes.nodeType == 'OPERATIONTYPE') {
                            vm.selectedMESObjectTypeNode = vm.selectedNode.attributes.nodeType;
                            MESObjectTypeService.getOperationsByType(data.attributes.typeObject.id, pageable).then(
                                function (data) {
                                    vm.selectedTypeObjects = data;
                                    deleteMESObjectTypeDialog();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);

                                }
                            );
                        }
                        else if (vm.selectedNode.attributes.nodeType == 'MBOMTYPE') {
                            vm.selectedMESObjectTypeNode = vm.selectedNode.attributes.nodeType;
                            MESObjectTypeService.getMBOMsByType(data.attributes.typeObject.id, pageable).then(
                                function (data) {
                                    vm.selectedTypeObjects = data;
                                    deleteMESObjectTypeDialog();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            );
                        }
                        else if (vm.selectedNode.attributes.nodeType == 'BOPTYPE') {
                            vm.selectedMESObjectTypeNode = vm.selectedNode.attributes.nodeType;
                            MESObjectTypeService.getBOPsByType(data.attributes.typeObject.id, pageable).then(
                                function (data) {
                                    vm.selectedTypeObjects = data;
                                    deleteMESObjectTypeDialog();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            );
                        }
                        else if (vm.selectedNode.attributes.nodeType == 'TOOLTYPE') {
                            vm.selectedMESObjectTypeNode = vm.selectedNode.attributes.nodeType;
                            MESObjectTypeService.getToolsByType(data.attributes.typeObject.id, pageable).then(
                                function (data) {
                                    vm.selectedTypeObjects = data;
                                    deleteMESObjectTypeDialog();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            );
                        }
                        else if (vm.selectedNode.attributes.nodeType == 'SPAREPARTTYPE') {
                            vm.selectedMESObjectTypeNode = vm.selectedNode.attributes.nodeType;
                            MESObjectTypeService.getSparePartsByType(data.attributes.typeObject.id, pageable).then(
                                function (data) {
                                    vm.selectedTypeObjects = data;
                                    deleteMESObjectTypeDialog();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            );
                        }
                        else if (vm.selectedNode.attributes.nodeType == 'ASSETTYPE') {
                            vm.selectedMESObjectTypeNode = vm.selectedNode.attributes.nodeType;
                            MESObjectTypeService.getAssetsByType(data.attributes.typeObject.id, pageable).then(
                                function (data) {
                                    vm.selectedTypeObjects = data;
                                    deleteMESObjectTypeDialog();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            );
                        }
                        else if (vm.selectedNode.attributes.nodeType == 'METERTYPE') {
                            vm.selectedMESObjectTypeNode = vm.selectedNode.attributes.nodeType;
                            MESObjectTypeService.getMetersByType(data.attributes.typeObject.id, pageable).then(
                                function (data) {
                                    vm.selectedTypeObjects = data;
                                    deleteMESObjectTypeDialog();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            );
                        }
                        else if (vm.selectedNode.attributes.nodeType == 'WORKREQUESTTYPE') {
                            vm.selectedMESObjectTypeNode = vm.selectedNode.attributes.nodeType;
                            MESObjectTypeService.getWorkRequestsByType(data.attributes.typeObject.id, pageable).then(
                                function (data) {
                                    vm.selectedTypeObjects = data;
                                    deleteMESObjectTypeDialog();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            );
                        }
                        else if (vm.selectedNode.attributes.nodeType == 'WORKORDERTYPE') {
                            vm.selectedMESObjectTypeNode = vm.selectedNode.attributes.nodeType;
                            MESObjectTypeService.getWorkOrdersByType(data.attributes.typeObject.id, pageable).then(
                                function (data) {
                                    vm.selectedTypeObjects = data;
                                    deleteMESObjectTypeDialog();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            );
                        } else if (vm.selectedNode.attributes.nodeType == 'MATERIALTYPE') {
                            vm.selectedMESObjectTypeNode = vm.selectedNode.attributes.nodeType;
                            MESObjectTypeService.getMaterialByType(data.attributes.typeObject.id, pageable).then(
                                function (data) {
                                    vm.selectedTypeObjects = data;
                                    deleteMESObjectTypeDialog();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            );
                        } else if (vm.selectedNode.attributes.nodeType == 'JIGFIXTURETYPE') {
                            vm.selectedMESObjectTypeNode = vm.selectedNode.attributes.nodeType;
                            MESObjectTypeService.getJigsFixturesByType(data.attributes.typeObject.id, pageable).then(
                                function (data) {
                                    vm.selectedTypeObjects = data;
                                    deleteMESObjectTypeDialog();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            );
                        } else if (vm.selectedNode.attributes.nodeType == 'EQUIPMENTTYPE') {
                            vm.selectedMESObjectTypeNode = vm.selectedNode.attributes.nodeType;
                            MESObjectTypeService.getEquipmentsByType(data.attributes.typeObject.id, pageable).then(
                                function (data) {
                                    vm.selectedTypeObjects = data;
                                    deleteMESObjectTypeDialog();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            );
                        } else if (vm.selectedNode.attributes.nodeType == 'INSTRUMENTTYPE') {
                            vm.selectedMESObjectTypeNode = vm.selectedNode.attributes.nodeType;
                            MESObjectTypeService.getInstrumentsByType(data.attributes.typeObject.id, pageable).then(
                                function (data) {
                                    vm.selectedTypeObjects = data;
                                    deleteMESObjectTypeDialog();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            );
                        }
                        else if (vm.selectedNode.attributes.nodeType == 'MANPOWERTYPE') {
                            vm.selectedMESObjectTypeNode = vm.selectedNode.attributes.nodeType;
                            MESObjectTypeService.getManpowerByType(data.attributes.typeObject.id, pageable).then(
                                function (data) {
                                    vm.selectedTypeObjects = data;
                                    deleteMESObjectTypeDialog();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            );
                        }
                        else if (vm.selectedNode.attributes.nodeType == 'REQUIREMENTTYPE') {
                            PMObjectTypeService.getRequirementsByType(data.attributes.typeObject.id, pageable).then(
                                function (data) {
                                    vm.selectedTypeObjects = data;
                                    deleteTypeDialog();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            );
                        } else if (vm.selectedNode.attributes.nodeType == 'REQUIREMENTDOCUMENTTYPE') {
                            PMObjectTypeService.getReqDocsByType(data.attributes.typeObject.id, pageable).then(
                                function (data) {
                                    vm.selectedTypeObjects = data;
                                    deleteTypeDialog();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            );
                        }
                        else if (vm.selectedNode.attributes.nodeType == 'PMOBJECTTYPE') {
                            PMObjectTypeService.getTypeObjectCount(vm.selectedNode.attributes.typeObject.id).then(
                                function (data) {
                                    if (data == 0) {
                                        var options = {
                                            title: deleteTypeDialogTitle,
                                            message: deleteTypeDialogMessage + " (" + vm.selectedNode.attributes.typeObject.name + ") ?",
                                            okButtonClass: 'btn-danger'
                                        };
                                        DialogService.confirm(options, function (yes) {
                                            if (yes == true) {
                                                PMObjectTypeService.deletePmType(vm.selectedNode.attributes.typeObject.id).then(
                                                    function (data) {
                                                        classificationTree.tree('remove', vm.selectedNode.target);
                                                        $rootScope.showSuccessMessage(vm.selectedNode.attributes.typeObject.name + " : " + deleteTypeSuccessMessage);
                                                        $scope.$broadcast('app.pmType.selected', {itemType: null});

                                                    }, function (error) {
                                                        $rootScope.showErrorMessage(error.message);
                                                        $rootScope.hideBusyIndicator();
                                                    }
                                                )
                                            }
                                        });
                                    } else {
                                        var options = {
                                            title: deleteTypeDialogTitle,
                                            message: deletePmTypeExistDialogMessage.format(vm.selectedNode.text),
                                            okButtonClass: 'btn-danger'
                                        };
                                        DialogService.confirm(options, function (yes) {
                                            if (yes == true) {

                                            }
                                        })
                                    }
                                }
                            )
                        }
                        else if (vm.selectedNode.attributes.nodeType == 'QUALITY_TYPE') {
                            QualityTypeService.getObjectsByType(data.attributes.typeObject.id, data.attributes.typeObject.qualityType).then(
                                function (data) {
                                    vm.selectedTypeObjects = data;
                                    vm.selectedQualityTypeNode = vm.selectedNode.attributes.typeObject.qualityType;
                                    deleteQualityTypeDialog();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            );
                        }
                        else if (vm.selectedNode.attributes.nodeType == 'PGCSUBSTANCETYPE') {
                            vm.selectedPGCObjectTypeNode = vm.selectedNode.attributes.nodeType;
                            PGCObjectTypeService.getSubstanceByType(data.attributes.typeObject.id, pageable).then(
                                function (data) {
                                    vm.selectedTypeObjects = data;
                                    deletePGCObjectTypeDialog();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            );
                        }
                        else if (vm.selectedNode.attributes.nodeType == 'PGCSUBSTANCEGROUPTYPE') {
                            vm.selectedPGCObjectTypeNode = vm.selectedNode.attributes.nodeType;
                            PGCObjectTypeService.getSubstanceGroupByType(data.attributes.typeObject.id, pageable).then(
                                function (data) {
                                    vm.selectedTypeObjects = data;
                                    deletePGCObjectTypeDialog();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            );
                        }
                        else if (vm.selectedNode.attributes.nodeType == 'PGCSPECIFICATIONTYPE') {
                            vm.selectedPGCObjectTypeNode = vm.selectedNode.attributes.nodeType;
                            PGCObjectTypeService.getSpecificationsByType(data.attributes.typeObject.id, pageable).then(
                                function (data) {
                                    vm.selectedTypeObjects = data;
                                    deletePGCObjectTypeDialog();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            );
                        }
                        else if (vm.selectedNode.attributes.nodeType == 'PGCDECLARATIONTYPE') {
                            vm.selectedPGCObjectTypeNode = vm.selectedNode.attributes.nodeType;
                            PGCObjectTypeService.getDeclarationsByType(data.attributes.typeObject.id, pageable).then(
                                function (data) {
                                    vm.selectedTypeObjects = data;
                                    deletePGCObjectTypeDialog();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            );
                        } else if (vm.selectedNode.attributes.nodeType == 'SUPPLIERTYPE') {
                            vm.selectedMESObjectTypeNode = vm.selectedNode.attributes.nodeType;
                            MESObjectTypeService.getSuppliersByType(data.attributes.typeObject.id, pageable).then(
                                function (data) {
                                    vm.selectedTypeObjects = data;
                                    deleteMESObjectTypeDialog();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            );
                        }
                    }
                }
            }


            function deletePGCObjectTypeDialog() {
                var options = null;
                if (vm.selectedTypeObjects.content.length == 0) {
                    var title = null;
                    if (vm.selectedPGCObjectTypeNode == "PGCSUBSTANCETYPE") {
                        title = substanceTypeTitle;
                    } else if (vm.selectedPGCObjectTypeNode == "PGCSUBSTANCEGROUPTYPE") {
                        title = substanceGroupTypeTitle;
                    } else if (vm.selectedPGCObjectTypeNode == "PGCSPECIFICATIONTYPE") {
                        title = specificationTypeTitle;
                    } else if (vm.selectedPGCObjectTypeNode == "PGCDECLARATIONTYPE") {
                        title = declarationTypeTitle;
                    }
                    options = {
                        title: deleteTypeDialogTitle,
                        message: deleteTypeDialogMessage + " [ " + vm.selectedNode.text + " ] " + title + " ? ",
                        okButtonClass: 'btn-danger'
                    };
                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            if (vm.selectedNode != null) {
                                var data = classificationTree.tree('getData', vm.selectedNode.target);
                                if (data != null && data.attributes.typeObject != null) {
                                    var promise = null;
                                    if (vm.selectedPGCObjectTypeNode == "PGCSUBSTANCETYPE") {
                                        promise = PGCObjectTypeService.deleteSubstanceType(data.attributes.typeObject.id);
                                    } else if (vm.selectedPGCObjectTypeNode == "PGCSUBSTANCEGROUPTYPE") {
                                        promise = PGCObjectTypeService.deleteSubstanceGroupType(data.attributes.typeObject.id);
                                    } else if (vm.selectedPGCObjectTypeNode == "PGCSPECIFICATIONTYPE") {
                                        promise = PGCObjectTypeService.deleteSpecificationType(data.attributes.typeObject.id);
                                    } else if (vm.selectedPGCObjectTypeNode == "PGCDECLARATIONTYPE") {
                                        promise = PGCObjectTypeService.deleteDeclarationType(data.attributes.typeObject.id);
                                    }
                                    promise.then(
                                        function (data) {
                                            vm.objectTypeAttributes = false;
                                            vm.type = null;
                                            classificationTree.tree('remove', vm.selectedNode.target);
                                            $rootScope.showSuccessMessage(vm.selectedNode.attributes.typeObject.name + " : " + deleteTypeSuccessMessage);
                                            $scope.$broadcast('app.pgcObjectType.selected', {itemType: null});
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        });
                                }
                            }
                        }
                    })

                } else {
                    var message = null;
                    vm.type = vm.selectedMESObjectTypeNode;
                    if (vm.type == "PGCSUBSTANCETYPE" || vm.type == "PGCSPECIFICATIONTYPE" || vm.type == "PGCDECLARATIONTYPE" || vm.type == "PGCSUBSTANCEGROUPTYPE") {
                        message = deleteManufacturingTypeTitle.format(vm.selectedNode.text);
                    }
                    options = {
                        title: deleteTypeDialogTitle,
                        message: message,
                        okButtonClass: 'btn-danger'
                    };
                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {

                        }
                    })
                }
            }


            function deleteMESObjectTypeDialog() {
                var options = null;
                if (vm.selectedTypeObjects.content.length == 0) {
                    var title = null;
                    if (vm.selectedMESObjectTypeNode == "PLANTTYPE") {
                        title = plantTypeTitle;
                    } else if (vm.selectedMESObjectTypeNode == "WORKCENTERTYPE") {
                        title = wcTypeTitle;
                    } else if (vm.selectedMESObjectTypeNode == "MACHINETYPE") {
                        title = mcTypeTitle;
                    } else if (vm.selectedMESObjectTypeNode == "MATERIALTYPE") {
                        title = maTypeTitle;
                    } else if (vm.selectedMESObjectTypeNode == "TOOLTYPE") {
                        title = toolsTypeTitle;
                    } else if (vm.selectedMESObjectTypeNode == "SPAREPARTTYPE") {
                        title = sparePartsTypeTitle;
                    } else if (vm.selectedMESObjectTypeNode == "ASSETTYPE") {
                        title = assetsTypeTitle;
                    } else if (vm.selectedMESObjectTypeNode == "METERTYPE") {
                        title = metersTypeTitle;
                    } else if (vm.selectedMESObjectTypeNode == "WORKREQUESTTYPE") {
                        title = workRequestsTypeTitle;
                    } else if (vm.selectedMESObjectTypeNode == "WORKORDERTYPE") {
                        title = workOrdersTypeTitle;
                    } else if (vm.selectedMESObjectTypeNode == "OPERATIONTYPE") {
                        title = operationsTypeTitle;
                    } else if (vm.selectedMESObjectTypeNode == "JIGFIXTURETYPE") {
                        title = jigsTypeTitle;
                    } else if (vm.selectedMESObjectTypeNode == "PRODUCTIONORDERTYPE") {
                        title = productionTypeTitle;
                    } else if (vm.selectedMESObjectTypeNode == "MBOMTYPE") {
                        title = mbomTypeTitle;
                    } else if (vm.selectedMESObjectTypeNode == "BOPTYPE") {
                        title = bopTypeTitle;
                    } else if (vm.selectedMESObjectTypeNode == "EQUIPMENTTYPE") {
                        title = equipmentTypeTitle;
                    } else if (vm.selectedMESObjectTypeNode == "INSTRUMENTTYPE") {
                        title = instrumentTypeTitle;
                    } else if (vm.selectedMESObjectTypeNode == "MANPOWERTYPE") {
                        title = manpowerTypeTitle;
                    } else if (vm.selectedMESObjectTypeNode == "SUPPLIERTYPE") {
                        title = supplierTypeTitle;
                    } else if (vm.selectedMESObjectTypeNode == "ASSEMBLYLINETYPE") {
                        title = assemblyLineTypeTitle;
                    }
                    options = {
                        title: deleteTypeDialogTitle,
                        message: deleteTypeDialogMessage + " [ " + vm.selectedNode.text + " ] " + title + " ? ",
                        okButtonClass: 'btn-danger'
                    };
                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            if (vm.selectedNode != null) {
                                var data = classificationTree.tree('getData', vm.selectedNode.target);
                                if (data != null && data.attributes.typeObject != null) {
                                    var promise = null;
                                    if (vm.selectedMESObjectTypeNode == "PLANTTYPE") {
                                        promise = MESObjectTypeService.deletePlantType(data.attributes.typeObject.id);
                                    } else if (vm.selectedMESObjectTypeNode == "WORKCENTERTYPE") {
                                        promise = MESObjectTypeService.deleteWorkCenterType(data.attributes.typeObject.id);
                                    } else if (vm.selectedMESObjectTypeNode == "MACHINETYPE") {
                                        promise = MESObjectTypeService.deleteMachineType(data.attributes.typeObject.id);
                                    } else if (vm.selectedMESObjectTypeNode == "MATERIALTYPE") {
                                        promise = MESObjectTypeService.deleteMaterialType(data.attributes.typeObject.id);
                                    } else if (vm.selectedMESObjectTypeNode == "TOOLTYPE") {
                                        promise = MESObjectTypeService.deleteToolType(data.attributes.typeObject.id);
                                    } else if (vm.selectedMESObjectTypeNode == "SPAREPARTTYPE") {
                                        promise = MESObjectTypeService.deleteSparePartType(data.attributes.typeObject.id);
                                    } else if (vm.selectedMESObjectTypeNode == "OPERATIONTYPE") {
                                        promise = MESObjectTypeService.deleteOperationType(data.attributes.typeObject.id);
                                    } else if (vm.selectedMESObjectTypeNode == "JIGFIXTURETYPE") {
                                        promise = MESObjectTypeService.deleteJigsFixtureType(data.attributes.typeObject.id);
                                    } else if (vm.selectedMESObjectTypeNode == "PRODUCTIONORDERTYPE") {
                                        promise = MESObjectTypeService.deleteProductionOrderType(data.attributes.typeObject.id);
                                    } else if (vm.selectedMESObjectTypeNode == "MBOMTYPE") {
                                        promise = MESObjectTypeService.deleteMBOMType(data.attributes.typeObject.id);
                                    } else if (vm.selectedMESObjectTypeNode == "BOPTYPE") {
                                        promise = MESObjectTypeService.deleteBOPType(data.attributes.typeObject.id);
                                    } else if (vm.selectedMESObjectTypeNode == "EQUIPMENTTYPE") {
                                        promise = MESObjectTypeService.deleteEquipmentType(data.attributes.typeObject.id);
                                    } else if (vm.selectedMESObjectTypeNode == "INSTRUMENTTYPE") {
                                        promise = MESObjectTypeService.deleteInstrumentType(data.attributes.typeObject.id);
                                    } else if (vm.selectedMESObjectTypeNode == "MANPOWERTYPE") {
                                        promise = MESObjectTypeService.deleteManpowerType(data.attributes.typeObject.id);
                                    } else if (vm.selectedMESObjectTypeNode == "WORKORDERTYPE") {
                                        promise = MESObjectTypeService.deleteWorkOrderType(data.attributes.typeObject.id);
                                    } else if (vm.selectedMESObjectTypeNode == "WORKREQUESTTYPE") {
                                        promise = MESObjectTypeService.deleteWorkRequestType(data.attributes.typeObject.id);
                                    } else if (vm.selectedMESObjectTypeNode == "ASSETTYPE") {
                                        promise = MESObjectTypeService.deleteAssetType(data.attributes.typeObject.id);
                                    } else if (vm.selectedMESObjectTypeNode == "METERTYPE") {
                                        promise = MESObjectTypeService.deleteMeterType(data.attributes.typeObject.id);
                                    } else if (vm.selectedMESObjectTypeNode == "SUPPLIERTYPE") {
                                        promise = MESObjectTypeService.deleteSupplierType(data.attributes.typeObject.id);
                                    } else if (vm.selectedMESObjectTypeNode == "ASSEMBLYLINETYPE") {
                                        promise = MESObjectTypeService.deleteAssemblyLineType(data.attributes.typeObject.id);
                                    }
                                    promise.then(
                                        function (data) {
                                            vm.objectTypeAttributes = false;
                                            vm.type = null;
                                            classificationTree.tree('remove', vm.selectedNode.target);
                                            $rootScope.showSuccessMessage(vm.selectedNode.attributes.typeObject.name + " : " + deleteTypeSuccessMessage);
                                            $scope.$broadcast('app.mesObjectType.selected', {itemType: null});
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        });
                                }
                            }
                        }
                    })

                } else {
                    var message = null;
                    vm.type = vm.selectedMESObjectTypeNode;
                    if (vm.type == "PLANTTYPE" || vm.type == "WORKCENTERTYPE" || vm.type == "MACHINETYPE" || vm.type == "MATERIALTYPE" || vm.type == "SUPPLIERTYPE"
                        || vm.type == "TOOLTYPE" || vm.type == "OPERATIONTYPE" || vm.type == "JIGFIXTURETYPE" || vm.type == "PRODUCTIONORDERTYPE" || vm.type == "MBOMTYPE" || vm.type == "BOPTYPE"
                        || vm.type == "EQUIPMENTTYPE" || vm.type == "INSTRUMENTTYPE" || vm.type == "MANPOWERTYPE" || vm.type == "SPAREPARTTYPE"
                        || vm.type == "WORKORDERTYPE" || vm.type == "WORKREQUESTTYPE" || vm.type == "ASSETTYPE" || vm.type == "METERTYPE" || vm.type == "ASSEMBLYLINETYPE") {
                        message = deleteManufacturingTypeTitle.format(vm.selectedNode.text);
                    }
                    options = {
                        title: deleteTypeDialogTitle,
                        message: message,
                        okButtonClass: 'btn-danger'
                    };
                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {

                        }
                    })
                }
            }


            function deleteQualityTypeDialog() {
                var options = null;
                if (vm.selectedTypeObjects == 0) {
                    var title = null;
                    if (vm.selectedQualityTypeNode == "PRODUCTINSPECTIONPLANTYPE" || vm.selectedQualityTypeNode == "MATERIALINSPECTIONPLANTYPE") {
                        title = planTypeTitle;
                    } else if (vm.selectedQualityTypeNode == "PRTYPE") {
                        title = prTypeTitle;
                    } else if (vm.selectedQualityTypeNode == "NCRTYPE") {
                        title = ncrTypeTitle;
                    } else if (vm.selectedQualityTypeNode == "QCRTYPE") {
                        title = qcrTypeTitle;
                    } else if (vm.selectedQualityTypeNode == "PPAPTYPE") {
                        title = ppapTypeTitle;
                    } else if (vm.selectedQualityTypeNode == "SUPPLIERAUDITTYPE") {
                        title = supplierAuditTypeTitle;
                    }
                    options = {
                        title: deleteTypeDialogTitle,
                        message: deleteTypeDialogMessage + " [ " + vm.selectedNode.text + " ] " + title + " ? ",
                        okButtonClass: 'btn-danger'
                    };
                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            if (vm.selectedNode != null) {
                                var data = classificationTree.tree('getData', vm.selectedNode.target);
                                if (data != null && data.attributes.typeObject != null) {
                                    var promise = null;
                                    promise = ClassificationService.deleteType(vm.selectedNode.attributes.nodeType, data.attributes.typeObject.id);
                                    promise.then(
                                        function (data) {
                                            vm.objectTypeAttributes = false;
                                            vm.type = null;
                                            classificationTree.tree('remove', vm.selectedNode.target);
                                            $rootScope.showSuccessMessage(vm.selectedNode.attributes.typeObject.name + " : " + deleteTypeSuccessMessage);
                                            $rootScope.selectedQualityType = null;
                                            $scope.$broadcast('app.qualityType.selected', {itemType: null});
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        });
                                }
                            }
                        }
                    })
                } else if (vm.selectedTypeObjects != 0) {
                    var message = null;
                    if (vm.selectedQualityTypeNode == "PRODUCTINSPECTIONPLANTYPE" || vm.selectedQualityTypeNode == "MATERIALINSPECTIONPLANTYPE") {
                        message = deletePlanTypeExistDialogMessage;
                    } else if (vm.selectedQualityTypeNode == "PRTYPE") {
                        message = deletePrTypeExistDialogMessage;
                    } else if (vm.selectedQualityTypeNode == "NCRTYPE") {
                        message = deleteNcrTypeExistDialogMessage;
                    } else if (vm.selectedQualityTypeNode == "QCRTYPE") {
                        message = deleteQcrTypeExistDialogMessage;
                    } else if (vm.selectedQualityTypeNode == "PPAPTYPE") {
                        message = deletePpapTypeExistDialogMessage;
                    } else if (vm.selectedQualityTypeNode == "SUPPLIERAUDITTYPE") {
                        message = deleteSupplierAuditTypeExistDialogMessage;
                    }
                    options = {
                        title: deleteTypeDialogTitle,
                        message: message + " [" + vm.selectedNode.text + " ] " + "?",
                        okButtonClass: 'btn-danger'
                    };
                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {

                        }
                    })
                }

            }

            var deleteTypeDialogTitle = parsed.html($translate.instant("DELETE_TYPE_DIALOG_TITLE")).html();
            var deleteTypeDialogMessage = parsed.html($translate.instant("DELETE_TYPE_DIALOG_MESSAGE")).html();
            var deleteTypeSuccessMessage = parsed.html($translate.instant("DELETE_TYPE_SUCCESS_MESSAGE")).html();
            var customObjectTypeTitle = parsed.html($translate.instant("CUSTOM_OBJECT_TYPE")).html();
            var changeTypeTitle = parsed.html($translate.instant("CHANGE_TYPE")).html();
            var manufacturerTypeTitle = parsed.html($translate.instant("MANUFACTURER_TYPE")).html();
            var manufacturerPartTypeTitle = parsed.html($translate.instant("MANUFACTURER_PART_TYPE")).html();
            var workflowTypeTitle = parsed.html($translate.instant("WORKFLOW_TYPE")).html();
            var reqTypeTitle = parsed.html($translate.instant("REQUIREMENT_TYPE")).html();
            var reqDocTypeTitle = parsed.html($translate.instant("REQUIREMENT_DOC_TYPE")).html();
            var specTypeTitle = parsed.html($translate.instant("SPECIFICATION_TYPE")).html();
            var toolTypeTitle = parsed.html($translate.instant("TOOL_TYPE")).html();
            var operationTypeTitle = parsed.html($translate.instant("OPERATION_TYPE")).html();
            var productionOrderTypeTitle = parsed.html($translate.instant("PRODUCTION_ORDER_TYPE")).html();
            var serviceOrderTypeTypeTitle = parsed.html($translate.instant("SERVICE_ORDER_TYPE")).html();
            var planTypeTitle = parsed.html($translate.instant("INSPECTION_PLAN_TYPE")).html();
            var prTypeTitle = parsed.html($translate.instant("PROBLEM_REPORT_TYPE")).html();
            var ncrTypeTitle = parsed.html($translate.instant("NCR_TYPE")).html();
            var qcrTypeTitle = parsed.html($translate.instant("QCR_TYPE")).html();
            var ppapTypeTitle = parsed.html($translate.instant("PPAP_TYPE")).html();
            var supplierAuditTypeTitle = parsed.html($translate.instant("NEW_SUPPLIER_AUDIT_TYPE")).html();
            var deleteChangeTypeExistDialogMessage = parsed.html($translate.instant("DELETE_CHANGE_TYPE_EXIST_DIALOG_MESSAGE")).html();
            var deleteMfrTypeExistDialogMessage = parsed.html($translate.instant("DELETE_MFR_TYPE_EXIST_DIALOG_MESSAGE")).html();
            var deleteMfrPartTypeExistDialogMessage = parsed.html($translate.instant("DELETE_MFR_PART_TYPE_EXIST_DIALOG_MESSAGE")).html();
            var deleteWorkflowTypeExistDialogMessage = parsed.html($translate.instant("DELETE_WORKFLOW_TYPE_EXIST_DIALOG_MESSAGE")).html();

            var deleteRequirementTypeExistDialogMessage = parsed.html($translate.instant("DELETE_REQUIREMENT_TYPE_EXIST_DIALOG_MESSAGE")).html();
            var deleteSpecTypeExistDialogMessage = parsed.html($translate.instant("DELETE_REQUIREMENT_TYPE_EXIST_DIALOG_MESSAGE")).html();

            var plantTypeTitle = parsed.html($translate.instant("PLANT_TYPE_TITLE")).html();
            var wcTypeTitle = parsed.html($translate.instant("WORKCENTER_TYPE_TITLE")).html();
            var mcTypeTitle = parsed.html($translate.instant("MACHINE_TYPE_TITLE")).html();
            var maTypeTitle = parsed.html($translate.instant("MATERIAL_TYPE_TITLE")).html();
            var toolsTypeTitle = parsed.html($translate.instant("TOOL_TYPE_TITLE")).html();
            var sparePartsTypeTitle = parsed.html($translate.instant("SPARE_PART_TYPE_TITLE")).html();
            var assetsTypeTitle = parsed.html($translate.instant("ASSET_TYPE_TITLE")).html();
            var metersTypeTitle = parsed.html($translate.instant("METER_TYPE_TITLE")).html();
            var workRequestsTypeTitle = parsed.html($translate.instant("WORK_REQUEST_TYPE_TITLE")).html();
            var workOrdersTypeTitle = parsed.html($translate.instant("WORK_ORDER_TYPE_TITLE")).html();
            var jigsTypeTitle = parsed.html($translate.instant("JIGS_TYPE_TITLE")).html();
            var operationsTypeTitle = parsed.html($translate.instant("OPERATION_TYPE_TITLE")).html();
            var serviceTypeTitle = parsed.html($translate.instant("SERVICE_ORDER_TYPE_TITLE")).html();
            var productionTypeTitle = parsed.html($translate.instant("PRODUCTION_ORDER_TYPE_TITLE")).html();
            var mbomTypeTitle = parsed.html($translate.instant("MBOM_TYPE_TITLE")).html();
            var bopTypeTitle = parsed.html($translate.instant("BOP_TYPE_TITLE")).html();
            var equipmentTypeTitle = parsed.html($translate.instant("EQUIPMENT_ORDER_TYPE_TITLE")).html();
            var instrumentTypeTitle = parsed.html($translate.instant("INSTRUMENT_ORDER_TYPE_TITLE")).html();
            var manpowerTypeTitle = parsed.html($translate.instant("MANPOWER_ORDER_TYPE_TITLE")).html();

            var substanceTypeTitle = parsed.html($translate.instant("SUBSTANCE_TYPE_TITLE")).html();
            var substanceGroupTypeTitle = parsed.html($translate.instant("SUBSTANCE_GROUP_TYPE_TITLE")).html();
            var specificationTypeTitle = parsed.html($translate.instant("SPECIFICATION_TYPE_TITLE")).html();
            var declarationTypeTitle = parsed.html($translate.instant("DECLARATION_TYPE_TITLE")).html();
            var supplierTypeTitle = parsed.html($translate.instant("SUPPLIER_TYPE_TITLE")).html();
            var assemblyLineTypeTitle = parsed.html($translate.instant("ASSEMBLY_LINE_TYPE_TITLE")).html();

            function deleteTypeDialog() {
                var options = null;
                if (vm.selectedTypeObjects.content.length == 0) {
                    if (vm.selectedNode.attributes.nodeType == 'ITEMTYPE') {
                        options = {
                            title: deleteTypeDialogTitle,
                            message: deleteTypeDialogMessage + " [ " + vm.selectedNode.text + " ] " + itemTypeTitle + " ? ",
                            okButtonClass: 'btn-danger'
                        };
                    } else if (vm.selectedNode.attributes.nodeType == 'CUSTOMOBJECTTYPE') {
                        options = {
                            title: deleteTypeDialogTitle,
                            message: deleteTypeDialogMessage + " [ " + vm.selectedNode.text + " ] " + customObjectTypeTitle + " ? ",
                            okButtonClass: 'btn-danger'
                        };
                    } else if (vm.selectedNode.attributes.nodeType == 'CHANGETYPE') {
                        options = {
                            title: deleteTypeDialogTitle,
                            message: deleteTypeDialogMessage + " [ " + vm.selectedNode.text + " ] " + changeTypeTitle + " ? ",
                            okButtonClass: 'btn-danger'
                        };
                    } else if (vm.selectedNode.attributes.nodeType == 'MANUFACTURERTYPE') {
                        options = {
                            title: deleteTypeDialogTitle,
                            message: deleteTypeDialogMessage + " [" + vm.selectedNode.text + " ] " + manufacturerTypeTitle + " ? ",
                            okButtonClass: 'btn-danger'
                        };
                    } else if (vm.selectedNode.attributes.nodeType == 'MANUFACTURERPARTTYPE') {
                        options = {
                            title: deleteTypeDialogTitle,
                            message: deleteTypeDialogMessage + " [ " + vm.selectedNode.text + " ] " + manufacturerPartTypeTitle + " ? ",
                            okButtonClass: 'btn-danger'
                        };
                    } else if (vm.selectedNode.attributes.nodeType == 'WORKFLOWTYPE') {
                        options = {
                            title: deleteTypeDialogTitle,
                            message: deleteTypeDialogMessage + " [ " + vm.selectedNode.text + " ] " + workflowTypeTitle + " ? ",
                            okButtonClass: 'btn-danger'
                        };
                    }

                    else if (vm.selectedNode.attributes.nodeType == 'REQUIREMENTTYPE') {
                        options = {
                            title: deleteTypeDialogTitle,
                            message: deleteTypeDialogMessage + " [ " + vm.selectedNode.text + " ] " + reqTypeTitle + " ? ",
                            okButtonClass: 'btn-danger'
                        };
                    }

                    else if (vm.selectedNode.attributes.nodeType == 'REQUIREMENTDOCUMENTTYPE') {
                        options = {
                            title: deleteTypeDialogTitle,
                            message: deleteTypeDialogMessage + " [ " + vm.selectedNode.text + " ] " + reqDocTypeTitle + " ? ",
                            okButtonClass: 'btn-danger'
                        };
                    }

                    else if (vm.selectedNode.attributes.nodeType == 'OPERATIONTYPE') {
                        options = {
                            title: deleteTypeDialogTitle,
                            message: deleteTypeDialogMessage + " [ " + vm.selectedNode.text + " ] " + operationTypeTitle + " ? ",
                            okButtonClass: 'btn-danger'
                        };
                    }
                    else if (vm.selectedNode.attributes.nodeType == 'PRODUCTIONORDERTYPE') {
                        options = {
                            title: deleteTypeDialogTitle,
                            message: deleteTypeDialogMessage + " [ " + vm.selectedNode.text + " ] " + productionOrderTypeTitle + " ? ",
                            okButtonClass: 'btn-danger'
                        };
                    }
                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            if (vm.selectedNode != null) {
                                var data = classificationTree.tree('getData', vm.selectedNode.target);
                                if (data != null && data.attributes.typeObject != null) {
                                    var promise = null;

                                    if (vm.selectedNode.attributes.nodeType == 'CUSTOMOBJECTTYPE') {
                                        promise = CustomObjectTypeService.deleteCustomObjectType(data.attributes.typeObject);
                                    }
                                    else {
                                        promise = ClassificationService.deleteType(vm.selectedNode.attributes.nodeType, data.attributes.typeObject.id);
                                    }
                                    promise.then(
                                        function (data) {
                                            vm.objectTypeAttributes = false;
                                            vm.type = null;
                                            classificationTree.tree('remove', vm.selectedNode.target);
                                            $rootScope.showSuccessMessage(vm.selectedNode.attributes.typeObject.name + " : " + deleteTypeSuccessMessage);
                                            if (vm.selectedNode.attributes.nodeType == 'ITEMTYPE') {
                                                $scope.$broadcast('app.itemType.selected', {itemType: null});
                                            } else if (vm.selectedNode.attributes.nodeType == 'CUSTOMOBJECTTYPE') {
                                                $scope.$broadcast('app.customObjectType.selected', {itemType: null});
                                            } else if (vm.selectedNode.attributes.nodeType == 'CHANGETYPE') {
                                                $scope.$broadcast('app.changeType.selected', {itemType: null});
                                            } else if (vm.selectedNode.attributes.nodeType == 'MANUFACTURERTYPE') {
                                                $scope.$broadcast('app.mfrType.selected', {itemType: null});
                                            } else if (vm.selectedNode.attributes.nodeType == 'MANUFACTURERPARTTYPE') {
                                                $scope.$broadcast('app.mfrPartType.selected', {itemType: null});
                                            } else if (vm.selectedNode.attributes.nodeType == 'WORKFLOWTYPE') {
                                                $scope.$broadcast('app.workflowType.selected', {itemType: null});
                                            } else if (vm.selectedNode.attributes.nodeType == 'REQUIREMENTTYPE' || vm.selectedNode.attributes.nodeType == 'REQUIREMENTDOCUMENTTYPE') {
                                                $scope.$broadcast('app.pmType.selected', {itemType: null});
                                            }

                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        });
                                }
                            }
                        }
                    })
                } else if (vm.selectedTypeObjects.content.length != 0) {
                    if (vm.selectedNode.attributes.nodeType == 'ITEMTYPE') {
                        options = {
                            title: deleteTypeDialogTitle,
                            message: deleteItemTypeExistDialogMessage + " [" + vm.selectedNode.text + " ] " + "",
                            okButtonClass: 'btn-danger'
                        };
                    } else if (vm.selectedNode.attributes.nodeType == 'CHANGETYPE') {
                        options = {
                            title: deleteTypeDialogTitle,
                            message: deleteChangeTypeExistDialogMessage + " [" + vm.selectedNode.text + " ] " + "",
                            okButtonClass: 'btn-danger'
                        };
                    } else if (vm.selectedNode.attributes.nodeType == 'MANUFACTURERTYPE') {
                        options = {
                            title: deleteTypeDialogTitle,
                            message: deleteMfrTypeExistDialogMessage + " [" + vm.selectedNode.text + " ] " + "",
                            okButtonClass: 'btn-danger'
                        };
                    } else if (vm.selectedNode.attributes.nodeType == 'MANUFACTURERPARTTYPE') {
                        options = {
                            title: deleteTypeDialogTitle,
                            message: deleteMfrPartTypeExistDialogMessage + " [" + vm.selectedNode.text + " ] " + "",
                            okButtonClass: 'btn-danger'
                        };
                    } else if (vm.selectedNode.attributes.nodeType == 'WORKFLOWTYPE') {
                        options = {
                            title: deleteTypeDialogTitle,
                            message: deleteWorkflowTypeExistDialogMessage + " [" + vm.selectedNode.text + " ] " + "",
                            okButtonClass: 'btn-danger'
                        };
                    }
                    else if (vm.selectedNode.attributes.nodeType == 'REQUIREMENTTYPE') {
                        options = {
                            title: deleteTypeDialogTitle,
                            message: deleteRequirementTypeExistDialogMessage + " [" + vm.selectedNode.text + " ] " + "",
                            okButtonClass: 'btn-danger'
                        };
                    }
                    else if (vm.selectedNode.attributes.nodeType == 'REQUIREMENTDOCUMENTTYPE') {
                        options = {
                            title: deleteTypeDialogTitle,
                            message: deleteRequirementTypeExistDialogMessage + " [" + vm.selectedNode.text + " ] " + "",
                            okButtonClass: 'btn-danger'
                        };
                    }
                    else if (vm.selectedNode.attributes.nodeType == 'OPERATIONTYPE') {
                        options = {
                            title: deleteTypeDialogTitle,
                            message: deleteSpecTypeExistDialogMessage + " [" + vm.selectedNode.text + " ] " + "",
                            okButtonClass: 'btn-danger'
                        };
                    }
                    else if (vm.selectedNode.attributes.nodeType == 'PRODUTIONORDERTYPE') {
                        options = {
                            title: deleteTypeDialogTitle,
                            message: deleteSpecTypeExistDialogMessage + " [" + vm.selectedNode.text + " ] " + "",
                            okButtonClass: 'btn-danger'
                        };
                    }
                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {

                        }
                    })
                }

            }

            var deleteItemTypeDialogTitle = parsed.html($translate.instant("DELETE_ITEM_TYPE_DIALOG_TITLE")).html();
            var deleteItemTypeDialogMessage = parsed.html($translate.instant("DELETE_TYPE_DIALOG_MESSAGE")).html();
            var deleteItemTypeExistDialogMessage = parsed.html($translate.instant("DELETE_ITEM_TYPE_EXIST_DIALOG_MESSAGE")).html();
            var deletePmTypeExistDialogMessage = parsed.html($translate.instant("DELETE_PM_TYPE_EXIST_DIALOG_MESSAGE")).html();
            var deletePlanTypeExistDialogMessage = parsed.html($translate.instant("DELETE_PLAN_TYPE_EXIST_DIALOG_MESSAGE")).html();
            var deletePrTypeExistDialogMessage = parsed.html($translate.instant("DELETE_PR_TYPE_EXIST_DIALOG_MESSAGE")).html();
            var deleteNcrTypeExistDialogMessage = parsed.html($translate.instant("DELETE_NCR_TYPE_EXIST_DIALOG_MESSAGE")).html();
            var deleteQcrTypeExistDialogMessage = parsed.html($translate.instant("DELETE_QCR_TYPE_EXIST_DIALOG_MESSAGE")).html();
            var deleteSupplierAuditTypeExistDialogMessage = parsed.html($translate.instant("DELETE_SUPPLIER_AUDIT_TYPE_EXIST_DIALOG_MESSAGE")).html();
            var deleteManufacturingTypeTitle = parsed.html($translate.instant("DELETE_MANUFACTURING_TYPE_EXIST_DIALOG_MESSAGE")).html();
            var itemTypeTitle = parsed.html($translate.instant("ITEM_TYPE")).html();
            var deletePpapTypeExistDialogMessage = parsed.html($translate.instant("DELETE_PPAP_TYPE_EXIST_DIALOG_MESSAGE")).html();

            function deleteItemType() {
                var items = [];
                var mfrParts = [];
                var selectedNode = classificationTree.tree('getSelected');
                if (selectedNode != null) {
                    var data = classificationTree.tree('getData', selectedNode.target);
                    if (data != null && data.attributes.typeObject != null) {
                        ItemService.getItemsByType(data.attributes.typeObject.id, pageable).then(
                            function (data) {
                                items = data;
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        );
                    }
                }

                $timeout(function () {
                    if (items.content.length == 0 && mfrParts.length == 0) {
                        var options = {
                            title: deleteItemTypeDialogTitle,
                            message: deleteItemTypeDialogMessage + " [ " + selectedNode.text + " ] " + itemTypeTitle,
                            okButtonClass: 'btn-danger'
                        };
                        DialogService.confirm(options, function (yes) {
                            if (yes == true) {
                                var selectedNode = classificationTree.tree('getSelected');
                                if (selectedNode != null) {
                                    var data = classificationTree.tree('getData', selectedNode.target);
                                    if (data != null && data.attributes.typeObject != null) {
                                        ClassificationService.deleteType('ITEMTYPE', data.attributes.typeObject.id).then(
                                            function (data) {
                                                classificationTree.tree('remove', selectedNode.target);
                                                $rootScope.showSuccessMessage(selectedNode.attributes.typeObject.name + " : " + deleteTypeSuccessMessage);
                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
                                            })
                                    }
                                }
                            }
                        })
                    } else if (items.content.length != 0) {
                        var options = {
                            title: deleteItemTypeDialogTitle,
                            message: " [ " + selectedNode.text + " ] " + deleteItemTypeExistDialogMessage,
                            okButtonClass: 'btn-danger'
                        };
                        DialogService.confirm(options, function (yes) {
                            if (yes == true) {

                            }
                        })
                    }

                }, 500)

            }

            var itemTitle = $translate.instant("ITEM");
            var changeTitle = $translate.instant("CHANGE");
            var customTitle = $translate.instant("CUSTOMOBJECT");
            var miscellaneousTitle = $translate.instant("MISCELLANEOUS");
            var customTypeTitle = $translate.instant("CUSTOM_TYPE");
            var requirementsTitle = $translate.instant("REQUIREMENTS");
            var specTitle = $translate.instant("SPECIFICATIONS");
            var manufacturerTitle = $translate.instant("MANUFACTURER");
            var manufacturerPartTitle = $translate.instant("MANUFACTURER_PART");
            var plantTitle = $translate.instant("PLANT_TYPE");
            var workCenterTitle = $translate.instant("WORKCENTER_TITLE");
            var machineTitle = $translate.instant("MACHINE_TITLE");
            var toolTitle = $translate.instant("TOOL_TITLE");
            var operationTitle = $translate.instant("OPERATION_TITLE");
            var productionOrderTitle = $translate.instant("PRODUCTION_ORDER_TITLE");
            var serviceOrderTitle = $translate.instant("SERVICE_ORDER_TITLE");
            var workflowTitle = $translate.instant("WORKFLOW");
            var reqTitle = $translate.instant("REQUIREMENT_TITLE");
            var pmTitle = $translate.instant("PROJECT_MANAGEMENT");
            var reqDocTitle = $translate.instant("REQUIREMENTS_DOCUMENT");
            var languageKey = $translate.storage().get($translate.storageKey());

            function loadClassification() {

                $rootScope.showBusyIndicator();

                ClassificationService.getAllClassificationTree().then(
                    function (data) {
                        var itemTypes = data.itemTypes;
                        var customObjectTypes = data.customObjectTypes;
                        var changeTypes = data.changeTypes;
                        var qualityTypes = data.qualityTypes;
                        var pmObjectTypesDto = data.pmObjectTypesDto;
                        var mesObjectTypesDto = data.mesObjectTypesDto;
                        var mroObjectTypesDto = data.mroObjectTypesDto;
                        var pgcObjectTypesDto = data.pgcObjectTypesDto;
                        var manufacturerTypes = data.manufacturerTypes;
                        var manufacturerPartTypes = data.manufacturerPartTypes;
                        var supplierTypes = data.supplierTypes;
                        var workflowTypes = data.workflowTypes;

                        if ($rootScope.isProfileMenu('classification.itemType') && $rootScope.hasPermission('itemtype', 'view')) loadItemClassification(itemTypes);
                        if ($rootScope.isProfileMenu('classification.changeType') && $rootScope.hasPermission('changetype', 'view')) loadChangeClassification(changeTypes);
                        if ($rootScope.isProfileMenu('classification.qualityType') && $rootScope.hasPermission('quality_type', 'view')) loadQualityClassification(qualityTypes);
                        if ($rootScope.isProfileMenu('classification.project-managementType') && $rootScope.checkPMPermission('view')) loadPMObjectsClassification(pmObjectTypesDto);
                        if ($rootScope.isProfileMenu('classification.manufacturingType') && $rootScope.checkMESPermission('view')) loadMESManufacturingClassification(mesObjectTypesDto);
                        if ($rootScope.isProfileMenu('classification.maintenance-repair') && $rootScope.checkMROPermission('view')) loadMROClassification(mroObjectTypesDto);
                        if ($rootScope.isProfileMenu('classification.sourcing') && $rootScope.checkSourcingPermission('view')) loadOEMClassification(manufacturerTypes, manufacturerPartTypes, supplierTypes);
                        if ($rootScope.isProfileMenu('classification.compliance') && $rootScope.checkPGCPermission('view'))loadPGCComplianceClassification(pgcObjectTypesDto);
                        if ($rootScope.isProfileMenu('classification.workflow') && $rootScope.hasPermission('workflowtype', 'view')) loadWorkflowClassification(workflowTypes);
                        if ($rootScope.isProfileMenu('classification.customType') && $rootScope.hasPermission('customobjecttype', 'view')) loadCustomObjectTypeClassification(customObjectTypes);
                        loadCustomTypesClassification();
                        loadAutoNumbers();
                        loadLovs();
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadItemClassification(data) {
                var itemRoot = {
                    id: ++nodeId,
                    text: itemTitle,
                    iconCls: 'itemtype-node',
                    attributes: {
                        root: true,
                        nodeType: 'ITEMTYPE'
                    },
                    children: []
                };
                if (data.length > 0) {
                    itemRoot.state = 'closed'
                }
                var nodes = [];
                angular.forEach(data, function (itemType) {
                    var node = makeNode(itemType);

                    if (itemType.children != null && itemType.children != undefined && itemType.children.length > 0) {
                        node.state = "closed";
                        visitChildren(node, itemType.children);
                    }
                    node.add = $rootScope.hasPermission('itemtype', 'create');
                    node.delete = $rootScope.hasPermission('itemtype', 'delete');
                    nodes.push(node);
                });

                itemRoot.children = nodes;
                itemRoot.add = $rootScope.hasPermission('itemtype', 'create');
                itemRoot.delete = $rootScope.hasPermission('itemtype', 'delete');

                classificationTree.tree('append', {
                    parent: rootNode.target,
                    data: itemRoot
                });
            }

            function loadCustomObjectTypeClassification(data) {
                var customObjectTypeRoot = {
                    id: ++nodeId,
                    text: customTypeTitle,
                    iconCls: 'custom-node',
                    attributes: {
                        root: true,
                        nodeType: 'CUSTOMOBJECTTYPE'
                    },
                    children: []
                };
                if (data.length > 0) {
                    customObjectTypeRoot.state = 'closed'
                }

                var nodes = [];
                angular.forEach(data, function (itemType) {
                    var node = makeCustomObjectNode(itemType);

                    if (itemType.children != null &&
                        itemType.children !== undefined &&
                        itemType.children.length > 0) {
                        node.state = "closed";
                        visitChildren(node, itemType.children);
                    }
                    node.add = $rootScope.hasPermission('customobjecttype', 'create');
                    node.delete = $rootScope.hasPermission('customobjecttype', 'delete');
                    nodes.push(node);
                });

                customObjectTypeRoot.add = $rootScope.hasPermission('customobjecttype', 'create');
                customObjectTypeRoot.delete = $rootScope.hasPermission('customobjecttype', 'delete');
                customObjectTypeRoot.children = nodes;

                classificationTree.tree('append', {
                    parent: rootNode.target,
                    data: customObjectTypeRoot
                });
            }

            function loadChangeClassification(data) {
                var changeRoot = {
                    id: ++nodeId,
                    text: changeTitle,
                    iconCls: 'change-node',
                    attributes: {
                        root: true,
                        nodeType: 'CHANGETYPE'
                    },
                    children: []
                };
                var mcoRoot = {
                    id: ++nodeId,
                    text: "MCO",
                    iconCls: 'change-node',
                    attributes: {
                        root: true,
                        nodeType: 'MCONODE'
                    },
                    children: []
                };
                if (data.length > 0) {
                    changeRoot.state = 'closed'
                }
                var nodes = [];
                var mcoNodes = [];
                angular.forEach(data, function (type) {
                    if (type.objectType == "MCOTYPE") {
                        var mcoNode = makeChangeNode(type);

                        if (type.children != null && type.children != undefined && type.children.length > 0) {
                            mcoNode.state = "closed";
                            visitChildren(mcoNode, type.children);
                        }
                        mcoNode.add = $rootScope.hasPermission('changetype', 'create');
                        mcoNode.delete = $rootScope.hasPermission('changetype', 'delete');
                        mcoNodes.push(mcoNode);
                    } else {
                        var node = makeChangeNode(type);

                        if (type.children != null && type.children != undefined && type.children.length > 0) {
                            node.state = "closed";
                            visitChildren(node, type.children);
                        }
                        node.add = $rootScope.hasPermission('changetype', 'create');
                        node.delete = $rootScope.hasPermission('changetype', 'delete');
                        nodes.push(node);
                    }
                });
                mcoRoot.state = "closed";
                mcoRoot.children = mcoNodes;
                changeRoot.children = nodes;
                changeRoot.add = $rootScope.hasPermission('changetype', 'create');
                changeRoot.delete = $rootScope.hasPermission('changetype', 'delete');
                changeRoot.children.push(mcoRoot);

                classificationTree.tree('append', {
                    parent: rootNode.target,
                    data: changeRoot
                });
            }

            function loadQualityClassification(data) {
                var qualityRoot = {
                    id: ++nodeId,
                    text: "Quality",
                    iconCls: 'quality-node',
                    attributes: {
                        root: true,
                        nodeType: 'QUALITY_TYPE'
                    },
                    children: []
                };

                if (data.length > 0) {
                    qualityRoot.state = 'closed'
                }
                var nodes = [];
                angular.forEach(data, function (type) {
                    var node = makeQualityNode(type);

                    if (type.children != null && type.children != undefined && type.children.length > 0) {
                        node.state = "closed";
                        visitChildren(node, type.children);
                    }
                    node.add = $rootScope.hasPermission('quality_type', 'create');
                    node.delete = $rootScope.hasPermission('quality_type', 'delete');
                    nodes.push(node);
                });
                qualityRoot.add = $rootScope.hasPermission('quality_type', 'create');
                qualityRoot.delete = $rootScope.hasPermission('quality_type', 'delete');
                qualityRoot.children = nodes;

                classificationTree.tree('append', {
                    parent: rootNode.target,
                    data: qualityRoot
                });
            }


            function loadMESManufacturingClassification(data) {
                var resourceRoot = {
                    id: ++nodeId,
                    text: 'Manufacturing',
                    iconCls: 'resource-node',
                    attributes: {
                        root: true,
                        nodeType: 'MESOBJECTTYPE'
                    },
                    children: []
                };
                var masterDataRoot = {
                    id: ++nodeId,
                    text: 'MasterData',
                    iconCls: 'masterData-node',
                    attributes: {
                        root: true,
                        nodeType: 'MASTERDATANODE'
                    },
                    children: []
                };
                if (data != null) {
                    resourceRoot.state = 'closed'
                }

                if ($rootScope.hasPermission('planttype', 'view')) var plantTypes = data.plantTypes;
                if ($rootScope.hasPermission('assemblylinetype', 'view')) var assemblyLineTypes = data.assemblyLineTypes;
                if ($rootScope.hasPermission('workcentertype', 'view')) var workCenterTypes = data.workCenterTypes;
                if ($rootScope.hasPermission('machinetype', 'view')) var machineTypes = data.machineTypes;
                if ($rootScope.hasPermission('equipmenttype', 'view')) var equipmentTypes = data.equipmentTypes;
                if ($rootScope.hasPermission('instrumenttype', 'view')) var instrumentTypes = data.instrumentTypes;
                if ($rootScope.hasPermission('tooltype', 'view')) var toolTypes = data.toolTypes;
                if ($rootScope.hasPermission('jigfixturetype', 'view')) var jigsFixtureTypes = data.jigsFixtureTypes;
                if ($rootScope.hasPermission('materialtype', 'view')) var materialTypes = data.materialTypes;
                if ($rootScope.hasPermission('manpowertype', 'view')) var manpowerTypes = data.manpowerTypes;
                if ($rootScope.hasPermission('operationtype', 'view')) var operationTypes = data.operationTypes;
                if ($rootScope.hasPermission('mbomtype', 'view')) var mbomTypes = data.mbomTypes;
                if ($rootScope.hasPermission('boptype', 'view')) var bopTypes = data.bopTypes;
                if ($rootScope.hasPermission('productionordertype', 'view')) var productionOrderTypes = data.productionOrderTypes;

                var nodes = [];
                var prNodes = [];
                var mbomNodes = [];
                var bopNodes = [];
                var node = null;
                angular.forEach(plantTypes, function (type) {
                    node = makePlantNode(type);
                    if (type.childrens != null && type.childrens != undefined && type.childrens.length > 0) {
                        node.state = "closed";
                        visitMesObjectsTypeChildren(node, type.childrens);
                    }
                    node.add = $rootScope.hasPermission('planttype', 'create');
                    node.delete = $rootScope.hasPermission('planttype', 'delete');
                    nodes.push(node);
                });
                angular.forEach(assemblyLineTypes, function (type) {
                    node = makeAssemblyLineNode(type);
                    if (type.childrens != null && type.childrens != undefined && type.childrens.length > 0) {
                        node.state = "closed";
                        visitMesObjectsTypeChildren(node, type.childrens);
                    }
                    node.add = $rootScope.hasPermission('assemblylinetype', 'create');
                    node.delete = $rootScope.hasPermission('assemblylinetype', 'delete');
                    nodes.push(node);
                });
                angular.forEach(workCenterTypes, function (type) {
                    node = makeWorkCenterNode(type);
                    if (type.childrens != null && type.childrens != undefined && type.childrens.length > 0) {
                        node.state = "closed";
                        visitMesObjectsTypeChildren(node, type.childrens);
                    }
                    node.add = $rootScope.hasPermission('workcentertype', 'create');
                    node.delete = $rootScope.hasPermission('workcentertype', 'delete');
                    nodes.push(node);
                });
                angular.forEach(machineTypes, function (type) {
                    node = makeMachineNode(type);
                    if (type.childrens != null && type.childrens != undefined && type.childrens.length > 0) {
                        node.state = "closed";
                        visitMesObjectsTypeChildren(node, type.childrens);
                    }
                    node.add = $rootScope.hasPermission('machinetype', 'create');
                    node.delete = $rootScope.hasPermission('machinetype', 'delete');
                    nodes.push(node);
                });
                angular.forEach(equipmentTypes, function (type) {
                    node = makeEquipmentNode(type);
                    if (type.childrens != null && type.childrens != undefined && type.childrens.length > 0) {
                        node.state = "closed";
                        visitMesObjectsTypeChildren(node, type.childrens);
                    }
                    node.add = $rootScope.hasPermission('equipmenttype', 'create');
                    node.delete = $rootScope.hasPermission('equipmenttype', 'delete');
                    nodes.push(node);
                });
                angular.forEach(instrumentTypes, function (type) {
                    node = makeInstrumentNode(type);
                    if (type.childrens != null && type.childrens != undefined && type.childrens.length > 0) {
                        node.state = "closed";
                        visitMesObjectsTypeChildren(node, type.childrens);
                    }
                    node.add = $rootScope.hasPermission('instrumenttype', 'create');
                    node.delete = $rootScope.hasPermission('instrumenttype', 'delete');
                    nodes.push(node);
                });
                angular.forEach(toolTypes, function (type) {
                    node = makeToolNode(type);
                    if (type.childrens != null && type.childrens != undefined && type.childrens.length > 0) {
                        node.state = "closed";
                        visitMesObjectsTypeChildren(node, type.childrens);
                    }
                    node.add = $rootScope.hasPermission('tooltype', 'create');
                    node.delete = $rootScope.hasPermission('tooltype', 'delete');
                    nodes.push(node);
                });
                angular.forEach(jigsFixtureTypes, function (type) {
                    node = makeJigsFixtureNode(type);
                    if (type.childrens != null && type.childrens != undefined && type.childrens.length > 0) {
                        node.state = "closed";
                        visitMesObjectsTypeChildren(node, type.childrens);
                    }
                    node.add = $rootScope.hasPermission('jigfixturetype', 'create');
                    node.delete = $rootScope.hasPermission('jigfixturetype', 'delete');
                    nodes.push(node);
                });
                angular.forEach(materialTypes, function (type) {
                    node = makeMaterialNode(type);
                    if (type.childrens != null && type.childrens != undefined && type.childrens.length > 0) {
                        node.state = "closed";
                        visitMesObjectsTypeChildren(node, type.childrens);
                    }
                    node.add = $rootScope.hasPermission('materialtype', 'create');
                    node.delete = $rootScope.hasPermission('materialtype', 'delete');
                    nodes.push(node);
                });
                angular.forEach(manpowerTypes, function (type) {
                    node = makeManPowerNode(type);
                    if (type.childrens != null && type.childrens != undefined && type.childrens.length > 0) {
                        node.state = "closed";
                        visitMesObjectsTypeChildren(node, type.childrens);
                    }
                    node.add = $rootScope.hasPermission('manpowertype', 'create');
                    node.delete = $rootScope.hasPermission('manpowertype', 'delete');
                    nodes.push(node);
                });
                angular.forEach(operationTypes, function (type) {
                    node = makeOperationNode(type);
                    if (type.childrens != null && type.childrens != undefined && type.childrens.length > 0) {
                        node.state = "closed";
                        visitMesObjectsTypeChildren(node, type.childrens);
                    }
                    node.add = $rootScope.hasPermission('operationtype', 'create');
                    node.delete = $rootScope.hasPermission('operationtype', 'delete');
                    nodes.push(node);
                });
                masterDataRoot.children = nodes;
                resourceRoot.children.push(masterDataRoot);
                angular.forEach(mbomTypes, function (type) {
                    node = makeMbomNode(type);
                    if (type.childrens != null && type.childrens != undefined && type.childrens.length > 0) {
                        node.state = "closed";
                        visitMesObjectsTypeChildren(node, type.childrens);
                    }
                    node.add = $rootScope.hasPermission('mbomtype', 'create');
                    node.delete = $rootScope.hasPermission('mbomtype', 'delete');
                    resourceRoot.children.push(node);
                });
                angular.forEach(bopTypes, function (type) {
                    node = makeBopNode(type);
                    if (type.childrens != null && type.childrens != undefined && type.childrens.length > 0) {
                        node.state = "closed";
                        visitMesObjectsTypeChildren(node, type.childrens);
                    }
                    node.add = $rootScope.hasPermission('boptype', 'create');
                    node.delete = $rootScope.hasPermission('boptype', 'delete');
                    resourceRoot.children.push(node);
                });
                angular.forEach(productionOrderTypes, function (type) {
                    node = makeProductionOrderNode(type);
                    if (type.childrens != null && type.childrens != undefined && type.childrens.length > 0) {
                        node.state = "closed";
                        visitMesObjectsTypeChildren(node, type.childrens);
                    }
                    node.add = $rootScope.hasPermission('productionordertype', 'create');
                    node.delete = $rootScope.hasPermission('productionordertype', 'delete');
                    resourceRoot.children.push(node);
                });
                resourceRoot.add = $rootScope.checkMESPermission('create');
                resourceRoot.delete = $rootScope.checkMESPermission('delete');
                masterDataRoot.state = 'closed'

                classificationTree.tree('append', {
                    parent: rootNode.target,
                    data: resourceRoot
                });
            }

            function loadMROClassification(data) {
                var mroRoot = {
                    id: ++nodeId,
                    text: 'Maintenance & Repair',
                    iconCls: 'mroObject-node',
                    attributes: {
                        root: true,
                        nodeType: 'MROOBJECTTYPE'
                    },
                    children: []
                };

                var workOrderRoot = {
                    id: ++nodeId,
                    text: 'Work Order',
                    iconCls: 'workOrder-node',
                    attributes: {
                        root: true,
                        nodeType: 'WORKORDERNODE'
                    },
                    children: []
                };

                if (data != null) {
                    mroRoot.state = 'closed'
                }
                if ($rootScope.hasPermission('assettype', 'view')) var assetTypes = data.assetTypes;
                if ($rootScope.hasPermission('metertype', 'view')) var meterTypes = data.meterTypes;
                if ($rootScope.hasPermission('spareparttype', 'view')) var sparePartTypes = data.mroSparePartTypes;
                if ($rootScope.hasPermission('workrequesttype', 'view')) var workRequestTypes = data.workRequestTypes;
                if ($rootScope.hasPermission('workordertype', 'view')) var workOrderTypes = data.workOrderTypes;

                var nodes = [];
                var workOrderNodes = [];
                var node = null;
                angular.forEach(assetTypes, function (type) {
                    node = makeAssetNode(type);
                    if (type.childrens != null && type.childrens != undefined && type.childrens.length > 0) {
                        node.state = "closed";
                        visitMesObjectsTypeChildren(node, type.childrens);
                    }
                    node.add = $rootScope.hasPermission('assettype', 'create');
                    node.delete = $rootScope.hasPermission('assettype', 'delete');
                    nodes.push(node);
                });

                angular.forEach(meterTypes, function (type) {
                    node = makeMeterNode(type);
                    if (type.childrens != null && type.childrens != undefined && type.childrens.length > 0) {
                        node.state = "closed";
                        visitMesObjectsTypeChildren(node, type.childrens);
                    }
                    node.add = $rootScope.hasPermission('metertype', 'create');
                    node.delete = $rootScope.hasPermission('metertype', 'delete');
                    nodes.push(node);
                });

                angular.forEach(sparePartTypes, function (type) {
                    node = makeSparePartNode(type);
                    if (type.childrens != null && type.childrens != undefined && type.childrens.length > 0) {
                        node.state = "closed";
                        visitMesObjectsTypeChildren(node, type.childrens);
                    }
                    node.add = $rootScope.hasPermission('spareparttype', 'create');
                    node.delete = $rootScope.hasPermission('spareparttype', 'delete');
                    nodes.push(node);
                });

                angular.forEach(workRequestTypes, function (type) {
                    node = makeWorkRequestNode(type);
                    if (type.childrens != null && type.childrens != undefined && type.childrens.length > 0) {
                        node.state = "closed";
                        visitMesObjectsTypeChildren(node, type.childrens);
                    }
                    node.add = $rootScope.hasPermission('workrequesttype', 'create');
                    node.delete = $rootScope.hasPermission('workrequesttype', 'delete');
                    nodes.push(node);
                });

                angular.forEach(workOrderTypes, function (type) {
                    node = makeWorkOrderNode(type);
                    if (type.childrens != null && type.childrens != undefined && type.childrens.length > 0) {
                        node.state = "closed";
                        visitMesObjectsTypeChildren(node, type.childrens);
                    }
                    node.add = $rootScope.hasPermission('workordertype', 'create');
                    node.delete = $rootScope.hasPermission('workordertype', 'delete');
                    workOrderNodes.push(node);
                });
                mroRoot.add = $rootScope.checkMROPermission('create');
                mroRoot.delete = $rootScope.checkMROPermission('delete');
                mroRoot.children = nodes;
                if ($rootScope.hasPermission('workordertype', 'view')) mroRoot.children.push(workOrderRoot);
                if (workOrderNodes.length > 0) {
                    workOrderRoot.state = 'closed';
                }
                workOrderRoot.children = workOrderNodes;
                classificationTree.tree('append', {
                    parent: rootNode.target,
                    data: mroRoot
                });
            }


            function loadPGCComplianceClassification(data) {
                var complianceRoot = {
                    id: ++nodeId,
                    text: 'Compliance',
                    iconCls: 'compliance-node',
                    attributes: {
                        root: true,
                        nodeType: 'PGCOBJECTTYPE'
                    },
                    children: []
                };

                if (data != null) {
                    complianceRoot.state = 'closed'
                }
                if ($rootScope.hasPermission('pgcsubstancetype', 'view')) var substanceTypes = data.substanceTypes;
                if ($rootScope.hasPermission('pgcsubstancetype', 'view')) var substanceGroupTypes = data.substanceGroupTypes;
                if ($rootScope.hasPermission('pgcspecificationtype', 'view')) var specificationTypes = data.specificationTypes;
                if ($rootScope.hasPermission('pgcdeclarationtype', 'view')) var declarationTypes = data.declarationTypes;
                var nodes = [];
                var node = null;
                angular.forEach(substanceTypes, function (type) {
                    node = makeSubstanceNode(type);
                    if (type.childrens != null && type.childrens != undefined && type.childrens.length > 0) {
                        node.state = "closed";
                        visitMesObjectsTypeChildren(node, type.childrens);
                    }
                    node.add = $rootScope.hasPermission('pgcsubstancetype', 'create');
                    node.delete = $rootScope.hasPermission('pgcsubstancetype', 'delete');
                    nodes.push(node);
                });
                angular.forEach(substanceGroupTypes, function (type) {
                    node = makeSubstanceGroupNode(type);
                    if (type.childrens != null && type.childrens != undefined && type.childrens.length > 0) {
                        node.state = "closed";
                        visitMesObjectsTypeChildren(node, type.childrens);
                    }
                    node.add = $rootScope.hasPermission('pgcsubstancetype', 'create');
                    node.delete = $rootScope.hasPermission('pgcsubstancetype', 'delete');
                    nodes.push(node);
                });
                angular.forEach(specificationTypes, function (type) {
                    node = makeSpecificationNode(type);
                    if (type.childrens != null && type.childrens != undefined && type.childrens.length > 0) {
                        node.state = "closed";
                        visitMesObjectsTypeChildren(node, type.childrens);
                    }
                    node.add = $rootScope.hasPermission('pgcspecificationtype', 'create');
                    node.delete = $rootScope.hasPermission('pgcspecificationtype', 'delete');
                    nodes.push(node);
                });
                angular.forEach(declarationTypes, function (type) {
                    node = makeDeclarationNode(type);
                    if (type.childrens != null && type.childrens != undefined && type.childrens.length > 0) {
                        node.state = "closed";
                        visitMesObjectsTypeChildren(node, type.childrens);
                    }
                    node.add = $rootScope.hasPermission('pgcdeclarationtype', 'create');
                    node.delete = $rootScope.hasPermission('pgcdeclarationtype', 'delete');
                    nodes.push(node);
                });
                complianceRoot.add = $rootScope.checkPGCPermission('create');
                complianceRoot.delete = $rootScope.checkPGCPermission('delete');
                complianceRoot.children = nodes;
                classificationTree.tree('append', {
                    parent: rootNode.target,
                    data: complianceRoot
                });
            }


            function loadOEMClassification(mfrs, parts, suppliers) {
                var oemRoot = {
                    id: ++nodeId,
                    text: 'Sourcing',
                    iconCls: 'sourcing-node',
                    attributes: {
                        root: true,
                        nodeType: 'SOURCING'
                    },
                    children: []
                };

                var manufacturerRoot = {
                    id: ++nodeId,
                    text: manufacturerTitle,
                    iconCls: 'mfr-node',
                    attributes: {
                        root: true,
                        nodeType: 'MANUFACTURERTYPE'
                    },
                    children: []
                };

                if (mfrs.length > 0) {
                    manufacturerRoot.state = 'closed'
                }
                var nodes = [];
                angular.forEach(mfrs, function (type) {
                    var node = makeMfrNode(type);

                    if (type.children != null && type.children != undefined && type.children.length > 0) {
                        node.state = "closed";
                        visitChildren(node, type.children);
                    }
                    node.add = $rootScope.hasPermission('manufacturertype', 'create');
                    node.delete = $rootScope.hasPermission('manufacturertype', 'delete');
                    nodes.push(node);
                });
                manufacturerRoot.add = $rootScope.hasPermission('manufacturertype', 'create');
                manufacturerRoot.delete = $rootScope.hasPermission('manufacturertype', 'delete');
                manufacturerRoot.children = nodes;

                var manufacturerPartRoot = {
                    id: ++nodeId,
                    text: manufacturerPartTitle,
                    iconCls: 'mfrpart-node',
                    attributes: {
                        root: true,
                        nodeType: 'MANUFACTURERPARTTYPE'
                    },
                    children: []
                }

                nodes = [];
                if (parts.length > 0) {
                    manufacturerPartRoot.state = 'closed'
                }
                angular.forEach(parts, function (type) {
                    var node = makeMfrPartNode(type);

                    if (type.children != null && type.children != undefined && type.children.length > 0) {
                        node.state = "closed";
                        visitChildren(node, type.children);
                    }
                    node.add = $rootScope.hasPermission('manufacturerparttype', 'create');
                    node.delete = $rootScope.hasPermission('manufacturerparttype', 'delete');
                    nodes.push(node);
                });
                manufacturerPartRoot.add = $rootScope.hasPermission('manufacturerparttype', 'create');
                manufacturerPartRoot.delete = $rootScope.hasPermission('manufacturerparttype', 'delete');
                manufacturerPartRoot.children = nodes;
                var supplierRoot = {
                    id: ++nodeId,
                    text: 'Supplier',
                    iconCls: 'supplierType-node',
                    attributes: {
                        root: true,
                        nodeType: 'SUPPLIERTYPE'
                    },
                    children: []
                };

                nodes = [];
                if (suppliers.length > 0) {
                    supplierRoot.state = 'closed'
                }
                angular.forEach(suppliers, function (type) {
                    var node = makeSupplierNode(type);

                    if (type.children != null && type.children != undefined && type.children.length > 0) {
                        node.state = "closed";
                        visitChildren(node, type.children);
                    }
                    node.add = $rootScope.hasPermission('suppliertype', 'create');
                    node.delete = $rootScope.hasPermission('suppliertype', 'delete');
                    nodes.push(node);
                });
                supplierRoot.add = $rootScope.hasPermission('suppliertype', 'create');
                supplierRoot.delete = $rootScope.hasPermission('suppliertype', 'delete');
                supplierRoot.children = nodes;
                oemRoot.state = "closed";
                oemRoot.children.push(manufacturerRoot);
                oemRoot.children.push(manufacturerPartRoot);
                oemRoot.children.push(supplierRoot);
                classificationTree.tree('append', {
                    parent: rootNode.target,
                    data: oemRoot
                });
            }

            function loadWorkflowClassification(data) {
                var workflowRoot = {
                    id: ++nodeId,
                    text: workflowTitle,
                    iconCls: 'workflow-node',
                    attributes: {
                        root: true,
                        nodeType: 'WORKFLOWTYPE'
                    },
                    children: []
                };

                if (data.length > 0) {
                    workflowRoot.state = 'closed'
                }
                var nodes = [];
                angular.forEach(data, function (type) {
                    var node = makeWorkflowNode(type);

                    if (type.children != null && type.children != undefined && type.children.length > 0) {
                        node.state = "closed";
                        visitChildren(node, type.children);
                    }
                    node.add = $rootScope.hasPermission('workflowtype', 'create');
                    node.delete = $rootScope.hasPermission('workflowtype', 'delete');
                    nodes.push(node);
                });
                workflowRoot.add = $rootScope.hasPermission('workflowtype', 'create');
                workflowRoot.delete = $rootScope.hasPermission('workflowtype', 'delete');
                workflowRoot.children = nodes;

                classificationTree.tree('append', {
                    parent: rootNode.target,
                    data: workflowRoot
                });
            }


            function loadPMObjectsClassification(data) {
                var pmRoot = {
                    id: ++nodeId,
                    text: pmTitle,
                    iconCls: 'project-node',
                    attributes: {
                        root: true,
                        nodeType: 'PMOBJECTTYPE'
                    },
                    children: []
                };
                var reqDocRoot = {
                    id: ++nodeId,
                    text: reqDocTitle,
                    iconCls: 'spec-node',
                    attributes: {
                        root: true,
                        nodeType: 'REQUIREMENTDOCUMENTTYPE'
                    },
                    children: []
                };

                var reqRoot = {
                    id: ++nodeId,
                    text: reqTitle,
                    iconCls: 'req-node',
                    attributes: {
                        root: true,
                        nodeType: 'REQUIREMENTTYPE'
                    },
                    children: []
                };

                if (data != null) {
                    pmRoot.state = 'closed'
                }
                var pmObjectTypes = data.pmObjectTypes;
                var reqDocTypes = data.requirementDocumentTypes;
                var reqTypes = data.requirementTypes;
                var nodes = [];
                var reqDocNodes = [];
                var reqNodes = [];
                angular.forEach(pmObjectTypes, function (type) {
                    var node = makeProjectNode(type);
                    if (type.children != null && type.children != undefined && type.children.length > 0) {
                        node.state = "closed";
                        visitChildren(node, type.children);
                    }
                    node.add = $rootScope.hasPermission('projecttype', 'create');
                    node.delete = $rootScope.hasPermission('projecttype', 'delete');
                    nodes.push(node);
                });
                angular.forEach(reqDocTypes, function (type) {
                    var node = makePMReqDocNode(type);
                    if (type.childrens != null && type.childrens != undefined && type.childrens.length > 0) {
                        node.state = "closed";
                        visitChildren(node, type.childrens);
                    }
                    node.add = $rootScope.hasPermission('requirementdocumenttype', 'create');
                    node.delete = $rootScope.hasPermission('requirementdocumenttype', 'delete');
                    reqDocNodes.push(node);
                });
                angular.forEach(reqTypes, function (type) {
                    var node = makePMReqNode(type);
                    if (type.childrens != null && type.childrens != undefined && type.childrens.length > 0) {
                        node.state = "closed";
                        visitChildren(node, type.childrens);
                    }
                    node.add = $rootScope.hasPermission('requirementtype', 'create');
                    node.delete = $rootScope.hasPermission('requirementtype', 'delete');
                    reqNodes.push(node);
                });
                reqDocRoot.add = $rootScope.hasPermission('requirementdocumenttype', 'create');
                reqDocRoot.delete = $rootScope.hasPermission('requirementdocumenttype', 'delete');
                reqRoot.add = $rootScope.hasPermission('requirementtype', 'create');
                reqRoot.delete = $rootScope.hasPermission('requirementtype', 'delete');
                reqDocRoot.state = "closed";
                reqRoot.state = "closed";
                pmRoot.children = nodes;
                reqDocRoot.children = reqDocNodes;
                reqRoot.children = reqNodes;
                pmRoot.children.push(reqDocRoot);
                pmRoot.children.push(reqRoot);

                classificationTree.tree('append', {
                    parent: rootNode.target,
                    data: pmRoot
                });
            }


            function loadCustomTypesClassification() {
                var customRoot = {
                    id: ++nodeId,
                    text: miscellaneousTitle,
                    iconCls: 'miscellaneous-node',
                    attributes: {
                        root: true,
                        nodeType: 'CUSTOMNODE'
                    },
                    children: []
                };
                var bomRoot = {
                    id: ++nodeId,
                    text: 'Bom Item',
                    iconCls: 'bomItem-node',
                    attributes: {
                        root: true,
                        nodeType: 'BOMITEM'
                    },
                    children: []
                };

                var fileRoot = {
                    id: ++nodeId,
                    text: 'File',
                    iconCls: 'req-node',
                    attributes: {
                        root: true,
                        nodeType: 'FILE'
                    },
                    children: []
                };

                customRoot.state = 'closed';
                customRoot.children.push(bomRoot);
                customRoot.children.push(fileRoot);
                classificationTree.tree('append', {
                    parent: rootNode.target,
                    data: customRoot
                });
            }


            function visitChildren(parent, itemTypes) {
                var nodes = [];
                angular.forEach(itemTypes, function (itemType) {
                    if (parent.attributes.nodeType == 'ITEMTYPE') {
                        var node = makeNode(itemType);
                        node.add = $rootScope.hasPermission('itemtype', 'create');
                        node.delete = $rootScope.hasPermission('itemtype', 'delete');
                    }
                    else if (parent.attributes.nodeType == 'CUSTOMOBJECTTYPE') {
                        var node = makeCustomObjectNode(itemType);
                    }
                    else if (parent.attributes.nodeType == 'CHANGETYPE') {
                        var node = makeChangeNode(itemType);
                        node.add = $rootScope.hasPermission('changetype', 'create');
                        node.delete = $rootScope.hasPermission('changetype', 'delete');
                    }
                    /* else if (parent.attributes.nodeType == 'REQUIREMENTTYPE') {
                     var node = makeReqNode(itemType);
                     }*/
                    else if (parent.attributes.nodeType == 'SPECIFICATIONTYPE') {
                        var node = makeSpecNode(itemType);
                        node.add = $rootScope.hasPermission('pgcspecificationtype', 'create');
                        node.delete = $rootScope.hasPermission('pgcspecificationtype', 'delete');
                    }
                    else if (parent.attributes.nodeType == 'MANUFACTURERTYPE') {
                        var node = makeMfrNode(itemType);
                        node.add = $rootScope.hasPermission('manufacturertype', 'create');
                        node.delete = $rootScope.hasPermission('manufacturertype', 'delete');
                    } else if (parent.attributes.nodeType == 'MANUFACTURERPARTTYPE') {
                        var node = makeMfrPartNode(itemType);
                        node.add = $rootScope.hasPermission('manufacturerparttype', 'create');
                        node.delete = $rootScope.hasPermission('manufacturerparttype', 'delete');
                    }
                    else if (parent.attributes.nodeType == 'WORKFLOWTYPE') {
                        var node = makeWorkflowNode(itemType);
                        node.add = $rootScope.hasPermission('workflowtype', 'create');
                        node.delete = $rootScope.hasPermission('workflowtype', 'delete');
                    }
                    else if (parent.attributes.nodeType == 'QUALITY_TYPE') {
                        var node = makeQualityNode(itemType);
                        node.add = $rootScope.hasPermission('quality_type', 'create');
                        node.delete = $rootScope.hasPermission('quality_type', 'delete');
                    }
                    else if (parent.attributes.nodeType == 'PMOBJECTTYPE') {
                        var node = makeProjectNode(itemType);
                        node.add = $rootScope.hasPermission('pmobjecttype', 'create');
                        node.delete = $rootScope.hasPermission('pmobjecttype', 'delete');
                    }
                    else if (parent.attributes.nodeType == 'REQUIREMENTTYPE') {
                        var node = makePMReqNode(itemType);
                        node.add = $rootScope.hasPermission('requirementtype', 'create');
                        node.delete = $rootScope.hasPermission('requirementtype', 'delete');
                    }
                    else if (parent.attributes.nodeType == 'REQUIREMENTDOCUMENTTYPE') {
                        var node = makePMReqDocNode(itemType);
                        node.add = $rootScope.hasPermission('requirementdocumenttype', 'create');
                        node.delete = $rootScope.hasPermission('requirementdocumenttype', 'delete');
                    }
                    else if (parent.attributes.nodeType == 'SUPPLIERTYPE') {
                        var node = makeSupplierNode(itemType);
                        node.add = $rootScope.hasPermission('suppliertype', 'create');
                        node.delete = $rootScope.hasPermission('suppliertype', 'delete');
                    }

                    if (itemType.children != null && itemType.children != undefined && itemType.children.length > 0) {
                        node.state = 'closed';
                        visitChildren(node, itemType.children);
                    }

                    nodes.push(node);
                });

                if (nodes.length > 0) {
                    parent.children = nodes;
                }
            }


            function visitMesObjectsTypeChildren(parent, itemTypes) {
                var nodes = [];
                angular.forEach(itemTypes, function (itemType) {
                    if (parent.attributes.nodeType == 'OPERATIONTYPE') {
                        var node = makeOperationNode(itemType);
                        node.add = $rootScope.hasPermission('operationtype', 'create');
                        node.delete = $rootScope.hasPermission('operationtype', 'delete');
                    }
                    else if (parent.attributes.nodeType == 'PLANTTYPE') {
                        var node = makePlantNode(itemType);
                        node.add = $rootScope.hasPermission('planttype', 'create');
                        node.delete = $rootScope.hasPermission('planttype', 'delete');
                    }
                    else if (parent.attributes.nodeType == 'WORKCENTERTYPE') {
                        var node = makeWorkCenterNode(itemType);
                        node.add = $rootScope.hasPermission('workcentertype', 'create');
                        node.delete = $rootScope.hasPermission('workcentertype', 'delete');
                    }
                    else if (parent.attributes.nodeType == 'MACHINETYPE') {
                        var node = makeMachineNode(itemType);
                        node.add = $rootScope.hasPermission('machinetype', 'create');
                        node.delete = $rootScope.hasPermission('machinetype', 'delete');
                    }
                    else if (parent.attributes.nodeType == 'EQUIPMENTTYPE') {
                        var node = makeEquipmentNode(itemType);
                        node.add = $rootScope.hasPermission('equipmenttype', 'create');
                        node.delete = $rootScope.hasPermission('equipmenttype', 'delete');
                    }
                    else if (parent.attributes.nodeType == 'INSTRUMENTTYPE') {
                        var node = makeInstrumentNode(itemType);
                        node.add = $rootScope.hasPermission('instrumenttype', 'create');
                        node.delete = $rootScope.hasPermission('instrumenttype', 'delete');
                    }
                    else if (parent.attributes.nodeType == 'MATERIALTYPE') {
                        var node = makeMaterialNode(itemType);
                        node.add = $rootScope.hasPermission('materialtype', 'create');
                        node.delete = $rootScope.hasPermission('materialtype', 'delete');
                    }
                    else if (parent.attributes.nodeType == 'JIGFIXTURETYPE') {
                        var node = makeJigsFixtureNode(itemType);
                        node.add = $rootScope.hasPermission('jigfixturetype', 'create');
                        node.delete = $rootScope.hasPermission('jigfixturetype', 'delete');

                    } else if (parent.attributes.nodeType == 'TOOLTYPE') {
                        var node = makeToolNode(itemType);
                        node.add = $rootScope.hasPermission('tooltype', 'create');
                        node.delete = $rootScope.hasPermission('tooltype', 'delete');
                    }
                    else if (parent.attributes.nodeType == 'MBOMTYPE') {
                        var node = makeMbomNode(itemType);
                        node.add = $rootScope.hasPermission('mbomtype', 'create');
                        node.delete = $rootScope.hasPermission('mbomtype', 'delete');
                    }
                    else if (parent.attributes.nodeType == 'BOPTYPE') {
                        var node = makeBopNode(itemType);
                        node.add = $rootScope.hasPermission('boptype', 'create');
                        node.delete = $rootScope.hasPermission('boptype', 'delete');
                    }
                    else if (parent.attributes.nodeType == 'PRODUCTIONORDERTYPE') {
                        var node = makeProductionOrderNode(itemType);
                        node.add = $rootScope.hasPermission('productionordertype', 'create');
                        node.delete = $rootScope.hasPermission('productionordertype', 'delete');
                    }
                    else if (parent.attributes.nodeType == 'MANPOWERTYPE') {
                        var node = makeManPowerNode(itemType);
                        node.add = $rootScope.hasPermission('manpowertype', 'create');
                        node.delete = $rootScope.hasPermission('manpowertype', 'delete');
                    }
                    else if (parent.attributes.nodeType == 'SPAREPARTTYPE') {
                        var node = makeSparePartNode(itemType);
                        node.add = $rootScope.hasPermission('spareparttype', 'create');
                        node.delete = $rootScope.hasPermission('spareparttype', 'delete');
                    }
                    else if (parent.attributes.nodeType == 'ASSETTYPE') {
                        var node = makeAssetNode(itemType);
                        node.add = $rootScope.hasPermission('assettype', 'create');
                        node.delete = $rootScope.hasPermission('assettype', 'delete');
                    }
                    else if (parent.attributes.nodeType == 'METERTYPE') {
                        var node = makeMeterNode(itemType);
                        node.add = $rootScope.hasPermission('metertype', 'create');
                        node.delete = $rootScope.hasPermission('metertype', 'delete');
                    }
                    else if (parent.attributes.nodeType == 'WORKREQUESTTYPE') {
                        var node = makeWorkRequestNode(itemType);
                        node.add = $rootScope.hasPermission('workrequesttype', 'create');
                        node.delete = $rootScope.hasPermission('workrequesttype', 'delete');
                    }
                    else if (parent.attributes.nodeType == 'WORKORDERTYPE') {
                        var node = makeWorkOrderNode(itemType);
                        node.add = $rootScope.hasPermission('workordertype', 'create');
                        node.delete = $rootScope.hasPermission('workordertype', 'delete');
                    }
                    else if (parent.attributes.nodeType == 'PGCSUBSTANCETYPE') {
                        var node = makeSubstanceNode(itemType);
                        node.add = $rootScope.hasPermission('pgcsubstancetype', 'create');
                        node.delete = $rootScope.hasPermission('pgcsubstancetype', 'delete');
                    } else if (parent.attributes.nodeType == 'PGCSUBSTANCEGROUPTYPE') {
                        var node = makeSubstanceGroupNode(itemType);
                        node.add = $rootScope.hasPermission('pgcsubstancetype', 'create');
                        node.delete = $rootScope.hasPermission('pgcsubstancetype', 'delete');
                    } else if (parent.attributes.nodeType == 'PGCSPECIFICATIONTYPE') {
                        var node = makeSpecificationNode(itemType);
                        node.add = $rootScope.hasPermission('pgcspecificationtype', 'create');
                        node.delete = $rootScope.hasPermission('pgcspecificationtype', 'delete');
                    } else if (parent.attributes.nodeType == 'PGCDECLARATIONTYPE') {
                        var node = makeDeclarationNode(itemType);
                        node.add = $rootScope.hasPermission('pgcdeclarationtype', 'create');
                        node.delete = $rootScope.hasPermission('pgcdeclarationtype', 'delete');
                    } else if (parent.attributes.nodeType == 'ASSEMBLYLINETYPE') {
                        var node = makeAssemblyLineNode(itemType);
                        node.add = $rootScope.hasPermission('assemblylinetype', 'create');
                        node.delete = $rootScope.hasPermission('assemblylinetype', 'delete');
                    }
                    if (itemType.childrens != null && itemType.childrens != undefined && itemType.childrens.length > 0) {
                        node.state = 'closed';
                        visitMesObjectsTypeChildren(node, itemType.childrens);
                    }

                    nodes.push(node);
                });
                if (nodes.length > 0) {
                    parent.children = nodes;
                }
            }

            function makeNode(itemType) {
                return {
                    id: ++nodeId,
                    text: itemType.name,
                    iconCls: 'itemtype-node',
                    attributes: {
                        typeObject: itemType,
                        nodeType: 'ITEMTYPE'
                    }
                };
            }

            function makeCustomObjectNode(type) {
                return {
                    id: ++nodeId,
                    text: type.name,
                    iconCls: 'custom-node',
                    attributes: {
                        typeObject: type,
                        nodeType: 'CUSTOMOBJECTTYPE'
                    }
                };
            }

            function makeChangeNode(type) {
                return {
                    id: ++nodeId,
                    text: type.name,
                    iconCls: 'change-node',
                    attributes: {
                        typeObject: type,
                        nodeType: 'CHANGETYPE'
                    }
                };
            }

            function makeMfrNode(type) {
                return {
                    id: ++nodeId,
                    text: type.name,
                    iconCls: 'mfr-node',
                    attributes: {
                        typeObject: type,
                        nodeType: 'MANUFACTURERTYPE'
                    }
                };
            }

            function makeReqNode(type) {
                return {
                    id: ++nodeId,
                    text: type.name,
                    iconCls: 'req-node',
                    attributes: {
                        typeObject: type,
                        nodeType: 'REQUIREMENTTYPE'
                    }
                };
            }

            function makeSpecNode(type) {
                return {
                    id: ++nodeId,
                    text: type.name,
                    iconCls: 'spec-node',
                    attributes: {
                        typeObject: type,
                        nodeType: 'SPECIFICATIONTYPE'
                    }
                };
            }

            function makeMfrPartNode(type) {
                return {
                    id: ++nodeId,
                    text: type.name,
                    iconCls: 'mfrpart-node',
                    attributes: {
                        typeObject: type,
                        nodeType: 'MANUFACTURERPARTTYPE'
                    }
                };
            }

            function makeOperationNode(type) {
                return {
                    id: ++nodeId,
                    text: type.name,
                    iconCls: 'operation-node',
                    attributes: {
                        typeObject: type,
                        nodeType: 'OPERATIONTYPE'
                    }
                };
            }

            function makeMbomNode(type) {
                return {
                    id: ++nodeId,
                    text: type.name,
                    iconCls: 'classification-root',
                    attributes: {
                        typeObject: type,
                        nodeType: 'MBOMTYPE'
                    }
                };
            }

            function makeBopNode(type) {
                return {
                    id: ++nodeId,
                    text: type.name,
                    iconCls: 'bop-node',
                    attributes: {
                        typeObject: type,
                        nodeType: 'BOPTYPE'
                    }
                };
            }

            function makePlantNode(type) {
                return {
                    id: ++nodeId,
                    text: type.name,
                    iconCls: 'plant-node',
                    attributes: {
                        typeObject: type,
                        nodeType: 'PLANTTYPE'
                    }
                };
            }

            function makeAssemblyLineNode(type) {
                return {
                    id: ++nodeId,
                    text: type.name,
                    iconCls: 'assemblyLine-node',
                    attributes: {
                        typeObject: type,
                        nodeType: 'ASSEMBLYLINETYPE'
                    }
                };
            }

            function makeMaterialNode(type) {
                return {
                    id: ++nodeId,
                    text: type.name,
                    iconCls: 'material-node',
                    attributes: {
                        typeObject: type,
                        nodeType: 'MATERIALTYPE'
                    }
                };
            }

            function makeJigsFixtureNode(type) {
                return {
                    id: ++nodeId,
                    text: type.name,
                    iconCls: 'jigs-node',
                    attributes: {
                        typeObject: type,
                        nodeType: 'JIGFIXTURETYPE'
                    }
                };
            }

            function makeProjectNode(type) {
                return {
                    id: ++nodeId,
                    text: type.name,
                    iconCls: 'project-node',
                    attributes: {
                        typeObject: type,
                        nodeType: 'PMOBJECTTYPE'
                    }
                };
            }

            function makePMReqNode(type) {
                return {
                    id: ++nodeId,
                    text: type.name,
                    iconCls: 'req-node',
                    attributes: {
                        typeObject: type,
                        nodeType: 'REQUIREMENTTYPE'
                    }
                };
            }


            function makeSupplierNode(type) {
                return {
                    id: ++nodeId,
                    text: type.name,
                    iconCls: 'supplierType-node',
                    attributes: {
                        typeObject: type,
                        nodeType: 'SUPPLIERTYPE'
                    }
                };
            }

            function makePMReqDocNode(type) {
                return {
                    id: ++nodeId,
                    text: type.name,
                    iconCls: 'spec-node',
                    attributes: {
                        typeObject: type,
                        nodeType: 'REQUIREMENTDOCUMENTTYPE'
                    }
                };
            }

            function makeToolNode(type) {
                return {
                    id: ++nodeId,
                    text: type.name,
                    iconCls: 'tool-node',
                    attributes: {
                        typeObject: type,
                        nodeType: 'TOOLTYPE'
                    }
                };
            }

            function makeProductionOrderNode(type) {
                return {
                    id: ++nodeId,
                    text: type.name,
                    iconCls: 'production-node',
                    attributes: {
                        typeObject: type,
                        nodeType: 'PRODUCTIONORDERTYPE'
                    }
                };
            }

            function makeMachineNode(type) {
                return {
                    id: ++nodeId,
                    text: type.name,
                    iconCls: 'machine-node',
                    attributes: {
                        typeObject: type,
                        nodeType: 'MACHINETYPE'
                    }
                };
            }

            function makeEquipmentNode(type) {
                return {
                    id: ++nodeId,
                    text: type.name,
                    iconCls: 'equipment-node',
                    attributes: {
                        typeObject: type,
                        nodeType: 'EQUIPMENTTYPE'
                    }
                };
            }

            function makeInstrumentNode(type) {
                return {
                    id: ++nodeId,
                    text: type.name,
                    iconCls: 'instrument-node',
                    attributes: {
                        typeObject: type,
                        nodeType: 'INSTRUMENTTYPE'
                    }
                };
            }

            function makeManPowerNode(type) {
                return {
                    id: ++nodeId,
                    text: type.name,
                    iconCls: 'manpower-node',
                    attributes: {
                        typeObject: type,
                        nodeType: 'MANPOWERTYPE'
                    }
                };
            }

            function makeSparePartNode(type) {
                return {
                    id: ++nodeId,
                    text: type.name,
                    iconCls: 'sparepart-node',
                    attributes: {
                        typeObject: type,
                        nodeType: 'SPAREPARTTYPE'
                    }
                };
            }

            function makeAssetNode(type) {
                return {
                    id: ++nodeId,
                    text: type.name,
                    iconCls: 'asset-node',
                    attributes: {
                        typeObject: type,
                        nodeType: 'ASSETTYPE'
                    }
                };
            }

            function makeMeterNode(type) {
                return {
                    id: ++nodeId,
                    text: type.name,
                    iconCls: 'meter-node',
                    attributes: {
                        typeObject: type,
                        nodeType: 'METERTYPE'
                    }
                };
            }

            function makeWorkRequestNode(type) {
                return {
                    id: ++nodeId,
                    text: type.name,
                    iconCls: 'workRequest-node',
                    attributes: {
                        typeObject: type,
                        nodeType: 'WORKREQUESTTYPE'
                    }
                };
            }

            function makeWorkOrderNode(type) {
                return {
                    id: ++nodeId,
                    text: type.name,
                    iconCls: 'workOrder-node',
                    attributes: {
                        typeObject: type,
                        nodeType: 'WORKORDERTYPE'
                    }
                };
            }

            function makeSubstanceNode(type) {
                return {
                    id: ++nodeId,
                    text: type.name,
                    iconCls: 'substance-node',
                    attributes: {
                        typeObject: type,
                        nodeType: 'PGCSUBSTANCETYPE'
                    }
                };
            }

            function makeSubstanceGroupNode(type) {
                return {
                    id: ++nodeId,
                    text: type.name,
                    iconCls: 'subGroup-node',
                    attributes: {
                        typeObject: type,
                        nodeType: 'PGCSUBSTANCEGROUPTYPE'
                    }
                };
            }

            function makeSpecificationNode(type) {
                return {
                    id: ++nodeId,
                    text: type.name,
                    iconCls: 'specification-node',
                    attributes: {
                        typeObject: type,
                        nodeType: 'PGCSPECIFICATIONTYPE'
                    }
                };
            }

            function makeDeclarationNode(type) {
                return {
                    id: ++nodeId,
                    text: type.name,
                    iconCls: 'declaration-node',
                    attributes: {
                        typeObject: type,
                        nodeType: 'PGCDECLARATIONTYPE'
                    }
                };
            }

            function makeWorkCenterNode(type) {
                return {
                    id: ++nodeId,
                    text: type.name,
                    iconCls: 'workCenter-node',
                    attributes: {
                        typeObject: type,
                        nodeType: 'WORKCENTERTYPE'
                    }
                };
            }

            function makeWorkflowNode(type) {
                return {
                    id: ++nodeId,
                    text: type.name,
                    iconCls: 'workflow-node',
                    attributes: {
                        typeObject: type,
                        nodeType: 'WORKFLOWTYPE'
                    }
                };
            }

            function makeQualityNode(type) {
                return {
                    id: ++nodeId,
                    text: type.name,
                    iconCls: 'quality-node',
                    attributes: {
                        typeObject: type,
                        nodeType: "QUALITY_TYPE"
                    }
                };
            }

            function onSave() {
                $scope.$broadcast('app.itemtype.save');
            }

            function update(event, args) {
                var node = classificationTree.tree('find', args.nodeId);
                if (node) {
                    classificationTree.tree('update', {
                        target: node.target,
                        text: args.nodeName
                    });
                }
            }

            function isExists(parent, node) {
                var exists = false;
                var count = 0;
                angular.forEach(parent.children, function (child) {
                    if (child.text == node.text) {
                        count++;
                    }
                });

                if (count > 1 || node.text == parent.text) {
                    exists = true;
                    $rootScope.showWarningMessage(node.text + ":" + Duplicate);
                    $scope.$apply();
                }
                return exists;
            }

            vm.collapseAll = collapseAll;
            vm.expandAll = expandAll;

            function collapseAll() {
                var node = $('#classificationTree').tree('getSelected');
                if (node) {
                    if (node.attributes.nodeType != "ROOT") {
                        $('#classificationTree').tree('collapseAll', node.target);
                    } else {
                        angular.forEach(node.children, function (child) {
                            $('#classificationTree').tree('collapseAll', child.target);
                        })
                    }

                }
                else {
                    $('#classificationTree').tree('collapseAll');
                }
            }

            function expandAll() {
                var node = $('#classificationTree').tree('getSelected');
                if (node) {
                    $('#classificationTree').tree('expandAll', node.target);
                }
                else {
                    $('#classificationTree').tree('expandAll');
                }
            }

            vm.searchTree = searchTree;
            vm.searchValue = null;
            function searchTree() {
                if (vm.searchValue != null) {
                    $('#classificationTree').tree('expandAll');
                }
                $('#classificationTree').tree('doFilter', vm.searchValue);
            }

            function loadAutoNumbers() {
                AutonumberService.getAutonumbers().then(
                    function (data) {
                        $rootScope.autoNumbers = data;
                        angular.forEach(data, function (item) {
                            if (item.name == "Default Part Number Source") {
                                vm.defaultPartNumberSource = item;
                            }
                            else if (item.name == "Default Inspection Number Source") {
                                vm.defaultInspectionNumberSource = item;
                            }
                            else if (item.name == "Default Inspection Plan Number Source") {
                                vm.defaultInspectionPlanNumberSource = item;
                            }
                            else if (item.name == "Default Workflow Number Source") {
                                vm.defaultWorkflowNumberSource = item;
                            }
                            else if (item.name == "Default Plant Number Source") {
                                vm.defaultPlantNumberSource = item;
                            }
                            else if (item.name == "Default Mbom Number Source") {
                                vm.defaultMbomNumberSource = item;
                            }
                            else if (item.name == "Default Bop Number Source") {
                                vm.defaultBopNumberSource = item;
                            }
                            else if (item.name == "Default Production Order Number Source") {
                                vm.defaultProductionOrderNumberSource = item;
                            }
                            else if (item.name == "Default Service Order Number Source") {
                                vm.defaultServiceOrderNumberSource = item;
                            }
                            else if (item.name == "Default WorkCenter Number Source") {
                                vm.defaultWorkCenterNumberSource = item;
                            }
                            else if (item.name == "Default Operation Number Source") {
                                vm.defaultOperationNumberSource = item;
                            }
                            else if (item.name == "Default Machine Number Source") {
                                vm.defaultMachineNumberSource = item;
                            }
                            else if (item.name == "Default Shift Number Source") {
                                vm.defaultShiftNumberSource = item;
                            }
                            else if (item.name == "Default Manpower Number Source") {
                                vm.defaultManpowerNumberSource = item;
                            }
                            else if (item.name == "Default JigsAndFixture Number Source") {
                                vm.defaultJigsFixNumberSource = item;
                            }
                            else if (item.name == "Default Tool Number Source") {
                                vm.defaultToolNumberSource = item;
                            }
                            else if (item.name == "Default Material Number Source") {
                                vm.defaultMaterialNumberSource = item;
                            }
                            else if (item.name == "Default Equipment Number Source") {
                                vm.defaultEquipmentNumberSource = item;
                            }
                            else if (item.name == "Default Instrument Number Source") {
                                vm.defaultInstrumentNumberSource = item;
                            }
                            else if (item.name == "Default Project Number Source") {
                                vm.defaultProjectNumberSource = item;
                            }
                            else if (item.name == "Default Program Number Source") {
                                vm.defaultProgramNumberSource = item;
                            }
                            else if (item.name == "Default Requirements Document Source") {
                                vm.defaultRequirementDocumentNumberSource = item;
                            }
                            else if (item.name == "Default Requirement Number Source") {
                                vm.defaultRequirementNumberSource = item;
                            }
                            else if (item.name == "Default SparePart Number Source") {
                                vm.defaultSparePartNumberSource = item;
                            }
                            else if (item.name == "Default Supplier OEM Number Source") {
                                vm.defaultSupplierNumberSource = item;
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadLovs() {
                CommonService.getLovs('Revision Sequence').then(
                    function (data) {
                        $rootScope.revSequences = data.content;
                        angular.forEach(data.content, function (item) {
                            if (item.name == 'Default Revision Sequence') {
                                vm.defaultRevisionSequence = item;
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );

                LovService.getAllLovs().then(
                    function (data) {
                        $rootScope.listOfValues = data;
                        angular.forEach(data, function (item) {
                            if (item.name == 'Default Failure Types') {
                                vm.defaultFailureType = item;
                            }
                            if (item.name == 'Default Severities') {
                                vm.defaultSeverities = item;
                            }
                            if (item.name == 'Default Dispositions') {
                                vm.defaultDispositions = item;
                            }
                            if (item.name == 'Default Requirement Priority List') {
                                vm.defaultReqRevisionSequence = item;
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );

                ItemTypeService.getLifeCycles().then(
                    function (data) {
                        $rootScope.lifecycles = data;
                        angular.forEach(data, function (item) {
                            if (item.name == 'Default' || item.name == 'Default Lifecycle') {
                                vm.defaultLifecycle = item;
                            }
                            if (item.name == 'Default Manufacturer Lifecycle') {
                                vm.defaultMfrLifecycle = item;
                            }
                            if (item.name == 'Default ManufacturerPart Lifecycle') {
                                vm.defaultMfrPartLifecycle = item;
                            }
                            if (item.name == 'Default Workflow Lifecycle') {
                                vm.defaultWorkflowLifecycle = item;
                            }
                            if (item.name == 'Default Requirements Document Lifecycle') {
                                vm.defaultReqDocLifecycle = item;
                            }
                            if (item.name == 'Default Requirement Lifecycle') {
                                vm.defaultReqLifecycle = item;
                            }
                            if (item.name == 'Default Supplier Lifecycle') {
                                vm.defaultSupplierLifecycle = item;
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            (function () {
                $scope.$on('$viewContentLoaded', function () {
                    $('div.split-right-pane').css({left: 300});
                    $('div.split-pane').splitPane();
                    $scope.$on("app.classification.update", update);
                    initClassificationTree();
                });
            })();
        }
    }
)
;