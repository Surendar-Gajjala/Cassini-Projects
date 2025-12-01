/**
 * Created by GSR on 26/12/18.
 */
define(
    [
        'app/desktop/modules/item/item.module'
    ],

    function (module) {
        module.factory('WorkflowSelector', WorkflowSelector);

        function WorkflowSelector(WorkflowService) {
            return {
                show: show,
                getDetails: getDetails
            };

            function show($rootScope, attributeDef, callback) {
                var options = {
                    title: $rootScope.selectWorkflowTitle,
                    template: 'app/desktop/modules/select/workflow/workflowSelectionView.jsp',
                    controller: 'WorkflowSelectionController as wrkSelectVm',
                    resolve: 'app/desktop/modules/select/workflow/workflowSelectionController',
                    width: 650,
                    data: {
                        existObjectId: $rootScope.objectAttributeValue,
                        selectAttDef: attributeDef
                    },
                    showMask: true,
                    side: 'left',
                    buttons: [
                        {text: $rootScope.add, broadcast: 'app.select.workflow'}
                    ],
                    callback: function (selectedTask) {
                        callback(selectedTask, selectedTask.master.number);
                        $rootScope.hideSidePanel("left");
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function getDetails(id, attribute) {
                WorkflowService.getWorkflow(id).then(
                    function (data) {
                        attribute.refValueString = data.name;
                    }, function (error) {
                        $rootScope.showErrorMessage(error.message);
                    }
                );
            }
        }
    }
);
