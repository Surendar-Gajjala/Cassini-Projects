<div>
    <style scoped>
        .basic_content{
            position: absolute;
            top: 20px;
            bottom: 0px;
            left: 20px;
            right: 20px;
        }
    </style>
<div class="basic_content">
    <div ng-if="reqTypeBasicVm.objectType != null" style="overflow-y: hidden;height: 100%;">

        <div style="width: 100px;margin: 10px 0 0 0 !important;">
            <button class="btn btn-sm btn-success min-width" ng-click="reqTypeBasicVm.onSave()"
                    ng-if="hasPermission('requirementtype','edit')" translate>SAVE
            </button>
        </div>
        <div class="">
            <div class="row">
                <div class="col-xs-12 col-sm-8 col-sm-offset-1 col-md-6 col-md-offset-2">

                    <form class="form-horizontal">
                        <div class="form-group">
                            <label class="col-sm-4 control-label">
                                <span translate>NAME</span>
                                <span class="asterisk">*</span> : </label>

                            <div class="col-sm-7">
                                <input type="text" class="form-control" name="title"
                                       ng-disabled="!reqTypeBasicVm.objectType.editable"
                                       ng-model="reqTypeBasicVm.objectType.name">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">
                                <span translate>DESCRIPTION</span> : </label>

                            <div class="col-sm-7">
                            <textarea name="description" rows="3" class="form-control" style="resize: none"
                                      ng-model="reqTypeBasicVm.objectType.description"
                                      ng-disabled="!reqTypeBasicVm.objectType.editable || !hasPermission('requirementtype','edit')"></textarea>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">
                                <span translate>AUTO_NUMBER_SOURCE</span>
                                <span class="asterisk">*</span> : </label>

                            <div class="col-sm-7">
                                <ui-select ng-model="reqTypeBasicVm.objectType.numberSource" theme="bootstrap"
                                           style="width:100%"
                                           title="{{reqTypeBasicVm.objectType.numberSource.name}}"
                                           ng-disabled="(reqTypeBasicVm.objectTypeItemExist == true || !hasPermission('requirementtype','edit')) && !hasPermission('admin','all')">
                                    <ui-select-match placeholder="Select">{{$select.selected.name}}
                                    </ui-select-match>
                                    <ui-select-choices
                                            repeat="source in reqTypeBasicVm.autoNumbers | filter: $select.search">
                                        <div title="{{source.name}}">{{source.name}}
                                        </div>
                                    </ui-select-choices>
                                </ui-select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">
                                <span translate>REVISION_SEQUENCE</span>
                                <span class="asterisk">*</span> : </label>

                            <div class="col-sm-7">
                                <ui-select ng-model="reqTypeBasicVm.objectType.revisionSequence" theme="bootstrap"
                                           style="width:100%"
                                           title="{{reqTypeBasicVm.objectType.revisionSequence.name}}"
                                           ng-disabled="reqTypeBasicVm.objectTypeItemExist == true && !hasPermission('admin','all')">
                                    <ui-select-match placeholder="Select">{{$select.selected.name}}
                                    </ui-select-match>
                                    <ui-select-choices
                                            repeat="lov in reqTypeBasicVm.revSequences | filter: $select.search">
                                        <div title="{{lov.name}}">{{lov.name}}
                                        </div>
                                    </ui-select-choices>
                                </ui-select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label">
                                <span translate>LIFE_CYCLE</span>
                                <span class="asterisk">*</span> : </label>

                            <div class="col-sm-7">
                                <ui-select ng-model="reqTypeBasicVm.objectType.lifecycle" theme="bootstrap"
                                           style="width:100%"
                                           title="{{reqTypeBasicVm.objectType.lifecycle.name}}"
                                           ng-disabled="reqTypeBasicVm.objectTypeItemExist == true && !hasPermission('admin','all')">
                                    <ui-select-match placeholder="Select">{{$select.selected.name}}
                                    </ui-select-match>
                                    <ui-select-choices
                                            repeat="lifecycle in reqTypeBasicVm.lifecycles | filter: $select.search">
                                        <div title="{{lifecycle.name}}">{{lifecycle.name}}
                                        </div>
                                    </ui-select-choices>
                                </ui-select>
                            </div>
                        </div>
                    </form>

                </div>
            </div>
        </div>
    </div>
</div>
    </div>