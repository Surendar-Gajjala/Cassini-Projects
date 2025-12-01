define(['app/app.modules', 'app/shared/factories/httpFactory'],
    function (app) {
        app.factory('roleFactory',
            [
                '$http', '$q', 'httpFactory',

                function ($http, $q, httpFactory) {
                    return {
                        getRolesGrid: function () {
                            var dfd = $q.defer(),
                                url = "app/components/admin/security/role/rolesGrid.json";
                            return httpFactory.get(url);
                        },
                        getAllRoles: function() {
                            var dfd = $q.defer(),
                                url = "api/security/role";
                            return httpFactory.get(url);
                        },
                        createRole: function(role) {
                            var dfd = $q.defer(),
                                url = "api/security/role/new";
                            return httpFactory.post(url, role);
                        },
                        saveRole: function(role) {
                            var dfd = $q.defer(),
                                url = "api/security/role/" + role.id;
                            return httpFactory.put(url, role);
                        },
                        saveRoles: function(roles) {
                            var dfd = $q.defer(),
                                url = "api/security/role";
                            return httpFactory.post(url, roles);
                        },
                        getLoginRoles: function(login) {
                            var dfd = $q.defer(),
                                url = "api/security/login/{0}/roles".format(login.id);
                            return httpFactory.get(url);
                        },
                        saveLoginRoles: function(login, roles) {
                            var dfd = $q.defer(),
                                url = "api/security/login/{0}/roles".format(login.id);
                            return httpFactory.post(url, roles);
                        }
                    }
                }
            ]
        );
    }
);