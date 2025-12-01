define(['app/desktop/modules/mes/mes.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/freetext/freeTextDirective',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectService',
        'app/shared/services/core/qualityTypeService',
        'app/shared/services/core/inspectionPlanService',
        'app/shared/services/core/problemReportService',
        'app/shared/services/core/ncrService',
        'app/shared/services/core/qcrService',
        'app/shared/services/core/classificationService',
        'app/desktop/modules/pqm/directives/qualityTypeDirective'
    ],
    function (module) {
        module.controller('QualityObjectsSelectionController', QualityObjectsSelectionController);

        function QualityObjectsSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $window, $cookies, $cookieStore, $translate,
                                                   $uibModal, QualityTypeService, ObjectService, InspectionPlanService, ProblemReportService, NcrService, QcrService, ClassificationService) {

            var vm = this;

            vm.loading = true;
            vm.nextPage = nextPage;
            vm.previousPage = previousPage;
            vm.selectRadio = selectRadio;
            vm.freeTextSearch = freeTextSearch;
            vm.clearFilter = clearFilter;
            vm.resetPage = resetPage;
            vm.radioChange = radioChange;
            vm.selectRadioChange = selectRadioChange;
            $scope.freeTextQuery = null;
            var objectId = $scope.data.existObjectId;
            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };
            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: vm.pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };

            vm.filters = {
                type: '',
                searchQuery: null
            };
            vm.selectedObj = null;
            vm.paginatedResults = angular.copy(pagedResults);
            var parsed = angular.element("<div></div>");
            var pleaseSelectOnePart = parsed.html($translate.instant("PLEASE_SELECT_ONE_OBJECT")).html();
            $scope.clearTitleSearch = parsed.html($translate.instant("CLEAR_SEARCH")).html();
            vm.selectAttributeDef = $scope.data.selectAttDef;

            function nextPage() {
                vm.pageable.page++;
                onSelectQualityType();
            }

            function previousPage() {
                vm.pageable.page--;
                onSelectQualityType();
            }

            vm.searchTerm = null;
            function freeTextSearch() {
                if (vm.searchTerm != null && vm.searchTerm != undefined && vm.searchTerm.trim() != "") {
                    $scope.freeTextQuery = vm.searchTerm;
                    vm.filters.searchQuery = vm.searchTerm;
                    onSelectQualityType();
                } else {
                    resetPage();
                    onSelectQualityType();
                }
            }

            function clearFilter() {
                vm.clear = false;
                vm.filters.number = null;
                vm.prFilter.prNumber = null;
                vm.ncrFilter.ncrNumber = null;
                vm.qcrFilter.qcrNumber = null;
                vm.filters.name = null;
                vm.prFilter.problem = null;
                vm.ncrFilter.title = null;
                vm.qcrFilter.title = null;
                vm.pageable.page = 0;
                vm.number = null;
                vm.name = null;
                vm.selectedType = null;
                onSelectQualityType();
            }

            function resetPage() {
                vm.paginatedResults = angular.copy(pagedResults);
                vm.pageable.page = 0;
                vm.filters.number = null;
                vm.prFilter.prNumber = null;
                vm.ncrFilter.ncrNumber = null;
                vm.qcrFilter.qcrNumber = null;
                vm.filters.name = null;
                vm.prFilter.problem = null;
                vm.ncrFilter.title = null;
                vm.qcrFilter.title = null;
                vm.number = null;
                vm.name = null;
                vm.selectedType = null;
                searchFilterItem();
            }

            function selectRadioChange(mfrPart, $event) {
                radioChange(mfrPart, $event);
                selectRadio();
            }

            function radioChange(mfrPart, $event) {
                $event.stopPropagation();
                if (vm.selectedObj === mfrPart) {
                    mfrPart.checked = false;
                    vm.selectedObj = null
                } else {
                    vm.selectedObj = mfrPart;
                }
            }

            function selectRadio() {
                if (vm.selectedObj != null) {
                    $rootScope.hideSidePanel('left');
                    $scope.callback(vm.selectedObj);
                }

                if (vm.selectedObj == null) {
                    $rootScope.showWarningMessage(pleaseSelectOnePart);
                }
            }

            module.directive('autoFocus', ['$timeout', function ($timeout) {
                return {
                    restrict: 'A',
                    link: function ($scope, $element) {
                        $timeout(function () {
                            $element[0].focus();
                        })
                    }
                }
            }]);

            vm.number = null;
            vm.name = null;

            vm.searchFilterItem = searchFilterItem;
            function searchFilterItem() {
                if ((vm.number != null && vm.number != "") || (vm.name != null && vm.name != "")) {
                    if (vm.selectedQualityObjectType == "PRODUCTINSPECTIONPLANTYPE" || vm.selectedQualityObjectType == "MATERIALINSPECTIONPLANTYPE") {
                        vm.filters.number = vm.number;
                        vm.filters.name = vm.name;
                    } else if (vm.selectedQualityObjectType == "PRTYPE") {
                        vm.prFilter.prNumber = vm.number;
                        vm.prFilter.problem = vm.name;
                    } else if (vm.selectedQualityObjectType == "NCRTYPE") {
                        vm.ncrFilter.ncrNumber = vm.number;
                        vm.ncrFilter.title = vm.name;
                    } else if (vm.selectedQualityObjectType == "QCRTYPE") {
                        vm.qcrFilter.qcrNumber = vm.number;
                        vm.qcrFilter.title = vm.name;
                    }
                    vm.clear = true;
                } else {
                    vm.filters.number = null;
                    vm.prFilter.prNumber = null;
                    vm.ncrFilter.ncrNumber = null;
                    vm.qcrFilter.qcrNumber = null;
                    vm.filters.name = null;
                    vm.prFilter.problem = null;
                    vm.ncrFilter.title = null;
                    vm.qcrFilter.title = null;
                    vm.clear = false;
                }
                vm.pageable.page = 0;
                onSelectQualityType();
            }

            vm.qualityTypes = [
                {label: "Product Inspection Plan", value: "PRODUCTINSPECTIONPLANTYPE"},
                {label: "Material Inspection Plan", value: "MATERIALINSPECTIONPLANTYPE"},
                {label: "Problem Report", value: "PRTYPE"},
                {label: "NCR", value: "NCRTYPE"},
                {label: "QCR", value: "QCRTYPE"}
            ];
            vm.numberTitle = parsed.html($translate.instant("NUMBER")).html();
            vm.nameTitle = parsed.html($translate.instant("NAME")).html();
            vm.onSelectQualityType = onSelectQualityType;
            function onSelectQualityType(type) {
                if (vm.selectedQualityObjectType == "PRODUCTINSPECTIONPLANTYPE" || vm.selectedQualityObjectType == "MATERIALINSPECTIONPLANTYPE") {
                    vm.nameTitle = parsed.html($translate.instant("NAME")).html();
                    loadInspectionPlans();
                } else if (vm.selectedQualityObjectType == "PRTYPE") {
                    vm.nameTitle = parsed.html($translate.instant("PROBLEM")).html();
                    loadProblemReports();
                } else if (vm.selectedQualityObjectType == "NCRTYPE") {
                    vm.nameTitle = parsed.html($translate.instant("TITLE")).html();
                    loadAllNcrs();
                } else if (vm.selectedQualityObjectType == "QCRTYPE") {
                    vm.nameTitle = parsed.html($translate.instant("TITLE")).html();
                    loadAllQcrs();
                }
                nodeId = 0;
                qualityTree = null;
                rootNode = null;
                qualityTree = $('#quality-type-tree').tree({
                    data: [
                        {
                            id: nodeId,
                            text: 'Quality',
                            iconCls: 'classification-root',
                            attributes: {
                                planType: null
                            },
                            children: []
                        }
                    ],
                    onSelect: onSelectType
                });

                rootNode = qualityTree.tree('find', 0);
                var nodes = [];
                angular.forEach(vm.qualityTypeTreeData, function (planType) {
                    if (planType.qualityType == vm.selectedQualityObjectType) {
                        var node = makeNode(planType);

                        if (planType.childrens != null && planType.childrens != undefined && planType.childrens.length > 0) {
                            node.state = "closed";
                            visitChildren(node, planType.childrens);
                        }

                        nodes.push(node);
                    }
                });

                qualityTree.tree('append', {
                    parent: rootNode.target,
                    data: nodes
                });
            }

            var nodeId = 0;
            var qualityTree = null;
            var rootNode = null;
            vm.selectedQualityObjectType = null;
            function loadQualityObjectType() {
                if (vm.selectAttributeDef.refSubType != null && vm.selectAttributeDef.refSubType != "") {
                    QualityTypeService.getQtType(vm.selectAttributeDef.refSubType).then(
                        function (data) {
                            vm.qualityObjectType = data;
                            if (vm.qualityObjectType != null && vm.qualityObjectType != "") {
                                vm.selectedQualityObjectType = vm.qualityObjectType.qualityType;
                                vm.filters.planType = vm.selectAttributeDef.refSubType;
                                vm.qcrFilter.qcrType = vm.selectAttributeDef.refSubType;
                                vm.ncrFilter.ncrType = vm.selectAttributeDef.refSubType;
                                vm.prFilter.prType = vm.selectAttributeDef.refSubType;
                                onSelectQualityType();
                            }
                        }
                    )
                } else {
                    ClassificationService.getClassificationTree("QUALITY_TYPE").then(
                        function (data) {
                            vm.qualityTypeTreeData = data;
                        }
                    )
                }
            }

            function onSelectType(node) {
                var data = qualityTree.tree('getData', node.target);
                var planType = data.attributes.planType;
                if (vm.selectedQualityObjectType == "PRODUCTINSPECTIONPLANTYPE" || vm.selectedQualityObjectType == "MATERIALINSPECTIONPLANTYPE") {
                    vm.filters.planType = planType.id;
                } else if (vm.selectedQualityObjectType == "PRTYPE") {
                    vm.prFilter.prType = planType.id;
                } else if (vm.selectedQualityObjectType == "NCRTYPE") {
                    vm.ncrFilter.ncrType = planType.id;
                } else if (vm.selectedQualityObjectType == "QCRTYPE") {
                    vm.qcrFilter.qcrType = planType.id;
                }
                vm.selectedType = planType;
                vm.clear = true;
                onSelectQualityType();
                window.$("body").trigger("click");
            }

            function visitChildren(parent, planTypes) {
                var nodes = [];
                angular.forEach(planTypes, function (planType) {
                    var node = makeNode(planType);

                    if (planType.childrens != null && planType.childrens != undefined && planType.childrens.length > 0) {
                        node.state = 'closed';
                        visitChildren(node, planType.childrens);
                    }

                    nodes.push(node);
                });

                if (nodes.length > 0) {
                    parent.children = nodes;
                }
            }

            function makeNode(planType) {
                return {
                    id: ++nodeId,
                    text: planType.name,
                    iconCls: 'quality-node',
                    attributes: {
                        planType: planType
                    }
                };
            }

            vm.filters = {
                name: null,
                planType: '',
                number: null,
                description: null,
                searchQuery: null
            };

            function loadInspectionPlans() {
                vm.loading = true;
                var promise = null;
                if (vm.selectedQualityObjectType == "PRODUCTINSPECTIONPLANTYPE") {
                    promise = InspectionPlanService.getAllProductInspectionPlans(vm.pageable, vm.filters);
                } else if (vm.selectedQualityObjectType == "MATERIALINSPECTIONPLANTYPE") {
                    promise = InspectionPlanService.getAllMaterialInspectionPlans(vm.pageable, vm.filters);
                }
                if (promise != null) {
                    promise.then(
                        function (data) {
                            vm.inspectionPlans = data;
                            var existObjectId = false;
                            angular.forEach(vm.inspectionPlans.content, function (plan) {
                                if (objectId != null && objectId != "" && objectId != undefined && objectId == plan.id) {
                                    vm.inspectionPlans.content.splice(vm.inspectionPlans.content.indexOf(plan), 1);
                                    existObjectId = true;
                                }
                            });
                            if (existObjectId) {
                                vm.inspectionPlans.totalElements = vm.inspectionPlans.totalElements - 1;
                                vm.inspectionPlans.numberOfElements = vm.inspectionPlans.numberOfElements - 1;
                            }

                            vm.paginatedResults = vm.inspectionPlans;
                            vm.loading = false;
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            vm.prFilter = {
                prNumber: null,
                prType: '',
                problem: null,
                searchQuery: null,
                product: null,
                qcr: '',
                ecr: '',
                released: false
            };

            function loadProblemReports() {
                vm.loading = true;
                ProblemReportService.getAllProblemReports(vm.pageable, vm.prFilter).then(
                    function (data) {
                        vm.problemReports = data;
                        var existObjectId = false;
                        angular.forEach(vm.problemReports.content, function (pr) {
                            if (objectId != null && objectId != "" && objectId != undefined && objectId == pr.id) {
                                vm.problemReports.content.splice(vm.problemReports.content.indexOf(pr), 1);
                                existObjectId = true;
                            }
                        });
                        if (existObjectId) {
                            vm.problemReports.totalElements = vm.problemReports.totalElements - 1;
                            vm.problemReports.numberOfElements = vm.problemReports.numberOfElements - 1;
                        }

                        vm.paginatedResults = vm.problemReports;
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.ncrFilter = {
                ncrNumber: null,
                ncrType: '',
                title: null,
                description: null,
                searchQuery: null,
                qcr: '',
                released: false
            };

            function loadAllNcrs() {
                vm.loading = true;
                NcrService.getAllNcrs(vm.pageable, vm.ncrFilter).then(
                    function (data) {
                        vm.ncrs = data;
                        var existObjectId = false;
                        angular.forEach(vm.ncrs.content, function (ncr) {
                            if (objectId != null && objectId != "" && objectId != undefined && objectId == ncr.id) {
                                vm.ncrs.content.splice(vm.ncrs.content.indexOf(ncr), 1);
                                existObjectId = true;
                            }
                        });
                        if (existObjectId) {
                            vm.ncrs.totalElements = vm.ncrs.totalElements - 1;
                            vm.ncrs.numberOfElements = vm.ncrs.numberOfElements - 1;
                        }

                        vm.paginatedResults = vm.ncr;
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            vm.qcrFilter = {
                qcrNumber: null,
                qcrType: '',
                title: null,
                searchQuery: null
            };

            function loadAllQcrs() {
                vm.loading = true;
                QcrService.getAllQcrs(vm.pageable, vm.qcrFilter).then(
                    function (data) {
                        vm.qcrs = data;
                        var existObjectId = false;
                        angular.forEach(vm.qcrs.content, function (qcr) {
                            if (objectId != null && objectId != "" && objectId != undefined && objectId == qcr.id) {
                                vm.qcrs.content.splice(vm.qcrs.content.indexOf(qcr), 1);
                                existObjectId = true;
                            }
                        });
                        if (existObjectId) {
                            vm.qcrs.totalElements = vm.qcrs.totalElements - 1;
                            vm.qcrs.numberOfElements = vm.qcrs.numberOfElements - 1;
                        }
                        vm.paginatedResults = vm.qcr;
                        vm.loading = false;
                        $rootScope.hideBusyIndicator();
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                        $rootScope.hideBusyIndicator();
                    }
                )
            }


            (function () {
                $rootScope.$on('app.select.qualityObject', selectRadio);
                loadQualityObjectType();
            })();
        }
    }
)
;