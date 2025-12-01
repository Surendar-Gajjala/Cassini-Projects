<div style="position: relative;">
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div style="padding: 20px;">
            <div class="form-group">
                <label class="control-label" translate>ITEM_FILENAME_TITLE</label> :
                <input type="text" class="form-control"
                       ng-model="fileEditVm.files.name" readonly/>
            </div>
            <div class="form-group">
                <label class="control-label" translate>DESCRIPTION</label> :
                    <textarea class="form-control" rows="3" style="resize: none;"
                              ng-model="fileEditVm.files.description"></textarea>
            </div>
        </div>
    </div>
</div>
