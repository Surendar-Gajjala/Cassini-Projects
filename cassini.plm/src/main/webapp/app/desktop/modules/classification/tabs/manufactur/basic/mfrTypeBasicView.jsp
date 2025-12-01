<div class="col-md-12" style="height: 100%;">
    <div ng-if="selectedClassificationType !=null  || (mfrTypeBasicVm.mfrType != null && !newItemTypeCreating)">
        <div style="width: 100px;margin: 10px 0 0 0 !important;">
            <button class="btn btn-sm btn-success min-width" ng-click="mfrTypeBasicVm.onSave()"
                    ng-if="hasPermission('manufacturertype','edit')" translate>SAVE
            </button>
        </div>
        <div class="">

            <div class="row">
                <div class="col-xs-12 col-sm-8 col-sm-offset-1 col-md-6 col-md-offset-2">

                    <form class="form-horizontal">
                        <div class="form-group">
                            <label class="col-sm-4 control-label"><span translate>NAME</span>
                                <span class="asterisk">*</span> : </label>

                            <div class="col-sm-7">
                                <input type="text" class="form-control" name="title"
                                       ng-model="mfrTypeBasicVm.mfrType.name"
                                       ng-disabled="mfrTypeBasicVm.mfrTypeMfrsExist == true || !hasPermission('manufacturertype','edit')">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label"><span translate>DESCRIPTION</span> : </label>

                            <div class="col-sm-7">
                        <textarea name="description" rows="3" class="form-control" style="resize: none"
                                  ng-disabled="!hasPermission('manufacturertype','edit')"
                                  ng-model="mfrTypeBasicVm.mfrType.description"></textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-4 control-label">
                                <span translate>LIFE_CYCLE</span>
                                <span class="asterisk">*</span> : </label>

                            <div class="col-sm-7">
                                <ui-select <%--ng-disabled="mfrTypeBasicVm.mfrType.lifecycle != null"--%>
                                           ng-model="mfrTypeBasicVm.mfrType.lifecycle" theme="bootstrap"
                                           style="width:100%"
                                           title="{{mfrTypeBasicVm.mfrType.lifecycle.name}}"
                                           on-select="mfrTypeBasicVm.onSelectLifecycle($item)">
                                    <ui-select-match placeholder="Select">{{$select.selected.name}}
                                    </ui-select-match>
                                    <ui-select-choices
                                            repeat="lifecycle in lifecycles | filter: $select.search">
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