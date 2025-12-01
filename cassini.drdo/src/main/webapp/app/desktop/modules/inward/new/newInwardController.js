define(
    [
        'app/desktop/modules/inward/inward.module',
        'moment',
        'moment-timezone-with-data',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/inwardService',
        'app/shared/services/core/procurementService',
        'app/shared/services/core/bomService'
    ],
    function (module) {

        module.controller('NewInwardController', NewInwardController);

        function NewInwardController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies,
                                     AutonumberService, ObjectAttributeService, AttachmentService, AttributeAttachmentService,
                                     ItemTypeService, InwardService, ProcurementService, BomService) {
            if ($application.homeLoaded == false) {
                return;
            }

            var vm = this;

            vm.newInwardAttributes = [];

            vm.gatePass = null;
            vm.newInward = {
                id: null,
                gatePass: null,
                gatePassNumber: null,
                notes: null,
                bom: null,
                supplier: null
            };

            vm.supplierTitle = "Select Supplier";
            vm.bomTitle = "Select BOM";

            function create() {
                $rootScope.closeNotification();
                if (validate() && validateAttributes()) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    InwardService.createInward(vm.newInward).then(
                        function (data) {
                            vm.newInward.id = data.id;
                            vm.newInward.number = data.number;
                            saveObjectAttributes();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function validate() {
                var valid = true;

                if (vm.newInward.gatePass == null || vm.newInward.gatePass == "" || vm.newInward.gatePass == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please select GatePass");
                } else if (vm.newInward.bom == null || vm.newInward.bom == "" || vm.newInward.bom == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please select BOM");
                } else if (vm.newInward.supplier == null || vm.newInward.supplier == "" || vm.newInward.supplier == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please select Supplier");
                }

                return valid;
            }


            function validateAttributes() {
                var valid = true;
                angular.forEach(vm.newInwardAttributes, function (attribute) {
                    if (valid) {
                        if (attribute.attributeDef.required == true && attribute.attributeDef.dataType != 'BOOLEAN' &&
                            attribute.attributeDef.dataType != 'TIMESTAMP') {
                            if ($rootScope.checkAttribute(attribute)) {
                                if (attribute.attributeDef.dataType == "ATTACHMENT") {
                                    angular.forEach(attribute.attachmentValues, function (attachment) {
                                        var fileExtension = attachment.name.substring(attachment.name.lastIndexOf(".") + 1);

                                        if (fileExtension.toLowerCase() != "pdf") {
                                            valid = false;
                                            $rootScope.showErrorMessage("Please upload pdf format certificate only");
                                        }
                                    })
                                }
                            }
                            else {
                                valid = false;
                                if (attribute.attributeDef.dataType == "ATTACHMENT" || attribute.attributeDef.dataType == "LIST") {
                                    $rootScope.showErrorMessage("Please select " + attribute.attributeDef.name);
                                } else {
                                    $rootScope.showErrorMessage("Please enter " + attribute.attributeDef.name);
                                }
                                $rootScope.hideBusyIndicator();
                            }
                        }
                    }
                });

                return valid;
            }

            function saveObjectAttributes() {
                var defered = $q.defer();
                if (vm.newInwardAttributes.length > 0) {
                    vm.propertyImageAttributes = [];
                    var propertyImages = new Hashtable();
                    angular.forEach(vm.newInwardAttributes, function (attribute) {
                        attribute.id.objectId = vm.newInward.id;
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
                        ObjectAttributeService.saveItemObjectAttributes(vm.newInward.id, vm.newInwardAttributes).then(
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
                                vm.newInward = {
                                    id: null,
                                    gatePass: null,
                                    gatePassNumber: null,
                                    notes: null
                                };
                                vm.newInwardAttributes = [];
                                $rootScope.hideSidePanel();
                                $rootScope.hideBusyIndicator();
                                $scope.callback();
                                $rootScope.showSuccessMessage("Inward created successfully");
                                defered.resolve();
                            },
                            function (error) {
                                $rootScope.hideBusyIndicator();
                                defered.reject();
                            }
                        )
                    }, 2000);
                } else {
                    defered.resolve();
                    vm.newInward = {
                        id: null,
                        gatePass: null,
                        notes: null
                    };
                    vm.newInwardAttributes = [];
                    $rootScope.hideSidePanel();
                    $rootScope.hideBusyIndicator();
                    $scope.callback();
                    $rootScope.showSuccessMessage("Inward created successfully");
                }

                return defered.promise;
            }

            function addItemPropertyAttachment(attribute) {
                var propertyAttachmentIds = [];
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'INWARD', attachmentFile).then(
                        function (data) {
                            propertyAttachmentIds.push(data[0].id);
                        }
                    )
                })
                return propertyAttachmentIds;
            }

            function loadInwardAttributes() {
                ItemTypeService.getAttributesByObjectType("INWARD").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newInward.id,
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
                            vm.newInwardAttributes.push(att);
                        });
                    }
                )
            }

            vm.gatePassFilter = {
                searchQuery: null
            };

            vm.performSearch = performSearch;
            vm.preventClick = preventClick;
            vm.downloadFile = downloadFile;

            function performSearch() {

                if (vm.gatePassFilter.searchQuery == "") {
                    $('#gatePassSearchResults').hide();
                }
                if (vm.gatePassFilter.searchQuery != "") {
                    $('#gatePassSearchResults').show();
                    InwardService.searchGatePass(vm.gatePassFilter).then(
                        function (data) {
                            vm.searchResults = data;
                        }
                    )
                }
            }

            function initSearchBox() {
                $(document).click(function () {
                    $('#gatePassSearchResults').hide();
                });
                $(document).on('keydown', function (evt) {
                    if (evt.keyCode == 27) {
                        $('#gatePassSearchResults').hide();
                    }
                });

                $timeout(function () {
                    $('#gatePassSearchResults').click(function (event) {
                        event.stopPropagation();
                        event.preventDefault();
                    });
                }, 1000);
            }

            vm.addGatePass = addGatePass;
            function addGatePass(gatePass) {
                vm.newInward.gatePass = gatePass;
                vm.gatePassFilter.searchQuery = '';
                $('#gatePassSearchResults').hide();
                if (vm.boms.length == 1) {
                    vm.newInward.bom = vm.boms[0];
                }
            }

            function preventClick(event) {
                event.stopPropagation();
                event.preventDefault();
            }

            function downloadFile(file) {
                var url = "{0}//{1}/api/drdo/inwards/gatePass/{2}/{3}/preview".
                    format(window.location.protocol, window.location.host,
                    vm.newInward.gatePass.id, file.id);
                var newWindow = window.open(url, "_blank");
                newWindow.addEventListener('load', function () {
                    newWindow.document.title = file.name;
                });
                /*window.open(url);
                 $timeout(function () {
                 window.close();
                 }, 2000);*/
            }

            function loadSuppliers() {
                ProcurementService.getSuppliers().then(
                    function (data) {
                        vm.suppliers = data;
                        if (vm.suppliers.length == 0) {
                            vm.supplierTitle = "No Suppliers";
                        }
                    }
                )
            }

            function loadBoms() {
                BomService.getAllBoms().then(
                    function (data) {
                        vm.boms = data;
                        if (vm.boms.length == 0) {
                            vm.bomTitle = "No BOMs";
                        }
                        loadSuppliers();
                    }
                )
            }


            (function () {
                if ($application.homeLoaded == true) {
                    loadBoms();
                    loadInwardAttributes();
                    initSearchBox();
                    $rootScope.$on('app.inwards.new', create);
                }
            })();
        }
    }
)
;