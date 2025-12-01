define(['app/app.modules', 'app/components/common/autonumber/autonumberFactory'],
    function (app) {
        app.controller('AutonumbersController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', 'autonumberFactory',

                function ($scope, $rootScope, $timeout, $interval, $state, autonumberFactory) {

                    $rootScope.iconClass = "fa fa-list-ol";
                    $rootScope.viewTitle = "Autonumbers";

                    $scope.autonumbers = [];

                    $scope.addAutonumber = function () {
                        $scope.newAtonumber = {
                            name: "",
                            description: "",
                            numbers: 5,
                            start: 1,
                            increment: 1,
                            padwith: "0",
                            prefix: "",
                            suffix: "",
                            editMode: true,
                            showValues: false
                        };

                        $scope.autonumbers.push($scope.newAtonumber);
                    };

                    $scope.acceptChanges = function (autonumber) {
                        autonumber.editMode = false;

                        $timeout(function() {
                            autonumber.showValues = true;
                        }, 500);
                    };

                    $scope.showEditMode = function (autonumber) {
                        autonumber.editMode = true;
                        autonumber.showValues = false;
                    };

                    $scope.hideEditMode = function (autonumber) {
                        autonumber.editMode = false;

                        $timeout(function() {
                            autonumber.showValues = true;
                        }, 500);
                    };

                    $scope.saveAutonumbers = function () {
                        autonumberFactory.saveAutonumbers($scope.autonumbers)
                            .then(function(data) {
                                $scope.autonumbers = data;
                                addFlags($scope.autonumbers);

                                $rootScope.showNotification('Autonumbers saved successfully!', 'alert-success');

                            }, function(data) {
                                console.log(data);
                            });
                    };

                    function addFlags(autonumbers) {
                        angular.forEach(autonumbers, function(autonumber) {
                            autonumber.editMode = false;
                            autonumber.showValues = true;
                        })
                    };

                    $scope.removeItem = function (index) {
                        $scope.autonumbers.splice(index, 1);
                    };

                    $scope.getAutonumberExamples = function(autonumber) {
                        var example = "";
                        var count = 3;

                        for(var i=0; i<count; i++) {
                            if(autonumber.prefix != null && autonumber.prefix.length > 0) {
                                example += autonumber.prefix;
                            }

                            var t = "";

                            if(i == 0) {
                                t = "" + autonumber.start;
                            }
                            else {
                                t = "" + (autonumber.start + (i * autonumber.increment));
                            }

                            example += getNumberPart(autonumber.numbers, autonumber.padwith, t);
                            example += t;
                            example += autonumber.suffix;

                            if(i != count-1) {
                                example += ", "
                            }
                        }

                        return example;
                    };

                    function getNumberPart(numbers, padwith, number) {
                        var p = "";

                        var t = "" + number;
                        var n = numbers - t.length;

                        p += getPadding(padwith, n);

                        return p;
                    }

                    function getPadding (padWith, times) {
                        var padding = "";

                        for(var i = 0; i<times; i++) {
                            padding += padWith;
                        }

                        return padding;
                    }


                    autonumberFactory.getAutonumbers().then(
                        function(data) {
                            $scope.autonumbers = data;
                            addFlags($scope.autonumbers);
                        }, function(error) {
                            console.error(error);
                        }
                    );

                }
            ]);
    });