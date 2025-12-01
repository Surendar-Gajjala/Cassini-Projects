/**
 * Created by Nageshreddy on 02-01-2019.
 */
define([
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('FailureValueListService', FailureValueListService);
        function FailureValueListService(httpFactory) {
            var url = "api/drdo/failure/values";
            return {
                createFailListValues: createFailListValues,
                createFailListValue: createFailListValue,
                updateFailListValues: updateFailListValues,
                getByItemFailListValues: getByItemFailListValues,
                getByItemAndInstanceFailListValues: getByItemAndInstanceFailListValues,
                getByItemAndLotInstanceFailListValues: getByItemAndLotInstanceFailListValues
            };

            function createFailListValue(id, failurevaluelist) {
                return httpFactory.post(url + "/" + id, failurevaluelist);
            }

            function createFailListValues(failurevaluelist) {
                return httpFactory.post(url + "/multiple", failurevaluelist);
            }

            function updateFailListValues(failurevaluelist) {
                return httpFactory.put(url + "/multiple", failurevaluelist);
            }

            function getByItemFailListValues(item) {
                return httpFactory.get(url + "/byItem/" + item);
            }

            function getByItemAndInstanceFailListValues(item, upn) {
                return httpFactory.get(url + "/byItemAndInstance/" + item + "/" + upn);
            }

            function getByItemAndLotInstanceFailListValues(item, upn) {
                return httpFactory.get(url + "/byItemAndLotInstance/" + item + "/" + upn);
            }

        }
    }
);

