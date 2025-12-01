define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('EmailMessageService', EmailMessageService);

        function EmailMessageService(httpFactory) {
            return {
                getObjectEmail: getObjectEmail
            };

            function getObjectEmail(id, pageable) {
                var url = "api/col/email/" + id + "?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

        }
    }
);