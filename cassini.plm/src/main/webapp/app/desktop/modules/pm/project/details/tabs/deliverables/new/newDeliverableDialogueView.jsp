<style>
    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    .round1 {
        width: 50%;
        border-radius: 26px;
        padding: 4px 5px 7px 142px;
        top: 0;
        left: 0;
        z-index: 5;
        border: 1px #e6e8ed solid;
        background-color: rgb(241, 243, 244);
        outline: none;
    }

    .round1 > input:focus {
        border: 1px solid #ddd !important;
        box-shadow: none;
    }

    .fa-search {
        position: relative;
        margin-top: 11px;
        color: grey;
        opacity: 0.5;
        margin-right: -28px !important;
    }

    i.fa-times-circle {
        margin-left: -25px;
        color: gray;
        cursor: pointer;
    }

    #footer-content {
        border-bottom: 1px solid lightgrey;
        padding: 2px;
        text-align: center;
        height: 50px;
        width: 100%;
        display: flex;
    }

</style>

<div id="deliverablesView">
    <style scoped>
        #rightSidePanelContent {
            overflow: hidden !important;
        }

        .ui-select-bootstrap > .ui-select-match > .btn {
            padding-bottom: 1px !important;
        }
        .open > .dropdown-toggle.btn {
            color: #444442 !important;
        }
    </style>
    <div id="form-content" style="height: 40px;border-bottom: 1px solid lightgrey;" ng-hide="newDeliverableVm.glossary">
        <form class="form-inline ng-pristine ng-valid" style="padding: 5px;display: flex;flex-direction: row;"
              ng-if="newDeliverableVm.showItemDeliverable == true">
            <div class="form-group" style="flex-grow: 1;margin-right: 0;">
                <div class="input-group input-group-sm" style="">
                    <div class="input-group-btn dropdown" uib-dropdown="" style="">
                        <button class="btn btn-default dropdown-toggle" uib-dropdown-toggle type="button">
                            <span translate>SELECT</span> <span class="caret" style="margin-left: 4px;"></span>
                        </button>
                        <div class="dropdown-menu" role="menu"
                             style="z-index: 9999 !important; margin-right: -56px !important;left: 0;">
                            <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;height: 200px;">
                                <classification-tree
                                        on-select-type="newDeliverableVm.onSelectType"></classification-tree>
                            </div>
                        </div>
                    </div>
                    <input type="text" class="form-control ng-pristine ng-valid ng-touched ng-untouched" name="title"
                           style="background-color: #fff;" ng-model="newDeliverableVm.selectedType.name" readonly="">
                </div>
            </div>
            <div class="form-group" style="flex-grow: 2;margin-right: 5px;text-align: center;">
                <input type="text" ng-model="newDeliverableVm.filters.itemNumber"
                       ng-change="newDeliverableVm.searchItems()"
                       style="margin-left: -16px; width: 100%;height: 30px;margin-top: 0;"
                       placeholder="{{'ITEM_NUMBER' | translate}}"
                       class="input-sm form-control ng-pristine ng-valid ng-touched ng-untouched">
            </div>
            <div class="form-group" style="flex-grow: 2;margin-right: 0;text-align: center;">
                <input type="text" ng-model="newDeliverableVm.filters.name"
                       ng-change="newDeliverableVm.searchItems()"
                       style="margin-left: -16px; height: 30px;width: 100%;margin-top: 0;"
                       placeholder="{{'ITEM_NAME' | translate}}"
                       class="input-sm form-control ng-pristine ng-valid ng-touched">
            </div>
            <div style="margin-top: 0;flex-grow: 1;" class="">
                <button ng-click="newDeliverableVm.clearFilter()" style="height: 29px;width: 100%"
                        class="btn btn-xs btn-danger ng-scope" translate>CLEAR
                </button>
            </div>
        </form>

        <form class="form-inline ng-pristine ng-valid" style="padding: 5px;display: flex;flex-direction: row;"
              ng-if="newDeliverableVm.specification == true">
            <div class="form-group" style="flex-grow: 1;margin-right: 0;">
                <div class="input-group input-group-sm" style="">
                    <div class="input-group-btn dropdown" uib-dropdown="" style="">
                        <button class="btn btn-default dropdown-toggle" uib-dropdown-toggle type="button">
                            <span translate>SELECT</span> <span class="caret" style="margin-left: 4px;"></span>
                        </button>
                        <div class="dropdown-menu" role="menu"
                             style="z-index: 9999 !important; margin-right: -56px !important;left: 0;">
                            <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;height: 200px;">
                                <specification-tree
                                        on-select-type="newDeliverableVm.onSelectSpecType"></specification-tree>
                            </div>
                        </div>
                    </div>
                    <input type="text" class="form-control ng-pristine ng-valid ng-touched ng-untouched" name="title"
                           style="background-color: #fff;" ng-model="newDeliverableVm.selectedType.name" readonly="">
                </div>
            </div>
            <div class="form-group" style="flex-grow: 2;margin-right: 5px;text-align: center;">
                <input type="text" ng-model="newDeliverableVm.SpecReqfilters.name"
                       ng-change="newDeliverableVm.searchSpecifications()"
                       style="margin-left: -16px; width: 100%;height: 30px;margin-top: 0;"
                       placeholder="{{'NAME' | translate}}"
                       class="input-sm form-control ng-pristine ng-valid ng-touched ng-untouched">
            </div>
            <div class="form-group" style="flex-grow: 2;margin-right: 0;text-align: center;">
                <input type="text" ng-model="newDeliverableVm.SpecReqfilters.description"
                       ng-change="newDeliverableVm.searchSpecifications()"
                       style="margin-left: -16px; height: 30px;width: 100%;margin-top: 0;"
                       placeholder="{{'ITEM_ALL_DESCRIPTION' | translate}}"
                       class="input-sm form-control ng-pristine ng-valid ng-touched">
            </div>
            <div style="margin-top: 0;flex-grow: 1;" class="">
                <button ng-click="newDeliverableVm.clearSpecificationFilter()"
                         style="height: 29px;width: 100%"
                        class="btn btn-xs btn-danger ng-scope" translate>CLEAR
                </button>
            </div>
        </form>

        <form class="form-inline ng-pristine ng-valid" style="padding: 5px;display: flex;flex-direction: row;"
              ng-if="newDeliverableVm.requirement == true">
            <div class="form-group" style="flex-grow: 1;margin-right: 0;">
                <div class="input-group input-group-sm" style="">
                    <div class="input-group-btn dropdown" uib-dropdown="" style="">
                        <button class="btn btn-default dropdown-toggle" uib-dropdown-toggle type="button">
                            <span translate>SELECT</span> <span class="caret" style="margin-left: 4px;"></span>
                        </button>
                        <div class="dropdown-menu" role="menu"
                             style="z-index: 9999 !important; margin-right: -56px !important;left: 0;">
                            <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;height: 200px;">
                                <requirement-tree
                                        on-select-type="newDeliverableVm.onRequirementSelectType"></requirement-tree>
                            </div>
                        </div>
                    </div>
                    <input type="text" class="form-control ng-pristine ng-valid ng-touched ng-untouched" name="title"
                           style="background-color: #fff;" ng-model="newDeliverableVm.selectedType.name" readonly="">
                </div>
            </div>
            <div class="form-group" style="flex-grow: 2;margin-right: 5px;text-align: center;">
                <input type="text" ng-model="newDeliverableVm.SpecReqfilters.name"
                       ng-change="newDeliverableVm.searchRequirements()"
                       style="margin-left: -16px; width: 100%;height: 30px;margin-top: 0;"
                       placeholder="{{'NAME' | translate}}"
                       class="input-sm form-control ng-pristine ng-valid ng-touched ng-untouched">
            </div>
            <div class="form-group" style="flex-grow: 2;margin-right: 0;text-align: center;">
                <input type="text" ng-model="newDeliverableVm.SpecReqfilters.description"
                       ng-change="newDeliverableVm.searchRequirements()"
                       style="margin-left: -16px; height: 30px;width: 100%;margin-top: 0;"
                       placeholder="{{'ITEM_ALL_DESCRIPTION' | translate}}"
                       class="input-sm form-control ng-pristine ng-valid ng-touched">
            </div>
            <div style="margin-top: 0;flex-grow: 1;" class="">
                <button ng-click="newDeliverableVm.clearRequirementFilter()"
                         style="height: 29px;width: 100%"
                        class="btn btn-xs btn-danger ng-scope" translate>CLEAR
                </button>
            </div>
        </form>
    </div>
    <div id="footer-content">
        <div class="form-group" style="width: 100%;margin-top: 2px;padding-bottom: 0 !important;">
            <div class="col-sm-4">
                <%--<ui-select ng-model="selected" theme="bootstrap"
                           style="width:100%;padding-top: 2px;" on-select="switchType($item)">
                    <ui-select-match placeholder="{{newProjectVm.select}}">{{$select.selected}}
                    </ui-select-match>
                    <ui-select-choices
                            repeat="list in newDeliverableVm.deliverableList">
                        <div ng-bind-html="list"></div>
                    </ui-select-choices>
                </ui-select>--%>
            </div>
            <div class="col-sm-8">
                <div ng-if="newDeliverableVm.showItemDeliverable == true" class="pull-right" style="padding: 10px;">
                <span>
                    <medium>
                                <span style="margin-right: 10px;">
                                    <span translate>DISPLAYING</span>
                                    <span ng-if="newDeliverableVm.projectDeliverables.numberOfElements ==0">
                            {{(newDeliverableVm.pageable.page*newDeliverableVm.pageable.size)}}
                        </span>

                                    <span ng-if="newDeliverableVm.projectDeliverables.numberOfElements > 0">
                            {{(newDeliverableVm.pageable.page*newDeliverableVm.pageable.size)+1}}
                        </span>
                                -
                                    <span ng-if="newDeliverableVm.projectDeliverables.last ==false">{{((newDeliverableVm.pageable.page+1)*newDeliverableVm.pageable.size)}}</span>
                                    <span ng-if="newDeliverableVm.projectDeliverables.last == true">{{newDeliverableVm.projectDeliverables.totalElements}}</span>

                                 <span translate> OF </span>
                                {{newDeliverableVm.projectDeliverables.totalElements}}<span
                                        translate>AN</span>
                                </span>
                    </medium>
                </span>
                <span class="mr10">Page {{newDeliverableVm.projectDeliverables.totalElements != 0 ? newDeliverableVm.projectDeliverables.number+1:0}} <span
                        translate> OF </span> {{newDeliverableVm.projectDeliverables.totalPages}}</span>
                    <a href="" ng-click="newDeliverableVm.previousPage()"
                       ng-class="{'disabled': newDeliverableVm.projectDeliverables.first}"><i
                            class="fa fa-arrow-circle-left mr10"></i></a>
                    <a href="" ng-click="newDeliverableVm.nextPage()"
                       ng-class="{'disabled': newDeliverableVm.projectDeliverables.last}"><i
                            class="fa fa-arrow-circle-right"></i></a>
                </div>
                <div ng-if="newDeliverableVm.glossary == true" class="pull-right" style="padding: 10px;">
                    <span>
                         <medium>
                                <span style="margin-right: 10px;">
                                    <span translate>DISPLAYING</span>
                                    <span ng-if="newDeliverableVm.projectGlossaries.numberOfElements ==0">
                            {{(newDeliverableVm.pageable.page*newDeliverableVm.pageable.size)}}
                        </span>

                                    <span ng-if="newDeliverableVm.projectGlossaries.numberOfElements > 0">
                            {{(newDeliverableVm.pageable.page*newDeliverableVm.pageable.size)+1}}
                        </span>
                                -
                                    <span ng-if="newDeliverableVm.projectGlossaries.last ==false">{{((newDeliverableVm.pageable.page+1)*newDeliverableVm.pageable.size)}}</span>
                                    <span ng-if="newDeliverableVm.projectGlossaries.last == true">{{newDeliverableVm.projectGlossaries.totalElements}}</span>

                                 <span translate> OF </span>
                                {{newDeliverableVm.projectGlossaries.totalElements}}<span
                                        translate>AN</span>
                                </span>
                         </medium>
                    </span>
                    <span class="mr10"> Page {{newDeliverableVm.projectGlossaries.totalElements != 0 ? newDeliverableVm.projectGlossaries.number+1:0}} <span
                            translate> OF </span> {{newDeliverableVm.projectGlossaries.totalPages}}
                    </span>
                    <a href="" ng-click="newDeliverableVm.previousPage()"
                       ng-class="{'disabled': newDeliverableVm.projectGlossaries.first}"><i
                            class="fa fa-arrow-circle-left mr10"></i></a>
                    <a href="" ng-click="newDeliverableVm.nextPage()"
                       ng-class="{'disabled': newDeliverableVm.projectGlossaries.last}"><i
                            class="fa fa-arrow-circle-right"></i></a>
                </div>
                <div ng-if="newDeliverableVm.specification == true" class="pull-right" style="padding: 10px;">
                    <span>
                        <medium>
                                <span style="margin-right: 10px;">
                                    <span translate>DISPLAYING</span>
                                    <span ng-if="newDeliverableVm.projectSpecifications.numberOfElements ==0">
                            {{(newDeliverableVm.pageable.page*newDeliverableVm.pageable.size)}}
                        </span>

                                    <span ng-if="newDeliverableVm.projectSpecifications.numberOfElements > 0">
                            {{(newDeliverableVm.pageable.page*newDeliverableVm.pageable.size)+1}}
                        </span>
                                -
                                    <span ng-if="newDeliverableVm.projectSpecifications.last ==false">{{((newDeliverableVm.pageable.page+1)*newDeliverableVm.pageable.size)}}</span>
                                    <span ng-if="newDeliverableVm.projectSpecifications.last == true">{{newDeliverableVm.projectSpecifications.totalElements}}</span>

                                 <span translate> OF </span>
                                {{newDeliverableVm.projectSpecifications.totalElements}}<span
                                        translate>AN</span>
                                </span>
                        </medium>
                    </span>
                    <span class="mr10">Page {{newDeliverableVm.projectSpecifications.totalElements != 0 ? newDeliverableVm.projectSpecifications.number+1:0}} <span
                            translate>OF</span> {{newDeliverableVm.projectSpecifications.totalPages}}
                    </span>
                    <a href="" ng-click="newDeliverableVm.previousSpecificationPage()"
                       ng-class="{'disabled': newDeliverableVm.projectSpecifications.first}"><i
                            class="fa fa-arrow-circle-left mr10"></i></a>
                    <a href="" ng-click="newDeliverableVm.nextSpecificationPage()"
                       ng-class="{'disabled': newDeliverableVm.projectSpecifications.last}"><i
                            class="fa fa-arrow-circle-right"></i></a>
                </div>
                <div ng-if="newDeliverableVm.requirement == true" class="pull-right" style="padding: 10px;">
                    <span>

                        <medium>
                                <span style="margin-right: 10px;">
                                    <span translate>DISPLAYING</span>
                                    <span ng-if="newDeliverableVm.projectRequirements.numberOfElements ==0">
                            {{(newDeliverableVm.pageable.page*newDeliverableVm.pageable.size)}}
                        </span>

                                    <span ng-if="newDeliverableVm.projectRequirements.numberOfElements > 0">
                            {{(newDeliverableVm.pageable.page*newDeliverableVm.pageable.size)+1}}
                        </span>
                                -
                                    <span ng-if="newDeliverableVm.projectRequirements.last ==false">{{((newDeliverableVm.pageable.page+1)*newDeliverableVm.pageable.size)}}</span>
                                    <span ng-if="newDeliverableVm.projectRequirements.last == true">{{newDeliverableVm.projectRequirements.totalElements}}</span>

                                 <span translate> OF </span>
                                {{newDeliverableVm.projectRequirements.totalElements}}<span
                                        translate>AN</span>
                                </span>
                        </medium>
                    </span>
                    <span class="mr10">Page {{newDeliverableVm.projectRequirements.totalElements != 0 ? newDeliverableVm.projectRequirements.number+1:0}} <span
                            translate>OF</span> {{newDeliverableVm.projectRequirements.totalPages}}
                    </span>
                    <a href="" ng-click="newDeliverableVm.previousRequirementPage()"
                       ng-class="{'disabled': newDeliverableVm.projectRequirements.first}"><i
                            class="fa fa-arrow-circle-left mr10"></i></a>
                    <a href="" ng-click="newDeliverableVm.nextRequirementPage()"
                       ng-class="{'disabled': newDeliverableVm.projectRequirements.last}"><i
                            class="fa fa-arrow-circle-right"></i></a>
                </div>
            </div>
        </div>
    </div>
    <div id="search-content">
        <div class="responsive-table" style="height: 100%;overflow: auto;"
             ng-show="newDeliverableVm.showItemDeliverable == true">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width: 30px">
                        <div class="ckbox ckbox-default"
                             style="display: inline-block;">
                            <input id="items{{$index}}" name="itemSelected" type="checkbox"
                                   ng-model="newDeliverableVm.selectedAll" ng-click="newDeliverableVm.checkAll(check)">
                            <label  ng-if="newDeliverableVm.projectDeliverables.content.length != 0"
                                    for="items{{$index}}" class="item-selection-checkbox"></label>
                        </div>
                    </th>
                    <th style="width: 150px" translate="ITEM_ALL_ITEM_NUMBER"></th>
                    <th style="width: 150px" translate="ITEM_ALL_ITEM_TYPE"></th>
                    <th style="width: 150px" translate="ITEM_ALL_ITEM_NAME"></th>
                    <th style="width: 200px;" translate="ITEM_ALL_DESCRIPTION"></th>
                    <th style="width: 100px; text-align: center;" translate="ITEM_ALL_REVISION"></th>
                    <th style="width: 150px" translate="ITEM_ALL_LIFECYCLE"></th>
                    <th style="width: 150px" translate="ITEM_ALL_RELEASED_DATE"></th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="newDeliverableVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_DELIVERABLES</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="newDeliverableVm.loading == false && newDeliverableVm.projectDeliverables.content.length == 0">
                    <td colspan="25" translate>NO_DELIVERABLES</td>
                </tr>

                <tr ng-repeat="item in newDeliverableVm.projectDeliverables.content">
                    <td>
                        <div class="ckbox ckbox-default"
                             style="display: inline-block;">
                            <input id="item{{$index}}" name="itemSelected" type="checkbox"
                                   ng-model="item.selected" ng-click="newDeliverableVm.select(item)">
                            <label for="item{{$index}}" class="item-selection-checkbox"></label>
                        </div>
                    </td>

                    <td style="width: 150px">
                        <a ng-if="item.configurable == true" title="{{configurableItem}}" class="fa fa-cog"
                           aria-hidden="true"></a>
                <span ng-click="newDeliverableVm.showItem(item)" title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                      ng-bind-html="item.itemNumber | highlightText: freeTextQuery"></span>

                    </td>
                    <td style="width: 150px"><span
                            ng-bind-html="item.itemType.name | highlightText: freeTextQuery"></span>
                    </td>
                    <td style="width: 150px"><span ng-bind-html="item.itemName | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="description-column" title="{{item.description}}"><span
                            ng-bind-html="item.description  | highlightText: freeTextQuery"></span>
                    </td>
                    <td style="width: 150px; text-align: center;">{{item.latestRevisionObject.revision}}</td>
                    <td style="width: 150px">
                        <item-status item="item.latestRevisionObject"></item-status>
                    </td>
                    <td style="width: 150px">
                        <span>{{item.latestRevisionObject.releasedDate}}</span>
                    </td>

                </tr>
                </tbody>
            </table>
        </div>


        <div class="responsive-table" style="height: 100%;overflow: auto;" ng-show="newDeliverableVm.glossary == true">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width: 30px">
                        <div class="ckbox ckbox-default" style="display: inline-block;">
                            <input id="glossaries{{$index}}" name="itemSelected" type="checkbox"
                                   ng-model="newDeliverableVm.selectedGlossaryAll"
                                   ng-click="newDeliverableVm.checkAllGlossary(check)">
                            <label ng-if="newDeliverableVm.projectGlossaries.content.length != 0"
                                   for="glossaries{{$index}}" class="item-selection-checkbox"></label>
                        </div>
                    </th>
                    <th style="width: 200px;" translate>NAME</th>
                    <th style="width: 200px" translate>DESCRIPTION</th>
                    <th style="width: 70px;text-align: center" translate>REVISION</th>
                    <th style="width: 100px;" translate>LIFE_CYCLE</th>
                    <th style="width: 100px;" translate>RELEASED_DATE</th>
                </tr>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="newDeliverableVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_DELIVERABLES</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="newDeliverableVm.loading == false && newDeliverableVm.projectGlossaries.content.length == 0">
                    <td colspan="25" translate>NO_DELIVERABLES</td>
                </tr>
                <tr ng-repeat="glossary in newDeliverableVm.projectGlossaries.content">
                    <td>
                        <div class="ckbox ckbox-default" style="display: inline-block;">
                            <input id="glossary{{$index}}" name="itemSelected" type="checkbox"
                                   ng-model="glossary.glossarySelected"
                                   ng-click="newDeliverableVm.selectedGlossare(glossary)">
                            <label for="glossary{{$index}}" class="item-selection-checkbox"></label>
                        </div>
                    </td>
                    <td>
                        <a href=""
                           ng-click="newDeliverableVm.openGlossaryDetails(glossary)"
                           title="{{newDeliverableVm.glossaryOpenTitle}}">
                            <span>{{glossary.defaultDetail.name}}</span>
                        </a>
                    </td>
                    <td><span id="td">{{glossary.defaultDetail.description}}</span></td>
                    <td class="revision-column" style="width: 70px;text-align: center">{{glossary.revision}}</td>
                    <td>
                        <item-status item="glossary"></item-status>
                    </td>
                    <td>{{glossary.releasedDate}}</td>

                </tr>
                </tbody>
            </table>
        </div>

        <div class="responsive-table" style="height: 100%;overflow: auto;"
             ng-show="newDeliverableVm.specification == true">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width: 30px">
                        <div class="ckbox ckbox-default" style="display: inline-block;">
                            <input id="specifications{{$index}}" name="itemSelected" type="checkbox"
                                   ng-model="newDeliverableVm.selectedSpecAll"
                                   ng-click="newDeliverableVm.checkAllSpecifications()">
                            <label ng-if="newDeliverableVm.projectSpecifications.content.length != 0"
                                    for="specifications{{$index}}" class="item-selection-checkbox"></label>
                        </div>
                    </th>
                    <th style="width: 150px" translate="NUMBER"></th>
                    <th style="width: 150px" translate="TYPE"></th>
                    <th style="width: 150px" translate="NAME"></th>
                    <th style="width: 150px" translate="ITEM_ALL_DESCRIPTION"></th>
                    <th style="width: 100px; text-align: center;" translate="ITEM_ALL_REVISION"></th>
                    <th style="width: 150px" translate="ITEM_ALL_LIFECYCLE"></th>
                    <th style="width: 150px" translate="ITEM_ALL_RELEASED_DATE"></th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="newDeliverableVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_DELIVERABLES</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="newDeliverableVm.loading == false && newDeliverableVm.projectSpecifications.content.length == 0">
                    <td colspan="25" translate>NO_DELIVERABLES</td>
                </tr>

                <tr ng-repeat="item in newDeliverableVm.projectSpecifications.content">
                    <td>
                        <div class="ckbox ckbox-default" style="display: inline-block;">
                            <input id="specification{{$index}}" name="itemSelected" type="checkbox"
                                   ng-model="item.specSelected"
                                   ng-click="newDeliverableVm.selectSpecification(item)">
                            <label for="specification{{$index}}" class="item-selection-checkbox"></label>
                        </div>
                    </td>

                    <td style="width: 150px">
                        <a href="" ng-click="newDeliverableVm.showSpecificationDetails(item)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span ng-bind-html="item.objectNumber | highlightText: freeTextQuery"></span></a>
                    </td>
                    <td style="width: 150px"><span
                            ng-bind-html="item.type.name | highlightText: freeTextQuery"></span>
                    </td>
                    <td style="width: 150px"><span ng-bind-html="item.name | highlightText: freeTextQuery"></span>
                    </td>
                    <td><span ng-bind-html="item.description | highlightText: freeTextQuery"></span></td>
                    <td style="width: 150px; text-align: center;">{{item.revision}}</td>
                    <td style="width: 150px">
                        {{item.lifecyclePhase.phase}}
                    </td>
                    <td style="width: 150px">
                        <span>{{item.releasedDate}}</span>
                    </td>

                </tr>
                </tbody>
            </table>
        </div>

        <div class="responsive-table" style="height: 100%;overflow: auto;"
             ng-show="newDeliverableVm.requirement == true">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width: 30px">
                        <div class="ckbox ckbox-default" style="display: inline-block;">
                            <input id="requirements{{$index}}" name="itemSelected" type="checkbox"
                                   ng-model="newDeliverableVm.selectedReqAll"
                                   ng-click="newDeliverableVm.checkAllRequirements()">
                            <label  ng-if=" newDeliverableVm.projectRequirements.content.length != 0"
                                    for="requirements{{$index}}" class="item-selection-checkbox"></label>
                        </div>
                    </th>
                    <th style="width: 150px" translate="NUMBER"></th>
                    <th style="width: 150px" translate="TYPE"></th>
                    <th style="width: 150px" translate="NAME"></th>
                    <th style="width: 150px" translate="ITEM_ALL_DESCRIPTION"></th>
                    <th style="width: 100px; text-align: center;" translate="VERSION"></th>
                    <th style="width: 150px; text-align: center;" translate="STATUS"></th>

                </tr>
                </thead>
                <tbody>
                <tr ng-if="newDeliverableVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_DELIVERABLES</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="newDeliverableVm.loading == false && newDeliverableVm.projectRequirements.content.length == 0">
                    <td colspan="25" translate>NO_DELIVERABLES</td>
                </tr>

                <tr ng-repeat="item in newDeliverableVm.projectRequirements.content">
                    <td>
                        <div class="ckbox ckbox-default" style="display: inline-block;">
                            <input id="requirement{{$index}}" name="itemSelected" type="checkbox"
                                   ng-model="item.reqSelected"
                                   ng-click="newDeliverableVm.selectRequirement(item)">
                            <label for="requirement{{$index}}" class="item-selection-checkbox"></label>
                        </div>
                    </td>

                    <td style="width: 150px">
                        <a href="" ng-click="newDeliverableVm.showRequirementDetails(item)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span ng-bind-html="item.objectNumber | highlightText: freeTextQuery"></span></a>
                    </td>
                    <td style="width: 150px"><span
                            ng-bind-html="item.type.name | highlightText: freeTextQuery"></span>
                    </td>
                    <td style="width: 150px"><span ng-bind-html="item.name | highlightText: freeTextQuery"></span>
                    </td>
                    <td><span ng-bind-html="item.description | highlightText: freeTextQuery"></span></td>
                    <td style="width: 100px; text-align: center;">
                        <span ng-if="item.version == 0">-</span>
                        <span ng-if="item.version > 0">{{item.version}}</span>
                    </td>
                    <td style="width: 150px;text-align: center">
                        <span><task-status task="item"></task-status></span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
