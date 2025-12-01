<div>
    <style scoped>
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
                <th style="width: 20px;">
                    <i class="la la-plus dropdown-toggle" ng-click="assemblyLineWorkCentersVm.addWorkCenters()"
                       title="{{addWorkCenters}}" style="cursor: pointer"></i>
                </th>
                <th class="col-width-150" translate>NUMBER</th>
                <th class="col-width-150" translate>TYPE</th>
                <th class="col-width-250" translate>NAME</th>
                <th class="col-width-150" translate>PLANT</th>
                <th class="col-width-250" translate>DESCRIPTION</th>
                <th style="width: 100px; text-align: center" translate>ACTIONS</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="assemblyLineWorkCentersVm.assemblyLineWorkCenters.length == 0">
                <td colspan="12" style="background-color: #f9fbfe  !important;color: unset !important;">
                    <div class="no-data">
                        <img src="app/assets/no_data_images/WorkCenter.png" alt="" class="image">

                        <div class="message">{{noWorkCenters}}
                        </div>
                        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                    </div>
                </td>
            </tr>
            <tr ng-repeat="workCenter in assemblyLineWorkCentersVm.assemblyLineWorkCenters track by $index">
                <td></td>
                <td class="col-width-150">
                    <a title="{{clickToShowDetails}}" ng-click="assemblyLineWorkCentersVm.showWorkCenter(workCenter)">
                        {{workCenter.number}}
                    </a>
                </td>
                <td class="col-width-150">{{workCenter.type.name}}</td>
                <td class="col-width-250" title="{{workCenter.name}}">
                    <span ng-bind-html="workCenter.name"></span>
                </td>
                <td class="col-width-150">{{workCenter.plantName}}</td>
                <td class="col-width-250">{{workCenter.description}}</td>
                <td class="text-center">

                <span class="row-menu" uib-dropdown dropdown-append-to-body
                      style="min-width: 50px">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li
                                ng-click="assemblyLineWorkCentersVm.removeWorkCenter(workCenter)">
                            <a translate>REMOVE_WORK_CENTER</a>
                        </li>
                        <plugin-table-actions context="assemblyLine.workCenters" object-value="workCenter"></plugin-table-actions>
                    </ul>
                </span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>