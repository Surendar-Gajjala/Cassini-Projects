<div class="col-md-12" style="height: 100%;">
    <div>
        <div style="width: 100px;margin: 10px 0 0 0 !important;">
            <button class="btn btn-sm btn-success min-width" ng-click="qualityTypeBasicVm.save()"
                    ng-if="hasPermission('quality_type','edit')" translate>SAVE
            </button>
        </div>
        <div class="">
            <div class="row">
                <div class="col-xs-12 col-sm-8 col-sm-offset-1 col-md-6 col-md-offset-2">

                    <form class="form-horizontal">
                        <div class="form-group">
                            <label class="col-sm-5 control-label"><span translate>NAME</span>
                                <span class="asterisk">*</span>: </label>

                            <div class="col-sm-7">
                                <input type="text" class="form-control" name="title"
                                       ng-model="qualityType.name"
                                       ng-disabled="qualityTypeObjectsExist || !hasPermission('quality_type','edit')">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-5 control-label"><span translate>DESCRIPTION</span> : </label>

                            <div class="col-sm-7">
                                <textarea name="description" rows="3" class="form-control" style="resize: none"
                                          ng-model="qualityType.description"
                                          ng-disabled="!hasPermission('quality_type','edit')"></textarea>
                            </div>
                        </div>

                        <div class="form-group"
                             ng-if="qualityType != null && qualityType.qualityType == 'PRODUCTINSPECTIONPLANTYPE'">
                            <label class="col-sm-5 control-label">
                                <span translate>PRODUCT_TYPE</span>
                                <span class="asterisk">*</span> : </label>

                            <div class="col-sm-7">
                                <div class="input-group mb15">
                                    <div class="input-group-btn" uib-dropdown>
                                        <button uib-dropdown-toggle class="btn btn-default dropdown-toggle"
                                                type="button" ng-disabled="qualityTypeObjectsExist">
                                            <span translate>SELECT</span> <span class="caret"
                                                                                style="margin-left: 4px;"></span>
                                        </button>
                                        <div class="dropdown-menu" role="menu">
                                            <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                                <item-class-tree item-class="PRODUCT"
                                                                 on-select-type="qualityTypeBasicVm.onSelectType"></item-class-tree>
                                            </div>
                                        </div>
                                    </div>
                                    <input type="text" class="form-control" name="title"
                                           ng-model="qualityType.productType.name" readonly>


                                </div>
                            </div>
                        </div>
                        <div class="form-group"
                             ng-if="qualityType != null && qualityType.qualityType == 'MATERIALINSPECTIONPLANTYPE'">
                            <label class="col-sm-5 control-label">
                                <span translate>MANUFACTURER_PART_TYPE</span>
                                <span class="asterisk">*</span> : </label>

                            <div class="col-sm-7">
                                <div class="input-group mb15">
                                    <div class="input-group-btn" uib-dropdown>
                                        <button uib-dropdown-toggle class="btn btn-default dropdown-toggle"
                                                type="button" ng-disabled="qualityTypeObjectsExist">
                                            <span translate>SELECT</span> <span class="caret"
                                                                                style="margin-left: 4px;"></span>
                                        </button>
                                        <div class="dropdown-menu" role="menu">
                                            <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                                <manufacturer-part-tree
                                                        on-select-type="qualityTypeBasicVm.onSelectPartType"></manufacturer-part-tree>
                                            </div>
                                        </div>
                                    </div>
                                    <input type="text" class="form-control" name="title"
                                           ng-model="qualityType.partType.name" readonly>


                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-5 control-label"><span translate>AUTO_NUMBER_SOURCE</span>
                                <span class="asterisk">*</span>: </label>

                            <div class="col-sm-7">
                                <ui-select ng-model="qualityType.numberSource" theme="bootstrap"
                                           style="width:100%"
                                           ng-disabled="qualityTypeObjectsExist || !hasPermission('quality_type','edit')">
                                    <ui-select-match placeholder="Select">{{$select.selected.name}}</ui-select-match>
                                    <ui-select-choices
                                            repeat="source in autoNumbers | filter: $select.search">
                                        <div ng-bind="source.name"></div>
                                    </ui-select-choices>
                                </ui-select>
                            </div>
                        </div>
                        <div class="form-group"
                             ng-if="qualityType != null && (qualityType.qualityType == 'PRODUCTINSPECTIONPLANTYPE' || qualityType.qualityType == 'MATERIALINSPECTIONPLANTYPE')">
                            <label class="col-sm-5 control-label">
                                <span translate>INSPECTION_NUMBER_SOURCE</span>
                                <span class="asterisk">*</span> :
                            </label>

                            <div class="col-sm-7">
                                <ui-select ng-model="qualityType.inspectionNumberSource" theme="bootstrap"
                                           style="width:100%"
                                           ng-disabled="qualityTypeObjectsExist || !hasPermission('quality_type','edit')">
                                    <ui-select-match placeholder="Select">{{$select.selected.name}}</ui-select-match>
                                    <ui-select-choices
                                            repeat="source in autoNumbers | filter: $select.search">
                                        <div ng-bind="source.name"></div>
                                    </ui-select-choices>
                                </ui-select>
                            </div>
                        </div>
                        <div class="form-group"
                             ng-if="qualityType != null && (qualityType.qualityType == 'PRODUCTINSPECTIONPLANTYPE' || qualityType.qualityType == 'MATERIALINSPECTIONPLANTYPE')">
                            <label class="col-sm-5 control-label">
                                <span translate>REVISION_SEQUENCE</span>
                                <span class="asterisk">*</span> : </label>

                            <div class="col-sm-7">
                                <ui-select ng-model="qualityType.revisionSequence" theme="bootstrap"
                                           style="width:100%" title="{{qualityType.revisionSequence.name}}"
                                           ng-disabled="qualityTypeObjectsExist || !hasPermission('quality_type','edit')">
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
                             ng-if="qualityType != null && (qualityType.qualityType == 'PRTYPE' || qualityType.qualityType == 'NCRTYPE')">
                            <label class="col-sm-5 control-label">
                                <span translate>DEFECT_TYPES</span>
                                <span class="asterisk">*</span> : </label>

                            <div class="col-sm-7">
                                <ui-select ng-model="qualityType.failureTypes" theme="bootstrap"
                                           style="width:100%" title="{{qualityType.revisionSequence.name}}"
                                           ng-disabled="qualityTypeObjectsExist || !hasPermission('quality_type','edit')">
                                    <ui-select-match placeholder="Select">
                                        {{$select.selected.name}}
                                    </ui-select-match>
                                    <ui-select-choices
                                            repeat="lov in listOfValues | filter: $select.search">
                                        <div title="{{lov.name}}">
                                            {{lov.name}}
                                        </div>
                                    </ui-select-choices>
                                </ui-select>
                            </div>
                        </div>
                        <div class="form-group"
                             ng-if="qualityType != null && (qualityType.qualityType == 'PRTYPE' || qualityType.qualityType == 'NCRTYPE')">
                            <label class="col-sm-5 control-label">
                                <span translate>SEVERITIES</span>
                                <span class="asterisk">*</span> : </label>

                            <div class="col-sm-7">
                                <ui-select ng-model="qualityType.severities" theme="bootstrap"
                                           style="width:100%" title="{{qualityType.revisionSequence.name}}"
                                           ng-disabled="qualityTypeObjectsExist || !hasPermission('quality_type','edit')">
                                    <ui-select-match placeholder="Select">
                                        {{$select.selected.name}}
                                    </ui-select-match>
                                    <ui-select-choices
                                            repeat="lov in listOfValues | filter: $select.search">
                                        <div title="{{lov.name}}">
                                            {{lov.name}}
                                        </div>
                                    </ui-select-choices>
                                </ui-select>
                            </div>
                        </div>
                        <div class="form-group"
                             ng-if="qualityType != null && (qualityType.qualityType == 'PRTYPE' || qualityType.qualityType == 'NCRTYPE')">
                            <label class="col-sm-5 control-label">
                                <span translate>DISPOSITIONS</span>
                                <span class="asterisk">*</span> : </label>

                            <div class="col-sm-7">
                                <ui-select ng-model="qualityType.dispositions" theme="bootstrap"
                                           style="width:100%" title="{{qualityType.revisionSequence.name}}"
                                           ng-disabled="qualityTypeObjectsExist || !hasPermission('quality_type','edit')">
                                    <ui-select-match placeholder="Select">
                                        {{$select.selected.name}}
                                    </ui-select-match>
                                    <ui-select-choices
                                            repeat="lov in listOfValues | filter: $select.search">
                                        <div title="{{lov.name}}">
                                            {{lov.name}}
                                        </div>
                                    </ui-select-choices>
                                </ui-select>
                            </div>
                        </div>
                        <div class="form-group"
                             ng-if="qualityType != null && (qualityType.qualityType == 'PRODUCTINSPECTIONPLANTYPE' || qualityType.qualityType == 'MATERIALINSPECTIONPLANTYPE')">
                            <label class="col-sm-5 control-label">
                                <span translate>LIFE_CYCLE</span>
                                <span class="asterisk">*</span> : </label>

                            <div class="col-sm-7">
                                <ui-select ng-model="qualityType.lifecycle" theme="bootstrap"
                                           style="width:100%" title="{{qualityType.lifecycle.name}}"
                                           ng-disabled="qualityTypeObjectsExist || !hasPermission('quality_type','edit')">
                                    <ui-select-match placeholder="Select">
                                        {{$select.selected.name}}
                                    </ui-select-match>
                                    <ui-select-choices
                                            repeat="lov in lifecycles | filter: $select.search">
                                        <div title="{{lov.name}}">
                                            {{lov.name}}
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