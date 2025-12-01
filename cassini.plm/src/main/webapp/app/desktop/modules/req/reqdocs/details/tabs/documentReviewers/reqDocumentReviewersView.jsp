<div>
    <style scoped>
        table {
            table-layout: fixed;
        }

        .ui-select-bootstrap .ui-select-match-text span {
            vertical-align: bottom;
        }

        .ui-select-bootstrap > .ui-select-match > .btn {

        }

        .ui-select-bootstrap > .ui-select-choices, .ui-select-bootstrap > .ui-select-no-choice {
            position: absolute !important;
        }
    </style>
    <div class='responsive-table'>
        <table class='table table-striped highlight-row'>
            <thead>
            <tr>
                <th style="width: 15px;" ng-hide="reqDocumentRevision.lifeCyclePhase.phase == 'Released'">
                    <i class="la la-plus" style="cursor: pointer"
                       title="Add reviwers & approvers"
                       ng-click="showReviewers()"></i>
                </th>
                <th style="width: 200px;" translate>NAME</th>
                <th style="width: 200px;" translate>TYPE</th>
                <th style="width: 200px;" translate>STATUS</th>
                <th style="width: 200px;" translate>NOTES</th>
                <th style="width: 200px;" translate>Timestamp</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="reqDocumentReviewersVm.loading == true">
                <td colspan="14"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                      class="mr5"><span translate>LOADING_REVIEWERS_APPROVERS</span>
                </td>
            </tr>
            <tr ng-if="reqDocumentReviewersVm.loading == false && reqDocumentReviewersVm.reqReviewers.length == 0">
                <td colspan="12" style="background-color: #f9fbfe  !important;color: unset !important;">
                    <div class="no-data">
                        <img src="app/assets/no_data_images/Substance.png" alt="" class="image">

                        <div class="message" translate>NO_REVIEWERS_APPROVERS</div>
                        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                    </div>
                </td>
            </tr>
            <tr ng-if="reqDocumentReviewersVm.reqReviewers.length > 0"
                ng-repeat="reviewer in reqDocumentReviewersVm.reqReviewers">
                <td ng-hide="reqDocumentRevision.lifeCyclePhase.phase == 'Released'"></td>
                <td class="col-width-250">{{reviewer.reviewerObject.fullName}}</td>
                <td class="col-width-150">
                    <span ng-if="reviewer.approver">Approver</span>
                    <span ng-if="!reviewer.approver">Reviewer</span>
                </td>
                <td class="col-width-150">
                    <object-status object="reviewer"></object-status>
                </td>
                <td class="col-width-250">{{reviewer.notes}}</td>
                <td class="col-width-250">{{reviewer.voteTimestamp}}</td>
            </tr>
            </tbody>
        </table>
    </div>
</div>