<div id="plan-instructions-view">
    <style scoped>
        #plan-instructions .note-editor .modal .modal-dialog .modal-footer .btn {
            color: black !important;
        }
    </style>
    <div id="plan-instructions">
        <!-- <summernote ng-model="mbomInstanceOperationInstructionsVm.planInstructions.instructions"
                    ng-if="!bopRevision.released && !bopRevision.rejected"></summernote> -->
        <span ng-if=" mbomInstanceOperationInstructionsVm.planInstructions.instructions != null && mbomInstanceOperationInstructionsVm.planInstructions.instructions != ''"
              ng-bind-html="mbomInstanceOperationInstructionsVm.planInstructions.instructionsValue">
        </span>
        <span style="padding: 10px;"
              ng-if="(mbomInstanceOperationInstructionsVm.planInstructions.instructions == null || mbomInstanceOperationInstructionsVm.planInstructions.instructions == '')">
            No digital work instructions found
        </span>
    </div>
</div>