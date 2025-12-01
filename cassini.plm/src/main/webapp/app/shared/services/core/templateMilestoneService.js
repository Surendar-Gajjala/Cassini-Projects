define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('TemplateMilestoneService', TemplateMilestoneService);

        function TemplateMilestoneService(httpFactory) {
            return {
                createTemplateMilestone: createTemplateMilestone,
                updateTemplateMilestone: updateTemplateMilestone,
                deleteTemplateMilestone: deleteTemplateMilestone,
                getTemplateMilestone: getTemplateMilestone,
                getTemplateMilestones: getTemplateMilestones,
                getMilestoneByNameAndWbs: getMilestoneByNameAndWbs,
                createMilestones: createMilestones
            };

            function createMilestones(milestoneList) {
                var url = 'api/plm/templates/wbs/milestones/milestoneList';
                return httpFactory.post(url, milestoneList);
            }

            function createTemplateMilestone(milestone) {
                var url = "api/plm/templates/wbs/milestones";
                return httpFactory.post(url, milestone);
            }

            function updateTemplateMilestone(milestone) {
                var url = "api/plm/templates/wbs/milestones/" + milestone.id;
                return httpFactory.put(url, milestone);
            }

            function deleteTemplateMilestone(milestoneId) {
                var url = "api/plm/templates/wbs/milestones/" + milestoneId;
                return httpFactory.delete(url);
            }

            function getTemplateMilestone(milestoneId) {
                var url = "api/plm/templates/wbs/milestones/" + milestoneId;
                return httpFactory.get(url);
            }

            function getTemplateMilestones() {
                var url = "api/plm/templates/wbs/milestones";
                return httpFactory.get(url);
            }

            function getMilestoneByNameAndWbs(name, wbs) {
                var url = "api/plm/templates/wbs/milestones/" + wbs + "/byName?milestoneName=" + name;
                return httpFactory.get(url);
            }
        }
    }
);