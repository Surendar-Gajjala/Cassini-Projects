define(
    [
        'app/desktop/desktop.app',
        'app/desktop/modules/settings/settings.module',
        'split-pane',
        'jquery.easyui',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/classificationService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/workflowDefinitionService',
        'app/shared/services/core/requirementsTypeService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/projectService',
        'app/shared/services/core/mesObjectTypeService',
        'app/desktop/modules/directives/mesResourceTypeDirective',
        'app/desktop/modules/directives/mroResourceTypeDirective',
        'app/desktop/modules/directives/pmObjectTypeDirective',
        'app/desktop/modules/directives/customObjectTypeDirective',
        'app/desktop/modules/workflow/directive/workflowtypeDirective',
        'app/desktop/modules/classification/workflowDirective/workflowManufacturerPartDirective',
        'app/desktop/modules/classification/workflowDirective/qualityWorkflowDirective',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/desktop/modules/classification/workflowDirective/workflowChangeDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService',
        'app/desktop/modules/classification/workflowDirective/itemClassificationDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/measurementService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/desktop/modules/classification/workflowDirective/workflowManufacturerDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/desktop/modules/classification/workflowDirective/workflowRequirementDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/customObjectTypeService',
        'app/shared/services/core/reqDocumentService',
        'app/shared/services/core/pmObjectTypeService'
    ],

    function (module) {
        module.directive('classificationAttributeView', ClassificationAttributeDirectiveController);

        function ClassificationAttributeDirectiveController($rootScope, $timeout, DialogService, $translate, ItemTypeService, ClassificationService, $window, MESObjectTypeService,
                                                            MfrService, RequirementsTypeService, CustomObjectService, QualityTypeService, MfrPartsService, ECOService, LovService,
                                                            MeasurementService, ObjectTypeAttributeService, ObjectAttributeService, CustomObjectTypeService, ProjectService, WorkflowDefinitionService, ReqDocumentService, PGCObjectTypeService) {
            return {
                templateUrl: 'app/desktop/modules/directives/classificationAttributesDirectiveView.jsp',
                restrict: 'E',
                replace: false,
                scope: {
                    'hasPermission': '='
                },
                link: function ($scope, $elem, attrs) {
                    $scope.autoNumbers = [];
                    $scope.revSequences = [];
                    $scope.lifecycleStates = [];
                    $scope.lifecycles = [];
                    $scope.lovs = [];

                    $scope.refTypes = ['ITEM', 'ITEMREVISION', 'CUSTOMOBJECT', 'CHANGE', 'QUALITY', 'MESOBJECT', 'MROOBJECT', 'WORKFLOW',
                        'MANUFACTURER', 'MANUFACTURERPART', 'PERSON', 'PROJECT', 'REQUIREMENTDOCUMENT', 'REQUIREMENT'];

                    $scope.addAttribute = addAttribute;
                    $scope.deleteAttribute = deleteAttribute;
                    $scope.applyChanges = applyChanges;
                    $scope.cancelChanges = cancelChanges;
                    $scope.editAttribute = editAttribute;

                    $scope.dataTypes = [
                        'TEXT',
                        'LONGTEXT',
                        'RICHTEXT',
                        'INTEGER',
                        'DOUBLE',
                        'DATE',
                        'BOOLEAN',
                        'LIST',
                        'MULTILISTCHECKBOX',
                        'TIME',
                        'TIMESTAMP',
                        'CURRENCY',
                        'OBJECT',
                        'IMAGE',
                        'ATTACHMENT',
                        'HYPERLINK',
                        'FORMULA'
                    ];

                    var newAttribute = {
                        id: null,
                        name: null,
                        description: null,
                        dataType: 'STRING',
                        /*configurable:false,*/
                        revisionSpecific: false,
                        changeControlled: false,
                        required: false,
                        visible: true,
                        objectType: null,
                        editMode: true,
                        showValues: false,
                        defaultValue: false,
                        refType: null,
                        isNew: true,
                        formula: null,
                        lov: null,
                        measurement: null,
                        attributeGroup: null,
                        newGroup: false,
                        refSubType: null,
                        showInTable: true
                    };


                    $scope.error = "";
                    var parsed = angular.element("<div></div>");
                    var attributeUsedInExclusion = parsed.html($translate.instant("ATTRIBUTE_USED_IN_EXCLUSION")).html();
                    var pleaseSaveNewLov = parsed.html($translate.instant("PLEASE_SAVE_NEW_LOV")).html();
                    var enterOneListValue = parsed.html($translate.instant("ENTER_ONE_LIST_VALUE")).html();
                    var enterListValue = parsed.html($translate.instant("ENTER_LIST_VALUE")).html();
                    var valueCannotBe = parsed.html($translate.instant("VALUE_CANNOT_BE")).html();
                    var enterValueListName = parsed.html($translate.instant("ENTER_VALUE_LIST_NAME")).html();
                    var the_notAllowed = parsed.html($translate.instant("THE_NOT_ALLOWED")).html();
                    var specialCharacterNotAllowed = parsed.html($translate.instant("SPECIAL_CHARACTER_NOT_ALLOWED")).html();
                    var commaNotAllowed = parsed.html($translate.instant("COMMA_NOT_ALLOWED")).html();
                    var valueAlreadyExist = parsed.html($translate.instant("VALUE_ALREADY_EXIST")).html();
                    var deleteAttributeDialogTitle = parsed.html($translate.instant("DELETE_ATTRIBUTE_DIALOG_TITLE")).html();
                    var deleteAttributeDialogMessage = parsed.html($translate.instant("DELETE_ATTRIBUTE_DIALOG_MESSAGE")).html();
                    var deleteAttributeMessage = parsed.html($translate.instant("DELETE_ATTRIBUTE_MESSAGE")).html();
                    $scope.rulesButton = parsed.html($translate.instant("EXCLUSIONS_BUTTON")).html();
                    $scope.classificationTypeJson = new Map();
                    $scope.classificationTypeJsonReflection = new Map();

                    $scope.configurableAttributes = [];
                    function loadAttributes() {
                        if ($rootScope.selectedClassificationType.objectType == 'PLMWORKFLOWDEFINITIONSTATUS') {
                            newAttribute.objectType = $rootScope.selectedClassificationType.objectType;
                            WorkflowDefinitionService.getWorkflowStatusAttributes($rootScope.selectedClassificationType.id).then(
                                function (data) {
                                    $scope.configurableAttributes = [];
                                    angular.forEach(data, function (attribute) {
                                        attribute.showValues = true;
                                        attribute.editMode = false;
                                        attribute.defaultValue = false;
                                        if (attribute.validations != null && attribute.validations != "") {
                                            attribute.newValidations = JSON.parse(attribute.validations);
                                        }
                                        if (attribute.configurable) {
                                            $scope.configurableAttributes.push(attribute);
                                        }
                                    });
                                    $rootScope.selectedClassificationType.attributes = data;
                                    $rootScope.hideBusyIndicator();
                                    $rootScope.hideSidePanel();
                                }, function (error) {
                                    $rootScope.formDataErrorNotification.show();
                                    $rootScope.formDataErrorNotification.removeClass('zoomOut');
                                    $rootScope.formDataErrorNotification.addClass('zoomIn');
                                    $rootScope.formDataNotificationClass = "fa-ban";
                                    $rootScope.formDataNotificationBackground = "alert-danger";
                                    $rootScope.formDataMessage = error.message;
                                    $timeout(function () {
                                        closeFormDataErrorNotification();
                                    }, 3000);
                                }
                            );
                        } else if ($rootScope.selectedClassificationType.objectType != 'CUSTOMOBJECTTYPE') {
                            newAttribute.objectType = $rootScope.selectedClassificationType.objectType;
                            ClassificationService.getAttributes($rootScope.selectedClassificationType.objectType, $rootScope.selectedClassificationType.id).then(
                                function (data) {
                                    $scope.configurableAttributes = [];
                                    angular.forEach(data, function (attribute) {
                                        attribute.showValues = true;
                                        attribute.editMode = false;
                                        attribute.defaultValue = false;
                                        if (attribute.validations != null && attribute.validations != "") {
                                            attribute.newValidations = JSON.parse(attribute.validations);
                                        }
                                        if (attribute.configurable) {
                                            $scope.configurableAttributes.push(attribute);
                                        }
                                    });
                                    $rootScope.selectedClassificationType.attributes = data;
                                    $rootScope.hideBusyIndicator();
                                    $rootScope.hideSidePanel();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            );
                        } else if ($rootScope.selectedClassificationType.objectType == 'CUSTOMOBJECTTYPE') {
                            newAttribute.objectType = $rootScope.selectedClassificationType.objectType;
                            ClassificationService.getCustomObjectAttributes($rootScope.selectedClassificationType.id).then(
                                function (data) {
                                    $scope.configurableAttributes = [];
                                    angular.forEach(data, function (attribute) {
                                        attribute.showValues = true;
                                        attribute.editMode = false;
                                        attribute.defaultValue = false;
                                        if (attribute.configurable) {
                                            $scope.configurableAttributes.push(attribute);
                                        }
                                    });
                                    $rootScope.selectedClassificationType.attributes = data;
                                    $rootScope.hideBusyIndicator();
                                    $rootScope.hideSidePanel();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            );
                        }
                    }

                    $scope.attributesForTable = [];
                    $scope.attributesForExclusion = [];
                    $scope.attributesForTable1 = [];
                    $scope.provideExclusionRules = provideExclusionRules;
                    $scope.concatArray = {firstItem: [], secondItem: []};
                    $scope.attributesForTable2 = [];
                    $scope.attributesForTable3 = [];
                    $scope.obj = {value: null, matching: false};
                    $scope.concatedFinalArray = [];
                    $scope.keyvalObj = {id: null, key: null, value: null, exclude: false};

                    $scope.finalValues = [];
                    $scope.nameHeaders = [];
                    $scope.values = [];
                    $scope.attributesForExclusion = [];
                    $scope.obj = {name: null, values: []};
                    $scope.attributes = [];
                    function provideExclusionRules() {
                        $rootScope.showBusyIndicator();
                        $scope.attributes = [];
                        $scope.nameHeaders = [];
                        $scope.values = [];
                        $scope.attributesForExclusion = [];
                        $scope.finalValues = [];
                        ClassificationService.getExclusionAttributes($rootScope.selectedClassificationType.objectType, $rootScope.selectedClassificationType.id).then(
                            function (data) {
                                $scope.attributesForExclusion = data.lovsForList;
                                angular.forEach(data.lovsForList, function (values, key) {
                                    var valuesForMap = values.lov.values;
                                    var i = values.id;
                                    var key1 = key;
                                    $scope.obj = {name: null, values: []};
                                    $scope.obj.name = key;
                                    $scope.obj.values = valuesForMap;
                                    $scope.attributes.push($scope.obj);

                                    var header = {name: key, numValues: valuesForMap.length};
                                    $scope.nameHeaders.push(header);

                                    angular.forEach(valuesForMap, function (val) {
                                        $scope.keyvalObj = {id: null, key: null, value: null, exclude: false};
                                        $scope.keyvalObj.id = i;
                                        $scope.keyvalObj.key = key1;
                                        $scope.keyvalObj.value = val;
                                        $scope.finalValues.push($scope.keyvalObj);
                                        $scope.values.push(val);
                                    })

                                });
                                $timeout(function () {
                                    $('#myModal1').modal('show');
                                    $rootScope.hideBusyIndicator();
                                }, 1000)

                            }, (function (error) {
                                $rootScope.hideBusyIndicator();
                                $rootScope.showWarningMessage(parsed.html(error.message).html());
                            })
                        );

                    }

                    $scope.shouldAddRowHeader = shouldAddRowHeader;
                    function shouldAddRowHeader(index) {
                        if (index == 0) {
                            return [$scope.attributes[0].name, $scope.attributes[0].values.length];
                        }
                        var temp = 0;
                        for (var i = 0; i < $scope.attributes.length; i++) {
                            var values = $scope.attributes[i].values;

                            temp = temp + values.length;
                            if (index == temp) {
                                return [$scope.attributes[i + 1].name, $scope.attributes[i + 1].values.length];
                            }

                        }

                        return ["", 0];
                    }

                    $scope.jsonObjectForExclusion = []

                    $scope.createExclusionObj = createExclusionObj;
                    $scope.checkExclude = checkExclude;
                    $scope.json = [];
                    $scope.backupVerticalObj = null;
                    $scope.backupHorizantalObj = null;
                    var map = new Map();
                    var map1 = new Map();

                    $rootScope.showBusyIndicator = function (parent) {
                        var w = null;
                        var h = null;
                        if (parent != null && parent != undefined) {
                            var pos = $(parent).offset();
                            w = $(parent).outerWidth();
                            h = $(parent).outerHeight();

                            $('#busy-indicator').css({top: pos.top, left: pos.left, width: w, height: h})
                        }
                        else {
                            w = $(window).outerWidth();
                            h = $(window).outerHeight();
                            $('#busy-indicator').css({top: 0, left: 0, width: w, height: h})
                        }
                        $('#busy-indicator').show();
                    };

                    $rootScope.hideBusyIndicator = function () {
                        $('#busy-indicator').hide();
                    };

                    $scope.exclSaveMessage = null;
                    $scope.attecl = false;
                    $scope.historyObj = {
                        combination: null,
                        added: false,
                        objId: null,
                        exclusion: false
                    }
                    function createExclusionObj(horizant, hIndex, vertical, vIndex) {
                        $scope.historyObj = {
                            combination: null,
                            added: false,
                            objId: null,
                            exclusion: false
                        }
                        $rootScope.showBusyIndicator();
                        $scope.attecl = false;
                        $scope.exclSaveMessage = null;
                        var esclAddedMsg = parsed.html($translate.instant("EXCL_ADDED_MESSAGE")).html();
                        var esclRmvdMsg = parsed.html($translate.instant("EXCL_REMOVED_MESSAGE")).html();
                        if ($scope.classificationTypeJson != null) {
                            if ($scope.classificationTypeJson.size > 0) {

                                if ($scope.classificationTypeJson.has(horizant.value + "_" + vertical.value + "_" + horizant.key + vertical.key)) {
                                    $scope.classificationTypeJson.delete(horizant.value + "_" + vertical.value + "_" + horizant.key + vertical.key);
                                    $scope.attecl = true;
                                    $scope.exclSaveMessage = "[" + horizant.value + " - " + vertical.value + "] " + esclRmvdMsg;
                                    $scope.historyObj.combination = "[" + horizant.value + " - " + vertical.value + "] ";
                                    $scope.historyObj.exclusion = true;
                                    $scope.historyObj.added = false;
                                }
                                else if ($scope.classificationTypeJson.has(vertical.value + "_" + horizant.value + "_" + vertical.key + horizant.key)) {
                                    $scope.attecl = true;
                                    $scope.classificationTypeJson.delete(vertical.value + "_" + horizant.value + "_" + vertical.key + horizant.key);
                                    $scope.exclSaveMessage = "[" + vertical.value + " - " + horizant.value + "] " + esclRmvdMsg;
                                    $scope.historyObj.combination = "[" + vertical.value + " - " + horizant.value + "] ";
                                    $scope.historyObj.exclusion = true;
                                    $scope.historyObj.added = false;
                                }

                                else {
                                    $scope.classificationTypeJson.set(horizant.value + "_" + vertical.value + "_" + horizant.key + vertical.key, [horizant, vertical]);
                                    $scope.attecl = true;
                                    $scope.exclSaveMessage = "[" + horizant.value + " - " + vertical.value + "] " + esclAddedMsg;
                                    $scope.historyObj.combination = "[" + horizant.value + " - " + vertical.value + "] ";
                                    $scope.historyObj.exclusion = true;
                                    $scope.historyObj.added = true;
                                }
                            }
                            else {
                                $scope.classificationTypeJson.set(horizant.value + "_" + vertical.value + "_" + horizant.key + vertical.key, [horizant, vertical]);
                                $scope.attecl = true;
                                $scope.exclSaveMessage = "[" + horizant.value + " - " + vertical.value + "] " + esclAddedMsg;
                                $scope.historyObj.combination = "[" + horizant.value + " - " + vertical.value + "] ";
                                $scope.historyObj.exclusion = true;
                                $scope.historyObj.added = true;
                            }
                        }
                        else {
                            $scope.classificationTypeJson = new Map();
                            $scope.classificationTypeJson.set(horizant.value + "_" + vertical.value + "_" + horizant.key + vertical.key, [horizant, vertical]);
                            $scope.attecl = true;
                            $scope.exclSaveMessage = "[" + horizant.value + " - " + vertical.value + "] " + esclAddedMsg;
                            $scope.historyObj.combination = "[" + horizant.value + " - " + vertical.value + "] ";
                            $scope.historyObj.exclusion = true;
                            $scope.historyObj.added = true;
                        }
                        $scope.classificationType.excluRules = JSON.stringify(Array.from($scope.classificationTypeJson.entries()));

                        ClassificationService.updateType($scope.classificationType.objectType, $scope.classificationType).then(
                            function (data) {
                                $scope.classificationTypeJson = new Map();
                                $scope.classificationTypeJson = new Map(JSON.parse(data.excluRules));
                                $scope.historyObj.objId = $scope.classificationType.id;
                                $timeout(function () {
                                    $scope.attecl = false;
                                }, 5000)
                                // $rootScope.showSuccessMessage($scope.exclSaveMessage);
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            });
                    }

                    function checkExclude(horizant, vertical) {
                        //$rootScope.showBusyIndicator();
                        var color = "normal";

                        if ($scope.classificationTypeJson != null) {
                            if ($scope.classificationTypeJson.size > 0) {
                                if ($scope.classificationTypeJson.has(horizant.value + "_" + vertical.value + "_" + horizant.key + vertical.key)) {
                                    color = "colorAdding";

                                }
                                else if ($scope.classificationTypeJson.has(vertical.value + "_" + horizant.value + "_" + vertical.key + horizant.key)) {
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
                        //$rootScope.hideBusyIndicator();

                        return color;
                    }


                    function addAttribute() {
                        var att = angular.copy(newAttribute);
                        if ($scope.attributeGroups.length == 0) {
                            att.newGroup = true;
                        }
                        if ($rootScope.selectedClassificationType.objectType == 'ITEMTYPE') att.itemType = $rootScope.selectedClassificationType.id;
                        if ($rootScope.selectedClassificationType.objectType == 'CHANGETYPE') att.changeType = $rootScope.selectedClassificationType.id;
                        if ($rootScope.selectedClassificationType.objectType == 'PLMWORKFLOWDEFINITIONSTATUS') att.workflowActivity = $rootScope.selectedClassificationType.id;
                        if ($rootScope.selectedClassificationType.objectType == 'QUALITY_TYPE') att.type = $rootScope.selectedClassificationType.id;
                        if ($rootScope.selectedClassificationType.objectType == 'MANUFACTURERTYPE') att.mfrType = $rootScope.selectedClassificationType.id;
                        if ($rootScope.selectedClassificationType.objectType == 'MANUFACTURERPARTTYPE') att.mfrPartType = $rootScope.selectedClassificationType.id;
                        if ($rootScope.selectedClassificationType.objectType == 'WORKFLOWTYPE') att.workflowType = $rootScope.selectedClassificationType.id;
                        if ($rootScope.selectedClassificationType.objectType == 'PLANTTYPE') att.type = $rootScope.selectedClassificationType.id;
                        if ($rootScope.selectedClassificationType.objectType == 'ASSEMBLYLINETYPE') att.type = $rootScope.selectedClassificationType.id;
                        if ($rootScope.selectedClassificationType.objectType == 'WORKCENTERTYPE') att.type = $rootScope.selectedClassificationType.id;
                        if ($rootScope.selectedClassificationType.objectType == 'MACHINETYPE') att.type = $rootScope.selectedClassificationType.id;
                        if ($rootScope.selectedClassificationType.objectType == 'TOOLTYPE') att.type = $rootScope.selectedClassificationType.id;
                        if ($rootScope.selectedClassificationType.objectType == 'MATERIALTYPE') att.type = $rootScope.selectedClassificationType.id;
                        if ($rootScope.selectedClassificationType.objectType == 'JIGFIXTURETYPE') att.type = $rootScope.selectedClassificationType.id;
                        if ($rootScope.selectedClassificationType.objectType == 'OPERATIONTYPE') att.type = $rootScope.selectedClassificationType.id;
                        if ($rootScope.selectedClassificationType.objectType == 'MBOMTYPE') att.type = $rootScope.selectedClassificationType.id;
                        if ($rootScope.selectedClassificationType.objectType == 'BOPTYPE') att.type = $rootScope.selectedClassificationType.id;
                        if ($rootScope.selectedClassificationType.objectType == 'PRODUCTIONORDERTYPE') att.type = $rootScope.selectedClassificationType.id;
                        if ($rootScope.selectedClassificationType.objectType == 'SERVICEORDERTYPE') att.type = $rootScope.selectedClassificationType.id;
                        if ($rootScope.selectedClassificationType.objectType == 'MANPOWERTYPE') att.type = $rootScope.selectedClassificationType.id;
                        if ($rootScope.selectedClassificationType.objectType == 'EQUIPMENTTYPE') att.type = $rootScope.selectedClassificationType.id;
                        if ($rootScope.selectedClassificationType.objectType == 'INSTRUMENTTYPE') att.type = $rootScope.selectedClassificationType.id;
                        if ($rootScope.selectedClassificationType.objectType == 'SPAREPARTTYPE') att.type = $rootScope.selectedClassificationType.id;
                        if ($rootScope.selectedClassificationType.objectType == 'ASSETTYPE') att.type = $rootScope.selectedClassificationType.id;
                        if ($rootScope.selectedClassificationType.objectType == 'METERTYPE') att.type = $rootScope.selectedClassificationType.id;
                        if ($rootScope.selectedClassificationType.objectType == 'WORKREQUESTTYPE') att.type = $rootScope.selectedClassificationType.id;
                        if ($rootScope.selectedClassificationType.objectType == 'WORKORDERTYPE') att.type = $rootScope.selectedClassificationType.id;
                        if ($rootScope.selectedClassificationType.objectType == 'CUSTOMOBJECTTYPE') att.customObjectType = $rootScope.selectedClassificationType.id;
                        if ($rootScope.selectedClassificationType.objectType == 'REQUIREMENTTYPE') att.type = $rootScope.selectedClassificationType.id;
                        if ($rootScope.selectedClassificationType.objectType == 'REQUIREMENTDOCUMENTTYPE') att.type = $rootScope.selectedClassificationType.id;
                        if ($rootScope.selectedClassificationType.objectType == 'PMOBJECTTYPE') {
                            att.type = $rootScope.selectedClassificationType.id;
                            att.objectType = $rootScope.selectedClassificationType.type;
                        }
                        if ($rootScope.selectedClassificationType.objectType == 'SUPPLIERTYPE') att.type = $rootScope.selectedClassificationType.id;
                        if ($rootScope.selectedClassificationType.objectType == 'PGCDECLARATIONTYPE') att.type = $rootScope.selectedClassificationType.id;
                        if ($rootScope.selectedClassificationType.objectType == 'PGCSUBSTANCETYPE') att.type = $rootScope.selectedClassificationType.id;
                        if ($rootScope.selectedClassificationType.objectType == 'PGCSUBSTANCEGROUPTYPE') att.type = $rootScope.selectedClassificationType.id;
                        if ($rootScope.selectedClassificationType.objectType == 'PGCSPECIFICATIONTYPE') att.type = $rootScope.selectedClassificationType.id;
                        if ($rootScope.selectedClassificationType.objectType == 'REQUIREMENTTYPE' || $rootScope.selectedClassificationType.objectType == 'SPECIFICATIONTYPE') {
                            att.rmObjectType = $rootScope.selectedClassificationType.id;
                            att.objectType = $rootScope.selectedClassificationType.objectType;
                        }
                        if ($rootScope.selectedClassificationType.attributes != undefined) {
                            $rootScope.selectedClassificationType.attributes.unshift(att);
                            $scope.unSavedAttributes.unshift(att);
                        } else {
                            $rootScope.selectedClassificationType.attributes = [];
                            $rootScope.selectedClassificationType.attributes.unshift(att);
                            $scope.unSavedAttributes.unshift(att);
                        }
                    }

                    $scope.keyArray = [];

                    function getKeysFromMap(obj, value) {
                        $scope.keyArray = [];

                        angular.forEach(obj, function (values, key) {
                            angular.forEach(values, function (val) {
                                if (value == val.id) {
                                    $scope.keyArray.push(key);
                                }
                            })

                        });
                        return $scope.keyArray;
                    }

                    function deleteAttribute(attribute) {
                        var id = $rootScope.selectedClassificationType.id;
                        if ($rootScope.selectedClassificationType.objectType == 'ITEMTYPE') {
                            ItemTypeService.getAttributeValues(attribute.id).then(
                                function (data) {
                                    if (data.length == 0) {
                                        if (attribute.dataType == "LIST" && attribute.configurable == true && $rootScope.selectedClassificationTypeJson != null && $rootScope.selectedClassificationTypeJson.size > 0) {
                                            var key = getKeysFromMap($scope.classificationTypeJson, attribute.id);
                                            if ($scope.keyArray.length > 0) {
                                                var options1 = {
                                                    title: $rootScope.deleteAttributeDialogTitle,
                                                    message: "The attribute already used in exclusion rules, Are you sure want to delete?",
                                                    okButtonClass: 'btn-danger'
                                                };
                                                var i = 0;
                                                DialogService.confirm(options1, function (yes) {
                                                    if (yes == true) {
                                                        $rootScope.showBusyIndicator();
                                                        angular.forEach($scope.keyArray, function (val) {
                                                            i++;
                                                            if ($scope.classificationTypeJson.has(val)) {
                                                                $scope.classificationTypeJson.delete(val);
                                                            }
                                                        })
                                                        if ($scope.keyArray.length == i) {
                                                            $scope.classificationType.excluRules = JSON.stringify(Array.from($scope.classificationTypeJson.entries()));
                                                            ClassificationService.updateType($scope.classificationType.objectType, $scope.classificationType).then(
                                                                function (data) {
                                                                    $scope.classificationTypeJson = new Map();
                                                                    $scope.classificationTypeJson = new Map(JSON.parse(data.excluRules));
                                                                    if (attribute.id != null && attribute.id != undefined) {
                                                                        ClassificationService.deleteAttribute($rootScope.selectedClassificationType.objectType, $rootScope.selectedClassificationType.id, attribute.id).then(
                                                                            function (data) {
                                                                                loadAttributes();
                                                                                loadAttributeGroups();
                                                                                $rootScope.selectedClassificationType.id = id;
                                                                                $rootScope.selectedClassificationType.attributes.splice($rootScope.selectedClassificationType.attributes.indexOf(attribute), 1);
                                                                                $rootScope.showSuccessMessage($rootScope.deleteAttributeMessage);
                                                                                $rootScope.hideBusyIndicator();
                                                                            }, function (error) {
                                                                                $rootScope.showErrorMessage(error.message);
                                                                            }
                                                                        )
                                                                        var typeAttributes = JSON.parse($window.localStorage.getItem("attributes"));
                                                                        angular.forEach(typeAttributes, function (typeAttribute) {
                                                                            if (typeAttribute.id == attribute.id) {
                                                                                typeAttributes.remove(typeAttribute);
                                                                                $window.localStorage.setItem("attributes", JSON.stringify(typeAttributes));
                                                                            }
                                                                        })
                                                                    }
                                                                }, function (error) {
                                                                    $rootScope.showErrorMessage(error.message);
                                                                });
                                                        }


                                                    }
                                                    else {
                                                        $scope.keyArray = [];
                                                    }
                                                });
                                            } else {
                                                var options = {
                                                    title: $rootScope.deleteAttributeDialogTitle,
                                                    message: $rootScope.deleteAttributeDialogMessage,
                                                    okButtonClass: 'btn-danger'
                                                };
                                                DialogService.confirm(options, function (yes) {
                                                    if (yes == true) {
                                                        if (attribute.id != null && attribute.id != undefined) {
                                                            ClassificationService.deleteAttribute($rootScope.selectedClassificationType.objectType, $rootScope.selectedClassificationType.id, attribute.id).then(
                                                                function (data) {
                                                                    /*loadAttributes();*/
                                                                    loadAttributeGroups();
                                                                    $rootScope.selectedClassificationType.id = id;
                                                                    $rootScope.selectedClassificationType.attributes.splice($rootScope.selectedClassificationType.attributes.indexOf(attribute), 1);
                                                                    $rootScope.showSuccessMessage($rootScope.deleteAttributeMessage);
                                                                }, function (error) {
                                                                    $rootScope.showErrorMessage(error.message);
                                                                }
                                                            )
                                                            var typeAttributes = JSON.parse($window.localStorage.getItem("attributes"));
                                                            angular.forEach(typeAttributes, function (typeAttribute) {
                                                                if (typeAttribute.id == attribute.id) {
                                                                    typeAttributes.remove(typeAttribute);
                                                                    $window.localStorage.setItem("attributes", JSON.stringify(typeAttributes));
                                                                }
                                                            })
                                                        }
                                                    }
                                                });
                                            }
                                        } else {
                                            var options = {
                                                title: $rootScope.deleteAttributeDialogTitle,
                                                message: $rootScope.deleteAttributeDialogMessage,
                                                okButtonClass: 'btn-danger'
                                            };
                                            DialogService.confirm(options, function (yes) {
                                                if (yes == true) {
                                                    if (attribute.id != null && attribute.id != undefined) {
                                                        ClassificationService.deleteAttribute($rootScope.selectedClassificationType.objectType, $rootScope.selectedClassificationType.id, attribute.id).then(
                                                            function (data) {
                                                                /*loadAttributes();*/
                                                                loadAttributeGroups();
                                                                $rootScope.selectedClassificationType.id = id;
                                                                $rootScope.selectedClassificationType.attributes.splice($rootScope.selectedClassificationType.attributes.indexOf(attribute), 1);
                                                                $rootScope.showSuccessMessage($rootScope.deleteAttributeMessage);
                                                            }, function (error) {
                                                                $rootScope.showErrorMessage(error.message);
                                                            }
                                                        )
                                                        var typeAttributes = JSON.parse($window.localStorage.getItem("attributes"));
                                                        angular.forEach(typeAttributes, function (typeAttribute) {
                                                            if (typeAttribute.id == attribute.id) {
                                                                typeAttributes.remove(typeAttribute);
                                                                $window.localStorage.setItem("attributes", JSON.stringify(typeAttributes));
                                                            }
                                                        })
                                                    }
                                                }
                                            });
                                        }
                                    } else {
                                        $rootScope.showWarningMessage(attribute.name + " : Attribute already in use");
                                    }
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                        if ($rootScope.selectedClassificationType.objectType == 'CHANGETYPE') {
                            ECOService.getEcoAttributeValues(attribute.id).then(
                                function (data) {
                                    if (data.length == 0) {
                                        var options = {
                                            title: $rootScope.deleteAttributeDialogTitle,
                                            message: $rootScope.deleteAttributeDialogMessage,
                                            okButtonClass: 'btn-danger'
                                        };
                                        DialogService.confirm(options, function (yes) {
                                            if (yes == true) {
                                                if (attribute.id != null && attribute.id != undefined) {
                                                    ClassificationService.deleteAttribute($rootScope.selectedClassificationType.objectType, $rootScope.selectedClassificationType.id, attribute.id).then(
                                                        function (data) {
                                                            /*loadAttributes();*/
                                                            loadAttributeGroups();
                                                            $rootScope.selectedClassificationType.id = id;
                                                            $rootScope.selectedClassificationType.attributes.splice($rootScope.selectedClassificationType.attributes.indexOf(attribute), 1);
                                                            $rootScope.showSuccessMessage($rootScope.deleteAttributeMessage);
                                                        }, function (error) {
                                                            $rootScope.showErrorMessage(error.message);
                                                        }
                                                    )
                                                    var changeAttributes = JSON.parse($window.localStorage.getItem("changeAttributes"));
                                                    angular.forEach(changeAttributes, function (changeAttribute) {
                                                        if (changeAttribute.id == attribute.id) {
                                                            changeAttributes.remove(changeAttribute);
                                                            $window.localStorage.setItem("changeAttributes", JSON.stringify(changeAttributes));
                                                        }
                                                    })
                                                }
                                            }
                                        });
                                    } else {
                                        $rootScope.showWarningMessage(attribute.name + " : Attribute already in use");
                                    }
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            );
                        } else if ($rootScope.selectedClassificationType.objectType == 'PLMWORKFLOWDEFINITIONSTATUS') {
                            var options = {
                                title: deleteAttributeDialogTitle,
                                message: deleteAttributeDialogMessage,
                                okButtonClass: 'btn-danger'
                            };
                            DialogService.confirm(options, function (yes) {
                                if (yes == true) {
                                    if (attribute.id != null && attribute.id != undefined) {
                                        $rootScope.showBusyIndicator($('#workflow-form-data'));
                                        WorkflowDefinitionService.deleteWorkflowStatusAttribute($rootScope.selectedClassificationType.id, attribute.id).then(
                                            function (data) {
                                                loadAttributeGroups();
                                                $rootScope.selectedClassificationType.id = id;
                                                $rootScope.selectedClassificationType.attributes.splice($rootScope.selectedClassificationType.attributes.indexOf(attribute), 1);

                                                $rootScope.formDataErrorNotification.show();
                                                $rootScope.formDataErrorNotification.removeClass('zoomOut');
                                                $rootScope.formDataErrorNotification.addClass('zoomIn');
                                                $rootScope.formDataNotificationClass = "fa-check";
                                                $rootScope.formDataNotificationBackground = "alert-success";
                                                $rootScope.formDataMessage = deleteAttributeMessage;
                                                $timeout(function () {
                                                    closeFormDataErrorNotification();
                                                }, 3000);
                                            }, function (error) {
                                                $rootScope.formDataErrorNotification.show();
                                                $rootScope.formDataErrorNotification.removeClass('zoomOut');
                                                $rootScope.formDataErrorNotification.addClass('zoomIn');
                                                $rootScope.formDataNotificationClass = "fa-ban";
                                                $rootScope.formDataNotificationBackground = "alert-danger";
                                                $rootScope.formDataMessage = error.message;
                                                $timeout(function () {
                                                    closeFormDataErrorNotification();
                                                }, 3000);
                                            }
                                        )
                                    }
                                }
                            });
                        }
                        if ($rootScope.selectedClassificationType.objectType == 'QUALITY_TYPE') {
                            QualityTypeService.getUsedAttributeValues(attribute.id).then(
                                function (data) {
                                    if (data.length == 0) {
                                        var options = {
                                            title: $rootScope.deleteAttributeDialogTitle,
                                            message: $rootScope.deleteAttributeDialogMessage,
                                            okButtonClass: 'btn-danger'
                                        };
                                        DialogService.confirm(options, function (yes) {
                                            if (yes == true) {
                                                if (attribute.id != null && attribute.id != undefined) {
                                                    ClassificationService.deleteAttribute($rootScope.selectedClassificationType.objectType, $rootScope.selectedClassificationType.id, attribute.id).then(
                                                        function (data) {
                                                            /* loadAttributes();*/
                                                            loadAttributeGroups();
                                                            $rootScope.selectedClassificationType.id = id;
                                                            $rootScope.selectedClassificationType.attributes.splice($rootScope.selectedClassificationType.attributes.indexOf(attribute), 1);
                                                            $rootScope.showSuccessMessage($rootScope.deleteAttributeMessage);
                                                        },
                                                        function (error) {
                                                            $rootScope.showErrorMessage(error.message);
                                                        }
                                                    )
                                                }
                                            }
                                        });
                                    } else {
                                        $rootScope.showWarningMessage(attribute.name + " : Attribute already in use");
                                    }
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            );
                        }
                        if ($rootScope.selectedClassificationType.objectType == 'WORKFLOWTYPE') {
                            ObjectAttributeService.getAttributeValuesByDef(attribute.id).then(
                                function (data) {
                                    if (data.length == 0) {
                                        var options = {
                                            title: $rootScope.deleteAttributeDialogTitle,
                                            message: $rootScope.deleteAttributeDialogMessage,
                                            okButtonClass: 'btn-danger'
                                        };
                                        DialogService.confirm(options, function (yes) {
                                            if (yes == true) {
                                                if (attribute.id != null && attribute.id != undefined) {
                                                    ClassificationService.deleteAttribute('WORKFLOWTYPE', $rootScope.selectedClassificationType.id, attribute.id).then(
                                                        function (data) {
                                                            /* loadAttributes();*/
                                                            loadAttributeGroups();
                                                            $rootScope.selectedClassificationType.id = id;
                                                            $rootScope.selectedClassificationType.attributes.splice($rootScope.selectedClassificationType.attributes.indexOf(attribute), 1);
                                                            $rootScope.showSuccessMessage($rootScope.deleteAttributeMessage);
                                                        },
                                                        function (error) {
                                                            $rootScope.showErrorMessage(error.message);
                                                        }
                                                    )
                                                }
                                            }
                                        });
                                    } else {
                                        $rootScope.showWarningMessage(attribute.name + " : Attribute already in use");
                                    }
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            );
                        }
                        if ($rootScope.selectedClassificationType.objectType == 'REQUIREMENTTYPE' || $rootScope.selectedClassificationType.objectType == 'SPECIFICATIONTYPE') {
                            ReqDocumentService.getUsedRequirementTypeAttributeValues(attribute.id).then(
                                function (data) {
                                    if (data.length == 0) {
                                        var options = {
                                            title: $rootScope.deleteAttributeDialogTitle,
                                            message: $rootScope.deleteAttributeDialogMessage,
                                            okButtonClass: 'btn-danger'
                                        };
                                        DialogService.confirm(options, function (yes) {
                                            if (yes == true) {
                                                if (attribute.id != null && attribute.id != undefined) {

                                                    if ($rootScope.selectedClassificationType.objectType == 'REQUIREMENTTYPE') {
                                                        ClassificationService.deleteAttribute('REQUIREMENTTYPE', $rootScope.selectedClassificationType.id, attribute.id).then(
                                                            function (data) {
                                                                /*loadAttributes();*/
                                                                loadAttributeGroups();
                                                                $rootScope.selectedClassificationType.id = id;
                                                                $rootScope.selectedClassificationType.attributes.splice($rootScope.selectedClassificationType.attributes.indexOf(attribute), 1);
                                                                $rootScope.showSuccessMessage($rootScope.deleteAttributeMessage);
                                                            }, function (error) {
                                                                $rootScope.showErrorMessage(error.message);
                                                            }
                                                        )
                                                        var typeAttributes = JSON.parse($window.localStorage.getItem("attributes"));
                                                        angular.forEach(typeAttributes, function (typeAttribute) {
                                                            if (typeAttribute.id == attribute.id) {
                                                                typeAttributes.remove(typeAttribute);
                                                                $window.localStorage.setItem("attributes", JSON.stringify(typeAttributes));
                                                            }
                                                        })
                                                    }
                                                    else if ($rootScope.selectedClassificationType.objectType == 'SPECIFICATIONTYPE') {
                                                        ClassificationService.deleteAttribute('SPECIFICATIONTYPE', $rootScope.selectedClassificationType.id, attribute.id).then(
                                                            function (data) {
                                                                $rootScope.selectedClassificationType.id = id;
                                                                $rootScope.selectedClassificationType.attributes.splice($rootScope.selectedClassificationType.attributes.indexOf(attribute), 1);
                                                                $rootScope.showSuccessMessage($rootScope.deleteAttributeMessage);
                                                            }, function (error) {
                                                                $rootScope.showErrorMessage(error.message);
                                                            }
                                                        )
                                                        var typeAttributes = JSON.parse($window.localStorage.getItem("attributes"));
                                                        angular.forEach(typeAttributes, function (typeAttribute) {
                                                            if (typeAttribute.id == attribute.id) {
                                                                typeAttributes.remove(typeAttribute);
                                                                $window.localStorage.setItem("attributes", JSON.stringify(typeAttributes));
                                                            }
                                                        })
                                                    }

                                                }
                                            }
                                        });
                                    } else {
                                        $rootScope.showWarningMessage(attribute.name + " : Attribute already in use");
                                    }
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            );
                        }
                        if ($rootScope.selectedClassificationType.objectType == 'MANUFACTURERTYPE') {
                            MfrService.getMfrAttributeValues(attribute.id).then(
                                function (data) {
                                    if (data.length == 0) {
                                        var options = {
                                            title: $rootScope.deleteAttributeDialogTitle,
                                            message: $rootScope.deleteAttributeDialogMessage,
                                            okButtonClass: 'btn-danger'
                                        };
                                        DialogService.confirm(options, function (yes) {
                                            if (yes == true) {
                                                if (attribute.id != null && attribute.id != undefined) {
                                                    ClassificationService.deleteAttribute('MANUFACTURERTYPE', $rootScope.selectedClassificationType.id, attribute.id).then(
                                                        function (data) {
                                                            /*loadAttributes();*/
                                                            loadAttributeGroups();
                                                            $rootScope.selectedClassificationType.id = id;
                                                            $rootScope.selectedClassificationType.attributes.splice($rootScope.selectedClassificationType.attributes.indexOf(attribute), 1);
                                                            $rootScope.showSuccessMessage($rootScope.deleteAttributeMessage);
                                                        }, function (error) {
                                                            $rootScope.showErrorMessage(error.message);
                                                        }
                                                    )
                                                    var mfrAttributes = JSON.parse($window.localStorage.getItem("mfrAttributes"));
                                                    angular.forEach(mfrAttributes, function (mfrAttribute) {
                                                        if (mfrAttribute.id == attribute.id) {
                                                            mfrAttributes.remove(mfrAttribute);
                                                            $window.localStorage.setItem("mfrAttributes", JSON.stringify(mfrAttributes));
                                                        }
                                                    })
                                                }
                                            }
                                        });
                                    } else {
                                        $rootScope.showWarningMessage(attribute.name + " : Attribute already in use");
                                    }
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            );
                        }
                        if ($rootScope.selectedClassificationType.objectType == 'SUPPLIERTYPE') {
                            QualityTypeService.getUsedAttributeValues(attribute.id).then(
                                function (data) {
                                    if (data.length == 0) {
                                        var options = {
                                            title: $rootScope.deleteAttributeDialogTitle,
                                            message: $rootScope.deleteAttributeDialogMessage,
                                            okButtonClass: 'btn-danger'
                                        };
                                        DialogService.confirm(options, function (yes) {
                                            if (yes == true) {
                                                if (attribute.id != null && attribute.id != undefined) {
                                                    ClassificationService.deleteAttribute('SUPPLIERTYPE', $rootScope.selectedClassificationType.id, attribute.id).then(
                                                        function (data) {
                                                            /*loadAttributes();*/
                                                            loadAttributeGroups();
                                                            $rootScope.selectedClassificationType.id = id;
                                                            $rootScope.selectedClassificationType.attributes.splice($rootScope.selectedClassificationType.attributes.indexOf(attribute), 1);
                                                            $rootScope.showSuccessMessage($rootScope.deleteAttributeMessage);
                                                        }, function (error) {
                                                            $rootScope.showErrorMessage(error.message);
                                                        }
                                                    )
                                                    var mfrAttributes = JSON.parse($window.localStorage.getItem("mfrAttributes"));
                                                    angular.forEach(mfrAttributes, function (mfrAttribute) {
                                                        if (mfrAttribute.id == attribute.id) {
                                                            mfrAttributes.remove(mfrAttribute);
                                                            $window.localStorage.setItem("mfrAttributes", JSON.stringify(mfrAttributes));
                                                        }
                                                    })
                                                }
                                            }
                                        });
                                    } else {
                                        $rootScope.showWarningMessage(attribute.name + " : Attribute already in use");
                                    }
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            );
                        }
                        if ($rootScope.selectedClassificationType.objectType == 'MANUFACTURERPARTTYPE') {
                            MfrPartsService.getMfrPartAttributeValues(attribute.id).then(
                                function (data) {
                                    if (data.length == 0) {
                                        var options = {
                                            title: $rootScope.deleteAttributeDialogTitle,
                                            message: $rootScope.deleteAttributeDialogMessage,
                                            okButtonClass: 'btn-danger'
                                        };
                                        DialogService.confirm(options, function (yes) {
                                            if (yes == true) {
                                                if (attribute.id != null && attribute.id != undefined) {
                                                    ClassificationService.deleteAttribute('MANUFACTURERPARTTYPE', $rootScope.selectedClassificationType.id, attribute.id).then(
                                                        function (data) {
                                                            /*loadAttributes();*/
                                                            loadAttributeGroups();
                                                            $rootScope.selectedClassificationType.id = id;
                                                            $rootScope.selectedClassificationType.attributes.splice($rootScope.selectedClassificationType.attributes.indexOf(attribute), 1);
                                                            $rootScope.showSuccessMessage($rootScope.deleteAttributeMessage);
                                                        }, function (error) {
                                                            $rootScope.showErrorMessage(error.message);
                                                        }
                                                    )
                                                    var mfrPartAttributes = JSON.parse($window.localStorage.getItem("mfrPartAttributes"));
                                                    angular.forEach(mfrPartAttributes, function (mfrPartAttribute) {
                                                        if (mfrPartAttribute.id == attribute.id) {
                                                            mfrPartAttributes.remove(mfrPartAttribute);
                                                            $window.localStorage.setItem("mfrPartAttributes", JSON.stringify(mfrPartAttributes));
                                                        }
                                                    })
                                                }
                                            }
                                        });
                                    } else {
                                        $rootScope.showWarningMessage(attribute.name + " : Attribute already in use");
                                    }
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            );
                        }
                        if ($rootScope.selectedClassificationType.objectType == 'CUSTOMOBJECTTYPE') {
                            MfrPartsService.getMfrPartAttributeValues(attribute.id).then(
                                function (data) {
                                    if (data.length == 0) {
                                        var options = {
                                            title: $rootScope.deleteAttributeDialogTitle,
                                            message: $rootScope.deleteAttributeDialogMessage,
                                            okButtonClass: 'btn-danger'
                                        };
                                        DialogService.confirm(options, function (yes) {
                                            if (yes == true) {
                                                if (attribute.id != null && attribute.id != undefined) {
                                                    CustomObjectTypeService.deleteAttribute($rootScope.selectedClassificationType.id, attribute.id).then(
                                                        function (data) {
                                                            /*loadAttributes();*/
                                                            loadAttributeGroups();
                                                            $rootScope.selectedClassificationType.id = id;
                                                            $rootScope.selectedClassificationType.attributes.splice($rootScope.selectedClassificationType.attributes.indexOf(attribute), 1);
                                                            $rootScope.showSuccessMessage($rootScope.deleteAttributeMessage);
                                                        }, function (error) {
                                                            $rootScope.showErrorMessage(error.message);
                                                        }
                                                    )
                                                    var customObjectAttributes = JSON.parse($window.localStorage.getItem("customObjectAttributes"));
                                                    angular.forEach(customObjectAttributes, function (plantAttribute) {
                                                        if (plantAttribute.id == attribute.id) {
                                                            customObjectAttributes.remove(plantAttribute);
                                                            $window.localStorage.setItem("customObjectAttributes", JSON.stringify(customObjectAttributes));
                                                        }
                                                    })
                                                }
                                            }
                                        });
                                    } else {
                                        $rootScope.showWarningMessage(attribute.name + " : Attribute already in use");
                                    }
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            );
                        }
                        if ($rootScope.selectedClassificationType.objectType == 'PLANTTYPE' || $rootScope.selectedClassificationType.objectType == 'MACHINETYPE' || $rootScope.selectedClassificationType.objectType == 'WORKCENTERTYPE'
                            || $rootScope.selectedClassificationType.objectType == 'TOOLTYPE' || $rootScope.selectedClassificationType.objectType == 'OPERATIONTYPE' || $rootScope.selectedClassificationType.objectType == 'MBOMTYPE' || $rootScope.selectedClassificationType.objectType == 'BOPTYPE' || $rootScope.selectedClassificationType.objectType == 'MANPOWERTYPE'
                            || $rootScope.selectedClassificationType.objectType == 'PRODUCTIONORDERTYPE' || $rootScope.selectedClassificationType.objectType == 'SERVICEORDERTYPE' || $rootScope.selectedClassificationType.objectType == 'MATERIALTYPE'
                            || $rootScope.selectedClassificationType.objectType == 'EQUIPMENTTYPE' || $rootScope.selectedClassificationType.objectType == 'INSTRUMENTTYPE' || $rootScope.selectedClassificationType.objectType == 'ASSEMBLYLINETYPE'
                            || $rootScope.selectedClassificationType.objectType == 'JIGFIXTURETYPE'
                        ) {
                            MESObjectTypeService.getUsedMesObjectAttributesValues(attribute.id).then(
                                function (data) {
                                    if (data.length == 0) {
                                        var options = {
                                            title: $rootScope.deleteAttributeDialogTitle,
                                            message: $rootScope.deleteAttributeDialogMessage,
                                            okButtonClass: 'btn-danger'
                                        };
                                        DialogService.confirm(options, function (yes) {
                                            if (yes == true) {
                                                if (attribute.id != null && attribute.id != undefined) {
                                                    ClassificationService.deleteAttribute($rootScope.selectedClassificationType.objectType, $rootScope.selectedClassificationType.id, attribute.id).then(
                                                        function (data) {
                                                            /*loadAttributes();*/
                                                            loadAttributeGroups();
                                                            $rootScope.selectedClassificationType.id = id;
                                                            $rootScope.selectedClassificationType.attributes.splice($rootScope.selectedClassificationType.attributes.indexOf(attribute), 1);
                                                            $rootScope.showSuccessMessage($rootScope.deleteAttributeMessage);
                                                        }, function (error) {
                                                            $rootScope.showErrorMessage(error.message);
                                                        }
                                                    )
                                                    var equipmentAttributes = JSON.parse($window.localStorage.getItem("equipmentAttributes"));
                                                    angular.forEach(equipmentAttributes, function (equipmentAttribute) {
                                                        if (equipmentAttribute.id == attribute.id) {
                                                            equipmentAttributes.remove(equipmentAttribute);
                                                            $window.localStorage.setItem("equipmentAttributes", JSON.stringify(equipmentAttributes));
                                                        }
                                                    })
                                                }
                                            }
                                        });
                                    } else {
                                        $rootScope.showWarningMessage(attribute.name + " : Attribute already in use");
                                    }
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            );
                        }
                        if ($rootScope.selectedClassificationType.objectType == 'ASSETTYPE' || $rootScope.selectedClassificationType.objectType == 'METERTYPE' || $rootScope.selectedClassificationType.objectType == 'SPAREPARTTYPE'
                            || $rootScope.selectedClassificationType.objectType == 'WORKREQUESTTYPE' || $rootScope.selectedClassificationType.objectType == 'WORKORDERTYPE'
                        ) {
                            MESObjectTypeService.getUsedMroObjectAttributesValues(attribute.id).then(
                                function (data) {
                                    if (data.length == 0) {
                                        var options = {
                                            title: $rootScope.deleteAttributeDialogTitle,
                                            message: $rootScope.deleteAttributeDialogMessage,
                                            okButtonClass: 'btn-danger'
                                        };
                                        DialogService.confirm(options, function (yes) {
                                            if (yes == true) {
                                                if (attribute.id != null && attribute.id != undefined) {
                                                    ClassificationService.deleteAttribute($rootScope.selectedClassificationType.objectType, $rootScope.selectedClassificationType.id, attribute.id).then(
                                                        function (data) {
                                                            /*loadAttributes();*/
                                                            loadAttributeGroups();
                                                            $rootScope.selectedClassificationType.id = id;
                                                            $rootScope.selectedClassificationType.attributes.splice($rootScope.selectedClassificationType.attributes.indexOf(attribute), 1);
                                                            $rootScope.showSuccessMessage($rootScope.deleteAttributeMessage);
                                                        }, function (error) {
                                                            $rootScope.showErrorMessage(error.message);
                                                        }
                                                    )
                                                    var equipmentAttributes = JSON.parse($window.localStorage.getItem("equipmentAttributes"));
                                                    angular.forEach(equipmentAttributes, function (equipmentAttribute) {
                                                        if (equipmentAttribute.id == attribute.id) {
                                                            equipmentAttributes.remove(equipmentAttribute);
                                                            $window.localStorage.setItem("equipmentAttributes", JSON.stringify(equipmentAttributes));
                                                        }
                                                    })
                                                }
                                            }
                                        });
                                    } else {
                                        $rootScope.showWarningMessage(attribute.name + " : Attribute already in use");
                                    }
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            );
                        }
                        if ($rootScope.selectedClassificationType.objectType == 'REQUIREMENTDOCUMENTTYPE') {
                            ReqDocumentService.getUsedRequirementTypeAttributeValues(attribute.id).then(
                                function (data) {
                                    if (data.length == 0) {
                                        var options = {
                                            title: $rootScope.deleteAttributeDialogTitle,
                                            message: $rootScope.deleteAttributeDialogMessage,
                                            okButtonClass: 'btn-danger'
                                        };
                                        DialogService.confirm(options, function (yes) {
                                            if (yes == true) {
                                                if (attribute.id != null && attribute.id != undefined) {
                                                    ClassificationService.deleteAttribute($rootScope.selectedClassificationType.objectType, $rootScope.selectedClassificationType.id, attribute.id).then(
                                                        function (data) {
                                                            /* loadAttributes();*/
                                                            loadAttributeGroups();
                                                            $rootScope.selectedClassificationType.id = id;
                                                            $rootScope.selectedClassificationType.attributes.splice($rootScope.selectedClassificationType.attributes.indexOf(attribute), 1);
                                                            $rootScope.showSuccessMessage($rootScope.deleteAttributeMessage);
                                                        },
                                                        function (error) {
                                                            $rootScope.showErrorMessage(error.message);
                                                        }
                                                    )
                                                }
                                            }
                                        });
                                    } else {
                                        $rootScope.showWarningMessage(attribute.name + " : Attribute already in use");
                                    }
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            );
                        }

                        if ($rootScope.selectedClassificationType.objectType == 'PMOBJECTTYPE') {
                            ObjectAttributeService.getAttributeValuesByDef(attribute.id).then(
                                function (data) {
                                    if (data.length == 0) {
                                        var options = {
                                            title: $rootScope.deleteAttributeDialogTitle,
                                            message: $rootScope.deleteAttributeDialogMessage,
                                            okButtonClass: 'btn-danger'
                                        };
                                        DialogService.confirm(options, function (yes) {
                                            if (yes == true) {
                                                if (attribute.id != null && attribute.id != undefined) {
                                                    ClassificationService.deleteAttribute('PMOBJECTTYPE', $rootScope.selectedClassificationType.id, attribute.id).then(
                                                        function (data) {
                                                            /* loadAttributes();*/
                                                            loadAttributeGroups();
                                                            $rootScope.selectedClassificationType.id = id;
                                                            $rootScope.selectedClassificationType.attributes.splice($rootScope.selectedClassificationType.attributes.indexOf(attribute), 1);
                                                            $rootScope.showSuccessMessage($rootScope.deleteAttributeMessage);
                                                        },
                                                        function (error) {
                                                            $rootScope.showErrorMessage(error.message);
                                                        }
                                                    )
                                                }
                                            }
                                        });
                                    } else {
                                        $rootScope.showWarningMessage(attribute.name + " : Attribute already in use");
                                    }
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            );
                        }

                    }


                    function editAttribute(attribute) {
                        attribute.isNew = false;
                        var id = $rootScope.selectedClassificationType.id;
                        if (attribute.dataType == "LIST" && attribute.configurable == true && $scope.classificationTypeJson != null && $scope.classificationTypeJson.size > 0) {
                            var promise = null;
                            var key = getKeysFromMap($scope.classificationTypeJson, attribute.id);
                            if ($scope.keyArray.length > 0) {
                                $rootScope.showWarningMessage(attributeUsedInExclusion)
                            }
                            else {
                                promise = ItemTypeService.getAttributeValues(attribute.id);
                            }
                        }
                        else {
                            if ($rootScope.selectedClassificationType.objectType == 'PLMWORKFLOWDEFINITIONSTATUS') {
                                attribute.showValues = false;
                                attribute.newName = attribute.name;
                                attribute.newDescription = attribute.description;
                                attribute.newDataType = attribute.dataType;
                                attribute.newRefType = attribute.refType;
                                attribute.newLov = attribute.lov;
                                attribute.newMeasurement = attribute.measurement;
                                attribute.oldValidations = attribute.validations;
                                attribute.oldAttributeGroup = attribute.attributeGroup;
                                attribute.oldSubType = attribute.subType;
                                attribute.oldFormula = attribute.formula;
                                attribute.oldDefaultListValue = attribute.defaultListValue;
                                attribute.oldListMultiple = attribute.listMultiple;
                                attribute.oldShowInTable = attribute.showInTable;
                                attribute.oldRevisionSpecific = attribute.revisionSpecific;
                                attribute.oldChangeControlled = attribute.changeControlled;
                                attribute.oldAllowEditAfterRelease = attribute.allowEditAfterRelease;
                                attribute.oldRequired = attribute.required;
                                $rootScope.selectedClassificationType.id = id;
                                if ($scope.attributeGroups.length == 0) {
                                    attribute.newGroup = true;
                                }
                                $scope.unSavedAttributes.push(attribute);
                                $timeout(function () {
                                    attribute.editMode = true;
                                    attribute.inVisible = false;
                                }, 500);
                            } else if ($rootScope.selectedClassificationType.objectType == 'ITEMTYPE') {
                                promise = ItemTypeService.getAttributeValues(attribute.id);
                            } else if ($rootScope.selectedClassificationType.objectType == 'CHANGETYPE') {
                                promise = ECOService.getEcoAttributeValues(attribute.id);
                            } else if ($rootScope.selectedClassificationType.objectType == 'QUALITY_TYPE' || $rootScope.selectedClassificationType.objectType == 'SUPPLIERTYPE') {
                                promise = QualityTypeService.getUsedAttributeValues(attribute.id);
                            } else if ($rootScope.selectedClassificationType.objectType == 'MANUFACTURERPARTTYPE') {
                                promise = MfrPartsService.getMfrPartAttributeValues(attribute.id);
                            } else if ($rootScope.selectedClassificationType.objectType == 'REQUIREMENTTYPE' || $rootScope.selectedClassificationType.objectType == 'SPECIFICATIONTYPE') {
                                promise = RequirementsTypeService.getAttributeValues(attribute.id);
                            } else if ($rootScope.selectedClassificationType.objectType == 'MANUFACTURERTYPE') {
                                promise = MfrService.getMfrAttributeValues(attribute.id);
                            } else if ($rootScope.selectedClassificationType.objectType == 'PLANTTYPE' || $rootScope.selectedClassificationType.objectType == 'MACHINETYPE' || $rootScope.selectedClassificationType.objectType == 'WORKCENTERTYPE'
                                || $rootScope.selectedClassificationType.objectType == 'TOOLTYPE' || $rootScope.selectedClassificationType.objectType == 'OPERATIONTYPE' || $rootScope.selectedClassificationType.objectType == 'MBOMTYPE' 
                                || $rootScope.selectedClassificationType.objectType == 'BOPTYPE' || $rootScope.selectedClassificationType.objectType == 'MANPOWERTYPE' || $rootScope.selectedClassificationType.objectType == 'JIGFIXTURETYPE'
                                || $rootScope.selectedClassificationType.objectType == 'PRODUCTIONORDERTYPE' || $rootScope.selectedClassificationType.objectType == 'SERVICEORDERTYPE' || $rootScope.selectedClassificationType.objectType == 'EQUIPMENTTYPE' 
                                || $rootScope.selectedClassificationType.objectType == 'INSTRUMENTTYPE') {
                                promise = MESObjectTypeService.getUsedMesObjectAttributesValues(attribute.id);
                            } else if ($rootScope.selectedClassificationType.objectType == 'ASSETTYPE' || $rootScope.selectedClassificationType.objectType == 'METERTYPE' || $rootScope.selectedClassificationType.objectType == 'SPAREPARTTYPE'
                                || $rootScope.selectedClassificationType.objectType == 'MROMAINTENANCEPLAN' || $rootScope.selectedClassificationType.objectType == 'WORKREQUESTTYPE' || $rootScope.selectedClassificationType.objectType == 'WORKORDERTYPE'){
                                promise = MESObjectTypeService.getUsedMroObjectAttributesValues(attribute.id);
                            } else if ($rootScope.selectedClassificationType.objectType == 'CUSTOMOBJECTTYPE') {
                                promise = ObjectAttributeService.getAttributeValuesByDef(attribute.id);
                            } else if ($rootScope.selectedClassificationType.objectType == 'PMOBJECTTYPE') {
                                promise = ObjectAttributeService.getAttributeValuesByDef(attribute.id);
                            } else if ($rootScope.selectedClassificationType.objectType == 'WORKFLOWTYPE') {
                                promise = ObjectAttributeService.getAttributeValuesByDef(attribute.id);
                            } else if ($rootScope.selectedClassificationType.objectType == 'PGCSUBSTANCETYPE' || $rootScope.selectedClassificationType.objectType == 'PGCDECLARATIONTYPE' || $rootScope.selectedClassificationType.objectType == 'PGCSPECIFICATIONTYPE') {
                                promise = PGCObjectTypeService.getUsedPgcObjectAttributesValues(attribute.id);
                            }
                            if (attribute.refSubType != null) {
                                getObjectSubType(attribute)
                            }

                        }

                        if (promise != null) {
                            promise.then(
                                function (data) {
                                    if (data.length == 0 || attribute.dataType == 'FORMULA') {
                                        attribute.showValues = false;
                                        attribute.newName = attribute.name;
                                        attribute.newDescription = attribute.description;
                                        attribute.newDataType = attribute.dataType;
                                        attribute.newRefType = attribute.refType;
                                        attribute.newLov = attribute.lov;
                                        attribute.newMeasurement = attribute.measurement;
                                        attribute.oldValidations = attribute.validations;
                                        attribute.oldAttributeGroup = attribute.attributeGroup;
                                        attribute.oldSubType = attribute.subType;
                                        attribute.oldFormula = attribute.formula;
                                        attribute.oldDefaultListValue = attribute.defaultListValue;
                                        attribute.oldListMultiple = attribute.listMultiple;
                                        attribute.oldShowInTable = attribute.showInTable;
                                        attribute.oldRevisionSpecific = attribute.revisionSpecific;
                                        attribute.oldChangeControlled = attribute.changeControlled;
                                        attribute.oldAllowEditAfterRelease = attribute.allowEditAfterRelease;
                                        attribute.oldRequired = attribute.required;
                                        $rootScope.selectedClassificationType.id = id;
                                        if ($scope.attributeGroups.length == 0) {
                                            attribute.newGroup = true;
                                        }
                                        $scope.unSavedAttributes.push(attribute);
                                        $timeout(function () {
                                            attribute.editMode = true;
                                            attribute.inVisible = false;
                                        }, 500);
                                    } else {
                                        attribute.newName = attribute.name;
                                        attribute.newDescription = attribute.description;
                                        attribute.newDataType = attribute.dataType;
                                        attribute.newRefType = attribute.refType;
                                        attribute.newLov = attribute.lov;
                                        attribute.newMeasurement = attribute.measurement;
                                        attribute.oldValidations = attribute.validations;
                                        $rootScope.selectedClassificationType.id = id;
                                        attribute.oldAttributeGroup = attribute.attributeGroup;
                                        attribute.oldSubType = attribute.subType;
                                        attribute.oldFormula = attribute.formula;
                                        attribute.oldDefaultListValue = attribute.defaultListValue;
                                        attribute.oldListMultiple = attribute.listMultiple;
                                        attribute.oldShowInTable = attribute.showInTable;
                                        attribute.oldRevisionSpecific = attribute.revisionSpecific;
                                        attribute.oldChangeControlled = attribute.changeControlled;
                                        attribute.oldAllowEditAfterRelease = attribute.allowEditAfterRelease;
                                        attribute.oldRequired = attribute.required;
                                        attribute.inVisible = true;
                                        attribute.defaultValue = true;
                                        if ($scope.attributeGroups.length == 0) {
                                            attribute.newGroup = true;
                                        }
                                        $scope.unSavedAttributes.push(attribute);
                                        /*$rootScope.showWarningMessage(attribute.name + " : Attribute already in use");*/
                                    }
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }

                    }

                    function getObjectSubType(attribute) {
                        ClassificationService.getAssignedObjectType($rootScope.selectedClassificationType.id, attribute.refSubType, attribute.refType).then(
                            function (data) {
                                attribute.subType = data;
                            }
                        )
                    }

                    var attributeNameValidation = $translate.instant("ATTRIBUTE_NEW_NAME_VALIDATION");

                    function validateAttribute(attribute) {
                        var valid = true;
                        var warningMessage = null;
                        if (attribute.newName == null || attribute.newName == "" || attribute.newName == undefined) {
                            $scope.hide = true;
                            warningMessage = nameValidation;
                            valid = false;
                        }/* else if (attribute.newDescription == null || attribute.newDescription == "" || attribute.newDescription == undefined) {
                         $scope.hide = true;
                         $scope.error = $rootScope.descriptionValidation;
                         valid = false;
                         }*/ else if (attribute.newDataType == null || attribute.newDataType == "" || attribute.newDataType == undefined) {
                            $scope.hide = true;
                            warningMessage = dataTypeValidation;
                            valid = false;
                        } else if (attribute.newDataType == 'LIST' &&
                            (attribute.newLov == null || attribute.newLov == "" || attribute.newLov == undefined)) {
                            $scope.hide = true;
                            warningMessage = listValueValidation;
                            valid = false;

                        } else if (attribute.newDataType == 'OBJECT' &&
                            (attribute.newRefType == null || attribute.newRefType == "" || attribute.newRefType == undefined)) {
                            $scope.hide = true;
                            warningMessage = referenceTypeValidation;
                            valid = false;
                        } else if (attribute.newDataType == 'FORMULA' &&
                            (attribute.formula == null || attribute.formula == "" || attribute.formula == undefined)) {
                            $scope.hide = true;
                            warningMessage = formulaAttributeValidation;
                            valid = false;
                        }
                        else {
                            angular.forEach($rootScope.selectedClassificationType.attributes, function (attr) {
                                if (valid) {
                                    if (attribute.attributeGroup != null && attribute.attributeGroup != "") {
                                        if (attr.attributeGroup == attribute.attributeGroup && attr.name == attribute.newName && attr.id != attribute.id) {
                                            warningMessage = attr.attributeGroup + " - " + attr.name + ":" + attributeNameValidation;
                                            valid = false;
                                        }
                                    } else {
                                        if ((attr.attributeGroup == null || attr.attributeGroup == "") && attr.name == attribute.newName && attr.id != attribute.id) {
                                            warningMessage = attr.name + ":" + attributeNameValidation;
                                            valid = false;
                                        }
                                    }
                                }
                            })
                        }
                        $scope.hide = false;
                        if (!valid) {
                            if ($rootScope.selectedClassificationType.objectType == 'PLMWORKFLOWDEFINITIONSTATUS') {
                                $rootScope.formDataErrorNotification.show();
                                $rootScope.formDataErrorNotification.removeClass('zoomOut');
                                $rootScope.formDataErrorNotification.addClass('zoomIn');
                                $rootScope.formDataNotificationClass = "fa-warning";
                                $rootScope.formDataNotificationBackground = "alert-warning";
                                $rootScope.formDataMessage = warningMessage;
                                $timeout(function () {
                                    closeFormDataErrorNotification();
                                }, 3000);
                            } else {
                                $rootScope.showWarningMessage(warningMessage);
                            }
                        }
                        return valid;
                    }

                    var attributeSavedMessage = $translate.instant("ATTRIBUTE_SAVED_MESSAGE");
                    var attributeNameExistMsg = parsed.html($translate.instant("ATTRIBUTE_NAME_EXIST")).html();
                    var nameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
                    var descriptionValidation = parsed.html($translate.instant("DESCRIPTION_VALIDATION")).html();
                    var dataTypeValidation = parsed.html($translate.instant("DATA_TYPE_VALIDATION")).html();
                    var listValueValidation = parsed.html($translate.instant("LIST_VALUE_VALIDATION")).html();
                    var referenceTypeValidation = parsed.html($translate.instant("REFERENCE_TYPE_VALIDATION")).html();
                    var formulaAttributeValidation = parsed.html($translate.instant("FORMULA_ATTRIBUTE_VALIDATION")).html();
                    $scope.hide = false;
                    $scope.itemname = [];
                    function applyChanges(attribute) {
                        if (attribute.newGroup && attribute.attributeGroup != null && attribute.attributeGroup != "") {
                            ObjectTypeAttributeService.getAttributeGroupsByName(attribute.attributeGroup).then(
                                function (data) {
                                    if (data.length > 0) {
                                        $rootScope.showErrorMessage(attribute.attributeGroup + " : Group Name already exist");
                                    } else {
                                        saveAttribute(attribute);
                                    }
                                }
                            )
                        } else {
                            saveAttribute(attribute);
                        }

                    }

                    function saveAttribute(attribute) {
                        var id = $rootScope.selectedClassificationType.id;
                        if (validateAttribute(attribute)) {
                            $scope.hide = false;
                            var promise = null;
                            attribute.name = attribute.newName;
                            attribute.description = attribute.newDescription;
                            attribute.dataType = attribute.newDataType;
                            attribute.refType = attribute.newRefType;
                            attribute.lov = attribute.newLov;
                            attribute.measurement = attribute.newMeasurement;
                            if (attribute.newValidations != null && attribute.newValidations != "" && attribute.newValidations != undefined) {
                                attribute.validations = JSON.stringify(attribute.newValidations);
                            }
                            if (attribute.id == null || attribute.id == undefined) {
                                if ($rootScope.selectedClassificationType.parentType != null) {
                                    if ($rootScope.selectedClassificationType.objectType == 'ITEMTYPE') {
                                        if (attribute.newName != null || attribute.newName != "" || attribute.newName != undefined) {
                                            $rootScope.showBusyIndicator($('.view-container'));
                                            ItemTypeService.getTypeAttributeName($rootScope.selectedClassificationType, attribute.newName).then(
                                                function (data) {
                                                    $scope.itemname = data;
                                                    if ($scope.itemname != "") {
                                                        attribute.itemMode = true;
                                                        attribute.inVisible = false;
                                                        $rootScope.showInfoMessage(attributeNameExistMsg);
                                                        $rootScope.hideBusyIndicator();
                                                    }
                                                    if ($scope.itemname == null || $scope.itemname == "") {
                                                        $rootScope.showBusyIndicator($('.view-container'));
                                                        ClassificationService.createAttribute($rootScope.selectedClassificationType.objectType, $rootScope.selectedClassificationType.id, attribute).then(
                                                            function (data) {
                                                                $scope.error = "";
                                                                attribute.id = data.id;
                                                                attribute.editMode = false;
                                                                attribute.itemMode = false;
                                                                attribute.inVisible = false;
                                                                $rootScope.selectedClassificationType.id = id;
                                                                $scope.unSavedAttributes.splice($scope.unSavedAttributes.indexOf(attribute), 1);
                                                                if ($scope.unSavedAttributes.length == 0) {
                                                                    loadAttributes();
                                                                }
                                                                loadAttributeGroups();
                                                                $timeout(function () {
                                                                    attribute.showValues = true;
                                                                }, 500);
                                                                $rootScope.showSuccessMessage(data.name + " : " + attributeSavedMessage);
                                                                $rootScope.hideBusyIndicator();
                                                            }, function (error) {
                                                                $rootScope.showErrorMessage(error.message);
                                                                $rootScope.hideBusyIndicator();
                                                            }
                                                        )
                                                    }
                                                }, function (error) {
                                                    $rootScope.showErrorMessage(error.message);
                                                    $rootScope.hideBusyIndicator();
                                                })
                                        }
                                    } else if ($rootScope.selectedClassificationType.objectType == 'PLMWORKFLOWDEFINITIONSTATUS') {
                                        if (attribute.newName != null || attribute.newName != "" || attribute.newName != undefined) {
                                            $rootScope.showBusyIndicator($('#workflow-form-data'));
                                            WorkflowDefinitionService.getWorkflowStatusAttributeByName($rootScope.selectedClassificationType.id, attribute.newName).then(
                                                function (data) {
                                                    $scope.itemname = data;
                                                    if ($scope.itemname != "") {
                                                        attribute.itemMode = true;
                                                        attribute.inVisible = false;
                                                        $rootScope.formDataErrorNotification.show();
                                                        $rootScope.formDataErrorNotification.removeClass('zoomOut');
                                                        $rootScope.formDataErrorNotification.addClass('zoomIn');
                                                        $rootScope.formDataNotificationClass = "fa-info-circle";
                                                        $rootScope.formDataNotificationBackground = "alert-info";
                                                        $rootScope.formDataMessage = attributeNameExistMsg;
                                                        $timeout(function () {
                                                            closeFormDataErrorNotification();
                                                        }, 3000);
                                                        $rootScope.hideBusyIndicator();
                                                    }
                                                    if ($scope.itemname == null || $scope.itemname == "") {
                                                        $rootScope.showBusyIndicator($('.view-container'));
                                                        WorkflowDefinitionService.createWorkflowStatusAttribute($rootScope.selectedClassificationType.id, attribute).then(
                                                            function (data) {
                                                                $scope.error = "";
                                                                attribute.id = data.id;
                                                                attribute.editMode = false;
                                                                attribute.itemMode = false;
                                                                attribute.inVisible = false;
                                                                $rootScope.selectedClassificationType.id = id;
                                                                $scope.unSavedAttributes.splice($scope.unSavedAttributes.indexOf(attribute), 1);
                                                                if ($scope.unSavedAttributes.length == 0) {
                                                                    loadAttributes();
                                                                }
                                                                loadAttributeGroups();
                                                                $timeout(function () {
                                                                    attribute.showValues = true;
                                                                }, 500);

                                                                $rootScope.formDataErrorNotification.show();
                                                                $rootScope.formDataErrorNotification.removeClass('zoomOut');
                                                                $rootScope.formDataErrorNotification.addClass('zoomIn');
                                                                $rootScope.formDataNotificationClass = "fa-check";
                                                                $rootScope.formDataNotificationBackground = "alert-success";
                                                                $rootScope.formDataMessage = data.name + " : " + attributeSavedMessage;
                                                                $timeout(function () {
                                                                    closeFormDataErrorNotification();
                                                                }, 3000);
                                                                $rootScope.hideBusyIndicator();
                                                            }, function (error) {
                                                                $rootScope.formDataErrorNotification.show();
                                                                $rootScope.formDataErrorNotification.removeClass('zoomOut');
                                                                $rootScope.formDataErrorNotification.addClass('zoomIn');
                                                                $rootScope.formDataNotificationClass = "fa-ban";
                                                                $rootScope.formDataNotificationBackground = "alert-danger";
                                                                $rootScope.formDataMessage = error.message;
                                                                $timeout(function () {
                                                                    closeFormDataErrorNotification();
                                                                }, 3000);
                                                                $rootScope.hideBusyIndicator();
                                                            }
                                                        )
                                                    }
                                                }, function (error) {
                                                    $rootScope.showErrorMessage(error.message);
                                                    $rootScope.hideBusyIndicator();
                                                })
                                        }
                                    } else if ($rootScope.selectedClassificationType.objectType == 'CUSTOMOBJECTTYPE') {
                                        $rootScope.showBusyIndicator($('.view-container'));
                                        CustomObjectTypeService.createAttribute($rootScope.selectedClassificationType.id, attribute).then(
                                            function (data) {
                                                $scope.error = "";
                                                attribute.id = data.id;
                                                attribute.editMode = false;
                                                attribute.itemMode = false;
                                                attribute.inVisible = false;
                                                $rootScope.selectedClassificationType.id = id;
                                                $scope.unSavedAttributes.splice($scope.unSavedAttributes.indexOf(attribute), 1);
                                                if ($scope.unSavedAttributes.length == 0) {
                                                    loadAttributes();
                                                }
                                                loadAttributeGroups();
                                                $timeout(function () {
                                                    attribute.showValues = true;
                                                }, 500);
                                                $rootScope.showSuccessMessage(data.name + " : " + attributeSavedMessage);
                                                $rootScope.hideBusyIndicator();
                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
                                                $rootScope.hideBusyIndicator();
                                            }
                                        )
                                    } else if ($rootScope.selectedClassificationType.objectType != 'CUSTOMOBJECTTYPE') {
                                        $rootScope.showBusyIndicator($('.view-container'));
                                        ClassificationService.createAttribute($rootScope.selectedClassificationType.objectType, $rootScope.selectedClassificationType.id, attribute).then(
                                            function (data) {
                                                $scope.error = "";
                                                attribute.id = data.id;
                                                attribute.editMode = false;
                                                attribute.itemMode = false;
                                                attribute.inVisible = false;
                                                $rootScope.selectedClassificationType.id = id;
                                                $scope.unSavedAttributes.splice($scope.unSavedAttributes.indexOf(attribute), 1);
                                                if ($scope.unSavedAttributes.length == 0) {
                                                    loadAttributes();
                                                }
                                                loadAttributeGroups();
                                                $timeout(function () {
                                                    attribute.showValues = true;
                                                }, 500);
                                                $rootScope.showSuccessMessage(data.name + " : " + attributeSavedMessage);
                                                $rootScope.hideBusyIndicator();
                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
                                                $rootScope.hideBusyIndicator();
                                            }
                                        )
                                    }
                                } else if ($rootScope.selectedClassificationType.objectType == 'PLMWORKFLOWDEFINITIONSTATUS') {
                                    if (attribute.newName != null || attribute.newName != "" || attribute.newName != undefined) {
                                        $rootScope.showBusyIndicator($('#workflow-form-data'));
                                        WorkflowDefinitionService.getWorkflowStatusAttributeByName($rootScope.selectedClassificationType.id, attribute.newName).then(
                                            function (data) {
                                                $scope.itemname = data;
                                                if ($scope.itemname != "") {
                                                    attribute.itemMode = true;
                                                    attribute.inVisible = false;
                                                    $rootScope.formDataErrorNotification.show();
                                                    $rootScope.formDataErrorNotification.removeClass('zoomOut');
                                                    $rootScope.formDataErrorNotification.addClass('zoomIn');
                                                    $rootScope.formDataNotificationClass = "fa-info-circle";
                                                    $rootScope.formDataNotificationBackground = "alert-info";
                                                    $rootScope.formDataMessage = attributeNameExistMsg;
                                                    $timeout(function () {
                                                        closeFormDataErrorNotification();
                                                    }, 3000);
                                                    $rootScope.hideBusyIndicator();
                                                }
                                                if ($scope.itemname == null || $scope.itemname == "") {
                                                    $rootScope.showBusyIndicator($('.view-container'));
                                                    WorkflowDefinitionService.createWorkflowStatusAttribute($rootScope.selectedClassificationType.id, attribute).then(
                                                        function (data) {
                                                            $scope.error = "";
                                                            attribute.id = data.id;
                                                            attribute.editMode = false;
                                                            attribute.itemMode = false;
                                                            attribute.inVisible = false;
                                                            $rootScope.selectedClassificationType.id = id;
                                                            $scope.unSavedAttributes.splice($scope.unSavedAttributes.indexOf(attribute), 1);
                                                            if ($scope.unSavedAttributes.length == 0) {
                                                                loadAttributes();
                                                            }
                                                            loadAttributeGroups();
                                                            $timeout(function () {
                                                                attribute.showValues = true;
                                                            }, 500);
                                                            $rootScope.formDataErrorNotification.show();
                                                            $rootScope.formDataErrorNotification.removeClass('zoomOut');
                                                            $rootScope.formDataErrorNotification.addClass('zoomIn');
                                                            $rootScope.formDataNotificationClass = "fa-check";
                                                            $rootScope.formDataNotificationBackground = "alert-success";
                                                            $rootScope.formDataMessage = data.name + " : " + attributeSavedMessage;
                                                            $timeout(function () {
                                                                closeFormDataErrorNotification();
                                                            }, 3000);
                                                            $rootScope.hideBusyIndicator();
                                                        }, function (error) {
                                                            $rootScope.showErrorMessage(error.message);
                                                            $rootScope.hideBusyIndicator();
                                                        }
                                                    )
                                                }
                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
                                                $rootScope.hideBusyIndicator();
                                            })
                                    }
                                } else if ($rootScope.selectedClassificationType.objectType != 'CUSTOMOBJECTTYPE') {
                                    $rootScope.showBusyIndicator($('.view-container'));
                                    ClassificationService.createAttribute($rootScope.selectedClassificationType.objectType, $rootScope.selectedClassificationType.id, attribute).then(
                                        function (data) {
                                            $scope.error = "";
                                            attribute.id = data.id;
                                            attribute.editMode = false;
                                            attribute.itemMode = false;
                                            attribute.inVisible = false;
                                            $rootScope.selectedClassificationType.id = id;
                                            $scope.unSavedAttributes.splice($scope.unSavedAttributes.indexOf(attribute), 1);
                                            if ($scope.unSavedAttributes.length == 0) {
                                                loadAttributes();
                                            }
                                            loadAttributeGroups();
                                            $timeout(function () {
                                                attribute.showValues = true;
                                            }, 500);
                                            $rootScope.showSuccessMessage(data.name + " : " + attributeSavedMessage);
                                            $rootScope.hideBusyIndicator();
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                            $rootScope.hideBusyIndicator();
                                        }
                                    )

                                } else if ($rootScope.selectedClassificationType.objectType == 'CUSTOMOBJECTTYPE') {
                                    $rootScope.showBusyIndicator($('.view-container'));
                                    CustomObjectTypeService.createAttribute($rootScope.selectedClassificationType.id, attribute).then(
                                        function (data) {
                                            $scope.error = "";
                                            attribute.id = data.id;
                                            attribute.editMode = false;
                                            attribute.itemMode = false;
                                            attribute.inVisible = false;
                                            $rootScope.selectedClassificationType.id = id;
                                            $scope.unSavedAttributes.splice($scope.unSavedAttributes.indexOf(attribute), 1);
                                            if ($scope.unSavedAttributes.length == 0) {
                                                loadAttributes();
                                            }
                                            loadAttributeGroups();
                                            $timeout(function () {
                                                attribute.showValues = true;
                                            }, 500);
                                            $rootScope.showSuccessMessage(data.name + " : " + attributeSavedMessage);
                                            $rootScope.hideBusyIndicator();
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                            $rootScope.hideBusyIndicator();
                                        }
                                    )

                                }

                            } else if ($rootScope.selectedClassificationType.objectType == 'PLMWORKFLOWDEFINITIONSTATUS') {
                                $rootScope.showBusyIndicator($('#workflow-form-data'));
                                WorkflowDefinitionService.updateWorkflowStatusAttribute($rootScope.selectedClassificationType.id, attribute).then(
                                    function (data) {
                                        $scope.error = "";
                                        attribute.id = data.id;
                                        attribute.editMode = false;
                                        attribute.itemMode = false;
                                        attribute.inVisible = false;
                                        $rootScope.selectedClassificationType.id = id;
                                        $scope.unSavedAttributes.splice($scope.unSavedAttributes.indexOf(attribute), 1);
                                        if ($scope.unSavedAttributes.length == 0) {
                                            loadAttributes();
                                        }
                                        loadAttributeGroups();
                                        $timeout(function () {
                                            attribute.showValues = true;
                                        }, 500);
                                        $rootScope.formDataErrorNotification.show();
                                        $rootScope.formDataErrorNotification.removeClass('zoomOut');
                                        $rootScope.formDataErrorNotification.addClass('zoomIn');
                                        $rootScope.formDataNotificationClass = "fa-check";
                                        $rootScope.formDataNotificationBackground = "alert-success";
                                        $rootScope.formDataMessage = data.name + " : " + attributeSavedMessage;
                                        $timeout(function () {
                                            closeFormDataErrorNotification();
                                        }, 3000);
                                        $rootScope.hideBusyIndicator();
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                        $rootScope.hideBusyIndicator();
                                    }
                                )
                            } else if ($rootScope.selectedClassificationType.objectType != 'CUSTOMOBJECTTYPE') {
                                $rootScope.showBusyIndicator($('.view-container'));
                                ClassificationService.updateAttribute($rootScope.selectedClassificationType.objectType, $rootScope.selectedClassificationType.id, attribute).then(
                                    function (data) {
                                        $scope.error = "";
                                        attribute.id = data.id;
                                        attribute.editMode = false;
                                        attribute.itemMode = false;
                                        attribute.inVisible = false;
                                        $rootScope.selectedClassificationType.id = id;
                                        $scope.unSavedAttributes.splice($scope.unSavedAttributes.indexOf(attribute), 1);
                                        if ($scope.unSavedAttributes.length == 0) {
                                            loadAttributes();
                                        }
                                        loadAttributeGroups();
                                        $timeout(function () {
                                            attribute.showValues = true;
                                        }, 500);
                                        $rootScope.showSuccessMessage(data.name + " : " + attributeSavedMessage);
                                        $rootScope.hideBusyIndicator();
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                        $rootScope.hideBusyIndicator();
                                    }
                                )
                            } else if ($rootScope.selectedClassificationType.objectType == 'CUSTOMOBJECTTYPE') {
                                $rootScope.showBusyIndicator($('.view-container'));
                                CustomObjectTypeService.updateAttribute($rootScope.selectedClassificationType.id, attribute).then(
                                    function (data) {
                                        $scope.error = "";
                                        attribute.id = data.id;
                                        attribute.editMode = false;
                                        attribute.itemMode = false;
                                        attribute.inVisible = false;
                                        $rootScope.selectedClassificationType.id = id;
                                        $scope.unSavedAttributes.splice($scope.unSavedAttributes.indexOf(attribute), 1);
                                        if ($scope.unSavedAttributes.length == 0) {
                                            loadAttributes();
                                        }
                                        loadAttributeGroups();
                                        $timeout(function () {
                                            attribute.showValues = true;
                                        }, 500);
                                        $rootScope.showSuccessMessage(data.name + " : " + attributeSavedMessage);
                                        $rootScope.hideBusyIndicator();
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                    }
                                )
                            }

                        }
                    }

                    $scope.applyChange = applyChange;

                    function applyChange(attribute) {
                        if (attribute.newGroup && attribute.attributeGroup != null && attribute.attributeGroup != "") {
                            ObjectTypeAttributeService.getAttributeGroupsByName(attribute.attributeGroup).then(
                                function (data) {
                                    if (data.length > 0) {
                                        $rootScope.showErrorMessage("Group Name already exist");
                                    } else {
                                        updateAttributeChanges(attribute);
                                    }
                                }
                            )
                        } else {
                            updateAttributeChanges(attribute);
                        }
                    }

                    function updateAttributeChanges(attribute) {
                        var id = $rootScope.selectedClassificationType.id;
                        if (validateAttribute(attribute)) {
                            $scope.hide = false;
                            var promise = null;
                            attribute.name = attribute.newName;
                            attribute.description = attribute.newDescription;
                            attribute.dataType = attribute.newDataType;
                            attribute.refType = attribute.newRefType;
                            attribute.lov = attribute.newLov;
                            attribute.measurement = attribute.newMeasurement;
                            attribute.validations = attribute.newValidations;
                            if (attribute.id == null || attribute.id == undefined) {
                                promise = ClassificationService.createAttribute($rootScope.selectedClassificationType.objectType, $rootScope.selectedClassificationType.id, attribute);
                                if (validateAttribute(attribute)) {

                                }

                            }
                            else {
                                promise = ClassificationService.updateAttribute($rootScope.selectedClassificationType.objectType, $rootScope.selectedClassificationType.id, attribute);
                            }

                            if (promise != null) {
                                promise.then(
                                    function (data) {
                                        $scope.error = "";
                                        attribute.id = data.id;
                                        attribute.editMode = false;
                                        attribute.itemMode = false;
                                        attribute.inVisible = false;
                                        $rootScope.selectedClassificationType.id = id;
                                        $timeout(function () {
                                            attribute.showValues = true;
                                        }, 500);
                                        $rootScope.showSuccessMessage(data.name + " : " + attributeSavedMessage);
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                        $rootScope.hideBusyIndicator();
                                    }
                                )
                            }
                        }
                    }

                    function cancelChanges(attribute) {
                        $scope.hide = false;
                        if (attribute.id == null || attribute.id == undefined) {
                            $rootScope.selectedClassificationType.attributes.splice($rootScope.selectedClassificationType.attributes.indexOf(attribute), 1);
                            $scope.unSavedAttributes.splice($scope.unSavedAttributes.indexOf(attribute), 1);
                            $scope.error = null;
                        }
                        else {
                            attribute.name = attribute.newName;
                            attribute.description = attribute.newDescription;
                            attribute.dataType = attribute.newDataType;
                            attribute.measurement = attribute.newMeasurement;
                            attribute.editMode = false;
                            attribute.inVisible = false;
                            attribute.defaultValue = false;
                            attribute.refType = attribute.newRefType;
                            attribute.subType = attribute.oldSubType;
                            attribute.formula = attribute.oldFormula;
                            attribute.defaultListValue = attribute.oldDefaultListValue;
                            attribute.listMultiple = attribute.oldListMultiple;
                            attribute.showInTable = attribute.oldShowInTable;
                            attribute.revisionSpecific = attribute.oldRevisionSpecific;
                            attribute.changeControlled = attribute.oldChangeControlled;
                            attribute.allowEditAfterRelease = attribute.oldAllowEditAfterRelease;
                            attribute.required = attribute.oldRequired;
                            attribute.lov = attribute.newLov;
                            attribute.measurement = attribute.newMeasurement;
                            attribute.validations = attribute.oldValidations;
                            attribute.attributeGroup = attribute.oldAttributeGroup;
                            $scope.error = null;
                            $timeout(function () {
                                attribute.showValues = true;
                            }, 500);
                            $scope.unSavedAttributes.splice($scope.unSavedAttributes.indexOf(attribute), 1);
                            //loadAttributes();
                            loadAttributeGroups();
                        }
                    }

                    function loadAllLovs() {
                        LovService.getAllLovs().then(
                            function (data) {
                                var emptyLov = {
                                    id: null,
                                    name: "Add New"
                                }
                                $scope.lovs = data;
                                $scope.lovs.unshift(emptyLov);
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }

                    $scope.getLimitedWord = getLimitedWord;
                    function getLimitedWord(word, size) {
                        if (word.length <= size) {
                            return word;
                        } else {
                            return word.substr(0, size) + '...';
                        }
                    }

                    $scope.emptyLov = {
                        name: 'New Lov',
                        newName: 'New Lov',
                        description: "",
                        defaultValue: "",
                        valueObjects: [],
                        values: [],
                        editTitle: true,
                        showBusy: false
                    };

                    var emptyLovObject = {
                        id: null,
                        name: null,
                        description: "",
                        defaultValue: "",
                        values: []
                    };
                    $scope.lovAttribute = null;
                    $scope.addNewLov = addNewLov;
                    function addNewLov(attribute) {
                        if (attribute.newLov.name == "Add New") {
                            var modal = document.getElementById("lov-view");
                            modal.style.display = "block";
                            var headerHeight = $('#lov-header').outerHeight();
                            var footerHeight = $('#lov-footer').outerHeight();
                            var bomContentHeight = $('.lov-content').outerHeight();
                            $("#lovView-content").height(bomContentHeight - (headerHeight + footerHeight));
                            $scope.newLov = angular.copy($scope.emptyLov);
                            $scope.lovAttribute = attribute;
                        }
                    }

                    $scope.hideLov = hideLov;
                    function hideLov() {
                        if ($scope.newLov.id == null) {
                            var modal = document.getElementById("lov-view");
                            modal.style.display = "none";
                            $scope.lovAttribute.newLov = null;
                        } else {
                            if ($scope.newLov.values.length == 0) {
                                deleteLov();
                                var modal = document.getElementById("lov-view");
                                modal.style.display = "none";
                                $scope.lovAttribute.newLov = null;
                            } else {
                                var modal = document.getElementById("lov-view");
                                modal.style.display = "none";
                                $scope.lovAttribute.newLov = null;
                            }
                        }
                    }

                    $scope.addLovToAttribute = addLovToAttribute;
                    function addLovToAttribute() {
                        if ($scope.newLov.id == null) {
                            $rootScope.showWarningMessage(pleaseSaveNewLov);
                        } else if ($scope.newLov.id != null && $scope.newLov.values.length == 0) {
                            $rootScope.showWarningMessage(enterOneListValue)
                        } else {
                            var lovObject = angular.copy(emptyLovObject);
                            lovObject.id = $scope.newLov.id;
                            lovObject.name = $scope.newLov.name;
                            lovObject.description = $scope.newLov.description;
                            lovObject.defaultValue = $scope.newLov.defaultValue;
                            lovObject.values = $scope.newLov.values;
                            $scope.lovs.push(lovObject);
                            $scope.lovAttribute.newLov = lovObject;
                            var modal = document.getElementById("lov-view");
                            modal.style.display = "none";
                        }
                    }

                    var newValue = $translate.instant("NEW_VALUE");
                    $scope.addLovValue = addLovValue;
                    function addLovValue() {
                        $scope.newLov.valueObjects.push({
                            string: newValue,
                            newString: newValue,
                            editMode: true,
                            newMode: true
                        });
                    }

                    function validateLovValues() {
                        var valid = true;

                        angular.forEach($scope.newLov.valueObjects, function (obj) {
                            if (obj.string == null || obj.string == "" || obj.string == undefined) {
                                if (valid) {
                                    valid = false;
                                    $rootScope.showWarningMessage(enterListValue);
                                }
                            } else if (obj.string == "New Value") {
                                if (valid) {
                                    valid = false;
                                    $rootScope.showWarningMessage(valueCannotBe + " [ " + newValue + " ]");
                                }
                            }
                        });

                        return valid;
                    }

                    $scope.applyLovChanges = applyLovChanges;
                    function applyLovChanges() {
                        if ($scope.newLov.newName == null || $scope.newLov.newName == "" || $scope.newLov.newName == undefined) {
                            $scope.newLov.editTitle = true;
                            $rootScope.showWarningMessage(enterValueListName);
                        } else {
                            $scope.newLov.values = [];
                            if (validateLovValues()) {
                                angular.forEach($scope.newLov.valueObjects, function (obj) {
                                    if (obj.newString != newValue && obj.newString != "" && obj.newString != null && obj.newString != undefined) {
                                        $scope.newLov.values.push(obj.string);
                                    }
                                });

                                saveLov($scope.newLov);
                            }
                        }

                    }

                    var lovSavedMessage = parsed.html($translate.instant("LOV_SAVED_MESSAGE")).html();

                    function saveLov(lov) {
                        var name = lov.name;
                        lov.name = lov.newName;
                        var promise = null;
                        if (lov.id == null) {
                            promise = LovService.createLov(lov);
                        }
                        else {
                            promise = LovService.updateLov(lov);
                        }

                        promise.then(
                            function (data) {
                                lov.id = data.id;
                                lov.editTitle = false;
                                $rootScope.showSuccessMessage(lovSavedMessage);
                            },
                            function (error) {
                                lov.editTitle = true;
                                lov.name = name;
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }

                    $scope.applyChangesList = applyChangesList;

                    function applyChangesList(value) {

                        if (validateLovNames(value)) {
                            $scope.newLov.values = [];
                            if (newValue.includes("_")) {
                                $rootScope.showWarningMessage(the_notAllowed);
                            }

                            angular.forEach($scope.newLov.valueObjects, function (obj) {
                                if (obj.newString != newValue && obj.newString != "" && obj.newString != null && obj.newString != undefined) {
                                    $scope.newLov.values.push(obj.string);
                                }
                            });

                            saveLov($scope.newLov);
                        }

                    }

                    $scope.cancelChangesList = cancelChangesList;
                    function cancelChangesList(value) {
                        $scope.newLov.valueObjects.remove(value);
                    }

                    function validateLovNames(value) {
                        var valid = true;
                        if (value.newString.includes("_")) {
                            valid = false;
                            value.editMode = true;
                            value.newMode = false;
                            $rootScope.showWarningMessage(specialCharacterNotAllowed);
                        } else if (value.newString.includes(",")) {
                            valid = false;
                            value.editMode = true;
                            value.newMode = false;
                            $rootScope.showWarningMessage(commaNotAllowed);
                        } else if (value.newString == "" || value.newString == null || value.newString == undefined) {
                            valid = false;
                            value.editMode = true;
                            $rootScope.showWarningMessage(enterListValue);
                        } else if (value.newString == newValue) {
                            valid = false;
                            value.editMode = true;
                            $rootScope.showWarningMessage(valueCannotBe + " [ " + newValue + " ]");
                        } else if (!validateNames(value)) {
                            valid = false;
                        }
                        return valid;
                    }

                    function validateNames(value) {
                        var valid = true;

                        var count = 0;
                        angular.forEach($scope.newLov.valueObjects, function (obj) {
                            if (obj.newString.toUpperCase() === value.newString.toUpperCase()) {
                                count++;
                            }
                        });

                        if (count > 1) {
                            valid = false;
                            value.editMode = true;
                            value.newMode = false;
                            $rootScope.showWarningMessage(valueAlreadyExist);
                        }

                        return valid;
                    }

                    var lovValueDeletedMessage = parsed.html($translate.instant("LOV_VALUE_DELETED_MESSAGE")).html();

                    $scope.toDeleteValue = null;
                    $scope.deleteLovValue = deleteLovValue;
                    function deleteLovValue() {
                        if ($scope.toDeleteValue != null) {
                            $scope.newLov.showBusy = true;
                            $scope.newLov.valueObjects.remove($scope.toDeleteValue);
                            $scope.newLov.values = [];
                            angular.forEach($scope.newLov.valueObjects, function (obj) {
                                if (obj.newString != newValue && obj.newString != "" && obj.newString != null && obj.newString != undefined) {
                                    $scope.newLov.values.push(obj.string);
                                }
                            });

                            LovService.updateLov(lov).then(
                                function (data) {
                                    $scope.newLov = data;
                                    $rootScope.showSuccessMessage(lovValueDeletedMessage);
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            );
                        }
                    }

                    var deleteLovSuccessMessage = parsed.html($translate.instant("DELETE_LOV_SUCCESS_MESSAGE")).html();

                    $scope.deleteLov = deleteLov;
                    function deleteLov() {
                        LovService.deleteLov($scope.newLov.id).then(
                            function (data) {
                                $scope.newLov = {
                                    name: 'New Lov',
                                    newName: 'New Lov',
                                    description: "",
                                    defaultValue: "",
                                    valueObjects: [],
                                    values: [],
                                    editTitle: true,
                                    showBusy: false
                                };
                                $rootScope.showSuccessMessage(deleteLovSuccessMessage);
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        );
                    }

                    $scope.deleteValueTitle = parsed.html($translate.instant("DELETE_VALUE")).html();
                    $scope.lovCancel = parsed.html($translate.instant("CANCEL_CHANGES")).html();

                    $scope.saveChangesTitle = parsed.html($translate.instant("SAVE_CHANGES")).html();
                    $scope.deleteLovTitle = parsed.html($translate.instant("DELETE_LOV")).html();
                    $scope.addNewValueTitle = parsed.html($translate.instant("ADD_NEW_VALUE")).html();
                    $scope.updateAttributeSeq = updateAttributeSeq;
                    function updateAttributeSeq(targetObject, actualRow) {
                        if ((targetObject.id != null && targetObject.id != '') && (actualRow.id != null && actualRow.id != '')) {
                            if ($rootScope.selectedClassificationType.objectType == 'ITEMTYPE') {
                                ItemTypeService.updateAttributeSeq(targetObject.id, actualRow.id).then(
                                    function (data) {
                                        loadAttributes();
                                        loadAttributeGroups();
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                    }
                                )
                            }
                            if ($rootScope.selectedClassificationType.objectType == 'CHANGETYPE') {
                                ItemTypeService.updateChangeAttributeSeq(targetObject.id, actualRow.id).then(
                                    function (data) {
                                        loadAttributes();
                                        loadAttributeGroups();
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                    }
                                )
                            }
                            if ($rootScope.selectedClassificationType.objectType == 'QUALITY_TYPE') {
                                QualityTypeService.updateQualityAttributeSeq(targetObject.id, actualRow.id).then(
                                    function (data) {
                                        loadAttributes();
                                        loadAttributeGroups();
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                    }
                                )
                            }
                            if ($rootScope.selectedClassificationType.objectType == 'REQUIREMENTTYPE' || $rootScope.selectedClassificationType.objectType == 'SPECIFICATIONTYPE') {
                                ItemTypeService.updateRmAttributeSeq(targetObject.id, actualRow.id).then(
                                    function (data) {
                                        loadAttributes();
                                        loadAttributeGroups();
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                    }
                                )
                            }
                            if ($rootScope.selectedClassificationType.objectType == 'MANUFACTURERTYPE') {
                                ItemTypeService.updateMfrAttributeSeq(targetObject.id, actualRow.id).then(
                                    function (data) {
                                        loadAttributes();
                                        loadAttributeGroups();
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                    }
                                )
                            }
                            if ($rootScope.selectedClassificationType.objectType == 'MANUFACTURERPARTTYPE') {
                                ItemTypeService.updateMfrPartAttributeSeq(targetObject.id, actualRow.id).then(
                                    function (data) {
                                        loadAttributes();
                                        loadAttributeGroups();
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                    }
                                )
                            }
                        }

                    }

                    function objectTypeSelectedForAttributes() {
                        $scope.classificationType = $rootScope.selectedClassificationType;
                        if ($rootScope.selectedClassificationType != null && $rootScope.selectedClassificationType.objectType == 'ECOTYPE' || $rootScope.selectedClassificationType.objectType == 'ECRTYPE'
                            || $rootScope.selectedClassificationType.objectType == 'DCOTYPE' || $rootScope.selectedClassificationType.objectType == 'DCRTYPE' || $rootScope.selectedClassificationType.objectType == 'MCOTYPE'
                            || $rootScope.selectedClassificationType.objectType == 'DEVIATIONTYPE' || $rootScope.selectedClassificationType.objectType == 'WAIVERTYPE') {
                            $rootScope.selectedClassificationType.objectType = 'CHANGETYPE';
                        }
                        if ($rootScope.selectedClassificationType != null && $rootScope.selectedClassificationType != undefined) {
                            $scope.unSavedAttributes = [];
                            loadAttributes();
                            loadAllLovs();
                        }
                    }

                    function resizeAttributesView() {
                        if ($rootScope.selectedClassificationType.objectType == "PLMWORKFLOWDEFINITIONSTATUS") {
                            var formDataContent = $('.form-data-content').outerHeight();
                            $timeout(function () {
                                $("#attributes-table").height(formDataContent - 20);
                            })
                        } else {
                            var tabPane = $('.tab-pane').outerHeight();
                            $("#attributes-table").height(tabPane - 50);
                        }
                    }

                    function loadMeasurements() {
                        $rootScope.showBusyIndicator();
                        MeasurementService.getAllMeasurements().then(
                            function (data) {
                                $scope.measurements = data;
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }

                    function loadAttributeGroups() {
                        $rootScope.showBusyIndicator();
                        ObjectTypeAttributeService.getAttributeGroups().then(
                            function (data) {
                                $scope.attributeGroups = data;
                                if ($scope.attributeGroups.length > 0) {
                                    $scope.attributeGroups.unshift("--New--");
                                }
                                $rootScope.hideBusyIndicator();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }

                    $scope.selectAttributeGroup = selectAttributeGroup;
                    function selectAttributeGroup(attribute, group) {
                        if (group == "--New--") {
                            attribute.attributeGroup = null;
                            attribute.newGroup = true;
                        } else {
                            attribute.atttributeGroup = group;
                        }
                    }

                    $scope.selectController = selectController;
                    function selectController(attribute) {
                        if (attribute.changeControlled) {
                            attribute.revisionSpecific = true;
                        } else {
                            attribute.revisionSpecific = false;
                        }
                    }

                    $scope.showValidations = showValidations;
                    function showValidations(attribute) {
                        var options = {
                            title: "Attribute Validations",
                            template: 'app/desktop/modules/directives/attributeValidations/attributeValidationView.jsp',
                            controller: 'AttributeValidationController as attributeValidationVm',
                            resolve: 'app/desktop/modules/directives/attributeValidations/attributeValidationController',
                            width: 600,
                            showMask: true,
                            data: {
                                attributeValidations: attribute.newValidations
                            },
                            buttons: [
                                {text: "Add", broadcast: 'app.attribute.validations'}
                            ],
                            callback: function (result) {
                                attribute.newValidations = result;
                                $rootScope.hideSidePanel();
                            }
                        };

                        $rootScope.showSidePanel(options);
                    }

                    $scope.attributeValidationsPopover = {
                        templateUrl: 'app/desktop/modules/directives/attributeValidations/attributeValidationsPopover.jsp'
                    };

                    var textValidations = [
                        {key: "MIN_LENGTH_OF_CHARACTERS", value: null, type: "number"},
                        {key: "MAX_LENGTH_OF_CHARACTERS", value: null, type: "number"},
                        {key: "ALL_CAPITAL", value: false, type: "checkbox"},
                        {key: "ALL_SMALL", value: false, type: "checkbox"},
                        {key: "CAPITAL_AND_SMALL", value: false, type: "checkbox"},
                        {key: "PATTERN", value: null, type: "text"}
                    ];

                    var integerValidations = [
                        {key: 'MIN_VALUE', value: null, type: "number"},
                        {key: 'MAX_VALUE', value: null, type: "number"},
                        {key: 'ONLY_POSITIVE_VALUES', value: false, type: "checkbox"},
                        {key: 'ONLY_NEGATIVE_VALUES', value: false, type: "checkbox"},
                        {key: 'POSITIVE_AND_NEGATIVE', value: false, type: "checkbox"}
                    ];

                    var longTextValidations = [
                        {key: 'MAX_LENGTH_OF_CHARACTERS', value: null, type: "number"},
                        {key: "ALL_CAPITAL", value: false, type: "checkbox"},
                        {key: "ALL_SMALL", value: false, type: "checkbox"},
                        {key: "CAPITAL_AND_SMALL", value: false, type: "checkbox"}
                    ];
                    var doubleValidations = [
                        {key: 'MIN_VALUE', value: null, type: "number"},
                        {key: 'MAX_VALUE', value: null, type: "number"},
                        {key: "NO_OF_DECIMALS_TO_DISPLAY", value: null, type: "number"},
                        {key: "NO_OF_DECIMALS_TO_ENTER", value: null, type: "number"},
                        {key: 'ONLY_POSITIVE_VALUES', value: false, type: "checkbox"},
                        {key: 'ONLY_NEGATIVE_VALUES', value: false, type: "checkbox"},
                        {key: 'POSITIVE_AND_NEGATIVE', value: false, type: "checkbox"}
                    ];
                    var dateValidations = [
                        {key: 'FROM_DATE', value: null, type: "date"},
                        {key: 'TO_DATE', value: null, type: "date"},
                        {
                            key: 'DATE_FORMAT',
                            value: null,
                            values: ['dd/mm/yyyy', 'yyyy/mm/dd', 'yyyy/dd/mm', 'dd/MM/yyyy'],
                            type: "select"
                        }
                    ]
                    var timeValidations = [
                        {key: 'FROM_TIME', value: null, type: "time"},
                        {key: 'TO_TIME', value: null, type: "time"},
                        {
                            key: 'TIME_FORMAT',
                            value: null,
                            values: ['hh:mm:ss', 'hh:mm', 'hh'],
                            type: "select"
                        }
                    ]
                    var currencyValidations = [
                        {key: "NO_OF_DECIMALS_TO_DISPLAY", value: null, type: "number"},
                        {key: "NO_OF_DECIMALS_TO_ENTER", value: null, type: "number"},
                        {key: 'ONLY_POSITIVE_VALUES', value: false, type: "checkbox"},
                        {key: 'ONLY_NEGATIVE_VALUES', value: false, type: "checkbox"},
                        {key: 'POSITIVE_AND_NEGATIVE', value: false, type: "checkbox"}
                    ]
                    $scope.selectDataType = selectDataType;
                    function selectDataType(attribute) {
                        if (attribute.newDataType == "TEXT") {
                            attribute.newValidations = angular.copy(textValidations);
                        } else if (attribute.newDataType == "INTEGER") {
                            attribute.newValidations = angular.copy(integerValidations);
                        } else if (attribute.newDataType == "LONGTEXT") {
                            attribute.newValidations = angular.copy(longTextValidations);
                        } else if (attribute.newDataType == "DOUBLE") {
                            attribute.newValidations = angular.copy(doubleValidations);
                        } else if (attribute.newDataType == "DATE") {
                            attribute.newValidations = angular.copy(dateValidations);
                        } else if (attribute.newDataType == "TIME") {
                            attribute.newValidations = angular.copy(timeValidations);
                        } else if (attribute.newDataType == "CURRENCY") {
                            attribute.newValidations = angular.copy(currencyValidations);
                        } else {
                            attribute.newValidations = null;
                        }
                    }

                    $scope.checkRequiredAttributes = checkRequiredAttributes;
                    function checkRequiredAttributes(attribute) {
                        if (attribute.required != false && !attribute.isNew) {
                            ItemTypeService.checkRequiredAttributeValues(attribute.id).then(
                                function (data) {

                                }, function (error) {
                                    attribute.required = false;
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    }

                    $scope.editAttributeGroup = editAttributeGroup;
                    function editAttributeGroup(attribute) {
                        var options = {
                            title: "Update Attribute Group",
                            template: 'app/desktop/modules/directives/editAttributeGroupView.jsp',
                            controller: 'EditAttributeGroupController as editAttributeGroupVm',
                            resolve: 'app/desktop/modules/directives/editAttributeGroupController',
                            width: 600,
                            showMask: true,
                            data: {
                                attribute: attribute,
                                selectedType: $rootScope.selectedClassificationType

                            },
                            buttons: [
                                {text: "Update", broadcast: 'app.attributes.group.update'}
                            ],
                            callback: function () {
                                loadAttributes();
                            }
                        };

                        $rootScope.showSidePanel(options);
                    }

                    $scope.onSelectObjectType = onSelectObjectType;
                    function onSelectObjectType(attribute) {
                        attribute.subType = null;
                        attribute.refSubType = null;
                    }

                    $scope.onSelectType = onSelectType;
                    function onSelectType(type, attribute) {
                        if (type != null && type != undefined) {
                            attribute.subType = type;
                            attribute.refSubType = type.id;
                        }
                    }


                    function loadType() {
                        if (vm.workflowType.assignedType != null && vm.workflowType.assignedType != "" && vm.workflowType.assignedType != undefined) {
                            ClassificationService.getAssignedObjectType($rootScope.selectedWfType.id, vm.workflowType.assignedType, vm.workflowType.assignable).then(
                                function (data) {
                                    vm.workflowType.itemType = data;
                                    $timeout(function () {
                                        $scope.$off('app.workflowType.selected', workflowTypeSelected);
                                    }, 200)
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    }

                    $rootScope.closeFormDataErrorNotification = closeFormDataErrorNotification;
                    function closeFormDataErrorNotification() {
                        $rootScope.formDataErrorNotification.removeClass('zoomIn');
                        $rootScope.formDataErrorNotification.addClass('zoomOut');
                        $rootScope.formDataErrorNotification.hide();
                    }

                    (function () {
                        $scope.$on('app.classification.attribute', function (event, data) {
                            objectTypeSelectedForAttributes();
                            $rootScope.classificationId = $rootScope.selectedClassificationType.id;
                            loadMeasurements();
                            loadAttributeGroups();
                            $timeout(function () {
                                resizeAttributesView();
                            }, 200)
                        });
                    })();
                }
            }
        }
    }
)
;
