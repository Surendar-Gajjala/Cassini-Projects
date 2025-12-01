<style scoped>
    .added-column {
        text-align: left;
        width: 350px;
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
        background-color: #7BB7EB;
        color: #141f9f;
        text-align: left;
        border-radius: 6px;
        padding: 5px 0;
        position: absolute;
        z-index: 1;
        top: -5px;
        bottom: auto;
        opacity: 0;
        transition: opacity 1s;
        margin-left: -25px !important;
        word-break: break-all !important;

    }

    .attributeTooltip .attributeTooltiptext::after {
        content: "";
        position: absolute;
        top: 25%;
        left: 102%;
        border-width: 5px;
        border-style: solid;
        border-color: transparent transparent transparent #7BB7EB;
    }

    .attributeTooltip:hover .attributeTooltiptext {
        position: relative !important;
        visibility: visible;
        opacity: 1;
    }

    /* The Close Button */
    .img-model .closeImage,
    .img-model .closeImage1 {
        position: absolute;
        top: 50px;
        right: 50px;

        font-size: 40px;
        font-weight: bold;
        transition: 0.3s;
    }

    .img-model .closeImage:hover,
    .img-model .closeImage:focus,
    .img-model .closeImage1:hover,
    .img-model .closeImage1:focus {
        color: #bbb;
        text-decoration: none;
        cursor: pointer;
    }

    .img-model .closeImage1 {
        position: absolute;
        top: 50px;
        right: 50px;

        font-size: 40px;
        font-weight: bold;
        transition: 0.3s;
    }

    .img-model .closeImage1:hover,
    .img-model .closeImage1:focus {
        color: #bbb;
        text-decoration: none;
        cursor: pointer;
    }

    .JCLRLastGrip {
        display: none !important;
    }

    /* .grip {

         width: 100px;
         height: 10px;
         margin-top: -10px;
         background-image: url('app/assets/images/favicon.png');
         margin-left: 13px;
         position: relative;
         z-index: 88;
         cursor: move !important;
         background-repeat: no-repeat;

     }*/

    .tab-content .tab-pane .responsive-table table thead th {
        position: -webkit-sticky !important;
        position: sticky !important;
        top: -10px !important;
        z-index: 4 !important;
        border-left: none !important;
    }

    .JCLRgrip {
        width: 13px;
        margin-left: -5px !important;
        position: absolute !important;
        height: 18px !important;
        cursor: col-resize !important;
        border-left: 4px solid #a0a0a0;
        background-repeat: no-repeat;
        margin-bottom: 0;
        margin-top: 10px;
    }

    .JCLRgrip:before {
        /*content: "\f7a5";
        font-family: FontAwesome;*/
        left: -5px;
        position: absolute;
        top: 0;
    }

    .JCLRdisabledGrip {
        display: none !important;
    }

    .grip:hover {
        background-position: 20px 0px;
        cursor: e-resize !important;

    }

    .grip:active {
        background-position: 20px 0px;
        cursor: e-resize !important;

    }

    .dragging .grip {
        background-position: 40px 0px;
    }

    .table {
        width: 100% !important;
    }

    .badge1 {
        top: -7px;
        left: 0px;
        padding: 0px 7px;
        border-radius: 50%;
        background-color: grey;
        color: white;
    }

    .notification1 {
        text-decoration: none;
        padding: 9px 0;
        position: relative;
        display: inline-block;
        border-radius: 2px;
    }

    .notification1 .badge12 {
        position: absolute;
        top: -4px;
        right: -16px;
        padding: 0px 7px;
        border-radius: 50%;
        background-color: grey;
        color: white;
    }


    .center {
        display: block;
        margin-left: auto;
        margin-right: auto;
        margin-top: 4%;
        width: 300px;
    }

    .no-conversations .no-conversations-message {
        font-size: 20px;
        font-weight: 300 !important;
        text-align: center;
    }


</style>

<style scoped>
    .scrollable-table {
        overflow-y: auto !important;
        max-height: 500px !important;
    }

    .btn-group .btn + .btn, .btn-group .btn + .btn-group, .btn-group .btn-group + .btn, .btn-group .btn-group + .btn-group {
        margin-left: -2px;
    }
