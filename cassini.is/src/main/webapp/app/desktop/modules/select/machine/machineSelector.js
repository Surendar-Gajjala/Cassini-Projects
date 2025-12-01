define(
    [
        'app/desktop/modules/proc/proc.module',
        'app/shared/services/core/itemService'
    ],

    function (module) {
        module.factory('MachineSelector', MachineSelector);

        function MachineSelector(ItemService) {
            return {
                show: show,
                getDetails: getDetails
            };

            function show($rootScope, callback) {
                console.log("Calling machine selector dialog");
                var options = {
                    title: 'Select Machine',
                    template: 'app/desktop/modules/select/machine/machineSelectionView.jsp',
                    controller: 'MachineSelectionController as machineSelectVm',
                    resolve: 'app/desktop/modules/select/machine/machineSelectionController.js',
                    width: 600,
                    side: 'left',
                    showMask: true,
                    buttons: [
                        {text: 'Select', broadcast: 'app.machine.selected'}
                    ],
                    callback: function (selectedMachine) {
                        callback(selectedMachine, selectedMachine.itemNumber);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function getDetails(id, attribute, attributeName) {
                ItemService.getMachineItem(id).then(
                    function (data) {
                        attribute.refValueString = data.itemNumber;
                        attribute[attributeName] = data.itemNumber;
                    }
                );
            }
        }
    }
);