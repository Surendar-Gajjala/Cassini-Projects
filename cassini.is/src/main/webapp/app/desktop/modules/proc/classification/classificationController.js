define(['app/desktop/modules/proc/proc.module',
        'split-pane',
        'jquery.easyui',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/shared/services/core/classificationService',
        'app/shared/services/core/itemService',
        'app/desktop/modules/proc/classification/details/materialTypeDetailsController',
        'app/desktop/modules/proc/classification/details/manpowerTypeDetailsController',
        'app/desktop/modules/proc/classification/details/machineTypeDetailsController',
        'app/desktop/modules/proc/classification/details/receiveTypeDetailsController',
        'app/desktop/modules/proc/classification/details/issueTypeDetailsController'
    ],
    function (module) {
        module.controller('ClassificationController', ClassificationController);

        function ClassificationController($scope, $rootScope, $timeout, CommonService, ClassificationService, ItemService, DialogService, $q, AutonumberService, $state, $cookies) {

            $rootScope.viewInfo.icon = "fa fa-snowflake-o";
            $rootScope.viewInfo.title = "Classification";
            $rootScope.setViewType('PROCUREMENT');
            var vm = this;

            vm.autoNumbers = [];
            vm.type = null;
            $rootScope.flag = false;
            var currentNode = null;
            vm.addType = addType;
            vm.deleteType = deleteType;
            vm.onSave = onSave;
            vm.checkPermission = checkPermission;

            var nodeId = 0;
            var classificationTree = null;
            var rootNode = null;
            var isExists = isExists;

            var pageable = {
                page: 0,
                size: 30,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            function initClassificationTree() {
                $rootScope.flag = true;
                classificationTree = $('#classificationTree').tree({
                    data: [
                        {
                            id: nodeId,
                            text: 'Classification',
                            iconCls: 'classification-root',
                            attributes: {
                                typeObject: null,
                                nodeType: 'ROOT'
                            },
                            children: []
                        }
                    ],
                    onContextMenu: onContextMenu,
                    onDblClick: onDblClick,
                    onAfterEdit: onAfterEdit,
                    onSelect: onSelectType
                });

                rootNode = classificationTree.tree('find', 0);

                $(document).click(function () {
                    $("#contextMenu").hide();
                });

                loadMaterialClassification();
            }

            function onDblClick(node) {
                if ($rootScope.hasPermission('permission.masterdata.editClassification')) {
                    var data = classificationTree.tree('getData', node.target);
                    if (data.id != 0 && (data.attributes.nodeType != null ||
                        data.attributes.nodeType != undefined) &&
                        node.attributes.root != true) {
                        classificationTree.tree('beginEdit', node.target);

                        $timeout(function () {
                            $('.tree-editor').focus().select();
                        }, 1);
                    }
                }

            }

            vm.selectedNode = null;
            function onAfterEdit(node) {
                var promise = null;
                var deleted = false;
                var parent = classificationTree.tree('getParent', node.target);
                var parentData = classificationTree.tree('getData', parent.target);
                var data = classificationTree.tree('getData', node.target);
                if (data.attributes.typeObject == null) {
                    data.attributes.typeObject = {}
                }

                vm.selectedNode = data.attributes.nodeType;

                if (parentData.id != 0 && parentData.attributes.typeObject != undefined) {
                    data.attributes.typeObject.parentType = parentData.attributes.typeObject.id;
                }

                data.attributes.typeObject.name = node.text;

                if (data.attributes.typeObject.id == undefined || data.attributes.typeObject.id == null) {
                    if (isExists(parent, node)) {
                        classificationTree.tree('remove', node.target);
                        deleted = true;
                    }
                }
                if (promise != null) {
                    promise.then(
                        function (materialType) {
                            data.attributes.typeObject = angular.copy(materialType);
                            classificationTree.tree('update', {target: node.target, attributes: data.attributes});
                            classificationTree.tree('select', node.target);
                        }
                    );
                }
                else if (!deleted) {
                    classificationTree.tree('select', node.target);
                }

            }

            function onSelectType(node) {
                currentNode = node;
                var data = classificationTree.tree('getData', node.target);
                var typeObject = data.attributes.typeObject;
                vm.type = data.attributes.nodeType;
                $scope.$apply();
                if (data.attributes.typeObject != undefined) {
                    if (vm.type == 'MATERIALTYPE') {
                        $scope.$broadcast('app.materialType.selected', {typeObject: typeObject, nodeId: node.id});
                    } else if (vm.type == 'MACHINETYPE') {
                        $scope.$broadcast('app.machineType.selected', {typeObject: typeObject, nodeId: node.id});
                    } else if (vm.type == 'MANPOWERTYPE') {
                        $scope.$broadcast('app.manpowerType.selected', {typeObject: typeObject, nodeId: node.id});
                    } else if (vm.type == 'MATERIALRECEIVETYPE') {
                        $scope.$broadcast('app.receiveType.selected', {typeObject: typeObject, nodeId: node.id});
                    } else if (vm.type == 'MATERIALISSUETYPE') {
                        $scope.$broadcast('app.issueType.selected', {typeObject: typeObject, nodeId: node.id});
                    }
                }
            }

            function onContextMenu(e, node) {
                e.preventDefault();
                var $contextMenu = $("#contextMenu");
                var parent = classificationTree.tree('getData', node.target);
                if ($rootScope.hasPermission('permission.masterdata.addType')) {
                    $('#addType').show();
                }
                if ($rootScope.hasPermission('permission.masterdata.deleteType')) {
                    $('#deleteType').show();
                }
                $contextMenu.show();
                if (node.attributes.root == true) {
                    if ($rootScope.hasPermission('permission.masterdata.addType')) {
                        $('#addType').text('Add Type');
                    }
                    $('#deleteType').hide();
                }
                if (node.attributes.root == undefined && parent.id === 0) {
                    $contextMenu.hide();
                }
                $contextMenu.css({
                    left: e.pageX,
                    top: e.pageY
                });

                $contextMenu.on("click", "a", function () {
                    $contextMenu.hide();
                });
                vm.selectedNode = classificationTree.tree('getSelected');
                var data = classificationTree.tree('getData', vm.selectedNode.target);
                if (data.attributes.typeObject != undefined) {
                    if (data.attributes.typeObject.id == undefined || data.attributes.typeObject == undefined) {
                        $('#addType').hide();
                        $('#deleteType').show();
                    }
                }
            }

            function loadMaterialClassification() {
                var materialRoot = {
                    id: ++nodeId,
                    text: 'Material',
                    iconCls: 'material-node',
                    attributes: {
                        root: true,
                        nodeType: 'MATERIALTYPE'
                    },
                    children: []
                };

                ClassificationService.getClassificationTree('MATERIALTYPE').then(
                    function (data) {
                        if (data.length > 0) {
                            materialRoot.state = 'open'
                        }
                        var nodes = [];
                        angular.forEach(data, function (materialType) {
                            var node = makeMaterialNode(materialType);

                            if (materialType.children != null && materialType.children != undefined && materialType.children.length > 0) {
                                node.state = "open";
                                visitChildren(node, materialType.children);
                            }

                            if (node.attributes.typeObject != null) {
                                nodes.push(node);
                            } else {
                                ClassificationService.deleteType(node.attributes.nodeType, node.attributes.typeObject.id);
                            }
                        });

                        materialRoot.children = nodes;

                        classificationTree.tree('append', {
                            parent: rootNode.target,
                            data: materialRoot
                        });

                        loadMachineClassification();
                    }
                )
            }

            function loadMachineClassification() {
                var machineRoot = {
                    id: ++nodeId,
                    text: 'Machine',
                    iconCls: 'machine-node',
                    attributes: {
                        root: true,
                        nodeType: 'MACHINETYPE'
                    },
                    children: []
                };

                ClassificationService.getClassificationTree('MACHINETYPE').then(
                    function (data) {
                        if (data.length > 0) {
                            machineRoot.state = 'open'
                        }
                        var nodes = [];
                        angular.forEach(data, function (type) {
                            var node = makeMachineNode(type);

                            if (type.children != null && type.children != undefined && type.children.length > 0) {
                                node.state = "open";
                                visitChildren(node, type.children);
                            }

                            if (node.attributes.typeObject != null) {
                                nodes.push(node);
                            } else {
                                ClassificationService.deleteType(node.attributes.nodeType, node.attributes.typeObject.id);
                            }
                        });

                        machineRoot.children = nodes;

                        classificationTree.tree('append', {
                            parent: rootNode.target,
                            data: machineRoot
                        });

                        loadManPowerClassification();
                    }
                )
            }

            function loadManPowerClassification() {
                var manpowerRoot = {
                    id: ++nodeId,
                    text: 'Manpower',
                    iconCls: 'manpower-node',
                    attributes: {
                        root: true,
                        nodeType: 'MANPOWERTYPE'
                    },
                    children: []
                };

                ClassificationService.getClassificationTree('MANPOWERTYPE').then(
                    function (data) {
                        if (data.length > 0) {
                            manpowerRoot.state = 'open'
                        }
                        var nodes = [];
                        angular.forEach(data, function (type) {
                            var node = makeManPowerNode(type);

                            if (type.children != null && type.children != undefined && type.children.length > 0) {
                                node.state = "open";
                                visitChildren(node, type.children);
                            }
                            if (node.attributes.typeObject != null) {
                                nodes.push(node);
                            } else {
                                ClassificationService.deleteType(node.attributes.nodeType, node.attributes.typeObject.id);
                            }
                        });

                        manpowerRoot.children = nodes;

                        classificationTree.tree('append', {
                            parent: rootNode.target,
                            data: manpowerRoot
                        });

                        loadReceiveTypeClassification();
                    }
                )

            }

            function loadReceiveTypeClassification() {
                var receiveTypeRoot = {
                    id: ++nodeId,
                    text: 'Receive Type',
                    iconCls: 'itemtype-node',
                    attributes: {
                        root: true,
                        nodeType: 'MATERIALRECEIVETYPE'
                    },
                    children: []
                };

                ClassificationService.getClassificationTree('MATERIALRECEIVETYPE').then(
                    function (data) {
                        if (data.length > 0) {
                            receiveTypeRoot.state = 'open'
                        }
                        var nodes = [];
                        angular.forEach(data, function (type) {
                            var node = makeNode(type, 'itemtype-node', 'MATERIALRECEIVETYPE');

                            if (type.children != null && type.children != undefined && type.children.length > 0) {
                                node.state = "open";
                                visitChildren(node, type.children);
                            }

                            if (node.attributes.typeObject != null) {
                                nodes.push(node);
                            } else {
                                ClassificationService.deleteType(node.attributes.nodeType, node.attributes.typeObject.id);
                            }
                        });

                        receiveTypeRoot.children = nodes;

                        classificationTree.tree('append', {
                            parent: rootNode.target,
                            data: receiveTypeRoot
                        });

                        loadIssueTypeClassification();
                    }
                )
            }

            function loadIssueTypeClassification() {
                var issueTypeRoot = {
                    id: ++nodeId,
                    text: 'Issue Type',
                    iconCls: 'sitemap-node',
                    attributes: {
                        root: true,
                        nodeType: 'MATERIALISSUETYPE'
                    },
                    children: []
                };

                ClassificationService.getClassificationTree('MATERIALISSUETYPE').then(
                    function (data) {
                        if (data.length > 0) {
                            issueTypeRoot.state = 'open'
                        }
                        var nodes = [];
                        angular.forEach(data, function (type) {
                            var node = makeNode(type, 'sitemap-node', 'MATERIALISSUETYPE');

                            if (type.children != null && type.children != undefined && type.children.length > 0) {
                                node.state = "open";
                                visitChildren(node, type.children);
                            }

                            if (node.attributes.typeObject != null) {
                                nodes.push(node);
                            } else {
                                ClassificationService.deleteType(node.attributes.nodeType, node.attributes.typeObject.id);
                            }
                        });

                        issueTypeRoot.children = nodes;

                        classificationTree.tree('append', {
                            parent: rootNode.target,
                            data: issueTypeRoot
                        });

                    }
                )
            }

            function visitChildren(parent, itemTypes) {
                var nodes = [];
                angular.forEach(itemTypes, function (itemType) {
                    var node = makeMachineNode(itemType);

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

            function visitChildren(parent, itemTypes) {
                var nodes = [];
                angular.forEach(itemTypes, function (itemType) {
                    if (parent.attributes.nodeType == 'MATERIALTYPE') {
                        var node = makeMaterialNode(itemType);
                    } else if (parent.attributes.nodeType == 'MACHINETYPE') {
                        var node = makeMachineNode(itemType);
                    } else if (parent.attributes.nodeType == 'MANPOWERTYPE') {
                        var node = makeManPowerNode(itemType);
                    } else if (parent.attributes.nodeType == 'MATERIALRECEIVETYPE') {
                        var node = makeNode(itemType, 'itemtype-node', 'MATERIALRECEIVETYPE');
                    } else if (parent.attributes.nodeType == 'MATERIALISSUETYPE') {
                        var node = makeNode(itemType, 'sitemap-node', 'MATERIALISSUETYPE');
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

            function makeMaterialNode(materialType) {
                return {
                    id: ++nodeId,
                    text: materialType.name,
                    iconCls: 'material-node',
                    attributes: {
                        typeObject: materialType,
                        nodeType: 'MATERIALTYPE'
                    }
                };
            }

            function makeMachineNode(type) {
                return {
                    id: ++nodeId,
                    text: type.name,
                    iconCls: 'machine-node',
                    attributes: {
                        typeObject: type,
                        nodeType: 'MACHINETYPE'
                    }
                };
            }

            function makeManPowerNode(type) {
                return {
                    id: ++nodeId,
                    text: type.name,
                    iconCls: 'manpower-node',
                    attributes: {
                        typeObject: type,
                        nodeType: 'MANPOWERTYPE'
                    }
                };
            }

            function makeNode(type, icon, nodeType) {
                return {
                    id: ++nodeId,
                    text: type.name,
                    iconCls: icon,
                    attributes: {
                        typeObject: type,
                        nodeType: nodeType
                    }
                };
            }

            function addType() {
                var selectedNode = classificationTree.tree('getSelected');
                if (selectedNode.attributes.nodeType == 'MATERIALTYPE') {
                    addClsType('material-node', 'New Material');
                } else if (selectedNode.attributes.nodeType == 'MACHINETYPE') {
                    addClsType('machine-node', 'New Machine');
                } else if (selectedNode.attributes.nodeType == 'MANPOWERTYPE') {
                    addClsType('manpower-node', 'New Manpower');
                } else if (selectedNode.attributes.nodeType == 'MATERIALRECEIVETYPE') {
                    addClsType('machine-node', 'New Receive Type');
                } else if (selectedNode.attributes.nodeType == 'MATERIALISSUETYPE') {
                    addClsType('sitemap-node', 'New Issue Type');
                }

            }

            function addClsType(icon, newText) {
                var selectedNode = classificationTree.tree('getSelected');

                if (selectedNode != null) {
                    var nodeid = ++nodeId;

                    classificationTree.tree('append', {
                        parent: selectedNode.target,
                        data: [
                            {
                                id: nodeid,
                                iconCls: icon,
                                text: newText,
                                attributes: {
                                    typeObject: null,
                                    nodeType: selectedNode.attributes.nodeType
                                }
                            }
                        ]
                    });

                    var newNode = classificationTree.tree('find', nodeid);
                    if (newNode != null) {
                        classificationTree.tree('beginEdit', newNode.target);

                        $timeout(function () {
                            $('.tree-editor').focus().select();
                        }, 1);
                    }
                }
            }

            function deleteType() {
                var selectedNode = classificationTree.tree('getSelected');
                if (!classificationTree.tree('isLeaf', selectedNode.target)) {
                    $rootScope.showErrorMessage("This item has children items!, We cannot delete this item");
                }
                else {
                    deleteClsType();
                }
            }

            vm.selectedTypeObjects = [];
            function deleteClsType() {
                vm.selectedTypeObjects = [];
                vm.selectedNode = classificationTree.tree('getSelected');
                if (vm.selectedNode != null) {
                    var data = classificationTree.tree('getData', vm.selectedNode.target);
                    if (data.attributes.typeObject.id == undefined) {
                        classificationTree.tree('remove', vm.selectedNode.target);
                        onSelectType(classificationTree.tree('find', 0));
                    }
                    else if (data != null && data.attributes.typeObject != null) {
                        if (vm.selectedNode.attributes.nodeType == 'MATERIALTYPE') {
                            ItemService.getMaterialItemsByType(data.attributes.typeObject.id, pageable).then(
                                function (data) {
                                    vm.selectedTypeObjects = data;
                                    deleteTypeDialog();
                                }
                            );
                        } else if (vm.selectedNode.attributes.nodeType == 'MACHINETYPE') {
                            ItemService.getMachineItemsByType(data.attributes.typeObject.id, pageable).then(
                                function (data) {
                                    vm.selectedTypeObjects = data;
                                    deleteTypeDialog();
                                }
                            );
                        } else if (vm.selectedNode.attributes.nodeType == 'MANPOWERTYPE') {
                            ItemService.getManpowerItemsByType(data.attributes.typeObject.id, pageable).then(
                                function (data) {
                                    vm.selectedTypeObjects = data;
                                    deleteTypeDialog();
                                }
                            );
                        } else if (vm.selectedNode.attributes.nodeType == 'MATERIALRECEIVETYPE') {
                            ItemService.getReceiveItemsByType(data.attributes.typeObject.id, pageable).then(
                                function (data) {
                                    vm.selectedTypeObjects = data;
                                    deleteTypeDialog();
                                }
                            );
                        } else if (vm.selectedNode.attributes.nodeType == 'MATERIALISSUETYPE') {
                            ItemService.getIssueItemsByType(data.attributes.typeObject.id, pageable).then(
                                function (data) {
                                    vm.selectedTypeObjects = data;
                                    deleteTypeDialog();
                                }
                            );
                        }
                    }
                }
            }

            function deleteTypeDialog() {
                var options = null;
                if (vm.selectedTypeObjects.content.length == 0) {
                    options = {
                        title: 'Delete Type',
                        message: 'Are you sure you want to delete this Type?',
                        okButtonClass: 'btn-danger'
                    };
                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {
                            if (vm.selectedNode != null) {
                                var data = classificationTree.tree('getData', vm.selectedNode.target);
                                if (data != null && data.attributes.typeObject != null) {
                                    ClassificationService.deleteType(vm.selectedNode.attributes.nodeType, data.attributes.typeObject.id).then(
                                        function (data) {
                                            classificationTree.tree('remove', vm.selectedNode.target);
                                            $rootScope.showSuccessMessage(vm.selectedNode.attributes.typeObject.name + " deleted successfully ");
                                            $scope.$broadcast('app.materialType.selected', {itemType: null});
                                            $scope.$broadcast('app.machineType.selected', {itemType: null});
                                            $scope.$broadcast('app.manpowerType.selected', {itemType: null});
                                            $scope.$broadcast('app.receiveType.selected', {itemType: null});
                                            $scope.$broadcast('app.issueType.selected', {itemType: null});
                                        })
                                }
                            }
                        }
                    })
                } else if (vm.selectedTypeObjects.content.length != 0) {
                    if (vm.selectedNode.attributes.nodeType == 'MATERIALTYPE') {
                        options = {
                            title: 'Delete Material Type',
                            message: 'This Material type has material! We cannot delete this Material type',
                            okButtonClass: 'btn-danger'
                        };
                    } else if (vm.selectedNode.attributes.nodeType == 'MACHINETYPE') {
                        options = {
                            title: 'Delete Machine Type',
                            message: 'This Machine type has machines! We cannot delete this Machine type',
                            okButtonClass: 'btn-danger'
                        };
                    } else if (vm.selectedNode.attributes.nodeType == 'MANPOWERTYPE') {
                        options = {
                            title: 'Delete Manpower Type',
                            message: 'This Manpower type has Manpower! We cannot delete this Manpower type',
                            okButtonClass: 'btn-danger'
                        };
                    } else if (vm.selectedNode.attributes.nodeType == 'MATERIALRECEIVETYPE') {
                        options = {
                            title: 'Delete Receive Type',
                            message: 'This Receive type has receives! We cannot delete this Receive type',
                            okButtonClass: 'btn-danger'
                        };
                    } else if (vm.selectedNode.attributes.nodeType == 'MATERIALISSUETYPE') {
                        options = {
                            title: 'Delete Issue Type',
                            message: 'This Issue type has issues! We cannot delete this Issue type',
                            okButtonClass: 'btn-danger'
                        };
                    }
                    DialogService.confirm(options, function (yes) {
                        if (yes == true) {

                        }
                    })
                }

            }

            vm.itemType = null;
            function onSave() {
                if (vm.type == 'MATERIALTYPE') {
                    $scope.$broadcast('app.materialType.save');
                } else if (vm.type == 'MACHINETYPE') {
                    $scope.$broadcast('app.machineType.save');
                } else if (vm.type == 'MANPOWERTYPE') {
                    $scope.$broadcast('app.manpowerType.save');
                } else if (vm.type == 'MATERIALRECEIVETYPE') {
                    $scope.$broadcast('app.receiveType.save');
                } else if (vm.type == 'MATERIALISSUETYPE') {
                    $scope.$broadcast('app.issueType.save');
                }

            }

            function isExists(parent, node) {
                var exists = false;
                var count = 0;
                var children = classificationTree.tree('getChildren', parent.target);
                angular.forEach(children, function (child) {
                    if (child.text == node.text) {
                        count++;
                    }
                });

                if (count > 1 || node.text == parent.text) {
                    exists = true;
                    $rootScope.showErrorMessage("Name already exists");
                }
                return exists;

            }

            function update(event, args) {
                var node = classificationTree.tree('find', args.nodeId);
                if (node) {
                    classificationTree.tree('update', {
                        target: node.target,
                        text: args.nodeName
                    });
                }
            }

            function checkPermission() {
                return $rootScope.hasPermission('permission.masterdata.editClassification');
            }

            (function () {
                $scope.$on('$viewContentLoaded', function () {
                    $('div.split-right-pane').css({left: 300});
                    $('div.split-pane').splitPane();
                    $scope.$on("app.classification.update", update);
                    initClassificationTree();
                });
            })();
        }
    }
);
