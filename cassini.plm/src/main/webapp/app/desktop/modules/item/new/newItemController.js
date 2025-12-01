define(
    [
        'app/desktop/modules/item/item.module',
        'moment',
        'moment-timezone-with-data',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/itemBomService',
        'app/desktop/modules/classification/directive/classificationTreeDirective',
        'app/desktop/modules/classification/directive/classificationTreeController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/desktop/modules/classification/directive/itemClassTreeDirective',
        'app/shared/services/core/githubService'
    ],
    function (module) {

        module.controller('NewItemController', NewItemController);

        function NewItemController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce,
                                   AutonumberService, ItemTypeService, ItemService, ObjectAttributeService, AttachmentService,
                                   AttributeAttachmentService, ItemBomService, GitHubService) {

            var vm = this;

            var parsed = angular.element("<div></div>");
            var Each = parsed.html($translate.instant("EACH")).html();

            $scope.itemsMode = $scope.data.itemsMode;

            vm.newItem = {
                id: null,
                itemType: null,
                itemNumber: null,
                itemName: null,
                description: null,
                makeOrBuy: "MAKE",
                configurable: false,
                configured: false,
                instanceItem: null,
                instance: null,
                itemAttributes: [],
                units: Each,
                bomConfiguration: null,
                workflowDefinition: null,
                workflowDefId: null,
                bomConfig: null,
                fromDate: null,
                toDate: null,
                requireCompliance: false
            };

            vm.itemImage = {
                thumbnail: null
            };

            vm.itemRevision = {
                id: null,
                itemMaster: null,
                revision: null,
                lifeCyclePhase: null,
                releasedDate: null,
                hasBom: false,
                hasFiles: false
            };

            vm.selectedGitHubRepo = null;

            vm.attribute = null;
            vm.imageView = true;
            vm.item = null;
            vm.attributes = [];
            vm.newItemAttributes = [];
            vm.newItemRevisionAttributes = [];
            vm.githubRepos = [];

            vm.onSelectType = onSelectType;
            vm.createItem = createItem;
            vm.autoNumber = autoNumber;
            vm.anotherItem = anotherItem;
            vm.addToListValue = addToListValue;
            vm.cancelToListValue = cancelToListValue;
            vm.addAttachment = addAttachment;

            vm.choices = [
                {
                    name: "True",
                    value: true
                },
                {
                    name: "False",
                    value: false
                }
            ];

            var newItemMessage = $translate.instant("ITEM_CREATED_SUCCESSFULLY");

            /*------------------ To get Custom Properties Names  -------------------*/

            function loadObjectAttributeDefs() {
                ItemTypeService.getAllTypeAttributes("ITEM").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newItem.id,
                                    attributeDef: attribute.id
                                },
                                attributeDef: attribute,
                                listValue: null,
                                stringValue: null,
                                newListValue: null,
                                mlistValue: [],
                                timeValue: null,
                                timestampValue: null,
                                listValueEditMode: false,
                                booleanValue: false,
                                dateValue: null,
                                imageValue: null,
                                refValue: null,
                                ref: null,
                                measurementUnit: null,
                                attachmentValues: []
                            };
                            if (attribute.dataType == "TEXT") {
                                att.stringValue = attribute.defaultTextValue;
                            }
                            if (attribute.dataType == "LIST" && !attribute.listMultiple && attribute.defaultListValue != null) {
                                att.listValue = attribute.defaultListValue;
                            }
                            if (attribute.dataType == "LIST" && attribute.listMultiple && attribute.defaultListValue != null) {
                                att.mlistValue.push(attribute.defaultListValue);
                            }
                            if (attribute.lov != null) {
                                att.lovValues = attribute.lov.values;
                            }
                            if (attribute.dataType == "RICHTEXT") {
                                $timeout(function () {
                                    $('.note-current-fontname').text('Arial');
                                }, 1000);

                            }

                            if (attribute.dataType == "TIMESTAMP") {
                                att.timestampValue = moment(new Date()).format("DD/MM/YYYY, HH:mm:ss");
                            }
                            vm.newItemAttributes.push(att);
                        });
                        return ItemTypeService.getAllTypeAttributes("ITEMREVISION");
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                ).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.itemRevision.id,
                                    attributeDef: attribute.id
                                },
                                attributeDef: attribute,
                                listValue: null,
                                mlistValue: [],
                                stringValue: null,
                                newListValue: null,
                                timeValue: null,
                                timestampValue: null,
                                listValueEditMode: false,
                                booleanValue: false,
                                dateValue: null,
                                imageValue: null,
                                refValue: null,
                                ref: null,
                                measurementUnit: null,
                                attachmentValues: []
                            };
                            if (attribute.lov != null) {
                                att.lovValues = attribute.lov.values;
                            }
                            if (attribute.dataType == "TEXT") {
                                att.stringValue = attribute.defaultTextValue;
                            }
                            if (attribute.dataType == "LIST" && !attribute.listMultiple && attribute.defaultListValue != null) {
                                att.listValue = attribute.defaultListValue;
                            }
                            if (attribute.dataType == "LIST" && attribute.listMultiple && attribute.defaultListValue != null) {
                                att.mlistValue.push(attribute.defaultListValue);
                            }
                            if (attribute.dataType == "RICHTEXT") {
                                $timeout(function () {
                                    $('.note-current-fontname').text('Arial');
                                }, 1000);

                            }
                            if (attribute.dataType == "TIMESTAMP") {
                                att.timestampValue = moment(new Date()).format("DD/MM/YYYY, HH:mm:ss");
                            }
                            vm.newItemRevisionAttributes.push(att);
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            /*-----------  To load Classification tree ----------------*/

            function onSelectType(itemType) {
                if (itemType != null && itemType != undefined) {
                    vm.newItem.itemType = itemType;

                    var revSequence = itemType.revisionSequence;
                    if (revSequence != null) {
                        vm.newItem.revision = revSequence.values[0];
                    }

                    var lifecycles = itemType.lifecycle;
                    if (lifecycles != null) {
                        vm.newItem.lifecycle = lifecycles;
                    }
                    if (itemType.itemClass == "PRODUCT" || itemType.itemClass == "DOCUMENT") {
                        document.getElementById("prTypeC").checked = true;
                        vm.newItem.makeOrBuy = "MAKE";
                    }
                    autoNumber();
                }
            }

            $scope.trustAsHtml = function (value) {
                return $sce.trustAsHtml(value);
            };
            function instanceItem() {
                vm.itemInstances = [];
                ItemService.getInstanceItem(vm.newItem.itemType).then(
                    function (data) {
                        vm.itemInstances = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            /*---------  To get Selected Type default Number  -------------*/

            function autoNumber() {
                if (vm.newItem.itemType != null && vm.newItem.itemType.itemNumberSource != null) {
                    var source = vm.newItem.itemType.itemNumberSource;
                    AutonumberService.getNextNumberByName(source.name).then(
                        function (data) {
                            vm.newItem.itemNumber = data;
                            vm.newItem.instanceItem = null;
                            vm.newItem.bomConfig = null;
                            vm.configuredAttributes = [];
                            loadAttributeDefs();
                            instanceItem();
                            loadWorkflows();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            /*--------- To get Selected Type Attribute Names  ------------*/

            function loadAttributeDefs() {
                vm.attributes = [];
                ItemTypeService.getAttributesWithHierarchy(vm.newItem.itemType.id).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newItem.id,
                                    attributeDef: attribute.id

                                },
                                attributeDef: attribute,
                                listValue: null,
                                stringValue: null,
                                mlistValue: [],
                                newListValue: null,
                                listValueEditMode: false,
                                timestampValue: null,
                                booleanValue: false,
                                dateValue: null,
                                timeValue: null,
                                imageValue: null,
                                refValue: null,
                                ref: null,
                                doubleValue: null,
                                measurementUnit: null,
                                attachmentValues: []
                            };
                            if (attribute.lov != null) {
                                att.lovValues = attribute.lov.values;
                            }
                            if (attribute.dataType == "TEXT") {
                                att.stringValue = attribute.defaultTextValue;
                            }
                            if (attribute.dataType == "LIST" && !attribute.listMultiple && attribute.defaultListValue != null) {
                                att.listValue = attribute.defaultListValue;
                            }
                            if (attribute.dataType == "LIST" && attribute.listMultiple && attribute.defaultListValue != null) {
                                att.mlistValue.push(attribute.defaultListValue);
                            }
                            if (attribute.dataType == "TIMESTAMP") {
                                att.timestampValue = moment(new Date()).format("DD/MM/YYYY, HH:mm:ss");
                            }
                            if (attribute.validations != null && attribute.validations != "") {
                                attribute.newValidations = JSON.parse(attribute.validations);
                            }

                            if (attribute.dataType == "RICHTEXT") {
                                $timeout(function () {
                                    $('.note-current-fontname').text('Arial');
                                }, 1000);

                            }

                            vm.attributes.push(att);
                        });

                        if (vm.newItem.configured) {
                            vm.newItem.configurable = false;
                            vm.newItem.configured = true;
                            angular.forEach(vm.attributes, function (attribute) {
                                if (attribute.attributeDef.configurable) {
                                    vm.configuredAttributes.push(attribute);
                                }
                            })
                            $scope.$evalAsync();
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            /*----------- To create items one by one  -------------------*/

            function anotherItem() {
                create().then(
                    function () {
                        document.getElementById("normalItem").checked = true;
                        document.getElementById("configurationItem").checked = false;
                        document.getElementById("configuredItem").checked = false;
                        document.getElementById("prTypeC").checked = false;
                        document.getElementById("prTypeI").checked = false;
                        $scope.$evalAsync();
                        vm.itemImage = {
                            thumbnail: null
                        };
                        vm.newItem = {
                            id: null,
                            itemType: null,
                            itemNumber: null,
                            itemName: null,
                            description: null,
                            configurable: false,
                            configured: false,
                            makeOrBuy: "MAKE",
                            instanceItem: null,
                            instance: null,
                            itemAttributes: [],
                            units: Each,
                            bomConfiguration: null,
                            workflowDefinition: null,
                            workflowDefId: null,
                            bomConfig: null,
                            fromDate: null,
                            toDate: null,
                            requireCompliance: false
                        };
                        vm.imageView = true;

                        setGitHubItemRepository();

                        $rootScope.hideBusyIndicator();
                        $scope.callback(vm.itemRevision);
                    }
                );
            }

            function setGitHubItemRepository() {
                if (vm.selectedGitHubRepo != null) {
                    GitHubService.setGitHubItemRepository(vm.selectedGitHubRepo.id, vm.item.id).then(
                        function () {
                            vm.selectedGitHubRepo = null;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    );
                }
            }

            $scope.setImageValue = function (attribute, files) {
                attribute.imageValue = files[0];
            };

            /*-------------  To create Item only Once --------------*/

            function createItem() {
                create().then(
                    function () {
                        $rootScope.hideSidePanel();
                        $scope.callback();
                    }
                );
            }

            /*-------------- To Create New Item  ---------------------*/

            var attributeRequired = $translate.instant("ATTRIBUTE_REQUIRED");

            function create() {
                var defered = $q.defer();
                $rootScope.closeNotification();
                if (validate()) {
                    if (vm.newItem.configured != true) {
                        vm.newItem.configured = false;
                        $rootScope.showBusyIndicator($('#rightSidePanel'));
                        vm.validattributes = [];
                        vm.validObjectAttributes = [];

                        if (vm.newItem.configurable) {
                            angular.forEach(vm.nonConfigurableAttributes, function (attribute) {
                                if (attribute.attributeDef.required == true && attribute.attributeDef.configurable == false && attribute.attributeDef.dataType != 'BOOLEAN' &&
                                    attribute.attributeDef.dataType != 'TIMESTAMP') {
                                    if ($rootScope.checkAttribute(attribute)) {
                                        vm.validattributes.push(attribute);
                                    }
                                    else {
                                        $rootScope.showErrorMessage(attribute.attributeDef.name + ":" + attributeRequired);
                                    }
                                } else {
                                    vm.validattributes.push(attribute);
                                }
                            })
                        } else {
                            angular.forEach(vm.attributes, function (attribute) {
                                if (attribute.attributeDef.required == true && attribute.attributeDef.dataType != 'BOOLEAN' &&
                                    attribute.attributeDef.dataType != 'TIMESTAMP') {
                                    if ($rootScope.checkAttribute(attribute)) {
                                        vm.validattributes.push(attribute);
                                    }
                                    else {
                                        $rootScope.showErrorMessage(attribute.attributeDef.name + ":" + attributeRequired);
                                    }
                                } else {
                                    vm.validattributes.push(attribute);
                                }
                            })
                        }
                        vm.objectAttributes = [];
                        if (vm.newItemAttributes != null && vm.newItemAttributes != undefined) {
                            vm.objectAttributes = vm.objectAttributes.concat(vm.newItemAttributes);
                        }
                        if (vm.newItemRevisionAttributes != null && vm.newItemRevisionAttributes != undefined) {
                            vm.objectAttributes = vm.objectAttributes.concat(vm.newItemRevisionAttributes);
                        }
                        angular.forEach(vm.objectAttributes, function (attribute) {
                            if (attribute.attributeDef.required == true && attribute.attributeDef.dataType != 'BOOLEAN' &&
                                attribute.attributeDef.dataType != 'TIMESTAMP') {
                                if ($rootScope.checkAttribute(attribute)) {
                                    vm.validObjectAttributes.push(attribute);
                                }
                                else {
                                    $rootScope.showErrorMessage(attribute.attributeDef.name + ":" + attributeRequired);
                                }
                            } else {
                                vm.validObjectAttributes.push(attribute);
                            }
                        });
                        if (!vm.newItem.configurable && (vm.attributes.length == vm.validattributes.length) && (vm.objectAttributes.length == vm.validObjectAttributes.length)) {
                            createNewItem(defered);
                        } else if (vm.newItem.configurable && (vm.nonConfigurableAttributes.length == vm.validattributes.length) && (vm.objectAttributes.length == vm.validObjectAttributes.length)) {
                            createNewItem(defered);
                        }
                        else
                            $rootScope.hideBusyIndicator();
                    } else {
                        if (validateConfiguredAttributes()) {
                            vm.newItem.itemAttributes = angular.copy(vm.configuredAttributes);
                            vm.newItem.configurable = false;
                            if (vm.newItem.instanceItem != null) {
                                vm.newItem.instance = vm.newItem.instanceItem.id;
                            }
                            if (vm.newItem.instanceItem != null && vm.newItem.instanceItem.hasBom && vm.newItem.bomConfig != null) {
                                vm.newItem.bomConfiguration = vm.newItem.bomConfig.id;
                            }
                            ItemBomService.createItemInstances(vm.newItem.instanceItem.latestRevision, vm.newItem).then(
                                function (data) {
                                    document.getElementById("filename").value = "";
                                    if (vm.itemImage.thumbnail != null) {
                                        ItemService.uploadImage(data.id, vm.itemImage.thumbnail).then(
                                            function (data) {
                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
                                            });
                                    }
                                    $rootScope.hideBusyIndicator();
                                    $rootScope.showSuccessMessage(newItemMessage);
                                    defered.resolve();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }

                    }
                }

                return defered.promise;
            }

            function createNewItem(defered) {
                if (vm.newItem.units == "St�ck") {
                    vm.newItem.units = "Each"
                }
                if (vm.newItem.instanceItem != null) {
                    vm.newItem.instance = vm.newItem.instanceItem.id;
                }
                if (vm.newItem.workflowDefinition != null) {
                    vm.newItem.workflowDefId = vm.newItem.workflowDefinition.id;
                }
                ItemService.createItem(vm.newItem).then(
                    function (data) {
                        vm.item = data;
                        vm.newItem.id = data.id;
                        vm.itemRevision.id = data.latestRevision;
                        document.getElementById("filename").value = "";
                        if (vm.itemImage.thumbnail != null) {
                            ItemService.uploadImage(data.id, vm.itemImage.thumbnail).then(
                                function (data) {
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                });
                        }
                        saveObjectAttributes().then(
                            function (data) {
                                saveAttributes().then(
                                    function (data) {
                                        vm.newItemAttributes = [];
                                        vm.newItemRevisionAttributes = [];
                                        loadObjectAttributeDefs();
                                        $rootScope.showSuccessMessage(newItemMessage);
                                        defered.resolve();
                                    }
                                )
                            }
                        )

                    }, function (error) {
                        $rootScope.showWarningMessage(error.message);
                        $rootScope.hideBusyIndicator();
                        defered.reject();
                    }
                )
            }

            function validateConfiguredAttributes() {
                var valid = true;

                angular.forEach(vm.configuredAttributes, function (attribute) {
                    if (valid) {
                        if (attribute.listValue == null || attribute.listValue == "" || attribute.listValue == undefined) {
                            valid = false;
                            $rootScope.showWarningMessage(attribute.attributeDef.name + ":" + attributeRequired);
                        }
                    }
                })


                return valid;
            }

            /*-------  To check Item fields empty or Not  ------------------*/

            var itemTypeValidation = parsed.html($translate.instant("ITEM_TYPE_VALIDATION")).html();
            var itemNumberValidation = parsed.html($translate.instant("ITEM_NUMBER_VALIDATION")).html();
            var itemNameValidation = parsed.html($translate.instant("ITEM_NAME_VALIDATION")).html();
            var makeOrBuyValidation = parsed.html($translate.instant("MAKE_OR_BUY_VALIDATION")).html();
            var descriptionValidation = $translate.instant("DESCRIPTION_VALIDATION");
            var unitsValidation = parsed.html($translate.instant("UNITS_VALIDATION")).html();
            var imageValidation = parsed.html($translate.instant("IMAGE_VALIDATION")).html();
            var itemInstanceValidation = parsed.html($translate.instant("INSTANCE_VALIDATION")).html();
            var configurationValidation = parsed.html($translate.instant("CONFIGURATION_VALIDATION")).html();
            var noConfigurationsMessage = parsed.html($translate.instant("NO_CONFIGURATION_MESSAGE")).html();
            var itemWorkflowValidation = parsed.html($translate.instant("ITEM_WORKFLOW_VALIDATION")).html();
            vm.selectInstance = parsed.html($translate.instant("SELECT_INSTANCE")).html();
            $rootScope.selectBomConfiguration = parsed.html($translate.instant("SELECT_BOM_CONFIGURATION")).html();
            var effectiveToValidation = parsed.html($translate.instant("EFFECTIVE_TO_VALIDATION")).html();
            $scope.effectiveFromPlaceholder = parsed.html($translate.instant("EFFECTIVE_FROM_PLACEHOLDER")).html();
            $scope.effectiveToPlaceholder = parsed.html($translate.instant("EFFECTIVE_TO_PLACEHOLDER")).html();

            function validate() {
                var valid = true;

                if (vm.newItem.itemType == null || vm.newItem.itemType == "" || vm.newItem.itemType == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(itemTypeValidation);
                }
                else if (vm.newItem.configured == true && vm.newItem.instanceItem == null) {
                    valid = false;
                    $rootScope.showWarningMessage(itemInstanceValidation);
                } else if (vm.newItem.configured == true && vm.newItem.instanceItem != null && vm.newItem.instanceItem.hasBom && vm.bomConfigurations.length == 0) {
                    valid = false;
                    $rootScope.showWarningMessage(noConfigurationsMessage);
                } else if (vm.newItem.configured == true && vm.newItem.instanceItem != null && vm.newItem.instanceItem.hasBom && vm.newItem.bomConfig == null) {
                    valid = false;
                    $rootScope.showWarningMessage(configurationValidation);
                }
                else if (vm.newItem.itemNumber == null || vm.newItem.itemNumber == "" || vm.newItem.itemNumber == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(itemNumberValidation);
                }
                else if (vm.newItem.itemName == null || vm.newItem.itemName == "" || vm.newItem.itemName == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(itemNameValidation);
                } else if (vm.newItem.makeOrBuy == null || vm.newItem.makeOrBuy == "" || vm.newItem.makeOrBuy == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(makeOrBuyValidation);
                } else if (vm.newItem.units == null || vm.newItem.units == "" || vm.newItem.units == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(unitsValidation)
                }
                else if (vm.newItem.revision == null) {
                    valid = false;
                    $rootScope.showWarningMessage('Item revision cannot be empty');
                }
                else if (vm.newItem.lifecycle == null) {
                    valid = false;
                    $rootScope.showWarningMessage('Life cycle  cannot be empty');
                }
                else if (vm.newItem.units == null) {
                    valid = false;
                    $rootScope.showWarningMessage(unitsValidation);
                } else if (vm.itemImage.thumbnail != null) {
                    var fup = document.getElementById('filename');
                    var fileName = fup.value;
                    var ext = fileName.substring(fileName.lastIndexOf('.') + 1);
                    if (ext == "JPEG" || ext == "jpeg" || ext == "jpg" || ext == "JPG" || ext == "PNG" || ext == "png" || ext == "GIF" || ext == "gif") {
                        return true;
                    } else {
                        $rootScope.showWarningMessage(imageValidation);
                        fup.focus();
                        valid = false;
                    }
                } else if (vm.newItem.fromDate != null && vm.newItem.fromDate != "" && vm.newItem.toDate != null && vm.newItem.toDate != "") {
                    var effectiveTo = moment(vm.newItem.toDate, $rootScope.applicationDateSelectFormat);
                    var effectiveFrom = moment(vm.newItem.fromDate, $rootScope.applicationDateSelectFormat);
                    var val = effectiveTo.isSame(effectiveFrom) || effectiveTo.isAfter(effectiveFrom);
                    if (!val) {
                        valid = false;
                        $rootScope.showWarningMessage(effectiveToValidation);
                    }
                } else if (!$rootScope.checkAttributeValidations(vm.attributes)) {
                    valid = false;
                } else if (!$rootScope.checkAttributeValidations(vm.requiredAttributes)) {
                    valid = false;
                }

                return valid;
            }

            /*-----------  To save Selected Type Attributes  ----------------*/

            function saveAttributes() {
                var defered = $q.defer();
                vm.imageAttributes = [];
                var images = new Hashtable();

                if (vm.newItem.configurable) {
                    angular.forEach(vm.nonConfigurableAttributes, function (attribute) {
                        if (attribute.attributeDef.revisionSpecific == false) {
                            attribute.id.objectId = vm.newItem.id;

                            if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                                images.put(attribute.id.attributeDef, attribute.imageValue);
                                vm.imageAttributes.push(attribute);
                                attribute.imageValue = null;
                            }
                            if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length > 0) {
                                attribute.attachmentValues = addAttachment(attribute);
                            }
                        } else if (attribute.attributeDef.revisionSpecific == true) {
                            attribute.id.objectId = vm.itemRevision.id;

                            if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                                images.put(attribute.id.attributeDef, attribute.imageValue);
                                vm.imageAttributes.push(attribute);
                                attribute.imageValue = null;
                            }
                            if (attribute.timeValue != null) {
                                /* attribute.timeValue = moment(attribute.timeValue).format("HH:mm:ss");*/
                            }
                            if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length > 0) {
                                attribute.attachmentValues = addRevisionAttachment(attribute);
                            }
                        }

                    });

                    if (vm.nonConfigurableAttributes.length > 0) {
                        $timeout(function () {
                            ItemService.saveItemAttributes(vm.item.id, vm.nonConfigurableAttributes).then(
                                function (data) {
                                    if (vm.imageAttributes.length > 0) {
                                        angular.forEach(vm.imageAttributes, function (imgAtt) {
                                            ItemService.updateImageValue(imgAtt.id.objectId, imgAtt.id.attributeDef, images.get(imgAtt.id.attributeDef)).then(
                                                function (data) {

                                                    defered.resolve();
                                                }, function (error) {
                                                    $rootScope.showErrorMessage(error.message);
                                                }
                                            )
                                        })
                                    } else {
                                        defered.resolve();
                                    }
                                }, function () {
                                    defered.reject();
                                }
                            )
                        }, 5000)
                    } else {
                        defered.resolve();
                    }

                } else {
                    angular.forEach(vm.attributes, function (attribute) {
                        if (attribute.attributeDef.revisionSpecific == false) {
                            attribute.id.objectId = vm.newItem.id;

                            if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                                images.put(attribute.id.attributeDef, attribute.imageValue);
                                vm.imageAttributes.push(attribute);
                                attribute.imageValue = null;
                            }
                            if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length > 0) {
                                attribute.attachmentValues = addAttachment(attribute);
                            }
                        } else if (attribute.attributeDef.revisionSpecific == true) {
                            attribute.id.objectId = vm.itemRevision.id;

                            if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                                images.put(attribute.id.attributeDef, attribute.imageValue);
                                vm.imageAttributes.push(attribute);
                                attribute.imageValue = null;
                            }
                            if (attribute.timeValue != null) {
                                /* attribute.timeValue = moment(attribute.timeValue).format("HH:mm:ss");*/
                            }
                            if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length > 0) {
                                attribute.attachmentValues = addRevisionAttachment(attribute);
                            }
                        }

                    });

                    if (vm.attributes.length > 0) {
                        $timeout(function () {
                            ItemService.saveItemAttributes(vm.item.id, vm.attributes).then(
                                function (data) {
                                    if (vm.imageAttributes.length > 0) {
                                        angular.forEach(vm.imageAttributes, function (imgAtt) {
                                            ItemService.updateImageValue(imgAtt.id.objectId, imgAtt.id.attributeDef, images.get(imgAtt.id.attributeDef)).then(
                                                function (data) {

                                                    defered.resolve();
                                                }, function (error) {
                                                    $rootScope.showErrorMessage(error.message);
                                                }
                                            )
                                        })
                                    } else {
                                        defered.resolve();
                                    }
                                }, function () {
                                    defered.reject();
                                }
                            )
                        }, 5000)
                    } else {
                        defered.resolve();
                    }
                }
                return defered.promise;
            }

            /*------------- To save Non-Revision specific Attachment ------------*/

            function addAttachment(attribute) {
                var attachmentIds = [];
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'ITEM', attachmentFile).then(
                        function (data) {
                            attachmentIds.push(data[0].id);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                })

                return attachmentIds;
            }

            /*------------ To save Revision specific Attachments  --------------*/

            function addRevisionAttachment(attribute) {
                var revisionAttachmentIds = [];
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'ITEMREVISION', attachmentFile).then(
                        function (data) {
                            revisionAttachmentIds.push(data[0].id);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        })
                })
                return revisionAttachmentIds;
            }

            /*----------- To save Item & ItemRevision Properties  ----------------*/

            function saveObjectAttributes() {
                var defered = $q.defer();
                if (vm.newItemAttributes.length > 0) {
                    vm.propertyImageAttributes = [];
                    var propertyImages = new Hashtable();
                    angular.forEach(vm.newItemAttributes, function (attribute) {
                        attribute.id.objectId = vm.newItem.id;
                        /*if (attribute.timeValue != null) {
                         attribute.timeValue = moment(attribute.timeValue).format("HH:mm:ss");

                         }*/
                        if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                            propertyImages.put(attribute.id.attributeDef, attribute.imageValue);
                            vm.propertyImageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length > 0) {
                            attribute.attachmentValues = addItemPropertyAttachment(attribute);
                        }
                    });
                    $timeout(function () {
                        ObjectAttributeService.saveItemObjectAttributes(vm.item.id, vm.newItemAttributes).then(
                            function (data) {
                                if (vm.propertyImageAttributes.length > 0) {
                                    angular.forEach(vm.propertyImageAttributes, function (propImgAtt) {
                                        ObjectAttributeService.uploadObjectAttributeImage(propImgAtt.id.objectId, propImgAtt.id.attributeDef, propertyImages.get(propImgAtt.id.attributeDef)).then(
                                            function (data) {
                                                defered.resolve();
                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
                                            }
                                        )
                                    })
                                } else {
                                    defered.resolve();
                                }
                                defered.resolve();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                                defered.reject();
                            }
                        )
                    }, 3000);
                } else if (vm.newItemRevisionAttributes <= 0) {
                    defered.resolve();
                }
                if (vm.newItemRevisionAttributes.length > 0) {
                    vm.itemRevisionPropertyImageAttributes = [];
                    var itemRevisionPropertyImages = new Hashtable();
                    angular.forEach(vm.newItemRevisionAttributes, function (attribute) {
                        attribute.id.objectId = vm.itemRevision.id;
                        /* if (attribute.timeValue != null) {
                         attribute.timeValue = moment(attribute.timeValue).format("HH:mm:ss");

                         }*/
                        if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                            itemRevisionPropertyImages.put(attribute.id.attributeDef, attribute.imageValue);
                            vm.itemRevisionPropertyImageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length > 0) {
                            attribute.attachmentValues = addRevisionPropertyAttachment(attribute);
                        }
                    });
                    $timeout(function (data) {
                        ObjectAttributeService.saveItemObjectAttributes(vm.itemRevision.id, vm.newItemRevisionAttributes).then(
                            function (data) {
                                if (vm.itemRevisionPropertyImageAttributes.length > 0) {
                                    angular.forEach(vm.itemRevisionPropertyImageAttributes, function (revpropImgAtt) {
                                        ObjectAttributeService.uploadObjectAttributeImage(revpropImgAtt.id.objectId, revpropImgAtt.id.attributeDef, itemRevisionPropertyImages.get(revpropImgAtt.id.attributeDef)).then(
                                            function (data) {
                                                defered.resolve();
                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
                                            }
                                        )
                                    })
                                } else {
                                    defered.resolve();
                                }
                                defered.resolve();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                                defered.reject();
                            }
                        )
                    }, 3000);
                } else if (vm.newItemAttributes <= 0) {
                    defered.resolve();
                }
                return defered.promise;
            }

            /*-------------- To save Item Property Attachment  -------------------*/

            function addItemPropertyAttachment(attribute) {
                var propertyAttachmentIds = [];
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'ITEM', attachmentFile).then(
                        function (data) {
                            propertyAttachmentIds.push(data[0].id);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                })
                return propertyAttachmentIds;
            }

            /*-------------- To save ItemRevision property Attachment  ----------------*/

            function addRevisionPropertyAttachment(attribute) {
                var revisionPropertyAttachmentIds = [];
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'ITEMREVISION', attachmentFile).then(
                        function (data) {
                            revisionPropertyAttachmentIds.push(data[0].id);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                })

                return revisionPropertyAttachmentIds;
            }

            function addToListValue(attribute) {
                if (attribute.listValue == undefined) {
                    attribute.listValue = [];
                }
                attribute.listValue.push(attribute.newListValue);
            }

            function cancelToListValue(attribute) {
                attribute.listValueEditMode = false;
            }

            vm.selectConfiguration = selectConfiguration;
            function selectConfiguration(value, event) {
                vm.configuredAttributes = [];
                vm.configurableAttributes = [];
                vm.nonConfigurableAttributes = [];
                if (value == 'None') {
                    vm.newItem.configurable = false;
                    vm.newItem.configured = false;
                    vm.newItem.instanceItem = null;
                    vm.newItem.bomConfig = null;
                }
                if (value == 'Configurable') {
                    angular.forEach(vm.attributes, function (attribute) {
                        if (attribute.attributeDef.configurable) {
                            vm.configurableAttributes.push(attribute);
                        } else {
                            vm.nonConfigurableAttributes.push(attribute);
                        }
                    })
                    vm.newItem.configurable = true;
                    vm.newItem.configured = false;
                    vm.newItem.instanceItem = null;
                    vm.newItem.bomConfig = null;
                }
                if (value == 'Configured') {
                    angular.forEach(vm.attributes, function (attribute) {
                        if (attribute.attributeDef.configurable) {
                            vm.configuredAttributes.push(attribute);
                        }
                    })
                    vm.newItem.configurable = false;
                    vm.newItem.configured = true;
                }
                $scope.$evalAsync();

            }

            vm.bomConfigurations = [];
            vm.selectItem = selectItem;
            function selectItem() {
                if (vm.newItem.instanceItem != null) {
                    ItemBomService.getItemBomConfigurations(vm.newItem.instanceItem.latestRevision).then(
                        function (data) {
                            vm.bomConfigurations = data;
                            if (vm.newItem.instanceItem.hasBom && vm.bomConfigurations.length == 0) {
                                $rootScope.showWarningMessage(noConfigurationsMessage)
                            }
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            vm.selectBomConfiguration = selectBomConfiguration;
            function selectBomConfiguration() {
                if (vm.newItem.bomConfig != null) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    ItemBomService.getConfiguredAttributeValues(0, vm.newItem.bomConfig.id).then(
                        function (data) {
                            vm.configuredAttributes = [];
                            angular.forEach(data, function (attribute) {
                                var att = {
                                    id: {
                                        objectId: vm.newItem.id,
                                        attributeDef: attribute.id

                                    },
                                    attributeDef: attribute,
                                    listValue: attribute.value,
                                    stringValue: null,
                                    mlistValue: [],
                                    newListValue: null,
                                    listValueEditMode: false,
                                    timestampValue: null,
                                    booleanValue: false,
                                    dateValue: null,
                                    timeValue: null,
                                    imageValue: null,
                                    refValue: null,
                                    ref: null,
                                    attachmentValues: []
                                };
                                if (attribute.lov != null) {
                                    att.lovValues = attribute.lov.values;
                                }

                                vm.configuredAttributes.push(att);
                            });
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            vm.newItem.bomConfig = null;
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function loadWorkflows() {
                ItemService.getWorkflows(vm.newItem.itemType.id, 'ITEMS').then(
                    function (data) {
                        vm.workflows = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.selectMakeOrBuy = selectMakeOrBuy;
            function selectMakeOrBuy(value) {
                if (value == 'make') {
                    vm.newItem.makeOrBuy = "MAKE";
                }
                if (value == 'buy') {
                    vm.newItem.makeOrBuy = "BUY";
                }
                $scope.$evalAsync();

            }


            function loadGitHubRepositories() {
                GitHubService.getAllGitHubRepositories().then(
                    function (data) {
                        vm.githubRepos = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                loadObjectAttributeDefs();
                loadGitHubRepositories();
                $rootScope.hideBusyIndicator();
                $rootScope.$on('app.items.new', anotherItem);
            })();
        }
    }
)
;