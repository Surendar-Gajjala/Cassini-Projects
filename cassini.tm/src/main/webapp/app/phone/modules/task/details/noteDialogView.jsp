<md-dialog aria-label="Add Note">
    <form>
        <div style="line-height: 20px;background-color: #4E342E">
            <div layout="row">
                <div flex style="padding-left: 10px">
                    <h2 class="md-flex" style="width: 100%;display: inline-block; font-size: 20px;text-align: center; color: #fff">Add Note</h2>
                </div>
                <div class="text-right" style="padding-right: 10px; padding-top: 15px;">
                    <ng-md-icon icon="close" style="fill: #fff" ng-click="onCancel()"></ng-md-icon>
                </div>
            </div>
        </div>
        <md-dialog-content style="max-width:500px;max-height:500px;">
            <div style="padding: 20px;">
                <md-input-container class="md-block">
                    <label>Enter note</label>
                    <textarea ng-model="note" md-maxlength="1000" rows="10"
                              md-select-on-focus></textarea>
                </md-input-container>
            </div>
        </md-dialog-content>
        <md-dialog-actions layout="row">
            <md-button ng-click="onCancel()" >
                Cancel
            </md-button>
            <md-button ng-click="onOk()">
                Add
            </md-button>
        </md-dialog-actions>
    </form>
</md-dialog>