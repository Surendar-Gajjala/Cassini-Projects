<div class="modal-header">
    <h3>Select WBS</h3>
</div>
<div class="modal-body" style="padding: 20px; max-height: 500px;">

    <div ng-repeat="wbs in wbsSelectVm.wbsData">
        <button title="Expand all" ng-disabled="wbs.children.length ==0"
                ng-if="wbs.showChildren == false" class="btn btn-sm"
                ng-click="wbsSelectVm.showChild(wbs)"><i
                class="fa fa-chevron-right"></i></button>
        <button title="Collapse all" ng-disabled="wbs.children.length ==0"
                ng-if="wbs.children.length !=0 && wbs.showChildren == true" class="btn btn-sm"
                ng-click="wbsSelectVm.hideChild(wbs)">
            <i class="fa fa-chevron-left"></i></button>
        <input type="radio" ng-click="wbsSelectVm.selectRadio(wbs)" name="wbs"/>
        <span style="font-size: 20px">{{wbs.name}}</span>

        <div style="margin-left: 20px" ng-if="wbs.showChildren == true" ng-repeat="wbsChild in wbs.children">
            <button title="Expand all" ng-disabled="wbsChild.children.length ==0"
                    ng-if="wbsChild.showChildren == false" class="btn btn-sm"
                    ng-click="wbsSelectVm.showChild(wbsChild)"><i
                    class="fa fa-chevron-right"></i></button>
            <button title="Collapse all" ng-disabled="wbsChild.children.length == 0"
                    ng-if="wbsChild.showChildren == true" class="btn btn-sm"
                    ng-click="wbsSelectVm.hideChild(wbsChild)">
                <i class="fa fa-chevron-left"></i></button>
            <input type="radio" ng-click="wbsSelectVm.selectRadio(wbsChild)" name="wbs"/>
            <span style="font-size: 17px">{{wbsChild.name}}</span>

            <div style="margin-left: 40px" ng-if="wbsChild.showChildren == true"
                 ng-repeat="wbsGrandChild in wbsChild.children">
                <%--<button title="Expand all" ng-disabled="wbsGrandChild.children.length ==0"
                        ng-if="wbsGrandChild.showChildren == false" class="btn btn-sm"
                        ng-click="wbsSelectVm.showChild(wbsGrandChild)"><i
                        class="fa fa-chevron-right"></i></button>
                <button title="Collapse all" ng-disabled="wbsGrandChild.children.length == 0"
                        ng-if="wbsGrandChild.showChildren == true" class="btn btn-sm"
                        ng-click="wbsSelectVm.hideChild(wbsGrandChild)">
                    <i class="fa fa-chevron-left"></i></button>--%>
                <input type="radio" ng-click="wbsSelectVm.selectRadio(wbsGrandChild)" name="wbs"/>
                <span style="font-size: 14px">{{wbsGrandChild.name}}</span>
            </div>
        </div>

    </div>


</div>
<div class="modal-footer">
    <div class="row">
        <div class="col-md-6">
        </div>
        <div class="modal-buttons" class="col-md-6">
            <button type="button" class="btn btn-sm btn-default"
                    ng-click="wbsSelectVm.cancel()">Cancel
            </button>
            <button type="button" class="btn btn-sm btn-success"
                    ng-click="wbsSelectVm.select()">Select
            </button>
        </div>
    </div>
</div>