<div style="position: relative;">
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div style="padding: 20px;">
            <div class="form-group">
                <label class="control-label" translate>NAME</label> <span class="asterisk">*</span> :
                <input type="text" class="form-control" placeholder="{{'ENTER_FOLDER_NAME' | translate}}"
                       ng-disabled="newFolderVm.selectedFolderType != 'Add'" ng-model="newFolderVm.newFolder.name"
                       autofocus/>
            </div>
            <div class="form-group">
                <label class="control-label" translate>DESCRIPTION</label> :
        <textarea class="form-control" rows="3" style="resize: none;"
                  placeholder="{{'ENTER_DESCRIPTION_TITLE' | translate}}"
                  ng-model="newFolderVm.newFolder.description"></textarea>
            </div>
        </div>
    </div>
</div>