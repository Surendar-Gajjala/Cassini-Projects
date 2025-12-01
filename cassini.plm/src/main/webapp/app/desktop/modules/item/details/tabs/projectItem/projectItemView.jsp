<div class='responsive-table'>
    <table class='table table-striped highlight-row'>
        <thead>
        <tr>
            <th translate>PROJECT</th>
            <th translate>ACTIVITY</th>
            <th translate>TASK</th>

        </tr>
        </thead>
        <tbody>
        <tr ng-if="projectItemsVm.loading == true">
            <td colspan="14"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                  class="mr5"><span translate>LOADING_PROJECTS</span>
            </td>
        </tr>
        <tr ng-if="projectItemsVm.loading == false && projectItemsVm.itemDeliverables.length == 0">
            <td colspan="11" style="background-color: #f9fbfe  !important;color: unset !important;">
                <div class="no-data">
                    <img src="app/assets/no_data_images/Project.png" alt="" class="image">

                    <div class="message">{{ 'NO_PROJECTS' | translate}}</div>
                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                </div>
            </td>
        </tr>
        <tr ng-repeat="item in projectItemsVm.itemDeliverables">
            <td>
                <span>{{item.project.name}}</span>
            </td>
            <td>
                <span ng-if="item.activity != null">{{item.activity.name}}</span>
                <span ng-if="item.activity == null" style="margin-left: 14px;"> -- </span>
            </td>
            <td>
                <span ng-if="item.task != null">{{item.task.name}}</span>
                <span ng-if="item.task == null" style="margin-left: 14px;"> -- </span>
            </td>
        </tr>
        </tbody>
    </table>
</div>