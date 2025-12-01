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
                <th style="width: 50px;">
                    <i class="la la-plus" style="cursor: pointer"
                       title="{{ 'ADD_REVIEWERS_AND_APPROVERS' | translate}}"
                       ng-click="showReqDocTemplateReviewers()"></i>
                </th>
                <th style="width: 650px;left: 22px" translate>NAME</th>
                <th style="width: 500px;" translate>TYPE</th>

            </tr>
            </thead>
            <tbody>
            <tr ng-if="reqDocTemplateReviewerVm.loading == true">
                <td colspan="14"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                      class="mr5"><span translate>LOADING_REVIEWERS_APPROVERS</span>
                </td>
            </tr>
            <tr ng-if="reqDocTemplateReviewerVm.loading == false && reqDocTemplateReviewerVm.reqReviewers.length == 0">
                <td colspan="12" style="background-color: #f9fbfe  !important;color: unset !important;">
                    <div class="no-data">
                        <img src="app/assets/no_data_images/Substance.png" alt="" class="image">

                        <div class="message" translate>NO_REVIEWERS_APPROVERS</div>
                        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                    </div>
                </td>
            </tr>
            <tr ng-if="reqDocTemplateReviewerVm.reqReviewers.length > 0"
                ng-repeat="reviewer in reqDocTemplateReviewerVm.reqReviewers">
                <td style="width: 50px"></td>
                <td class="col-width-250">{{reviewer.reviewerObject.fullName}}</td>

                <td class="col-width-250">
                    <span ng-if="reviewer.approver">Approver</span>
                    <span ng-if="!reviewer.approver">Reviewer</span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>