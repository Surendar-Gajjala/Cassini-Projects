<div class="panel panel-default panel-alt widget-messaging" style="height: 350px">
    <div class="panel-heading" style="background-color: #E4E7EA; padding:0; height: 56px">
        <div class="row" style="padding-right: 10px;">
            <div class="panel-title col-sm-8" style="font-size:15px; padding: 20px 0 0 20px">Process Steps</div>
            <div style="float:right;padding-right: 15px;padding-top: 12px;">
                <button class="btn btn-primary mr5 btn-sm" data-ng-click="addNewProcessStep();">
                    <i class="glyphicon glyphicon-plus"></i> Add Process Step
                </button>
            </div>
        </div>
    </div>
    <div class="panel-body" style=" min-height: 250px; max-height: 250px; overflow: auto">

        <table class="table table-striped">
            <thead>
            <tr>
                <th>Actions</th>
                <th>Name</th>
                <th>Description</th>
                <th>Process</th>
                <th>Workcenter</th>
                <th>Sequence Number</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="processStepList.length == 0">
                <td colspan="7">No Records</td>
            </tr>

            <tr data-ng-repeat="processStep in processStepList">
                <td>
                    <div class="btn-group" dropdown ng-hide="processStep.editMode">
                        <button type="button" class="btn btn-sm btn-default dropdown-toggle actionbtn" dropdown-toggle>
                            <i class="fa fa-cog fa-fw"></i></span>
                        </button>
                        <ul class="dropdown-menu" role="menu">
                            <li><a href="" ng-click="showEditMode(processStep)">Edit</a></li>
                            <li><a href="" ng-click="removeItem($index,processStep);">Delete</a></li>
                        </ul>
                    </div>
                    <div class="btn-group" ng-show="processStep.editMode">
                        <button type="button" class="btn btn-sm btn-success" ng-click="acceptChanges(processStep)"><i
                                class="fa fa-check"></i></button>
                        <button type="button" class="btn btn-sm btn-default" ng-click="hideEditMode(processStep);"><i
                                class="fa fa-times"></i></button>
                    </div>
                </td>
                <td>
                    <span ng-hide="processStep.editMode">{{processStep.name}}</span>
                    <input ng-show="processStep.editMode" placeholder="Enter name" class="form-control" type="text"
                           data-ng-model="processStep.name">
                </td>
                <td>
                    <span ng-hide="processStep.editMode">{{processStep.description}}</span>
                    <input ng-show="processStep.editMode" placeholder="Enter description" class="form-control"
                           type="text" data-ng-model="processStep.description">
                </td>
                <td>
                    <span ng-hide="processStep.editMode">{{processStep.process.name}}</span>

                    <div ng-show="processStep.editMode">
                        <ui-select ng-model="processStep.process" theme="bootstrap" style="width: 100%;"
                                   title="Choose a Process">
                            <ui-select-match placeholder="Choose a Process">{{$select.selected.name}}</ui-select-match>
                            <ui-select-choices repeat="process in processList | filter: $select.search">
                                <div ng-bind-html="process.name | highlight: $select.search"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>

                    <%--   <input ng-show="processStep.editMode" placeholder="Enter description" class="form-control" type="text" data-ng-model="processStep.workcenter">--%>
                </td>

                <td>
                    <span ng-hide="processStep.editMode">{{processStep.workcenter.name}}</span>

                    <div ng-show="processStep.editMode">
                        <ui-select ng-model="processStep.workcenter" theme="bootstrap" style="width: 100%;"
                                   title="Choose a Workcenter">
                            <ui-select-match placeholder="Choose a Workcenter">{{$select.selected.name}}
                            </ui-select-match>
                            <ui-select-choices repeat="workcenter in workcenterList | filter: $select.search">
                                <div ng-bind-html="workcenter.name | highlight: $select.search"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>

                    <%--<input ng-show="processStep.editMode" placeholder="Enter description" class="form-control" type="text" data-ng-model="processStep.workcenter">--%>
                </td>
                <td>
                    <span ng-hide="processStep.editMode">{{processStep.sequenceNumber}}</span>
                    <input ng-show="processStep.editMode" placeholder="Enter description" class="form-control"
                           type="text" data-ng-model="processStep.sequenceNumber">
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>

