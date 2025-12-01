<div class="col-md-12" style="height: 100%;">
    <div ng-if="selectedClassificationType !=null || (mfrPartTypeBasicVm.mfrPartType != null && !newItemTypeCreating)">
        <div style="width: 100px;margin: 10px 0 0 0 !important;">
            <button class="btn btn-sm btn-success min-width" ng-click="mfrPartTypeBasicVm.onSave()"
                    ng-if="hasPermission('manufacturerparttype','edit')" translate>SAVE
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
                                       ng-model="mfrPartTypeBasicVm.mfrPartType.name"
                                       ng-disabled="mfrPartTypeBasicVm.mfrPartTypePartsExist == true || !hasPermission('manufacturerparttype','edit')">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label"><span translate>DESCRIPTION</span> : </label>

                            <div class="col-sm-7">
                        <textarea name="description" rows="3" class="form-control" style="resize: none"
                                  ng-disabled="!hasPermission('manufacturerparttype','edit')"
                                  ng-model="mfrPartTypeBasicVm.mfrPartType.description"></textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-4 control-label">
                                <span translate>LIFE_CYCLE</span>
                                <span class="asterisk">*</span> : </label>

                            <div class="col-sm-7">
                                <ui-select <%--ng-disabled="mfrPartTypeBasicVm.mfrPartType.lifecycle != null"--%>
                                           ng-model="mfrPartTypeBasicVm.mfrPartType.lifecycle" theme="bootstrap"
                                           style="width:100%"
                                           title="{{mfrPartTypeBasicVm.mfrPartType.lifecycle.name}}"
                                           on-select="mfrPartTypeBasicVm.onSelectLifecycle($item)">
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