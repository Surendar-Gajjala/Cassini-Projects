<style>
    .view-content {
        position: relative;
    }

    .view-content .responsive-table {
        padding: 10px;
        position: absolute;
        bottom: 40px;
        top: 0;
        overflow: auto;
    }

    .view-content .responsive-table table thead th {
        position: -webkit-sticky;
        position: sticky;
        top: -10px;
        z-index: 5;
    }

    .view-content .table-footer {
        padding: 0 10px 0 10px;
        position: absolute;
        bottom: 0px !important;
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

    .sticky-col {
        position: sticky !important;
        position: -webkit-sticky !important;
    }

    .sticky-actions-col {
        right: -10px !important;
    }

    #freeTextSearchDirective {
        top: 7px !important;
    }

    .attributeTooltip {
        position: relative;
        display: inline-block;
    }

    .attributeTooltip .attributeTooltiptext {
        visibility: hidden;
        width: 200px;
        background-color: #7BB7EB;
        color: #141f9f;
        text-align: left;
        border-radius: 6px;
        padding: 5px 0;
        position: absolute;
        z-index: 1;
        top: -5px;
        bottom: auto;
        right: 100%;
        opacity: 0;
        transition: opacity 1s;
    }

    .attributeTooltip .attributeTooltiptext::after {
        content: "";
        position: absolute;
        top: 25%;
        left: 102%;
        margin-left: -5px;
        border-width: 5px;
        border-style: solid;
        border-color: transparent transparent transparent #7BB7EB;
    }

    .attributeTooltip:hover .attributeTooltiptext {
        visibility: visible;
        opacity: 1;
    }
