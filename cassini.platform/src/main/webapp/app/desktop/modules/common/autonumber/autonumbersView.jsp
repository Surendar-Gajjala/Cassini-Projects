<style scoped>
    .headerSticky .responsive-table {
        padding: 10px !important;
        position: absolute !important;
        bottom: 88px !important;
        top: -20px !important;
        overflow: auto;
    }

    .headerSticky .responsive-table table thead th {
        position: -webkit-sticky !important;
        position: sticky !important;
        top: -10px !important;
        z-index: 5 !important;
        /*background-color: #fff !important;*/
    }

    .headerSticky .responsive-table .autonumber-editmode > td {
        background-color: #d6e1e0 !important;
    }

    .view-content .table-footer {
        padding: 0 10px 0 10px;
        position: absolute;
        bottom: 50px !important;
        height: 42px;
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

    .autoNumber-height {
        margin: -14px 0 0 0 !important;
    }
</style>

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

        #freeTextSearchDirective ::-webkit-input-placeholder {
            font-weight: bold;
        }

        #freeTextSearchDirective :-moz-placeholder {
            font-weight: bold;
        }

        #freeTextSearchDirective {
            top: -50px !important;
            right: 0 !important;
        }

        #freeTextSearchDirective .search-box > input {
            padding-left: 30px !important;
        }

        .sticky-col {
            position: sticky !important;
            position: -webkit-sticky !important;
        }

        .sticky-actions-col {
            right: -10px !important;
        }

    </style>

    <div class="col-md-12 autoNumber-height">
        <free-text-search on-clear="autoVm.resetPage" search-term="autoVm.filters.searchQuery"
                          on-search="autoVm.freeTextSearch">
        </free-text-search>
    </div>


    <div class="col-md-12 headerSticky" style="padding-left: 0;">
        <div class="responsive-table">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="" translate>NAME</th>
                    <th class="col-width-250" translate>DESCRIPTION</th>
                    <th style="width:100px; text-align: center" translate>NUMBERS</th>
                    <th style="width:100px; text-align: center" translate>START</th>
                    <th style="width:100px; text-align: center" translate>INCREMENT</th>
                    <th style="width:100px; text-align: center" translate>PAD_WITH</th>
                    <th style="width:100px; text-align: left" translate>PREFIX</th>
                    <th style="width:100px; text-align: left" translate>SUFFIX</th>
                    <th style="" translate>EXAMPLES</th>
                    <th style="width:120px; text-align: center" class="sticky-col sticky-actions-col" translate>
                        ACTIONS
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="autonumbers.content.length == 0">
                    <td colspan="10"><span translate>NO_AUTO_NUMBERS</span></td>
                </tr>

                <tr ng-repeat="autonumber in autonumbers.content"
                    ng-class="{'autonumber-editmode': autonumber.editMode}">
                    <td style="vertical-align: middle;">
                        <span ng-show="autonumber.showValues"
                              ng-bind-html="autonumber.name  | highlightText: freeTextQuery"></span>
                        <input placeholder="Enter name" class="form-control" type="text" autofocus
                               ng-show="autonumber.editMode" ng-model="autonumber.newName">
                    </td>

                    <td class="col-width-250">
                        <span ng-show="autonumber.showValues"
                              ng-bind-html="autonumber.description  | highlightText: freeTextQuery"></span>
                        <input placeholder="Enter description" class="form-control" type="text"
                               ng-show="autonumber.editMode" ng-model="autonumber.newDescription">
                    </td>

                    <td style="width:100px; text-align: center; vertical-align: middle;">
                        <span ng-show="autonumber.showValues">{{ autonumber.numbers }}</span>
                        <input placeholder="" class="form-control" type="number" style="width: 90px;"
                               ng-show="autonumber.editMode" ng-model="autonumber.newNumber">
                    </td>

                    <td style="width:100px; text-align: center; vertical-align: middle;">
                        <span ng-show="autonumber.showValues">{{ autonumber.start }}</span>
                        <input placeholder="" class="form-control" type="number" style="width: 90px;"
                               ng-show="autonumber.editMode" ng-model="autonumber.newStart">
                    </td>

                    <td style="width:100px; text-align: center; vertical-align: middle;">
                        <span ng-show="autonumber.showValues">{{ autonumber.increment }}</span>
                        <input placeholder="" class="form-control" type="number" style="width: 90px;"
                               ng-show="autonumber.editMode" ng-model="autonumber.newIncrement">
                    </td>

                    <td style="width:100px; text-align: center; vertical-align: middle;">
                        <span ng-show="autonumber.showValues">{{ autonumber.padwith }}</span>
                        <input placeholder="" class="form-control" type="text" style="width: 90px;"
                               ng-show="autonumber.editMode" ng-model="autonumber.newPadwith">
                    </td>

                    <td style="width:100px; text-align: left; vertical-align: middle;">
                        <span ng-show="autonumber.showValues"
                              ng-bind-html="autonumber.prefix  | highlightText: freeTextQuery"></span>
                        <input placeholder="" class="form-control" type="text" style="width: 90px;"
                               ng-show="autonumber.editMode" ng-model="autonumber.newPrefix">
                    </td>

                    <td style="width:100px; text-align: left; vertical-align: middle;">
                        <span ng-show="autonumber.showValues"
                              ng-bind-html="autonumber.suffix  | highlightText: freeTextQuery"></span>
                        <input placeholder="" class="form-control" type="text" style="width: 90px;"
                               ng-show="autonumber.editMode" ng-model="autonumber.newSuffix">
                    </td>
                    <td style="vertical-align: middle;">
                        {{ autoVm.getAutonumberExamples(autonumber) }} ...
                    </td>

                    <td style="width:120px; text-align: center; vertical-align: middle;"
                        class="sticky-col sticky-actions-col">
                        <div ng-if="!autonumber.editMode">
                            <i ng-if="hasPermission('settings','edit')"
                               ng-click="autoVm.showEditMode(autonumber)" title="{{autoVm.editTitle}}"
                               class="la la-pencil">
                            </i>
                            <i ng-if="hasPermission('settings','delete')"
                               ng-click="autoVm.deleteAutoNumber(autonumber)" title="{{autoVm.deleteTitle}}"
                               class="la la-trash">
                            </i>
                        </div>


                        <div class="btn-group" style="margin-bottom: 0px;" ng-if="autonumber.editMode">
                            <i ng-click="autoVm.acceptChanges(autonumber)" title="{{autoVm.saveTitle}}"
                               class="la la-check">
                            </i>
                            <i title="{{autoVm.cancel}}"
                               ng-click="autoVm.hideEditMode(autonumber);" class="la la-times">

                            </i>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <table-footer objects="autonumbers" pageable="autoVm.pageable"
                          previous-page="autoVm.previousPage" next-page="autoVm.nextPage"
                          page-size="autoVm.pageSize"></table-footer>
        </div>
    </div>
</div>