define(
    [
        'app/desktop/modules/mro/mro.module',
        'moment',
        'dropzone',
        'moment-timezone-with-data',
        'app/desktop/modules/directives/mesObjectTypeDirective',
        'app/shared/services/core/workRequestService',
        'app/shared/services/core/mesObjectTypeService',
        'app/shared/services/core/qualityTypeService',
        'app/shared/services/core/sparePartsService',
        'app/shared/services/core/assetService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/measurementService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/shared/services/core/classificationService'
    ],
    function (module) {

        module.controller('NewWorkRequestController', NewWorkRequestController);

        function NewWorkRequestController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce, CommonService, ClassificationService,
                                          AutonumberService, WorkRequestService, LoginService, AssetService, SparePartService, QualityTypeService) {

            var vm = this;


            var parsed = angular.element("<div></div>");

            var attributeRequired = parsed.html($translate.instant("ATTRIBUTE_REQUIRED")).html();
            var workRequestNumberValidation = parsed.html($translate.instant("NUMBER_CANNOT_BE_EMPTY")).html();
            var assetSelectionValidation = parsed.html($translate.instant("ASSET_CANNOT_BE_EMPTY")).html();
            var requestorSelectionValidation = parsed.html($translate.instant("REQUESTOR_CANNOT_BE_EMPTY")).html();
            var workRequestTypeValidation = parsed.html($translate.instant("WORK_REQUEST_TYPE_VALIDATION")).html();
            var workRequestNameValidation = parsed.html($translate.instant("NAME_CANNOT_BE_EMPTY")).html();
            var workRequestCreatedMsg = parsed.html($translate.instant("WORK_REQUEST_CREATED_MSG")).html();
            vm.selectAssetTitle = parsed.html($translate.instant("SELECT_ASSET")).html();
            $scope.select = parsed.html($translate.instant("SELECT")).html();


            vm.prioritys = ["LOW", "MEDIUM", "HIGH", "CRITICAL"];
            vm.workRequestStatus = ["PENDING", "FINISH"];

            vm.workRequest = {
                id: null,
                type: null,
                asset: null,
                number: null,
                requestor: null,
                description: null,
                notes: null,
                priority: 'LOW',
                status: 'PENDING'
            };

            vm.onSelectType = onSelectType;
            function onSelectType(objectType) {
                if (objectType != null && objectType != undefined) {
                    vm.workRequest.type = objectType;
                    vm.type = objectType;
                    autoNumber();
                }
            }

            function autoNumber() {
                if (vm.workRequest.type != null && vm.workRequest.type.autoNumberSource != null) {
                    var source = vm.workRequest.type.autoNumberSource;
                    AutonumberService.getNextNumberByName(source.name).then(
                        function (data) {
                            vm.workRequest.number = data;
                            loadAttributeDefs();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function create() {
                if (validate()) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    vm.imageAttributes = [];
                    vm.attachmentAttributes = [];
                    vm.images = new Hashtable();
                    vm.attachments = new Hashtable();
                    angular.forEach(vm.attributes, function (attribute) {
                        if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                            vm.images.put(attribute.id.attributeDef, attribute.imageValue);
                            vm.imageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length > 0) {
                            vm.attachments.put(attribute.id.attributeDef, attribute.attachmentValues);
                            vm.attachmentAttributes.push(attribute);
                            attribute.attachmentValues = [];
                        }
                    });
                    vm.workRequest.mroObjectAttributes = vm.attributes;
                    WorkRequestService.createWorkRequest(vm.workRequest).then(
                        function (data) {
                            vm.workRequest = data;
                            if (vm.workRequestFiles.length > 0) {
                                WorkRequestService.uploadWorkRequestFiles(vm.workRequest.id, vm.workRequestFiles).then(
                                    function (data) {
                                    }, function (error) {
                                        $rootScope.showErrorMessage(error.message);
                                        $rootScope.hideBusyIndicator();
                                    })
                            }
                            saveAttributes().then(
                                function (attributes) {
                                    $scope.callback(vm.workRequest);
                                    $rootScope.hideBusyIndicator();
                                    $rootScope.hideSidePanel();
                                    $rootScope.showSuccessMessage(workRequestCreatedMsg);
                                    vm.workRequest = {
                                        id: null,
                                        type: null,
                                        asset: null,
                                        number: null,
                                        requestor: null,
                                        description: null,
                                        notes: null,
                                        priority: 'LOW',
                                        status: 'OPEN'
                                    };
                                }
                            )
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function validate() {
                var valid = true;
                if (vm.workRequest.type == null || vm.workRequest.type == undefined ||
                    vm.workRequest.type == "") {
                    $rootScope.showErrorMessage(workRequestTypeValidation);
                    valid = false;
                } else if (vm.workRequest.number == null || vm.workRequest.number == undefined ||
                    vm.workRequest.number == "") {
                    $rootScope.showErrorMessage(workRequestNumberValidation);
                    valid = false;
                } else if (vm.workRequest.asset == null || vm.workRequest.asset == undefined ||
                    vm.workRequest.asset == "") {
                    $rootScope.showErrorMessage(assetSelectionValidation);
                    valid = false;
                } else if (vm.workRequest.name == null || vm.workRequest.name == undefined ||
                    vm.workRequest.name == "") {
                    $rootScope.showErrorMessage(workRequestNameValidation);
                    valid = false;
                } else if (vm.workRequest.requestor == null || vm.workRequest.requestor == undefined ||
                    vm.workRequest.requestor == "") {
                    $rootScope.showErrorMessage(requestorSelectionValidation);
                    valid = false;
                } else if (vm.attributes.length > 0 && !validateAttributes()) {
                    valid = false;
                } else if (!$rootScope.checkAttributeValidations(vm.attributes)) {
                    valid = false;
                }

                return valid;
            }


            function validateAttributes() {
                var valid = true;
                angular.forEach(vm.attributes, function (attribute) {
                    if (valid) {
                        if (attribute.attributeDef.required == true && attribute.attributeDef.dataType != 'BOOLEAN' &&
                            attribute.attributeDef.dataType != 'TIMESTAMP') {
                            if (!$rootScope.checkAttribute(attribute)) {
                                valid = false;
                                $rootScope.showWarningMessage(attribute.attributeDef.name + ":" + attributeRequired);
                            }
                        }
                    }
                });
                return valid;
            }

            function saveAttributes() {
                var defered = $q.defer();
                if (vm.imageAttributes.length > 0 || vm.attachmentAttributes.length > 0) {
                    angular.forEach(vm.imageAttributes, function (imgAtt) {
                            ClassificationService.updateAttributeImageValue("MROOBJECTTYPE", vm.workRequest.id, imgAtt.id.attributeDef, vm.images.get(imgAtt.id.attributeDef)).then(
                                function (data) {
                                    defered.resolve();
                                }
                                , function (error) {
                                    defered.resolve();
                                }
                            )
                        }
                    );
                    angular.forEach(vm.attachmentAttributes, function (imgAtt) {
                            ClassificationService.updateAttributeAttachmentValues("MROOBJECTTYPE", vm.workRequest.id, imgAtt.id.attributeDef, vm.attachments.get(imgAtt.id.attributeDef)).then(
                                function (data) {
                                    defered.resolve();
                                }
                                , function (error) {
                                    defered.resolve();
                                }
                            )
                        }
                    )
                } else {
                    defered.resolve();
                }
                return defered.promise;
            }

            function loadAttributeDefs() {
                vm.attributes = [];
                SparePartService.getObjectAttributesWithHierarchy(vm.workRequest.type.id).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.type.id,
                                    attributeDef: attribute.id

                                },
                                attributeDef: attribute,
                                listValue: null,
                                mlistValue: [],
                                newListValue: null,
                                listValueEditMode: false,
                                booleanValue: false,
                                refValue: null,
                                timestampValue: null,
                                ref: null,
                                imageValue: null,
                                attachmentValues: []
                            };
                            if (attribute.dataType == "TIMESTAMP") {
                                att.timestampValue = moment(new Date()).format("DD/MM/YYYY, HH:mm:ss");
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
                            if (attribute.lov != null) {
                                att.lovValues = attribute.lov.values;
                            }
                            if (attribute.validations != null && attribute.validations != "") {
                                attribute.newValidations = JSON.parse(attribute.validations);
                            }
                            vm.attributes.push(att);
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadPersons() {
                vm.persons = [];
                LoginService.getAllLogins().then(
                    function (data) {
                        angular.forEach(data, function (login) {
                            if (login.isActive == true && login.external == false) {
                                vm.persons.push(login.person);
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            function loadAssets() {
                AssetService.getAssets().then(
                    function (data) {
                        vm.assets = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            var dropZoneComponent = null;
            vm.showFilesDropZone = false;
            function initDropZone() {
                //Dropzone.options.url
                var previewNode = document.getElementById("fileUploadTemplate");
                previewNode.id = "";
                previewNode.style.display = "block";
                var previewTemplate = previewNode.parentNode.innerHTML;
                previewNode.parentNode.removeChild(previewNode);

                dropZoneComponent = new Dropzone(document.getElementById("workReqFiles"), {
                    url: "/target", // Set the url
                    thumbnailWidth: 50,
                    thumbnailHeight: 50,
                    parallelUploads: 20,
                    autoProcessQueue: false, // Make sure the files aren't queued until manually processed
                    previewTemplate: previewTemplate,
                    previewsContainer: "#fileUploadPreviews",
                    success: function (file, response) {
                    },
                    error: function (file, response) {
                    }
                });

                dropZoneComponent.on("queuecomplete", function (progress) {
                });

                dropZoneComponent.on("addedfiles", function (files) {
                    $(".drop-files-label").hide();
                    vm.workRequestFiles = dropZoneComponent.files;
                    $scope.$evalAsync();
                });
            }

            vm.selectWrFiles = selectWrFiles;
            function selectWrFiles() {
                $('#workReqFiles')[0].click();
            }

            vm.workRequestFiles = [];
            vm.loadDropZoneFiles = loadDropZoneFiles;
            function loadDropZoneFiles() {
                vm.showFilesDropZone = !vm.showFilesDropZone;
                if (vm.showFilesDropZone) {
                    $timeout(function () {
                        initDropZone();
                    }, 1000);
                }
                else {
                    if (dropZoneComponent != null) {
                        vm.workRequestFiles = [];
                        vm.showFilesDropZone = true;
                        dropZoneComponent.files = [];
                        /*dropZoneComponent.destroy();*/
                        /* $timeout(function () {
                         initDropZone();
                         }, 1000);*/
                    }
                }
            }

            vm.removeFiles = removeFiles;

            function removeFiles() {
                vm.workRequestFiles = [];
            }

            (function () {
                //if ($application.homeLoaded == true) {
                loadDropZoneFiles();
                loadPersons();
                loadAssets();
                $rootScope.$on('app.workRequest.new', create);
                //}
            })();
        }
    }
)
;