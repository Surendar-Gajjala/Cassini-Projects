<style>
    /* The Close Button */
    .img-model .closeimage {
        position: absolute;
        top: 50px;
        right: 50px;

        font-size: 40px;
        font-weight: bold;
        transition: 0.3s;
    }

    .img-model .closeimage:hover,
    .img-model .closeimage:focus {
        color: #bbb;
        text-decoration: none;
        cursor: pointer;
    }

    .img-model .closeImage {
        position: absolute;
        top: 50px;
        right: 50px;

        font-size: 40px;
        font-weight: bold;
        transition: 0.3s;
    }

    .img-model .closeImage:hover,
    .img-model .closeImage:focus {
        color: #bbb;
        text-decoration: none;
        cursor: pointer;
    }

    .browse-control {
        -moz-border-radius: 3px;
        -webkit-border-radius: 3px;
        border-radius: 3px;
        padding: 5px;
        height: auto;
        -moz-box-shadow: none;
        -webkit-box-shadow: none;
        box-shadow: none;
        font-size: 13px;
        border: 1px solid #ccc;
    }

    /*.btn-default {
        background: #e4e7ea !important;
        color: #636e7b !important;
    }*/
    .thumbnail-container {
        border: 1px solid #ddd;
        height: 380px;
        width: 380px;
        position: absolute;
        right: 30px;
        background-color: #fff;
        /*z-index: 9999 !important;*/
    }

    .thumbnail-container .thumbnail-panel {

    }
</style>

<div ng-if="qcrBasicVm.loading == true" style="padding: 30px;">
    <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">
        <span translate>LOADING_QCR_DETAILS</span>
    </span>
    <br/>
</div>
<div ng-if="qcrBasicVm.loading == false">
    <div class="row row-eq-height">
        <div class="col-sm-12">
            <div class="item-details" style="padding: 30px">
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>QCR_TYPE</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{qcrBasicVm.qcr.qcrType.name}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>QCR_NUMBER</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{qcrBasicVm.qcr.qcrNumber}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>WORKFLOW</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{qcrBasicVm.qcr.workflowObject.name}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>QCR_FOR</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        {{qcrBasicVm.qcr.qcrFor}}
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>TITLE</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <a href="" ng-if="!qcrReleased && hasPermission('qcr','edit')"
                           onaftersave="qcrBasicVm.updateQCR()"
                           editable-textarea="qcrBasicVm.qcr.title">
                            <span ng-bind-html="qcrBasicVm.qcr.titleHtml"></span>
                        </a>
                        <span ng-if="qcrReleased || !hasPermission('qcr','edit')">{{qcrBasicVm.qcr.titleHtml}}</span>
                    </div>
                </div>

                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>DESCRIPTION</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <a href=""
                           ng-if="!qcrReleased && hasPermission('qcr','edit')"
                           onaftersave="qcrBasicVm.updateQCR()"
                           editable-textarea="qcrBasicVm.qcr.description">
                            <span ng-bind-html="qcrBasicVm.qcr.description  || 'ADD_DESCRIPTION' | translate"
                                  title="{{qcrBasicVm.qcr.description}}"></span>
                        </a>
                        <span ng-if="qcrReleased || !hasPermission('qcr','edit')">{{qcrBasicVm.qcr.descriptionHtml}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>WORKFLOW_STATUS</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <workflow-status-settings workflow="qcrBasicVm.qcr"></workflow-status-settings>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>QUALITY_ADMINISTRATOR</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <a href="#" e-style="width: 250px"
                           ng-if="!qcrReleased && hasPermission('admin','all')"
                           onaftersave="qcrBasicVm.updateQCR()"
                           editable-select="qcrBasicVm.qcr.qualityAdministratorObject"
                           title="{{qcrBasicVm.clickToUpdatePerson}}"
                           e-ng-options="person as person.fullName for person in qcrBasicVm.qualityAnalysts | orderBy:'fullName' track by person.id">
                            {{qcrBasicVm.qcr.qualityAdministratorObject.fullName}}
                        </a>
                        <span ng-if="qcrReleased || !hasPermission('admin','all')">{{qcrBasicVm.qcr.qualityAdministratorObject.fullName}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>CREATED_BY</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{qcrBasicVm.qcr.createdByPerson.fullName}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>CREATED_DATE</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{qcrBasicVm.qcr.createdDate}}</span>
                    </div>
                </div>

                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>MODIFIED_BY</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{qcrBasicVm.qcr.modifiedByPerson.fullName}}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="label col-xs-4 col-sm-3 text-right">
                        <span translate>MODIFIED_DATE</span> :
                    </div>
                    <div class="value col-xs-8 col-sm-9">
                        <span>{{qcrBasicVm.qcr.modifiedDate}}</span>
                    </div>
                </div>

                <basic-attribute-details-view object-type="QUALITY"
                                              quality-type="QCR"
                                              has-permission="hasPermission('qcr','edit') && !qcrBasicVm.qcr.released && qcrBasicVm.qcr.statusType != 'REJECTED'"
                                              object-id="qcr.id"></basic-attribute-details-view>

                <object-attribute-details-view object-type="QCRTYPE" show-attributes="true"
                                               has-permission="hasPermission('qcr','edit') && !qcr.released && qcr.statusType != 'REJECTED'"
                                               object-type-id="qcr.qcrType.id"
                                               object-id="qcr.id"></object-attribute-details-view>
            </div>
        </div>
    </div>
</div>