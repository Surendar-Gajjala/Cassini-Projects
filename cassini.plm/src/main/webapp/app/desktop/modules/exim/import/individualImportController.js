define(
    [
        'app/desktop/modules/exim/exim.module',
        'dropzone',
        'app/shared/services/core/itemService',
        'app/shared/services/core/eximService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/preferenceService'
    ],
    function (module) {
        module.controller('IndividualImportController', IndividualImportController);

        function IndividualImportController($scope, $rootScope, $timeout, $interval, $http, $state, $cookies, $translate,
                                            ItemService, ExImService, PreferenceService) {

            var vm = this;
            var file;
            $rootScope.viewInfo.icon = "fa fa-download";
            $rootScope.viewInfo.title = "Import";
            $rootScope.viewInfo.showDetails = false;
            var dropZoneComponent = null;
            vm.importFile = [];
            vm.showFilesDropZone = false;
            vm.headers = [];
            vm.headerObjs = [];
            vm.mapHeaders = [];
            vm.objs = [];
            vm.headers1 = [];
            vm.objectTypes = [{name: "Classification", label: "Classification"},
                {name: "Items", label: "Items"},
                {name: "BOM", label: "BOM"},
                {name: "Manufacturer and Manufacturer Parts", label: "Manufacturer and Manufacturer Parts"},
                {name: "Customers", label: "Customers"},
                {name: "Suppliers", label: "Suppliers"},
                {name: "Manufacturing Master Data", label: "Manufacturing Master Data"},
                {name: "MRO", label: "Maintenance & Repair"},
                {name: "Compliance", label: "Compliance"},
                {name: "Projects", label: "Projects"},
                {name: "Programs", label: "Programs"},
            ];

            vm.subTypes = [];
            vm.showDropZone = showDropZone;
            function showDropZone() {
                vm.showFilesDropZone = !vm.showFilesDropZone;
                if (vm.showFilesDropZone) {
                    $timeout(function () {
                        initDropZone();
                    }, 1000);
                }
                else {
                    if (dropZoneComponent != null) {
                        vm.importFile = [];
                        dropZoneComponent.destroy();
                    }
                }
            }

            vm.mappingPreference;
            function loadPreferenceMapping() {
                PreferenceService.getPreferenceByKey("SYSTEM.IMPORT_MAPPINGS").then(
                    function (data) {
                        vm.mappingPreference = data;
                        loadValues()
                    }
                );
            }

            function loadValues() {
                $http.get('app/assets/json/importColumnNames.json').success(function (data) {
                    var objectColumnNames = data;
                    if (vm.subType != null && vm.subType.name != undefined) vm.mapHeaders = objectColumnNames[vm.subType.name];
                    else vm.mapHeaders = objectColumnNames[vm.objectType.name];

                    if (vm.mapHeaders == undefined || vm.mapHeaders == null) {
                        vm.mapHeaders = vm.headers;
                    }
                    setMappingColumns();
                });
            }

            vm.closeDropZone = closeDropZone;
            function closeDropZone() {
                if (vm.objectType.name != null && vm.objectType.name != "") {
                    showDropZone();
                    vm.enableTemplateLink = true;
                }
                else {
                    vm.enableTemplateLink = false;
                }
            }

            vm.enableTemplateLink = false;
            vm.subTypeFlag = false;
            vm.objectType = null;
            vm.subType = null;
            vm.selectObject = selectObject;
            vm.downloadObjectTemplates = downloadObjectTemplates;
            function selectObject(type) {
                vm.enableTemplateLink = false;
                vm.subType = null;
                vm.subTypes = [];
                if (type.name == "Manufacturing Master Data") {
                    vm.subTypes = [{name: "Plants", label: "Plants"},
                        {name: "Assembly Lines", label: "Assembly Lines"},
                        {name: "Work Centers", label: "Work Centers"},
                        {name: "Machines", label: "Machines"},
                        {name: "Equipments", label: "Equipments"},
                        {name: "Instruments", label: "Instruments"},
                        {name: "Tools", label: "Tools"},
                        {name: "Jigs & Fixture", label: "Jigs & Fixture"},
                        {name: "Materials", label: "Materials"},
                        {name: "Manpowers", label: "Manpowers"},
                        {name: "Shifts", label: "Shifts"},
                        {name: "Operations", label: "Operations"}];
                    vm.subTypeFlag = true;
                    loadDropZone();
                }
                else if (type.name == "MRO") {  
                    vm.subTypes = [{name: "Assets", label: "Assets"},
                        {name: "Meters", label: "Meters"},
                        {name: "Spare Parts", label: "Spare Parts"}];
                    vm.subTypeFlag = true;
                    loadDropZone();
                }
                else if (type.name == "Compliance") {
                    vm.subTypes = [{name: "Substances", label: "Substances"},
                        {name: "Specifications", label: "Specifications"}];
                    vm.subTypeFlag = true;
                    loadDropZone();
                }
                else {
                    vm.enableTemplateLink = true;
                    vm.subTypeFlag = false;
                    loadDropZone();
                }
            }

            vm.selectObjectSubTypes = selectObjectSubTypes;
            function selectObjectSubTypes(subType) {
                vm.enableTemplateLink = true;
                loadDropZone();
            }


            function downloadObjectTemplates() {
                if (vm.objectType.name != null && vm.objectType.name != "") {
                    if (vm.subType != null && vm.subType != "") {
                        vm.objectType = vm.subType;
                    }
                    $rootScope.showBusyIndicator();
                    var url = "api/plm/exim/download/" + vm.objectType.name + "/excel";
                    window.open(url);
                    $rootScope.hideBusyIndicator();
                    vm.enableTemplateLink = true;

                }
            }


            vm.importFiles = [];
            vm.loadDropZone = loadDropZone;
        function loadDropZone() {
                vm.headerObjs = [];
                vm.mapHeaders = [];
                vm.objs = [];
                vm.headers1 = [];
                vm.headers = [];
                vm.showFilesDropZone = true;
                $timeout(function () {
                    initDropZone();
                }, 1000);

            }

            function initDropZone() {
                //Dropzone.options.url
                var previewNode = document.getElementById("fileUploadTemplate");
                previewNode.id = "";
                previewNode.style.display = "block";
                var previewTemplate = previewNode.parentNode.innerHTML;
                previewNode.parentNode.removeChild(previewNode);
                dropZoneComponent = new Dropzone(document.getElementById("import-file"), {
                    url: "api/plm/exim/" + vm.objectType.name + "/import/" + 0,
                    uploadMultiple: false,
                    maxFiles: 1,
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
                dropZoneComponent.on("maxfilesexceeded", function (file) {
                    dropZoneComponent.removeAllFiles();
                    dropZoneComponent.addFile(file);
                });

                dropZoneComponent.on("success", function (file, response) {

                });

                dropZoneComponent.on("error", function (file, response) {

                });

                dropZoneComponent.on("queuecomplete", function (progress) {
                    //dropZoneComponent.removeAllFiles(true);
                    $scope.$apply();
                });

                dropZoneComponent.on("addedfiles", function (files) {
                    $(".drop-files-label").hide();
                    // dropZoneComponent.removeAllFiles(true);
                    if (files.length > 0) {
                        vm.importFiles = files[0];
                        getTemplateHeaders(vm.importFiles);
                        $scope.$evalAsync();
                    }
                });
            }

            function getTemplateHeaders(file) {
                vm.headerObjs = [];
                vm.mapHeaders = [];
                vm.objs = [];
                vm.headers1 = [];
                vm.headers = [];
                this.file = file;
                $rootScope.showBusyIndicator("#rightSidePanel");
                ExImService.getHeadersFromFile(this.file).then(
                    function (data) {
                        vm.headers = data;
                        loadPreferenceMapping();
                        $rootScope.hideBusyIndicator();
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function setMappingColumns() {
                angular.copy(vm.headers, vm.headers1);
                angular.forEach(vm.mapHeaders, function (obj) {
                    var ob = {
                        header: obj,
                        mapHeader: ""
                    };
                    vm.headerObjs.push(ob);
                });
                if (vm.mappingPreference != null && vm.mappingPreference != undefined
                    && vm.mappingPreference.jsonValue != null && vm.mappingPreference.jsonValue != "") {
                    var map = JSON.parse(vm.mappingPreference.jsonValue);
                    var objs;
                    if (vm.subType != null && vm.subType.name != undefined)objs = map[vm.subType.name];
                    else objs = map[vm.objectType.name];
                    if (objs != undefined && objs != null) {
                        angular.forEach(vm.headerObjs, function (obj) {
                            var val = objs[obj.header];
                            if (val != null && vm.headers.includes(val)) {
                                obj.mapHeader = val;
                                removeFromHeader(val);
                            } else if (vm.headers.includes(obj.header)) {
                                obj.mapHeader = obj.header;
                                removeFromHeader(obj.header);
                            }
                        });
                    } else {
                        mapHeaders();
                    }
                    vm.headers1.push("Do not map this field");
                } else {
                    mapHeaders();
                    vm.headers1.push("Do not map this field");
                }
            }

            function mapHeaders() {
                angular.forEach(vm.headerObjs, function (obj) {
                    if (vm.headers.includes(obj.header)) {
                        obj.mapHeader = obj.header;
                        removeFromHeader(obj.header);
                    }
                });
            }

            Array.prototype.pushArray = function (arr) {
                this.push.apply(this, arr);
            };

            function removeFromHeader(valu) {
                var index = vm.headers1.indexOf(valu);
                if (index !== -1) {
                    vm.headers1.splice(index, 1);
                }
            }

            function doMapping() {
                var headMap = {};
                angular.forEach(vm.headerObjs, function (obje) {
                    if (obje.mapHeader != "") {
                        headMap[obje.header] = obje.mapHeader;
                    }
                });
                var type;
                if (vm.subType != null && vm.subType.name != undefined) type = vm.subType.name;
                else type = vm.objectType.name;
                ExImService.sendMappingHeaders(type, headMap).then(
                    function (data) {
                        startImport();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            var parsed = angular.element("<div></div>");
            var dataImported = parsed.html($translate.instant("DATA_IMPORTED")).html();

            function startImport() {
                if (this.file != null && this.file != "" && this.file != undefined) {
                    $rootScope.showBusyIndicator($('.view-container'));

                    if (vm.subType != null && vm.subType != "") {
                        vm.objectType = vm.subType;
                    }
                    ExImService.importUploadedObjectFile(vm.objectType.name, this.file).then(
                        function (data) {
                            var currentRoute = $state.current.url;
                            if (currentRoute == "/home") {
                                $rootScope.getDashBoardCounts();
                            }
                            $rootScope.hideSidePanel();
                            this.file = null;
                            $rootScope.showSuccessMessage(dataImported);
                            $rootScope.hideBusyIndicator();
                        },
                        function (error) {
                            $rootScope.hideSidePanel();
                            this.file = null;
                            var fileElem = document.getElementById("import-file").value = "";
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    $rootScope.showWarningMessage("Please upload the import file ");
                    //$rootScope.showWarningMessage("Please upload the " + vm.objectType.name + " import file ");
                }

            }

            $scope.selectFiles = selectFiles;
            function selectFiles() {
                $('#import-file')[0].click();
            }

            (function () {
                $rootScope.$on('app.exim.individual.mapping', doMapping);
            })();
        }
    }
)
;