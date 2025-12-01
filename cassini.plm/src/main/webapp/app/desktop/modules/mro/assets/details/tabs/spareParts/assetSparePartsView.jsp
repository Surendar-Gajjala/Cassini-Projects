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
                    <i class="la la-plus dropdown-toggle" ng-click="assetSparePartVm.addSpareParts()"
                       title="{{addAffectedItemTitle}}" style="cursor: pointer"></i>
                </th>
                <th class="col-width-150" translate>NUMBER</th>
                <th class="col-width-150" translate>NAME</th>
                <th class="col-width-150" translate>TYPE</th>
                <th class="col-width-250" translate>DESCRIPTION</th>
                <th style="width: 100px; text-align: center" translate>ACTIONS</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="assetSparePartVm.assetSpareParts.length == 0">
                <td colspan="12" style="background-color: #f9fbfe  !important;color: unset !important;">
                    <div class="no-data">
                        <img src="app/assets/no_data_images/SpareParts.png" alt="" class="image">
                        <div class="message">{{ 'NO_SPARE_PARTS' | translate}} </div>
                        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                    </div>
                </td>
            </tr>
            <tr ng-repeat="sparePart in assetSparePartVm.assetSpareParts track by $index">
                <td></td>
                <td class="col-width-150">
                    <a title="{{clickToShowDetails}}" ng-click="assetSparePartVm.showSparePart(sparePart)">
                        {{sparePart.sparePart.number}}
                    </a>
                </td>
                <td class="col-width-150" title="{{sparePart.sparePart.name}}">
                    <span ng-bind-html="sparePart.sparePart.name"></span>
                </td>
                <td class="col-width-150">{{sparePart.sparePart.type.name}}</td>
                <td class="col-width-250">{{sparePart.sparePart.description}}</td>
                <td class="text-center">

                <span class="row-menu" uib-dropdown dropdown-append-to-body
                      style="min-width: 50px">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li
                                ng-click="assetSparePartVm.deletePart(sparePart)">
                            <a translate>REMOVE_PART</a>
                        </li>
                        <plugin-table-actions context="asset.spareParts" object-value="sparePart"></plugin-table-actions>
                    </ul>
                </span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>