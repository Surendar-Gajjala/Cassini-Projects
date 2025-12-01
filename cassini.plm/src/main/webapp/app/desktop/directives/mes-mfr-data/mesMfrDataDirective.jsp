<h4 class="section-title" style="margin-top: 30px;">Manufacturer Info</h4>

<div ng-if="updateType == 'creation'">
    <div class="form-group">
        <label class="col-sm-4 control-label">
            MFR. <span style="word-wrap: break-word; !important;" translate>NAME</span> :
        </label>

        <div class="col-sm-7">
            <input type="text" class="form-control" placeholder="{{'ENTER_MFR_NAME' | translate}}"
                   ng-model="manufacturerData.mfrName"/>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-4 control-label">
            MFR. <span style="word-wrap: break-word; !important;" translate>DESCRIPTION</span> :
        </label>

        <div class="col-sm-7">
            <textarea type="text" rows="3" class="form-control" style="resize: none"
                      placeholder="{{'ENTER_MFR_DESCRIPTION_TITLE' | translate}}"
                      ng-model="manufacturerData.mfrDescription"></textarea>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-4 control-label">
            MFR. <span style="word-wrap: break-word; !important;" translate>MODEL_NUMBER</span> :
        </label>

        <div class="col-sm-7">
            <input type="text" class="form-control" placeholder="{{'ENTER_MODEL_NUMBER' | translate}}"
                   ng-model="manufacturerData.mfrModelNumber"/>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-4 control-label">
            MFR. <span style="word-wrap: break-word; !important;" translate>PART_NUMBER</span> :
        </label>

        <div class="col-sm-7">
            <input type="text" class="form-control" placeholder="{{'ENTER_MFR_PART_NUMBER' | translate}}"
                   ng-model="manufacturerData.mfrPartNumber"/>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-4 control-label">
            MFR. <span style="word-wrap: break-word; !important;" translate>SERIAL_NUMBER</span> :
        </label>

        <div class="col-sm-7">
            <input type="text" class="form-control" placeholder="{{'ENTER_SERIAL_NUMBER' | translate}}"
                   ng-model="manufacturerData.mfrSerialNumber"/>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-4 control-label">
            MFR. <span style="word-wrap: break-word; !important;" translate>LOT_NUMBER</span> :
        </label>

        <div class="col-sm-7">
            <input type="text" class="form-control" placeholder="{{'ENTER_LOT_NUMBER' | translate}}"
                   ng-model="manufacturerData.mfrLotNumber"/>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-4 control-label">
            MFR. <span style="word-wrap: break-word; !important;" translate>DATE</span> :
        </label>

        <div class="col-sm-7">
            <div class="input-group">
                <input type="text" class="form-control" inward-date-picker
                       ng-model="manufacturerData.mfrDate" placeholder="dd/mm/yyyy">
                <span class="input-group-addon">
                    <i class="glyphicon glyphicon-calendar"></i>
                </span>
            </div>
        </div>
    </div>
</div>

<div ng-if="updateType == 'update'">
    <div class="item-details">
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                MFR. <span translate>NAME</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <a href="" e-style="width:250px" ng-if="hasPermission"
                   onaftersave="updateData()"
                   editable-text="manufacturerData.mfrName">
                    <span ng-bind-html="manufacturerData.mfrName || 'ADD_NAME' | translate"></span>
                </a>
                <span ng-if="!hasPermission">{{manufacturerData.mfrName}}</span>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                MFR. <span translate>DESCRIPTION</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span ng-if="!hasPermission">{{manufacturerData.mfrDescription}}</span>
                <a href="" ng-if="hasPermission"
                   onaftersave="updateData()"
                   editable-textarea="manufacturerData.mfrDescription">
                    <span ng-bind-html="manufacturerData.mfrDescription || 'ADD_DESCRIPTION' | translate"></span>
                </a>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                MFR. <span translate>MODEL_NUMBER</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span ng-if="!hasPermission">{{manufacturerData.mfrModelNumber}}</span>
                <a href="" e-style="width:250px" ng-if="hasPermission"
                   onaftersave="updateData()"
                   editable-text="manufacturerData.mfrModelNumber">
                    <span ng-bind-html="manufacturerData.mfrModelNumber || 'ADD_MODEL_NUMBER' | translate"></span>
                </a>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                MFR. <span translate>PART_NUMBER</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span ng-if="!hasPermission">{{manufacturerData.mfrPartNumber}}</span>
                <a href="" e-style="width:250px" ng-if="hasPermission"
                   onaftersave="updateData()"
                   editable-text="manufacturerData.mfrPartNumber">
                    <span ng-bind-html="manufacturerData.mfrPartNumber || 'ADD_PART_NUMBER' | translate"></span>
                </a>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                MFR. <span translate>SERIAL_NUMBER</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span ng-if="!hasPermission">{{manufacturerData.mfrSerialNumber}}</span>
                <a href="" e-style="width:250px" ng-if="hasPermission"
                   onaftersave="updateData()"
                   editable-text="manufacturerData.mfrSerialNumber">
                    <span ng-bind-html="manufacturerData.mfrSerialNumber || 'ADD_SERIAL_NUMBER' | translate"></span>
                </a>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                MFR. <span translate>LOT_NUMBER</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span ng-if="!hasPermission">{{manufacturerData.mfrLotNumber}}</span>
                <a href="" e-style="width:250px" ng-if="hasPermission"
                   onaftersave="updateData()"
                   editable-text="manufacturerData.mfrLotNumber">
                    <span ng-bind-html="manufacturerData.mfrLotNumber || 'ADD_LOT_NUMBER' | translate"></span>
                </a>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                MFR. <span translate>DATE</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span ng-if="!hasPermission">{{manufacturerData.mfrDate}}</span>
                <a href="" ng-if="hasPermission && !editMfrDate"
                   ng-click="changeDate()">
                    <span ng-bind-html="manufacturerData.mfrDate || 'ADD_DATE' | translate"></span>
                </a>

                <div ng-if="editMfrDate" style="display: flex;">
                    <input class="form-control" inward-date-picker placeholder="Select Date"
                           type="text" ng-model="manufacturerData.mfrDate" style="width: 200px;">
                    <i class="fa fa-times" ng-if="manufacturerData.mfrDate != null"
                       style="position: absolute;margin-top: 10px;margin-left: 185px;cursor: pointer;"
                       ng-click="manufacturerData.mfrDate = null"></i>
                    <button class="btn btn-sm btn-primary"
                            ng-click="updateData()">
                        <i class="fa fa-check"></i>
                    </button>
                    <button class="btn btn-sm btn-default"
                            ng-click="cancelDate()">
                        <i class="fa fa-times"></i>
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>