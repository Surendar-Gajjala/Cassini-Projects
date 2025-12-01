<div class="view-container" fitcontent>
    <style scoped>
        .bold-style {
            font-weight: bold;
            color: grey;
            font-size: 16px;
        }

        .about-card {
            background-color: #fff;
            box-shadow: 0 0 30px 0 rgba(82, 63, 105, .05);
            border-radius: 5px;
        }

        .about-card-body {
            padding: 15px !important;
        }

        .about-card-title {
            text-align: center !important;
        }

        .about-card-text {
            text-align: center !important;
        }

        /* .about-card:hover {
             box-shadow: 0 0.06rem 0.18rem 0 rgba(0, 0, 0, 0.11), 0 0.32rem 0.72rem 0 rgba(0, 0, 0, 0.13);
         }*/
    </style>
    <div class="view-toolbar">
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;"
              ng-bind-html="viewInfo.title | translate"></span>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;">

        <div style="display: flex !important;">
            <div ng-click="aboutMainVm.systemInformation()"
                 style="margin: 16px -12px 0px 8px !important;cursor: pointer !important;"
                 ng-class="{'la la-minus': systemInfo, 'la la-plus': !systemInfo}">
            </div>
            <h4 style="margin-left: 20px;" translate>DATA_SPACE_HEADER</h4>
        </div>


        <div ng-show="systemInfo">
            <div class="row">
                <div class="col-sm-4">
                    <div class="about-card">
                        <div class="about-card-body">
                            <h5 class="about-card-title" translate>DATA_TOTAL_SPACE</h5>

                            <p class="about-card-text">{{systemInformation.totalStorageReadableFormat}}</p>
                        </div>
                    </div>
                </div>
                <div class="col-sm-4">
                    <div class="about-card">
                        <div class="about-card-body">
                            <h5 class="about-card-title" translate>DATA_USED_SPACE</h5>

                            <p class="about-card-text">{{systemInformation.usedStorageReadableFormat}}</p>
                        </div>
                    </div>
                </div>

                <div class="col-sm-4">
                    <div class="about-card">
                        <div class="about-card-body">
                            <h5 class="about-card-title" translate>FILE_SYSTEM_USED_SPACE</h5>

                            <p class="about-card-text">{{aboutMainVm.fileSystemSpace}}</p>
                        </div>
                    </div>
                </div>
            </div>
            <%--  <div class="row row-eq-height" style="margin: 0;">
                  <div class="item-details col-sm-7">

                      <div class="row">
                          <div class="label col-md-2 text-right"
                               style="color: grey;font-size: 14px;text-align: right;">

                          </div>
                          <div class="value col-md-10">
                              <div>
                                  <canvas id="pie-chart" width="800" height="450"></canvas>

                              </div>
                          </div>
                      </div>
                  </div>
              </div>--%>
        </div>

        <%--<h3 style="margin-left: 20px;">Admin Info</h3>--%>

        <div style="display: flex !important;">
            <div ng-click="aboutMainVm.aboutInformation()"
                 style="margin: 16px -12px 0px 8px !important;cursor: pointer !important;"
                 ng-class="{'la la-minus': aboutInfo, 'la la-plus': !aboutInfo}">
            </div>
            <h4 style="margin-left: 20px;" translate>ADMIN_INFO</h4>
        </div>

        <div ng-repeat="(key,value) in aboutMainVm.adminInfo" ng-show="aboutInfo">

            <div class="row row-eq-height" style="margin: 0;">
                <div class="item-details col-sm-7">

                    <div class="row" ng-repeat="(heading,number) in value">
                        <div class="label col-xs-5 col-sm-4 text-right"
                             style="color: grey;font-size: 14px;text-align: right;">
                            <span ng-class="{'bold-style':heading == 'Total Items' || heading == 'Total Files'}">{{heading}}</span>
                            :
                        </div>
                        <div class="value col-xs-7 col-sm-8">
                            <span ng-class="{'bold-style':heading == 'Total Items' || heading == 'Total Files'}">{{number}}</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>


        <div style="display: flex !important;">
            <div ng-click="aboutMainVm.applicationInformation()"
                 style="margin: 16px -12px 0px 8px !important;cursor: pointer !important;"
                 ng-class="{'la la-minus': appInfo, 'la la-plus': !appInfo}">
            </div>
            <h4 style="margin-left: 20px;" translate>APP_INFO</h4>
        </div>
        <div ng-show="appInfo">

            <div ng-if="aboutMainVm.loading == true" style="padding: 30px;">
                <br/>
                <span style="font-size: 15px;">
                    <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">
                    <span translate>LOADING_ABOUT_DETAILS</span>
                </span>
                <br/>
            </div>
            <div class="row row-eq-height" style="margin: 0;" ng-if="aboutMainVm.loading == false">
                <div class="item-details col-sm-7" style="padding: 30px;">

                    <div class="row">
                        <div class="label col-xs-5 col-sm-4 text-right">
                            <span translate>SYSTEM_UP_TIME</span> :
                        </div>
                        <div class="value col-xs-7 col-sm-8">
                            <span>{{aboutMainVm.systemUpTime}}</span>
                        </div>
                    </div>


                    <div class="row" ng-repeat="details in aboutMainVm.listDetails | orderBy: 'optionKey'">
                        <div class="label col-xs-5 col-sm-4 text-right">
                            <span>{{details.name}}</span> :
                        </div>
                        <div class="value col-xs-7 col-sm-8" ng-if="aboutMainVm.validate(details.optionKey)">
                            <span ng-if="details.optionKey != 2 && details.optionKey != 4">{{details.value}}</span>
                        <span ng-if="details.optionKey == 2">{{version}}
                        </span>
                        <span ng-if="details.optionKey == 4">{{date}}
                        </span>
                        </div>
                        <div class="value col-xs-7 col-sm-8" ng-if="!aboutMainVm.validate(details.optionKey)">

                            <div>
                                <a ng-if="details.optionKey == 5" href="#"
                                   onaftersave="aboutMainVm.updateDetails(details)"
                                   editable-text="details.value">{{details.value}}</a>
                            <span ng-if="details.optionKey == 5"
                                  ng-show="!hasPermission('admin','all') && !hasPermission('about','edit')">

                                {{details.value}}
                            </span>
                            </div>


                            <div>
                                <span ng-show="hasPermission('admin','all') || hasPermission('about','edit')">
                                     <a ng-if="details.optionKey == 11 && details.editMode == false"

                                        ng-disabled="true"
                                        href="" ng-click="aboutMainVm.changeLoginAttempts(details)">
                                         {{details.value || 'CLICK_TO_SET_VALUE' | translate}}
                                     </a>
                                    <%--<span ng-if="details.optionKey == 11 && details.editMode == false">
                                        {{details.value}}
                                    </span>--%>

                                <input type="text" class="form-control" name="title"
                                       ng-if="details.optionKey == 11 && details.editMode == true"
                                       placeholder="{{'ENTER_NUMBER' | translate}}"
                                <%--ng-keyup="aboutMainVm.validateNumbers($event,details)"--%>
                                       ng-model="details.value" style="width:200px;">
                                <button ng-if="details.optionKey == 11 && details.editMode == true"
                                        class="btn btn-sm btn-primary"
                                        type="button"
                                        style="margin-top:-65px;margin-left:205px;padding: 8px;width:33px;"
                                        ng-click="aboutMainVm.updateDetails(details)"><i class="fa fa-check"></i>
                                </button>

                                <button
                                        ng-if="details.optionKey == 11 && details.editMode == true"
                                        class="btn btn-sm btn-default"
                                        type="button" title="{{cancelChangesTitle}}"
                                        style="margin-top:-65px;padding: 8px;width:33px;"
                                        ng-click="aboutMainVm.cancelChanges(details)"><i class="fa fa-times"></i>
                                </button>
                                </span>

                            <span ng-if="details.optionKey == 11"
                                  ng-show="!hasPermission('admin','all') && !hasPermission('about','edit')">
                                {{details.value}}
                            </span>
                            </div>


                            <div>
                                <a ng-if="details.optionKey == 9"
                                   ng-show="hasPermission('admin','all') || hasPermission('about','edit')"
                                   editable-select="details.value"
                                   href="" e-style="width:200px;"
                                   e-ng-options="value for value in ['English','German'] track by value"
                                   onaftersave="aboutMainVm.updateDefaultLanguage(details)"
                                   ng-bind-html="details.value">
                                </a>
                            <span ng-if="details.optionKey == 9"
                                  ng-show="!hasPermission('admin','all') && !hasPermission('about','edit')">
                                {{details.value}}
                            </span>
                            </div>


                            <div>
                                <a ng-if="details.optionKey == 6 || details.optionKey == 8"
                                   ng-show="hasPermission('admin','all') || hasPermission('about','edit')"
                                   editable-select="details.value"
                                   href="" e-style="width:200px;"
                                   e-ng-options="value for value in ['true','false'] track by value"
                                   onaftersave="aboutMainVm.updateDetails(details)"
                                   ng-bind-html="details.value">
                                </a>
                            <span ng-if="details.optionKey == 6 || details.optionKey == 8"
                                  ng-show="!hasPermission('admin','all') && !hasPermission('about','edit')">

                                {{details.value}}
                            </span>
                            </div>


                            <%--<a ng-if="details.optionKey == 7" editable-select="details.value"
                               href="" e-style="width:200px;"
                               e-ng-options="value for value in ['High','Medium','Low'] track by value"
                               onaftersave="aboutMainVm.updateDetails(details)"
                               ng-bind-html="details.value">
                            </a>--%>
                        </div>
                    </div>

                    <div class="row">
                        <div class="label col-xs-5 col-sm-4 text-right">
                            <span translate>NO_OF_LICENCES</span> :
                        </div>
                        <div class="value col-xs-7 col-sm-8">
                            <span>{{aboutMainVm.licenceDetails.licenses}}</span>
                        </div>
                    </div>
                    <div class="row">
                        <div class="label col-xs-5 col-sm-4 text-right">
                            <span translate>LICENCE_EXPIRATION</span> :
                        </div>
                        <div class="value col-xs-7 col-sm-8">
                            <span>{{aboutMainVm.licenceDetails.expirationDate}}</span>
                        </div>
                    </div>
                </div>
            </div>

        </div>


    </div>
</div>