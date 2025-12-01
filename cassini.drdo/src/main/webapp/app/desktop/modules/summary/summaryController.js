define(
    [
        'app/desktop/modules/summary/summary.module',
        'app/shared/services/core/bomService',
        'app/shared/services/drdoApplication',
        'app/shared/services/core/reportingService'
    ],
    function (module) {
        module.controller('SummaryController', SummaryController);

        function SummaryController($scope, $rootScope, $timeout, $state, $bom, BomService, ReportingService) {

            if ($application.homeLoaded == false) {
                return;
            }

            $rootScope.viewInfo.icon = "fa fa-newspaper-o";
            $rootScope.viewInfo.title = "Executive Summary";


            var vm = this;

            vm.loading = true;

            var nodeId = 0;
            var bomTree = null;
            var rootNode = null;

            vm.selectedNode = null;
            vm.selectedItem = null;
            var secMap = null;
            vm.systemSummary = {
                missilesCompleted: 0,
                missilesInProgress: 0,
                missilesNotYetStarted: 0,
                missilesStatuses: []
            };

            vm.missileSummary = {
                sectionsCompleted: 0,
                sectionsInProgress: 0,
                sectionsNotYetStarted: 0,
                sectionsStatuses: []
            };

            function initClassificationTree() {
                bomTree = $('#bomTree').tree({
                    data: [
                        {
                            id: nodeId,
                            text: 'BOM',
                            iconCls: 'bom-root',
                            attributes: {
                                item: null
                            },
                            children: []
                        }
                    ],
                    onSelect: onSelectNode
                });

                rootNode = bomTree.tree('find', 0);
            }

            function onSelectNode(node) {
                vm.selectedNode = node;
                vm.selectedItem = node.attributes.item;
                if (vm.selectedItem != null) {
                    $rootScope.showBusyIndicator($('#summaryContainer'));
                    if (vm.selectedItem.objectType == "BOM") {
                        ReportingService.getSystemSummary(vm.selectedItem.id).then(
                            function (data) {
                                vm.systemSummary = data;

                                $timeout(function () {
                                    drawChart();
                                    $rootScope.hideBusyIndicator();
                                }, 1000);
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        );
                    }
                    else if (vm.selectedItem.objectType == "BOMINSTANCE") {
                        ReportingService.getMissileSummary(vm.selectedItem.id).then(
                            function (data) {
                                vm.missileSummary = data;

                                $timeout(function () {
                                    drawChart();
                                    $rootScope.hideBusyIndicator();
                                }, 1000);
                            },
                            function (error) {
                                $rootScope.showErrorMessage(error.message);
                                $rootScope.hideBusyIndicator();
                            }
                        );
                    }
                }
                $scope.$apply();
            }

            function loadClassificationTree() {
                BomService.getBomTree().then(
                    function (data) {
                        var nodes = [];
                        angular.forEach(data, function (item) {
                            var node = makeNode(item);
                            if (item.children != null && item.children != undefined && item.children.length > 0) {
                                node.state = "closed";
                                visitChildren(node, item.children);
                            }

                            nodes.push(node);
                        });

                        bomTree.tree('append', {
                            parent: rootNode.target,
                            data: nodes
                        });
                    }
                )
            }

            function visitChildren(parent, items) {
                var nodes = [];
                angular.forEach(items, function (item) {
                    var node = makeChildNode(item);
                    nodes.push(node);
                });

                if (nodes.length > 0) {
                    parent.children = nodes;
                }
            }

            function makeNode(item) {
                return {
                    id: ++nodeId,
                    text: item.item.itemMaster.itemName,
                    iconCls: 'bom-node',
                    attributes: {
                        item: item
                    }
                };
            }

            function makeChildNode(item) {
                return {
                    id: ++nodeId,
                    text: item.item.instanceName,
                    iconCls: 'inst-node',
                    attributes: {
                        item: item
                    }
                };
            }

            function drawChart() {
                var bardata = [];
                secMap = new Hashtable();
                if (vm.selectedItem.objectType == "BOM") {
                    angular.forEach(vm.systemSummary.missilesStatuses, function (missile) {
                        var percent = missile.percentComplete != null ? missile.percentComplete : 0;
                        percent = Math.ceil(percent);
                        secMap.put(missile.name + " (" + percent + "%)", missile);
                        //bardata.push([missile.name + " (" + percent + "%)", percent]);
                        if (missile.onHold) {
                            bardata.push({data: [[missile.name + " (" + percent + "%)", percent]], color: '#F5291C'});
                        } else {
                            bardata.push({data: [[missile.name + " (" + percent + "%)", percent]], color: '#1D7F0E'});
                        }
                    });
                }
                else if (vm.selectedItem.objectType == "BOMINSTANCE") {
                    angular.forEach(vm.missileSummary.sectionsStatuses, function (section) {
                        var percent = section.percentComplete != null ? section.percentComplete : 0;
                        percent = Math.ceil(percent);
                        secMap.put(section.name + " (" + percent + "%)", section);
                        //bardata.push([section.name + " (" + percent + "%)", percent]);
                        if (section.onHold) {
                            bardata.push({data: [[section.name + " (" + percent + "%)", percent]], color: '#F5291C'});
                        } else {
                            bardata.push({data: [[section.name + " (" + percent + "%)", percent]], color: '#1D7F0E'});
                        }
                    });
                }

                $.plot("#barchart", bardata, {
                    series: {
                        lines: {
                            lineWidth: 1
                        },
                        bars: {
                            show: true,
                            barWidth: 0.5,
                            align: "center",
                            lineWidth: 0,
                            fill: true,
                            clickable: true
                        }
                    },
                    grid: {
                        hoverable: true,
                        borderColor: '#ddd',
                        borderWidth: 1,
                        labelMargin: 10,
                        clickable: true
                    },
                    xaxis: {
                        mode: "categories",
                        tickLength: 0
                    },
                    yaxis: {
                        tickColor: "#FDFDFD",
                        min: 0, max: 100, tickSize: 10,
                        tickFormatter: function (v, axis) {
                            return v + "%";
                        }
                    }
                });

                $("#barchart").unbind("plotclick");
                $("#barchart").bind("plotclick", function (event, pos, item) {
                    if (item != null) {
                        openPartTracking(item.series.xaxis.ticks[item.seriesIndex].label);
                    }
                });

                $("#barchart").UseTooltip();
            }

            var previousPoint = null;
            var previousLabel = null;

            function showTooltip(x, y, color, contents) {
                $('<div id="tooltip">' + contents + '</div>').css({
                    position: 'absolute',
                    display: 'none',
                    top: y - 40,
                    left: x - 120,
                    border: '2px solid ' + color,
                    padding: '3px',
                    'font-size': '16px',
                    'border-radius': '5px',
                    'background-color': '#fff',
                    'z-index': 1022,
                    'font-family': 'Verdana, Arial, Helvetica, Tahoma, sans-serif',
                    'opacity': 0.9
                }).appendTo("body").fadeIn(200);
            }

            $.fn.UseTooltip = function () {
                $(this).bind("plothover", function (event, pos, item) {
                    if (item) {
                        if ((previousLabel != item.series.label) || (previousPoint != item.dataIndex)) {
                            previousPoint = item.dataIndex;
                            previousLabel = item.series.label;
                            $("#tooltip").remove();

                            var x = item.datapoint[0];
                            var y = item.datapoint[1];

                            var color = item.series.color;
                            var obj = secMap.get(item.series.xaxis.ticks[item.seriesIndex].label);
                            var htmlCode = "";
                            if (obj.toolTip != null) {
                                htmlCode += "<span  style='color: rgba(161, 0, 0, 0.91);'>On-hold Reason: " + obj.toolTip + "<span><br>";
                            }
                            if (obj.partTrackingStep != null && obj.percentComplete < 100) {
                                htmlCode += "<span style='color: #008000;'>Status: " + obj.partTrackingStep + "<span><br>";
                            }
                            if (obj.percentComplete == 100) {
                                htmlCode += "<span style='color: rgba(28, 51, 124, 0.82);'>Completed<span><br>";
                            }
                            if (obj.initialiseDate != null) {
                                htmlCode += "<span style='color: #174246;'>Initial Date: " + obj.initialiseDate + "<span><br>";
                            }
                            if (obj.completedDate != null) {
                                htmlCode += "<span style='color: #174246;'>Completed Date: " + obj.completedDate + "<span>";
                            }
                            showTooltip(item.pageX,
                                item.pageY,
                                color,
                                htmlCode);
                        }
                    } else {
                        $("#tooltip").remove();
                        previousPoint = null;
                    }
                });
            };

            function openPartTracking(key) {
                var promise = null;
                $("#tooltip").remove();
                var obj = secMap.get(key);
                if (vm.selectedItem.objectType == "BOM") {
                    promise = BomService.getBomInstance(obj.id);
                }
                else if (vm.selectedItem.objectType == "BOMINSTANCE") {
                    promise = BomService.getByInstanceById(obj.id);
                }
                promise.then(
                    function (data) {
                        $bom.bomObject = data;
                        $state.go('app.bom.partTracking', {bomId: obj.id});
                    })
            }

            (function () {
                $scope.$on('$viewContentLoaded', function () {
                    initClassificationTree();
                    loadClassificationTree();
                });
            })();
        }
    }
);