</style>
<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <%--<span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate>JIG_AND_FIXTURE</span>--%>

        <button class="btn btn-sm new-button" ng-click="allJigsAndFixturesVm.newJigsAndFixtures()"
                title=" {{ jigsFixtureType == 'JIG' ? newJig : newFixture }} "
                ng-if="hasPermission('jigfixture','create')">
            <i class="las la-plus" aria-hidden="true"></i>
            <span ng-show="jigsFixtureType == 'JIG'">{{newJig}}</span>
            <span ng-show="jigsFixtureType == 'FIXTURE'">{{newFixture}}</span>
        </button>


          <span class="form-check" style="padding:10px;border-right: none;">
            <label class="form-check-label" style="margin-right: 5px">
                <input class="form-check-input" type="radio" name="jigsFixtureTypes"
                       id="jigsType"
                       ng-click="allJigsAndFixturesVm.selectJigsFixType('JIG')"
                       checked>
                <span style="padding: 2px;margin-left: 5px;" translate>JIGS</span>
            </label>
            <label class="form-check-label" style="margin-right: 5px">
                <input class="form-check-input" type="radio" name="jigsFixtureTypes"
                       id="fixtureType"
                       ng-click="allJigsAndFixturesVm.selectJigsFixType('FIXTURE')">
                <span style="padding: 2px;margin-left: 5px;" translate>FIXTURES</span>
            </label>
        </span>

        <div class="btn-group">
            <button class="btn btn-sm btn-success" ng-click="allJigsAndFixturesVm.showTypeAttributes()"
                    title="{{'ALL_VIEW_ADD_ATTRIBUTES' | translate}}">
                <i class="fa fa-newspaper-o" style=""></i>
            </button>
        </div>
        <free-text-search on-clear="allJigsAndFixturesVm.resetPage" search-term="allJigsAndFixturesVm.searchText"
                          on-search="allJigsAndFixturesVm.freeTextSearch"
                          filter-search="allJigsAndFixturesVm.filterSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width:1px;white-space:nowrap;"></th>
                    <th style="width: 100px" translate>NUMBER</th>
                    <th class="col-width-150" translate>TYPE</th>
                    <th class="col-width-200" translate>NAME</th>
                    <th class="description-column" translate>DESCRIPTION</th>
                    <th class="col-width-100" translate>STATUS</th>
                    <th class="col-width-150" translate>REQUIRESMAINTENANCE</th>
                    <th style="width: 100px;text-align: center;" translate>IMAGE_TITLE</th>
                    <th style="width: 150px" translate>MODIFIED_BY</th>
                    <th style="width: 150px" translate>MODIFIED_DATE</th>
                    <th class='added-column'
                        style="width: 150px;z-index: auto !important;"
                        ng-repeat="selectedAttribute in allJigsAndFixturesVm.selectedAttributes">
                        {{selectedAttribute.name}}
                        <i class="fa fa-times-circle"
                           ng-click="allJigsAndFixturesVm.removeAttribute(selectedAttribute)"
                           title={{allJigsAndFixturesVm.RemoveColumnTitle}}></i>
                    </th>
                    <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;"
                        translate>ACTIONS
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="allJigsAndFixturesVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_JIGS_FIXTURES</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="allJigsAndFixturesVm.loading == false && jigsFixtureType == 'JIG' && allJigsAndFixturesVm.jigsAndFixtures.content.length == 0">
                    <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                        <div class="no-data">
                            <img src="app/assets/no_data_images/JigsAndFixtures.png" alt="" class="image">

                            <div class="message" translate>NO_JIGS</div>
                            <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                        </div>
                    </td>
                </tr>
                <tr ng-if="allJigsAndFixturesVm.loading == false && jigsFixtureType == 'FIXTURE' && allJigsAndFixturesVm.jigsAndFixtures.content.length == 0">
                    <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                        <div class="no-data">
                            <img src="app/assets/no_data_images/JigsAndFixtures.png" alt="" class="image">

                            <div class="message" translate>NO_FIXTURES</div>
                            <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                        </div>
                    </td>
                </tr>
                <tr ng-repeat="jigFixture in allJigsAndFixturesVm.jigsAndFixtures.content">
                    <td style="width:1px !important;white-space: nowrap;text-align: left;">
                        <all-view-icons object="jigFixture"></all-view-icons>
                    </td>
                    <td class="col-width-100">
                        <a href="" ng-click="allJigsAndFixturesVm.showJigsFixture(jigFixture)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span ng-bind-html="jigFixture.number | highlightText: freeTextQuery"></span>
                        </a>
                    </td>
                    <td Class="col-width-150">
                        <span ng-bind-html="jigFixture.type | highlightText: freeTextQuery"></span>
                    </td>
                    <td Class="col-width-200">
                        <span ng-bind-html="jigFixture.name | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="description-column" title="{{jigFixture.description}}"><span
                            ng-bind-html="jigFixture.description  | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="col-width-100">
                        <span ng-if="jigFixture.active == true" class="label label-outline bg-light-success" translate>C_ACTIVE</span>
                        <span ng-if="jigFixture.active == false" class="label label-outline bg-light-danger" translate>C_INACTIVE</span>
                    </td>
                    <td class="col-width-150">
                        <span ng-if="jigFixture.requiresMaintenance == true"
                              class="label label-outline bg-light-success"
                              translate>YES</span>
                        <span ng-if="jigFixture.requiresMaintenance == false"
                              class="label label-outline bg-light-danger"
                              translate>NO</span>
                    </td>
                    <td style="width: 100px;text-align: center">
                        <div>
                            <a ng-if="jigFixture.hasImage"
                               href="" ng-click="allJigsAndFixturesVm.showImage(jigFixture)"
                               title="{{clickToShowLargeImage}}">
                                <img ng-src="{{jigFixture.imagePath}}"
                                     style="height: 20px;width: 30px;">
                            </a>
                            <a ng-if="!jigFixture.hasImage" href="">
                                <img src="app/assets/images/cassini-logo-greyscale.png" title="No image" alt=""
                                     class="no-image-preview" style="height: 20px;width: 30px;">
                            </a>

                            <div id="item-thumbnail{{jigFixture.id}}" class="item-thumbnail modal">
                                <div class="item-thumbnail-content">
                                    <div class="thumbnail-content" style="display: flex;width: 100%;">
                                        <div class="thumbnail-view" id="thumbnail-view{{jigFixture.id}}">
                                            <div id="thumbnail-image{{jigFixture.id}}"
                                                 style="display: table-cell;vertical-align: middle;text-align: center;">
                                                <img ng-src="{{jigFixture.imagePath}}"
                                                     style="height: auto;width: auto;max-width: 100%;max-height: 90%"/>
                                                    <span class="thumbnail-close"
                                                          id="thumbnail-close{{jigFixture.id}}"></span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </td>
                    <td>{{jigFixture.modifiedBy}}</td>
                    <td>{{jigFixture.modifiedDate}}</td>
                    <td class="added-column"
                        ng-repeat="objectAttribute in allJigsAndFixturesVm.selectedAttributes">
                        <all-view-attributes object="jigFixture"
                                             object-attribute="objectAttribute"></all-view-attributes>
                    </td>
                    <td class="text-center actions-col sticky-col sticky-actions-col">
                        <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                            <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                                <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                    style="z-index: 9999 !important;">
                                    <li>
                                        <a href=""
                                           ng-click="showPrintOptions(jigFixture.id,'JIGFIXTURE')" translate>PREVIEW_AND_PRINT</a>
                                    </li>
                                    <li title="{{hasPermission('jigfixture','delete') ? '' : noPermission}}"
                                        ng-class="{'cursor-override': !hasPermission('jigfixture','delete')}">
                                        <a ng-class="{'permission-text-disabled': !hasPermission('jigfixture','delete')}"
                                           ng-click="allJigsAndFixturesVm.deleteJigsFixture(jigFixture)"
                                           translate>DELETE</a>
                                    </li>
                                    <plugin-table-actions context="jigfixtures.all" object-value="jigfixture"></plugin-table-actions>
                                </ul>
                        </span>
                    </td>
                </tr>


                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <table-footer objects="allJigsAndFixturesVm.jigsAndFixtures" pageable="allJigsAndFixturesVm.pageable"
                          previous-page="allJigsAndFixturesVm.previousPage" page-size="allJigsAndFixturesVm.pageSize"
                          next-page="allJigsAndFixturesVm.nextPage"></table-footer>
        </div>
    </div>
</div>
