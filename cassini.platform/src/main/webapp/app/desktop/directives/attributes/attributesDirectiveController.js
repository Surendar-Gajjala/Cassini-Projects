/**
 * Created by Nageshreddy on 13-07-2017.
 */
define(
    [
        'app/desktop/desktop.app',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/objectAttributeService'
    ],

    function (module) {
        module.directive('attributesView', ['$rootScope', '$compile', '$timeout', 'ObjectAttributeService', '$application', function ($rootScope, $compile, $timeout, ObjectAttributeService, $application) {

            return {
                templateUrl: 'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/attributesDirective.jsp',
                restrict: 'E',
                replace: false,
                scope: {
                    object: '=',
                    attributes: '=',
                    showObjects: '='
                },
                link: function (scope, elem, attr) {

                    scope.showSelectionDialog = showSelectionDialog;

                    function showSelectionDialog(objectType, attribute) {
                        var objectSelector = $application.getObjectSelector(objectType);
                        if (objectSelector != null) {
                            if (attribute.refValue != null && attribute.refValue != "") {
                                $rootScope.objectAttributeValue = attribute.refValue;
                            }
                            objectSelector.show($rootScope, attribute.attributeDef, function (object, displayValue) {
                                attribute.refValue = object.id;
                                attribute.refValueString = displayValue;
                            });
                        }
                    }

                    scope.showitemAttributePopoverUrl = 'app/assets/bower_components/cassini-platform/app/desktop/directives/attributes/configurableAttributePopoverTemplate.jsp';

                    scope.checkValidations = checkValidations;
                    function checkValidations(attribute) {
                        if (attribute.attributeDef.dataType == "TEXT") {
                            angular.forEach(attribute.attributeDef.newValidations, function (validation) {
                                if (validation.key == "ALL_CAPITAL" && validation.value) {
                                    attribute.stringValue = attribute.stringValue.toUpperCase();
                                } else if (validation.key == "ALL_SMALL" && validation.value) {
                                    attribute.stringValue = attribute.stringValue.toLowerCase();
                                }
                            })
                        } else if (attribute.attributeDef.dataType == "LONGTEXT") {
                            angular.forEach(attribute.attributeDef.newValidations, function (validation) {
                                if (validation.key == "ALL_CAPITAL" && validation.value) {
                                    attribute.longTextValue = attribute.longTextValue.toUpperCase();
                                } else if (validation.key == "ALL_SMALL" && validation.value) {
                                    attribute.longTextValue = attribute.longTextValue.toLowerCase();
                                }
                            })
                        }
                    }

                    scope.attributeValidationsPopover = {
                        templateUrl: 'app/desktop/modules/directives/attributeValidations/attributeValidationsPopover.jsp'
                    };

                    ObjectAttributeService.getCurrencies().then(
                        function (data) {
                            scope.currencies = data;
                        }
                    );

                    function loadGroupAttributes() {
                        scope.objectProperties = [];
                        scope.groupAttributes = [];
                        var groupAttributeMap = new Hashtable();
                        angular.forEach(scope.attributes, function (attribute) {
                            if (attribute.attributeDef.attributeGroup != null && attribute.attributeDef.attributeGroup != "") {
                                var groupAttribute = groupAttributeMap.get(attribute.attributeDef.attributeGroup);
                                if (groupAttribute == null) {
                                    var groupAttributes = {
                                        groupName: attribute.attributeDef.attributeGroup,
                                        objectProperties: [attribute]
                                    }
                                    groupAttributeMap.put(attribute.attributeDef.attributeGroup, groupAttributes);
                                } else {
                                    groupAttribute.objectProperties.push(attribute);
                                    groupAttributeMap.put(attribute.attributeDef.attributeGroup, groupAttribute)
                                }
                            } else {
                                scope.objectProperties.push(attribute);
                            }
                        })
                        scope.groupAttributes = groupAttributeMap.values();
                    }

                    scope.selectMultiListCheckBox = selectMultiListCheckBox;
                    function selectMultiListCheckBox(attribute, value) {
                        if (attribute.mlistValue.indexOf(value) == -1) {
                            attribute.mlistValue.push(value);
                        } else {
                            attribute.mlistValue.splice(attribute.mlistValue.indexOf(value), 1);
                        }
                    }

                    scope.checkForHideAttribute = checkForHideAttribute;
                    function checkForHideAttribute(attribute) {
                        var hide = false;
                        if (attribute.attributeDef.objectType == "CUSTOMOBJECT" && attribute.attributeDef.name == "Albonair Internal Audit") {
                            hide = true;
                        }
                        return hide;
                    }

                    (function () {
                        loadGroupAttributes();
                    })();

                }
            };
        }]);
    }
);
