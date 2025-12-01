define(
    [
        'app/desktop/modules/npr/npr.module',
        'app/shared/services/core/nprService',
        'app/shared/services/core/projectService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/autonumberService'
    ],
    function (module) {

        module.controller('NewNprController', NewNprController);

        function NewNprController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce, $application, NprService,
                                  LoginService, AutonumberService, ProjectService) {

            var vm = this;

            var parsed = angular.element("<div></div>");
            var numberValidateMsg = parsed.html($translate.instant("NUMBER_CANNOT_BE_EMPTY")).html();
            var requesterValidateMsg = parsed.html($translate.instant("SELECT_REQUESTER")).html();
            var descriptionValidateMsg = parsed.html($translate.instant("DESCRIPTION_VALIDATION")).html();
            var reasonForRequestValidateMsg = parsed.html($translate.instant("ENTER_REASON_REQUEST")).html();
            var nprCreatedMsg = parsed.html($translate.instant("NPR_CREATED_SUCCESSFULLY")).html();

            $scope.personDetails = $rootScope.loginPersonDetails;
            vm.newNpr = {
                id: null,
                number: null,
                requester: $scope.personDetails.person,
                notes: null,
                description: null,
                status: 'OPEN',
                reasonForRequest: null,
                workflow: null
            };

            function create() {
                if (validate()) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    vm.newNpr.requester = vm.newNpr.requester.id;
                    NprService.createNpr(vm.newNpr).then(
                        function (data) {
                            vm.newNpr = data;
                            $scope.callback(vm.newNpr);
                            $rootScope.showSuccessMessage(nprCreatedMsg);
                            $rootScope.hideBusyIndicator();
                            $rootScope.hideSidePanel();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function validate() {
                var valid = true;
                if (vm.newNpr.number == null || vm.newNpr.number == undefined ||
                    vm.newNpr.number == "") {
                    $rootScope.showErrorMessage(numberValidateMsg);
                    valid = false;
                }
                if (vm.newNpr.requester == null || vm.newNpr.requester == undefined ||
                    vm.newNpr.requester == "") {
                    $rootScope.showErrorMessage(requesterValidateMsg);
                    valid = false;
                }
                if (vm.newNpr.description == null || vm.newNpr.description == undefined ||
                    vm.newNpr.description == "") {
                    $rootScope.showErrorMessage(descriptionValidateMsg);
                    valid = false;
                } else if (vm.newNpr.reasonForRequest == null || vm.newNpr.reasonForRequest == undefined ||
                    vm.newNpr.reasonForRequest == "") {
                    $rootScope.showErrorMessage(reasonForRequestValidateMsg);
                    valid = false;
                }

                return valid;
            }


            function loadPersons() {
                vm.persons = [];
                LoginService.getAllLogins().then(
                    function (data) {
                        angular.forEach(data, function (login) {
                            if (login.isActive == true && login.external == false) {
                                vm.persons.push(login.person);
                            }
                        });
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.autoNumber = autoNumber;
            function autoNumber() {
                var preference = $application.defaultValuesPreferences.get("DEFAULT_NPR_NUMBER_SOURCE");
                if (preference != null && preference.defaultValueName != null) {
                    var name = preference.defaultValueName;
                    AutonumberService.getAutonumberName(name).then(
                        function (data) {
                            if (data.id != null && data.id != "") {
                                AutonumberService.getNextNumberByName(data.name).then(
                                    function (data) {
                                        vm.newNpr.number = data;
                                    }
                                )
                            }
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            vm.workflows = [];
            function loadWorkflows() {
                ProjectService.getWorkflows("NPR").then(
                    function (data) {
                        vm.workflows = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                )
            }

            (function () {
                loadPersons();
                autoNumber();
                loadWorkflows();
                $rootScope.$on('app.newNpr.new', create);
            })();
        }
    }
)
;