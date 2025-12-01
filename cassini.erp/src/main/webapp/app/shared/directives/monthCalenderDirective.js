define(['app/app.modules'], function (app) {
    app.directive('monthCalender', ['$compile', '$timeout', function ($compile, $timeout) {
        var controller = ['$scope', function ($scope) {
            var init = function () {
                $scope.months = angular.copy(staticMonths);
                $scope.currentMonth = new Date().getMonth();
            },
            staticMonths = [
                {name: 'JAN', index: 1},{name: 'FEB', index: 2},{name: 'MAR', index: 3},
                {name: 'APR', index: 4},{name: 'MAY', index: 5},{name: 'JUN', index: 6},
                {name: 'JUL', index: 7},{name: 'AUG', index: 8},{name: 'SEP', index: 9},
                {name: 'OCT', index: 10},{name: 'NOV', index: 11},{name: 'DEC', index: 12}
            ];

            $scope.currentYear = new Date().getFullYear();
            $scope.months = [];
            $scope.currentMonth = 0;

            $scope.isMonthCurrent = function(index) {
                return new Date().getMonth() == index;
            };

            $scope.isMonthBeforeCurrentMonth = function(index) {
                return (index < new Date().getMonth());
            };

            $scope.isYearCurrent = function() {
                return ($scope.currentYear == new Date().getFullYear() && $scope.currentMonth == new Date().getMonth());
            };

            $scope.nextMonth = function() {
                if($scope.currentMonth == 11) {
                    $scope.currentMonth = 0;
                    $scope.currentYear++;
                }else{
                    $scope.currentMonth++;
                }
                $scope.onMonthClick({data:{month:$scope.currentMonth,year:$scope.currentYear,type:$scope.calender}});
            };

            $scope.previousMonth = function() {
                if($scope.currentMonth == 0){
                    $scope.currentYear--;
                    $scope.currentMonth = 11;
                }else {
                    $scope.currentMonth--;
                }
                $scope.onMonthClick({data:{month:$scope.currentMonth,year:$scope.currentYear,type:$scope.calender}});
            };

            init();

        }];
        return {
            templateUrl: 'app/shared/directives/templates/monthCalender.html',
            restrict: 'E',
            controller: controller,
            scope: {
                calender:'=',
                onMonthClick: '&'
            }
        };
    }]);
});
