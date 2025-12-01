define(['app/app.modules', 'app/components/prod/production/productionFactory', 'app/components/prod/production/order/productionOrderFactory',],
    function($app) {
        $app.controller('ProductionController',
            function($scope, $compile, $rootScope, $timeout, $interval, $state, $cookies, productionFactory, productionOrderFactory) {

                var orderDateFormat = "DD/MM/YYYY,hh:mm A",
                    init = function() {
                        productionOrderFactory.getProductionOrders($scope.pageable).then(function(data){
                                $scope.productionOrders = data;
                                constructCalendarOrders(data.content);
                            }
                        )
                    },
                    constructCalendarOrders = function(orders) {
                        /*var order = {},
                         orderList = [];

                         angular.forEach(orders, function(value , key) {

                         order.title = value.objectType;
                         order.start = moment(value.createdDate, orderDateFormat);
                         order.end = moment(value.createdDate, orderDateFormat);

                         orderList.push(order);

                         });*/

                        //dummyData
                        var date = new Date();
                        var d = date.getDate();
                        var m = date.getMonth();
                        var y = date.getFullYear();

                        var events = [
                            {title: 'order 1',start: new Date(y, m, 1)},
                            {title: 'order 2',start: new Date(y, m, d - 5),end: new Date(y, m, d - 2)},
                            {title: 'order 3',start: new Date(y, m, d - 3, 16, 0)},
                            {title: 'order 4',start: new Date(y, m, d + 4, 16, 0)},
                            {title: 'order 5',start: new Date(y, m, d + 1, 19, 0),end: new Date(y, m, d + 1, 22, 30)},
                            {title: 'order 5',start: new Date(y, m, 28),end: new Date(y, m, 29)}
                        ];

                        angular.forEach(events, function(value, key) {
                            $scope.events.push(value);
                        });
                    };

                $rootScope.iconClass = "fa fa-tachometer";
                $rootScope.viewTitle = "Production";

                $scope.productionOrders = {};
                $scope.pageable = {
                    page: 1,
                    size: 5,
                    sort: {
                        label: "id",
                        field: "id",
                        order: "asc"
                    }
                };

                $rootScope.viewType = 'calendar';
                $scope.isCalendar = true;
                $scope.events = [];
                $scope.orders = [$scope.events];

                /* config object */
                $scope.uiConfig = {
                    calendar:{
                        height: 450,
                        editable: false,
                        header: {
                            left: 'prev,next today',
                            center: 'title',
                            right: 'month,basicWeek,basicDay'
                        },
                        eventClick: $scope.onEventClick,
                        eventDrop: $scope.onDrop,
                        eventResize: $scope.onResize,
                        eventRender: $scope.eventRender
                    }
                };

                $rootScope.newPon = function() {
                    $state.go('app.prod.productionorder', { createDisplay:"new",orderId: 0 });
                };

                $rootScope.setViewType = function(view) {
                    $scope.isCalendar = (view === 'calendar') ? true : false;
                    $rootScope.viewType = view;
                };

                $scope.$on('$viewContentLoaded', function(){
                    $rootScope.setToolbarTemplate('prod-view-tb');
                });

                $scope.pageChanged = function() {
                    init();
                };

                /*on eventClick */
                $scope.onEventClick = function( date, jsEvent, view){

                };

                /* on Drop */
                $scope.alertOnDrop = function(event, delta, revertFunc, jsEvent, ui, view){

                };

                /*on Resize */
                $scope.alertOnResize = function(event, delta, revertFunc, jsEvent, ui, view ){

                };

                /* Change View */
                $scope.changeView = function(view,calendar) {
                    uiCalendarConfig.calendars[calendar].fullCalendar('changeView',view);
                };

                /* Change View */
                $scope.renderCalender = function(calendar) {
                    if(uiCalendarConfig.calendars[calendar]){
                        uiCalendarConfig.calendars[calendar].fullCalendar('render');
                    }
                };

                /* Render Tooltip */
                $scope.eventRender = function( event, element, view ) {
                    element.attr({'tooltip': event.title,'tooltip-append-to-body': true});
                    $compile(element)($scope);
                };

                init();
            },
            [

                '$scope', '$compile', '$rootScope', '$timeout', '$interval', '$state', '$cookies', 'productionFactory', 'productionOrderFactory'
            ]
        );
    }
);