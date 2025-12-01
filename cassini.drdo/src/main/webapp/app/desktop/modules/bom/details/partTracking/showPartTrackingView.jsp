<%--
  Created by IntelliJ IDEA.
  User: Nageshreddy
  Date: 04-01-2018
  Time: 17:10
  To change this template use File | Settings | File Templates.
--%>


<style scoped>
    .timeline1 {
        list-style: none;
        padding: 20px 0 20px;
        position: relative;
    }

    /*.timeline1:before {
        top: 3%;
        bottom: 50px;
        position: absolute;
        content: " ";
        width: 10px;
        background-color: #eeeeee;
        left: 9%;
        margin-left: -1.5px;
    }*/

    .timeline1 > li {
        margin-bottom: 20px;
        position: relative;
    }

    .timeline1 > li:before,
    .timeline1 > li:after {
        content: " ";
        display: table;
    }

    .timeline1 > li:after {
        clear: both;
    }

    .timeline1 > li:before,
    .timeline1 > li:after {
        content: " ";
        display: table;
    }

    .timeline1 > li:after {
        clear: both;
    }

    .timeline1 > li > .timeline1-panel-left {
        width: 23%;
        float: left;
        float: left;
        border: 1px solid #d4d4d4;
        border-radius: 2px;
        padding: 20px;
        position: relative;
        -webkit-box-shadow: 0 1px 6px rgba(0, 0, 0, 0.175);
        margin-left: 290px;
    }

    .timeline1 > li > .timeline1-panel-right {
        width: 23%;
        float: left;
        border: 1px solid #d4d4d4;
        border-radius: 2px;
        padding: 20px;
        position: relative;
        -webkit-box-shadow: 0 1px 15px rgba(0, 0, 0, 0.175);
        margin-left: 20px;
    }

    .timeline1 > li > .box-shadow-blue {
        box-shadow: 10px 10px 10px rgba(36, 59, 117, 0.9);
    }

    .timeline1 > li > .box-shadow-green {
        box-shadow: 10px 10px 10px rgba(23, 127, 30, 0.9);
    }

    .timeline1 > li > .box-shadow-danger {
        box-shadow: 10px 10px 10px rgba(149, 27, 29, 0.9);
    }

    /*.timeline1 > li > .timeline1-panel-start-left {
        width: 25%;
        float: left;
        text-align: center;
        border-radius: 2px;
        padding: 20px;
        position: relative;
        margin-left: 290px;
    }

    .timeline1 > li > .timeline1-panel-start-center {
        width: 25%;
        float: left;
        text-align: center;
        border-radius: 2px;
        padding: 20px;
        position: relative;
        margin-left: 45px;
    }

    .timeline1 > li > .timeline1-panel-start-right {
        width: 25%;
        float: right;
        text-align: center;
        border-radius: 2px;
        padding: 20px;
        position: relative;
    }*/

    .timeline1 > li.timeline1-inverted + li:not(.timeline1-inverted),
    .timeline1 > li:not(.timeline1-inverted) + li.timeline1-inverted {
        margin-top: -60px;
    }

    .timeline1 > li:not(.timeline1-inverted) {
        padding-right: 90px;
    }

    .timeline1 > li.timeline1-inverted {
        padding-left: 90px;
    }

    .timeline1 > li > .timeline1-panel:before {
        position: absolute;
        top: 2%;
        right: -15px;
        display: inline-block;
        border-top: 15px solid transparent;
        border-left: 15px solid #ccc;
        border-right: 0 solid #ccc;
        border-bottom: 15px solid transparent;
        content: " ";
    }

    .timeline1 > li > .timeline1-panel:after {
        position: absolute;
        top: 27px;
        right: -14px;
        display: inline-block;
        border-top: 14px solid transparent;
        border-left: 14px solid #fff;
        border-right: 0 solid #fff;
        border-bottom: 14px solid transparent;
        content: " ";
    }

    .timeline1 > li > .timeline1-badge {
        color: #140f86;
        width: 250px;
        min-height: 50px;
        line-height: 22px;
        font-size: 16px;
        text-align: center;
        position: absolute;
        top: 16px;
        left: 3%;
        margin-left: -25px;
        background-color: rgba(195, 234, 228, 0.79);
        z-index: 300;
        box-shadow: 10px 10px 10px rgba(164, 53, 138, 0.9);
        padding-top: 10px;
        border-top-right-radius: 20%;
        /*border-top-left-radius: 20%;*/
        /*border-bottom-right-radius: 20%;*/
        border-bottom-left-radius: 20%;
    }

    .timeline1 > li > .timeline1-startend-badge {
        color: #140f86;
        width: 100px;
        height: 50px;
        line-height: 50px;
        font-size: 1.4em;
        text-align: center;
        position: relative;
        top: -15px;
        left: 7%;
        margin-left: -15px;
        background-color: rgba(195, 234, 228, 0.79);
        box-shadow: 10px 10px 10px #f43f2a;
        z-index: 300;
        border-top-right-radius: 50%;
        border-top-left-radius: 50%;
        border-bottom-right-radius: 50%;
        border-bottom-left-radius: 50%;
    }

    .timeline1 > li.timeline1-inverted > .timeline1-panel {
        float: right;
    }

    .timeline1 > li.timeline1-inverted > .timeline1-panel:before {
        border-left-width: 0;
        border-right-width: 15px;
        left: -15px;
        right: auto;
    }

    .timeline1 > li.timeline1-inverted > .timeline1-panel:after {
        border-left-width: 0;
        border-right-width: 14px;
        left: -14px;
        right: auto;
    }

    .timeline1-badge.primary {
        background-color: #2e6da4 !important;
    }

    .timeline1-badge.success {
        background-color: #3f903f !important;
    }

    .timeline1-badge.warning {
        background-color: #f0ad4e !important;
    }

    .timeline1-badge.danger {
        background-color: #d9534f !important;
    }

    .timeline1-badge.info {
        background-color: #5bc0de !important;
    }

    .timeline1-title {
        margin-top: 0;
        color: inherit;
    }

    .timeline1-body > p,
    .timeline1-body > ul {
        margin-bottom: 0;
    }

    .timeline1-body > p + p {
        margin-top: 5px;
    }

    .disabled {
        opacity: 0.5;
        cursor: not-allowed;
        pointer-events: none;
    }

    #td {
        display: run-in;
        word-wrap: break-word;
        width: 300px;
        white-space: normal;
        text-align: center;
    }

    .table thead > tr > th {
        background-color: #fff;
        color: #000000;
    }

    .result-hover:hover {
        font-size: 30px !important;
    }

    button.btn-scan {
        margin-top: 3px;
        margin-left: 165px;
        height: 30px;
        width: 85px;
        float: right;
    }

    #orangeBox {
        background: #f90;
        color: #fff;
        font-family: 'Helvetica', 'Arial', sans-serif;
        font-size: 2em;
        font-weight: bold;
        text-align: center;
        width: 40px;
        height: 40px;
        border-radius: 5px;
    }

    #spa {
        content: "\2713";
    }

    .checkmark {
        font-family: arial;
        -ms-transform: scaleX(-1) rotate(-35deg); /* IE 9 */
        -webkit-transform: scaleX(-1) rotate(-35deg); /* Chrome, Safari, Opera */
        transform: scaleX(-1) rotate(-35deg);
    }
