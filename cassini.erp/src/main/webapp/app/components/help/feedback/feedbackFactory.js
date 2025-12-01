/**
 * Created by lakshmi on 1/16/2016.
 */
define(
    [
        'app/app.modules', 'app/shared/factories/httpFactory' ],
    function ($app) {
        $app.factory('FeedbackService', 
        		[ '$http', '$q', 'httpFactory',
        function ($http, $q, httpFactory) {
            return {
               createFeedback: createFeedback
            };

                function createFeedback(feedback) {
                var url = "api/common/feedback";
                return httpFactory.post(url, feedback);
            }
        }
        		]
        );
    }   
    
);