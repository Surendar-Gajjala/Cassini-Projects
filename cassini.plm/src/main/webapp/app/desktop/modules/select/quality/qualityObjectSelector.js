/**
 * Created by GSR on 26/12/18.
 */
define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/shared/services/core/qualityTypeService'
    ],

    function (module) {
        module.factory('QualityObjectSelector', QualityObjectSelector);

        function QualityObjectSelector(QualityTypeService) {
            return {
                show: show,
                getDetails: getDetails
            };

            function show($rootScope, attributeDef, callback) {
                var options = {
                    title: 'Select Quality Object',
                    template: 'app/desktop/modules/select/quality/qualityObjectsSelectionView.jsp',
                    controller: 'QualityObjectsSelectionController as qualityObjectSelectVm',
                    resolve: 'app/desktop/modules/select/quality/qualityObjectsSelectionController',
                    width: 620,
                    data: {
                        existObjectId: $rootScope.objectAttributeValue,
                        selectAttDef: attributeDef
                    },
                    showMask: true,
                    side: 'left',
                    buttons: [
                        {text: $rootScope.add, broadcast: 'app.select.qualityObject'}
                    ],
                    callback: function (selectedType) {
                        if (selectedType.objectType == "PRODUCTINSPECTIONPLAN" || selectedType.objectType == "MATERIALINSPECTIONPLAN") {
                            callback(selectedType, selectedType.number);
                        } else if (selectedType.objectType == "PROBLEMREPORT") {
                            callback(selectedType, selectedType.prNumber);
                        } else if (selectedType.objectType == "NCR") {
                            callback(selectedType, selectedType.ncrNumber);
                        } else if (selectedType.objectType == "QCR") {
                            callback(selectedType, selectedType.qcrNumber);
                        }
                        $rootScope.hideSidePanel("left");
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function getDetails(id, attribute) {
                QualityTypeService.getMESObject(id).then(
                    function (data) {
                        attribute.refValueString = data.number;
                    }
                );
            }
        }
    }
);
