define(
    [
        'app/desktop/modules/item/item.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/classification/directive/classificationTreeDirective',
        'app/desktop/modules/classification/directive/classificationTreeController',
        'app/shared/services/core/itemTypeService',
        'app/shared/services/core/relationshipService',
        'app/shared/services/core/relatedItemService',
        'app/shared/services/core/itemService',
        'app/desktop/modules/settings/relationships/new/relationClassificationTreeDirective',
        'app/desktop/modules/settings/relationships/new/relationClassificationTreeController',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirectiveController',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService'
    ],
    function (module) {
        module.controller('NewRelatedItemController', NewRelatedItemController);

        function NewRelatedItemController($q, $scope, $rootScope, $timeout, $state, DialogService, $stateParams, $cookies,
                                          CommonService, $translate, ItemTypeService, RelationshipService, RelatedItemService,
                                          ItemService, AttributeAttachmentService, ObjectAttributeService) {
            var vm = this;

            vm.onSelectRelationship = onSelectRelationship;

            vm.selectedFromItem = $scope.data.selectedFromItemData;
            vm.selectedRevision = $scope.data.selectedItemRevisionData;
            vm.editRelatedItem = $scope.data.editRelatedItem;

            vm.selectedItems = [];
            vm.attributes = [];
            vm.requiredAttributes = [];
            vm.showAttributes = false;
            vm.showItem = false;

            vm.relatedItem = {
                id: null,
                fromItem: vm.selectedFromItem,
                toItem: null,
                relationship: null
            };

            vm.selectCheck = selectCheck;
            vm.selectAll = selectAll;
            vm.selectItem = selectItem;
            $scope.relationship = null;

            function selectItem(item) {
                if (vm.selectedItem != null && vm.selectedItem.id === item.id) {
                    item.checked = false;
                    vm.showAttributes = false;
                    vm.showItem = false;
                    vm.selectedItem = null;
                    // $('.table-div').css({top: '230px !important'});
                } else {
                    vm.showAttributes = true;
                    vm.showItem = true;
                    vm.selectedItem = item;
                    vm.relatedItem.fromItem = vm.selectedRevision.id;
                    vm.relatedItem.toItem = vm.selectedItem;
                    vm.relatedItem.toItem.id = vm.selectedItem.latestRevision;
                    vm.relatedItem.toItem.itemMaster = vm.selectedItem.id;
                    vm.relatedItem.relationship = vm.selectedItem.relationship;
                    $scope.relationship = vm.selectedItem.relationship;
                    //loadAttributeDefs($scope.relationship);

                }
            }

            function selectAll(check) {
                vm.selectedItems = [];
                if (check) {
                    $scope.check = false;
                    angular.forEach(vm.toTypeItems, function (item) {
                        item.selected = false;
                    })
                } else {
                    $scope.check = true;
                    vm.error = "";
                    angular.forEach(vm.toTypeItems, function (item) {
                        item.selected = true;
                        vm.selectedItems.push(item);
                    })
                }
            }

            function selectCheck(item) {
                var flag = true;
                vm.error = "";
                angular.forEach(vm.selectedItems, function (selectedItem) {
                    if (selectedItem.id == item.id) {
                        flag = false;
                        var index = vm.selectedItems.indexOf(item);
                        vm.selectedItems.splice(index, 1);
                    }
                });
                if (flag) {
                    vm.selectedItems.push(item);
                }
            }

            var parsed = angular.element("<div></div>");
            var attributeRequired = parsed.html($translate.instant("ATTRIBUTE_REQUIRED")).html();
            var fromAndToItemValidation = parsed.html($translate.instant("FROM_AND_TO_ITEM_VALIDATION")).html();
            var atLeastOneItemValidation = parsed.html($translate.instant("ATLEAST_ONE_ITEM_VALIDATION")).html();
            $scope.configurableItem = parsed.html($translate.instant("CONFIGURABLE_ITEM")).html();
            vm.SelectRelation = parsed.html($translate.instant("SELECT_RELATION")).html();
            var relatedItemAddedMessage = parsed.html($translate.instant("RELATIONSHIP_ITEM_ADDED_MESSAGE")).html();

            function validate() {
                var valid = true;
                if (vm.selectedItem == null) {
                    valid = false;
                    $rootScope.showWarningMessage(atLeastOneItemValidation);
                }
                return valid;
            }

            function createRelatedItem() {
                create().then(
                    function () {
                        $rootScope.showSuccessMessage(relatedItemAddedMessage);
                        onSelectRelationship(vm.selectedRelationship)
                        vm.showAttributes = false;
                        vm.selectedItem = null;
                        vm.relatedItem = {
                            id: null,
                            fromItem: vm.selectedFromItem,
                            toItem: null,
                            relationship: $scope.relationship
                        };
                        $scope.callback(vm.selectedItem);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function create() {
                var defered = $q.defer();
                if (validate()) {
                    $rootScope.showBusyIndicator($("#rightSidePanel"));
                    vm.validattributes = [];
                    angular.forEach(vm.requiredAttributes, function (attribute) {
                        if (attribute.attributeDef.required == true && attribute.attributeDef.dataType != 'BOOLEAN' &&
                            attribute.attributeDef.dataType != 'TIMESTAMP') {
                            if (vm.checkAttribute(attribute)) {
                                vm.validattributes.push(attribute);
                            }
                            else {
                                $rootScope.hideBusyIndicator();
                                $rootScope.showWarningMessage(attribute.attributeDef.name + ":" + attributeRequired);
                            }
                        } else {
                            vm.validattributes.push(attribute);
                        }
                    });
                    if (vm.selectedItem != null && vm.requiredAttributes.length == vm.validattributes.length) {
                        vm.relatedItem.toItem = vm.selectedItem;
                        RelatedItemService.createRelatedItem(vm.relatedItem).then(
                            function (data) {
                                vm.relatedItem = data;
                                saveAttributes().then(
                                    function (data) {
                                        defered.resolve();
                                    }
                                )
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                                defered.reject();
                            }
                        )
                    }
                }
                return defered.promise;
            }

            vm.checkAttribute = function (attribute) {
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
            };

            function saveAttributes() {
                var defered = $q.defer();
                vm.relatedItemId = [];
                vm.imageAttributes = [];
                var images = new Hashtable();
                angular.forEach(vm.requiredAttributes, function (reqatt) {
                    vm.attributes.push(reqatt);
                });
                if (vm.attributes.length > 0) {
                    angular.forEach(vm.attributes, function (attribute) {
                        attribute.id.objectId = vm.relatedItem.id;
                        vm.relatedItemId = vm.relatedItem.id;

                        if (attribute.attributeDef.dataType == 'IMAGE' && attribute.imageValue != null) {
                            images.put(attribute.id.attributeDef, attribute.imageValue);
                            vm.imageAttributes.push(attribute);
                            attribute.imageValue = null;
                        }
                        if (attribute.attributeDef.dataType == 'ATTACHMENT' && attribute.attachmentValues.length != null) {
                            attribute.attachmentValues = addAttachment(attribute)
                        }
                    });
                    $timeout(function () {
                        RelatedItemService.createRelatedItemAttributes(vm.relatedItemId, vm.attributes).then(
                            function (data) {
                                if (vm.imageAttributes.length > 0) {
                                    angular.forEach(vm.imageAttributes, function (imgAtt) {
                                        ObjectAttributeService.uploadObjectAttributeImage(imgAtt.id.objectId, imgAtt.id.attributeDef, images.get(imgAtt.id.attributeDef)).then(
                                            function (data) {
                                                defered.resolve();
                                                //loadNewRelatedItemView();
                                            }, function (error) {
                                                $rootScope.showErrorMessage(error.message);
                                            }
                                        )
                                    })
                                } else {
                                    defered.resolve();
                                    //loadNewRelatedItemView();
                                }
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                                defered.reject();
                            })
                    }, 2000)
                } else {
                    defered.resolve();
                    //loadNewRelatedItemView();
                }
                return defered.promise;
            }

            function loadNewRelatedItemView() {
                /* var createdSelectedItem = vm.selectedItem;
                 vm.selectedItem = null;
                 angular.forEach(vm.toTypeItems, function (item) {
                 if (createdSelectedItem.id == item.id) {
                 var index = vm.toTypeItems.indexOf(item);
                 vm.toTypeItems.splice(index, 1);
                 }
                 });
                 loadAttributeDefs($scope.relationship);
                 vm.showAttributes = false;
                 $scope.callback();*/

                $rootScope.showSuccessMessage(itemsAddedMessage);
                var index = vm.toTypeItems.indexOf(vm.selectedItem);
                vm.toTypeItems.splice(index, 1);
                vm.showAttributes = false;
                vm.selectedItem = null;
                vm.showItem = false;
                vm.relatedItem = {
                    id: null,
                    fromItem: vm.selectedFromItem,
                    toItem: null,
                    relationship: $scope.relationship
                };
                $scope.callback(vm.selectedItem);
                $rootScope.hideBusyIndicator();
            }

            function addAttachment(attribute) {
                var attachmentIds = [];
                angular.forEach(attribute.attachmentValues, function (attachmentFile) {
                    AttributeAttachmentService.saveAttributeAttachment(attribute.id.objectId, attribute.id.attributeDef, 'RELATEDITEM', attachmentFile).then(
                        function (data) {
                            attachmentIds.push(data[0].id);
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        })
                })
                return attachmentIds;
            }

            function edit() {
                if (validate()) {
                    RelatedItemService.updateRelatedItem(vm.relatedItem).then(
                        function (data) {
                            vm.relatedItem = {
                                id: null,
                                name: null,
                                description: null,
                                fromItem: null,
                                toItem: null,
                                relationship: null
                            };
                            $scope.callback();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                }
            }

            vm.filters = {
                itemNumber: null,
                itemName: null,
                searchQuery: null,
                description: null
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
            vm.toTypeItems = angular.copy(pagedResults);
            vm.selectedRelationship = null;
            function onSelectRelationship(relationship) {
                $rootScope.showBusyIndicator($('#rightSidePanel'));
                vm.selectedRelationship = relationship;
                RelationshipService.getItemsByRelationshipAndFromItem(relationship.id, vm.selectedRevision.id, vm.pageable, vm.filters).then(
                    function (data) {
                        vm.toTypeItems = data;
                        vm.selectedItem = null;
                        vm.showItem = false;
                        vm.showAttributes = false;
                        angular.forEach(vm.toTypeItems.content, function (item) {
                            item.relationship = relationship;
                            item.checked = false;
                        });
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                );

                loadAttributeDefs(relationship);

            }

            vm.searchFilterItem = searchFilterItem;
            vm.clearFilter = clearFilter;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.clear = false;
            function searchFilterItem() {
                if ((vm.filters.itemName != null && vm.filters.itemName != "") || (vm.filters.itemNumber != null && vm.filters.itemNumber != "") || (vm.filters.description != null && vm.filters.description != "")) {
                    vm.pageable.page = 0;
                    vm.clear = true;
                    onSelectRelationship(vm.selectedRelationship);
                } else {
                    clearFilter();
                }
            }

            function clearFilter() {
                vm.filters = {
                    itemNumber: null,
                    itemName: null,
                    searchQuery: null,
                    description: null
                }
                vm.pageable.page = 0;
                vm.clear = false;
                onSelectRelationship(vm.selectedRelationship);
            }

            function nextPage() {
                if (vm.toTypeItems.last != true) {
                    vm.pageable.page++;
                    onSelectRelationship(vm.selectedRelationship);
                }
            }

            function previousPage() {
                if (vm.toTypeItems.first != true) {
                    vm.pageable.page--;
                    onSelectRelationship(vm.selectedRelationship);
                }
            }

            function loadAttributeDefs(relationship) {
                vm.attributes = [];
                vm.requiredAttributes = [];
                RelationshipService.getAllAttributesByRelationship(relationship.id).then(
                    function (data) {
                        vm.relationshipAttributes = data;
                        angular.forEach(vm.relationshipAttributes, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.relatedItem.id,
                                    attributeDef: attribute.id

                                },
                                attributeDef: attribute,
                                listValue: null,
                                newListValue: null,
                                listValueEditMode: false,
                                timestampValue: moment(new Date()).format("DD/MM/YYYY, HH:mm:ss"),
                                booleanValue: false,
                                dateValue: null,
                                timeValue: null,
                                imageValue: null,
                                refValue: null,
                                ref: null,
                                attachmentValues: []
                            };
                            if (attribute.lov != null) {
                                att.lovValues = attribute.lov.values;
                            }
                            if (attribute.required == false) {
                                vm.attributes.push(att);
                            } else {
                                vm.requiredAttributes.push(att);
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            vm.initialSelectedRelation = null;
            function loadRelationships() {
                vm.initialSelectedRelation = null;
                RelationshipService.getRelationshipsByFromType(vm.selectedFromItem.itemType).then(
                    function (data) {
                        vm.relationships = data;
                        if (vm.relationships.length == 1) {
                            vm.relatedItem.relationship = vm.relationships[0];
                            onSelectRelationship(vm.relatedItem.relationship);
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                $rootScope.$on('app.items.details.relatedItems.new', createRelatedItem);
                //$scope.$on('app.items.details.relatedItems.edit', edit);

                loadRelationships();
            })();
        }
    }
);