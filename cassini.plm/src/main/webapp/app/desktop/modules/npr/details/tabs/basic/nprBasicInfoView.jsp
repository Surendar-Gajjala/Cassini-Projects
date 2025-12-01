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
</style>

<div ng-if="nprBasicVm.loading == true" style="padding: 30px;">
    <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">
        <span translate>LOADING_PART_REQUESTS</span>
    </span>
    <br/>
</div>

<div class="item-details" style="padding: 30px" ng-if="nprBasicVm.loading == false">
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NUMBER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{nprBasicVm.npr.number}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>REQUESTER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <a href="#" e-style="width: 250px" ng-if="nprBasicVm.status == false"
               onaftersave="nprBasicVm.updateNpr()"
               editable-select="nprBasicVm.npr.requesterObject"
               e-ng-options="person as person.firstName for person in nprBasicVm.persons track by person.id">
                {{nprBasicVm.npr.requesterObject.fullName}}
            </a>
            <span ng-if="nprBasicVm.status == true">
                <span ng-bind-html="nprBasicVm.npr.requesterObject.fullName"></span>
            </span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>DESCRIPTION</span> :
        </div>
        <div class="value col-xs-4 col-sm-5">
            <a href="" ng-if="nprBasicVm.status == false"
               onaftersave="nprBasicVm.updateNpr()"
               editable-textarea="nprBasicVm.npr.description">
                <span ng-bind-html="nprBasicVm.npr.description || 'ADD_DESCRIPTION' | translate"></span>
            </a>
              <span ng-if="nprBasicVm.status == true">
                <span ng-bind-html="nprBasicVm.npr.description"></span>
            </span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>STATUS</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <object-status object="nprBasicVm.npr"></object-status>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>REASON_FOR_REQUEST</span> :
        </div>
        <div class="value col-xs-4 col-sm-5">
            <a href="" ng-if="nprBasicVm.status == false"
               onaftersave="nprBasicVm.updateNpr()"
               editable-textarea="nprBasicVm.npr.reasonForRequest">
                <span ng-bind-html="nprBasicVm.npr.reasonForRequest || 'ADD_REASON_FOR_REQUEST' | translate"></span>
            </a>
            <span ng-if="nprBasicVm.status == true">
                <span ng-bind-html="nprBasicVm.npr.reasonForRequest"></span>
            </span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>REJECT_REASON</span> :
        </div>
        <div class="value col-xs-4 col-sm-5">
            <a href="" ng-if="nprBasicVm.status == false"
               onaftersave="nprBasicVm.updateNpr()"
               editable-textarea="nprBasicVm.npr.rejectReason">
                <span ng-bind-html="nprBasicVm.npr.rejectReason || 'ADD_REJECT_REASON' | translate"></span>
            </a>
               <span ng-if="nprBasicVm.status == true">
                <span ng-bind-html="nprBasicVm.npr.rejectReason"></span>
            </span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NOTES</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <a href="" ng-if="nprBasicVm.status == false"
               onaftersave="nprBasicVm.updateNpr()"
               editable-textarea="nprBasicVm.npr.notes"><span ng-bind-html="(nprBasicVm.npr.notes ) || 'ADD_NOTES' |
                translate" title="{{nprBasicVm.npr.notes}}"></span>
            </a>
               <span ng-if="nprBasicVm.status == true">
                <span ng-bind-html="nprBasicVm.npr.notes"></span>
            </span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{nprBasicVm.npr.createdByObject.firstName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{nprBasicVm.npr.createdDate}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{nprBasicVm.npr.modifiedByObject.firstName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{nprBasicVm.npr.modifiedDate}}</span>
        </div>
    </div>
</div>
