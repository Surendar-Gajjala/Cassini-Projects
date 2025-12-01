<div style="position: relative;">
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin: 0;">
            <div>
                <form class="form-horizontal">

                    <div class="form-group">
                        <label class="col-sm-3 control-label">Comment <span class="asterisk">*</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="5" style="resize: none"
                                      ng-model="newCommitVm.newCommit.comments"></textarea>
                        </div>
                    </div>

                    <hr>

                    <div class="row">
                        <h4><span>Selected Files</span></h4>
                    </div>

                    <div class="col-md-12" style="padding:0px;height: auto;overflow: auto;">
                        <table class="table table-striped highlight-row">
                            <thead>
                            <tr>
                                <th style="vertical-align: middle;">
                                    Name
                                </th>
                                <th style="vertical-align: middle;">
                                    Size
                                </th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-if="newCommitVm.files.length == 0">
                                <td colspan="15">No Files</td>
                            </tr>
                            <tr ng-repeat="file in newCommitVm.files">
                                <td style="vertical-align: middle;">
                                    {{file.name}}
                                </td>
                                <td style="vertical-align: middle;">
                                    {{newCommitVm.fileSizeToString(file.size)}}
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </form>
                <br><br>
            </div>
        </div>
    </div>
</div>
