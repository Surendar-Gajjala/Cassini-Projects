define(
    [
        'app/desktop/modules/pm/pm.module',
        'app/shared/services/pm/project/projectService'
    ],
    function (module) {
        module.controller('NewProjectCloneController', NewProjectCloneController);

        function NewProjectCloneController($scope, $rootScope, $sce, ProjectService) {

            var vm = this;

            vm.projectClone = {
                id: null,
                name: null,
                description: null,
                portfolio: null,
                projectOwner: null,
                plannedStartDate: null,
                plannedFinishDate: null

            };

            $scope.trustAsHtml = function (value) {
                return $sce.trustAsHtml(value);
            };

            function saveAsProject() {
                if (validate()) {
                    vm.projectClone.projectOwner = vm.project.projectOwner;
                    vm.projectClone.portfolio = vm.projectClone.portfolio.id;
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    ProjectService.cloneProject(vm.project.id, vm.projectClone).then(
                        function (data) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showSuccessMessage("Project saved successfully");
                            $rootScope.hideSidePanel();
                        }, function (error) {
                            $rootScope.hideBusyIndicator();
                            $rootScope.showErrorMessage(error.message);
                            error.message = null;
                        }
                    )
                }
            }

            function validate() {

                var valid = true;
                /*

                 if (vm.projectClone.portfolio.name == null || vm.projectClone.portfolio.name == undefined || vm.projectClone.portfolio.name == "") {
                 vm.valid = false;
                 $rootScope.showWarningMessage(NameValidation);
                 }
                 else if (vm.projectClone.portfolio.get(vm.projectClone.portfolio.name) != null) {
                 vm.valid = false;
                 $rootScope.showWarningMessage("{0}".format(vm.projectClone.portfolio.name) + " " + NameExists);
                 }*/
                if (vm.projectClone.portfolio == null || vm.projectClone.portfolio == "" || vm.projectClone.portfolio == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Please select Portfolio");
                } else if (vm.projectClone.name == null || vm.projectClone.name == "" || vm.projectClone.name == undefined) {
                    valid = false;
                    $rootScope.showWarningMessage("Project Name cannot be empty");
                } else if (vm.projectClone.plannedStartDate == null ||
                    vm.projectClone.plannedStartDate == undefined || vm.projectClone.plannedStartDate == "") {
                    $rootScope.showErrorMessage("Planned Start Date cannot be empty");
                    valid = false;
                } else if (vm.projectClone.plannedFinishDate == null ||
                    vm.projectClone.plannedFinishDate == undefined || vm.projectClone.plannedFinishDate == "") {
                    $rootScope.showErrorMessage("Planned Finish Date cannot be empty");
                    valid = false;
                } else if (vm.projectClone.plannedStartDate != null) {
                    var today = moment(new Date());
                    var todayStr = today.format('DD/MM/YYYY');
                    var todayDate = moment(todayStr, 'DD/MM/YYYY');
                    var plannedStartDate = moment(vm.projectClone.plannedStartDate, 'DD/MM/YYYY');
                    var val1 = plannedStartDate.isSame(todayDate) || plannedStartDate.isAfter(todayDate);
                    if (!val1) {
                        $rootScope.showErrorMessage("Start Date should be on (or) after Today's Date");
                        valid = false;
                    } else if (vm.projectClone.plannedFinishDate == null || vm.projectClone.plannedFinishDate == undefined || vm.projectClone.plannedFinishDate == "") {
                        $rootScope.showErrorMessage("Planned Finish Date cannot be empty");
                        valid = false;
                    } else if (vm.projectClone.plannedStartDate != null && vm.projectClone.plannedFinishDate != null) {
                        var plannedFinishDate = moment(vm.projectClone.plannedFinishDate, 'DD/MM/YYYY');
                        var plannedStartDate = moment(vm.projectClone.plannedStartDate, 'DD/MM/YYYY');
                        var val = plannedFinishDate.isSame(plannedStartDate) || plannedFinishDate.isAfter(plannedStartDate);
                        if (!val) {
                            $rootScope.showErrorMessage("Planned Finish Date should be after Planned Start Date");
                            valid = false;
                        }
                    }
                }

                return valid;
            }

            function loadPortfolios() {
                ProjectService.getAllPortfolios().then(
                    function (data) {
                        vm.portfolios = data;
                    })
            }

            (function () {
                if ($application.homeLoaded == true) {
                    vm.project = $scope.data.projectPlan;
                    if (vm.project != null) {
                        vm.projectClone.name = vm.project.name;
                        vm.projectClone.description = vm.project.description;
                    }
                    loadPortfolios();
                    $scope.$on('app.project.saveAs', saveAsProject);
                }
            })();
        }
    }
);
