<style>
    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }
</style>
<div style="padding: 0px 20px;" <%--ng-if="taskResourcesVm.mode == 'Null'"--%>>
    <div style="position: relative;">
        <div style="overflow-y: auto; overflow-x: hidden; padding: 20px">
            <%--   <div ng-if="taskResourcesVm.persons.length == 0 &&
                           taskResourcesVm.machineItems.length == 0 &&
                           taskResourcesVm.materialItems.length == 0 &&
                           taskResourcesVm.projectRoles.length == 0">
                   <tr>No Resources</tr>
               </div>--%>

            <table class="table table-striped highlight-row table-condensed"
                   style="max-height: 100px; overflow-y: scroll">
                <thead>
                <tr>
                    <th>Date</th>
                    <th>Person</th>
                    <th>Units Completed</th>
                    <th style="text-align: left; text-indent: 30px ">Notes</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>{{taskResourcesVm.newUnitOfWorkCompleted.timeStamp}}</td>
                    <td>{{taskResourcesVm.newUnitOfWorkCompleted.completedBy.fullName}}</td>
                    <td>
                        <div class="row">
                            <input placeholder="Qty" class="form-control input-sm"
                                   ng-enter="taskBasicVm.updateTaskCompletion()"
                                   type="number" style="width: 100px; display: inline-block"
                                   data-ng-model="taskResourcesVm.newUnitOfWorkCompleted.unitsCompleted">
                        </div>
                    </td>
                    <td>
                        <div class="row" style="padding-right: 8px;">
                            <input placeholder="Enter Notes" class="form-control input-sm"
                                   type="text" style="width: 150px; display: inline-block"
                                   data-ng-model="taskResourcesVm.newUnitOfWorkCompleted.notes">
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>

            <div class="row" style="margin-right: 48px;"
                 ng-if="taskResourcesVm.persons.length > 0 ||
                        taskResourcesVm.machineItems.length > 0 ||
                        taskResourcesVm.materialItems.length > 0 ||
                        taskResourcesVm.projectRoles.length > 0">
                <form class="form-horizontal" ng-if="taskResourcesVm.mode != 'EDIT'">
                    <h3 style="margin-left: 270px">Resources</h3>
                    <hr>
                    <h5>ManPower</h5>
                    <hr>
                    <div ng-repeat="person in taskResourcesVm.persons">
                        <table>
                            <tbody>
                            <tr>
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">{{person.fullName}}</label>

                                </div>
                                <%-- <td>{{person.fullName}}</td>
                                 <td>
                                     <input type="text" class="form-control">
                                 </td>--%>
                            </tr>
                            </tbody>
                        </table>

                    </div>
                    <h5>Machines</h5>
                    <hr>
                    <div ng-repeat="machineItem in taskResourcesVm.machineItems">
                        <table>
                            <tbody>
                            <tr>
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">{{machineItem.itemNumber}} :</label>

                                    <div class="col-sm-5">
                                        <input type="number" class="form-control" name="title"
                                               ng-model="machineItem.machineQty">
                                    </div>
                                </div>
                            </tr>
                            </tbody>
                        </table>
                    </div>

                    <h5>Materials</h5>
                    <hr>
                    <div ng-repeat="materialItem in taskResourcesVm.materialItems">
                        <table>
                            <tbody>
                            <tr>
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">{{materialItem.itemNumber}} :</label>

                                    <div class="col-sm-5">
                                        <input type="number" class="form-control" name="title"
                                               ng-model="materialItem.materialQty">
                                    </div>
                                </div>

                            </tr>
                            </tbody>
                        </table>
                    </div>

                    <h5>Role</h5>
                    <hr>
                    <div ng-repeat="projectRole in taskResourcesVm.projectRoles">
                        <table>
                            <tbody>
                            <tr>
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">{{projectRole.role}} :</label>

                                    <div class="col-sm-5">
                                        <input type="number" class="form-control" name="title"
                                               ng-model="projectRole.roleQty">
                                    </div>
                                </div>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </form>
                <div style="padding: 5px"></div>
            </div>
        </div>
    </div>
</div>
<%--

