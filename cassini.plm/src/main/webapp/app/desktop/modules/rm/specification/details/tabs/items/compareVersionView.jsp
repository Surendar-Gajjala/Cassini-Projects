<style>
    #rightSidePanelContent {
        overflow: hidden !important;
    }
</style>
<div style="padding: 20px; height: auto;">


    <div class="row">
        <div class="col-md-12"
             style="padding:0px; height: 378px;overflow: auto !important;position: relative !important;">
            <span ng-if="compareVm.error == true">{{compareVm.noVersionsMessage}}</span>
            <table ng-hide="compareVm.error == true" class="table table-bordered">
                <thead>
                <tr>
                    <th></th>
                    <%--<th translate>Description</th>--%>
                    <th><span translate>CURRENT_VERSION</span>({{compareVm.currentVersion}})</th>
                    <th><span translate>PREVIOUS_VERSION</span>({{compareVm.previousVersion}})</th>
                </tr>
                </thead>
                <tbody>


                <tr ng-repeat="reqVersion in compareVm.reqCurrentPreviousVersions">
                    <td style="text-align: left;font-weight: 700;color: #444" translate>
                        NAME
                    </td>
                    <td style="vertical-align: middle;">
                         <span ng-if="reqVersion.latest == true"
                               style="word-wrap: break-word !important;white-space:normal !important;">
                                <div style="max-width:150px !important; min-width:150px !important;">
                                    <span ng-bind-html="reqVersion.name"></span></div>
                                </span>
                    </td>
                    <td style="vertical-align: middle;">
                         <span
                             style="word-wrap: break-word !important;white-space:normal !important;">
                                <div style="max-width:150px !important; min-width:150px !important;">
                                    <span ng-bind-html="reqVersion.previousVersionName"></span></div>
                                </span>
                    </td>

                </tr>

                <tr ng-repeat="reqVersion in compareVm.reqCurrentPreviousVersions">
                    <td style="text-align: left;font-weight: 700;color: #444" translate=>
                        DESCRIPTION
                    </td>
                    <td style="vertical-align: middle;">
                         <span ng-if="reqVersion.latest == true"
                               style="word-wrap: break-word !important;white-space:normal !important;">
                                <div style="max-width:150px !important; min-width:150px !important;">
                                    <span ng-bind-html="reqVersion.description"></span></div>
                                </span>
                    </td>
                    <td style="vertical-align: middle;">
                         <span
                             style="word-wrap: break-word !important;white-space:normal !important;">
                                <div style="max-width:150px !important; min-width:150px !important;">
                                    <span ng-bind-html="reqVersion.previousVersionDescription"></span></div>
                                </span>
                    </td>

                </tr>


                <tr ng-repeat="versionAttribute in compareVm.finalVersions">

                    <td><i
                        ng-if="versionAttribute.valueChaged == true" class="fa fa-check-square"
                        style="font-size: 15px;color: #0b93d5;margin-left: 137px;"></i>
                         <span style="word-wrap: break-word !important;white-space:normal !important;">
                                <div
                                    style="max-width:150px !important; min-width:150px !important;font-weight: 700;color: #444">
                                    <span ng-bind-html="versionAttribute.objectTypeAttribute.name"></span></div>
                                </span>

                    </td>

                    <td style="vertical-align: middle;">
                        <span ng-if="versionAttribute.objectTypeAttribute.dataType =='TEXT'">{{versionAttribute.stringValue}}</span>
                        <%--<span ng-if="versionAttribute.objectTypeAttribute.dataType =='LONGTEXT'">{{versionAttribute.longTextValue}}</span>--%>


                        <span ng-if="versionAttribute.objectTypeAttribute.dataType =='LONGTEXT'"
                              style="word-wrap: break-word !important;white-space:normal !important;">
                                <div style="max-width:150px !important; min-width:150px !important;">
                                    <span ng-bind-html="versionAttribute.longTextValue"></span></div>
                                </span>



                        <span ng-if="versionAttribute.objectTypeAttribute.dataType =='RICHTEXT'">

                            <span style="white-space: normal;word-wrap: break-word;">
                        <div style="-webkit-column-width: 250px;-moz-column-width: 250px;column-width: 250px;
                     webkit-column-count: 1;-moz-column-count: 1;column-count: 1;">
                            <span ng-bind-html="versionAttribute.richTextValue"></span>
                        </div>

                    </span>
                            </span>
                        <span ng-if="versionAttribute.objectTypeAttribute.dataType =='INTEGER'">{{versionAttribute.integerValue}}</span>
                        <span ng-if="versionAttribute.objectTypeAttribute.dataType =='BOOLEAN'">{{versionAttribute.booleanValue}}</span>
                        <span ng-if="versionAttribute.objectTypeAttribute.dataType =='DOUBLE'">{{versionAttribute.doubleValue}}</span>
                        <span ng-if="versionAttribute.objectTypeAttribute.dataType =='DATE'">{{versionAttribute.dateValue}}</span>
                        <span ng-if="versionAttribute.objectTypeAttribute.dataType =='LIST'">

                            <span ng-if="versionAttribute.objectTypeAttribute.listMultiple == false "> {{versionAttribute.listValue}}</span>
                            <span ng-if="versionAttribute.objectTypeAttribute.listMultiple == true ">

                                <ul ng-repeat="list in versionAttribute.mlistValue track by $index">
                                    <li>
                                        {{list}}
                                    </li>
                                </ul>

                            </span>
                            </span>
                        <span ng-if="versionAttribute.objectTypeAttribute.dataType =='TIME'">{{versionAttribute.timeValue}}</span>
                        <span ng-if="versionAttribute.objectTypeAttribute.dataType =='TIMESTAMP'">{{versionAttribute.timestampValue}}</span>
                        <span ng-if="versionAttribute.objectTypeAttribute.dataType =='CURRENCY'">

                            <span ng-bind-html="versionAttribute.currVal"></span>

                            </span>
                        <span ng-if="versionAttribute.objectTypeAttribute.dataType =='ATTACHMENT'">
                            <ul ng-repeat="attachment in versionAttribute.attachmentValues track by $index">
                                <li>
                                    {{attachment.name}}
                                </li>
                            </ul>
                        </span>

                        <span ng-if="versionAttribute.objectTypeAttribute.dataType =='IMAGE'">
                            <img ng-if="versionAttribute.imageValue != null"
                                 ng-src="{{'data:image/png;base64,'+versionAttribute.imageValue}}"
                                 style="height: 30px;width: 40px;margin-bottom: 5px;">
                           </span>
                        <span ng-if="versionAttribute.objectTypeAttribute.dataType =='OBJECT'">{{versionAttribute.objectValue}}</span>


                    </td>
                    <td style="vertical-align: middle;">
                        <span ng-if="versionAttribute.objectTypeAttribute.dataType =='TEXT'">{{versionAttribute.preVersionValue}}</span>

                         <span ng-if="versionAttribute.objectTypeAttribute.dataType =='LONGTEXT'"
                               style="word-wrap: break-word !important;white-space:normal !important;">
                                <div style="max-width:150px !important; min-width:150px !important;">
                                    <span ng-bind-html="versionAttribute.preVersionValue"></span></div>
                                </span>


                        <span ng-if="versionAttribute.objectTypeAttribute.dataType =='RICHTEXT'">
                        <span style="white-space: normal;word-wrap: break-word;">
                        <div style="-webkit-column-width: 250px;-moz-column-width: 250px;column-width: 250px;
                     webkit-column-count: 1;-moz-column-count: 1;column-count: 1;">
                            <span ng-bind-html="versionAttribute.preVersionValue"></span>
                        </div>

                    </span>

                        </span>
                        <span ng-if="versionAttribute.objectTypeAttribute.dataType =='INTEGER'">{{versionAttribute.preVersionValue}}</span>
                        <span ng-if="versionAttribute.objectTypeAttribute.dataType =='BOOLEAN'">{{versionAttribute.preVersionValue}}</span>
                        <span ng-if="versionAttribute.objectTypeAttribute.dataType =='DOUBLE'">{{versionAttribute.preVersionValue}}</span>
                        <span ng-if="versionAttribute.objectTypeAttribute.dataType =='DATE'">{{versionAttribute.preVersionValue}}</span>
                        <span ng-if="versionAttribute.objectTypeAttribute.dataType =='LIST'">

                            <span ng-if="versionAttribute.objectTypeAttribute.listMultiple == false ">{{versionAttribute.preVersionSingleValue}}</span>
                            <span ng-if="versionAttribute.objectTypeAttribute.listMultiple == true ">

                                 <ul ng-repeat="mList in versionAttribute.preVersionValue track by $index">
                                     <li>
                                         {{mList}}
                                     </li>
                                 </ul>

                            </span>
                        </span>
                        <span ng-if="versionAttribute.objectTypeAttribute.dataType =='TIME'">{{versionAttribute.preVersionValue}}</span>
                        <span ng-if="versionAttribute.objectTypeAttribute.dataType =='TIMESTAMP'">{{versionAttribute.preVersionValue}}</span>
                        <span ng-if="versionAttribute.objectTypeAttribute.dataType =='CURRENCY'">
                             <span ng-bind-html="versionAttribute.preVersionValue"></span>
                        </span>
                        <span ng-if="versionAttribute.objectTypeAttribute.dataType =='ATTACHMENT'">
                            <ul ng-repeat="attachment in versionAttribute.preVersionValue track by $index">
                                <li>
                                    {{attachment.name}}
                                </li>
                            </ul>
                        </span>
                        <span ng-if="versionAttribute.objectTypeAttribute.dataType =='IMAGE'">
                            <img ng-if="versionAttribute.preVersionValue != null"

                                 ng-src="{{versionAttribute.preVersionValue}}"
                                 style="height: 30px;width: 40px;margin-bottom: 5px;">
                        </span>
                        <span ng-if="versionAttribute.objectTypeAttribute.dataType =='OBJECT'">{{versionAttribute.preVersionValue}}</span>


                    </td>
                </tr>


                </tbody>
            </table>
        </div>
    </div>
    <br>
    <br>
</div>
