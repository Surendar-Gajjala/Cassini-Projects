define(
    [
        'app/desktop/modules/main/main.module'
    ],

    function (module) {
        module.controller('QuickAccessController', QuickAccessController);

        function QuickAccessController($scope, $rootScope, $timeout, $interval, $state, $location, $application, $translate, $window) {

            var vm = this;

            var animationShowCls = "animated bounceIn";

            var newItemTitle = $translate.instant("NEW_ITEM_TITLE");
            var createButton = $translate.instant("CREATE");
            var newProjectTitle = $translate.instant("NEW_PROJECT");
            var ecoTitle = $translate.instant("ECO_ALL_NEW_ECO");
            var newManufacturerTitle = $translate.instant("NEW_MANUFACTURER_TITLE");
            var newSpecTitle = $translate.instant("NEW_SPECIFICATION");

            vm.close = close;
            vm.newItem = newItem;
            vm.newProject = newProject;
            vm.newECO = newECO;
            vm.newWorkflow = newWorkflow;
            vm.newManufacturer = newManufacturer;
            vm.newSpecification = newSpecification;
            vm.gotoClassification = gotoClassification;
            vm.gotoAdmin = gotoAdmin;
            vm.gotoSettings = gotoSettings;

            function newItem() {
                close();
                var options = {
                    title: newItemTitle,
                    template: 'app/desktop/modules/item/new/newItemView.jsp',
                    controller: 'NewItemController as newItemVm',
                    resolve: 'app/desktop/modules/item/new/newItemController',
                    width: 600,
                    data: {
                        itemsMode: ''
                    },
                    showMask: true,
                    buttons: [
                        {text: createButton, broadcast: 'app.items.new'}
                    ],
                    callback: function (itemRevision) {
                        $rootScope.hideSidePanel();
                        $timeout(function() {
                            $state.go('app.items.details', {itemId: itemRevision.id});
                        }, 1000);
                    }
                };

                $rootScope.showSidePanel(options);

            }

            function newProject() {
                close();
                var options = {
                    title: newProjectTitle,
                    showMask: true,
                    template: 'app/desktop/modules/pm/project/new/newProjectDialog.jsp',
                    controller: 'NewProjectController as newProjectVm',
                    resolve: 'app/desktop/modules/pm/project/new/newProjectDialogController',
                    width: 550,
                    data: {
                        projectCreationFrom: "",
                        selectedProgramId: null
                    },
                    buttons: [
                        {text: createButton, broadcast: 'app.project.new'}
                    ],
                    callback: function (newProject) {
                        $rootScope.hideSidePanel();
                        $timeout(function() {
                            $state.go('app.pm.project.details', {projectId: newProject.id});
                        }, 1000);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function newECO() {
                close();
                var options = {
                    title: ecoTitle,
                    template: 'app/desktop/modules/change/eco/new/newEcoView.jsp',
                    controller: 'NewECOController as newEcoVm',
                    resolve: 'app/desktop/modules/change/eco/new/newEcoController',
                    width: 700,
                    showMask: true,
                    buttons: [
                        {text: createButton, broadcast: 'app.changes.ecos.new'}
                    ],
                    callback: function (newEco) {
                        $rootScope.hideSidePanel();
                        $timeout(function() {
                            $state.go('app.changes.eco.details', {ecoId: newEco.id});
                        }, 1000);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function newWorkflow() {
                close();
                $state.go('app.workflow.editor');
            }

            function newManufacturer() {
                close();
                var options = {
                    title: newManufacturerTitle,
                    template: 'app/desktop/modules/mfr/new/newMfrView.jsp',
                    controller: 'NewMfrController as newMfrVm',
                    resolve: 'app/desktop/modules/mfr/new/newMfrController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: createButton, broadcast: 'app.mfrs.new'}
                    ],
                    callback: function (newMfr) {
                        $rootScope.hideSidePanel();
                        $timeout(function() {
                            $state.go('app.mfr.details', {manufacturerId: newMfr.id});
                        }, 1000);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function newSpecification() {
                close();
                var options = {
                    title: newSpecTitle,
                    template: 'app/desktop/modules/rm/specification/new/newSpecificationView.jsp',
                    controller: 'NewSpecificationController as newSpecificationVm',
                    resolve: 'app/desktop/modules/rm/specification/new/newSpecificationController',
                    width: 600,
                    showMask: true,
                    buttons: [
                        {text: createButton, broadcast: 'app.rm.specifications.new'}
                    ],
                    callback: function (newSpec) {
                        $rootScope.hideSidePanel();
                        $timeout(function() {
                            $state.go('app.rm.specifications.details', {specId: newSpec.id});
                        }, 1000);
                    }
                };

                $rootScope.showSidePanel(options);

            }

            function gotoClassification() {
                close();
                $state.go('app.classification');
            }

            function gotoAdmin() {
                close();
                $state.go('app.newadmin.users');
            }

            function gotoSettings() {
                close();
                $state.go('app.settings');
            }

            function close() {
                $('#quickAccess').removeClass(animationShowCls);
                $('#quickAccess').hide();
            }

            function initEvents() {
                $('body').on('click', function () {
                    close();
                });

                //Double Ctrl key press event
                $('body').dbKeypress(17, {
                    eventType: 'keyup',
                    callback: function () {
                        var quickAccessPanel = $('#quickAccess');
                        quickAccessPanel.show();
                        quickAccessPanel.outerWidth();
                        quickAccessPanel.addClass(animationShowCls);
                    }
                });

                //ESC key press event
                $(document).on('keyup', function (evt) {
                    if (evt.keyCode == 27) {
                        close();
                    }
                });
            }

            (function () {
                initEvents();
            })();
        }
    }
);