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
    <div style="width: 50%">
        <div class='responsive-table'>
            <table class='table table-striped highlight-row'>
                <thead>
                <tr>
                    <th style="width: 30px;">
                        <i class="la la-plus" title="Add Specifications"
                           style="cursor: pointer" ng-if="!itemRevision.released && !itemRevision.rejected"
                           ng-click="itemSpecificationsVm.addSpecifications()"></i>
                    </th>
                    <th style="width: 200px;" translate>NUMBER</th>
                    <th class="name-column" translate>TYPE</th>
                    <th style="width: 200px;" translate>NAME</th>
                    <th class="col-width-250" translate>DESCRIPTION</th>
                    <th style="width: 200px;" translate>STATUS</th>
                    <th style="width: 100px;text-align: center">
                        <span translate>ACTIONS</span>
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="itemSpecificationsVm.loading == true">
                    <td colspan="14"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                          class="mr5"><span translate>LOADING_SPECIFICATIONS</span>
                    </td>
                </tr>
                <tr ng-if="itemSpecificationsVm.loading == false && itemSpecificationsVm.itemSpecifications.length == 0">
                    <td colspan="12" style="background-color: #f9fbfe  !important;color: unset !important;">
                        <div class="no-data">
                            <img src="app/assets/no_data_images/Specification.png" alt="" class="image">

                            <div class="message" translate>NO_SPECIFICATIONS</div>
                            <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                        </div>
                    </td>
                </tr>

                <tr ng-repeat="itemSpecification in itemSpecificationsVm.itemSpecifications">
                    <td></td>
                    <td>
                        <a href=""
                           ng-click="itemSpecificationsVm.showSpecificationDetails(itemSpecification)">{{itemSpecification.specification.number}}</a>
                    </td>
                    <td>{{itemSpecification.specification.type.name}}</td>
                    <td>{{itemSpecification.specification.name}}</td>
                    <td class="col-width-250">
                        <span>{{itemSpecification.specification.description}}</span>
                    </td>
                    <td class="name-column">
                        <span ng-if="itemSpecification.specification.active == true"
                              class="label label-outline bg-light-success"
                              translate>C_ACTIVE</span>
                        <span ng-if="itemSpecification.specification.active == false"
                              class="label label-outline bg-light-danger"
                              translate>C_INACTIVE</span>
                    </td>
                    <td class="text-center">
                        <span class="row-menu" uib-dropdown dropdown-append-to-body
                              ng-if="!itemRevision.released && !itemRevision.rejected"
                              style="min-width: 50px">
                            <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li>
                                    <a href=""
                                       ng-click="itemSpecificationsVm.deleteItemSpecification(itemSpecification)"
                                       translate>REMOVE_SPECIFICATION</a>
                                </li>
                                <plugin-table-actions context="item.compliance" object-value="itemSpecification"></plugin-table-actions>
                            </ul>
                        </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>