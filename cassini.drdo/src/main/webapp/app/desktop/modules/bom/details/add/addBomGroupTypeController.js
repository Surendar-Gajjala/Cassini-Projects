define(
    [
        'app/desktop/modules/bom/bom.module',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogService',
        'app/shared/services/core/bomGroupService'
    ],
    function (module) {
        module.controller('AddBomGroupTypeController', AddBomGroupTypeController);

        function AddBomGroupTypeController($scope, $rootScope, $q, $timeout, $state, $stateParams, $uibModal, $cookies, $window, $translate,
                                           CommonService, DialogService, BomGroupService) {

            if ($application.homeLoaded == false) {
                return;
            }

            var vm = this;
            vm.searchBomTypes = searchBomTypes;
            vm.selectedBom = $scope.data.selectedBomItem;
            vm.selectedType = $scope.data.selectedBomItemType;

            vm.newBomItem = {
                id: null,
                parent: null,
                bom: null,
                item: null,
                newItem: {
                    id: null,
                    itemType: null,
                    itemNumber: null,
                    itemName: null,
                    description: null,
                    units: "Each",
                    drawingNumber: null
                },
                quantity: null,
                fractionalQuantity: null,
                bomItemType: null,
                typeRef: null,
                hierarchicalCode: null
            };

            function closeRightSidePanel() {
                vm.newBomItem = {
                    id: null,
                    parent: null,
                    bom: null,
                    item: null,
                    newItem: {
                        id: null,
                        itemType: null,
                        itemNumber: null,
                        itemName: null,
                        description: null,
                        units: "Each",
                        drawingNumber: null
                    },
                    quantity: null,
                    fractionalQuantity: null,
                    bomItemType: null,
                    typeRef: null,
                    hierarchicalCode: null
                };
            }

            var pageable = {
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
                size: pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };

            vm.searchResults = angular.copy(pagedResults);

            vm.sectionFilter = {
                type: 'SECTION',
                name: "",
                bom: vm.selectedBom.id
            };

            $rootScope.loadSectionBomGroups = loadSectionBomGroups;
            vm.addSectionToBom = addSectionToBom;

            function addSectionToBom(item) {
                $rootScope.showBusyIndicator($("#rightSidePanel"));
                $rootScope.addBomGroupTypeToBom(item);
                $rootScope.addedSectionTypes.push(item);

                /*var index = vm.searchResults.content.indexOf(item);
                 vm.searchResults.numberOfElements = vm.searchResults.numberOfElements - 1;
                 vm.searchResults.totalElements = vm.searchResults.totalElements - 1;
                 vm.searchResults.content.splice(index, 1);*/
            }

            function loadSectionBomGroups() {
                vm.sectionFilter.type = vm.selectedType;
                BomGroupService.getBomGroupTypesByBom(pageable, vm.sectionFilter).then(
                    function (data) {
                        vm.searchResults = data;

                        if (vm.selectedType == "SECTION") {
                            vm.placeholderTitle = "Section";
                        } else if (vm.selectedType == "SUBSYSTEM") {
                            vm.placeholderTitle = "Sub System";
                        } else if (vm.selectedType == "UNIT") {
                            vm.placeholderTitle = "Unit";
                        }
                        $rootScope.hideBusyIndicator();
                    }
                )
            }

            function searchBomTypes() {
                $rootScope.showBusyIndicator($("#rightSidePanel"));
                pageable.page = 0;
                loadSectionBomGroups();
            }

            vm.nextPage = nextPage;
            function nextPage() {
                if (vm.searchResults.last != true) {
                    $rootScope.showBusyIndicator($("#rightSidePanel"));
                    pageable.page++;
                    loadSectionBomGroups();
                }
            }

            vm.previousPage = previousPage;
            function previousPage() {
                if (vm.searchResults.first != true) {
                    $rootScope.showBusyIndicator($("#rightSidePanel"));
                    pageable.page--;
                    loadSectionBomGroups();
                }
            }

            function addItem() {
                closeRightSidePanel();
                $rootScope.hideSidePanel();
                $rootScope.hideBusyIndicator();
            }

            (function () {
                if ($application.homeLoaded == true) {
                    loadSectionBomGroups();
                    $scope.$on('app.type.bomItem.new', addItem);
                    $rootScope.$on('app.rightside.panel.closing', closeRightSidePanel);
                }
            })();
        }
    }
);