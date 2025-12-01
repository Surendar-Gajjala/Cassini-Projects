/**
 * Created by Anusha on 13-08-2016.
 */
define(['app/desktop/modules/widgets/departments/departmentWidget.module',
        'app/shared/services/departmentService'
    ],
    function (module) {
        module.controller('DepartmentWidgetController', DepartmentWidgetController);

        function DepartmentWidgetController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $uibModal, DepartmentService) {
            var vm = this;

            vm.departments = [];
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.loading = true;

            var pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
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
            vm.departments = angular.copy(pagedResults);

            function nextPage() {
                if (vm.departments.last != true) {
                    pageable.page++;
                    loadDepartments();
                }
            }

            function previousPage() {
                if (vm.departments.first != true) {
                    pageable.page--;
                    loadDepartments();
                }
            }

            function loadDepartments() {
                DepartmentService.getPagedDepartments(pageable).then(
                    function (data) {
                        vm.departments = data;
                    });
                vm.loading = false;
            }


            (function () {

                loadDepartments();

            })();
        }
    }
);

