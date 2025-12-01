define(
    [
        'app/desktop/modules/template/template.module',
        'app/shared/services/core/projectTemplateService',
        'app/shared/services/core/recentlyVisitedService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective'
    ],
    function (module) {
        module.controller('AllTemplatesController', AllTemplatesController);

        function AllTemplatesController($scope, $rootScope, $state, $stateParams, $cookies, $timeout, $translate, ProjectTemplateService,
                                        DialogService, RecentlyVisitedService, CommonService, $window) {

            var vm = this;
            $rootScope.viewInfo.icon = "fa fa-wpforms";
            $rootScope.viewInfo.title = $translate.instant('TEMPLATE');
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
                if (vm.projectTemplates.last != true) {
                    vm.pageable.page++;
                    loadProjectTemplates();
                }
            }

            function previousPage() {
                if (vm.projectTemplates.first != true) {
                    vm.pageable.page--;
                    loadProjectTemplates();
                }
            }

            vm.pageSize = pageSize;
            function pageSize(page) {
                vm.pageable.page = 0;
                vm.pageable.size = page;
                loadProjectTemplates();
            }

            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "Desc"
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
                var session = JSON.parse(localStorage.getItem('local_storage_login'));
                $rootScope.loginPersonDetails = session.login;
                $state.go('app.templates.details', {templateId: template.id});
                vm.recentlyVisited.objectId = template.id;
                vm.recentlyVisited.objectType = template.objectType;
                vm.recentlyVisited.person = $rootScope.loginPersonDetails.person.id;
                RecentlyVisitedService.createRecentlyVisited(vm.recentlyVisited).then(
                    function (data) {

                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            function loadProjectTemplates() {
                $rootScope.showBusyIndicator();
                vm.loading = true;
                ProjectTemplateService.getAllProjectTemplates(vm.pageable, vm.filters).then(
                    function (data) {
                        vm.projectTemplates = data;
                        CommonService.getPersonReferences(vm.projectTemplates.content, 'createdBy');
                        CommonService.getPersonReferences(vm.projectTemplates.content, 'modifiedBy');
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
                    template: 'app/desktop/modules/template/new/newTemplateView.jsp',
                    controller: 'NewTemplateController as newTemplateVm',
                    resolve: 'app/desktop/modules/template/new/newTemplateController',
                    width: 550,
                    data: {
                        projectPlan: null
                    },
                    buttons: [
                        {text: createTitle, broadcast: 'app.template.new'}
                    ],
                    callback: function () {
                        loadProjectTemplates();
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
                    loadProjectTemplates();
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
                vm.projectTemplates = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.filters.searchQuery = null;
                $scope.freeTextQuery = null;
                $rootScope.showBusyIndicator($('.view-container'));
                loadProjectTemplates();
            }


            function deleteTemplate(template) {
                var options = {
                    title: deleteTemplateTitle,
                    message: deleteTemplateTitleMessage + " [ " + template.name + " ] " + itemDelete + "?",
                    okButtonClass: 'btn-danger'
                };

                DialogService.confirm(options, function (yes) {
                    if (yes == true) {
                        ProjectTemplateService.deleteProjectTemplate(template.id).then(
                            function () {
                                var index = vm.projectTemplates.content.indexOf(template);
                                vm.projectTemplates.content.splice(index, 1);
                                loadProjectTemplates();
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
                //if ($application.homeLoaded == true) {
                $window.localStorage.setItem("lastSelectedTemplateTab", JSON.stringify("details.plan"));
                $window.localStorage.setItem("template_open_from", null);
                loadProjectTemplates();
                //}
            })();
        }
    }
);