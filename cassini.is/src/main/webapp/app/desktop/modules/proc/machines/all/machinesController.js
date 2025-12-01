define(
    [
        'app/desktop/modules/proc/proc.module',
        'split-pane',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/itemService',
        'app/shared/services/pm/project/bomService',
        'app/shared/services/core/itemTypeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService'
    ],
    function (module) {
        module.controller('MachinesController', MachinesController);

        function MachinesController($scope, $rootScope, $timeout, $interval, $state, $stateParams, $cookies, $window,
                                    CommonService, ItemService, DialogService, BomService, ItemTypeService, ObjectTypeAttributeService,
                                    AttributeAttachmentService, ObjectAttributeService, $sce) {
            if ($application.homeLoaded == false) {
                return;
            }

            $rootScope.viewInfo.icon = "fa fa-truck";
            $rootScope.viewInfo.title = "Machines";

            var vm = this;

            vm.loading = true;
            vm.selectedItemType = null;
            vm.mode = $stateParams.mode;
            vm.showSearchMode = false;
            vm.editMachine = editMachine;
            vm.showMachine = showMachine;
            vm.showNewMachine = showNewMachine;
            vm.showMachineAttributes = showMachineAttributes;
            vm.machineAttributeSearch = machineAttributeSearch;
            vm.clearMachineAttributeSearch = clearMachineAttributeSearch;
            var currencyMap = new Hashtable();

            vm.deleteItem = deleteItem;
            vm.previousPage = previousPage;
            vm.nextPage = nextPage;
            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;
            $scope.freeTextQuery = null;

            vm.objectIds = [];
            vm.mode = null;

            vm.itemIds = [];
            vm.attributeIds = [];
            vm.machineAttributes = [];
            vm.machineRequiredIds = [];
            vm.machineTypeRequiredIds = [];

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

            vm.machine = angular.copy(pagedResults);

            vm.emptyFilters = {
                itemType: null,
                itemNumber: null,
                active: null,
                unitPrice: null,
                unitCost: null,
                description: null,
                createdDate: null,
                modifiedDate: null
            };

            vm.filters = angular.copy(vm.emptyFilters);

            var machineCriteria = {
                freeTextSearch: false,
                searchQuery: null,
                attributeSearch: false
            };

            function editMachine(machine) {
                $state.go('app.proc.machines.edit', {machineId: machine.id});
            }

            function showMachine(machine) {
                $rootScope.machineId = machine.id;
                $state.go('app.proc.machines.details', {machineId: machine.id});
            }

            vm.machine = angular.copy(pagedResults);

            function loadMachine() {
                vm.loading = true;
                ItemService.getMachines(vm.pageable).then(
                    function (data) {
                        vm.loading = false;
                        vm.machine = data;
                        CommonService.getPersonReferences(vm.machine.content, 'createdBy');
                        CommonService.getPersonReferences(vm.machine.content, 'modifiedBy');
                        /* loadUserData();*/

                        loadMachineAttributeValues();
                    }
                )
            }

            function nextPage() {
                if (vm.machine.last != true) {
                    vm.pageable.page++;
                    if (vm.showSearchMode) {
                        freeTextSearch($scope.freeTextQuery);
                    }
                    else {
                        loadMachine();
                    }
                }
            }

            function previousPage() {
                if (vm.machine.first != true) {
                    vm.pageable.page--;
                    if (vm.showSearchMode) {
                        freeTextSearch($scope.freeTextQuery);
                    }
                    else {
                        loadMachine();
                    }
                }
            }

            function resetPage() {
                vm.pageable.page = 0;
                vm.showSearchMode = false;
                machineCriteria.freeTextSearch = false;
                machineCriteria.searchQuery = null;
            }

            function freeTextSearch(freeText) {
                $scope.freeTextQuery = freeText;
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    machineCriteria.searchQuery = freeText;
                    machineCriteria.freeTextSearch = true;
                    ItemService.machineFreeTextSearch(vm.pageable, machineCriteria).then(
                        function (data) {
                            vm.machine = data;
                            vm.showSearchMode = true;
                            CommonService.getPersonReferences(vm.machine.content, 'createdBy');
                            CommonService.getPersonReferences(vm.machine.content, 'modifiedBy');
                            vm.clear = true;
                        }
                    );
                } else {
                    resetPage();
                    loadMachine();
                }
            }

            function showNewMachine() {
                var options = {
                    title: 'New Machine',
                    showMask: true,
                    template: 'app/desktop/modules/proc/machines/new/newMachineView.jsp',
                    controller: 'NewMachineController as newMachineVm',
                    resolve: 'app/desktop/modules/proc/machines/new/newMachineController',
                    width: 500,
                    buttons: [
                        {text: 'Create', broadcast: 'app.machines.new'}
                    ],
                    callback: function () {
                        loadMachine();
                        loadRequiredTrueAttributes();
                    }
                };

                $rootScope.showSidePanel(options);

            }

            function deleteItem(machine) {
                var options = {
                    title: 'Delete Machine',
                    message: 'Are you sure you want to delete (' + machine.itemNumber + ') Machine?',
                    okButtonClass: 'btn-danger'
                };

                BomService.getBoqItemByItemNumber(machine.itemNumber).then(
                    function (data) {
                        if (data == null || data == '') {
                            DialogService.confirm(options, function (yes) {
                                if (yes == true) {
                                    ItemService.deleteMachine(machine.id).then(
                                        function (data) {
                                            var index = vm.machine.content.indexOf(machine);
                                            vm.machine.content.splice(index, 1);
                                            $rootScope.showSuccessMessage(machine.itemNumber + " : Machine deleted successfully");
                                        }
                                    )
                                }
                            })
                        }
                        else {
                            $rootScope.showErrorMessage("This Machine is already in use(BOQ), We cannot delete this Machine");
                        }
                    }
                );

            }

            function showMachineAttributes() {
                var options = {
                    title: 'Machine Attributes',
                    showMask: true,
                    template: 'app/desktop/modules/home/attributes/allAttributesView.jsp',
                    controller: 'AllAttributesController as allAttributesVm',
                    resolve: 'app/desktop/modules/home/attributes/allAttributesController',
                    width: 600,
                    data: {
                        selectedAttributes: vm.machineAttributes,
                        attributesMode: 'MACHINE'
                    },
                    buttons: [
                        {text: 'Add', broadcast: 'app.items.attributes.select'}
                    ],
                    callback: function (result) {
                        vm.machineAttributes = result;
                        $window.localStorage.setItem("machineAttributes", JSON.stringify(vm.machineAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage("Attributes added successfully");
                        }
                        $rootScope.hideSidePanel();
                        loadRequiredTrueAttributes();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            vm.removeAttribute = removeAttribute;

            function removeAttribute(att) {
                vm.machineAttributes.remove(att);
                $window.localStorage.setItem("machineAttributes", JSON.stringify(vm.machineAttributes));
            }

            vm.showImage = showImage;
            function showImage(attribute) {

                var modal = document.getElementById('myModal2');
                var modalImg = document.getElementById('img12');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage12")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            vm.showMachineImage = showMachineImage;
            function showMachineImage(attribute) {

                var modal = document.getElementById('myModal1');
                var modalImg = document.getElementById('img11');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage11")[0];

                span.onclick = function () {
                    modal.style.display = "block";
                    modal.style.display = "none";
                }
            }

            vm.showMachineTypeImage = showMachineTypeImage;
            function showMachineTypeImage(attribute) {

                var modal = document.getElementById('myModal3');
                var modalImg = document.getElementById('img13');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage13")[0];

                span.onclick = function () {
                    modal.style.display = "block";
                    modal.style.display = "none";
                }
            }

            vm.openAttachment = openAttachment;
            function openAttachment(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/download".
                    format(window.location.protocol, window.location.host,
                    attachment.id);
                launchUrl(url);
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("machineAttributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            function loadRequiredTrueAttributes() {
                ItemService.getTypeAttributesRequiredTrue("MACHINE").then(
                    function (data) {
                        vm.requiredTrueMachineAttributes = data;
                        loadMachine();
                        /*loadRequiredTrueMachineTypeAttributes()*/
                    }
                )
            }

            /* function loadRequiredTrueMachineTypeAttributes() {
             ItemTypeService.getItemTypeAttributesRequiredTrue("MACHINE").then(
             function (data) {
             vm.requiredTrueMachineTypeAttributes = data;
             loadMachine();
             }
             )
             }*/

            vm.loadMachineAttributeValues = loadMachineAttributeValues;

            function loadMachineAttributeValues() {
                angular.forEach(vm.machine.content, function (item) {
                    vm.itemIds.push(item.id);
                });
                angular.forEach(vm.machineAttributes, function (machineAttribute) {
                    if (machineAttribute.id != null && machineAttribute.id != "" && machineAttribute.id != 0) {
                        vm.attributeIds.push(machineAttribute.id);
                    }
                });

                angular.forEach(vm.requiredTrueMachineAttributes, function (machineAttribute) {
                    if (machineAttribute.id != null && machineAttribute.id != "" && machineAttribute.id != 0) {
                        vm.machineRequiredIds.push(machineAttribute.id);
                    }
                });

                /* angular.forEach(vm.requiredTrueMachineTypeAttributes, function (machineAttribute) {
                 if (machineAttribute.id != null && machineAttribute.id != "" && machineAttribute.id != 0) {
                 vm.machineTypeRequiredIds.push(machineAttribute.id);
                 }
                 });
                 */
                if (vm.itemIds.length > 0 && vm.attributeIds.length > 0) {
                    ItemService.getAttributesByItemIdAndAttributeId(vm.itemIds, vm.attributeIds).then(
                        function (data) {
                            vm.selectedObjectAttributes = data;

                            var map = new Hashtable();
                            angular.forEach(vm.machineAttributes, function (att) {
                                if (att.id != null && att.id != "" && att.id != 0) {
                                    map.put(att.id, att);
                                }
                            });

                            angular.forEach(vm.machine.content, function (item) {
                                var attributes = [];

                                var itemAttributes = vm.selectedObjectAttributes[item.id];
                                if (itemAttributes != null && itemAttributes != undefined) {
                                    attributes = attributes.concat(itemAttributes);
                                }
                                angular.forEach(attributes, function (attribute) {
                                    var selectatt = map.get(attribute.id.attributeDef);
                                    if (selectatt != null) {
                                        var attributeName = selectatt.name;
                                        if (selectatt.dataType == 'TEXT') {
                                            item[attributeName] = attribute.stringValue;
                                        } else if (selectatt.dataType == 'INTEGER') {
                                            item[attributeName] = attribute.integerValue;
                                        } else if (selectatt.dataType == 'BOOLEAN') {
                                            item[attributeName] = attribute.booleanValue;
                                        } else if (selectatt.dataType == 'DOUBLE') {
                                            item[attributeName] = attribute.doubleValue;
                                        } else if (selectatt.dataType == 'DATE') {
                                            item[attributeName] = attribute.dateValue;
                                        } else if (selectatt.dataType == 'LIST') {
                                            item[attributeName] = attribute.listValue;
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
                                            var attachmentIds = [];
                                            if (attribute.attachmentValues.length > 0) {
                                                angular.forEach(attribute.attachmentValues, function (attachmentId) {
                                                    attachmentIds.push(attachmentId);
                                                });
                                                AttributeAttachmentService.getMultipleAttributeAttachments(attachmentIds).then(
                                                    function (data) {
                                                        vm.machineAttachments = data;
                                                        item[attributeName] = vm.machineAttachments;
                                                    }
                                                )
                                            }
                                        } else if (selectatt.dataType == 'IMAGE') {
                                            if (attribute.imageValue != null) {
                                                item[attributeName] = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();

                                            }
                                        } else if (selectatt.dataType == 'OBJECT') {
                                            if (attribute.refValue != null) {
                                                var objectSelector = $application.getObjectSelector(selectatt.refType);
                                                if (objectSelector != null && attribute.refValue != null) {
                                                    objectSelector.getDetails(attribute.refValue, item, attributeName);
                                                }
                                            }
                                        }
                                    }
                                })
                            })

                        }
                    );
                }

                if (vm.itemIds.length > 0 && vm.machineRequiredIds.length > 0) {
                    ItemService.getAttributesByItemIdAndAttributeId(vm.itemIds, vm.machineRequiredIds).then(
                        function (data) {
                            vm.requiredMachineAttributes = data;

                            var map = new Hashtable();
                            angular.forEach(vm.requiredTrueMachineAttributes, function (att) {
                                if (att.id != null && att.id != "" && att.id != 0) {
                                    map.put(att.id, att);
                                }
                            });

                            angular.forEach(vm.machine.content, function (item) {
                                var attributes = [];
                                item.refValueString = null;

                                var itemAttributes = vm.requiredMachineAttributes[item.id];
                                if (itemAttributes != null && itemAttributes != undefined) {
                                    attributes = attributes.concat(itemAttributes);
                                }
                                angular.forEach(attributes, function (attribute) {
                                    var selectatt = map.get(attribute.id.attributeDef);
                                    if (selectatt != null) {
                                        var attributeName = selectatt.name;
                                        if (selectatt.dataType == 'TEXT') {
                                            item[attributeName] = attribute.stringValue;
                                        } else if (selectatt.dataType == 'INTEGER') {
                                            item[attributeName] = attribute.integerValue;
                                        } else if (selectatt.dataType == 'BOOLEAN') {
                                            item[attributeName] = attribute.booleanValue;
                                        } else if (selectatt.dataType == 'DOUBLE') {
                                            item[attributeName] = attribute.doubleValue;
                                        } else if (selectatt.dataType == 'DATE') {
                                            item[attributeName] = attribute.dateValue;
                                        } else if (selectatt.dataType == 'LIST') {
                                            item[attributeName] = attribute.listValue;
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
                                            var attachmentIds = [];
                                            if (attribute.attachmentValues.length > 0) {
                                                angular.forEach(attribute.attachmentValues, function (attachmentId) {
                                                    attachmentIds.push(attachmentId);
                                                });
                                                AttributeAttachmentService.getMultipleAttributeAttachments(attachmentIds).then(
                                                    function (data) {
                                                        vm.requiredMachineAttachments = data;
                                                        item[attributeName] = vm.requiredMachineAttachments;
                                                    }
                                                )
                                            }
                                        } else if (selectatt.dataType == 'IMAGE') {
                                            if (attribute.imageValue != null) {
                                                item[attributeName] = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();

                                            }
                                        } else if (selectatt.dataType == 'OBJECT') {
                                            if (attribute.refValue != null) {
                                                var objectSelector = $application.getObjectSelector(selectatt.refType);
                                                if (objectSelector != null && attribute.refValue != null) {
                                                    objectSelector.getDetails(attribute.refValue, item, attributeName);
                                                }
                                            }
                                        }
                                    }
                                })
                            })

                        }
                    );
                }

                if (vm.itemIds.length > 0 && vm.machineTypeRequiredIds.length > 0) {
                    ItemService.getAttributesByItemIdAndAttributeId(vm.itemIds, vm.machineTypeRequiredIds).then(
                        function (data) {
                            vm.machineTypeAttributes = data;

                            var map = new Hashtable();
                            angular.forEach(vm.requiredTrueMachineTypeAttributes, function (att) {
                                if (att.id != null && att.id != "" && att.id != 0) {
                                    map.put(att.id, att);
                                }
                            });

                            angular.forEach(vm.machine.content, function (item) {
                                var attributes = [];

                                var itemAttributes = vm.machineTypeAttributes[item.id];
                                if (itemAttributes != null && itemAttributes != undefined) {
                                    attributes = attributes.concat(itemAttributes);
                                }
                                angular.forEach(attributes, function (attribute) {
                                    var selectatt = map.get(attribute.id.attributeDef);
                                    if (selectatt != null) {
                                        var attributeName = selectatt.name;
                                        if (selectatt.dataType == 'TEXT') {
                                            item[attributeName] = attribute.stringValue;
                                        } else if (selectatt.dataType == 'INTEGER') {
                                            item[attributeName] = attribute.integerValue;
                                        } else if (selectatt.dataType == 'BOOLEAN') {
                                            item[attributeName] = attribute.booleanValue;
                                        } else if (selectatt.dataType == 'DOUBLE') {
                                            item[attributeName] = attribute.doubleValue;
                                        } else if (selectatt.dataType == 'DATE') {
                                            item[attributeName] = attribute.dateValue;
                                        } else if (selectatt.dataType == 'LIST') {
                                            item[attributeName] = attribute.listValue;
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
                                            var attachmentIds = [];
                                            if (attribute.attachmentValues.length > 0) {
                                                angular.forEach(attribute.attachmentValues, function (attachmentId) {
                                                    attachmentIds.push(attachmentId);
                                                });
                                                AttributeAttachmentService.getMultipleAttributeAttachments(attachmentIds).then(
                                                    function (data) {
                                                        vm.machineTypeAttachments = data;
                                                        item[attributeName] = vm.machineTypeAttachments;
                                                    }
                                                )
                                            }
                                        } else if (selectatt.dataType == 'IMAGE') {
                                            if (attribute.imageValue != null) {
                                                item[attributeName] = "api/core/objects/" + attribute.id.objectId + "/attributes/" + attribute.id.attributeDef + "/imageAttribute/download?" + new Date().getTime();

                                            }
                                        }
                                    }
                                })
                            })

                        }
                    );
                }
            }

            function machineAttributeSearch() {
                var options = {
                    title: 'Attribute Search',
                    showMask: true,
                    template: 'app/desktop/modules/attributeSearch/attributeSearchView.jsp',
                    controller: 'AttributeSearchController as attributeSearchVm',
                    resolve: 'app/desktop/modules/attributeSearch/attributeSearchController',
                    width: 500,
                    buttons: [
                        {text: 'Search', broadcast: 'app.store.attribute.search'}
                    ],
                    data: {
                        objectType: 'MACHINE',
                        type: 'MACHINETYPE'
                    },
                    callback: function (result) {
                        angular.forEach(result, function (attribute) {
                            if (attribute.aBoolean != null) {
                                attribute.booleanSearch = true;
                            }
                        })
                        machineCriteria.searchAttributes = result;
                        machineCriteria.attributeSearch = true;
                        machineCriteria.freeTextSearch = false;
                        vm.loading = true;
                        machineCriteria.freeTextSearch = false;
                        ItemService.machineFreeTextSearch(vm.pageable, machineCriteria).then(
                            function (data) {
                                vm.loading = false;
                                vm.attributeSearch = true;
                                vm.machine = data;
                                CommonService.getPersonReferences(vm.machine.content, 'createdBy');
                                CommonService.getPersonReferences(vm.machine.content, 'modifiedBy');
                                loadMachineAttributeValues();
                            }
                        );
                        $rootScope.hideSidePanel();
                        $rootScope.hideBusyIndicator();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function clearMachineAttributeSearch() {
                vm.attributeSearch = false;
                machineCriteria.attributeSearch = false;
                vm.pageable.page = 0;
                loadRequiredTrueAttributes();
            }

            (function () {
                //loadMachine();
                $scope.$on('$viewContentLoaded', function () {
                    $('div.split-right-pane').css({left: 300});
                    $('div.split-pane').splitPane();
                });
                $rootScope.$on('app.machines.all', function () {
                    loadMachine();
                    loadRequiredTrueAttributes();
                });
                if (validateJSON()) {
                    var setAttributes = JSON.parse($window.localStorage.getItem("machineAttributes"));
                } else {
                    var setAttributes = null;
                }
                ObjectAttributeService.getCurrencies().then(
                    function (data) {
                        vm.currencies = data;
                        angular.forEach(vm.currencies, function (currency) {
                            currencyMap.put(currency.id, $sce.trustAsHtml(currency.symbol));
                        });
                    }
                );

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
                                $window.localStorage.setItem("machineAttributes", "");
                                vm.machineAttributes = setAttributes
                            } else {
                                vm.machineAttributes = setAttributes;
                            }

                            if (vm.mode == null || vm.mode == undefined || vm.mode == "") {
                                loadRequiredTrueAttributes();
                            }
                        }
                    )
                } else {
                    if (vm.mode == null || vm.mode == undefined || vm.mode == "") {
                        loadRequiredTrueAttributes();
                    }
                }

            })();
        }
    }
);