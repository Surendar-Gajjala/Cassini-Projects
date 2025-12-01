define(
    [
        'app/desktop/modules/rm/rm.module',
        'app/shared/services/core/specificationsService',
        'app/shared/services/core/reqIFService',
        'app/shared/services/core/recentlyVisitedService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemService',
        'app/desktop/modules/classification/directive/folderDirective',
        'app/desktop/modules/classification/directive/folderController',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/desktop/modules/item/all/itemSearchDialogueController',
        'app/desktop/modules/item/all/advancedSearchController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/folderService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/workflowDefinitionService',
        'app/shared/services/core/itemFileService',
        'app/shared/services/core/recentlyVisitedService',
        'app/shared/services/core/projectService',
        'app/desktop/directives/all-view-attributes/allViewAttributesDirective'
    ],
    function (module) {
        module.controller('AllSpecificationsController', AllSpecificationsController);

        function AllSpecificationsController($scope, $rootScope, $timeout, $state, $sce, $stateParams, $cookies, $translate, $window, $interval, $application,
                                             CommonService, SpecificationsService, ItemService, DialogService, ObjectTypeAttributeService,
                                             AttributeAttachmentService, ECOService, WorkflowDefinitionService, MfrService, MfrPartsService,
                                             RecentlyVisitedService, ReqIFService, ProjectService) {

            $rootScope.viewInfo.icon = "fa flaticon-debit-card";
            $rootScope.viewInfo.title = $translate.instant('SPECIFICATIONS');
            $rootScope.viewInfo.showDetails = false;
            var vm = this;
            var parsed = angular.element("<div></div>");
            vm.loading = false;
            vm.selectedAttribute = [];
            vm.selectedObjectAttributes = [];
            vm.objectIds = [];

            vm.mode = null;
            vm.mode = $stateParams.mode;
            var currencyMap = new Hashtable();

            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: vm.pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };

            vm.specifications = angular.copy(pagedResults);
            vm.specifications = [];

            var newItemTitle = $translate.instant("NEW_SPECIFICATION");
            var createButton = $translate.instant("CREATE");
            var specificationAddedToClipboard = $translate.instant("SPECIFICATION_ADDED_TO_CLIPBOARD");

            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.showNewItem = showNewItem;
            vm.showDetails = showDetails;
            $scope.importReqIF = importReqIF;
            $scope.importExcel = importExcel;

            var searchMode = null;

            vm.search = {
                name: null,
                description: null,
                searchType: null,
                query: null,
                type: '',
                objectNumber: null
            };

            function freeTextSearch(freeText) {
                searchMode = "freetext";
                $scope.freeTextQuery = freeText;
                vm.searchText = freeText;
                $rootScope.projectFreeTextSearchText = freeText;
                vm.pageable.page = 0;
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    vm.search.searchType = "freetext";
                    vm.search.query = angular.toJson(freeText);
                    SpecificationsService.specificationFreeTextSearch(freeText, vm.pageable).then(
                        function (data) {
                            vm.specifications = data;
                            vm.clear = true;
                            vm.loading = false;
                            $rootScope.showSearch = true;
                            $rootScope.searchModeType = true;
                            CommonService.getPersonReferences(vm.specifications.content, 'modifiedBy');
                            CommonService.getPersonReferences(vm.specifications.content, 'createdBy');
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
                else {
                    vm.resetPage();
                    loadSpecifications();
                }
            }

            function resetPage() {
                vm.specifications = angular.copy(pagedResults);
                vm.pageable.page = 0;
                $rootScope.showSearch = false;
                $rootScope.searchModeType = false;
            }

            function nextPage() {
                if (vm.specifications.last != true) {
                    vm.pageable.page++;
                    loadSpecifications();
                }
            }

            function previousPage() {
                if (vm.specifications.first != true) {
                    vm.pageable.page--;
                    loadSpecifications();
                }
            }


            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.page = 0;
                vm.pageable.size = page;
                loadSpecifications();
            }

            vm.recentlyVisited = {
                id: null,
                objectId: null,
                objectType: null,
                person: null,
                visitedDate: null
            };

            vm.showTypeAttributes = showTypeAttributes;
            vm.removeAttribute = removeAttribute;

            var attributesTitle = $translate.instant("ATTRIBUTES");
            var addButton = parsed.html($translate.instant("ADD")).html();
            $scope.deleteSpecificationTitle = parsed.html($translate.instant("DELETE_THIS_SPECIFICATION")).html();
            var selectedAttributesMessage = parsed.html($translate.instant("SELECTED_ATTRIBUTES_MESSAGE")).html();
            vm.newSpecTitle = parsed.html($translate.instant("NEW_SPEC_TITLE")).html();
            vm.specAttributesTitle = parsed.html($translate.instant("SPEC_ATTRIBUTES")).html();

            function showTypeAttributes() {
                var selecteAttrs = angular.copy(vm.selectedAttribute);
                var options = {
                    title: attributesTitle,
                    template: 'app/desktop/modules/rm/specification/all/specTypeAttributesView.jsp',
                    resolve: 'app/desktop/modules/rm/specification/all/specTypeAttributesController',
                    controller: 'SpecificationTypeAttributesController as specTypeAttributesVm',
                    width: 500,
                    showMask: true,
                    data: {
                        selectedAttributes: selecteAttrs
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.select.attributes'}
                    ],
                    callback: function (result) {
                        vm.selectedAttribute = result;
                        reInitializeColResize();
                        //$scope.$broadcast('reInitializeColResizable', result);
                        //loadColResize();
                        $window.localStorage.setItem("allSpecattributes", JSON.stringify(vm.selectedAttribute));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesMessage);
                        }
                        loadSpecifications();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function removeAttribute(att) {
                vm.selectedAttribute.remove(att);
                reInitializeColResize();
                //$scope.$broadcast('reInitializeColResizable', att);
                $window.localStorage.setItem("allSpecattributes", JSON.stringify(vm.selectedAttribute));
            }

            function showDetails(spec) {
                var session = JSON.parse(localStorage.getItem('local_storage_login'));
                $rootScope.loginPersonDetails = session.login;
                $state.go('app.rm.specifications.details', {specId: spec.id});
                vm.recentlyVisited.objectId = spec.id;
                vm.recentlyVisited.objectType = spec.objectType;
                vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                    function (data) {

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                     }
                )
            }

            function showNewItem() {
                var options = {
                    title: newItemTitle,
                    template: 'app/desktop/modules/rm/specification/new/newSpecificationView.jsp',
                    controller: 'NewSpecificationController as newSpecificationVm',
                    resolve: 'app/desktop/modules/rm/specification/new/newSpecificationController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: createButton, broadcast: 'app.rm.specifications.new'}
                    ],
                    callback: function () {
                        loadSpecifications();
                    }
                };

                $rootScope.showSidePanel(options);

            }

            vm.itemIds = [];
            vm.attributeIds = [];
            function loadSpecifications() {
                vm.loading = true;
                SpecificationsService.findAllSpecifications(vm.pageable).then(
                    function (data) {
                        vm.specifications = data;
                        angular.forEach(vm.specifications.content, function (obj) {
                            if (obj.modifiedDate) {
                                obj.modifiedDatede = moment(obj.modifiedDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY, HH:mm:ss");
                            }
                        });
                        vm.loading = false;
                        CommonService.getPersonReferences(vm.specifications.content, 'modifiedBy');
                        CommonService.getPersonReferences(vm.specifications.content, 'createdBy');
                        loadItemAttributeValues();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.showImage = showImage;
            function showImage(attribute) {
                var modal = document.getElementById('myModal2');
                var modalImg = document.getElementById('img03');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage1")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            vm.Subscribes = parsed.html($translate.instant("SUBSCRIBES")).html();
            vm.specSubscribesPopover = {
                templateUrl: 'app/desktop/modules/rm/specification/all/specSubscribesTemplate.jsp'
            };

            vm.specPopover = specPopover;
            function specPopover(item) {
                item.openPopover = true;
            }

            function loadItemAttributeValues() {
                vm.itemIds = [];
                vm.attributeIds = [];
                angular.forEach(vm.specifications.content, function (item) {
                    vm.itemIds.push(item.id);

                });
                angular.forEach(vm.selectedAttribute, function (selectedAttribute) {
                    if (selectedAttribute.id != null && selectedAttribute.id != "" && selectedAttribute.id != 0) {
                        vm.attributeIds.push(selectedAttribute.id);
                    }
                });
                if (vm.itemIds.length > 0 && vm.attributeIds.length > 0) {
                    ItemService.getAttributesByItemIdAndAttributeId(vm.itemIds, vm.attributeIds).then(
                        function (data) {
                            vm.selectedObjectAttributes = data;

                            var map = new Hashtable();
                            angular.forEach(vm.selectedAttribute, function (att) {
                                if (att.id != null && att.id != "" && att.id != 0) {
                                    map.put(att.id, att);
                                }
                            });

                            angular.forEach(vm.specifications.content, function (item) {
                                var attributes = [];
                                var revisionAttributes = vm.selectedObjectAttributes[item.latestRevision];
                                if (revisionAttributes != null && revisionAttributes != undefined) {
                                    attributes = attributes.concat(revisionAttributes);
                                }
                                var itemAttributes = vm.selectedObjectAttributes[item.id];
                                if (itemAttributes != null && itemAttributes != undefined) {
                                    attributes = attributes.concat(itemAttributes);
                                }
                                if (attributes.length > 0) {
                                    angular.forEach(attributes, function (attribute) {
                                        var selectatt = map.get(attribute.id.attributeDef);
                                        if (selectatt != null) {
                                            var attributeName = selectatt.id;
                                            if (selectatt.dataType == 'TEXT') {
                                                item[attributeName] = attribute.stringValue;
                                            } else if (selectatt.dataType == 'LONGTEXT') {
                                                item[attributeName] = attribute.longTextValue;
                                            } else if (selectatt.dataType == 'RICHTEXT') {
                                                item[attributeName] = attribute;
                                                // item[attributeName] = $sce.trustAsHtml(attribute.richTextValue);
                                            } else if (selectatt.dataType == 'INTEGER') {
                                                item[attributeName] = attribute.integerValue;
                                            } else if (selectatt.dataType == 'BOOLEAN') {
                                                item[attributeName] = attribute.booleanValue;
                                            } else if (selectatt.dataType == 'HYPERLINK') {
                                                item[attributeName] = attribute.hyperLinkValue;
                                            } else if (selectatt.dataType == 'DOUBLE') {
                                                item[attributeName] = attribute.doubleValue;
                                            } else if (selectatt.dataType == 'DATE') {
                                                item[attributeName] = attribute.dateValue;
                                            } else if (selectatt.dataType == 'LIST') {
                                                if (attribute.listValue != null) {
                                                    item[attributeName] = attribute.listValue;
                                                }
                                                else if (attribute.mlistValue.length > 0) {
                                                    item[attributeName] = attribute.mlistValue;
                                                }
                                            } else if (selectatt.dataType == 'TIME') {
                                                item[attributeName] = attribute.timeValue;
                                            } else if (selectatt.dataType == 'TIMESTAMP') {
                                                item[attributeName] = attribute.timestampValue;
                                            } else if (selectatt.dataType == 'CURRENCY') {
                                                item[attributeName] = attribute.currencyValue;
                                                if (attribute.currencyType != null) {
                                                    item[attributeName + 'type'] = currencyMap.get(attribute.currencyType);
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
                                                            item[attributeName] = vm.revisionAttachments;
                                                        }, function (error) {
                                                            $rootScope.showErrorMessage(error.message);
                                                         }
                                                    )
                                                }
                                            } else if (selectatt.dataType == 'IMAGE') {
                                                if (attribute.imageValue != null) {
                                                    item[attributeName] = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();

                                                }
                                            } else if (selectatt.dataType == 'OBJECT') {
                                                if (selectatt.refType != null) {
                                                    if (selectatt.refType == 'ITEM' && attribute.refValue != null) {
                                                        ItemService.getItem(attribute.refValue).then(
                                                            function (itemValue) {
                                                                item[attributeName] = itemValue;
                                                            }, function (error) {
                                                                $rootScope.showErrorMessage(error.message);
                                                            }
                                                        )
                                                    } else if (selectatt.refType == 'ITEMREVISION' && attribute.refValue != null) {
                                                        ItemService.getRevisionId(attribute.refValue).then(
                                                            function (revisionValue) {
                                                                item[attributeName] = revisionValue;
                                                                ItemService.getItem(revisionValue.itemMaster).then(
                                                                    function (data) {
                                                                        item[attributeName].itemMaster = data.itemNumber;
                                                                    }, function (error) {
                                                                        $rootScope.showErrorMessage(error.message);
                                                                    }
                                                                )
                                                            }, function (error) {
                                                                $rootScope.showErrorMessage(error.message);
                                                            }
                                                        )
                                                    } else if (selectatt.refType == 'CHANGE' && attribute.refValue != null) {
                                                        ECOService.getECO(attribute.refValue).then(
                                                            function (changeValue) {
                                                                item[attributeName] = changeValue;
                                                            }, function (error) {
                                                                $rootScope.showErrorMessage(error.message);
                                                            }
                                                        )
                                                    } else if (selectatt.refType == 'WORKFLOW' && attribute.refValue != null) {
                                                        WorkflowDefinitionService.getWorkflowDefinition(attribute.refValue).then(
                                                            function (workflowValue) {
                                                                item[attributeName] = workflowValue;
                                                            }, function (error) {
                                                                $rootScope.showErrorMessage(error.message);
                                                            }
                                                        )
                                                    } else if (selectatt.refType == 'MANUFACTURER' && attribute.refValue != null) {
                                                        MfrService.getManufacturer(attribute.refValue).then(
                                                            function (mfrValue) {
                                                                item[attributeName] = mfrValue;
                                                            }, function (error) {
                                                                $rootScope.showErrorMessage(error.message);
                                                            }
                                                        )
                                                    } else if (selectatt.refType == 'MANUFACTURERPART' && attribute.refValue != null) {
                                                        MfrPartsService.getManufacturepart(attribute.refValue).then(
                                                            function (mfrPartValue) {
                                                                item[attributeName] = mfrPartValue;
                                                            }, function (error) {
                                                                $rootScope.showErrorMessage(error.message);
                                                            }
                                                        )
                                                    } else if (selectatt.refType == 'PERSON' && attribute.refValue != null) {
                                                        CommonService.getPerson(attribute.refValue).then(
                                                            function (person) {
                                                                item[attributeName] = person;
                                                            }, function (error) {
                                                                $rootScope.showErrorMessage(error.message);
                                                            }
                                                        )
                                                    } else if (selectatt.refType == 'PROJECT' && attribute.refValue != null) {
                                                        ProjectService.getProject(attribute.refValue).then(
                                                            function (project) {
                                                                item[attributeName] = project;
                                                            }, function (error) {
                                                                $rootScope.showErrorMessage(error.message);
                                                            }
                                                        )
                                                    } else if (selectatt.refType == 'REQUIREMENT' && attribute.refValue != null) {
                                                        SpecificationsService.getRequirement(attribute.refValue).then(
                                                            function (reqValue) {
                                                                item[attributeName] = reqValue;
                                                            }, function (error) {
                                                                $rootScope.showErrorMessage(error.message);
                                                            }
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    })
                                } else {
                                    angular.forEach(vm.selectedAttributes, function (selectedAttribute) {
                                        if (selectedAttribute.dataType == "TEXT" && selectedAttribute.defaultTextValue != null) {
                                            var attributeName = selectedAttribute.id;
                                            item[attributeName] = selectedAttribute.defaultTextValue;
                                        }
                                        if (selectedAttribute.dataType == "LIST" && selectedAttribute.defaultListValue != null) {
                                            var attributeName = selectedAttribute.id;
                                            item[attributeName] = selectedAttribute.defaultListValue;
                                        }
                                    });
                                }
                            })

                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                }
            }

            vm.showAttributeDetails = showAttributeDetails;
            function showAttributeDetails(attribute) {
                if (attribute.objectType == 'ITEM') {
                    $state.go('app.items.details', {itemId: attribute.id, tab: 'details.basic'});
                } else if (attribute.objectType == 'ITEMREVISION') {
                    $state.go('app.items.details', {itemId: attribute.id, tab: 'details.basic'});
                } else if (attribute.objectType == 'CHANGE') {
                    $state.go('app.changes.eco.details', {ecoId: attribute.id, tab: 'details.basic'});
                } else if (attribute.objectType == 'PLMWORKFLOWDEFINITION') {
                    $state.go('app.workflow.editor', {mode: 'edit', workflow: attribute.id, tab: 'details.basic'});
                } else if (attribute.objectType == 'MANUFACTURER') {
                    $state.go('app.mfr.details', {manufacturerId: attribute.id, tab: 'details.basic'});
                } else if (attribute.objectType == 'MANUFACTURERPART') {
                    $state.go('app.mfr.mfrparts.details', {
                        mfrId: attribute.manufacturer,
                        manufacturePartId: attribute.id,
                        tab: 'details.basic'
                    });
                } else if (attribute.objectType == 'REQUIREMENT') {
                    $state.go('app.rm.requirements.details', {requirementId: attribute.id});
                } else if (attribute.objectType == 'PROJECT') {
                    $state.go('app.pm.project.details', {projectId: attribute.id});
                }
            }


            /*----- Delete Spec Sections and Requirements ----*/

            vm.deleteSpecification = deleteSpecification;
            var deleteSpecDialogTitle = parsed.html($translate.instant("DELETE_SPECIFICATION")).html();
            var deleteSpecDialogMessage = parsed.html($translate.instant("DELETE_SPECIFICATION_DIALOG_MESSAGE")).html();
            var specDeletedMessage = parsed.html($translate.instant("SPECIFICATION_DELETE_MSG")).html();
            var specDelete = parsed.html($translate.instant("ITEMDELETE")).html();

            function deleteSpecification(spec) {
                var options = {
                    title: deleteSpecDialogTitle,
                    message: deleteSpecDialogMessage + " [ " + spec.name + " ] " + specDelete + "?",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        SpecificationsService.deleteSpecification(spec.id).then(
                            function (data) {
                                $rootScope.showSuccessMessage(specDeletedMessage);
                                loadSpecifications();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                });
            }

            $('#myModalHorizontal').on('hidden', function () {
                $(this).data('modal').$element.removeData();
            })

            vm.openAttachment = openAttachment;
            function openAttachment(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/download".
                    format(window.location.protocol, window.location.host,
                    attachment.id);
                //launchUrl(url);
                window.open(url);
                $timeout(function () {
                    window.close();
                }, 2000);

            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("allSpecattributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            function importReqIF() {
                $rootScope.showBusyIndicator($('.view-container'));
                var fileElem = document.getElementById("fileReqIf");
                var file = fileElem.files[0];
                ReqIFService.convertToCassini(file).then(
                    function (data) {
                        loadSpecifications();
                        $rootScope.hideBusyIndicator();
                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            /*    Show Modal dialogue for RichText*/
            $scope.showModal = showModal;
            function showModal(data) {
                $("#myModalHorizontal").show();
                var mymodal = $('#myModalHorizontal');
                vm.modalValue = data
                mymodal.modal('show');
            }

            function importExcel() {
                $rootScope.showBusyIndicator($('.view-container'));
            }

            /*  ReInitialize ColResizable columns*/
            vm.reInitializeColResize = reInitializeColResize;
            function reInitializeColResize() {
                $(".JCLRgrips").css("display", "none");
                $timeout(function () {
                    $scope.$broadcast('reInitializeColResizable', "");
                }, 1000)
            }

            vm.clipBoardSpecs = $application.clipboard.deliverables.specIds;
            vm.showCopyToClipBoard = false;

            vm.selectSpecification = selectSpecification;
            vm.copyToClipBoard = copyToClipBoard;
            vm.clearAndCopyToClipBoard = clearAndCopyToClipBoard;
            vm.selectedSpecs = [];
            function copyToClipBoard() {
                angular.forEach(vm.selectedSpecs, function (glossary) {
                    glossary.selected = false;
                    $application.clipboard.deliverables.specIds.push(glossary.id);
                });

                vm.clipBoardSpecs = $application.clipboard.deliverables.specIds;
                $rootScope.clipBoardDeliverables.specIds = $application.clipboard.deliverables.specIds;
                vm.showCopyToClipBoard = false;
                vm.selectedSpecs = [];
                $rootScope.showSuccessMessage(specificationAddedToClipboard);
            }

            function clearAndCopyToClipBoard() {
                $application.clipboard.deliverables.specIds = [];
                copyToClipBoard();
            }

            function selectSpecification(glossary) {
                if (glossary.selected) {
                    vm.selectedSpecs.push(glossary);
                } else {
                    vm.selectedSpecs.splice(vm.selectedSpecs.indexOf(glossary), 1);
                }

                if (vm.selectedSpecs.length > 0) {
                    vm.showCopyToClipBoard = true;
                } else {
                    vm.showCopyToClipBoard = false;
                }
            }


            vm.showThumbnailImage = showThumbnailImage;
            function showThumbnailImage(item) {
                var modal = document.getElementById('item-thumbnail' + item.id);
                modal.style.display = "block";

                var span = document.getElementById("thumbnail-close" + item.id);
                $("#thumbnail-image" + item.id).width($('#thumbnail-view' + item.id).outerWidth());
                $("#thumbnail-image" + item.id).height($('#thumbnail-view' + item.id).outerHeight());

                span.onclick = function () {
                    modal.style.display = "none";
                }
                $scope.$evalAsync();
            }

            (function () {
                $scope.$on('$viewContentLoaded', function () {
                    reInitializeColResize();
                    angular.forEach($application.currencies, function (data) {
                        currencyMap.put(data.id, $sce.trustAsHtml(data.symbol));
                    })
                    if (validateJSON()) {
                        var setAttributes = JSON.parse($window.localStorage.getItem("allSpecattributes"));
                    } else {
                        var setAttributes = null;
                    }
                    $window.localStorage.setItem("lastSelectedRequirementHeight", "");
                    if (setAttributes != null && setAttributes != undefined) {
                        angular.forEach(setAttributes, function (setAtt) {
                            if (setAtt.id != null && setAtt.id != "" && setAtt.id != 0) {
                                vm.objectIds.push(setAtt.id);
                            }
                        });
                        ObjectTypeAttributeService.getObjectTypeAttributesByIds(vm.objectIds).then(
                            function (data) {
                                if (data.length == 0) {
                                    setAttributes = null;
                                    $window.localStorage.setItem("allSpecattributes", "");
                                    vm.selectedAttribute = setAttributes
                                } else {
                                    vm.selectedAttribute = setAttributes;
                                }

                                if (vm.mode == null || vm.mode == undefined || vm.mode == "") {
                                    loadSpecifications();
                                } else {
                                    loadSpecifications();
                                }
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    } else {
                        if (vm.mode == null || vm.mode == undefined || vm.mode == "") {
                            loadSpecifications();
                        } else {
                            loadSpecifications();
                        }
                    }
                    $window.localStorage.setItem("lastSelectedSpecificationTab", JSON.stringify('details.sections'));
                });
            })();
        }
    }
);