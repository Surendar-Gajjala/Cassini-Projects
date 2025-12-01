<div style="position: relative;height: 100%">
    <div style="overflow-y: auto;padding: 20px;height: 100%;">
        <div class="row" style="margin: 0;">
            <div style="padding-bottom: 100px;">
                <%--<h4 class="section-title" style="margin-top: 0;" translate>BASIC_INFO</h4>--%>

                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>MBOM</span><span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newProductionOrderItemVm.selectedMbom"
                                       on-select="newProductionOrderItemVm.onSelectMbom($item)"
                                       theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder="Select MBOM">
                                    {{$select.selected.number}} - {{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="mbom.id as mbom in newProductionOrderItemVm.mboms | filter: $select.search">
                                    <div>{{mbom.number}} - {{mbom.name}}</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>MBOM_REVISION</span><span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newProductionOrderItemVm.productionOrderItem.mbomRevision"
                                       on-select="newProductionOrderItemVm.onSelectMbomRevision($item)"
                                       theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder="Select Revision">
                                    Revision : {{$select.selected.revision}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="mbomRevision.id as mbomRevision in newProductionOrderItemVm.selectedMbom.mbomRevisions | filter: $select.search">
                                    <div>Revision : {{mbomRevision.revision}}</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>BOP</span><span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newProductionOrderItemVm.selectedBop"
                                       on-select="newProductionOrderItemVm.onSelectBop($item)"
                                       theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder="Select BOP">
                                    {{$select.selected.number}} - {{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="bop.id as bop in newProductionOrderItemVm.bops | filter: $select.search">
                                    <div>{{bop.number}} - {{bop.name}}</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>BOP_REVISION</span><span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newProductionOrderItemVm.productionOrderItem.bopRevision"
                                       theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder="Select Revision">
                                    Revision : {{$select.selected.revision}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="bopRevision.id as bopRevision in newProductionOrderItemVm.selectedBop.bopRevisions | filter: $select.search">
                                    <div>Revision : {{bopRevision.revision}}</div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group">

                        <label class="col-sm-4 control-label">
                            <span translate>QUANTITY_PRODUCED</span>
                            <span class="asterisk">*</span> :
                        </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title" numbers-only
                                   placeholder="Enter quantity produce"
                                   ng-model="newProductionOrderItemVm.productionOrderItem.quantityProduced">
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
