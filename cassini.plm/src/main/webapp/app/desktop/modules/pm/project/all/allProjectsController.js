define(['app/desktop/modules/pm/pm.module',
        'app/shared/services/core/projectService',
        'app/shared/services/core/activityService',
        'app/shared/services/core/recentlyVisitedService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/folderService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/workflowDefinitionService',
        'app/shared/services/core/itemFileService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/specificationsService',
        'app/desktop/directives/all-view-attributes/allViewAttributesDirective',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective',
        'app/desktop/modules/directives/projectTypeDirective'
    ],
    function (module) {
        module.controller('AllProjectsController', AllProjectsController);

        function AllProjectsController($scope, $rootScope, $translate, $timeout, $state, $window, $stateParams, $cookies, $sce,
                                       ProjectService, ActivityService, DialogService, CommonService, RecentlyVisitedService, ItemService, ObjectTypeAttributeService,
                                       AttributeAttachmentService, ECOService, WorkflowDefinitionService, MfrService, MfrPartsService, SpecificationsService) {
            $rootScope.viewInfo.icon = "fa fa-calendar";
            $rootScope.viewInfo.title = $translate.instant("PROJECTS");
            $rootScope.viewInfo.showDetails = false;
            var vm = this;
            vm.showNewProjectDialog = showNewProjectDialog;
            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            var searchMode = null;
            vm.deleteProject = deleteProject;
            vm.editProject = editProject;
            $rootScope.fromMyTaskWidget = null;

            vm.viewType = "cards";
            $rootScope.localStorageLogin = JSON.parse(localStorage.getItem('local_storage_login'));
            vm.search = {
                name: null,
                type: null,
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

            vm.projects = angular.copy(pagedResults);

            var parsed = angular.element("<div></div>");

            vm.deleteProjectTitle = parsed.html($translate.instant("DELETE_PROJECT")).html();
            vm.editProjectTitle = parsed.html($translate.instant("EDIT_PROJECT")).html();
            vm.projectOpenTitle = parsed.html($translate.instant("PROJECT_OPEN_TITLE")).html();
            var deleteProjectTitle = parsed.html($translate.instant("DELETE_PROJECT_TITLE")).html();
            var deleteProjectDialogMessage = parsed.html($translate.instant("DELETE_TYPE_DIALOG_MESSAGE")).html();
            var projectDelete = parsed.html($translate.instant("ITEMDELETE")).html();
            var projectDeletedMessage = parsed.html($translate.instant("PROJECT_DELETED_MESSAGE")).html();
            var newProject = parsed.html($translate.instant("NEW_PROJECT")).html();
            var EditProject = parsed.html($translate.instant("EDIT_PROJECTS")).html();
            var Update = parsed.html($translate.instant("UPDATE")).html();
            var create = parsed.html($translate.instant("CREATE")).html();
            vm.attributeTitle = parsed.html($translate.instant("PROJECT_ATTRIBUTE_TITLE")).html();
            vm.share = parsed.html($translate.instant("SHARE")).html();
            vm.tasks = parsed.html($translate.instant("TASKS")).html();
            vm.conversations = parsed.html($translate.instant("CONVERSATIONS")).html();
            vm.deliverables = parsed.html($translate.instant("DELIVERABLES")).html();
            var subscribeTitle = parsed.html($translate.instant("SUBSCRIBE_TITLE")).html();
            var unsubscribeTitle = parsed.html($translate.instant("UN_SUBSCRIBE_TITLE")).html();
            var unsubscribeMsg = parsed.html($translate.instant("PROJECT_UN_SUBSCRIBE_MSG")).html();
            var subscribeMsg = parsed.html($translate.instant("PROJECT_SUBSCRIBE_MSG")).html();
            $rootScope.RemoveColumnTitle = parsed.html($translate.instant("REMOVE_ATTRIBUTE_COLUMN")).html();
            $scope.startedProjectCannotBeDeleted = parsed.html($translate.instant("STARTED_PROJECT_CANNOT_BE_DELETED")).html();
            $scope.completedProjectCannotBeEdited = parsed.html($translate.instant("COMPLETED_PROJECT_CANNOT_EDIT")).html();

            function resetPage() {
                vm.projectFilter.searchQuery = null;
                vm.projects = angular.copy(pagedResults);
                vm.pageable.page = 0;
                $rootScope.showSearch = false;
                $rootScope.searchModeType = false;
            }

            function nextPage() {
                if (vm.projects.last != true) {
                    vm.pageable.page++;
                    //performSearch();
                    loadProjects();
                }
            }

            function previousPage() {
                if (vm.projects.first != true) {
                    vm.pageable.page--;
                    //performSearch();
                    loadProjects();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.page = 0;
                vm.pageable.size = page;
                loadProjects();
            }

            vm.clearTypeSelection = clearTypeSelection;
            function clearTypeSelection() {
                vm.pageable.page = 0;
                vm.selectedProjectType = null;
                vm.projectFilter.type = '';
                loadProjects();
            }

            vm.onSelectType = onSelectType;
            function onSelectType(projectType) {
                vm.pageable.page = 0;
                vm.selectedProjectType = projectType;
                vm.projectFilter.type = projectType.id;
                vm.projectFilter.freeTextSearch = false;
                loadProjects();
            }

            vm.projectIds = [];
            vm.attributeIds = [];
            vm.projectFilter = {
                name: null,
                type: '',
                projectManager: '',
                description: null,
                searchQuery: null,
                program: ''
            };

            var share = parsed.html($translate.instant("SHARE")).html();
            var shareProjectTitle = parsed.html($translate.instant("SHARE_PROJECT")).html();
            vm.detailsShareTitle = parsed.html($translate.instant("PROJECT_SHARE_TITLE")).html();
            $rootScope.shareProject = shareProject;
            function shareProject(project) {
                var options = {
                    title: shareProjectTitle,
                    template: 'app/desktop/modules/shared/share/shareView.jsp',
                    controller: 'ShareController as shareVm',
                    resolve: 'app/desktop/modules/shared/share/shareController',
                    width: 600,
                    showMask: true,
                    data: {
                        sharedItem: project,
                        itemsSharedType: 'itemSelection',
                        objectType: "PROJECT"
                    },
                    buttons: [
                        {text: share, broadcast: 'app.share.item'}
                    ],
                    callback: function (data) {
                        $rootScope.showSuccessMessage(project.name + " : Shared successfully");
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.subscribeProject = subscribeProject;
            function subscribeProject(project) {
                ProjectService.subscribe(project.id).then(
                    function (data) {
                        vm.subscribe = data;
                        project.isSubscribed = vm.subscribe.subscribe;
                        if (vm.subscribe.subscribe) {
                            $rootScope.showSuccessMessage(subscribeMsg);
                            $scope.subscribeButtonTitle = unsubscribeTitle;
                        } else {
                            $rootScope.showSuccessMessage(unsubscribeMsg);
                            $scope.subscribeButtonTitle = subscribeTitle;
                        }

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function loadProjects() {
                $rootScope.showBusyIndicator();
                vm.clear = false;
                vm.loading = true;
                ProjectService.getFilteredProjects(vm.pageable, vm.projectFilter).then(
                    function (data) {
                        vm.projects = data;
                        vm.loading = false;
                        loadProjectAttributeValues();
                        CommonService.getPersonReferences(vm.projects.content, 'projectManager');
                        angular.forEach(vm.projects.content, function (project) {
                            if (project.percentComplete > 0 && project.percentComplete < 100) {
                                project.percentComplete = parseInt(project.percentComplete);
                            }
                            if (project.plannedStartDate) {
                                project.plannedStartDatede = moment(project.plannedStartDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY");
                            }
                            if (project.plannedFinishDate) {
                                project.plannedFinishDatede = moment(project.plannedFinishDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY");
                            }
                            if (project.actualStartDate) {
                                project.actualStartDatede = moment(project.actualStartDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY");
                            }
                            if (project.actualFinishDatede) {
                                project.actualFinishDatede = moment(project.actualFinishDate, "DD/MM/YYYY, HH:mm:ss").format("DD.MM.YYYY");
                            }
                            var firstLetter = "";
                            var lastLetter = "";
                            if (project.managerFirstName != null && project.managerFirstName != "") {
                                firstLetter = project.managerFirstName.substring(0, 1).toUpperCase();
                            }
                            if (project.managerLastName != null && project.managerLastName != "") {
                                lastLetter = project.managerLastName.substring(0, 1).toUpperCase();
                            }
                            project.managerWord = firstLetter + "" + lastLetter;
                            if (project.hasManagerImage) {
                                project.managerImage = "api/common/persons/" + project.projectManager + "/image/download?" + new Date().getTime();
                            }
                            angular.forEach(project.projectMembers, function (projectMember) {
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

            $scope.clearProjectManager = clearProjectManager;
            function clearProjectManager() {
                $rootScope.showBusyIndicator($('.view-container'));
                $scope.selectedPerson = null;
                vm.projectFilter.projectManager = '';
                loadProjects();
                $rootScope.hideBusyIndicator();
            }

            $scope.projectManagers = [];
            function loadProjectManagers() {
                ProjectService.getProjectManagers().then(
                    function (data) {
                        $scope.projectManagers = data;
                    }
                )
            }

            $scope.selectedPerson = null;
            $scope.onSelectProjectManager = onSelectProjectManager;
            function onSelectProjectManager(person) {
                vm.pageable.page = 0;
                $scope.selectedPerson = person;
                vm.projectFilter.projectManager = person.id;
                loadProjects();
            }

            function resizeView() {
                $timeout(function () {
                    var viewContent = $('.view-content').outerHeight();
                    $('.projects-container').height(viewContent - 140)
                }, 500)
            }

            function showNewProjectDialog() {
                var options = {
                    title: newProject,
                    template: 'app/desktop/modules/pm/project/new/newProjectDialog.jsp',
                    controller: 'NewProjectController as newProjectVm',
                    resolve: 'app/desktop/modules/pm/project/new/newProjectDialogController',
                    width: 550,
                    showMask: true,
                    data: {
                        projectCreationFrom: "",
                        selectedProgramId: null
                    },
                    buttons: [
                        {text: create, broadcast: 'app.project.new'}
                    ],
                    callback: function (project) {
                        showProject(project);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function editProject(project) {
                var options = {
                    title: EditProject,
                    showMask: true,
                    template: 'app/desktop/modules/pm/project/new/editProjectView.jsp',
                    controller: 'EditProjectController as editProjectVm',
                    resolve: 'app/desktop/modules/pm/project/new/editProjectController',
                    width: 550,
                    data: {
                        projectDetails: project
                    },
                    buttons: [
                        {text: Update, broadcast: 'app.project.edit'}
                    ],
                    callback: function () {
                        loadProjects();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.recentlyVisited = {
                id: null,
                objectId: null,
                objectType: null,
                person: null,
                visitedDate: null
            };

            vm.openProjectDetails = openProjectDetails;
            function openProjectDetails(project) {
                /*var session = JSON.parse(localStorage.getItem('local_storage_login'));
                 $rootScope.loginPersonDetails = session.login;*/
                $state.go('app.pm.project.details', {projectId: project.id, tab: 'details.plan'});
                /*vm.recentlyVisited.objectId = project.id;
                 vm.recentlyVisited.objectType = "PROJECT";
                 vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                 RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                 function (data) {

                 }, function (error) {
                 $rootScope.showErrorMessage(error.message);
                 }
                 )*/
            }

            function freeTextSearch(freeText) {
                searchMode = "freetext";
                $scope.freeTextQuery = freeText;
                vm.searchText = freeText;
                $rootScope.projectFreeTextSearchText = freeText;
                vm.pageable.page = 0;
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    vm.projectFilter.searchQuery = freeText;
                    loadProjects();
                    /*vm.search.searchType = "freetext";
                     vm.search.query = angular.toJson(freeText);
                     ProjectService.freeTextSearch(freeText, vm.pageable).then(
                     function (data) {
                     vm.projects = data;
                     angular.forEach(vm.projects.content, function (project) {
                     project.percentComplete = parseInt(project.percentComplete);
                     });
                     vm.clear = true;
                     vm.loading = false;
                     $rootScope.showSearch = true;
                     $rootScope.searchModeType = true;
                     CommonService.getPersonReferences(vm.projects.content, 'projectManager');
                     }
                     )*/
                }
                else {
                    vm.resetPage();
                    loadProjects();
                }
            }

            function deleteProject(project) {
                var options = {
                    title: deleteProjectTitle,
                    message: deleteProjectDialogMessage + " [ " + project.name + " ] " + projectDelete + "?",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        ProjectService.deleteProject(project.id).then(
                            function (data) {
                                var index = vm.projects.content.indexOf(project);
                                vm.projects.content.splice(index, 1);
                                loadProjectManagers();
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
                        objectType: "PROJECT"
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.select.attributes'}
                    ],
                    callback: function (result) {
                        vm.selectedAttributes = result;
                        $window.localStorage.setItem("allProjectattributes", JSON.stringify(vm.selectedAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesMessage);
                        }
                        loadProjects();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function removeAttribute(att) {
                vm.selectedAttributes.remove(att);
                $window.localStorage.setItem("allProjectattributes", JSON.stringify(vm.selectedAttributes));
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
                    JSON.parse($window.localStorage.getItem("allProjectattributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            function loadProjectAttributeValues() {
                vm.projectIds = [];
                vm.attributeIds = [];
                CommonService.getPersonReferences(vm.projects.content, 'modifiedBy');
                CommonService.getPersonReferences(vm.projects.content, 'createdBy');
                angular.forEach(vm.projects.content, function (item) {
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

                            angular.forEach(vm.projects.content, function (item) {
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

            vm.showProject = showProject;
            function showProject(projectId) {
                $state.go('app.pm.project.details', {projectId: projectId.id, tab: 'details.plan'});
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
            function showComments(project) {
                var options = {
                    title: commentsTitle,
                    template: 'app/desktop/modules/shared/comments/newCommentsView.jsp',
                    controller: 'NewCommentsController as commentsVm',
                    resolve: 'app/desktop/modules/shared/comments/newCommentsController',
                    width: 600,
                    showMask: true,
                    data: {
                        objectType: "PROJECT",
                        objectId: project.id,
                        commentCount: project.comments
                    },
                    callback: function (result) {
                        project.comments = result;
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.setViewType = setViewType;
            function setViewType(type) {
                $window.localStorage.setItem('projects-view-type', type);
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
                    var setAttributes = JSON.parse($window.localStorage.getItem("allProjectattributes"));
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
                                $window.localStorage.setItem("allProjectattributes", "");
                                vm.selectedAttributes = setAttributes
                            } else {
                                vm.selectedAttributes = setAttributes;
                            }
                            loadProjects();

                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }

                else {
                    loadProjects();
                    loadProjectManagers()
                }

                /*if ($rootScope.projectFreeTextSearchText == null) {
                 //loadProjects();
                 } else {
                 freeTextSearch($rootScope.projectFreeTextSearchText);
                 }*/
                var adminViewType = $window.localStorage.getItem("projects-view-type");
                if (adminViewType != null && adminViewType != "" && adminViewType != undefined) {
                    vm.viewType = adminViewType;
                }
                $window.localStorage.setItem("project_open_from", null);
                //$window.localStorage.setItem("lastSelectedProjectTab", JSON.stringify('details.plan'));
                //}
            })();
        }
    }
);


