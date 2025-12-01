define(['app/app.modules', 'sugar'],
    function (app) {
        app.factory('authorizationFactory',
            [
                function () {
                    var session;
                    var rolesMap = new Hashtable();
                    var permissionsMap = new Hashtable();

                    var buildMaps = function () {
                        var roles = session.login.roles;
                        angular.forEach(roles, function (role) {
                            rolesMap.put(role.name, role);
                            buildPermissionsMap(role.permissions);
                        });
                    };

                    var buildPermissionsMap = function (permissions) {
                        angular.forEach(permissions, function (permission) {
                            console.log(permission.id);
                            permissionsMap.put(permission.id, permission);
                        });
                    };

                    var hasPermission = function (permission) {
                        var has = false;

                        var p = permissionsMap.get(permission);
                        if (p != null && p != undefined) {
                            has = true;
                        }
                        else {
                            if (permission.endsWith(".all")) {
                                var sub = permission.remove(".all");
                                var index = sub.lastIndexOf(".");
                                if (index != -1) {
                                    sub = sub.substring(0, index) + ".all";
                                    has = hasPermission(sub);
                                }
                            }
                            else {
                                var index = permission.lastIndexOf(".");
                                if (index != -1) {
                                    permission = permission.substring(0, index);
                                }
                                permission += ".all";
                                has = hasPermission(permission);
                            }
                        }

                        return has;
                    };

                    return {
                        initialize: function (s) {
                            session = s;
                            buildMaps();

                            console.log("Authorization service initialized!")
                        },
                        hasPermission: hasPermission
                    }
                }
            ]
        );
    }
);