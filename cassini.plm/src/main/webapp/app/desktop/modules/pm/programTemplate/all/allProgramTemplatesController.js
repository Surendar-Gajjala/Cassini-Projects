define(
    [
        'app/desktop/modules/pm/pm.module',
        'app/shared/services/core/programTemplateService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('AllProgramTemplatesController', AllProgramTemplatesController);

        function AllProgramTemplatesController($scope, $rootScope, $state, $stateParams, $cookies, $timeout, $translate, ProgramTemplateService,
                                               DialogService, CommonService, $window) {

            var vm = this;
            $rootScope.viewInfo.icon = "fa fa-wpforms";
            $rootScope.viewInfo.title = $translate.instant('PROGRAM_TEMPLATES');
            $rootScope.viewInfo.showDetails = false;

            var parsed = angular.element("<div></div>");

            vm.deleteTemplateTitle = parsed.html($translate.instant("DELETE_TEMPLATE_TITLE")).html();
            vm.openTemplateTitle = parsed.html($translate.instant("OPEN_TEMPLATE_TITLE")).html();
            vm.newTemplateTitle = parsed.html($translate.instant("NEW_TEMPLATE_TITLE")).html();
            var newTemplate = parsed.html($translate.instant("NEW_TEMPLATE")).html();
            var deleteTemplateTitle = parsed.html($translate.instant("DELETE_TEMPLATE")).html();
            var templateCreatedMessage = parsed.html($translate.instant("TEMPLATE_CREATE_MESSAGE")).html();
            var templateUpdatedMessage = parsed.html($translate.instant("TEMPLATE_UPDATE_MESSAGE")).html();
            var templateDeletedMessage = parsed.html($translate.instant("TEMPLATE_DELETED_MESSAGE")).html();
            var deleteTemplateTitleMessage = parsed.html($translate.instant("DELETE_TYPE_DIALOG_MESSAGE")).html();
            var itemDelete = parsed.html($translate.instant("ITEMDELETE")).html();
            var createTitle = parsed.html($translate.instant("CREATE")).html();


            vm.loading = true;

            vm.showNewTemplate = showNewTemplate;
            vm.deleteTemplate = deleteTemplate;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.freeTextSearch = freeTextSearch;
            vm.resetPage = resetPage;

            vm.searchText = null;
            vm.filterSearch = null;

            vm.filters = {
                searchQuery: null,
            };
            $scope.freeTextQuery = null;

            function nextPage() {
                if (vm.programTemplates.last != true) {
                    vm.pageable.page++;
                    loadProgramTemplates();
                }
            }

            function previousPage() {
                if (vm.programTemplates.first != true) {
                    vm.pageable.page--;
                    loadProgramTemplates();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.page = 0;
                vm.pageable.size = page;
                loadProgramTemplates();
            }

            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            vm.recentlyVisited = {
                id: null,
                objectId: null,
                objectType: null,
                person: null,
                visitedDate: null
            };

            vm.showTemplate = showTemplate;

            function showTemplate(template) {
                $state.go('app.pm.programtemplate.details', {templateId: template.id, tab: "details.basic"});
            }

            function loadProgramTemplates() {
                $rootScope.showBusyIndicator();
                vm.loading = true;
                ProgramTemplateService.getAllProgramTemplates(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.programTemplates = data;
                        CommonService.getPersonReferences(vm.programTemplates.content, 'createdBy');
                        CommonService.getPersonReferences(vm.programTemplates.content, 'modifiedBy');
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function showNewTemplate() {
                var options = {
                    title: newTemplate,
                    showMask: true,
                    template: 'app/desktop/modules/pm/programTemplate/new/newProgramTemplateView.jsp',
                    controller: 'NewProgramTemplateController as newProgramTemplateVm',
                    resolve: 'app/desktop/modules/pm/programTemplate/new/newProgramTemplateController',
                    width: 550,
                    data: {
                        selectedProgramTemplateId: null
                    },
                    buttons: [
                        {text: createTitle, broadcast: 'app.program.template.new'}
                    ],
                    callback: function () {
                        loadProgramTemplates();
                        $rootScope.showSuccessMessage(templateCreatedMessage);
                    }
                };

                $rootScope.showSidePanel(options);
            }


            function freeTextSearch(freeText) {
                vm.pageable.page = 0;
                $rootScope.showBusyIndicator($('.view-container'));
                if (freeText != null && freeText != "" && freeText != undefined) {
                    vm.filters.searchQuery = freeText;
                    $scope.freeTextQuery = freeText;
                    loadProgramTemplates();
                } else {
                    resetPage();
                }
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

            function resetPage() {
                vm.programTemplates = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                $rootScope.showBusyIndicator($('.view-container'));
                loadProgramTemplates();
            }


            function deleteTemplate(template) {
                var options = {
                    title: deleteTemplateTitle,
                    message: deleteTemplateTitleMessage + " [ " + template.name + " ] " + itemDelete + "?",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        ProgramTemplateService.deleteProgramTemplate(template.id).then(
                            function () {
                                var index = vm.programTemplates.content.indexOf(template);
                                vm.programTemplates.content.splice(index, 1);
                                loadProgramTemplates();
                                $rootScope.showSuccessMessage(templateDeletedMessage);
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                            }
                        )
                    }
                })
            }

            (function () {
                loadProgramTemplates();
                $window.localStorage.setItem("template_open_from", null);
            })();
        }
    }
);