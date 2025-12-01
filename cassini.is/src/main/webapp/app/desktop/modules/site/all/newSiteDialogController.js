define(['app/desktop/modules/site/sites.module',
        'app/shared/services/pm/project/projectSiteService',
        'app/shared/services/core/storeService',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController'
    ],
    function (module) {
        module.controller('NewSiteDialogController', NewSiteDialogController);

        function NewSiteDialogController($scope, $rootScope, $q, $timeout, $state, $stateParams, $cookies, $sce, ProjectSiteService,
                                         StoreService, ItemService, ObjectAttributeService, ObjectTypeAttributeService, AttributeAttachmentService) {
            var vm = this;

            vm.valid = true;
            vm.error = "";
            vm.hasError = false;
            vm.stores = [];

            vm.requiredAttributes = [];
            vm.attributes = [];

            vm.site = {
                name: null,
                description: "",
                project: $stateParams.projectId
            };
            var sites = $scope.data.siteList;
            var sitesMap = new Hashtable();

            vm.create = create;

            $scope.trustAsHtml = function (value) {
                return $sce.trustAsHtml(value);
            };

            function validateSite() {
                vm.valid = true;

                if (vm.site.name === null || vm.site.name == undefined || vm.site.name == "") {
                    vm.valid = false;
                    $rootScope.showWarningMessage("Site Name cannot be empty");
                }
                else if (sitesMap.get(vm.site.name) != null) {
                    vm.valid = false;
                    $rootScope.showErrorMessage("{0} Name already exists".format(vm.site.name));
                }
                return vm.valid;
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

            function addAttachment(attribute) {
                var defered = $q.defer();
                vm.propertyAttachmentIds = [];
                var counter = 0;
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'SITE', attachmentFile).then(
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
                if (vm.newSiteAttributes.length > 0) {
                    angular.forEach(vm.newSiteAttributes, function (att) {
                        if (att.dateValue == "") {
                            att.dateValue = null;
                        }
                    });
                    ObjectAttributeService.saveItemObjectAttributes(vm.site.id, vm.newSiteAttributes).then(
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
                if (vm.newSiteAttributes != null && vm.newSiteAttributes != undefined) {
                    vm.objectAttributes = vm.objectAttributes.concat(vm.newSiteAttributes);
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

            function intializationForAttributesSave() {
                var defered = $q.defer();
                var attrCount = 0;
                vm.propertyImageAttributes = [];
                vm.propertyImages = new Hashtable();
                vm.imageAttributes = [];
                vm.images = new Hashtable();
                vm.requiredAttributes = [];
                if (vm.newSiteAttributes.length == 0) {
                    defered.resolve();
                }
                else {
                    angular.forEach(vm.newSiteAttributes, function (attribute) {
                        attribute.id.objectId = vm.site.id;
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
                                    if (attrCount == vm.newSiteAttributes.length) {
                                        saveObjectAttributes().then(
                                            function (data) {
                                                vm.newSiteAttributes = [];
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
                            if (attrCount == vm.newSiteAttributes.length) {
                                saveObjectAttributes().then(
                                    function (data) {
                                        vm.newSiteAttributes = [];
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

            function create() {
                if (validateSite()) {
                    attributesValidate().then(
                        function (success) {
                            ProjectSiteService.createSite(vm.site).then(
                                function (data) {
                                    vm.site = data;
                                    sitesMap.put(vm.site.name, vm.site);
                                    intializationForAttributesSave().then(
                                        function (sucess) {
                                            $rootScope.hideSidePanel();
                                            $rootScope.hideBusyIndicator();
                                            $rootScope.showSuccessMessage("Site created successfully");
                                            $scope.callback();
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

            function loadSites() {
                angular.forEach($scope.data.siteList, function (site) {
                    sitesMap.put(site.name, site);
                })
            }

            function loadStores() {
                StoreService.getByProject(vm.site.project).then(
                    function (data) {
                        vm.stores = data;
                    }
                )
            }

            function loadObjectAttributeDefs() {
                vm.newSiteAttributes = [];
                ObjectTypeAttributeService.getObjectTypeAttributesByType("SITE").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.site.id,
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

                            vm.newSiteAttributes.push(att);
                        });
                    }, function (error) {

                    });
            }

            $scope.$on('app.site.new', function () {
                create();
            });

            (function () {
                if ($application.homeLoaded == true) {
                    loadSites();
                    loadStores();
                    loadObjectAttributeDefs();

                }
            })();
        }
    }
);