<%--
  Created by IntelliJ IDEA.
  User: Nageshreddy
  Date: 02-01-2019
  Time: 18:54
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
        border: 3px solid #cc54ad;
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
        <tr ng-repeat="step in failureListVm.failureValueList2 track by $index">
            <td style="text-align: center">{{step.sno}}</td>
            <td>{{step.status}}</td>
            <td><input ng-if="step.dataType == 'DATE'" style="width: 330px;"
                       class="form-control" ng-model="step.value"
                       placeholder="dd-mm-yyyy" type="text" date-picker/>
                <select ng-if="step.dataType == 'LIST' && step.status == 'Observation Major/ Minor'"
                        style="width: 330px;height: 40px"
                        data-placement="Select"
                        ng-model="step.value" ng-options="bug for bug in failureListVm.bugs">
                    <option>{{bug}}</option>
                </select>
                <input type="file" multiple="true" class="form-control" style="width: 95%;padding: 5px;"
                       ng-if="step.dataType == 'FILES'" ng-file-model="failureListVm.attachments"/>
                <select ng-if="step.dataType == 'LIST' && step.status == 'Activity'"
                        style="width: 330px;height: 40px" data-placement="Select"
                        ng-model="step.value" ng-options="activity for activity in failureListVm.activities">
                    <option>{{activity}}</option>
                </select>
                <input ng-if="step.dataType == 'STRING'" style="width: 330px"
                       class="form-control" type="text"
                       ng-model="step.value"/>
            <textarea ng-if="step.dataType == 'TEXT'" style="width: 330px"
                      class="form-control" ng-model="step.value"></textarea>
            </td>
        </tr>
        </tbody>
    </table>
</div>
<br>
<br>

<%--<div>
    <span style="font-size: 20px;margin-left: 10px"><strong>Approvals</strong></span>
</div>
<br>
<br>

<div class="col-md-3 approveform" ng-repeat="step in failureListVm.failureValueList1">
    <a href="" ng-if="step.value == false && ((step.status == 'BDL' && hasPermission('permission.bdl.failure'))
    || (step.status == 'SSQAG' && hasPermission('permission.ssqag.failure')) || (step.status == 'CAS' && hasPermission('permission.cas.failure'))
    || (step.status == 'STORE' && hasPermission('permission.store.failure')))"
       ng-click="failureListVm.check(step);step.value = true;">
        <p><i title="Check" class="glyphicon glyphicon-unchecked result-hover"
              style="font-size:20px;color:green"></i></a>
    <i title="Accepted" ng-if="step.value" class="glyphicon glyphicon-check result-hover"
       style="font-size:20px;color:blue"></i><span><strong>{{step.status}}</strong></span></p>
    <span ng-if="step.value == true">{{step.checkedDate}},<span>
        <span ng-if="step.value == true">{{step.checkedByObject.firstName}}<span>
</div>--%>
</body>
</html>