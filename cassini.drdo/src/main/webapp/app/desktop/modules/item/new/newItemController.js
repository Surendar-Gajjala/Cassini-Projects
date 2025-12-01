define(
    [
        'app/desktop/modules/item/item.module',
        'moment',
        'moment-timezone-with-data',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemService',
        'app/desktop/modules/classification/directive/classificationTreeDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController'
    ],
    function (module) {

        module.controller('NewItemController', NewItemController);

        function NewItemController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies,
                                   AutonumberService, ItemTypeService, ItemService, ObjectAttributeService, AttachmentService, AttributeAttachmentService) {
            if ($application.homeLoaded == false) {
                return;
            }

            var vm = this;

            vm.newItem = {
                id: null,
                itemType: null,
                itemNumber: null,
                itemName: null,
                itemCode: null,
                description: null,
                units: "Each",
                drawingNumber: null,
                partSpec: null,
                material: null
            };

            vm.itemImage = {
                thumbnail: null
            };

            vm.itemRevision = {
                id: null,
                itemMaster: null,
                revision: null,
                hasBom: false,
                hasFiles: false
            };

            vm.attribute = null;
            vm.searchValue = null;
            vm.imageView = true;
            vm.item = null;
            vm.attributes = [];
            vm.requiredAttributes = [];
            vm.newItemAttributes = [];
            vm.newItemRevisionAttributes = [];
            vm.onSelectType = onSelectType;
            vm.createItem = createItem;
            vm.autoNumber = autoNumber;
            vm.anotherItem = anotherItem;
            vm.addToListValue = addToListValue;
            vm.cancelToListValue = cancelToListValue;
            vm.addAttachment = addAttachment;
            vm.searchTree = searchTree;

            vm.specificationTitle = "Select Specification";
            vm.typeCodeMap = new Hashtable();
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
                ItemTypeService.getAttributesByObjectType("ITEMMASTER").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newItem.id,
                                    attributeDef: attribute.id
                                },
                                attributeDef: attribute,
                                listValue: null,
                                newListValue: null,
                                timeValue: null,
                                timestampValue: null,
                                listValueEditMode: false,
                                booleanValue: false,
                                dateValue: null,
                                imageValue: null,
                                attachmentValues: [],
                                mListValue: []
                            };
                            if (attribute.lov != null) {
                                att.lovValues = attribute.lov.values;
                            }

                            if (attribute.dataType == "TIMESTAMP") {
                                att.timestampValue = moment(new Date()).format("DD/MM/YYYY, HH:mm:ss");
                            }
                            vm.newItemAttributes.push(att);
                        });
                        return ItemTypeService.getAttributesByObjectType("ITEMREVISION");
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
                                newListValue: null,
                                timeValue: null,
                                timestampValue: null,
                                listValueEditMode: false,
                                booleanValue: false,
                                dateValue: null,
                                imageValue: null,
                                attachmentValues: [],
                                mListValue: []
                            };
                            if (attribute.lov != null) {
                                att.lovValues = attribute.lov.values;
                            }
                            if (attribute.dataType == "TIMESTAMP") {
                                att.timestampValue = moment(new Date()).format("DD/MM/YYYY, HH:mm:ss");
                            }
                            vm.newItemRevisionAttributes.push(att);
                        });
                    }
                )
            }

            /*-----------  To load Classification tree ----------------*/

            function onSelectType(itemType) {
                $("#typeTree").show();
                if (itemType != null && itemType != undefined) {
                    vm.newItem.itemType = itemType;
                    vm.newItem.itemCode = itemType.typeCode;
                    vm.newItem.itemName = itemType.name;
                    vm.newItem.material = null;
                    vm.newItem.partSpec = null;
                    vm.newItem.drawingNumber = null;
                    $("#typeTree").hide();
                    loadSelectTypeParentNode();
                }
            }

            /*---------  To get Selected Type default Number  -------------*/

            function autoNumber() {
                if (vm.newItem.itemType != null && vm.newItem.itemType.itemNumberSource != null) {
                    var source = vm.newItem.itemType.itemNumberSource;
                    AutonumberService.getNextNumber(source.id).then(
                        function (data) {
                            vm.newItem.itemNumber = data;
                            loadAttributeDefs();
                        }
                    )
                }
            }

            function loadSelectTypeParentNode() {
                ItemTypeService.getTypeParentNode(vm.newItem.itemType.id).then(
                    function (data) {
                        vm.typeParentNode = data.itemType;
                        vm.itemTypeSpecs = data.itemTypeSpecs;
                        if (vm.itemTypeSpecs.length == 0) {
                            vm.specificationTitle = "No Specifications";
                        } else {
                            vm.specificationTitle = "Select Specification";
                        }
                    }
                )
            }

            /*--------- To get Selected Type Attribute Names  ------------*/

            function loadAttributeDefs() {
                vm.requiredAttributes = [];
                vm.attributes = [];
                ItemTypeService.getAttributesByTypeId(vm.newItem.itemType.id).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newItem.id,
                                    attributeDef: attribute.id

                                },
                                attributeDef: attribute,
                                listValue: null,
                                newListValue: null,
                                listValueEditMode: false,
                                timestampValue: null,
                                booleanValue: false,
                                dateValue: null,
                                timeValue: null,
                                imageValue: null,
                                attachmentValues: []
                            };
                            if (attribute.lov != null) {
                                att.lovValues = attribute.lov.values;
                            }
                            if (attribute.dataType == "TIMESTAMP") {
                                att.timestampValue = moment(new Date()).format("DD/MM/YYYY, HH:mm:ss");
                            }
                            if (attribute.required == false) {
                                vm.attributes.push(att);
                            } else {
                                vm.requiredAttributes.push(att);
                            }
                        });
                    }
                )
            }

            /*----------- To create items one by one  -------------------*/

            function anotherItem() {
                create().then(
                    function () {
                        vm.itemImage = {
                            thumbnail: null
                        };
                        vm.newItem = {
                            itemType: vm.item.itemType,
                            itemNumber: null,
                            itemName: vm.item.itemType.name,
                            itemCode: vm.item.itemType.typeCode,
                            description: null,
                            units: vm.item.itemType.units,
                            drawingNumber: null,
                            partSpec: null,
                            material: null
                        };
                        vm.imageView = true;
                        $rootScope.hideBusyIndicator();
                        $scope.callback();
                    }
                );
            }

            $scope.setImageValue = function (attribute, files) {
                attribute.imageValue = files[0];
            }

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

            function create() {
                var defered = $q.defer();
                $rootScope.closeNotification();
                if (validate()) {
                    vm.imageView = false;
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    vm.validattributes = [];
                    vm.validObjectAttributes = [];
                    angular.forEach(vm.requiredAttributes, function (attribute) {
                        if (attribute.attributeDef.required == true && attribute.attributeDef.dataType != 'BOOLEAN' &&
                            attribute.attributeDef.dataType != 'TIMESTAMP') {
                            if ($rootScope.checkAttribute(attribute)) {
                                vm.validattributes.push(attribute);
                            }
                            else {
                                $rootScope.showErrorMessage(attribute.attributeDef.name + " : Attribute value required");
                            }
                        } else {
                            vm.validattributes.push(attribute);
                        }
                    });
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
                                $rootScope.showErrorMessage(attribute.attributeDef.name + " : Attribute value required");
                            }
                        } else {
                            vm.validObjectAttributes.push(attribute);
                        }
                    });
                    if ((vm.requiredAttributes.length == vm.validattributes.length) && (vm.objectAttributes.length == vm.validObjectAttributes.length)) {
                        ItemService.createItem(vm.newItem).then(
                            function (data) {
                                vm.item = data;
                                vm.newItem.id = data.id;
                                vm.itemRevision.id = data.latestRevision;
                                if (vm.itemImage.thumbnail != null) {
                                    ItemService.uploadImage(data.id, vm.itemImage.thumbnail).then(
                                        function (data) {
                                        }
                                    );
                                }
                                saveObjectAttributes().then(
                                    function (data) {
                                        saveAttributes().then(
                                            function (data) {
                                                vm.newItemAttributes = [];
                                                vm.newItemRevisionAttributes = [];
                                                loadObjectAttributeDefs();
                                                $rootScope.showSuccessMessage("Item created successfully");
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
                    else
                        $rootScope.hideBusyIndicator();
                }

                return defered.promise;
            }

            /*-------  To check Item fields empty or Not  ------------------*/

            function validate() {
                var valid = true;
                var mess = "";

                if (vm.newItem.itemType == null || vm.newItem.itemType == "" || vm.newItem.itemType == undefined) {
                    valid = false;
                    mess = "Please select Type";
                } else if (vm.newItem.itemName == null || vm.newItem.itemName == "" || vm.newItem.itemName == undefined) {
                    valid = false;
                    mess = "Please enter Nomenclature";
                } else if (vm.newItem.itemType.hasSpec && (vm.newItem.partSpec == null || vm.newItem.partSpec == "" || vm.newItem.partSpec == undefined)) {
                    valid = false;
                    mess = "Please select Specification";
                } else if (vm.itemImage.thumbnail != null) {
                    var fup = document.getElementById('filename');
                    var fileName = fup.value;
                    var ext = fileName.substring(fileName.lastIndexOf('.') + 1);
                    if (ext == "JPEG" || ext == "jpeg" || ext == "jpg" || ext == "JPG" || ext == "PNG" || ext == "png" || ext == "GIF" || ext == "gif") {
                        valid = true;
                    } else {
                        mess = "Please upload image only";
                        fup.focus();
                        valid = false;
                    }
                }
                if (mess != "") {
                    $rootScope.showErrorMessage(mess);
                }
                return valid;
            }

            function validTypeCode() {
                var valid = true;

                var itemCode = vm.typeCodeMap.get(vm.newItem.itemCode.toUpperCase());

                if (itemCode != null) {
                    valid = false;
                    $rootScope.showWarningMessage(itemCode.itemCode + " : Item Code already exist");
                }

                return valid;
            }

            function validTypeCodeLength() {
                var valid = true;

                if (vm.typeParentNode.name == "Section" || vm.typeParentNode.name == "Sub System") {
                    if (vm.newItem.itemCode.length != 1) {
                        valid = false;
                        $rootScope.showWarningMessage("Item code accepts 1 character (or) digit only")
                    }
                } else if (vm.typeParentNode.name == "System" || vm.typeParentNode.name == "Unit" || vm.typeParentNode.name == "Part") {
                    if (vm.newItem.itemCode.length != 2) {
                        valid = false;
                        $rootScope.showWarningMessage("Item code accepts 2 characters (or) digits only")
                    }
                }

                return valid;
            }

            /*-----------  To save Selected Type Attributes  ----------------*/

            function saveAttributes() {
                var defered = $q.defer();
                vm.imageAttributes = [];
                var images = new Hashtable();
                angular.forEach(vm.requiredAttributes, function (reqatt) {
                    vm.attributes.push(reqatt);
                })
                if (vm.attributes.length > 0) {
                    vm.requiredAttributes = [];
                    angular.forEach(vm.attributes, function (attribute) {
                        if (attribute.attributeDef.revisionSpecific == false) {
                            attribute.id.objectId = vm.newItem.id;

                            if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                                images.put(attribute.id.attributeDef, attribute.imageValue);
                                vm.imageAttributes.push(attribute);
                                attribute.imageValue = null;
                            }
                            if (attribute.timeValue != null) {
                                attribute.timeValue = moment(attribute.timeValue).format("HH:mm:ss");
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
                                attribute.timeValue = moment(attribute.timeValue).format("HH:mm:ss");
                            }
                            if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length > 0) {
                                attribute.attachmentValues = addRevisionAttachment(attribute);
                            }
                        }

                    });
                    $timeout(function () {
                        if (vm.attributes.length > 0) {
                            ItemService.saveItemAttributes(vm.item.id, vm.attributes).then(
                                function (data) {
                                    if (vm.imageAttributes.length > 0) {
                                        angular.forEach(vm.imageAttributes, function (imgAtt) {
                                            ItemService.updateImageValue(imgAtt.id.objectId, imgAtt.id.attributeDef, images.get(imgAtt.id.attributeDef)).then(
                                                function (data) {

                                                    defered.resolve();
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
                        }
                    }, 5000)
                } else {
                    defered.resolve();
                }
                return defered.promise;
            }

            /*------------- To save Non-Revision specific Attachment ------------*/

            function addAttachment(attribute) {
                var attachmentIds = [];
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'ITEMMASTER', attachmentFile).then(
                        function (data) {
                            attachmentIds.push(data[0].id);
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
                        if (attribute.timeValue != null) {
                            attribute.timeValue = moment(attribute.timeValue).format("HH:mm:ss");

                        }
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
                                            }
                                        )
                                    })
                                } else {
                                    defered.resolve();
                                }
                                defered.resolve();
                            },
                            function (error) {
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
                        if (attribute.timeValue != null) {
                            attribute.timeValue = moment(attribute.timeValue).format("HH:mm:ss");

                        }
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

                                            }
                                        )
                                    })
                                } else {
                                    defered.resolve();
                                }
                                defered.resolve();
                            },
                            function (error) {
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
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'ITEMMASTER', attachmentFile).then(
                        function (data) {
                            propertyAttachmentIds.push(data[0].id);
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

            function searchTree() {
                if (vm.searchValue != null) {
                    $('#classificationTree').tree('expandAll');
                }
                $('#classificationTree').tree('doFilter', vm.searchValue);
            }

            vm.showDropMenu = showDropMenu;
            function showDropMenu() {

                $("#typeTree").show();
                vm.searchValue = null;
                $('#classificationTree').tree('doFilter', vm.searchValue)
            }

            function loadItemTypes() {
                ItemTypeService.getAllItemTypesWithCode().then(
                    function (data) {
                        vm.itemTypes = data;
                    }
                )
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadObjectAttributeDefs();
                    loadItemTypes();
                    $rootScope.$on('app.items.new', anotherItem);
                }
            })();
        }
    }
)
;