define(
    [
        'app/desktop/modules/customObject/customObject.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/customObjectService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/lovService',
        'app/desktop/directives/plugin-directive/pluginTableActionDirective',
        'app/shared/services/core/supplierAuditService'
    ],
    function (module) {
        module.controller('AuditController', AuditController);

        function AuditController($scope, $rootScope, $timeout, $state, $stateParams, $q, $translate, $application,
                                 LovService, DialogService, SupplierAuditService) {
            var vm = this;

            vm.printTemplate = printTemplate;
            vm.customId = $stateParams.customId;
            vm.months = [
                {label: "Jan", value: 0},
                {label: "Feb", value: 1},
                {label: "Mar", value: 2},
                {label: "Apr", value: 3},
                {label: "May", value: 4},
                {label: "Jun", value: 5},
                {label: "Jul", value: 6},
                {label: "Aug", value: 7},
                {label: "Sep", value: 8},
                {label: "Oct", value: 9},
                {label: "Nov", value: 10},
                {label: "Dec", value: 11}
            ];

            vm.statuses = ['Audit Planned', 'Corrective Action Needed', 'Audit Conducted', 'Audit Closed'];

            function loadValues() {
                vm.loading = true;
                LovService.getLovByName("Internal Audit Areas").then(
                    function (data) {
                        vm.internalAuditAreas = data;
                        LovService.getLovByName("Internal Audit Products").then(
                            function (data) {
                                vm.internalAuditProducts = data;
                                if (vm.internalAuditProducts != null && vm.internalAuditProducts != "") {
                                    vm.products = vm.internalAuditProducts.values;
                                }
                                loadAudit();
                            }
                        )
                    }
                )
            }

            vm.internalAudit = null;
            $rootScope.loadAudit = loadAudit;
            function loadAudit() {
                SupplierAuditService.getAlbonairInternalAudit(vm.customId).then(
                    function (data) {
                        vm.areaTable = new Hashtable();
                        vm.internalAudit = data;
                        if (vm.internalAudit != null && vm.internalAudit.audits.length == 0) {
                            if (vm.internalAuditAreas != "" && vm.internalAuditAreas != null) {
                                var areas = [];
                                angular.forEach(vm.months, function (month) {
                                    var products = angular.copy({
                                        month: month.value,
                                        products: []
                                    });
                                    products.products.push(angular.copy({number: null, auditor: null}));
                                    areas.push(products);
                                    vm.areaTable.put("System Audit External" + month.value, products)
                                });
                                vm.internalAudit.audits.push(angular.copy({
                                    name: "System Audit External",
                                    areas: areas
                                }));
                                areas = [];
                                angular.forEach(vm.months, function (month) {
                                    var products = angular.copy({
                                        month: month.value,
                                        products: []
                                    });
                                    products.products.push(angular.copy({number: null, auditor: null}));
                                    areas.push(products);
                                    vm.areaTable.put("System Audit Internal" + month.value, products)
                                });

                                vm.internalAudit.audits.push(angular.copy({
                                    name: "System Audit Internal",
                                    areas: areas
                                }));

                                angular.forEach(vm.internalAuditAreas.values, function (value) {
                                    var areas = [];
                                    angular.forEach(vm.months, function (month) {
                                        var products = angular.copy({
                                            month: month.value,
                                            products: [],
                                            selected: []
                                        });
                                        areas.push(products);
                                        vm.areaTable.put(value + "" + month.value, products)
                                    })
                                    vm.internalAudit.audits.push(angular.copy({name: value, areas: areas}));
                                })
                            }
                        } else {
                            angular.forEach(vm.internalAudit.audits, function (audit) {
                                audit.editMode = false;
                                angular.forEach(audit.areas, function (area) {
                                    area.selected = [];
                                    angular.forEach(area.products, function (product) {
                                        area.selected.push(product.name);
                                    })
                                    vm.areaTable.put(audit.name + "" + area.month, area)
                                })
                            })
                        }
                        vm.loading = false;
                    }
                )
            }

            vm.selectedProduct = null;
            vm.selectedStatus = null;
            vm.selectProduct = selectProduct;
            function selectProduct(product, audit, month) {
                var areaProducts = vm.areaTable.get(audit.name + "" + month.value);
                if (areaProducts != null) {
                    areaProducts.products.push({name: product, auditor: null, status: null});
                    vm.areaTable.put(audit.name + "" + month.value, areaProducts);
                    angular.forEach(audit.areas, function (area) {
                        if (area.month == month.value) {
                            area.products = areaProducts.products;
                        }
                    })
                }
            }

            vm.removeProduct = removeProduct;
            function removeProduct(productName, audit, month) {
                var areaProducts = vm.areaTable.get(audit.name + "" + month.value);
                if (areaProducts != null) {
                    angular.forEach(areaProducts.products, function (product) {
                        if (productName == product.name) {
                            areaProducts.products.splice(areaProducts.products.indexOf(product), 1);
                        }
                    })
                    vm.areaTable.put(audit.name + "" + month.value, areaProducts);
                    angular.forEach(audit.areas, function (area) {
                        if (area.month == month.value) {
                            angular.forEach(area.products, function (product) {
                                if (productName == product.name) {
                                    area.products.splice(area.products.indexOf(product), 1);
                                }
                            })
                        }
                    })
                }
            }

            vm.getProducts = getProducts;
            function getProducts(audit, month) {
                return vm.areaTable.get(audit.name + "" + month.value);
            }

            vm.getStatus = getStatus;
            function getStatus(audit, month, productName) {
                var areaProducts = vm.areaTable.get(audit.name + "" + month.value);
                if (areaProducts != null) {
                    angular.forEach(areaProducts.products, function (product) {
                        if (productName == product.name) {
                            return product;
                        }
                    })
                }
            }

            vm.selectStatus = selectStatus;
            function selectStatus(audit, month, product, status) {

            }

            vm.editAudit = editAudit;
            vm.cancelChanges = cancelChanges;
            vm.saveAudit = saveAudit;

            function editAudit(audit) {
                audit.editMode = true;
                audit.oldAreas = angular.copy(audit.areas);
            }

            function cancelChanges(audit) {
                audit.editMode = false;
                audit.areas = audit.oldAreas;
                angular.forEach(vm.internalAudit.audits, function (audit) {
                    audit.editMode = false;
                    angular.forEach(audit.areas, function (area) {
                        area.selected = [];
                        angular.forEach(area.products, function (product) {
                            area.selected.push(product.name);
                        })
                        vm.areaTable.put(audit.name + "" + area.month, area)
                    })
                })
            }

            function saveAudit(audit) {
                if (validateAudit(audit)) {
                    $rootScope.showBusyIndicator($('.view-container'));
                    SupplierAuditService.createAlbonairInternalAudit(vm.customId, vm.internalAudit).then(
                        function (data) {
                            vm.internalAudit = data;
                            angular.forEach(vm.internalAudit.audits, function (audit) {
                                audit.editMode = false;
                                angular.forEach(audit.areas, function (area) {
                                    area.selected = [];
                                    angular.forEach(area.products, function (product) {
                                        area.selected.push(product.name);
                                    })
                                    vm.areaTable.put(audit.name + "" + area.month, area)
                                })
                            })
                            $rootScope.showSuccessMessage("Audit saved successfully");
                            $rootScope.hideBusyIndicator();
                        }, function (error) {
                            $rootScope.showErrorMessage(error.message);
                            $rootScope.hideBusyIndicator();
                        }
                    )
                }
            }

            function validateAudit(audit) {
                var valid = true;


                return valid;
            }

            function printTemplate() {
                var contents = document.getElementById("4mcrf").innerHTML;
                var frame1 = document.createElement('iframe');
                frame1.name = "frame1";
                frame1.style.position = "absolute";
                frame1.style.top = "-1000000px";
                document.body.appendChild(frame1);
                var frameDoc = frame1.contentWindow ? frame1.contentWindow : frame1.contentDocument.document ? frame1.contentDocument.document : frame1.contentDocument;
                frameDoc.document.open();
                frameDoc.document.write('<html><head><link rel="stylesheet" type="text/css" href="app/assets/css/app/desktop/4mcrf.css" media="print" /></head><body onload="window.print()">' + contents + '</body></html>');
                frameDoc.document.close();

                return false;
            }

            $scope.checkAllAreaApproved = checkAllAreaApproved;
            function checkAllAreaApproved() {
                var disable = false;
                var count = 0;
                if (vm.internalAudit != null && vm.internalAudit.audits.length == 0) {
                    angular.forEach(vm.internalAudit.audits, function (audit) {
                        if (!disable && audit.name != 'System Audit External' && audit.name != 'System Audit Internal') {
                            angular.forEach(audit.areas, function (area) {
                                angular.forEach(area.products, function (product) {
                                    count++;
                                    if (product.status == null || product.status == "") {
                                        disable = true;
                                    } else if (product.status != "Audit Closed") {
                                        disable = true;
                                    }
                                })
                            })
                        }
                    })
                }
                if (count == 0) {
                    disable = true;
                }
                return disable;
            }

            module.directive('appFilereader', function ($q) {
                var slice = Array.prototype.slice;
                return {
                    restrict: 'A',
                    require: '?ngModel',
                    link: function (scope, element, attrs, ngModel) {
                        if (!ngModel) return;

                        ngModel.$render = function () {
                        };

                        element.bind('change', function (e) {
                            var element = e.target;

                            $q.all(slice.call(element.files, 0).map(readFile))
                                .then(function (values) {
                                    if (element.multiple) ngModel.$setViewValue(values);
                                    else ngModel.$setViewValue(values.length ? values[0] : null);
                                });

                            function readFile(file) {
                                var deferred = $q.defer();

                                var reader = new FileReader();
                                reader.onload = function (e) {
                                    deferred.resolve(e.target.result);
                                };
                                reader.onerror = function (e) {
                                    deferred.reject(e);
                                };
                                reader.readAsDataURL(file);

                                return deferred.promise;
                            }

                        }); //change

                    } //link
                }; //return
            });

            (function () {
                $scope.$on('app.customObj.tabActivated', function (event, data) {
                    if (data.tabId == 'details.audit') {
                        loadValues();
                    }
                });
            })();
        }
    }
);
