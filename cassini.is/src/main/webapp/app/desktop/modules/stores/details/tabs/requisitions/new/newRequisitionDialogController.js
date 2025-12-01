/**
 * Created by swapna on 13/09/18.
 */
define(['app/desktop/modules/stores/store.module',
        'app/shared/services/pm/project/projectService',
        'app/shared/services/store/topStoreService',
        'app/shared/services/core/itemService',
        'app/shared/services/store/requisitionService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController'

    ],
    function (module) {
        module.controller('NewRequestController', NewRequestController);

        function NewRequestController($scope, $rootScope, $stateParams, $state, ProjectService, TopStoreService, ItemService, DialogService,
                                      RequisitionService, AutonumberService, ObjectTypeAttributeService, $application, AttributeAttachmentService, ObjectAttributeService, $q, $timeout) {
            var vm = this;
            $rootScope.viewInfo.title = "New Requisition";

            vm.showAddRequestsButton = false;
            vm.showCreateRequisitionButton = false;
            vm.loading = false;
            vm.projects = [];
            vm.requisitionItems = [];
            vm.addItems = addItems;
            var reqItemsMap = new Hashtable();
            vm.create = create;
            vm.removeFromItemList = removeFromItemList;
            vm.generateAutoNumber = generateAutoNumber;

            vm.back = back;
            vm.requisitionAttributes = [];
            vm.requiredAttributes = [];
            vm.attributes = [];
            vm.newRequisition = {
                project: null,
                notes: null,
                store: null,
                requestedBy: null,
                customRequisitionItems: []

            };

            vm.emptyReqItem = {
                materialItem: null,
                notes: null,
                quantity: null
            };

            function loadProjects() {
                ProjectService.getAllProjects().then(
                    function (data) {
                        vm.projects = data;
                    }
                );
            }

            function loadStore() {
                TopStoreService.getTopStore($rootScope.storeId).then(
                    function (data) {
                        vm.newRequisition.store = data;
                        $rootScope.viewInfo.description = "Store : " + data.storeName;
                    }
                )
            }

            function removeFromItemList(item) {
                var index = vm.requisitionItems.indexOf(item);
                vm.requisitionItems.splice(index, 1);
                reqItemsMap.remove(item.itemNumber);
            }

            function generateAutoNumber() {
                if ($application.config != undefined) {
                    var prefix = $application.config.autoNumber.prefix;
                    /*var year = $application.config.autoNumber.year;*/
                    var year = new Date().getFullYear();
                }
                AutonumberService.getAutonumberByName('Default Requisition Number Source').then(
                    function (data) {
                        AutonumberService.getNextNumber(data.id).then(
                            function (data) {
                                vm.generatedNumber = data;
                                if (prefix != "" && year != "" && prefix != undefined && year != undefined) {
                                    vm.newRequisition.requisitionNumber = prefix + "/ " + $rootScope.selectedStore.storeName + "/" + year + "/" + vm.generatedNumber;
                                }
                                else {
                                    vm.newRequisition.requisitionNumber = vm.generatedNumber;
                                }
                            }
                        )
                    });
            }

            function addItems() {
                if (validate()) {
                    var options = {
                        title: 'Requisition Items',
                        showMask: true,
                        template: 'app/desktop/modules/stores/details/tabs/requisitions/new/newRequisitionItemsDialogView.jsp',
                        controller: 'NewRequisitionItemsController as newReqItemsVm',
                        resolve: 'app/desktop/modules/stores/details/tabs/requisitions/new/newRequisitionItemsDialogController.js',
                        width: 700,
                        data: {
                            projctObj: vm.newRequisition.project,
                            newRequisition: vm.newRequisition,
                            addedItemsMap: reqItemsMap
                        },
                        buttons: [],
                        callback: function (reqItem) {
                            var item = reqItemsMap.get(reqItem.itemNumber);
                            if (item == null) {
                                reqItemsMap.put(reqItem.itemNumber, reqItem);
                                vm.requisitionItems.push(reqItem);
                            }
                        }
                    };

                    $rootScope.showSidePanel(options);
                }
            }

            function validateRequisition() {
                var valid = false;
                vm.reqItems = [];
                var counter = 0;
                angular.forEach(vm.requisitionItems, function (reqItem) {
                    if (reqItem.quantity > 0) {
                        var copiedreqItem = angular.copy(vm.emptyReqItem);
                        copiedreqItem.materialItem = reqItem;
                        copiedreqItem.notes = reqItem.notes;
                        copiedreqItem.quantity = reqItem.quantity;
                        vm.reqItems.push(copiedreqItem);
                        counter++;
                        if (counter == vm.requisitionItems.length) {
                            valid = true;
                            vm.newRequisition.customRequisitionItems = vm.reqItems;
                        }
                    } else {
                        valid = false;
                        $rootScope.showErrorMessage("Please enter qty to item's");
                    }
                });

                return valid;
            }

            function create() {
                if (validateRequisition()) {
                    attributesValidate().then(
                        function (success) {
                            RequisitionService.createRequisition($rootScope.storeId, vm.newRequisition).then(
                                function (data) {
                                    vm.newRequisition = data;
                                    intializationForAttributesSave().then(
                                        function (sucess) {
                                            $rootScope.hideBusyIndicator();
                                            $rootScope.requisitionId = vm.newRequisition.id;
                                            $state.go('app.store.stock.requisitionDetails', {requisitionId: vm.newRequisition.id});
                                            $rootScope.showSuccessMessage("Requisition (" + vm.newRequisition.requisitionNumber + ") created successfully");
                                        }, function (error) {

                                        });

                                }, function (error) {

                                }
                            )
                        }, function (error) {

                        });
                }
            }

            function validate() {
                var valid = true;
                if (vm.newRequisition.project == null || vm.newRequisition.project == undefined) {
                    valid = false;
                    $rootScope.showErrorMessage("Please select Project");
                }
                else if (vm.newRequisition.requestedBy == null || vm.newRequisition.requestedBy == "") {
                    valid = false;
                    $rootScope.showErrorMessage("Field 'Requested By' cannot be empty");
                }
                return valid;
            }

            function resize() {
                var height = $(window).height();
                $('#contentpanel').height(height - 200);
            }

            function back() {
                $state.go('app.store.details', {storeId: $rootScope.storeId, mode: 'REQ'});
            }

            /* ............. start attributes methods block ............. */

            function addAttachment(attribute) {
                var defered = $q.defer();
                vm.propertyAttachmentIds = [];
                var counter = 0;
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'CUSTOM_REQUISITION', attachmentFile).then(
                        function (data) {
                            vm.propertyAttachmentIds.push(data[0].id);
                            counter++;
                            if (counter == attribute.attachmentValues.length) {
                                defered.resolve(true);
                            }
                        }
                    )
                });
                return defered.promise;
            }

            function saveObjectAttributes() {
                var defered = $q.defer();
                if (vm.newRequisitionAttributes.length > 0) {
                    angular.forEach(vm.newRequisitionAttributes, function (att) {
                        if (att.dateValue == "") {
                            att.dateValue = null;
                        }
                    });
                    ObjectAttributeService.saveItemObjectAttributes(vm.newRequisition.id, vm.newRequisitionAttributes).then(
                        function (data) {
                            var secCount = 0;
                            if (vm.propertyImageAttributes.length > 0) {
                                angular.forEach(vm.propertyImageAttributes, function (propImgAtt) {
                                    ObjectAttributeService.uploadObjectAttributeImage(propImgAtt.id.objectId, propImgAtt.id.attributeDef, vm.propertyImages.get(propImgAtt.id.attributeDef)).then(
                                        function (data) {
                                        });
                                    secCount++;
                                    if (secCount == vm.propertyImageAttributes.length) {
                                        defered.resolve();
                                    }
                                });
                            } else {
                                defered.resolve();
                            }
                        },
                        function (error) {
                            defered.reject();
                        }
                    )

                } else {
                    defered.resolve();
                }
                return defered.promise;
            }

            function attributesValidate() {
                var defered = $q.defer();
                $rootScope.closeNotification();
                vm.objectAttributes = [];
                vm.validObjectAttributes = [];
                vm.validattributes = [];
                angular.forEach(vm.requiredAttributes, function (attribute) {
                    if (attribute.attributeDef.required == true && attribute.attributeDef.dataType != 'BOOLEAN' &&
                        attribute.attributeDef.dataType != 'TIMESTAMP') {
                        if (checkAttribute(attribute)) {
                            vm.validattributes.push(attribute);
                        }
                        else {
                            $rootScope.showErrorMessage(attribute.attributeDef.name + ": attribute is required");
                            $rootScope.hideBusyIndicator();
                        }
                    } else {
                        vm.validattributes.push(attribute);
                    }
                });
                vm.objectAttributes = [];
                if (vm.newRequisitionAttributes != null && vm.newRequisitionAttributes != undefined) {
                    vm.objectAttributes = vm.objectAttributes.concat(vm.newRequisitionAttributes);
                }
                angular.forEach(vm.objectAttributes, function (attribute) {
                    if (attribute.attributeDef.required == true && attribute.attributeDef.dataType != 'BOOLEAN' &&
                        attribute.attributeDef.dataType != 'TIMESTAMP') {
                        if (checkAttribute(attribute)) {
                            vm.validObjectAttributes.push(attribute);
                        }
                        else {
                            $rootScope.showErrorMessage(attribute.attributeDef.name + ": attribute is required");
                            $rootScope.hideBusyIndicator();
                        }
                    } else {
                        vm.validObjectAttributes.push(attribute);
                    }
                });
                if (vm.requiredAttributes.length == vm.validattributes.length && vm.objectAttributes.length == vm.validObjectAttributes.length) {
                    defered.resolve();
                } else {
                    defered.reject();
                }
                return defered.promise;
            }

            function loadObjectAttributeDefs() {
                vm.newRequisitionAttributes = [];
                ObjectTypeAttributeService.getObjectTypeAttributesByType("CUSTOM_REQUISITION").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newRequisition.id,
                                    attributeDef: attribute.id
                                },
                                attributeDef: attribute,
                                listValue: null,
                                newListValue: null,
                                timeValue: null,
                                timestampValue: moment(new Date()).format("DD/MM/YYYY, HH:mm:ss"),
                                listValueEditMode: false,
                                booleanValue: false,
                                dateValue: null,
                                imageValue: null,
                                refValue: null,
                                ref: null,
                                attachmentValues: []
                            };
                            if (attribute.lov != null) {
                                att.lovValues = attribute.lov.values;
                            }
                            if (attribute.required == false) {
                                vm.attributes.push(att);
                            } else {
                                vm.requiredAttributes.push(att);
                            }

                            vm.newRequisitionAttributes.push(att);
                        });
                    }, function (error) {

                    });
            }

            function checkAttribute(attribute) {
                if ((attribute.stringValue != null && attribute.stringValue != undefined && attribute.stringValue != "") ||
                    (attribute.integerValue != null && attribute.integerValue != undefined && attribute.integerValue != "") ||
                    (attribute.doubleValue != null && attribute.doubleValue != undefined && attribute.doubleValue != "") ||
                    (attribute.dateValue != null && attribute.dateValue != undefined && attribute.dateValue != "") ||
                    (attribute.imageValue != null && attribute.imageValue != undefined && attribute.imageValue != "") ||
                    (attribute.currencyValue != null && attribute.currencyValue != undefined && attribute.currencyValue != "") ||
                    (attribute.timeValue != null && attribute.timeValue != undefined && attribute.timeValue != "") ||
                    (attribute.attachmentValues.length != 0) ||
                    (attribute.refValue != null && attribute.refValue != undefined && attribute.refValue != "") ||
                    (attribute.listValue != null && attribute.listValue != undefined && attribute.listValue != "")) {
                    return true;
                } else {
                    return false;
                }
            }

            function intializationForAttributesSave() {
                var defered = $q.defer();
                var attrCount = 0;
                vm.propertyImageAttributes = [];
                vm.propertyImages = new Hashtable();
                vm.imageAttributes = [];
                vm.images = new Hashtable();
                vm.requiredAttributes = [];

                if (vm.newRequisitionAttributes.length == 0) {
                    defered.resolve();
                }
                else {
                    angular.forEach(vm.newRequisitionAttributes, function (attribute) {
                        attribute.id.objectId = vm.newRequisition.id;
                        if (attribute.attributeDef.dataType == "IMAGE" && attribute.imageValue != null) {
                            vm.propertyImages.put(attribute.attributeDef.id, attribute.imageValue);
                            vm.propertyImageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length > 0) {
                            addAttachment(attribute).then(
                                function (data) {
                                    attribute.attachmentValues = vm.propertyAttachmentIds;
                                    attrCount++;
                                    if (attrCount == vm.newRequisitionAttributes.length) {
                                        saveObjectAttributes().then(
                                            function (data) {
                                                vm.newRequisitionAttributes = [];
                                                loadObjectAttributeDefs();
                                                defered.resolve();
                                            }, function (error) {
                                                defered.reject();
                                            }
                                        )
                                    }
                                });
                        } else {
                            attrCount++;
                            if (attrCount == vm.newRequisitionAttributes.length) {
                                saveObjectAttributes().then(
                                    function (data) {
                                        vm.newRequisitionAttributes = [];
                                        loadObjectAttributeDefs();
                                        defered.resolve();
                                    }, function (error) {
                                        defered.reject();
                                    }
                                )
                            }
                        }
                    });
                }
                return defered.promise;
            }

            /* ............. attributes methods end block ............. */

            (function () {
                loadProjects();
                loadStore();
                loadObjectAttributeDefs();
                resize();
            })();
        }
    }
)
;