<div style="padding: 0px 20px;" ng-if="taskResourcesVm.mode == 'EDIT'">
    <div class="col-md-12" style="padding:0px; height: auto;overflow: auto;"
         ng-if="taskResourcesVm.mode == 'EDIT'">
        <table class="table table-striped">
            <thead>
            <tr>
                <th>Resource</th>
                <th>
                    Quantity
                </th>
            </tr>
            </thead>

            <tbody>
            <tr>
                <div ng-repeat="resource in taskResourcesVm.taskCompletionResources">
                    <div class="form-group" ng-if="resource.resourceType == 'MANPOWER'">
                        <h3>Manpower </h3>
                        <label class="col-sm-4 control-label">{{resource.resourceIdObject.fullName}}
                            :</label>

                        <div class="col-sm-5">{{resource.quantity}}
                            &lt;%&ndash; <input type="number" class="form-control" name="title"
                                    ng-model="materialItem.materialQty">&ndash;%&gt;
                        </div>
                    </div>
                    <div class="form-group" ng-if="resource.resourceType == 'MATERIAL'">
                        <h3>Material </h3>
                        <label class="col-sm-4 control-label">{{resource.resourceIdObject.itemNumber}}
                            :</label>

                        <div class="col-sm-5">
                            <span>{{resource.quantity}}</span>
                        </div>
                    </div>
                    <div class="form-group" ng-if="resource.resourceType == 'MACHINE'">
                        <h3>Machine </h3>
                        <label class="col-sm-4 control-label">{{resource.resourceIdObject.itemNumber}}
                            :</label>

                        <div class="col-sm-5">
                            <span>{{resource.quantity}}</span>
                        </div>
                    </div>
                    <div class="form-group" ng-if="resource.resourceType == 'ROLE'">
                        <h3>Role</h3>
                        <label class="col-sm-4 control-label">{{resource.resourceIdObject.role}}
                            :</label>

                        <div class="col-sm-5">
                            <span>{{resource.quantity}}</span>
                        </div>
                    </div>
                </div>
            </tr>
            </tbody>
        </table>
    </div>
</div>--%>


<style>
    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }
</style>
<div style="padding: 0px 20px;" ng-if="taskResourcesVm.mode == 'EDIT'">
    <div class="row">
        <div class="col-md-12" style="padding:0px; height: auto;overflow: auto;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th>Resource</th>
                    <th>Resource Type</th>
                    <th>Quantity</th>
                </tr>
                </thead>
                <tbody>
                <%--<tr ng-if="taskResourcesVm.machines.content.length == 0 && !machineDialogueVm.loading ">
                    <td colspan="11" style="padding-left: 30px;">No Machines</td>
                </tr>--%>
                <tr ng-repeat="resource in taskResourcesVm.taskCompletionResources">
                    <td>
                    <span ng-if="resource.resourceType == 'MANPOWER'">
                        {{resource.resourceIdObject.fullName}}
                    </span>
                    <span ng-if="resource.resourceType == 'MATERIAL'">
                        {{resource.resourceIdObject.itemNumber}}
                    </span>
                    <span ng-if="resource.resourceType == 'MACHINE'">
                        {{resource.resourceIdObject.itemNumber}}
                    </span>
                    <span ng-if="resource.resourceType == 'ROLE'">
                        {{resource.resourceIdObject.role}}
                    </span>
                    </td>
                    <td>
                    <span ng-if="resource.resourceType == 'MANPOWER'">
                        {{resource.resourceType}}
                    </span>
                    <span ng-if="resource.resourceType == 'MATERIAL'">
                        {{resource.resourceType}}
                    </span>
                    <span ng-if="resource.resourceType == 'MACHINE'">
                        {{resource.resourceType}}
                    </span>
                    <span ng-if="resource.resourceType == 'ROLE'">
                        {{resource.resourceType}}
                    </span>
                    </td>
                    <td>
                        <span ng-if="resource.resourceType == 'MANPOWER'">
                        {{resource.quantity}}
                    </span>
                    <span ng-if="resource.resourceType == 'MATERIAL'">
                        {{resource.quantity}}
                    </span>
                    <span ng-if="resource.resourceType == 'MACHINE'">
                        {{resource.quantity}}
                    </span>
                    <span ng-if="resource.resourceType == 'ROLE'">
                        {{resource.quantity}}
                    </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <br>
    <br>
</div>
