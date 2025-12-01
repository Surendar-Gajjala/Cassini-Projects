define(
    [
        'app/desktop/modules/proc/proc.module',
        'split-pane',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/itemService',
        'app/shared/services/pm/project/projectService',
        'app/shared/services/core/itemTypeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService'
    ],
    function (module) {
        module.controller('ManpowerController', ManpowerController);

        function ManpowerController($scope, $rootScope, $timeout, $interval, $state, $stateParams, $cookies,
                                    CommonService, ItemService, DialogService, ProjectService, $window, ItemTypeService,
                                    AttributeAttachmentService, ObjectTypeAttributeService, ObjectAttributeService, $sce) {
            if ($application.homeLoaded == false) {
                return;
            }

            $rootScope.viewInfo.icon = "fa fa-users";
            $rootScope.viewInfo.title = "Manpower";

            var vm = this;

            vm.loading = true;
            vm.selectedItemType = null;
            vm.mode = $stateParams.mode;
            vm.showSearchMode = false;
            vm.editManpower = editManpower;
            vm.showManpowerDetails = showManpowerDetails;
            vm.previousPage = previousPage;
            vm.nextPage = nextPage;
            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;
            vm.showNewManpower = showNewManpower;
            vm.deleteItem = deleteItem;
            $scope.freeTextQuery = null;
            vm.importUsers = importUsers;
            var currencyMap = new Hashtable();

            vm.showManpowerAttributes = showManpowerAttributes;
            vm.showManpowerImage = showManpowerImage;
            vm.removeAttribute = removeAttribute;
            vm.showImage = showImage;
            vm.showManpowerTypeImage = showManpowerTypeImage;
            vm.showPersonImage = showPersonImage;
            vm.manpowerAttributeSearch = manpowerAttributeSearch;
            vm.clearManpowerAttributeSearch = clearManpowerAttributeSearch;
            vm.objectIds = [];

            vm.itemIds = [];
            vm.attributeIds = [];
            vm.manpowerRequiredIds = [];
            vm.manpowerTypeRequiredIds = [];

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

            vm.manpower = angular.copy(pagedResults);

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

            var manpowerCriteria = {
                freeTextSearch: false,
                searchQuery: null,
                attributeSearch: false
            };

            function showManpowerDetails(manpower) {
                $rootScope.manpowerId = manpower.id;
                $state.go('app.proc.manpower.details', {manpowerId: manpower.id})
            }

            function loadManpower() {
                vm.manpower = angular.copy(pagedResults);
                vm.loading = true;
                ItemService.getManpower(vm.pageable).then(
                    function (data) {
                        vm.loading = false;
                        vm.manpower = data;
                        angular.forEach(vm.manpower.content, function (manapower) {
                            manapower.person.photoPath = null;
                            if (manapower.person.image != null) {
                                manapower.person.photoPath = "api/is/items/" + manapower.person.id + "/personImage/download?" + new Date().getTime();
                            }
                        });
                        CommonService.getPersonReferences(vm.manpower.content, 'createdBy');
                        CommonService.getPersonReferences(vm.manpower.content, 'modifiedBy');

                        loadManpowerAttributeValues();
                    }
                )
            }

            function nextPage() {
                if (vm.manpower.last != true) {
                    vm.pageable.page++;
                    if (vm.showSearchMode) {
                        freeTextSearch($scope.freeTextQuery);
                    }
                    else {
                        loadManpower();
                    }
                }
            }

            function previousPage() {
                if (vm.manpower.first != true) {
                    vm.pageable.page--;
                    if (vm.showSearchMode) {
                        freeTextSearch($scope.freeTextQuery);
                    }
                    else {
                        loadManpower();
                    }
                }
            }

            function resetPage() {
                vm.pageable.page = 0;
                vm.showSearchMode = false;
                manpowerCriteria.freeTextSearch = false;
                manpowerCriteria.searchQuery = null;
            }

            function freeTextSearch(freeText) {
                $scope.freeTextQuery = freeText;
                if (freeText != null && freeText != undefined && freeText.trim() != "") {
                    manpowerCriteria.freeTextSearch = true;
                    manpowerCriteria.searchQuery = freeText;
                    ItemService.manpowerFreeTextSearch(vm.pageable, manpowerCriteria).then(
                        function (data) {
                            vm.manpower = data;
                            vm.showSearchMode = true;
                            CommonService.getPersonReferences(vm.manpower.content, 'createdBy');
                            CommonService.getPersonReferences(vm.manpower.content, 'modifiedBy');
                            vm.clear = true;

                            loadManpowerAttributeValues();
                        }
                    );
                } else {
                    resetPage();
                    loadManpower();
                }
            }

            function editManpower(manpower) {
                $state.go('app.proc.manpower.edit', {manpowerId: manpower.id});
            }

            function showNewManpower() {
                var options = {
                    title: 'New Manpower',
                    showMask: true,
                    template: 'app/desktop/modules/proc/manpower/new/newManpowerView.jsp',
                    controller: 'NewManpowerController as newManpowerVm',
                    resolve: 'app/desktop/modules/proc/manpower/new/newManpowerController',
                    width: 600,
                    data: {
                        manpower: vm.manpower
                    },
                    buttons: [
                        {text: 'Create', broadcast: 'app.manpower.new'}
                    ],
                    callback: function () {
                        loadManpower();
                        loadRequiredTrueAttributes();
                    }
                };
                $rootScope.showSidePanel(options);
            }

            function importUsers() {
                var options = {
                    title: 'New Manpower',
                    showMask: true,
                    template: 'app/desktop/modules/proc/manpower/import/userDialogView.jsp',
                    controller: 'UserDialogController as userDialogVm',
                    resolve: 'app/desktop/modules/proc/manpower/import/userDialogController',
                    width: 600,
                    data: {
                        manpower: vm.manpower
                    },
                    buttons: [
                        {text: 'Import', broadcast: 'app.manpower.items'}
                    ],
                    callback: function (results) {
                        ItemService.createMultipleManpowerItems(results).then(
                            function (data) {
                                $rootScope.showSuccessMessage("Imported Users successfully");
                                loadManpower();
                            }, function (error) {

                            }
                        )
                        $rootScope.hideSidePanel('right');
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function deleteItem(manPower) {
                var options = {
                    title: 'Delete Person',
                    message: 'Are you sure you want to delete (' + manPower.itemNumber + ') Person ?',
                    okButtonClass: 'btn-danger'
                };
                ProjectService.getProjectPerson(manPower.person.id).then(
                    function (data) {
                        if (data == null || data == '') {
                            DialogService.confirm(options, function (yes) {
                                if (yes == true) {
                                    ItemService.deleteManPower(manPower.id).then(
                                        function (data) {
                                            var index = vm.manpower.content.indexOf(manPower);
                                            vm.manpower.content.splice(index, 1);
                                            $rootScope.showSuccessMessage(manPower.itemNumber + " : Person deleted successfully");
                                        }
                                    )
                                }
                            })
                        }
                        else {
                            $rootScope.showErrorMessage("This Manpower is already in use(Project team)! We cannot delete this Manpower");
                        }
                    }
                );

            }

            vm.manpowerAttributes = [];
            function showManpowerAttributes() {
                var options = {
                    title: 'Manpower Attributes',
                    showMask: true,
                    template: 'app/desktop/modules/home/attributes/allAttributesView.jsp',
                    controller: 'AllAttributesController as allAttributesVm',
                    resolve: 'app/desktop/modules/home/attributes/allAttributesController',
                    width: 600,
                    data: {
                        selectedAttributes: vm.manpowerAttributes,
                        attributesMode: 'MANPOWER'
                    },
                    buttons: [
                        {text: 'Add', broadcast: 'app.items.attributes.select'}
                    ],
                    callback: function (result) {
                        vm.manpowerAttributes = result;
                        $window.localStorage.setItem("manpowerAttributes", JSON.stringify(vm.manpowerAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage("Attributes added successfully");
                        }
                        $rootScope.hideSidePanel();
                        loadRequiredTrueAttributes();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function loadManpowerAttributeValues() {
                angular.forEach(vm.manpower.content, function (item) {
                    item.refValueString = null;
                    vm.itemIds.push(item.id);
                });
                angular.forEach(vm.manpowerAttributes, function (manpowerAttribute) {
                    if (manpowerAttribute.id != null && manpowerAttribute.id != "" && manpowerAttribute.id != 0) {
                        vm.attributeIds.push(manpowerAttribute.id);
                    }
                });

                angular.forEach(vm.requiredTrueManpowerAttributes, function (manpowerAttribute) {
                    if (manpowerAttribute.id != null && manpowerAttribute.id != "" && manpowerAttribute.id != 0) {
                        vm.manpowerRequiredIds.push(manpowerAttribute.id);
                    }
                });

                /* angular.forEach(vm.requiredTrueManpowerTypeAttributes, function (manpowerAttribute) {
                 if (manpowerAttribute.id != null && manpowerAttribute.id != "" && manpowerAttribute.id != 0) {
                 vm.manpowerTypeRequiredIds.push(manpowerAttribute.id);
                 }
                 });*/

                if (vm.itemIds.length > 0 && vm.attributeIds.length > 0) {
                    ItemService.getAttributesByItemIdAndAttributeId(vm.itemIds, vm.attributeIds).then(
                        function (data) {
                            vm.selectedObjectAttributes = data;

                            var map = new Hashtable();
                            angular.forEach(vm.manpowerAttributes, function (att) {
                                if (att.id != null && att.id != "" && att.id != 0) {
                                    map.put(att.id, att);
                                }
                            });

                            angular.forEach(vm.manpower.content, function (item) {
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
                                                        vm.manpowerAttachments = data;
                                                        item[attributeName] = vm.manpowerAttachments;
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

                if (vm.itemIds.length > 0 && vm.manpowerRequiredIds.length > 0) {
                    ItemService.getAttributesByItemIdAndAttributeId(vm.itemIds, vm.manpowerRequiredIds).then(
                        function (data) {
                            vm.requiredManpowerAttributes = data;

                            var map = new Hashtable();
                            angular.forEach(vm.requiredTrueManpowerAttributes, function (att) {
                                if (att.id != null && att.id != "" && att.id != 0) {
                                    map.put(att.id, att);
                                }
                            });

                            angular.forEach(vm.manpower.content, function (item) {
                                var attributes = [];

                                var itemAttributes = vm.requiredManpowerAttributes[item.id];
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
                                                        vm.requiredManpowerAttachments = data;
                                                        item[attributeName] = vm.requiredManpowerAttachments;
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

                if (vm.itemIds.length > 0 && vm.manpowerTypeRequiredIds.length > 0) {
                    ItemService.getAttributesByItemIdAndAttributeId(vm.itemIds, vm.manpowerTypeRequiredIds).then(
                        function (data) {
                            vm.manpowerTypeAttributes = data;

                            var map = new Hashtable();
                            angular.forEach(vm.requiredTrueManpowerTypeAttributes, function (att) {
                                if (att.id != null && att.id != "" && att.id != 0) {
                                    map.put(att.id, att);
                                }
                            });

                            angular.forEach(vm.manpower.content, function (item) {
                                var attributes = [];

                                var itemAttributes = vm.manpowerTypeAttributes[item.id];
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
                                                        vm.manpowerTypeAttachments = data;
                                                        item[attributeName] = vm.manpowerTypeAttachments;
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

            function removeAttribute(att) {
                vm.manpowerAttributes.remove(att);
                $window.localStorage.setItem("manpowerAttributes", JSON.stringify(vm.manpowerAttributes));
            }

            function showImage(attribute) {
                var modal = document.getElementById('mpModal1');
                var modalImg = document.getElementById('mpImg1');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage1")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }

            }

            function showManpowerImage(attribute) {

                var modal = document.getElementById('myModal3');
                var modalImg = document.getElementById('img13');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage13")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }

            }

            function showManpowerTypeImage(attribute) {

                var modal = document.getElementById('mpModal2');
                var modalImg = document.getElementById('mpImg2');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage2")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            function showPersonImage(item) {
                var modal = document.getElementById('mpModal');
                var modalImg = document.getElementById('mpImg');

                modal.style.display = "block";
                modalImg.src = item.person.photoPath + "/personImage/download?" + new Date().getTime();

                var span = document.getElementsByClassName("closeImage")[0];

                span.onclick = function () {
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
                    JSON.parse($window.localStorage.getItem("manpowerAttributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            function loadRequiredTrueAttributes() {
                ItemService.getTypeAttributesRequiredTrue("MANPOWER").then(
                    function (data) {
                        vm.requiredTrueManpowerAttributes = data;
                        loadManpower();
                        /*loadRequiredTrueManpowerTypeAttributes()*/
                    }
                )
            }

            function manpowerAttributeSearch() {
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
                        objectType: 'MANPOWER',
                        type: 'MANPOWERTYPE'
                    },
                    callback: function (result) {
                        angular.forEach(result, function (attribute) {
                            if (attribute.aBoolean != null) {
                                attribute.booleanSearch = true;
                            }
                        })
                        manpowerCriteria.searchAttributes = result;
                        manpowerCriteria.attributeSearch = true;
                        manpowerCriteria.freeTextSearch = false;
                        vm.loading = true;
                        manpowerCriteria.freeTextSearch = false;
                        ItemService.manpowerFreeTextSearch(vm.pageable, manpowerCriteria).then(
                            function (data) {
                                vm.loading = false;
                                vm.attributeSearch = true;
                                vm.manpower = data;
                                CommonService.getPersonReferences(vm.manpower.content, 'createdBy');
                                CommonService.getPersonReferences(vm.manpower.content, 'modifiedBy');
                                loadManpowerAttributeValues();
                            }
                        );
                        $rootScope.hideSidePanel();
                        $rootScope.hideBusyIndicator();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function clearManpowerAttributeSearch() {
                vm.attributeSearch = false;
                manpowerCriteria.attributeSearch = false;
                vm.pageable.page = 0;
                loadRequiredTrueAttributes();
            }

            (function () {
                $scope.$on('$viewContentLoaded', function () {
                    $('div.split-right-pane').css({left: 300});
                    $('div.split-pane').splitPane();
                });
                $rootScope.$on('app.manpower.all', function () {
                    loadManpower();
                    loadRequiredTrueAttributes();
                });
                if (validateJSON()) {
                    var setAttributes = JSON.parse($window.localStorage.getItem("manpowerAttributes"));
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
                                $window.localStorage.setItem("manpowerAttributes", "");
                                vm.manpowerAttributes = setAttributes
                            } else {
                                vm.manpowerAttributes = setAttributes;
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
)
;
