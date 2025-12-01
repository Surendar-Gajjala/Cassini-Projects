define(
    [
        'app/desktop/modules/pm/pm.module',
        'app/shared/services/pm/project/projectService',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('ProjectPersonController', ProjectPersonController);

        function ProjectPersonController($scope, $rootScope, $timeout, $state, $sce, $stateParams, $cookies, ItemService, CommonService,
                                         ProjectService) {
            $rootScope.viewInfo.icon = "fa fa-home";
            $rootScope.viewInfo.title = "Home";

            var vm = this;
            var args = $scope.data.node;
            var projectId = $stateParams.projectId;
            vm.node = true;
            vm.person = false;
            vm.hideRadioButton = false;

            vm.personData = {
                rowId: null,
                node: null,
                projectRole: null,
                role: null,
                nodeName: null,
                person: null,
                projectPersonId: null,
                parent: null
            }

            function validate() {
                vm.valid = true;

                return vm.valid;
            }

            function create() {
                /* if (vm.personData.id == null) {*/
                vm.personData.node = args.node.id;
                vm.personData.rowId = args.node.data.id;
                if (vm.personData.person != null) {
                    vm.personData.personName = vm.personData.person.fullName;
                    vm.personData.person = vm.personData.person.id;
                }
                vm.personData.parentId = args.node.parent.id;
                if (vm.personData.projectRole != null) {
                    //vm.personData.role = vm.personData.projectRole.id;
                }
                ProjectService.createOrgChart($stateParams.projectId, vm.personData).then(
                    function (data) {
                        $rootScope.hideSidePanel();
                        $scope.callback();
                        $rootScope.hideBusyIndicator();
                        if (data.nodeName != null) {
                            $rootScope.showSuccessMessage("Node created successfully");
                        } else {
                            $rootScope.showSuccessMessage("Person Node created successfully");
                        }
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
                /*} else {

                 }*/
            }

            vm.persons = [];
            function loadManpower() {
                vm.loading = true;
                ItemService.getActiveManpower().then(
                    function (data) {
                        vm.loading = false;
                        vm.manpower = data;
                        angular.forEach(vm.manpower, function (manpower) {
                            vm.persons.push(manpower.person)
                        });

                    }
                );

            }

            $scope.trustAsHtml = function (value) {
                return $sce.trustAsHtml(value);
            };

            vm.projectRoles = [];
            function loadProjectRoles() {
                ProjectService.getProjectRoles(projectId).then(
                    function (data) {
                        vm.loading = false;
                        vm.projectRoles = data;

                    });
            }

            vm.showNode = showNode;
            vm.showPerson = showPerson;

            function showNode() {
                vm.node = true;
                vm.person = false;
            }

            function showPerson() {
                vm.node = false;
                vm.person = true;
            }

            (function () {
                if ($application.homeLoaded == true) {
                    var args = $scope.data.node;
                    if (args.node.data != null) {
                        vm.personData.rowId = args.node.data.id;
                        vm.personData.node = args.node.data.id;
                        vm.personData.role = args.node.data.role;
                        if (args.node.data.person != null) {
                            vm.personData.person = args.node.data.person;
                            vm.personData.projectPersonId = args.node.data.person.id;
                            vm.personData.projectRole = args.node.data.projectRole;
                            vm.person = true;
                            vm.hideRadioButton = true;
                            vm.node = false;
                        }
                        if (args.node.data.nodeName != null) {
                            vm.personData.nodeName = args.node.data.nodeName;
                            vm.node = true;
                            vm.hideRadioButton = true;
                            vm.person = false;
                        }
                        vm.personData.parent = args.node.data.id;
                    }
                    var projectId = $stateParams.projectId;
                    loadManpower();
                    loadProjectRoles();
                    $rootScope.$on('app.projectPerson.new', create);
                }
            })();
        }
    }
);