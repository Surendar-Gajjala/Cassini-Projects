define(['app/desktop/modules/person/person.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/desktop/modules/person/details/tabs/basic/personBasicInfoController',
        'app/desktop/modules/person/details/tabs/emergencyContact/personEmergencyContactController',
        'app/desktop/modules/person/details/tabs/otherInfo/personOtherInfoController',
        'app/desktop/modules/person/details/tabs/tasks/personTasksController'
    ],
    function (module) {
        module.controller('PersonDetailsController', PersonDetailsController);

        function PersonDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, CommonService) {

            var vm = this;
            $rootScope.viewInfo.icon = "fa flaticon-businessman276";
            $rootScope.viewInfo.title = "Person Details";

            vm.back = back;
            vm.personId = $stateParams.personId;
            vm.updatePerson = updatePerson;
            $rootScope.personEditable = false;
            vm.updateEmergencyContact = updateEmergencyContact;
            vm.updateOtherInfo = updateOtherInfo;

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: 'Basic',
                    template: 'app/desktop/modules/person/details/tabs/basic/personBasicInfoView.jsp',
                    active: true
                },
                emergencyContact: {
                    id: 'details.emergencyContact',
                    heading: 'Emergency Contact',
                    template: 'app/desktop/modules/person/details/tabs/emergencyContact/personEmergencyContactView.jsp',
                    active: false
                },
                otherInfo: {
                    id: 'details.otherInfo',
                    heading: 'Other Info',
                    template: 'app/desktop/modules/person/details/tabs/otherInfo/personOtherInfoView.jsp',
                    active: false
                },

                tasks: {
                    id: 'details.tasks',
                    heading: 'Tasks',
                    template: 'app/desktop/modules/person/details/tabs/tasks/personTasksView.jsp',
                    active: false
                },

            };

            function back() {
                window.history.back();
                loadPersons();
            }

            function loadPersons(personId) {
                CommonService.getPerson(personId).then(
                    function (data) {
                        vm.person = data;
                        vm.loading = false;
                    }
                )
            }

            function updatePerson() {
                $scope.$broadcast('app.person.update');
            }

            function updateEmergencyContact() {
                $scope.$broadcast('app.emergencyContact.update');
            }

            function updateOtherInfo() {
                $scope.$broadcast('app.otherInfo.update');
            }

            (function () {
                if ($application.homeLoaded == true) {
                   var login = $application.login.person;
                    if($rootScope.isAdmin() == true || vm.personId == login.id){
                        $rootScope.personEditable = true;
                    }
                }
            })();
        }
    }
);