<div style="position: relative;">
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin-right: 48px;">
            <div>
                <br>

                <form class="form-horizontal">

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>NAME</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7" ng-if="newEntryVm.mode == 'NEW'">
                            <input type="text" class="form-control" name="title"
                                   ng-model="newEntryVm.glossaryEntry.name">
                        </div>
                        <div class="col-sm-7" ng-if="newEntryVm.mode == 'EDIT'">
                            <input type="text" class="form-control" name="title"
                                   ng-model="newEntryVm.glossaryEntry.name" readonly>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <textarea name="description" rows="3" class="form-control" style="resize: none"
                                      ng-model="newEntryVm.glossaryEntry.description">
                            </textarea>
                        </div>
                    </div>

                    <div class="form-group" ng-if='newEntryVm.mode == "EDIT"'>
                        <label class="col-sm-4 control-label">
                            <span translate>NOTES</span>: </label>

                        <div class="col-sm-7">
                            <textarea name="description" rows="3" class="form-control" style="resize: none;"
                                      ng-model="newEntryVm.glossaryEntry.notes">
                            </textarea>
                        </div>
                    </div>
                    <div ng-if='newEntryVm.mode == "NEW"'>
                        <attributes-view show-objects="selectObjectValues"
                                         attributes="newEntryVm.entryRequiredAttributes"></attributes-view>
                        <br>
                        <attributes-view show-objects="selectObjectValues"
                                         attributes="newEntryVm.entryAttributes"></attributes-view>
                    </div>
                </form>
                <br>
            </div>
        </div>
    </div>
</div>

