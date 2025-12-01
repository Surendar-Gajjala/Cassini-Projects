/**
 * Created by swapna on 28/12/18.
 */
define(
    [
        'app/desktop/modules/proc/proc.module',
        'app/shared/services/store/customIndentService'
    ],

    function (module) {
        module.factory('CustomIndentSelector', CustomIndentSelector);

        function CustomIndentSelector(CustomIndentService) {
            return {
                show: show,
                getDetails: getDetails
            };

            function show($rootScope, callback) {
                console.log("Calling customIndent selector dialog");
                var options = {
                    title: 'Select CustomIndent',
                    template: 'app/desktop/modules/select/customIndent/customIndentSelectionView.jsp',
                    controller: 'CustomIndentSelectionController as customIndentSelectVm',
                    resolve: 'app/desktop/modules/select/customIndent/customIndentSelectionController.js',
                    width: 600,
                    side: 'left',
                    showMask: true,
                    buttons: [
                        {text: 'Select', broadcast: 'app.customIndent.selected'}
                    ],
                    callback: function (selectedCustomIndent) {
                        callback(selectedCustomIndent, selectedCustomIndent.indentNumber);
                    }
                };

                $rootScope.showSidePanel(options);
            }

            function getDetails(id, attribute, attributeName) {
                CustomIndentService.getIndent(id).then(
                    function (data) {
                        attribute.refValueString = data.indentNumber;
                        attribute[attributeName] = data.indentNumber;
                    }
                );
            }
        }
    }
);