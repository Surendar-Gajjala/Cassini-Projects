define(
    [
        'app/desktop/modules/proc/proc.module',
        'app/desktop/modules/proc/manpower/details/tabs/basic/manpowerBasicDetailsController',
        'app/desktop/modules/proc/manpower/details/tabs/attributes/manpowerAttributesController',
        'app/shared/services/core/itemService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService'
    ],
    function (module) {
        module.controller('ManpowerDetailsController', ManpowerDetailsController);

        function ManpowerDetailsController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, ItemService, CommonService) {
            if ($application.homeLoaded == false) {
                return;
            }
            $rootScope.viewInfo.icon = "fa fa-users";
            $rootScope.viewInfo.title = "Manpower Details";

            var vm = this;

            vm.manpowerId = $stateParams.manpowerId;
            vm.prjId = $stateParams.projectId;
            vm.editManpower = editManpower;
            vm.back = back;
            vm.manpowerDetailsTabActivated = manpowerDetailsTabActivated;

            vm.tabs = {
                basic: {
                    id: 'details.basic',
                    heading: 'Basic',
                    template: 'app/desktop/modules/proc/manpower/details/tabs/basic/manpowerBasicDetailsView.jsp',
                    active: true,
                    activated: true
                },
                attributes: {
                    id: 'details.attributes',
                    heading: 'ManpowerType Attributes',
                    template: 'app/desktop/modules/proc/manpower/details/tabs/attributes/manpowerAttributesView.jsp',
                    active: false,
                    activated: false
                }
            };

            function back() {
                window.history.back();
            }

            function activateTab(tab) {
                for (var t in vm.tabs) {
                    if (vm.tabs.hasOwnProperty(t)) {
                        vm.tabs[t].active = (t != undefined && t == tab);
                    }
                }
            }

            function manpowerDetailsTabActivated(tabId) {
                var tab = getTabById(tabId);
                if (tab != null && !tab.activated) {
                    tab.activated = true;
                    $scope.$broadcast('app.manpower.tabactivated', {tabId: tabId});

                    if (tab.id == "details.basic") {
                        loadManpower();
                    }
                }
                if (tab != null) {
                    activateTab(tab);
                }
            }

            function getTabById(tabId) {
                var tab = null;
                for (var t in vm.tabs) {
                    if (vm.tabs.hasOwnProperty(t) && vm.tabs[t].id == tabId) {
                        tab = t;
                    }
                }

                return tab;
            }

            function loadManpower() {
                vm.loading = true;
                ItemService.getManpowerItem(vm.manpowerId).then(
                    function (data) {
                        vm.manpower = data;
                        vm.loading = false;
                        $rootScope.viewInfo.title = "Name : " + vm.manpower.person.fullName;
                        $rootScope.viewInfo.description = "Type: {0}".format(vm.manpower.itemType.name);
                        CommonService.getPersonReferences([vm.manpower], 'createdBy');
                        CommonService.getPersonReferences([vm.manpower], 'modifiedBy');
                        //loadObjectAttributeDefs();
                    }
                )
            }

            function editManpower() {
                $state.go('app.proc.manpower.edit', {manpowerId: vm.manpowerId});
            }

            (function () {
                if ($application.homeLoaded == true) {

                }
            })();

        }
    }
);