</style>
<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <button class="btn btn-sm btn-success backButton" ng-click="showPartVm.back();">Back</button>
        <%--<button class="btn btn-sm btn-primary min-width" ng-click="showPartVm.generateReport();">Report</button>--%>
        <i class="fa fa-file-pdf-o" ng-click="showPartVm.printPartTracking()" title="Pdf Print"
           style="font-size:23px;color: green;cursor: pointer;"></i>
        <span style="color: green;font-size: 18px;font-weight: bolder;"
              ng-if="showPartVm.countChecks > 0 && showPartVm.countChecks == showPartVm.checkedCount">Part Tracking Completed</span>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">

        <ul class="timeline1">
            <li>
                <div class="timeline1-startend-badge">
                    <span>Start</span>
                </div>
                <%--<div class="timeline1-panel-start-left">
                    <div class="timeline1-heading">
                        <strong style="font-size:20px">BDL</strong>
                    </div>
                </div>
                <div class="timeline1-panel-start-center">
                    <div class="timeline1-heading">
                        <strong  style="font-size:20px">SSQAG</strong>
                    </div>
                </div>
                <div class="timeline1-panel-start-right">
                    <div class="timeline1-heading">
                        <strong  style="font-size:20px">CAS</strong>
                    </div>

                </div>--%>
            </li>
            <li ng-repeat="partTracking in showPartVm.partTrackings"
                ng-init="showPartVm.listSize = showPartVm.listSize+1"
                ng-if="partTracking.previousCompleted">
                <div class="timeline1-badge"><i class="glyphicon glyphicon-check"></i>
                    <span>{{partTracking.partTrackingStepObject.description}}</span></div>

                <div class="timeline1-panel-left"
                     ng-if="partTracking.partTrackingStepObject.bdl<%-- && hasPermission('permission.bdl.check')--%>"
                     ng-class="{'box-shadow-blue': partTracking.bdl.checked,
                                'disabled':(partTracking.reject || !hasPermission('permission.bdl.check')),
                                'box-shadow-green': !partTracking.bdl.checked}">
                    <div class="timeline1-heading">
                        <i title="Accepted" ng-if="partTracking.bdl.checked"
                           class="glyphicon glyphicon-check result-hover"
                           style="font-size:20px;color:blue"></i>
                        <a href="" ng-if="partTracking.bdl.checked == null"
                           ng-click="showPartVm.check(partTracking, 'BDL', true,'save');">
                            <i title="Check" class="glyphicon glyphicon-unchecked result-hover"
                               style="font-size:20px;color:green"></i></a>
                        <a href="" ng-if="partTracking.bdl.checked && !partTracking.bdl.edit"
                           ng-click="showPartVm.editPartTracking(partTracking,'BDL');">
                            <i title="Edit" class="glyphicon glyphicon-edit result-hover"
                               style="font-size:20px;color:#ab4f6c"></i>
                        </a>
                        <a href="" ng-if="partTracking.bdl.edit"
                           ng-click="showPartVm.updatePartTracking(partTracking,'BDL')">
                            <i title="Update" class="glyphicon glyphicon-floppy-disk result-hover"
                               style="font-size:20px;color:#1fb527"></i>

                        </a>
                        <a href="" ng-if="partTracking.bdl.edit"
                           ng-click="partTracking.bdl.newComment = partTracking.bdl.comment;partTracking.bdl.edit = false">
                            <i title="Cancel edit" class="glyphicon glyphicon-remove result-hover"
                               style="font-size:20px;color:#f43f2a"></i>

                        </a>
                        <strong style="font-size:18px">BDL</strong>
                        <span class="pull-right" ng-if="partTracking.bdl.checked">
                            <strong>Checked By:</strong>{{partTracking.bdl.createdByObject.firstName}}<br>
                            <strong>Checked Date:</strong>{{partTracking.bdl.modifiedDate}}</span>
                    </div>
                    <div class="timeline1-body">
                        <form>
                            <input ng-if="(partTracking.bdl.checked == null || partTracking.bdl.edit) && partTracking.partTrackingStepObject.attachment"
                                   style="width: 230px"
                                   class="form-control" type="file"
                                   ng-file-model="partTracking.bdl.attachmentObject"/>
                            <a href="" ng-if="!partTracking.bdl.edit"
                               ng-click="showPartVm.openAttachment(partTracking.bdl.attachmentObject)"
                               title="Click to view or download file"
                               style="margin-bottom: 5px;width:250px;color: #002451;">
                                {{partTracking.bdl.attachmentObject.name}}
                            </a>
                            <br>
                            <input ng-if="partTracking.bdl.checked == null || partTracking.bdl.edit"
                                   placeholder="Enter comment"
                                   style="width: 230px" class="form-control"
                                   type="textarea" cols="200" rows="5"
                                   ng-model="partTracking.bdl.newComment"/>
                            <span ng-if="!partTracking.bdl.edit && partTracking.bdl.checked == true">{{partTracking.bdl.newComment}}</span>
                            <button class="btn btn-sm btn-success btn-scan"
                                    ng-if="(partTracking.bdl.checked == null || partTracking.bdl.edit) && partTracking.partTrackingStepObject.scan &&
                                    (partTracking.bdl.scan == undefined || !partTracking.bdl.scan)"
                                    ng-click="showPartVm.scanItems(partTracking)">Scan
                            </button>
                            <button class="btn btn-sm btn-info btn-scan"
                                    ng-click="showPartVm.showReScanItems(partTracking)" title="Re Scan UPNs"
                                    ng-if="<%--!partTracking.bdl.edit && partTracking.bdl.checked == true && --%>partTracking.bdl.edit &&partTracking.bdl.scan">
                                Scanned
                            </button>
                            <button class="btn btn-sm btn-info btn-scan"
                                    ng-click="showPartVm.showScanItems(partTracking)"
                                    title="View Scanned UPNs"
                                    ng-if="<%--!partTracking.bdl.edit && partTracking.bdl.checked == true && --%>!partTracking.bdl.edit && partTracking.bdl.scan">
                                Scanned
                            </button>
                        </form>
                    </div>
                </div>
                <div ng-if="partTracking.partTrackingStepObject.ssqag" class="timeline1-panel-right"
                     ng-class="{'box-shadow-green': (partTracking.bdl.checked && !partTracking.ssqag.checked),
                                'box-shadow-blue': partTracking.ssqag.checked,
                                'box-shadow-danger': (partTracking.bdl.checked == null),
                                'disabled':(partTracking.bdl.checked == null || partTracking.reject || !hasPermission('permission.ssqag.check'))}">
                    <div class="timeline1-heading">
                        <i title="Accepted" ng-if="partTracking.ssqag.checked  && !partTracking.ssqag.edit"
                           class="glyphicon glyphicon-check result-hover"
                           style="font-size:20px;color:blue"></i>
                        <i title="On Hold" ng-if="partTracking.ssqag.checked == false && !partTracking.ssqag.edit"
                           class="glyphicon glyphicon-ban-circle result-hover"
                           style="font-size:20px;color:red"></i>
                        <a href=""
                           ng-if="partTracking.ssqag.checked == null || (partTracking.ssqag.checked == false && partTracking.ssqag.edit)"
                           ng-click="showPartVm.check(partTracking, 'SSQAG', true, 'save');">
                            <i title="Check" class="glyphicon glyphicon-unchecked result-hover"
                               style="font-size:20px;color:green"></i></a>
                        <a href=""
                           ng-if="partTracking.ssqag.checked == null || (partTracking.ssqag.checked && partTracking.ssqag.edit)"
                           ng-click="showPartVm.check(partTracking, 'SSQAG', false, 'save');">
                            <i title="On Hold" class="glyphicon glyphicon-ban-circle result-hover"
                               style="font-size:20px;color:#aa2814"></i></a>
                        <a href="" ng-if="partTracking.ssqag.checked != null && !partTracking.ssqag.edit"
                           ng-click="showPartVm.editPartTracking(partTracking,'SSQAG');">
                            <i title="Edit" class="glyphicon glyphicon-edit result-hover"
                               style="font-size:20px;color:#ab4f6c"></i>
                        </a>
                        <a href="" ng-if="partTracking.ssqag.edit"
                           ng-click="showPartVm.updatePartTracking(partTracking,'SSQAG')">
                            <i title="Update" class="glyphicon glyphicon-floppy-disk result-hover"
                               style="font-size:20px;color:#1fb527"></i>
                        </a>
                        <a href="" ng-if="partTracking.ssqag.edit"
                           ng-click="partTracking.ssqag.newComment = partTracking.ssqag.comment;partTracking.ssqag.edit = false">
                            <i title="Cancel edit" class="glyphicon glyphicon-remove result-hover"
                               style="font-size:20px;color:#f43f2a"></i>

                        </a>
                        <strong style="font-size:18px">SSQAG</strong>
                        <span class="pull-right"
                              ng-if="partTracking.ssqag.checked != undefined && partTracking.ssqag.checked">
                            <strong>Checked By:</strong>{{partTracking.ssqag.createdByObject.firstName}}
                        <br>
                            <strong>Checked Date:</strong>{{partTracking.ssqag.modifiedDate}}</span>
                        <span class="pull-right"
                              ng-if="partTracking.ssqag.checked != undefined && !partTracking.ssqag.checked">
                            <strong>On Holded By:</strong>{{partTracking.ssqag.createdByObject.firstName}}
                        <br>
                            <strong>On Holded Date:</strong>{{partTracking.ssqag.modifiedDate}}</span>
                    </div>
                    <div class="timeline1-body">
                        <form>
                            <%--<input ng-if="(partTracking.ssqag.checked == null || partTracking.ssqag.edit) && partTracking.partTrackingStepObject.attachment"
                                   style="width: 230px"
                                   class="form-control" type="file"
                                   ng-file-model="partTracking.ssqag.attachmentObject"/>
                            <a href="" ng-if="!partTracking.ssqag.edit"
                               ng-click="showPartVm.openAttachment(partTracking.ssqag.attachmentObject)"
                               title="Click to view or download file"
                               style="margin-bottom: 5px;width:250px;color: #002451;">
                                {{partTracking.ssqag.attachmentObject.name}}
                            </a>
                            <br>--%>
                            <input ng-if="partTracking.ssqag.checked == null || partTracking.ssqag.edit"
                                   placeholder="Enter comment"
                                   style="width: 230px" class="form-control"
                                   type="textarea" cols="200" rows="5"
                                   ng-model="partTracking.ssqag.newComment"/>
                            <span ng-if="!partTracking.ssqag.edit && partTracking.ssqag.checked != null"">{{partTracking.ssqag.newComment}}</span>
                            <%--<button class="btn btn-sm btn-success btn-scan"
                                    ng-if="(partTracking.ssqag.checked == null || partTracking.ssqag.edit) && partTracking.partTrackingStepObject.scan"
                                    ng-click="showPartVm.scanItems()">Scan
                            </button>--%>
                        </form>
                    </div>
                </div>
                <div <%--class="timeline1-panel-right"--%>
                        ng-if="partTracking.partTrackingStepObject.cas" class="timeline1-panel-right"
                        ng-class="{'box-shadow-green': (partTracking.bdl.checked && !partTracking.cas.checked),
                                'box-shadow-blue': partTracking.cas.checked,
                                'box-shadow-danger': (partTracking.bdl.checked == null),
                                'disabled':((partTracking.partTrackingStepObject.ssqag && partTracking.ssqag.checked == null) || partTracking.bdl.checked == null || partTracking.reject || !hasPermission('permission.cas.check'))}">
                    <div class="timeline1-heading">
                        <i title="Accepted" ng-if="partTracking.cas.checked && !partTracking.cas.edit"
                           class="glyphicon glyphicon-check result-hover"
                           style="font-size:20px;color:blue"></i>
                        <i title="On Holded" ng-if="partTracking.cas.checked == false && !partTracking.cas.edit"
                           class="glyphicon glyphicon-ban-circle result-hover"
                           style="font-size:20px;color:red"></i>
                        <a href=""
                           ng-if="partTracking.cas.checked == null || (partTracking.cas.checked == false && partTracking.cas.edit)"
                           ng-click="showPartVm.check(partTracking, 'CAS', true,'save');">
                            <i title="Check" class="glyphicon glyphicon-unchecked result-hover"
                               style="font-size:20px;color:green"></i></a>
                        <a href=""
                           ng-if="partTracking.cas.checked == null || (partTracking.cas.checked && partTracking.cas.edit)"
                           ng-click="showPartVm.check(partTracking, 'CAS', false,'save');">
                            <i title="On Hold" class="glyphicon glyphicon-ban-circle result-hover"
                               style="font-size:20px;color:#ab151a"></i></a>
                        <a href="" ng-if="partTracking.cas.checked != null && !partTracking.cas.edit"
                           ng-click="showPartVm.editPartTracking(partTracking,'CAS');">
                            <i title="Edit" class="glyphicon glyphicon-edit result-hover"
                               style="font-size:20px;color:#ab4f6c"></i>
                        </a>
                        <a href="" ng-if="partTracking.cas.edit"
                           ng-click="showPartVm.updatePartTracking(partTracking,'CAS')">
                            <i title="Update" class="glyphicon glyphicon-floppy-disk result-hover"
                               style="font-size:20px;color:#1fb527"></i>
                        </a>
                        <a href="" ng-if="partTracking.cas.edit"
                           ng-click="partTracking.cas.newComment = partTracking.cas.comment;partTracking.cas.edit = false">
                            <i title="Cancel edit" class="glyphicon glyphicon-remove result-hover"
                               style="font-size:20px;color:#f43f2a"></i>
                        </a>
                        <strong style="font-size:18px">CAS</strong>
                        <span class="pull-right"
                              ng-if="partTracking.cas.checked != undefined && partTracking.cas.checked">
                            <strong>Checked By:</strong>{{partTracking.cas.createdByObject.firstName}}
                        <br>
                        <strong>Checked Date:</strong>{{partTracking.cas.modifiedDate}}</span>
                        <span class="pull-right"
                              ng-if="partTracking.cas.checked != undefined && !partTracking.cas.checked">
                            <strong>On Holded By:</strong>{{partTracking.cas.createdByObject.firstName}}
                        <br>
                        <strong>On Holded Date:</strong>{{partTracking.cas.modifiedDate}}</span>

                    </div>
                    <div class="timeline1-body">
                        <form>
                            <%--<input ng-if="(partTracking.cas.checked == null || partTracking.cas.edit) && partTracking.partTrackingStepObject.attachment"
                                   style="width: 230px"
                                   class="form-control" type="file"
                                   ng-file-model="partTracking.cas.attachmentObject"/>
                            <a href="" ng-if="!partTracking.cas.edit"
                               ng-click="showPartVm.openAttachment(partTracking.cas.attachmentObject)"
                               title="Click to view or download file"
                               style="margin-bottom: 5px;width:250px;color: #002451;">
                                {{partTracking.cas.attachmentObject.name}}
                            </a>
                            <br>--%>
                            <input ng-if="partTracking.cas.checked == null || partTracking.cas.edit"
                                   placeholder="Enter comment"
                                   style="width: 230px" class="form-control"
                                   type="textarea" cols="200" rows="5"
                                   ng-model="partTracking.cas.newComment"/>
                            <span ng-if="!partTracking.cas.edit && partTracking.cas.checked != null">{{partTracking.cas.newComment}}</span>
                            <%--<button class="btn btn-sm btn-success btn-scan"
                                    ng-if="(partTracking.cas.checked == null || partTracking.cas.edit) && partTracking.partTrackingStepObject.scan"
                                    ng-click="showPartVm.scanItems()">Scan
                            </button>--%>
                        </form>
                    </div>
                </div>
            </li>
            <li ng-show="showPartVm.listSize == showPartVm.partTrackings.length">
                <div class="timeline1-startend-badge">
                    <span>Finish</span>
                </div>
            </li>
        </ul>
    </div>
