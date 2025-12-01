/**
 * Created by lakshmi on 1/16/2016.
 */
define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('FeedbackService', FeedbackService);

        function FeedbackService(httpFactory) {
            return {
               createFeedback: createFeedback,
                getFeedbackMappings:getFeedbackMappings,
                getAllOpenTickets: getAllOpenTickets,
                getAllClosedTickets: getAllClosedTickets
            };

                function createFeedback(feedback) {
                var url = "api/common/feedback";
                return httpFactory.post(url, feedback);
            }

            function getAllOpenTickets(reportId, pageNumber) {
                var url = "api/common/feedback/getAllOpenTickets/report/" + reportId + "/page/" +pageNumber;
                return httpFactory.get(url);
            }

            function getAllClosedTickets(reportId, pageNumber) {
                var url = "api/common/feedback/getAllClosedTickets/report/" + reportId + "/page/" +pageNumber;
                return httpFactory.get(url);
            }

             function getFeedbackMappings() {

                var url = "app/assets/bower_components/cassini-platform/app/desktop/modules/help/feedback/feedbackmappings.json";
                return httpFactory.get(url);
            }
        }
    }
);