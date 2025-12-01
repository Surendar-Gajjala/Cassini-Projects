/**
 * Created by swapna on 26/12/18.
 */
define(
    [
        'app/desktop/modules/proc/proc.module',
        'app/shared/services/pm/project/projectSiteService'
    ],

    function (module) {
        module.factory('SiteSelector', SiteSelector);

        function SiteSelector(ProjectSiteService) {
            return {
                show: show,
                getDetails: getDetails
            };

            function show($rootScope, callback) {
                console.log("Calling site selector dialog");
                var options = {
                    title: 'Select Site',
                    template: 'app/desktop/modules/select/site/siteSelectionView.jsp',
                    controller: 'SiteSelectionController as siteSelectVm',
                    resolve: 'app/desktop/modules/select/site/siteSelectionController',
                    width: 600,
                    data: {},
                    showMask: true,
                    side: 'left',
                    buttons: [
                        {text: 'Select', broadcast: 'app.site.selected'}
                    ],
                    callback: function (selectedSite) {
                        callback(selectedSite, selectedSite.name);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function getDetails(id, attribute, attributeName) {
                ProjectSiteService.getSite(id).then(
                    function (data) {
                        attribute.refValueString = data.name;
                        attribute[attributeName] = data.name;
                    }
                );
            }
        }
    }
);