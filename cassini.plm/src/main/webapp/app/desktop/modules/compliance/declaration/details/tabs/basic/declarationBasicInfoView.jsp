<style>
    .thumbnail-container {
        border: 1px solid #ddd;
        height: 380px;
        width: 380px;
        position: absolute;
        right: 30px;
        background-color: #fff;
        z-index: 10 !important;
    }

    .medium-image {
        max-height: 378px;
        width: 378px;
        position: absolute;
        top: 50%;
        transform: translateY(-50%);
    }

    .col-sm-5 .editable-textarea {
        width: 450px;
    }
</style>
<div ng-if="declarationBasicVm.loading == true" style="padding: 30px;">
    <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">
       <span translate>LOADING_DECLARATION_DETAILS</span>
    </span>
    <br/>
</div>

<div class="item-details" style="padding: 30px" ng-if="declarationBasicVm.loading == false">

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>DECLARATION_NUMBER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{declarationBasicVm.declaration.number}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>DECLARATION_TYPE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{declarationBasicVm.declaration.type.name}}</span>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>NAME</span> :
        </div>
        <div class="value col-xs-4 col-sm-5"
             ng-class="{'cursor-override': !hasPermission('pgcdeclaration','edit') || declarationBasicVm.declaration.status == 'ACCEPTED'}"
             title="{{hasPermission('pgcdeclaration','edit') ? '' : noPermission}}">
            <a href="" e-style="width:250px"
               ng-class="{'permission-text-disabled': !hasPermission('pgcdeclaration','edit') || declarationBasicVm.declaration.status == 'ACCEPTED'}"
               onaftersave="declarationBasicVm.updateDeclaration()"
               editable-text="declarationBasicVm.declaration.name">
                <span ng-bind-html="declarationBasicVm.declaration.name"></span>
            </a>
        </div>
    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>DESCRIPTION</span> :
        </div>
        <div class="value col-xs-4 col-sm-5"
             ng-class="{'cursor-override': !hasPermission('pgcdeclaration','edit') || declarationBasicVm.declaration.status == 'ACCEPTED'}"
             title="{{hasPermission('pgcdeclaration','edit') ? '' : noPermission}}">
            <a href=""
               ng-class="{'permission-text-disabled': !hasPermission('pgcdeclaration','edit') || declarationBasicVm.declaration.status == 'ACCEPTED'}"
               onaftersave="declarationBasicVm.updateDeclaration()"
               editable-textarea="declarationBasicVm.declaration.description">
                <span ng-bind-html="(declarationBasicVm.declaration.description) || 'CLICK_TO_ENTER_DESCRIPTION' |
                translate" title="{{declarationBasicVm.declaration.description}}"></span>
            </a>
        </div>

    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>SUPPLIER</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <%-- <a href="#" e-style="width: 250px"
                onaftersave="declarationBasicVm.updateDeclaration()"
                editable-select="declarationBasicVm.declaration.requestorObject"
                title="select"
                e-ng-options="person as person.fullName for person in declarationBasicVm.persons track by person.id">
                 {{declarationBasicVm.declaration.supplierName}}
             </a>--%>
            {{declarationBasicVm.declaration.supplierName}}
        </div>

    </div>

    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CONTACT</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <%--<a href="#" e-style="width: 250px"
               onaftersave="declarationBasicVm.updateDeclaration()"
               editable-select="declarationBasicVm.declaration.requestorObject"
               title="{{ecrBasicVm.clickToUpdatePerson}}"
               e-ng-options="person as person.fullName for person in declarationBasicVm.persons track by person.id">
                {{declarationBasicVm.declaration.supplierContactName}}
            </a>--%>
            {{declarationBasicVm.declaration.supplierContactName}}
        </div>
    </div>


    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>STATUS</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <declaration-status object="declarationBasicVm.declaration"></declaration-status>
        </div>
    </div>


    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{declarationBasicVm.declaration.createdByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>CREATED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{declarationBasicVm.declaration.createdDate}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_BY</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{declarationBasicVm.declaration.modifiedByObject.fullName}}</span>
        </div>
    </div>
    <div class="row">
        <div class="label col-xs-4 col-sm-3 text-right">
            <span translate>MODIFIED_DATE</span> :
        </div>
        <div class="value col-xs-8 col-sm-9">
            <span>{{declarationBasicVm.declaration.modifiedDate}}</span>
        </div>
    </div>
    <object-attribute-details-view object-type="PGCOBJECTTYPE" show-attributes="true"
                                   actual-object-type="{{declaration.objectType}}"
                                   has-permission="true"
                                   object-type-id="declaration.type.id"
                                   object-id="declaration.id"></object-attribute-details-view>
</div>
