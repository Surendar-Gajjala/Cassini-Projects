<div class="responsive-table">
    <table class="table table-striped highlight-row">
        <thead>
        <tr style="font-size: 14px;">
            <th style="width: 100px" translate>NUMBER</th>
            <th style="width: 100px" translate>NAME</th>
            <th style="width: 100px" translate>TYPE</th>
            <th style="width: 40px" translate>LIFE_CYCLE</th>
            <th style="width: 100px">Workflow Name</th>
            <th style="width: 100px" translate>CURRENT_ACTIVITY</th>
            <th style="width: 100px" translate>TIME_STAMP</th>
            <th style="width: 50px" translate>HISTORY
            </th>
        </tr>
        </thead>

        <tbody>
        <tr ng-if="workflowWigVm.loading == true">
            <td colspan="7">
                           <span style="font-size: 15px;">
                               <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                    class="mr5"><span translate>LOADING_ITEMS</span>
                           </span>
            </td>
        </tr>

        <tr ng-if="workflowWigVm.loading == false && workflowWigVm.workflows.content.length == 0">
            <td colspan="15" translate>MO_WORKFLOW</td>
        </tr>
        <tr ng-repeat="workflow in workflowWigVm.workflows.content" style="font-size: 14px;">
            <td style="width: 100px;">
                <a href="" ng-click="workflowWigVm.showMfrPartDetails(workflow.manufacturerPart)">{{workflow.manufacturerPart.partNumber}}</a>
            </td>
            <td style="width: 100px">{{workflow.manufacturerPart.partName}}</td>
            <td style="width: 100px">{{workflow.manufacturerPart.mfrPartType.name}}</td>
            <td style="width:40px">
                <item-status item="workflow.manufacturerPart"></item-status>
            </td>
            <td style="width: 100px">{{workflow.name}}</td>
            <td style="width: 100px">{{workflow.status}}</td>
            <td style="width: 100px">{{workflow.timeStamp}}</td>
            <td style="width: 50px; text-align: center" ng-click="workflowWigVm.showWorkflowHistory(workflow)"
                title="{{wfHistory}}">
                <i style="color: #337ab7;font-size: 18px;font-weight: 900" class="las la-sync"></i>
            </td>
        </tr>
        </tbody>
    </table>
</div>
