define(['app/desktop/modules/rm/rm.module',
        'split-pane',
        'jquery.easyui',
        'app/shared/services/core/classificationService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/requirementsTypeService',
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
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService'

    ],
    function (module) {
        module.controller('SpecificationElementsController', SpecificationElementsController);

        function SpecificationElementsController($q, $scope, $rootScope, $timeout, $state, DialogService, $stateParams, $cookies, $window, $sce,
                                                 ClassificationService, $translate, CommonService, SpecificationsService, ItemTypeService, ItemService, DialogService, FolderService, ObjectTypeAttributeService,
                                                 AttributeAttachmentService, ECOService, WorkflowDefinitionService, MfrService, MfrPartsService,
                                                 ItemFileService, RecentlyVisitedService, RequirementsTypeService, LoginService, LovService) {
            
            var vm = this;
            var parsed = angular.element("<div></div>");
            vm.autoNumbers = [];
            vm.type = null;
            var nodeId = 0;
            var classificationTree = null;
            var rootNode = null;
            vm.selectedAttribute = [];
            vm.selectedObjectAttributes = [];
            vm.objectIds = [];
            vm.itemIds = [];
            vm.attributeIds = [];
            var currencyMap = new Hashtable();
            vm.createNewRequirement = createNewRequirement;
            var specId = $stateParams.specId;
            var pageable = {
                page: 0,
                size: 30,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            $scope.searchTitle = parsed.html($translate.instant("SEARCH")).html();
            var requirementsTitle = parsed.html($translate.instant("REQUIREMENTS")).html();
            var addTypeText = parsed.html($translate.instant("ADD_TYPE")).html();
            var create = parsed.html($translate.instant("CREATE")).html();
            var newReqTitle = parsed.html($translate.instant("NEW_REQUIREMENT")).html();
            var attributesTitle = $translate.instant("ATTRIBUTES");
            var addButton = parsed.html($translate.instant("ADD")).html();
            var selectedAttributesMessage = parsed.html($translate.instant("SELECTED_ATTRIBUTES_MESSAGE")).html();
            var revisionHistory = parsed.html($translate.instant("ITEM_DETAILS_REVISION_HISTORY")).html();
            var versionHistory = parsed.html($translate.instant("VERSION_HISTORY")).html();
            var requirementEditHistory = parsed.html($translate.instant("REQUIREMENT_EDIT_HISTORY")).html();
            var statusUpdateMsg = parsed.html($translate.instant("STATUS_UPDATE_MSG")).html();
            vm.editRequirementTitle = parsed.html($translate.instant("EDIT_REQUIREMENT_TITLE")).html();
            vm.editSectioTitle = parsed.html($translate.instant("EDIT_REQUIREMENT_TITLE")).html();
            vm.showRevisionHistoryTitle = parsed.html($translate.instant("SHOW_REVISION_HISTORY")).html();
            vm.showEditHistoryTitle = parsed.html($translate.instant("SHOW_REQUIREMENT_EDITHISTORY")).html();
            vm.showPromoteTitle = parsed.html($translate.instant("SHOW_PROMOTE_TITLE")).html();
            vm.showDemoteTitle = parsed.html($translate.instant("SHOW_DEMOTE_TITLE")).html();
            var requirementUpdated = parsed.html($translate.instant("REQUIREMENT_UPDATE")).html();
            var sectionUpdated = parsed.html($translate.instant("SECTION_UPDATE")).html();
            vm.deleteTitle = parsed.html($translate.instant("DELETE_TITLE")).html();
            var editSection = parsed.html($translate.instant("EDIT_SECTION")).html();
            var sidePanelTitle = $translate.instant("COMPARE");

            var deleteSpecSectionDialogTitle = parsed.html($translate.instant("REMOVE_SECTION")).html();
            var deleteSpecSectionDialogMessage = parsed.html($translate.instant("DELETE_SECTION_DIALOG_MESSAGE")).html();
            var specSectionDeletedMessage = parsed.html($translate.instant("SECTION_DELETE_MSG")).html();
            var deleteSpecReqDialogTitle = parsed.html($translate.instant("REMOVE_REQUIREMENT")).html();
            var deleteSpecReqDialogMessage = parsed.html($translate.instant("DELETE_REQUIREMENT_DIALOG_MESSAGE")).html();
            var specReqDeletedMessage = parsed.html($translate.instant("REQUIREMENT_DELETE_MSG")).html();
            var searchValidation = parsed.html($translate.instant("SEARCH_MESSAGE_VALIDATION")).html();

            $scope.searchTitle = parsed.html($translate.instant("SEARCH")).html();
            $scope.clearTitle = parsed.html($translate.instant("CLEAR")).html();
            $scope.clearTitleSearch = parsed.html($translate.instant("CLEAR_SEARCH")).html();

            /*-------  Initialize SpecRequirementsTree ----------*/
            function initClassificationTree() {
                nodeId = 0;
                classificationTree = $('#classificationTree').tree({
                    data: [
                        {
                            id: nodeId,
                            text: requirementsTitle,
                            iconCls: 'requirements-node',
                            state: open,
                            attributes: {
                                typeObject: null,
                                nodeType: 'ROOT'
                            },
                            children: []
                        }
                    ],
                    onContextMenu: onContextMenu,
                    onSelect: onSelectType
                });

                rootNode = classificationTree.tree('find', 0);

                $(document).click(function () {
                    $("#contextMenu").hide();
                });

            }

            /* ----------  classificationTree editable disable ----------------*/
            $('#classificationTree').tree({
                onBeforeSelect: function (node) {
                    var selected = $(this).tree('getSelected');
                    if (selected && selected.target == node.target) {
                        return false;
                    }
                }
            })

            /* -------------------  FreeText Logic ----------------*/

            var pageable = {
                page: 0,
                size: 25,
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
                size: pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.selectedSpecElements = angular.copy(pagedResults);

            function nextPage() {
                if (vm.selectedSpecElements.last != true) {
                    pageable.page++;
                    loadSpecRequirements();
                }
            }

            function previousPage() {
                if (vm.selectedSpecElements.first != true) {
                    pageable.page--;
                    loadSpecRequirements();
                }
            }

            /* ----------------------  Load SpecRequirements By OnSelect on Tree --------------*/
            var nodes = [];

            function loadClassificationTree() {
                SpecificationsService.getAllSpecSections(specId).then(
                    function (data) {
                        nodes = [];
                        angular.forEach(data, function (item) {
                            var node = makeNode(item);
                            if (item.children != null && item.children != undefined && item.children.length > 0) {
                                node.state = "open";
                                visitChildren(node, item.children);
                            }
                            nodes.push(node);
                        });

                        classificationTree.tree('append', {
                            parent: rootNode.target,
                            data: nodes
                        });

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )

            }

            /* ---------------------- Load SpecRequirements -------------- */
            function loadSpecRequirements() {

                $rootScope.sessionInSelectedNode = $application.sessionDataStorage.get("selectedNode");
                if ($rootScope.sessionInSelectedNode != null) {
                    vm.selectedNode = $rootScope.sessionInSelectedNode;
                }

                SpecificationsService.getAllSpecRequirements(vm.selectedNode.attributes.typeObject.id, pageable).then(
                    function (data) {
                        vm.selectedSpecElements = data;
                        CommonService.getPersonReferences(vm.selectedSpecElements.content, 'modifiedBy');
                        CommonService.getPersonReferences(vm.selectedSpecElements.content, 'createdBy');
                        loadItemAttributeValues();
                        $application.sessionDataStorage.put(specId + "requirements", vm.selectedSpecElements.content);

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    })
            }

            vm.selectedSpecElements.content = [];
            vm.selectedNodeType = [];
            vm.selectedNode = null;
            $rootScope.sessionInSelectedNode = null;
            function onSelectType(node) {
                $rootScope.sessionInSelectedNode = null;
                $rootScope.sessionInSelectedNode = $application.sessionDataStorage.put("selectedNode", node);
                if (node.attributes.typeObject != null) {
                    $rootScope.requirementFreeTextSearchFilter = null;
                    $rootScope.requirementFreeTextSearchFilterExist = false;
                    vm.reqSearchFilter.searchQuery = null;
                    if (vm.selectedNode != node) {
                        vm.searchText = "";
                        vm.selectedSpecElements = [];
                        vm.selectedNodeType = [];
                        vm.selectedNode = node;
                        pageable.page = 0;
                        loadSpecRequirements();

                    }
                }
            }

            function onContextMenu(e, node) {
                vm.selectedAddItemNode = node;
                e.preventDefault();
                var parent = classificationTree.tree('getParent', node.target);
                var $contextMenu = $("#contextMenu");
                $contextMenu.show();
                $('#addType').show();
                $('#deleteType').show();
                if (parent === null) {
                    $('#addType').show();
                    $contextMenu.show();
                } else if (parent.id === 0) {
                    $('#addType').show();
                } else {
                    $('#addType').show();
                }

                $contextMenu.css({
                    left: e.pageX,
                    top: e.pageY
                });

                $contextMenu.on("click", "a", function () {
                    $contextMenu.hide();
                });

                if (node.attributes.item != undefined) {
                    $('#addType').hide();
                    $contextMenu.hide();
                }
            }

            /* ----------- create Requirement ------------*/

            function createNewRequirement() {
                vm.selectedNode = classificationTree.tree('getSelected');
                var reqParent = null;
                if (vm.selectedNode.attributes.typeObject != null) {
                    reqParent = vm.selectedNode.attributes.typeObject.id;
                }
                else {
                    reqParent = null;
                }
                var options = {
                    title: newReqTitle,
                    template: 'app/desktop/modules/rm/specification/details/tabs/items/newRequirementView.jsp',
                    controller: 'NewRequirementsController as newReqVm',
                    resolve: 'app/desktop/modules/rm/specification/details/tabs/items/newRequirementController',
                    width: 600,
                    showMask: true,
                    data: {
                        specSection: reqParent
                    },
                    buttons: [
                        {text: create, broadcast: 'app.spec.requirement.new'}
                    ],
                    callback: function (data) {
                        $application.sessionDataStorage.put(specId + "requirements", "");
                        var nodeid = ++nodeId;
                        var nodes = [];
                        var node = makeNode(data);
                        data.children = [];
                        /*data.children.push(data.requirement);*/
                        nodes.push(node);
                        classificationTree.tree('append', {
                            parent: vm.selectedNode.target,
                            data: nodes
                        });

                        //vm.selectedSpecElements.content.push(data);
                        loadSpecRequirements();
                        $application.sessionDataStorage.put(specId + "requirements", vm.selectedSpecElements.content);
                        loadItemAttributeValues();
                        $rootScope.loadSpecification();

                    }
                };

                $rootScope.showSidePanel(options);

            }

            function visitChildren(parent, itemTypes) {
                var nodes = [];
                angular.forEach(itemTypes, function (itemType) {
                    if (parent.attributes.nodeType == 'REQUIREMENT') {
                        var node = makeNode(itemType);
                    }

                    if (itemType.children != null && itemType.children != undefined && itemType.children.length > 0) {
                        node.state = 'open';
                        visitChildren(node, itemType.children);
                    }

                    nodes.push(node);
                });

                if (nodes.length > 0) {
                    parent.children = nodes;
                }
            }

            function makeNode(specElement) {
                return {
                    id: ++nodeId,
                    text: specElement.seqNumber + " " + " " + specElement.requirement.name,
                    iconCls: 'requirements-node',
                    state: 'open',
                    attributes: {
                        typeObject: specElement,
                        nodeType: 'REQUIREMENT'
                    }
                };
            }

            /* ------------- Collapse and Expand tree --------------*/

            vm.collapseAll = collapseAll;
            vm.expandAll = expandAll;

            function collapseAll() {
                var node = $('#classificationTree').tree('getSelected');
                if (node) {
                    if (node.attributes.nodeType != "ROOT") {
                        $('#classificationTree').tree('collapseAll', node.target);
                    } else {
                        angular.forEach(node.children, function (child) {
                            $('#classificationTree').tree('collapseAll', child.target);
                        })
                    }

                }
                else {
                    $('#classificationTree').tree('collapseAll');
                }
            }

            function expandAll() {
                var node = $('#classificationTree').tree('getSelected');
                if (node) {
                    $('#classificationTree').tree('expandAll', node.target);
                }
                else {
                    $('#classificationTree').tree('expandAll');
                }
            }

            vm.searchTree = searchTree;
            vm.searchValue = null;
            function searchTree() {
                if (vm.searchValue != null) {
                    $('#classificationTree').tree('expandAll');
                }
                $('#classificationTree').tree('doFilter', vm.searchValue);
            }

            vm.recentlyVisited = {
                id: null,
                objectId: null,
                objectType: null,
                person: null,
                visitedDate: null
            };

            /* --------------- Show RequirementDetails -------------*/
            vm.showRequirementDetails = showRequirementDetails;
            function showRequirementDetails(req) {
                $window.localStorage.setItem("lastSelectedSpecificationTab", JSON.stringify('details.sections'));
                $state.go('app.rm.requirements.details', {requirementId: req.id});
                var session = JSON.parse(localStorage.getItem('local_storage_login'));
                $rootScope.loginPersonDetails = session.login;
                vm.recentlyVisited.objectId = req.id;
                vm.recentlyVisited.objectType = req.objectType;
                vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                    function (data) {

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                     }
                )
            }

            /* ------------ Edit Requirement -------------*/

            vm.editRequirement = editRequirement;
            function editRequirement(specRequirement) {
                var options = {
                    title: "Edit Requirement",
                    showMask: true,
                    template: 'app/desktop/modules/rm/specification/details/tabs/items/editRequirementView.jsp',
                    controller: 'EditRequirementController as editReqVm',
                    resolve: 'app/desktop/modules/rm/specification/details/tabs/items/editRequirementController',
                    width: 700,
                    data: {
                        requirementDetails: specRequirement.requirement
                    },
                    buttons: [
                        {text: "Update", broadcast: 'app.requirement.edit'}
                    ],
                    callback: function (result) {

                        specRequirement.reqEditsLength = result.requirementEditLength;

                        angular.forEach(vm.selectedSpecElements.content, function (specItem) {
                            if (specItem.id == specRequirement.id) {
                                specItem.reqEditsLength = specItem.reqEditsLength;
                            }
                        })

                        $rootScope.showSuccessMessage(requirementUpdated);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            /* ---------- Show Edit History --------*/
            vm.showEditHistory = showEditHistory;
            function showEditHistory(item) {
                var options = {
                    title: requirementEditHistory,
                    template: 'app/desktop/modules/rm/specification/all/requirementVersionHistoryView.jsp',
                    controller: 'RequirementVersionHistoryController as requirementEditHistoryVm',
                    resolve: 'app/desktop/modules/rm/specification/all/requirementVersionHistoryController',
                    width: 700,
                    data: {

                        requirementId: item
                    },
                    callback: function (data) {
                        item.requirement = data.requirements;
                        angular.forEach(vm.selectedSpecElements.content, function (specItem) {
                            if (specItem.id == item.id && data.status == "FINAL") {
                                specItem.requirementsEdit = specItem.requirementsEdit - item.reqEditsLength;
                            }

                        })

                        item.reqEditsLength = 0;

                    }
                };

                $rootScope.showSidePanel(options);
            }

            /*  --------- Show Revision History ----------*/
            vm.showRevisionHistory = showRevisionHistory;
            function showRevisionHistory(requirement) {
                var options = {
                    title: versionHistory,
                    template: 'app/desktop/modules/rm/specification/all/specRequirementRevisionHistoryView.jsp',
                    controller: 'SpecRequirementRevisionHistoryController as requirementRevisionHistoryVm',
                    resolve: 'app/desktop/modules/rm/specification/all/specRequirementRevisionHistoryController',
                    width: 700,
                    data: {
                        requirementId: requirement
                    },
                    callback: function (msg) {

                    }
                };

                $rootScope.showSidePanel(options);
            }

            /* ------- Compare Requirement versions --------*/

            vm.compareVersion = compareVersion;
            function compareVersion(requirement) {
                var options = {
                    title: sidePanelTitle,
                    showMask: true,
                    template: 'app/desktop/modules/rm/specification/details/tabs/items/compareVersionView.jsp',
                    controller: 'CompareVersionController as compareVm',
                    resolve: 'app/desktop/modules/rm/specification/details/tabs/items/compareVersionController',
                    width: 800,
                    data: {
                        selectedReq: requirement
                    }
                };

                $rootScope.showSidePanel(options);
            }

            /* ----------- Load Requirement Versions -----------------------------*/
            $rootScope.loadRequirementVersions = loadRequirementVersions;
            vm.reqVersions = [];
            function loadRequirementVersions() {
                vm.reqVersions = [];
                SpecificationsService.getAllRequirementVersions(specId).then(
                    function (data) {
                        angular.forEach(data, function (rev) {
                            if (rev == 0) {
                                rev = '-';
                            }
                            vm.reqVersions.push(rev);
                        })
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                     }
                )
            }

            /* ---------- Delete SpecTRequirements -------------*/

            $scope.deleteSpecElement = function (item) {
                var options = {
                    title: deleteSpecReqDialogTitle,
                    message: deleteSpecReqDialogMessage + " [ " + item.name + " ] " + " ?",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        SpecificationsService.deleteSpecElement(item.id).then(
                            function (data) {
                                $rootScope.showSuccessMessage(item.requirement.name + specReqDeletedMessage);
                                $rootScope.loadSpecification();
                                var i = vm.selectedSpecElements.content.indexOf(item);
                                if (item.parent != null) {
                                    vm.selectedSpecElements.content.splice(i, 1);
                                    initClassificationTree();
                                    loadClassificationTree();
                                    loadSpecRequirements();
                                }
                                else {
                                    vm.selectedSpecElements.content = [];
                                    initClassificationTree();
                                    loadClassificationTree();
                                    loadSpecRequirements();
                                }
                                //loadSpecSections();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                });

            };

            /* ------------ Attributes Search ----------*/

            vm.searchAttributes = [];
            var emptySearchAttribute = {
                objectTypeAttribute: null,
                text: null,
                longText: null,
                date: null,
                integer: null,
                aBoolean: false,
                aDouble: 0.0,
                list: null,
                time: null,
                currency: null,
                attributeId: [],
                mlistValue: [],
                attributeSearch: true,
                booleanSearch: false,
                doubleSearch: false
            };

            vm.search = search;
            function search(object) {
                var index = vm.reqAttributeFilter.attributeId.indexOf(object.objectTypeAttribute.id);
                if (index == -1) {
                    vm.reqAttributeFilter.attributeId.push(object.objectTypeAttribute.id);
                }
                if (object.text == "" || object.longText == "" || object.integer == "" || object.double == "" || object.list == "" || object.date == "" || object.currency == "") {
                    var index = vm.reqAttributeFilter.attributeId.indexOf(object.objectTypeAttribute.id);
                    vm.reqAttributeFilter.attributeId.splice(index);
                }
            }

            vm.mListValueSelect = mListValueSelect;
            vm.removeMlistValue = removeMlistValue;

            function removeMlistValue(object, select) {
                var index = object.mlistValue.indexOf(select);
                object.mlistValue.splice(index);
            }

            function mListValueSelect(object, select) {
                object.mlistValue.push(select);
                var index = vm.reqAttributeFilter.attributeId.indexOf(object.id);
                if (index == -1) {
                    vm.reqAttributeFilter.attributeId.push(object.id);
                }
            }

            vm.attributeBoolean = attributeBoolean;
            function attributeBoolean(object) {
                if (object.objectTypeAttribute.dataType == 'BOOLEAN') {
                    object.booleanSearch = true;
                    var index = vm.reqAttributeFilter.attributeId.indexOf(object.id);
                    if (index == -1) {
                        vm.reqAttributeFilter.attributeId.push(object.id);
                    }
                }
                if (object.objectTypeAttribute.dataType == 'DOUBLE') {
                    object.doubleSearch = true;
                    var index = vm.reqAttributeFilter.attributeId.indexOf(object.id);
                    if (index == -1) {
                        vm.reqAttributeFilter.attributeId.push(object.id);
                    }
                }
                if (object.objectTypeAttribute.dataType == 'TIME') {
                    var index = vm.reqAttributeFilter.attributeId.indexOf(object.id);
                    if (index == -1) {
                        vm.reqAttributeFilter.attributeId.push(object.id);
                    }
                }

            }

            vm.requirementSearch = false;
            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };

            vm.reqAttributeFilter = {
                specification: '',
                attributeSearch: true,
                searchQuery: null,
                attributeId: [],
                searchAttributes: []
            };

            vm.attributeSearch = attributeSearch;
            function attributeSearch() {
                vm.reqAttributeFilter.searchAttributes = vm.searchAttributes;
                vm.reqAttributeFilter.specification = $stateParams.specId;
                vm.pageMode = "REQUIREMENTS";
                SpecificationsService.getRequirementAttributeSearch(specId, pageable, vm.reqAttributeFilter).then(
                    function (data) {
                        if (data == "") {
                            vm.selectedSpecElements = angular.copy(pagedResults)
                        } else {
                            vm.selectedSpecElements = data;
                            vm.loading = false;
                            loadSearchAttributeValues();
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )

            }

            vm.clearAttFilter = clearAttFilter;
            function clearAttFilter() {
                vm.reqAttributeFilter = {
                    specification: '',
                    attributeSearch: false,
                    searchQuery: null,
                    attributeId: [],
                    searchAttributes: []
                };

                angular.forEach(vm.searchAttributes, function (attribute) {
                    attribute.text = null,
                        attribute.longText = null,
                        attribute.date = null,
                        attribute.integer = null,
                        attribute.aBoolean = false,
                        attribute.aDouble = 0.0,
                        attribute.list = null,
                        attribute.time = null,
                        attribute.currency = null,
                        attribute.attributeId = [],
                        attribute.mlistValue = [],
                        attribute.booleanSearch = false,
                        attribute.doubleSearch = false
                })
            }

            $scope.clear = function ($event, $select) {
                $rootScope.sectiontoggle = $application.sessionDataStorage.get(specId + "requirements");
                if ($rootScope.sectiontoggle != null) {
                    vm.selectedSpecElements.content = $rootScope.sectiontoggle;
                    $select.selected = undefined;
                    //reset search query
                    $select.search = undefined;
                    //focus and open dropdown
                    $select.activate();

                }
                else {
                    vm.selectedSpecElements.content = [];
                    $select.selected = undefined;
                    //reset search query
                    $select.search = undefined;
                    //focus and open dropdown
                    $select.activate();

                }
            };

            vm.clearFilter = clearFilter;
            function clearFilter() {
                vm.searchText = " ";
                vm.reqSearchFilter = {
                    name: null,
                    description: null,
                    assignedTo: [],
                    status: null,
                    objectNumber: null,
                    version: '',
                    version1: '',
                    searchQuery: null,
                    specification: '',
                    plannedFinishdate: null
                };
                vm.pageMode = "REQUIREMENTS";
                $rootScope.requirementFreeTextSearchFilter = null;
                $rootScope.requirementFreeTextSearchFilterExist = false;
                $rootScope.sectiontoggle = $application.sessionDataStorage.get(specId + "requirements");
                if ($rootScope.sectiontoggle != null) {
                    // vm.selectedSpecElements.content = $rootScope.sectiontoggle;
                    loadSpecRequirements();
                    clearAttFilter();
                }
                else {
                    vm.selectedSpecElements.content = [];
                    clearAttFilter();
                }

            }

            vm.searchText = "";
            vm.freeTextSearch = freeTextSearch;
            vm.reqSearchFilter = {
                name: null,
                description: null,
                assignedTo: [],
                status: null,
                objectNumber: null,
                version: '',
                version1: '',
                searchQuery: null,
                specification: '',
                plannedFinishdate: null,
                attributeSearch: false
            };

            vm.reqMode = "REQUIREMENTS";
            function freeTextSearch(mode) {
                if (vm.reqSearchFilter.searchQuery == "") {
                    clearFilter()

                }
                else if (mode == "REQUIREMENTS") {
                    $rootScope.requirementFreeTextSearchFilterExist = true;
                    $rootScope.requirementFreeTextSearchFilter = vm.reqSearchFilter;
                    vm.loading = true;
                    vm.searchText = vm.reqSearchFilter.searchQuery;
                    vm.reqSearchFilter.attributeSearch = false;
                    if (vm.reqSearchFilter.version1 == '-') {
                        vm.reqSearchFilter.version = 0;
                    } else {
                        vm.reqSearchFilter.version = vm.reqSearchFilter.version1
                    }
                    if (vm.reqAttributeFilter.attributeId.length > 0) {
                        vm.reqAttributeFilter.searchAttributes = vm.searchAttributes;
                        vm.reqAttributeFilter.specification = $stateParams.specId;
                        vm.reqAttributeFilter.attributeSearch = true;
                        vm.pageMode = "REQUIREMENTS";
                        SpecificationsService.getRequirementAttributeSearch(specId, pageable, vm.reqAttributeFilter).then(
                            function (data) {
                                if (data == "") {
                                    vm.specRequirements = angular.copy(pagedResults)
                                } else {
                                    //vm.specRequirements = data;
                                    vm.selectedSpecElements = data;
                                    vm.loading = false;
                                    loadSearchAttributeValues();
                                }
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    } else if (vm.reqSearchFilter.name != null || vm.reqSearchFilter.description != null || vm.reqSearchFilter.assignedTo.length > 0 || vm.reqSearchFilter.status != null || vm.reqSearchFilter.objectNumber != null || vm.reqSearchFilter.searchQuery != null || vm.reqSearchFilter.plannedFinishdate != null || vm.reqSearchFilter.version != "" || vm.reqSearchFilter.version1 != "") {
                        vm.pageMode = "REQUIREMENTS";
                        vm.reqSearchFilter.attributeSearch = false;
                        SpecificationsService.getRequirementSearch(specId, pageable, vm.reqSearchFilter).then(
                            function (data) {
                                //  vm.specRequirements = data;
                                vm.selectedSpecElements = data;
                                vm.loading = false;
                                loadSearchAttributeValues();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    } else {
                        $rootScope.showWarningMessage(searchValidation);
                        vm.loading = false;
                        $rootScope.requirementFreeTextSearchFilter = null;
                        $rootScope.requirementFreeTextSearchFilterExist = false;
                        $rootScope.sectiontoggle = $application.sessionDataStorage.get(specId + "requirements");
                        if ($rootScope.sectiontoggle != null) {
                            vm.selectedSpecElements.content = $rootScope.sectiontoggle;
                        }
                    }
                }
            }

            /* --------------- Load SpecRequirement Attributes -------------*/
            function loadSearchAttributeValues() {
                vm.itemIds = [];
                vm.attributeIds = [];
                angular.forEach(vm.selectedSpecElements.content, function (item) {
                    if (item.requirement != undefined && item.requirement.objectType == "REQUIREMENT") {
                        vm.itemIds.push(item.requirement.id);
                    }
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

                            angular.forEach(vm.selectedSpecElements.content, function (item) {

                                if (item.requirement != undefined && item.requirement.objectType == "REQUIREMENT") {
                                    var attributes = [];

                                    var itemAttributes = vm.selectedObjectAttributes[item.requirement.id];
                                    if (itemAttributes != null && itemAttributes != undefined) {
                                        attributes = attributes.concat(itemAttributes);
                                    }
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
                                            } else if (selectatt.dataType == 'INTEGER') {
                                                item[attributeName] = attribute.integerValue;
                                            } else if (selectatt.dataType == 'BOOLEAN') {
                                                item[attributeName] = attribute.booleanValue;
                                            } else if (selectatt.dataType == 'DOUBLE') {
                                                item[attributeName] = attribute.doubleValue;
                                            } else if (selectatt.dataType == 'DATE') {
                                                item[attributeName] = attribute.dateValue;
                                            } else if (selectatt.dataType == 'LIST') {
                                                if (attribute.listValue != null) {
                                                    item[attributeName] = attribute.listValue;
                                                } else if (attribute.mlistValue.length > 0) {
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
                                                                $rootScope.hideBusyIndicator();
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
                                                    }
                                                }
                                            }
                                        }
                                    })

                                }
                            })

                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                }
            }

            vm.searchAttributes = [];
            var emptySearchAttribute = {
                objectTypeAttribute: null,
                text: null,
                longText: null,
                date: null,
                integer: null,
                aBoolean: false,
                aDouble: 0.0,
                list: null,
                time: null,
                currency: null,
                attributeId: [],
                mlistValue: [],
                attributeSearch: true,
                booleanSearch: false,
                doubleSearch: false
            };

            /*-------- Show SpecRequiremente Attributes On Table OverView -------*/

            $rootScope.showReqTypeAttributes = showReqTypeAttributes;
            function showReqTypeAttributes() {
                var selecteAttrs = angular.copy(vm.selectedAttribute);
                var options = {
                    title: attributesTitle,
                    template: 'app/desktop/modules/rm/requirements/reqTypeAttributesView.jsp',
                    resolve: 'app/desktop/modules/rm/requirements/reqTypeAttributesController',
                    controller: 'RequirementTypeAttributesController as reqTypeAttributesVm',
                    width: 500,
                    data: {
                        selectedAttributes: selecteAttrs
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.select.attributes'}
                    ],
                    callback: function (result) {
                        vm.selectedAttribute = result;

                        angular.forEach(vm.selectedAttribute, function (attrbute) {
                            var newSearchAttribute = angular.copy(emptySearchAttribute);
                            newSearchAttribute.objectTypeAttribute = attrbute;
                            vm.searchAttributes.push(newSearchAttribute);
                        })
                        $window.localStorage.setItem(specId + "specattributes", JSON.stringify(vm.selectedAttribute));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesMessage);
                        }

                        loadItemAttributeValues();
                        //loadSpecSections();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            /* ---- Remove PsecRequirements Attributes from on Table OverView ----*/
            vm.removeReqAttribute = removeReqAttribute;
            function removeReqAttribute(att) {
                angular.forEach(vm.searchAttributes, function (attribute) {
                    if (attribute.objectTypeAttribute.id == att.id) {
                        vm.searchAttributes.splice(vm.searchAttributes.indexOf(attribute), 1);
                    }
                })
                vm.selectedAttribute.splice(vm.selectedAttribute.indexOf(att), 1);
                $window.localStorage.setItem(specId + "specattributes", JSON.stringify(vm.selectedAttribute));
            }

            function loadAllLovs() {
                LovService.getAllLovs().then(
                    function (data) {
                        vm.lovValues = [];
                        vm.lovs = data;
                        angular.forEach(vm.lovs, function (lov) {
                            angular.forEach(lov.values, function (value) {
                                vm.lovValues.push(value);
                            })

                        })
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            /* -------------- Load SpecRequirementAttributes -----------*/
            function loadItemAttributeValues() {
                vm.itemIds = [];
                vm.attributeIds = [];
                angular.forEach(vm.selectedSpecElements.content, function (item) {
                    if (item.requirement != undefined && item.requirement.objectType == "REQUIREMENT") {
                        vm.itemIds.push(item.requirement.id);
                    }
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

                            angular.forEach(vm.selectedSpecElements.content, function (item) {

                                if (item.requirement != undefined && item.requirement.objectType == "REQUIREMENT") {
                                    var attributes = [];

                                    var itemAttributes = vm.selectedObjectAttributes[item.requirement.id];
                                    if (itemAttributes != null && itemAttributes != undefined) {
                                        attributes = attributes.concat(itemAttributes);
                                    }
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

                                            } else if (selectatt.dataType == 'INTEGER') {
                                                item[attributeName] = attribute.integerValue;
                                            } else if (selectatt.dataType == 'BOOLEAN') {
                                                item[attributeName] = attribute.booleanValue;
                                            } else if (selectatt.dataType == 'DOUBLE') {
                                                item[attributeName] = attribute.doubleValue;
                                            } else if (selectatt.dataType == 'DATE') {
                                                item[attributeName] = attribute.dateValue;
                                            } else if (selectatt.dataType == 'LIST') {
                                                if (attribute.listValue != null) {
                                                    item[attributeName] = attribute.listValue;
                                                } else if (attribute.mlistValue.length > 0) {
                                                    item[attributeName] = attribute.mlistValue;
                                                }
                                            } else if (selectatt.dataType == 'LIST') {
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
                                                                $rootScope.hideBusyIndicator();
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
                                                    }
                                                }
                                            }
                                        }
                                    })

                                }
                            })

                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                }
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem(specId + "specattributes"));
                    //JSON.parse($window.localStorage.getItem("requirements"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            vm.openAttachment = openAttachment;
            function openAttachment(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/download".
                    format(window.location.protocol, window.location.host,
                    attachment.id);
                window.open(url);
                $timeout(function () {
                    window.close();
                }, 2000);
                //launchUrl(url);
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

            $rootScope.sectiontoggle = [];
            (function () {
                $scope.$on('app.spec.tabactivated', function (event, data) {
                    if (data.tabId == 'details.sections') {
                        loadAllLovs();
                        $('div.split-right-pane').css({left: 300});
                        $('div.split-pane').splitPane();
                        initClassificationTree();
                        loadClassificationTree();
                        angular.forEach($application.currencies, function (data) {
                            currencyMap.put(data.id, $sce.trustAsHtml(data.symbol));
                        });

                        if (validateJSON()) {
                            //Get SessionStorage data
                            $rootScope.sectiontoggle = $application.sessionDataStorage.get(specId + "requirements");
                            var setAttributes = JSON.parse($window.localStorage.getItem(specId + "specattributes"));
                        } else {
                            var setAttributes = null;
                        }

                        if ($rootScope.sectiontoggle != null && $rootScope.requirementFreeTextSearchFilterExist == false) {
                            loadSpecRequirements();

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
                                        vm.searchAttributes = [];
                                        $window.localStorage.setItem(specId + "specattributes", "");
                                        vm.selectedAttribute = setAttributes;
                                        angular.forEach(vm.selectedAttribute, function (attrbute) {
                                            var newSearchAttribute = angular.copy(emptySearchAttribute);
                                            newSearchAttribute.objectTypeAttribute = attrbute;
                                            vm.searchAttributes.push(newSearchAttribute);
                                        })
                                        loadItemAttributeValues();

                                    } else {
                                        vm.searchAttributes = [];
                                        vm.selectedAttribute = setAttributes;
                                        angular.forEach(vm.selectedAttribute, function (attrbute) {
                                            var newSearchAttribute = angular.copy(emptySearchAttribute);
                                            newSearchAttribute.objectTypeAttribute = attrbute;
                                            vm.searchAttributes.push(newSearchAttribute);
                                        })
                                        loadItemAttributeValues();
                                    }

                                }, function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                    $rootScope.hideBusyIndicator();
                                }
                            )
                        }

                        if ($rootScope.requirementFreeTextSearchFilterExist == false) {
                            //vm.selectedSpecElements = $rootScope.sectiontoggle;
                        }
                        if ($rootScope.requirementFreeTextSearchFilterExist == true) {
                            vm.reqSearchFilter = $rootScope.requirementFreeTextSearchFilter;
                            vm.selectedSpecElements.content = [];
                            freeTextSearch("REQUIREMENTS");
                        }

                    }
                });
            })();
        }
    }
)
;


