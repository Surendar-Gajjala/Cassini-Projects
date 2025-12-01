define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('MilestoneService', MilestoneService);

        function MilestoneService(httpFactory) {
            return {
                createMilestone: createMilestone,
                createMilestones: createMilestones,
                updateMilestone: updateMilestone,
                deleteMilestone: deleteMilestone,
                deleteMilestoneById: deleteMilestoneById,
                getMilestone: getMilestone,
                finishWbsMilestone: finishWbsMilestone
            };

            function createMilestone(wbsId, milestone) {
                var url = 'api/plm/projects/wbs/' + wbsId + "/milestones";
                return httpFactory.post(url, milestone);
            }

            function createMilestones(projectId, wbsId, milestoneList) {
                var url = 'api/plm/projects/wbs/' + wbsId + "/milestones/milestoneList/" + projectId;
                return httpFactory.post(url, milestoneList);
            }

            function updateMilestone(wbsId, milestone) {
                var url = 'api/plm/projects/wbs/' + wbsId + "/milestones/" + milestone.id;
                return httpFactory.put(url, milestone);
            }

            function finishWbsMilestone(wbsId, milestone) {
                var url = 'api/plm/projects/wbs/' + wbsId + "/milestones/" + milestone.id + "/finish";
                return httpFactory.put(url, milestone);
            }

            function deleteMilestone(wbsId, milestone) {
                var url = 'api/plm/projects/wbs/' + wbsId + "/milestones/" + milestone.id;
                return httpFactory.delete(url);
            }

            function deleteMilestoneById(wbsId, milestoneId) {
                var url = 'api/plm/projects/wbs/' + wbsId + "/milestones/" + milestoneId;
                return httpFactory.delete(url);
            }

            function getMilestone(wbsId, milestone) {
                var url = 'api/plm/projects/wbs/' + wbsId + "/milestones/" + milestone.id;
                return httpFactory.get(url);
            }

        }
    }
)