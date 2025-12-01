/**
 * Created by swapna on 26/12/18.
 */
define(
    [
        'app/desktop/modules/proc/proc.module',
        'app/shared/services/pm/project/projectService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/tm/taskService',
        'app/shared/services/pm/project/wbsService'
    ],
    function (module) {
        module.controller('TaskSelectionController', TaskSelectionController);

        function TaskSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $window, $cookies, $cookieStore,
                                         ProjectService, CommonService, WbsService, TaskService) {

            var vm = this;

            vm.loading = false;
            vm.selectedObj = null;
            var nodeId = 0;
            var wbsTree = null;
            var rootNode = null;
            vm.project = null;
            vm.wbs = null;
            vm.selectRadio = selectRadio;
            vm.radioChange = radioChange;
            vm.selectRadioChange = selectRadioChange;
            vm.loadProjectWBS = loadProjectWBS;
            vm.onSelectWbs = onSelectWbs;

            function loadProjects() {
                vm.clear = false;
                ProjectService.getAllProjects().then(
                    function (data) {
                        vm.projects = data;
                    }
                );
            }

            function loadProjectWBS(project) {
                vm.project = project;
                initWbsTree();
                loadWbsTree();
            }

            function initWbsTree() {
                wbsTree = $('#wbsTree').tree({
                    data: [
                        {
                            id: nodeId,
                            text: 'Wbs',
                            iconCls: 'classification-root',
                            attributes: {
                                itemType: null
                            },
                            children: []
                        }
                    ],
                    onSelect: vm.onSelectWbs
                });

                rootNode = wbsTree.tree('find', 0);

                $(document).click(function () {
                    $("#contextMenu").hide();
                });
            }

            function loadWbsTree() {
                WbsService.getWbsTree(vm.project.id).then(
                    function (data) {
                        var nodes = [];
                        angular.forEach(data, function (wbs) {
                            var node = makeNode(wbs);

                            if (wbs.children != null && wbs.children != undefined && wbs.children.length > 0) {
                                node.state = "closed";
                                visitChildren(node, wbs.children);
                            }

                            nodes.push(node);
                        });
                        rootNode.state = "closed";
                        wbsTree.tree('append', {
                            parent: rootNode.target,
                            data: nodes
                        });
                    }
                );
            }

            function visitChildren(parent, wbss) {
                var nodes = [];
                angular.forEach(wbss, function (wbs) {
                    var node = makeNode(wbs);

                    if (wbs.children != null && wbs.children != undefined && wbs.children.length > 0) {
                        node.state = 'closed';
                        visitChildren(node, wbs.children);
                    }

                    nodes.push(node);
                });

                if (nodes.length > 0) {
                    parent.children = nodes;
                }
            }

            function makeNode(wbs) {
                return {
                    id: ++nodeId,
                    text: wbs.name,
                    iconCls: 'itemtype-node',
                    attributes: {
                        wbs: wbs
                    }
                };
            }

            function onSelectWbs(node) {
                var data = node.attributes.wbs.children;
                if (data.length == 0 || data.length == null || data.length == "") {
                    $rootScope.closeNotification();
                    vm.wbs = node.attributes.wbs;
                    window.$("body").trigger("click");
                }
                else if (data.length != 0 && data.length != null && data.length != "") {
                    $rootScope.showErrorMessage("Please click Children Node");
                }
                loadWBSTasks(vm.wbs);
            }

            function loadWBSTasks(wbs) {
                vm.loading = true;
                TaskService.tasksByWbs(vm.project.id, wbs.id).then(
                    function (data) {
                        vm.taskList = data;
                        if (vm.taskList.length == 0) {
                            $rootScope.showErrorMessage("Selected WBS has no tasks");
                        }
                        else {
                            angular.forEach(vm.taskList, function (task) {
                                task.checked = false;
                            });
                            vm.loading = false;
                        }
                        CommonService.getPersonReferences(vm.taskList, 'person');
                    })
            }

            function selectRadioChange(task, $event) {
                radioChange(task, $event);
                selectRadio();
            }

            function radioChange(task, $event) {
                $event.stopPropagation();
                if (vm.selectedObj === task) {
                    task.checked = false;
                    vm.selectedObj = null
                } else {
                    vm.selectedObj = task;
                }
            }

            function selectRadio() {
                if (vm.selectedObj != null) {
                    $rootScope.hideSidePanel('left');
                    $scope.callback(vm.selectedObj);
                }

                if (vm.selectedObj == null) {
                    $rootScope.showWarningMessage("Please select task");
                }
            }

            (function () {
                $rootScope.$on('app.task.selected', selectRadio);
                loadProjects();
            })();
        }
    }
)
;
