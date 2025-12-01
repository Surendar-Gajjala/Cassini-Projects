define(
    [
        'app/desktop/modules/reqTemplate/reqDocTemplate.module',
        'ckeditor',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/requirementService',
        'app/shared/services/core/reqDocTemplateService'
    ],
    function (module, InlineEditor) {
        module.controller('ReqDocTemplateRequirementController', ReqDocTemplateRequirementController);

        function ReqDocTemplateRequirementController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window,
                                                     DialogService, $translate, CommonService, RequirementService, ReqDocTemplateService) {
            var vm = this;

            vm.loading = true;
            vm.reqDocId = $stateParams.reqDocId;

            var nodeId = 0;
            var requirementsTree = null;
            var rootNode = null;

            var parsed = angular.element("<div></div>");
            var create = parsed.html($translate.instant("CREATE")).html();
            var newReqHeading = parsed.html($translate.instant("NEW_REQUIREMENT")).html();
            var reqTemplateHeading = parsed.html($translate.instant("REQ_TEMPLATE")).html();
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
            function initCkeditor(event, requirement) {
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
                        detectFocusOut(editor, requirement);
                    });
                    $timeout(function () {
                        prevEditorElem.focus();
                    });
                }, 500);
            }

            function detectFocusOut(editor, requirement) {
                editor.ui.focusTracker.on('change:isFocused', function (evt, name, isFocused) {
                    if (!isFocused) {
                        if (prevEditorElem != null) {
                            prevEditorElem.contenteditable = false;
                            prevEditorElem = null;

                            saveRequirement(requirement);
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
                            text: "{0}".format($rootScope.reqDocTemplate.name),
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
                    text: "{0} {1}".format(req.path, req.name),
                    iconCls: 'requirements-node',
                    attributes: {
                        reqObject: req,
                        nodeType: 'REQUIREMENT'
                    }
                };
            }


            function processRequirements() {
                $rootScope.showBusyIndicator();
                vm.reqsTree = [];
                vm.requirements = [];
                ReqDocTemplateService.getRequirementTemplateTree(vm.reqDocId).then(
                    function (data) {
                        vm.reqsTree = data;
                        var nodes = [];
                        angular.forEach(vm.reqsTree, function (req) {
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

                        CommonService.getPersonReferences(vm.requirements, "assignedTo");

                        vm.loading = false;

                        $timeout(function () {
                            preventEnterKey();
                        }, 2000);
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )

            }

            vm.destroyContentEditable = destroyContentEditable;
            function destroyContentEditable(event, req) {
                event.target.contentEditable = false;

                var node = requirementsTree.tree('find', req.treeNodeId);
                if (node != null) {
                    requirementsTree.tree('update', {
                        target: node.target,
                        text: "{0} {1}".format(req.path, req.name)
                    });
                }

                saveRequirement(req);
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
            function newRequirement(parent) {
                var options = {
                    title: reqTemplateHeading,
                    template: 'app/desktop/modules/reqTemplate/new/newRequirementTemplateView.jsp',
                    controller: 'NewRequirementTemplateController as newReqTemplateVm',
                    resolve: 'app/desktop/modules/reqTemplate/new/newRequirementTemplateController',
                    width: 600,
                    showMask: true,
                    data: {
                        reqDoc: $rootScope.reqDocTemplate,
                        parent: parent
                    },
                    buttons: [
                        {text: create, broadcast: 'app.requirement.template.new'}
                    ],
                    callback: function (result) {
                        insertRequirement(parent, result)
                        //$rootScope.loadReqDocumentTabCounts();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function insertRequirement(parent, newReq) {
                var node = makeNode(newReq);
                if (parent == null) {
                    vm.requirements.push(newReq);
                    requirementsTree.tree('append', {
                        parent: rootNode.target,
                        data: node
                    });
                    CommonService.getPersonReferences(vm.requirements, "assignedTo");
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
                CommonService.getPersonReferences(vm.requirements, "assignedTo");

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
            var deleteSuccessMessage = parsed.html($translate.instant("REQ_TEMPLATE_DELETE")).html();

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
                        ReqDocTemplateService.deleteRequirementTemplate(req.id).then(
                            function (data) {
                                deleteAllTreeNodes();
                                vm.reqsTree = [];
                                vm.requirements = [];
                                processRequirements();
                                //$rootScope.loadReqDocumentTabCounts();
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
            function saveRequirement(req) {
                ReqDocTemplateService.updateRequirementTemplate(req).then(
                    function (data) {

                    },
                    function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.showRequirementDetails = showRequirementDetails;
            function showRequirementDetails(req) {
                $state.go('app.req.template.details', {requirementId: req.id, tab: 'details.basic'});
            }


            function onSelectType(node) {
                var data = requirementsTree.tree('getData', node.target);
                var reqObject = data.attributes.reqObject;
                if (reqObject != null) {
                    var id = reqObject.id;
                    var elem = document.getElementById("" + id);
                    var parent = document.getElementById("requirementsTemplateTable");
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
                searchQuery: null
            };

            function searchRequirements(freeText) {
                vm.filters.searchQuery = freeText;
                $rootScope.freeTextQuerys = freeText;
                RequirementService.searchRequirements(vm.filters).then(
                    function (data) {
                        vm.requirements = data;
                        CommonService.getPersonReferences(vm.requirements, "assignedTo");
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.onMouseLeave = onMouseLeave;
            function onMouseLeave() {
                $('body').click();
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
                        objectType: 'REQUIREMENTTEMPLATE',
                        objectId: requirement.id,
                        updateCount: false
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.showReqTemplateReviewers = showReqTemplateReviewers;
            function showReqTemplateReviewers(requirement) {
                var options = {
                    title: reqReviewersTitle,
                    template: 'app/desktop/modules/req/reqdocs/details/tabs/requirements/reviewersView.jsp',
                    controller: 'ReviewersController as reviewersVm',
                    resolve: 'app/desktop/modules/req/reqdocs/details/tabs/requirements/reviewersController',
                    width: 400,
                    showMask: true,
                    data: {
                        requirement: requirement,
                        type: 'REQUIREMENTTEMPLATE'
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

            (function () {
                $scope.$on('app.req.documentTemplate.tabActivated', function (event, data) {
                    if (data.tabId === 'details.requirements') {
                        $timeout(function () {
                            initRequirementsTree();
                            updateEditorMaxWidth();
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