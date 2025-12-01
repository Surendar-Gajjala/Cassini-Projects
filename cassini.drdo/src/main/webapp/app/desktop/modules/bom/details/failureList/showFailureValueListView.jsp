<%--
  Created by IntelliJ IDEA.
  User: Nageshreddy
  Date: 02-01-2019
  Time: 18:30
  To change this template use File | Settings | File Templates.
--%>
<style>
    th {
        width: 350px;
        text-align: left;
        font-size: 16px;
    }

    tr {
        height: 50px;
    }

    .approveform {
        border: 3px solid #cc289c;
        margin: 15px;
    }
</style>
<html>
<head>
    <title></title>
</head>
<body>
<div>
    <table style="margin-left: 10px;">
        <thead>
        <tr>
            <th style="width:60px !important;">S.No.</th>
            <th>Status</th>
            <th>Details</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-repeat="step in failureValueListVm.failureValueList2 track by $index">
            <td style="text-align: center">{{step.sno}}</td>
            <td>{{step.status}}</td>
            <td>
                <a href="#" ng-if="step.dataType == 'LIST' && step.status == 'Observation Major/ Minor'"
                   editable-select="step.value" e-style="width:150px;"
                   e-ng-options="bug as bug for bug in failureValueListVm.bugs">{{step.value || 'Click here to enter
                    value'}}</a>
                <a href="#" ng-if="step.dataType == 'LIST' && step.status == 'Activity'" editable-select="step.value"
                   e-style="width:150px;"
                   e-ng-options="activity as activity for activity in failureValueListVm.activities">{{step.value ||
                    'Click here to enter
                    value'}}</a>
                <span ng-if="step.dataType == 'DATE'">{{step.value}}</span>
                <a href="#" ng-if="step.dataType == 'STRING'"
                   editable-text="step.value">{{step.value || 'Click here to enter value'}}</a>
                <a href="#" ng-if="step.dataType == 'TEXT'" e-style="width:200px;"
                   editable-textarea="step.value">{{step.value || 'Click here to enter value'}}</a>

                <span ng-if="step.dataType == 'FILES'">
                    <input class="form-control" name="file" multiple="true"
                           ng-if="(step.value == '' || step.value == null || step.value == undefined)"
                           type="file" ng-file-model="failureValueListVm.newAttachments"/>
                    <a ng-repeat="file in failureValueListVm.attachments"
                       ng-click="failureValueListVm.downloadFile(file)">
                        {{file.name}}<br>
                    </a>
                </span>
            </td>
        </tr>
        </tbody>
    </table>
</div>
<br>
<br>

<div>
    <span style="font-size: 20px;margin-left: 10px"><strong>Approvals</strong></span>
</div>
<br>
<br>

<div class="col-md-2 approveform" ng-repeat="step in failureValueListVm.failureValueList1 track by $index"
<%--ng-init="step0 = ($index > 0 ? failureValueListVm.failureValueList1[$index-1] : null)"--%>>
    <a href="" ng-if="step.value == false && ((step.status == 'BDL' && hasPermission('permission.bdl.failure'))
    || (step.status == 'SSQAG' && hasPermission('permission.ssqag.failure')) || (step.status == 'CAS' && hasPermission('permission.cas.failure'))
    || (step.status == 'STORES' && hasPermission('permission.store.failure')))"
       ng-click="failureValueListVm.check(step);step.value = true;">
        <p><i title="Check" <%--ng-enabled="step0.value"--%> class="glyphicon glyphicon-unchecked result-hover"
              style="font-size:20px;color:green"></i></a>
    <i title="Accepted" ng-if="step.value"
       class="glyphicon glyphicon-check result-hover"
       style="font-size:20px;color:blue"></i><span><strong>{{step.status+" "}}</strong></span></p>
    <span ng-if="step.value">{{step.checkedDate}},</span>
    <span ng-if="step.value">{{step.checkedByObject.firstName}}</span>
</div>

<div id="failureListPdf" style="display: none">
    <table style="margin-left: 10px;" id="failureTablepdf">
        <thead>
        <tr>
            <th>S/No</th>
            <th>Description</th>
            <th>Details</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-repeat="step in failureValueListVm.failureValueList2 track by $index">
            <td>{{$index + 1}}</td>
            <td>{{step.status}}</td>
            <td>
                <span ng-if="step.sno != 10">{{step.value}}</span>
                <span ng-if="step.sno == 10">
                    <a ng-repeat="file in failureValueListVm.attachments"
                       ng-click="failureValueListVm.downloadFile(file)">
                        {{file.name}}<br>
                    </a>
                </span>
            </td>
        </tr>
        </tbody>
    </table>
    <table style="margin-left: 10px;" id="failureTablepdfForSection">
        <thead>
        <tr>
            <th>System</th>
            <th>Missile</th>
            <th>Item Name</th>
            <th>UPN</th>
            <th>Serial Number</th>
            <th>Certificate Number</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>{{failureValueListVm.systemForPdf}}</td>
            <td>{{failureValueListVm.missileForPdf}}</td>
            <td>{{failureValueListVm.partItemName}}</td>
            <td>{{failureValueListVm.upn}}</td>
            <td>{{failureValueListVm.serialNo}}</td>
            <td>{{failureValueListVm.certificateNo}}</td>
        </tr>
        </tbody>
    </table>
</div>
</body>
</html>