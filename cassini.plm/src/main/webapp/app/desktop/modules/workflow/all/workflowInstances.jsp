<div class="responsive-table" style="padding: 10px;height: 100%; overflow: auto">
    <table class="table table-striped highlight-row">
        <thead>
        <tr style="font-size: 14px;">
            <th style="width: 100px" translate>NUMBER</th>
            <th style="width: 100px" translate>NAME</th>
            <th style="width: 100px;text-align: center" translate>REVISION</th>
            <th style="width: 100px" translate>Object Number</th>
            <th style="width: 100px" translate>TYPE</th>
            <th style="width: 100px" translate>CURRENT_ACTIVITY</th>
            <th style="width: 100px" translate>TIME_STAMP</th>
            <th style="width: 50px" translate>HISTORY
            </th>
        </tr>
        </thead>

        <tbody>
        <tr ng-if="wfInstanceVm.loading == true">
            <td colspan="12">
                           <span style="font-size: 15px;">
                               <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                    class="mr5"><span translate>LOADING_ITEMS</span>
                           </span>
            </td>
        </tr>

        <tr ng-if="wfInstanceVm.loading == false && wfInstanceVm.workflows.length == 0">
            <td colspan="12" style="background-color: #f9fbfe  !important;color: unset !important;">
                <div class="no-data">
                    <img src="app/assets/no_data_images/Workflow.png" alt="" class="image">

                    <div class="message">{{ 'NO_WORKFLOW_INSTANCES' | translate}} </div>
                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                </div>
            </td>
        </tr>
        <tr ng-repeat="workflow in wfInstanceVm.workflows | orderBy: '-timeStamp'" style="font-size: 14px;">
            <td style="width: 100px" title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                <a href="" ng-click="wfInstanceVm.gotoWorkflow(workflow.workflow)">
                    <span ng-bind-html="workflow.workflow.workflowRevisionObject.master.number"></span></a>
            </td>

            <td style="width: 100px">
                {{workflow.workflow.name}}
            </td>
            <td style="width: 100px;text-align: center">
                {{workflow.workflow.workflowRevisionObject.revision}}
            </td>
            <td style="width: 100px;">
                <span>
                    <a href="" title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                       ng-click="wfInstanceVm.showInstanceDetails(workflow)">{{workflow.number}}</a>
                </span>
            </td>
            <td style="width: 100px">{{workflow.type}}</td>
            <td style="width: 100px">{{workflow.currentStatus}}</td>
            <td style="width: 100px">{{workflow.timeStamp}}</td>
            <td style="width: 50px; text-align: center" ng-click="wfInstanceVm.showWorkflowHistory(workflow)"
                title="{{wfHistory}}">
                <i style="color: #337ab7;font-size: 18px;font-weight: 900" class="las la-sync"></i>
            </td>
        </tr>
        </tbody>
    </table>
</div>
