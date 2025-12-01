define(['app/desktop/modules/mfr/mfr.module',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/itemService',
        'app/desktop/modules/mfr/mfrparts/new/newmfrpartController',
        'app/shared/services/core/itemService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/workflowDefinitionService',
        'app/shared/services/core/recentlyVisitedService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('MfrPartsController', MfrPartsController);

        function MfrPartsController($scope, $rootScope, $timeout, $state, $sce, $stateParams, $window, $cookies, $cookieStore, $translate,
                                    $uibModal, CommonService, MfrPartsService, MfrService, DialogService, ItemService,
                                    AttributeAttachmentService, ECOService, WorkflowDefinitionService,
                                    ObjectTypeAttributeService, RecentlyVisitedService) {
            var vm = this;
            var parsed = angular.element("<div></div>");
            var selectedAttributesAdded = parsed.html($translate.instant("SELECTED_ATTRIBUTES_MESSAGE")).html();
            vm.loading = true;
            var mfrId = $stateParams.manufacturerId;
            vm.part = null;
            vm.showMfrpartDetails = showMfrpartDetails;
            vm.deleteMfrpart = deleteMfrpart;
            vm.loadMfrParts = loadMfrParts;
            vm.clearFilter = clearFilter;
            vm.resetPage = resetPage;
            vm.removeAttribute = removeAttribute;

            var currencyMap = new Hashtable();
            vm.filters = {
                searchQuery: null
            };

            var pageable = {
                page: 0,
                size: 10,
                sort: {
                    field: "modifiedDate"
                }
            };
            var freeText = null;
            var searchMode = null;
            $scope.freeTextQuery = null;

            vm.objectIds = [];
            vm.mfrparts = [];
            vm.selectedMfrPartAttributes = [];
            $rootScope.localStorageLogin = JSON.parse(localStorage.getItem('local_storage_login'));
            if ($rootScope.localStorageLogin.login.person.id != null) {
                vm.person = $rootScope.localStorageLogin.login.person.id;
            }

            vm.search = {
                name: null,
                description: null,
                searchType: null,
                query: null,
                objectType: 'SAVEDSEARCH',
                searchObjectType: 'MANUFACTURERPART',
                type: 'PRIVATE',
                owner: vm.person
            };
            vm.mfrPartsIds = [];
            vm.attributeIds = [];

            function loadMfrParts() {
                vm.loading = true;
                vm.mfrparts = [];
                $rootScope.showBusyIndicator($('body'));
                MfrPartsService.getManufacturerPartsByManufacturer(mfrId).then(
                    function (data) {
                        vm.mfrparts = data;
                        loadMfrPartAttributeValues();
                        $rootScope.hideBusyIndicator();
                        vm.loading = false;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    });
            }

            function loadMfrPartAttributeValues() {
                angular.forEach(vm.mfrparts, function (mfrPart) {
                    vm.mfrPartsIds.push(mfrPart.id);
                    if (mfrPart.thumbnail != null) {
                        mfrPart.imagePath = "api/plm/mfr/parts/" + mfrPart.id + "/image/download?" + new Date().getTime();
                    }
                })
                angular.forEach(vm.selectedMfrPartAttributes, function (partAttribute) {
                    vm.attributeIds.push(partAttribute.id)
                })
                if (vm.mfrPartsIds.length > 0 && vm.attributeIds.length > 0) {
                    ItemService.getAttributesByItemIdAndAttributeId(vm.mfrPartsIds, vm.attributeIds).then(
                        function (data) {
                            vm.mfrPartProperties = data;

                            var map = new Hashtable();
                            angular.forEach(vm.selectedMfrPartAttributes, function (att) {
                                map.put(att.id, att);
                            })

                            angular.forEach(vm.mfrparts, function (mfrPart) {
                                var mfrPartAttributes = [];
                                mfrPartAttributes = vm.mfrPartProperties[mfrPart.id];
                                angular.forEach(mfrPartAttributes, function (attribute) {
                                    var selectatt = map.get(attribute.id.attributeDef);
                                    if (selectatt != null) {
                                        var attributeName = selectatt.id;
                                        if (selectatt.dataType == 'TEXT') {
                                            mfrPart[attributeName] = attribute.stringValue;
                                        } else if (selectatt.dataType == 'LONGTEXT') {
                                            mfrPart[attributeName] = attribute.longTextValue;
                                        } else if (selectatt.dataType == 'RICHTEXT') {
                                            mfrPart[attributeName] = $sce.trustAsHtml(attribute.richTextValue);
                                        } else if (selectatt.dataType == 'INTEGER') {
                                            mfrPart[attributeName] = attribute.integerValue;
                                        } else if (selectatt.dataType == 'BOOLEAN') {
                                            mfrPart[attributeName] = attribute.booleanValue;
                                        } else if (selectatt.dataType == 'HYPERLINK') {
                                            mfrPart[attributeName] = attribute.hyperLinkValue;
                                        } else if (selectatt.dataType == 'DOUBLE') {
                                            mfrPart[attributeName] = attribute.doubleValue;
                                        } else if (selectatt.dataType == 'DATE') {
                                            mfrPart[attributeName] = attribute.dateValue;
                                        } else if (selectatt.dataType == 'LIST' && !selectatt.listMultiple) {
                                            mfrPart[attributeName] = attribute.listValue;
                                        } else if (selectatt.dataType == 'LIST' && selectatt.listMultiple) {
                                            mfrPart[attributeName] = attribute.mlistValue;
                                        } else if (selectatt.dataType == 'TIME') {
                                            mfrPart[attributeName] = attribute.timeValue;
                                        } else if (selectatt.dataType == 'TIMESTAMP') {
                                            mfrPart[attributeName] = attribute.timestampValue;
                                        } else if (selectatt.dataType == 'CURRENCY') {
                                            mfrPart[attributeName] = attribute.currencyValue;
                                            if (attribute.currencyType != null) {
                                                mfrPart[attributeName + 'type'] = currencyMap.get(attribute.currencyType);
                                            }
                                        } else if (selectatt.dataType == 'ATTACHMENT') {
                                            var revisionAttachmentIds = [];
                                            if (attribute.attachmentValues.length > 0) {
                                                angular.forEach(attribute.attachmentValues, function (attachmentId) {
                                                    revisionAttachmentIds.push(attachmentId);
                                                });
                                                AttributeAttachmentService.getMultipleAttributeAttachments(revisionAttachmentIds).then(
                                                    function (data) {
                                                        vm.revisionAttachments = data;
                                                        mfrPart[attributeName] = vm.revisionAttachments;
                                                    }, function (error) {
                                                        $rootScope.showErrorMessage(error.message);
                                                    }
                                                )
                                            }
                                        } else if (selectatt.dataType == 'IMAGE') {
                                            if (attribute.imageValue != null) {
                                                mfrPart[attributeName] = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();
                                            }
                                        } else if (selectatt.dataType == 'OBJECT') {
                                            if (selectatt.refType != null) {
                                                if (selectatt.refType == 'ITEM' && attribute.refValue != null) {
                                                    ItemService.getItem(attribute.refValue).then(
                                                        function (itemValue) {
                                                            mfrPart[attributeName] = itemValue;
                                                        }, function (error) {
                                                            $rootScope.showErrorMessage(error.message);
                                                        }
                                                    )
                                                } else if (selectatt.refType == 'ITEMREVISION' && attribute.refValue != null) {
                                                    ItemService.getItemRevision(attribute.refValue).then(
                                                        function (revisionValue) {
                                                            mfrPart[attributeName] = revisionValue;
                                                        }, function (error) {
                                                            $rootScope.showErrorMessage(error.message);
                                                        }
                                                    )
                                                } else if (selectatt.refType == 'CHANGE' && attribute.refValue != null) {
                                                    ECOService.getECO(attribute.refValue).then(
                                                        function (changeValue) {
                                                            mfrPart[attributeName] = changeValue;
                                                        }, function (error) {
                                                            $rootScope.showErrorMessage(error.message);
                                                        }
                                                    )
                                                } else if (selectatt.refType == 'WORKFLOW' && attribute.refValue != null) {
                                                    WorkflowDefinitionService.getWorkflowDefinition(attribute.refValue).then(
                                                        function (workflowValue) {
                                                            mfrPart[attributeName] = workflowValue;
                                                        }, function (error) {
                                                            $rootScope.showErrorMessage(error.message);
                                                        }
                                                    )
                                                } else if (selectatt.refType == 'MANUFACTURER' && attribute.refValue != null) {
                                                    MfrService.getManufacturer(attribute.refValue).then(
                                                        function (mfrValue) {
                                                            mfrPart[attributeName] = mfrValue;
                                                        }, function (error) {
                                                            $rootScope.showErrorMessage(error.message);
                                                        }
                                                    )
                                                } else if (selectatt.refType == 'MANUFACTURERPART' && attribute.refValue != null) {
                                                    MfrPartsService.getManufacturepart(attribute.refValue).then(
                                                        function (mfrPartValue) {
                                                            mfrPart[attributeName] = mfrPartValue;
                                                        }, function (error) {
                                                            $rootScope.showErrorMessage(error.message);
                                                        }
                                                    )
                                                } else if (selectatt.refType == 'PERSON' && attribute.refValue != null) {
                                                    CommonService.getPerson(attribute.refValue).then(
                                                        function (person) {
                                                            mfrPart[attributeName] = person;
                                                        }, function (error) {
                                                            $rootScope.showErrorMessage(error.message);
                                                        }
                                                    )
                                                }
                                            }

                                        }
                                    }
                                })
                            })
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            vm.showAttributeDetails = showAttributeDetails;
            function showAttributeDetails(attribute) {
                if (attribute.objectType == 'ITEM') {
                    $state.go('app.items.details', {itemId: attribute.id});
                } else if (attribute.objectType == 'ITEMREVISION') {
                    $state.go('app.items.details', {itemId: attribute.id});
                } else if (attribute.objectType == 'CHANGE') {
                    $state.go('app.changes.ecos.details', {ecoId: attribute.id});
                } else if (attribute.objectType == 'PLMWORKFLOWDEFINITION') {
                    $state.go('app.workflow.editor', {mode: 'edit', workflow: attribute.id});
                } else if (attribute.objectType == 'MANUFACTURER') {
                    $state.go('app.mfr.details', {manufacturerId: attribute.id});
                } else if (attribute.objectType == 'MANUFACTURERPART') {
                    $state.go('app.mfr.mfrparts.details', {
                        mfrId: attribute.manufacturer,
                        manufacturePartId: attribute.id
                    });
                }
            }

            /*--------- To Download ATTACHMENT Attribute File  --------------*/

            vm.openAttachment = openAttachment;
            function openAttachment(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/download".
                    format(window.location.protocol, window.location.host,
                    attachment.id);
                $rootScope.downloadFileFromIframe(url);
            }

            vm.showImage = showImage;
            function showImage(attribute) {
                var modal = document.getElementById('myModal21');
                var modalImg = document.getElementById('img031');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage11")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            function clearFilter() {
                loadMfrParts();
                vm.clear = false;
            }

            function removeAttribute(att) {
                vm.selectedMfrPartAttributes.remove(att);
                $window.localStorage.setItem("attributes", JSON.stringify(vm.selectedMfrPartAttributes));
            }

            vm.recentlyVisited = {
                id: null,
                objectId: null,
                objectType: null,
                person: null,
                visitedDate: null
            };

            function showMfrpartDetails(mfrpart) {
                var session = JSON.parse(localStorage.getItem('local_storage_login'));
                $rootScope.loginPersonDetails = session.login;
                $window.localStorage.setItem("lastSelectedMfrTab", JSON.stringify(vm.mfrPartTabId));
                $state.go('app.mfr.mfrparts.details', {mfrId: mfrId, manufacturePartId: mfrpart.id});
                vm.recentlyVisited.objectId = mfrpart.id;
                vm.recentlyVisited.objectType = mfrpart.objectType;
                vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                    function (data) {

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            $scope.deletePartTitle = parsed.html($translate.instant("PART_DIALOG_TITLE")).html();
            $scope.deleteDialogMessage = parsed.html($translate.instant("PART_DIALOG_MESSAGE")).html();
            var deletePartMessage = $translate.instant("PART_DELETE_MESSAGE");
            var partDelete = parsed.html($translate.instant("ITEMDELETE")).html();
            $scope.PartUsedMessage = parsed.html($translate.instant("PART_USED_MESSAGE")).html();

            function deleteMfrpart(mfrpart) {
                MfrPartsService.getMfrPartUsedCount(mfrpart.manufacturer, mfrpart.id).then(
                    function (data) {
                        if (data == 0) {
                            var options = {
                                title: $scope.deletePartTitle,
                                message: $scope.deleteDialogMessage + " [ " + mfrpart.partName + " ] " + partDelete + "?",
                                okButtonClass: 'btn-danger'
                            };
                            DialogService.confirm(options, function (yes) {
                                if (yes == true) {
                                    MfrPartsService.deleteManufacturepart(mfrpart.manufacturer, mfrpart.id).then(
                                        function (data) {
                                            var index = vm.mfrparts.indexOf(mfrpart);
                                            vm.mfrparts.splice(index, 1);
                                            $rootScope.showSuccessMessage(deletePartMessage);
                                            $rootScope.loadMfrCounts();
                                        }, function (error) {
                                            $rootScope.showErrorMessage(error.message);
                                            $rootScope.hideBusyIndicator();
                                        }
                                    )
                                }
                            });
                        } else {

                            $rootScope.showErrorMessage($scope.PartUsedMessage);

                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function resetPage() {
                clearFilter();
                pageable.page = 0;
                $rootScope.showSearch = false;
                $rootScope.searchModeType = false;
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("mfrPartAttributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            /*    Show Modal dialogue for RichText*/
            $scope.showModal = showModal;
            function showModal(data) {
                $("#myModalHorizontal").show();
                var mymodal = $('#myModalHorizontal');
                vm.modalValue = data
                mymodal.modal('show');
            }

            $scope.saved = parsed.html($translate.instant("SAVED_SEARCH_TITLE")).html();

            $scope.$on('app.mfr.freetextsearchpart', function (event, args) {
                var freeText = args;
                freeTextSearch(freeText);
            })

            function freeTextSearch(freeText) {
                searchMode = "freetext";
                vm.searchText = freeText;
                $rootScope.mfrPartFreeTextSearchText = freeText;
                $scope.freeTextQuery = freeText;
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    vm.search.searchType = "freetext";
                    vm.search.query = angular.toJson(freeText);
                    $rootScope.searchModeType = true;
                    MfrPartsService.freeTextSearch(pageable, freeText, mfrId).then(
                        function (data) {
                            vm.mfrparts = data.content;
                            loadMfrPartAttributeValues();
                            vm.clear = true;
                            vm.loading = false;
                            $rootScope.showSearch = true;
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    vm.resetPage();
                    // loadMfrParts();
                    $rootScope.mfrPartFreeTextSearchText = null;
                }
            }

            var MfrPartTitle = parsed.html($translate.instant("NEW_MANUFACTURER_PART_TITLE")).html();
            var MfrAttributeTitle = $translate.instant("ATTRIBUTES");
            var SearchTitle = $translate.instant("NEW_SEARCH");
            var createButton = parsed.html($translate.instant("CREATE")).html();
            var addButton = parsed.html($translate.instant("ADD")).html();
            vm.crateParts = parsed.html($translate.instant("CREATE_PART")).html();

            vm.addMfrParts = addMfrParts;
            function addMfrParts() {
                var options = {
                    title: MfrPartTitle,
                    template: 'app/desktop/modules/mfr/mfrparts/new/newmfrpartView.jsp',
                    controller: 'NewmfrpartController as newMfrPartVm',
                    resolve: 'app/desktop/modules/mfr/mfrparts/new/newmfrpartController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: createButton, broadcast: 'app.mfr.mfrparts.new'}
                    ],
                    data: {
                        mfrId: mfrId
                    },
                    callback: function (mfrPart) {
                        $timeout(function () {
                            loadMfrParts();
                        }, 500);
                        $rootScope.loadMfrCounts();
                    }
                };
                $rootScope.showSidePanel(options);
            }


            vm.showThumbnailImage = showThumbnailImage;
            function showThumbnailImage(item) {
                var modal = document.getElementById('mfrpart-thumbnail' + item.id);
                modal.style.display = "block";

                var span = document.getElementById("thumbnail-close" + item.id);
                $("#thumbnail-image" + item.id).width($('#thumbnail-view' + item.id).outerWidth());
                $("#thumbnail-image" + item.id).height($('#thumbnail-view' + item.id).outerHeight());

                span.onclick = function () {
                    modal.style.display = "none";
                }
                $scope.$evalAsync();
            }

            $rootScope.showPartAttributes = showPartAttributes;
            function showPartAttributes() {
                var options = {
                    title: MfrAttributeTitle,
                    template: 'app/desktop/modules/shared/attributes/attributesView.jsp',
                    resolve: 'app/desktop/modules/shared/attributes/attributesController',
                    controller: 'AttributesController as attributesVm',
                    width: 500,
                    data: {
                        selectedAttributes: vm.selectedMfrPartAttributes,
                        type: "MANUFACTURERPARTTYPE",
                        objectType: "MANUFACTURERPART"
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.select.attributes'}
                    ],
                    callback: function (result) {

                        vm.selectedMfrPartAttributes = result;
                        /*$cookieStore.put('mfrPartAttributes', vm.selectedMfrPartAttributes);*/
                        $window.localStorage.setItem("mfrPartAttributes", JSON.stringify(vm.selectedMfrPartAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesAdded);
                        }
                        loadMfrParts();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            $rootScope.showMfrPartSavedSearches = showMfrPartSavedSearches;
            function showMfrPartSavedSearches() {
                var options = {
                    title: $scope.saved,
                    template: 'app/desktop/modules/item/showSavedSearches.jsp',
                    controller: 'SavedSearchesController as searchVm',
                    resolve: 'app/desktop/modules/item/savedSearchesController',
                    width: 600,
                    showMask: true,
                    data: {
                        type: "MANUFACTURERPART"
                    },
                    callback: function (result) {
                        var query = angular.fromJson(result.query);
                        if (result.searchType == 'freetext') {
                            freeTextSearch(query);
                        }
                    }
                };
                $rootScope.showSidePanel(options);
            }

            $rootScope.showSaveSearch = showSaveSearch;

            function showSaveSearch() {
                var options = {
                    title: SearchTitle,
                    template: 'app/desktop/modules/item/newSearchView.jsp',
                    controller: 'NewSearchController as newSearchVm',
                    resolve: 'app/desktop/modules/item/newSearchController',
                    width: 600,
                    data: {
                        search: vm.search
                    },
                    showMask: true,
                    buttons: [
                        {text: createButton, broadcast: 'app.search.new'}
                    ],
                    callback: function () {

                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.showImage = showImage;
            function showImage(part) {
                var modal = document.getElementById('item-thumbnail' + part.id);
                modal.style.display = "block";

                var span = document.getElementById("thumbnail-close" + part.id);
                $("#thumbnail-image" + part.id).width($('#thumbnail-view' + part.id).outerWidth());
                $("#thumbnail-image" + part.id).height($('#thumbnail-view' + part.id).outerHeight());

                span.onclick = function () {
                    modal.style.display = "none";
                }
                $scope.$evalAsync();
            }


            (function () {
                //if ($application.homeLoaded == true) {
                $scope.$on('app.mfr.tabactivated', function (event, data) {
                    if (data.tabId == 'details.parts') {
                        // loadMfrParts();
                        vm.mfrPartTabId = data.tabId;
                        angular.forEach($application.currencies, function (data) {
                            currencyMap.put(data.id, $sce.trustAsHtml(data.symbol));
                        })

                        if (validateJSON()) {
                            var setAttributes = JSON.parse($window.localStorage.getItem("mfrPartAttributes"));
                        } else {
                            var setAttributes = null;
                        }
                        if (setAttributes != null && setAttributes != undefined) {
                            angular.forEach(setAttributes, function (setAtt) {
                                vm.objectIds.push(setAtt.id);
                            })
                            ObjectTypeAttributeService.getObjectTypeAttributesByIds(vm.objectIds).then(
                                function (data) {
                                    if (data.length == 0) {
                                        setAttributes = null;
                                        $window.localStorage.setItem("mfrPartAttributes", "");
                                        vm.selectedMfrPartAttributes = setAttributes
                                    } else {
                                        vm.selectedMfrPartAttributes = setAttributes;
                                    }
                                    if ($rootScope.projectFreeTextSearchText == null) {
                                        loadMfrParts();
                                    } else {
                                        freeTextSearch($rootScope.projectFreeTextSearchText);
                                    }

                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        } else {
                            if ($rootScope.mfrPartFreeTextSearchText == null) {
                                loadMfrParts();
                            } else {
                                freeTextSearch($rootScope.mfrPartFreeTextSearchText);
                            }
                        }
                    }
                    /*$scope.$on('app.mfr.freetextsearchpart', function (event, args) {
                     var freeText = args;
                     freeTextSearch(freeText);
                     })*/

                })
                //}
            })();
        }
    }
)
;