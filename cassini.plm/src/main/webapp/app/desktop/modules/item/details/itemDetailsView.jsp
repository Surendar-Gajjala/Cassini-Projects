<div>
    <style scoped>


        .bomCompareModal .responsive-table {
            top: 0 !important;
            overflow: auto;
            height: 93% !important;
        }

        .bomCompareModal .responsive-table table thead th {
            position: -webkit-sticky !important;
            position: sticky !important;;
            top: -10px !important;;
            z-index: 5 !important;;
            background-color: #fff !important;;
        }

        .item-number {
            display: inline-block;
        }

        .item-rev {
            font-size: 16px;
            font-weight: normal;
        }

        .tab-content {
            padding: 0px !important;
        }

        .tab-content .tab-pane {
            overflow: auto !important;
        }

        .tab-pane {
            position: relative;
        }

        .tab-content .tab-pane .responsive-table {
            height: 100%;
            position: absolute;
            overflow: auto !important;
            padding: 5px;
        }

        .tab-content .tab-pane .responsive-table table thead th {
            position: -webkit-sticky;
            position: sticky;
            top: -10px !important;
            z-index: 5;
        }

        table {
            table-layout: fixed;
        }

        .bomCompareModal {
            display: none; /* Hidden by default */
            position: fixed; /* Stay in place */
            padding-top: 20px; /* Location of the box */
            left: 0;
            top: 0;
            width: 100%; /* Full width */
            height: 100%; /* Full height */
            overflow: hidden !important; /* Enable scroll if needed */
            background-color: rgb(0, 0, 0); /* Fallback color */
            background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
        }

        .bomCompareModal .compareContent {
            margin: auto;
            display: block;
            height: 94%;
            width: 97%;
            background-color: white;
            border-radius: 7px !important;
        }

        .compareHeadre {
            padding: 5px;
            text-align: center;
            border-bottom: 1px solid lightgrey;
            height: 50px;

        }

        .bomCompareModalBody {
            padding: 10px;
            overflow: hidden;
            min-width: 100%;
            width: auto;
            height: 90% !important;
        }

        .strike {
            text-decoration: line-through;
        }

        .strikeText {
            /*text-decoration: line-through;*/
            color: orange !important;
        }

        .red_color {
            /* font-weight: bold !important;*/
            color: #D43F3A !important;
        }

        .green_color {
            /*  font-weight: bold !important;*/
            color: #1CAF9A !important;
        }

        .white_color {
            /* font-weight: bold !important;*/
            color: #5c6876 !important;
        }

        .orange_color {
            /*    font-weight: bold !important;*/
            color: #ec8c04 !important;
        }

        .dialogue {
            width: 95% !important;
        }

        .legend {
            list-style: none;
            text-align: center;
        }

        .legend li {
            display: inline-block;
            margin-left: 10px;
        }

        .legend span {
            border: 1px solid #ccc;
            float: left;
            width: 12px;
            height: 12px;
            margin: 2px;
        }

        /* your colors */
        .legend .nonChanges {
            background-color: black !important;
            margin: 4px !important;
            cursor: pointer !important;
        }

        .legend .deleted {
            background-color: #D43F3A !important;
            margin: 4px !important;
            cursor: pointer !important;
        }

        .legend .added {
            background-color: #1CAF9A !important;
            margin: 4px !important;
            cursor: pointer !important;
        }

        .legend .changes {
            margin: 4px !important;
            background-color: #ec8c04 !important;
            cursor: pointer !important;
        }

        #freeTextSearchDirective {
            top: 7px !important;
            right: 7px !important;
        }

        .item-header {
            font-size: 18px;
            color: #1CAF9A !important;
        }

        .att-header {
            font-size: 16px;
            color: #337ab7 !important;
        }

        .myOwnBg {
            /* background-color: red !important;*/
            cursor: not-allowed !important;
            /*pointer-events: none !important;*/
        }

        .attribute-table {
            font-family: "Trebuchet MS", Arial, Helvetica, sans-serif;
            border-collapse: collapse;
            table-layout: fixed !important;
        }

        .attribute-table td, .attribute-table th {
            border: 1px solid #ddd;
            padding: 8px;
        }

        .split-pane-divider {
            z-index: 1 !important;
        }

        td.sameWidth {
            width: 50px !important;
            text-align: center !important;
        }

        div.vertical {
            position: absolute;
            width: 100px;
            transform: rotate(-90deg);
            -webkit-transform: rotate(-90deg); /* Safari/Chrome */
            -moz-transform: rotate(-90deg); /* Firefox */
            -o-transform: rotate(-90deg); /* Opera */
            -ms-transform: rotate(-90deg); /* IE 9 */
            line-height: 40px;
            padding-left: 25px;
            margin-left: -35px;
            white-space: nowrap !important;

        }

        th.vertical {
            height: 100px;
            line-height: 40px;
            padding-bottom: 20px;
            text-align: left;
            width: 30px;
        }

        .colorAdding {
            background-color: #a9f2c2;
            /*background-image: url('app/assets/images/tick.svg');*/
            background-repeat: no-repeat;
            background-position: center center;
            background-size: 100% 100%, auto;
            z-index: 1 !important;
        }

        .att-name {
            text-align: center;
        }

        /*  .bomRuleexclusionBody {
              margin: 0 22px 0 0 !important;
          }*/

        .inclusionModel {
            display: none; /* Hidden by default */
            position: fixed; /* Stay in place */
            padding-top: 20px; /* Location of the box */
            left: 0;
            top: 0;
            width: 100%; /* Full width */
            height: 100%; /* Full height */
            overflow: auto; /* Enable scroll if needed */
            background-color: rgb(0, 0, 0); /* Fallback color */
            background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
        }

        .inclusionModel .bomRulecompareContent {
            margin: auto;
            display: block;
            height: 94%;
            width: 97%;
            background-color: white;
            border-radius: 7px !important;
        }

        .bomRulecompareHeadre {
            padding: 5px;
            text-align: center;
            border-bottom: 1px solid lightgrey;
            height: 50px;

        }

        .bomRuleexclusionBody {
            padding: 10px;
            overflow: auto;
            min-width: 100%;
            width: auto;
        }

        .config-closeBomInclusion {
            position: absolute !important;
            right: 35px !important;
            top: 25px !important;
            width: 38px !important;
            height: 38px !important;
            opacity: 0.3 !important;
        }

        .config-closeBomInclusion:hover {
            opacity: 0.6 !important;
            border-radius: 50% !important;
            background-color: #ddd !important;
        }

        .config-closeBomInclusion:before, .config-closeBomInclusion:after {
            position: absolute !important;
            top: 7px !important;
            left: 18px !important;
            content: ' ' !important;
            height: 22px !important;
            width: 2px !important;
            background-color: #333 !important;
        }

        .config-closeBomInclusion:before {
            transform: rotate(45deg) !important;
        }

        .config-closeBomInclusion:after {
            transform: rotate(-45deg) !important;
        }

        .exclusionModel {
            display: none; /* Hidden by default */
            position: fixed; /* Stay in place */
            padding-top: 20px; /* Location of the box */
            left: 0;
            top: 0;
            width: 100%; /* Full width */
            height: 100%; /* Full height */
            overflow: auto; /* Enable scroll if needed */
            background-color: rgb(0, 0, 0); /* Fallback color */
            background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
        }

        .exclusionModel .bomItemRulecompareContent {
            margin: auto;
            display: block;
            height: 94%;
            width: 97%;
            background-color: white;
            border-radius: 7px !important;
        }

        .bomItemRulecompareHeadre {
            padding: 5px;
            text-align: center;
            border-bottom: 1px solid lightgrey;
            height: 50px;

        }

        .bomRuleItemexclusionBody {
            padding: 10px;
            overflow: auto;
            min-width: 100%;
            width: auto;
        }

        .myOwnBg {
            /* background-color: red !important;*/
            cursor: not-allowed !important;
            /*pointer-events: none !important;*/
        }

        .itemattribute-table {
            border-collapse: collapse;
            table-layout: fixed !important;
        }

        .itemattribute-table td, .itemattribute-table th {
            border: 1px solid #ddd;
            padding: 8px;
        }

        .split-pane-divider {
            z-index: 1 !important;
        }

        td.itemsameWidth {
            width: 50px !important;
            text-align: center !important;
        }

        div.itemvertical {
            position: absolute;
            width: 100px;
            transform: rotate(-90deg);
            -webkit-transform: rotate(-90deg); /* Safari/Chrome */
            -moz-transform: rotate(-90deg); /* Firefox */
            -o-transform: rotate(-90deg); /* Opera */
            -ms-transform: rotate(-90deg); /* IE 9 */
            line-height: 40px;
            padding-left: 25px;
            margin-left: -35px;
            white-space: nowrap !important;
        }

        th.itemvertical {
            height: 100px;
            line-height: 40px;
            padding-bottom: 20px;
            text-align: left;
            width: 30px;
        }

        .itemColor {
            background-color: #f4b7c8;
            background-repeat: no-repeat;
            background-position: center center;
            background-size: 100% 100%, auto;
        }

        .modalClose {
            font-size: 40px !important;
            color: #0e0e0e !important;
            opacity: 0.3 !important;
            border: 0 !important;
        }

        .modalClose:hover {
            opacity: 0.8 !important;
        }

        .hr {
            display: block;
            height: 1px;
            border: 0;
            border-top: 1px solid #ccc;
            margin: 0 -5px 0px -5px;
            padding: 0;
        }

        .unselectable {
            /*background-color: #f4b7c8;*/
            cursor: not-allowed !important;
        }

        .view-toolbar .ui-select-match a.btn {
            margin-right: 10px;
            border: 0 !important;
            height: 18px !important;
            margin-top: -3px !important;
        }

        .view-toolbar .ui-select-match a i {
            font-size: 10px !important;
        }

        .col-width-250-compare {
            word-wrap: break-word;
            min-width: 200px;
            width: 200px !important;
            white-space: normal !important;
            text-align: left;
        }

        #dLabel {
            outline: none;
        }

        .dropdown-submenu {
            position: relative;
        }

        .dropdown-submenu > .dropdown-menu {
            top: 0;
            left: 100%;
            margin-top: -6px;
            margin-left: 1px;
            -webkit-border-radius: 0 6px 6px 6px;
            -moz-border-radius: 0 6px 6px;
            border-radius: 1px 6px 6px 6px;
        }

        .dropdown-submenu:hover > .dropdown-menu {
            display: block;
        }

        .dropdown-submenu > a:after {
            display: block;
            content: " ";
            float: right;
            width: 0;
            height: 0;
            border-color: transparent;
            border-style: solid;
            border-width: 5px 0 5px 5px;
            border-left-color: #ccc;
            margin-top: 5px;
            margin-right: -10px;
        }

        .dropdown-submenu:hover > a:after {
            border-left-color: #fff;
        }

        .dropdown-submenu.pull-left {
            float: none;
        }

        .dropdown-submenu.pull-left > .dropdown-menu {
            left: -100%;
            margin-left: 10px;
            -webkit-border-radius: 6px 0 6px 6px;
            -moz-border-radius: 6px 0 6px 6px;
            border-radius: 6px 0 6px 6px;
        }

        .itemCompareModal {
            display: none; /* Hidden by default */
            position: fixed; /* Stay in place */
            padding-top: 20px; /* Location of the box */
            left: 0;
            top: 0;
            width: 100%; /* Full width */
            height: 100%; /* Full height */
            overflow: auto; /* Enable scroll if needed */
            background-color: rgb(0, 0, 0); /* Fallback color */
            background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
        }

        .itemCompareModal .itemCompareContent {
            margin: auto;
            display: block;
            height: 94%;
            width: 97%;
            background-color: white;
            border-radius: 7px !important;
        }

        .itemCompareHeadre {
            padding: 5px;
            text-align: center;
            border-bottom: 1px solid lightgrey;
            height: 50px;

        }

        .itemCompareModalBody {
            padding: 10px;
            overflow: auto;
            min-width: 100%;
            width: auto;
            height: 90% !important;
        }

        .legends .compareFilter {
            margin: -33px 0 0 0 !important;
        }

        .legends .filter-message {
            margin: -33px 0 0 17px !important;
        }

    </style>

    <%--  Item To Item Comparision--%>

    <div class="modal fade itemCompareModal" id="myModalForItemCompare" data-backdrop="static"
         data-keyboard="true">

        <div class="itemCompareContent">
            <div class="itemCompareHeadre">
                <h4 class="text-secondary" translate style="text-align: center!important;">ITEM_COMPARISION</h4>
                <a href="" class="config-closeBomInclusion pull-right" data-dismiss="modal"
                   ng-click="itemVm.closeModalForItemCompare()"
                   style="display: inline-block;top: 7px;right: 10px;"></a>

            </div>

            <div class="itemCompareModalBody">
                <%--Legends--%>


                <div style="height: 100% !important;">
                    <div class="row" style="height: 100% !important;">
                        <div class="responsive-table" style="padding: 10px;">
                            <table class="table table-striped" style="width: 100% !important;"
                                   id="specifications2">

                                <thead>
                                <tr class="noBorder">
                                    <th class="" translate></th>
                                    <th class="" translate>
                                        {{itemToItemFromItem}}
                                    </th>
                                    <th class="" translate>
                                        {{itemToItemToItem}}
                                    </th>

                                </tr>
                                </thead>
                                <tbody>
                                <tr ng-repeat="value in itemToItemElements">
                                    <td style="font-weight: 600 !important;width: 29% !important;">
                                    <span style="white-space: normal;word-wrap: break-word;">
                                        <div style="-webkit-column-width: 250px;-moz-column-width: 250px;column-width: 250px;
                                                                  webkit-column-count: 1;-moz-column-count: 1;column-count: 1;">
                                            <span ng-bind-html="value.propertyName"></span>
                                        </div>


                                     </span>

                                    </td>
                                    <td class="">
                                    <span style="white-space: normal;word-wrap: break-word;">
                                        <div <%--style="-webkit-column-width: 250px;-moz-column-width: 250px;column-width: 250px;
                                                                  webkit-column-count: 1;-moz-column-count: 1;column-count: 1;"--%>
                                                class="col-width-250">
                                            <span ng-bind-html="value.newValue"></span>
                                        </div>


                                     </span>

                                    </td>
                                    <td class="">
                                    <span style="white-space: normal;word-wrap: break-word;">
                                        <div <%--style="-webkit-column-width: 250px;-moz-column-width: 250px;column-width: 250px;
                                                                  webkit-column-count: 1;-moz-column-count: 1;column-count: 1;"--%>
                                                class="col-width-250">
                                               <span ng-if="value.oldValue ==''" ng-bind-html="value.oldValue">
                                    </span>
                                            <span class="col-width-250"
                                                  ng-class="{'strikeText': value.oldValue !='' && value.changesMade==true}"
                                                  ng-if="value.oldValue !=''" ng-bind-html="value.oldValue">
                                    </span>
                                        </div>


                                     </span>


                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </div>

    <%--
       Modal for Item to Item Exclusion
       --%>
    <div class="exclusionModel modal fade" id="myModalForItemExclusions"
         data-backdrop="static"
         data-keyboard="true">

        <div class="bomItemRulecompareContent">
            <div class="modal-header bomItemRulecompareHeadre">
                <h4 class="text-secondary" translate style="text-align: center!important;">Item To Item
                    Exclusions</h4>
                <a href="" class="config-closeBomInclusion pull-right" data-dismiss="modal"
                   ng-click="itemVm.closeItemExclusionModal()"
                   style="display: inline-block;top: 7px;right: 10px;"></a>

            </div>


            <div class="bomRuleItemexclusionBody modal-body">
                <p class="text-success text-center"
                   ng-if="exclmsg == true">
                    {{exclSaveMessage}}</p>

                <table class="itemattribute-table" style="margin: 0 auto !important;">
                    <thead>
                    <tr>
                        <th colspan="3" rowspan="4"></th>

                    </tr>
                    <tr>
                        <th class="item-header att-name" ng-repeat="header1 in itemtoItemchildrenHeaders"
                            style="text-align: center !important;"
                            colspan="{{header1.length}}" title="{{header1.name}}">
                            {{header1.name }}{{header1.name.length > 20 ? '...' : ''}}

                        </th>
                    </tr>
                    <tr>

                        <th class="att-header att-name" ng-repeat="header in itemnameHeaders"
                            style="" title="{{header.name}}"
                            colspan="{{header.numValues}}">
                            {{header.name}}
                        </th>
                    </tr>
                    <tr>
                        <th class="itemvertical"
                            ng-repeat="value in itemfinalValuesForChildren track by $index">
                            <div class="itemvertical" style=""
                                 title="{{value.value}}">
                                {{value.value}}

                            </div>
                        </th>
                    </tr>
                    </thead>
                    <tbody>

                    <tr ng-repeat="val in itemfinalValuesForChildren track by $index"
                        ng-init='pair = shouldAddRowHeaderForItem($index); pair1 = shouldAddRowHeaderForItemSubHeader($index);'>
                        <td class="item-header att-name" rowspan="{{pair1[1]}}" ng-if="pair1[1] > 0"
                            style="font-weight: bold !important;"
                            title="{{pair1[0]}}">
                            {{pair1[0] }}{{pair1[0].length > 20 ? '...' : ''}}
                        </td>

                        <td class="att-header att-name" rowspan="{{pair[1]}}" ng-if="pair[1] > 0"
                            title="{{pair[0]}}"
                            style="font-weight: bold !important;">
                            {{pair[0] | limitTo: 10 }}{{pair[0].length > 10 ? '...' : ''}}
                        </td>
                        <td class="att-value" style="font-weight: bold;"
                            title="{{val.value}}">
                            {{val.value | limitTo: 10 }}{{val.value.length > 10 ? '...' : ''}}

                        </td>
                        <td ng-repeat="val1 in itemfinalValuesForChildren track by $index"
                            class="itemsameWidth"
                            style="cursor: pointer;"
                            ng-click="val.itemName == val1.itemName || createItemToItemExclusionObj(val,val1)"
                            ng-disabled="val.itemName == val1.itemName"
                            ng-class="[checkItemToItemExclusion(val,val1), {'myOwnBg': val.itemName == val1.itemName}]">
                                          <span ng-if="val.itemName != val1.itemName"
                                                title="{{val.itemName}} {{val.key}}({{val.value}}) - {{val1.itemName}} {{val1.key}}({{val1.value}})"> {{val.value|
                                          limitTo:
                                          1}}{{val1.value| limitTo: 1}}</span>
                        </td>
                    </tr>
                    </tbody>
                </table>

            </div>

        </div>


    </div>

    <%--
    Modal for Item to Bom Inclusions
    --%>
    <div class="modal fade inclusionModel" id="myModalForBomRules"
         data-backdrop="static"
         data-keyboard="true">
        <div class="bomRulecompareContent">
            <div class="modal-header bomRulecompareHeadre">
                <div class=""
                     style="display: inline-block;padding-right: 20px;position: relative;top: -3px;left: -100px;">
                    <button class="btn btn-xs btn-primary" ng-click="inclusionAll()"
                            translate>
                        ADD_ALL_INCLUSIONS
                    </button>
                    <button class="btn btn-xs btn-warning"
                            ng-click="resetAllInclusions()"
                            translate>
                        CLEAR_ALL
                    </button>
                </div>

                <h4 translate style="display: inline-block;text-align: center!important;" class="text-secondary">
                    BOM_INCLUSION_RULES</h4>

                <%--<button type="button" class="close modalClose" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>--%>
                <a href="" class="config-closeBomInclusion pull-right" data-dismiss="modal"
                   ng-click="itemVm.closeBomInlcusionModal()"
                   style="display: inline-block;top: 7px;right: 10px;"></a>
            </div>
            <div class="row bomRuleexclusionBody modal-body" style="margin-bottom: 20px;">
                <div class="col-md-12" style="max-height: 25px">
                    <p style="text-align: center; font-size: 15px;height: 25px;" class="text-success text-center">
                        {{inclSaveMessage}}</p>

                    <p style="text-align: center; font-size: 15px;height: 25px;" class="text-warning text-center">
                        {{errorSaveMessage}}</p>
                </div>
                <div class="col-sm-12" style="display: inline !important;">
                    <div class="row">
                        <div class="" id="container" style="padding-bottom: 30px;">
                            <table class="attribute-table" style="margin: 0 auto !important;">
                                <thead>
                                <tr>
                                    <%-- <th colspan="2" rowspan="4"></th>--%>
                                    <th colspan="3" rowspan="4"></th>

                                </tr>
                                <tr>
                                    <%-- <th colspan="2" rowspan="4"></th>--%>
                                    <%--<th colspan="3" rowspan="4"></th>--%>
                                    <th class="item-header att-name" ng-repeat="header1 in childrenItemHeaders"
                                        style="text-align: center !important;"
                                        colspan="{{header1.length}}" title="{{header1.name}}">
                                        {{header1.name }}{{header1.name.length > 25 ? '...' : ''}}
                                    </th>
                                </tr>
                                <tr>

                                    <th class="att-header att-name" ng-repeat="header in nameHeaders"
                                        style=""
                                        colspan="{{header.numValues}}" title="{{header.name}}">
                                        {{header.name}}
                                    </th>
                                </tr>
                                <tr>
                                    <th class="vertical" ng-repeat="value in finalValuesForChildren">
                                        <div class="vertical" style=""
                                             title="{{value.value}}">
                                            {{value.value}}
                                        </div>
                                    </th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td class="item-header att-value" style="font-weight: bold;"
                                        colspan="1" rowspan="{{finalValuesForParent.length+1}}"
                                        title="{{parentItemObj.itemName}}">
                                        {{parentItemObj.itemName}}
                                    </td>
                                </tr>
                                <tr ng-repeat="val in finalValuesForParent track by $index"
                                    ng-init="pair = shouldAddRowHeader($index)">
                                    <td class="att-header att-name" rowspan="{{pair[1]}}" ng-if="pair[1] > 0"
                                        style="font-weight: bold !important;"
                                        title="{{pair[0]}}"> {{pair[0] |
                                        limitTo: 10 }}{{pair[0].length > 10 ? '...' : ''}}
                                    <td class="att-value" style="font-weight: bold;"
                                        title="{{val.value}}">
                                        {{val.value | limitTo: 10 }}{{val.value.length > 10 ? '...' : ''}}
                                    </td>
                                    <td ng-repeat="val1 in finalValuesForChildren track by $index" class="sameWidth"
                                        style="cursor: pointer;"
                                        ng-init="title1 = val.itemName+' '+val.key+'('+val.value+') - '+val1.itemName+' '+val1.key+'('+val1.value+')'"
                                        ng-attr-title="{{ bomConfigCombinations.indexOf(title1) == -1 ? title1 : 'Combination already in use we can not un-check'}}"
                                        ng-click="createBomInlcusionExclusionObj(val,val1)"
                                        ng-class="[checkBomInclusion(val,val1), {'unselectable': bomConfigCombinations.indexOf(title1) != -1}]">
                                          <span>
                                              {{val.value| limitTo:1}}{{val1.value| limitTo: 1}}</span>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

        </div>

    </div>
    <%--
    Modal for Bom Compare
    --%>
    <div class="modal fade bomCompareModal" id="myModal1" data-backdrop="static"
         data-keyboard="true">

        <div class="compareContent">
            <div class="compareHeadre">
                <div style="text-align: center;font-weight: bold !important;" class="text-secondary">

                    <div>
                        <h4 class="text-secondary"
                            title="{{fromItemName}} ({{fromItemNumber}} - Rev {{fromItemRev}}) - {{toItemName}} ({{toItemNumber}} - Rev {{toItemRev}})"
                            style="overflow: hidden;white-space: nowrap;text-overflow: ellipsis;width: 90%;display: inline-block;">
                            <span>{{fromItemName  }}{{fromItemName.length > 20 ? '...' : ''}}({{fromItemNumber}}<span>-Rev</span>- {{fromItemRev}})</span>
                            -
                            <span>{{toItemName  }}{{toItemName.length > 20 ? '...' : ''}}({{toItemNumber}} <span>-Rev</span>- {{toItemRev}})</span>
                        </h4>
                    </div>

                </div>

                <a href="" class="config-closeBomInclusion pull-right" data-dismiss="modal"
                   ng-click="itemVm.closeModal()"
                   style="display: inline-block;top: 7px;right: 10px;"></a>

            </div>

            <div class="bomCompareModalBody">
                <%--Legends--%>


                <div class="legends">
                    <ul class="legend">
                        <li ng-click="itemVm.showAddedItems()"><span class="added"></span> Item added</li>
                        <li ng-click="itemVm.showChangedItems()"><span class="changes"></span> Item has changes</li>
                        <li ng-click="itemVm.showDeletedItems()"><span class="deleted"></span> Item does not exist</li>
                        <li ng-click="itemVm.showNoChangeItems()"><span class="nonChanges"></span>Item has no changes
                        </li>
                    </ul>

                    <div class="pull-left filter-message" ng-if="itemVm.bomCompareFilter==true">
                    <span>
                        {{itemVm.filterMessage}}
                    </span>
                    </div>
                    <div class="pull-right compareFilter" ng-if="itemVm.bomCompareFilter==true"
                            >
                        <button class="btn btn-xs btn-warning"
                                ng-click="itemVm.clearBomCompareFilter()"
                                translate>
                            BOM_COMPARE_CLEAR_FILTER
                        </button>
                    </div>

                </div>


                <div style="height: 100% !important;">
                    <div class="row" style="height: 100% !important;">
                        <div class="responsive-table" style="padding: 10px;">
                            <table class="table table-striped" style="width: 100% !important;"
                                   id="specifications1">

                                <thead>
                                <tr class="noBorder">
                                    <th style="width:1px !important;white-space: nowrap !important;" translate>
                                        ITEM_NUMBER
                                    </th>
                                    <th class="" translate style="z-index: auto !important;">ITEM_TYPE</th>
                                    <th class="col-width-250-compare" translate style="z-index: auto !important;">
                                        ITEM_NAME
                                    </th>
                                    <th class="col-width-250" translate>DESCRIPTION</th>
                                    <th class="" translate style="z-index: auto !important;">REVISION</th>
                                    <th class="" translate style="z-index: auto !important;">LIFE_CYCLE_PHASE</th>
                                    <th class="text-center" translate style="z-index: auto !important;">QUANTITY</th>
                                    <th style="text-align: center" translate>UNITS</th>
                                    <th class="col-width-250-compare" style="z-index: auto !important;" translate>
                                        REF_DES
                                    </th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr ng-if="itemList.length == 0">
                                    <td colspan="100"><span translate>NO_ITEMS</span></td>
                                </tr>
                                <tr ng-repeat="value in itemList" ng-init="color = value.color">
                                    <td style="width:1px !important;white-space: nowrap !important;">
                                        <span class="{{color}} level{{value.level}}">
                                          {{value.item.itemNumber}}
                                        </span>
                                    </td>
                                    <td>
                                        <span class="{{color}}">
                                          {{value.item.itemType.name}}
                                        </span>
                                    </td>
                                    <td class="col-width-250-compare">
                                        <span class="{{color}}">
                                          {{value.item.itemName}}
                                        </span>
                                    </td>
                                    <td class="col-width-250-compare">
                                        <span class="{{color}}">
                                          {{value.item.description}}
                                        </span>
                                    </td>
                                    <td>
                                    <span class="strike {{color}}"
                                          ng-if="value.updatedRevision !=null">{{value.rev}}</span>&nbsp;
                                        <span class="{{color}}" ng-if="value.updatedRevision != null">
                                            {{value.updatedRevision}}
                                        </span>
                                         <span class="{{color}}" ng-if="value.updatedRevision == null">
                                            {{value.rev}}
                                        </span>
                                    </td>


                                    <td>
                                        <span class="strike {{color}}" ng-if="value.updatedLifeCycle !=null">{{value.lifeCycle}}</span>
                                        &nbsp;
                                        <span class="{{color}}" ng-if="value.updatedLifeCycle != null">
                                            {{value.updatedLifeCycle}}
                                        </span>
                                         <span class="{{color}}" ng-if="value.updatedLifeCycle == null">
                                            {{value.lifeCycle}}
                                        </span>
                                    </td>


                                    <td style="text-align: center !important;">
                                        <span class="strike {{color}}" ng-if="value.updatedQty != null">
                                            {{value.quantity}}
                                        </span>
                                        <span class="{{color}}" ng-if="value.updatedQty == null">
                                            {{value.quantity}}
                                        </span> &nbsp;
                                        <span class="{{color}}"
                                              ng-if="value.updatedQty !=null">{{value.updatedQty}}</span>
                                    </td>

                                    <td style="text-align: center !important;">
                                        <span class="{{color}}">
                                            {{value.item.units}}
                                        </span>

                                    <td class="col-width-250-compare">
                                        <span class="strike {{color}}"
                                              ng-if="value.updatedDefdes != null && value.refdes !=null">
                                            {{value.refdes}}
                                        </span>
                                    <span class="{{color}}"
                                          ng-if="value.updatedDefdes !=null">{{value.updatedDefdes}}</span>
                                        <span class="{{color}}"
                                              ng-if="value.updatedDefdes ==null && value.refdes !=null">{{value.refdes}}</span>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </div>
    <div class="view-container" fitcontent>

        <div ng-if="loginPersonDetails.external == false" class="view-toolbar">
            <div class="btn-group">
                <button class="btn btn-sm btn-default" ng-click="itemVm.showItems()"
                        title="{{'SHOW_ALL' | translate}}">
                    <i class="fa fa-table" aria-hidden="true"></i>
                </button>
                <button class="btn btn-sm btn-default" title="{{'ITEM_DETAILS_REVISION_HISTORY' | translate}}"
                        ng-click="itemVm.showItemRevisionHistory()">
                    <i class="fa fa-history" aria-hidden="true" style=""></i>
                </button>
                <button title="{{'COPY_ITEM' | translate}}" ng-if="editItemPermission"
                        ng-disabled="item.configured || itemVm.item.lockObject  && loginPersonDetails.person.id != itemVm.item.lockedBy.id && !adminPermission"
                        class="btn btn-sm btn-default"
                        ng-click="itemVm.copyItem()">
                    <i class="fa fa-clipboard" aria-hidden="true" style=""></i>
                </button>
                <button title="{{itemVm.itemDetails.pendingEco ? 'Item has pending ECO':'ITEM_ECO' | translate}}"
                        ng-if="editItemPermission && itemVm.hasDisplayTab('changes') && itemVm.item.itemType.requiredEco && !itemVm.itemDetails.rejectedOrOldRevision"
                        ng-disabled="item.configured || itemVm.item.lockObject  && loginPersonDetails.person.id != itemVm.item.lockedBy.id && !adminPermission
                         || itemVm.itemDetails.pendingEco"
                        class="btn btn-sm btn-default"
                        ng-click="itemVm.createEco()">
                    <i class="fa fa-pencil-square-o" style="" aria-hidden="true"></i>
                </button>

                <div class="btn-group" ng-if="!itemVm.item.itemType.requiredEco && !item.configured">
                    <button class="btn btn-sm btn-success"
                            ng-hide="itemVm.itemRevision.lifeCyclePhase.phase == itemVm.firstLifecyclePhase.phase"
                            title="{{demoteItemPermission ? itemVm.demoteItemTitle : noPermission}}"
                            ng-class="{'cursor-override': !demoteItemPermission}"
                            ng-click="itemVm.demoteItem()">
                        <i class="fa fa-toggle-left"
                           style="" ng-class="{'disabled': !demoteItemPermission}"></i>
                    </button>
                    <button class="btn btn-sm btn-success"
                            title="{{promoteItemPermission ? itemVm.promoteItemTitle : noPermission}}"
                            ng-if="itemVm.itemRevision.lifeCyclePhase.phase != itemVm.lastLifecyclePhase.phase"
                            ng-class="{'cursor-override': !promoteItemPermission}"
                            ng-click="itemVm.promoteItem()">
                        <i class="fa fa-toggle-right"
                           ng-class="{'disabled': !promoteItemPermission}" style=""></i>
                    </button>
                    <button class="btn btn-sm btn-success" title="{{itemVm.reviseItemTitle}}"
                            ng-if="itemVm.itemRevision.released"
                            ng-click="itemVm.reviseItem()">
                        <i class="fa fa-random"></i>
                    </button>
                </div>

                <%--<button ng-if="itemVm.item.itemType.requiredEco && !item.configured && itemVm.item.latestRevision == itemVm.itemRevision.id && itemVm.itemRevision.lifeCyclePhase.phaseType == 'RELEASED' && itemVm.lastLifecyclePhase.phaseType == 'OBSOLETE'"
                        title="{{promoteItemPermission ? itemVm.promoteItemRevisionTitle : noPermission}}"
                        class="btn btn-sm btn-success" ng-click="itemVm.promoteItem()"
                        ng-class="{'cursor-override': !promoteItemPermission}">
                    <i class="fa fa-toggle-right" aria-hidden="true" style=""></i>
                </button>--%>
                <button ng-if="itemVm.tabs.files.active && editItemPermission && hasFiles == true"
                        ng-disabled="item.configured"
                        title="{{downloadTitle}}"
                        class="btn btn-sm btn-success" ng-click="downloadObjectFilesAsZip()">
                    <i class="fa fa-download" aria-hidden="true" style=""></i>
                </button>
                <div class="btn-group" ng-if="itemVm.tabs.bom.active && itemVm.hasDisplayTab('bom')">
                    <div class="btn-group">
                        <button class="btn btn-sm btn-maroon" ng-disabled="item.configured"
                                ng-click="itemVm.showTypeAttributes()" title="{{itemVm.bomItemAttributes}}">
                            <i class="fa fa-film" style=""></i>
                        </button>
                    </div>

                </div>

                <div class="btn-group" ng-if="itemVm.tabs.references.active && editItemPermission"
                     ng-hide="item.configured">
                    <button type="button" class="btn btn-success min-width dropdown-toggle" data-toggle="dropdown"
                            aria-haspopup="true" aria-expanded="false">
                        Add Item <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu">
                        <li><a href="" ng-click="itemVm.broadcast('app.item.addreference')">Single</a></li>
                        <li><a href="" ng-click="itemVm.broadcast('app.item')">Multiple</a></li>
                    </ul>
                </div>

                <div class="btn btn-sm btn-info"
                     onclick="$('#importFile').click();" title="{{itemVm.bomItemImport}}"
                     ng-if="itemVm.tabs.bom.active && !item.configured && !itemRevision.released && !itemRevision.rejected"
                     ng-disabled="item.configured || itemRevision.released || itemRevision.rejected">
                    <i class="fa fa-download" style="margin-top: 7px !important;"></i>
                </div>
                <div class="btn btn-sm btn-info" title="{{itemVm.bomItemImport}}"
                     ng-if="itemVm.tabs.bom.active && (item.configured || itemRevision.released || itemRevision.rejected)"
                     ng-disabled="item.configured || itemRevision.released || itemRevision.rejected">
                    <i class="fa fa-download" style="margin-top: 7px !important;"></i>
                </div>

                <input type="file" id="importFile" value="file"
                       onchange="angular.element(this).scope().importData1()"
                       style="display: none">

                <button class="btn btn-sm btn-success" ng-if="itemVm.tabs.bom.active"
                        ng-click="itemVm.exportData()" title="{{itemVm.bomItemExport}}">
                    <i class="fa fa-upload" style=""></i>
                </button>
                <button class="btn btn-sm"
                        ng-if="itemVm.tabs.bom.active && itemVm.item.configurable && showConfigEditor==true"
                        ng-click="loadBomConfigurationEditor()" title="{{bomConfigurationEditorTitle}}">
                    <i class="fa fa-sitemap" style=""></i>
                </button>
                <button class="btn btn-sm"
                        ng-if="itemVm.tabs.bom.active && itemRevision.hasBom"
                        ng-click="showBomRollupAttributes()" title="{{bomRollupReportTitle}}">
                    <i class="fa fa-calculator" style=""></i>
                </button>
                <button class="btn btn-sm"
                        ng-if="itemVm.tabs.bom.active && itemRevision.hasBom"
                        ng-click="loadBomWhereUsedReport()" title="{{bomWhereUsedReportTitle}}">
                    <i class="fa fa-server" style=""></i>
                </button>
                <button class="btn btn-sm btn-success"
                        title="{{itemVm.getCombination}}"
                        ng-show="itemVm.tabs.configured.active && item.configurable && !itemVm.itemRevision.hasBom"
                        ng-disabled="item.configured || itemRevision.released || itemRevision.rejected || !lockedObjectPermission"
                        ng-click="getAllCombinations()">
                    <i class="fa fa-book" style=""></i>
                </button>
                <button class="btn btn-sm btn-success"
                        title="{{itemVm.addWorkflowTitle}}"
                        ng-show="itemVm.tabs.workflow.active && itemRevision.workflow == null && !itemRevision.released && !itemRevision.rejected"
                        ng-click="itemVm.addWorkflow()">
                    <i class="fa flaticon-plan2 nav-icon-font" aria-hidden="true" style=""></i>
                </button>
                <button ng-if="itemVm.tabs.workflow.active"
                        ng-show="startWorkflow != true && itemRevision.workflow != null && !itemRevision.released && !itemRevision.rejected"
                        title="{{itemVm.changeWorkflowTitle}}"
                        class="btn btn-sm btn-success" ng-click="itemVm.changeWorkflow()">
                    <i class="fa fa-indent" aria-hidden="true" style=""></i>
                </button>
            </div>
            <button class="btn btn-sm btn-success"
                    title="{{itemVm.resloveBom}}"
                    ng-show="itemVm.tabs.bom.active && item.configured && showResolveBomButton"
                    ng-click="resolveBom()">
                <i class="fa fa-shield"></i>
            </button>
            <div class="btn-group">
                <div class="dropdown">

                    <button type="button" class="btn btn-sm btn-info dropdown-toggle" data-toggle="dropdown"
                            aria-haspopup="true" aria-expanded="false" title="{{'COMPARE' | translate }}">
                        <span><i class="fa fa-exchange" style="" aria-hidden="true"></i></span>
                        <span class="caret"></span>
                    </button>

                    <ul class="dropdown-menu multi-level" role="menu" aria-labelledby="dropdownMenu">

                        <li class="dropdown-submenu">
                            <a tabindex="-1" translate>ITEM_COMPARE</a>
                            <ul class="dropdown-menu" style="max-height: 400px;overflow-y: auto;">
                                <li ng-repeat="item in itemVm.toRevisionBomItems">
                                    <a tabindex="-1" ng-if="item.id !=null"
                                       ng-click="itemVm.selectRevisionItemToCompare(item)">{{item.itemName}}</a>
                                    <a tabindex="-1" ng-if="item.id ==null"
                                       ng-click="itemVm.selectRevisionItemToCompare(item)">{{item.itemName}}</a>
                                </li>
                            </ul>
                        </li>

                        <!--<li class="divider" ng-if="itemVm.tabs.bom.active && checkBomAvailOrNot == true"></li>-->
                        <li class="dropdown-submenu" ng-if="checkBomAvailOrNot == true">
                            <a tabindex="-1" translate>BOM_COMPARE</a>
                            <ul class="dropdown-menu" style="max-height: 400px;overflow-y: auto;">
                                <li ng-repeat="item in itemVm.toRevisionBomItems">
                                    <a tabindex="-1" ng-if="item.id !=null"
                                       ng-click="itemVm.selectToRevisionItem(item)">{{item.itemName}}</a>
                                    <a tabindex="-1" ng-if="item.id ==null"
                                       ng-click="itemVm.selectToRevisionItem(item)">{{item.itemName}}</a>
                                </li>
                            </ul>
                        </li>

                    </ul>
                </div>
            </div>
            <div class="btn-group">
                <button
                        title="{{ 'PREVIEW_AND_PRINT' | translate }}"
                        class="btn btn-sm"
                        ng-click="itemVm.getPrintOptions(itemVm.item)">
                    <i class="fa fa-print" aria-hidden="true" style=""></i>
                </button>
            </div>
            <button class="btn btn-sm btn-success"
                    ng-disabled="itemVm.itemDetails.specifications == 0"
                    title="{{itemVm.itemDetails.specifications == 0 ? addSpecification : complianceReport }}"
                    ng-show="itemVm.tabs.bom.active" ng-click="generateComplianceReport()">
                <i class="fa fa-list-alt"></i>
            </button>
            <button class="btn btn-sm btn-success" title="Incorporate"
                    ng-if="itemVm.item.latestRevision == itemVm.itemRevision.id && itemVm.itemRevision.released && !itemVm.itemRevision.incorporate && !item.configured"
                    ng-click="itemVm.updateIncorporate()">
                <i class="la la-user-edit"></i>
            </button>
            <button class="btn btn-sm btn-success" title="Unincorporate"
                    ng-if="itemVm.item.latestRevision == itemVm.itemRevision.id && itemVm.itemRevision.released && itemVm.itemRevision.incorporate && !item.configured"
                    ng-click="itemVm.updateUnIncorporate()">
                <i class="la la-user-slash"></i>
            </button>
            <!--<button class="btn btn-sm" ng-click="itemVm.updateItemConfigurableAttributes()">Update Attributes</button>-->

            <div class="pull-right" style="margin-right: 310px;">
                <!-- Custom actions and action groups -->
                <plugin-actions object-value="itemVm.item" object-revision="itemVm.itemRevision"
                                tab-custom-actions="itemVm.tabCustomActions"
                                tab-custom-action-groups="itemVm.tabCustomActionGroups"
                                custom-actions="itemVm.customActions"
                                custom-action-groups="itemVm.customActionGroups"></plugin-actions>


                <div class="btn-group" id="action-buttons">
                    <button class="btn btn-default"
                            ng-show="showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length == 0"
                            ng-click="copyObjectFilesToClipBoard()" title="{{copyFileToClipboard}}">
                        <i class="fa fa-copy" style=""></i>
                    </button>
                    <div class="btn-group" ng-show="showCopyObjectFilesToClipBoard && clipBoardObjectFiles.length > 0">
                        <button type="button" class="btn btn-sm btn-info dropdown-toggle" data-toggle="dropdown"
                                aria-haspopup="true" aria-expanded="false">
                            <span class="fa fa-copy" style=""></span><span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu" style="left: auto;right: -100px;">
                            <li ng-click="clearAndCopyObjectFilesToClipBoard()">
                                <a href="" translate="">CLEAR_AND_ADD_FILES</a></li>
                            <li ng-click="copyObjectFilesToClipBoard()">
                                <a href=""><span translate>ADD_TO_EXISTING_FILES</span>
                                    ({{clipBoardObjectFiles.length}})</a>
                            </li>
                        </ul>
                    </div>
                    <button class="btn btn-sm btn-default"
                            title="{{subscribeButtonTitle}}" <%--ng-disabled="item.configured"--%>
                            ng-click="itemVm.subscribeItem(itemVm.item)">
                        <i ng-if="itemVm.subscribe == null || (itemVm.subscribe != null && !itemVm.subscribe.subscribe)"
                           class="la la-bell" style=""></i>
                        <i ng-if="itemVm.subscribe != null && itemVm.subscribe.subscribe" class="la la-bell-slash"
                           style=""
                           title="{{'UN_SUBSCRIBE_TITLE' | translate }}"></i></button>
                    <button class="btn btn-sm btn-default" ng-if="editItemPermission"
                            ng-click="itemVm.shareItem()"
                            style=""
                            title="{{itemVm.detailsShareTitle}}">
                        <i class="las la-share" style=""></i></button>
                    <button class="btn btn-sm btn-default"
                            ng-click="itemVm.refreshDetails()"
                            style="margin-right: 10px;"
                            title="{{itemVm.refreshTitle}}">
                        <i class="fa fa-refresh" style=""></i></button>
                </div>
            </div>
            <free-text-search ng-if="itemVm.tabs.bom.active || itemVm.tabs.files.active"
                              search-term="freeTextQuerys" on-clear="itemVm.onClear"
                              on-search="itemVm.freeTextSearch"></free-text-search>
        </div>

        <div ng-if="loginPersonDetails.external == true" class="view-toolbar">
            <div class="btn-group">
                <button class="btn btn-sm btn-default" ng-click='itemVm.showExternalUserItems()'
                        title="{{'SHOW_ALL' | translate}}">
                    <i class="fa fa-table" aria-hidden="true"></i>
                </button>
            </div>

            <free-text-search ng-if="itemVm.tabs.files.active" on-clear="itemVm.onClear"
                              on-search="itemVm.freeTextSearch"></free-text-search>
        </div>

        <div class="view-content no-padding" style="overflow-y: hidden;">
            <div class="row row-eq-height" style="margin: 0;">
                <div class="col-sm-12" style="padding: 10px;">
                    <div class="item-details-tabs">
                        <scrollable-tabset show-drop-down="false">
                            <uib-tabset active="itemVm.active">
                                <uib-tab heading="{{itemVm.tabs.basic.heading}}" active="itemVm.tabs.basic.active"
                                         select="tabActivated(itemVm.tabs.basic.id)">
                                    <div ng-include="itemVm.tabs.basic.template"
                                         ng-controller="ItemBasicInfoController as itemBasicVm"></div>
                                </uib-tab>

                                <%--<uib-tab heading="{{'DETAILS_TAB_ATTRIBUTES' | translate}}"
                                         active="itemVm.tabs.attributes.active"
                                         select="tabActivated(itemVm.tabs.attributes.id)">
                                    <div ng-include="itemVm.tabs.attributes.template"
                                         ng-controller="ItemAttributesController as itemAttributesVm"></div>
                                </uib-tab>--%>
                                <uib-tab id="bom" ng-show="itemVm.hasDisplayTab('bom')"
                                         heading="{{itemVm.tabs.bom.heading}}"
                                         active="itemVm.tabs.bom.active"
                                         select="tabActivated(itemVm.tabs.bom.id)">
                                    <div ng-include="itemVm.tabs.bom.template"
                                         ng-controller="ItemBomController as itemBomVm"></div>
                                </uib-tab>
                                <uib-tab id="configured" ng-show="item.configurable"
                                         heading="{{itemVm.tabs.configured.heading}}"
                                         active="itemVm.tabs.configured.active"
                                         select="tabActivated(itemVm.tabs.configured.id)">
                                    <div ng-include="itemVm.tabs.configured.template"
                                         ng-controller="ItemConfiguredController as itemConfiguredVm"></div>
                                </uib-tab>
                                <uib-tab id="whereUser" ng-show="itemVm.hasDisplayTab('whereUsed')"
                                         heading="{{itemVm.tabs.whereUsed.heading}}"
                                         active="itemVm.tabs.whereUsed.active"
                                         select="tabActivated(itemVm.tabs.whereUsed.id)">
                                    <div ng-include="itemVm.tabs.whereUsed.template"
                                         ng-controller="ItemWhereUsedController as itemWhereUsedVm"></div>
                                </uib-tab>
                                <uib-tab id="changesTab" ng-show="itemVm.hasDisplayTab('changes')"
                                         heading="{{itemVm.tabs.changes.heading}}"
                                         active="itemVm.tabs.changes.active"
                                         select="tabActivated(itemVm.tabs.changes.id)">
                                    <div ng-include="itemVm.tabs.changes.template"
                                         ng-controller="ItemChangesController as itemChangesVm"></div>
                                </uib-tab>
                                <uib-tab id="variance" ng-show="itemVm.hasDisplayTab('variance')"
                                         heading="{{itemVm.tabs.variance.heading}}"
                                         active="itemVm.tabs.variance.active"
                                         select="tabActivated(itemVm.tabs.variance.id)">
                                    <div ng-include="itemVm.tabs.variance.template"
                                         ng-controller="ItemVarianceController as itemVarianceVm"></div>
                                </uib-tab>
                                <uib-tab id="qualityTab" ng-show="itemVm.hasDisplayTab('quality')"
                                         heading="{{itemVm.tabs.quality.heading}}"
                                         active="itemVm.tabs.quality.active"
                                         select="tabActivated(itemVm.tabs.quality.id)">
                                    <div ng-include="itemVm.tabs.quality.template"
                                         ng-controller="ItemQualityController as itemQualityVm"></div>
                                </uib-tab>

                                <uib-tab id="files" ng-show="itemVm.hasDisplayTab('files')"
                                         heading="{{'DETAILS_TAB_FILES' | translate}}"
                                         active="itemVm.tabs.files.active"
                                         select="tabActivated(itemVm.tabs.files.id)">
                                    <div ng-include="itemVm.tabs.files.template"
                                         ng-controller="ItemFilesController as itemFilesVm"></div>
                                </uib-tab>
                                <uib-tab id="mfr"
                                         ng-show="itemVm.hasDisplayTab('mfrParts') && itemVm.item.makeOrBuy == 'BUY'"
                                         heading="{{'ITEM_DETAILS_TAB_MANUFACTURER_PARTS' | translate}}"
                                         active="itemVm.tabs.mfr.active"
                                         select="tabActivated(itemVm.tabs.mfr.id)">
                                    <div ng-include="itemVm.tabs.mfr.template"
                                         ng-controller="ItemMfrController as itemMfrVm"></div>
                                </uib-tab>
                                <uib-tab id="relatedItems" ng-show="itemVm.hasDisplayTab('relatedItems')"
                                         heading="{{itemVm.tabs.relatedItems.heading}}"
                                         active="itemVm.tabs.relatedItems.active"
                                         select="tabActivated(itemVm.tabs.relatedItems.id)">
                                    <div ng-include="itemVm.tabs.relatedItems.template"
                                         ng-controller="RelatedItemsController as relatedItemsVm"></div>
                                </uib-tab>

                                <uib-tab id="projectItem" ng-show="itemVm.hasDisplayTab('projects')"
                                         heading="{{itemVm.tabs.projectItem.heading}}"
                                         active="itemVm.tabs.projectItem.active"
                                         select="tabActivated(itemVm.tabs.projectItem.id)">
                                    <div ng-include="itemVm.tabs.projectItem.template"
                                         ng-controller="ProjectItemController as projectItemsVm"></div>
                                </uib-tab>
                                <uib-tab id="requirements-tab" ng-show="itemVm.hasDisplayTab('requirements')"
                                         heading="{{itemVm.tabs.requirements.heading}}"
                                         active="itemVm.tabs.requirements.active"
                                         select="tabActivated(itemVm.tabs.requirements.id)">
                                    <div ng-include="itemVm.tabs.requirements.template"
                                         ng-controller="ItemRequirementsController as itemRequirementsVm"></div>
                                </uib-tab>
                                <uib-tab id="itemSpecifications" ng-show="itemVm.hasDisplayTab('specifications')"
                                         heading="{{itemVm.tabs.specifications.heading}}"
                                         active="itemVm.tabs.specifications.active"
                                         select="tabActivated(itemVm.tabs.specifications.id)">
                                    <div ng-include="itemVm.tabs.specifications.template"
                                         ng-controller="ItemSpecificationsController as itemSpecificationsVm"></div>
                                </uib-tab>


                                <uib-tab id="workflow" ng-show="!itemVm.item.itemType.requiredEco"
                                         heading="{{itemVm.tabs.workflow.heading}}"
                                         active="itemVm.tabs.workflow.active"
                                         select="tabActivated(itemVm.tabs.workflow.id)">
                                    <div ng-include="itemVm.tabs.workflow.template"
                                         ng-controller="ItemWorkflowController as itemWfVm"></div>
                                </uib-tab>

                                <!-- Custom tabs -->
                                <%--<uib-tab ng-repeat="customTab in itemVm.customTabs"
                                         id="{{customTab.id}}"
                                         class="custom-tab"
                                         heading="{{customTab.heading}}"
                                         active="customTab.active"
                                         select="tabActivated(customTab.id)">
                                    <div ng-include="customTab.template"
                                         dynamic-ctrl="customTab.controller"></div>
                                </uib-tab>--%>
                                <plugin-tabs tabs="itemVm.tabs"
                                             custom-tabs="itemVm.customTabs"
                                             object-value="itemVm.item" object-revision="itemVm.itemRevision"
                                             tab-id="itemVm.tabId" active="itemVm.active"></plugin-tabs>
                                <uib-tab id="inspections"
                                         ng-show="itemVm.item.itemType.itemClass == 'PRODUCT' && itemVm.hasDisplayTab('inspections')"
                                         heading="{{itemVm.tabs.inspection.heading}}"
                                         active="itemVm.tabs.inspection.active"
                                         select="tabActivated(itemVm.tabs.inspection.id)">
                                    <div ng-include="itemVm.tabs.inspection.template"
                                         ng-controller="ItemInspectionController as itemInspectionVm"></div>
                                </uib-tab>
                                <uib-tab id="itemHistory"
                                         heading="{{itemVm.tabs.itemTimelineHistory.heading}}"
                                         active="itemVm.tabs.itemTimelineHistory.active"
                                         select="tabActivated(itemVm.tabs.itemTimelineHistory.id)">
                                    <div ng-include="itemVm.tabs.itemTimelineHistory.template"
                                         ng-controller="ItemTimelineHistoryController as itemTimelineHistoryVm"></div>
                                </uib-tab>

                            </uib-tabset>
                        </scrollable-tabset>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>