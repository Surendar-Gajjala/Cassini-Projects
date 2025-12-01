<style>

    table .ui-select-choices {
        position: absolute !important;;
        top: auto !important;
        left: auto !important;
        width: auto !important;
    }

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

    .responsive-table {
        width: 100%;
        margin-bottom: 0;
        padding-bottom: 20px;
        overflow-y: visible;
        overflow-x: visible;
    }

    .img-model .closeImage {
        position: absolute;
        top: 50px;
        right: 50px;
        color: black;
        font-size: 40px;
        font-weight: bold;
        transition: 0.3s;
    }

    .img-model .closeImage:hover,
    .img-model .closeImage:focus {
        color: #bbb;
        text-decoration: none;
        cursor: pointer;
    }

    .img-model .closeImage1 {
        position: absolute;
        top: 50px;
        right: 50px;
        color: black;
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

    .img-model.modal {
        display: none; /* Hidden by default */
        position: fixed; /* Stay in place */
        z-index: 1; /* Sit on top */
        padding-top: 100px; /* Location of the box */
        left: 0;
        top: 0;
        width: 100%; /* Full width */
        height: 100%; /* Full height */
        overflow: auto; /* Enable scroll if needed */
        background-color: rgb(0, 0, 0); /* Fallback color */
        background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
    }

    .img-model.modal1 {
        display: block; /* Hidden by default */
    }

    /* Modal Content (image) */
    .img-model .modal-content {
        margin: auto;
        display: block;
        height: 90%;
        width: 60%;
        /*max-width: 70%;*/
    }

    /* Caption of Modal Image */
    .img-model #caption {
        margin: auto;
        display: block;
        width: 80%;
        max-width: 700px;
        text-align: center;
        color: #ccc;
        padding: 10px 0;
        height: 150px;
    }

    /* Add Animation */
    .img-model .modal-content, #caption {
        -webkit-animation-name: zoom;
        -webkit-animation-duration: 0.6s;
        animation-name: zoom;
        animation-duration: 0.6s;
    }

    .view-content {
        position: relative;
    }

    .attributeTooltip {
        position: relative;
        display: inline-block;
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

    .view-content .responsive-table {
        padding: 10px;
        position: absolute;
        bottom: 40px;
        top: 0;
        overflow: auto;
    }

    .view-content .responsive-table table thead th {
        position: -webkit-sticky;
        position: sticky;
        top: -15px;
        background-color: #fff;
    }

    .view-content .responsive-table table tbody #search {
        position: -webkit-sticky;
        position: sticky;
        top: 25px;
        background-color: #fff;
    }

    .view-content .table-footer {
        padding: 0 10px 0 10px;
        position: absolute;
        bottom: 0;
        height: 40px;
        width: 100%;
        border-top: 1px solid #D3D7DB;
        display: table;
    }

    .view-content .table-footer > div {
        display: table-row;
        line-height: 30px;
    }

    .view-content .table-footer > div h5 {
        margin: 0;
    }

    .view-content .table-footer > div > div {
        display: table-cell;
        vertical-align: middle;
    }

    .view-content .table-footer > div > div > i {
        font-size: 16px;
    }

    .ui-select-bootstrap > .ui-select-match > .btn {
        text-align: left !important;
        height: 30px;
        line-height: 12px;
    }

    .ui-select-container .ui-select-placeholder {
        width: 50px !important;
    }

    .glyphicon-remove :hover {
        color: red;;
    }

    .caret :hover {
        color: red;;
    }

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    .classification-pane .tree-node .tree-title {
        color: grey !important;

    }

    .tree-node-selected {
        background-color: white !important;
    }

    #freeTextSearchDirective {
        top: 7px !important;
    }

    .panels-container {
        margin-top: -4px;
        display: inline-flex;
        margin-left: 329px;
    }

    .panels-container span:first-child {
        border-radius: 3px 0 0 3px;
    }

    .panel-summary {
        height: 34px;
        margin: 3px -20px 0px 8px;
        display: inline-block;
        width: 200px;
        border-radius: 0 3px 3px 0;
        padding-left: 10px;
        line-height: 34px;
    }

    .panel-summary span:first-child {
        width: 150px;
        height: 34px;
    }

    .panel-summary span:first-child h2 {
        margin: 0;
        line-height: 34px;
        color: #fff;
        font-size: 17px;
        display: inline-block;
        padding-right: 10px;
        border-right: 1px solid #fff;
        /*width: 120px;*/
    }

    .panel-summary span:nth-child(2) h1 {
        margin: 0;
        line-height: 34px;
        color: #fff;
        font-size: 18px;
        display: inline-block;
        width: 60px;
    }

    .panel-summary h2,
    .panel-summary h1 {
        text-align: center;
    }

    .panel-summary h1 {
        font-size: 16px;
    }

    .panel-total {
        background: #005C97; /* fallback for old browsers */
        background: -webkit-linear-gradient(to left, #363795, #005C97); /* Chrome 10-25, Safari 5.1-6 */
        background: linear-gradient(to left, #363795, #005C97); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
    }

    .panel-finish {
        background: #159957; /* fallback for old browsers */
        background: -webkit-linear-gradient(to right, #159957, #155799); /* Chrome 10-25, Safari 5.1-6 */
        background: linear-gradient(to right, #159957, #155799); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
    }

    .panel-inprogress {
        background: #fdc830; /* fallback for old browsers */
        background: -webkit-linear-gradient(to right, #fdc830, #f37335); /* Chrome 10-25, Safari 5.1-6 */
        background: linear-gradient(to right, #fdc830, #f37335); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
    }

</style>
<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <div class="row">
            <div class="col-sm-3">
                <button class="btn btn-sm btn-success"
                        ng-disabled="(!tasksVm.newTaskButton || selectedProject.locked == true) || !(hasPermission('permission.tasks.add') || hasPermission('permission.tasks.edit') || login.person.isProjectOwner)"
                        ng-click="tasksVm.newTask()">New Task
                </button>
                <button class="btn btn-sm btn-primary" ng-click="tasksVm.showTaskAttributes()"
                        ng-disabled="selectedProject.locked == true || hasPermission('permission.tasks.taskAttributes') == false">
                    Show Attributes
                </button>
                <button class="btn btn-sm btn-primary" ng-click="tasksVm.importTask()">
                    Import Task
                </button>
            </div>
            <div id="myForm"
                 style="background-color: #f9f9f9;border: 1px solid #ddd;width: 400px;margin-left: auto;margin-right: auto;text-align: center;padding-top: 5px;border-radius: 20px;">
                <span class="mr10 text-primary" style="font-weight: bold">Tasks : </span>

                <div ng-show="hasPermission('permission.tasks.all')" class="rdio rdio-primary"
                     style="display: inline-block;margin-right: 10px;">
                    <input id="tasks" name="task" type="radio" value="tasks">
                    <label for="tasks">All Tasks</label>
                </div>

                <div class="rdio rdio-primary" style="display: inline-block;margin-right: 10px;">
                    <input id="mytasks" name="task" type="radio" value="myTasks">
                    <label for="mytasks">My Tasks</label>
                </div>

                <div class="rdio rdio-primary" style="display: inline-block;margin-right: 10px;">
                    <input id="delayTasks" name="task" type="radio" value="delayTask">
                    <label for="delayTasks">Delayed Tasks</label>
                </div>
            </div>
        </div>

        <free-text-search on-clear="tasksVm.resetPage"
                          on-search="tasksVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="padding: 10px;">

        <div class="responsive-table" style="padding: 10px;">

            <div>
               <span class="panels-container" <%--ng-show="showTaskNotification == true"--%>>
                            <span class="panel-summary panel-total">
                              <span><h2>Total Tasks</h2></span>
                              <span><h1>{{totalTasks}}</h1></span>
                            </span>
                            <span class="panel-summary panel-finish">
                              <span><h2>Finished Tasks</h2></span>
                                <span><h1>{{finishedTasks}}</h1></span>
                             </span>
                             <span class="panel-summary panel-inprogress">
                              <span><h2>Delayed Tasks</h2></span>
                                <span><h1>{{pendingTasks}}</h1></span>
                            </span>
                        </span>
            </div>
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="min-width: 50px"></th>
                    <th style="min-width: 200px;">Name</th>
                    <th style="min-width: 200px;">Description</th>
                    <th style="min-width: 200px;">Site</th>
                    <th style="min-width: 200px;">Subcontract</th>
                    <th style="min-width: 200px;">Assigned To</th>
                    <th style="min-width: 200px;">WBS Ref</th>
                    <th style="min-width: 200px;">Status</th>
                    <th style="min-width: 200px;">Percent Complete</th>
                    <th style="min-width: 200px;">Planned Start Date</th>
                    <th style="min-width: 200px;">Planned Finish Date</th>
                    <th style="min-width: 200px;">Actual Start Date</th>
                    <th style="min-width: 200px;">Actual Finish Date</th>
                    <th style="min-width: 200px;">Inspected On</th>
                    <th style="min-width: 200px;">Inspected By</th>
                    <th style="min-width: 150px;">Inspection Result</th>
                    <th style="min-width: 150px;" ng-repeat="requiredAttribute in tasksVm.requiredTaskAttributes">
                        {{requiredAttribute.name}}
                    </th>
                    <th style="min-width: 150px;" class='added-column'
                        ng-repeat="taskAttribute in tasksVm.taskAttributes">
                        {{taskAttribute.name}}
                        <i class="fa fa-times-circle" style="cursor: pointer;"
                           ng-click="tasksVm.removeAttribute(taskAttribute)"
                           title="Remove this column"></i>
                    </th>
                    <th class="actions-col" style="min-width: 200px; text-align: center">Actions</th>

                </tr>
                </thead>
                <tbody>
                <tr id="search">
                    <td style="min-width: 50px"></td>
                    <td style="text-align: center; width: 150px;">
                        <input type="text" class="form-control input-sm" name="Name"
                               ng-enter="tasksVm.applyFilters()" placeholder="Name" style="margin-top: 0px;height: 30px"
                               ng-model="tasksVm.filters.name">
                    </td>

                    <td style="text-align: center; width: 150px;">
                        <input type="text" class="form-control input-sm" name="Description"
                               style="margin-top: 0px;height: 30px"
                               ng-enter="tasksVm.applyFilters()" placeholder="Description"
                               ng-model="tasksVm.filters.description">
                    </td>

                    <td style="text-align: center; min-width: 150px; max-width: 150px;">
                        <ui-select ng-model="tasksVm.filters.siteObject" on-select="tasksVm.applyFilters()"
                                   theme="bootstrap" style="width:100%;">
                            <ui-select-match allow-clear="true" placeholder="Site">{{$select.selected.name}}
                            </ui-select-match>
                            <ui-select-choices on-highlight="tasksVm.sortValues(tasksVm.sites)"
                                               repeat="site in tasksVm.sites | filter: $select.search |orderBy: 'name'">
                                <div ng-bind="site.name| highlight: $select.name.search"></div>
                            </ui-select-choices>
                        </ui-select>
                    </td>

                    <td style="width: 150px;text-align:center;">
                        <ui-select ng-model="tasksVm.filters.subContract" on-select="tasksVm.applyFilters()"
                                   theme="bootstrap"
                                   style="width:100%">
                            <ui-select-match allow-clear="true" placeholder="subcontract">{{tasksVm.flag}}
                            </ui-select-match>
                            <ui-select-choices repeat="flag in tasksVm.flags | filter: $select.search">
                                <div ng-bind="flag | highlight: $select.flag.search"></div>
                            </ui-select-choices>
                        </ui-select>
                    </td>

                    <td style="width: 150px;text-align:center;">
                        <ui-select ng-model="tasksVm.filters.personObject" on-select="tasksVm.applyFilters()"
                                   theme="bootstrap"
                                   style="width:100%">
                            <ui-select-match allow-clear="true" placeholder="AssignedTo">
                                {{$select.selected.fullName}}
                            </ui-select-match>
                            <ui-select-choices
                                    repeat="person in tasksVm.persons | filter: $select.search |orderBy: 'fullName'">
                                <div ng-bind="person.fullName | highlight: $select.person.fullName.search"></div>
                            </ui-select-choices>
                        </ui-select>
                    </td>
                    <td style="text-align: center; width: 150px;">
                        <%--   <div class="input-group">
                               <input type="text" class="form-control" name="title"
                                      ng-model="tasksVm.filters.wbsItemObject" readonly/>

                               <div class="input-group-btn" uib-dropdown>
                                   <button uib-dropdown-toggle class="btn btn-default dropdown-toggle" type="button"
                                           ng-click="$event.stopPropagation();">
                                       Select WBS<span class="caret" style="margin-left: 4px;"></span>
                                   </button>
                                   <div class="dropdown-menu" role="menu">
                                       <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                           <wbs-tree-select on-select-wbs="tasksVm.onSelectWbsTree"></wbs-tree-select>
                                       </div>
                                   </div>
                               </div>
                           </div>--%>
                        <div class="input-group">
                            <div class="input-group-btn" uib-dropdown>
                                <button uib-dropdown-toggle class="btn btn-default dropdown-toggle" type="button"
                                        style="padding: 4px">
                                    <span translate>SELECT</span> <span class="caret"
                                                                        style="margin-left: 4px;"></span>
                                </button>
                                <div class="dropdown-menu" role="menu">
                                    <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                        <wbs-tree-select
                                                on-select-wbs="tasksVm.onSelectWbsTree"></wbs-tree-select>
                                    </div>
                                </div>
                            </div>
                            <input type="text" class="form-control" name="title" style="padding: 5px;"
                                   ng-model="tasksVm.filters.wbsItemObject.name">


                        </div>
                    </td>
                    <%--
                                        <td style="text-align: center; width: 150px;">
                                            <ui-select ng-model="tasksVm.filters.wbsItemObject" on-select="tasksVm.applyFilters()"
                                                       theme="bootstrap" style="width:100%">
                                                <ui-select-match allow-clear="true" placeholder="wbsItem">{{$select.selected.name}}
                                                </ui-select-match>
                                                <ui-select-choices on-highlight="tasksVm.sortValues(tasksVm.wbsItems)"
                                                                   repeat="wbsItem in tasksVm.wbsItems | filter: $select.search">
                                                    <div ng-bind="wbsItem.name| highlight: $select.name.search"></div>
                                                </ui-select-choices>
                                            </ui-select>
                                        </td>--%>

                    <td style="width: 150px;text-align:center;">
                        <ui-select ng-model="tasksVm.filters.status" on-select="tasksVm.applyFilters()"
                                   theme="bootstrap"
                                   style="width:100%">
                            <ui-select-match allow-clear="true" placeholder="Status">{{$select.selected}}
                            </ui-select-match>
                            <ui-select-choices repeat="status in tasksVm.listStatus | filter: $select.search">
                                <div ng-bind="status | highlight: $select.status.search"></div>
                            </ui-select-choices>
                        </ui-select>
                    </td>

                    <td style="text-align: center; width: 150px;">
                        <input type="text" class="form-control input-sm" name="Percent"
                               ng-enter="tasksVm.applyFilters()" placeholder="Percent"
                               style="margin-top: 0px;height: 30px"
                               ng-model="tasksVm.filters.percentCompleteObject">
                    </td>

                    <td style="text-align: center; width: 150px;">
                        <div class="input-group">
                            <input type="text" class="form-control input-sm" date-picker
                                   ng-model="plannedStartDate" style="margin-top: 0px;height: 30px"
                                   name="plannedStartDate" placeholder="dd/mm/yyyy">
                        </div>
                    </td>

                    <td style="text-align: center; width: 150px;">
                        <div class="input-group">
                            <input type="text" class="form-control input-sm" date-picker
                                   ng-model="plannedFinishDate"
                                   on-select="tasksVm.applyFilters()" style="margin-top: 0px;height: 30px"
                                   name="PlannedFinishDate" placeholder="dd/mm/yyyy">
                        </div>
                    </td>
                    <td style="text-align: center; width: 150px;">
                        <div class="input-group">
                            <input type="text" class="form-control input-sm" date-picker
                                   ng-model="actualStartDate" style="margin-top: 0px;height: 30px"
                                   on-select="tasksVm.applyFilters()"
                                   name="ActualStartDate" placeholder="dd/mm/yyyy">
                        </div>
                    </td>
                    <td style="text-align: center; width: 150px;">
                        <div class="input-group">
                            <input type="text" class="form-control input-sm" date-picker
                                   ng-model="actualFinishDate" style="margin-top: 0px;height: 30px"
                                   on-select="tasksVm.applyFilters()"
                                   name="ActualFinishDate" placeholder="dd/mm/yyyy">
                        </div>
                    </td>
                    <td style="text-align: center; width: 150px;">
                        <div class="input-group">
                            <input type="text" class="form-control input-sm" date-picker
                                   ng-model="inspectedOn" style="margin-top: 0px;height: 30px"
                                   name="inspectedDate" placeholder="dd/mm/yyyy">
                        </div>
                    </td>
                    <td style="width: 150px;text-align:center;">
                        <ui-select ng-model="tasksVm.filters.inspectedByPerson" on-select="tasksVm.applyFilters()"
                                   theme="bootstrap"
                                   style="width:100%">
                            <ui-select-match allow-clear="true" placeholder="Inspected By">
                                {{$select.selected.fullName}}
                            </ui-select-match>
                            <ui-select-choices
                                    repeat="person in tasksVm.persons | filter: $select.search |orderBy: 'fullName'">
                                <div ng-bind="person.fullName | highlight: $select.person.fullName.search"></div>
                            </ui-select-choices>
                        </ui-select>
                    </td>
                    <td style="width: 150px;text-align:center;">
                        <ui-select ng-model="tasksVm.filters.inspectionResult" on-select="tasksVm.applyFilters()"
                                   theme="bootstrap"
                                   style="width:100%">
                            <ui-select-match allow-clear="true" placeholder="Status">{{$select.selected}}
                            </ui-select-match>
                            <ui-select-choices repeat="status in tasksVm.inspectionResults | filter: $select.search">
                                <div ng-bind="status | highlight: $select.status.search"></div>
                            </ui-select-choices>
                        </ui-select>
                    </td>
                    <td style="width: 150px;" ng-repeat="requiredAttribute in tasksVm.requiredTaskAttributes"></td>
                    <td ng-repeat="taskAttribute in tasksVm.taskAttributes"></td>
                    <td class="actions-col" style="width:150px;text-align: center;">
                        <div class="btn-group btn-group-xs">

                            <button title="Clear Filters" type="button" class="btn btn-xs btn-default"
                                    ng-click="tasksVm.resetFilters()">
                                <i class="fa fa-times"></i>
                            </button>

                            <button title="Apply Filters" type="button" class="btn btn-xs btn-success"
                                    ng-click="tasksVm.applyFilters()">
                                <i class="fa fa-search"></i>
                            </button>
                        </div>

                    </td>
                </tr>
                <tr ng-if="tasksVm.loading == true">
                    <td colspan="10">
                            <span style="font-size: 15px;">
                                <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading Tasks..
                            </span>
                    </td>
                </tr>
                <tr ng-if="tasksVm.loading == false && tasksVm.tasks.content.length == 0">
                    <td colspan="20">No Tasks are available to view</td>
                </tr>


                <tr ng-repeat="task in tasksVm.tasks.content" ng-if="tasksVm.loading == false">
                    <td style="min-width: 50px">
                        <span ng-if="task.workflow != null"><i class="fa flaticon-plan2 nav-icon-font"
                                                               title="Task has workflow"></i></span>
                    </td>
                    <td><a ng-click="tasksVm.showTaskDetails(task)"><span
                            ng-bind-html="task.name | highlightText: freeTextQuery"></span></a></td>
                    <td>
                        <span title="{{task.description}}">{{task.description | limitTo: 20}}{{task.description.length > 20 ? '...' : ''}}</span>
                    </td>
                    <%--<td>{{task.siteObject.name}}</td>--%>
                    <td>
                        <a style="color:#428bca; width: 150px;"
                           ng-if="task.percentComplete == 0"
                           href="#"
                           editable-select="task.siteObject"
                           onaftersave="tasksVm.updateSite(task)"
                           e-ng-options="site as site.name for site in tasksVm.sites | orderBy: ['name'] track by site.id">
                            {{task.siteObject.name || 'Click to Change Site'}}
                        </a>
                         <span ng-if="task.percentComplete > 0">
                                 {{task.siteObject.name}}
                        </span>
                    </td>
                    <td>{{task.subContract}}</td>

                    <td>
                        <a style=" color:#428bca; width: 150px;"
                           ng-if="((task.status == 'NEW' || task.status == 'ASSIGNED') &&
                        (hasPermission('permission.tasks.edit') || (login.person.id == task.createdBy) ||
                        login.person.isProjectOwner))"
                           href="#"
                           editable-select="task.personObject"
                           onaftersave="tasksVm.assignPerson(task)"
                           e-ng-options="person as person.fullName for person in tasksVm.persons | orderBy: ['fullName'] track by person.id">
                            {{task.personObject.fullName || 'Click to assign person'}}
                        </a>
                        <span ng-if="!((task.status == 'NEW' || task.status == 'ASSIGNED') && (hasPermission('permission.tasks.edit') || (login.person.id == task.createdBy) || login.person.isProjectOwner))">
                                 {{task.personObject.fullName}}
                        </span>
                    </td>


                    <%--<td>{{task.personObject.fullName}}</td>--%>
                    <td style="width: auto">{{task.wbsStructure}}</td>
                    <td><span ng-if="task.workflow == null" class="label" ng-class="{
                                    'label-success': task.status == 'NEW',
                                    'label-info': task.status == 'ASSIGNED',
                                    'label-warning': task.status == 'INPROGRESS',
                                    'label-primary': task.status == 'FINISHED'}">
                            {{task.status}}
                        </span>
                        <span ng-if="task.workflow != null">
                            {{task.wfStatus}}
                        </span>
                    </td>
                    <td>
                        <div class="task-progress progress text-center">
                            <div style="width:{{task.percentComplete}}%"
                                 class="progress-bar progress-bar-primary progress-bar-striped active"
                                 role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100">
                                <span style="margin-left: 2px;">{{task.percentComplete}}%</span>
                            </div>
                        </div>
                    </td>
                    <td>{{task.plannedStartDate}}</td>
                    <td>{{task.plannedFinishDate}}</td>
                    <td>{{task.actualStartDate}}</td>
                    <td>{{task.actualFinishDate}}</td>
                    <td>{{task.inspectedOn}}</td>
                    <td>{{task.inspectedByObject.fullName}}</td>
                    <td><span class="label" ng-class="{
                                    'label-success': task.inspectionResult == 'ACCEPTED',
                                    'label-danger': task.inspectionResult == 'REJECTED'}">
                            {{task.inspectionResult}}
                        </span></td>
                    <td class="added-column" ng-repeat="objectAttribute in tasksVm.requiredTaskAttributes">
                        <div class="attributeTooltip" ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                             && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'ATTACHMENT'"
                             ng-init="attrName = objectAttribute.name">

                            <p>
                                <a ng-if="task[attrName].length > 0" href="">
                                    {{task[attrName].length}} Attachments
                                </a>
                            </p>

                            <div class="attributeTooltiptext">
                                <ul>
                                    <li ng-repeat="attachment in task[attrName]">
                                        <a href="" ng-click="tasksVm.openAttachment(attachment)"
                                           title="Click to download file"
                                           style="margin-bottom: 5px;width:200px;display: run-in;white-space: normal;word-wrap: break-word;color: #141f9f !important;">
                                            {{attachment.name}}
                                        </a>
                                    </li>
                                </ul>
                            </div>
                        </div>

                        <div ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'&& objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'IMAGE'"
                             ng-init="attrName = objectAttribute.name">

                            <a href="" ng-click="tasksVm.showImage(task[attrName])"
                               title="Click to show large Image">
                                <img ng-if="task[attrName] != null"
                                     ng-src="{{task[attrName]}}"
                                     style="height: 30px;width: 40px;margin-bottom: 5px;">
                            </a>

                            <div id="taskModal" class="img-model modal">
                                <span class="closeImage">&times;</span>
                                <img class="modal-content" id="taskImg">
                            </div>
                        </div>

                        <p ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'&& objectAttribute.dataType != 'LIST'
                                        && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'OBJECT' && objectAttribute.dataType != 'RICHTEXT'"
                           ng-init="attrName = objectAttribute.name">
                            {{task[attrName]}}
                        </p>

                        <p ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy' && objectAttribute.dataType != 'OBJECT' && objectAttribute.dataType != 'ATTACHMENT'&& objectAttribute.dataType != 'IMAGE' && objectAttribute.dataType != 'CURRENCY'"
                           ng-init="attrName = objectAttribute.name">
                            {{task[attrName]}}
                        </p>

                        <span ng-init="currencyType = objectAttribute.name+'type'"
                              ng-if="objectAttribute.dataType == 'CURRENCY'" ng-bind-html="task[currencyType]">
                        </span>
                        <span ng-init="attrName = objectAttribute.name" ng-if="objectAttribute.dataType == 'CURRENCY'">
                            {{task[attrName]}}
                        </span>
                    </td>

                    <td class="added-column" ng-repeat="objectAttribute in tasksVm.taskAttributes">

                        <div class="attributeTooltip"
                             ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                             && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'ATTACHMENT'"
                             ng-init="attrName = objectAttribute.name">
                            <p>
                                <a ng-if="task[attrName].length > 0" href="">
                                    {{task[attrName].length}} Attachments
                                </a>
                            </p>

                            <div class="attributeTooltiptext">
                                <ul>
                                    <li ng-repeat="attachment in task[attrName]">
                                        <a href="" ng-click="tasksVm.openAttachment(attachment)"
                                           title="Click to download file"
                                           style="margin-bottom: 5px;width:200px;display: run-in;white-space: normal;word-wrap: break-word;color: #141f9f !important;">
                                            {{attachment.name}}
                                        </a>
                                    </li>
                                </ul>
                            </div>
                        </div>

                        <div ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                                        && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'IMAGE'"
                             ng-init="attrName = objectAttribute.name">
                            <a href="" ng-click="tasksVm.showRequiredImage(task[attrName])"
                               title="Click to show large Image">
                                <img ng-if="task[attrName] != null"
                                     ng-src="{{task[attrName]}}"
                                     style="height: 30px;width: 40px;margin-bottom: 5px;">
                            </a>

                            <div id="taskModal1" class="img-model modal">
                                <span class="closeImage1">&times;</span>
                                <img class="modal-content" id="taskImg1">
                            </div>
                        </div>

                        <p style="vertical-align: middle;width: 150px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                           ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'&& objectAttribute.dataType != 'LIST'
                                        && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'OBJECT' && objectAttribute.dataType != 'RICHTEXT'"
                           ng-init="attrName = objectAttribute.name" title="{{task[attrName]}}">
                            {{task[attrName]}}
                        </p>

                        <p style="vertical-align: middle;width: 150px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                           ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                                        && objectAttribute.dataType != 'OBJECT' && objectAttribute.dataType != 'ATTACHMENT'
                                        && objectAttribute.dataType != 'IMAGE' && objectAttribute.dataType != 'CURRENCY'"
                           ng-init="attrName = objectAttribute.name" title="{{task[attrName]}}">
                            {{task[attrName]}}
                        </p>

                        <span ng-init="currencyType = objectAttribute.name+'type'"
                              ng-if="objectAttribute.dataType == 'CURRENCY'" ng-bind-html="task[currencyType]">
                        </span>
                        <span ng-init="attrName = objectAttribute.name"
                              ng-if="objectAttribute.dataType == 'CURRENCY'">
                            {{task[attrName]}}
                        </span>
                    </td>
                    <td class="text-center" ng-if="!(task.status == 'FINISHED' || selectedProject.locked == true) &&
                                (hasPermission('permission.task.delete') || login.person.isProjectOwner || task.hasProblems)">
                                    <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                        style="z-index: 9999 !important;">
                                        <li ng-if="!(task.status == 'FINISHED' || selectedProject.locked == true ||
                                !(hasPermission('permission.task.delete') || login.person.isProjectOwner))"
                                            ng-click='tasksVm.deleteProjectTask(task)'>
                                            <a class="dropdown-item" title="Delete Task">
                                                <span style="padding-left: 3px;">Delete</span>
                                            </a>
                                        </li>
                                        <li ng-if="task.status == 'INPROGRESS' && !task.hasProblems"
                                            ng-click='tasksVm.finishTask(task)'>
                                            <a class="dropdown-item">
                                                <span style="padding-left: 3px;">Finish</span>
                                            </a>
                                        </li>
                                    </ul>
                                </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <div>
                <div>
                    <h5 style="font-weight: 700">Displaying {{tasksVm.tasks.numberOfElements}} of
                        {{tasksVm.tasks.totalElements}}</h5>
                </div>

                <div class="text-right">
                    <span class="mr10">Page {{tasksVm.tasks.totalElements != 0 ? tasksVm.tasks.number+1:0}} of {{tasksVm.tasks.totalPages}}</span>
                    <a href="" ng-click="tasksVm.previousPage()"
                       ng-class="{'disabled': tasksVm.tasks.first}"><i class="fa fa-arrow-circle-left mr10"></i></a>
                    <a href="" ng-click="tasksVm.nextPage()"
                       ng-class="{'disabled': tasksVm.tasks.last}"><i class="fa fa-arrow-circle-right"></i></a>
                </div>
            </div>
        </div>
    </div>
</div>
