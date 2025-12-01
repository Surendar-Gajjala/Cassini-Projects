/**
 * Created by swapna on 31/07/18.
 */
define(
    [
        'app/desktop/modules/stores/store.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/shared/services/store/scrapService',
        'app/shared/services/common/commonService',
        'app/shared/services/pm/project/projectService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/shared/services/store/topStoreService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController'
    ],
    function (module) {
        module.controller('NewScrapRequestController', NewScrapRequestController);

        function NewScrapRequestController($scope, $rootScope, $timeout, $state, $stateParams, ScrapService, AutonumberService,
                                           ProjectService, $q, CommonService, TopStoreService, ObjectTypeAttributeService, ObjectAttributeService, AttributeAttachmentService) {

            var vm = this;
            vm.addItems = addItems;
            vm.scrapItems = [];
            var scrapItemsMap = new Hashtable();
            vm.remove = remove;
            vm.back = back;

            vm.newScrap = {
                scrapNumber: null,
                requestedBy: null,
                notes: null,
                project: null,
                store: null,
                items: []
            };

            vm.create = create;
            vm.scraps = [];
            vm.autoNumber = autoNumber;
            vm.project = null;
            vm.requiredAttributes = [];
            vm.attributes = [];

            function addItems() {
                if (validate()) {
                    var options = {
                        title: 'Scrap Items',
                        showMask: true,
                        template: 'app/desktop/modules/stores/scrapRequest/new/newScrapItemsView.jsp',
                        controller: 'NewScrapItemsController as scrapItemsVm',
                        resolve: 'app/desktop/modules/stores/scrapRequest/new/newScrapItemsController',
                        width: 700,
                        data: {
                            newScrap: vm.newScrap,
                            scrapItems: vm.scrapItems,
                            addedItemsMap: scrapItemsMap
                        },
                        buttons: [],
                        callback: function (scrapItem) {
                            var item = scrapItemsMap.get(scrapItem.id);
                            if (item == null) {
                                scrapItemsMap.put(scrapItem.id, scrapItem);
                                vm.scrapItems.push(scrapItem);
                            }
                        }
                    };

                    $rootScope.showSidePanel(options);
                }
            }

            function remove(item) {
                var index = vm.scrapItems.indexOf(item);
                vm.scrapItems.splice(index, 1);
                scrapItemsMap.remove(item.id);
            }

            function back() {
                window.history.back();
            }

            function autoNumber() {
                AutonumberService.getAutonumberName("Default Scrap Number Source").then(
                    function (data) {
                        AutonumberService.getNextNumber(data.id).then(
                            function (data) {
                                vm.newScrap.scrapNumber = data;
                            }
                        )
                    }
                )
            }

            function loadProjects() {
                ProjectService.getAllProjects().then(
                    function (data) {
                        vm.projects = data;
                    }
                );
            }

            function loadStores() {
                TopStoreService.getTopStore($rootScope.storeId).then(
                    function (data) {
                        vm.store = data;
                    }
                ).then(
                    TopStoreService.getTopStores().then(
                        function (stores) {
                            vm.stores = stores;
                        }
                    )
                )
            }

            function loadPersons() {
                CommonService.getAllPersons().then(
                    function (data) {
                        vm.persons = data;
                    }
                )

            }

            function validate() {
                vm.valid = true;

                if (vm.newScrap.scrapNumber == null || vm.newScrap.scrapNumber == undefined || vm.newScrap.scrapNumber == "") {
                    vm.valid = false;
                    $rootScope.showErrorMessage("Scrap Number cannot be empty");
                }

                else if (vm.newScrap.requestedBy == null || vm.newScrap.requestedBy == undefined || vm.newScrap.requestedBy == "") {
                    vm.valid = false;
                    $rootScope.showErrorMessage("Requested By cannot be empty");
                }
                else if (vm.newScrap.store == null || vm.newScrap.store == undefined || vm.newScrap.store == "") {
                    vm.valid = false;
                    $rootScope.showErrorMessage("Store cannot be empty");
                }
                return vm.valid;
            }

            function loadObjectAttributeDefs() {
                vm.newScrapRequestAttributes = [];
                ObjectTypeAttributeService.getObjectTypeAttributesByType("SCRAPREQUEST").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newScrap.id,
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

                            vm.newScrapRequestAttributes.push(att);
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
            };

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
                if (vm.newScrapRequestAttributes != null && vm.newScrapRequestAttributes != undefined) {
                    vm.objectAttributes = vm.objectAttributes.concat(vm.newScrapRequestAttributes);
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

            function addAttachment(attribute) {
                var defered = $q.defer();
                vm.propertyAttachmentIds = [];
                var counter = 0;
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'SCRAPREQUEST', attachmentFile).then(
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

            function intializationForAttributesSave() {
                var attrCount = 0;
                var defered = $q.defer();
                vm.propertyImageAttributes = [];
                vm.propertyImages = new Hashtable();
                vm.imageAttributes = [];
                vm.images = new Hashtable();
                vm.requiredAttributes = [];

                if (vm.newScrapRequestAttributes.length == 0) {
                    defered.resolve();
                }
                else {
                    angular.forEach(vm.newScrapRequestAttributes, function (attribute) {
                        attribute.id.objectId = vm.newScrap.id;
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
                                    if (attrCount == vm.newScrapRequestAttributes.length) {
                                        saveObjectAttributes().then(
                                            function (data) {
                                                vm.newScrapRequestAttributes = [];
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
                            if (attrCount == vm.newScrapRequestAttributes.length) {
                                saveObjectAttributes().then(
                                    function (data) {
                                        vm.newScrapRequestAttributes = [];
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

            function saveObjectAttributes() {
                var defered = $q.defer();
                if (vm.newScrapRequestAttributes.length > 0) {
                    angular.forEach(vm.newScrapRequestAttributes, function (att) {
                        if (att.dateValue == "") {
                            att.dateValue = null;
                        }
                    });
                    ObjectAttributeService.saveItemObjectAttributes(vm.newScrap.id, vm.newScrapRequestAttributes).then(
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

            vm.emptyScrapItem = {
                materialItem: null,
                notes: null,
                quantity: null
            };

            function validateScrap() {
                var valid = false;
                var counter = 0;
                var scrItems = [];
                angular.forEach(vm.scrapItems, function (scrapItem) {
                    if (scrapItem.quantity > 0) {
                        var copiedScrapItem = angular.copy(vm.emptyScrapItem);
                        copiedScrapItem.item = scrapItem.id;
                        copiedScrapItem.notes = scrapItem.notes;
                        copiedScrapItem.quantity = scrapItem.quantity;
                        copiedScrapItem.recordedBy = window.$application.login.person.id;
                        copiedScrapItem.movementType = "RECEIVED";
                        copiedScrapItem.receivedBy = window.$application.login.person.id;
                        scrItems.push(copiedScrapItem);
                        counter++;
                        if (counter == vm.scrapItems.length) {
                            valid = true;
                            vm.newScrap.items = scrItems;
                        }
                    } else {
                        valid = false;
                        $rootScope.showErrorMessage("Please enter qty to item's");
                    }
                });

                return valid;
            }

            function create() {
                if (validateScrap()) {
                    attributesValidate().then(
                        function (success) {
                            vm.newScrap.requestedBy = vm.newScrap.requestedBy.id;
                            if (vm.newScrap.project != null) {
                                vm.newScrap.project = vm.newScrap.project.id;
                            }
                            vm.newScrap.store = vm.newScrap.store.id;
                            ScrapService.create(vm.newScrap).then(
                                function (data) {
                                    vm.newScrap = data;
                                    intializationForAttributesSave().then(
                                        function (success) {
                                            $rootScope.scrapDetailsId = vm.newScrap.id;
                                            $rootScope.hideSidePanel();
                                            $rootScope.showSuccessMessage("Scrap Request (" + vm.newScrap.scrapNumber + ") created successfully");
                                            $state.go('app.store.scrapDetails', {scrapDetailsId: vm.newScrap.id});
                                        }, function (error) {

                                        }
                                    )
                                }, function (error) {

                                }
                            )
                        }, function (error) {

                        });
                }
            }

            $scope.$on('app.store.scrapRequest', function () {
                create();
            });

            (function () {
                if ($application.homeLoaded == true) {
                    loadProjects();
                    loadPersons();
                    loadStores();
                    loadObjectAttributeDefs();
                }
            })();
        }
    }
);