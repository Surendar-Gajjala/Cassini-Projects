define(
    [
        'app/desktop/modules/reqTemplate/reqDocTemplate.module',
        'app/desktop/directives/all-view-attributes/allViewAttributesDirective',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/shared/services/core/reqDocTemplateService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('ReqDocTemplateController', ReqDocTemplateController);

        function ReqDocTemplateController($scope, $rootScope, $translate, $timeout, $state, $window, DialogService, $application, $stateParams, $cookies, $sce,
                                          CommonService, ReqDocTemplateService) {

            var vm = this;
            $rootScope.viewInfo.title = "";
            $rootScope.viewInfo.showDetails = false;
            vm.loading = false;
            vm.newReqDocTemplate = newReqDocTemplate;
            vm.reqDocTemplates = [];
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
            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.page = 0;
                vm.pageable.size = page;
                loadReqDocTemplates();
            }

            vm.filters = {
                name: null,
                description: null,
                searchQuery: null
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

            $scope.freeTextQuery = null;

            vm.reqDocTemplates = angular.copy(pagedResults);

            function nextPage() {
                if (vm.reqDocuments.last != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page++;
                    vm.flag = false;
                    loadReqDocTemplates();
                }
            }

            function previousPage() {
                if (vm.reqDocuments.first != true) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    vm.pageable.page--;
                    vm.flag = false;
                    loadReqDocTemplates();
                }
            }

            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.filters.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    loadReqDocTemplates();
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
                loadReqDocTemplates();
            }


            function loadReqDocTemplates() {
                $rootScope.showBusyIndicator();
                vm.loading=true;
                ReqDocTemplateService.getAllReqDocTemplates(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.reqDocTemplates = data;
                        CommonService.getPersonReferences(vm.reqDocTemplates.content, 'modifiedBy');
                        CommonService.getPersonReferences(vm.reqDocTemplates.content, 'createdBy');
                        vm.loading=false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.showReqDocTemplate = showReqDocTemplate;
            function showReqDocTemplate(template) {
                $state.go('app.req.document.template.details', {reqDocId: template.id, tab: 'details.basic'});
            }

            var parsed = angular.element("<div></div>");
            var create = parsed.html($translate.instant("CREATE")).html();
            $scope.cannotDeleteApprovedDoc = parsed.html($translate.instant("CANNOT_DELETE_APPROVED_REQ_DOC")).html();
            var reqDocTemplateTitle = parsed.html($translate.instant("REQ_DOC_TEMPLATE")).html();
            var reqDocTemPlateCreatedMsg = parsed.html($translate.instant("REQ_DOC__TEMPLATE_CREATED_SUCCESS")).html();

            function newReqDocTemplate() {
                var options = {
                    title: reqDocTemplateTitle,
                    template: 'app/desktop/modules/reqTemplate/new/newReqDocTemplateView.jsp',
                    controller: 'NewReqDocTemplateController as newReqDocTemplateVm',
                    resolve: 'app/desktop/modules/reqTemplate/new/newReqDocTemplateController',
                    width: 650,
                    showMask: true,
                    data: {
                        documentRevision: null
                    },
                    buttons: [
                        {text: create, broadcast: 'app.req.doc.template.new'}
                    ],
                    callback: function (result) {
                        $timeout(function () {
                            loadReqDocTemplates();
                        }, 500);
                        $rootScope.showSuccessMessage(reqDocTemPlateCreatedMsg);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            var deleteSubstances = parsed.html($translate.instant("DELETE_REQ_DOC_TEMPLATE")).html();
            var deleteDialogMessage = parsed.html($translate.instant("DELETE_TYPE_DIALOG_MESSAGE")).html();
            var reqDocDeleteMsg = parsed.html($translate.instant("REQ_DOC_TEMPLATE_DELETE")).html();


            vm.deleteReqDocTemplate = deleteReqDocTemplate;
            function deleteReqDocTemplate(template) {
                var options = {
                    title: deleteSubstances,
                    message: deleteDialogMessage + " [ " + template.name + " ] " + "?",
                    okButtonClass: 'btn-danger'
                };
                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        if (template.id != null && template.id != undefined) {
                            ReqDocTemplateService.deleteReqDocTemplate(template.id).then(
                                function (data) {
                                    var index = vm.reqDocTemplates.content.indexOf(template);
                                    vm.reqDocTemplates.content.splice(index, 1);
                                    vm.reqDocTemplates.totalElements--;
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


            (function () {
                loadReqDocTemplates();
            })();

        }
    }
);