define(['app/app.modules', 'app/shared/factories/httpFactory'],
    function (app) {
        app.factory('sessionFactory',
            [
                '$http', '$q', 'httpFactory',

                function ($http, $q, httpFactory) {
                    return {
                        getSessions: function (criteria, pageable) {
                            var dfd = $q.defer(),
                                url = "api/security/session?page={0}&size={1}&sort={2}:{3}".
                                            format(pageable.page-1, pageable.size, pageable.sort.field, pageable.sort.order);
                            url += "&id={0}&login={1}&user={2}&ipAddress={3}&loginTime={4}:{5}&logoutTime={6}:{7}".
                                        format(criteria.id, criteria.login, criteria.user, criteria.ipAddress,
                                            criteria.loginTime.startDate, criteria.loginTime.endDate,
                                            criteria.logoutTime.startDate, criteria.logoutTime.endDate);
                            return httpFactory.get(url);
                        },

                        getSession: function (sessionId) {
                            var dfd = $q.defer(),
                                url = "api/security/session/" + sessionId;
                            return httpFactory.get(url);
                        },

                        isSesstionActive: function () {
                            var dfd = $q.defer(),
                                url = "api/security/session/isactive";
                            return httpFactory.get(url);
                        }
                    }
                }
            ]
        );
    }
);