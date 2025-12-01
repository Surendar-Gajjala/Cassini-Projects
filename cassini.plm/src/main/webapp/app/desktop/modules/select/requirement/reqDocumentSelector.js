define(
    [
        'app/desktop/modules/mes/mes.module',
        'app/shared/services/core/reqDocumentService'
    ],

    function (module) {
        module.factory('ReqDocumentSelector', ReqDocumentSelector);

        function ReqDocumentSelector(ReqDocumentService) {
            return {
                show: show,
                getDetails: getDetails
            };

            function show($rootScope, attributeDef, callback) {
                var options = {
                    title: 'Select Req Document',
                    template: 'app/desktop/modules/select/requirement/reqDocumentSelectionView.jsp',
                    controller: 'ReqDocumentSelectionController as reqDocumentSelectVm',
                    resolve: 'app/desktop/modules/select/requirement/reqDocumentSelectionController',
                    width: 620,
                    data: {
                        existObjectId: $rootScope.objectAttributeValue,
                        selectAttDef: attributeDef
                    },
                    showMask: true,
                    side: 'left',
                    buttons: [
                        {text: $rootScope.add, broadcast: 'app.select.reqDocument'}
                    ],
                    callback: function (selectedPart) {
                        callback(selectedPart, selectedPart.number);
                        $rootScope.hideSidePanel("left");
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function getDetails(id, attribute) {
                ReqDocumentService.getReqDocument(id).then(
                    function (data) {
                        attribute.refValueString = data.number;
                    }
                );
            }
        }
    }
);
