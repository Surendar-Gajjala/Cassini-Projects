<style>
    .popover-content {
        padding: 5px !important;
    }
</style>
<div style="max-height: 240px;overflow-y: auto !important;">
    <table class="table table-striped">
        <thead>
        <tr style="">
            <th style="width: 150px;">Name</th>
            <th style="width: 80px; text-align: center;">Version</th>
            <th>Modified Date</th>
        </tr>
        <tr ng-if="item.item.itemFiles.length == 0" style="border-top: 1px solid lightgrey;">
            <td colspan="10"
                style="width: 100px;background: #fcfffa !important;color: blue !important;text-align: left;">
                No Files
            </td>
        </tr>
        </thead>
        <tbody>
        <tr ng-repeat="file in item.item.itemFiles">
            <td style="width: 150px;">
                <a href="" title="Click to download file" ng-click="itemBomVm.downloadFile(file)">{{file.name}}</a>
            </td>
            <td style="width: 80px; text-align: center"> {{file.version}}</td>
            <td> {{file.modifiedDate}}</td>
        </tr>
        </tbody>
    </table>
</div>
