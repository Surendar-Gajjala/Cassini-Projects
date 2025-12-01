define(
    [
        'app/desktop/modules/item/item.module',
        'moment',
        'moment-timezone-with-data',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/itemBomService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/workflowDefinitionService',
        'app/shared/services/core/shareService',
        'app/shared/services/core/itemFileService',
        'app/shared/services/core/classificationService',
        'app/shared/services/core/eximService',
        'app/shared/services/core/mesObjectTypeService',
        'app/shared/services/core/reqDocumentService',
        'app/shared/services/core/requirementService',
        'app/desktop/modules/item/details/itemSelectionController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/customObjectService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'

    ],
    function (module) {
        module.controller('ItemBomController', ItemBomController);

        function ItemBomController($scope, $rootScope, $timeout, $application, $sce, $state, $stateParams, $cookies, $window, ItemTypeService, ItemService,
                                   ItemBomService, ObjectAttributeService, DialogService, $uibModal, $translate, ObjectTypeAttributeService,
                                   AttributeAttachmentService, ECOService, MfrService, MfrPartsService, WorkflowDefinitionService,
                                   ShareService, CommonService, ItemFileService, ClassificationService, ExImService, MESObjectTypeService,
                                   CustomObjectService, ReqDocumentService, RequirementService) {
            var vm = this;

            var dataAlreadyLoaded = false;
            vm.loading = true;
            vm.itemId = $stateParams.itemId;
            vm.bomItems = [];
            vm.searchItems = [];
            vm.bomRevisions = [];
            vm.revisions = [];
            vm.bomIds = [];
            vm.selectedBomAttributeIds = [];
            $rootScope.selectedAttributes = [];
            vm.itemBomImageAttributes = [];
            vm.expandedAll = false;
            vm.bomRules = {
                latest: {
                    key: 'bom.latest',
                    label: 'Latest Revision'
                },
                latestReleased: {
                    key: 'bom.latest.released',
                    label: 'Latest Released Revision'
                },
                asReleased: {
                    key: 'bom.as.released',
                    label: 'As Released Revision'
                }
            };

            vm.selectedBomRule = vm.bomRules.latest.key;

            vm.toggleNode = toggleNode;
            vm.onOk = onOk;
            vm.onCancel = onCancel;
            vm.findItem = findItem;
            vm.editItem = editItem;
            vm.deleteItem = deleteItem;
            vm.itemSelection = itemSelection;
            vm.applyBomRuleForAll = applyBomRuleForAll;
            vm.loadBom = loadBom;
            vm.removeAttribute = removeAttribute;
            vm.showImage = showImage;
            vm.showObjectViews = showObjectViews;
            vm.selectedBomAttributes = [];
            vm.itemBomImages = new Hashtable();
            vm.selectedAttIds = [];
            vm.attachmentIds = [];
            vm.objectIds = [];
            vm.copiedItems = [];
            vm.copiedItems = $application.clipboard.items;

            vm.editBomPermission = $rootScope.hasPermission('itembom', 'edit');
            vm.deleteBomPermission = $rootScope.hasPermission('itembom', 'delete');
            vm.adminPermission = $rootScope.hasPermission('admin', 'all');
            vm.createBomPermission = $rootScope.hasPermission('itembom', 'create');
            vm.editItemPermission = $rootScope.hasPermission('item', 'edit');

            var parsed = angular.element("<div></div>");
            var submitButton = parsed.html($translate.instant("SUBMIT")).html();
            var addButton = parsed.html($translate.instant("ADD")).html();
            var lastSelectedItem = null;
            var currencyMap = new Hashtable();

            var attributeTitle = $translate.instant("ATTRIBUTES");
            var selectedAttributesMessage = parsed.html($translate.instant("SELECTED_ATTRIBUTES_MESSAGE")).html();
            var quantityWarningMessage = parsed.html($translate.instant("QUANTITY_WARNING_MESSAGE")).html();
            var itemWarningMessage = parsed.html($translate.instant("SELECT_FOLDERITEM")).html();
            var bomItemCreatedMessage = parsed.html($translate.instant("BOM_ITEM_CREATED_MESSAGE")).html();
            var bomItemUpdateMessage = parsed.html($translate.instant("BOM_ITEM_UPDATED_MESSAGE")).html();
            var bomItemSavedMessage = parsed.html($translate.instant("BOM_ITEM_SAVED_MESSAGE")).html();
            var bomItemDeleteMessage = parsed.html($translate.instant("BOM_ITEM_DELETED_MESSAGE")).html();
            var itemDelete = parsed.html($translate.instant("ITEMDELETE")).html();
            var bomItemTitleMessage = parsed.html($translate.instant("BOM_ITEM_DELETE_TITLE")).html();
            var bomItemDialogMessage = parsed.html($translate.instant("BOM_ITEM_DELETE_MESSAGE")).html();
            var itemSelectionTitle = parsed.html($translate.instant("BOM_ITEM_SELECTION")).html();
            var itemExistMessage = parsed.html($translate.instant("BOM_ITEM_EXIST_MESSAGE")).html();
            var itemNotExist = parsed.html($translate.instant("ITEM_NOT_EXIST")).html();
            var itemsNotAddedToUnresolvedParent = parsed.html($translate.instant("ITEM_NOT_ADD_TO_UNRESOLVED_PARENT")).html();
            var itemsNotAddedToReleasedParent = parsed.html($translate.instant("ITEM_NOT_ADD_TO_RELEASE_ITEM")).html();
            var multipleItemsSelectionTitle = parsed.html($translate.instant("MULTIPLE_ITEM_SELECTION")).html();
            $scope.clickToSelectTitle = parsed.html($translate.instant("CLICK_TO_SELECT")).html();
            $scope.selectTitle = parsed.html($translate.instant("SELECT")).html();
            $scope.editThisItemTitle = parsed.html($translate.instant("EDIT_THIS_ITEM")).html();
            $scope.addItemTitle = parsed.html($translate.instant("ADD_THIS_ITEM")).html();
            $scope.deleteThisItemTitle = parsed.html($translate.instant("DELETE_THIS_ITEM")).html();
            $scope.saveItemTitle = parsed.html($translate.instant("SAVE_THIS_ITEM")).html();
            vm.AddItem = parsed.html($translate.instant("ADD_ITEM")).html();
            vm.ExpandCollapse = parsed.html($translate.instant("EXPAND_COLLAPSE")).html();
            $scope.expandAll = parsed.html($translate.instant("EXPAND_ALL")).html();
            $scope.collapseAll = parsed.html($translate.instant("COLLAPSE_ALL")).html();
            $scope.selectBomConfigurationTitle = parsed.html($translate.instant("SELECT_BOM_CONFIGURATION")).html();
            $scope.enterName = parsed.html($translate.instant("ENTER_NAME")).html();
            $scope.enterDescription = parsed.html($translate.instant("ENTER_DESCRIPTION_TITLE")).html();
            $scope.bomRollUpReportTitle = parsed.html($translate.instant("BOM_ROLL_UP_REPORT")).html();
            $scope.totalTitle = parsed.html($translate.instant("TOTAL")).html();
            $scope.unitTitle = parsed.html($translate.instant("UNIT")).html();

            var thereIsNoConfigurableItems = parsed.html($translate.instant("THERE_IS_NO_CONFIGURABLE_ITEMS")).html();
            var pleaseEnterQuantity = parsed.html($translate.instant("PLEASE_ENTER_QUANTITY")).html();
            var pleaseEnterQuantityForItem = parsed.html($translate.instant("PLEASE_ENTER_QUANTITY_FOR_ITEM")).html();
            var pleaseEnterPositiveQuantity = parsed.html($translate.instant("PLEASE_ENTER_POSITIVE_QUANTITY")).html();
            var pleaseEnterPositiveQuantityForItem = parsed.html($translate.instant("PLEASE_ENTER_POSITIVE_QUANTITY_FOR_ITEM")).html();
            var bomImported = parsed.html($translate.instant("BOM_IMPORTED")).html();
            var bomSequenceUpdated = parsed.html($translate.instant("BOM_SEQUENCE_UPDATED")).html();
            var itemsAddedToBomSuccessfullyAnd = parsed.html($translate.instant("ITEMS_ADDED_TO_BOM_SUCCESSFULLY_AND")).html();
            var itemsAddedToBomSuccessfully = parsed.html($translate.instant("ITEMS_ADDED_TO_BOM_SUCCESSFULLY")).html();
            var undoSuccessful = parsed.html($translate.instant("UNDO_SUCCESSFUL")).html();
            var bomItemResolved = parsed.html($translate.instant("BOM_ITEM_RESOLVED")).html();
            var bomItemCannotResolve = parsed.html($translate.instant("BOM_ITEM_CANNOT_RESOLVE")).html();
            var bomConfigurationSaved = parsed.html($translate.instant("BOM_CONFIGURATION_SAVED")).html();
            var pleaseEnterName = parsed.html($translate.instant("PLEASE_ENTER_NAME")).html();
            var pleaseSelect = parsed.html($translate.instant("PLEASE_SELECT")).html();
            var bomItemsResolved = parsed.html($translate.instant("BOM_ITEMS_RESOLVED")).html();
            var bomItemsNotResolved = parsed.html($translate.instant("BOM_ITEMS_NOT_RESOLVED")).html();
            var and = parsed.html($translate.instant("AND")).html();
            var effectiveToValidation = parsed.html($translate.instant("EFFECTIVE_TO_VALIDATION")).html();
            var effectiveFromDateValidation = parsed.html($translate.instant("EFFECTIVE_FROM_DATE_VALIDATION")).html();
            var effectiveToDateValidation = parsed.html($translate.instant("EFFECTIVE_TO_DATE_VALIDATION")).html();
            var bomItemEffectiveToValidation = parsed.html($translate.instant("BOMITEM_EFFECTIVE_TO_VALIDATION")).html();
            var effectiveFromAfterCreatedDate = parsed.html($translate.instant("EFFECTIVE_FROM_AFTER_CREATION")).html();
            var effectiveToAfterCreatedDate = parsed.html($translate.instant("EFFECTIVE_TO_AFTER_CREATION")).html();
            $scope.effectiveFromPlaceholder = parsed.html($translate.instant("EFFECTIVE_FROM_PLACEHOLDER")).html();
            $scope.effectiveToPlaceholder = parsed.html($translate.instant("EFFECTIVE_TO_PLACEHOLDER")).html();
            $scope.removeTitle = parsed.html($translate.instant("REMOVE")).html();
            var selectRollupAttributesTitle = parsed.html($translate.instant("SELECT_ROLLUP_ATTRIBUTES")).html();
            var configurationEditorTitle = parsed.html($translate.instant("CONFIGURATION_EDITOR")).html();
            var noConfigurableAttributeValidation = parsed.html($translate.instant("NO_CONFIGURABLE_ATTRIBUTE_VALIDATION")).html();
            var importFileWithProperData = parsed.html($translate.instant("IMPORT_FILE_WITH_PROPER_DATA")).html();
            var importFileWithProperFormatMessage = parsed.html($translate.instant("PLEASE_IMPORT_PROPER_FILE_FOR_REF_SEE_HELP")).html();
            $scope.hasAlternateParts = parsed.html($translate.instant("HAS_ALTERNATE_PARTS")).html();
            $scope.hasSubstituteParts = parsed.html($translate.instant("HAS_SUBSTITUTE_PARTS")).html();

            var emptyBomItem = {
                id: null,
                item: null,
                parent: null,
                newQuantity: 1,
                quantity: null,
                refdes: null,
                notes: null,
                level: 0,
                expanded: false,
                editMode: true,
                isNew: true,
                itemNumber: null,
                editItemNumber: true,
                bomChildren: [],
                disableEdit: false,
                itemRevision: null,
                effectiveFrom: null,
                effectiveTo: null
            };

            vm.bomAttribute = [];
            vm.bomAttributeData = {
                id: {
                    objectId: null,
                    attributeDef: null
                },
                stringValue: null,
                integerValue: null,
                doubleValue: null,
                booleanValue: null,
                dateValue: null,
                listValue: null,
                newListValue: null,
                listValueEditMode: false,
                timeValue: null,
                timestampValue: null,
                refValue: null,
                ref: null,
                imageValue: null,
                attachmentValues: [],
                currencyValue: null,
                currencyType: null,
                imagePath: null,
                mlistValue: [],
                longTextValue: null,
                richTextValue: null,
                hyperLinkValue: null,
                imageFile: null
            };

            vm.copyBomAttributeData = {
                id: {
                    objectId: null,
                    attributeDef: null
                },
                stringValue: null,
                integerValue: null,
                doubleValue: null,
                booleanValue: null,
                dateValue: null,
                listValue: null,
                newListValue: null,
                listValueEditMode: false,
                timeValue: null,
                timestampValue: null,
                refValue: null,
                ref: null,
                imageValue: null,
                attachmentValues: [],
                currencyValue: null,
                currencyType: null,
                imagePath: null,
                mlistValue: [],
                longTextValue: null,
                richTextValue: null,
                hyperLinkValue: null,
                imageFile: null
            };

            /*----------------- To load item Details  --------------------*/
            vm.itemRevision = null;
            vm.item = null;
            vm.bomRulesParentItemWithAttributes = null;
            var bomRulesJson = new Map();
            var itemToItemRulesJson = new Map();

            function loadItem() {
                vm.loading = true;
                ItemService.getRevisionId(vm.itemId).then(
                    function (data) {
                        vm.itemRevision = data;
                        vm.lifeCycleStatus = data.lifeCyclePhase.phaseType;
                        ItemService.getItem(vm.itemRevision.itemMaster).then(
                            function (data) {
                                vm.itemRevision.itemMasterObject = data;
                                vm.item = data;
                            }
                        );
                        loadBom();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            /**
             * Item Load For Bom Rules
             * */

            $rootScope.loadItemForBomRules = loadItemForBomRules;
            vm.bomRuleItem = null;
            vm.bomRuleItemRevision = null;
            function loadItemForBomRules() {
                ItemService.getRevisionId(vm.itemId).then(
                    function (data) {
                        vm.bomRuleItemRevision = data;
                        bomRulesJson = new Map(JSON.parse(vm.bomRuleItemRevision.inclusionRules));
                        itemToItemRulesJson = new Map(JSON.parse(vm.bomRuleItemRevision.itemExclusionRules));
                        vm.bomItemsExlusionsJson = new Map(JSON.parse(vm.bomRuleItemRevision.itemExclusions));
                        ItemService.getItem(vm.bomRuleItemRevision.itemMaster).then(
                            function (data) {
                                vm.bomRuleItem = data;
                                if (vm.bomRuleItem.configurable == true) {
                                    vm.bomRulesParentItemWithAttributes = data;
                                    loadParentAttributes(vm.bomRulesParentItemWithAttributes);
                                }

                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        );
                        loadBomForBomRules();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.bomItemsForExclusion = [];
            vm.bomRuleItems = [];
            vm.normalBomItems = [];
            function loadBomForBomRules() {
                vm.bomItemsForExclusion = [];
                vm.normalBomItems = [];
                ItemBomService.getItemBom(vm.itemId, false).then(
                    function (data) {
                        vm.bomRuleItems = [];
                        angular.forEach(data, function (item) {
                            if (item.item.configurable == true) {
                                vm.bomItemsForExclusion.push(item);
                            } else {
                                vm.normalBomItems.push(item);
                            }
                        });
                        vm.bomRuleItems = data;
                        $scope.$evalAsync();
                        angular.forEach(vm.bomItemsForExclusion, function (item) {
                            loadChildrenAttributes(item.item);
                        });

                        configurationEditorTree = $('#configurationEditor').tree({
                            data: [
                                {
                                    id: nodeId,
                                    text: "<span class='bold-item'>{0}</span>".format(configurationEditorTitle),
                                    iconCls: 'tree-node-icon-bom-configeditor',
                                    attributes: {
                                        typeObject: null,
                                        nodeType: 'ROOT'
                                    },
                                    children: []
                                }
                            ],
                            onContextMenu: onContextMenu,
                            onSelect: onSelectType
                        });

                        rootNode = configurationEditorTree.tree('find', 0);

                        $(document).click(function () {
                            $("#contextMenu").hide();
                        });

                        var attributesNode = {
                            id: ++nodeId,
                            text: "Configurable Attributes",
                            iconCls: 'tree-node-icon-configatts',
                            root: true,
                            nodeType: 'ATTRIBUTES',
                            children: []
                        };

                        configurationEditorTree.tree('append', {
                            parent: rootNode.target,
                            data: attributesNode
                        });

                        var attributeConfigExclusionRoot = {
                            id: ++nodeId,
                            text: "Attribute Exclusion Rules",
                            iconCls: 'tree-node-icon-bomexcl',
                            root: true,
                            nodeType: 'EXCLUSION_RULES',
                            children: []
                        };

                        configurationEditorTree.tree('append', {
                            parent: rootNode.target,
                            data: attributeConfigExclusionRoot
                        });

                        if (vm.itemRevision.hasBom && vm.bomItemsForExclusion.length > 0) {
                            var bomInclusionRulesRoot = {
                                id: ++nodeId,
                                text: "Configurable Item Rules",
                                iconCls: 'tree-node-icon-bomrules',
                                root: true,
                                nodeType: 'INCLUSION_RULES',
                                children: []
                            };

                            configurationEditorTree.tree('append', {
                                parent: rootNode.target,
                                data: bomInclusionRulesRoot
                            });
                        }

                        if (vm.normalBomItems.length > 0) {
                            var bomItemsRulesRoot = {
                                id: ++nodeId,
                                text: "Non-Configurable Item Rules",
                                iconCls: 'tree-node-icon-bomrules',
                                root: true,
                                nodeType: 'NON_CONFIGURABLE_RULES',
                                children: []
                            };

                            configurationEditorTree.tree('append', {
                                parent: rootNode.target,
                                data: bomItemsRulesRoot
                            });
                        }

                        vm.bomConfigItemInclusions = [];
                        vm.bomNonConfigItemInclusions = [];
                        vm.bomItemAttributesExclusions = [];
                        showBomConfigurationEditor();
                        if (vm.itemRevision.hasBom) {
                            loadItemBomConfigurations();
                        } else {
                            $rootScope.hideBusyIndicator();
                            updateItemConfigurableAttributes();
                        }

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            /**
             * Item to Item Inclusion Rules
             * */
            $rootScope.itemChildrenHeader = {name: null, length: null};
            $rootScope.itemtoItemchildrenHeaders = [];
            $rootScope.itemobj = {name: null, values: []};
            $rootScope.showItemExclusions = showItemExclusions;
            $rootScope.itemnameHeaders = [];
            $rootScope.itemkeyvalObj = {itemId: null, itemName: null, key: null, value: null};
            $rootScope.itemvalues = [];
            $rootScope.itemfinalValuesForChildren = [];
            $rootScope.itemchildrenAttributes = [];
            function showItemExclusions() {
                $rootScope.showBusyIndicator();
                $rootScope.itemnameHeaders = [];
                $rootScope.itemChildrenHeader = {name: null, length: null};
                $rootScope.itemtoItemchildrenHeaders = [];
                $rootScope.itemobj = {name: null, values: []};
                $rootScope.itemvalues = [];
                $rootScope.itemfinalValuesForChildren = [];
                $rootScope.itemchildrenAttributes = [];
                $rootScope.itemkeyvalObj = {itemId: null, itemName: null, key: null, value: null};
                if (vm.bomItemsForExclusion.length > 0) {
                    angular.forEach(vm.bomItemsForExclusion, function (allitems) {
                        $rootScope.itemChildrenHeader = {name: null, length: null};
                        $rootScope.itemChildrenHeader.name = allitems.item.itemName;
                        var valuesLen = 0;
                        angular.forEach(allitems.item.exclusionChildrenAttributes, function (map) {
                            valuesLen = 0;
                            angular.forEach(map, function (values, key) {
                                var valuesForMap = values.lov.values;
                                valuesLen = valuesLen + valuesForMap.length;
                                var key1 = key;
                                $rootScope.itemobj = {name: null, values: []};
                                $rootScope.itemobj.name = key;
                                $rootScope.itemobj.values = valuesForMap;
                                $rootScope.itemchildrenAttributes.push($rootScope.itemobj);

                                var header = {name: key, numValues: valuesForMap.length};
                                $rootScope.itemnameHeaders.push(header);
                                angular.forEach(valuesForMap, function (val) {
                                    $rootScope.itemkeyvalObj = {itemId: null, itemName: null, key: null, value: null};
                                    $rootScope.itemkeyvalObj.itemId = allitems.item.id;
                                    $rootScope.itemkeyvalObj.itemName = allitems.item.itemName;
                                    $rootScope.itemkeyvalObj.key = key1;
                                    $rootScope.itemkeyvalObj.value = val;
                                    $rootScope.itemfinalValuesForChildren.push($rootScope.itemkeyvalObj);
                                    $rootScope.itemvalues.push(val);
                                })

                            });

                        });
                        $rootScope.itemChildrenHeader.length = valuesLen;
                        $rootScope.itemtoItemchildrenHeaders.push($rootScope.itemChildrenHeader);
                        $timeout(function () {
                            $('#myModalForItemExclusions').modal('show');
                            $rootScope.closebomIncl = false;
                            $rootScope.closeItemExcl = true;
                            $rootScope.hideBusyIndicator();
                        }, 1000)
                    });
                }
            }

            /**
             * RowHeaders adding item to item exclusions
             *
             * */

            $rootScope.shouldAddRowHeaderForItem = shouldAddRowHeaderForItem;
            function shouldAddRowHeaderForItem(index) {
                if (index == 0) {
                    return [$rootScope.itemchildrenAttributes[0].name, $rootScope.itemchildrenAttributes[0].values.length];
                }
                var temp = 0;
                for (var i = 0; i < $rootScope.itemchildrenAttributes.length; i++) {
                    var values = $rootScope.itemchildrenAttributes[i].values;

                    temp = temp + values.length;
                    if (index == temp) {
                        return [$rootScope.itemchildrenAttributes[i + 1].name, $rootScope.itemchildrenAttributes[i + 1].values.length];
                    }

                }

                return ["", 0];
            }

            /**
             * Row sub headers adding item to item exclusions
             *
             * */
            $rootScope.shouldAddRowHeaderForItemSubHeader = shouldAddRowHeaderForItemSubHeader;
            function shouldAddRowHeaderForItemSubHeader(index) {
                if (index == 0) {
                    return [$rootScope.itemtoItemchildrenHeaders[0].name, $rootScope.itemtoItemchildrenHeaders[0].length];
                }
                var temp = 0;
                for (var i = 0; i < $rootScope.itemtoItemchildrenHeaders.length; i++) {
                    var values = $rootScope.itemtoItemchildrenHeaders[i].length;

                    temp = temp + values;
                    if (index == temp) {
                        return [$rootScope.itemtoItemchildrenHeaders[i + 1].name, $rootScope.itemtoItemchildrenHeaders[i + 1].length];
                    }

                }

                return ["", 0];
            }

            $rootScope.checkItemToItemExclusion = checkItemToItemExclusion;
            $rootScope.createItemToItemExclusionObj = createItemToItemExclusionObj;
            /**
             * Create Item To Item Exclusions Objects
             * */


            $rootScope.exclSaveMessage = null;
            $rootScope.inclSaveMessage = null;
            $rootScope.exclmsg = false;
            $rootScope.inclmsg = false;

            function createItemToItemExclusionObj(horizant, vertical) {
                vm.historyObj = {
                    combination: null,
                    added: false,
                    id: null,
                    inclusion: false,
                    exclusion: false
                }
                $rootScope.showBusyIndicator();
                var esclAddedMsg = parsed.html($translate.instant("EXCL_ADDED_MESSAGE")).html();
                var esclRmvdMsg = parsed.html($translate.instant("EXCL_REMOVED_MESSAGE")).html();
                $rootScope.exclmsg = false;
                $rootScope.exclSaveMessage = null;
                if (itemToItemRulesJson != null) {
                    if (itemToItemRulesJson.size > 0) {

                        if (itemToItemRulesJson.has(horizant.value + "_" + vertical.value + "_" + horizant.itemName + vertical.itemName)) {
                            itemToItemRulesJson.delete(horizant.value + "_" + vertical.value + "_" + horizant.itemName + vertical.itemName);
                            $rootScope.exclSaveMessage = "[" + horizant.value + " - " + vertical.value + "] " + esclRmvdMsg;
                            $rootScope.exclmsg = true;
                            vm.historyObj.combination = "[" + horizant.value + " - " + vertical.value + "] ";
                            vm.historyObj.exclusion = true;
                            vm.historyObj.added = false;
                        }
                        else if (itemToItemRulesJson.has(vertical.value + "_" + horizant.value + "_" + vertical.itemName + horizant.itemName)) {
                            itemToItemRulesJson.delete(vertical.value + "_" + horizant.value + "_" + vertical.itemName + horizant.itemName);
                            $rootScope.exclmsg = true;
                            $rootScope.exclSaveMessage = "[" + vertical.value + " - " + horizant.value + "] " + esclRmvdMsg;
                            vm.historyObj.combination = "[" + vertical.value + " - " + horizant.value + "] ";
                            vm.historyObj.exclusion = true;
                            vm.historyObj.added = false;
                        }

                        else {
                            itemToItemRulesJson.set(horizant.value + "_" + vertical.value + "_" + horizant.itemName + vertical.itemName, [horizant, vertical]);
                            $rootScope.exclmsg = true;
                            $rootScope.exclSaveMessage = "[" + horizant.value + " - " + vertical.value + "] " + esclAddedMsg;
                            vm.historyObj.combination = "[" + horizant.value + " - " + vertical.value + "] ";
                            vm.historyObj.exclusion = true;
                            vm.historyObj.added = true;
                        }
                    }
                    else {
                        $rootScope.exclmsg = true;
                        itemToItemRulesJson.set(horizant.value + "_" + vertical.value + "_" + horizant.itemName + vertical.itemName, [horizant, vertical]);
                        $rootScope.exclSaveMessage = "[" + horizant.value + " - " + vertical.value + "] " + esclAddedMsg;
                        vm.historyObj.combination = "[" + horizant.value + " - " + vertical.value + "] ";
                        vm.historyObj.exclusion = true;
                        vm.historyObj.added = true;
                    }
                }
                else {
                    $rootScope.exclmsg = true;
                    itemToItemRulesJson = new Map();
                    itemToItemRulesJson.set(horizant.value + "_" + vertical.value + "_" + horizant.itemName + vertical.itemName, [horizant, vertical]);
                    $rootScope.exclSaveMessage = "[" + horizant.value + " - " + vertical.value + "] " + esclAddedMsg;
                    vm.historyObj.combination = "[" + horizant.value + " - " + vertical.value + "] ";
                    vm.historyObj.exclusion = true;
                    vm.historyObj.added = true;
                }
                vm.itemRevision.itemExclusionRules = JSON.stringify(Array.from(itemToItemRulesJson.entries()));

                ItemService.updateBomIncluions(vm.itemRevision).then(
                    function (data) {
                        itemToItemRulesJson = new Map();
                        itemToItemRulesJson = new Map(JSON.parse(data.itemExclusionRules));
                        vm.historyObj.id = vm.itemRevision.itemMaster;
                        $rootScope.hideBusyIndicator();
                        $timeout(function () {
                            $rootScope.exclmsg = false;
                        }, 5000)
                        //$rootScope.showSuccessMessage($rootScope.exclSaveMessage);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )

            }

            /**
             * Check Item To Item Exclusions Already Exist
             *
             * */
            function checkItemToItemExclusion(horizant, vertical) {
                //$rootScope.showBusyIndicator();
                var color = "normal";

                if (itemToItemRulesJson != null) {
                    if (itemToItemRulesJson.size > 0) {
                        if (itemToItemRulesJson.has(horizant.value + "_" + vertical.value + "_" + horizant.itemName + vertical.itemName)) {
                            color = "itemColor";
                        }
                        else if (itemToItemRulesJson.has(vertical.value + "_" + horizant.value + "_" + vertical.itemName + horizant.itemName)) {
                            color = "itemColor";
                        }
                        else {
                            color = "normal";
                        }
                    }
                    else {
                        color = "normal";
                    }
                }
                else {
                    color = "normal";
                }
                //$rootScope.hideBusyIndicator();

                return color;
            }

            /**
             * Select all BomIclusion Rules
             * */
            $rootScope.inclusionAll = inclusionAll;
            $rootScope.resetAllInclusions = resetAllInclusions;
            function inclusionAll() {
                $rootScope.showBusyIndicator();
                var i = 0;
                bomRulesJson = new Map();
                angular.forEach($rootScope.finalValuesForParent, function (parent) {
                    i++;
                    angular.forEach($rootScope.finalValuesForChildren, function (child) {
                        if (bomRulesJson.has(parent.value + "_" + child.value + "_" + parent.itemName + child.itemName)) {
                            bomRulesJson.delete(parent.value + "_" + child.value + "_" + parent.itemName + child.itemName);
                        }
                        else {
                            bomRulesJson.set(parent.value + "_" + child.value + "_" + parent.itemName + child.itemName, [parent, child]);
                        }
                    })
                });
                if ($rootScope.finalValuesForParent.length == i) {
                    vm.itemRevision.inclusionRules = JSON.stringify(Array.from(bomRulesJson.entries()));
                    ItemService.updateBomIncluions(vm.itemRevision).then(
                        function (data) {
                            bomRulesJson = new Map();
                            bomRulesJson = new Map(JSON.parse(data.inclusionRules));
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            /**
             * Reset all BomIclusion Rules
             * */

            function resetAllInclusions() {
                $rootScope.showBusyIndicator();
                var i = 0;
                bomRulesJson = new Map();
                angular.forEach($rootScope.finalValuesForParent, function (parent) {
                    i++;
                    angular.forEach($rootScope.finalValuesForChildren, function (child) {
                        var combination = parent.itemName + ' ' + parent.key + '(' + parent.value + ') - ' + child.itemName + ' ' + child.key + '(' + child.value + ')';
                        if ($rootScope.bomConfigCombinations.indexOf(combination) == -1) {
                            if (bomRulesJson.has(parent.value + "_" + child.value + "_" + parent.itemName + child.itemName)) {
                                bomRulesJson.delete(parent.value + "_" + child.value + "_" + parent.itemName + child.itemName);
                            }
                        } else {
                            bomRulesJson.set(parent.value + "_" + child.value + "_" + parent.itemName + child.itemName, [parent, child]);
                        }
                    })

                });
                if ($rootScope.finalValuesForParent.length == i) {
                    vm.itemRevision.inclusionRules = JSON.stringify(Array.from(bomRulesJson.entries()));
                    ItemService.updateBomIncluions(vm.itemRevision).then(
                        function (data) {
                            bomRulesJson = new Map();
                            bomRulesJson = new Map(JSON.parse(data.inclusionRules));
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            $rootScope.checkBomInclusion = checkBomInclusion;
            $rootScope.createBomInlcusionExclusionObj = createBomInlcusionExclusionObj;
            /**
             * Create Bom Inclusion Rules json objects
             * */
            vm.bomItemsExlusionsJson = null;
            vm.historyObj = {
                combination: null,
                added: false,
                id: null,
                inclusion: false,
                exclusion: false
            }

            function createBomInlcusionExclusionObj(horizant, vertical) {
                if (vertical.configurable) {
                    if (vertical.itemName != null) {
                        vm.historyObj = {
                            combination: null,
                            added: false,
                            id: null
                        }
                        $rootScope.showBusyIndicator();
                        $rootScope.inclSaveMessage = "";
                        $rootScope.errorSaveMessage = "";
                        var combination = horizant.itemName + ' ' + horizant.key + '(' + horizant.value + ') - ' + vertical.itemName + ' ' + vertical.key + '(' + vertical.value + ')';
                        vm.historyObj.combination = combination;
                        if ($rootScope.bomConfigCombinations.indexOf(combination) == -1) {
                            var inclAddedMsg = parsed.html($translate.instant("INCL_ADDED_MESSAGE")).html();
                            var inclRmvdMsg = parsed.html($translate.instant("INCL_REMOVED_MESSAGE")).html();

                            if (bomRulesJson != null) {
                                if (bomRulesJson.size > 0) {

                                    if (bomRulesJson.has(horizant.value + "_" + vertical.value + "_" + horizant.itemName + vertical.itemName)) {
                                        bomRulesJson.delete(horizant.value + "_" + vertical.value + "_" + horizant.itemName + vertical.itemName);
                                        vm.historyObj.added = false;
                                        //$rootScope.inclmsg = true;
                                        $rootScope.inclSaveMessage = "[" + horizant.value + " - " + vertical.value + "] " + inclRmvdMsg;

                                    }

                                    else {
                                        vm.historyObj.added = true;
                                        //$rootScope.inclmsg = true;
                                        bomRulesJson.set(horizant.value + "_" + vertical.value + "_" + horizant.itemName + vertical.itemName, [horizant, vertical]);
                                        $rootScope.inclSaveMessage = "[" + horizant.value + " - " + vertical.value + "] " + inclAddedMsg;

                                    }
                                }
                                else {
                                    vm.historyObj.added = true;
                                    //$rootScope.inclmsg = true;
                                    bomRulesJson.set(horizant.value + "_" + vertical.value + "_" + horizant.itemName + vertical.itemName, [horizant, vertical]);
                                    $rootScope.inclSaveMessage = "[" + horizant.value + " - " + vertical.value + "] " + inclAddedMsg;

                                }
                            }
                            else {
                                vm.historyObj.added = true;
                                bomRulesJson = new Map();
                                bomRulesJson.set(horizant.value + "_" + vertical.value + "_" + horizant.itemName + vertical.itemName, [horizant, vertical]);
                                $rootScope.inclSaveMessage = "[" + horizant.value + " - " + vertical.value + "] " + inclAddedMsg;
                            }
                            vm.itemRevision.inclusionRules = JSON.stringify(Array.from(bomRulesJson.entries()));
                            vm.bomConfigItemInclusions.push(vm.historyObj);
                            ItemService.updateBomIncluions(vm.itemRevision).then(
                                function (data) {
                                    bomRulesJson = new Map();
                                    bomRulesJson = new Map(JSON.parse(data.inclusionRules));
                                    vm.historyObj.id = vm.itemRevision.itemMaster;
                                    $rootScope.hideBusyIndicator();
                                    $timeout(function () {
                                        $rootScope.inclSaveMessage = "";
                                    }, 5000)
                                    //$rootScope.inclmsg = false;
                                    //  $rootScope.showSuccessMessage($rootScope.inclSaveMessage);
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        } else {
                            $rootScope.errorSaveMessage = "This Combination already in use we can not remove!";
                            $rootScope.hideBusyIndicator();
                            $timeout(function () {
                                $rootScope.errorSaveMessage = "";
                            }, 5000)
                        }
                    }
                } else {
                    vm.historyObj = {
                        combination: null,
                        added: false,
                        id: null
                    }
                    $rootScope.showBusyIndicator();
                    $rootScope.inclSaveMessage = "";
                    $rootScope.errorSaveMessage = "";

                    vertical.value = !vertical.value;

                    var combination = horizant.itemName + ' ' + horizant.key + '(' + horizant.value + ') - ' + vertical.itemName;
                    vm.historyObj.combination = combination;
                    vm.historyObj.inclusion = true;
                    if ($rootScope.bomConfigCombinations.indexOf(combination) == -1) {
                        var inclAddedMsg = parsed.html($translate.instant("INCL_ADDED_MESSAGE")).html();
                        var inclRmvdMsg = parsed.html($translate.instant("INCL_REMOVED_MESSAGE")).html();

                        if (vm.bomItemsExlusionsJson != null) {
                            if (vm.bomItemsExlusionsJson.size > 0) {
                                if (vm.bomItemsExlusionsJson.has(horizant.key + " " + horizant.value + " " + horizant.itemName + "_" + vertical.itemName)) {
                                    vm.bomItemsExlusionsJson.delete(horizant.key + " " + horizant.value + " " + horizant.itemName + "_" + vertical.itemName);
                                    vm.historyObj.added = true;
                                    $rootScope.inclSaveMessage = "[" + horizant.key + " " + horizant.value + " " + horizant.itemName + "_" + vertical.itemName + "] " + inclAddedMsg;

                                } else {
                                    vm.historyObj.added = false;
                                    if (vertical.value) {
                                        vertical.value = false;
                                    }
                                    vm.bomItemsExlusionsJson.set(horizant.key + " " + horizant.value + " " + horizant.itemName + "_" + vertical.itemName, [horizant, vertical]);
                                    $rootScope.inclSaveMessage = "[" + horizant.key + " " + horizant.value + " " + horizant.itemName + "_" + vertical.itemName + "] " + inclRmvdMsg;

                                }
                            }
                            else {
                                vm.historyObj.added = false;
                                if (vertical.value) {
                                    vertical.value = false;
                                }
                                vm.bomItemsExlusionsJson.set(horizant.key + " " + horizant.value + " " + horizant.itemName + "_" + vertical.itemName, [horizant, vertical]);
                                $rootScope.inclSaveMessage = "[" + horizant.key + " " + horizant.value + " " + horizant.itemName + "_" + vertical.itemName + "] " + inclRmvdMsg;

                            }
                        }
                        else {
                            vm.historyObj.added = false;
                            vm.bomItemsExlusionsJson = new Map();
                            if (vertical.value) {
                                vertical.value = false;
                            }
                            vm.bomItemsExlusionsJson.set(horizant.key + " " + horizant.value + " " + horizant.itemName + "_" + vertical.itemName, [horizant, vertical]);
                            $rootScope.inclSaveMessage = "[" + horizant.key + " " + horizant.value + " " + horizant.itemName + "_" + vertical.itemName + "] " + inclRmvdMsg;
                        }
                        vm.itemRevision.itemExclusions = JSON.stringify(Array.from(vm.bomItemsExlusionsJson.entries()));
                        vm.bomNonConfigItemInclusions.push(vm.historyObj);
                        ItemService.updateBomIncluions(vm.itemRevision).then(
                            function (data) {
                                vm.bomItemsExlusionsJson = new Map();
                                vm.bomItemsExlusionsJson = new Map(JSON.parse(data.itemExclusions));
                                vm.historyObj.id = vm.itemRevision.itemMaster;
                                $rootScope.hideBusyIndicator();
                                $timeout(function () {
                                    $rootScope.inclSaveMessage = "";
                                }, 5000)
                                //$rootScope.inclmsg = false;
                                //  $rootScope.showSuccessMessage($rootScope.inclSaveMessage);
                                $scope.$evalAsync();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    } else {
                        $rootScope.errorSaveMessage = "This Combination already in use we can not remove!";
                        $rootScope.hideBusyIndicator();
                        $timeout(function () {
                            $rootScope.errorSaveMessage = "";
                        }, 5000)
                    }
                }
            }

            $scope.checkBomItemIncluded = checkBomItemIncluded;
            function checkBomItemIncluded(horizant, vertical) {
                var inclusion = true;

                if (vm.bomItemsExlusionsJson != null && vm.bomItemsExlusionsJson.size > 0) {

                    if (vm.bomItemsExlusionsJson.has(horizant.key + " " + horizant.value + " " + horizant.itemName + "_" + vertical.itemName)) {
                        var itemsKey = horizant.key + " " + horizant.value + " " + horizant.itemName + "_" + vertical.itemName;
                        angular.forEach(vm.bomItemsExlusionsJson, function (value, key) {
                            if (key == itemsKey) {
                                angular.forEach(value, function (itemValue) {
                                    if (itemValue.itemId == vertical.itemId) {
                                        inclusion = itemValue.value;
                                    }
                                })
                            }
                        })
                    }
                }

                return inclusion;
            }

            $rootScope.checkBomItemIncludedCombination = checkBomItemIncludedCombination;
            function checkBomItemIncludedCombination(horizant, vertical) {
                var color = "normal";

                if (vm.bomItemsExlusionsJson != null) {
                    if (vm.bomItemsExlusionsJson.size > 0) {
                        if (vm.bomItemsExlusionsJson.has(horizant.key + " " + horizant.value + " " + horizant.itemName + "_" + vertical.itemName)) {
                            color = "colorExclude";

                        }
                        else if (vm.bomItemsExlusionsJson.has(horizant.key + " " + horizant.value + " " + horizant.itemName + "_" + vertical.itemName)) {
                            color = "colorExclude";
                        }
                        else {
                            color = "colorAdding";
                        }
                    }
                    else {
                        color = "colorAdding";
                    }
                }
                else {
                    color = "colorAdding";
                }

                return color;
            }

            $rootScope.checkBomAttributeInclusion = checkBomAttributeInclusion;
            function checkBomAttributeInclusion(horizant, vertical) {
                var color = "normal";

                var combination = horizant.key + "(" + vertical.value + "-" + horizant.value + ")";
                if ($rootScope.itemAttributesExclusions.indexOf(combination) == -1) {
                    color = "normal";
                } else {
                    color = "colorAdding";
                }
                return color;
            }

            /**
             * Check Bom Rules already Exist
             * */
            function checkBomInclusion(horizant, vertical) {
                //$rootScope.showBusyIndicator();
                var color = "normal";

                if (vertical.configurable) {
                    if (bomRulesJson != null) {
                        if (bomRulesJson.size > 0) {
                            if (bomRulesJson.has(horizant.value + "_" + vertical.value + "_" + horizant.itemName + vertical.itemName)) {
                                color = "colorAdding";
                            }
                            else {
                                color = "normal";
                            }
                        }
                        else {
                            color = "normal";
                        }
                    }
                    else {
                        color = "normal";
                    }
                }

                return color;
            }

            /**
             * Load Parent Object Attributes
             * */
            vm.loadParentAttributes = loadParentAttributes;
            vm.loadChildrenAttributes = loadChildrenAttributes;
            vm.obj = {name: null, values: []};
            vm.parentAttributesForExclusion = [];
            //vm.parentAttributes = [];
            function loadParentAttributes(item) {
                item.exclusionAttributes = [];
                ItemService.getConfigurableAttributes(item.latestRevision).then(
                    function (data) {
                        vm.parentAttributesForExclusion = data;
                        item.exclusionAttributes.push(data);
                    }, (function (error) {
                        $rootScope.showWarningMessage(parsed.html(error.message).html());
                        $rootScope.hideBusyIndicator();
                    })
                );
            }

            /**
             * Show Bom Rules
             * */
            $rootScope.showRules = showRules;
            $rootScope.obj = {name: null, values: []};
            $rootScope.childrenAttributes = [];
            $rootScope.nameHeaders = [];
            $rootScope.keyvalObj = {itemId: null, itemName: null, key: null, value: null};
            $rootScope.keyvalObjParent = {itemId: null, itemName: null, key: null, value: null};
            $rootScope.finalValuesForChildren = [];
            $rootScope.values = [];
            $rootScope.childrenItemHeader = {name: null, length: null};
            $rootScope.childrenItemHeaders = [];
            $rootScope.finalValuesForParent = [];
            $rootScope.parentAttributes = [];
            $rootScope.parentobj = {name: null, values: []};
            $rootScope.parentItemObj = null;
            $rootScope.bomConfigCombinations = [];
            $rootScope.closebomIncl = false;
            $rootScope.closeItemExcl = false;
            $rootScope.closebomComp = false;

            function showRules() {
                $rootScope.showBusyIndicator();
                ItemBomService.getBomConfigurationInclusions(vm.itemRevision.id).then(
                    function (data) {
                        $rootScope.bomConfigCombinations = data;

                        $rootScope.finalValuesForParent = [];
                        $rootScope.childrenItemHeaders = [];
                        $rootScope.obj = {name: null, values: []};
                        $rootScope.childrenAttributes = [];
                        $rootScope.nameHeaders = [];
                        $rootScope.keyvalObj = {itemId: null, itemName: null, key: null, value: null};
                        $rootScope.keyvalObjParent = {itemId: null, itemName: null, key: null, value: null};
                        $rootScope.finalValuesForChildren = [];
                        $rootScope.values = [];
                        $rootScope.parentAttributes = [];
                        $rootScope.parentobj = {name: null, values: []};
                        $rootScope.parentItemObj = null;
                        if (vm.bomRulesParentItemWithAttributes != null) {
                            $rootScope.parentItemObj = vm.bomRulesParentItemWithAttributes;

                            angular.forEach(vm.bomRulesParentItemWithAttributes.exclusionAttributes, function (map1) {
                                angular.forEach(map1, function (parentValues, parentKey) {
                                    var valuesForMap = parentValues.values;
                                    var key1 = parentKey;
                                    $rootScope.parentObj = {name: null, values: []};
                                    $rootScope.parentObj.name = key1;
                                    $rootScope.parentObj.values = valuesForMap;
                                    $rootScope.parentAttributes.push($rootScope.parentObj);
                                    angular.forEach(valuesForMap, function (val) {
                                        $rootScope.keyvalObjParent = {
                                            itemId: null,
                                            itemName: null,
                                            key: null,
                                            value: null
                                        };
                                        $rootScope.keyvalObjParent.itemId = $rootScope.parentItemObj.latestRevision;
                                        $rootScope.keyvalObjParent.itemName = $rootScope.parentItemObj.itemName;
                                        $rootScope.keyvalObjParent.key = key1;
                                        $rootScope.keyvalObjParent.value = val;
                                        $rootScope.finalValuesForParent.push($rootScope.keyvalObjParent);
                                    })
                                });
                            });
                        }
                        if (vm.bomItemsForExclusion.length > 0) {
                            angular.forEach(vm.bomItemsForExclusion, function (allitems) {
                                $rootScope.childrenItemHeader = {name: null, length: null};
                                $rootScope.childrenItemHeader.name = allitems.item.itemName;
                                var valuesLen = 0;
                                angular.forEach(allitems.item.exclusionChildrenAttributes, function (map) {
                                    valuesLen = 0;
                                    angular.forEach(map, function (values, key) {
                                        var valuesForMapChildren = values.values;
                                        valuesLen = valuesLen + valuesForMapChildren.length;
                                        var key1 = key;
                                        $rootScope.obj = {name: null, values: []};
                                        $rootScope.obj.name = key;
                                        $rootScope.obj.values = valuesForMapChildren;
                                        $rootScope.childrenAttributes.push($rootScope.obj);

                                        var header = {name: key, numValues: valuesForMapChildren.length};
                                        $rootScope.nameHeaders.push(header);
                                        angular.forEach(valuesForMapChildren, function (val) {
                                            $rootScope.keyvalObj = {
                                                itemId: null,
                                                itemName: null,
                                                key: null,
                                                value: null,
                                                configurable: true
                                            };
                                            $rootScope.keyvalObj.itemId = allitems.item.id;
                                            $rootScope.keyvalObj.itemName = allitems.item.itemName;
                                            $rootScope.keyvalObj.key = key1;
                                            $rootScope.keyvalObj.value = val;
                                            $rootScope.finalValuesForChildren.push($rootScope.keyvalObj);
                                            $rootScope.values.push(val);
                                        })

                                    });

                                });

                                if (allitems.item.exclusionChildrenAttributes.length == 0) {
                                    var header = {name: null, numValues: 0};
                                    $rootScope.nameHeaders.push(header);
                                    $rootScope.keyvalObj = {
                                        itemId: null,
                                        itemName: null,
                                        key: null,
                                        value: null,
                                        configurable: true
                                    };
                                    $rootScope.finalValuesForChildren.push($rootScope.keyvalObj);
                                }
                                $rootScope.childrenItemHeader.length = valuesLen;
                                $rootScope.childrenItemHeaders.push($rootScope.childrenItemHeader);
                            });
                        }
                        if (vm.bomRulesParentItemWithAttributes != null && vm.bomItemsForExclusion.length > 0) {
                            $rootScope.closebomIncl = true;
                            $rootScope.closeItemExcl = false;
                            //$('#myModalForBomRules').modal('show');
                            $rootScope.hideBusyIndicator();
                        }
                        else {
                            //$rootScope.showWarningMessage(thereIsNoConfigurableItems);
                            $rootScope.hideBusyIndicator();
                        }
                        vm.loading = false;
                        $timeout(function () {
                            var bomContentH2 = $('.editor-right').outerHeight();
                            var inclusionButtons = $('#inclusion-buttons').outerHeight();
                            $('#inclusions-content').height(bomContentH2 - (inclusionButtons + 25));
                        }, 100);
                        $scope.$evalAsync();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );

            }


            function showNonConfigurableItemRules() {
                $rootScope.showBusyIndicator();
                ItemBomService.getBomConfigurationInclusions(vm.itemRevision.id).then(
                    function (data) {
                        $rootScope.bomConfigCombinations = data;

                        $rootScope.finalValuesForParent = [];
                        $rootScope.childrenItemHeaders = [];
                        $rootScope.obj = {name: null, values: []};
                        $rootScope.childrenAttributes = [];
                        $rootScope.nameHeaders = [];
                        $rootScope.keyvalObj = {itemId: null, itemName: null, key: null, value: null};
                        $rootScope.keyvalObjParent = {itemId: null, itemName: null, key: null, value: null};
                        $rootScope.finalValuesForChildren = [];
                        $rootScope.values = [];
                        $rootScope.parentAttributes = [];
                        $rootScope.parentobj = {name: null, values: []};
                        $rootScope.parentItemObj = null;
                        if (vm.bomRulesParentItemWithAttributes != null) {
                            $rootScope.parentItemObj = vm.bomRulesParentItemWithAttributes;
                            var valuesLen = 0;
                            angular.forEach(vm.bomRulesParentItemWithAttributes.exclusionAttributes, function (map) {
                                valuesLen = 0;
                                angular.forEach(map, function (values, key) {
                                    var valuesForMapChildren = values.values;
                                    valuesLen = valuesLen + valuesForMapChildren.length;
                                    var key1 = key;
                                    $rootScope.obj = {name: null, values: []};
                                    $rootScope.obj.name = key;
                                    $rootScope.obj.values = valuesForMapChildren;
                                    $rootScope.childrenAttributes.push($rootScope.obj);
                                    $rootScope.parentObj = {name: null, values: []};
                                    $rootScope.parentObj.name = key1;
                                    $rootScope.parentObj.values = valuesForMapChildren;
                                    $rootScope.parentAttributes.push($rootScope.parentObj);
                                    var header = {name: key, numValues: valuesForMapChildren.length};
                                    $rootScope.nameHeaders.push(header);


                                    angular.forEach(valuesForMapChildren, function (val) {
                                        $rootScope.keyvalObj = {
                                            itemId: null,
                                            itemName: null,
                                            key: null,
                                            value: null,
                                            configurable: false
                                        };
                                        $rootScope.keyvalObj.itemId = vm.itemId;
                                        $rootScope.keyvalObj.itemName = $rootScope.parentItemObj.itemName;
                                        $rootScope.keyvalObj.key = key1;
                                        $rootScope.keyvalObj.value = val;
                                        $rootScope.finalValuesForChildren.push($rootScope.keyvalObj);
                                        $rootScope.values.push(val);
                                    })
                                });
                                $rootScope.parentItemObj.length = valuesLen;
                            });
                        }

                        if (vm.normalBomItems.length > 0) {
                            angular.forEach(vm.normalBomItems, function (bomItem) {
                                $rootScope.childrenItemHeader = {name: null, length: null};
                                $rootScope.childrenItemHeader.name = bomItem.item.itemName;

                                $rootScope.keyvalObj = {
                                    itemId: bomItem.item.id,
                                    itemName: bomItem.item.itemName,
                                    key: null,
                                    value: true,
                                    configurable: false
                                };
                                $rootScope.finalValuesForParent.push($rootScope.keyvalObj);

                                $rootScope.childrenItemHeader.length = 0;
                                $rootScope.childrenItemHeaders.push($rootScope.childrenItemHeader);
                            })
                        }
                        $timeout(function () {
                            if (vm.bomRulesParentItemWithAttributes != null && vm.bomItemsForExclusion.length > 0) {
                                $rootScope.closebomIncl = true;
                                $rootScope.closeItemExcl = false;
                                //$('#myModalForBomRules').modal('show');
                                $rootScope.hideBusyIndicator();
                                vm.loading = false
                            }
                            else {
                                //$rootScope.showWarningMessage(thereIsNoConfigurableItems);
                                $rootScope.hideBusyIndicator();
                                vm.loading = false
                            }

                        }, 1000);
                        var bomContentH2 = $('.editor-right').outerHeight();
                        var inclusionButtons = $('#inclusion-buttons').outerHeight();
                        $('#inclusions-content').height(bomContentH2 - (inclusionButtons + 25));
                        $scope.$evalAsync();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );

            }

            /**
             * Load Bom Rules Header
             * */
            $rootScope.shouldAddRowHeader = shouldAddRowHeader;
            function shouldAddRowHeader(index) {
                if (vm.selectConfigEditorNode.nodeType == "INCLUSION_RULES") {
                    if (index == 0) {
                        return [$rootScope.parentAttributes[0].name, $rootScope.parentAttributes[0].values.length];
                    }
                    var temp = 0;
                    for (var i = 0; i < $rootScope.parentAttributes.length; i++) {
                        var values = $rootScope.parentAttributes[i].values;

                        temp = temp + values.length;
                        if (index == temp) {
                            return [$rootScope.parentAttributes[i + 1].name, $rootScope.parentAttributes[i + 1].values.length];
                        }

                    }
                    return ["", 0];
                }
            }

            /**
             *
             * Load Bom childen attributes
             * */
            vm.childrenAttributesForExclusion = [];
            function loadChildrenAttributes(item) {
                item.exclusionChildrenAttributes = [];
                ItemService.getConfigurableAttributes(item.latestRevision).then(
                    function (data) {
                        if (data != null && data != "") {
                            item.exclusionChildrenAttributes.push(data);
                        }
                    }, (function (error) {
                        $rootScope.showWarningMessage(parsed.html(error.message).html());
                        $rootScope.hideBusyIndicator();
                    })
                );
            }

            /*-----------------  To load ItemBom Details  ----------------*/
            vm.bomItemMap = new Hashtable();
            function loadBom() {
                vm.bomItemsForExclusion = [];
                $scope.showExpandAll = false;
                vm.selectedPhase = null;
                var hierarchy = false;
                $rootScope.showBusyIndicator($('.view-container'));
                ItemBomService.getTotalBom(vm.itemId, hierarchy, vm.selectedBomRule).then(
                    function (data) {
                        setBomDetails(data);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            $rootScope.showConfigEditor = false;
            $scope.showExpandAll = false;
            $rootScope.showResolveBomButton = false;
            function setBomDetails(data) {
                vm.oldBomItemsData = null;
                vm.bomItemsCopy = angular.copy(data);
                $scope.limit = 30;
                vm.bomIds = [];
                vm.masters = [];
                angular.forEach(data, function (item) {
                    item.parentBom = null;
                    item.isNew = false;
                    item.editMode = false;
                    item.expanded = false;
                    item.level = 0;
                    item.isRoot = true;
                    if (item.hasBom) {
                        $scope.showExpandAll = true;
                    }
                    item.disableEdit = (item.parentLifecyclePhaseType == 'RELEASED' || item.parentLifecyclePhaseType == 'OBSOLETE');
                    if (item.configurable) {
                        $rootScope.showResolveBomButton = true;
                    }
                    item.bomChildren = [];
                    if (item.hasThumbnail) {
                        item.thumbnailImage = "api/plm/items/" + item.item + "/itemImageAttribute/download?" + new Date().getTime();
                    }
                    vm.masters.push(item.item);
                    vm.bomIds.push(item.id);
                    vm.bomIds.push(item.item);
                    vm.bomItemMap.put(item.id, item);
                });
                vm.bomItems = data;

                $scope.$evalAsync();
                vm.loading = false;
                ItemService.getItemReferences(vm.masters, 'itemMaster');
                angular.forEach(vm.selectedBomAttributes, function (bomAttribute) {
                    vm.selectedBomAttributeIds.push(bomAttribute.id);
                });
                if (vm.bomIds.length > 0 && vm.selectedBomAttributeIds.length > 0) {
                    loadBomAttributeValues();
                }
                if ($rootScope.external.external == true) {
                    var session = JSON.parse(localStorage.getItem('local_storage_login'));
                    $rootScope.loginPersonDetails = session.login;
                    angular.forEach(vm.bomItems, function (bomItem) {
                        ShareService.getSharedObjectByPersonAndItem(bomItem.item, $rootScope.loginPersonDetails.person.id).then(
                            function (sharedObject) {
                                if (sharedObject.length > 0) {
                                    if (sharedObject.length == 1) {
                                        angular.forEach(sharedObject, function (shared) {
                                            bomItem.sharedPermission = shared.permission;
                                        })
                                    }
                                } else {
                                    bomItem.sharedPermission = "READ";
                                }

                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    })
                }
                $timeout(function () {
                    calculateColumnWidthForSticky();
                    $rootScope.showConfigEditor = true;
                    $rootScope.hideBusyIndicator();
                }, 1000);
            }

            function loadBomAttributeValues() {
                ItemService.getAttributesByItemIdAndAttributeId(vm.bomIds, vm.selectedBomAttributeIds).then(
                    function (data) {
                        vm.selectedBomtAttributesValues = data;

                        var map = new Hashtable();
                        angular.forEach(vm.selectedBomAttributes, function (att) {
                            if (att.id != null && att.id != "" && att.id != 0) {
                                map.put(att.id, att);
                            }
                        });

                        angular.forEach(vm.bomItems, function (item) {
                            var attributes = [];
                            var bomAttributes = null;
                            bomAttributes = vm.selectedBomtAttributesValues[item.id];
                            if (bomAttributes != null && bomAttributes != undefined) {
                                attributes = attributes.concat(bomAttributes);
                            }

                            bomItemAttr(attributes, item, map);
                        });
                        angular.forEach(vm.bomItems, function (bom) {
                            var attributes = [];
                            var bomAttributes = null;
                            bomAttributes = vm.selectedBomtAttributesValues[bom.item];
                            if (bomAttributes != null && bomAttributes != undefined) {
                                attributes = attributes.concat(bomAttributes);
                            }
                            bomItemAttr(attributes, bom, map);
                        });
                        angular.forEach(vm.bomItems, function (item) {
                            angular.forEach(vm.selectedBomAttributes, function (object) {
                                var objectName = object.name;
                                if (object.dataType == 'OBJECT') {
                                    if (item[object.name] == undefined) {
                                        item[objectName] = angular.copy(object);
                                    }
                                }
                            })
                        })
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );
            }

            function bomItemAttr(attributes, item, map) {
                angular.forEach(attributes, function (attribute) {
                    var selectatt = map.get(attribute.id.attributeDef);
                    if (selectatt != null) {
                        var attributeName = selectatt.name;
                        if (selectatt.dataType == 'TEXT') {
                            item[attributeName] = attribute;
                        } else if (selectatt.dataType == 'LONGTEXT') {
                            item[attributeName] = attribute;
                        } else if (selectatt.dataType == 'RICHTEXT') {
                            item[attributeName] = attribute;
                            if (attribute.richTextValue != null) {
                                item[attributeName].encodedRichText = $sce.trustAsHtml(attribute.richTextValue);
                            }
                        } else if (selectatt.dataType == 'HYPERLINK') {
                            item[attributeName] = attribute;
                        } else if (selectatt.dataType == 'INTEGER') {
                            item[attributeName] = attribute;
                        } else if (selectatt.dataType == 'BOOLEAN') {
                            item[attributeName] = attribute;
                        } else if (selectatt.dataType == 'DOUBLE') {
                            item[attributeName] = attribute;
                        } else if (selectatt.dataType == 'DATE') {
                            item[attributeName] = attribute;
                        } else if (selectatt.dataType == 'LIST') {
                            item[attributeName] = attribute;
                        } else if (selectatt.dataType == 'TIME') {
                            item[attributeName] = attribute;
                        } else if (selectatt.dataType == 'TIMESTAMP') {
                            item[attributeName] = attribute;
                        } else if (selectatt.dataType == 'CURRENCY') {
                            item[attributeName] = attribute;
                            if (attribute.currencyType != null) {
                                item[attributeName + 'type'] = currencyMap.get(attribute.currencyType);
                            }
                        } else if (selectatt.dataType == 'ATTACHMENT') {
                            var revisionAttachmentIds = [];
                            if (attribute.attachmentValues.length > 0) {
                                angular.forEach(attribute.attachmentValues, function (attachmentId) {
                                    revisionAttachmentIds.push(attachmentId);
                                });
                                AttributeAttachmentService.getMultipleAttributeAttachments(revisionAttachmentIds).then(
                                    function (data) {
                                        vm.revisionAttachments = data;
                                        item[attributeName] = vm.revisionAttachments;
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                    }
                                )
                            }
                        } else if (selectatt.dataType == 'IMAGE') {
                            if (attribute.imageValue != null) {
                                item[attributeName] = attribute;
                                item[attributeName].imagePath = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();
                            }
                        } else if (selectatt.dataType == 'OBJECT') {
                            if (selectatt.refType != null) {
                                if (selectatt.refType == 'ITEM' && attribute.refValue != null) {
                                    ItemService.getItem(attribute.refValue).then(
                                        function (itemValue) {
                                            item[attributeName].refValue = itemValue;
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    )
                                } else if (selectatt.refType == 'ITEMREVISION' && attribute.refValue != null) {
                                    ItemService.getRevisionId(attribute.refValue).then(
                                        function (revisionValue) {
                                            item[attributeName] = revisionValue;
                                            ItemService.getItem(revisionValue.itemMaster).then(
                                                function (data) {
                                                    item[attributeName].refValue = data;
                                                }, function (error) {
                                                    $rootScope.showErrorMessage(error.message);
                                                }
                                            )
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    )
                                } else if (selectatt.refType == 'CHANGE' && attribute.refValue != null) {
                                    ECOService.getChangeObject(attribute.refValue).then(
                                        function (changeValue) {
                                            item[attributeName].refValue = changeValue;
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    )
                                } else if (selectatt.refType == 'WORKFLOW' && attribute.refValue != null) {
                                    WorkflowDefinitionService.getWorkflowDefinition(attribute.refValue).then(
                                        function (workflowValue) {
                                            item[attributeName].refValue = workflowValue;
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    )
                                } else if (selectatt.refType == 'MANUFACTURER' && attribute.refValue != null) {
                                    MfrService.getManufacturer(attribute.refValue).then(
                                        function (mfrValue) {
                                            item[attributeName].refValue = mfrValue;
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    )
                                } else if (selectatt.refType == 'MANUFACTURERPART' && attribute.refValue != null) {
                                    MfrPartsService.getManufacturepart(attribute.refValue).then(
                                        function (mfrPartValue) {
                                            item[attributeName].refValue = mfrPartValue;
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    )
                                } else if (selectatt.refType == 'PERSON' && attribute.refValue != null) {
                                    CommonService.getPerson(attribute.refValue).then(
                                        function (person) {
                                            item[attributeName].refValue = person;
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    )
                                } else if (selectatt.refType == 'REQUIREMENT' && attribute.refValue != null) {
                                    RequirementService.getRequirement(attribute.refValue).then(
                                        function (reqValue) {
                                            item[attributeName].refValue = reqValue;
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    )
                                } else if (selectatt.refType == 'REQUIREMENTDOCUMENT' && attribute.refValue != null) {
                                    ReqDocumentService.getReqDocument(attribute.refValue).then(
                                        function (reqValue) {
                                            item[attributeName].refValue = reqValue;
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    )
                                }
                                else if (selectatt.refType == 'MESOBJECT' && attribute.refValue != null) {
                                    MESObjectTypeService.getMESObject(attribute.refValue).then(
                                        function (reqValue) {
                                            item[attributeName].refValue = reqValue;
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    )
                                } else if (selectatt.refType == 'MROOBJECT' && attribute.refValue != null) {
                                    MESObjectTypeService.getMROObject(attribute.refValue).then(
                                        function (reqValue) {
                                            item[attributeName].refValue = reqValue;
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    )
                                } else if (selectatt.refType == 'CUSTOMOBJECT' && attribute.refValue != null) {
                                    CustomObjectService.getCustomObject(attribute.refValue).then(
                                        function (reqValue) {
                                            item[attributeName].refValue = reqValue;
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    )
                                }
                            }
                        }
                    }
                })
            }

            function calculateColumnWidthForSticky() {
                var addColumnWidth = 20;//$('.add-column').outerWidth();
                var toggelColumnWidth = $('.toggle-column').outerWidth();
                var numberColumnWidth = $('#itemNumber-column').outerWidth();
                //var typeColumnWidth = $('.type-column').outerWidth();
                var nameColumnWidth = $('.item-name-column').outerWidth();

                $('.add-column').css("left", -6 + "px");
                $('.add-column').width(addColumnWidth);

                $('.toggle-column').css("left", (addColumnWidth - 6) + "px");
                $('.toggle-column').width(addColumnWidth + toggelColumnWidth);

                $('.number-column').css("left", ((addColumnWidth + toggelColumnWidth) - 7) + "px");
                $('.number-column').width(addColumnWidth + toggelColumnWidth + numberColumnWidth);

                if (vm.expandedAll) {
                    if (vm.collapseAction) {
                        $('.item-name-column').css("left", ((addColumnWidth + toggelColumnWidth + numberColumnWidth) - 8) + "px");
                        $('.item-name-column').width(addColumnWidth + toggelColumnWidth + numberColumnWidth + nameColumnWidth);
                    } else {
                        $('.item-name-column').css("left", ((addColumnWidth + toggelColumnWidth + numberColumnWidth) + 5) + "px");
                        $('.item-name-column').width(addColumnWidth + toggelColumnWidth + numberColumnWidth + nameColumnWidth);
                    }
                } else {
                    if (vm.collapseAction) {
                        $('.item-name-column').css("left", ((addColumnWidth + toggelColumnWidth + numberColumnWidth) - 8) + "px");
                        $('.item-name-column').width(addColumnWidth + toggelColumnWidth + numberColumnWidth + nameColumnWidth);
                    } else {
                        $('.item-name-column').css("left", ((addColumnWidth + toggelColumnWidth + numberColumnWidth) - 5) + "px");
                        $('.item-name-column').width(addColumnWidth + toggelColumnWidth + numberColumnWidth + nameColumnWidth);
                    }
                }
            }

            function rollupReportSticky() {
                var rollupNumberWidth = $('.r-item-number').outerWidth();
                var rollupTypeWidth = $('.r-item-type').outerWidth();
                var rollupNameWidth = $('.r-item-name').outerWidth();

                $('.r-item-number').css("left", -6 + "px");
                $('.r-item-number').width(rollupNumberWidth);

                $('.r-item-type').css("left", (rollupNumberWidth - 6) + "px");
                $('.r-item-type').width(rollupNumberWidth + rollupTypeWidth);

                $('.r-item-name').css("left", ((rollupNumberWidth + rollupTypeWidth) - 7) + "px");
                $('.r-item-name').width(rollupNumberWidth + rollupTypeWidth + rollupNameWidth);
            }

            function resizeCalculateColumnWidthForSticky() {
                var addColumnWidth = 20;//$('.add-column').outerWidth();
                var toggelColumnWidth = $('.toggle-column').outerWidth();
                var numberColumnWidth = $('#itemNumber-column').outerWidth();
                //var typeColumnWidth = $('.type-column').outerWidth();
                var nameColumnWidth = $('.item-name-column').outerWidth();

                $('.add-column').css("left", -6 + "px");
                $('.add-column').width(addColumnWidth);

                $('.toggle-column').css("left", (addColumnWidth - 6) + "px");
                $('.toggle-column').width(addColumnWidth + toggelColumnWidth);

                $('.number-column').css("left", ((addColumnWidth + toggelColumnWidth) - 7) + "px");
                $('.number-column').width(addColumnWidth + toggelColumnWidth + numberColumnWidth);

                $('.item-name-column').css("left", ((addColumnWidth + toggelColumnWidth + numberColumnWidth) - 8) + "px");
                $('.item-name-column').width(addColumnWidth + toggelColumnWidth + numberColumnWidth + nameColumnWidth);
            }

            $scope.bomSearch = {
                searchQuery: null,
                item: '',
                fromDate: null,
                toDate: null
            }

            function watchDates() {
                $scope.$watch('bomSearch.fromDate', function (newVal, oldVal) {
                    if (angular.equals(newVal, oldVal)) {

                    } else {
                        $scope.bomSearch.fromDate = newVal;
                        searchBomItems($scope.bomSearch.searchQuery);
                    }
                    $scope.$evalAsync();
                }, true);

                $scope.$watch('bomSearch.toDate', function (newVal, oldVal) {
                    if (angular.equals(newVal, oldVal)) {

                    } else {
                        $scope.bomSearch.toDate = newVal;
                        searchBomItems($scope.bomSearch.searchQuery);
                    }
                    $scope.$evalAsync();
                }, true);
            }


            vm.clearFromDate = clearFromDate;
            vm.clearToDate = clearToDate;
            function clearFromDate() {
                $scope.bomSearch.fromDate = null;
                searchBomItems($scope.bomSearch.searchQuery);
            }

            function clearToDate() {
                $scope.bomSearch.toDate = null;
                searchBomItems($scope.bomSearch.searchQuery);
            }

            vm.searchMode = false;
            $rootScope.searchBomItems = searchBomItems;
            function searchBomItems(freeText) {
                vm.loading = true;
                $scope.bomSearch.searchQuery = freeText;
                $scope.limit = 30;
                vm.bomIds = [];
                vm.masters = [];
                vm.phaseCount = 0;
                $rootScope.showBusyIndicator($('.view-content'));
                ItemBomService.getTotalBom(vm.itemId, true, vm.selectedBomRule).then(
                    function (data) {
                        if (vm.oldBomItemsData == null) {
                            vm.oldBomItemsData = angular.copy(vm.bomItems);
                        }
                        vm.bomItemsCopy = angular.copy(data);
                        vm.bomItems = [];
                        if ((freeText != null && freeText != "") || ($scope.bomSearch.fromDate != null || $scope.bomSearch.toDate != null) || (vm.selectedPhase != null)) {
                            var presentBomItemsData = angular.copy(vm.bomItemsCopy);
                            vm.bomItems = [];
                            angular.forEach(presentBomItemsData, function (item) {
                                item.bomChildren = [];
                                item.parentBom = null;
                                if ($scope.bomSearch.fromDate == null && $scope.bomSearch.toDate == null && vm.selectedPhase == null) {
                                    if (item.itemNumber.toLowerCase().includes(freeText.toLowerCase()) || item.itemName.toLowerCase().includes(freeText.toLowerCase()) || item.itemTypeName.toLowerCase().includes(freeText.toLowerCase())) {
                                        item.searchExist = true;
                                    }
                                    if (item.description != null && item.description != "" && item.description.toLowerCase().includes(freeText.toLowerCase())) {
                                        item.searchExist = true;
                                    }
                                    if (item.notes != null && item.notes != "" && item.notes.toLowerCase().includes(freeText.toLowerCase())) {
                                        item.searchExist = true;
                                    }
                                    if (item.refdes != null && item.refdes != "" && item.refdes.toLowerCase().includes(freeText.toLowerCase())) {
                                        item.searchExist = true;
                                    }
                                } else if ((freeText != null && freeText != "") && ($scope.bomSearch.fromDate != null || $scope.bomSearch.toDate != null) && vm.selectedPhase == null) {
                                    item = checkEffectiveDatesWithSearchQuery(item, freeText);
                                } else if ((freeText != null && freeText != "") && ($scope.bomSearch.fromDate != null || $scope.bomSearch.toDate != null) && vm.selectedPhase != null) {
                                    item = checkEffectiveDatesWithSearchQueryAndPhase(item, freeText);
                                } else if ((freeText != null && freeText != "") && vm.selectedPhase != null) {
                                    if ((item.itemNumber.toLowerCase().includes(freeText.toLowerCase()) || item.itemName.toLowerCase().includes(freeText.toLowerCase()) || item.itemTypeName.toLowerCase().includes(freeText.toLowerCase())) && item.lifeCyclePhase != null && item.lifeCyclePhase.phase == vm.selectedPhase) {
                                        item.searchExist = true;
                                    }
                                    if (item.description != null && item.description != "" && item.description.toLowerCase().includes(freeText.toLowerCase()) && item.lifeCyclePhase != null && item.lifeCyclePhase.phase == vm.selectedPhase) {
                                        item.searchExist = true;
                                    }
                                    if (item.notes != null && item.notes != "" && item.notes.toLowerCase().includes(freeText.toLowerCase()) && item.lifeCyclePhase != null && item.lifeCyclePhase.phase == vm.selectedPhase) {
                                        item.searchExist = true;
                                    }
                                    if (item.refdes != null && item.refdes != "" && item.refdes.toLowerCase().includes(freeText.toLowerCase()) && item.lifeCyclePhase != null && item.lifeCyclePhase.phase == vm.selectedPhase) {
                                        item.searchExist = true;
                                    }
                                } else if (($scope.bomSearch.fromDate != null || $scope.bomSearch.toDate != null) && vm.selectedPhase == null) {
                                    item = checkEffectiveDates(item);
                                } else if (($scope.bomSearch.fromDate != null || $scope.bomSearch.toDate != null) && vm.selectedPhase != null) {
                                    item = checkEffectiveDatesWithPhase(item);
                                } else if (vm.selectedPhase != null) {
                                    if (item.lifeCyclePhase != null && item.lifeCyclePhase.phase == vm.selectedPhase) {
                                        item.searchExist = true;
                                    }
                                }
                                item = populateBomItemsSearchChildrenExist(item, freeText);
                            })
                            $timeout(function () {
                                vm.bomItems = [];
                                vm.phaseCount = 0;
                                angular.forEach(presentBomItemsData, function (item) {
                                    if (item.searchExist) {
                                        item.parentBom = null;
                                        item.isNew = false;
                                        item.editMode = false;
                                        item.expanded = true;
                                        item.level = 0;
                                        item.disableEdit = (item.parentLifecyclePhaseType == 'RELEASED' || item.parentLifecyclePhaseType == 'OBSOLETE');
                                        if (item.configurable) {
                                            $rootScope.showResolveBomButton = true;
                                        }
                                        item.bomChildren = [];
                                        if (item.hasThumbnail) {
                                            item.thumbnailImage = "api/plm/items/" + bomItem.item + "/itemImageAttribute/download?" + new Date().getTime();
                                        }
                                        vm.bomItems.push(item);
                                        if (vm.selectedPhase != null && item.lifeCyclePhase != null && item.lifeCyclePhase.phase == vm.selectedPhase) {
                                            vm.phaseCount++;
                                        }
                                        vm.masters.push(item.item);
                                        vm.bomIds.push(item.id);
                                        vm.bomIds.push(item.item);
                                        var index = vm.bomItems.indexOf(item);
                                        index = populateBomItemsSearchChildren(item, index);
                                    }
                                });
                                vm.selectedBomAttributeIds = [];
                                angular.forEach(vm.selectedBomAttributes, function (bomAttribute) {
                                    vm.selectedBomAttributeIds.push(bomAttribute.id);
                                });
                                if (vm.bomIds.length > 0 && vm.selectedBomAttributeIds.length > 0) {
                                    loadBomAttributeValues();
                                }
                                $rootScope.hideBusyIndicator();
                                $timeout(function () {
                                    vm.loading = false;
                                    calculateColumnWidthForSticky();
                                }, 200);
                            }, 1000)
                        } else {
                            vm.searchMode = false;
                            vm.phaseCount = 0;
                            vm.bomItems = angular.copy(vm.oldBomItemsData);
                            $timeout(function () {
                                vm.loading = false;
                                calculateColumnWidthForSticky();
                            }, 200);
                            $rootScope.hideBusyIndicator();
                        }
                    }
                )
            }

            function checkEffectiveDatesWithSearchQuery(item, freeText) {
                var selectedFromDate = null;
                var selectedToDate = null;
                var itemFromDate = null;
                var itemToDate = null;
                if ($scope.bomSearch.fromDate != null && $scope.bomSearch.toDate != null) {
                    selectedFromDate = moment($scope.bomSearch.fromDate, $rootScope.applicationDateSelectFormat);
                    selectedToDate = moment($scope.bomSearch.toDate, $rootScope.applicationDateSelectFormat);
                    if (item.effectiveFrom != null && item.effectiveTo != null) {
                        itemFromDate = moment(item.effectiveFrom, $rootScope.applicationDateSelectFormat);
                        itemToDate = moment(item.effectiveTo, $rootScope.applicationDateSelectFormat);
                        if ((itemFromDate.isAfter(selectedFromDate) || itemFromDate.isSame(selectedFromDate)) && (itemToDate.isBefore(selectedToDate) || itemToDate.isSame(selectedToDate))
                            && (item.itemNumber.toLowerCase().includes(freeText.toLowerCase()) || item.itemName.toLowerCase().includes(freeText.toLowerCase()) || item.itemTypeName.toLowerCase().includes(freeText.toLowerCase()))) {
                            item.searchExist = true;
                        }
                    } else {
                        if (item.effectiveFrom == null && item.effectiveTo == null && (item.itemNumber.toLowerCase().includes(freeText.toLowerCase()) || item.itemName.toLowerCase().includes(freeText.toLowerCase()) || item.itemTypeName.toLowerCase().includes(freeText.toLowerCase()))) {
                            item.searchExist = true;
                        }
                    }
                } else if ($scope.bomSearch.fromDate != null) {
                    selectedFromDate = moment($scope.bomSearch.fromDate, $rootScope.applicationDateSelectFormat);
                    selectedFromDate = new Date(selectedFromDate);
                    selectedFromDate.setDate(selectedFromDate.getDate() - 1);
                    if (item.effectiveFrom == null && item.effectiveTo == null && (item.itemNumber.toLowerCase().includes(freeText.toLowerCase()) || item.itemName.toLowerCase().includes(freeText.toLowerCase()) || item.itemTypeName.toLowerCase().includes(freeText.toLowerCase()))) {
                        item.searchExist = true;
                    } else if (item.effectiveFrom != null && item.effectiveTo == null) {
                        itemFromDate = moment(item.effectiveFrom, $rootScope.applicationDateSelectFormat);
                        if ((itemFromDate.isAfter(selectedFromDate) || itemFromDate.isSame(selectedFromDate)) && (item.itemNumber.toLowerCase().includes(freeText.toLowerCase()) || item.itemName.toLowerCase().includes(freeText.toLowerCase()) || item.itemTypeName.toLowerCase().includes(freeText.toLowerCase()))) {
                            item.searchExist = true;
                        }
                    } else if (item.effectiveFrom == null && item.effectiveTo != null) {
                        itemToDate = moment(item.effectiveTo, $rootScope.applicationDateSelectFormat);
                        if ((itemToDate.isAfter(selectedFromDate) || itemToDate.isSame(selectedFromDate)) && (item.itemNumber.toLowerCase().includes(freeText.toLowerCase()) || item.itemName.toLowerCase().includes(freeText.toLowerCase()) || item.itemTypeName.toLowerCase().includes(freeText.toLowerCase()))) {
                            item.searchExist = true;
                        }
                    } else {
                        itemFromDate = moment(item.effectiveFrom, $rootScope.applicationDateSelectFormat);
                        if ((itemFromDate.isAfter(selectedFromDate) || itemFromDate.isSame(selectedFromDate)) && (item.itemNumber.toLowerCase().includes(freeText.toLowerCase()) || item.itemName.toLowerCase().includes(freeText.toLowerCase()) || item.itemTypeName.toLowerCase().includes(freeText.toLowerCase()))) {
                            item.searchExist = true;
                        }
                    }
                } else if ($scope.bomSearch.toDate != null) {
                    selectedToDate = moment($scope.bomSearch.fromDate, $rootScope.applicationDateSelectFormat);
                    if (item.effectiveFrom == null && item.effectiveTo == null && (item.itemNumber.toLowerCase().includes(freeText.toLowerCase()) || item.itemName.toLowerCase().includes(freeText.toLowerCase()) || item.itemTypeName.toLowerCase().includes(freeText.toLowerCase()))) {
                        item.searchExist = true;
                    } else if (item.effectiveTo != null && item.effectiveFrom == null) {
                        itemToDate = moment(item.effectiveTo, $rootScope.applicationDateSelectFormat);
                        if ((itemToDate.isBefore(selectedToDate) || itemToDate.isSame(selectedToDate)) && (item.itemNumber.toLowerCase().includes(freeText.toLowerCase()) || item.itemName.toLowerCase().includes(freeText.toLowerCase()) || item.itemTypeName.toLowerCase().includes(freeText.toLowerCase()))) {
                            item.searchExist = true;
                        }
                    } else if (item.effectiveTo == null && item.effectiveFrom != null) {
                        itemFromDate = moment(item.effectiveFrom, $rootScope.applicationDateSelectFormat);
                        if ((itemFromDate.isBefore(selectedToDate) || itemFromDate.isSame(selectedToDate)) && (item.itemNumber.toLowerCase().includes(freeText.toLowerCase()) || item.itemName.toLowerCase().includes(freeText.toLowerCase()) || item.itemTypeName.toLowerCase().includes(freeText.toLowerCase()))) {
                            item.searchExist = true;
                        }
                    } else {
                        itemToDate = moment(item.effectiveTo, $rootScope.applicationDateSelectFormat);
                        if ((itemToDate.isBefore(selectedToDate) || itemToDate.isSame(selectedToDate)) && (item.itemNumber.toLowerCase().includes(freeText.toLowerCase()) || item.itemName.toLowerCase().includes(freeText.toLowerCase()) || item.itemTypeName.toLowerCase().includes(freeText.toLowerCase()))) {
                            item.searchExist = true;
                        }
                    }
                }
                return item;
            }

            function checkEffectiveDatesWithSearchQueryAndPhase(item, freeText) {
                var selectedFromDate = null;
                var selectedToDate = null;
                var itemFromDate = null;
                var itemToDate = null;
                if ($scope.bomSearch.fromDate != null && $scope.bomSearch.toDate != null) {
                    selectedFromDate = moment($scope.bomSearch.fromDate, $rootScope.applicationDateSelectFormat);
                    selectedToDate = moment($scope.bomSearch.toDate, $rootScope.applicationDateSelectFormat);
                    if (item.effectiveFrom != null && item.effectiveTo != null) {
                        itemFromDate = moment(item.effectiveFrom, $rootScope.applicationDateSelectFormat);
                        itemToDate = moment(item.effectiveTo, $rootScope.applicationDateSelectFormat);
                        if ((itemFromDate.isAfter(selectedFromDate) || itemFromDate.isSame(selectedFromDate)) && (itemToDate.isBefore(selectedToDate) || itemToDate.isSame(selectedToDate)) && (item.itemNumber.toLowerCase().includes(freeText.toLowerCase()) || item.itemName.toLowerCase().includes(freeText.toLowerCase()) || item.itemTypeName.toLowerCase().includes(freeText.toLowerCase())) && item.lifeCyclePhase != null && item.lifeCyclePhase.phase == vm.selectedPhase) {
                            item.searchExist = true;
                        }
                    } else {
                        if (item.effectiveFrom == null && item.effectiveTo == null && (item.itemNumber.toLowerCase().includes(freeText.toLowerCase()) || item.itemName.toLowerCase().includes(freeText.toLowerCase()) || item.itemTypeName.toLowerCase().includes(freeText.toLowerCase())) && item.lifeCyclePhase != null && item.lifeCyclePhase.phase == vm.selectedPhase) {
                            item.searchExist = true;
                        }
                    }
                } else if ($scope.bomSearch.fromDate != null) {
                    selectedFromDate = moment($scope.bomSearch.fromDate, $rootScope.applicationDateSelectFormat);
                    if (item.effectiveFrom == null && item.effectiveTo == null && (item.itemNumber.toLowerCase().includes(freeText.toLowerCase()) || item.itemName.toLowerCase().includes(freeText.toLowerCase()) || item.itemTypeName.toLowerCase().includes(freeText.toLowerCase())) && item.lifeCyclePhase != null && item.lifeCyclePhase.phase == vm.selectedPhase) {
                        item.searchExist = true;
                    } else if (item.effectiveFrom != null && item.effectiveTo == null) {
                        itemFromDate = moment(item.effectiveFrom, $rootScope.applicationDateSelectFormat);
                        if ((itemFromDate.isAfter(selectedFromDate) || itemFromDate.isSame(selectedFromDate)) && (item.itemNumber.toLowerCase().includes(freeText.toLowerCase()) || item.itemName.toLowerCase().includes(freeText.toLowerCase())
                            || item.itemTypeName.toLowerCase().includes(freeText.toLowerCase())) && item.lifeCyclePhase != null && item.lifeCyclePhase.phase == vm.selectedPhase) {
                            item.searchExist = true;
                        }
                    } else if (item.effectiveFrom == null && item.effectiveTo != null) {
                        itemToDate = moment(item.effectiveTo, $rootScope.applicationDateSelectFormat);
                        if ((itemToDate.isAfter(selectedFromDate) || itemToDate.isSame(selectedFromDate)) && (item.itemNumber.toLowerCase().includes(freeText.toLowerCase()) || item.itemName.toLowerCase().includes(freeText.toLowerCase())
                            || item.itemTypeName.toLowerCase().includes(freeText.toLowerCase())) && item.lifeCyclePhase != null && item.lifeCyclePhase.phase == vm.selectedPhase) {
                            item.searchExist = true;
                        }
                    } else {
                        itemFromDate = moment(item.effectiveFrom, $rootScope.applicationDateSelectFormat);
                        if ((itemFromDate.isAfter(selectedFromDate) || itemFromDate.isSame(selectedFromDate)) && (item.itemNumber.toLowerCase().includes(freeText.toLowerCase()) || item.itemName.toLowerCase().includes(freeText.toLowerCase())
                            || item.itemTypeName.toLowerCase().includes(freeText.toLowerCase())) && item.lifeCyclePhase != null && item.lifeCyclePhase.phase == vm.selectedPhase) {
                            item.searchExist = true;
                        }
                    }
                } else if ($scope.bomSearch.toDate != null) {
                    selectedToDate = moment($scope.bomSearch.fromDate, $rootScope.applicationDateSelectFormat);
                    if (item.effectiveFrom == null && item.effectiveTo == null && (item.itemNumber.toLowerCase().includes(freeText.toLowerCase()) || item.itemName.toLowerCase().includes(freeText.toLowerCase())
                        || item.itemTypeName.toLowerCase().includes(freeText.toLowerCase())) && item.lifeCyclePhase != null && item.lifeCyclePhase.phase == vm.selectedPhase) {
                        item.searchExist = true;
                    } else if (item.effectiveTo != null && item.effectiveFrom == null) {
                        itemToDate = moment(item.effectiveTo, $rootScope.applicationDateSelectFormat);
                        if ((itemToDate.isBefore(selectedToDate) || itemToDate.isSame(selectedToDate)) && (item.itemNumber.toLowerCase().includes(freeText.toLowerCase()) || item.itemName.toLowerCase().includes(freeText.toLowerCase())
                            || item.itemTypeName.toLowerCase().includes(freeText.toLowerCase())) && item.lifeCyclePhase != null && item.lifeCyclePhase.phase == vm.selectedPhase) {
                            item.searchExist = true;
                        }
                    } else if (item.effectiveTo == null && item.effectiveFrom != null) {
                        itemFromDate = moment(item.effectiveFrom, $rootScope.applicationDateSelectFormat);
                        if ((itemFromDate.isBefore(selectedToDate) || itemFromDate.isSame(selectedToDate)) && (item.itemNumber.toLowerCase().includes(freeText.toLowerCase()) || item.itemName.toLowerCase().includes(freeText.toLowerCase())
                            || item.itemTypeName.toLowerCase().includes(freeText.toLowerCase())) && item.lifeCyclePhase != null && item.lifeCyclePhase.phase == vm.selectedPhase) {
                            item.searchExist = true;
                        }
                    } else {
                        itemToDate = moment(item.effectiveTo, $rootScope.applicationDateSelectFormat);
                        if ((itemToDate.isBefore(selectedToDate) || itemToDate.isSame(selectedToDate)) && (item.itemNumber.toLowerCase().includes(freeText.toLowerCase()) || item.itemName.toLowerCase().includes(freeText.toLowerCase())
                            || item.itemTypeName.toLowerCase().includes(freeText.toLowerCase())) && item.lifeCyclePhase != null && item.lifeCyclePhase.phase == vm.selectedPhase) {
                            item.searchExist = true;
                        }
                    }
                }
                return item;
            }

            function checkEffectiveDates(item) {
                var selectedToDate = null;
                var selectedFromDate = null;
                var itemFromDate = null;
                var itemToDate = null;
                if ($scope.bomSearch.fromDate != null && $scope.bomSearch.toDate != null) {
                    selectedFromDate = moment($scope.bomSearch.fromDate, $rootScope.applicationDateSelectFormat);
                    selectedToDate = moment($scope.bomSearch.toDate, $rootScope.applicationDateSelectFormat);
                    if (item.effectiveFrom != null && item.effectiveTo != null) {
                        itemFromDate = moment(item.effectiveFrom, $rootScope.applicationDateSelectFormat);
                        itemToDate = moment(item.effectiveTo, $rootScope.applicationDateSelectFormat);
                        if ((itemFromDate.isAfter(selectedFromDate) || itemFromDate.isSame(selectedFromDate)) && (itemToDate.isBefore(selectedToDate) || itemToDate.isSame(selectedToDate))) {
                            item.searchExist = true;
                        }
                    } else {
                        if (item.effectiveFrom == null && item.effectiveTo == null) {
                            item.searchExist = true;
                        }
                    }
                } else if ($scope.bomSearch.fromDate != null) {
                    selectedFromDate = moment($scope.bomSearch.fromDate, $rootScope.applicationDateSelectFormat);
                    if (item.effectiveFrom == null && item.effectiveTo == null) {
                        item.searchExist = true;
                    } else if (item.effectiveFrom != null && item.effectiveTo == null) {
                        itemFromDate = moment(item.effectiveFrom, $rootScope.applicationDateSelectFormat);
                        if (itemFromDate.isAfter(selectedFromDate) || itemFromDate.isSame(selectedFromDate)) {
                            item.searchExist = true;
                        }
                    } else if (item.effectiveFrom == null && item.effectiveTo != null) {
                        itemToDate = moment(item.effectiveTo, $rootScope.applicationDateSelectFormat);
                        if (itemToDate.isAfter(selectedFromDate) || itemToDate.isSame(selectedFromDate)) {
                            item.searchExist = true;
                        }
                    } else {
                        itemFromDate = moment(item.effectiveFrom, $rootScope.applicationDateSelectFormat);
                        if (itemFromDate.isAfter(selectedFromDate) || itemFromDate.isSame(selectedFromDate)) {
                            item.searchExist = true;
                        }
                    }
                } else if ($scope.bomSearch.toDate != null) {
                    selectedToDate = moment($scope.bomSearch.toDate, $rootScope.applicationDateSelectFormat);
                    if (item.effectiveFrom == null && item.effectiveTo == null) {
                        item.searchExist = true;
                    } else if (item.effectiveTo != null && item.effectiveFrom == null) {
                        itemToDate = moment(item.effectiveTo, $rootScope.applicationDateSelectFormat);
                        if (itemToDate.isBefore(selectedToDate) || itemToDate.isSame(selectedToDate)) {
                            item.searchExist = true;
                        }
                    } else if (item.effectiveTo == null && item.effectiveFrom != null) {
                        itemFromDate = moment(item.effectiveFrom, $rootScope.applicationDateSelectFormat);
                        if (itemFromDate.isBefore(selectedToDate) || itemFromDate.isSame(selectedToDate)) {
                            item.searchExist = true;
                        }
                    } else {
                        itemToDate = moment(item.effectiveTo, $rootScope.applicationDateSelectFormat);
                        if (itemToDate.isBefore(selectedToDate) || itemToDate.isSame(selectedToDate)) {
                            item.searchExist = true;
                        }
                    }
                }
                return item;
            }

            function checkEffectiveDatesWithPhase(item) {
                var selectedToDate = null;
                var selectedFromDate = null;
                var itemFromDate = null;
                var itemToDate = null;
                if ($scope.bomSearch.fromDate != null && $scope.bomSearch.toDate != null) {
                    selectedFromDate = moment($scope.bomSearch.fromDate, $rootScope.applicationDateSelectFormat);
                    selectedToDate = moment($scope.bomSearch.toDate, $rootScope.applicationDateSelectFormat);
                    if (item.effectiveFrom != null && item.effectiveTo != null) {
                        itemFromDate = moment(item.effectiveFrom, $rootScope.applicationDateSelectFormat);
                        itemToDate = moment(item.effectiveTo, $rootScope.applicationDateSelectFormat);
                        if ((itemFromDate.isAfter(selectedFromDate) || itemFromDate.isSame(selectedFromDate)) && (itemToDate.isBefore(selectedToDate) || itemToDate.isSame(selectedToDate)) && item.lifeCyclePhase != null && item.lifeCyclePhase.phase == vm.selectedPhase) {
                            item.searchExist = true;
                        }
                    } else {
                        if (item.effectiveFrom == null && item.effectiveTo == null && item.lifeCyclePhase != null && item.lifeCyclePhase.phase == vm.selectedPhase) {
                            item.searchExist = true;
                        }
                    }
                } else if ($scope.bomSearch.fromDate != null) {
                    selectedFromDate = moment($scope.bomSearch.fromDate, $rootScope.applicationDateSelectFormat);
                    if (item.effectiveFrom == null && item.effectiveTo == null && item.lifeCyclePhase != null && item.lifeCyclePhase.phase == vm.selectedPhase) {
                        item.searchExist = true;
                    } else if (item.effectiveFrom != null && item.effectiveTo == null) {
                        itemFromDate = moment(item.effectiveFrom, $rootScope.applicationDateSelectFormat);
                        if ((itemFromDate.isAfter(selectedFromDate) || itemFromDate.isSame(selectedFromDate)) && item.lifeCyclePhase != null && item.lifeCyclePhase.phase == vm.selectedPhase) {
                            item.searchExist = true;
                        }
                    } else if (item.effectiveFrom == null && item.effectiveTo != null) {
                        itemToDate = moment(item.effectiveTo, $rootScope.applicationDateSelectFormat);
                        if ((itemToDate.isAfter(selectedFromDate) || itemToDate.isSame(selectedFromDate)) && item.lifeCyclePhase != null && item.lifeCyclePhase.phase == vm.selectedPhase) {
                            item.searchExist = true;
                        }
                    } else {
                        itemFromDate = moment(item.effectiveFrom, $rootScope.applicationDateSelectFormat);
                        if ((itemFromDate.isAfter(selectedFromDate) || itemFromDate.isSame(selectedFromDate)) && item.lifeCyclePhase != null && item.lifeCyclePhase.phase == vm.selectedPhase) {
                            item.searchExist = true;
                        }
                    }
                } else if ($scope.bomSearch.toDate != null) {
                    selectedToDate = moment($scope.bomSearch.toDate, $rootScope.applicationDateSelectFormat);
                    if (item.effectiveFrom == null && item.effectiveTo == null && item.lifeCyclePhase != null && item.lifeCyclePhase.phase == vm.selectedPhase) {
                        item.searchExist = true;
                    } else if (item.effectiveTo != null && item.effectiveFrom == null) {
                        itemToDate = moment(item.effectiveTo, $rootScope.applicationDateSelectFormat);
                        if ((itemToDate.isBefore(selectedToDate) || itemToDate.isSame(selectedToDate)) && item.lifeCyclePhase != null && item.lifeCyclePhase.phase == vm.selectedPhase) {
                            item.searchExist = true;
                        }
                    } else if (item.effectiveTo == null && item.effectiveFrom != null) {
                        itemFromDate = moment(item.effectiveFrom, $rootScope.applicationDateSelectFormat);
                        if ((itemFromDate.isBefore(selectedToDate) || itemFromDate.isSame(selectedToDate)) && item.lifeCyclePhase != null && item.lifeCyclePhase.phase == vm.selectedPhase) {
                            item.searchExist = true;
                        }
                    } else {
                        itemToDate = moment(item.effectiveTo, $rootScope.applicationDateSelectFormat);
                        if ((itemToDate.isBefore(selectedToDate) || itemToDate.isSame(selectedToDate)) && item.lifeCyclePhase != null && item.lifeCyclePhase.phase == vm.selectedPhase) {
                            item.searchExist = true;
                        }
                    }
                }
                return item;
            }

            function populateBomItemsSearchChildrenExist(bomItem, freeText) {
                angular.forEach(bomItem.children, function (item) {
                    item.parentBom = null;
                    item.bomChildren = [];
                    if ($scope.bomSearch.fromDate == null && $scope.bomSearch.toDate == null && vm.selectedPhase == null) {
                        if (item.itemNumber.toLowerCase().includes(freeText.toLowerCase()) || item.itemName.toLowerCase().includes(freeText.toLowerCase()) || item.itemTypeName.toLowerCase().includes(freeText.toLowerCase())) {
                            item.searchExist = true;
                        }
                        if (item.description != null && item.description != "" && item.description.toLowerCase().includes(freeText.toLowerCase())) {
                            item.searchExist = true;
                        }
                        if (item.notes != null && item.notes != "" && item.notes.toLowerCase().includes(freeText.toLowerCase())) {
                            item.searchExist = true;
                        }
                        if (item.refdes != null && item.refdes != "" && item.refdes.toLowerCase().includes(freeText.toLowerCase())) {
                            item.searchExist = true;
                        }
                    } else if ((freeText != null && freeText != "") && ($scope.bomSearch.fromDate != null || $scope.bomSearch.toDate != null) && vm.selectedPhase == null) {
                        item = checkEffectiveDatesWithSearchQuery(item, freeText);
                    } else if ((freeText != null && freeText != "") && ($scope.bomSearch.fromDate != null || $scope.bomSearch.toDate != null) && vm.selectedPhase != null) {
                        item = checkEffectiveDatesWithSearchQueryAndPhase(item, freeText);
                    } else if ((freeText != null && freeText != "") && vm.selectedPhase != null) {
                        if ((item.itemNumber.toLowerCase().includes(freeText.toLowerCase()) || item.itemName.toLowerCase().includes(freeText.toLowerCase()) || item.itemTypeName.toLowerCase().includes(freeText.toLowerCase())) && item.lifeCyclePhase != null && item.lifeCyclePhase.phase == vm.selectedPhase) {
                            item.searchExist = true;
                        }
                        if (item.description != null && item.description != "" && item.description.toLowerCase().includes(freeText.toLowerCase()) && item.lifeCyclePhase != null && item.lifeCyclePhase.phase == vm.selectedPhase) {
                            item.searchExist = true;
                        }
                        if (item.notes != null && item.notes != "" && item.notes.toLowerCase().includes(freeText.toLowerCase()) && item.lifeCyclePhase != null && item.lifeCyclePhase.phase == vm.selectedPhase) {
                            item.searchExist = true;
                        }
                        if (item.refdes != null && item.refdes != "" && item.refdes.toLowerCase().includes(freeText.toLowerCase()) && item.lifeCyclePhase != null && item.lifeCyclePhase.phase == vm.selectedPhase) {
                            item.searchExist = true;
                        }
                    } else if (($scope.bomSearch.fromDate != null || $scope.bomSearch.toDate != null) && vm.selectedPhase == null) {
                        item = checkEffectiveDates(item);
                    } else if (($scope.bomSearch.fromDate != null || $scope.bomSearch.toDate != null) && vm.selectedPhase != null) {
                        item = checkEffectiveDatesWithPhase(item);
                    } else if (vm.selectedPhase != null) {
                        if (item.lifeCyclePhase != null && item.lifeCyclePhase.phase == vm.selectedPhase) {
                            item.searchExist = true;
                        }
                    }
                    item = populateBomItemsSearchChildrenExist(item, freeText);
                });

                angular.forEach(bomItem.children, function (item) {
                    if (item.searchExist) {
                        bomItem.searchExist = true;
                    }
                })
                return bomItem;
            }


            function populateBomItemsSearchChildren(bomItem, lastIndex) {
                angular.forEach(bomItem.children, function (item) {
                    if (item.searchExist) {
                        lastIndex++;
                        item.parentBom = bomItem;
                        item.isNew = false;
                        item.expanded = true;
                        item.editMode = false;
                        item.level = bomItem.level + 1;
                        item.count = 0;
                        item.bomChildren = [];
                        item.disableEdit = (item.parentLifecyclePhaseType == 'RELEASED' || item.parentLifecyclePhaseType == 'OBSOLETE');
                        if (item.hasThumbnail) {
                            item.thumbnailImage = "api/plm/items/" + item.item + "/itemImageAttribute/download?" + new Date().getTime();
                        }
                        if (vm.selectedPhase != null && item.lifeCyclePhase != null && item.lifeCyclePhase.phase == vm.selectedPhase) {
                            vm.phaseCount++;
                        }
                        vm.masters.push(item.item);
                        vm.bomIds.push(item.id);
                        vm.bomIds.push(item.item);
                        vm.bomItems.splice(lastIndex, 0, item);
                        bomItem.count = bomItem.children.length;
                        item.count = item.children.length;
                        bomItem.bomChildren.push(item);
                        lastIndex = populateBomItemsSearchChildren(item, lastIndex)
                    }
                });

                return lastIndex;
            }


            /*--------------  To add single Item to BOM ----------------------*/
            vm.onAddSingle = onAddSingle;
            function onAddSingle(item) {
                /*$scope.$on('app.item.addbom', function (event, args) {*/
                var newRow = angular.copy(emptyBomItem);
                if (item != null) {
                    lastSelectedItem = item;
                }
                if (lastSelectedItem == null || lastSelectedItem == undefined) {
                    newRow.parent = vm.itemRevision;
                    newRow.parentBom = {
                        level: -1
                    };
                    vm.bomItems.push(newRow);
                    $("#scrollcontent").animate({scrollTop: $('#scrollcontent').height()}, 1000);
                }
                else if (lastSelectedItem.itemRevision == null ||
                    lastSelectedItem.itemRevision == undefined ||
                    lastSelectedItem.itemRevision.revision == '?') {
                    $rootScope.showWarningMessage(itemsNotAddedToUnresolvedParent);
                }
                else if (lastSelectedItem.itemRevision != null &&
                    lastSelectedItem.itemRevision != undefined &&
                    lastSelectedItem.itemRevision.revision != '?' &&
                    lastSelectedItem.itemRevision.lifeCyclePhase.phaseType == 'RELEASED') {
                    $rootScope.showWarningMessage(itemsNotAddedToReleasedParent);
                }
                else {
                    if (lastSelectedItem.expanded == true) {
                        addSingle();
                    }
                    else {
                        ItemBomService.getItemBom(lastSelectedItem.item.latestRevision, false).then(
                            function (data) {
                                var index = vm.bomItems.indexOf(lastSelectedItem);
                                angular.forEach(data, function (item) {
                                    item.parentBom = lastSelectedItem;
                                    item.isNew = false;

                                    item.expanded = false;
                                    item.editMode = false;
                                    item.level = lastSelectedItem.level + 1;
                                    lastSelectedItem.bomChildren.push(item);
                                });

                                angular.forEach(lastSelectedItem.bomChildren, function (item) {
                                    index = index + 1;
                                    vm.bomItems.splice(index, 0, item);
                                });
                                addSingle();
                                lastSelectedItem.expanded = true;

                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                }
                /*});*/
            }

            function addSingle() {
                var newRow = angular.copy(emptyBomItem);
                newRow.parent = lastSelectedItem.itemRevision;
                newRow.parentBom = lastSelectedItem;
                newRow.level = newRow.parentBom.level + 1;
                var index = vm.bomItems.indexOf(lastSelectedItem);
                if (lastSelectedItem.bomChildren == undefined) {
                    lastSelectedItem.bomChildren = [];
                }

                index = index + getIndexTopInsertNewChild(lastSelectedItem) + 1;
                lastSelectedItem.bomChildren.push(newRow);

                vm.bomItems.splice(index, 0, newRow);
                vm.bomItemMap.put(lastSelectedItem.id, lastSelectedItem)
                var index1 = vm.bomItems.indexOf(newRow);
                var rows = document.getElementById('scrollcontent').getElementsByTagName('tr');
                $('#scrollcontent').animate({
                    scrollTop: rows[(index1 - 1)].offsetTop
                }, 'slow');

                //document.getElementById('scrollcontent').scrollTop = rows[(index1 - 1)].offsetTop;
            }

            function getIndexTopInsertNewChild(bomItem) {
                var index = 0;

                if (bomItem.bomChildren != undefined && bomItem.bomChildren != null) {
                    index = bomItem.bomChildren.length;
                    angular.forEach(bomItem.bomChildren, function (child) {
                        var childCount = getIndexTopInsertNewChild(child);
                        index = index + childCount;
                    })
                }

                return index;
            }

            /*------------------  To show Item Selection View  ----------------------*/

            function itemSelection(item) {
                var options = {
                    title: itemSelectionTitle,
                    template: 'app/desktop/modules/item/details/itemSelectionView.jsp',
                    controller: 'ItemSelectionController as itemSelectionVm',
                    resolve: 'app/desktop/modules/item/details/itemSelectionController',
                    width: 700,
                    showMask: true,
                    data: {
                        itemSelectionItem: lastSelectedItem,
                        mode: "items"
                    },
                    buttons: [
                        {text: submitButton, broadcast: 'add.select.item'}
                    ],
                    callback: function (result) {
                        item.itemNumber = result.itemNumber;
                        item.itemFiles = result.itemFiles;
                        findItem(item);
                    }
                };
                $rootScope.showSidePanel(options);
            }

            /*-----------------  To show Multiple Items View -----------------------*/
            vm.onAddMultiple = onAddMultiple;
            function onAddMultiple(item) {
                /* $scope.$on('app.item.bom.multiple', function (event, args) {*/
                if (item != null) {
                    lastSelectedItem = item;
                }
                if (item == undefined) {
                    lastSelectedItem = null;
                }
                if (lastSelectedItem != null && (lastSelectedItem.revision == null ||
                    lastSelectedItem.revision == undefined ||
                    lastSelectedItem.revision == '?')) {
                    $rootScope.showWarningMessage(itemsNotAddedToUnresolvedParent);
                }
                else if (lastSelectedItem != null && lastSelectedItem.revision != null &&
                    lastSelectedItem.revision != undefined &&
                    lastSelectedItem.revision != '?' &&
                    lastSelectedItem.lifeCyclePhase.phaseType == 'RELEASED') {
                    $rootScope.showWarningMessage(itemsNotAddedToReleasedParent);
                }
                else {
                    var options = {
                        title: multipleItemsSelectionTitle,
                        template: 'app/desktop/modules/item/details/itemsSelectionView.jsp',
                        controller: 'ItemsSelectionController as itemsSelectionVm',
                        resolve: 'app/desktop/modules/item/details/itemsSelectionController',
                        data: {
                            multipleItemSelection: lastSelectedItem,
                            mode: "items"
                        },
                        width: 700,
                        showMask: true,
                        buttons: [
                            {text: addButton, broadcast: 'add.select.items'}
                        ],
                        callback: function (result) {
                            if (lastSelectedItem != null && lastSelectedItem.expanded == false) {
                                toggleNode(lastSelectedItem);
                            }

                            $timeout(function () {
                                addMultiple(result);
                            }, 1000)
                        }
                    };
                    $rootScope.showSidePanel(options);
                }
            }

            vm.onSubstituteItem = onSubstituteItem;
            function onSubstituteItem(item) {
                vm.substituteBomItem = item;
                if (item != null) {
                    lastSelectedItem = item;
                }
                if (item == undefined) {
                    lastSelectedItem = null;
                }
                var options = {
                    title: multipleItemsSelectionTitle,
                    template: 'app/desktop/modules/item/details/itemsSelectionView.jsp',
                    controller: 'ItemsSelectionController as itemsSelectionVm',
                    resolve: 'app/desktop/modules/item/details/itemsSelectionController',
                    data: {
                        multipleItemSelection: lastSelectedItem,
                        mode: "substituteItem"
                    },
                    width: 700,
                    showMask: true,
                    buttons: [
                        {text: addButton, broadcast: 'add.select.items'}
                    ],
                    callback: function (item) {
                        vm.substituteBomItem.substituteItem = item;
                        vm.substituteBomItem.parentBom = null;
                        substituteBomItem(vm.substituteBomItem);
                    }
                };
                $rootScope.showSidePanel(options);
            }

            vm.addSubstituteParts = addSubstituteParts;
            function addSubstituteParts(item) {
                vm.substituteBomItem = item;
                var options = {
                    title: multipleItemsSelectionTitle,
                    template: 'app/desktop/modules/item/details/itemsSelectionView.jsp',
                    controller: 'ItemsSelectionController as itemsSelectionVm',
                    resolve: 'app/desktop/modules/item/details/itemsSelectionController',
                    data: {
                        multipleItemSelection: item,
                        mode: "substitutePart"
                    },
                    width: 700,
                    showMask: true,
                    buttons: [
                        {text: addButton, broadcast: 'add.select.items'}
                    ],
                    callback: function (items) {
                        var emptySubstitutePart = {
                            id: null,
                            parent: item.parentItemMaster,
                            part: item.item,
                            replacementPart: null
                        }
                        vm.selectedSubstituteParts = [];
                        angular.forEach(items, function (item) {
                            var substitutePart = angular.copy(emptySubstitutePart);
                            substitutePart.replacementPart = item.id;
                            vm.selectedSubstituteParts.push(substitutePart);
                        })
                        $rootScope.showBusyIndicator($('.view-container'));
                        ItemService.createSubstituteParts(item.id, vm.selectedSubstituteParts).then(
                            function (data) {
                                $rootScope.showSuccessMessage("Substitute parts added successfully");
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                };
                $rootScope.showSidePanel(options);
            }

            function substituteBomItem(bomItem) {
                ItemBomService.substituteBomItem(vm.itemId, bomItem).then(
                    function (data) {
                        updateBomItemSequence();
                    }, function (error) {
                        updateBomItemSequence();
                    }
                )
            }

            vm.editModeItemsCount = 0;
            vm.addedItemsToBom = [];
            function addMultiple(result) {
                var bomItems = [];
                angular.forEach(result, function (item) {
                    vm.editModeItemsCount++;
                    var bomItem = angular.copy(emptyBomItem);
                    bomItem.newQuantity = 1;
                    bomItem.item = item.id;
                    bomItem.itemNumber = item.itemNumber;
                    bomItem.itemName = item.itemName;
                    bomItem.itemTypeName = item.itemType.name;
                    bomItem.description = item.description;
                    bomItem.revision = item.latestRevisionObject.revision;
                    bomItem.lifeCyclePhase = item.latestRevisionObject.lifeCyclePhase;
                    bomItem.makeOrBuy = item.makeOrBuy;
                    bomItem.units = item.units;
                    bomItem.configurable = item.configurable;
                    bomItem.itemCreatedDate = item.createdDate;
                    bomItem.isNew = true;
                    if (item.thumbnail != null) {
                        bomItem.thumbnailImage = "api/plm/items/" + item.id + "/itemImageAttribute/download?" + new Date().getTime();
                    }

                    if (lastSelectedItem != null) {
                        bomItem.parent = lastSelectedItem.latestRevision;
                        bomItem.parentBom = lastSelectedItem;
                        bomItem.level = lastSelectedItem.level + 1;
                    }
                    else {
                        bomItem.parent = vm.itemRevision.id;
                        bomItem.parentBom = {
                            level: -1
                        }
                    }

                    if (lastSelectedItem != null) {
                        bomItem.parent = lastSelectedItem.latestRevision;
                        bomItem.parentBom = lastSelectedItem;
                        var index = vm.bomItems.indexOf(lastSelectedItem);
                        if (lastSelectedItem.bomChildren == undefined) {
                            lastSelectedItem.bomChildren = [];
                        }

                        var index = index + getIndexTopInsertNewChild(lastSelectedItem) + 1;
                        lastSelectedItem.bomChildren.push(bomItem);

                        vm.bomItems.splice(index, 0, bomItem);
                    }
                    else {
                        vm.bomItems.push(bomItem);
                    }
                    vm.addedItemsToBom.push(bomItem);
                    angular.forEach(vm.selectedBomAttributes, function (object) {
                        var objectName = object.name;
                        if (object.dataType == 'OBJECT') {
                            if (bomItem[object.name] == undefined) {
                                bomItem[objectName] = angular.copy(object);
                            }
                        }
                    })
                });
                if (lastSelectedItem != null) {
                    vm.bomItemMap.put(lastSelectedItem.id, lastSelectedItem);
                }
                if (lastSelectedItem == null) {
                    $("#scrollcontent").animate({scrollTop: $('#scrollcontent').height()}, 1000);
                }

                $timeout(function () {
                    calculateColumnWidthForSticky();
                }, 500)
            }

            /*----------  To get Details of selected Item(s) by using ItemeSelectionViews  ----------------*/

            function findItem(bomItem) {
                if (bomItem.itemNumber != null || bomItem.itemNumber != undefined) {
                    vm.valid = true;
                    angular.forEach(bomItem.parentBom.bomChildren, function (child) {
                        if (child.item != null && bomItem.itemNumber == child.item.itemNumber) {
                            vm.valid = false;
                            $rootScope.showErrorMessage(bomItem.itemNumber + " : " + itemExistMessage);
                        }
                    });
                    if (vm.valid == true) {
                        ItemService.findByItemNumber(bomItem.itemNumber).then(
                            function (data) {
                                if (data.length == 0) {
                                    $rootScope.showWarningMessage(bomItem.itemNumber + " : " + itemNotExist);
                                    bomItem.itemNumber = null;
                                }
                                if (data.length == 1) {
                                    var foundItem = data[0];
                                    bomItem.item = foundItem;
                                    bomItem.item.itemFiles = bomItem.itemFiles;
                                    if (bomItem.item.thumbnail != null) {
                                        bomItem.thumbnailImage = "api/plm/items/" + data[0].id + "/itemImageAttribute/download?" + new Date().getTime();
                                    }
                                    applyBomRule([bomItem]);
                                }
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                }
                angular.forEach(vm.selectedBomAttributes, function (object) {
                    var objectName = object.name;
                    if (object.dataType == 'OBJECT') {
                        if (bomItem[object.name] == undefined) {
                            bomItem[objectName] = object;
                        }
                    }
                })
            }

            vm.saveAddedItems = saveAddedItems;
            vm.removeAddedItems = removeAddedItems;
            function removeAddedItems() {
                angular.forEach(vm.addedItemsToBom, function (bomItem) {
                    if (bomItem.id == null || bomItem.id == undefined) {
                        vm.bomItems.splice(vm.bomItems.indexOf(bomItem), 1);
                    } else {
                        bomItem.editMode = false;
                        bomItem.newQuantity = bomItem.quantity;
                        bomItem.newRefdes = bomItem.refdes;
                        bomItem.newNotes = bomItem.notes;
                    }
                })
                vm.addedItemsToBom = [];
            }

            function saveAddedItems() {
                if (validateAddItems()) {
                    $rootScope.showBusyIndicator($('.view-container'))
                    var addedItems = 0;
                    angular.forEach(vm.addedItemsToBom, function (item) {
                        addedItems++;
                        item.quantity = item.newQuantity;
                        item.refdes = item.newRefdes;
                        item.notes = item.newNotes;
                        item.parentBom = null;
                        if (addedItems == vm.addedItemsToBom.length) {
                            ItemBomService.createMultipleBomItems(vm.itemId, vm.addedItemsToBom).then(
                                function (data) {
                                    angular.forEach(vm.addedItemsToBom, function (bomItem) {
                                        bomItem.isNew = false;
                                        bomItem.editMode = false;
                                    })
                                    vm.addedItemsToBom = [];
                                    vm.expandedAll = false;
                                    $rootScope.loadItemDetails();
                                    loadBom();
                                    $rootScope.showSuccessMessage(bomItemSavedMessage);
                                    $rootScope.hideBusyIndicator();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                            $rootScope.checkBomAvailOrNot = true;
                        }
                    })
                }
            }

            function validateAddItems() {
                var valid = true;

                angular.forEach(vm.addedItemsToBom, function (item) {
                    if (valid) {
                        if (item.newQuantity == null || item.newQuantity == "" || item.newQuantity == undefined) {
                            valid = false;
                            $rootScope.showWarningMessage(pleaseEnterQuantityForItem.format(item.itemNumber));
                        } else if (item.newQuantity != null && item.newQuantity < 0) {
                            valid = false;
                            $rootScope.showWarningMessage(pleaseEnterPositiveQuantityForItem.format(item.itemNumber));
                        } else if (item.itemEffectiveFrom != null && item.itemEffectiveFrom != "" && item.effectiveFrom != null && item.effectiveFrom != "") {
                            var effectiveFromDate = moment(item.effectiveFrom, $rootScope.applicationDateSelectFormat);
                            var itemEffectiveFromDate = moment(item.itemEffectiveFrom, $rootScope.applicationDateSelectFormat);
                            var itemCreationDate = moment(item.itemCreatedDate, $rootScope.applicationDateSelectFormat);
                            var createdDate = itemCreationDate.format("DD/MM/YYYY");
                            var validDate = effectiveFromDate.isSame(itemEffectiveFromDate) || effectiveFromDate.isAfter(itemEffectiveFromDate);
                            var fromDateAfterItemCreationDate = effectiveFromDate.isSameOrAfter(itemCreationDate);
                            if (!validDate) {
                                valid = false;
                                $rootScope.showWarningMessage(effectiveFromDateValidation + " " + item.itemEffectiveFrom);
                            }

                            if (!fromDateAfterItemCreationDate) {
                                valid = false;
                                $rootScope.showWarningMessage(effectiveFromAfterCreatedDate + " " + createdDate);
                            }
                        } else if (item.effectiveFrom != null && item.effectiveFrom != "") {
                            var effectiveFromDate = moment(item.effectiveFrom, $rootScope.applicationDateSelectFormat);
                            var itemCreationDate = moment(item.itemCreatedDate, $rootScope.applicationDateSelectFormat);
                            var createdDate = itemCreationDate.format("DD/MM/YYYY");
                            var fromDateAfterItemCreationDate = effectiveFromDate.isSameOrAfter(itemCreationDate);
                            if (!fromDateAfterItemCreationDate) {
                                valid = false;
                                $rootScope.showWarningMessage(effectiveFromAfterCreatedDate + " " + createdDate);
                            }
                        }

                        if (valid) {
                            if (item.itemEffectiveTo != null && item.itemEffectiveTo != "" && item.effectiveTo != null && item.effectiveTo != "") {
                                var effectiveToDate = moment(item.effectiveTo, $rootScope.applicationDateSelectFormat);
                                var itemEffectiveToDate = moment(item.itemEffectiveTo, $rootScope.applicationDateSelectFormat);
                                var itemCreatedDate = moment(item.itemCreatedDate, 'DD/MM.YYYY');
                                var createdDate = itemCreationDate.format("DD/MM/YYYY");
                                var value = effectiveToDate.isSame(itemEffectiveToDate) || effectiveToDate.isBefore(itemEffectiveToDate);
                                var toDateAfterItemCreationDate = effectiveToDate.isSame(itemCreatedDate) || effectiveToDate.isAfter(itemCreatedDate);
                                if (!value) {
                                    valid = false;
                                    $rootScope.showWarningMessage(effectiveToDateValidation + " " + item.itemEffectiveTo);
                                }

                                if (!toDateAfterItemCreationDate) {
                                    valid = false;
                                    $rootScope.showWarningMessage(effectiveToAfterCreatedDate + " " + createdDate);
                                }
                            } else if (item.effectiveTo != null && item.effectiveTo != "") {
                                var effectiveToDate = moment(item.effectiveTo, $rootScope.applicationDateSelectFormat);
                                var itemCreationDate = moment(item.itemCreatedDate, $rootScope.applicationDateSelectFormat);
                                var createdDate = itemCreationDate.format("DD/MM/YYYY");
                                var toDateAfterItemCreationDate = effectiveToDate.isSameOrAfter(itemCreationDate);
                                if (!toDateAfterItemCreationDate) {
                                    valid = false;
                                    $rootScope.showWarningMessage(effectiveToAfterCreatedDate + " " + createdDate);
                                }
                            }
                        }


                        if (valid) {
                            if (item.effectiveFrom != null && item.effectiveTo != null && item.effectiveFrom != "" && item.effectiveTo != "") {
                                var effectiveTo = moment(item.effectiveTo, $rootScope.applicationDateSelectFormat);
                                var effectiveFrom = moment(item.effectiveFrom, $rootScope.applicationDateSelectFormat);
                                var val = effectiveTo.isSame(effectiveFrom) || effectiveTo.isAfter(effectiveFrom);
                                if (!val) {
                                    valid = false;
                                    $rootScope.showWarningMessage(effectiveToValidation);
                                }
                            }
                        }
                    }
                })

                return valid;
            }

            /*-------------------  To Save Item Details  ---------------*/
            function onOk(item) {
                if (validateItem(item)) {
                    vm.bomAttribute = [];
                    var copyItem = angular.copy(item);
                    copyItem.quantity = item.newQuantity;
                    copyItem.refdes = item.newRefdes;
                    copyItem.notes = item.newNotes;
                    if (item.item == null) {
                        $rootScope.showWarningMessage(itemWarningMessage);
                    }
                    copyItem.itemNumber = item.itemNumber;
                    var parentBom = copyItem.parentBom;
                    copyItem.parentBom = null;

                    if (item.id == null) {
                        if (copyItem.newQuantity == null || copyItem.newQuantity == undefined || copyItem.newQuantity == ""
                            || copyItem.newQuantity < 1) {
                            $rootScope.showWarningMessage(quantityWarningMessage);
                        } else if (copyItem.itemNumber == null || copyItem.newQuantity == undefined || copyItem.newQuantity == "") {
                            $rootScope.showWarningMessage(itemWarningMessage);
                        } else {
                            $rootScope.showBusyIndicator($(".view-container"));
                            ItemBomService.createNewItemBom(copyItem.parent, copyItem).then(
                                function (data) {
                                    $rootScope.checkBomAvailOrNot = true;
                                    $rootScope.itemRevision.hasBom = true;
                                    item.id = data.id;
                                    item.refdes = data.refdes;
                                    item.notes = data.notes;
                                    item.quantity = item.newQuantity;
                                    item.isNew = false;
                                    item.expanded = false;
                                    item.hasBom = data.hasBom;
                                    item.latestRevision = data.latestRevision;
                                    if (parentBom != null && parentBom != undefined) {
                                        parentBom.hasBom = true;
                                        parentBom.count = item.parentBom.count + 1;
                                        item.level = item.parentBom.level + 1;
                                    }
                                    item.parentBom = parentBom;
                                    lastSelectedItem = null;
                                    vm.editModeItemsCount--;
                                    if (vm.addedItemsToBom.length > 0) {
                                        vm.addedItemsToBom.splice(vm.addedItemsToBom.indexOf(item), 1);
                                    }
                                    if (item.hasBom) {
                                        $scope.showExpandAll = true;
                                    }
                                    if (vm.selectedBomAttributes.length > 0) {
                                        var count = 0;
                                        vm.bomIds.push(item.id);
                                        angular.forEach(vm.selectedBomAttributes, function (attr) {
                                            vm.bomAttributeData = angular.copy(vm.copyBomAttributeData);
                                            if (item[attr.name] != undefined && item[attr.name].id == undefined) {
                                                vm.bomAttributeData.id.objectId = data.id;
                                                vm.bomAttributeData.id.attributeDef = attr.id;
                                                angular.merge(vm.bomAttributeData, item[attr.name]);
                                                if (attr.dataType == 'TIMESTAMP') {
                                                    if (vm.bomAttributeData.timestampValue != null) {
                                                        vm.bomAttributeData.timestampValue = moment(vm.bomAttributeData.timestampValue).format('DD/MM/YYYY, HH:mm:ss');
                                                    }
                                                    count++;
                                                    vm.bomAttribute.push(vm.bomAttributeData);
                                                } else if (attr.dataType == 'ATTACHMENT') {
                                                    var propertyAttachmentIds = [];
                                                    if (item[attr.name].attachmentValues != undefined) {
                                                        angular.forEach(item[attr.name].attachmentValues, function (attachmentFile) {
                                                            AttributeAttachmentService.saveAttributeAttachment(data.id, attr.id, 'BOMITEM', attachmentFile).then(
                                                                function (data) {
                                                                    propertyAttachmentIds.push(data[0].id);
                                                                    vm.bomAttributeData.attachmentValues.push(data[0].id);
                                                                    if (propertyAttachmentIds.length == item[attr.name].attachmentValues.length) {
                                                                        count++;
                                                                        vm.bomAttributeData.id.objectId = item.id;
                                                                        vm.bomAttributeData.id.attributeDef = attr.id;
                                                                        vm.bomAttribute.push(vm.bomAttributeData);
                                                                        if (vm.selectedBomAttributes.length == count) {
                                                                            createBomAttributes(item, vm.bomAttribute);
                                                                        }
                                                                    }
                                                                }, function (error) {
                                                                    $rootScope.showErrorMessage(error.message);
                                                                }
                                                            )
                                                        })
                                                    } else {
                                                        count++;
                                                        if (vm.selectedBomAttributes.length == count) {
                                                            createBomAttributes(item, vm.bomAttribute);
                                                        }
                                                    }
                                                } else if (attr.dataType == 'IMAGE') {
                                                    if (item[attr.name].imageFile != null || item[attr.name].imageFile != undefined) {
                                                        vm.itemBomImages.put(attr.id, item[attr.name].imageFile);
                                                        vm.itemBomImageAttributes.push(vm.bomAttributeData);
                                                        vm.bomAttributeData.imageValue = null;
                                                    }
                                                    count++;
                                                    vm.bomAttribute.push(vm.bomAttributeData);
                                                } else if (attr.dataType == 'CURRENCY') {
                                                    vm.bomAttributeData.currencyType = item[attr.name].currencyType;
                                                    vm.bomAttributeData.currencyValue = item[attr.name].currencyValue;
                                                    count++;
                                                    vm.bomAttribute.push(vm.bomAttributeData);
                                                } else if (attr.dataType == 'OBJECT' && vm.bomAttributeData.ref != null && vm.bomAttributeData.refValue != null) {
                                                    vm.bomAttributeData.refValue = null;
                                                    vm.bomAttributeData.refValue = vm.bomAttributeData.ref.id;
                                                    count++;
                                                    vm.bomAttribute.push(vm.bomAttributeData);
                                                } else {
                                                    count++;
                                                    vm.bomAttribute.push(vm.bomAttributeData);
                                                }
                                                if (vm.selectedBomAttributes.length == count) {
                                                    createBomAttributes(item, vm.bomAttribute);
                                                }
                                            } else {
                                                if (item[attr.name] != undefined) {
                                                    if (attr.dataType == 'TIMESTAMP') {
                                                        item[attr.name].timestampValue = moment(new Date()).format("DD/MM/YYYY, HH:mm:ss");
                                                        count++;
                                                        vm.bomAttribute.push(item[attr.name]);
                                                    } else if (attr.dataType == 'ATTACHMENT') {
                                                        var propertyAttachmentIds = [];
                                                        if (item[attr.name].attachmentValues != undefined) {
                                                            angular.forEach(item[attr.name].attachmentValues, function (attachmentFile) {
                                                                AttributeAttachmentService.saveAttributeAttachment(data.id, attr.id, 'BOMITEM', attachmentFile).then(
                                                                    function (data) {
                                                                        propertyAttachmentIds.push(data[0].id);
                                                                        item[attr.name].attachmentValues.push(data[0].id);
                                                                        if (propertyAttachmentIds.length == item[attr.name].attachmentValues.length) {
                                                                            count++;
                                                                            if (vm.selectedBomAttributes.length == count) {
                                                                                createBomAttributes(item, vm.bomAttribute);
                                                                            }
                                                                        }
                                                                    }, function (error) {
                                                                        $rootScope.showErrorMessage(error.message);
                                                                    }
                                                                )
                                                            });
                                                        } else {
                                                            count++;
                                                            if (vm.selectedBomAttributes.length == count) {
                                                                createBomAttributes(item, vm.bomAttribute);
                                                            }
                                                        }
                                                        vm.bomAttribute.push(item[attr.name]);
                                                    } else if (attr.dataType == 'IMAGE') {
                                                        if (item[attr.name].imageFile != null || item[attr.name].imageFile != undefined) {
                                                            vm.itemBomImages.put(attr.id, item[attr.name].imageFile);
                                                            vm.itemBomImageAttributes.push(item[attr.name]);
                                                            item[attr.name].imagePath = "api/core/objects/" + data.id + "/attributes/" + attr.id + "/imageAttribute/download?" + new Date().getTime();
                                                        }
                                                        count++;
                                                        vm.bomAttribute.push(item[attr.name]);
                                                    } else if (attr.dataType == "OBJECT") {
                                                        if (item[attr.name].refValue != null || item[attr.name].refValue != undefined) {
                                                            item[attr.name].refValue = item[attr.name].refValue.id;
                                                        }
                                                        item[attr.name].id = {objectId: data.id, attributeDef: attr.id};
                                                        item[attr.name].mlistValue = [];
                                                        item[attr.name].attachmentValues = [];
                                                        vm.bomAttribute.push(item[attr.name]);
                                                        count++;
                                                    } else {
                                                        count++;
                                                        vm.bomAttribute.push(item[attr.name]);
                                                    }
                                                    if (item[attr.name].mlistValue == null) {
                                                        item[attr.name].mlistValue = [];
                                                    }
                                                } else {
                                                    count++;
                                                }
                                                if (vm.selectedBomAttributes.length == count) {
                                                    createBomAttributes(item, vm.bomAttribute);
                                                }
                                            }
                                        });
                                    } else {
                                        item.editMode = false;
                                        $rootScope.hideBusyIndicator();
                                        $rootScope.loadItemDetails();
                                    }
                                    $rootScope.showSuccessMessage(bomItemSavedMessage);
                                    $scope.showExpandAll = false;
                                    angular.forEach(vm.bomItems, function (item) {
                                        if (item.level == 0 && item.hasBom) {
                                            $scope.showExpandAll = true;
                                        }
                                    })
                                    vm.expandedAll = false;
                                    var childCount = 0;
                                    var expandedCount = 0;
                                    angular.forEach(vm.bomItems, function (item) {
                                        if (item.level == 0) {
                                            if (item.hasBom) {
                                                childCount++;
                                                if (!item.expanded) {
                                                    vm.expandedAll = false;
                                                } else {
                                                    expandedCount++;
                                                }
                                            }
                                        }
                                    })
                                    if (childCount == expandedCount) {
                                        vm.expandedAll = true;
                                    }
                                }, function (error) {
                                    copyItem.parentBom = parentBom;
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    }
                    else {
                        $rootScope.showBusyIndicator($('.view-container'));
                        ItemBomService.updateItemBom(copyItem.parent, copyItem).then(
                            function (data) {
                                item.quantity = data.quantity;
                                item.refdes = data.refdes;
                                item.notes = data.notes;
                                vm.editModeItemsCount--;
                                if (vm.addedItemsToBom.length > 0) {
                                    vm.addedItemsToBom.splice(vm.addedItemsToBom.indexOf(item), 1);
                                }
                                var count = 0;
                                if (vm.selectedBomAttributes.length > 0) {
                                    angular.forEach(vm.selectedBomAttributes, function (attr) {
                                        vm.bomAttributeData = angular.copy(vm.copyBomAttributeData);
                                        if (item[attr.name] != undefined && item[attr.name].id == undefined) {
                                            if (item[attr.name].objectType == 'ITEM' || item[attr.name].objectType == 'ITEMREVISION' || item[attr.name].objectType == 'CHANGE'
                                                || item[attr.name].objectType == 'MANUFACTURER' || item[attr.name].objectType == 'MANUFACTURERPART' || item[attr.name].objectType == 'PLMWORKFLOWDEFINITION') {
                                                vm.refValue = item[attr.name];
                                                item[attr.name] = null;
                                            }
                                            angular.merge(vm.bomAttributeData, item[attr.name]);
                                            vm.bomAttributeData.id.objectId = data.id;
                                            vm.bomAttributeData.id.attributeDef = attr.id;
                                            if (attr.dataType == 'TIMESTAMP') {
                                                count++;
                                                if (vm.bomAttributeData.timestampValue != null) {
                                                    vm.bomAttributeData.timestampValue = moment(vm.bomAttributeData.timestampValue).format('DD/MM/YYYY, HH:mm:ss');
                                                }
                                                vm.bomAttribute.push(vm.bomAttributeData);
                                            } else if (attr.dataType == 'ATTACHMENT') {
                                                var propertyAttachmentIds = [];
                                                if (item[attr.name].attachmentValues != undefined) {
                                                    angular.forEach(item[attr.name].attachmentValues, function (attachmentFile) {
                                                        AttributeAttachmentService.saveAttributeAttachment(data.id, attr.id, 'BOMITEM', attachmentFile).then(
                                                            function (data) {
                                                                propertyAttachmentIds.push(data[0].id);
                                                                vm.bomAttributeData.attachmentValues.push(data[0].id);
                                                                if (propertyAttachmentIds.length == item[attr.name].attachmentValues.length) {
                                                                    count++;
                                                                    vm.bomAttributeData.id.objectId = item.id;
                                                                    vm.bomAttributeData.id.attributeDef = attr.id;
                                                                    vm.bomAttribute.push(vm.bomAttributeData);
                                                                    if (vm.selectedBomAttributes.length == count) {
                                                                        updateBomAttributes(item, vm.bomAttribute);
                                                                    }
                                                                }
                                                            }, function (error) {
                                                                $rootScope.showErrorMessage(error.message);
                                                            }
                                                        )
                                                    });
                                                } else {
                                                    count++;
                                                    vm.bomAttribute.push(vm.bomAttributeData);
                                                    if (vm.selectedBomAttributes.length == count) {
                                                        updateBomAttributes(item, vm.bomAttribute);
                                                    }
                                                }
                                            } else if (attr.dataType == 'IMAGE') {
                                                if (item[attr.name].imageFile != null || item[attr.name].imageFile != undefined) {
                                                    vm.itemBomImages.put(attr.id, item[attr.name].imageFile);
                                                    vm.itemBomImageAttributes.push(vm.bomAttributeData);
                                                    vm.bomAttributeData.imageValue = null;
                                                }
                                                count++;
                                                vm.bomAttribute.push(vm.bomAttributeData);
                                            } else if (attr.dataType == 'CURRENCY') {
                                                vm.bomAttributeData.currencyType = item[attr.name].currencyType;
                                                vm.bomAttributeData.currencyValue = item[attr.name].currencyValue;
                                                count++;
                                                vm.bomAttribute.push(vm.bomAttributeData);
                                            } else if (attr.dataType == 'OBJECT') {
                                                if (vm.bomAttributeData.refValue != null && vm.bomAttributeData.ref != null) {
                                                    vm.bomAttributeData.refValue = null;
                                                    vm.bomAttributeData.refValue = vm.bomAttributeData.ref.id;
                                                } else {
                                                    vm.bomAttributeData.refValue = null;
                                                    vm.bomAttributeData.refValue = vm.refValue.id;
                                                }
                                                count++;
                                                vm.bomAttributeData.id = {objectId: item.id, attributeDef: attr.id};
                                                vm.bomAttributeData.mlistValue = [];
                                                vm.bomAttributeData.attachmentValues = [];
                                                vm.bomAttribute.push(vm.bomAttributeData);
                                            } else {
                                                count++;
                                                vm.bomAttribute.push(vm.bomAttributeData);
                                            }
                                            angular.forEach(vm.attachments, function (attachment) {
                                                vm.bomAttributeData.attachmentValues.push(attachment.id);
                                            });
                                            if (vm.selectedBomAttributes.length == count) {
                                                updateBomAttributes(item, vm.bomAttribute);
                                            }
                                        } else {
                                            if (item[attr.name] != undefined) {
                                                if (attr.dataType == 'TIMESTAMP') {
                                                    item[attr.name].timestampValue = moment(new Date()).format("DD/MM/YYYY, HH:mm:ss");
                                                    count++;
                                                    vm.bomAttribute.push(item[attr.name]);
                                                } else if (attr.dataType == 'ATTACHMENT') {
                                                    var propertyAttachmentIds = [];
                                                    if (item[attr.name].attachmentValues != undefined) {
                                                        angular.forEach(item[attr.name].attachmentValues, function (attachmentFile) {
                                                            AttributeAttachmentService.saveAttributeAttachment(data.id, attr.id, 'BOMITEM', attachmentFile).then(
                                                                function (data) {
                                                                    propertyAttachmentIds.push(data[0].id);
                                                                    item[attr.name].attachmentValues.push(data[0].id);
                                                                    if (propertyAttachmentIds.length == item[attr.name].attachmentValues.length) {
                                                                        count++;
                                                                        if (vm.selectedBomAttributes.length == count) {
                                                                            updateBomAttributes(item, vm.bomAttribute);
                                                                        }
                                                                    }
                                                                }, function (error) {
                                                                    $rootScope.showErrorMessage(error.message);
                                                                }
                                                            )
                                                        });
                                                    } else {
                                                        count++;
                                                        if (vm.selectedBomAttributes.length == count) {
                                                            updateBomAttributes(item, vm.bomAttribute);
                                                        }
                                                    }
                                                    vm.bomAttribute.push(item[attr.name]);
                                                } else if (attr.dataType == 'IMAGE') {
                                                    if (item[attr.name].imageFile != null || item[attr.name].imageFile != undefined) {
                                                        vm.itemBomImages.put(attr.id, item[attr.name].imageFile);
                                                        vm.itemBomImageAttributes.push(item[attr.name]);
                                                        item[attr.name].imagePath = "api/core/objects/" + item.id + "/attributes/" + attr.id + "/imageAttribute/download?" + new Date().getTime();
                                                    }
                                                    count++;
                                                    vm.bomAttribute.push(item[attr.name]);
                                                } else if (attr.dataType == "OBJECT") {
                                                    if (item[attr.name].refValue != null || item[attr.name].refValue != undefined) {
                                                        item[attr.name].refValue = item[attr.name].refValue.id;
                                                    }
                                                    item[attr.name].id = {objectId: item.id, attributeDef: attr.id};
                                                    item[attr.name].mlistValue = [];
                                                    item[attr.name].attachmentValues = [];
                                                    vm.bomAttribute.push(item[attr.name]);
                                                    count++;
                                                } else {
                                                    count++;
                                                    vm.bomAttribute.push(item[attr.name]);
                                                }
                                                angular.forEach(vm.attachments, function (attachment) {
                                                    item[attr.name].attachmentValues.push(attachment.id);
                                                });
                                                if (item[attr.name].mlistValue == null) {
                                                    item[attr.name].mlistValue = [];
                                                }
                                            } else {
                                                count++;
                                            }
                                            if (vm.selectedBomAttributes.length == count) {
                                                updateBomAttributes(item, vm.bomAttribute);
                                            }
                                        }
                                    });
                                } else {
                                    item.editMode = false;
                                    $rootScope.hideBusyIndicator();
                                }
                                $rootScope.showSuccessMessage(bomItemSavedMessage);
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                }
            }

            $scope.showBomItemRichText = showBomItemRichText;
            $scope.hideRichTextDialog = hideRichTextDialog;
            function showBomItemRichText(attribute, itemAttribute, item) {
                if (itemAttribute == undefined) {
                    item[attribute.name] = {};
                    item[attribute.name].oldRichTextValue = null;
                    item[attribute.name].richTextValue = null;
                    vm.bomItemRichText = item[attribute.name];
                } else {
                    vm.bomItemRichText = itemAttribute;
                    vm.bomItemRichText.oldRichTextValue = itemAttribute.richTextValue;
                    vm.bomItemRichText.notSavedOldRichTextValue = itemAttribute.richTextValue;
                }
                vm.bomItemRichText.encodedRichText = $sce.trustAsHtml(vm.bomItemRichText.richTextValue);
                vm.bomItemAttribute = attribute;
                vm.attributeBomItem = item;
                showProcedureDialog()
            }

            function showProcedureDialog() {
                var modal = document.getElementById("bom-item-richText");
                modal.style.display = "block";
                $timeout(function () {
                    var headerHeight = $('.bom-item-header').outerHeight();
                    var checklistContentHeight = $('.bom-item-content').outerHeight();
                    $(".procedure-content").height(checklistContentHeight - (headerHeight + 20));
                    $(".note-editable").height($(".procedure-content").outerHeight() - 120);
                    $(".note-editor").height($(".procedure-content").outerHeight() - 30);
                }, 200)
            }

            function hideRichTextDialog() {
                var modal = document.getElementById("bom-item-richText");
                modal.style.display = "none";
                vm.bomItemRichText.richTextValue = vm.bomItemRichText.oldRichTextValue;
            }

            function validateItem(item) {
                var valid = true;

                if (item.newQuantity == null || item.newQuantity == "" || item.newQuantity == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(pleaseEnterPositiveQuantity);
                } else if (item.newQuantity != null && item.newQuantity < 0) {
                    valid = false;
                    $rootScope.showWarningMessage(pleaseEnterPositiveQuantity);
                } else if (item.itemEffectiveFrom != null && item.itemEffectiveFrom != "" && item.effectiveFrom != null && item.effectiveFrom != "") {
                    var effectiveFromDate = moment(item.effectiveFrom, $rootScope.applicationDateSelectFormat);
                    var itemEffectiveFromDate = moment(item.itemEffectiveFrom, $rootScope.applicationDateSelectFormat);
                    var itemCreationDate = moment(item.itemCreatedDate, $rootScope.applicationDateSelectFormat);
                    var createdDate = itemCreationDate.format("DD/MM/YYYY");
                    var validDate = effectiveFromDate.isSame(itemEffectiveFromDate) || effectiveFromDate.isAfter(itemEffectiveFromDate);
                    var fromDateAfterItemCreationDate = effectiveFromDate.isSameOrAfter(itemCreationDate);
                    if (!validDate) {
                        valid = false;
                        $rootScope.showWarningMessage(effectiveFromDateValidation + " " + item.itemEffectiveFrom);
                    }

                    if (!fromDateAfterItemCreationDate) {
                        valid = false;
                        $rootScope.showWarningMessage(effectiveFromAfterCreatedDate + " " + createdDate);
                    }
                } else if (item.effectiveFrom != null && item.effectiveFrom != "") {
                    var effectiveFromDate = moment(item.effectiveFrom, $rootScope.applicationDateSelectFormat);
                    var itemCreationDate = moment(item.itemCreatedDate, $rootScope.applicationDateSelectFormat);
                    var createdDate = itemCreationDate.format("DD/MM/YYYY");
                    var fromDateAfterItemCreationDate = effectiveFromDate.isSameOrAfter(itemCreationDate);
                    if (!fromDateAfterItemCreationDate) {
                        valid = false;
                        $rootScope.showWarningMessage(effectiveFromAfterCreatedDate + " " + createdDate);
                    }
                }

                if (valid) {
                    if (item.itemEffectiveTo != null && item.itemEffectiveTo != "" && item.effectiveTo != null && item.effectiveTo != "") {
                        var effectiveToDate = moment(item.effectiveTo, $rootScope.applicationDateSelectFormat);
                        var itemEffectiveToDate = moment(item.itemEffectiveTo, $rootScope.applicationDateSelectFormat);
                        var itemCreatedDate = moment(item.itemCreatedDate, 'DD/MM.YYYY');
                        var createdDate = itemCreationDate.format("DD/MM/YYYY");
                        var value = effectiveToDate.isSame(itemEffectiveToDate) || effectiveToDate.isBefore(itemEffectiveToDate);
                        var toDateAfterItemCreationDate = effectiveToDate.isSame(itemCreatedDate) || effectiveToDate.isAfter(itemCreatedDate);
                        if (!value) {
                            valid = false;
                            $rootScope.showWarningMessage(effectiveToDateValidation + " " + item.itemEffectiveTo);
                        }

                        if (!toDateAfterItemCreationDate) {
                            valid = false;
                            $rootScope.showWarningMessage(effectiveToAfterCreatedDate + " " + createdDate);
                        }
                    } else if (item.effectiveTo != null && item.effectiveTo != "") {
                        var effectiveToDate = moment(item.effectiveTo, $rootScope.applicationDateSelectFormat);
                        var itemCreationDate = moment(item.itemCreatedDate, $rootScope.applicationDateSelectFormat);
                        var createdDate = itemCreationDate.format("DD/MM/YYYY");
                        var toDateAfterItemCreationDate = effectiveToDate.isSameOrAfter(itemCreationDate);
                        if (!toDateAfterItemCreationDate) {
                            valid = false;
                            $rootScope.showWarningMessage(effectiveToAfterCreatedDate + " " + createdDate);
                        }
                    }
                }


                if (valid) {
                    if (item.effectiveFrom != null && item.effectiveTo != null && item.effectiveFrom != "" && item.effectiveTo != "") {
                        var effectiveTo = moment(item.effectiveTo, $rootScope.applicationDateSelectFormat);
                        var effectiveFrom = moment(item.effectiveFrom, $rootScope.applicationDateSelectFormat);
                        var val = effectiveTo.isSame(effectiveFrom) || effectiveTo.isAfter(effectiveFrom);
                        if (!val) {
                            valid = false;
                            $rootScope.showWarningMessage(effectiveToValidation);
                        }
                    }
                }

                return valid;
            }

            /*------------  To Create BOMITEM attributes for selected Item Bom Creation ----------*/

            function createBomAttributes(bomItem, attributes) {
                ObjectAttributeService.saveItemObjectAttributes(bomItem.id, attributes).then(
                    function (data) {
                        if (vm.itemBomImageAttributes.length > 0) {
                            angular.forEach(vm.itemBomImageAttributes, function (itemBomImage) {
                                ObjectAttributeService.uploadObjectAttributeImage(itemBomImage.id.objectId, itemBomImage.id.attributeDef, vm.itemBomImages.get(itemBomImage.id.attributeDef)).then(
                                    function (data) {
                                        loadBomAttributeValues();
                                        bomItem.editMode = false;
                                        $rootScope.hideBusyIndicator();
                                        $rootScope.loadItemDetails();
                                    }, function (error) {
                                        loadBomAttributeValues();
                                        bomItem.editMode = false;
                                        $rootScope.hideBusyIndicator();
                                    }
                                )
                            })
                        } else {
                            loadBomAttributeValues();
                            bomItem.editMode = false;
                            $rootScope.hideBusyIndicator();
                            $rootScope.loadItemDetails();
                        }
                    }, function (error) {
                        loadBomAttributeValues();
                        bomItem.editMode = false;
                        $rootScope.hideBusyIndicator();
                        $rootScope.loadItemDetails();
                    }
                )
            }

            /*------------  To Update BOMITEM attributes for selected Item Bom Creation ----------*/

            function updateBomAttributes(bomItem, attributes) {
                ObjectAttributeService.saveItemObjectAttributes(bomItem.id, attributes).then(
                    function (data) {
                        if (vm.itemBomImageAttributes.length > 0) {
                            angular.forEach(vm.itemBomImageAttributes, function (itemBomImage) {
                                ObjectAttributeService.uploadObjectAttributeImage(itemBomImage.id.objectId, itemBomImage.id.attributeDef, vm.itemBomImages.get(itemBomImage.id.attributeDef)).then(
                                    function (data) {
                                        loadBomAttributeValues();
                                        bomItem.editMode = false;
                                        $rootScope.hideBusyIndicator();
                                    }, function (error) {
                                        loadBomAttributeValues();
                                        bomItem.editMode = false;
                                        $rootScope.hideBusyIndicator();
                                    }
                                )
                            })
                        } else {
                            loadBomAttributeValues();
                            bomItem.editMode = false;
                            $rootScope.hideBusyIndicator();
                        }
                    }, function (error) {
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.attachments = [];
            function editItem(item) {
                ItemService.getItemRevisions(item.id).then(
                    function (data) {
                        vm.revisions = data;
                        angular.forEach(vm.revisions, function (revision) {
                            vm.bomRevisions.push(revision.revision);
                        })
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
                angular.forEach(vm.selectedBomAttributes, function (selected) {
                    if (selected.objectType == "BOMITEM") {
                        selected.editMode = true;
                    }
                    if (selected.dataType == 'ATTACHMENT') {
                        if (item[selected.name] != undefined) {
                            vm.attachments = item[selected.name].attachmentValues;
                        }
                    } else if (selected.dataType == 'IMAGE') {
                        if (item[selected.name] != undefined) {
                            vm.imageValue = item[selected.name].imageValue;
                        }
                    }
                });

                item.newQuantity = item.quantity;
                item.newRefdes = item.refdes;
                item.newNotes = item.notes;
                item.editMode = true;
                item.flag = false;
                vm.editModeItemsCount++;
                vm.addedItemsToBom.push(item);
                $scope.$evalAsync();
            }

            /*-------------------  To delete selected Item ----------------*/

            function getKeysFromMap(obj, value) {
                vm.keyArray = [];

                angular.forEach(obj, function (values, key) {
                    angular.forEach(values, function (val) {
                        if (value == val.itemId) {
                            vm.keyArray.push(key);
                        }
                    })

                });
                return vm.keyArray;
            }

            function deleteItem(item) {
                var options = {
                    title: bomItemTitleMessage,
                    message: bomItemDialogMessage + "item" + " [ " + item.itemName + " ]" + itemDelete + "?",
                    okButtonClass: 'btn-danger'
                };
                var bomMap = new Hashtable();
                angular.forEach(vm.bomItems, function (bomItem) {
                    bomMap.put(bomItem.latestRevision, bomItem);
                })
                ItemService.getItemRevision(item.parent).then(
                    function (data) {
                        var itemParent = data;
                        vm.itemTypeJson = new Map();
                        vm.itemTypeJson = new Map(JSON.parse(itemParent.inclusionRules));
                        var key = getKeysFromMap(vm.itemTypeJson, item.item);
                        var i = 0;
                        angular.forEach(vm.keyArray, function (val) {
                            i++;
                            if (vm.itemTypeJson.has(val)) {
                                vm.itemTypeJson.delete(val);
                            }
                        })

                        DialogService.confirm(options, function (yes) {
                            if (yes == true) {
                                $rootScope.showBusyIndicator($('.view-content'));
                                var parentId = item.item;
                                ItemBomService.deleteBomItem(parentId, item.id).then(
                                    function () {
                                        if (vm.itemTypeJson != null && vm.itemTypeJson.size > 0) {
                                            itemParent.inclusionRules = JSON.stringify(Array.from(vm.itemTypeJson.entries()));
                                            ItemService.updateItem(itemParent).then(
                                                function (data) {
                                                    $rootScope.loadItemDetails();
                                                    updateParent(item, bomMap);
                                                }, function (error) {
                                                    $rootScope.showErrorMessage(error.message);
                                                }
                                            )
                                        } else {
                                            $rootScope.loadItemDetails();
                                            updateParent(item, bomMap);
                                        }
                                        $timeout(function () {
                                            calculateColumnWidthForSticky();
                                        }, 1000);
                                        if (item.parentBom != undefined && item.parentBom != null && item.parentBom.id != undefined) {
                                            var parent = vm.bomItemMap.get(item.parentBom.id);
                                            parent.count--;
                                        }
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                        $rootScope.hideBusyIndicator();
                                    }
                                )
                            }
                        });
                    }
                )
            }

            function updateParent(item, bomMap) {
                if (item.bomChildren != undefined) {
                    if (item.expanded && item.bomChildren.length > 0) {
                        removeChildren(item);
                    }
                }
                if (item.parentBom != null && item.parentBom.bomChildren != undefined && item.parentBom.bomChildren.length > 0) {
                    item.parentBom.bomChildren.splice(item.parentBom.bomChildren.indexOf(item), 1);
                }
                var index = vm.bomItems.indexOf(item);
                vm.bomItems.splice(index, 1);
                lastSelectedItem = null;
                $rootScope.showSuccessMessage(bomItemDeleteMessage);
                $rootScope.hideBusyIndicator();
                $scope.showExpandAll = false;
                angular.forEach(vm.bomItems, function (item) {
                    if (item.level == 0 && item.hasBom) {
                        $scope.showExpandAll = true;
                    }
                })
                vm.expandedAll = false;
                var childCount = 0;
                var expandedCount = 0;
                angular.forEach(vm.bomItems, function (item) {
                    if (item.level == 0) {
                        if (item.hasBom) {
                            childCount++;
                            if (!item.expanded) {
                                vm.expandedAll = false;
                            } else {
                                expandedCount++;
                            }
                        }
                    }
                })
                if (childCount == expandedCount) {
                    vm.expandedAll = true;
                }
            }

            function onCancel(item) {
                if (item.parent != vm.itemId) {
                    var bomIndex = item.parentBom.bomChildren.indexOf(item);
                    item.parentBom.bomChildren.splice(bomIndex, 1);
                }
                if (item.isNew == true) {
                    var index = vm.bomItems.indexOf(item);
                    vm.bomItems.splice(index, 1);
                    vm.addedItemsToBom.splice(vm.addedItemsToBom.indexOf(item), 1);
                }
                else {
                    item.editMode = false;
                    item.flag = true;
                    item.newQuantity = item.quantity;
                    item.newRefdes = item.refdes;
                    item.newNotes = item.notes;
                    angular.forEach(vm.selectedBomAttributes, function (attribute) {
                        if (attribute.dataType == "RICHTEXT" && item[attribute.name] != null && item[attribute.name] != undefined) {
                            item[attribute.name].richTextValue = vm.bomItemRichText.notSavedOldRichTextValue;
                        }
                    })
                    vm.addedItemsToBom.splice(vm.addedItemsToBom.indexOf(item), 1);
                }
                vm.editModeItemsCount--;
            }

            vm.expandAllBomItems = expandAllBomItems;
            function expandAllBomItems() {
                vm.expandedAll = !vm.expandedAll;
                vm.selectedPhase = null;
                $scope.limit = 30;
                vm.bomItems = [];
                vm.bomIds = [];
                vm.loading = true;
                if (!vm.expandedAll) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    if (vm.searchMode) {
                        searchBomItems($scope.bomSearch.searchQuery);
                    } else {
                        loadBom();
                    }
                } else {
                    $rootScope.showBusyIndicator($('.view-container'));
                    var bomCall = null;
                    if (vm.searchMode) {
                        bomCall = ItemBomService.searchBomItems(vm.itemId, true, $scope.bomSearch);
                    } else {
                        bomCall = ItemBomService.getTotalBom(vm.itemId, true, vm.selectedBomRule)
                    }

                    if (bomCall != null) {
                        bomCall.then(
                            function (data) {
                                vm.oldBomItemsData = null;
                                vm.bomItemsCopy = angular.copy(data);
                                vm.bomItems = [];
                                angular.forEach(data, function (item) {
                                    item.parentBom = null;
                                    item.isNew = false;
                                    item.editMode = false;
                                    item.expanded = true;
                                    item.level = 0;
                                    item.isRoot = true;
                                    item.count = 0;
                                    item.bomChildren = [];
                                    item.disableEdit = (item.parentLifecyclePhaseType == 'RELEASED' || item.parentLifecyclePhaseType == 'OBSOLETE');
                                    if (item.hasThumbnail) {
                                        item.thumbnailImage = "api/plm/items/" + item.item + "/itemImageAttribute/download?" + new Date().getTime();
                                    }
                                    if (item.configurable) {
                                        $rootScope.showResolveBomButton = true;
                                    }
                                    vm.bomItems.push(item);
                                    vm.bomIds.push(item.id);
                                    vm.bomIds.push(item.item);
                                    var index = vm.bomItems.indexOf(item);
                                    index = populateChildren(item, index);
                                    vm.bomItemMap.put(item.id, item);
                                });
                                vm.selectedBomAttributeIds = [];
                                angular.forEach(vm.selectedBomAttributes, function (bomAttribute) {
                                    vm.selectedBomAttributeIds.push(bomAttribute.id);
                                });
                                if (vm.bomIds.length > 0 && vm.selectedBomAttributeIds.length > 0) {
                                    ItemService.getAttributesByItemIdAndAttributeId(vm.bomIds, vm.selectedBomAttributeIds).then(
                                        function (data) {
                                            vm.selectedBomChildrentAttributesValues = data;

                                            var map = new Hashtable();
                                            angular.forEach(vm.selectedBomAttributes, function (att) {
                                                if (att.id != null && att.id != "" && att.id != 0) {
                                                    map.put(att.id, att);
                                                }
                                            });

                                            angular.forEach(vm.bomItems, function (item) {
                                                var childrenAttributes = [];
                                                var bomChildrenAttributes = vm.selectedBomChildrentAttributesValues[item.id];
                                                if (bomChildrenAttributes != null && bomChildrenAttributes != undefined) {
                                                    childrenAttributes = childrenAttributes.concat(bomChildrenAttributes);
                                                }

                                                bomItemAttr(childrenAttributes, item, map);
                                            });
                                            angular.forEach(vm.bomItems, function (bom) {
                                                var childrenAttributes = [];
                                                var bomChildrenAttributes = vm.selectedBomChildrentAttributesValues[bom.item];
                                                if (bomChildrenAttributes != null && bomChildrenAttributes != undefined) {
                                                    childrenAttributes = childrenAttributes.concat(bomChildrenAttributes);
                                                }
                                                bomItemAttr(childrenAttributes, bom, map);
                                            });
                                            angular.forEach(vm.bomItems, function (item) {
                                                angular.forEach(vm.selectedBomAttributes, function (object) {
                                                    var objectName = object.name;
                                                    if (object.dataType == 'OBJECT') {
                                                        if (item[object.name] == undefined) {
                                                            item[objectName] = object;
                                                        }
                                                    }
                                                })
                                            })
                                        }
                                        ,
                                        function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                        }
                                    );
                                }
                                $timeout(function () {
                                    calculateColumnWidthForSticky();
                                    $rootScope.hideBusyIndicator();
                                    vm.loading = false;
                                }, 500);
                            }
                            ,
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                }
            }

            function populateChildren(bomItem, lastIndex) {
                angular.forEach(bomItem.children, function (item) {
                    lastIndex++;
                    item.parentBom = bomItem;
                    item.isNew = false;
                    item.expanded = true;
                    item.editMode = false;
                    item.level = bomItem.level + 1;
                    item.count = 0;
                    item.bomChildren = [];
                    item.disableEdit = (item.parentLifecyclePhaseType == 'RELEASED' || item.parentLifecyclePhaseType == 'OBSOLETE');
                    if (item.hasThumbnail) {
                        item.thumbnailImage = "api/plm/items/" + item.item + "/itemImageAttribute/download?" + new Date().getTime();
                    }

                    vm.bomItems.splice(lastIndex, 0, item);
                    vm.bomIds.push(item.id);
                    vm.bomIds.push(item.item);
                    bomItem.count = bomItem.count + 1;
                    bomItem.bomChildren.push(item);
                    lastIndex = populateChildren(item, lastIndex)
                });

                return lastIndex;
            }


            /*--------  To Load selected Item Childrens, when click on plus(+) Button ----------- */

            vm.toggleBomItem = toggleBomItem;
            function toggleBomItem(bomItem) {
                $rootScope.showBusyIndicator($('.view-content'));
                ItemService.updateBomItemSequence(bomItem.latestRevision).then(
                    function (data) {
                        toggleNode(bomItem);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.collapseAction = true;
            function toggleNode(bomItem) {
                vm.bomIds = [];
                vm.selectedBomAttributeIds = [];
                if (bomItem.expanded == null || bomItem.expanded == undefined) {
                    bomItem.expanded = false;
                }
                bomItem.expanded = !bomItem.expanded;
                var index = vm.bomItems.indexOf(bomItem);
                if (bomItem.expanded == false) {
                    removeChildren(bomItem);
                    vm.collapseAction = true;
                    $timeout(function () {
                        calculateColumnWidthForSticky();
                        $rootScope.hideBusyIndicator();
                    }, 200);
                    vm.expandedAll = false;
                    var childCount = 0;
                    var expandedCount = 0;
                    angular.forEach(vm.bomItems, function (item) {
                        if (item.level == 0) {
                            if (item.hasBom) {
                                childCount++;
                                if (!item.expanded) {
                                    vm.expandedAll = false;
                                } else {
                                    expandedCount++;
                                }
                            }
                        }
                    })
                    if (childCount == expandedCount) {
                        vm.expandedAll = true;
                    }
                }
                else {
                    if (bomItem.hasBom) {
                        var bomCall = null;
                        vm.collapseAction = false;
                        if (vm.resolvedBom) {
                            bomCall = ItemBomService.getItemBomInstances(bomItem.id);
                        } else {
                            if (vm.searchMode) {
                                bomCall = ItemBomService.searchBomItems(bomItem.latestRevision, false, $scope.bomSearch);
                            } else {
                                bomCall = ItemBomService.getTotalBom(bomItem.latestRevision, false, vm.selectedBomRule);
                            }
                        }

                        if (bomCall != null) {
                            bomCall.then(
                                function (data) {
                                    bomItem.count = data.length;
                                    angular.forEach(data, function (item) {
                                        item.parentBom = bomItem;
                                        item.isNew = false;
                                        item.expanded = false;
                                        item.editMode = false;
                                        item.level = bomItem.level + 1;
                                        item.bomChildren = [];

                                        item.disableEdit = (item.parentLifecyclePhaseType == 'RELEASED' || item.parentLifecyclePhaseType == 'OBSOLETE');
                                        if (item.hasThumbnail) {
                                            item.thumbnailImage = "api/plm/items/" + item.item + "/itemImageAttribute/download?" + new Date().getTime();
                                        }
                                        bomItem.bomChildren.push(item);
                                    });

                                    angular.forEach(bomItem.bomChildren, function (item) {
                                        if ($rootScope.external.external == true) {
                                            var session = JSON.parse(localStorage.getItem('local_storage_login'));
                                            $rootScope.loginPersonDetails = session.login;
                                            ShareService.getSharedObjectByPersonAndItem(item.item, $rootScope.loginPersonDetails.person.id).then(
                                                function (sharedObject) {
                                                    if (sharedObject.length > 0) {
                                                        if (sharedObject.length == 1) {
                                                            angular.forEach(sharedObject, function (shared) {
                                                                item.sharedPermission = shared.permission;
                                                            })
                                                            index = index + 1;
                                                            vm.bomItems.splice(index, 0, item);
                                                        }
                                                    } else {
                                                        item.sharedPermission = "READ";
                                                        index = index + 1;
                                                        vm.bomItems.splice(index, 0, item);
                                                    }
                                                }, function (error) {
                                                    $rootScope.showErrorMessage(error.message);
                                                }
                                            )
                                        } else {
                                            index = index + 1;
                                            vm.bomItems.splice(index, 0, item);
                                        }
                                    });

                                    vm.bomItemMap.put(bomItem.id, bomItem);

                                    angular.forEach(bomItem.bomChildren, function (bomChild) {
                                        vm.bomIds.push(bomChild.id);
                                        vm.bomIds.push(bomChild.item.id);
                                    });

                                    angular.forEach(vm.selectedBomAttributes, function (bomAttribute) {
                                        vm.selectedBomAttributeIds.push(bomAttribute.id);
                                    });
                                    if (vm.bomIds.length > 0 && vm.selectedBomAttributeIds.length > 0) {
                                        ItemService.getAttributesByItemIdAndAttributeId(vm.bomIds, vm.selectedBomAttributeIds).then(
                                            function (data) {
                                                vm.selectedBomChildrentAttributesValues = data;

                                                var map = new Hashtable();
                                                angular.forEach(vm.selectedBomAttributes, function (att) {
                                                    if (att.id != null && att.id != "" && att.id != 0) {
                                                        map.put(att.id, att);
                                                    }
                                                });

                                                angular.forEach(bomItem.bomChildren, function (item) {
                                                    var childrenAttributes = [];
                                                    var bomChildrenAttributes = vm.selectedBomChildrentAttributesValues[item.id];
                                                    if (bomChildrenAttributes != null && bomChildrenAttributes != undefined) {
                                                        childrenAttributes = childrenAttributes.concat(bomChildrenAttributes);
                                                    }

                                                    bomItemAttr(childrenAttributes, item, map)

                                                });
                                                angular.forEach(bomItem.bomChildren, function (bomItem) {
                                                    var childrenAttributes = [];
                                                    var bomChildrenAttributes = vm.selectedBomChildrentAttributesValues[bomItem.item];
                                                    if (bomChildrenAttributes != null && bomChildrenAttributes != undefined) {
                                                        childrenAttributes = childrenAttributes.concat(bomChildrenAttributes);
                                                    }
                                                    bomItemAttr(childrenAttributes, bomItem, map)

                                                });
                                                angular.forEach(bomItem.bomChildren, function (item) {
                                                    angular.forEach(vm.selectedBomAttributes, function (object) {
                                                        var objectName = object.name;
                                                        if (object.dataType == 'OBJECT') {
                                                            if (item[object.name] == undefined) {
                                                                item[objectName] = object;
                                                            }
                                                        }
                                                    })
                                                })
                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
                                                $rootScope.hideBusyIndicator();
                                            }
                                        );
                                    }
                                    vm.expandedAll = false;
                                    var childCount = 0;
                                    var expandedCount = 0;
                                    angular.forEach(vm.bomItems, function (item) {
                                        if (item.level == 0) {
                                            if (item.hasBom) {
                                                childCount++;
                                                if (!item.expanded) {
                                                    vm.expandedAll = false;
                                                } else {
                                                    expandedCount++;
                                                }
                                            }
                                        }
                                    })
                                    if (childCount == expandedCount) {
                                        vm.expandedAll = true;
                                    }
                                    $timeout(function () {
                                        calculateColumnWidthForSticky();
                                        $rootScope.hideBusyIndicator();
                                    }, 1000);
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    }
                }
            }

            function applyBomRuleForAll() {
                applyBomRule(vm.bomItems);
            }

            function applyBomRule(bomItems) {
                var items = [];
                angular.forEach(bomItems, function (bomItem) {
                    if (vm.selectedBomRule == vm.bomRules.latest.key &&
                        bomItem.item.latestRevision != null) {
                        items.push(bomItem.item)
                    }
                    else if (vm.selectedBomRule == vm.bomRules.latestReleased.key &&
                        bomItem.item.latestRevision != null) {
                        items.push(bomItem.item)
                    }
                    else if (vm.selectedBomRule == vm.bomRules.asReleased.key &&
                        bomItem.asReleasedRevision != null) {
                        items.push(bomItem)
                    }
                });

                if (vm.selectedBomRule == vm.bomRules.latest.key) {
                    ItemService.getRevisionReferences(items, 'latestRevision', function () {
                        updateRevisions(bomItems)
                    });
                }
                else if (vm.selectedBomRule == vm.bomRules.latestReleased.key) {
                    ItemService.getRevisionReferences(items, 'latestReleasedRevision', function () {
                        updateRevisions(bomItems)
                    });
                }
                else if (vm.selectedBomRule == vm.bomRules.asReleased.key) {
                    ItemService.getRevisionReferences(items, 'asReleasedRevision', function () {
                        updateRevisions(bomItems)
                    });
                }
            }

            function removeChildren(bomItem) {
                if (bomItem != null && bomItem.bomChildren != null && bomItem.bomChildren != undefined) {
                    angular.forEach(bomItem.bomChildren, function (item) {
                        removeChildren(item);
                    });

                    var index = vm.bomItems.indexOf(bomItem);
                    vm.bomItems.splice(index + 1, bomItem.bomChildren.length);
                    bomItem.bomChildren = [];
                    bomItem.expanded = false;

                }
            }

            function removeAttribute(att) {
                vm.selectedBomAttributes.remove(att);
                $window.localStorage.setItem("bomAttributes", JSON.stringify(vm.selectedBomAttributes));
            }

            /*--------------------  To Show Large image  --------------------------*/

            function showImage(attribute) {
                var modal = document.getElementById('myModal2');
                var modalImg = document.getElementById('img03');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            /*--------- To Download ATTACHMENT Attribute File  --------------*/

            vm.openAttachment = openAttachment;
            function openAttachment(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/download".
                    format(window.location.protocol, window.location.host,
                    attachment.id);
                $rootScope.downloadFileFromIframe(url);
            }

            function updateRevisions(bomItems) {
                angular.forEach(bomItems, function (bomItem) {
                    bomItem.itemRevision = {
                        revision: '?',
                        lifeCyclePhase: {
                            phase: '?'
                        },
                        hasBom: false
                    };
                    if (vm.selectedBomRule == vm.bomRules.latest.key &&
                        bomItem.item.latestRevisionObject != null &&
                        bomItem.item.latestRevisionObject != undefined) {
                        bomItem.itemRevision = bomItem.item.latestRevisionObject;
                    }
                    else if (vm.selectedBomRule == vm.bomRules.latestReleased.key &&
                        bomItem.item.latestReleasedRevisionObject != null &&
                        bomItem.item.latestReleasedRevisionObject != undefined) {
                        bomItem.itemRevision = bomItem.item.latestReleasedRevisionObject;
                    }
                    else if (vm.selectedBomRule == vm.bomRules.asReleased.key &&
                        bomItem.asReleasedRevisionObject != null &&
                        bomItem.asReleasedRevisionObject != undefined) {
                        bomItem.itemRevision = bomItem.asReleasedRevisionObject;
                    }

                    if (bomItem.itemRevision.hasBom == true) {
                        if (bomItem.bomChildren == undefined || bomItem.bomChildren.length == 0 || bomItem.bomChildren == null)
                            bomItem.bomChildren = [];
                        bomItem.bomChildren.hasbom = false;
                    }
                });
            }

            /*----------------  To Select BOM ITEM Attributes View  --------------------*/

            function showBomAttributes() {
                var selectedAttribute = angular.copy(vm.selectedBomAttributes);
                var options = {
                    title: attributeTitle,
                    template: 'app/desktop/modules/shared/attributes/attributesView.jsp',
                    resolve: 'app/desktop/modules/shared/attributes/attributesController',
                    controller: 'AttributesController as attributesVm',
                    width: 500,
                    showMask: true,
                    data: {
                        selectedAttributes: selectedAttribute,
                        type: "",
                        objectType: "BOMITEM",
                        selectionType: "DISPLAY",
                        bomItem: true
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.select.attributes'}
                    ],
                    callback: function (result) {
                        angular.forEach(result, function (data) {
                            data.editMode = false;
                        })
                        vm.selectedBomAttributes = result;
                        $window.localStorage.setItem("bomAttributes", JSON.stringify(vm.selectedBomAttributes));
                        loadBom();
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesMessage);
                        }
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.disableItems = disableItems;
            function disableItems() {
                if (!$rootScope.itemRevision.released) {
                    angular.forEach(vm.bomItems, function (bomItem) {
                        bomItem.disableEdit = false;
                    })
                }
            }

            function showSelectionDialog(item, objectType, attribute) {
                var objectSelector = $application.getObjectSelector(objectType);
                if (objectSelector != null) {
                    if (objectType == "PERSON" && attribute.value.refValue != null && attribute.value.refValue != "") {
                        $rootScope.personObjectValue = attribute.value.refValue.id;
                    }
                    objectSelector.show($rootScope, attribute, function (object, displayValue) {
                        item[attribute.name].refValue = object;
                        item[attribute.name].ref = object;
                    });
                }
            }

            function showObjectViews(item, object) {
                var options = null;
                var attribute = null;
                angular.forEach(vm.selectedBomAttributes, function (selectedObject) {
                    if (object.name == selectedObject.name) {
                        attribute = item[selectedObject.name];
                    } else if (object.objectType == selectedObject.refType) {
                        attribute = selectedObject;
                    } else if (object.objectType == 'PLMWORKFLOWDEFINITION') {
                        attribute = selectedObject;
                    }
                });
                showSelectionDialog(item, attribute.refType, attribute);
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("bomAttributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            vm.addItem = addItem;
            function addItem(item) {
                $scope.$emit("app.item.addbom", item)
            }

            vm.addMultipleItem = addMultipleItem;
            function addMultipleItem(item) {
                $scope.$emit("app.item.bom.multiple", item)
            }

            vm.bomItemFilePopover = {
                templateUrl: 'app/desktop/modules/item/details/tabs/bom/bomItemFilePopover.jsp'
            };

            vm.substancePopover = {
                templateUrl: 'app/desktop/modules/item/details/tabs/bom/substancePopover.jsp'
            };

            vm.downloadFile = downloadFile;
            function downloadFile(file) {
                var url = "{0}//{1}/api/plm/items/{2}/files/{3}/download".
                    format(window.location.protocol, window.location.host,
                    file.item.id, file.id);
                $rootScope.downloadFileFromIframe(url);
                ItemFileService.updateFileDownloadHistory(file.item.id, file.id).then(
                    function (data) {

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.showThumbnailImage = showThumbnailImage;
            function showThumbnailImage(bomItem) {
                var modal = document.getElementById('item-thumbnail' + bomItem.id + "" + bomItem.item);
                modal.style.display = "block";

                var span = document.getElementById("thumbnail-close" + bomItem.id + "" + bomItem.item);
                $("#thumbnail-image" + bomItem.id + "" + bomItem.item).width($('#thumbnail-view' + bomItem.id + "" + bomItem.item).outerWidth());
                $("#thumbnail-image" + bomItem.id + "" + bomItem.item).height($('#thumbnail-view' + bomItem.id + "" + bomItem.item).outerHeight());

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            vm.showAttributeImage = showAttributeImage;
            function showAttributeImage(bomItem) {
                var modal = document.getElementById('item-thumbnail' + bomItem.id + "" + bomItem.id);
                modal.style.display = "block";

                var span = document.getElementById("thumbnail-close" + bomItem.id + "" + bomItem.id);
                $("#thumbnail-image" + bomItem.id + "" + bomItem.id).width($('#thumbnail-view' + bomItem.id + "" + bomItem.id).outerWidth());
                $("#thumbnail-image" + bomItem.id + "" + bomItem.id).height($('#thumbnail-view' + bomItem.id + "" + bomItem.id).outerHeight());

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            function importData() {
                var fileElem = document.getElementById("importFile");
                file = fileElem.files[0];
                $rootScope.showBusyIndicator($('.view-container'));
                ExImService.getHeadersFromFile(file).then(
                    function (data) {
                        vm.headers = data;
                        mapColumns();
                        $rootScope.hideBusyIndicator();
                    },
                    function (error) {
                        var fileElem = document.getElementById("importFile").value = "";
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function startImport(objs) {

                $rootScope.showBusyIndicator($('.view-container'));
                ItemService.importBom(vm.itemId, file).then(
                    function (data) {
                        $rootScope.showSuccessMessage(bomImported);
                        loadBom();
                        var fileElem = document.getElementById("importFile").value = "";
                        $rootScope.loadItemDetails();
                        $rootScope.hideBusyIndicator();
                    },
                    function (error) {
                        var fileElem = document.getElementById("importFile").value = "";
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            var mappingColumns = parsed.html($translate.instant("MAPPING_COLUMNS")).html();

            function mapColumns() {
                vm.objs = [];
                angular.forEach(vm.headers, function (header) {

                        switch (header) {
                            case "Type Path":
                                vm.objs.push("Item Type");
                                break;
                            case "Item Number":
                                vm.objs.push("Item");
                                break;
                            case "Level":
                                vm.objs.push("Bom");
                                break;
                            case "Manufacturer Type" :
                                vm.objs.push("Manufacturer Type");
                                break;
                            case "Manufacturer" :
                                vm.objs.push("Manufacturer");
                                break;
                            case "Manufacturer Part Type":
                                vm.objs.push("Manufacturer Part Type");
                                break;
                            case "Manufacturer Part Number":
                                vm.objs.push("Manufacturer Part");
                                break;
                            case "Manufacturer Part Item Status":
                                vm.objs.push("Manufacturer Part Item")
                                break;
                        }

                        if (!vm.objs.includes("Item") && header.includes("Item")) {
                            vm.objs.push("Item");
                        }

                        if (!vm.objs.includes("Item Type") && (header.includes("Item Type") || header.includes("ItemType"))) {
                            vm.objs.push("Item Type");
                        }
                    }
                );

                if (vm.headers.length > 2 && vm.objs.length > 1) {
                    var options = {
                        title: mappingColumns,
                        template: 'app/desktop/modules/exim/import/importColumnsMappingView.jsp',
                        controller: 'ImportColumnMapController as mapVm',
                        resolve: 'app/desktop/modules/exim/import/importColumnMapController',
                        width: 650,
                        showMask: true,
                        data: {
                            headers: vm.headers
                        },
                        buttons: [
                            {text: "Import", broadcast: 'app.exim.mapping'}
                        ],
                        callback: function (objs) {
                            startImport(objs);
                        }
                    };
                    $rootScope.showSidePanel(options);
                } else {
                    $rootScope.showErrorMessage(importFileWithProperFormatMessage);
                }
            }


            function exportData() {
                $rootScope.showBusyIndicator($('.view-container'));
                ItemService.exportBom(vm.itemId, true, vm.selectedBomRule).then(
                    function (data) {
                        var exportRows = [];
                        var bomItemHeaders = ["ItemNumber", "ItemType", "ItemName", "Quantity", "Level", "Revision", "LifeCyclePhase", "RefDes", "Notes"];
                        var count = 0;
                        vm.exportItems = [];
                        angular.forEach(data, function (item) {
                            item.level = 0;
                            vm.exportItems.push(item);
                            var index = vm.exportItems.indexOf(item);
                            index = populateExportItemChildren(item, index);
                        });

                        $timeout(function () {
                            angular.forEach(vm.exportItems, function (exprtBom) {
                                var exportRwDetails = [];
                                var ItemNumber = {
                                    columnName: "ItemNumber",
                                    columnValue: null,
                                    columnType: "string"
                                };
                                ItemNumber.columnValue = exprtBom.itemNumber;

                                var ItemType = {
                                    columnName: "ItemType",
                                    columnValue: null,
                                    columnType: "string"
                                };

                                ItemType.columnValue = exprtBom.itemTypeName;

                                var ItemName = {
                                    columnName: "ItemName",
                                    columnValue: null,
                                    columnType: "string"
                                };

                                ItemName.columnValue = exprtBom.itemName;

                                var Quantity = {
                                    columnName: "Quantity",
                                    columnValue: null,
                                    columnType: "string"
                                };

                                Quantity.columnValue = exprtBom.quantity;
                                var Level = {
                                    columnName: "Level",
                                    columnValue: null,
                                    columnType: "string"
                                };

                                Level.columnValue = exprtBom.level;

                                var Revision = {
                                    columnName: "Revision",
                                    columnValue: null,
                                    columnType: "string"
                                };

                                Revision.columnValue = exprtBom.revision;

                                var LifeCyclePhase = {
                                    columnName: "LifeCyclePhase",
                                    columnValue: null,
                                    columnType: "string"
                                };
                                if (exprtBom.lifeCyclePhase != null) {
                                    LifeCyclePhase.columnValue = exprtBom.lifeCyclePhase.phase;
                                }
                                var refDes = {
                                    columnName: "RefDes",
                                    columnValue: null,
                                    columnType: "string"
                                };

                                refDes.columnValue = exprtBom.refdes;
                                var notes = {
                                    columnName: "Notes",
                                    columnValue: null,
                                    columnType: "string"
                                };

                                notes.columnValue = exprtBom.notes;

                                exportRwDetails.push(ItemNumber);
                                exportRwDetails.push(ItemType);
                                exportRwDetails.push(ItemName);
                                exportRwDetails.push(Quantity);
                                exportRwDetails.push(Level);
                                exportRwDetails.push(Revision);
                                exportRwDetails.push(LifeCyclePhase);
                                exportRwDetails.push(refDes);
                                exportRwDetails.push(notes);
                                var exporter = {
                                    exportRowDetails: exportRwDetails
                                };
                                exportRows.push(exporter);
                                count++;
                            });
                            if (count == vm.exportItems.length) {
                                var exportObject = {
                                    "exportRows": exportRows,
                                    "fileName": $rootScope.item.itemNumber + "BomExport",
                                    "headers": bomItemHeaders
                                };

                                CommonService.exportReport("EXCEL", exportObject).then(
                                    function (data) {
                                        var url = "{0}//{1}//api/common/exports/file/".format(window.location.protocol, window.location.host);
                                        url += data + "/download";
                                        $rootScope.downloadFileFromIframe(url);
                                        $rootScope.hideBusyIndicator();
                                    },
                                    function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                        $rootScope.hideBusyIndicator();
                                    }
                                )
                            }
                        }, 1000);
                    }

                    ,
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
                ;
            }

            function populateExportItemChildren(bomItem, lastIndex) {
                angular.forEach(bomItem.children, function (item) {
                    lastIndex++;
                    item.level = bomItem.level + 1;
                    vm.exportItems.splice(lastIndex, 0, item);
                    lastIndex = populateExportItemChildren(item, lastIndex)
                });

                return lastIndex;
            }


            vm.updateBomItemSeq = updateBomItemSeq;
            function updateBomItemSeq(target, actual) {
                if (actual.id != null && actual.id != undefined && target.id != null && target.id != undefined) {
                    $rootScope.showBusyIndicator($('.view-content'));
                    ItemBomService.updateBomItemSeq(0, actual.id, target.id).then(
                        function (data) {
                            vm.itemId = $stateParams.itemId;
                            if (actual.parent == vm.itemId) {
                                if (actual.expanded) {
                                    removeChildren(actual);
                                }
                                if (target.expanded) {
                                    removeChildren(target);
                                }
                                $timeout(function () {
                                    var targetIndex = vm.bomItems.indexOf(target);
                                    var actualIndex = vm.bomItems.indexOf(actual);
                                    vm.bomItems.splice(actualIndex, 1);
                                    vm.bomItems.splice(targetIndex, 0, actual);
                                    $rootScope.hideBusyIndicator();
                                }, 200)
                            } else {
                                var parent = vm.bomItemMap.get(actual.parentBom.id);
                                if (parent != null && parent != undefined) {
                                    parent.expanded = false;
                                    removeChildren(parent);

                                    $timeout(function () {
                                        toggleNode(parent);
                                    }, 500)
                                }
                            }
                            $rootScope.showSuccessMessage(bomSequenceUpdated);
                            //$rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            vm.copiedData = [];
            vm.bomItem = null;
            vm.pasteItemsFromClipboard = pasteItemsFromClipboard;
            function pasteItemsFromClipboard(item, type) {
                $rootScope.showBusyIndicator($('.view-content'));
                var itemId = null;
                if (type == "BOM") {
                    itemId = item.latestRevision;
                    if (!item.expanded) {
                        toggleNode(item);
                    }
                } else {
                    itemId = item.latestRevision;
                }

                $timeout(function () {
                    ItemBomService.pasteClipBoardItemsToBomItem(itemId, vm.copiedItems).then(
                        function (data) {
                            vm.copiedData = data;
                            if (type == "BOM") {
                                item.count = item.count + data.length;
                                var newRowIndex = null;
                                angular.forEach(data, function (bomItem) {
                                    bomItem.level = item.level + 1;
                                    bomItem.editMode = true;
                                    bomItem.isNew = false;
                                    bomItem.newQuantity = bomItem.quantity;
                                    bomItem.newRefdes = bomItem.refdes;
                                    bomItem.newNotes = bomItem.notes;
                                    var index = vm.bomItems.indexOf(item);
                                    if (item.bomChildren == undefined) {
                                        item.bomChildren = [];
                                    }

                                    index = index + getIndexTopInsertNewChild(item) + 1;
                                    item.bomChildren.push(bomItem);
                                    if (bomItem.hasThumbnail) {
                                        bomItem.thumbnailImage = "api/plm/items/" + bomItem.item + "/itemImageAttribute/download?" + new Date().getTime();
                                    }
                                    vm.bomItems.splice(index, 0, bomItem);
                                    if (newRowIndex == null) {
                                        newRowIndex = vm.bomItems.indexOf(bomItem);
                                    }
                                    vm.addedItemsToBom.push(bomItem);
                                    vm.editModeItemsCount++;
                                })
                                vm.bomItem = item;
                                $timeout(function () {
                                    var bomRows = document.getElementById('scrollcontent').getElementsByTagName('tr');
                                    if (newRowIndex > 0) {
                                        $('#scrollcontent').animate({
                                            scrollTop: bomRows[(newRowIndex - 1)].offsetTop
                                        }, 'slow');
                                    }
                                    calculateColumnWidthForSticky();
                                }, 500)
                            } else {
                                var index1 = null;
                                angular.forEach(data, function (bomItem) {
                                    bomItem.level = 0;
                                    bomItem.editMode = true;
                                    bomItem.isNew = false;
                                    bomItem.newQuantity = bomItem.quantity;
                                    bomItem.newRefdes = bomItem.refdes;
                                    bomItem.newNotes = bomItem.notes;
                                    if (bomItem.hasThumbnail) {
                                        bomItem.thumbnailImage = "api/plm/items/" + bomItem.item + "/itemImageAttribute/download?" + new Date().getTime();
                                    }
                                    vm.bomItems.push(bomItem);
                                    vm.addedItemsToBom.push(bomItem);
                                    vm.editModeItemsCount++;
                                    if (index1 == null) {
                                        index1 = vm.bomItems.indexOf(bomItem);
                                    }
                                })
                                $timeout(function () {
                                    var rows = document.getElementById('scrollcontent').getElementsByTagName('tr');
                                    if (index1 > 0) {
                                        $('#scrollcontent').animate({
                                            scrollTop: rows[(index1 - 1)].offsetTop
                                        }, 'slow');
                                    }
                                    calculateColumnWidthForSticky();
                                }, 500)
                            }
                            $rootScope.loadItemDetails();
                            var clipboardItemsCount = $application.clipboard.items.length;
                            if ((clipboardItemsCount - vm.copiedData.length) > 0) {
                                $rootScope.showSuccessMessage("[ " + vm.copiedData.length + " ] " + itemsAddedToBomSuccessfullyAnd + "[ " + (clipboardItemsCount - vm.copiedData.length) + " ] Item(s) already exist", true, "BOM");
                            } else {
                                $rootScope.showSuccessMessage("[ " + vm.copiedData.length + " ] " + itemsAddedToBomSuccessfully, true, "BOM");
                            }
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }, 500)
            }

            $rootScope.undoCopiedBomItems = undoCopiedBomItems;
            function undoCopiedBomItems() {
                if (vm.copiedData.length > 0) {
                    $rootScope.closeNotification();
                    $rootScope.showBusyIndicator($(".view-content"));
                    ItemBomService.undoCopiedItems(vm.itemId, vm.copiedData).then(
                        function (data) {
                            if (vm.copiedData.length > 0) {
                                if (vm.bomItem != null) {
                                    if (vm.bomItem.expanded) {
                                        removeChildren(vm.bomItem);

                                        $timeout(function () {
                                            toggleNode(vm.bomItem);
                                        }, 200)
                                    } else {
                                        toggleNode(vm.bomItem);
                                    }
                                    angular.forEach(vm.copiedData, function (file) {
                                        vm.addedItemsToBom.splice(vm.addedItemsToBom.indexOf(file), 1);
                                        vm.editModeItemsCount--;
                                    })
                                } else {
                                    angular.forEach(vm.copiedData, function (file) {
                                        vm.bomItems.splice(vm.bomItems.indexOf(file), 1);
                                        vm.addedItemsToBom.splice(vm.addedItemsToBom.indexOf(file), 1);
                                        vm.editModeItemsCount--;
                                    })
                                }
                                $rootScope.loadItemDetails();
                                $rootScope.showSuccessMessage(undoSuccessful);
                                $rootScope.hideBusyIndicator();
                            } else {
                                $rootScope.hideBusyIndicator();
                            }
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            var emptyConfiguration = {
                id: null,
                name: null,
                item: null,
                description: null,
                rules: null,
                bomModalDto: null
            };

            vm.hasError = false;
            vm.message = "";

            var errorNotification = null;
            $rootScope.showBomConfiguration = showBomConfiguration;
            $rootScope.openBomConfiguration = openBomConfiguration;

            function showBomConfiguration() {
                loadBomModal();
            }

            vm.bomConfig = [];
            vm.resolvedBom = false;
            function openBomConfiguration(config) {
                if (config != null) {
                    $rootScope.showBusyIndicator($(".view-container"));
                    ItemBomService.getBomConfigItemWithValues(config.id).then(
                        function (data) {
                            setBomDetails(data);
                            vm.resolvedBom = true;
                            setBomDetails(data);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                } else {
                    $rootScope.showBusyIndicator($(".view-container"));
                    vm.resolvedBom = false;
                    loadBom();
                }
            }

            vm.resolveSavedBomConfig = resolveSavedBomConfig;
            vm.resolveModel = null;
            function resolveSavedBomConfig() {
                ItemBomService.resolveSelectedBomConfig().then(
                    function (data) {
                        data.level = 0;
                        vm.resolveModel = data;
                        vm.showConfig = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }


            vm.showConfig = true;
            vm.notificationClass = "fa-warning";
            vm.notificationBackground = "alert-warning";
            vm.modalBom = [];
            vm.bomConfigurations = [];
            vm.loadBomModal = loadBomModal;
            vm.selectedBomConfiguration = null;
            vm.openedBomConfiguration = false;
            vm.openedBomRollUpReport = false;
            vm.openedBomRollWhereUsedReport = false;
            vm.openedBomConfigurationEditor = false;
            function loadBomModal() {
                vm.modalBom = [];
                vm.showClear = false;
                vm.selectedBomConfiguration = null;
                $rootScope.showBusyIndicator($('.view-container'));
                $rootScope.loadItemForBomRules();
                ItemBomService.getItemBomConfigurations(vm.itemId).then(
                    function (data) {
                        vm.bomConfigurations = data;
                        ItemBomService.getBomModal(vm.itemId).then(
                            function (data) {
                                data.level = 0;
                                vm.modalBom.push(data);
                                vm.bomConfiguration = angular.copy(emptyConfiguration);
                                vm.bomConfiguration.id = null;
                                vm.bomConfiguration.name = data.item.itemName + " - ";
                                vm.bomConfiguration.description = data.item.itemName;
                                $rootScope.hideBusyIndicator();
                                var modal = document.getElementById("bom-configuration");
                                modal.style.display = "block";
                                var headerHeight = $('.bom-header').outerHeight();
                                var footerHeight = $('.bom-footer').outerHeight();
                                var bomContentHeight = $('.bomModel-content').outerHeight();
                                $("#bom-configuration .bom-content").height(bomContentHeight - (headerHeight + footerHeight) - 20);
                                errorNotification = $('#configuration-error');
                                vm.openedBomConfiguration = true;
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.onSelectBomConfiguration = onSelectBomConfiguration;
            function onSelectBomConfiguration(configuration) {
                if (configuration != null) {
                    vm.bomConfiguration = configuration;
                    ItemBomService.getBomConfigurationModal(vm.itemId, configuration.id).then(
                        function (data) {
                            vm.modalBom = [];
                            data.level = 0;
                            vm.modalBom.push(data);
                            angular.forEach(data.children, function (child) {
                                child.level = data.level + 1;
                                vm.modalBom.push(child);
                            });
                            loadSelectedBomConfigValues(vm.modalBom[0]);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                } else {
                    loadBomConfigurationModal();
                }
            }

            function loadSelectedBomConfigValues(modal) {
                ItemBomService.getBomInclusionRules(modal.item.latestRevision, modal).then(
                    function (data) {
                        vm.modalBom = [];
                        data.level = 0;
                        vm.modalBom.push(data);
                        angular.forEach(data.children, function (child) {
                            child.level = data.level + 1;
                            vm.modalBom.push(child);
                        });
                        $rootScope.hideBusyIndicator();
                        vm.loading = false;
                        $timeout(function () {
                            var bomContentH1 = $('.configurationEditor-content').outerHeight();
                            var bomContentWidth = $('.configurationEditor-content').outerWidth();
                            var editorLeftWidth = $('.editor-left').outerWidth();
                            $(".editor-content").height(bomContentH1 - 50);
                            $(".editor-content .editor-right").width(bomContentWidth - 300);
                            $(".editor-content .editor-right").height(bomContentH1 - 50);
                            var bomContentH2 = $('.editor-right').outerHeight();
                            $(".editor-configuration-content").height(bomContentH2 - 50);
                            $(".editor-configuration-content").width(bomContentWidth - 300);
                        }, 100)
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            $rootScope.resolveBom = resolveBom;
            function resolveBom() {
                $rootScope.showBusyIndicator($('.view-container'));
                ItemBomService.resolveItemBom(vm.itemId).then(
                    function (data) {
                        $rootScope.showResolveBomButton = false;
                        loadBom();
                        var resolved = 0;
                        var unResolved = 0;
                        angular.forEach(data, function (item) {
                            if (item.configured) {
                                resolved++;
                            } else {
                                unResolved++;
                            }
                        })
                        $rootScope.showSuccessMessage(" [ " + resolved + " ] " + bomItemsResolved + " " + and + " [ " + unResolved + "] " + bomItemsNotResolved);
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.resolveBomItem = resolveBomItem;
            function resolveBomItem(bomItem) {
                $rootScope.showBusyIndicator($('.view-content'));
                ItemBomService.resolveBomItemInstance(vm.itemId, bomItem.id).then(
                    function (data) {
                        if (data.item.configured) {
                            bomItem.id = data.id;
                            bomItem.item = data.item;
                            ItemService.getRevisionId(bomItem.item.latestRevision).then(
                                function (data) {
                                    bomItem.itemRevision = data;
                                    if (bomItem.expanded) {
                                        removeChildren(bomItem);
                                        bomItem.expanded = false;
                                        $timeout(function () {
                                            toggleNode(bomItem);
                                        }, 500)
                                    }
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                            $rootScope.showSuccessMessage(bomItemResolved);
                        } else {
                            $rootScope.showWarningMessage(bomItemCannotResolve);
                        }
                        $rootScope.showResolveBomButton = false;
                        angular.forEach(vm.bomItems, function (bomItem) {
                            if (bomItem.item.configurable) {
                                $rootScope.showResolveBomButton = true;
                            }
                        })
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }


            $scope.resizeBomConfigurationView = resizeBomConfigurationView;
            function resizeBomConfigurationView() {
                $timeout(function () {

                    if (vm.openedBomRollUpReport) {
                        var rollupModal = document.getElementById("bom-rollup");
                        if (rollupModal != null) {
                            var headerHeight1 = $('.bom-header').outerHeight();
                            var bomContentHeight1 = $('.bomRollup-content').outerHeight();
                            $("#bom-rollup .bom-content").height(bomContentHeight1 - headerHeight1);
                        }
                    }

                    if (vm.openedBomRollWhereUsedReport) {
                        var whereUsedModal = document.getElementById("bom-whereUsedReport");
                        if (whereUsedModal != null) {
                            var headerHeight2 = $('.bom-header').outerHeight();
                            var bomContentHeight2 = $('.bomWhereUsed-content').outerHeight();
                            $("#bom-whereUsedReport .bom-content").height(bomContentHeight2 - headerHeight2);
                            var itemSelection = $('#item-selection').outerHeight();
                            var h1 = bomContentHeight2 - (headerHeight2 + itemSelection);
                            $("#whereUsed-height").height(h1);
                        }
                    }
                    if (vm.openedBomComplianceReport) {
                        var whereUsedModal = document.getElementById("bom-complianceReport");
                        if (whereUsedModal != null) {
                            var headerHeight2 = $('.bom-header').outerHeight();
                            var bomContentHeight2 = $('.bomCompliance-content').outerHeight();
                            $("#bom-complianceReport .bom-content").height(bomContentHeight2 - headerHeight2);
                            var h1 = bomContentHeight2 - (headerHeight2);
                            $("#compliance-height").height(h1);
                        }
                    }
                    if (vm.openedBomConfigurationEditor) {
                        var configurationEditor = document.getElementById("configuration-editor");
                        if (configurationEditor != null) {
                            var bomContentH1 = $('.configurationEditor-content').outerHeight();
                            var bomContentWidth = $('.editor-content').outerWidth();
                            var editorLeftWidth = $('.editor-left').outerWidth();
                            $(".editor-content").height(bomContentH1 - 50);
                            $(".editor-content .editor-right").width(bomContentWidth - 300);
                            $(".editor-content .editor-right").height(bomContentH1 - 50);
                            var bomContentH2 = $('.editor-right').outerHeight();
                            $(".editor-configuration-content").height(bomContentH2 - 50);
                            $(".editor-configuration-content").width(bomContentWidth - 300);
                            $(".bom-footer").width(bomContentWidth - 300);

                            var inclusionButtons = $('#inclusion-buttons').outerHeight();
                            $('#inclusions-content').height(bomContentH2 - (inclusionButtons + 25));
                            $("#inclusions-content").width(bomContentWidth - 300);

                            var nonConfigButtons = $('#non-configurable-buttons').outerHeight();
                            $('#non-configurable-content').height(bomContentH2 - (nonConfigButtons + 25));
                            $("#non-configurable-content").width(bomContentWidth - editorLeftWidth);
                        }
                    }
                }, 50)
            }

            vm.getBomValidationRules = getBomValidationRules;
            function getBomValidationRules(modal) {
                if (modal.item.latestRevision == vm.itemId) {
                    getBomInclusionRules(modal);
                } else {
                    getBomItemToItemInclusions(modal);
                }
                vm.showClear = true;
            }

            vm.showClear = false;
            vm.getBomInclusionRules = getBomInclusionRules;
            function getBomInclusionRules(modal) {
                $rootScope.showBusyIndicator();
                ItemBomService.getBomInclusionRules(modal.item.latestRevision, modal).then(
                    function (data) {
                        vm.modalBom = [];
                        data.level = 0;
                        vm.modalBom.push(data);
                        if (validateBomItem(vm.modalBom[0])) {
                            vm.showClear = true;
                            angular.forEach(data.children, function (child) {
                                child.level = data.level + 1;
                                vm.modalBom.push(child);
                            })
                        }
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.getBomItemToItemInclusions = getBomItemToItemInclusions;
            function getBomItemToItemInclusions(modal) {
                $rootScope.showBusyIndicator();
                ItemBomService.getBomItemToItemInclusions(vm.itemId, vm.modalBom[0]).then(
                    function (data) {
                        vm.modalBom = [];
                        data.level = 0;
                        vm.modalBom.push(data);
                        angular.forEach(data.children, function (child) {
                            child.level = data.level + 1;
                            vm.modalBom.push(child);
                        })
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function validateBomItem(modal) {
                var valid = true;

                if (modal.item.latestRevision != vm.itemId) {
                    valid = false;
                } else if (modal.attributes.length == 0) {
                    valid = false;
                } else {
                    angular.forEach(modal.attributes, function (attribute) {
                        if (valid) {
                            if (attribute.itemAttribute.attribute.configurable && (attribute.listValue == null || attribute.listValue == "" || attribute.listValue == undefined)) {
                                valid = false;
                            }
                        }
                    })
                }

                return valid;
            }

            $rootScope.hideBomConfiguration = hideBomConfiguration;
            function hideBomConfiguration() {
                var configModal = document.getElementById("bom-configuration");
                var modal = document.getElementById("bomConfig-details");
                if (configModal != null) {
                    configModal.style.display = "none";
                }

                if (modal != null) {
                    modal.style.display = "none";
                }
                vm.openedBomConfiguration = false;
                vm.openedBomRollUpReport = false;
                vm.openedBomRollWhereUsedReport = false;
                vm.openedBomConfigurationEditor = false;
            }

            vm.saveBomConfiguration = saveBomConfiguration;
            function saveBomConfiguration() {
                if (validateBomConfiguration()) {
                    $rootScope.showBusyIndicator();
                    vm.bomConfiguration.item = vm.itemId;
                    var configModal = {
                        id: null,
                        children: []
                    }
                    var parentItem = angular.copy(configModal);
                    parentItem.id = vm.modalBom[0].item.latestRevision;
                    angular.forEach(vm.modalBom[0].attributes, function (attribute) {
                        parentItem[attribute.itemAttribute.attribute.name] = attribute.listValue;
                    })

                    angular.forEach(vm.modalBom[0].children, function (bom) {
                        var newModal = angular.copy(configModal);
                        newModal.id = bom.item.id;
                        angular.forEach(bom.attributes, function (attribute) {
                            newModal[attribute.itemAttribute.attribute.name] = attribute.listValue;
                        })
                        parentItem.children.push(newModal);
                    })

                    vm.bomConfiguration.rules = JSON.stringify(parentItem);
                    vm.bomConfiguration.bomModalDto = vm.modalBom[0];
                    ItemBomService.createBomConfiguration(vm.itemId, vm.bomConfiguration).then(
                        function (data) {
                            vm.showClear = false;
                            var configurations = null;
                            if (vm.bomItemsForExclusion.length > 0 && vm.normalBomItems.length > 0) {
                                configurations = configurationEditorTree.tree('find', 5);
                            } else {
                                configurations = configurationEditorTree.tree('find', 4);
                            }
                            var configurationExist = false;
                            configurations.target.className = "tree-node";
                            $('#configurationEditor').tree('expandAll');
                            angular.forEach(configurations.children, function (config) {
                                if (config.object.id == data.id) {
                                    configurationExist = true;
                                    var configuration = configurationEditorTree.tree('find', config.id);
                                    configurationEditorTree.tree('update', {
                                        target: configuration.target,
                                        text: data.name
                                    });
                                    onSelectBomConfiguration(data);
                                }
                            })
                            if (!configurationExist) {
                                var node = makeNode(data);

                                configurations.children.push(node);

                                configurationEditorTree.tree('append', {
                                    parent: configurations.target,
                                    data: node
                                });
                                if (vm.selectConfigEditorNode.nodeType != "NEW_BOM_CONFIGURATION") {
                                    vm.selectConfigEditorNode.target.className = "tree-node";
                                }
                                var configuration = configurationEditorTree.tree('find', node.id);
                                configuration.target.className = "tree-node tree-node-hover tree-node-selected";
                                onSelectType(configuration);
                            }
                            //loadBomConfigurationModal();
                            $rootScope.loadBomConfigurations();
                            $rootScope.hideBusyIndicator();
                            vm.notificationClass = "fa-check";
                            vm.notificationBackground = "alert-success";
                            vm.message = bomConfigurationSaved;
                            errorNotification.show();
                            errorNotification.removeClass('zoomOut');
                            errorNotification.addClass('zoomIn');
                            $timeout(function () {
                                closeErrorNotification();
                            }, 3000);
                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                            vm.notificationClass = "fa-ban";
                            vm.notificationBackground = "alert-danger";
                            vm.message = error.message;
                            errorNotification.show();
                            errorNotification.removeClass('zoomOut');
                            errorNotification.addClass('zoomIn');
                            $timeout(function () {
                                closeErrorNotification();
                            }, 5000)
                        }
                    )
                }
            }

            function validateBomConfiguration() {
                var valid = true;

                if (!validateAttributes()) {
                    vm.hasError = true;
                    valid = false;
                } else if (vm.bomConfiguration.name == "" || vm.bomConfiguration.name == null || vm.bomConfiguration.name == undefined) {
                    valid = false;
                    errorNotification.show();
                    errorNotification.removeClass('zoomOut');
                    errorNotification.addClass('zoomIn');
                    vm.notificationClass = "fa-warning";
                    vm.notificationBackground = "alert-warning";
                    vm.message = pleaseEnterName;
                }

                if (!valid) {
                    $timeout(function () {
                        closeErrorNotification();
                    }, 3000)
                }

                return valid;
            }

            function validateAttributes() {
                var valid = true;

                angular.forEach(vm.modalBom, function (modal) {
                    if (valid) {
                        angular.forEach(modal.attributes, function (attribute) {
                            if (valid) {
                                if (attribute.itemAttribute.attribute.configurable && (attribute.listValue == null || attribute.listValue == "" || attribute.listValue == undefined)) {
                                    valid = false;
                                    errorNotification.show();
                                    errorNotification.removeClass('zoomOut');
                                    errorNotification.addClass('zoomIn');
                                    vm.notificationClass = "fa-warning";
                                    vm.notificationBackground = "alert-warning";
                                    vm.message = pleaseSelect + " [ " + modal.item.itemName + " ] " + attribute.itemAttribute.attribute.name;
                                }
                            }
                        })

                        if (modal.attributes.length == 0) {
                            valid = false;
                            errorNotification.show();
                            errorNotification.removeClass('zoomOut');
                            errorNotification.addClass('zoomIn');
                            vm.notificationClass = "fa-warning";
                            vm.notificationBackground = "alert-warning";
                            vm.message = noConfigurableAttributeValidation + " " + modal.item.itemName;
                        }
                    }
                });

                if (!valid) {
                    $timeout(function () {
                        closeErrorNotification();
                    }, 3000)
                }

                return valid;
            }

            vm.closeErrorNotification = closeErrorNotification;
            function closeErrorNotification() {
                errorNotification.removeClass('zoomIn');
                errorNotification.addClass('zoomOut');
                errorNotification.hide();
            }

            function updateBomItemSequence() {
                ItemService.updateBomItemSequence(vm.itemId).then(
                    function (data) {
                        loadItem();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.selectedBomRollUpAttributes = [];
            vm.rollupAttributeIds = [];
            $rootScope.showBomRollupAttributes = showBomRollupAttributes;
            function showBomRollupAttributes() {
                var options = {
                    title: selectRollupAttributesTitle,
                    template: 'app/desktop/modules/shared/attributes/attributesView.jsp',
                    resolve: 'app/desktop/modules/shared/attributes/attributesController',
                    controller: 'AttributesController as attributesVm',
                    width: 600,
                    showMask: true,
                    data: {
                        type: "",
                        objectType: "ITEMTYPE",
                        selectionType: "ROLLUP"
                    },
                    buttons: [
                        {text: submitButton, broadcast: 'add.select.attributes'}
                    ],
                    callback: function (result) {
                        vm.selectedBomRollUpAttributes = result;
                        vm.rollupAttributeIds = [];
                        angular.forEach(vm.selectedBomRollUpAttributes, function (attribute) {
                            vm.rollupAttributeIds.push(attribute.id);
                        })
                        vm.bomRollupItems = [];
                        $rootScope.showBusyIndicator($('.view-container'));
                        ItemBomService.getItemBomRollUpReport(vm.itemId, vm.selectedBomRollUpAttributes).then(
                            function (data) {
                                $scope.bollupLimit = 30;
                                vm.bomRollUpReport = data;
                                vm.selectedBomRollUpAttributes = vm.bomRollUpReport.bomAttributes;
                                vm.bomRollupItems.push(vm.bomRollUpReport.bomItems[0]);
                                var index = vm.bomRollupItems.indexOf(vm.bomRollUpReport.bomItems[0]);
                                index = populateBomItemChildren(vm.bomRollUpReport.bomItems[0], index);
                                showBomRollupReport();
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function populateBomItemChildren(reportBomItem, lastIndex) {
                reportBomItem.count = 0;
                reportBomItem.bomChildren = [];
                angular.forEach(reportBomItem.children, function (reportItem) {
                    lastIndex++;
                    reportItem.bomItem.parentBom = reportBomItem.bomItem;
                    reportItem.expanded = true;
                    reportItem.level = reportBomItem.level + 1;
                    reportItem.count = 0;
                    reportItem.bomChildren = [];
                    vm.bomRollupItems.splice(lastIndex, 0, reportItem);
                    reportBomItem.count = reportBomItem.count + 1;
                    reportBomItem.bomChildren.push(reportItem);
                    lastIndex = populateBomItemChildren(reportItem, lastIndex)
                });

                return lastIndex;
            }

            function addspace(level, str) {
                str = new Array(level + 1).join('   ') + str;
                return str;

            }

            vm.exportWhereUsedReport = exportWhereUsedReport;
            function exportWhereUsedReport(type) {
                $rootScope.showBusyIndicator();
                var bomRollUpHeaders = ["ItemNumber", "ItemType", "ItemName", "Description", "Level", "Revision", "LifeCyclePhase", "Qty", "Units"];
                var exportRows = [];
                var fileName = $rootScope.item.itemNumber + "_BOMWhereUsedReport";
                var i = 0;
                angular.forEach(vm.bomWhereUsedItems, function (bomItem) {
                    var exportRwDetails = [];
                    var itemNumber = {
                        columnName: "ItemNumber",
                        columnValue: null,
                        columnType: "string"
                    };
                    itemNumber.columnValue = addspace(bomItem.level, bomItem.itemNumber);
                    var itemType = {
                        columnName: "ItemType",
                        columnValue: null,
                        columnType: "string"
                    };
                    itemType.columnValue = bomItem.itemTypeName;
                    var itemName = {
                        columnName: "ItemName",
                        columnValue: null,
                        columnType: "string"
                    };
                    itemName.columnValue = bomItem.itemName;
                    var description = {
                        columnName: "Description",
                        columnValue: null,
                        columnType: "string"
                    };
                    description.columnValue = bomItem.description;
                    var level = {
                        columnName: "Level",
                        columnValue: null,
                        columnType: "string"
                    };
                    level.columnValue = bomItem.level;
                    var revision = {
                        columnName: "Revision",
                        columnValue: null,
                        columnType: "string"
                    };
                    revision.columnValue = bomItem.revision;
                    var lifeCyclePhase = {
                        columnName: "LifeCyclePhase",
                        columnValue: null,
                        columnType: "string"
                    };
                    lifeCyclePhase.columnValue = bomItem.lifeCyclePhase.phase;
                    var quantity = {
                        columnName: "Qty",
                        columnValue: null,
                        columnType: "string"
                    };
                    quantity.columnValue = bomItem.quantity;
                    var units = {
                        columnName: "Units",
                        columnValue: null,
                        columnType: "string"
                    };
                    units.columnValue = bomItem.units;
                    exportRwDetails.push(itemNumber);
                    exportRwDetails.push(itemType);
                    exportRwDetails.push(itemName);
                    exportRwDetails.push(description);
                    exportRwDetails.push(level);
                    exportRwDetails.push(revision);
                    exportRwDetails.push(lifeCyclePhase);
                    exportRwDetails.push(quantity);
                    exportRwDetails.push(units);
                    var exporter = {
                        exportRowDetails: exportRwDetails
                    };
                    exportRows.push(exporter);
                    i++;
                });
                var exportObject = {
                    "exportRows": exportRows,
                    "fileName": fileName,
                    "headers": bomRollUpHeaders
                };
                if (i == vm.bomWhereUsedItems.length) {
                    CommonService.exportReport(type, exportObject).then(
                        function (data) {
                            var url = "{0}//{1}//api/common/exports/file/".format(window.location.protocol, window.location.host);
                            url += data + "/download";
                            $rootScope.downloadFileFromIframe(url);
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        })
                }

            }

            vm.exportRollUpReport = exportRollUpReport;
            function exportRollUpReport(type) {
                $rootScope.showBusyIndicator();
                populateHeaders();
                var bomRollUpHeaders = ["ItemNumber", "ItemType", "ItemName", "Description", "Level", "Revision", "LifeCyclePhase", "Qty", "Units"];
                bomRollUpHeaders = bomRollUpHeaders.concat(dynamicColumns);
                var exportRows = [];
                var fileName = $rootScope.item.itemNumber + "_BOMRollupReport";
                var i = 0;
                angular.forEach(vm.bomRollupItems, function (reportItem) {
                    var exportRwDetails = [];
                    var exportRwDetails1 = [];
                    var itemNumber = {
                        columnName: "ItemNumber",
                        columnValue: null,
                        columnType: "string"
                    };
                    itemNumber.columnValue = addspace(reportItem.bomItem.level, reportItem.bomItem.itemNumber);
                    var itemType = {
                        columnName: "ItemType",
                        columnValue: null,
                        columnType: "string"
                    };
                    itemType.columnValue = reportItem.bomItem.itemTypeName;
                    var itemName = {
                        columnName: "ItemName",
                        columnValue: null,
                        columnType: "string"
                    };
                    itemName.columnValue = reportItem.bomItem.itemName;
                    var description = {
                        columnName: "Description",
                        columnValue: null,
                        columnType: "string"
                    };
                    description.columnValue = reportItem.bomItem.description;
                    var level = {
                        columnName: "Level",
                        columnValue: null,
                        columnType: "string"
                    };
                    level.columnValue = reportItem.bomItem.level;
                    var revision = {
                        columnName: "Revision",
                        columnValue: null,
                        columnType: "string"
                    };
                    revision.columnValue = reportItem.bomItem.revision;
                    var lifeCyclePhase = {
                        columnName: "LifeCyclePhase",
                        columnValue: null,
                        columnType: "string"
                    };
                    lifeCyclePhase.columnValue = reportItem.bomItem.lifeCyclePhase.phase;
                    var quantity = {
                        columnName: "Qty",
                        columnValue: null,
                        columnType: "string"
                    };
                    quantity.columnValue = reportItem.bomItem.quantity;
                    var units = {
                        columnName: "Units",
                        columnValue: null,
                        columnType: "string"
                    };
                    units.columnValue = reportItem.bomItem.units;
                    exportRwDetails.push(itemNumber);
                    exportRwDetails.push(itemType);
                    exportRwDetails.push(itemName);
                    exportRwDetails.push(description);
                    exportRwDetails.push(level);
                    exportRwDetails.push(revision);
                    exportRwDetails.push(lifeCyclePhase);
                    exportRwDetails.push(quantity);
                    exportRwDetails.push(units);
                    var rowHeaders = dynamicHeaders[i];
                    angular.forEach(rowHeaders.exportRowDetails, function (header) {
                        exportRwDetails.push(header);
                    });
                    i++;
                    var exporter = {
                        exportRowDetails: exportRwDetails
                    };
                    exportRows.push(exporter);
                });
                var exportObject = {
                    "exportRows": exportRows,
                    "fileName": fileName,
                    "headers": bomRollUpHeaders
                };
                if (i == vm.bomRollupItems.length) {
                    CommonService.exportReport(type, exportObject).then(
                        function (data) {
                            var url = "{0}//{1}//api/common/exports/file/".format(window.location.protocol, window.location.host);
                            url += data + "/download";
                            $rootScope.hideBusyIndicator();
                            $rootScope.downloadFileFromIframe(url);
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        })
                }

            }

            var dynamicColumns = [];
            var dynamicHeaders = [];

            function populateHeaders() {
                dynamicColumns = [];
                dynamicHeaders = [];
                angular.forEach(vm.selectedBomRollUpAttributes, function (selectedAttribute) {
                    var index = vm.selectedBomRollUpAttributes.indexOf(selectedAttribute);
                    if (index % 2 == 0) {
                        var column = $scope.unitTitle + selectedAttribute.name;
                        if (selectedAttribute.measurementUnit != null) {
                            column = column + "( " + selectedAttribute.measurementUnit.symbol + " )";
                        }
                        dynamicColumns.push(column);
                    } else {
                        var column = $scope.totalTitle + selectedAttribute.name;
                        if (selectedAttribute.measurementUnit != null) {
                            column = column + "( " + selectedAttribute.measurementUnit.symbol + " )";
                        }
                        dynamicColumns.push(column);
                    }
                });
                angular.forEach(vm.bomRollupItems, function (reportItem) {
                    var dynamicHeaders1 = [];
                    angular.forEach(reportItem.bomRollUpAttributes, function (attribute) {
                        var header = {
                            columnName: $scope.unitTitle + attribute.name,
                            columnValue: null,
                            columnType: "string"
                        };
                        var index = reportItem.bomRollUpAttributes.indexOf(attribute);
                        if (index % 2 == 0) {
                            if (attribute.rollUpValue > 0 || attribute.approximated) {
                                if (reportItem.bomItem.hasBom) {
                                    header.columnValue = attribute.rollUpValue + attribute.unitSymbol;
                                }
                                if (!reportItem.bomItem.hasBom) {
                                    header.columnValue = attribute.actualValue + attribute.unitSymbol;
                                }
                                if (reportItem.bomItem.hasBom && attribute.rollUpValue == 0 && attribute.approximated) {
                                    header.columnValue = '~';
                                }

                            }
                            if (attribute.measurementUnit != null) {
                                header.columnName = header.columnName + "( " + attribute.unitSymbol + " )";
                            }
                            // dynamicHeaders.push(header);
                        } else {
                            if (attribute.multipliedValue > 0 || attribute.approximated) {
                                if (reportItem.bomItem.hasBom) {
                                    header.columnValue = attribute.multipliedValue + attribute.unitSymbol;
                                }
                                if (!reportItem.bomItem.hasBom) {
                                    header.columnValue = attribute.actualMultipliedValue + attribute.unitSymbol;
                                }
                                if (reportItem.bomItem.hasBom && attribute.multipliedValue == 0 && attribute.approximated) {
                                    header.columnValue = '~';
                                }
                            }
                            if (attribute.measurementUnit != null) {
                                header.columnName = header.columnName + "( " + attribute.unitSymbol + " )";
                            }


                        }
                        dynamicHeaders1.push(header);
                    })
                    var exporter = {
                        exportRowDetails: dynamicHeaders1
                    };
                    dynamicHeaders.push(exporter);

                })

            }

            vm.toggleRollUpItem = toggleRollUpItem;
            function toggleRollUpItem(reportItem) {
                reportItem.expanded = !reportItem.expanded;
                var index = vm.bomRollupItems.indexOf(reportItem);
                if (reportItem.expanded == false) {
                    removeChildren(reportItem);
                    $rootScope.hideBusyIndicator();
                }
                else {
                    ItemBomService.getBomRollupItemChildren(reportItem.bomItem.id, vm.rollupAttributeIds).then(
                        function (data) {
                            angular.forEach(data.bomItems, function (item) {
                                item.bomItem.parentBom = reportItem.bomItem;
                                item.expanded = true;
                                item.level = reportItem.level + 1;
                                item.count = 0;
                                item.bomChildren = [];
                                reportItem.bomChildren.push(item);
                            });

                            angular.forEach(reportItem.bomChildren, function (item) {
                                index = index + 1;
                                vm.bomRollupItems.splice(index, 0, item);
                            });
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function removeRollupItemChildren(reportItem) {
                if (reportItem != null && reportItem.bomChildren != null && reportItem.bomChildren != undefined) {
                    angular.forEach(reportItem.bomChildren, function (item) {
                        removeRollupItemChildren(item);
                    });

                    var index = vm.bomRollupItems.indexOf(reportItem);
                    vm.bomRollupItems.splice(index + 1, reportItem.bomChildren.length);
                    reportItem.bomChildren = [];
                    reportItem.expanded = false;

                }
            }

            $scope.showBomRollupReport = showBomRollupReport;
            function showBomRollupReport() {
                var modal = document.getElementById("bom-rollup");
                modal.style.display = "block";
                $timeout(function () {
                    var headerHeight = $('.bom-header').outerHeight();
                    var bomContentHeight = $('.bomRollup-content').outerHeight();
                    $(".bom-content").height(bomContentHeight - headerHeight);
                    vm.openedBomRollUpReport = true;
                    $timeout(function () {
                        rollupReportSticky();
                    }, 500);
                }, 200)

            }

            $scope.hideBomRollup = hideBomRollup;
            function hideBomRollup() {
                var modal = document.getElementById("bom-rollup");
                modal.style.display = "none";
            }

            vm.bomWhereUsedItems = [];
            $rootScope.bomTreeSearchItems = [];
            vm.selectedWhereUsedItems = [];
            vm.whereUsedTableView = true;
            vm.whereUsedGraphicalView = false;
            $rootScope.loadBomWhereUsedReport = loadBomWhereUsedReport;
            function loadBomWhereUsedReport() {
                $rootScope.showBusyIndicator($('.view-container'));
                ItemBomService.getTotalBom(vm.itemId, true, "bom.latest").then(
                    function (data) {
                        vm.bomWhereUsedReportData = data;
                        var bomDataDto = {
                            number: vm.item.itemNumber,
                            name: vm.item.itemName,
                            itemId: vm.itemId,
                            children: null
                        }
                        var bomDto = {
                            id: null,
                            itemName: vm.item.itemName,
                            itemNumber: vm.item.itemNumber,
                            itemTypeName: vm.item.itemType.name,
                            description: vm.item.description,
                            revision: vm.itemRevision.revision,
                            lifeCyclePhase: vm.itemRevision.lifeCyclePhase,
                            units: vm.item.units,
                            quantity: null,
                            level: 0
                        }
                        vm.bomWhereUsedItems = [];
                        vm.bomWhereUsedItems.push(bomDto);
                        vm.whereUsedItemMap = new Hashtable();
                        vm.whereUsedLastItems = [];
                        angular.forEach(data, function (item) {
                            item.bomChildren = [];
                            item.level = 1;
                            if (bomDataDto.children == null) {
                                bomDataDto.children = [];
                            }
                            var dataDto = {
                                number: null,
                                name: null,
                                itemId: null,
                                children: null
                            }
                            dataDto.number = item.itemNumber;
                            dataDto.name = item.itemName;
                            dataDto.itemId = item.item;
                            bomDataDto.children.push(dataDto);
                            if (!item.hasBom) {
                                var bomItem = vm.whereUsedItemMap.get(item.item);
                                if (bomItem != null) {
                                    bomItem.count = bomItem.count + 1;
                                } else {
                                    bomItem = item;
                                    bomItem.count = 1;
                                    vm.whereUsedLastItems.push(bomItem);
                                }
                                vm.whereUsedItemMap.put(item.item, bomItem);
                            }
                            vm.bomWhereUsedItems.push(item);
                            var index = vm.bomWhereUsedItems.indexOf(item);
                            index = populateWhereUsedChildren(item, index, dataDto);
                        });
                        $rootScope.bomTreeSearchItems = vm.whereUsedLastItems;
                        showBomWhereUsedReport();
                        $rootScope.bomTreeWhereUsedItem = bomDataDto;
                        $rootScope.hideBusyIndicator();
                        vm.openedBomRollWhereUsedReport = true;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function populateWhereUsedChildren(bomItem, lastIndex, dataDto) {
                angular.forEach(bomItem.children, function (item) {
                    lastIndex++;
                    var dto = {
                        number: null,
                        name: null,
                        itemId: null,
                        children: null
                    }
                    dto.number = item.itemNumber;
                    dto.name = item.itemName;
                    dto.itemId = item.item;
                    if (dataDto.children == null) {
                        dataDto.children = [];
                    }
                    dataDto.children.push(dto);
                    item.level = bomItem.level + 1;
                    vm.bomWhereUsedItems.splice(lastIndex, 0, item);
                    bomItem.count = bomItem.count + 1;
                    if (!item.hasBom) {
                        var wheredUsedItem = vm.whereUsedItemMap.get(item.item);
                        if (wheredUsedItem != null) {
                            wheredUsedItem.count = wheredUsedItem.count + 1;
                        } else {
                            wheredUsedItem = item;
                            wheredUsedItem.count = 1;
                            vm.whereUsedLastItems.push(wheredUsedItem);
                        }
                        vm.whereUsedItemMap.put(item.item, wheredUsedItem);
                    }
                    item.bomChildren = [];
                    bomItem.bomChildren.push(item);
                    lastIndex = populateWhereUsedChildren(item, lastIndex, dto)
                });

                return lastIndex;
            }

            vm.onRemoveSelectedWhereUsedItem = onRemoveSelectedWhereUsedItem;
            function onRemoveSelectedWhereUsedItem() {
                $rootScope.showBusyIndicator($("#bom-whereUsedReport"));
                $scope.whereUsedLimit = 30;
                var copyItems = angular.copy(vm.bomWhereUsedReportData);
                var bomDataDto = {
                    number: vm.item.itemNumber,
                    name: vm.item.itemName,
                    itemId: vm.itemId,
                    children: null
                }
                var bomDto = {
                    id: null,
                    itemName: vm.item.itemName,
                    itemNumber: vm.item.itemNumber,
                    itemTypeName: vm.item.itemType.name,
                    description: vm.item.description,
                    revision: vm.itemRevision.revision,
                    lifeCyclePhase: vm.itemRevision.lifeCyclePhase,
                    units: vm.item.units,
                    quantity: null,
                    level: 0
                }
                vm.bomWhereUsedItems = [];
                vm.bomWhereUsedItems.push(bomDto);
                vm.whereUsedItemMap = new Hashtable();
                vm.whereUsedLastItems = [];
                angular.forEach(copyItems, function (item) {
                    item.bomChildren = [];
                    item.level = 1;
                    bomDataDto.children = [];
                    var dataDto = {
                        number: null,
                        name: null,
                        itemId: null,
                        children: null
                    }
                    dataDto.number = item.itemNumber;
                    dataDto.name = item.itemName;
                    dataDto.itemId = item.item;
                    bomDataDto.children.push(dataDto);
                    if (!item.hasBom) {
                        var bomItem = vm.whereUsedItemMap.get(item.item);
                        if (bomItem != null) {
                            bomItem.count = bomItem.count + 1;
                        } else {
                            bomItem = item;
                            bomItem.count = 1;
                        }
                        vm.whereUsedLastItems.push(bomItem);
                        vm.whereUsedItemMap.put(item.item, bomItem);
                    }
                    vm.bomWhereUsedItems.push(item);
                    var index = vm.bomWhereUsedItems.indexOf(item);
                    index = populateWhereUsedChildren(item, index, dataDto);
                });
                $rootScope.bomTreeSearchItems = vm.whereUsedLastItems;
                $rootScope.bomTreeWhereUsedItem = bomDataDto;
                $rootScope.hideBusyIndicator();
                vm.openedBomRollWhereUsedReport = true;
            }

            $scope.searchWhereUsedReport = searchWhereUsedReport;
            function searchWhereUsedReport() {
                if (vm.selectedWhereUsedItems.length > 0) {
                    if (vm.whereUsedTableView) {
                        var itemIds = [];
                        $rootScope.showBusyIndicator($("#bom-whereUsedReport"));
                        angular.forEach(vm.selectedWhereUsedItems, function (item) {
                            itemIds.push(item.item);
                        });
                        $scope.whereUsedLimit = 30;
                        var copyWhereUsedItems = angular.copy(vm.bomWhereUsedReportData);
                        var bomDto = {
                            id: null,
                            itemName: vm.item.itemName,
                            itemNumber: vm.item.itemNumber,
                            itemTypeName: vm.item.itemType.name,
                            description: vm.item.description,
                            revision: vm.itemRevision.revision,
                            lifeCyclePhase: vm.itemRevision.lifeCyclePhase,
                            units: vm.item.units,
                            quantity: null,
                            level: 0
                        }
                        vm.bomWhereUsedItems = [];
                        vm.bomWhereUsedItems.push(bomDto);
                        angular.forEach(copyWhereUsedItems, function (item) {
                            item.bomChildren = [];
                            if (!item.hasBom) {
                                angular.forEach(itemIds, function (itemId) {
                                    if (item.item == itemId) {
                                        item.itemExist = true;
                                    }
                                })
                            }
                            item = populateWhereUsedSearchChildrenExist(item, itemIds);
                        })
                        $timeout(function () {
                            angular.forEach(copyWhereUsedItems, function (item) {
                                if (item.itemExist) {
                                    item.bomChildren = [];
                                    item.level = 1;
                                    vm.bomWhereUsedItems.push(item);
                                    var index = vm.bomWhereUsedItems.indexOf(item);
                                    index = populateWhereUsedSearchChildren(item, index);
                                }
                            });
                            $rootScope.hideBusyIndicator();
                        }, 1000)
                    } else {
                        clearAll(root);
                        angular.forEach(vm.selectedWhereUsedItems, function (item) {
                            expandAll(root);
                            searchField = "d.itemId";
                            searchText = item.item;
                            searchTree(root);
                            root.children.forEach(collapseAllNotFound);
                            update(root);
                        })
                    }
                } else {
                    onRemoveSearch();
                }
            }

            function populateWhereUsedSearchChildren(bomItem, lastIndex) {
                angular.forEach(bomItem.children, function (item) {
                    if (item.itemExist) {
                        lastIndex++;
                        item.level = bomItem.level + 1;
                        vm.bomWhereUsedItems.splice(lastIndex, 0, item);
                        bomItem.count = bomItem.count + 1;
                        item.bomChildren = [];
                        bomItem.bomChildren.push(item);
                        lastIndex = populateWhereUsedSearchChildren(item, lastIndex)
                    }
                });

                return lastIndex;
            }

            function populateWhereUsedTreeChildren(bomItem, dataDto) {
                angular.forEach(bomItem.children, function (item) {
                    var dto = {
                        number: item.itemNumber,
                        name: item.itemName,
                        itemId: item.item,
                        children: null
                    }
                    if (dataDto.children == null) {
                        dataDto.children = [];
                    }
                    dataDto.children.push(dto);
                    dto = populateWhereUsedTreeChildren(item, dto)
                });

                return dataDto;
            }

            function populateWhereUsedTreeSearchChildren(bomItem, dataDto) {
                angular.forEach(bomItem.children, function (item) {
                    if (item.itemExist) {
                        var dto = {
                            number: item.itemNumber,
                            name: item.itemName,
                            itemId: item.item,
                            children: null
                        }
                        if (dataDto.children == null) {
                            dataDto.children = [];
                        }
                        dataDto.children.push(dto);
                        dto = populateWhereUsedTreeSearchChildren(item, dto)
                    }
                });

                return dataDto;
            }

            function populateWhereUsedSearchChildrenExist(bomItem, itemIds) {
                angular.forEach(bomItem.children, function (item) {
                    if (item.hasBom) {
                        item.itemExist = false;
                        item = populateWhereUsedSearchChildrenExist(item, itemIds)
                    } else {
                        item.itemExist = false;
                        angular.forEach(itemIds, function (itemId) {
                            if (item.item == itemId) {
                                item.itemExist = true;
                            }
                        })
                    }
                });

                angular.forEach(bomItem.children, function (item) {
                    if (item.itemExist) {
                        bomItem.itemExist = true;
                    }
                })
                return bomItem;
            }

            $scope.selectWhereUsedView = selectWhereUsedView;
            function selectWhereUsedView(value) {
                $rootScope.showBusyIndicator();
                if (value == 'table') {
                    vm.whereUsedGraphicalView = false;
                    vm.whereUsedTableView = true;
                    var treeStructure = document.getElementById("whereUsed-height");
                    if (treeStructure != null) {
                        var i = 0;
                        angular.forEach(treeStructure.childNodes, function (childNode) {
                            if (childNode.nodeName == "svg") {
                                treeStructure.removeChild(treeStructure.childNodes[i]);
                            }
                            i++;
                        })
                    }
                    if (vm.selectedWhereUsedItems.length > 0) {
                        searchWhereUsedReport();
                    } else {
                        onRemoveSelectedWhereUsedItem();
                    }
                }
                if (value == 'graphical') {
                    vm.whereUsedTableView = false;
                    vm.whereUsedGraphicalView = true;
                    loadBomTree();
                    onBomTreeSearch();
                    $rootScope.hideBusyIndicator();
                }
            }

            $scope.clearSelections = clearSelections;
            function clearSelections() {
                vm.selectedWhereUsedItems = [];
                $timeout(function () {
                    if (vm.whereUsedTableView) {
                        onRemoveSelectedWhereUsedItem();
                    } else {
                        onRemoveSearch()
                    }
                }, 500);
            }

            function populateBomItemWhereUsedChildren(bomItem, lastIndex) {
                bomItem.count = 0;
                bomItem.bomChildren = [];
                angular.forEach(bomItem.children, function (item) {
                    lastIndex++;
                    item.expanded = true;
                    item.level = bomItem.level + 1;
                    item.count = 0;
                    item.bomChildren = [];
                    vm.bomWhereUsedItems.splice(lastIndex, 0, item);
                    bomItem.count = bomItem.count + 1;
                    bomItem.bomChildren.push(item);
                    lastIndex = populateBomItemWhereUsedChildren(item, lastIndex)
                });

                return lastIndex;
            }

            function showBomWhereUsedReport() {
                var modal = document.getElementById("bom-whereUsedReport");
                modal.style.display = "block";
                $timeout(function () {
                    var headerHeight = $('.bom-header').outerHeight();
                    var bomContentHeight = $('.bomWhereUsed-content').outerHeight();
                    var itemSelectionHeight = $('#item-selection').outerHeight();
                    $(".bom-content").height(bomContentHeight - headerHeight);
                    var h1 = bomContentHeight - (headerHeight + itemSelectionHeight);
                    $("#whereUsed-height").height(h1);

                    vm.openedBomRollWhereUsedReport = true;
                }, 200)

            }

            $scope.hideBomWhereUsedReport = hideBomWhereUsedReport;
            function hideBomWhereUsedReport() {
                var modal = document.getElementById("bom-whereUsedReport");
                modal.style.display = "none";
                vm.selectedWhereUsedItems = [];
                $rootScope.bomTreeSearchItems = [];
                vm.bomWhereUsedItems = [];
            }

            $scope.hasSelectedItems = hasSelectedItems;
            function hasSelectedItems(bomItem) {
                var hasItem = false;

                angular.forEach(vm.selectedWhereUsedItems, function (item) {
                    if (item.item == bomItem.item) {
                        hasItem = true;
                    }
                })

                return hasItem;
            }

            var nodeId = 0;
            var configurationEditorTree = null;
            var rootNode = null;
            $rootScope.loadBomConfigurationEditor = loadBomConfigurationEditor;
            function loadBomConfigurationEditor() {
                $rootScope.showBusyIndicator($('.view-container'));
                $rootScope.loadItem();
                loadItemForBomRules();
            }

            function onContextMenu(e, node) {
                e.preventDefault();
                var $contextMenu = $("#contextMenu");
                $('#configurationEditor').tree('select', node.target);
                var parent = configurationEditorTree.tree('getData', node.target);
                if (parent.nodeType == "CONFIGURATIONS") {
                    $contextMenu.show();
                    $('#newConfiguration').show();
                } else {
                    $contextMenu.hide();
                    $('#newConfiguration').hide();
                }

                if (node.root == undefined && parent.id === 0) {
                    $contextMenu.hide();
                }
                $contextMenu.css({
                    left: e.pageX,
                    top: e.pageY
                });

                $contextMenu.on("click", "a", function () {
                    $contextMenu.hide();
                });
            }

            vm.newBomConfiguration = newBomConfiguration;
            function newBomConfiguration() {
                var selectedNode = configurationEditorTree.tree('getSelected');
                if (selectedNode != null) {
                    var nodeid = ++nodeId;

                    var newNode = {
                        id: nodeid,
                        iconCls: "tree-node-icon-bomrconfigs",
                        text: "New BOM Configuration",
                        object: null,
                        nodeType: "NEW_BOM_CONFIGURATION"
                    };
                    vm.selectConfigEditorNode = newNode;
                    vm.showClear = false;
                    loadBomConfigurationModal();
                    $scope.$evalAsync();
                }
            }

            function loadItemBomConfigurations() {
                var configurationsRoot = {
                    id: ++nodeId,
                    text: "BOM Configurations",
                    iconCls: 'tree-node-icon-bomconfigs',
                    root: true,
                    nodeType: 'CONFIGURATIONS',
                    children: []
                };

                ItemBomService.getBomConfigurations(vm.itemId).then(
                    function (data) {
                        vm.bomConfigs = data;
                        if (data.length > 0) {
                            configurationsRoot.state = 'closed'
                        }
                        var nodes = [];
                        angular.forEach(data, function (itemType) {
                            var node = makeNode(itemType);
                            nodes.push(node);
                        });

                        configurationsRoot.children = nodes;

                        configurationEditorTree.tree('append', {
                            parent: rootNode.target,
                            data: configurationsRoot
                        });
                        $rootScope.hideBusyIndicator();
                        updateItemConfigurableAttributes();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function makeNode(configuration) {
                return {
                    id: ++nodeId,
                    text: configuration.name,
                    iconCls: 'tree-node-icon-bomconfigs',
                    object: configuration,
                    nodeType: 'CONFIGURATION'
                };
            }

            vm.selectConfigEditorNode = null;
            function onSelectType(node) {
                vm.loading = true;
                var data = configurationEditorTree.tree('getData', node.target);
                vm.selectConfigEditorNode = data;
                $rootScope.showBusyIndicator();
                if (vm.selectConfigEditorNode.nodeType == "CONFIGURATION") {
                    vm.bomConfiguration = angular.copy(vm.selectConfigEditorNode.object);
                    onSelectBomConfiguration(vm.bomConfiguration);
                } else if (vm.selectConfigEditorNode.nodeType == "NEW_BOM_CONFIGURATION") {
                    loadBomConfigurationModal();
                } else if (vm.selectConfigEditorNode.nodeType == "EXCLUSION_RULES") {
                    vm.itemTypeJson = new Map(JSON.parse(vm.itemRevision.attributeExclusionRules));
                    getConfigurableAttributes();
                } else if (vm.selectConfigEditorNode.nodeType == "INCLUSION_RULES") {
                    showRules();
                } else if (vm.selectConfigEditorNode.nodeType == "NON_CONFIGURABLE_RULES") {
                    showNonConfigurableItemRules();
                } else if (vm.selectConfigEditorNode.nodeType == "ATTRIBUTES") {
                    getItemConfigurableAttributes();
                } else {
                    $rootScope.hideBusyIndicator();
                    vm.loading = false;
                }
                $scope.$evalAsync();
            }


            function loadBomConfigurationModal() {
                ItemBomService.getBomModal(vm.itemId).then(
                    function (data) {
                        vm.modalBom = [];
                        data.level = 0;
                        vm.modalBom.push(data);
                        vm.bomConfiguration = angular.copy(emptyConfiguration);
                        vm.bomConfiguration.id = null;
                        vm.bomConfiguration.name = data.item.itemName + " - ";
                        vm.bomConfiguration.description = data.item.itemName;
                        $rootScope.hideBusyIndicator();
                        errorNotification = $('#configuration-error');
                        vm.openedBomConfigurationEditor = true;
                        vm.loading = false;
                        $timeout(function () {
                            var bomContentH1 = $('.configurationEditor-content').outerHeight();
                            var bomContentWidth = $('.editor-content').outerWidth();
                            $(".editor-content").height(bomContentH1 - 50);
                            $(".editor-content .editor-right").width(bomContentWidth - 300);
                            $(".editor-content .editor-right").height(bomContentH1 - 50);
                            var bomContentH2 = $('.editor-right').outerHeight();
                            $(".editor-configuration-content").height(bomContentH2 - 50);
                            $(".editor-configuration-content").width(bomContentWidth - 300);
                        }, 100)
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.attributesForTable = [];
            vm.attributesForExclusion = [];
            vm.attributesForTable1 = [];
            vm.concatArray = {firstItem: [], secondItem: []};
            vm.attributesForTable2 = [];
            vm.attributesForTable3 = [];
            vm.obj = {value: null, matching: false};
            vm.concatedFinalArray = [];
            vm.keyvalObj = {id: null, key: null, value: null, exclude: false};
            vm.finalValues = [];
            vm.nameHeaders = [];
            vm.values = [];
            vm.attributesForExclusion = [];
            vm.obj = {name: null, values: []};
            vm.attributes = [];
            vm.itemAttributes = [];


            function getItemConfigurableAttributes() {
                ItemService.getItemConfigurableAttributes(vm.itemId).then(
                    function (data) {
                        vm.itemAttributes = data;
                        angular.forEach(vm.itemAttributes, function (attribute) {
                            attribute.valueObjects = [];
                            angular.forEach(attribute.values, function (value) {
                                attribute.valueObjects.push({string: value, newString: value, editMode: false});
                            })
                        });
                        $rootScope.hideBusyIndicator();
                        vm.loading = false
                    },
                    function (error) {
                        vm.loading = false
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function getConfigurableAttributes() {
                vm.attributes = [];
                vm.nameHeaders = [];
                vm.values = [];
                vm.attributesForExclusion = [];
                vm.finalValues = [];
                $rootScope.itemAttributesExclusions = [];

                ItemBomService.getBomConfigurationAttributeExclusions(vm.itemId).then(
                    function (data) {
                        $rootScope.itemAttributesExclusions = data;
                        ItemService.getConfigurableAttributes(vm.itemId).then(
                            function (data) {
                                vm.attributesForExclusion = data;
                                angular.forEach(data, function (values, key) {
                                    var valuesForMap = values.values;
                                    var i = values.attribute.id;
                                    var key1 = key;
                                    vm.obj = {name: null, values: []};
                                    vm.obj.name = key;
                                    vm.obj.values = valuesForMap;
                                    vm.attributes.push(vm.obj);

                                    var header = {name: key, numValues: valuesForMap.length};
                                    vm.nameHeaders.push(header);

                                    angular.forEach(valuesForMap, function (val) {
                                        vm.keyvalObj = {id: null, key: null, value: null, exclude: false};
                                        vm.keyvalObj.id = i;
                                        vm.keyvalObj.key = key1;
                                        vm.keyvalObj.value = val;
                                        vm.finalValues.push(vm.keyvalObj);
                                        vm.values.push(val);
                                    });
                                });
                                $rootScope.hideBusyIndicator();
                                vm.loading = false;
                            }, (function (error) {
                                $rootScope.hideBusyIndicator();
                                $rootScope.showWarningMessage(parsed.html(error.message).html());
                                vm.loading = false
                            })
                        );
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.shouldAddAttributeRowHeader = shouldAddAttributeRowHeader;
            function shouldAddAttributeRowHeader(index) {
                if (index == 0) {
                    return [vm.attributes[0].name, vm.attributes[0].values.length];
                }
                var temp = 0;
                for (var i = 0; i < vm.attributes.length; i++) {
                    var values = vm.attributes[i].values;

                    temp = temp + values.length;
                    if (index == temp) {
                        return [vm.attributes[i + 1].name, vm.attributes[i + 1].values.length];
                    }

                }

                return ["", 0];
            }

            vm.itemTypeJson = new Map();
            vm.itemTypeJsonReflection = new Map();
            vm.jsonObjectForExclusion = [];
            vm.createExclusionObj = createExclusionObj;
            vm.checkExclude = checkExclude;
            vm.json = [];
            vm.backupVerticalObj = null;
            vm.backupHorizantalObj = null;
            vm.exclSaveMessage = "";
            vm.historyObj = {
                combination: null,
                added: false,
                id: null,
                exclusion: false
            }
            $rootScope.attExcludeMessage = "";
            function createExclusionObj(horizant, hIndex, vertical, vIndex) {
                vm.historyObj = {
                    combination: null,
                    added: false,
                    id: null,
                    exclusion: false
                }
                vm.exclSaveMessage = null;
                var combination = horizant.key + "(" + vertical.value + "-" + horizant.value + ")";
                if ($rootScope.itemAttributesExclusions.indexOf(combination) == -1) {
                    var esclAddedMsg = parsed.html($translate.instant("EXCL_ADDED_MESSAGE")).html();
                    var esclRmvdMsg = parsed.html($translate.instant("EXCL_REMOVED_MESSAGE")).html();
                    if (vm.itemTypeJson != null) {
                        if (vm.itemTypeJson.size > 0) {

                            if (vm.itemTypeJson.has(horizant.value + "_" + vertical.value + "_" + horizant.key + vertical.key)) {
                                vm.itemTypeJson.delete(horizant.value + "_" + vertical.value + "_" + horizant.key + vertical.key);
                                vm.exclSaveMessage = "[" + horizant.value + " - " + vertical.value + "] " + esclRmvdMsg;
                                vm.historyObj.combination = horizant.key + " ( " + horizant.value + " ) - " + vertical.key + " ( " + vertical.value + " )";
                                vm.historyObj.exclusion = true;
                                vm.historyObj.added = false;
                            }
                            else if (vm.itemTypeJson.has(vertical.value + "_" + horizant.value + "_" + vertical.key + horizant.key)) {
                                vm.itemTypeJson.delete(vertical.value + "_" + horizant.value + "_" + vertical.key + horizant.key);
                                vm.exclSaveMessage = "[" + vertical.value + " - " + horizant.value + "] " + esclRmvdMsg;
                                vm.historyObj.combination = vertical.key + " ( " + vertical.value + " ) - " + horizant.key + " ( " + horizant.value + " )";
                                vm.historyObj.exclusion = true;
                                vm.historyObj.added = false;
                            }

                            else {
                                vm.itemTypeJson.set(horizant.value + "_" + vertical.value + "_" + horizant.key + vertical.key, [horizant, vertical]);
                                vm.exclSaveMessage = "[" + horizant.value + " - " + vertical.value + "] " + esclAddedMsg;
                                vm.historyObj.combination = horizant.key + " ( " + horizant.value + " ) - " + vertical.key + " ( " + vertical.value + " )";
                                vm.historyObj.exclusion = true;
                                vm.historyObj.added = true;
                            }
                        }
                        else {
                            vm.itemTypeJson.set(horizant.value + "_" + vertical.value + "_" + horizant.key + vertical.key, [horizant, vertical]);
                            vm.exclSaveMessage = "[" + horizant.value + " - " + vertical.value + "] " + esclAddedMsg;
                            vm.historyObj.combination = horizant.key + " ( " + horizant.value + " ) - " + vertical.key + " ( " + vertical.value + " )";
                            vm.historyObj.exclusion = true;
                            vm.historyObj.added = true;
                        }
                    }
                    else {
                        vm.itemTypeJson = new Map();
                        vm.itemTypeJson.set(horizant.value + "_" + vertical.value + "_" + horizant.key + vertical.key, [horizant, vertical]);
                        vm.exclSaveMessage = "[" + horizant.value + " - " + vertical.value + "] " + esclAddedMsg;
                        vm.historyObj.combination = horizant.key + " ( " + horizant.value + " ) - " + vertical.key + " ( " + vertical.value + " ) ";
                        vm.historyObj.exclusion = true;
                        vm.historyObj.added = true;
                    }
                    vm.itemRevision.attributeExclusionRules = JSON.stringify(Array.from(vm.itemTypeJson.entries()));
                    vm.bomItemAttributesExclusions.push(vm.historyObj);
                    ItemService.updateItem(vm.itemRevision).then(
                        function (data) {
                            vm.itemTypeJson = new Map();
                            vm.itemTypeJson = new Map(JSON.parse(data.attributeExclusionRules));
                            vm.historyObj.id = vm.itemRevision.id;
                            $timeout(function () {
                                vm.exclSaveMessage = ""
                            }, 5000);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                } else {
                    $rootScope.attExcludeMessage = "This Combination already in use we can not exclude!";
                    $rootScope.hideBusyIndicator();
                    $timeout(function () {
                        $rootScope.attExcludeMessage = "";
                    }, 5000)
                }
            }

            function checkExclude(horizant, vertical) {
                var color = "normal";

                if (vm.itemTypeJson != null) {
                    if (vm.itemTypeJson.size > 0) {
                        if (vm.itemTypeJson.has(horizant.value + "_" + vertical.value + "_" + horizant.key + vertical.key)) {
                            color = "colorExclude";

                        }
                        else if (vm.itemTypeJson.has(vertical.value + "_" + horizant.value + "_" + vertical.key + horizant.key)) {
                            color = "colorExclude";
                        }
                        else {
                            color = "normal";
                        }
                    }
                    else {
                        color = "normal";
                    }
                }
                else {
                    color = "normal";
                }

                return color;
            }

            function showBomConfigurationEditor() {
                var modal = document.getElementById("configuration-editor");
                modal.style.display = "block";
                $timeout(function () {
                    var headerH1 = $('.bom-header').outerHeight();
                    var bomContentH2 = $('.configurationEditor-content').outerHeight();
                    var bomContentWidth = $('.configurationEditor-content').outerWidth();
                    var editorLeftWidth = $('.editor-left').outerWidth();
                    $("#configuration-editor .editor-content").height(bomContentH2 - headerH1);
                    $(".editor-content .editor-right").width(bomContentWidth - editorLeftWidth);
                    errorNotification = $('#configuration-error');
                    vm.openedBomConfigurationEditor = true;
                }, 200)
            }

            $scope.hideConfigurationEditor = hideConfigurationEditor;
            function hideConfigurationEditor() {
                if (vm.bomConfigItemInclusions.length > 0) {
                    ItemBomService.createBomConfigItemInclusionRules(vm.itemId, vm.bomConfigItemInclusions);
                }
                if (vm.bomNonConfigItemInclusions.length > 0) {
                    ItemBomService.createBomNonConfigItemInclusionRules(vm.itemId, vm.bomNonConfigItemInclusions);
                }

                if (vm.bomItemAttributesExclusions.length > 0) {
                    ItemBomService.createBomConfigAttributeExclusionRules(vm.itemId, vm.bomItemAttributesExclusions);
                }
                var modal = document.getElementById("configuration-editor");
                vm.selectConfigEditorNode = null;
                vm.loading = false;
                modal.style.display = "none";
                nodeId = 0;
                configurationEditorTree = null;
                rootNode = null;
            }

            $rootScope.generateComplianceReport = generateComplianceReport;
            function generateComplianceReport() {
                $rootScope.showBusyIndicator($('.view-container'));
                ItemBomService.getBomComplianceReport(vm.itemId).then(
                    function (data) {
                        vm.bomComplianceItems = [];
                        vm.bomComplianceReport = data;
                        vm.itemSpecifications = vm.bomComplianceReport.specifications;
                        vm.bomComplianceReport.bomComplianceData.level = 0;
                        vm.bomComplianceReport.bomComplianceData.itemSpecifications = vm.itemSpecifications;
                        vm.bomComplianceItems.push(vm.bomComplianceReport.bomComplianceData);
                        var index = vm.bomComplianceItems.indexOf(vm.bomComplianceReport.bomComplianceData);
                        index = populateBomComplianceItemChildren(vm.bomComplianceReport.bomComplianceData, index);
                        showBomComplianceReport();
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function populateBomComplianceItemChildren(bomItem, lastIndex) {
                bomItem.count = 0;
                bomItem.bomChildren = [];
                bomItem.hasBom = false;
                angular.forEach(bomItem.children, function (item) {
                    bomItem.hasBom = true;
                    lastIndex++;
                    item.expanded = true;
                    item.level = bomItem.level + 1;
                    item.count = 0;
                    item.bomChildren = [];
                    vm.bomComplianceItems.splice(lastIndex, 0, item);
                    bomItem.count = bomItem.count + 1;
                    bomItem.bomChildren.push(item);
                    lastIndex = populateBomComplianceItemChildren(item, lastIndex)
                });

                return lastIndex;
            }

            vm.toggleComplianceItem = toggleComplianceItem;
            function toggleComplianceItem(bomItem) {
                bomItem.expanded = !bomItem.expanded;
                var index = vm.bomComplianceItems.indexOf(bomItem);
                if (bomItem.expanded == false) {
                    removeComplianceItemChildren(bomItem);
                } else {
                    angular.forEach(bomItem.children, function (item) {
                        item.hasBom = false;
                        item.expanded = true;
                        item.level = bomItem.level + 1;
                        item.count = 0;
                        item.bomChildren = [];
                        bomItem.count = bomItem.count + 1;
                        bomItem.bomChildren.push(item);
                    });
                    angular.forEach(bomItem.bomChildren, function (item) {
                        index = index + 1;
                        vm.bomComplianceItems.splice(index, 0, item);
                    });
                }
            }

            vm.removeComplianceItemChildren = removeComplianceItemChildren;
            function removeComplianceItemChildren(bomItem) {
                if (bomItem != null && bomItem.bomChildren != null && bomItem.bomChildren != undefined) {
                    angular.forEach(bomItem.bomChildren, function (item) {
                        removeComplianceItemChildren(item);
                    });

                    var index = vm.bomComplianceItems.indexOf(bomItem);
                    vm.bomComplianceItems.splice(index + 1, bomItem.bomChildren.length);
                    bomItem.bomChildren = [];
                    bomItem.expanded = false;

                }
            }

            function showBomComplianceReport() {
                var modal = document.getElementById("bom-complianceReport");
                modal.style.display = "block";
                $timeout(function () {
                    var headerHeight = $('.bom-header').outerHeight();
                    var bomContentHeight = $('.bomCompliance-content').outerHeight();
                    $(".bom-content").height(bomContentHeight - headerHeight);
                    $("#compliance-height").height(bomContentHeight);

                    vm.openedBomComplianceReport = true;
                }, 200)

            }

            $scope.hideBomComplianceReport = hideBomComplianceReport;
            function hideBomComplianceReport() {
                var modal = document.getElementById("bom-complianceReport");
                modal.style.display = "none";
            }

            function updateItemConfigurableAttributes() {
                ItemService.updateItemConfigurableAttributes(vm.itemId).then(
                    function (data) {
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            var enterListValue = parsed.html($translate.instant("ENTER_ATTRIBUTE_VALUE")).html();
            var valueCannotBe = parsed.html($translate.instant("VALUE_CANNOT_BE")).html();
            var the_notAllowed = parsed.html($translate.instant("THE_NOT_ALLOWED")).html();
            var specialCharacterNotAllowed = parsed.html($translate.instant("SPECIAL_CHARACTER_NOT_ALLOWED")).html();
            var valueAlreadyExist = parsed.html($translate.instant("VALUE_ALREADY_EXIST")).html();
            var newValue = $translate.instant("NEW_VALUE");
            vm.addAttributeValue = addAttributeValue;
            function addAttributeValue(attribute) {
                attribute.valueObjects.push({string: newValue, newString: newValue, editMode: true, newMode: true});
            }

            function validateAttributeValues(attribute) {
                var valid = true;

                angular.forEach(attribute.valueObjects, function (obj) {
                    if (obj.string == null || obj.string == "" || obj.string == undefined) {
                        if (valid) {
                            valid = false;
                            vm.notificationClass = "fa-check";
                            vm.notificationBackground = "alert-success";
                            vm.message = enterListValue;
                            errorNotification.show();
                            errorNotification.removeClass('zoomOut');
                            errorNotification.addClass('zoomIn');
                            $timeout(function () {
                                closeErrorNotification();
                            }, 3000);
                        }
                    } else if (obj.string == newValue) {
                        if (valid) {
                            valid = false;
                            vm.notificationClass = "fa-check";
                            vm.notificationBackground = "alert-success";
                            vm.message = valueCannotBe + " [ " + newValue + " ]";
                            errorNotification.show();
                            errorNotification.removeClass('zoomOut');
                            errorNotification.addClass('zoomIn');
                            $timeout(function () {
                                closeErrorNotification();
                            }, 3000);
                        }
                    }
                });

                return valid;
            }

            vm.applyAttributeChanges = applyAttributeChanges;
            function applyAttributeChanges(attribute) {
                vm.newLov.values = [];
                if (validateAttributeValues()) {
                    angular.forEach(attribute.valueObjects, function (obj) {
                        attribute.values.push(obj.string);
                    });

                    ItemService.updateItemConfigAttribute(attribute).then(
                        function (data) {
                            vm.notificationClass = "fa-check";
                            vm.notificationBackground = "alert-success";
                            vm.message = valueSaved;
                            errorNotification.show();
                            errorNotification.removeClass('zoomOut');
                            errorNotification.addClass('zoomIn');
                            $timeout(function () {
                                closeErrorNotification();
                            }, 3000);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }

            }

            var valueSaved = parsed.html($translate.instant("ATTRIBUTE_VALUE_SAVED")).html();

            vm.applyChangesList = applyChangesList;

            function applyChangesList(value, attribute) {

                if (validateLovNames(attribute, value)) {
                    value.newMode = false;
                    attribute.values = [];
                    angular.forEach(attribute.valueObjects, function (obj) {
                        attribute.values.push(obj.string);

                    });

                    ItemService.updateItemConfigAttribute(attribute).then(
                        function (data) {
                            vm.notificationClass = "fa-check";
                            vm.notificationBackground = "alert-success";
                            vm.message = valueSaved;
                            errorNotification.show();
                            errorNotification.removeClass('zoomOut');
                            errorNotification.addClass('zoomIn');
                            $timeout(function () {
                                closeErrorNotification();
                            }, 3000);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }

            }

            vm.cancelChangesList = cancelChangesList;
            function cancelChangesList(value, attribute) {
                if (value.newMode) {
                    attribute.valueObjects.splice(attribute.valueObjects.indexOf(value), 1);
                } else {
                    value.newString = value.string;
                }
            }

            function validateLovNames(attribute, value) {
                var valid = true;
                if (value.newString.includes("_")) {
                    valid = false;
                    value.editMode = true;
                    value.newMode = false;
                    vm.notificationClass = "fa-warning";
                    vm.notificationBackground = "alert-warning";
                    vm.message = specialCharacterNotAllowed;
                    errorNotification.show();
                    errorNotification.removeClass('zoomOut');
                    errorNotification.addClass('zoomIn');
                    $timeout(function () {
                        closeErrorNotification();
                    }, 3000);
                } else if (value.newString == "" || value.newString == null || value.newString == undefined) {
                    valid = false;
                    value.editMode = true;
                    vm.notificationClass = "fa-warning";
                    vm.notificationBackground = "alert-warning";
                    vm.message = enterListValue;
                    errorNotification.show();
                    errorNotification.removeClass('zoomOut');
                    errorNotification.addClass('zoomIn');
                    $timeout(function () {
                        closeErrorNotification();
                    }, 3000);
                } else if (value.newString == newValue) {
                    valid = false;
                    value.editMode = true;
                    vm.notificationClass = "fa-warning";
                    vm.notificationBackground = "alert-warning";
                    vm.message = valueCannotBe + " [ " + newValue + " ]";
                    errorNotification.show();
                    errorNotification.removeClass('zoomOut');
                    errorNotification.addClass('zoomIn');
                    $timeout(function () {
                        closeErrorNotification();
                    }, 3000);
                } else {
                    angular.forEach(attribute.values, function (obj) {
                        if (obj.toUpperCase() === value.newString.toUpperCase()) {
                            valid = false;
                            value.editMode = true;
                            value.newMode = false;
                            vm.notificationClass = "fa-warning";
                            vm.notificationBackground = "alert-warning";
                            vm.message = valueAlreadyExist;
                            errorNotification.show();
                            errorNotification.removeClass('zoomOut');
                            errorNotification.addClass('zoomIn');
                            $timeout(function () {
                                closeErrorNotification();
                            }, 3000);
                        }

                    });
                }
                return valid;
            }

            var atributeValueDeleted = parsed.html($translate.instant("ATTRIBUTE_VALUE_DELETE")).html();

            vm.toDeleteValue = null;
            vm.deleteAttributeValue = deleteAttributeValue;
            function deleteAttributeValue(attribute) {
                if (vm.toDeleteValue != null) {
                    ItemBomService.getAttributeValueUsedInConfigurations(vm.itemId, attribute.attribute.id, vm.toDeleteValue.string).then(
                        function (data) {
                            if (data.length == 0) {
                                attribute.showBusy = true;
                                attribute.valueObjects.remove(vm.toDeleteValue);
                                attribute.values = [];
                                angular.forEach(attribute.valueObjects, function (obj) {
                                    attribute.values.push(obj.string);
                                });

                                ItemService.updateItemConfigAttribute(attribute).then(
                                    function (data) {
                                        vm.notificationClass = "fa-check";
                                        vm.notificationBackground = "alert-success";
                                        vm.message = atributeValueDeleted;
                                        errorNotification.show();
                                        errorNotification.removeClass('zoomOut');
                                        errorNotification.addClass('zoomIn');
                                        $timeout(function () {
                                            closeErrorNotification();
                                        }, 3000);
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                        $rootScope.hideBusyIndicator();
                                    }
                                )
                            } else {
                                vm.notificationClass = "fa-ban";
                                vm.notificationBackground = "alert-danger";
                                vm.message = vm.toDeleteValue.string + " attribute value already in use";
                                errorNotification.show();
                                errorNotification.removeClass('zoomOut');
                                errorNotification.addClass('zoomIn');
                                $timeout(function () {
                                    closeErrorNotification();
                                }, 3000);
                            }
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }


            function select2DataCollectName(d) {
                d.title = null;
                d.title = d.name;
                if (d.children)
                    d.children.forEach(select2DataCollectName);
                else if (d._children)
                    d._children.forEach(select2DataCollectName);
                select2Data.push(d.name);
            }

            //===============================================
            function searchTree(d) {
                if (d.children)
                    d.children.forEach(searchTree);
                else if (d._children)
                    d._children.forEach(searchTree);
                var searchFieldValue = eval(searchField);
                if (searchFieldValue && searchFieldValue == searchText) {
                    // Walk parent chain
                    var ancestors = [];
                    var parent = d;
                    while (typeof(parent) !== "undefined") {
                        ancestors.push(parent);
                        //console.log(parent);
                        parent.class = "found";
                        parent = parent.parent;
                    }
                    //console.log(ancestors);
                }
            }

            //===============================================
            function clearAll(d) {
                d.class = "";
                if (d.children)
                    d.children.forEach(clearAll);
                else if (d._children)
                    d._children.forEach(clearAll);
            }

            //===============================================
            function collapse(d) {
                if (d.children) {
                    d._children = d.children;
                    d._children.forEach(collapse);
                    d.children = null;
                }
            }

            //===============================================
            function collapseAllNotFound(d) {
                if (d.children) {
                    if (d.class !== "found") {
                        d._children = d.children;
                        d._children.forEach(collapseAllNotFound);
                        d.children = null;
                    } else
                        d.children.forEach(collapseAllNotFound);
                }
            }

            //===============================================
            function expandAll(d) {
                if (d._children) {
                    d.children = d._children;
                    d.children.forEach(expandAll);
                    d._children = null;
                } else if (d.children)
                    d.children.forEach(expandAll);
            }

            //===============================================
            // Toggle children on click.
            function toggle(d) {
                if (d.children) {
                    d._children = d.children;
                    d.children = null;
                } else {
                    d.children = d._children;
                    d._children = null;
                }
                clearAll(root);
                update(d);
                angular.forEach(vm.selectedWhereUsedItems, function (item) {
                    //expandAll(root);
                    searchField = "d.itemId";
                    searchText = item.item;
                    searchTree(root);
                    root.children.forEach(collapseAllNotFound);
                    update(root);
                })
            }

            $rootScope.bomTreeWhereUsedItem = null;
            $rootScope.bomTreeSearchItems = [];
            vm.selectedWhereUsedItems = [];
            vm.searchName = null;
            var searchField = null;
            var searchText = null;
            $rootScope.onBomTreeSearch = onBomTreeSearch;
            function onBomTreeSearch(item) {
                if (vm.selectedWhereUsedItems.length > 0) {
                    var itemIds = [];
                    angular.forEach(vm.selectedWhereUsedItems, function (item) {
                        itemIds.push(item.item);
                    })
                    if (vm.whereUsedTableView) {
                        searchWhereUsedReport();
                    } else {
                        var copyWhereUsedItems = angular.copy(vm.bomWhereUsedReportData);
                        var bomDataDto = {
                            number: vm.item.itemNumber,
                            name: vm.item.itemName,
                            itemId: vm.itemId,
                            children: null
                        }
                        angular.forEach(copyWhereUsedItems, function (item) {
                            item.bomChildren = [];
                            if (!item.hasBom) {
                                angular.forEach(itemIds, function (itemId) {
                                    if (item.item == itemId) {
                                        item.itemExist = true;
                                    }
                                })
                            }
                            item = populateWhereUsedSearchChildrenExist(item, itemIds);
                        })
                        $timeout(function () {
                            angular.forEach(copyWhereUsedItems, function (item) {
                                if (item.itemExist) {
                                    var dataDto = {
                                        number: vm.item.itemNumber,
                                        name: item.itemName,
                                        itemId: item.item,
                                        children: null
                                    }
                                    if (bomDataDto.children == null) {
                                        bomDataDto.children = [];
                                    }
                                    bomDataDto.children.push(dataDto);
                                    dataDto = populateWhereUsedTreeSearchChildren(item, dataDto);
                                }
                            });

                            $rootScope.bomTreeWhereUsedItem = bomDataDto;
                            loadBomTree();
                            $timeout(function () {
                                clearAll(root);
                                expandAll(root);
                                update(root);
                                angular.forEach(vm.selectedWhereUsedItems, function (item) {
                                    expandAll(root);
                                    searchField = "d.itemId";
                                    searchText = item.item;
                                    searchTree(root);
                                    root.children.forEach(collapseAllNotFound);
                                    update(root);
                                })
                            }, 200);
                        }, 500)
                    }
                } else {
                    var copyWhereUsedItems = angular.copy(vm.bomWhereUsedReportData);
                    var bomDataDto = {
                        number: vm.item.itemNumber,
                        name: vm.item.itemName,
                        itemId: vm.itemId,
                        children: null
                    }
                    angular.forEach(copyWhereUsedItems, function (item) {
                        var dataDto = {
                            number: item.itemNumber,
                            name: item.itemName,
                            itemId: item.item,
                            children: null
                        }
                        if (bomDataDto.children == null) {
                            bomDataDto.children = [];
                        }
                        bomDataDto.children.push(dataDto);
                        dataDto = populateWhereUsedTreeChildren(item, dataDto);
                    });

                    $rootScope.bomTreeWhereUsedItem = bomDataDto;
                    loadBomTree();
                    $timeout(function () {
                        clearAll(root);
                        expandAll(root);
                        update(root);
                        angular.forEach(vm.selectedWhereUsedItems, function (item) {
                            expandAll(root);
                            searchField = "d.itemId";
                            searchText = item.item;
                            searchTree(root);
                            root.children.forEach(collapseAllNotFound);
                            update(root);
                        })
                    }, 200);
                }

                var headerHeight = $('.bom-header').outerHeight();
                var bomContentHeight = $('.bomWhereUsed-content').outerHeight();
                var itemSelectionHeight = $('#item-selection').outerHeight();
                $(".bom-content").height(bomContentHeight - headerHeight);
                var h1 = bomContentHeight - (headerHeight + itemSelectionHeight);
                $("#whereUsed-height").height(h1);
                /* else {
                 angular.forEach(vm.selectedWhereUsedItems, function (item) {
                 expandAll(root);
                 searchField = "d.itemId";
                 searchText = item.id;
                 searchTree(root);
                 root.children.forEach(collapseAllNotFound);
                 update(root);
                 })
                 }*/
            }

            $rootScope.onRemoveSearch = onRemoveSearch;
            function onRemoveSearch(item) {
                if (vm.whereUsedTableView) {
                    if (vm.selectedWhereUsedItems.length > 0) {
                        searchWhereUsedReport();
                    } else {
                        onRemoveSelectedWhereUsedItem();
                    }
                } else {
                    clearAll(root);
                    expandAll(root);
                    update(root);
                    if (vm.selectedWhereUsedItems.length > 0) {
                        /*angular.forEach(vm.selectedWhereUsedItems, function (selectedItem) {
                         expandAll(root);
                         searchField = "d.name";
                         searchText = selectedItem.item;
                         searchTree(root);
                         root.children.forEach(collapseAllNotFound);
                         update(root);
                         })*/

                        clearAll(root);
                        angular.forEach(vm.selectedWhereUsedItems, function (item) {
                            expandAll(root);
                            searchField = "d.itemId";
                            searchText = item.item;
                            searchTree(root);
                            root.children.forEach(collapseAllNotFound);
                            update(root);
                        })
                    } else {
                        var copyWhereUsedItems = angular.copy(vm.bomWhereUsedReportData);
                        var bomDataDto = {
                            number: vm.item.itemNumber,
                            name: vm.item.itemName,
                            itemId: vm.itemId,
                            children: null
                        }
                        angular.forEach(copyWhereUsedItems, function (item) {
                            var dataDto = {
                                number: item.itemNumber,
                                name: item.itemName,
                                itemId: item.item,
                                children: null
                            }
                            if (bomDataDto.children == null) {
                                bomDataDto.children = [];
                            }
                            bomDataDto.children.push(dataDto);
                            dataDto = populateWhereUsedTreeChildren(item, dataDto);
                        });

                        $rootScope.bomTreeWhereUsedItem = bomDataDto;
                        loadBomTree();
                        $timeout(function () {
                            clearAll(root);
                            expandAll(root);
                            update(root);
                            angular.forEach(vm.selectedWhereUsedItems, function (item) {
                                expandAll(root);
                                searchField = "d.itemId";
                                searchText = item.item;
                                searchTree(root);
                                root.children.forEach(collapseAllNotFound);
                                update(root);
                            })
                        }, 200);
                    }
                }
            }

            //===============================================
            var margin = {top: 20, right: 120, bottom: 20, left: 120},
                width = 960 - margin.right - margin.left,
                height = 700 - margin.top - margin.bottom;

            var i = 0,
                duration = 750,
                root;

            var tree;
            var diagonal;
            var svg;
            var select2Data = [];
            var select2DataObject = [];

            $rootScope.loadBomTree = loadBomTree;
            function loadBomTree() {
                var modal = document.getElementById('bom-whereUsedReport');
                modal.style.display = "block";
                var headerHeight = $('.bom-header').outerHeight();
                var bomContentHeight = $('.bomWhereUsed-content').outerHeight();
                var bomContentWidth = $('.bomWhereUsed-content').outerWidth();
                $(".bom-content").height(bomContentHeight - headerHeight);
                var itemSelection = $('#item-selection').outerHeight();
                var h1 = bomContentHeight - (headerHeight + itemSelection);
                $("#whereUsed-height").height(h1);
                width = bomContentWidth - margin.right - margin.left;
                height = h1;

                tree = d3.layout.tree()
                    .size([height, width]);

                diagonal = d3.svg.diagonal()
                    .projection(function (d) {
                        return [d.y, d.x];
                    });
                var treeStructure = document.getElementById("whereUsed-height");
                if (treeStructure != null) {
                    var i = 0;
                    angular.forEach(treeStructure.childNodes, function (childNode) {
                        if (childNode.nodeName == "svg") {
                            treeStructure.removeChild(treeStructure.childNodes[i]);
                        }
                        i++;
                    })
                }
                svg = d3.select("#whereUsed-height").append("svg")
                    .attr("width", bomContentWidth - 50)
                    .attr("height", height + margin.top + margin.bottom)
                    .append("g")
                    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

                root = $rootScope.bomTreeWhereUsedItem;
                root.x0 = height / 2;
                root.y0 = 0;

                select2Data = [];
                select2DataCollectName(root);
                select2DataObject = [];
                root.children.forEach(collapse);
                update(root);

            }

            function update(source) {
                var newHeight = Math.max(tree.nodes(root).reverse().length * 20, height);
                d3.select("#whereUsed-height svg")
                    .attr("width", width + margin.right + margin.left)
                    .attr("height", newHeight + margin.top + margin.bottom);
                tree = d3.layout.tree().size([newHeight, width]);

                // Compute the new tree layout.
                var nodes = tree.nodes(root).reverse(),
                    links = tree.links(nodes);

                // Normalize for fixed-depth.
                nodes.forEach(function (d) {
                    d.y = d.depth * 180;
                });

                // Update the nodes�
                var node = svg.selectAll("g.node")
                    .data(nodes, function (d) {
                        return d.id || (d.id = ++i);
                    });

                // Enter any new nodes at the parent's previous position.
                var nodeEnter = node.enter().append("g")
                    .attr("class", "node")
                    .attr("transform", function (d) {
                        return "translate(" + source.y0 + "," + source.x0 + ")";
                    })
                    .on("click", toggle).on("mouseover", function (d) {
                        var g = d3.select(this); // The node
                        // The class is used to remove the additional text later
                        var info = g.append('text')
                            .classed('info', true)
                            .attr('x', 10)
                            .attr('y', 30)
                            .text(d.title);
                    })
                    .on("mouseout", function () {
                        // Remove the info text on mouse out.
                        d3.select(this).select('text.info').remove();
                    });

                nodeEnter.append("circle")
                    .attr("r", 1e-6)
                    .style("fill", function (d) {
                        return d._children ? "lightsteelblue" : "#fff";
                    });

                nodeEnter.append("text")
                    .attr("x", function (d) {
                        return d.children || d._children ? -10 : 10;
                    })
                    .attr("dy", ".35em")
                    .attr("title", function (d) {
                        return d.name;
                    })
                    .attr("text-anchor", function (d) {
                        return d.children || d._children ? "end" : "start";
                    })
                    .text(function (d) {
                        if (d.name == $rootScope.item.itemName && d.name.length > 15) {
                            d.name = d.name.substring(0, 13) + "..";
                        }

                        if (d.name != $rootScope.item.itemName && d.name.length > 20) {
                            d.name = d.name.substring(0, 18) + "..";
                        }
                        return "[{0}] {1}".format(d.number, d.name);
                    })
                    .style("fill-opacity", 1e-6);

                // Transition nodes to their new position.
                var nodeUpdate = node.transition()
                    .duration(duration)
                    .attr("transform", function (d) {
                        return "translate(" + d.y + "," + d.x + ")";
                    });

                nodeUpdate.select("circle")
                    .attr("r", 4.5)
                    .style("fill", function (d) {
                        if (d.class === "found") {
                            return "#ff4136"; //red
                        } else if (d._children) {
                            return "lightsteelblue";
                        } else {
                            return "#fff";
                        }
                    })
                    .style("stroke", function (d) {
                        if (d.class === "found") {
                            return "#ff4136"; //red
                        }
                    });

                nodeUpdate.select("text")
                    .style("fill-opacity", 1);

                // Transition exiting nodes to the parent's new position.
                var nodeExit = node.exit().transition()
                    .duration(duration)
                    .attr("transform", function (d) {
                        return "translate(" + source.y + "," + source.x + ")";
                    })
                    .remove();

                nodeExit.select("circle")
                    .attr("r", 1e-6);

                nodeExit.select("text")
                    .style("fill-opacity", 1e-6);

                // Update the links
                var link = svg.selectAll("path.link")
                    .data(links, function (d) {
                        return d.target.id;
                    });

                // Enter any new links at the parent's previous position.
                link.enter().insert("path", "g")
                    .attr("class", "link")
                    .attr("d", function (d) {
                        var o = {x: source.x0, y: source.y0};
                        return diagonal({source: o, target: o});
                    });

                // Transition links to their new position.
                link.transition()
                    .duration(duration)
                    .attr("d", diagonal)
                    .style("stroke", function (d) {
                        if (d.target.class === "found") {
                            return "#ff4136";
                        }
                    });

                // Transition exiting nodes to the parent's new position.
                link.exit().transition()
                    .duration(duration)
                    .attr("d", function (d) {
                        var o = {x: source.x, y: source.y};
                        return diagonal({source: o, target: o});
                    })
                    .remove();

                // Stash the old positions for transition.
                nodes.forEach(function (d) {
                    d.x0 = d.x;
                    d.y0 = d.y;
                });
            }

            var revisionHistoryTitle = $translate.instant("REVISION_HISTORY_TITLE");
            vm.showItemRevisionHistory = showItemRevisionHistory;
            function showItemRevisionHistory(item) {
                var options = {
                    title: item.itemNumber + " - " + item.itemName + " " + revisionHistoryTitle,
                    template: 'app/desktop/modules/item/details/tabs/basic/itemRevisionHistoryView.jsp',
                    controller: 'ItemRevisionHistoryController as revHistoryVm',
                    resolve: 'app/desktop/modules/item/details/tabs/basic/itemRevisionHistoryController',
                    data: {
                        itemId: item.item,
                        revisionHistoryType: "ITEM"
                    },
                    width: 700,
                    showMask: true
                };

                $rootScope.showSidePanel(options);
            }


            vm.showItemDetails = showItemDetails;
            function showItemDetails(item) {
                $rootScope.selectedMasterItemId = null;
                $state.go("app.items.details", null, {reload: true});
                $state.go('app.items.details', {itemId: item.latestRevision, tab: 'details.basic'});
            }

            $scope.limit = 30;
            $scope.increaseLimit = function () {
                if ($scope.limit < vm.bomItems.length) {
                    $scope.limit += 30;
                    $timeout(function () {
                        calculateColumnWidthForSticky();
                    }, 200);
                }
            };
            $scope.bollupLimit = 30;
            $scope.increaseRollupLimit = function () {
                if ($scope.bollupLimit < vm.bomRollupItems.length) {
                    $scope.bollupLimit += 30;
                    $timeout(function () {
                        rollupReportSticky();
                    }, 500);
                }
            };
            $scope.whereUsedLimit = 30;
            $scope.increaseWhereUsedLimit = function () {
                if ($scope.whereUsedLimit < vm.bomWhereUsedItems.length) {
                    $scope.whereUsedLimit += 30;
                }
            };
            vm.lifecyclePhases = [];
            function loadItemTypeLifecycles() {
                vm.lifecyclePhases = [];
                ItemTypeService.getItemTypeLifecycles().then(
                    function (data) {
                        vm.itemTypeLifecycles = data;
                        angular.forEach(vm.itemTypeLifecycles, function (lifecycle) {
                            angular.forEach(lifecycle.phases, function (phase) {
                                if (vm.lifecyclePhases.indexOf(phase.phase) == -1) {
                                    vm.lifecyclePhases.push(phase.phase);
                                }
                            })
                        })
                    }
                )
            }

            vm.selectedPhase = null;
            vm.onSelectPhase = onSelectPhase;
            function onSelectPhase(phase) {
                $rootScope.showBusyIndicator($('.view-container'));
                vm.selectedPhase = phase;
                searchBomItems($scope.bomSearch.searchQuery);

            }

            vm.clearPhase = clearPhase;
            function clearPhase() {
                $rootScope.showBusyIndicator($('.view-container'));
                vm.selectedPhase = null;
                searchBomItems($scope.bomSearch.searchQuery);
            }

            (function () {
                $scope.$on('app.item.tabactivated', function (event, data) {

                    $(window).resize(function () {
                        resizeBomConfigurationView();
                        $timeout(function () {
                            resizeCalculateColumnWidthForSticky();
                        }, 500)
                    });
                    ObjectAttributeService.getCurrencies().then(
                        function (data) {
                            vm.currencies = data;
                            angular.forEach(vm.currencies, function (data) {
                                currencyMap.put(data.id, $sce.trustAsHtml(data.symbol));
                            });
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    );
                    if (data.tabId == 'details.bom') {
                        if ($application.defaultRevisionConfiguration != undefined && $application.defaultRevisionConfiguration != null) {
                            vm.selectedBomRule = $application.defaultRevisionConfiguration.key;
                        }
                        loadItemTypeLifecycles();
                        $scope.bomSearch = {
                            searchQuery: null,
                            item: '',
                            fromDate: null,
                            toDate: null
                        }
                        watchDates();
                        vm.expandedAll = false;
                        $rootScope.freeTextQuerys = null;
                        if ($rootScope.selectedMasterItemId == null) {
                            $rootScope.masterDetailsEnable = false;
                            $rootScope.arrangeFreeTextSearch();
                        }
                        if ($rootScope.selectedMasterItemId != null) {
                            $stateParams.itemId = $rootScope.selectedMasterItemId;
                            vm.itemId = $stateParams.itemId;
                        }
                        if (validateJSON()) {
                            var setAttributes = JSON.parse($window.localStorage.getItem("bomAttributes"));
                        } else {
                            var setAttributes = null;
                        }
                        if (setAttributes != null && setAttributes != undefined) {
                            angular.forEach(setAttributes, function (setAtt) {
                                if (setAtt.id != null && setAtt.id != "" && setAtt.id != 0) {
                                    vm.objectIds.push(setAtt.id);
                                }
                            });
                            ObjectTypeAttributeService.getObjectTypeAttributesByIds(vm.objectIds).then(
                                function (data) {
                                    if (data.length == 0) {
                                        setAttributes = null;
                                        $window.localStorage.setItem("bomAttributes", "");
                                        vm.selectedBomAttributes = setAttributes
                                    } else {
                                        vm.selectedBomAttributes = setAttributes;
                                    }
                                    updateBomItemSequence();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        } else {
                            updateBomItemSequence();
                        }

                        dataAlreadyLoaded = true;
                    }
                });
                $scope.$on('app.item.importBom', function (event, data) {
                    importData();
                });
                $scope.$on('app.item.exportBom', function (event, data) {
                    exportData();
                });
                $scope.$on('app.bomItems.showAttributes', showBomAttributes);
                $scope.$on('app.bomItems.disableEdit', disableItems);
                $scope.$on('app.item.searchBom', function (event, data) {
                    searchBomItems(data.name);
                });
            })();
        }
    }
)
;