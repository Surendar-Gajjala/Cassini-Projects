define(
    [
        'app/desktop/modules/item/item.module'
    ],
    function (module) {

        module.controller('AttributeValidationController', AttributeValidationController);

        function AttributeValidationController($scope, $q, $rootScope, $translate, $timeout) {

            var vm = this;

            var parsed = angular.element("<div></div>");
            var emptyErrorMessage = parsed.html($translate.instant("EMPTY_VALIDATION_MESSAGE")).html();
            var positiveValueValidation = parsed.html($translate.instant("POSITIVE_VALUE_VALIDATION")).html();
            var negativeValueValidation = parsed.html($translate.instant("NEGATIVE_VALUE_VALIDATION")).html();
            var minMaxZeroValidation = parsed.html($translate.instant("MIN_MAX_ZERO_VALIDATION")).html();
            var decimalZeroValidation = parsed.html($translate.instant("DECIMAL_ZERO_VALIDATION")).html();
            var minMaxConditionValidation = parsed.html($translate.instant("MIN_GREATER_MAX_VALIDATION")).html();
            var characterNegativeValidation = parsed.html($translate.instant("CHARACTER_NEGATIVE_VALIDATION")).html();
            var positiveValidation = parsed.html($translate.instant("POSITIVE_VALIDATION")).html();
            var characterConditionValidation = parsed.html($translate.instant("CHARACTER_CONDITION_VALIDATION")).html();
            var toDateValidation = parsed.html($translate.instant("TO_DATE_VALIDATION")).html();

            vm.attributeValidations = $scope.data.attributeValidations;

            function validate() {
                var valid = true;
                var minValue = null;
                var maxValue = null;
                var mintextValue = null;
                var maxtextValue = null;
                var decimalTodisplayValue = null;
                var decimalToEnterValue = null;
                var fromDate = null;
                var toDate = null;
                var isPositive = false;
                var isNegative = false;
                var data = vm.attributeValidations;

                angular.forEach(vm.attributeValidations, function (attributeValidation) {
                    if (attributeValidation.key == 'MIN_VALUE' && attributeValidation.value != null) {
                        minValue = attributeValidation.value;
                    }
                    else if (attributeValidation.key == 'MAX_VALUE' && attributeValidation.value != null) {
                        maxValue = attributeValidation.value;
                    }
                    else if (attributeValidation.key == 'MIN_LENGTH_OF_CHARACTERS' && attributeValidation.value != null) {
                        mintextValue = attributeValidation.value;
                    }
                    else if (attributeValidation.key == 'MAX_LENGTH_OF_CHARACTERS' && attributeValidation.value != null) {
                        maxtextValue = attributeValidation.value;
                    }
                    else if (attributeValidation.key == 'NO_OF_DECIMALS_TO_DISPLAY' && attributeValidation.value != null) {
                        decimalTodisplayValue = attributeValidation.value;
                    }
                    else if (attributeValidation.key == 'NO_OF_DECIMALS_TO_ENTER' && attributeValidation.value != null) {
                        decimalToEnterValue = attributeValidation.value;
                    }
                    else if (attributeValidation.key == 'ONLY_NEGATIVE_VALUES') {
                        isNegative = attributeValidation.value;
                    }
                    else if (attributeValidation.key == 'ONLY_POSITIVE_VALUES') {
                        isPositive = attributeValidation.value;
                    } else if (attributeValidation.key == "FROM_DATE") {
                        fromDate = attributeValidation.value;
                    } else if (attributeValidation.key == "TO_DATE") {
                        toDate = attributeValidation.value;
                    }


                });
                if ( minValue == null && maxValue == null && mintextValue == null && maxtextValue == null && decimalTodisplayValue == null ) {
                    valid = false;
                    $rootScope.showWarningMessage(emptyErrorMessage);
                }
               else if (parseInt(minValue) == 0 || parseInt(maxValue) == 0) {
                    valid = false;
                    $rootScope.showWarningMessage(minMaxZeroValidation);
                }
                else if (parseInt(decimalTodisplayValue) == 0 || parseInt(decimalToEnterValue) == 0) {
                    valid = false;
                    $rootScope.showWarningMessage(decimalZeroValidation);
                }
                else if (isPositive) {
                    if (minValue != null && minValue != '' && parseInt(minValue) < 0) {
                        valid = false;
                        $rootScope.showWarningMessage(positiveValueValidation);
                    }
                    else if (maxValue != null && maxValue != '' && parseInt(maxValue) < 0) {
                        valid = false;
                        $rootScope.showWarningMessage(positiveValueValidation);
                    }
                    else if (decimalTodisplayValue != null && decimalTodisplayValue != '' && parseInt(decimalTodisplayValue) < 0) {
                        valid = false;
                        $rootScope.showWarningMessage(positiveValueValidation);
                    }
                    else if (decimalToEnterValue != null && decimalToEnterValue != '' && parseInt(decimalToEnterValue) < 0) {
                        valid = false;
                        $rootScope.showWarningMessage(positiveValueValidation);
                    }
                    else if (parseInt(minValue) > parseInt(maxValue)) {
                        valid = false;
                        $rootScope.showWarningMessage(minMaxConditionValidation);
                    }
                }
                else if (isNegative) {
                    if (minValue != null && minValue != '' && parseInt(minValue) > 0) {
                        valid = false;
                        $rootScope.showWarningMessage(negativeValueValidation);
                    }
                    else if (maxValue != null && maxValue != '' && parseInt(maxValue) > 0) {
                        valid = false;
                        $rootScope.showWarningMessage(negativeValueValidation);
                    }

                    else if (decimalTodisplayValue != null && decimalTodisplayValue != '' && parseInt(decimalTodisplayValue) > 0) {
                        valid = false;
                        $rootScope.showWarningMessage(positiveValueValidation);
                    }
                    else if (decimalToEnterValue != null && decimalToEnterValue != '' && parseInt(decimalToEnterValue) > 0) {
                        valid = false;
                        $rootScope.showWarningMessage(positiveValueValidation);
                    }

                    else if (parseInt(minValue) > parseInt(maxValue)) {
                        valid = false;
                        $rootScope.showWarningMessage(minMaxConditionValidation);
                    }

                }
                else if (parseInt(minValue) > parseInt(maxValue)) {
                    valid = false;
                    $rootScope.showWarningMessage(minMaxConditionValidation);
                }
                else if (parseInt(mintextValue) < 0 || parseInt(maxtextValue) < 0) {
                    valid = false;
                    $rootScope.showWarningMessage(characterNegativeValidation);
                }
                else if (parseInt(mintextValue) == 0 || parseInt(maxtextValue) == 0) {
                    valid = false;
                    $rootScope.showWarningMessage(positiveValidation);
                }
                else if (parseInt(mintextValue) > 0 && parseInt(maxtextValue) > 0 && parseInt(mintextValue) > parseInt(maxtextValue)) {
                    valid = false;
                    $rootScope.showWarningMessage(characterConditionValidation);
                } else if (fromDate != null && toDate != null) {
                    var fromDateValue = moment(fromDate, $rootScope.applicationDateSelectFormat);
                    var toDateValue = moment(toDate, $rootScope.applicationDateSelectFormat);
                    var val = fromDateValue.isSame(toDateValue) || fromDateValue.isBefore(toDateValue);
                    if (!val) {
                        valid = false;
                        $rootScope.showWarningMessage(toDateValidation);
                    }
                }

                return valid;

            }

            function addValidations() {
                if (validate()) {
                    $scope.callback(vm.attributeValidations);
                }

            }

            $scope.checkValidation = checkValidation;
            function checkValidation(validation) {
                var valid = true;
                if (validation.key == "POSITIVE_AND_NEGATIVE" || validation.key == "ONLY_NEGATIVE_VALUES" || validation.key == "ONLY_POSITIVE_VALUES"
                    || validation.key == "ALL_CAPITAL" || validation.key == "ALL_SMALL" || validation.key == "CAPITAL_AND_SMALL") {
                    angular.forEach(vm.attributeValidations, function (attributeValidation) {
                        if (validation.key != attributeValidation.key && (attributeValidation.key == "POSITIVE_AND_NEGATIVE" || attributeValidation.key == "ONLY_NEGATIVE_VALUES"
                            || attributeValidation.key == "ONLY_POSITIVE_VALUES" || attributeValidation.key == "ALL_CAPITAL" || attributeValidation.key == "ALL_SMALL"
                            || attributeValidation.key == "CAPITAL_AND_SMALL") && attributeValidation.value) {
                            valid = false;
                        }
                    })
                } else {
                    valid = true;
                }

                return valid;
            }

            (function () {
                $rootScope.$on('app.attribute.validations', addValidations);
            })();
        }
    }
)
;