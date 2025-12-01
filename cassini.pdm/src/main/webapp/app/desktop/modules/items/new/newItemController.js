define(
    [
        'app/desktop/modules/items/item.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService',
        'app/shared/services/itemTypeService',
        'app/shared/services/itemService',
        'app/desktop/modules/classification/directive/classificationTreeDirective',
        'app/desktop/modules/classification/directive/classificationTreeController'
    ],
    function (module) {

        module.controller('NewItemController', NewItemController);

        function NewItemController($scope, $q, $rootScope, $timeout, $state, $stateParams, $cookies,
                                   AutonumberService, ItemTypeService, ItemService) {
            if ($application.homeLoaded == false) {
                return;
            }

            var vm = this;

            vm.newItem = {
                itemType: null,
                itemNumber: null,
                description: null,
                units: "Each"
            };

            vm.attribute = null;
            vm.attributes = [];
            vm.allAttributes = [];
            vm.requiredAttributes = [];

            vm.onSelectType = onSelectType;
            vm.createItem = createItem;
            vm.autoNumber = autoNumber;
            vm.anotherItem = anotherItem;
            vm.addToListValue = addToListValue;
            vm.cancelToListValue = cancelToListValue;

            function anotherItem() {
                create().then(
                    function () {
                        vm.newItem = {
                            itemType: null,
                            itemNumber: null,
                            description: null,
                            units: "Each"
                        };
                        $scope.callback();
                    }
                );
            }

            function createItem() {
                create().then(
                    function () {
                        $rootScope.hideSidePanel();
                        $scope.callback();
                    }
                );
            }

            function create() {
                var defered = $q.defer();
                $rootScope.closeNotification();

                vm.allAttributes=[];

                angular.forEach(vm.requiredAttributes, function(reqAttribute){
                    vm.allAttributes.push(reqAttribute);
                })

                angular.forEach(vm.attributes, function(attribute){
                    vm.allAttributes.push(attribute);
                })
                if (validate()) {
                    vm.validattributes = [];
                    angular.forEach(vm.allAttributes, function (attribute) {
                        if (attribute.attributeDef.required == true) {
                            if (checkAttribute(attribute)) {
                                vm.validattributes.push(attribute);
                            }
                            else {
                                $rootScope.showErrorMessage(attribute.attributeDef.name + ":" + "Attribute is Required");
                            }
                        } else {
                            vm.validattributes.push(attribute);
                        }
                    });
                    if (vm.allAttributes.length == vm.validattributes.length) {
                        ItemService.createItem(vm.newItem).then(
                            function (data) {
                                vm.newItem.id = data.id;
                                saveAttributes();
                                $rootScope.showSuccessMessage("Item created successfully");
                                defered.resolve();
                            }, function (error) {
                                defered.reject();
                            }
                        )
                    }
                }

                return defered.promise;
            }

            function onSelectType(itemType) {
                if (itemType != null && itemType != undefined) {
                    vm.newItem.itemType = itemType;

                    var revSequence = itemType.revisionSequence;
                    if (revSequence != null) {
                        vm.newItem.revision = revSequence.values[0];
                    }

                    var lifecycles = itemType.lifecycleStates;
                    if (lifecycles != null) {
                        vm.newItem.lifecycleStates = lifecycles;
                    }
                    autoNumber();
                }
            }

            function loadAttributeDefs() {
                vm.requiredAttributes = [];
                vm.attributes = [];
                ItemTypeService.getAttributesWithHierarchy(vm.newItem.itemType.id).then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: vm.newItem.id,
                                    attributeDef: attribute.id

                                },
                                attributeDef: attribute,
                                listValue: [],
                                newListValue: null,
                                listValueEditMode: false
                            };
                            if (attribute.required == false) {
                                vm.attributes.push(att);
                            } else{
                                vm.requiredAttributes.push(att);
                            }
                        });
                    }
                )
            }

            function saveAttributes() {
                if (vm.allAttributes.length > 0) {

                    angular.forEach(vm.allAttributes, function (attribute) {
                        attribute.id.objectId = vm.newItem.id;
                    });
                    ItemService.saveItemAttributes(vm.newItem.id, vm.allAttributes).then(
                        function (data) {
                        }
                    )
                }

            }

            function addToListValue(attribute) {
                if (attribute.listValue == undefined) {
                    attribute.listValue = [];
                }

                attribute.listValue.push(attribute.newListValue);
                attribute.newListValue="";
            }

            function cancelToListValue(attribute) {
                attribute.listValueEditMode = false;
            }

            function checkAttribute(attribute) {
                if ((attribute.stringValue != null && attribute.stringValue != undefined && attribute.stringValue != "") ||
                    (attribute.integerValue != null && attribute.integerValue != undefined && attribute.integerValue != "") ||
                    (attribute.doubleValue != null && attribute.doubleValue != undefined && attribute.doubleValue != "") ||
                    (attribute.booleanValue != null && attribute.booleanValue != undefined && attribute.booleanValue != "") ||
                    (attribute.dateValue != null && attribute.dateValue != undefined && attribute.dateValue != "") ||
                    (attribute.listValue.length != 0)) {
                    return true;
                } else {
                    return false;
                }
            }


            function validate() {
                var valid = true;

                if (vm.newItem.itemType == null) {
                    valid = false;
                    $rootScope.showErrorMessage('Item type cannot be empty');
                }
                else if (vm.newItem.itemNumber == null) {
                    valid = false;
                    $rootScope.showErrorMessage('Item number cannot be empty');
                }
                else if (vm.newItem.description == null) {
                    valid = false;
                    $rootScope.showErrorMessage('Item description cannot be empty');
                }
                else if (vm.newItem.units == null) {
                    valid = false;
                    $rootScope.showErrorMessage('Item units cannot be empty');
                }

                return valid;
            }

            function autoNumber() {
                if (vm.newItem.itemType != null && vm.newItem.itemType.itemNumberSource != null) {
                    var source = vm.newItem.itemType.itemNumberSource;
                    AutonumberService.getNextNumber(source.id).then(
                        function (data) {
                            vm.newItem.itemNumber = data;
                            loadAttributeDefs();
                        }, function (error) {
                            $rootScope.showErrorMessage("Problem loading itemNumber.. Please create item once")
                        }
                    )
                }
            }


            (function () {
                if ($application.homeLoaded == true) {
                    $rootScope.$on('app.items.new', anotherItem);
                }
            })();
        }
    }
)
;