define(['app/desktop/modules/im/im.module',
        'app/shared/services/core/storeService',
        'app/shared/services/store/topStoreService',
        'app/shared/services/pm/project/projectService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService'
    ],
    function (module) {
        module.controller('NewStoreDialogController', NewStoreDialogController);

        function NewStoreDialogController($scope, $rootScope, $timeout, $state, $stateParams, $q, $sce, StoreService,
                                          TopStoreService, ObjectTypeAttributeService, AttributeAttachmentService, ObjectAttributeService) {
            var vm = this;

            vm.valid = true;
            vm.error = "";
            vm.hasError = false;
            var personDetails = window.$application.login;

            vm.topStore = {
                id: null,
                storeName: null,
                description: null,
                locationName: null,
                createdBy: personDetails.person.id
            };

            var stores = [];
            vm.attributes = [];
            vm.requiredAttributes = [];

            vm.create = create;
            vm.downloadFileFormat = downloadFileFormat;
            vm.storeType = $scope.data.storeType;

            $scope.trustAsHtml = function (value) {
                return $sce.trustAsHtml(value);
            };

            function validateStore() {
                vm.valid = true;

                if (vm.topStore.storeName == null || vm.topStore.storeName == undefined || vm.topStore.storeName == "") {
                    vm.valid = false;
                    $rootScope.showWarningMessage("Store Name cannot be empty");
                } else if (vm.topStore.locationName == null || vm.topStore.locationName == undefined || vm.topStore.locationName == "") {
                    vm.valid = false;
                    $rootScope.showWarningMessage("Store Location cannot be empty");
                }

                return vm.valid;
            }

            function create() {
                if (validateStore()) {
                    attributesValidate().then(
                        function (success) {
                            $rootScope.showBusyIndicator();
                            TopStoreService.createTopStore(vm.topStore).then(
                                function (data) {
                                    vm.topStore = data;
                                    $rootScope.storeId = vm.topStore.id;
                                    $rootScope.storeName = vm.topStore.storeName;
                                    vm.topStore = {
                                        id: null,
                                        storeName: null,
                                        description: null,
                                        locationName: null,
                                        createdBy: personDetails.person.id
                                    };
                                    var file = document.getElementById("file").valueOf();

                                    if (file.files[0] != null && file.files[0] != undefined) {
                                        TopStoreService.importStoreItems($rootScope.storeId, file.files[0]).then(
                                            function (data) {
                                                intializationForAttributesSave();
                                                $rootScope.hideBusyIndicator();
                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
                                                TopStoreService.deleteTopStore($rootScope.storeId).then(
                                                    function (data) {
                                                        vm.topStore = {
                                                            id: null,
                                                            storeName: null,
                                                            description: null,
                                                            locationName: null,
                                                            createdBy: personDetails.person.id
                                                        };
                                                        $rootScope.hideBusyIndicator();
                                                    }, function (error) {
                                                        vm.topStore = {
                                                            id: null,
                                                            storeName: null,
                                                            description: null,
                                                            locationName: null,
                                                            createdBy: personDetails.person.id
                                                        };
                                                    }
                                                )
                                            }
                                        )
                                    } else {
                                        intializationForAttributesSave();
                                        $rootScope.hideBusyIndicator();
                                    }
                                }, function (error) {
                                    $rootScope.hideBusyIndicator();
                                    $rootScope.showErrorMessage(error.message);
                                }
                            );
                        }
                    )
                }
            }

            /* ............. start attributes methods block ............. */

            function addAttachment(attribute) {
                var defered = $q.defer();
                vm.propertyAttachmentIds = [];
                var counter = 0;
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'STORE', attachmentFile).then(
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
                var countloop = 0;
                if (vm.newStoreAttributes.length > 0) {
                    angular.forEach(vm.newStoreAttributes, function (att) {
                        if (att.dateValue == "") {
                            att.dateValue = null;
                        }
                        countloop++;
                        if (countloop == vm.newStoreAttributes.length) {
                            ObjectAttributeService.saveItemObjectAttributes(vm.topStore.id, vm.newStoreAttributes).then(
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
                        }
                    });

                } else {
                    defered.resolve();
                }
                $state.go('app.store.details', {storeId: $rootScope.storeId});
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
                if (vm.newStoreAttributes != null && vm.newStoreAttributes != undefined) {
                    vm.objectAttributes = vm.objectAttributes.concat(vm.newStoreAttributes);
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
                vm.newStoreAttributes = [];
                ObjectTypeAttributeService.getObjectTypeAttributesByType("STORE").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.topStore.id,
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

                            vm.newStoreAttributes.push(att);
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
                var attrCount = 0;
                vm.propertyImageAttributes = [];
                vm.propertyImages = new Hashtable();
                vm.imageAttributes = [];
                vm.images = new Hashtable();
                vm.requiredAttributes = [];

                if (vm.newStoreAttributes.length == 0) {
                    $rootScope.hideBusyIndicator();
                    /*$rootScope.storeId = vm.topStore.id;*/
                    $state.go('app.store.details', {storeId: $rootScope.storeId});
                    $rootScope.showSuccessMessage("Store (" + $rootScope.storeName + ") created successfully");
                }
                else {
                    angular.forEach(vm.newStoreAttributes, function (attribute) {
                        attribute.id.objectId = vm.topStore.id;
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
                                    if (attrCount == vm.newStoreAttributes.length) {
                                        saveObjectAttributes().then(
                                            function (data) {
                                                vm.newStoreAttributes = [];
                                                loadObjectAttributeDefs();
                                                $rootScope.hideBusyIndicator();
                                                $rootScope.showSuccessMessage("Store (" + $rootScope.storeName + ") created successfully");
                                            }
                                        )
                                    }
                                });
                        } else {
                            attrCount++;
                            if (attrCount == vm.newStoreAttributes.length) {
                                saveObjectAttributes().then(
                                    function (data) {
                                        vm.newStoreAttributes = [];
                                        loadObjectAttributeDefs();
                                        $rootScope.hideBusyIndicator();
                                        $rootScope.showSuccessMessage("Store (" + $rootScope.storeName + ") created successfully");
                                    }
                                )
                            }
                        }
                    });
                }
            }

            /* ............. attributes methods end block ............. */

            var inputParamHeaders = ["Project Name", "ItemNumber", "Item Name", "Item Type", "Description", "Units", "Opening Quantity"];

            function downloadFileFormat() {
                var exportRows = [];

                var exportObject = {
                    "exportRows": exportRows,
                    "fileName": "StoreImportFileHeaders",
                    "headers": angular.copy(inputParamHeaders)
                };

                TopStoreService.downloadStoreImportFileFormat("EXCEL", exportObject).then(
                    function (data) {
                        var url = "{0}//{1}//api/is/stores/".format(window.location.protocol, window.location.host);
                        url += "/file/" + data + "/download";
                        window.open(url, '_self');
                    }
                )
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadObjectAttributeDefs();
                    $scope.$on('app.stores.new', function () {
                        create();

                    });
                }
            })();
        }
    }
)
;