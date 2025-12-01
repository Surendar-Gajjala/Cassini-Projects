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
                        <i class="la la-plus" title="Add Specifications" ng-if="declaration.status == 'OPEN'"
                           style="cursor: pointer"
                           ng-click="declarationSpecificationsVm.addSpecifications()"></i>
                    </th>
                    <th style="width: 200px;" translate>NUMBER</th>
                    <th class="col-width-250" translate>TYPE</th>
                    <th style="width: 200px;" translate>NAME</th>
                    <th class="col-width-250" translate>DESCRIPTION</th>
                    <th class="col-width-200" translate>STATUS</th>
                    <th style="width: 100px;text-align: center">
                        <span translate>ACTIONS</span>
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="declarationSpecificationsVm.loading == true">
                    <td colspan="14"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                          class="mr5"><span translate>LOADING_SPECIFICATIONS</span>
                    </td>
                </tr>
                <tr ng-if="declarationSpecificationsVm.loading == false && declarationSpecificationsVm.declarationSpecifications.length == 0">
                    <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                        <div class="no-data">
                            <img src="app/assets/no_data_images/Specifications.png" alt="" class="image">

                            <div class="message" translate>NO_SPECIFICATIONS</div>
                            <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                        </div>
                    </td>
                </tr>

                <tr ng-repeat="declarationSpecification in declarationSpecificationsVm.declarationSpecifications">
                    <td></td>
                    <td>
                        <a href=""
                           ng-click="declarationSpecificationsVm.showSpecification(declarationSpecification)">{{declarationSpecification.specification.number}}</a>
                        <%--<span ng-if="!hasPermission('specification','view')">{{declarationSpecification.specification.number}}</span>--%>
                    </td>
                    <td>{{declarationSpecification.specification.type.name}}</td>
                    <td>{{declarationSpecification.specification.name}}</td>
                    <td class="col-width-250">
                        <span>{{declarationSpecification.specification.description}}</span>
                    </td>
                    <td class="col-width-200">
                        <span ng-if="declarationSpecification.specification.active == true" class="label label-outline bg-light-success"
                              translate>C_ACTIVE</span>
                        <span ng-if="declarationSpecification.specification.active == false" class="label label-outline bg-light-danger"
                              translate>C_INACTIVE</span>
                    </td>
                    <td class="text-center">
                        <span class="row-menu" ng-if="declaration.status == 'OPEN'" uib-dropdown dropdown-append-to-body
                              style="min-width: 50px">
                            <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li>
                                    <a href=""
                                       ng-click="declarationSpecificationsVm.deleteDeclarationSpecification(declarationSpecification)"
                                       translate>REMOVE_SPECIFICATION</a>
                                </li>
                            </ul>
                        </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>