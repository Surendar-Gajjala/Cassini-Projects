define(['app/app.modules',
        'app/components/store/storeFactory', /*,
         'app/components/store/storeFactory',
         'app/shared/services/pm/project/projectService',
         'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
         'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService'*/
    ],
    function (module) {
        module.controller('NewStoreDialogController', NewStoreDialogController);

        function NewStoreDialogController($scope, $rootScope, $timeout, $state, $stateParams,
                                          $cookies, $sce, StoreFactory, storeType, $modalInstance) {
            var vm = this;

            $scope.valid = true;
            $scope.error = "";
            $scope.hasError = false;
            var personDetails = null;
            $scope.store = {
                id: null,
                name: null,
                description: null
            };

            $scope.store = {
                id: null,
                storeName: null,
                description: null,
                locationName: null
            };

            var stores = [];
            var storesMap = new Hashtable();
            var topStoresMap = new Hashtable();

            $scope.create = create;
            $scope.storeType = storeType;

            $scope.trustAsHtml = function (value) {
                return $sce.trustAsHtml(value);
            };

            function validateStore() {
                $scope.valid = true;

                if ($scope.store.name == null || $scope.store.name == undefined || $scope.store.name == "") {
                    $scope.valid = false;
                    $rootScope.showWarningMessage("Store name cannot be empty");
                } /*else if ($scope.store.description == null || $scope.store.description == undefined || $scope.store.description == "") {
                 $scope.valid = false;
                 $rootScope.showWarningMessage("Store description cannot be empty");
                 } */
                else if (storesMap.get($scope.store.name) != null) {
                    $scope.valid = false;
                    $rootScope.showWarningMessage("{0} name already exists".format($scope.store.name));
                }

                return $scope.valid;
            }

            function create() {
                if (validateStore() == true) {
                    StoreFactory.createStore($stateParams.projectId, $scope.store).then(
                        function (data) {
                            $rootScope.hideSidePanel('left');
                            $scope.callback(data);
                            $scope.creating = false;
                            $rootScope.showSuccessMessage($scope.store.name + " : Store created successfully!");
                            $scope.store = {
                                name: null,
                                description: null,
                                project: $stateParams.projectId
                            };
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            function loadStores() {
                if ($stateParams.projectId != null) {
                    StoreFactory.getAllStores($stateParams.projectId).then(
                        function (data) {
                            stores = data;
                            angular.forEach(data, function (store) {
                                storesMap.put(store.name, store);
                            })
                        }
                    )
                }
            }

            function createStore() {
                if (validate()) {
                    $scope.validattributes = [];
                    angular.forEach($scope.storeRequiredAttributes, function (attribute) {
                        if (attribute.attributeDef.required == true && attribute.attributeDef.dataType != 'BOOLEAN' &&
                            attribute.attributeDef.dataType != 'TIMESTAMP') {
                            if (checkAttribute(attribute)) {
                                $scope.validattributes.push(attribute);
                            }
                            else {
                                $rootScope.showErrorMessage(attribute.attributeDef.name + ": Attribute is required");
                            }
                        } else {
                            $scope.validattributes.push(attribute);
                        }
                    });
                    if ($scope.storeRequiredAttributes.length == $scope.validattributes.length) {
                        $rootScope.showBusyIndicator($('#rightSidePanel'));
                        StoreFactory.getStoreByName($scope.topStore.storeName).then(
                            function (data) {
                                if (data != null && data != "" && data != undefined) {
                                    $rootScope.showWarningMessage($scope.topStore.storeName + " : Store name already exists!");
                                } else {
                                    StoreFactory.createTopStore($scope.topStore).then(
                                        function (data) {
                                            $scope.topStore = data;
                                            saveAttributes();

                                        }
                                    )
                                }
                            }
                        )
                    }
                }
            }

            function saveAttributes() {
                $scope.imageAttributes = [];
                var images = new Hashtable();
                angular.forEach($scope.storeRequiredAttributes, function (reqatt) {
                    $scope.storeAttributes.push(reqatt);
                });
                if ($scope.storeAttributes.length > 0) {
                    $scope.propertyImageAttributes = [];
                    var propertyImages = new Hashtable();
                    angular.forEach($scope.storeAttributes, function (attribute) {
                        attribute.id.objectId = $scope.topStore.id;
                        if (attribute.timeValue != null) {
                            attribute.timeValue = moment(attribute.timeValue).format("HH:mm:ss");

                        }
                        if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                            propertyImages.put(attribute.id.attributeDef, attribute.imageValue);
                            $scope.propertyImageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length > 0) {
                            attribute.attachmentValues = addStoreAttachment(attribute);
                        }
                    });
                    $timeout(function () {
                        ObjectAttributeService.saveItemObjectAttributes($scope.topStore.id, $scope.storeAttributes).then(
                            function (data) {
                                if ($scope.propertyImageAttributes.length > 0) {
                                    angular.forEach($scope.propertyImageAttributes, function (propImgAtt) {
                                        ObjectAttributeService.uploadObjectAttributeImage(propImgAtt.id.objectId, propImgAtt.id.attributeDef, propertyImages.get(propImgAtt.id.attributeDef)).then(
                                            function (data) {

                                            }
                                        )
                                    })
                                }
                                $rootScope.showSuccessMessage($scope.topStore.storeName + " : Store created successfully.");
                                $scope.callback();
                                $rootScope.hideSidePanel();
                                $rootScope.hideBusyIndicator();
                                $scope.topStore = {
                                    storeName: null,
                                    description: null,
                                    locationName: null,
                                    createdBy: personDetails.person.id
                                };
                            },
                            function (error) {
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }, 3000);
                } else {
                    $rootScope.showSuccessMessage($scope.topStore.storeName + " : Store created successfully.");
                    $scope.callback();
                    $rootScope.hideSidePanel();
                    $rootScope.hideBusyIndicator();
                    $scope.topStore = {
                        storeName: null,
                        description: null,
                        locationName: null,
                        createdBy: personDetails.person.id
                    };
                }
            }

            function addStoreAttachment(attribute) {
                var propertyAttachmentIds = [];
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'PROJECT', attachmentFile).then(
                        function (data) {
                            propertyAttachmentIds.push(data[0].id);
                        }
                    )
                })
                return propertyAttachmentIds;
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
            };

            function validate() {
                $scope.valid = true;

                if ($scope.topStore.storeName == null || $scope.topStore.storeName == undefined || $scope.topStore.storeName == "") {
                    $scope.valid = false;
                    $rootScope.showWarningMessage("Store name cannot be empty");
                } else if ($scope.topStore.locationName == null || $scope.topStore.locationName == undefined || $scope.topStore.locationName == "") {
                    $scope.valid = false;
                    $rootScope.showWarningMessage("Store location cannot be empty");
                }
                /*else if ($scope.topStore.description == null || $scope.topStore.description == undefined || $scope.topStore.description == "") {
                 $scope.valid = false;
                 $rootScope.showWarningMessage("Store description cannot be empty");
                 }*/
                return $scope.valid;
            }

            $scope.storeAttributes = [];
            $scope.storeRequiredAttributes = [];
            function loadObjectAttributeDefs() {
                ProjectService.getAllProjectAttributes("STORE").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: $scope.topStore.id,
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
                                $scope.storeAttributes.push(att);
                            } else {
                                $scope.storeRequiredAttributes.push(att);
                            }
                        });
                    }
                )
            }

            $scope.ok = function () {
                StoreFactory.createStore($scope.store).then(
                    function (data) {
                        $modalInstance.close(data);
                    }
                )
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

            (function () {
                $rootScope.$broadcast('app.activate.procurement', {project: {name: 'Procurement'}});
                loadStores();

            })();
        }
    }
)
;