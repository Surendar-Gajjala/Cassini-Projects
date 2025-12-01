define(['app/desktop/modules/pm/pm.module',
        'app/shared/services/core/programService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/desktop/directives/all-view-attributes/allViewAttributesDirective',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective',
        'app/desktop/modules/directives/projectTypeDirective'
    ],
    function (module) {
        module.controller('AllProgramsController', AllProgramsController);

        function AllProgramsController($scope, $rootScope, $translate, $timeout, $state, $window, $stateParams, $cookies, $sce,
                                       ProgramService, ItemService, DialogService, CommonService, ObjectTypeAttributeService, AttributeAttachmentService) {
            $rootScope.viewInfo.icon = "fa fa-calendar";
            $rootScope.viewInfo.title = $translate.instant("PROGRAMS");
            $rootScope.viewInfo.showDetails = false;
            var vm = this;
            vm.newProgram = newProgram;
            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            var searchMode = null;
            vm.deleteProgram = deleteProgram;
            vm.programManagers = [];
            vm.selectedPerson = null;

            vm.viewType = "cards";
            $rootScope.localStorageLogin = JSON.parse(localStorage.getItem('local_storage_login'));
            vm.search = {
                name: null,
                description: null,
                searchType: null,
                query: null,
                objectType: 'ITEM'
            };
            if ($rootScope.localStorageLogin != null) {
                vm.search.owner = $rootScope.localStorageLogin.login.person.id;
            }
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

            vm.programs = angular.copy(pagedResults);

            var parsed = angular.element("<div></div>");

            vm.deleteProjectTitle = parsed.html($translate.instant("DELETE_PROGRAM")).html();
            vm.editProjectTitle = parsed.html($translate.instant("EDIT_PROGRAM")).html();
            vm.projectOpenTitle = parsed.html($translate.instant("PROGRAM_OPEN_TITLE")).html();
            var deleteProjectTitle = parsed.html($translate.instant("DELETE_PROGRAM")).html();
            var deleteProjectDialogMessage = parsed.html($translate.instant("DELETE_TYPE_DIALOG_MESSAGE")).html();
            var projectDelete = parsed.html($translate.instant("ITEMDELETE")).html();
            var projectDeletedMessage = parsed.html($translate.instant("PROGRAM_DELETED_MESSAGE")).html();
            var newProject = parsed.html($translate.instant("NEW_PROGRAM")).html();
            var EditProject = parsed.html($translate.instant("EDIT_PROGRAMS")).html();
            var Update = parsed.html($translate.instant("UPDATE")).html();
            var create = parsed.html($translate.instant("CREATE")).html();
            vm.attributeTitle = parsed.html($translate.instant("PROGRAM_ATTRIBUTE_TITLE")).html();
            vm.conversations = parsed.html($translate.instant("CONVERSATIONS")).html();
            $rootScope.RemoveColumnTitle = parsed.html($translate.instant("REMOVE_ATTRIBUTE_COLUMN")).html();
            $scope.completedProjectCannotDeleteProgram = parsed.html($translate.instant("CANNOT_DELETE_PROGRAM")).html();
            function resetPage() {
                vm.filters.searchQuery = null;
                vm.searchText = null;
                vm.programs = angular.copy(pagedResults);
                vm.pageable.page = 0;
                $rootScope.showSearch = false;
                $rootScope.searchModeType = false;
            }

            function nextPage() {
                if (vm.programs.last != true) {
                    vm.pageable.page++;
                    //performSearch();
                    loadPrograms();
                }
            }

            function previousPage() {
                if (vm.programs.first != true) {
                    vm.pageable.page--;
                    //performSearch();
                    loadPrograms();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.page = 0;
                vm.pageable.size = page;
                loadPrograms();
            }

            vm.clearTypeSelection = clearTypeSelection;
            function clearTypeSelection() {
                vm.pageable.page = 0;
                vm.selectedProgramType = null;
                vm.filters.type = '';
                loadPrograms();
            }

            vm.onSelectType = onSelectType;
            function onSelectType(programType) {
                vm.pageable.page = 0;
                vm.selectedProgramType = programType;
                vm.filters.type = programType.id;
                vm.filters.freeTextSearch = false;
                loadPrograms();
            }

            vm.projectIds = [];
            vm.attributeIds = [];
            vm.filters = {
                name: null,
                type: '',
                programManager: '',
                description: null,
                searchQuery: null
            };

            var share = parsed.html($translate.instant("SHARE")).html();
            var shareProgramTitle = parsed.html($translate.instant("SHARE_PROJECT")).html();
            vm.detailsShareTitle = parsed.html($translate.instant("PROJECT_SHARE_TITLE")).html();
            $rootScope.shareProject = shareProject;
            function shareProject(program) {
                var options = {
                    title: share,
                    template: 'app/desktop/modules/shared/share/shareView.jsp',
                    controller: 'ShareController as shareVm',
                    resolve: 'app/desktop/modules/shared/share/shareController',
                    width: 600,
                    showMask: true,
                    data: {
                        sharedItem: program,
                        itemsSharedType: 'itemSelection',
                        objectType: "PROGRAM"
                    },
                    buttons: [
                        {text: share, broadcast: 'app.share.item'}
                    ],
                    callback: function (data) {
                        $rootScope.showSuccessMessage(program.name + " : Shared successfully");
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function loadPrograms() {
                $rootScope.showBusyIndicator();
                vm.clear = false;
                vm.loading = true;
                ProgramService.getAllPrograms(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.programs = data;
                        vm.loading = false;
                        loadProjectAttributeValues();
                        CommonService.getPersonReferences(vm.programs.content, 'programManager');
                        angular.forEach(vm.programs.content, function (program) {
                            if (program.percentComplete > 0 && program.percentComplete < 100) {
                                program.percentComplete = parseInt(program.percentComplete);
                            }
                            var firstLetter = "";
                            var lastLetter = "";
                            if (program.managerFirstName != null && program.managerFirstName != "") {
                                firstLetter = program.managerFirstName.substring(0, 1).toUpperCase();
                            }
                            if (program.managerLastName != null && program.managerLastName != "") {
                                lastLetter = program.managerLastName.substring(0, 1).toUpperCase();
                            }
                            program.managerWord = firstLetter + "" + lastLetter;
                            if (program.hasManagerImage) {
                                program.managerImage = "api/common/persons/" + program.programManager + "/image/download?" + new Date().getTime();
                            }
                            angular.forEach(program.projectMembers, function (projectMember) {
                                var firstLetter = "";
                                var lastLetter = "";
                                if (projectMember.firstName != null && projectMember.firstName != "") {
                                    firstLetter = projectMember.firstName.substring(0, 1).toUpperCase();
                                }
                                if (projectMember.lastName != null && projectMember.lastName != "") {
                                    lastLetter = projectMember.lastName.substring(0, 1).toUpperCase();
                                }
                                projectMember.imageWord = firstLetter + "" + lastLetter;
                                if (projectMember.hasImage) {
                                    projectMember.personImage = "api/common/persons/" + projectMember.id + "/image/download?" + new Date().getTime();
                                }
                            })
                        });

                        resizeView();
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            $scope.clearProgramManager = clearProgramManager;
            function clearProgramManager() {
                $rootScope.showBusyIndicator($('.view-container'));
                $scope.selectedPerson = null;
                vm.filters.programManager = '';
                loadPrograms();
                $rootScope.hideBusyIndicator();
            }

            $scope.programManagers = [];
            function loadProgramManagers() {
                ProgramService.getProgramManagers().then(
                    function (data) {
                        $scope.programManagers = data;
                    }
                )
            }

            $scope.selectedPerson = null;
            $scope.onSelectProgramManager = onSelectProgramManager;
            function onSelectProgramManager(person) {
                vm.pageable.page = 0;
                $scope.selectedPerson = person;
                vm.filters.programManager = person.id;
                loadPrograms();
            }

            function resizeView() {
                $timeout(function () {
                    var viewContent = $('.view-content').outerHeight();
                    $('.programs-container').height(viewContent - 140)
                }, 500)
            }

            function newProgram() {
                var options = {
                    title: "New Program",
                    template: 'app/desktop/modules/pm/program/new/newProgramView.jsp',
                    controller: 'NewProgramController as newProgramVm',
                    resolve: 'app/desktop/modules/pm/program/new/newProgramController',
                    width: 550,
                    showMask: true,
                    buttons: [
                        {text: create, broadcast: 'app.program.new'}
                    ],
                    callback: function (program) {
                        loadPrograms();
                        loadProgramManagers();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function freeTextSearch(freeText) {
                searchMode = "freetext";
                $scope.freeTextQuery = freeText;
                vm.searchText = freeText;
                $rootScope.projectFreeTextSearchText = freeText;
                vm.pageable.page = 0;
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    vm.filters.searchQuery = freeText;
                    loadPrograms();
                }
                else {
                    vm.resetPage();
                    loadPrograms();
                }
            }

            function deleteProgram(program) {
                var options = {
                    title: deleteProjectTitle,
                    message: deleteProjectDialogMessage + " [ " + program.name + " ] " + projectDelete + "?",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        ProgramService.deleteProgram(program.id).then(
                            function (data) {
                                var index = vm.programs.content.indexOf(program);
                                vm.programs.content.splice(index, 1);
                                loadProgramManagers();
                                $rootScope.showSuccessMessage(projectDeletedMessage);
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                });
            }

            /* ------------- Project Custom properties ----------*/

            vm.selectedAttributes = [];
            vm.objectIds = [];
            var currencyMap = new Hashtable();
            vm.showTypeAttributes = showTypeAttributes;
            vm.removeAttribute = removeAttribute;

            var attributesTitle = $translate.instant("ATTRIBUTES");
            var addButton = parsed.html($translate.instant("ADD")).html();
            var selectedAttributesMessage = parsed.html($translate.instant("SELECTED_ATTRIBUTES_MESSAGE")).html();

            function showTypeAttributes() {
                var options = {
                    title: attributesTitle,
                    template: 'app/desktop/modules/shared/attributes/attributesView.jsp',
                    resolve: 'app/desktop/modules/shared/attributes/attributesController',
                    controller: 'AttributesController as attributesVm',
                    width: 500,
                    showMask: true,
                    data: {
                        selectedAttributes: vm.selectedAttributes,
                        objectType: "PROGRAM"
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.select.attributes'}
                    ],
                    callback: function (result) {
                        vm.selectedAttributes = result;
                        $window.localStorage.setItem("allProgramAttributes", JSON.stringify(vm.selectedAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesMessage);
                        }
                        loadPrograms();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function removeAttribute(att) {
                vm.selectedAttributes.remove(att);
                $window.localStorage.setItem("allProgramAttributes", JSON.stringify(vm.selectedAttributes));
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

            $('#myModalHorizontal').on('hidden', function () {
                $(this).data('modal').$element.removeData();
            });

            vm.openAttachment = openAttachment;
            function openAttachment(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/download".
                    format(window.location.protocol, window.location.host,
                    attachment.id);
                $rootScope.downloadFileFromIframe(url);

            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("allProgramAttributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            function loadProjectAttributeValues() {
                vm.projectIds = [];
                vm.attributeIds = [];
                CommonService.getPersonReferences(vm.programs.content, 'modifiedBy');
                CommonService.getPersonReferences(vm.programs.content, 'createdBy');
                angular.forEach(vm.programs.content, function (item) {
                    vm.projectIds.push(item.id);

                });
                angular.forEach(vm.selectedAttributes, function (selectedAttribute) {
                    if (selectedAttribute.id != null && selectedAttribute.id != "" && selectedAttribute.id != 0) {
                        vm.attributeIds.push(selectedAttribute.id);
                    }
                });
                if (vm.projectIds.length > 0 && vm.attributeIds.length > 0) {
                    ItemService.getAttributesByItemIdAndAttributeId(vm.projectIds, vm.attributeIds).then(
                        function (data) {
                            vm.selectedObjectAttributes = data;

                            var map = new Hashtable();
                            angular.forEach(vm.selectedAttributes, function (att) {
                                if (att.id != null && att.id != "" && att.id != 0) {
                                    map.put(att.id, att);
                                }
                            });

                            angular.forEach(vm.programs.content, function (item) {
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
                                                //item[attributeName] = $sce.trustAsHtml(attribute.richTextValue);
                                                item[attributeName] = attribute;
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
                                            } else if (selectatt.dataType == 'LIST' && !selectatt.listMultiple) {
                                                item[attributeName] = attribute.listValue;
                                            } else if (selectatt.dataType == 'LIST' && selectatt.listMultiple) {
                                                item[attributeName] = attribute.mlistValue;
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
                                                        ProgramService.getProject(attribute.refValue).then(
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

            vm.showProgram = showProgram;
            function showProgram(program) {
                vm.searchText = null;
                $state.go('app.pm.program.details', {programId: program.id, tab: 'details.basic'});
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
                    $state.go('app.pm.project.details', {programId: attribute.id});
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
                };
                $scope.$evalAsync();
            }

            var commentsTitle = parsed.html($translate.instant("CONVERSATION")).html();

            vm.showComments = showComments;
            function showComments(program) {
                var options = {
                    title: commentsTitle,
                    template: 'app/desktop/modules/shared/comments/newCommentsView.jsp',
                    controller: 'NewCommentsController as commentsVm',
                    resolve: 'app/desktop/modules/shared/comments/newCommentsController',
                    width: 600,
                    showMask: true,
                    data: {
                        objectType: "PROGRAM",
                        objectId: program.id,
                        commentCount: program.comments
                    },
                    callback: function (result) {
                        program.comments = result;
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.setViewType = setViewType;
            function setViewType(type) {
                $window.localStorage.setItem('programs-view-type', type);
                resizeView();
            }

            (function () {
                $(window).resize(function () {
                    resizeView();
                });
                //if ($application.homeLoaded == true) {
                angular.forEach($application.currencies, function (data) {
                    currencyMap.put(data.id, $sce.trustAsHtml(data.symbol));
                });
                if (validateJSON()) {
                    var setAttributes = JSON.parse($window.localStorage.getItem("allProgramAttributes"));
                } else {
                    var setAttributes = null;
                }
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
                                $window.localStorage.setItem("allProgramAttributes", "");
                                vm.selectedAttributes = setAttributes
                            } else {
                                vm.selectedAttributes = setAttributes;
                            }
                            loadPrograms();
                            loadProgramManagers();

                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }

                else {
                    loadPrograms();
                    loadProgramManagers();
                }

                /*if ($rootScope.projectFreeTextSearchText == null) {
                 //loadPrograms();
                 } else {
                 freeTextSearch($rootScope.projectFreeTextSearchText);
                 }*/
                $window.localStorage.setItem("project_open_from", null);
                var adminViewType = $window.localStorage.getItem("programs-view-type");
                if (adminViewType != null && adminViewType != "" && adminViewType != undefined) {
                    vm.viewType = adminViewType;
                }
            })();
        }
    }
);


