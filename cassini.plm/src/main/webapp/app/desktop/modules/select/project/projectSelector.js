/**
 * Created by GSR on 26/12/18.
 */
define(
    [
        'app/desktop/modules/item/item.module',
        'app/shared/services/core/projectService'
    ],

    function (module) {
        module.factory('ProjectSelector', ProjectSelector);

        function ProjectSelector(ProjectService) {
            return {
                show: show,
                getDetails: getDetails
            };

            function show($rootScope, attributeDef, callback) {
                var options = {
                    title: 'Select Project',
                    template: 'app/desktop/modules/select/project/projectSelectionView.jsp',
                    controller: 'ProjectSelectionController as projectSelectVm',
                    resolve: 'app/desktop/modules/select/project/projectSelectionController',
                    width: 620,
                    data: {
                        existObjectId: $rootScope.objectAttributeValue,
                        selectAttDef: attributeDef
                    },
                    showMask: true,
                    side: 'left',
                    buttons: [
                        {text: $rootScope.add, broadcast: 'app.select.project'}
                    ],
                    callback: function (selectedProject) {
                        callback(selectedProject, selectedProject.name);
                        $rootScope.hideSidePanel("left");
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function getDetails(id, attribute) {
                ProjectService.getProject(id).then(
                    function (data) {
                        attribute.refValueString = data.name;
                    }
                );
            }
        }
    }
);
