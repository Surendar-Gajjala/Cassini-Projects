define(
    [
        'app/desktop/modules/pqm/pqm.module',
        'app/shared/services/core/problemReportService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/mfrService',
        'app/shared/services/core/mfrPartsService',
        'app/shared/services/core/workflowDefinitionService',
        'app/shared/services/core/projectService',
        'app/shared/services/core/specificationsService',
        'app/desktop/directives/all-view-attributes/allViewAttributesDirective',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('AllProblemReportController', AllProblemReportController);

        function AllProblemReportController($scope, $rootScope, $translate, $timeout, $state, $window, $stateParams, $application, $cookies, $sce, ItemService,
                                            ProblemReportService, DialogService, ECOService, WorkflowDefinitionService, ObjectTypeAttributeService, RecentlyVisitedService,
                                            MfrService, MfrPartsService, AttributeAttachmentService, CommonService, ProjectService, SpecificationsService) {

            $rootScope.viewInfo.icon = "fa fa-calendar";
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            var vm = this;
            vm.loading = false;
            var parsed = angular.element("<div></div>");
            $scope.createProblemReportTitle = parsed.html($translate.instant("CREATE_NEW_PROBLEM_REPORT")).html();
            var newProblemReportTitle = parsed.html($translate.instant("NEW_PROBLEM_REPORT")).html();
            var createButton = parsed.html($translate.instant("CREATE")).html();
            var deleteDialogTitle = parsed.html($translate.instant("DELETE_PROBLEM_REPORT")).html();
            var deleteProblemReportDialogMessage = parsed.html($translate.instant("DELETE_PROBLEM_REPORT_DIALOG_MESSAGE")).html();
            var problemReportDeletedMessage = parsed.html($translate.instant("PROBLEM_REPORT_DELETED_MESSAGE")).html();
            vm.RemoveColumnTitle = parsed.html($translate.instant("REMOVE_ATTRIBUTE_COLUMN")).html();
            vm.showAttributes = parsed.html($translate.instant("SHOW_ATTRIBUTES")).html();
            $scope.cannotDeleteApprovedProblemReport = parsed.html($translate.instant("CANNOT_DELETE_APPROVED_PROBLEM")).html();

            vm.newProblemReport = newProblemReport;
            vm.showProblemReport = showProblemReport;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;
            var currencyMap = new Hashtable();
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

            vm.prFilter = {
                prNumber: null,
                prType: '',
                problem: null,
                searchQuery: null,
                product: null,
                qcr: '',
                ecr: '',
                released: false
            };


            vm.problemReports = angular.copy(pagedResults);

            function newProblemReport() {
                var options = {
                    title: newProblemReportTitle,
                    template: 'app/desktop/modules/pqm/problemReport/new/newProblemReportView.jsp',
                    controller: 'NewProblemReportController as newProblemReportVm',
                    resolve: 'app/desktop/modules/pqm/problemReport/new/newProblemReportController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: createButton, broadcast: 'app.problemReports.new'}
                    ],
                    callback: function (problemReport) {
                        $timeout(function () {
                            showProblemReport(problemReport)
                        }, 500);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function nextPage() {
                if (vm.problemReports.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadProblemReports();
                }
            }

            function previousPage() {
                if (vm.problemReports.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadProblemReports();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.page = 0;
                vm.pageable.size = page;
                loadProblemReports();
            }

            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.prFilter.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    loadProblemReports();
                } else {
                    resetPage();
                }
            }

            function resetPage() {
                vm.problemReports = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.prFilter.searchQuery = null;
                $scope.freeTextQuery = null;
                $rootScope.showBusyIndicator($('.view-container'));
                loadProblemReports();
            }

            function loadProblemReports() {
                $rootScope.showBusyIndicator();
                vm.loading=true;
                ProblemReportService.getAllProblemReports(vm.pageable, vm.prFilter).then(
                    function (data) {
                        vm.problemReports = data;
                        loadAttributeValues();
                        vm.loading=false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.objectIds = [];
            vm.attributeIds = [];
            function loadAttributeValues() {
                vm.objectIds = [];
                angular.forEach(vm.problemReports.content, function (item) {
                    vm.objectIds.push(item.id);
                });
                $rootScope.getObjectAttributeValues(vm.objectIds, vm.attributeIds, vm.selectedAttributes, vm.problemReports.content);
            }

            vm.openAttachment = openAttachment;
            function openAttachment(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/download".
                    format(window.location.protocol, window.location.host,
                    attachment.id);
                $rootScope.downloadFileFromIframe(url);
            }


            function showProblemReport(problemReport) {
                vm.recentlyVisited = {};
                vm.recentlyVisited.objectId = problemReport.id;
                vm.recentlyVisited.objectType = "PROBLEMREPORT";
                vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                    function (data) {
                        $state.go("app.pqm.pr.details", {problemReportId: problemReport.id, tab: 'details.basic'});
                    }, function (error) {
                        $state.go("app.pqm.pr.details", {problemReportId: problemReport.id, tab: 'details.basic'});
                    }
                )
            }

            vm.deleteProblemReport = deleteProblemReport;
            function deleteProblemReport(report) {
                var options = {
                    title: deleteDialogTitle,
                    message: deleteProblemReportDialogMessage + " [" + report.prNumber + "] ?",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        $rootScope.showBusyIndicator($('.view-container'));
                        ProblemReportService.deleteProblemReport(report.id).then(
                            function (data) {
                                $rootScope.showSuccessMessage(problemReportDeletedMessage);
                                loadProblemReports();
                            }, function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        )
                    }
                });
            }

            var attributesTitle = $translate.instant("ATTRIBUTES");
            var addButton = parsed.html($translate.instant("ADD")).html();
            var selectedAttributesMessage = parsed.html($translate.instant("SELECTED_ATTRIBUTES_MESSAGE")).html();

            vm.showTypeAttributes = showTypeAttributes;
            vm.removeAttribute = removeAttribute;
            vm.selectedAttributes = [];
            function showTypeAttributes() {
                var options = {
                    title: attributesTitle,
                    template: 'app/desktop/modules/pqm/attributes/qualityTypeAttributeSelectionView.jsp',
                    resolve: 'app/desktop/modules/pqm/attributes/qualityTypeAttributeSelectionController',
                    controller: 'QualityTypeAttributeSelectionController as qualityTypeAttributeSelectionVm',
                    width: 500,
                    showMask: true,
                    data: {
                        selectedAttributes: vm.selectedAttributes,
                        selectedObjectType: "PRTYPE",
                        selectedObject: "PROBLEMREPORT",
                        selectedParentObjectType: "QUALITY"
                    },
                    buttons: [
                        {text: addButton, broadcast: 'add.quality.attributes.select'}
                    ],
                    callback: function (result) {
                        vm.selectedAttributes = result;
                        $window.localStorage.setItem("problemReportAttributes", JSON.stringify(vm.selectedAttributes));
                        if (result.length > 0) {
                            $rootScope.showSuccessMessage(selectedAttributesMessage);
                        }
                        loadProblemReports();
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function removeAttribute(att) {
                vm.selectedAttributes.remove(att);
                $window.localStorage.setItem("problemReportAttributes", JSON.stringify(vm.selectedAttributes));
            }

            function validateJSON() {
                try {
                    JSON.parse($window.localStorage.getItem("problemReportAttributes"));
                } catch (e) {
                    return false;
                }
                return true;
            }

            vm.showAttributeDetails = showAttributeDetails;
            function showAttributeDetails(attribute) {
                if (attribute.objectType == 'ITEM') {
                    $state.go('app.items.details', {itemId: attribute.id});
                } else if (attribute.objectType == 'ITEMREVISION') {
                    $state.go('app.items.details', {itemId: attribute.id});
                } else if (attribute.objectType == 'CHANGE') {
                    $state.go('app.changes.ecos.details', {ecoId: attribute.id});
                } else if (attribute.objectType == 'PLMWORKFLOWDEFINITION') {
                    $state.go('app.workflow.editor', {mode: 'edit', workflow: attribute.id})
                } else if (attribute.objectType == 'MANUFACTURER') {
                    $state.go('app.mfr.details', {manufacturerId: attribute.id});
                } else if (attribute.objectType == 'MANUFACTURERPART') {
                    $state.go('app.mfr.mfrparts.details', {
                        mfrId: attribute.manufacturer,
                        manufacturePartId: attribute.id
                    });
                } else if (attribute.objectType == 'REQUIREMENT') {
                    $state.go('app.rm.requirements.details', {requirementId: attribute.id});
                } else if (attribute.objectType == 'PROJECT') {
                    $state.go('app.pm.project.details', {projectId: attribute.id});
                }
            }

            vm.showThumbnailImage = showThumbnailImage;
            function showThumbnailImage(item) {
                var modal = document.getElementById('item-thumbnail' + item.id);
                modal.style.display = "block";

                var span = document.getElementById("thumbnail-close" + item.id);
                $("#thumbnail-image" + item.id).width($('#thumbnail-view' + item.id).outerWidth());
                $("#thumbnail-image" + item.id).height($('#thumbnail-view' + item.id).outerHeight());

                span.onclick = function () {
                    modal.style.display = "none";
                }
                $scope.$evalAsync();
            }

            (function () {
                //if ($application.homeLoaded == true) {
                $rootScope.getQualityAttributes("QUALITY", "PRTYPE", vm.attributeIds);
                angular.forEach($application.currencies, function (data) {
                    currencyMap.put(data.id, $sce.trustAsHtml(data.symbol));
                })
                var setAttributes = null;
                if (validateJSON()) {
                    setAttributes = JSON.parse($window.localStorage.getItem("problemReportAttributes"));
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
                                $window.localStorage.setItem("problemReportAttributes", "");
                                vm.selectedAttributes = setAttributes
                            } else {
                                vm.selectedAttributes = setAttributes;
                            }
                            loadProblemReports();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                        }
                    )
                } else {
                    loadProblemReports();
                }
                //}
            })();
        }
    }
);


