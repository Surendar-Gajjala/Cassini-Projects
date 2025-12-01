<div class="col-md-12" style="height: 100%;">
    <div>
        <div style="width: 100px;margin: 10px 0 0 0 !important;">
            <button class="btn btn-sm btn-success min-width" ng-click="resourceTypeBasicVm.onSave()"
                    ng-if="checkMESPermission('edit')" translate>SAVE
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
                                       ng-model="resourceType.name"
                                       ng-disabled="mesTypeObjectsExist == true || !checkMESPermission('edit')">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-4 control-label"><span translate>DESCRIPTION</span> : </label>

                            <div class="col-sm-7">
                        <textarea name="description" rows="3" class="form-control" style="resize: none"
                                  ng-disabled="!hasPermission(selectedProductionResType.objectType,'edit')"
                                  ng-model="resourceType.description"></textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-4 control-label">
                                <span translate>AUTO_NUMBER_SOURCE</span>
                                <span class="asterisk">*</span> : </label>

                            <div class="col-sm-7">
                                <ui-select ng-model="resourceType.autoNumberSource" theme="bootstrap"
                                           style="width:100%"
                                           title="{{resourceType.autoNumberSource.name}}"
                                           ng-disabled="mesTypeObjectsExist == true || !checkMESPermission('edit')">
                                    <ui-select-match placeholder="Select">{{$select.selected.name}}
                                    </ui-select-match>
                                    <ui-select-choices
                                            repeat="source in autoNumbers | filter: $select.search">
                                        <div title="{{source.name}}">{{source.name}}
                                        </div>
                                    </ui-select-choices>
                                </ui-select>
                            </div>
                        </div>
                        <div class="form-group"
                             ng-show="selectedProductionResType.objectType == 'PRODUCTIONORDERTYPE' || selectedProductionResType.objectType == 'MBOMTYPE' || selectedProductionResType.objectType == 'BOPTYPE'">
                            <label class="col-sm-4 control-label">
                                <span translate>LIFE_CYCLE</span>
                                <span class="asterisk">*</span> : </label>

                            <div class="col-sm-7">
                                <ui-select ng-model="resourceType.lifecycle" theme="bootstrap"
                                           style="width:100%"
                                           title="{{resourceTypeBasicVm.resourceType.lifecycle.name}}"
                                           ng-disabled="mesTypeObjectsExist == true || !checkMESPermission('edit')"
                                           on-select="resourceTypeBasicVm.onSelectLifecycle($item)">
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
                        <div class="form-group"
                             ng-show="selectedProductionResType.objectType == 'MBOMTYPE' || selectedProductionResType.objectType == 'BOPTYPE'">
                            <label class="col-sm-4 control-label">
                                <span translate>REVISION_SEQUENCE</span>
                                <span class="asterisk">*</span> : </label>

                            <div class="col-sm-7">
                                <ui-select ng-model="resourceType.revisionSequence" theme="bootstrap"
                                           style="width:100%" title="{{resourceType.revisionSequence.name}}"
                                           ng-disabled="mesTypeObjectsExist == true || !checkMESPermission('edit')">
                                    <ui-select-match placeholder="Select">
                                        {{$select.selected.name}}
                                    </ui-select-match>
                                    <ui-select-choices
                                            repeat="lov in revSequences | filter: $select.search">
                                        <div title="{{lov.name}}">
                                            {{lov.name}}
                                        </div>
                                    </ui-select-choices>
                                </ui-select>
                            </div>
                        </div>
                        <div class="form-group"
                             ng-show="selectedProductionResType.objectType == 'SUPPLIERTYPE'">
                            <label class="col-sm-4 control-label">
                                <span translate>LIFE_CYCLE</span>
                                <span class="asterisk">*</span> : </label>

                            <div class="col-sm-7">
                                <ui-select ng-model="resourceType.lifecycle" theme="bootstrap"
                                           style="width:100%"
                                           title="{{resourceTypeBasicVm.resourceType.lifecycle.name}}"
                                           ng-disabled="mesTypeObjectsExist == true || !checkMESPermission('edit') || resourceType.lifecycle !=null"
                                           on-select="resourceTypeBasicVm.onSelectLifecycle($item)">
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