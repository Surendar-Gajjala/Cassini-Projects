<style scoped>
    .item-details > div.row.master-att:last-child,
    .item-details > div.row.revision-att:last-child {
        border-bottom: 0 !important;
    }

    /* The Close Button */
    .img-model .closeImage {
        position: absolute;
        top: 50px;
        right: 50px;
        color: black;
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

    /* The Modal (background) */
    .img-model.modal {
        display: none; /* Hidden by default */
        position: fixed; /* Stay in place */
        z-index: 1; /* Sit on top */
        padding-top: 100px; /* Location of the box */
        left: 0;
        top: 0;
        width: 100%; /* Full width */
        height: 100%; /* Full height */
        overflow: auto; /* Enable scroll if needed */
        background-color: rgb(0, 0, 0); /* Fallback color */
        background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
    }

    /* Modal Content (image) */
    .img-model .modal-content {
        margin: auto;
        display: block;
        height: 90%;
        width: 60%;
        /*max-width: 70%;*/
    }

    /* Caption of Modal Image */
    .img-model #caption {
        margin: auto;
        display: block;
        width: 80%;
        max-width: 700px;
        text-align: center;
        color: #ccc;
        padding: 10px 0;
        height: 150px;
    }

    /* Add Animation */
    .img-model .modal-content, #caption {
        -webkit-animation-name: zoom;
        -webkit-animation-duration: 0.6s;
        animation-name: zoom;
        animation-duration: 0.6s;
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

<div ng-if="manpowerBasicVm.loading == true" style="padding: 30px;">
    <br/>
                <span style="font-size: 15px;">
                    <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">Loading item details...
                </span>
    <br/>
</div>
<div class="row row-eq-height" style="margin: 0;" ng-if="!manpowerBasicVm.loading">
    <div class="item-details col-sm-10 col-sm-offset-1" style="padding: 30px;">
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Manpower Type: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{manpowerBasicVm.manpower.itemType.name}}</span>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Manpower Number: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{manpowerBasicVm.manpower.itemNumber}}</span>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Manpower Name: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <a style="color:#428bca;" href=""
                   editable-text="manpowerBasicVm.manpower.person.fullName"  ng-if="hasPermission('permission.manpower.edit')"
                   onaftersave="manpowerBasicVm.updateManpower()">
                    {{manpowerBasicVm.manpower.person.fullName}}
                </a>

                <p ng-if="hasPermission('permission.manpower.edit') == false">
                    {{manpowerBasicVm.manpower.person.fullName}}</p>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span> Phone No: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <a style="color:#428bca;" href=""
                   editable-text="manpowerBasicVm.manpower.person.phoneMobile"  ng-if="hasPermission('permission.manpower.edit')"
                   onaftersave="manpowerBasicVm.updateManpower()">
                    {{manpowerBasicVm.manpower.person.phoneMobile || 'Click to enter phone number'}}
                </a>

                <p ng-if="hasPermission('permission.manpower.edit') == false">
                    {{manpowerBasicVm.manpower.person.phoneMobile}}</p>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span> Email: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <a style="color:#428bca;" href=""
                   editable-text="manpowerBasicVm.manpower.person.email"   ng-if="hasPermission('permission.manpower.edit')"
                   onaftersave="manpowerBasicVm.updateManpower()">
                    {{manpowerBasicVm.manpower.person.email || 'Click to enter email'}}
                </a>

                <p ng-if="hasPermission('permission.manpower.edit') == false">
                    {{manpowerBasicVm.manpower.person.email}}</p>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span> Photo: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <a style="color: #428bca;"
                   title="Click to show large image">
                    <img ng-show="manpowerBasicVm.manpower.person.image != null" id="image1"
                         ng-src="{{manpowerBasicVm.personImagePath}}"
                         ng-click="manpowerBasicVm.showImage()"
                         style="height: 100px;width: 100px;margin-bottom: 5px;"></a>
                </a>

                <div id="myModal2" class="img-model modal">
                    <span class="closeImage">&times;</span>
                    <img class="modal-content" id="img03">
                </div>

                <a href=""
                   ng-if="manpowerBasicVm.value.imageValue == null && hasPermission('permission.manpower.edit')"
                   ng-click="manpowerBasicVm.change()"
                   ng-hide="manpowerBasicVm.changeImage == true" title="{{addImage}}">
                    <i class="fa fa-pencil" title="{{'ADD_IMAGE' | translate}}" style="padding-left: 7px"></i>
                </a>
                <i ng-if="manpowerBasicVm.value.imageValue != null && hasPermission('permission.manpower.edit')"
                   class="fa fa-pencil"
                   style="color: #337ab7"
                   ng-click="manpowerBasicVm.change()"
                   ng-hide="manpowerBasicVm.changeImage == true" title="{{changeImageMessage}}">
                </i>
                <input ng-show="manpowerBasicVm.changeImage == true"
                       class="browse-control" name="file"
                       accept="image/*" id="image2" onchange="angular.element(this).scope().setImage(this.files)"
                       type="file" ng-model="manpowerBasicVm.personImage">
                <button class="btn btn-xs btn-primary"
                        ng-click="manpowerBasicVm.savePersonImage()"
                        ng-show="manpowerBasicVm.changeImage == true"
                        title="Save image"
                        style="margin-top: -62px;margin-left: 28%;padding-top: 8px;"><i
                        class="fa fa-check"></i>
                </button>
                <button class="btn btn-xs btn-default"
                        ng-click="manpowerBasicVm.cancel()" title="{{cancelChangesTitle}}"
                        ng-show="manpowerBasicVm.changeImage == true" style="margin-top:-62px;padding-top:8px;"><i
                        class="fa fa-times"></i>
                </button>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Description: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <a style="color:#428bca;" href="" editable-text="manpowerBasicVm.manpower.description"
                   ng-if="hasPermission('permission.manpower.edit')"
                   onaftersave="manpowerBasicVm.updateManpower()">
                    {{manpowerBasicVm.manpower.description || 'Click to enter description'}}</a>

                <p ng-if="hasPermission('permission.manpower.edit') == false">
                    {{materialBasicVm.manpower.description}}</p>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Created On: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{manpowerBasicVm.manpower.createdDate}}</span>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Created By: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{manpowerBasicVm.manpower.createdByObject.fullName}}</span>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Modified On: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{manpowerBasicVm.manpower.modifiedDate}}</span>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Modified By: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{manpowerBasicVm.manpower.modifiedByObject.fullName}}</span>
            </div>
        </div>
        <attributes-details-view attribute-id="manpowerId" attribute-type="MANPOWER"
                                 has-permission="hasPermission('permission.manpower.edit')"></attributes-details-view>
    </div>
</div>

