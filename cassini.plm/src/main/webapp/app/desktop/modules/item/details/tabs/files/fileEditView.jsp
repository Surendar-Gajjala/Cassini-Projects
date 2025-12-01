<div style="position: relative;">
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div style="padding: 20px;">
            <div class="form-group">
                <label class="control-label" translate>ITEM_FILENAME_TITLE</label> :
                <input type="text" class="form-control"
                       ng-model="fileEditVm.files.name" disabled/>
            </div>
            <div class="form-group">
                <label class="control-label" translate>DESCRIPTION</label> :
                    <textarea class="form-control" rows="3" style="resize: none;"
                              ng-disabled="itemRevision.released || itemRevision.rejected || !fileEditVm.fileEditPermission"
                              ng-model="fileEditVm.files.description"></textarea>
            </div>
            <%--<attribute-details-view object-type="FILE"
                                    object-id="fileEditVm.filesView.id"></attribute-details-view>--%>
            <basic-attribute-details-view object-type="FILE"
                                          has-permission="fileEditVm.fileEditPermission"
                                          quality-type="FILE"
                                          object-id="fileEditVm.filesView.id"></basic-attribute-details-view>
        </div>
    </div>
</div>
