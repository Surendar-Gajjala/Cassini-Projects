<div style="position: relative;">
    <div style="overflow-y: hidden; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin: 0;">
            <div style="padding-bottom: 100px;">
                <h4 class="section-title" style="margin-top: 0;" translate>BASIC_INFO</h4>

                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>TYPE</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn" uib-dropdown>
                                    <button uib-dropdown-toggle class="btn btn-default dropdown-toggle"
                                            type="button">
                                        <span translate id="Select">SELECT</span> <span class="caret"
                                                                                        style="margin-left: 4px;"></span>
                                    </button>
                                    <div class="dropdown-menu" role="menu">
                                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                            <project-management-type-tree
                                                    on-select-type="newReqDocTemplateVm.onSelectType"
                                                    object-type="REQUIREMENTDOCUMENTTYPE"></project-management-type-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newReqDocTemplateVm.type.name" readonly>

                            </div>
                        </div>
                    </div>


                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>NAME</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title"
                                   placeholder="{{'ENTER_NAME' | translate}}"
                                   ng-model="newReqDocTemplateVm.newReqDocTemplate.name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      placeholder="{{'ENTER_DESCRIPTION_TITLE' | translate}}"
                                      ng-model="newReqDocTemplateVm.newReqDocTemplate.description"></textarea>
                        </div>
                    </div>

                    <div class="form-group" ng-if="newReqDocTemplateVm.docRevision != null">
                        <label class="col-sm-4 control-label">
                            <span translate>COPY_DOCUMENT_REVIEWER</span> : </label>

                        <div class="col-sm-7" style="top: 10px; cursor: pointer">
                            <input type="checkbox" ng-model="newReqDocTemplateVm.newReqDocTemplate.documentReviewer">
                        </div>
                    </div>

                    <div class="form-group" ng-if="newReqDocTemplateVm.docRevision != null">
                        <label class="col-sm-4 control-label">
                            <span translate>COPY_REQUIREMENT_REVIEWER</span>
                            : </label>

                        <div class="col-sm-7" style="top: 10px; cursor: pointer">
                            <input type="checkbox" ng-model="newReqDocTemplateVm.newReqDocTemplate.requirementReviewer">
                        </div>
                    </div>


                </form>
                <br>

            </div>
        </div>
    </div>
</div>