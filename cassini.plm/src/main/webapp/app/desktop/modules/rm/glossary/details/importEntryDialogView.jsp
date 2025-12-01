<div style="position: relative;">
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin-right: 48px;">
            <div>
                <br>

                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span>{{importEntryDialogVm.selectFileTitle}}</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="file" class="form-control" name="title"
                                   ng-file-model="importEntryDialogVm.selectedFile">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>COMMENTS</span> : </label>

                        <div class="col-sm-7">
                            <textarea name="description" rows="10" class="form-control" style="resize: none"
                                      placeholder="Enter comment"
                                      ng-model="importEntryDialogVm.importComment">
                            </textarea>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>