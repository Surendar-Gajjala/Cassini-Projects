/**
 * Created by swapna on 13/09/18.
 */
define(['app/desktop/modules/stores/store.module',
        'app/shared/services/store/customIndentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService'
    ],
    function (module) {
        module.controller('IndentsController', IndentsController);

        function IndentsController($scope, $rootScope, $window, $state, $sce, ObjectAttributeService, ObjectTypeAttributeService, CustomIndentService, $stateParams, AttributeAttachmentService) {
            var vm = this;

            vm.loading = false;
            vm.openIndentDetails = openIndentDetails;
            vm.showImage = showImage;
            vm.indentIds = [];
            vm.openAttachment = openAttachment;

            var currencyMap = new Hashtable();

            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            vm.pagedResults = {
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

            vm.indents = angular.copy(vm.pagedResults);

            function openAttachment(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/download".
                    format(window.location.protocol, window.location.host,
                    attachment.id);
                launchUrl(url);
            }

            function openIndentDetails(indent) {
                $rootScope.indentId = indent.id;
                $state.go('app.store.stock.indentDetails', {storeId: $stateParams.storeId, indentId: indent.id});
            }

            function back() {
                $window.history.back();
            }

            function loadProjectIndents() {
                CustomIndentService.getPageableIndentsByStore(vm.pageable, $stateParams.storeId).then(
                    function (data) {
                        vm.indents = data;
                        if ($stateParams.mode == "IND") {
                            $rootScope.viewInfo.title = "Indents";
                        }
                        $rootScope.storeIndentsList = data;
                        loadAttributeValues();
                    }, function (error) {

                    });
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("storesAttributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            /*  attributes block start */

            function loadRequiredAttributes() {
                CustomIndentService.getRequiredIndentAttributes("CUSTOM_INDENT").then(
                    function (data) {
                        vm.requiredIndentAttributes = data;
                        loadProjectIndents();
                    }, function (error) {

                    }
                )
            }

            function loadAttributes() {
                if (validateJSON()) {
                    var setAttributes = JSON.parse($window.localStorage.getItem("indentAttributes"));
                } else {
                    var setAttributes = null;
                }

                if (setAttributes != null && setAttributes != undefined) {
                    angular.forEach(setAttributes, function (setAtt) {
                        if (setAtt.id != null && setAtt.id != "" && setAtt.id != 0) {
                            vm.indentIds.push(setAtt.id);
                        }
                    });
                    ObjectTypeAttributeService.getObjectTypeAttributesByIds(vm.indentIds).then(
                        function (data) {
                            if (data.length == 0) {
                                setAttributes = null;
                                $window.localStorage.setItem("indentAttributes", "");
                                vm.indentAttributes = setAttributes
                            } else {
                                vm.indentAttributes = setAttributes;
                            }

                            if (vm.mode == null || vm.mode == undefined || vm.mode == "") {
                                loadRequiredAttributes();
                            }
                        }
                    )
                } else {
                    if (vm.mode == null || vm.mode == undefined || vm.mode == "") {
                        loadRequiredAttributes();
                    }
                }
            }

            vm.removeAttribute = removeAttribute;
            function removeAttribute(att) {
                vm.indentAttributes.remove(att);
            }

            function showAttributes() {
                var options = {
                    title: 'Indent Attributes',
                    showMask: true,
                    template: 'app/desktop/modules/home/attributes/allAttributesView.jsp',
                    controller: 'AllAttributesController as allAttributesVm',
                    resolve: 'app/desktop/modules/home/attributes/allAttributesController',
                    width: 600,
                    data: {
                        selectedAttributes: vm.indentAttributes,
                        attributesMode: 'CUSTOM_INDENT'
                    },
                    buttons: [
                        {text: 'Add', broadcast: 'app.items.attributes.select'}
                    ],
                    callback: function (result) {
                        vm.indentAttributes = result;
                        $window.localStorage.setItem("indentAttributes", JSON.stringify(vm.indentAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage("Attributes added successfully");
                        }
                        loadAttributeValues();
                        loadRequiredAttributes();
                        $rootScope.hideSidePanel('right');
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function loadAttributeValues() {
                vm.indentIds = [];
                vm.attributeIds = [];
                vm.requiredAttributeIds = [];
                angular.forEach(vm.indents.content, function (indent) {
                    indent.refValueString = null;
                    vm.indentIds.push(indent.id);
                });
                angular.forEach(vm.indentAttributes, function (indentAttribute) {
                    if (indentAttribute.id != null && indentAttribute.id != "" && indentAttribute.id != 0) {
                        vm.attributeIds.push(indentAttribute.id);
                    }
                });

                angular.forEach(vm.requiredIndentAttributes, function (indentAttribute) {
                    if (indentAttribute.id != null && indentAttribute.id != "" && indentAttribute.id != 0) {
                        vm.requiredAttributeIds.push(indentAttribute.id);
                    }
                });

                if (vm.indentIds.length > 0 && vm.attributeIds.length > 0) {
                    CustomIndentService.getAttributesByIndentIdsAndAttributeId(vm.indentIds, vm.attributeIds).then(
                        function (data) {
                            vm.selectedObjectAttributes = data;
                            var map = new Hashtable();
                            angular.forEach(vm.indentAttributes, function (att) {
                                if (att.id != null && att.id != "" && att.id != 0) {
                                    map.put(att.id, att);
                                }
                            });

                            angular.forEach(vm.indents.content, function (item) {
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
                                                        vm.indentAttachments = data;
                                                        item[attributeName] = vm.indentAttachments;
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

                        }, function (error) {

                        }
                    );
                }

                if (vm.indentIds.length > 0 && vm.requiredAttributeIds.length > 0) {
                    CustomIndentService.getAttributesByIndentIdsAndAttributeId(vm.indentIds, vm.requiredAttributeIds).then(
                        function (data) {
                            vm.requiredAttributes = data;

                            var map = new Hashtable();
                            angular.forEach(vm.requiredIndentAttributes, function (att) {
                                if (att.id != null && att.id != "" && att.id != 0) {
                                    map.put(att.id, att);
                                }
                            });

                            angular.forEach(vm.indents.content, function (item) {
                                var attributes = [];

                                var indentAttributes = vm.requiredAttributes[item.id];
                                if (indentAttributes != null && indentAttributes != undefined) {
                                    attributes = attributes.concat(indentAttributes);
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
                                                        vm.requiredIndentAttachments = data;
                                                        item[attributeName] = vm.requiredIndentAttachments;
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

                        }, function (error) {

                        }
                    );
                }

            }

            /* ........... end attribute block........*/

            function nextPage() {
                if (vm.indents.last != true) {
                    vm.pageable.page++;
                    loadProjectIndents();
                }
            }

            function previousPage() {
                if (vm.indents.first != true) {
                    vm.pageable.page--;
                    loadProjectIndents();
                }
            }

            function showImage(attribute) {
                var modal = document.getElementById('indModal');
                var modalImg = document.getElementById('indImg');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage2")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            vm.showRequiredImage = showRequiredImage;

            function showRequiredImage(attribute) {
                var modal = document.getElementById('image');
                var modalImg = document.getElementById('img');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage2")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            function loadCurrencies() {
                ObjectAttributeService.getCurrencies().then(
                    function (data) {
                        vm.currencies = data;
                        angular.forEach(vm.currencies, function (currency) {
                            currencyMap.put(currency.id, $sce.trustAsHtml(currency.symbol));
                        });
                    }
                );
            }

            (function () {
                $scope.$on('app.store.tabactivated', function (event, data) {
                    if (data.tabId == 'details.indent') {
                        loadAttributes();
                    }
                });
                if ($stateParams.mode == "IND") {
                    loadAttributes();
                }
                $scope.$on('app.stores.indents.nextPageDetails', nextPage);
                $scope.$on('app.stores.indents.previousPageDetails', previousPage);
                $scope.$on('app.Store.indent.attributes', function () {
                    showAttributes();
                });
                loadCurrencies();
            })();
        }
    }
)
;