</div>


<div>
    <%--<div id="firstTable">
        <table style="margin-left:10px;margin-right:10px" id="checkListPdf">
            <thead>
            <tr>
                <th>System</th>
                <th>Missile</th>
                <th>Section Name</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>{{showPartVm.parentOfMissileForPdf}}</td>
                <td>{{showPartVm.missileNumberForPdf}}</td>
                <td>{{showPartVm.sectionForPdf}}</td>
            </tr>
            </tbody>
        </table>
    </div>--%>


    <div id="firstTable">
        <table style="margin-left:10px;margin-right:10px" id="checkListPdf1">
            <thead>
            <tr>
                <th>S.No.</th>
                <th>Status</th>
                <th>comments</th>
                <th>Attachments</th>
                <th>BDL</th>
                <th>SSQAG</th>
                <th>CAS</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-repeat="value in showPartVm.partTrackings">
                <td>{{value.partTrackingStepObject.serialNo}}</td>
                <td>{{value.partTrackingStepObject.status}}</td>
                <td><span ng-if="value.bdl.checked == true"
                          style="color: green">{{value.bdl.comment}}</span><span ng-show="value.bdl.comment != null"> by BDL</span><span
                        style="color: #000088"
                        ng-if="value.ssqag.checked == true"><br/>{{value.ssqag.comment}}</span>
                    <span ng-show="value.ssqag.comment != null"> by SSQAG</span><span
                            style="color: purple"
                            ng-if="value.cas.checked == true"><br/>{{value.cas.comment}}</span>
                    <span ng-show="value.cas.comment != null"> by CAS</span></td>
                <td>{{value.bdl.attachmentObject.name}}</td>
                <td>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <span ng-if="value.bdl.checked ==true">
                Approved</span>

                    <div ng-if="value.bdl.checked ==false" id="orangeBox">
                        <span id="x">X</span>
                    </div>

                </td>
                <td>
                <span ng-if="value.ssqag.checked ==true" class="checkmark">
                     Approved
                </span>

                    <div ng-if="value.ssqag.checked ==false" id="orangeBox">
                        <span id="x">X</span>
                    </div>
                </td>
                <td>
                <span ng-if="value.cas.checked ==true" class="checkmark">
                     Approved
                </span>

                    <div ng-if="value.cas.checked ==false" id="orangeBox">
                        <span id="x">X</span>
                    </div>
                </td>
                <%--<td >&lt;%&ndash;{{value.bdl.checked}}&ndash;%&gt;<img ng-show="value.bdl.checked == false" src="app/assets/images/admin.png"><img ng-show="value.bdl.checked == true" src="app/assets/images/favicon.png"></td>
                <td >&lt;%&ndash;{{value.ssqag.checked}}&ndash;%&gt;<img ng-show="value.ssqag.checked == false" src="app/assets/images/admin.png"><img ng-show="value.ssqag.checked == true" src="app/assets/images/favicon.png"></td>
                <td >&lt;%&ndash;{{value.cas.checked}}&ndash;%&gt;<img ng-show="value.cas.checked == false" src="app/assets/images/admin.png"><img ng-show="value.cas.checked == true" src="app/assets/images/favicon.png"></td>
