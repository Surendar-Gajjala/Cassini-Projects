define(['app/desktop/modules/classification/classification.module',
        'split-pane',
        'jquery.easyui',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/itemService',
        'app/desktop/modules/classification/details/itemTypeDetailsController'
    ],
    function (module) {
        module.controller('ClassificationController', ClassificationController);

        function ClassificationController($q, $scope, $rootScope, $timeout, $state, DialogService, $stateParams,
                                          CommonService, AutonumberService, $translate,
                                          ItemTypeService, ItemService) {
            if ($application.homeLoaded == false) {
                return;
            }

            $rootScope.viewInfo.icon = "fa flaticon-marketing8";
            $rootScope.viewInfo.title = "Classification";

            var vm = this;
            vm.autoNumbers = [];
            vm.type = null;

            vm.addType = addType;
            vm.deleteType = deleteType;
            vm.onSave = onSave;

            var pageable = {
                page: 0,
                size: 30,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            var nodeId = 0;
            var classificationTree = null;
            var rootNode = null;
            $scope.uploadedFile = {file: null};

            function initClassificationTree() {
                classificationTree = $('#classificationTree').tree({
                    data: [
                        {
                            id: nodeId,
                            text: "Classification",
                            iconCls: 'classification-root',
                            attributes: {
                                typeObject: null,
                                nodeType: 'ROOT'
                            },
                            children: []
                        }
                    ],
                    onContextMenu: onContextMenu,
                    onAfterEdit: onAfterEdit,
                    onSelect: onSelectType
                });

                rootNode = classificationTree.tree('find', 0);

                $(document).click(function () {
                    $("#contextMenu").hide();
                });

                loadItemClassification();
            }

            vm.selectedNode = null;
            function onAfterEdit(node) {
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
                    data.attributes.typeObject.units = vm.units;
                }

                data.attributes.typeObject.name = node.text;

                if (data.attributes.typeObject.id == undefined || data.attributes.typeObject.id == null) {
                    if (isExists(parent, node)) {
                        classificationTree.tree('remove', node.target);
                        deleted = true;
                    }
                    else {
                        data.attributes.typeObject.itemNumberSource = vm.defaultPartNumberSource;
                        data.attributes.typeObject.revisionSequence = vm.defaultRevisionSequence;
                        data.attributes.typeObject.units = parentData.attributes.typeObject.units;
                        data.attributes.typeObject.hasLots = false;
                        data.attributes.typeObject.hasSpec = false;
                        data.attributes.typeObject.hasBom = false;

                        if (data.attributes.typeObject.name != "") {
                            if (parentData != null && parentData != undefined) {
                                data.attributes.typeObject.hasBom = parentData.attributes.typeObject.hasBom;
                                data.attributes.typeObject.hasLots = parentData.attributes.typeObject.hasLots;
                            }
                            ItemTypeService.createItemType(data.attributes.typeObject).then(
                                function (type) {
                                    node.text = type.name;
                                    vm.doubleClickType = null;
                                    $rootScope.newItemTypeCreating = false;
                                    data.attributes.typeObject = angular.copy(type);
                                    classificationTree.tree('update', {
                                        target: node.target,
                                        attributes: data.attributes
                                    });
                                    classificationTree.tree('select', node.target);
                                    $scope.$broadcast('app.itemType.selected', {typeObject: type, nodeId: node.id});
                                    $timeout(function () {
                                        $rootScope.showSuccessMessage("Item Type saved successfully");
                                    }, 100);
                                }
                            );
                        } else {
                            $rootScope.showWarningMessage("Type Name cannot be empty");
                            classificationTree.tree('remove', node.target);
                            deleted = true;
                        }
                    }
                } else {
                    if (data.attributes.typeObject.name != "") {
                        ItemTypeService.updateItemType(data.attributes.typeObject).then(
                            function (type) {
                                node.text = type.name;
                                vm.doubleClickType = null;
                                $rootScope.newItemTypeCreating = false;
                                data.attributes.typeObject = angular.copy(type);
                                classificationTree.tree('update', {target: node.target, attributes: data.attributes});
                                classificationTree.tree('select', node.target);
                                $scope.$broadcast('app.itemType.selected', {typeObject: type, nodeId: node.id});
                                $timeout(function () {
                                    $rootScope.showSuccessMessage("Item Type updated successfully");
                                }, 100);
                            }
                        );
                    } else {
                        $rootScope.showWarningMessage("Type Name cannot be empty");
                        data.attributes.typeObject.name = vm.doubleClickType;
                        ItemTypeService.updateItemType(data.attributes.typeObject).then(
                            function (type) {
                                node.text = type.name;
                                vm.doubleClickType = null;
                                $rootScope.newItemTypeCreating = false;
                                data.attributes.typeObject = angular.copy(type);
                                classificationTree.tree('update', {target: node.target, attributes: data.attributes});
                                classificationTree.tree('select', node.target);
                                $scope.$broadcast('app.itemType.selected', {typeObject: type, nodeId: node.id});
                            }
                        );
                    }

                }

            }


            vm.selectedNodeType = null;
            var typeObject = null;

            function onSelectType(node) {
                vm.selectedNodeType = node;
                $rootScope.closeNotification();
                var data = classificationTree.tree('getData', node.target);
                typeObject = data.attributes.typeObject;
                vm.type = data.attributes.nodeType;
                $scope.$broadcast('app.itemType.selected', {typeObject: typeObject, nodeId: node.id});
            }

            /*vm.importTypes = importTypes;
             function importTypes() {
             $("#fileUpload").trigger("click");
             }*/

            $scope.uploadFile = function () {
                var file = document.getElementById("fileUpload").valueOf();
                if (file.files[0] != null && file.files[0] != undefined && file != "") {
                    $rootScope.showExportMessage("File import is in process");
                    ItemTypeService.importClassificationTypes(typeObject.id, file.files[0]).then(
                        function (data) {
                            $rootScope.showSuccessMessage("Imported successfully");
                            nodeId = 0;
                            initClassificationTree();
                            $("#fileUpload").val("");
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $("#fileUpload").val("");
                        })
                }
            };

            function onContextMenu(e, node) {
                e.preventDefault();
                var $contextMenu = $("#contextMenu");
                $('#classificationTree').tree('select', node.target);
                var parent = classificationTree.tree('getData', node.target);
                $('#addType').show();
                $('#deleteType').show();
                $('#importTypes').hide();
                $contextMenu.show();
                if (node.attributes.root == true || node.attributes.nodeType == "ROOT") {
                    $('#addType').hide();
                    $('#deleteType').hide();
                    $('#importTypes').hide();
                    $contextMenu.hide();
                } else if (node.attributes.typeObject.children.length > 0) {
                    $('#addType').text("New Type");
                    $('#deleteType').hide();
                    $('#importTypes').hide();
                }

                if (node.attributes.typeObject.name == 'Part') {
                    $('#importTypes').show();
                }


                if (node.attributes.root == undefined && parent.id === 0) {
                    $contextMenu.hide();
                }
                $contextMenu.css({
                    left: e.pageX,
                    top: e.pageY
                });

                if (node.attributes.typeObject != null && node.attributes.typeObject.parentNode) {
                    $('#deleteType').hide();
                }

                $contextMenu.on("click", "a", function () {
                    $contextMenu.hide();
                });
            }


            function addType() {
                var selectedNode = classificationTree.tree('getSelected');

                if (selectedNode != null) {
                    var nodeid = ++nodeId;

                    classificationTree.tree('append', {
                        parent: selectedNode.target,
                        data: [
                            {
                                id: nodeid,
                                iconCls: 'itemtype-node',
                                text: "New Type",
                                attributes: {
                                    typeObject: null,
                                    nodeType: selectedNode.attributes.nodeType
                                }
                            }
                        ]
                    });

                    if (selectedNode.children.length != null) {
                        var newNode = classificationTree.tree('find', nodeid);
                        classificationTree.tree('expandTo', newNode.target);
                    }

                    var newNode = classificationTree.tree('find', nodeid);
                    if (newNode != null) {
                        classificationTree.tree('beginEdit', newNode.target);

                        $timeout(function () {
                            $('.tree-editor').focus().select();
                        }, 1);
                    }

                    loadAutoNumbers();
                    loadLovs();
                }
            }

            vm.selectedTypeObjects = [];
            function deleteType() {
                vm.selectedTypeObjects = [];
                vm.selectedNode = classificationTree.tree('getSelected');
                var parent = classificationTree.tree('getParent', vm.selectedNode.target);
                var parentData = classificationTree.tree('getData', parent.target);
                if (vm.selectedNode != null) {
                    var data = classificationTree.tree('getData', vm.selectedNode.target);
                    if (data != null && data.attributes.typeObject != null) {
                        ItemTypeService.getItemTypeItems(data.attributes.typeObject.id, pageable).then(
                            function (data) {
                                vm.selectedTypeObjects = data;
                                if (vm.selectedTypeObjects.length == 0) {
                                    var options = {
                                        title: "Delete Item Type",
                                        message: "Please confirm to delete" + " (" + vm.selectedNode.text + ") Item Type",
                                        okButtonClass: 'btn-danger'
                                    };
                                    DialogService.confirm(options, function (yes) {
                                        if (yes == true) {
                                            if (vm.selectedNode != null) {
                                                var data = classificationTree.tree('getData', vm.selectedNode.target);
                                                if (data != null && data.attributes.typeObject != null) {
                                                    ItemTypeService.deleteItemType(data.attributes.typeObject.id).then(
                                                        function (data) {
                                                            classificationTree.tree('remove', vm.selectedNode.target);
                                                            $rootScope.showSuccessMessage(vm.selectedNode.attributes.typeObject.name + " : Item Type deleted successfully");
                                                            $scope.$broadcast('app.itemType.selected', {itemType: null});

                                                            parentData.attributes.typeObject.children.splice(parentData.attributes.typeObject.children.indexOf(vm.selectedNode.attributes.typeObject), 1);

                                                            var node = classificationTree.tree('find', parentData.id);
                                                            if (node) {
                                                                classificationTree.tree('update', {
                                                                    target: parentData.target,
                                                                    attributes: parentData.attributes
                                                                });
                                                            }
                                                        }
                                                    )
                                                }
                                            }
                                        }
                                    })
                                } else {
                                    $rootScope.showWarningMessage("Item Type has Items! We can't delete this Item Type");
                                }
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        );
                    }
                }
            }


            function loadItemClassification() {
                var itemRoot = {
                    id: ++nodeId,
                    text: "Item",
                    iconCls: 'itemtype-node',
                    attributes: {
                        root: true,
                        nodeType: 'ITEMTYPE'
                    },
                    children: []
                };

                ItemTypeService.getClassificationTree('ITEMTYPE').then(
                    function (data) {
                        if (data.length == 0) {
                            itemRoot.state = 'closed'
                        }
                        var nodes = [];
                        angular.forEach(data, function (itemType) {
                            var node = makeNode(itemType);

                            if (itemType.children != null && itemType.children != undefined && itemType.children.length > 0) {
                                node.state = "closed";
                                visitChildren(node, itemType.children);
                            }
                            nodes.push(node);
                        });

                        itemRoot.children = nodes;

                        classificationTree.tree('append', {
                            parent: rootNode.target,
                            data: itemRoot
                        });

                    }
                )
            }

            function visitChildren(parent, itemTypes) {
                var nodes = [];
                angular.forEach(itemTypes, function (itemType) {
                    if (parent.attributes.nodeType == 'ITEMTYPE') {
                        var node = makeNode(itemType);
                    }

                    if (itemType.children != null && itemType.children != undefined && itemType.children.length > 0) {
                        node.state = 'closed';
                        visitChildren(node, itemType.children);
                    }

                    nodes.push(node);
                });

                if (nodes.length > 0) {
                    parent.children = nodes;
                }
            }

            function makeNode(itemType) {
                return {
                    id: ++nodeId,
                    text: itemType.name,
                    iconCls: 'itemtype-node',
                    attributes: {
                        typeObject: itemType,
                        nodeType: 'ITEMTYPE'
                    }
                };
            }

            function onSave() {
                $scope.$broadcast('app.itemtype.save');
            }

            function update(event, args) {
                var node = classificationTree.tree('find', args.nodeId);
                if (node) {
                    node.attributes.typeObject = args.typeObject;
                    classificationTree.tree('update', {
                        target: node.target,
                        text: args.nodeName,
                        attributes: node.attributes
                    });
                }
            }

            function isExists(parent, node) {
                var exists = false;
                var count = 0;
                angular.forEach(parent.children, function (child) {
                    if (child.text == node.text) {
                        count++;
                    }
                });

                if (count > 1 || node.text == parent.text) {
                    exists = true;
                    $rootScope.showWarningMessage(node.text + ": Item Type already exist");
                    $scope.$apply();
                }
                return exists;
            }


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

            vm.importClassification = importClassification;
            function importClassification() {
                $('#fileUpload').trigger('click');
            }


            function loadAutoNumbers() {
                AutonumberService.getAutonumbers().then(
                    function (data) {
                        vm.autoNumbers = data;
                        angular.forEach(data, function (item) {
                            if (item.name == "Default Part Number Source") {
                                vm.defaultPartNumberSource = item;
                            }
                        });
                    }
                )
            }

            function loadLovs() {
                CommonService.getLovByType('Revision Sequence').then(
                    function (data) {
                        vm.revSequences = data;
                        angular.forEach(data, function (item) {
                            if (item.name == 'Default Revision Sequence') {
                                vm.defaultRevisionSequence = item;
                            }
                        });
                    }
                );

                CommonService.getLovByType('Units of Measure').then(
                    function (data) {
                        vm.unitsOfMeasures = data;
                        angular.forEach(data, function (item) {
                            if (item.name == 'Units of Measure') {
                                vm.units = item.defaultValue;
                            }
                        });
                    }
                );
            }

            (function () {
                /*$scope.$watch('uploadedFile.file', function (newValue, oldValue) {
                 $scope.uploadFile();
                 }, true);*/
                $scope.$on('$viewContentLoaded', function () {
                    $('div.split-right-pane').css({left: 300});
                    $('div.split-pane').splitPane();
                    $scope.$on("app.classification.update", update);
                    initClassificationTree();
                })
            })
            ();
        }
    }
)
;