<style>
    .item-container {
        overflow-y: auto;
    }

    .item-container .container-item {
        cursor: pointer;
        padding: 10px;
        border-bottom: 1px solid #ddd;
    }

    .item-container .container-item.active-item {
        background-image: -webkit-linear-gradient(top, #337ab7 0, #2b669a 100%);
        background-image: -o-linear-gradient(top, #337ab7 0, #2b669a 100%);
        background-image: -webkit-gradient(linear, left top, left bottom, from(#337ab7), to(#2b669a));
        background-image: linear-gradient(to bottom, #337ab7 0, #2b669a 100%);
        filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#ff337ab7', endColorstr='#ff2b669a', GradientType=0);
        background-repeat: repeat-x;
        border-color: #2b669a;
        color: #fff !important;
    }

    .item-container .container-item:hover {

    }

    .item-container .container-item .inward-number {
        font-size: 20px;
    }

    .item-container .container-item .prop-name {
        color: #8A8A8A;
        font-size: 14px;
    }

    .item-container .container-item.active-item .prop-name {
        color: #d8d8d8;
    }

    .item-container .container-item .prop-value {
        font-size: 16px;
    }

    .item-container .container-item .margin-bottom {
        margin-bottom: 10px;
    }

    .row {
        margin-left: 0;
        margin-right: 0;
    }

    .selected {
        background-color: #0390fd;
        color: #fff;
        border: #0390fd;
    }

    .view-toolbar {
        height: 50px;
        padding-top: 4px;
        padding-left: 0px;
        border-bottom: 1px solid #D3D7DB;
        /* background-color: #F7F7F7; */
        background-color: #fff;
    }

    .panel-default {
        border: 1px solid #d7d7d7;
    }

    .item-container {
        overflow-y: auto;
    }

    .item-container .container-item {
        cursor: pointer;
        padding: 10px;
        border-bottom: 1px solid #ddd;
    }

    .item-container .container-item.active-item {
        background-image: -webkit-linear-gradient(top, #337ab7 0, #2b669a 100%);
        background-image: -o-linear-gradient(top, #337ab7 0, #2b669a 100%);
        background-image: -webkit-gradient(linear, left top, left bottom, from(#337ab7), to(#2b669a));
        background-image: linear-gradient(to bottom, #337ab7 0, #2b669a 100%);
        filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#ff337ab7', endColorstr='#ff2b669a', GradientType=0);
        background-repeat: repeat-x;
        border-color: #2b669a;
        color: #fff !important;
    }

    .item-container .container-item:hover {

    }

    .item-container .container-item .inward-number {
        font-size: 20px;
    }

    .item-container .container-item .prop-name {
        color: #8A8A8A;
        font-size: 14px;
    }

    .item-container .container-item.active-item .prop-name {
        color: #d8d8d8;
    }

    .item-container .container-item .prop-value {
        font-size: 16px;
    }

    .item-container .container-item .margin-bottom {
        margin-bottom: 10px;
    }

    .row {
        margin-left: 0;
        margin-right: 0;
    }
</style>
<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <div style="font-size: 20px;font-weight: bold;padding: 13px;">Commits Dialog</div>
    </div>

    <div class="view-content no-padding" style="padding: 10px;">
        <div class="split-pane fixed-left">
            <div class="split-pane-component split-left-pane" style="width: 400px;">
                <div class="item-container">

                    <div>
                        <table class="table">
                            <thead>
                            <tr>
                                <th>Commit ID</th>
                                <th>Committed By</th>
                                <th>Committed On</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-if="commitsVm.loading == false && commitsVm.commits.length == 0">
                                <td colspan="15">No Commits</td>
                            </tr>
                            <tr ng-repeat="commit in commitsVm.commits"
                                class="container-item"
                                ng-class="{'active-item': commit.id == commitsVm.selectedCommit.id}"
                                ng-click="commitsVm.selectCommits(commit)">
                                <td>{{commit.id}}</td>
                                <td>{{commit.createdByObject.firstName}}</td>
                                <td>{{commit.createdDate}}</td>
                            </tr>
                            <%-- <tr ng-repeat="commit in commitsVm.commits"
                                 ng-class="{'selected': commit.selected}"
                                 ng-click="commitsVm.selectCommits(commit)">

                             </tr>--%>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="split-pane-divider" style="left: 400px;"></div>
            <div class="split-pane-component split-right-pane noselect" style="left:400px;overflow-x: auto">
                <h4 ng-if="commitsVm.selectedCommit == null">Select commit to see details</h4>

                <div class="row" style="padding-top: 19px;" ng-if="commitsVm.selectedCommit != null">
                    <div class="form-group col-sm-3">
                        <label><b>Commit ID :</b></label>
                        <span>{{commitsVm.selectedCommit.id}}</span>
                    </div>

                    <div class="form-group col-sm-4">
                        <label><b>Committed By :</b></label>
                        <span>{{commitsVm.selectedCommit.createdByObject.firstName}}</span>
                    </div>

                    <div class="form-group col-sm-5">
                        <label><b>Committed Date :</b></label>
                        <span>{{commitsVm.selectedCommit.createdDate}}</span>
                    </div>
                </div>

                <br>

                <div class="row" ng-if="commitsVm.selectedCommit != null">
                    <div class="form-group col-sm-12">
                        <label><b>Comments :</b></label>
                        <span style="margin-left: 17px;">{{commitsVm.selectedCommit.comments}}</span>
                    </div>
                </div>

                <br>

                <div class="panel panel-primary" ng-if="commitsVm.selectedCommit != null">
                    <div class="panel-heading" style="font-size: 20px;font-weight: bold;padding: 10px;">Committed
                        Files
                    </div>
                    <div class="panel-body" style="font-size: 14px !important;">
                        <table class="table table-striped highlight-row">
                            <thead>
                            <tr>
                                <th>Name</th>
                                <th>Size</th>
                                <th>Version</th>
                                <th>Modified Date</th>
                                <th>Modified By</th>
                            </tr>
                            </thead>

                            <tbody>
                            <tr ng-repeat="file in commitsVm.commitedFiles" style="color: black;">
                                <td><a href="" title="Click to downloadFile"
                                       ng-click="commitsVm.downloadFile(file)">{{file.name}}</a></td>
                                <td>{{commitsVm.fileSizeToString(file.size)}}</td>
                                <td>{{file.version}}</td>
                                <td>{{file.modifiedDate}}</td>
                                <td>{{file.createdByObject.firstName}}</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                <br>

            </div>

        </div>
    </div>
</div>
</div>
