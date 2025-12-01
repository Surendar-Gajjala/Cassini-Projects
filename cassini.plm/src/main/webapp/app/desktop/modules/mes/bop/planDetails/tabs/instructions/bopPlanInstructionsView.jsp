<div id="plan-instructions-view">
    <style scoped>
        #plan-instructions .note-editor .modal .modal-dialog .modal-footer .btn {
            color: black !important;
        }
    </style>
    <div id="plan-instructions">
        <summernote ng-model="bopPlanInstructionsVm.planInstructions.instructions"
                    ng-if="!bopRevision.released && !bopRevision.rejected"></summernote>
        <span ng-if="(bopRevision.released || bopRevision.rejected) && bopPlanInstructionsVm.planInstructions.instructions != null && bopPlanInstructionsVm.planInstructions.instructions != ''"
              ng-bind-html="bopPlanInstructionsVm.planInstructions.instructionsValue">
        </span>
        <span style="padding: 10px;"
              ng-if="(bopRevision.released || bopRevision.rejected) && (bopPlanInstructionsVm.planInstructions.instructions == null || bopPlanInstructionsVm.planInstructions.instructions == '')">
            No digital work instructions found
        </span>
    </div>
</div>