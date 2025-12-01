define(['app/app.modules',
        'app/components/common/commonFactory',
        'app/components/prod/supplier/suppliersFactory',
        'app/components/prod/material/materialFactory',
        'app/components/prod/supplier/dialog/selectMaterialController'
    ],
    function ($app) {
        $app.controller('NewSupplierController',
            [
                '$scope', '$rootScope', '$timeout', '$interval', '$state', '$stateParams', '$cookies', '$modal',
                'commonFactory', 'suppliersFactory', 'materialFactory',
                function ($scope, $rootScope, $timeout, $interval, $state, $stateParams, $cookies, $modal,
                          commonFactory, suppliersFactory, materialFactory) {

                    $scope.loading = true;
                    $rootScope.createOrSave = true;

                    $scope.addr = {
                        id: null,
                        addressType: commonFactory.getAddressTypeByName("Office"),
                        addressText: null,
                        city: null,
                        state: null,
                        pincode: null,
                        country: commonFactory.getCountryAndStatesMapByCountry("India").country
                    };

                    $scope.contactPersonn = {
                        id: null,
                        personType: commonFactory.getPersonTypeByName("Customer"),
                        firstName: null,
                        lastName: null,
                        phoneMobile: "",
                        email: ""
                    };

                    $scope.supplier = {
                        id: null,
                        name: null,
                        officePhone: null,
                        officeFax: null,
                        officeEmail: null,
                        address: angular.copy($scope.addr),
                        contactPerson: angular.copy($scope.contactPersonn),
                        materialSuppliers: []
                    };

                    $scope.states = commonFactory.getCountryAndStatesMapByCountry("India").states;

                    $rootScope.saveSupplier = function () {
                        if (validate($scope.supplier)) {
                            suppliersFactory.updateSupplier($scope.supplier).then(
                                function (data) {
                                    $state.go('app.prod.suppliers');
                                }
                            )
                        }

                    };

                    $scope.$on('$viewContentLoaded', function () {
                        $rootScope.setToolbarTemplate('new-supplier-view-tb');

                    });

                    $rootScope.cancelSupplier = function () {
                        $state.go('app.prod.suppliers');

                    };
                    $rootScope.createSupplier = function () {
                        if (validate($scope.supplier)) {

                            suppliersFactory.createSupplier($scope.supplier).then(
                                function (data) {
                                    $state.go('app.prod.suppliers');
                                }
                            )
                        }

                    };

                    $rootScope.validateSupplier = function () {
                        if ($scope.supplier.name != "" && $scope.supplier.officePhone != "") {
                            suppliersFactory.validateSupplier($scope.supplier.name, $scope.supplier.officePhone).then(
                                function (data) {
                                    if (data != "") {
                                        $rootScope.showErrorMessage("Supplier: " + $scope.supplier.name + " is already exists");
                                    }
                                }
                            )
                        }
                    };

                    function loadSupplier(supplierId) {
                        suppliersFactory.getSupplier(supplierId).then(
                            function (data) {
                                $scope.supplier = data;
                                materialFactory.getMaterialSuppliersBySupplier(supplierId).then(
                                    function(data){
                                        $scope.supplier.materialSuppliers = data;
                                        materialFactory.getMaterialNameReferences($scope.supplier.materialSuppliers, 'materialId');
                                    }
                                )
                            }
                        );
                    }


                    function validate(supplier) {
                        var valid = true;

                        if (supplier.name == null || supplier.name.trim() == "") {
                            valid = false;
                            $rootScope.showErrorMessage("Supplier name cannot be empty");
                        }
                        else if (supplier.address.addressText == null || supplier.address.addressText.trim() == "") {
                            valid = false;
                            $rootScope.showErrorMessage("Address text cannot be empty");
                        }
                        else if (supplier.address.city == null || supplier.address.city.trim() == "") {
                            valid = false;
                            $rootScope.showErrorMessage("Address city cannot be empty");
                        }
                        else if (supplier.address.state == null) {
                            valid = false;
                            $rootScope.showErrorMessage("Address state cannot be empty");
                        }
                        else if (supplier.address.pincode == null || supplier.address.pincode.trim() == "") {
                            valid = false;
                            $rootScope.showErrorMessage("Address pincode cannot be empty");
                        }
                        else if (supplier.contactPerson.firstName == null || supplier.contactPerson.firstName.trim() == "") {
                            valid = false;
                            $rootScope.showErrorMessage("Contact person first name cannot be empty");
                        }
                        else if (supplier.contactPerson.lastName == null || supplier.contactPerson.lastName.trim() == "") {
                            valid = false;
                            $rootScope.showErrorMessage("Contact person last name cannot be empty");
                        }

                        return valid;
                    }

                    $scope.addMaterial = function () {
                        var materialSuppliers = [];
                        var modalInstance = $modal.open({
                            animation: true,
                            templateUrl: 'app/components/prod/supplier/dialog/selectMaterialDialog.jsp',
                            controller: 'SelectMaterialController',
                            size: 'md',
                            resolve: {
                                supplierId: function () {
                                    return $stateParams.supplierId;
                                },
                                "dialogTitle": "Select material for suppliers"
                            }
                        });

                        modalInstance.result.then(
                            function (selectedMaterials) {
                                angular.forEach(selectedMaterials, function (material) {
                                    var ms = {
                                        materialId: material.id,
                                        cost: material.cost,
                                        supplierId: $stateParams.supplierId
                                    };
                                    materialSuppliers.push(ms);
                                });
                                materialFactory.createMaterialSuppliers(materialSuppliers).then(
                                    function (data) {
                                        loadSupplier($stateParams.supplierId);
                                    }
                                )
                            }
                        );
                    };

                    $scope.deleteMaterialSupplier = function (ms) {
                        materialFactory.deleteMaterialSupplier(ms).then(
                            function (data) {
                                var index = $scope.supplier.materialSuppliers.indexOf(ms);
                                $scope.supplier.materialSuppliers.splice(index,1);
                                $rootScope.showSuccessMessage("Material supplier deleted successfully");
                            }
                        )
                    };

                    (function () {
                        $rootScope.createOrSave = true;
                        if ($stateParams.supplierId != null && $stateParams.supplierId != undefined && $stateParams.supplierId > 0) {
                            $rootScope.createOrSave = false;
                            loadSupplier($stateParams.supplierId);

                        } else {
                            $rootScope.createOrSave = true;
                        }

                    })();
                }
            ]
        );
    }
);


