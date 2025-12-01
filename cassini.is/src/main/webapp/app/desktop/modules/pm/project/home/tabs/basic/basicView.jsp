<style>
    .browse-control {
        -moz-border-radius: 3px;
        -webkit-border-radius: 3px;
        border-radius: 3px;
        padding: 5px;
        height: auto;
        -moz-box-shadow: none;
        -webkit-box-shadow: none;
        box-shadow: none;
        font-size: 13px;
        border: 1px solid #ccc;
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

    .chart .flot {
        width: 32%;
    }

    .flot2 {
        width: 34% !important;
    }

    .chart-info-panel {
        position: relative;
        height: 30px;
        padding-left: 10px;
        display: flex;
        text-align: center;
    }
</style>
<div ng-if="basicVm.loading == true" style="padding: 30px;">
    <br/>
        <span style="font-size: 15px;">
            <img src="app/assets/images/loaders/loader6.gif" class="mr5">Loading project details...
        </span>
    <br/>
</div>

<div class="row row-eq-height" style="margin: 0" ng-if="basicVm.loading == false">
    <div class="item-details col-sm-12" style="padding: 30px;">
        <!-- Categories -->
        <div class="row" style="border: 0; background-color: whitesmoke; margin-bottom: 25px">
            <div class="col-sm-4">
                <div class="kt-portlet kt-portlet--height-fluid" style="background-color: white; height: 450px">
                    <div class="kt-widget" style="padding: 25px !important;">
                        <div class="kt-widget__header kt-margin-b-30" style="padding: 0.5rem 0 1.1rem 0;">
                            <h3 class="kt-widget__title" style="margin: 0">
                                Tasks by Status
                            </h3>
                        </div>
                        <div class="kt-widget__chart" style="height:120px;">
                            <div id="chart"></div>
                        </div>
                    </div>
                </div>

            </div>
            <div class="col-sm-4">
                <div class="kt-portlet kt-portlet--height-fluid" style="background-color: white; height: 450px">
                    <div class="kt-widget" style="padding: 25px !important;">
                        <div class="kt-widget__header kt-margin-b-30" style="padding: 0.5rem 0 1.1rem 0;">
                            <h3 class="kt-widget__title" style="margin: 0">
                                Project Problems by Priority
                            </h3>
                        </div>
                        <div class="kt-widget__chart" style="height:120px;">
                            <div id="pie"></div>
                        </div>
                    </div>
                </div>

            </div>
            <div class="col-sm-4">
                <div class="kt-portlet kt-portlet--height-fluid" style="background-color: white; height: 450px">
                    <div class="kt-widget" style="padding: 25px !important;">
                        <div class="kt-widget__header kt-margin-b-30" style="padding: 0.5rem 0 1.1rem 0;">
                            <h3 class="kt-widget__title" style="margin: 0">
                                Project Media
                            </h3>
                        </div>
                        <div class="kt-widget__chart" style="height:120px;">
                            <div id="mediaChart"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="label col-sm-2">
                <span>Name: </span>
            </div>
            <div class="value col-sm-10">
                <a style="color:#428bca;"
                   ng-if="selectedProject.locked == false && (hasPermission('permission.home.editBasic') || login.person.isProjectOwner)"
                   href="#" editable-text="basicVm.project.name"
                   onaftersave="basicVm.updateProject()">
                    {{basicVm.project.name}}</a>

                <p ng-if="selectedProject.locked == false && !(hasPermission('permission.home.editBasic') || login.person.isProjectOwner)">
                    {{basicVm.project.name}}</p>

                <p ng-if="selectedProject.locked == true">{{basicVm.project.name}}</p>
            </div>
        </div>
        <div class="row">
            <div class="label col-sm-2">
                <span>Description: </span>
            </div>
            <div class="value col-sm-10">
                <a style="color:#428bca;"
                   ng-if="(selectedProject.locked == false) && (hasPermission('permission.home.editBasic')  || login.person.isProjectOwner)"
                   href="#" editable-text="basicVm.project.description"
                   onaftersave="basicVm.updateProject()">
                    {{basicVm.project.description || 'Click to enter description'}}</a>

                <p ng-if="selectedProject.locked == false && !(hasPermission('permission.home.editBasic') || login.person.isProjectOwner)">
                    {{basicVm.project.description}}</p>

                <p ng-if="selectedProject.locked == true">{{basicVm.project.description}}</p>
            </div>
        </div>
        <div class="row">
            <div class="label col-sm-2">
                <span>Status: </span>
            </div>
            <div class="value col-xs-8 col-sm-9" style="line-height: 20px">

                <p ng-if="(selectedProject.locked == false) && !(hasPermission('permission.home.editBasic') || login.person.isProjectOwner)">

                            <span class="label" style="color: white;"
                                  ng-class="{'label-success': basicVm.project.status == 'NEW',
                                    'label-info': basicVm.project.status == 'ASSIGNED',
                                    'label-warning': basicVm.project.status == 'INPROGRESS',
                                    'label-danger': basicVm.project.status == 'CLOSED'}">
                            {{basicVm.project.status}} &nbsp &nbsp
                        <span style="color:white;"></span>
                    </span>
                </p>

                <a ng-if="selectedProject.locked == false && (hasPermission('permission.home.editBasic') || login.person.isProjectOwner)"
                   href="#" title="edit status"
                   style="text-decoration: none !important;" editable-select="basicVm.project.status"
                   onaftersave="basicVm.update()" e-ng-options="status for status in basicVm.status">

                            <span class="label" style="color: white;" ng-class="{
                                    'label-success': basicVm.project.status == 'NEW',
                                    'label-info': basicVm.project.status == 'ASSIGNED',
                                    'label-warning': basicVm.project.status == 'INPROGRESS',
                                    'label-danger': basicVm.project.status == 'CLOSED'}">
                            {{basicVm.project.status}} &nbsp &nbsp<span style="color:white;"><i
                                    class="fa fa-pencil-square-o" aria-hidden="true"></i>
                            </span> </span>
                </a>

                <p ng-if="selectedProject.locked == true">
                    <span class="label"
                          ng-class="{'label-success': basicVm.project.status == 'NEW',
                                    'label-info': basicVm.project.status == 'ASSIGNED',
                                    'label-warning': basicVm.project.status == 'INPROGRESS',
                                    'label-danger': basicVm.project.status == 'CLOSED'}">
                            {{basicVm.project.status}} &nbsp &nbsp
                        <span style="color:white;"></span>
                    </span>
                </p>
            </div>
        </div>
        <div class="row">
            <div class="label col-sm-2">
                <span>Created By: </span>
            </div>
            <div class="value col-sm-10">{{basicVm.project.createdByObject.fullName}}</div>
        </div>
        <div class="row">
            <div class="label col-sm-2">
                <span>Project Owner: </span>
            </div>
            <div class="value col-sm-10">
                <a style="color:#428bca;"
                   ng-if="selectedProject.locked == false && (hasPermission('permission.home.editBasic') || login.person.isProjectOwner)"
                   href="#" editable-select="basicVm.project.projectOwnerObject"
                   onaftersave="basicVm.updateProject()"
                   e-ng-options="person as person.fullName for person in basicVm.persons |orderBy: ['fullName'] track by person.id">
                    <span>{{basicVm.project.projectOwnerObject.fullName}}</span>
                </a>

                <p ng-if="selectedProject.locked == false && !(hasPermission('permission.home.editBasic') || login.person.isProjectOwner)">
                    {{basicVm.project.projectOwnerObject.fullName}}</p>

                <p ng-if="selectedProject.locked == true">{{basicVm.project.projectOwnerObject.fullName}}</p>
            </div>
        </div>
        <div class="row">
            <div class="label col-sm-2">
                <span>Planned Start Date: </span>
            </div>
            <div class="value col-sm-10">{{basicVm.project.plannedStartDate}}</div>
        </div>

        <div class="row">
            <div class="label col-sm-2">
                <span>Planned Finish Date: </span>
            </div>
            <div class="value col-sm-10">{{basicVm.project.plannedFinishDate}}</div>
        </div>

        <div class="row">
            <div class="label col-sm-2">
                <span> Actual Start Date:</span>
            </div>

            <div class="value col-sm-10">
                <a style="color:#428bca;"
                   ng-if="selectedProject.locked == false  && (hasPermission('permission.home.editBasic') || login.person.isProjectOwner)"
                   href=""
                   e-is-open="opened.$data" e-ng-click="open($event,'$data')" data-format="DD/MM/YYYY"
                   editable-bsdate="basicVm.project.actualStartDate" onaftersave="basicVm.updateStartDate()">
                    {{ (basicVm.project.actualStartDate | date:"dd/MM/yyyy") || 'empty' }}
                </a>

                <p ng-if="(selectedProject.locked == false) && !(hasPermission('permission.home.editBasic')  || login.person.isProjectOwner)">
                    {{ (basicVm.project.actualStartDate | date:"dd/MM/yyyy") || 'empty' }}</p>

                <p ng-if="selectedProject.locked == true">
                    {{ (basicVm.project.actualStartDate | date:"dd/MM/yyyy") || 'empty' }}</p>
            </div>
        </div>

        <div class="row">
            <div class="label col-sm-2">
                <span> Actual Finish Date:</span>
            </div>

            <div class="value col-sm-10">
                <a style="color:#428bca;"
                   ng-if="selectedProject.locked == false  && (hasPermission('permission.home.editBasic') || login.person.isProjectOwner)"
                   href=""
                   e-is-open="opened.$data" e-ng-click="open($event,'$data')"
                   data-format="DD/MM/YYYY" editable-bsdate="basicVm.project.actualFinishDate"
                   onaftersave="basicVm.updateFinishDate()">
                    {{ (basicVm.project.actualFinishDate | date:"dd/MM/yyyy") || 'empty' }}
                </a>

                <p ng-if="(selectedProject.locked == false) && !(hasPermission('permission.home.editBasic') || login.person.isProjectOwner)">
                    {{ (basicVm.project.actualFinishDate | date:"dd/MM/yyyy") || 'empty' }}</p>

                <p ng-if="selectedProject.locked == true">
                    {{ (basicVm.project.actualFinishDate | date:"dd/MM/yyyy") || 'empty' }}</p>
            </div>
        </div>

        <h4><i ng-if="!basicVm.viewDetails" class="fa fa-plus-circle"
               title="View Details"
               ng-click="basicVm.showDetails()"
               style="padding-right: 10px; padding-top: 10px;cursor: pointer;"
               aria-hidden="true"></i>

            <i ng-if="basicVm.viewDetails" class="fa fa-minus-circle"
               title="View Details"
               ng-click="basicVm.showDetails()"
               style="padding-right: 10px; padding-top: 10px;cursor: pointer;"
               aria-hidden="true"></i>
            Mail Server Settings:</h4>

        <hr>
        <div class="row" ng-if="basicVm.mailServerObject.mailServer != null && basicVm.viewDetails">
            <div class="label col-sm-2">
                <span>Mail Server: </span>
            </div>

            <div class="value col-sm-10">
                <a style="color:#428bca;"
                   ng-if="selectedProject.locked == false && (hasPermission('permission.home.editBasic') || login.person.isProjectOwner)"
                   href="#" editable-select="basicVm.mailServerObject.mailServerObj"
                   onaftersave="basicVm.updateObjectMail()"
                   e-ng-options="mailServer.name for mailServer in basicVm.mailServers">
                    {{basicVm.mailServerObject.mailServerObj.name || 'Click to select Mail Server'}}
                </a>

                <p ng-if="selectedProject.locked == false && !(hasPermission('permission.home.editBasic') || login.person.isProjectOwner)">
                    {{basicVm.mailServerObject.mailServerObj.name}}</p>

                <p ng-if="selectedProject.locked == true">{{basicVm.mailServerObject.mailServerObj.name}}</p>
            </div>
        </div>

        <div class="row" ng-if="basicVm.mailServerObject.mailServer != null && basicVm.viewDetails">
            <div class="label col-sm-2">
                <span>Receiver User: </span>
            </div>
            <div class="value col-sm-10">
                <a style="color:#428bca;"
                   ng-if="selectedProject.locked == false && (hasPermission('permission.home.editBasic') || login.person.isProjectOwner)"
                   href="#" editable-text="basicVm.mailServerObject.receiverUser"
                   onaftersave="basicVm.updateObjectMail()">
                    {{basicVm.mailServerObject.receiverUser || 'Click to enter Receiver User'}}</a>

                <p ng-if="selectedProject.locked == false && !(hasPermission('permission.home.editBasic') || login.person.isProjectOwner)">
                    {{basicVm.mailServerObject.receiverUser}}</p>

                <p ng-if="selectedProject.locked == true">{{basicVm.mailServers.receiverUser}}</p>
            </div>
        </div>

        <div class="row" ng-if="basicVm.mailServerObject.mailServer != null && basicVm.viewDetails">
            <div class="label col-sm-2">
                <span>Receiver Email: </span>
            </div>
            <div class="value col-sm-10">
                <a style="color:#428bca;"
                   ng-if="selectedProject.locked == false && (hasPermission('permission.home.editBasic') || login.person.isProjectOwner)"
                   href="#" editable-text="basicVm.mailServerObject.receiverEmail"
                   onaftersave="basicVm.updateObjectMail()">
                    {{basicVm.mailServerObject.receiverEmail || 'Click to enter Receiver Email'}}</a>

                <p ng-if="selectedProject.locked == false && !(hasPermission('permission.home.editBasic') || login.person.isProjectOwner)">
                    {{basicVm.mailServerObject.receiverEmail}}</p>

                <p ng-if="selectedProject.locked == true">{{basicVm.mailServerObject.receiverEmail}}</p>
            </div>
        </div>

        <div class="row" ng-if="basicVm.mailServerObject.mailServer != null && basicVm.viewDetails">
            <div class="label col-sm-2">
                <span>Receiver Password:</span></div>
            <div class="value col-sm-10">

                <a style="color:#428bca;"
                   ng-if="selectedProject.locked == false && (hasPermission('permission.home.editBasic')  || login.person.isProjectOwner)"
                   href="#" editable-text="basicVm.mailServerObject.receiverPassword" e-type="password"
                   onaftersave="basicVm.updateObjectMail()">
                    {{basicVm.mailServerObject.recPassword || 'Click to set Receiver Password'}}</a>

                <p ng-if="selectedProject.locked == false && !(hasPermission('permission.home.editBasic') || login.person.isProjectOwner)">
                    {{basicVm.mailServerObject.recPassword}}</p>

                <p ng-if="selectedProject.locked == true">{{basicVm.mailServerObject.recPassword}}</p>

            </div>
        </div>

        <div class="row" ng-if="basicVm.mailServerObject.mailServer != null && basicVm.viewDetails">
            <div class="label col-sm-2">
                <span>Sender User: </span>
            </div>
            <div class="value col-sm-10">
                <a style="color:#428bca;"
                   ng-if="selectedProject.locked == false && (hasPermission('permission.home.editBasic') || login.person.isProjectOwner)"
                   href="#" editable-text="basicVm.mailServerObject.senderUser"
                   onaftersave="basicVm.updateObjectMail()">
                    {{basicVm.mailServerObject.senderUser || 'Click to enter Sender User'}}</a>

                <p ng-if="selectedProject.locked == false && !(hasPermission('permission.home.editBasic') || login.person.isProjectOwner)">
                    {{basicVm.mailServerObject.senderUser}}</p>

                <p ng-if="selectedProject.locked == true">{{basicVm.mailServerObject.senderUser}}</p>
            </div>
        </div>

        <div class="row" ng-if="basicVm.mailServerObject.mailServer != null && basicVm.viewDetails">
            <div class="label col-sm-2">
                <span>Sender Email: </span>
            </div>
            <div class="value col-sm-10">
                <a style="color:#428bca;"
                   ng-if="selectedProject.locked == false && (hasPermission('permission.home.editBasic') || login.person.isProjectOwner)"
                   href="#" editable-text="basicVm.mailServerObject.senderEmail"
                   onaftersave="basicVm.updateObjectMail()">
                    {{basicVm.mailServerObject.senderEmail || 'Click to enter Sender Email'}}</a>

                <p ng-if="selectedProject.locked == false && !(hasPermission('permission.home.editBasic') || login.person.isProjectOwner)">
                    {{basicVm.mailServerObject.senderEmail}}</p>

                <p ng-if="selectedProject.locked == true">{{basicVm.mailServerObject.senderEmail}}</p>
            </div>
        </div>


        <div class="row"
             ng-if="basicVm.mailServerObject.mailServer != null && basicVm.viewDetails && selectedProject.locked == false && (hasPermission('permission.home.editBasic') || login.person.isProjectOwner)">
            <div class="label col-sm-2">
                <span>Sender Password :</span></div>
            <div class="value col-sm-10">

                <a style="color:#428bca;"
                   ng-if="selectedProject.locked == false && (hasPermission('permission.home.editBasic')  || login.person.isProjectOwner)"
                   href="#" editable-text="basicVm.mailServerObject.senderPassword" e-type="password"
                   onaftersave="basicVm.updateObjectMail()">
                    {{basicVm.mailServerObject.senPassword || 'Click to set Sender Password'}}</a>

                <p ng-if="selectedProject.locked == false && !(hasPermission('permission.home.editBasic') || login.person.isProjectOwner)">
                    {{basicVm.mailServerObject.senPassword}}</p>

                <p ng-if="selectedProject.locked == true">{{basicVm.mailServerObject.senPassword}}</p>

            </div>
        </div>

        <div ng-if="basicVm.mailServerObject.mailServer == null && basicVm.viewDetails && selectedProject.locked == false && (hasPermission('permission.home.editBasic') || login.person.isProjectOwner)"
             style="padding: 30px;;">
            <form class="form-horizontal">
                <div class="form-group">
                    <label class="col-sm-2 control-label" style="bottom: 18px;">Mail Server :</label>

                    <div class="col-sm-4">
                        <ui-select ng-model="basicVm.mailServerObject.mailServerObj" theme="bootstrap"
                                   style="width:100%; bottom: 15px">
                            <ui-select-match placeholder="Select MailServer">{{$select.selected.name}}</ui-select-match>
                            <ui-select-choices repeat="mailServer in basicVm.mailServers | filter: $select.name.search">
                                <div ng-bind="mailServer.name | highlight: $select.name.search"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                </div>

                <div ng-if="basicVm.mailServerObject.mailServerObj != null">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">Receiver User :<span class="asterisk">*</span></label>

                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="receiverUser"
                                   ng-model="basicVm.mailServerObject.receiverUser">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label">Receiver Email :<span
                                class="asterisk">*</span></label>

                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="receiverEmail"
                                   ng-model="basicVm.mailServerObject.receiverEmail">
                        </div>
                    </div>


                    <div class="form-group">
                        <label class="col-sm-2 control-label">Receiver Password :<span
                                class="asterisk">*</span></label>

                        <div class="col-sm-4">
                            <input type="password" class="form-control" name="receiverPassword" id="receiverPassword"
                                   ng-model="basicVm.mailServerObject.receiverPassword">
                                <span id="showPassword" class="fa fa-fw fa-eye"
                                      ng-click="basicVm.showReceiverPassword()"
                                      style="float: right;z-index: 2;margin-top: -28px;position: relative;cursor: pointer;
                              font-size: 18px;margin-right: 5px;color: black;"></span>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label">Sender User :<span class="asterisk">*</span></label>

                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="senderUser"
                                   ng-model="basicVm.mailServerObject.senderUser">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="mail">Sender Email :<span
                                class="asterisk">*</span></label>

                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="senderEmail" id="mail"
                                   ng-model="basicVm.mailServerObject.senderEmail">
                        </div>
                    </div>


                    <div class="form-group">
                        <label class="col-sm-2 control-label">Sender Password :<span
                                class="asterisk">*</span></label>

                        <div class="col-sm-4">
                            <input type="password" class="form-control" name="senderPassword" id="senderPassword"
                                   ng-model="basicVm.mailServerObject.senderPassword">
                                <span id="showPassword1" class="fa fa-fw fa-eye" ng-click="basicVm.showSenderPassword()"
                                      style="float: right;z-index: 2;margin-top: -28px;position: relative;cursor: pointer;
                              font-size: 18px;margin-right: 5px;color: black;"></span>
                        </div>
                    </div>
                </div>
            </form>

        </div>

        <attributes-details-view attribute-id="selectedProject.id" attribute-type="PROJECT"
                                 has-permission="(hasPermission('permission.home.editBasic') || login.person.isProjectOwner)"></attributes-details-view>

    </div>
</div>
 