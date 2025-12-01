<style scoped>
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

    .attributeTooltip {
        position: relative;
        display: inline-block;
    }

    .attributeTooltip .attributeTooltiptext {
        visibility: hidden;
        width: 200px;
        background-color: #7BB7EB;
        color: #141f9f;
        text-align: left;
        border-radius: 6px;
        padding: 5px 0;
        position: absolute;
        z-index: 1;
        top: -5px;
        bottom: auto;
        right: 100%;
        opacity: 0;
        transition: opacity 1s;
    }

    .attributeTooltip .attributeTooltiptext::after {
        content: "";
        position: absolute;
        top: 25%;
        left: 102%;
        margin-left: -5px;
        border-width: 5px;
        border-style: solid;
        border-color: transparent transparent transparent #7BB7EB;
    }

    .attributeTooltip:hover .attributeTooltiptext {
        visibility: visible;
        opacity: 1;
    }

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
</style>
<div class="row row-eq-height" style="margin: 0">
    <div class="item-details col-sm-12" style="padding: 30px;">
        <%--<h4 class="section-title" style="margin-left: -10px; margin-right: -10px;">Basic Information</h4>--%>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Name: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <a href="" editable-text="contractorDetailsVm.contractor.name"
                   ng-if="hasPermission('permission.contractors.editBasic') == true"
                   onaftersave="contractorDetailsVm.updateContractor()">
                    {{contractorDetailsVm.contractor.name}}</a>

                <p ng-if="hasPermission('permission.contractors.editBasic') == false">
                    {{contractorDetailsVm.contractor.name}}</p>
            </div>
        </div>

        <div class="row">
            <div class="label col-sm-4 col-md-3 text-right">
                <span>Contact Person: </span>
            </div>

            <div class="value col-sm-8 col-md-9">
                <a style="color:#428bca;" href="#"
                   ng-if="hasPermission('permission.contractors.editBasic')"
                   editable-select="contractorDetailsVm.contractor.contact"
                   onaftersave="contractorDetailsVm.updateContractor()"
                   e-ng-options="login.person.id as login.person.fullName for login in contractorDetailsVm.logins">
                    {{contractorDetailsVm.contractor.contactObject.fullName}}
                </a>

                <p ng-if="!hasPermission('permission.contractors.editBasic')">
                    {{contractorDetailsVm.contractor.contactObject.fullName}}</p>

            </div>
        </div>

        <div class="row">
            <div class="label col-sm-4 col-md-3 text-right">
                <span>Active</span>
            </div>
            <div class="value col-sm-8 col-md-9">
                <a style="color: #428bca;" href=""
                   ng-if="hasPermission('permission.contractors.editBasic')"
                   editable-radiolist="contractorDetailsVm.contractor.active"
                   e-ng-options="s.value as s.name for s in contractorDetailsVm.flags track by s.value"
                   onaftersave="contractorDetailsVm.updateContractor()"
                   ng-model="contractorDetailsVm.contractor.active"> {{contractorDetailsVm.contractor.active}}
                </a>

                <p ng-if="!hasPermission('permission.contractors.editBasic')">
                    {{contractorDetailsVm.contractor.active}}</p>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Created By : </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{contractorDetailsVm.contractor.createdByObject.fullName}}</span>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Created On : </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{contractorDetailsVm.contractor.createdDate}}</span>
            </div>
        </div>

            <attributes-details-view attribute-id="contractorDetailsVm.contractor.id"
                                     has-permission="hasPermission('permission.contractors.editBasic')"
                                 attribute-type="CONTRACTOR"></attributes-details-view>
    </div>
</div>




