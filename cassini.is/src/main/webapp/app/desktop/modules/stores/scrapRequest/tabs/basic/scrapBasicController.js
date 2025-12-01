define(
    [
        'app/desktop/modules/stores/store.module',
        'app/shared/services/store/scrapService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/pm/project/projectService',
        'app/shared/services/store/topStoreService',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDetailsDirectiveController'
    ],
    function (module) {
        module.controller('ScrapBasicController', ScrapBasicController);

        function ScrapBasicController($scope, $rootScope, $timeout, $state, $stateParams, CommonService, TopStoreService, ProjectService,
                                      ScrapService) {

            var vm = this;
            vm.back = back;
            vm.scrap = null;
            vm.loading = true;
            vm.updateScrap = updateScrap;

            function loadScrap() {
                ScrapService.get($stateParams.scrapDetailsId).then(
                    function (data) {
                        vm.scrap = data;
                        $rootScope.viewInfo.title = "Scrap : " + vm.scrap.scrapNumber;

                        vm.loading = false;
                        CommonService.getPerson(vm.scrap.requestedBy).then(
                            function (person) {
                                vm.scrap.requestedByPerson = person.firstName;
                            }
                        );
                        TopStoreService.getTopStore(vm.scrap.store).then(
                            function (store) {
                                vm.scrap.storeName = store.storeName;
                            }
                        );
                        if (vm.scrap.project != null) {
                            ProjectService.getProject(vm.scrap.project).then(
                                function (project) {
                                    vm.scrap.projectName = project.name;
                                }
                            )
                        }
                    }
                );
            }

            function back() {
                window.history.back();
            }

            function updateScrap(scrap) {
                ScrapService.updateScrap(scrap).then(
                    function (data) {
                        loadScrap();
                        $rootScope.showSuccessMessage("Scrap updated successfully");
                    }
                );
            }

            (function () {
                loadScrap();
            })();
        }
    }
);