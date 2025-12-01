define(
    [
        'app/desktop/modules/admin/admin.module'
    ],
    function (module) {

        module.controller('NewGroupController', NewGroupController);

        function NewGroupController($scope, $q, $rootScope, $translate, $timeout, $state, $stateParams, $cookies, $sce,
                                    CommonService, LoginService, PersonGroupService, ProfileService) {

            var vm = this;

            var parsed = angular.element("<div></div>");
            var nameValidation = parsed.html($translate.instant("NAME_VALIDATION")).html();
            var groupCreatedMessage = parsed.html($translate.instant("ROLE_CREATED_MESSAGE")).html();
            var profileValidation = parsed.html($translate.instant("VALIDATE_PROFILE_MESSAGE")).html();

            vm.personGroup = {
                id: null,
                name: null,
                description: null,
                groupMember: [],
                groupId: null,
                parent: null,
                isActive: true,
                external: false,
                profiles: []
            };
            function createGroup() {
                if (validateGroup()) {
                    $rootScope.showBusyIndicator($('#rightSidePanel'));
                    PersonGroupService.createPersonGroup(vm.personGroup).then(
                        function (data) {
                            $scope.callback(data);
                            vm.personGroup = {
                                name: null,
                                description: null,
                                groupMember: [],
                                groupId: null,
                                parent: null,
                                isActive: true,
                                external: false,
                                profile: null
                            };
                            $rootScope.showSuccessMessage(groupCreatedMessage);
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showWarningMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    );
                }
            }

            function validateGroup() {
                var valid = true;
                if (vm.personGroup.name == null || vm.personGroup.name == "" || vm.personGroup.name == undefined) {
                    valid = false;
                    $rootScope.showErrorMessage(nameValidation);
                } else if (vm.personGroup.profile == null || vm.personGroup.profile == "") {
                    valid = false;
                    $rootScope.showErrorMessage(profileValidation);
                }

                return valid;
            }

            function loadProfiles() {
                ProfileService.getAllProfiles().then(
                    function (data) {
                        vm.allProfiles = data;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }

            (function () {
                $rootScope.hideBusyIndicator();
                loadProfiles();
                $rootScope.$on('app.groups.new', createGroup);
            })();
        }
    }
)
;