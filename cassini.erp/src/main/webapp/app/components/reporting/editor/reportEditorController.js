define(['app/app.modules',
        'app/components/reporting/reportFactory'
    ],
    function($app) {
        $app.controller('ReportEditorController',
            [
                '$scope', '$rootScope', '$state', '$sce', 'reportFactory',

                function($scope, $rootScope, $state, $sce, reportFactory) {
                    $rootScope.iconClass = "fa fa-list";
                    $rootScope.viewTitle = "Report Editor";

                    $scope.scriptText = "";

                    $scope.chart = {
                        data: []
                    };

                    $scope.showChart = false;


                    $scope.aceLoaded = function(_editor) {
                        _editor.setFontSize("16px");
                        _editor.setShowPrintMargin(false);
                    };

                    $scope.aceChanged = function(e) {
                    };

                    $scope.executeScript = function() {
                        reportFactory.executeScript($scope.scriptText).then (
                            function(data) {
                                //$scope.chart.data = data;
                                //$scope.showChart = true;
                            }
                        );
                    }
                }
            ]
        );
    }
);