</style>
<div class='responsive-table' <%--ng-init="dragMe()"--%> id="specItemsView">
    <style scoped>
        .pad-cell {
            padding-left: 30px;
        }

        tr.unresolved-bom-item {
            background: repeating-linear-gradient(135deg, transparent, transparent 10px, #e6e6e6 10px, #ececec 20px),
            linear-gradient(to bottom, #eee, #fff) !important;
        }

        tr.unresolved-bom-item td {
            background: transparent;
        }

        .name-col {
            width: 400px;
            word-wrap: break-word !important;
            white-space: normal !important;
            text-align: left;
        }

        .description-col {
            word-wrap: break-word !important;
            width: 500px !important;
            white-space: normal !important;
            text-align: left;
        }

        .number-col {
            min-width: 50px !important;
            max-width: 50px !important;
            text-align: left;
        }

        .rev-col {
            width: 50px;
            text-align: center;
        }

        .lc-col {
            width: 100px;
        }

        .actions-col {
            width: 200px;
            text-align: center;
        }

        .section-row {
            font-style: italic;
        }

        .section-row .seq-col {
            font-style: normal;
        }

        .row-menu {
            font-size: 15px;
        }

        .add-col {
            min-width: 50px;
            text-align: center;
        }

        .add-col i {
            cursor: pointer;
        }

        .spec-items-table td {
            vertical-align: top !important;
        }

        .seq-col {
            min-width: 100px !important;
            max-width: 100px !important;
        }

        .row-menu i {
            cursor: pointer;
        }

        .handle {
            cursor: move;
        }

        .search-box > input {
            height: 30px;
            border-radius: 15px;
            padding-left: 30px;
            display: inline-block !important;
            background-color: rgb(241, 243, 244);
            border: 1px solid #ddd;
            width: 300px;
            box-shadow: none;
        }

        .search-box > input:hover {
            background-color: rgb(229, 231, 234);
        }

        .search-box > input:focus {
            box-shadow: none;
        }

        .search-box i.fa-search {
            position: relative;
            margin-top: 11px;
            color: grey;
            opacity: 0.5;
            margin-right: -28px !important;
        }

        .search-box i.fa-times-circle {
            margin-left: -27px;
            color: gray;
            cursor: pointer;
        }

        /* tr.hover {
             border-bottom: 3px solid blue !important;

         }*/

        .glyphicon-remove .a {
            display: block !important;
        }

        p.limit.ng-binding.ng-scope.tl {
            min-width: 200px !important;
            max-width: 200px;;
            margin-bottom: 12px;
            word-wrap: break-word;
            overflow: visible;
            white-space: normal;
            height: auto; /* just added this line */
        }

        th.added-column.ng-binding.ng-scope {
            min-width: 200px !important;
            max-width: 200px;;
            margin-bottom: 12px;
            word-wrap: break-word;
            overflow: visible;
            white-space: normal;
        }

        /* .JColResizer {
             table-layout: auto !important;
         }*/

        /*  .btn-group-vertical>.btn,.btn-group>.btn{
              position: relative !important;
              float: none !important;
          }*/
        .ui-select-toggle::after {
            position: absolute;
            right: 1em;
            padding-top: 4px;
        }

        .search-box i.fa-search {
            font-size: 12px;
        }

        .search-box input.req-search-form {
            border-radius: 5px;
        }

        .round {
            width: 300px;
            min-width: 11%;
            border-radius: 3px;
            padding: 4px 5px 7px 119px;
            top: 0;
            left: 0;
            z-index: 5;
            border: 1px #e6e8ed solid;
            background-color: rgb(241, 243, 244);
            outline: none;
        }

        .round > input:focus {
            border: 1px solid #ddd !important;
            box-shadow: none;
        }

    </style>

    <div class="search-box" style="text-align: center">

        <form id="spec-search">
            <i class="fa fa-search"></i>
            <input autocomplete="off"
                   class="round"
                   onfocus="this.setSelectionRange(0, this.value.length)"
                   ng-model="specItemsVm.reqSearchFilter.searchQuery"
                   ng-model-options="{ debounce: 500 }"
                   ng-change="specItemsVm.freeTextSearch(specItemsVm.reqMode)">

            <i class="fa fa-times-circle" title="{{clearTitleSearch}}"
               ng-show="specItemsVm.reqSearchFilter.searchQuery.length > 0"
               ng-click="specItemsVm.reqSearchFilter.searchQuery = '';specItemsVm.freeTextSearch()"></i>
        </form>
    </div>
    <table class='table table-striped' style="width: 100% !important;" col-resizeable id="specItemsTable">

        <thead>





        <tr>
            <th class="add-col"><i class="la la-plus" title="{{specItemsVm.addRootSection}}"
                                   ng-if="hasPermission('pgcspecification','edit') || specPermission.editPermission == true"
                                   ng-class="{disabled:selectedSpecification.lifecyclePhase.phaseType == 'RELEASED'}"
                                   ng-click="specItemsVm.createSection(null)"></i></th>
            <th class="seq-col" translate>SEQUENCENUMBER</th>
            <th style="min-width: 300px" translate>NAME</th>
            <th style="min-width: 600px" translate>DESCRIPTION</th>
            <th style="min-width: 100px" translate>NUMBER</th>
            <th style="min-width: 100px" translate>VERSION</th>
            <th style="min-width: 100px" translate>STATUS</th>
            <th style="min-width: 100px" translate>
                ASSIGNED_TO
            </th>
            <th style="min-width: 100px"
                translate>
                PLANNED_FINISH_DATE
            </th>
            <th class='added-column'
                style="word-wrap: break-word;white-space: normal;min-width: 150px;"
                ng-repeat="selectedAttribute in specItemsVm.selectedAttribute">

                <span>{{selectedAttribute.name}}
                    <i class="fa fa-times-circle"
                       ng-click="specItemsVm.removeReqAttribute(selectedAttribute)"
                       title="Remove this column"></i>
                </span>


            </th>
            <th style="width: 50px;" translate>ITEM_ALL_ACTIONS</th>
        </tr>
        </thead>
        <tr id="search">
            <td class="add-col"></td>
            <td class="seq-col"></td>
            <td class="" style="text-align: center; min-width: 300px;">
                <form>
                    <input type="text" class="form-control" name="Name"
                       ng-enter="specItemsVm.freeTextSearch(specItemsVm.reqMode)"
                       placeholder=" {{'NAME' | translate}}"
                       ng-model="specItemsVm.reqSearchFilter.name"
                       ng-model-options="{ debounce: 500 }">
                </form>
            </td>

            <td class="" style="text-align: center; min-width: 600px;">
               <form>
                   <input type="text" class="form-control" name="Description"
                          ng-enter="specItemsVm.freeTextSearch(specItemsVm.reqMode)"
                          placeholder=" {{'DESCRIPTION' | translate}}"
                          ng-model="specItemsVm.reqSearchFilter.description"
                          ng-model-options="{ debounce: 500 }">
               </form>
            </td>
            <td class="" style="text-align: center; min-width: 100px">
               <form>
                   <input type="text" class="form-control" name="Number" style="width: 85px !important;"
                          ng-enter="specItemsVm.freeTextSearch(specItemsVm.reqMode)"
                          placeholder=" {{'NUMBER' | translate}}"
                          ng-model="specItemsVm.reqSearchFilter.objectNumber"
                          ng-model-options="{ debounce: 500 }">
               </form>

            </td>
            <%--
                        <td class="" style="text-align: center; min-width: 100px">

                            <ui-select ng-model="specItemsVm.reqSearchFilter.version1" style="width: 85px !important;"
                                       on-select="specItemsVm.freeTextSearch(specItemsVm.reqMode)"
                                       theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match allow-clear="false"
                                                 placeholder="Version">
                                    <a class="btn btn-xs btn-link pull-right"
                                       ng-click="clear($event, $select)"><i
                                            class="glyphicon glyphicon-remove a"></i></a>

                                    {{$select.selected}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="rev in specItemsVm.reqVersions | filter: $select.search">
                                    <div ng-bind="rev | highlight: $select.rev.search"></div>
                                </ui-select-choices>
                            </ui-select>


                        </td>--%>


            <td class="" style="text-align: center; min-width: 100px;">
                <div style="min-width: 100px;">
                    <select class="form-control"
                            style="width: 100%;padding: 5px;border-radius: 2px;"
                            ng-options="rev for rev in specItemsVm.reqVersions"
                            ng-model="specItemsVm.reqSearchFilter.version1"
                            ng-change="specItemsVm.freeTextSearch(specItemsVm.reqMode)">
                    </select>
                </div>
            </td>

            <%--            <td style="min-width: 100px;text-align:center;">
                            <ui-select ng-model="specItemsVm.reqSearchFilter.status" style="width: 85px !important;"
                                       on-select="specItemsVm.freeTextSearch(specItemsVm.reqMode)"
                                       theme="bootstrap"
                                       style="width:100%">
                                <ui-select-match allow-clear="false"
                                                 placeholder="Status">


                                    <a class="btn btn-xs btn-link pull-right"
                                       ng-click="clear($event, $select)"><i
                                            class="glyphicon glyphicon-remove a"></i></a>

                                    {{$select.selected}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="status in specItemsVm.listStatus | filter: $select.search">
                                    <div ng-bind="status | highlight: $select.status.search"></div>
                                </ui-select-choices>
                            </ui-select>
                        </td>--%>
            <td class="" style="text-align: center; min-width: 100px;">
                <div style="min-width: 100px;">
                    <select class="form-control"
                            style="width: 100%;padding: 5px;border-radius: 2px;"
                            ng-options="status for status in specItemsVm.listStatus"
                            ng-model="specItemsVm.reqSearchFilter.status"
                            ng-change="specItemsVm.freeTextSearch(specItemsVm.reqMode)">
                    </select>
                </div>
            </td>
            <td class="status-col"
                style="text-align: center; min-width: 100px !important;">
                <form>
                    <ui-select multiple ng-model="specItemsVm.reqSearchFilter.assignedTo"
                               ng-change="specItemsVm.reInitializeColResize()" theme="bootstrap"

                               style="width:100%">
                        <ui-select-match placeholder="Select">{{$item.firstName}}
                        </ui-select-match>
                        <ui-select-choices style="margin-left: -380px !important;"
                                           repeat="person.id as person in specItemsVm.persons | filter: $select.firstName.search">
                            <div ng-bind="person.fullName"></div>
                        </ui-select-choices>
                    </ui-select>
                </form>

            </td>
            <td class="planned-finish-date-col" style="text-align: center; min-width: 100px">
                <div class="input-group">
                <form>
                    <input date-picker-edit class="form-control date-picker" type="text"
                           style="width: 134px !important;"
                           placeholder="{{'PLANNED_FINISH_DATE' | translate}}"
                           ng-model="specItemsVm.reqSearchFilter.plannedFinishdate"/>
                </form>

                </div>
            </td>
            <td class="added-column" ng-repeat="objectAttribute in specItemsVm.searchAttributes">
                <div class="planned-finish-date-col" ng-if="objectAttribute.objectTypeAttribute.dataType == 'DATE'"
                     style="text-align: center; width: 190px;">
                    <div class="input-group">

                        <input date-picker-edit class="form-control date-picker" type="text"
                               style="width: 134px !important;"
                               placeholder="Date"
                               ng-click="specItemsVm.search(objectAttribute)"
                               ng-model="objectAttribute.date"/>

                    </div>
                </div>

                       <span class="" style="text-align: center; width: 100px;"
                             ng-if="objectAttribute.objectTypeAttribute.dataType == 'TEXT'">
                         <input type="text" class="form-control" name="Name"
                                style="width: 90px !important;"
                                ng-change="specItemsVm.search(objectAttribute)"
                                placeholder="Text"
                                ng-model="objectAttribute.text"
                                ng-model-options="{ debounce: 500 }">
                      </span>

                   <span class="" style="text-align: center; width: 100px;"
                         ng-if="objectAttribute.objectTypeAttribute.dataType == 'LONGTEXT'">
                       <input type="text" class="form-control" name="Name"
                              style="width: 90px !important;"
                              ng-change="specItemsVm.search(objectAttribute)"
                              placeholder="Long Text"
                              ng-model="objectAttribute.longText"
                              ng-model-options="{ debounce: 500 }">
                        </span>

                       <span class="" style="text-align: center; width: 100px;"
                             ng-if="objectAttribute.objectTypeAttribute.dataType == 'INTEGER'">
                         <input type="text" class="form-control" name="Number" style="width: 85px !important;"
                                ng-change="specItemsVm.search(objectAttribute)"
                                placeholder="Integer"
                                ng-model="objectAttribute.integer"
                                ng-model-options="{ debounce: 500 }">
                        </span>

                       <span class="" style="text-align: center; width: 100px;"
                             ng-if="objectAttribute.objectTypeAttribute.dataType == 'CURRENCY'">
                       <input type="text" class="form-control" name="Number" style="width: 85px !important;"
                              ng-change="specItemsVm.search(objectAttribute)"
                              placeholder="Currency Value"
                              ng-model="objectAttribute.currency"
                              ng-model-options="{ debounce: 500 }">
                    </span>
                   <span class="" style="text-align: center; width: 100px;"
                         ng-if="objectAttribute.objectTypeAttribute.dataType == 'DOUBLE'">
                     <input type="text" class="form-control" name="Number" style="width: 85px !important;"
                            ng-change="specItemsVm.attributeBoolean(objectAttribute)"
                            placeholder="Double Value"
                            ng-model="objectAttribute.aDouble"
                            ng-model-options="{ debounce: 500 }">
                        </span>

                 <span style="width: 150px;text-align:center;"
                       ng-if="objectAttribute.objectTypeAttribute.dataType == 'BOOLEAN'">
                      <ui-select ng-model="objectAttribute.aBoolean" style="width: 85px !important;"
                                 on-select="specItemsVm.attributeBoolean(objectAttribute)"
                                 theme="bootstrap"
                                 style="width:100%">
                          <ui-select-match allow-clear="false"
                                           placeholder="Boolean">


                              <a class="btn btn-xs btn-link pull-right"
                                 ng-click="clear($event, $select)"><i
                                      class="glyphicon glyphicon-remove a"></i></a>

                              {{$select.selected}}
                          </ui-select-match>
                          <ui-select-choices
                                  repeat="boolean in specItemsVm.booleanValue | filter: $select.search">
                              <div ng-bind="boolean | highlight: $select.boolean.search"></div>
                          </ui-select-choices>
                      </ui-select>
               </span>


              <span style="text-align: center; width: 100px !important;"
                    ng-if="objectAttribute.objectTypeAttribute.dataType == 'LIST' && objectAttribute.objectTypeAttribute.listMultiple == false">
                <ui-select ng-model="objectAttribute.list"
                           ng-change="specItemsVm.search(objectAttribute)" theme="bootstrap"

                           style="width:100%">
                    <ui-select-match placeholder="Select">{{$select.selected}}
                    </ui-select-match>
                    <ui-select-choices
                            repeat="lovValue in specItemsVm.lovValues | filter: $select.search">
                        <div ng-bind="lovValue"></div>
                    </ui-select-choices>
                </ui-select>

                </span>
                <span style="text-align: center; width: 100px !important;"
                      ng-if="objectAttribute.objectTypeAttribute.dataType == 'LIST' && objectAttribute.objectTypeAttribute.listMultiple == true">
                       <ui-select multiple ng-model="objectAttribute.mListvalue" theme="bootstrap"
                                  on-select="specItemsVm.mListValueSelect(objectAttribute,$item)"
                                  on-remove="specItemsVm.removeMlistValue(objectAttribute,$item)"
                                  checkboxes="true"
                                  close-on-select="false" title="Choose a List" remove-selected="true">
                           <ui-select-match placeholder="Select listValues...">{{$item}}</ui-select-match>
                           <ui-select-choices repeat="lovValue in specItemsVm.lovValues | filter: $select.search">
                               <div ng-bind="lovValue"></div>
                           </ui-select-choices>
                       </ui-select>
                 </span>

                <span class="" style="text-align: center; width: 100px;"
                      ng-if="objectAttribute.objectTypeAttribute.dataType == 'TIME'">
                <input type="text" class="form-control" name="Number" style="width: 85px !important;"
                       ng-click="specItemsVm.attributeBoolean(objectAttribute)"
                       placeholder="Time Value"
                       ng-model="objectAttribute.time"
                       ng-model-options="{ debounce: 500 }" time-picker>
                 </span>
            </td>
            <td style="width: 92px !important;">
                <div class="btn-group btn-group-xs" style="display: inline-flex !important;">

                    <button
                            title="{{searchTitle}}" type="button" class="btn btn-xs btn-success"
                            ng-click="specItemsVm.freeTextSearch(specItemsVm.reqMode)">
                        <i class="fa fa-search"></i>
                    </button>
                    <button title="{{clearTitle}}" type="button" class="btn btn-xs btn-default"
                            ng-click="specItemsVm.clearFilter()">
                        <i class="fa fa-times"></i>
                    </button>
                </div>
            </td>
        </tr>
        <tbody class="connectedSortable">


        <tr ng-if="specItemsVm.loading == true">
            <td colspan="14"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                  class="mr5"><span translate>LOADING_ITEMS</span>
            </td>
        </tr>
        <tr ng-if="specItemsVm.loading == false && specItemsVm.pageMode == 'SECTIONS' && specItemsVm.specItems.length == 0">
            <td colspan="14" style="color: unset !important;">
                <div class="no-data">
                    <img src="app/assets/no_data_images/affectedItems.png" alt="" class="image">

                    <div class="message">{{ 'NO_ITEMS' | translate}} </div>
                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                </div>
            </td>
        </tr>
        <tr ng-if="specItemsVm.loading == false && specItemsVm.pageMode == 'REQUIREMENTS' && specItemsVm.specRequirements.content.length == 0">
            <td colspan="14" ng-if="!specItemsVm.requirementSearch" style="color: unset !important;">
                <div class="no-data">
                    <img src="app/assets/no_data_images/Requirements.png" alt="" class="image">

                    <div class="message">{{ 'NO_REQUIREMENTS' | translate}} </div>
                    <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                </div>
            </td>

            <td colspan="14" ng-if="specItemsVm.requirementSearch"><span translate>NO Search Results found</span></td>
        </tr>
        <tr id="{{$index}}" ng-show="specItemsVm.pageMode == 'SECTIONS'" ng-repeat="item in specItemsVm.specItems"
        <%--drag-me drop-on-me--%>
            ng-class="{'section-row': item.type == 'SECTION'}">
            <td class="add-col">
                <span ng-if="item.type == 'SECTION'"
                      ng-class="{disabled:selectedSpecification.lifecyclePhase.phaseType == 'RELEASED'}">
                    <span uib-dropdown dropdown-append-to-body>
                        <i class="la la-plus dropdown-toggle" uib-dropdown-toggle
                           title="{{specItemsVm.addRootSection}}"
                           ng-if="hasPermission('pgcspecification','edit') || specPermission.editPermission == true"
                           ng-disabled="selectedSpecification.lifecyclePhase.phaseType == 'RELEASED'"></i>
                        <ul uib-dropdown-menu class="dropdown-menu" role="menu" style="z-index: 9999 !important;">
                            <li><a href ng-click="specItemsVm.createSection(item)"
                                   ng-show="item.canAddSection" translate>ADD_SECTION</a></li>
                            <li><a href ng-click="specItemsVm.createRequirement(item)"
                                   ng-show="item.canAddRequirement" translate>ADD_REQUIREMENT</a></li>
                        </ul>
                    </span>
                </span>

                <div ng-if="item.type == 'REQUIREMENT'" class="ckbox ckbox-default" style="display: inline-block;">
                    <input id="item{{$index}}" name="itemSelected" type="checkbox" ng-value="true"
                           ng-model="item.selected" ng-change="specItemsVm.selectRequirement(item)">
                    <label for="item{{$index}}" class="item-selection-checkbox"></label>
                </div>
            </td>
            <td class="seq-col handle hover">
                <span ng-if="item.type == 'SECTION'"><img src="app/assets/images/note.png" alt=""></span>
                <span ng-if="item.type == 'REQUIREMENT'"><img src="app/assets/images/req.png" alt=""></span>
                <span>{{item.seqNumber}}</span>
            </td>
            <td class="" style="min-width: 300px;">
                <div class="level{{item.level}}" <%--style="max-width: 263px !important;min-width: 263px !important;"--%>>
                    <i class="mr5 fa" ng-if="item.type == 'SECTION'" title="{{specItemsVm.ExpandCollapse}}"
                       style="cursor: pointer !important; font-size: 15px;"
                       ng-class="{'fa-caret-right': (item.expanded == false || item.expanded == null || item.expanded == undefined),
                           'fa-caret-down': item.expanded == true}"
                       ng-click="specItemsVm.toggleNode(item)"></i>
                    <span ng-if="item.type == 'SECTION'" ng-click="specItemsVm.toggleNode(item)" class="autoClick"
                          style="cursor: pointer !important;white-space: normal;word-wrap: break-word;"
                          ng-bind-html="item.name">
                    </span>
                    <span ng-if="item.type == 'REQUIREMENT'" style="white-space: normal;word-wrap: break-word;">
                        <div style="-webkit-column-width: 250px;-moz-column-width: 250px;column-width: 250px;
                     webkit-column-count: 1;-moz-column-count: 1;column-count: 1;">
                            <span ng-bind-html="item.requirement.name"></span>
                        </div>

                    </span>
                </div>
            </td>
            <td class="" style="min-width: 600px;">
                <span ng-if="item.type == 'SECTION'" style="white-space: normal;word-wrap: break-word;cursor: pointer">
                     <div <%--style="max-width:263px !important; min-width:263px !important;"--%>
                             title="{{item.description}}">
                         <span ng-bind-html="item.description | limitTo: 30"></span>
                     </div>
                    </span>
                <span ng-if="item.type == 'REQUIREMENT'"
                      style="white-space: normal;word-wrap: break-word;">
                     <div style="-webkit-column-width: 250px;-moz-column-width: 250px;column-width: 250px;
                     webkit-column-count: 1;-moz-column-count: 1;column-count: 1;margin-top: 9px;">
                         <span style="max-height: 250px !important;display:block;overflow:auto !important;"
                               ng-bind-html="item.requirement.description"></span>
                     </div>
                   </span>
            </td>

            <td class="number-col" style="min-width: 100px;">
                <span ng-if="item.type == 'REQUIREMENT'">
                    <a href="" class="notification1" ng-click="specItemsVm.showRequirementDetails(item.requirement)"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">{{item.requirement.objectNumber}}
                        <%--<span class="badge12" ng-if="item.reqEdits > 0" title="{{'NO_OF_REQ_EDITS' | translate}}">{{item.reqEdits}}</span>--%>
                    </a>
                    <span class="label label-default" ng-if="item.reqEdits > 0"
                          title="{{'NO_OF_REQ_EDITS' | translate}}"
                          style="font-size: 12px;background-color: rgb(139, 152, 167);padding: 1px 4px;"
                          ng-bind-html="item.reqEdits"></span>
                </span>

                <span class="label label-default" ng-if="item.type == 'SECTION'"
                      title="{{'NO_OF_REQ_EDITS' | translate}}"
                      style="font-size: 12px;background-color: rgb(139, 152, 167);padding: 1px 4px;"
                      ng-bind-html="item.requirementsEdit"></span>
            </td>

            <td class="number-col" style="text-align: center;min-width: 100px;">
                <span ng-if="item.requirement.version > 0"
                      style="width: 85px !important;">{{item.requirement.version}}</span>
                <span ng-if="item.requirement.version == 0" style="width: 85px !important;">-</span>
            </td>

            <td class="status-col" style="min-width: 100px;">
                <span ng-if="item.type == 'REQUIREMENT'"><span><task-status
                        task="item.requirement"></task-status></span></span>
            </td>
            <td class="assigned-col" style="min-width: 100px;">
                <span ng-if="item.type == 'REQUIREMENT'" ng-bind-html="item.requirement.assignedTo.fullName"></span>
            </td>
            <td class="planned-finish-date-col" style="min-width: 100px;">
                <span ng-if="item.type == 'REQUIREMENT'">{{item.requirement.plannedFinishDate}}</span>
            </td>

            <td class="added-column"

                ng-repeat="objectAttribute in specItemsVm.selectedAttribute">
                <p class="limit"
                   ng-class="{'tl': objectAttribute.dataType == 'TEXT' || objectAttribute.dataType == 'LONGTEXT'}"
                   ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy' && objectAttribute.dataType != 'LIST' && objectAttribute.dataType != 'BOOLEAN'
                        && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'OBJECT' && objectAttribute.dataType != 'RICHTEXT'"
                   ng-init="attrName = objectAttribute.id">
                    <a href=""
                       ng-if="objectAttribute.refType == 'ITEM'"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                       ng-click="specItemsVm.showAttributeDetails(item[attrName])">{{item[attrName].itemNumber}}
                    </a>
                    <a href=""
                       ng-if="objectAttribute.refType == 'ITEMREVISION'"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                       ng-click="specItemsVm.showAttributeDetails(item[attrName])">
                        {{item[attrName].itemMaster+" "+item[attrName].revision+" "+
                        item[attrName].lifeCyclePhase.phase}}
                    </a>
                    <a href=""
                       ng-if="objectAttribute.refType == 'CHANGE'"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                       ng-click="specItemsVm.showAttributeDetails(item[attrName])">{{item[attrName].ecoNumber}}
                    </a>
                    <a href=""
                       ng-if="objectAttribute.refType == 'MANUFACTURER'"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                       ng-click="specItemsVm.showAttributeDetails(item[attrName])">{{item[attrName].name}}
                    </a>
                    <a href=""
                       ng-if="objectAttribute.refType == 'MANUFACTURERPART'"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                       ng-click="specItemsVm.showAttributeDetails(item[attrName])">{{item[attrName].partNumber}}
                    </a>
                    <a href=""
                       ng-if="objectAttribute.refType == 'WORKFLOW'"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                       ng-click="specItemsVm.showAttributeDetails(item[attrName])">{{item[attrName].name}}
                    </a>
                    <a href="#" ng-if="objectAttribute.refType == 'PERSON'">
                        {{item.requirement.assignedTo[attrName].firstName}}
                    </a>
                </p>

                <div class="attributeTooltip"
                     ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy' && objectAttribute.dataType != 'LIST' && objectAttribute.dataType != 'BOOLEAN'
                             && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'ATTACHMENT' && objectAttribute.dataType != 'RICHTEXT'"
                     ng-init="attrName = objectAttribute.id">
                    <p>
                        <a ng-if="item[attrName].length > 0" href="">
                            {{item[attrName].length}} Attachments
                        </a>
                    </p>

                    <div class="attributeTooltiptext">
                        <ul>
                            <li ng-repeat="attachment in item[attrName]">
                                <a href="" ng-click="specItemsVm.openAttachment(attachment)"
                                   title="{{'CLICK_TO_DOWNLOAD_ATTACHMENT' | translate}}"
                                   style="margin-bottom: 5px;width:200px;white-space: normal;word-wrap: break-word;color: #141f9f !important;">
                                    {{attachment.name}}
                                </a>
                            </li>
                        </ul>
                    </div>
                </div>

                <div class="limit"
                     ng-class="{'tl': objectAttribute.dataType == 'TEXT' || objectAttribute.dataType == 'LONGTEXT'}"
                     ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy' && objectAttribute.dataType != 'LIST' && objectAttribute.dataType != 'BOOLEAN'
                         && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'IMAGE' && objectAttribute.dataType != 'RICHTEXT'"
                     ng-init="attrName = objectAttribute.id">
                    <a href="" ng-click="specItemsVm.showThumbnailImage(objectAttribute)"
                       title="{{'CLICK_TO_SHOW_IMAGE' | translate}}">
                        <img ng-if="item[attrName] != null"
                             ng-src="{{item[attrName]}}"
                             style="height: 30px;width: 40px;margin-bottom: 5px;">
                    </a>

                    <div id="item-thumbnail{{objectAttribute.id}}" class="item-thumbnail modal">
                        <div class="item-thumbnail-content">
                            <div class="thumbnail-content" style="display: flex;width: 100%;">
                                <div class="thumbnail-view" id="thumbnail-view{{objectAttribute.id}}">
                                    <div id="thumbnail-image{{objectAttribute.id}}"
                                         style="display: table-cell;vertical-align: middle;text-align: center;">
                                        <img ng-src="{{item[attrName]}}"
                                             style="height: auto;width: auto;max-width: 100%;max-height: 90%"/>
                                                    <span class="thumbnail-close"
                                                          id="thumbnail-close{{objectAttribute.id}}"></span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div>
                    <p class="limit"
                       ng-class="{'tl': objectAttribute.dataType == 'LONGTEXT'}"
                       ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                        && objectAttribute.dataType != 'OBJECT' && objectAttribute.dataType != 'ATTACHMENT' && objectAttribute.dataType != 'BOOLEAN'
                        && objectAttribute.dataType != 'IMAGE' && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType != 'RICHTEXT' && objectAttribute.dataType != 'LIST' && objectAttribute.dataType != 'TEXT'"
                       ng-init="attrName = objectAttribute.id">
                        {{item[attrName]}}
                    </p>

                    <div class="attributeTooltip"
                         ng-if="objectAttribute.dataType == 'LIST' && objectAttribute.multiList"
                         ng-init="attrName = objectAttribute.id">
                        <p>
                            <a ng-if="item[attrName].length > 0" href="">
                                {{item[attrName].length}} Values
                            </a>
                        </p>

                        <div class="attributeTooltiptext">
                            <ul>
                                <li ng-repeat="value in item[attrName]">
                                    {{value}}
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>


                <div ng-if="objectAttribute.dataType == 'BOOLEAN' && item.type == 'REQUIREMENT'"
                     ng-init="attrName = objectAttribute.id">
                    <p ng-if="item[attrName] == true">{{item[attrName]}}</p>

                    <p ng-if="item[attrName] == false || item[attrName] == null">false</p>
                </div>


                <div ng-if="item.type == 'REQUIREMENT'">
                                 <span ng-if="objectAttribute.dataType == 'LIST' && (objectAttribute.listMultiple == true || objectAttribute.listMultiple == false)"
                                       ng-init="attrName = objectAttribute.id">
                                     <p ng-if="item[attrName] == null" ng-hide="item[attrName] != null">
                                         {{objectAttribute.defaultListValue}}</p>

                                     <p ng-if="item[attrName] != null">
                                         {{item[attrName]}}</p>
                                 </span>

                                 <span ng-if="objectAttribute.dataType == 'TEXT'"
                                       ng-init="attrName = objectAttribute.id">
                                     <p ng-if="item[attrName] == null" ng-hide="item[attrName] != null">
                                         {{objectAttribute.defaultTextValue}}</p>

                                     <p ng-if="item[attrName] != null">
                                         {{item[attrName]}}</p>
                                 </span>
                </div>


                <%--  Show Rich text content in modal --%>
                     <span style="height: 30px;width: 40px;margin-bottom: 5px;" ng-init="attrName = objectAttribute.id"
                           ng-if="objectAttribute.dataType == 'RICHTEXT'">
                          <a href=""
                             ng-if="item[attrName] != null && item[attrName] != undefined && item[attrName] != ''"
                             data-toggle="modal"
                             ng-click="showRichTextSidePanel(item[attrName],objectAttribute,item.requirement)"
                             translate>
                              CLICK_TO_SHOW_RICHTEXT</a>
                     </span>
                        <span ng-init="currencyType = objectAttribute.id+'type'"
                              ng-if="objectAttribute.dataType == 'CURRENCY'" ng-bind-html="item[currencyType]"
                                >
                        </span>

                 <span ng-init="attrName = objectAttribute.id"
                       ng-if="objectAttribute.dataType == 'CURRENCY'">
                            {{item[attrName]}}
                        </span>

                <p style="margin-top: 9px;" ng-if="objectAttribute.name == 'Modified By'">
                    {{item.requirement.modifiedByObject.firstName}}</p>

                <p style="margin-top: 9px;" ng-if="objectAttribute.name == 'Created By'">
                    {{item.requirement.createdByObject.firstName}}</p>

                <p style="margin-top: 9px;" ng-if="objectAttribute.name == 'Created Date'">
                    {{item.requirement.createdDate}}</p>

                <p style="margin-top: 9px;" ng-if="objectAttribute.name == 'Modified Date'">
                    {{item.requirement.modifiedDate}}</p>

            </td>


            <td class="text-center" id="dropDownAndDropUp">
                <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                    <i class="fa fa-ellipsis-h dropdown-toggle"
                       uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important; top:445px !important;">
                        <li ng-if="item.type == 'REQUIREMENT'"
                            ng-click="specItemsVm.showEditHistory(item)">
                            <a href="" translate>REQUIREMENT_EDIT_TITLE</a>
                        </li>
                        <li ng-if="item.type == 'REQUIREMENT'"
                            ng-click="specItemsVm.showRevisionHistory(item.requirement)">
                            <a href="" translate>REQUIREMENT_SHOW_VERSION_HISTORY</a>
                        </li>
                        <li ng-if="item.type == 'REQUIREMENT' && (hasPermission('admin','all') || hasPermission('pgcspecification','edit') || specPermission.editPermission == true)"
                            ng-hide="item.requirement.finalAcceptEdit || selectedSpecification.lifecyclePhase.phaseType == 'RELEASED' || item.requirement.workflow != null
                            ||  selectedSpecification.lifecyclePhase.phaseType == 'PRELIMINARY' || item.requirement.requirement.status == 'FINISHED'"
                            ng-click="specItemsVm.editRequirement(item)">
                            <a href="" translate>EDIT_REQUIREMENT</a>
                        </li>
                        <li ng-if="item.type == 'REQUIREMENT' && (hasPermission('admin','all') || hasPermission('pgcspecification','delete') || specPermission.deletePermission == true)"
                            ng-hide="selectedSpecification.lifecyclePhase.phaseType == 'RELEASED' || item.requirement.workflow != null"
                            ng-click="deleteSpecElement(item)">
                            <a href="" translate>DELETE_REQUIREMENT</a>
                        </li>
                        <li ng-if="item.type == 'SECTION' && (hasPermission('admin','all') || hasPermission('pgcspecification','edit') || specPermission.editPermission == true)"
                            ng-hide="selectedSpecification.lifecyclePhase.phaseType == 'RELEASED'"
                            ng-click="specItemsVm.editSection(item)">
                            <a href="" translate>EDIT_SECTION</a>
                        </li>
                        <li ng-if="item.type == 'SECTION' && (hasPermission('admin','all') || hasPermission('pgcspecification','delete') || specPermission.deletePermission == true)"
                            ng-class="{'disabled':selectedSpecification.lifecyclePhase.phaseType == 'RELEASED'}"
                            ng-click="deleteSpecElement(item)">
                            <a href="" translate>DELETE_SECTION</a>
                        </li>

                        <li ng-if="item.type == 'REQUIREMENT'"
                            ng-click="specItemsVm.compareVersion(item.requirement)">
                            <a href="" translate>COMPARE</a>
                        </li>

                    </ul>
                </span>
            </td>
        </tr>

        <tr ng-if="specItemsVm.pageMode == 'REQUIREMENTS'"
            ng-repeat="item in specItemsVm.specRequirements.content">
            <td>
                <div ng-if="item.type == 'REQUIREMENT'" class="ckbox ckbox-default" style="display: inline-block;">
                    <input id="item{{$index}}" name="itemSelected" type="checkbox" ng-value="true"
                           ng-model="item.selected" ng-change="specItemsVm.selectRequirement(item)">
                    <label for="item{{$index}}" class="item-selection-checkbox"></label>
                </div>
            </td>
            <td>
                <span ng-bind-html="item.seqNumber | highlightText: specItemsVm.searchText"></span>
            </td>
            <td>
                <span style="white-space: normal;word-wrap: break-word;">
                        <div style="-webkit-column-width: 250px;-moz-column-width: 250px;column-width: 250px;
                     margin-top: 3px !important;webkit-column-count: 1;-moz-column-count: 1;column-count: 1;">
                            <span ng-bind-html="item.requirement.name | highlightText: specItemsVm.searchText"></span>
                        </div>
                    </span>
            </td>

            <td>
                <%--<span style="white-space: normal;word-wrap: break-word;">
                    <div style="max-width:250px !important; min-width:250px !important;overflow: auto !important;max-height: 250px !important;margin-bottom: -11px !important;">
                        <span ng-bind-html="item.requirement.description"></span>
                    </div>
                    </span>--%>

                <span
                        style="white-space: normal;word-wrap: break-word;">
                     <div style="-webkit-column-width: 250px;-moz-column-width: 250px;column-width: 250px;
                            webkit-column-count: 1;-moz-column-count: 1;column-count: 1;margin-top: 9px;">
                         <span style="max-height: 250px !important;display:block;overflow:auto !important;"
                               ng-bind-html="item.requirement.description"></span>
                     </div>
                   </span>

            </td>
            <td>
                  <span>
                       <a href="" ng-click="specItemsVm.showRequirementDetails(item.requirement)">
                           <span ng-bind-html="item.requirement.objectNumber | highlightText: specItemsVm.searchText"></span>
                       </a>
                      <span class="label label-default" ng-if="item.reqEdits > 0"
                            title="{{'NO_OF_REQ_EDITS' | translate}}"
                            style="font-size: 12px;background-color: rgb(139, 152, 167);padding: 1px 4px;"
                            ng-bind-html="item.reqEdits"></span>
                </span>

            </td>
            <td class="number-col" style="text-align: center">
                <span ng-if="item.requirement.version > 0"
                      style="width: 85px !important;">{{item.requirement.version}}</span>
                <span ng-if="item.requirement.version == 0" style="width: 85px !important;">-</span>
            </td>
            <td>
                <task-status task="item.requirement"></task-status>
            </td>
            <td>
                <span ng-bind-html="item.requirement.assignedTo.fullName | highlightText: specItemsVm.searchText"></span>
            </td>
            <td>
                {{item.requirement.plannedFinishDate}}
            </td>

            <td class="added-column"

                ng-repeat="objectAttribute in specItemsVm.selectedAttribute">
                <p class="limit"
                   ng-class="{'tl': objectAttribute.dataType == 'TEXT' || objectAttribute.dataType == 'LONGTEXT'}"
                   ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy' && objectAttribute.dataType != 'LIST' && objectAttribute.dataType != 'BOOLEAN'
                        && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'OBJECT' && objectAttribute.dataType != 'RICHTEXT'"
                   ng-init="attrName = objectAttribute.id">
                    <a href=""
                       ng-if="objectAttribute.refType == 'ITEM'"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                       ng-click="specItemsVm.showAttributeDetails(item[attrName])">{{item[attrName].itemNumber}}
                    </a>
                    <a href=""
                       ng-if="objectAttribute.refType == 'ITEMREVISION'"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                       ng-click="specItemsVm.showAttributeDetails(item[attrName])">
                        {{item[attrName].itemMaster+" "+item[attrName].revision+" "+
                        item[attrName].lifeCyclePhase.phase}}
                    </a>
                    <a href=""
                       ng-if="objectAttribute.refType == 'CHANGE'"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                       ng-click="specItemsVm.showAttributeDetails(item[attrName])">{{item[attrName].ecoNumber}}
                    </a>
                    <a href=""
                       ng-if="objectAttribute.refType == 'MANUFACTURER'"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                       ng-click="specItemsVm.showAttributeDetails(item[attrName])">{{item[attrName].name}}
                    </a>
                    <a href=""
                       ng-if="objectAttribute.refType == 'MANUFACTURERPART'"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                       ng-click="specItemsVm.showAttributeDetails(item[attrName])">{{item[attrName].partNumber}}
                    </a>
                    <a href=""
                       ng-if="objectAttribute.refType == 'WORKFLOW'"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                       ng-click="specItemsVm.showAttributeDetails(item[attrName])">{{item[attrName].name}}
                    </a>
                    <a href="#" ng-if="objectAttribute.refType == 'PERSON'">
                        {{item.requirement.assignedTo[attrName].firstName}}
                    </a>
                </p>

                <div class="attributeTooltip"
                     ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy' && objectAttribute.dataType != 'LIST' && objectAttribute.dataType != 'BOOLEAN'
                             && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'ATTACHMENT' && objectAttribute.dataType != 'RICHTEXT'"
                     ng-init="attrName = objectAttribute.id">
                    <p>
                        <a ng-if="item[attrName].length > 0" href="">
                            {{item[attrName].length}} Attachments
                        </a>
                    </p>

                    <div class="attributeTooltiptext">
                        <ul>
                            <li ng-repeat="attachment in item[attrName]">
                                <a href="" ng-click="specItemsVm.openAttachment(attachment)"
                                   title="{{'CLICK_TO_DOWNLOAD_ATTACHMENT' | translate}}"
                                   style="margin-bottom: 5px;width:200px;white-space: normal;word-wrap: break-word;color: #141f9f !important;">
                                    {{attachment.name}}
                                </a>
                            </li>
                        </ul>
                    </div>
                </div>

                <div class="limit"
                     ng-class="{'tl': objectAttribute.dataType == 'TEXT' || objectAttribute.dataType == 'LONGTEXT'}"
                     ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy' && objectAttribute.dataType != 'LIST' && objectAttribute.dataType != 'BOOLEAN'
                         && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'IMAGE' && objectAttribute.dataType != 'RICHTEXT'"
                     ng-init="attrName = objectAttribute.id">
                    <a href="" ng-click="specItemsVm.showImage(item[attrName])"
                       title="{{'CLICK_TO_SHOW_IMAGE' | translate}}">
                        <img ng-if="item[attrName] != null"
                             ng-src="{{item[attrName]}}"
                             style="height: 30px;width: 40px;margin-bottom: 5px;">
                    </a>

                    <div id="myModal2" class="img-model modal">
                        <span class="closeImage1">&times;</span>
                        <img class="modal-content" id="img03">
                    </div>
                </div>
                <div>
                    <p class="limit"
                       ng-class="{'tl': objectAttribute.dataType == 'LONGTEXT'}"
                       ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                        && objectAttribute.dataType != 'OBJECT' && objectAttribute.dataType != 'ATTACHMENT' && objectAttribute.dataType != 'BOOLEAN'
                        && objectAttribute.dataType != 'IMAGE' && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType != 'RICHTEXT' && objectAttribute.dataType != 'LIST' && objectAttribute.dataType != 'TEXT'"
                       ng-init="attrName = objectAttribute.id">
                        {{item[attrName]}}
                    </p>

                    <div class="attributeTooltip"
                         ng-if="objectAttribute.dataType == 'LIST' && objectAttribute.multiList"
                         ng-init="attrName = objectAttribute.id">
                        <p>
                            <a ng-if="item[attrName].length > 0" href="">
                                {{item[attrName].length}} Values
                            </a>
                        </p>

                        <div class="attributeTooltiptext">
                            <ul>
                                <li ng-repeat="value in item[attrName]">
                                    {{value}}
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>


                <div ng-if="objectAttribute.dataType == 'BOOLEAN' && item.type == 'REQUIREMENT'"
                     ng-init="attrName = objectAttribute.id">
                    <p ng-if="item[attrName] == true">{{item[attrName]}}</p>

                    <p ng-if="item[attrName] == false || item[attrName] == null">false</p>
                </div>


                <div ng-if="item.type == 'REQUIREMENT'">
                                 <span ng-if="objectAttribute.dataType == 'LIST' && (objectAttribute.listMultiple == true || objectAttribute.listMultiple == false)"
                                       ng-init="attrName = objectAttribute.id">
                                     <p ng-if="item[attrName] == null" ng-hide="item[attrName] != null">
                                         {{objectAttribute.defaultListValue}}</p>

                                     <p ng-if="item[attrName] != null">
                                         {{item[attrName]}}</p>
                                 </span>

                                 <span ng-if="objectAttribute.dataType == 'TEXT'"
                                       ng-init="attrName = objectAttribute.id">
                                     <p ng-if="item[attrName] == null" ng-hide="item[attrName] != null">
                                         {{objectAttribute.defaultTextValue}}</p>

                                     <p ng-if="item[attrName] != null">
                                         {{item[attrName]}}</p>
                                 </span>
                </div>


                <%--  Show Rich text content in modal --%>
                     <span style="height: 30px;width: 40px;margin-bottom: 5px;" ng-init="attrName = objectAttribute.id"
                           ng-if="objectAttribute.dataType == 'RICHTEXT'">
                          <a href=""
                             ng-if="item[attrName] != null && item[attrName] != undefined && item[attrName] != ''"
                             data-toggle="modal"
                             ng-click="showRichTextSidePanel(item[attrName],objectAttribute,item.requirement)"
                             translate>
                              CLICK_TO_SHOW_RICHTEXT</a>
                     </span>
                        <span ng-init="currencyType = objectAttribute.name+'type'"
                              ng-if="objectAttribute.dataType == 'CURRENCY'" ng-bind-html="item[currencyType]"
                                >
                        </span>

                 <span ng-init="attrName = objectAttribute.id"
                       ng-if="objectAttribute.dataType == 'CURRENCY'">
                            {{item[attrName]}}
                        </span>

                <p style="margin-top: 9px;" ng-if="objectAttribute.name == 'ModifiedBy'">
                    {{item.requirement.modifiedByObject.firstName}}</p>

                <p style="margin-top: 9px;" ng-if="objectAttribute.name == 'CreatedBy'">
                    {{item.requirement.createdByObject.firstName}}</p>

                <p style="margin-top: 9px;" ng-if="objectAttribute.name == 'CreatedDate'">
                    {{item.requirement.createdDate}}</p>

                <p style="margin-top: 9px;" ng-if="objectAttribute.name == 'ModifiedDate'">
                    {{item.requirement.modifiedDate}}</p>

            </td>

            <td class="text-center" style="width: 50px;">
                <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important; top:445px !important;">
                        <li ng-if="item.type == 'REQUIREMENT'"
                            ng-click="specItemsVm.showEditHistory(item)">
                            <a href="" translate>REQUIREMENT_EDIT_TITLE</a>
                        </li>
                        <li ng-if="item.type == 'REQUIREMENT'"
                            ng-click="specItemsVm.showRevisionHistory(item.requirement)">
                            <a href="" translate>REQUIREMENT_SHOW_VERSION_HISTORY</a>
                        </li>
                        <li ng-if="item.type == 'REQUIREMENT' && (hasPermission('admin','all') || hasPermission('pgcspecification','edit') || specPermission.editPermission == true)"
                            ng-hide="item.requirement.finalAcceptEdit || selectedSpecification.lifecyclePhase.phaseType == 'RELEASED' ||  selectedSpecification.lifecyclePhase.phaseType == 'PRELIMINARY' || item.requirement.requirement.status == 'FINISHED'"
                            ng-click="specItemsVm.editRequirement(item)">
                            <a href="" translate>EDIT_REQUIREMENT</a>
                        </li>
                        <li ng-if="item.type == 'REQUIREMENT' && (hasPermission('admin','all') || hasPermission('pgcspecification','delete') || specPermission.deletePermission == true)"
                            ng-class="{'disabled':selectedSpecification.lifecyclePhase.phaseType == 'RELEASED'}"
                            ng-click="deleteSpecElement(item)">
                            <a href="" translate>DELETE_REQUIREMENT</a>
                        </li>
                    </ul>
                </span>
            </td>
        </tr>
        </tbody>
    </table>
    <div style="height: 80px">

    </div>
</div>
