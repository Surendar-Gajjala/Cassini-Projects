define(
    [
        'app/desktop/modules/req/req.module',
        'ckeditor',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/requirementService',
        'app/desktop/modules/directives/reqObjectTypeDirective',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module, InlineEditor) {
        module.controller('RequirementsController', RequirementsController);

        function RequirementsController($scope, $sce, $rootScope, $timeout, $state, $stateParams, $cookies, $window,
                                        DialogService, $translate, CommonService, RequirementService) {
            var vm = this;

            vm.loading = true;
            vm.reqDocId = $stateParams.reqId;

            var nodeId = 0;
            var requirementsTree = null;
            var rootNode = null;

            var parsed = angular.element("<div></div>");
            var create = parsed.html($translate.instant("CREATE")).html();
            var newReqHeading = parsed.html($translate.instant("NEW_REQUIREMENT")).html();
            var commentsTitle = parsed.html($translate.instant("CONVERSATION")).html();
            var reviewersTitle = parsed.html($translate.instant("REVIEWERS")).html();
            var reqReviewersTitle = parsed.html($translate.instant("REQ_REVIEWERS")).html();
            $scope.cannotDeleteApprovedReq = parsed.html($translate.instant("CANNOT_DELETE_APPROVED_REQ")).html();

            vm.requirements = [];

            vm.loading = true;
            vm.reqsTree = [];

            var prevEditorElem = null;
            var previousEditor = null;
            vm.initCkeditor = initCkeditor;
            function initCkeditor(event, id, requirement) {
                $timeout(function () {
                    if (prevEditorElem != null && $.contains(prevEditorElem, event.target)) {
                        return;
                    }

                    if (prevEditorElem != null) {
                        prevEditorElem.contenteditable = false;
                    }

                    if (previousEditor != null) {
                        previousEditor.destroy();
                    }


                    if ($(event.target).hasClass('.req-desc')) {
                        prevEditorElem = event.target;
                    }
                    else {
                        prevEditorElem = $(event.target).closest('.req-desc')[0];
                    }

                    InlineEditor.create(prevEditorElem, {
                        toolbar: {
                            items: [
                                'heading',
                                '|',
                                'bold',
                                'italic',
                                'underline',
                                'subscript',
                                'superscript',
                                'fontBackgroundColor',
                                'fontColor',
                                'fontSize',
                                'fontFamily',
                                'highlight',
                                'link',
                                'bulletedList',
                                'numberedList',
                                '|',
                                'indent',
                                'outdent',
                                '|',
                                'imageUpload',
                                'blockQuote',
                                'insertTable',
                                'mediaEmbed',
                                'undo',
                                'redo',
                                'imageInsert'
                            ]
                        },
                        language: 'en',
                        image: {
                            toolbar: [
                                'imageTextAlternative',
                                'imageStyle:full',
                                'imageStyle:side',
                                'linkImage'
                            ]
                        },
                        table: {
                            contentToolbar: [
                                'tableColumn',
                                'tableRow',
                                'mergeTableCells',
                                'tableCellProperties',
                                'tableProperties'
                            ]
                        },
                        removePlugins: ['Title'],
                        licenseKey: ''

                    }).then(function (editor) {
                        editor.config.title = false;
                        previousEditor = editor;
                        detectFocusOut(editor, id, requirement);
                    });
                    $timeout(function () {
                        prevEditorElem.focus();
                    });
                }, 500);
            }

            function detectFocusOut(editor, id, requirement) {
                editor.ui.focusTracker.on('change:isFocused', function (evt, name, isFocused) {
                    if (!isFocused) {
                        if (prevEditorElem != null) {
                            prevEditorElem.contenteditable = false;
                            prevEditorElem = null;

                            saveRequirement(id, requirement);
                        }

                        if (previousEditor != null) {
                            previousEditor.destroy();
                            previousEditor = null;
                        }
                    }
                });
            }

            function updateEditorMaxWidth() {
                var width = $('#descriptionColumn').width();
                var style = $('<style>.req-desc.ck.ck-content.ck-editor__editable_inline { max-width: {0}px !important;}</style>'.format(width));
                $('html > head').append(style);
            }


            $rootScope.initRequirementsTree = initRequirementsTree;
            function initRequirementsTree() {
                nodeId = 0;
                requirementsTree = $('#requirementsTree').tree({
                    data: [
                        {
                            id: nodeId,
                            text: "{0} {1}".format($rootScope.reqDocumentRevision.master.number, $rootScope.reqDocumentRevision.name),
                            iconCls: 'requirements-root',
                            attributes: {
                                reqObject: null,
                                nodeType: 'ROOT'
                            },
                            children: []
                        }
                    ],
                    onSelect: onSelectType
                });

                rootNode = requirementsTree.tree('find', 0);

                processRequirements();
            }

            function traverseNode(parent, req) {
                var nodes = [];
                var children = req.children;
                if (children != null && children !== undefined) {
                    angular.forEach(children, function (child) {
                        vm.reqVersions.push(child.requirementVersion);
                        vm.requirements.push(child);
                        var node = makeNode(child);
                        child.treeNodeId = node.id;
                        traverseNode(node, child);
                        nodes.push(node);
                    });
                }

                if (nodes.length > 0) {
                    parent.children = nodes;
                }
            }

            function makeNode(req) {
                return {
                    id: ++nodeId,
                    text: "{0} {1} {2}".format(req.requirementVersion.master.path, req.requirementVersion.master.number, req.requirementVersion.name),
                    iconCls: 'requirements-node',
                    attributes: {
                        reqObject: req,
                        nodeType: 'REQUIREMENT'
                    }
                };
            }


            function processRequirements() {
                vm.reqVersions = [];
                vm.reqsTree = [];
                vm.requirements = [];
                RequirementService.getRequirementChildrenTree(vm.reqDocId).then(
                    function (data) {
                        vm.reqsTree = data;
                        var nodes = [];
                        angular.forEach(vm.reqsTree, function (req) {
                            vm.reqVersions.push(req.requirementVersion);
                            req.requirementVersion.descriptionCopy = req.requirementVersion.description;
                            vm.requirements.push(req);
                            var node = makeNode(req);
                            req.treeNodeId = node.id;
                            traverseNode(node, req);
                            nodes.push(node);

                            requirementsTree.tree('append', {
                                parent: rootNode.target,
                                data: node
                            });
                        });

                        CommonService.getPersonReferences(vm.reqVersions, "assignedTo");

                        vm.loading = false;

                        $timeout(function () {
                            preventEnterKey();
                        }, 2000);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )

            }

            vm.destroyContentEditable = destroyContentEditable;
            function destroyContentEditable(event, id, req) {
                event.target.contentEditable = false;

                var node = requirementsTree.tree('find', req.treeNodeId);
                if (node != null) {
                    requirementsTree.tree('update', {
                        target: node.target,
                        text: "{0} {1} {2}".format(req.master.path, req.master.number, req.name)
                    });
                }

                saveRequirement(id, req);
            }

            vm.preventEnterKey = preventEnterKey;
            function preventEnterKey(event, req) {
                if (event != null && event != undefined) {
                    if (event.keyCode == 10 || event.keyCode == 13) {
                        event.preventDefault();
                        $timeout(function () {
                            event.target.blur();
                        }, 200);
                    }
                }
            }

            vm.newRequirement = newRequirement;
            function newRequirement(parent, parentObject) {
                var options = {
                    title: newReqHeading,
                    template: 'app/desktop/modules/req/reqdocs/new/newRequirementView.jsp',
                    controller: 'NewRequirementController as newReqVm',
                    resolve: 'app/desktop/modules/req/reqdocs/new/newRequirementController',
                    width: 600,
                    showMask: true,
                    data: {
                        reqDoc: $scope.reqDocumentRevision,
                        parent: parentObject
                    },
                    buttons: [
                        {text: create, broadcast: 'app.requirement.new'}
                    ],
                    callback: function (result) {
                        insertRequirement(parent, result);
                        $rootScope.loadReqDocumentTabCounts();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function insertRequirement(parent, newReq) {
                var node = makeNode(newReq);
                newReq.requirementVersion.descriptionCopy = newReq.requirementVersion.description;
                vm.reqVersions.push(newReq.requirementVersion);
                if (parent == null) {
                    vm.requirements.push(newReq);
                    requirementsTree.tree('append', {
                        parent: rootNode.target,
                        data: node
                    });
                    // CommonService.getPersonReferences(vm.reqVersions, "assignedTo");
                }
                else {
                    var index = findIndexForNewReq(parent);
                    vm.requirements.splice(index + 1, 0, newReq);
                    parent.children.push(newReq);
                    var parentNode = requirementsTree.tree('find', parent.treeNodeId);
                    if (parentNode != null) {
                        requirementsTree.tree('append', {
                            parent: parentNode.target,
                            data: node
                        });
                    }
                }
                CommonService.getPersonReferences(vm.reqVersions, "assignedTo");

                newReq.treeNodeId = node.id;
            }

            function findIndexForNewReq(parent) {
                var index = -1;
                var children = parent.children;
                if (children.length > 0) {
                    var lastChild = children[children.length - 1];
                    index = indexOfById(lastChild);
                }
                else {
                    index = indexOfById(parent);
                }

                return index;
            }

            function indexOfById(req) {
                var index = -1;
                for (var i = 0; i < vm.requirements.length; i++) {
                    var r = vm.requirements[i];
                    if (r.id === req.id) {
                        index = i;
                        break;
                    }
                }
                return index;
            }

            var deleteTitle = parsed.html($translate.instant("DELETE_REQ")).html();
            var deleteMessage = parsed.html($translate.instant("DELETED_REQ_MESSAGE")).html();
            var deleteSuccessMessage = parsed.html($translate.instant("REQUIREMENT_DELETE_MSG")).html();

            vm.deleteRequirement = deleteRequirement;
            function deleteRequirement(req) {
                var options = {
                    title: deleteTitle,
                    message: deleteMessage,
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        RequirementService.deleteRequirementDocChildren(req.id).then(
                            function (data) {
                                deleteAllTreeNodes();
                                vm.reqsTree = [];
                                vm.requirements = [];
                                processRequirements();
                                $rootScope.loadReqDocumentTabCounts();
                                $rootScope.hideBusyIndicator();
                                $rootScope.showSuccessMessage(deleteSuccessMessage);
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                });
            }


            function deleteAllTreeNodes() {
                var children = requirementsTree.tree('getChildren', rootNode.target);
                for (var i = children.length - 1; i >= 0; i--) {
                    requirementsTree.tree('remove', children[i].target);
                }
            }

            vm.saveRequirement = saveRequirement;
            function saveRequirement(reqId, req) {
                RequirementService.updateRequirementVersion(reqId, req).then(
                    function (data) {

                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function validateReq(req) {
                var valid = true;
                if (req.description != null) {
                    var text = req.description.replace(/(<([^>]+)>)/g, "");
                    if (text == null || text == "" || text == undefined) {
                        valid = false;
                        req.description = req.descriptionCopy;
                        $rootScope.showWarningMessage("Description cannot be empty")
                    }
                }
                return valid;
            }

            vm.showRequirementDetails = showRequirementDetails;
            function showRequirementDetails(req) {
                $state.go('app.req.requirements.details', {
                    requirementId: req.id,
                    tab: 'details.basic'
                });
            }

            vm.onSelectRequirementType = onSelectRequirementType;
            function onSelectRequirementType(requirementType) {
                searchRequirements(requirementType.name);
                vm.selectedRequirementType = requirementType;
                vm.filters.type = requirementType.id;
            }

            vm.clearTypeSelection = clearTypeSelection;
            function clearTypeSelection() {
                vm.selectedRequirementType = null;
                vm.filters.type = '';
                searchRequirements(null);
            }

            function onSelectType(node) {
                var data = requirementsTree.tree('getData', node.target);
                var reqObject = data.attributes.reqObject;
                if (reqObject != null) {
                    var id = reqObject.id;
                    var elem = document.getElementById("" + id);
                    var parent = document.getElementById("requirementsTable");
                    if (elem != null) {
                        scrollIfNeeded(elem, parent);
                    }
                }
            }

            function scrollIfNeeded(element, container) {
                if (element.offsetTop < container.scrollTop) {
                    container.scrollTop = element.offsetTop - 30;
                } else {
                    var offsetBottom = element.offsetTop + element.offsetHeight;
                    var scrollBottom = container.scrollTop + container.offsetHeight;
                    if (offsetBottom > scrollBottom) {
                        container.scrollTop = offsetBottom - container.offsetHeight + 200;
                    }
                }
            }

            vm.filters = {
                requirementDocument: vm.reqDocId,
                type: '',
                phase: null,
                priority: null,
                assignedTo: '',
                searchQuery: null
            };

            function searchRequirements(freeText) {
                vm.filters.searchQuery = freeText;
                $rootScope.freeTextQuerys = freeText;
                vm.reqVersions = [];
                RequirementService.searchRequirementObjects(vm.filters).then(
                    function (data) {
                        vm.requirements = data;
                        angular.forEach(vm.requirements, function (req) {
                            vm.reqVersions.push(req.requirementVersion);
                        });
                        CommonService.getPersonReferences(vm.reqVersions, "assignedTo");
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.onMouseLeave = onMouseLeave;
            function onMouseLeave() {
                $('body').click();
            }

            $scope.assignedTo = [];
            function loadAssignedTo() {
                RequirementService.getAssignedTo(vm.reqDocId).then(
                    function (data) {
                        $scope.assignedTo = data;
                    }
                )
            }

            $scope.selectedPerson = null;
            $scope.onSelectAssignedTo = onSelectAssignedTo;
            function onSelectAssignedTo(person) {
                $scope.selectedPerson = person;
                vm.filters.assignedTo = person.id;
                searchRequirements(null);
            }

            $scope.clearAssignedTo = clearAssignedTo;
            function clearAssignedTo() {
                $rootScope.showBusyIndicator($('.view-container'));
                $scope.selectedPerson = null;
                vm.filters.assignedTo = '';
                searchRequirements(null);
                $rootScope.hideBusyIndicator();
            }


            $scope.priority = [];
            function loadPriority() {
                RequirementService.getPriorities(vm.reqDocId).then(
                    function (data) {
                        $scope.priority = data;
                    }
                )
            }

            $scope.selectedPriority = null;
            $scope.onSelectPriority = onSelectPriority;
            function onSelectPriority(priority) {
                $scope.selectedPriority = priority;
                vm.filters.priority = priority;
                searchRequirements(null);
            }

            $scope.clearPriority = clearPriority;
            function clearPriority() {
                $rootScope.showBusyIndicator($('.view-container'));
                $scope.selectedPriority = null;
                vm.filters.priority = '';
                searchRequirements(null);
                $rootScope.hideBusyIndicator();
            }

            vm.lifecyclePhases = [];
            function loadReqTypeLifecycles() {
                RequirementService.getLifecyclePhases(vm.reqDocId).then(
                    function (data) {
                        vm.reqTypeLifecycles = data;
                        angular.forEach(vm.reqTypeLifecycles, function (lifecycle) {
                            vm.lifecyclePhases.push(lifecycle.phase);
                        })
                    })
            }

            vm.selectedPhase = null;
            vm.onSelectPhase = onSelectPhase;
            function onSelectPhase(phase) {
                vm.selectedPhase = phase;
                vm.filters.phase = phase;
                searchRequirements(null);
            }


            vm.clearPhase = clearPhase;
            function clearPhase() {
                $rootScope.showBusyIndicator($('.view-container'));
                vm.selectedPhase = null;
                vm.filters.phase = null;
                searchRequirements(null);
                $rootScope.hideBusyIndicator();
            }

            vm.showComments = showComments;
            function showComments(requirement) {
                var options = {
                    title: commentsTitle,
                    template: 'app/desktop/modules/shared/comments/newCommentsView.jsp',
                    controller: 'NewCommentsController as commentsVm',
                    resolve: 'app/desktop/modules/shared/comments/newCommentsController',
                    width: 600,
                    showMask: true,
                    data: {
                        objectType: 'REQUIREMENT',
                        objectId: requirement.id,
                        updateCount: false
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.showReviewers = showReviewers;
            function showReviewers(requirement) {
                var options = {
                    title: reqReviewersTitle,
                    template: 'app/desktop/modules/req/reqdocs/details/tabs/requirements/reviewersView.jsp',
                    controller: 'ReviewersController as reviewersVm',
                    resolve: 'app/desktop/modules/req/reqdocs/details/tabs/requirements/reviewersController',
                    width: 400,
                    showMask: true,
                    data: {
                        requirement: requirement,
                        reqDoc: vm.reqDocId,
                        type: 'REQUIREMENT'
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function clearSearchResults() {
                vm.requirements = [];
                angular.forEach(vm.reqsTree, function (req) {
                    vm.requirements.push(req);
                    traverseChildren(req);
                });
            }

            function traverseChildren(req) {
                var children = req.children;
                if (children != null && children !== undefined) {
                    angular.forEach(children, function (child) {
                        vm.requirements.push(child);
                        traverseChildren(child);
                    });
                }
            }

            vm.ignoreRequirement = ignoreRequirement;
            function ignoreRequirement(requirement) {
                requirement.ignoreForRelease = true;
                RequirementService.updateRequirement(requirement).then(
                    function (data) {
                        $rootScope.showSuccessMessage("Requirement ignored successfully");
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            var versionHistoryTitle = $translate.instant("VERSION_HISTORY");
            vm.showReqVersionHistory = showReqVersionHistory;
            function showReqVersionHistory(reqVersion) {
                var options = {
                    title: reqVersion.master.number + " - " + versionHistoryTitle,
                    template: 'app/desktop/modules/item/details/tabs/basic/itemRevisionHistoryView.jsp',
                    controller: 'ItemRevisionHistoryController as revHistoryVm',
                    resolve: 'app/desktop/modules/item/details/tabs/basic/itemRevisionHistoryController',
                    data: {
                        itemId: reqVersion.master.id,
                        revisionHistoryType: "REQUIREMENTVERSION"
                    },
                    width: 700,
                    showMask: true
                };

                $rootScope.showSidePanel(options);
            }

            (function () {
                $scope.$on('app.req.document.tabActivated', function (event, data) {
                    if (data.tabId === 'details.requirements') {
                        $timeout(function () {
                            initRequirementsTree();
                            updateEditorMaxWidth();
                            loadAssignedTo();
                            loadPriority();
                            loadReqTypeLifecycles();
                        }, 2000)
                    }
                });

                $scope.$on('app.details.requirements.search', function (event, data) {
                    searchRequirements(data.freeText);
                });

                $scope.$on('app.details.requirements.clearsearch', function (event, data) {
                    clearSearchResults();
                });

            })();
        }
    }
);