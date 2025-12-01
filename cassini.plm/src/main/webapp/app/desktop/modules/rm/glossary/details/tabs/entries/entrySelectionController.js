define(
    [
        'app/desktop/modules/rm/rm.module',
        'dropzone',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/shared/services/core/glossaryService',
        'app/shared/services/core/itemTypeService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService'
    ],
    function (module) {
        module.controller('EntrySelectionController', EntrySelectionController);

        function EntrySelectionController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $translate,
                                          CommonService, DialogService, GlossaryService, ItemTypeService, ObjectAttributeService,
                                          AttributeAttachmentService) {

            var vm = this;
            vm.loading = true;

            var glossaryId = $stateParams.glossaryId;
            vm.selectedItems = [];
            vm.selectAllCheck = false;
            vm.selectCheck = selectCheck;
            vm.selectAll = selectAll;

            var parsed = angular.element("<div></div>");
            var attributeRequired = $translate.instant("ATTRIBUTE_REQUIRED");
            var nameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            var descriptionValidation = parsed.html($translate.instant("ENTER_DESCRIPTION")).html();
            var entryCreateMsg = parsed.html($translate.instant("ENTRY_CREATE_MSG")).html();
            var entriesAddedMsg = parsed.html($translate.instant("ENTRIES_ADDED_MSG")).html();
            var selectOneEntryValid = parsed.html($translate.instant("SELECT_ATLEAST_ONE_ENTRY")).html();
            var enterNameIn = parsed.html($translate.instant("ENTER_NAME_IN")).html();
            var enterDescriptionIn = parsed.html($translate.instant("ENTER_DESCRIPTION_IN")).html();
            var attribute = parsed.html($translate.instant("ATTRIBUTES")).html();

            var pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };
            vm.glossaryEntryDetails = [];

            vm.glossaryEntry = {
                id: null,
                version: null,
                latest: null,
                defaultDetail: null,
                defaultGlossaryEntryDetail: null,
                glossaryEntryDetails: []
            };

            vm.showEntries = showEntries;
            vm.showNewEntry = showNewEntry;

            vm.newGlossaryEntryDetail = {
                id: null,
                name: null,
                description: null,
                glossaryEntry: null,
                language: null
            };
            function loadEntryDetails() {
                angular.forEach($rootScope.selectedGlossaryLanguages, function (language) {
                    var newEntryDetail = angular.copy(vm.newGlossaryEntryDetail);
                    newEntryDetail.language = language.language;
                    newEntryDetail.languageName = language.language.language;
                    vm.glossaryEntryDetails.push(newEntryDetail);
                    if (language.language.defaultLanguage) {
                        vm.glossaryEntry.defaultGlossaryEntryDetail = newEntryDetail;
                    }
                })

                $timeout(function () {
                    var newEntryDetail = angular.copy(vm.newGlossaryEntryDetail);
                    newEntryDetail.languageName = attribute;
                    vm.glossaryEntryDetails.push(newEntryDetail);
                }, 100)
            }

            function showEntries() {
                vm.allEntries = true;
                vm.newEntry = false;
            }

            function showNewEntry() {
                vm.allEntries = false;
                vm.newEntry = true;
            }

            function loadGlossaryEntries() {
                GlossaryService.getAllEntriesByLanguage(glossaryId, pageable, $rootScope.selectedLanguage).then(
                    function (data) {
                        vm.glossaryEntries = data;
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function selectAll() {
                vm.selectedItems = [];
                if (vm.selectAllCheck == false) {
                    angular.forEach(vm.glossaryEntries.content, function (item) {
                        item.selected = false;
                    })
                    vm.selectedItems = [];

                } else {
                    vm.error = "";
                    angular.forEach(vm.glossaryEntries.content, function (item) {
                        item.selected = true;
                        vm.selectedItems.push(item);
                    })
                    if (vm.selectedItems.length == vm.glossaryEntries.content.length) {
                        vm.selectAllCheck = true;
                    }
                }
            }

            function selectCheck(item) {
                var flag = true;
                vm.error = "";
                angular.forEach(vm.selectedItems, function (selectedItem) {
                    if (selectedItem.id == item.id) {
                        flag = false;
                        var index = vm.selectedItems.indexOf(item);
                        vm.selectedItems.splice(index, 1);
                        vm.selectAllCheck = false;
                    }
                });
                if (flag) {
                    vm.selectedItems.push(item);
                    if (vm.selectedItems.length == vm.glossaryEntries.content.length) {
                        vm.selectAllCheck = true;
                    }
                }
            }

            function create() {
                if (vm.allEntries == true) {
                    if (vm.selectedItems.length > 0) {
                        var entryItems = [];
                        angular.forEach(vm.selectedItems, function (entry) {
                            var newEntry = {
                                id: null,
                                glossary: glossaryId,
                                entry: entry,
                                notes: null
                            };
                            entryItems.push(newEntry);
                        });
                        if (entryItems.length == vm.selectedItems.length) {
                            GlossaryService.saveGlossaryEntryItems(glossaryId, entryItems).then(
                                function (data) {
                                    $scope.callback(data);
                                    $rootScope.hideSidePanel();
                                    vm.selectedItems = [];
                                    $rootScope.showSuccessMessage(entriesAddedMsg);
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }

                    }

                    if (vm.selectedItems.length == 0) {
                        $rootScope.showWarningMessage(selectOneEntryValid);
                    }
                } else {
                    if (validateEntryDetails()) {
                        vm.validattributes = [];
                        angular.forEach(vm.entryRequiredAttributes, function (attribute) {
                            if (attribute.attributeDef.required == true && attribute.attributeDef.dataType != 'BOOLEAN' &&
                                attribute.attributeDef.dataType != 'TIMESTAMP') {
                                if ($rootScope.checkAttribute(attribute)) {
                                    vm.validattributes.push(attribute);
                                }
                                else {
                                    $rootScope.showWarningMessage(attribute.attributeDef.name + ":" + attributeRequired);
                                }
                            } else {
                                vm.validattributes.push(attribute);
                            }
                        });
                        if (vm.validattributes.length == vm.entryRequiredAttributes.length) {
                            vm.glossaryEntry.glossaryEntryDetails = [];
                            angular.forEach(vm.glossaryEntryDetails, function (detail) {
                                if (detail.languageName != "Attributes") {
                                    vm.glossaryEntry.glossaryEntryDetails.push(detail);
                                }
                            });
                            $rootScope.showBusyIndicator($('#rightSidePanel'));
                            GlossaryService.addGlossaryEntryItem(glossaryId, vm.glossaryEntry).then(
                                function (data) {
                                    vm.glossaryEntry = data;
                                    saveEntryProperties();
                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }
                    }
                }
            }

            function saveEntryProperties() {
                angular.forEach(vm.entryRequiredAttributes, function (reqatt) {
                    vm.entryAttributes.push(reqatt);
                });
                if (vm.entryAttributes.length > 0) {
                    vm.propertyImageAttributes = [];
                    var propertyImages = new Hashtable();
                    angular.forEach(vm.entryAttributes, function (attribute) {
                        attribute.id.objectId = vm.glossaryEntry.id;
                        if (attribute.timeValue != null) {
                            attribute.timeValue = moment(attribute.timeValue).format("HH:mm:ss");
                        }
                        if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                            propertyImages.put(attribute.id.attributeDef, attribute.imageValue);
                            vm.propertyImageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length > 0) {
                            attribute.attachmentValues = addEntryPropertyAttachment(attribute);
                        }
                    });
                    $timeout(function () {
                        ObjectAttributeService.saveItemObjectAttributes(vm.glossaryEntry.id, vm.entryAttributes).then(
                            function (data) {
                                if (vm.propertyImageAttributes.length > 0) {
                                    angular.forEach(vm.propertyImageAttributes, function (propImgAtt) {
                                        ObjectAttributeService.uploadObjectAttributeImage(propImgAtt.id.objectId, propImgAtt.id.attributeDef, propertyImages.get(propImgAtt.id.attributeDef)).then(
                                            function (data) {

                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
                                             }
                                        )
                                    })
                                }
                                $rootScope.hideSidePanel();
                                $rootScope.showSuccessMessage(entryCreateMsg);
                                $scope.callback();
                                $rootScope.hideBusyIndicator();
                                vm.glossaryEntry = {
                                    id: null,
                                    version: null,
                                    latest: null,
                                    defaultDetail: null,
                                    defaultGlossaryEntryDetail: null,
                                    glossaryEntryDetails: []
                                };
                                vm.glossaryEntryDetails = [];
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }, 2000);
                } else {
                    $rootScope.hideSidePanel();
                    $rootScope.showSuccessMessage(entryCreateMsg);
                    $scope.callback();
                    $rootScope.hideBusyIndicator();
                    vm.glossaryEntry = {
                        id: null,
                        version: null,
                        latest: null,
                        defaultDetail: null,
                        defaultGlossaryEntryDetail: null,
                        glossaryEntryDetails: []
                    };
                    vm.glossaryEntryDetails = [];
                }
            }

            function addEntryPropertyAttachment(attribute) {
                var propertyAttachmentIds = [];
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'GLOSSARYENTRY', attachmentFile).then(
                        function (data) {
                            propertyAttachmentIds.push(data[0].id);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                         }
                    )
                });

                return propertyAttachmentIds;
            }

            function validate() {
                var valid = true;
                if (vm.glossaryEntry.name == "" || vm.glossaryEntry.name == null || vm.glossaryEntry.name == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(nameValidation);
                } else if (vm.glossaryEntry.description == "" || vm.glossaryEntry.description == null || vm.glossaryEntry.description == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage(descriptionValidation);
                }

                return valid;
            }

            function validateEntryDetails() {
                var valid = true;
                angular.forEach(vm.glossaryEntryDetails, function (detail) {
                    if (detail.languageName != "Attributes" && detail.language.defaultLanguage == true) {
                        if (valid) {
                            if (detail.name == null || detail.name == "" || detail.name == undefined) {
                                valid = false;
                                $rootScope.showWarningMessage(enterNameIn + " " + detail.language.language);
                            } else if (detail.description == null || detail.description == "" || detail.description == undefined) {
                                valid = false;
                                $rootScope.showWarningMessage(enterDescriptionIn + " " + detail.language.language)
                            }
                        }
                    }

                });

                return valid;
            }

            vm.entryAttributes = [];
            vm.entryRequiredAttributes = [];

            function loadEntryCustomProperties() {
                ItemTypeService.getAllTypeAttributes("GLOSSARYENTRY").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.glossaryEntry.id,
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
                                refValue: null,
                                ref: null,
                                attachmentValues: []
                            };
                            if (attribute.lov != null) {
                                att.lovValues = attribute.lov.values;
                            }

                            if (attribute.dataType == "TIMESTAMP") {
                                att.timestampValue = moment(new Date()).format("DD/MM/YYYY, HH:mm:ss");
                            }
                            if (attribute.required == false) {
                                vm.entryAttributes.push(att);
                            } else {
                                vm.entryRequiredAttributes.push(att);
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            $(document).ready(function () {
                $(".nav-tabs a").click(function () {
                    $(this).tab('show');
                });
            });

            (function () {
                //if ($application.homeLoaded == true) {
                    vm.allEntries = false;
                    vm.newEntry = true;
                    vm.glossaryEntry = {
                        id: null,
                        version: null,
                        latest: null,
                        defaultDetail: null,
                        defaultGlossaryEntryDetail: null,
                        glossaryEntryDetails: []
                    };
                    vm.glossaryEntryDetails = [];
                    //loadGlossaryEntries();
                    loadEntryCustomProperties();
                    loadEntryDetails();
                    vm.selectedItems = [];
                    $scope.$on('app.glossary.entryItem', create);
                //}
            })();

        }
    }
)
;