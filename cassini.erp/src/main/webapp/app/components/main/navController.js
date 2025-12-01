define(['app/app.modules',
        'app/shared/ui/modal/modalService',
        'app/components/main/navFactory',
        'app/components/admin/security/session/sessionFactory'
    ],
    function (app) {
        app.controller('NavController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state',
                'modalService', 'navFactory', 'sessionFactory',

                function ($scope, $rootScope, $timeout, $interval, $state,
                          modalService, navFactory, sessionFactory) {
                    $scope.navigation = [];
                    $scope.idToNavMap = new Hashtable();


                    $scope.getCss = function (item) {
                        var cls = "";

                        if(item.active) {
                            cls += "active";
                            $scope.activeNav = item;
                        }

                        if(item.children.length > 0) {
                            cls += " nav-parent";
                        }

                        return cls;
                    };

                    $scope.getChildCss = function (item) {
                        if(item.active) {
                            return "active";
                        }
                        else {
                            return "";
                        }
                    };

                    $scope.activate = function (item) {
                        if(item.children.length == 0) {
                            $scope.clearPreviousActives();
                            item.active = true;
                            $scope.currentChild = item;

                            var arr = item.id.split(".");
                            if (arr.length == 2) {
                                var parent = $scope.idToNavMap.get(arr[0]);
                                parent.active = true;
                            }
                        }
                    };

                    $scope.clearPreviousActives = function() {
                        angular.forEach($scope.navigation, function(item) {
                            item.active = false;

                            angular.forEach(item.children, function(child) {
                                child.active = false;
                            });
                        });
                    };

                    $scope.closeOtherNavs = function(item) {
                        if(item.children.length == 0) {
                            closeVisibleSubMenu();
                        }
                    };

                    createMap = function() {
                        angular.forEach($scope.navigation, function(item) {
                            $scope.idToNavMap.put(item.id, item);

                            angular.forEach(item.children, function(child) {
                                $scope.idToNavMap.put(child.id, child);
                            });
                        });
                    };

                    sessionFactory.isSesstionActive().then(
                        function (session) {
                            if (session.active == true) {
                                navFactory.getNavigation().then(
                                    function(data) {
                                        $scope.navigation = data;
                                    }
                                );
                            }
                        }
                    );


                    /*
                    $scope.$on('uiInited', function() {
                        if(app.navLoaded == false) {
                            navFactory.getNavigation()
                                .then(function(data) {
                                    if(app.navLoaded == false) {
                                        console.log("Creating navigation menus...");

                                        app.navLoaded = true;

                                        $scope.navigation = data;

                                        createMap();

                                        $timeout(function () {
                                            if(app.navInited == false) {
                                                initLeftPanel();
                                                app.navInited = true;
                                            }
                                        }, 500);
                                    }

                                }, function(data) {
                                    console.log(data);
                                }
                            );
                        }
                    });
                    */
                }
            ]);
});