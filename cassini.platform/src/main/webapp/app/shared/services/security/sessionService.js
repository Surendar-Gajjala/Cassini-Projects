define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('SessionService',SessionService);

        function SessionService ( httpFactory) {
            return {
                getSessions: getSessions,
                getSession: getSession,
                isSesstionActive: isSesstionActive
            };

            function getSessions(criteria, pageable) {
                var  url = "api/security/session?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page-1, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&id={0}&login={1}&user={2}&ipAddress={3}&loginTime={4}:{5}&logoutTime={6}:{7}".
                    format(criteria.id, criteria.login, criteria.user, criteria.ipAddress,
                    criteria.loginTime.startDate, criteria.loginTime.endDate,
                    criteria.logoutTime.startDate, criteria.logoutTime.endDate);
                return httpFactory.get(url);
            }

            function getSession(sessionId) {
                var url = "api/security/session/" + sessionId;
                return httpFactory.get(url);
            }

            function isSesstionActive() {
                var url = "api/security/session/isactive";
                return httpFactory.get(url);
            }

        }
    }
);