<div>
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

        #freeTextSearchDirective {
            top: 7px !important;
        }

        .sticky-col {
            position: sticky !important;
            position: -webkit-sticky !important;
        }

        .sticky-actions-col {
            right: -10px !important;
        }

        .table-striped > tbody > tr:nth-child(2n) > td.actions-col {
            /*background-color: #fff;*/
        }

        .table-striped > tbody > tr:nth-child(2n):hover > td.sticky-col {
            background-color: #d6e1e0;
        }

        .added-column {
            text-align: left;
            width: 150px;
        }

        .added-column i {
            display: none;
            cursor: pointer;
            margin-left: 5px;
        }

        .added-column:hover i {
            display: inline-block;
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

        /* The Close Button */
        .img-model .closeImage11 {
            position: absolute;
            top: 50px;
            right: 50px;

            font-size: 40px;
            font-weight: bold;
            transition: 0.3s;
        }

        .img-model .closeImage11:hover,
        .img-model .closeImage11:focus {
            color: #bbb;
            text-decoration: none;
            cursor: pointer;
        }


    </style>
    <div class="view-container" fitcontent>
        <div class="view-toolbar">
            <span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate>MANUFACTURER_PARTS</span>

            <button class="btn btn-sm new-button" ng-if="hasPermission('manufacturerpart','create')"
                    id="newManufacturerPart"
                    ng-click="allMfrPartsVm.createParts()" title="{{allMfrPartsVm.mfrPartTitle}}">
                <i class="las la-plus" aria-hidden="true"></i>
                <span>{{'NEW' | translate}} {{'MANUFACTURER_PART' | translate }}</span>
            </button>

            <div class="btn-group">
                <button class="btn btn-sm btn-primary" ng-click="allMfrPartsVm.showPartAttributes()"
                        title="{{allMfrPartsVm.partAttributesTitle}}">
                    <i class="fa fa-newspaper-o"></i>
                </button>
                <button class="btn btn-sm btn-info" class="btn btn-sm btn-white"
                        ng-click="allMfrPartsVm.showPartSavedSearches()"
                        title="{{allMfrPartsVm.savedSearchItems}}">
                    <i class="fa fa-save"></i>
                </button>
                <button ng-show="showSearch == true" class="btn btn-sm btn-black"
                        ng-click="allMfrPartsVm.showSaveSearch()"
                        title="{{allMfrPartsVm.saveSearchMfrPart}}">
                    <i class="fa fa-clipboard"></i>
                </button>
                <button ng-show="allMfrPartsVm.showShareButton == true && hasPermission('manufacturerpart','edit')"
                        class="btn btn-sm btn-default"
                        style="border-radius:4px;"
                        ng-click="allMfrPartsVm.shareSelectedParts()" title="{{allMfrPartsVm.share}}">
                    <i class="las la-share" style=""></i>
                </button>
                <button class="btn btn-sm btn-default"
                        style="" title="{{preferredPage}}"
                        ng-click="savePreferredPage()">
                    <i class="fa fa fa-anchor" style=""></i>
                </button>
            </div>

            <free-text-search on-clear="allMfrPartsVm.resetPage" search-term="allMfrPartsVm.searchText"
                              on-search="allMfrPartsVm.freeTextSearch"
                              filter-search="allMfrPartsVm.filterSearch"></free-text-search>
        </div>
        <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
            <div class="responsive-table" style="padding: 10px;">
                <table class="table table-striped highlight-row">
                    <thead>
                    <tr>
                        <th style="width: 40px">
                            <div ng-if="allMfrPartsVm.mfrParts.content.length > 1" class="ckbox ckbox-default"
                                 style="display: inline-block;">
                                <input id="mfrPart{{$index}}" name="itemSelected" ng-value="true" type="checkbox"
                                       ng-model="allMfrPartsVm.flag" ng-click="allMfrPartsVm.selectAll()">
                                <label for="mfrPart{{$index}}" class="item-selection-checkbox"></label>
                            </div>
                        </th>
                        <th style="width:1px;white-space:nowrap;"></th>
                        <th class="col-width-150" translate>PART_NUMBER</th>
                        <th class="col-width-200" translate>MANUFACTURER</th>
                      <!-- <th class="col-width-150" translate>TYPE</th> -->

                        <th>
                            <span ng-if="allMfrPartsVm.selectedMfrPartType != null"
                              style="font-weight:normal;font-size: 13px;cursor: pointer !important;">
                                ({{allMfrPartsVm.selectedMfrPartType.name}})
                                <i class="fa fa-times-circle" ng-click="allMfrPartsVm.clearTypeSelection()"
                                   title="{{removeTitle}}"></i>
                          </span>
                        <br>
                                <div class="dropdown" uib-dropdown style="display: inline-block"
                                     ng-if="allMfrsVm.search.searchType != 'advanced'">
                                    <span uib-dropdown-toggle><span translate>TYPE</span>
                                        <i class="caret dropdown-toggle" style="margin-left: 5px;cursor: pointer;"></i>
                                    </span>
                                                <div class="dropdown-menu" role="menu">
                                                    <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                                        <workflow-part
                                                                on-select-type="allMfrPartsVm.onSelectType"></workflow-part>
                                                    </div>
                                                </div>
                                </div>
                        </th>
                        <th class="col-width-200" translate>PART_NAME</th>
                        <th class="description-column" translate>DESCRIPTION</th>
                        <th style="width: 100px;" translate>SERIALIZED</th>
                        <th style="width: 150px;" translate>LIFECYCLE</th>
                        <th style="width: 200px;" translate>STATUS</th>
                        <th style="width: 100px;text-align: center;" translate="THUMBNAIL"></th>
                        <th class='added-column' style="width: 200px;"
                            ng-repeat="mfrPartAttribute in allMfrPartsVm.selectedMfrPartAttributes">
                            {{mfrPartAttribute.name}}
                            <i class="fa fa-times-circle"
                               ng-click="allMfrPartsVm.removeAttribute(mfrPartAttribute)"
                               title="Remove this column"></i>
                        </th>
                        <th style="width: 50px;">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-if="allMfrPartsVm.loading == true">
                        <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_MFR_PARTS</span>
                        </span>
                        </td>
                    </tr>

                    <tr ng-if="allMfrPartsVm.loading == false && allMfrPartsVm.mfrParts.content.length == 0">
                        <td colspan="10" style="background-color: #f9fbfe  !important;color: unset !important;">
                            <div class="no-data">
                                <img src="app/assets/no_data_images/ManufacturerParts.png" alt="" class="image">

                                <div class="message">{{ 'NO_MFR_PARTS' | translate}}</div>
                                <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                    NO_PERMISSION_MESSAGE
                                </div>
                            </div>
                        </td>
                    </tr>

                    <tr ng-repeat="mfrPart in allMfrPartsVm.mfrParts.content">
                        <td style="width: 40px;" ng-if="allMfrPartsVm.mfrParts.content.length > 0">
                            <div class="ckbox ckbox-default" style="display: inline-block;">
                                <input id="mfrPart{{$index}}" name="itemSelected" ng-value="true" type="checkbox"
                                       ng-model="mfrPart.selected" ng-click="allMfrPartsVm.selectCheck(mfrPart)">
                                <label for="mfrPart{{$index}}" class="item-selection-checkbox"></label>
                            </div>
                        </td>
                        <td style="width:1px !important;white-space: nowrap;text-align: left;">
                            <all-view-icons object="mfrPart"></all-view-icons>
                        </td>
                        <td class="col-width-150"><a href="" ng-click="allMfrPartsVm.showMfrPartDetails(mfrPart)"
                                                     title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"><span
                                ng-bind-html="mfrPart.partNumber | highlightText: freeTextQuery"></span></a></td>
                        <td class="col-width-200"><a href="" ng-click="allMfrPartsVm.showMfrDetails(mfrPart)"
                                                     title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"><span
                                ng-bind-html="mfrPart.mfrName | highlightText: freeTextQuery"></span></a></td>
                        <td class="col-width-150"><span
                                ng-bind-html="mfrPart.mfrPartType.name | highlightText: freeTextQuery"></span></td>
                        <td class="col-width-250"><span
                                ng-bind-html="mfrPart.partName | highlightText: freeTextQuery"></span>
                        </td>
                        <td class="description-column">
                     <span title="{{mfrPart.description}}"><span
                             ng-bind-html="mfrPart.description  | highlightText: freeTextQuery"></span>
                     </span>
                        </td>

                        <td class="col-width-150">
                            <span ng-if="mfrPart.serialized == true"
                                  class="label label-outline bg-light-success"
                                  translate>YES</span>
                            <span ng-if="mfrPart.serialized == false"
                                  class="label label-outline bg-light-danger"
                                  translate>NO</span>
                        </td>

                        <td style="width: 150px;">
                            <item-status item="mfrPart"></item-status>
                        </td>
                        <td>{{mfrPart.status}}</td>
                        <td style="width: 100px;text-align: center">
                            <div>
                                <a ng-if="mfrPart.thumbnail != null && mfrPart.thumbnail != ''"
                                   href="" ng-click="allMfrPartsVm.showImage(mfrPart)"
                                   title="{{clickToShowLargeImage}}">
                                    <img ng-src="{{mfrPart.imagePath}}"
                                         style="height: 20px;width: 30px;">
                                </a>
                                <a ng-if="mfrPart.thumbnail == null && mfrPart.thumbnail != ''" href="">
                                    <img src="app/assets/images/cassini-logo-greyscale.png" title="No image" alt=""
                                         class="no-image-preview" style="height: 20px;width: 30px;">
                                </a>

                                <div id="item-thumbnail{{mfrPart.id}}" class="item-thumbnail modal">
                                    <div class="item-thumbnail-content">
                                        <div class="thumbnail-content" style="display: flex;width: 100%;">
                                            <div class="thumbnail-view" id="thumbnail-view{{mfrPart.id}}">
                                                <div id="thumbnail-image{{mfrPart.id}}"
                                                     style="display: table-cell;vertical-align: middle;text-align: center;">
                                                    <img ng-src="{{mfrPart.imagePath}}"
                                                         style="height: auto;width: auto;max-width: 100%;max-height: 90%"/>
                                                    <span class="thumbnail-close"
                                                          id="thumbnail-close{{mfrPart.id}}"></span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </td>
                        <td class="added-column"
                            ng-repeat="objectAttribute in allMfrPartsVm.selectedMfrPartAttributes">
                            <all-view-attributes object="mfrPart"
                                                 object-attribute="objectAttribute"></all-view-attributes>
                        </td>
                        <td class="text-center actions-col sticky-col sticky-actions-col">
                     <span class="row-menu" uib-dropdown dropdown-append-to-body>
                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li>
                                    <a href=""
                                       ng-click="showPrintOptions(mfrPart.id,'MANUFACTURERPART')" translate>PREVIEW_AND_PRINT</a>
                                </li>
                                <tags-button object-type="'MANUFACTURERPART'" object="mfrPart.id"
                                             tags-count="mfrPart.tags.length"></tags-button>
                                <li ng-if="hasPermission('manufacturer','delete')"
                                    ng-click="allMfrPartsVm.deleteMfrpart(mfrPart)"><a translate>DELETE_PART</a>
                                </li>
                                <plugin-table-actions context="manufacturerPart.all"
                                                      object-value="mfrPart"></plugin-table-actions>
                            </ul>
                     </span>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="table-footer">
                <table-footer objects="allMfrPartsVm.mfrParts" pageable="allMfrPartsVm.pageable"
                              previous-page="allMfrPartsVm.previousPage"
                              next-page="allMfrPartsVm.nextPage" page-size="allMfrPartsVm.pageSize"></table-footer>
            </div>
        </div>
    </div>
</div>
