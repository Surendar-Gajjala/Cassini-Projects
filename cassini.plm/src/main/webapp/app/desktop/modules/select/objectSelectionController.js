/**
 * Created by GSR on 24/12/18.
 */
define(
    [
        'app/desktop/modules/item/item.module',
        'app/desktop/modules/select/person/personSelector',
        //'app/assets/bower_components/cassini-platform/app/desktop/modules/select/person/personSelector',
        'app/desktop/modules/select/changes/changesSelector',
        'app/desktop/modules/select/item/itemSelector',
        'app/desktop/modules/select/itemRevision/itemRevisionSelector',
        'app/desktop/modules/select/mfrParts/mrPartSelector',
        'app/desktop/modules/select/mfr/mfrSelector',
        'app/desktop/modules/select/project/projectSelector',
        'app/desktop/modules/select/workflow/workflowSelector',
        'app/desktop/modules/select/requirement/requirementSelector',
        'app/desktop/modules/select/mes/mesObjectSelector',
        'app/desktop/modules/select/mro/mroObjectSelector',
        'app/desktop/modules/select/customObject/customObjectSelector',
        'app/desktop/modules/select/requirement/reqDocumentSelector',
        'app/desktop/modules/select/quality/qualityObjectSelector'
    ],
    function (module) {
        module.controller('ObjectSelectionController', ObjectSelectionController);

        function ObjectSelectionController($scope, $rootScope, $timeout, $state, $stateParams, $window, $cookies, $cookieStore, $application,
                                           PersonSelector, ChangeSelector, ItemSelector, ItemRevisionSelector, MfrSelector, MfrPartSelector,
                                           ProjectSelector, WorkflowSelector, RequirementSelector, MESObjectSelector, ReqDocumentSelector,
                                           MROObjectSelector, CustomObjectSelector, QualityObjectSelector) {

            var vm = this;
            vm.loadObjectSelectors = loadObjectSelectors;

            function loadObjectSelectors() {
                $application.registerObjectSelector('PERSON', PersonSelector);
                $application.registerObjectSelector('ITEM', ItemSelector);
                $application.registerObjectSelector('ITEMREVISION', ItemRevisionSelector);
                $application.registerObjectSelector('CHANGE', ChangeSelector);
                $application.registerObjectSelector('BOMITEM', ProjectSelector);
                $application.registerObjectSelector('WORKFLOW', WorkflowSelector);
                $application.registerObjectSelector('MANUFACTURER', MfrSelector);
                $application.registerObjectSelector('MANUFACTURERPART', MfrPartSelector);
                $application.registerObjectSelector('PROJECT', ProjectSelector);
                $application.registerObjectSelector('REQUIREMENT', RequirementSelector);
                $application.registerObjectSelector('MESOBJECT', MESObjectSelector);
                $application.registerObjectSelector('MROOBJECT', MROObjectSelector);
                $application.registerObjectSelector('CUSTOMOBJECT', CustomObjectSelector);
                $application.registerObjectSelector('REQUIREMENTDOCUMENT', ReqDocumentSelector);
                $application.registerObjectSelector('MFRSUPPLIER', MROObjectSelector);
                $application.registerObjectSelector('QUALITY', QualityObjectSelector);
            }

            (function () {
                loadObjectSelectors();
            })();
        }
    }
)
;

