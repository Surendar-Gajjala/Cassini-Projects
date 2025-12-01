<div style="position: relative;">
    <div class="view-content no-padding" style="overflow-y: hidden;">
        <div class="row row-eq-height" style="margin: 0;">
            <div class="col-sm-12" style="padding: 10px;">
                <form class="form-horizontal">
                    <div>
                        <div class="form-group" style="margin: 0px;">
                            <label class="col-sm-3 control-label" style="text-align: left;margin: 0px;">
                                <span translate>NAME</span>
                                <span class="asterisk">*</span> : </label>
                        </div>
                        <div class="form-group" style="margin: 0px;">
                            <div class="col-sm-12">
                                <input type="text" class="form-control" name="title"
                                       ng-model="editReqVm.requirementDetails.name"/>
                            </div>
                        </div>
                        <div class="form-group" style="margin: 0px;">
                            <label class="col-sm-3 control-label" style="margin: 0px;text-align: left;">
                                <span translate>DESCRIPTION</span>
                                <span class="asterisk">*</span> : </label>
                        </div>
                        <div class="form-group" style="margin: 0px;">
                            <div class="col-sm-12">

                                <summernote
                                        ng-model="editReqVm.requirementDetails.description"></summernote>

                            </div>
                        </div>
                        <div class="form-group" style="margin: 0px;">
                            <label class="col-sm-3 control-label" style="margin: 0px;text-align: left;">
                                <span translate>NOTES</span>: </label>
                        </div>
                        <div class="form-group" style="margin: 0px;">
                            <div class="col-sm-12">
                                                <textarea name="description" rows="7" class="form-control"
                                                          style="resize: none;"
                                                          ng-model="editReqVm.requirementDetails.notes">
                                                </textarea>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