--%>


            </tr>
            </tbody>
        </table>
    </div>


    <%--<table style="margin-left:10px;margin-right:10px" id="checkListBomPdf">
        <thead>
        <tr>

            <th>NomenClature</th>
            <th>BOM Qty</th>
            <th>Units</th>
            <th>Part Number</th>
            &lt;%&ndash; <th>Part/LotNumber</th>&ndash;%&gt;
            &lt;%&ndash;<th>Serial Number</th>&ndash;%&gt;
        </tr>
        </thead>
        <tbody>
        <tr ng-repeat="item in showPartVm.items">

            <td>{{item.item.itemName}}</td>
            <td>{{item.quantity}}</td>
            <td>{{item.item.units}}</td>
            <td style="text-align: center">
                <div>
                    <div>
                        <ul>
                            <li ng-repeat="partNumber in item.batchItemsIssued"
                                style="color: black;">
                                {{partNumber.upnNumber}}
                            </li>

                            <li ng-repeat="lotNumber in item.imLotItemsIssueds"
                                style="color: black;">
                                {{lotNumber.upnNumber}}
                            </li>


                        </ul>
                    </div>
                </div>

            </td>
            &lt;%&ndash;<td>
                <div>
                    <div>
                        <ul>
                            <li ng-repeat="partNumber in item.batchItemsIssued"
                                style="color: black;">
                                {{partNumber.oemNumber}}
                            </li>
                        </ul>
                    </div>
                </div>
                <span ng-if="item.numbers.length == 0">{{'--'}}</span>
            </td>&ndash;%&gt;
        </tr>
        </tbody>
    </table>--%>
</div>