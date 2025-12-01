/**
 * Created by swapna on 24/12/18.
 */
define(
    [
        'app/desktop/modules/proc/proc.module',
        'app/shared/services/pm/project/projectService'
    ],

    function (module) {
        module.factory('ProjectSelector', ProjectSelector);

        function ProjectSelector(ProjectService) {
            return {
                show: show,
                getDetails: getDetails
            };

            function show($rootScope, callback) {
                console.log("Calling project selector dialog");
                var options = {
                    title: 'Select Project',
                    template: 'app/desktop/modules/select/project/projectSelectionView.jsp',
                    controller: 'ProjectSelectionController as projectSelectVm',
                    resolve: 'app/desktop/modules/select/project/projectSelectionController',
                    width: 550,
                    data: {},
                    showMask: true,
                    side: 'left',
                    buttons: [
                        {text: 'Select', broadcast: 'app.project.selected'}
                    ],
                    callback: function (selectedProject) {
                        callback(selectedProject, selectedProject.name);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function getDetails(id, attribute, attributeName) {
                ProjectService.getProject(id).then(
                    function (data) {
                        attribute.refValueString = data.name;
                        attribute[attributeName] = data.name;
                    }
                );
            }
        }
    }
);