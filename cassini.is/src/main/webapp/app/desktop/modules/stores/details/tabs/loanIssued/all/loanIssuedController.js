/**
 * Created by swapna on 10/08/18.
 */
define(['app/desktop/modules/stores/store.module',
        'app/shared/services/pm/project/projectService',
        'app/shared/services/store/loanService',
        'app/shared/services/store/topStoreService',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectTypeAttributeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService'

    ],
    function (module) {
        module.controller('LoanIssuedController', LoanIssuedController);

        function LoanIssuedController($scope, $rootScope, $sce, $window, $state, LoanService, ObjectAttributeService,
                                      $stateParams, TopStoreService, ProjectService, ObjectTypeAttributeService, ItemService, AttributeAttachmentService) {
            var vm = this;
            $rootScope.storeLoanIssueList = [];
            vm.objectIds = [];
            vm.showDetails = showDetails;
            vm.openAttachment = openAttachment;
            vm.removeAttribute = removeAttribute;
            vm.showImage = showImage;
            var currencyMap = new Hashtable();
            vm.requiredObjectAttributes = [];
            vm.objectAttributes = [];

            vm.loanAttributes = [];
            vm.itemIds = [];
            var pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "createdDate",
                    order: "DESC"
                }
            };

            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };

            $rootScope.storeLoanIssueList = pagedResults;

            function back() {
                $window.history.back();
            }

            function loadLoans() {
                vm.loading = true;
                LoanService.getPagedLoansIssued($rootScope.storeId, pageable).then(
                    function (loans) {
                        vm.loading = false;
                        $rootScope.storeLoanIssueList = loans;
                        if ($stateParams.mode == "LOANISSUE") {
                            $rootScope.viewInfo.title = "Loan Issues";
                        }
                        ProjectService.getProjectReferences($rootScope.storeLoanIssueList.content, 'fromProject');
                        ProjectService.getProjectReferences($rootScope.storeLoanIssueList.content, 'toProject');
                        TopStoreService.getStoreReferences($rootScope.storeLoanIssueList.content, 'toStore');
                        loadAttributeValues();
                    }
                );
            }

            function loadAttributeValues() {
                vm.objectIds = [];
                vm.attributeIds = [];
                vm.requiredAttributeIds = [];
                angular.forEach($rootScope.storeLoanIssueList.content, function (obj) {
                    obj.refValueString = null;
                    vm.objectIds.push(obj.id);
                });
                angular.forEach(vm.objectAttributes, function (objAttr) {
                    if (objAttr.id != null && objAttr.id != "" && objAttr.id != 0) {
                        vm.attributeIds.push(objAttr.id);
                    }
                });

                angular.forEach(vm.requiredObjectAttributes, function (reqAttr) {
                    if (reqAttr.id != null && reqAttr.id != "" && reqAttr.id != 0) {
                        vm.requiredAttributeIds.push(reqAttr.id);
                    }
                });

                if (vm.objectIds.length > 0 && vm.attributeIds.length > 0) {
                    ItemService.getAttributesByObjectIdsAndAttributeIds(vm.objectIds, vm.attributeIds).then(
                        function (data) {
                            vm.selectedObjectAttributes = data;
                            var map = new Hashtable();
                            angular.forEach(vm.objectAttributes, function (att) {
                                if (att.id != null && att.id != "" && att.id != 0) {
                                    map.put(att.id, att);
                                }
                            });

                            angular.forEach($rootScope.storeLoanIssueList.content, function (item) {
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
                                                        vm.objectAttachments = data;
                                                        item[attributeName] = vm.objectAttachments;
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

                if (vm.objectIds.length > 0 && vm.requiredAttributeIds.length > 0) {
                    ItemService.getAttributesByObjectIdsAndAttributeIds(vm.objectIds, vm.requiredAttributeIds).then(
                        function (data) {
                            vm.requiredAttributes = data;
                            var map = new Hashtable();
                            angular.forEach(vm.requiredObjectAttributes, function (att) {
                                if (att.id != null && att.id != "" && att.id != 0) {
                                    map.put(att.id, att);
                                }
                            });

                            angular.forEach($rootScope.storeLoanIssueList.content, function (item) {
                                var attributes = [];

                                var objectAttributes = vm.requiredAttributes[item.id];
                                if (objectAttributes != null && objectAttributes != undefined) {
                                    attributes = attributes.concat(objectAttributes);
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
                                                        vm.requiredObjectAttachments = data;
                                                        item[attributeName] = vm.requiredObjectAttachments;
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

            }

            function removeAttribute(att) {
                vm.objectAttributes.remove(att);
            }

            vm.showRequiredImage = showRequiredImage;

            function showRequiredImage(attribute) {
                var modal = document.getElementById('issueModal1');
                var modalImg = document.getElementById('issueImg1');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage12")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            function showImage(attribute) {
                var modal = document.getElementById('issueModal');
                var modalImg = document.getElementById('issueImg');

                modal.style.display = "block";
                modalImg.src = attribute;

                var span = document.getElementsByClassName("closeImage11")[0];

                span.onclick = function () {
                    modal.style.display = "none";
                }
            }

            function openAttachment(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/download".
                    format(window.location.protocol, window.location.host,
                    attachment.id);
                launchUrl(url);
            }

            function showDetails(loan) {
                $rootScope.loanId = loan.id;
                $state.go('app.store.stock.loanIssuedDetails', {loanId: loan.id});
            }

            function loadStore() {
                TopStoreService.getTopStore($rootScope.storeId).then(
                    function (data) {
                        $rootScope.viewInfo.description = "Store : " + data.storeName;
                    }
                )
            }

            function showLoanIssuedAttributes() {
                var options = {
                    title: 'Loan Attributes',
                    showMask: true,
                    template: 'app/desktop/modules/home/attributes/allAttributesView.jsp',
                    controller: 'AllAttributesController as allAttributesVm',
                    resolve: 'app/desktop/modules/home/attributes/allAttributesController',
                    width: 600,
                    data: {
                        selectedAttributes: vm.objectAttributes,
                        attributesMode: 'LOAN'
                    },
                    buttons: [
                        {text: 'Add', broadcast: 'app.items.attributes.select'}
                    ],
                    callback: function (result) {
                        vm.objectAttributes = result;
                        $window.localStorage.setItem("loanIssueAttributes", JSON.stringify(vm.objectAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage("Attributes added successfully");
                        }
                        loadAttributes();
                        $rootScope.hideSidePanel('right');
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("loanIssueAttributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            function loadAttributes() {
                if (validateJSON()) {
                    var setAttributes = JSON.parse($window.localStorage.getItem("loanIssueAttributes"));
                } else {
                    var setAttributes = null;
                }

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
                                $window.localStorage.setItem("loanIssueAttributes", "");
                                vm.objectAttributes = setAttributes
                            } else {
                                vm.objectAttributes = setAttributes;
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

            function loadRequiredAttributes() {
                ItemService.getTypeAttributesRequiredTrue("LOAN").then(
                    function (data) {
                        vm.requiredObjectAttributes = data;
                        loadLoans();
                    }
                )
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

            function nextPage() {
                if ($rootScope.storeLoanIssueList.last != true) {
                    pageable.page++;
                    loadLoans();
                }
            }

            function previousPage() {
                if ($rootScope.storeLoanIssueList.first != true) {
                    pageable.page--;
                    loadLoans();
                }
            }

            (function () {
                $scope.$on('app.store.tabactivated', function (event, data) {
                    if (data.tabId == 'details.loanIssued') {
                        loadAttributes();
                        loadCurrencies();
                        loadStore();
                    }
                });
                if ($stateParams.mode == "LOANISSUE") {
                    loadAttributes();
                    loadCurrencies();
                    loadStore();
                }
                $scope.$on('app.stores.loanIssue.nextPageDetails', nextPage);
                $scope.$on('app.stores.loanIssue.previousPageDetails', previousPage);
                $scope.$on('app.Store.loan.attributes', function () {
                    showLoanIssuedAttributes();
                })
            })();
        }
    }
)
;