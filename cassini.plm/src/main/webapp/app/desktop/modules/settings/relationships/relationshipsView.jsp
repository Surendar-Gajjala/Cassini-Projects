<div classs="row">
    <style scoped>
        .addAutonumberButton:hover {
            background-color: #0081c2 !important;
            border-color: #028af3;
            cursor: pointer;
        }

        .addAutonumberButton:hover i {
            color: #fff !important;
        }

        .headerSticky .responsive-table {
            padding: 10px !important;
            position: absolute !important;
            bottom: 88px !important;
            top: -20px !important;
            overflow: auto;
        }

        .headerSticky .responsive-table table thead th {
            position: -webkit-sticky !important;
            position: sticky !important;;
            top: -10px !important;;
            z-index: 5 !important;;
            background-color: #fff !important;;
        }

        .headerSticky .responsive-table .autonumber-editmode > td {
            background-color: #d6e1e0 !important;
        }

        .view-content .table-footer {
            padding: 0 10px 0 10px;
            position: absolute;
            bottom: 50px !important;
            height: 40px;
            width: 100%;
            border-top: 1px solid #D3D7DB;
            display: table;
        }

        .view-content .table-footer > div {
            display: table-row;
            line-height: 30px;
        }

        .view-content .table-footer > div h5 {
            margin: 0;
        }

        .view-content .table-footer > div > div {
            display: table-cell;
            vertical-align: middle;
        }

        .view-content .table-footer > div > div > i {
            font-size: 16px;
        }

        a.disabled {
            cursor: not-allowed;
            color: lightgrey;
        }

        .pagination-header {
            margin: -14px 0 0 0 !important;
        }

    </style>
    <div class="col-md-12 headerSticky" style="padding-left: 0;">
        <div class="responsive-table">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width: 250px;background-color: #f9fbfe !important;" translate>NAME</th>
                    <th class="col-width-250" style="background-color: #f9fbfe !important;" translate>DESCRIPTION</th>
                    <th style="background-color: #f9fbfe !important;" translate>FIRST_TYPE</th>
                    <th style="background-color: #f9fbfe !important;" translate>SECOND_TYPE</th>
                    <th style="width: 120px;text-align: center;background-color: #f9fbfe !important;" translate>ACTIONS</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="relationshipVm.relationships.content.length == 0">
                    <td colspan="10"><span translate>NO_RELATIONSHIPS</span></td>
                </tr>

                <tr ng-repeat="relationship in relationshipVm.relationships.content">
                    <td style="vertical-align: middle;" title="{{relationship.name}}">
                        {{relationship.name}}
                    </td>

                    <td class="col-width-250" title="{{relationship.description}}">
                        {{relationship.description}}
                    </td>

                    <td style="vertical-align: middle;">
                        {{relationship.fromType.name}}
                    </td>

                    <td style=" vertical-align: middle;">
                        {{relationship.toType.name}}
                    </td>

                    <td class="text-center">
                       <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                         <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li ng-if="hasPermission('settings','edit')"
                                    ng-click="relationshipVm.editRelationship(relationship)"><a translate>EDIT</a>
                                </li>

                                <li ng-if="hasPermission('settings','delete')"
                                    ng-click="relationshipVm.deleteRelationship(relationship)"><a translate>DELETE</a>
                                </li>
                            </ul>
                       </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <table-footer objects="relationshipVm.relationships" pageable="relationshipVm.pageable"
                          previous-page="relationshipVm.previousPage" next-page="relationshipVm.nextPage"
                          page-size="relationshipVm.pageSize"></table-footer>
        </div>
    </div>
</div>

