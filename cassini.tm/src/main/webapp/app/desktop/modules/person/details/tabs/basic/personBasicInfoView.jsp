<div class="view-container">

    <div class="view-content">

        <div class="item-details" style="padding: 30px">
            <div class="row">
                <div class="label col-xs-4 col-sm-3 text-right">
                    <span>First Name :</span>
                </div>
                <div class="value col-xs-8 col-sm-9">
                    <a href="#" e-ng-disabled="!personEditable"
                       editable-text="personBasicInfoVm.person.firstName">{{personBasicInfoVm.person.firstName}}</a>
                </div>
            </div>
            <div class="row">
                <div class="label col-xs-4 col-sm-3 text-right">
                    <span>Middle Name : </span>
                </div>
                <div class="value col-xs-8 col-sm-9">
                    <a e-ng-disabled="!personEditable"
                       href="#" editable-text="personBasicInfoVm.person.middleName">{{personBasicInfoVm.person.middleName}}</a>
                </div>
            </div>

            <div class="row">
                <div class="label col-xs-4 col-sm-3 text-right">
                    <span>Last Name : </span>
                </div>
                <div class="value col-xs-8 col-sm-9">
                    <a href="#" e-ng-disabled="!personEditable"
                       editable-text="personBasicInfoVm.person.lastName">{{personBasicInfoVm.person.lastName}}</a>
                </div>
            </div>

            <div class="row">
                <div class="label col-xs-4 col-sm-3 text-right">
                    <span>Phone Mobile : </span>
                </div>
                <div class="value col-xs-8 col-sm-9">
                    <a href="#" e-ng-disabled="!personEditable"
                       editable-text="personBasicInfoVm.person.phoneMobile">{{personBasicInfoVm.person.phoneMobile}}</a>
                </div>
            </div>

            <div class="row">
                <div class="label col-xs-4 col-sm-3 text-right">
                    <span>Phone Office : </span>
                </div>
                <div class="value col-xs-8 col-sm-9">
                    <a href="#" e-ng-disabled="!personEditable"
                       editable-text="personBasicInfoVm.person.phoneOffice">{{personBasicInfoVm.person.phoneOffice}}</a>
                </div>
            </div>
            <div class="row">
                <div class="label col-xs-4 col-sm-3 text-right">
                    <span>Email : </span>
                </div>
                <div class="value col-xs-8 col-sm-9">
                    <a href="#" e-ng-disabled="!personEditable"
                       editable-text="personBasicInfoVm.person.email">{{personBasicInfoVm.person.email}}</a>
                </div>
            </div>
            <div class="row">
                <div class="label col-xs-4 col-sm-3 text-right">
                    <span>Login Name : </span>
                </div>
                <div class="value col-xs-8 col-sm-9">
                   <span>{{personBasicInfoVm.person.loginName}}</span>
                </div>
            </div>
            <div ng-if="personEditable" class="label col-xs-7 col-sm-6 text-right">
                <button class="btn btn-danger btn-sm" data-ng-click="personBasicInfoVm.showChangePassword();"><i
                        class="fa fa-key"></i> Change Password
                </button>
            </div>
        </div>
    </div>
</div>









