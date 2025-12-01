/**
 * Created by swapna on 31/07/18.
 */
define(
    [
        'app/desktop/modules/stores/store.module',
        'app/shared/services/store/topStockIssuedService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/shared/services/pm/project/projectService',
        'app/shared/services/tm/taskService',
        'app/shared/services/pm/project/wbsService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/desktop/modules/stores/details/tabs/issues/directive/issueTypeDirective',
        'app/shared/services/core/itemTypeService'
    ],
    function (module) {
        module.controller('NewStockIssueController', NewStockIssueController);

        function NewStockIssueController($scope, $rootScope, $timeout, $state, $stateParams, AutonumberService, $q, ProjectService,
                                         TopStockIssuedService, $application, AttributeAttachmentService, ObjectAttributeService,
                                         WbsService, TaskService, CommonService, ObjectTypeAttributeService, ItemTypeService) {

            var vm = this;
            vm.newStockIssueAttributes = [];
            vm.storeId = $stateParams.storeId;
            vm.newStockIssue = {
                materialIssueType: null,
                name: null,
                store: vm.storeId,
                issueNumberSource: null,
                notes: null,
                project: null,
                task: null,
                issuedTo: null,
                issueDate: null,
                stockIssueItems: [],
                issuedToName: null
            };
            vm.newPerson = {
                fullName: "New Person",
                firstName: null,
                lastName: null,
                phoneMobile: " ",
                email: " ",
                personType: 1,
                defaultGroup: null
            };

            vm.newTaskAttributes = [];
            vm.autoNumber = autoNumber;
            vm.loadProjectWBS = loadProjectWBS;
            vm.onSelectWbs = onSelectWbs;
            vm.onSelectType = onSelectType;
            vm.changeMode = changeMode;
            var nodeId = 0;
            var wbsTree = null;
            var rootNode = null;
            vm.project = null;
            vm.wbs = null;
            vm.projectPersons = [];
            vm.persons = [];
            vm.requiredAttributes = [];
            vm.attributes = [];
            vm.remove = remove;
            vm.back = back;
            vm.addItems = addItems;
            vm.create = create;
            vm.stockIssueItems = [];
            vm.mode = null;
            var stockIssueItemsMap = new Hashtable();
            $rootScope.viewInfo.title = "Stock Issue";

            vm.emptyIssueItem = {
                materialItem: null,
                notes: null,
                quantity: null
            };

            function onSelectType(itemType) {
                if (itemType != null && itemType != undefined) {
                    vm.newStockIssue.materialIssueType = null;
                    vm.newStockIssue.materialIssueType = itemType;
                    loadIssueTypeAttributeDefs();
                }
            }

            function remove(item) {
                var index = vm.stockIssueItems.indexOf(item);
                vm.stockIssueItems.splice(index, 1);
                stockIssueItemsMap.remove(item.id);
            }

            function back() {
                $state.go('app.store.details', {storeId: $rootScope.storeId, mode: 'ISSUE'});
            }

            function changeMode() {
                if (vm.newStockIssue.issuedTo.fullName == "New Person" && (vm.newStockIssue.issuedTo.id == null || vm.newStockIssue.issuedTo.id == undefined)) {
                    vm.mode = 'NEW';
                }
                else {
                    vm.mode = 'EXIST';
                }
            }

            function addItems() {
                if (validate()) {
                    var options = {
                        title: 'Stock Issue Items',
                        showMask: true,
                        template: 'app/desktop/modules/stores/details/tabs/issues/new/newStockIssueItemsView.jsp',
                        controller: 'NewStockIssueItemsController as stockIssueItemsVm',
                        resolve: 'app/desktop/modules/stores/details/tabs/issues/new/newStockIssueItemsController',
                        width: 700,
                        data: {
                            newStockIssue: vm.newStockIssue,
                            stockIssueItems: vm.stockIssueItems,
                            addedItemsMap: stockIssueItemsMap
                        },
                        buttons: [],
                        callback: function (issueItem) {
                            var item = stockIssueItemsMap.get(issueItem.id);
                            if (item == null) {
                                stockIssueItemsMap.put(issueItem.id, issueItem);
                                vm.stockIssueItems.push(issueItem);
                            }
                        }
                    };

                    $rootScope.showSidePanel(options);
                }
            }

            function create() {
                $rootScope.closeNotification();
                if (validateIssue()) {
                    attributesValidate().then(
                        function (data) {
                            vm.newStockIssue.project = vm.newStockIssue.project.id;
                            if (vm.newStockIssue.task != null) {
                                vm.newStockIssue.task = vm.newStockIssue.task.id;
                            }
                            TopStockIssuedService.createTopStockIssue($stateParams.storeId, vm.newStockIssue).then(
                                function (data) {
                                    vm.newStockIssue = data;
                                    intializationForAttributesSave().then(
                                        function (success) {
                                            $rootScope.hideBusyIndicator();
                                            $rootScope.issueId = vm.newStockIssue.id;
                                            $state.go('app.store.stock.issueDetails', {issueId: vm.newStockIssue.id});
                                            $rootScope.showSuccessMessage("Stock Issue (" + vm.newStockIssue.issueNumberSource + ") created successfully");
                                        }, function (error) {

                                        });
                                }, function (error) {
                                    $rootScope.showErrorMessage("Stock Issue not created, Please try again");
                                }
                            )

                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        });
                }
            }

            function validateIssue() {
                var valid = false;
                var counter = 0;
                var stockissItems = [];
                angular.forEach(vm.stockIssueItems, function (stockIssueItem) {
                    if (stockIssueItem.quantity > stockIssueItem.storeInventory) {
                        valid = false;
                        $rootScope.hideBusyIndicator();
                        $rootScope.showErrorMessage("Issuing qty cannot be greater than Inventory qty");
                    }
                    else {
                        if (stockIssueItem.quantity > 0) {
                            var copiedstockIssueItem = angular.copy(vm.emptyIssueItem);
                            copiedstockIssueItem.item = stockIssueItem.id;
                            copiedstockIssueItem.notes = stockIssueItem.notes;
                            copiedstockIssueItem.quantity = stockIssueItem.quantity;
                            copiedstockIssueItem.recordedBy = window.$application.login.person.id;
                            copiedstockIssueItem.movementType = "ISSUED";
                            copiedstockIssueItem.issuedBy = window.$application.login.person.id;
                            copiedstockIssueItem.boqReference = stockIssueItem.boqReference;
                            copiedstockIssueItem.project = vm.newStockIssue.project.id;
                            stockissItems.push(copiedstockIssueItem);
                            counter++;
                            if (counter == vm.stockIssueItems.length) {
                                valid = true;
                                vm.newStockIssue.stockIssueItems = stockissItems;
                            }
                        } else {
                            valid = false;
                            $rootScope.showErrorMessage("Please enter qty for item(s)");
                        }
                    }
                });

                return valid;
            }

            function validate() {
                var flag = true;
                if (vm.newStockIssue.issueNumberSource == null || vm.newStockIssue.issueNumberSource == "") {
                    flag = false;
                    $rootScope.showErrorMessage("Please select Auto Number");
                }
                else if (vm.newStockIssue.project == null) {
                    flag = false;
                    $rootScope.showErrorMessage("Please select Project");
                }
                //else if(vm.newStockIssue.task == null) {
                //    flag = false;
                //    $rootScope.showErrorMessage("Please select Task");
                //}
                else if (vm.mode == "NEW" && vm.newPerson.firstName == null) {
                    flag = false;
                    $rootScope.showErrorMessage("Issued To person First Name cannot be empty");
                }
                else if (vm.newStockIssue.issuedToName == null) {
                    flag = false;
                    $rootScope.showErrorMessage("Please enter Issued To");
                }
                else if (vm.newStockIssue.issueDate == null) {
                    flag = false;
                    $rootScope.showErrorMessage("Please select Issue Date");
                }
                return flag;
            }

            function autoNumber() {
                var prefix = $application.config.autoNumber.prefix;
                /*var year = $application.config.autoNumber.year;*/
                var year = new Date().getFullYear()
                AutonumberService.getAutonumberByName('Default Stock Issue Number Source').then(
                    function (data) {
                        AutonumberService.getNextNumber(data.id).then(
                            function (data) {
                                vm.generatedNumber = data;
                                if (prefix != "" && year != "") {
                                    vm.newStockIssue.issueNumberSource = prefix + "/ " + $rootScope.selectedStore.storeName + "/" + year + "/" + vm.generatedNumber;
                                }
                                else {
                                    vm.newStockIssue.issueNumberSource = vm.generatedNumber;
                                }
                            }
                        )
                    });
            }

            function loadWBSTasks(wbs) {
                TaskService.tasksByWbs(vm.project.id, wbs.id).then(
                    function (data) {
                        vm.taskList = data;
                        if (vm.taskList.length == 0) {
                            $rootScope.showErrorMessage("Selected WBS has no tasks");
                        }
                        else {
                            angular.forEach(vm.taskList, function (task) {
                                if (task.status == 'FINISHED') {
                                    var index = vm.taskList.indexOf(task);
                                    vm.taskList.splice(index, 1);
                                }
                            })
                        }
                    })
            }

            function loadProjects() {
                ProjectService.getAllProjects().then(
                    function (data) {
                        vm.projects = data;
                    }
                );
            }

            function loadProjectWBS(project) {
                vm.project = project;
                //initWbsTree();
                //loadWbsTree();
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
                    $rootScope.showErrorMessage("Please select children node");
                }
                loadWBSTasks(vm.wbs);
            }

            function loadPersons() {
                vm.persons.push(vm.newPerson);
                CommonService.getAllPersons().then(
                    function (data) {
                        angular.forEach(data, function (obj) {
                            vm.persons.push(obj);
                        })
                    }
                )
            }

            /* ............. start attributes methods block ............. */

            function addAttachment(attribute) {
                var defered = $q.defer();
                var counter = 0;
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'ISSUE', attachmentFile).then(
                        function (data) {
                            attribute.propertyAttachmentIds.push(data[0].id);
                            counter++;
                            if (counter == attribute.attachmentValues.length) {
                                defered.resolve(true);
                            }
                        }
                    )
                });
                return defered.promise;
            }

            function saveObjectAttributes() {
                var defered = $q.defer();
                if (vm.newStockIssueAttributes.length > 0) {
                    angular.forEach(vm.newStockIssueAttributes, function (att) {
                        if (att.dateValue == "") {
                            att.dateValue = null;
                        }
                    });
                    ObjectAttributeService.saveItemObjectAttributes(vm.newStockIssue.id, vm.newStockIssueAttributes).then(
                        function (data) {
                            var secCount = 0;
                            if (vm.propertyImageAttributes.length > 0) {
                                angular.forEach(vm.propertyImageAttributes, function (propImgAtt) {
                                    ObjectAttributeService.uploadObjectAttributeImage(propImgAtt.id.objectId, propImgAtt.id.attributeDef, vm.propertyImages.get(propImgAtt.id.attributeDef)).then(
                                        function (data) {
                                        });
                                    secCount++;
                                    if (secCount == vm.propertyImageAttributes.length) {
                                        defered.resolve();
                                    }
                                });
                            } else {
                                defered.resolve();
                            }
                        },
                        function (error) {
                            defered.reject();
                        }
                    )

                }
                return defered.promise;
            }

            function attributesValidate() {
                var defered = $q.defer();
                $rootScope.closeNotification();
                vm.objectAttributes = [];
                vm.validObjectAttributes = [];
                vm.validattributes = [];
                angular.forEach(vm.requiredAttributes, function (attribute) {
                    if (attribute.attributeDef.required == true && attribute.attributeDef.dataType != 'BOOLEAN' &&
                        attribute.attributeDef.dataType != 'TIMESTAMP') {
                        if (checkAttribute(attribute)) {
                            vm.validattributes.push(attribute);
                        }
                        else {
                            $rootScope.showErrorMessage(attribute.attributeDef.name + ": attribute is required");
                            $rootScope.hideBusyIndicator();
                        }
                    } else {
                        vm.validattributes.push(attribute);
                    }
                });
                vm.objectAttributes = [];
                if (vm.newStockIssueAttributes != null && vm.newStockIssueAttributes != undefined) {
                    vm.objectAttributes = vm.objectAttributes.concat(vm.newStockIssueAttributes);
                }
                if (vm.allIssueTypeAttributes != null && vm.allIssueTypeAttributes != undefined) {
                    vm.objectAttributes = vm.objectAttributes.concat(vm.allIssueTypeAttributes);
                }
                angular.forEach(vm.objectAttributes, function (attribute) {
                    if (attribute.attributeDef.required == true && attribute.attributeDef.dataType != 'BOOLEAN' &&
                        attribute.attributeDef.dataType != 'TIMESTAMP') {
                        if (checkAttribute(attribute)) {
                            vm.validObjectAttributes.push(attribute);
                        }
                        else {
                            $rootScope.showErrorMessage(attribute.attributeDef.name + ": attribute is required");
                            $rootScope.hideBusyIndicator();
                        }
                    } else {
                        vm.validObjectAttributes.push(attribute);
                    }
                });
                if (vm.requiredAttributes.length == vm.validattributes.length && vm.objectAttributes.length == vm.validObjectAttributes.length) {
                    defered.resolve();
                } else {
                    defered.reject();
                }
                return defered.promise;
            }

            function loadObjectAttributeDefs() {
                vm.newStockIssueAttributes = [];
                ObjectTypeAttributeService.getObjectTypeAttributesByType("ISSUE").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newStockIssue.id,
                                    attributeDef: attribute.id
                                },
                                attributeDef: attribute,
                                listValue: null,
                                stringValue: null,
                                newListValue: null,
                                mListValue: [],
                                timeValue: null,
                                timestampValue: null,
                                listValueEditMode: false,
                                booleanValue: false,
                                dateValue: null,
                                imageValue: null,
                                refValue: null,
                                ref: null,
                                attachmentValues: [],
                                currencyType: null,
                                longTextValue: null,
                                richTextValue: null
                            };
                            if (attribute.lov != null) {
                                att.lovValues = attribute.lov.values;
                            }
                            if (attribute.required == false) {
                                vm.attributes.push(att);
                            } else {
                                vm.requiredAttributes.push(att);
                            }

                            vm.newStockIssueAttributes.push(att);
                        });
                    }, function (error) {

                    });
            }

            function loadIssueTypeAttributeDefs() {
                vm.issueTypeAttributes = [];
                vm.requiredIssueTypeAttributes = [];
                vm.allIssueTypeAttributes = [];
                ItemTypeService.getIssueTypeAttributesWithHierarchy(vm.newStockIssue.materialIssueType.id).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newStockIssue.id,
                                    attributeDef: attribute.id
                                },
                                value: {
                                    id: {
                                        objectId: vm.newStockIssue.id,
                                        attributeDef: attribute.id
                                    }
                                },
                                attributeDef: attribute,
                                listValue: null,
                                stringValue: null,
                                newListValue: null,
                                mListValue: [],
                                timeValue: null,
                                timestampValue: null,
                                listValueEditMode: false,
                                booleanValue: false,
                                dateValue: null,
                                imageValue: null,
                                refValue: null,
                                ref: null,
                                attachmentValues: [],
                                currencyType: null,
                                longTextValue: null,
                                richTextValue: null,
                                changeImage: false,
                                newImageValue: null,
                                showAttachment: false,
                                showTimeAttribute: false,
                                showTimestamp: false,
                                editMode: false,
                                changeCurrency: false,
                                editTimeValue: null,
                                attachmentIds: []
                            };
                            if (attribute.lov != null) {
                                att.lovValues = attribute.lov.values;
                            }
                            if (attribute.required == false) {
                                vm.issueTypeAttributes.push(att);
                            } else {
                                vm.requiredIssueTypeAttributes.push(att);
                            }

                            vm.allIssueTypeAttributes.push(att);
                        });
                    }, function (error) {

                    });
            }

            function checkAttribute(attribute) {
                if ((attribute.stringValue != null && attribute.stringValue != undefined && attribute.stringValue != "") ||
                    (attribute.integerValue != null && attribute.integerValue != undefined && attribute.integerValue != "") ||
                    (attribute.doubleValue != null && attribute.doubleValue != undefined && attribute.doubleValue != "") ||
                    (attribute.dateValue != null && attribute.dateValue != undefined && attribute.dateValue != "") ||
                    (attribute.imageValue != null && attribute.imageValue != undefined && attribute.imageValue != "") ||
                    (attribute.currencyValue != null && attribute.currencyValue != undefined && attribute.currencyValue != "") ||
                    (attribute.timeValue != null && attribute.timeValue != undefined && attribute.timeValue != "") ||
                    (attribute.attachmentValues.length != 0) ||
                    (attribute.refValue != null && attribute.refValue != undefined && attribute.refValue != "") ||
                    (attribute.listValue != null && attribute.listValue != undefined && attribute.listValue != "")) {
                    return true;
                } else {
                    return false;
                }
            }

            function intializationForAttributesSave() {
                var defered = $q.defer();
                var attrCount = 0;
                vm.propertyImageAttributes = [];
                vm.propertyImages = new Hashtable();
                vm.images = new Hashtable();
                vm.requiredAttributes = [];
                vm.requiredIssueTypeAttributes = [];
                vm.issueTypeImageAttributes = [];
                vm.issueTypeImages = new Hashtable();
                vm.imageAttributes = [];
                vm.requiredIssueTypeAttributes = [];
                if (vm.allIssueTypeAttributes != null && vm.allIssueTypeAttributes != undefined) {
                    vm.newStockIssueAttributes = vm.newStockIssueAttributes.concat(vm.allIssueTypeAttributes);
                }

                if (vm.newStockIssueAttributes.length > 0) {
                    angular.forEach(vm.newStockIssueAttributes, function (attribute) {
                        attribute.propertyAttachmentIds = [];
                        attribute.id.objectId = vm.newStockIssue.id;
                        if (attribute.attributeDef.dataType == "IMAGE" && attribute.imageValue != null) {
                            vm.propertyImages.put(attribute.attributeDef.id, attribute.imageValue);
                            vm.propertyImageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length > 0) {
                            addAttachment(attribute).then(
                                function (data) {
                                    attribute.attachmentValues = attribute.propertyAttachmentIds;
                                    attrCount++;
                                    if (attrCount == vm.newStockIssueAttributes.length) {
                                        saveObjectAttributes().then(
                                            function (data) {
                                                defered.resolve();
                                            }, function (error) {
                                                defered.reject();
                                            }
                                        )
                                    }
                                });
                        } else {
                            attrCount++;
                            if (attrCount == vm.newStockIssueAttributes.length) {
                                saveObjectAttributes().then(
                                    function (data) {
                                        defered.resolve();
                                    }, function (error) {
                                        defered.reject();
                                    }
                                )
                            }
                        }
                    });
                }
                else {
                    defered.resolve();
                }
                return defered.promise;
            }

            /* ............. attributes methods end block ............. */

            $rootScope.$on('app.storeItems.issue', function () {
                create();
            });

            (function () {
                if ($application.homeLoaded == true) {
                    loadProjects();
                    loadObjectAttributeDefs();
                    loadPersons();
                }
            })();
        }
    }
);