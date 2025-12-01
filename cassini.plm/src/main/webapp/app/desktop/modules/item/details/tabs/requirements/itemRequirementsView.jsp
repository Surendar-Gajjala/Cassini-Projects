<div class='responsive-table'>
    <style scoped>
        .image.image_resized > img {
            width: 300px !important;
        }

        .ck.ck-reset, .ck.ck-reset_all, .ck.ck-reset_all * {
            width: 300px;
        }

        .ck .ck-widget__resizer__handle {
            background: none !important;
        }
    </style>
    <table class='table table-striped highlight-row'>
        <thead>
        <tr>
            <th class="col-width-100" translate>NUMBER</th>
            <th class="col-width-100" translate>TYPE</th>
            <th class="col-width-100" translate>NAME</th>
            <th class="col-width-250" translate>DESCRIPTION</th>
            <th class="col-width-100" translate>STATUS</th>
            <th class="col-width-100" translate>PRIORITY</th>
            <th class="col-width-100" translate>ASSIGNED_TO</th>

        </tr>
        </thead>
        <tbody>
        <tr ng-if="itemRequirementsVm.loading == true">
            <td colspan="14"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                  class="mr5"><span translate>LOADING_REQUIREMENTS</span>
            </td>
        </tr>
        <tr ng-if="itemRequirementsVm.loading == false && itemRequirementsVm.itemRequirements.length == 0">
            <td colspan="11" style="background-color: #f9fbfe  !important;color: unset !important;">
                <div class="no-data">
                    <img src="app/assets/no_data_images/Requirements.png" alt="" class="image">

                    <div class="message">{{ 'NO_REQUIREMENTS' | translate}}</div>
                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                </div>
            </td>
        </tr>
        <tr ng-repeat="itemReq in itemRequirementsVm.itemRequirements">
            <td class="col-width-100">
                <a href=""
                   title="{{ 'SHOW_REQ_DETAILS' | translate }} "
                   ng-click="itemRequirementsVm.showRequirementDetails(itemReq)">
                    <span ng-bind-html="itemReq.requirementVersion.master.number"></span></a>
            </td>
            <td class="col-width-100">
                <span ng-bind-html="itemReq.requirementVersion.master.type.name"></span>
            </td>
            <td class="col-width-100">
                <span>{{itemReq.requirementVersion.name}}</span>
            </td>
            <td class="col-width-250">
                <span ng-bind-html="itemReq.requirementVersion.description"></span>
            </td>
            <td class="col-width-100">
                <priority object="itemReq.requirementVersion"></priority>
            </td>
            <td class="col-width-100">
                <item-status item="itemReq.requirementVersion"></item-status>
            </td>
            <td class="col-width-100">
                <span>{{itemReq.personName}}</span>
            </td>
        </tr>
        </tbody>
    </table>
</div>