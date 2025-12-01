define(['app/app.modules'],
    function(app) {
    app.factory('ERPLists', [
        function(){
            return {
                orderStatus: [
                    'NEW',
                    'APPROVED',
                    'PROCESSING',
                    'PROCESSED',
                    'PARTIALLYSHIPPED',
                    'CANCELLED',
                    'SHIPPED'
                ],
                orderTypes: [
                    'PRODUCT',
                    'SAMPLE',
                    'QUESTIONPAPER'
                ],
                customerTypes: [
                    'SCHOOL',
                    'DISTRIBUTOR'
                ],
                inventoryStatuses:[
                    'STOCKIN',
                    'STOCKOUT'
                ]

            }
        }
    ]);
});