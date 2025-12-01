<div style="position: relative;">
    <div style="overflow-y: hidden; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin: 0;">
            <div style="padding-bottom: 100px;">
                <h4 class="section-title" style="margin-top: 0;" translate>BASIC_INFO</h4>

                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>PLANT</span>
                            <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newAssemblyLineVm.newAssemblyLine.plant" theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match placeholder="{{newAssemblyLineVm.selectPlantTitle}}">
                                    {{$select.selected.number}} - {{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="plant.id as plant in newAssemblyLineVm.plants | filter: $select.search">
                                    <div>{{plant.number}} - {{plant.name}}</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>ASSEMBLYLINE_TYPE_TITLE</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn" uib-dropdown>
                                    <button uib-dropdown-toggle class="btn btn-default dropdown-toggle"
                                            type="button">
                                        <span id="select" translate>SELECT</span> <span class="caret"
                                                                                        style="margin-left: 4px;"></span>
                                    </button>
                                    <div class="dropdown-menu" role="menu">
                                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                            <manufacturing-type-tree
                                                    on-select-type="newAssemblyLineVm.onSelectType"
                                                    object-type="ASSEMBLYLINETYPE"></manufacturing-type-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newAssemblyLineVm.type.name" readonly>

                            </div>
                        </div>
                    </div>

                    <div class="form-group">

                        <label class="col-sm-4 control-label">
                            <span translate>NUMBER</span>
                            <span class="asterisk">*</span> :
                        </label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn">
                                    <button class="btn btn-default" type="button" style="width: 85px"
                                            ng-click="newAssemblyLineVm.autoNumber()" translate>AUTO
                                    </button>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newAssemblyLineVm.newAssemblyLine.number">
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>NAME</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" placeholder="{{'ENTER_NAME' | translate}}"
                                   name="title" ng-model="newAssemblyLineVm.newAssemblyLine.name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      placeholder="{{'ENTER_DESCRIPTION_TITLE' | translate}}"
                                      ng-model="newAssemblyLineVm.newAssemblyLine.description"></textarea>
                        </div>
                    </div>
                    <attributes-view show-objects="selectObjectValues"
                                     ng-if="newAssemblyLineVm.attributes.length > 0"
                                     attributes="newAssemblyLineVm.attributes"></attributes-view>
                </form>
                <br>
            </div>
        </div>
    </div>
</div>
