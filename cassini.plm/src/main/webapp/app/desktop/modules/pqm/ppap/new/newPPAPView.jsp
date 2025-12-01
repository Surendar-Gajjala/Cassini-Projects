<div style="position: relative;">
    <div style="overflow-y: hidden; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin: 0;">
            <div style="padding-bottom: 100px;">
                <h4 class="section-title" style="margin-top: 0;" translate>BASIC_INFO</h4>

                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>PPAP_TYPE</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn" uib-dropdown>
                                    <button uib-dropdown-toggle class="btn btn-default dropdown-toggle"
                                            type="button">
                                        <span id="Select" translate>SELECT</span> <span class="caret"
                                                                                        style="margin-left: 4px;"></span>
                                    </button>
                                    <div class="dropdown-menu" role="menu">
                                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                            <quality-type-tree on-select-type="newPpapVm.onSelectType"
                                                               quality-type="PPAPTYPE"></quality-type-tree>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newPpapVm.newPpap.type.name" readonly>

                            </div>
                        </div>

                    </div>

                    <div class="form-group">

                        <label class="col-sm-4 control-label">
                            <span translate>NUMBER</span>
                            <span class="asterisk">*</span> :
                        </label>

                        <!-- <div class="col-sm-7">
                            <div class="input-group mb15">
                                <div class="input-group-btn">
                                    <button class="btn btn-default" type="button" style="width: 85px"
                                            ng-click="newPpapVm.autoNumber()" translate>AUTO
                                    </button>
                                </div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newPpapVm.newPpap.number">
                            </div>
                        </div> -->


                        <div class="col-sm-7">
                            <input type="text" class="form-control" placeholder="Enter number" name="title"
                                ng-model="newPpapVm.newPpap.number">
                        </div>

                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>SELECT_SUPPLIER</span>
                            <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newPpapVm.newPpap.supplier" theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder="Select Supplier" >
                                    {{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices repeat="supplier.id as supplier in newPpapVm.suppliers | filter: $select.search" >
                                    <div ng-click="newPpapVm.loadParts(supplier.id)" ng-bind="supplier.name"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>

                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>SELECT_PART</span>
                            <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newPpapVm.newPpap.supplierPart" theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder="select Part">
                                    {{$select.selected.manufacturerPart.partName}}
                                </ui-select-match>
                                <ui-select-choices repeat="part.id as part in newPpapVm.parts | filter: $select.search">
                                    <div ng-bind="part.manufacturerPart.partName"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>

                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label"><span translate>PPAP Name</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" placeholder="{{'ENTER_NAME' | translate}}"
                                name="title" ng-model="newPpapVm.newPpap.name">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                placeholder="{{'ENTER_DESCRIPTION_TITLE' | translate}}"
                                ng-model="newPpapVm.newPpap.description"></textarea>
                        </div>
                    </div>
                    <attributes-view show-objects="selectObjectValues"
                                     ng-if="newPpapVm.qcrRequiredProperties.length > 0"
                                     attributes="newPpapVm.qcrRequiredProperties"></attributes-view>
                    <attributes-view show-objects="selectObjectValues"
                                     ng-if="newPpapVm.qcrProperties.length > 0"
                                     attributes="newPpapVm.qcrProperties"></attributes-view>
                    <attributes-view show-objects="selectObjectValues" ng-if="newPpapVm.attributes.length > 0"
                                     attributes="newPpapVm.attributes"></attributes-view>
                </form>
            </div>     
        </div>
    </div>
</div>