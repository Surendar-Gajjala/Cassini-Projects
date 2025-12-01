define(
    [
        'app/desktop/modules/req/req.module',
        'app/shared/services/core/reqDocumentService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/desktop/directives/all-view-attributes/allViewAttributesDirective',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective',
        'app/desktop/modules/directives/reqObjectTypeDirective'
    ],
    function (module) {
        module.controller('ReqDocumentsController', ReqDocumentsController);

        function ReqDocumentsController($scope, $rootScope, $translate, $timeout, $state, $window, DialogService, $application, $stateParams, $cookies, $sce,
                                        ObjectTypeAttributeService, CommonService, ReqDocumentService) {

            var vm = this;
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            vm.loading = false;
            vm.newReqDocument = newReqDocument;
            vm.reqDocuments = [];

            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;

            vm.searchText = null;
            vm.filterSearch = null;


            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            vm.filters = {
                number: null,
                type: '',
                owner: '',
                name: null,
                description: null,
                searchQuery: null,
                phase: null,
                project: ''
            };

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.page = 0;
                vm.pageable.size = page;
                loadReqDocuments();
            }

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

            vm.clearTypeSelection = clearTypeSelection;
            function clearTypeSelection() {
                vm.pageable.page = 0;
                vm.selectedReqDocType = null;
                vm.filters.type = '';
                loadReqDocuments();
            }

            vm.onSelectType = onSelectType;
            function onSelectType(reqDocType) {
                vm.pageable.page = 0;
                vm.selectedReqDocType = reqDocType;
                vm.filters.type = reqDocType.id;
                vm.filters.freeTextSearch = false;
                loadReqDocuments();
            }

            $scope.clearOwner = clearOwner;
            function clearOwner() {
                $scope.selectedPerson = null;
                vm.filters.owner = '';
                loadReqDocuments();
                $rootScope.hideBusyIndicator();
            }

            $scope.owners = [];
            function loadOwners() {
                ReqDocumentService.getOwners().then(
                    function (data) {
                        $scope.owners = data;
                    }
                )
            }

            $scope.selectedPerson = null;
            $scope.onSelectOwner = onSelectOwner;
            function onSelectOwner(person) {
                vm.pageable.page = 0;
                $scope.selectedPerson = person;
                vm.filters.owner = person.id;
                loadReqDocuments();
            }

            $scope.freeTextQuery = null;

            vm.reqDocuments = angular.copy(pagedResults);

            var parsed = angular.element("<div></div>");
            var create = parsed.html($translate.instant("CREATE")).html();
            var newReqDocumentHeading = parsed.html($translate.instant("NEW_REQUIREMENT_DOC")).html();
            $scope.cannotDeleteApprovedDoc = parsed.html($translate.instant("CANNOT_DELETE_APPROVED_REQ_DOC")).html();

            function newReqDocument() {
                var options = {
                    title: newReqDocumentHeading,
                    template: 'app/desktop/modules/req/reqdocs/new/newReqDocumentView.jsp',
                    controller: 'NewReqDocumentController as newReqDocumentVm',
                    resolve: 'app/desktop/modules/req/reqdocs/new/newReqDocumentController',
                    width: 675,
                    showMask: true,
                    buttons: [
                        {text: create, broadcast: 'app.req.doc.new'}
                    ],
                    callback: function (result) {
                        $timeout(function () {
                            loadReqDocuments();
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }


            function nextPage() {
                if (vm.reqDocuments.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadReqDocuments();
                }
            }

            function previousPage() {
                if (vm.reqDocuments.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadReqDocuments();
                }
            }

            vm.lifecyclePhases = [];
            function loadReqDocTypeLifecycles() {
                vm.lifecyclePhases = [];
                ReqDocumentService.getReqDocTypeLifecycles().then(
                    function (data) {
                        vm.docTypeLifecycles = data;
                        angular.forEach(vm.docTypeLifecycles, function (lifecycle) {
                            angular.forEach(lifecycle.phases, function (phase) {
                                if (vm.lifecyclePhases.indexOf(phase.phase) == -1) {
                                    vm.lifecyclePhases.push(phase.phase);
                                }
                            })
                        })
                    }
                )
            }

            vm.selectedPhase = null;
            vm.onSelectPhase = onSelectPhase;
            function onSelectPhase(phase) {
                vm.pageable.page = 0;
                vm.selectedPhase = phase;
                vm.filters.phase = phase;
                loadReqDocuments();
            }


            vm.clearPhase = clearPhase;
            function clearPhase() {
                vm.pageable.page = 0;
                vm.selectedPhase = null;
                vm.filters.phase = null;
                loadReqDocuments();
            }

            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.filters.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    loadReqDocuments();
                } else {
                    resetPage();
                }
            }

            function resetPage() {
                vm.reqDocuments = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                $rootScope.showBusyIndicator($('.view-container'));
                loadReqDocuments();
            }

            function loadReqDocuments() {
                $rootScope.showBusyIndicator();
                vm.loading = true;
                ReqDocumentService.getAllReqDocuments(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.reqDocuments = data;
                        loadOwners();
                        loadReqDocTypeLifecycles();
                        loadAttributeValues();
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.objectIds = [];
            vm.attributeIds = [];
            vm.selectedAttributes = [];
            function loadAttributeValues() {
                vm.objectIds = [];
                vm.attributeIds = [];
                angular.forEach(vm.reqDocuments.content, function (item) {
                    vm.objectIds.push(item.latestRevision);
                });
                angular.forEach(vm.selectedAttributes, function (selectedAttribute) {
                    if (selectedAttribute.id != null && selectedAttribute.id != "" && selectedAttribute.id != 0) {
                        vm.attributeIds.push(selectedAttribute.id);
                    }
                });
                $rootScope.getObjectAttributeValues(vm.objectIds, vm.attributeIds, vm.selectedAttributes, vm.reqDocuments.content);
            }

            vm.showReqDocument = showReqDocument;
            function showReqDocument(req) {
                $state.go('app.req.document.details', {reqId: req.latestRevision, tab: 'details.basic'});
            }

            var deleteSubstances = parsed.html($translate.instant("DELETE_REQ_DOCUMENT")).html();
            var deleteDialogMessage = parsed.html($translate.instant("DELETE_TYPE_DIALOG_MESSAGE")).html();
            var reqDocDeleteMsg = parsed.html($translate.instant("REQ_DOCUMENT_DELETE_MSG")).html();


            vm.deleteReqDocument = deleteReqDocument;
            function deleteReqDocument(reqDoc) {
                var options = {
                    title: deleteSubstances,
                    message: deleteDialogMessage + " [ " + reqDoc.number + " ] " + "?",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        if (reqDoc.id != null && reqDoc.id != undefined) {
                            ReqDocumentService.deleteReqDocument(reqDoc.id).then(
                                function (data) {
                                    var index = vm.reqDocuments.content.indexOf(reqDoc);
                                    vm.reqDocuments.content.splice(index, 1);
                                    vm.reqDocuments.totalElements--;
                                    $rootScope.showSuccessMessage(reqDocDeleteMsg);
                                },
                                function (error) {
                                    $rootScope.showErrorMessage(error.message);
                                }
                            )
                        }
                    }
                });
            }

            var currencyMap = new Hashtable();
            var plantsAttributeTitle = parsed.html($translate.instant("ATTRIBUTES")).html();
            var selectedAttributesAdded = parsed.html($translate.instant("SELECTED_ATTRIBUTES_MESSAGE")).html();
            var addButton = parsed.html($translate.instant("ADD")).html();

            vm.showTypeAttributes = showTypeAttributes;
            function showTypeAttributes() {
                var options = {
                    title: plantsAttributeTitle,
                    template: 'app/desktop/modules/shared/attributes/attributesView.jsp',
                    resolve: 'app/desktop/modules/shared/attributes/attributesController',
                    controller: 'AttributesController as attributesVm',
                    width: 500,
                    showMask: true,
                    data: {
                        selectedAttributes: vm.selectedAttributes,
                        type: "REQUIREMENTDOCUMENTTYPE",
                        objectType: "REQUIREMENTDOCUMENT"
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.select.attributes'}
                    ],
                    callback: function (result) {
                        vm.selectedAttributes = result;
                        $window.localStorage.setItem("reqDocAttributes", JSON.stringify(vm.selectedAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesAdded);
                        }
                        loadReqDocuments();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            var revisionHistoryTitle = $translate.instant("REVISION_HISTORY_TITLE");
            vm.showDocumentRevisionHistory = showDocumentRevisionHistory;
            function showDocumentRevisionHistory(document) {
                var options = {
                    title: document.number + " - " + revisionHistoryTitle,
                    template: 'app/desktop/modules/item/details/tabs/basic/itemRevisionHistoryView.jsp',
                    controller: 'ItemRevisionHistoryController as revHistoryVm',
                    resolve: 'app/desktop/modules/item/details/tabs/basic/itemRevisionHistoryController',
                    data: {
                        itemId: document.id,
                        revisionHistoryType: "REQUIREMENTDOCUMENT"
                    },
                    width: 700,
                    showMask: true,
                };

                $rootScope.showSidePanel(options);
            }

            vm.removeAttribute = removeAttribute;
            function removeAttribute(att) {
                vm.selectedAttributes.remove(att);
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("reqDocAttributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            (function () {
                angular.forEach($application.currencies, function (data) {
                    currencyMap.put(data.id, $sce.trustAsHtml(data.symbol));
                })
                var setAttributes = null;
                if (validateJSON()) {
                    setAttributes = JSON.parse($window.localStorage.getItem("reqDocAttributes"));
                } else {
                    setAttributes = null;
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
                                $window.localStorage.setItem("reqDocAttributes", "");
                                vm.selectedAttributes = setAttributes
                            } else {
                                vm.selectedAttributes = setAttributes;
                            }
                            loadReqDocuments();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                } else {
                    loadReqDocuments();
                                       
                }
            })();

        }
    }
);

