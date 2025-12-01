<style>

    .added-column {
        text-align: left;
        width: 150px;
    }

    .added-column i {
        display: none;
        cursor: pointer;
        margin-left: 5px;
    }

    .added-column:hover i {
        display: inline-block;
    }

    .activityTask-progress.progress {
        background-color: #B0C7CF;
        height: 20px !important;
        margin: 0 !important;
    }

    i.fa-times-circle {
        margin-left: -1px !important;
        color: gray !important;
        cursor: pointer !important;
    }

    .attributeTooltip .attributeTooltiptext {
        visibility: hidden;
        width: 200px;
        background-color: #7BB7EB;
        color: #141f9f;
        text-align: left;
        border-radius: 6px;
        padding: 5px 0;
        position: absolute;
        z-index: 1;
        top: -5px;
        bottom: auto;
        right: 100%;
        opacity: 0;
        transition: opacity 1s;
    }

    .attributeTooltip .attributeTooltiptext::after {
        content: "";
        position: absolute;
        top: 25%;
        left: 102%;
        margin-left: -5px;
        border-width: 5px;
        border-style: solid;
        border-color: transparent transparent transparent #7BB7EB;
    }

    .attributeTooltip:hover .attributeTooltiptext {
        visibility: visible;
        opacity: 1;
    }

    /* The Close Button */
    .img-model .closeImage,
    .img-model .closeImage1 {
        position: absolute;
        top: 50px;
        right: 50px;

        font-size: 40px;
        font-weight: bold;
        transition: 0.3s;
    }

    .img-model .closeImage:hover,
    .img-model .closeImage:focus,
    .img-model .closeImage1:hover,
    .img-model .closeImage1:focus {
        color: #bbb;
        text-decoration: none;
        cursor: pointer;
    }

    .img-model .closeImage1 {
        position: absolute;
        top: 50px;
        right: 50px;

        font-size: 40px;
        font-weight: bold;
        transition: 0.3s;
    }

    .img-model .closeImage1:hover,
    .img-model .closeImage1:focus {
        color: #bbb;
        text-decoration: none;
        cursor: pointer;
    }

    .attributeTooltip {
        position: relative;
        display: inline-block;
    }
</style>
<div class="responsive-table" style="padding: 10px;">
    <table class="table table-striped highlight-row">
        <thead>
        <tr>
            <th style="width: 10px"><i class="la la-plus"
                                   style="cursor: pointer !important;"
                                   ng-if="hasPermission('project','edit')"
                                   title="{{templateActivityTasksVm.addTask}}"
                                   ng-click="addActivityTasks()"></i></th>
            <th class="col-width-250" translate>NAME</th>
            <th class="description-column" translate>DESCRIPTION</th>
            <th class="col-width-250" translate>ACTIONS</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="templateActivityTasksVm.loading == true">
            <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_TASKS</span>
                        </span>
            </td>
        </tr>

        <tr ng-if="templateActivityTasksVm.activityTasks.length == 0">
            <td colspan="25" translate>NO_TASKS</td>
        </tr>

        <tr ng-repeat="task in templateActivityTasksVm.activityTasks">
            <td></td>
            <td class="col-width-250">
                <span ng-if="task.editMode == false">{{task.name}}</span>
                <span ng-if="task.editMode == true"><input type="text" class="form-control" style="width: 150px;"
                                                           ng-model="task.name"/></span>
            </td>
            <td class="description-column">
                <span ng-if="task.editMode == false">{{task.description}}</span>
                 <span ng-if="task.editMode == true">
                    <input type="text" class="form-control" style="width: 150px;" ng-model="task.description"/>
                </span>
            </td>
            <td class="col-width-250" class="text-center">
                <span class="row-menu" uib-dropdown dropdown-append-to-body>
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                      <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                          style="z-index: 9999 !important;">

                          <li ng-if="task.editMode == false" ng-click="templateActivityTasksVm.editTask(task)">
                              <a translate>EDIT_TASK</a>
                          </li>
                          <li ng-if="task.editMode == true"
                              ng-click="templateActivityTasksVm.updateTask(task)">
                              <a translate>SAVE_TASK</a>
                          </li>
                          <li ng-if="task.editMode == true"
                              ng-click="templateActivityTasksVm.cancelChanges(task)">
                              <a translate>CANCEL</a>
                          </li>
                          <li ng-if="task.editMode == false" ng-click="templateActivityTasksVm.deleteTask(task)">
                              <a translate>TASK_DIALOG_TITLE</a>
                          </li>
                      </ul>
                </span>
            </td>

        </tr>
        </tbody>
    </table>
</div>