/**
 * Created by Nageshreddy on 13-11-2019.
 */
define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],

    function (mdoule) {
        mdoule.factory('ActivityStreamService', ActivityStreamService);

        function ActivityStreamService(httpFactory) {


            return {
                getAllActivityStream: getAllActivityStream,
                getActivityStreamByObjectId: getActivityStreamByObjectId,
                getDatewiseActivityStreamByObjectId: getDatewiseActivityStreamByObjectId,
                getDateWiseActivityStream: getDateWiseActivityStream,
                getAllActivitySessions: getAllActivitySessions,
                getObjectTypes: getObjectTypes,
                getObjectTypeActions: getObjectTypeActions,
                getObjectTypeActionsIds: getObjectTypeActionsIds
            };

            function getActivityStreamByObjectId(objectId, pageable, filters) {
                var url = "api/common/activitystream/" + objectId + "?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&objectId={0}&user={1}&date={2}"
                    .format(filters.objectId, filters.user, filters.date);
                return httpFactory.get(url);
            }

            function getDatewiseActivityStreamByObjectId(objectId, pageable, filters) {
                var url = "api/common/activitystream/" + objectId + "/datewise?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&objectId={0}&user={1}&date={2}&objectIds={3}&action={4}"
                    .format(filters.objectId, filters.user, filters.date, [filters.objectIds], filters.action);
                return httpFactory.get(url);
            }

            function getAllActivityStream(pageable) {
                var url = "api/common/activitystream?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getDateWiseActivityStream(pageable, filters) {
                var url = "api/common/activitystream/datewise?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&objectId={0}&user={1}&date={2}&type={3}&action={4}"
                    .format(filters.objectId, filters.user, filters.date, filters.type, filters.action);
                return httpFactory.get(url);
            }

            function getAllActivitySessions(pageable, filter) {
                var url = "api/common/activitystream/sessions?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&loginTime={0}&user={1}".format(filter.loginTime, filter.user);
                return httpFactory.get(url);
            }

            function getObjectTypes() {
                var url = "api/common/activitystream/objecttypes";
                return httpFactory.get(url);
            }

            function getObjectTypeActions(objectId, objectType) {
                var url = "api/common/activitystream/" + objectId + "/objecttypes/" + objectType + "/actions";
                return httpFactory.get(url);
            }

            function getObjectTypeActionsIds(objectIds, objectType) {
                var url = "api/common/activitystream/[" + objectIds + "]/objecttypes/" + objectType + "/actions/multiple";
                return httpFactory.get(url);
            }

        }
    }
);
