define(['app/desktop/modules/pm/pm.module',
        'app/shared/services/core/projectService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/shared/services/security/loginService',
        'app/desktop/modules/directives/objectAttributeDetailsDirectiveController'

    ],
    function (module) {
        module.controller('ProgramBasicController', ProgramBasicController);

        function ProgramBasicController($scope, $translate, $rootScope, $timeout, $sce, $state, $stateParams, $cookies, ProgramService, CommonService,
                                        LoginService) {

            var vm = this;
            vm.programId = $stateParams.programId;
            vm.program = null;
            vm.back = back;
            vm.persons = [];
            vm.updateProgram = updateProgram;
            var session = JSON.parse(localStorage.getItem('local_storage_login'));
            $rootScope.loginPersonDetails = session.login;
            $rootScope.external = $rootScope.loginPersonDetails;
            vm.newProgramManager = null;

            var parsed = angular.element("<div></div>");
            var updatedSuccessfullyMsg = parsed.html($translate.instant("UPDATED_SUCCESS_MESSAGE")).html();
            var NameValidation = parsed.html($translate.instant("PROJECT_NAME_VALIDATION")).html();
            vm.clickToUpdateDes = parsed.html($translate.instant("CLICK_TO_UPDATE_DESCRIPTION")).html();
            vm.nameTooltip = parsed.html($translate.instant("CLICK_TO_UPDATE_NAME")).html();
            vm.clickToUpdatePerson = parsed.html($translate.instant("CLICK_TO_UPDATE_PERSON")).html();

            function loadProgram() {
                vm.loading = true;
                ProgramService.getProgram(vm.programId).then(
                    function (data) {
                        vm.program = data;
                        $rootScope.programInfo = vm.program;
                        vm.program.editMode = false;
                        vm.finishDateFlag = false;
                        $rootScope.programId = vm.program.id;
                        CommonService.getMultiplePersonReferences([vm.program], ['createdBy', 'modifiedBy', 'programManager']);
                        $rootScope.viewInfo.title = vm.program.name;
                        vm.projectName = $rootScope.viewInfo.title;
                        $timeout(function () {
                            $scope.$broadcast('app.attributes.tabActivated', {});
                        }, 1000);
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function back() {
                window.history.back();
            }

            function loadPersons() {
                LoginService.getInternalActiveLogins().then(
                    function (data) {
                        angular.forEach(data, function (login) {
                            vm.persons.push(login.person);
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function validate() {
                vm.valid = true;
                if (vm.newProgramManager != null) {
                    vm.program.programManager = vm.newProgramManager.id;
                }

                if (vm.program.name == null || vm.program.name == undefined || vm.program.name == "") {
                    vm.valid = false;
                    $rootScope.showErrorMessage(NameValidation);
                }

                return vm.valid;
            }

            function updateProgram() {
                if (validate() == false) {
                    loadProgram();
                }
                else if (validate() == true) {
                    vm.program.programManager = vm.program.programManagerObject.id;
                    ProgramService.updateProgram(vm.program).then(
                        function (data) {
                            loadProgram();
                            $rootScope.viewInfo.title = vm.program.name;
                            $rootScope.viewInfo.description = "Manager: " + vm.program.programManagerObject.firstName;
                            $rootScope.showSuccessMessage(vm.program.name + " : " + updatedSuccessfullyMsg);
                        },
                        function (error) {
                            $rootScope.showErrorMessage(error.message);
                            vm.program.name = vm.projectName;
                        }
                    )
                }

            }

            (function () {
                loadProgram();
                loadPersons();
            })();
        }
    }
);
