<div style="position: relative;">
    <style scoped>

    </style>
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px;">
        <div class="row" style="margin: 0;">
            <div>
                <br>

                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>NAME</span>
                            <span class="asterisk">*</span> :
                        </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="name"
                                   placeholder="{{'NAME' | translate}}"
                                   ng-model="newGroupVm.personGroup.name">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span> :
                        </label>

                        <div class="col-sm-7">
                            <textarea type="text" class="form-control" name="description"
                                      placeholder="{{'DESCRIPTION' | translate}}" rows="3" style="resize: none;"
                                      ng-model="newGroupVm.personGroup.description"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>PROFILE_LABEL</span>
                            <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newGroupVm.personGroup.profile" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="Select Profile">
                                    {{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="profile in newGroupVm.allProfiles | filter: $select.search | orderBy:'name'">
                                    <div ng-bind-html="profile.name | highlight: $select.name.search"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>IS_ACTIVE</span> :</label>

                        <div class="col-sm-7">
                            <input type="checkbox" id="isActive" switch="none" checked=""
                                   ng-model="newGroupVm.personGroup.isActive">
                            <label for="isActive" data-on-label="Yes" data-off-label="No"></label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>EXTERNAL</span> :</label>

                        <div class="col-sm-7">
                            <input type="checkbox" id="external" switch="none" checked=""
                                   ng-model="newGroupVm.personGroup.external">
                            <label for="external" data-on-label="Yes" data-off-label="No"></label>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
