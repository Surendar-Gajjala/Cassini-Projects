define(
    [
        'app/desktop/modules/main/main.module'
    ],

    function (module) {
        module.controller('ObjectRightsController', ObjectRightsController);

        function ObjectRightsController($scope, $rootScope, $timeout, $interval, $state, $location, $application, $translate, $window) {

            $rootScope.hasPermission = function (arg1, arg2, arg3) {
                var has = false;
                if ($rootScope.loginPersonDetails != null) {
                    if (!$rootScope.loginPersonDetails.isAdmin) {
                        if (arg3 == undefined) {
                            if (arg1 != null && arg1 != undefined && arg2 != null && arg2 != undefined) {
                                if (arg2 == '*') {
                                    angular.forEach($rootScope.loginPersonDetails.defaultGroup.groupSecurityPermissions, function (securityPermission) {
                                        if (arg1 == securityPermission.objectType.toLowerCase()) has = true;
                                    });
                                } else {
                                    angular.forEach($rootScope.loginPersonDetails.defaultGroup.groupSecurityPermissions, function (securityPermission) {
                                        if (arg1 == securityPermission.objectType.toLowerCase() && securityPermission.subType == null) {
                                            if (securityPermission.privilege == 'all') has = true;
                                            else if (arg2 == securityPermission.privilege) has = true;
                                            else if(securityPermission.privilege.includes(",") && securityPermission.privilege.includes(arg2)) has = true;
                                        }
                                    });
                                }
                            }
                        } else {
                            if (arg1 != null && arg1 != undefined && arg2 != null && arg2 != undefined && arg3 != null && arg3 != undefined) {
                                if (arg3 == '*') {
                                    angular.forEach($rootScope.loginPersonDetails.defaultGroup.groupSecurityPermissions, function (securityPermission) {
                                        if (securityPermission.subType != null) {
                                            if (arg1 == securityPermission.objectType.toLowerCase() && arg2 == securityPermission.subType.toLowerCase()) has = true;
                                        }
                                    });
                                } else {
                                    angular.forEach($rootScope.loginPersonDetails.defaultGroup.groupSecurityPermissions, function (securityPermission) {
                                        if (securityPermission.subType != null) {
                                            if (arg1 == securityPermission.objectType.toLowerCase() && arg2 == securityPermission.subType.toLowerCase()) {
                                                if (securityPermission.privilege == 'all') has = true;
                                                else if (arg3 == securityPermission.privilege) has = true;
                                                else if(securityPermission.privilege.includes(",") && securityPermission.privilege.includes(arg2)) has = true;
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    } else {
                        has = true;
                    }
                }
                return has;
            };

            $rootScope.checkMESPermission = checkMESPermission;
            function checkMESPermission(privilege) {
                return $rootScope.hasPermission('planttype', privilege)
                    || $rootScope.hasPermission('workcentertype', privilege) || $rootScope.hasPermission('machinetype', privilege) || $rootScope.hasPermission('equipmenttype', privilege)
                    || $rootScope.hasPermission('instrumenttype', privilege) || $rootScope.hasPermission('tooltype', privilege) || $rootScope.hasPermission('jigfixturetype', privilege)
                    || $rootScope.hasPermission('materialtype', privilege) || $rootScope.hasPermission('manpowertype', privilege) || $rootScope.hasPermission('operationtype', privilege)
                    || $rootScope.hasPermission('productionordertype', privilege);
            }

            $rootScope.checkMROPermission = checkMROPermission;
            function checkMROPermission(privilege) {
                return $rootScope.hasPermission('assettype', privilege) || $rootScope.hasPermission('metertype', privilege) || $rootScope.hasPermission('spareparttype', privilege)
                    || $rootScope.hasPermission('workrequesttype', privilege) || $rootScope.hasPermission('workordertype', privilege);
            }

            $rootScope.checkPMPermission = checkPMPermission;
            function checkPMPermission(privilege) {
                return $rootScope.hasPermission('requirementtype', privilege)
                    || $rootScope.hasPermission('requirementdocumenttype', privilege) || $rootScope.hasPermission('pmobject', privilege);
            }

            $rootScope.checkSourcingPermission = checkSourcingPermission;
            function checkSourcingPermission(privilege) {
                return $rootScope.hasPermission('manufacturertype', privilege) || $rootScope.hasPermission('manufacturerparttype', privilege)
                    || $rootScope.hasPermission('suppliertype', privilege);
            }

            $rootScope.checkPGCPermission = checkPGCPermission;
            function checkPGCPermission(privilege) {
                return $rootScope.hasPermission('pgcsubstancetype', privilege)
                    || $rootScope.hasPermission('pgcspecificationtype', privilege) || $rootScope.hasPermission('pgcdeclarationtype', privilege)
            }

            $rootScope.checkPDMPermission = checkPDMPermission;
            function checkPDMPermission(privilege) {
                return $rootScope.hasPermission('pdm_vault', privilege) || $rootScope.hasPermission('pdmobject', 'all')
                    || $rootScope.hasPermission('pdm_assembly', privilege) || $rootScope.hasPermission('pdm_part', privilege) || $rootScope.hasPermission('pdm_drawing', privilege)
            }

            $rootScope.checkChangePermission = checkChangePermission;
            function checkChangePermission(privilege) {
                return $rootScope.hasPermission('change', 'eco', privilege) || $rootScope.hasPermission('change', 'ecr', privilege) || $rootScope.hasPermission('change', 'dco', privilege) || $rootScope.hasPermission('change', 'dcr', privilege)
                    || $rootScope.hasPermission('change', 'mco', privilege) || $rootScope.hasPermission('change', 'deviation', privilege) || $rootScope.hasPermission('change', 'waiver', privilege) || $rootScope.hasPermission('change', privilege)
            }

            $rootScope.checkDashboardPermission = checkDashboardPermission;
            function checkDashboardPermission(privilege) {
                return $rootScope.hasPermission('dashboard', 'item', privilege) || $rootScope.hasPermission('dashboard', 'change', privilege) || $rootScope.hasPermission('dashboard', 'quality', privilege) || $rootScope.hasPermission('dashboard', 'project', privilege)
                    || $rootScope.hasPermission('dashboard', 'workflow', privilege) || $rootScope.hasPermission('dashboard', 'sourcing', privilege)
            }


            (function () {
            })();
        }
    }
);