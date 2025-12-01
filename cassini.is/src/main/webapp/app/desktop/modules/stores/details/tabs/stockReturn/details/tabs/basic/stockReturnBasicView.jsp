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
        /*overflow: auto; /!* Enable scroll if needed *!/*/
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

<div ng-if="basicVm.loading == true" style="padding: 30px;">
    <br/>
                <span style="font-size: 15px;">
                    <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">Loading..
                </span>
    <br/>
</div>
<div class="row row-eq-height" style="margin: 0;">
    <div class="item-details col-sm-10 col-sm-offset-1" style="padding: 30px;">

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Stock Return Number: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{basicVm.stockReturn.returnNumberSource}}</span>
            </div>
        </div>

        <div class="row" ng-if="basicVm.stockReturn.project != null">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Project: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{basicVm.stockReturn.projectName}}</span>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Returned Date: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{basicVm.stockReturn.returnedDate}}</span>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Returned To: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{basicVm.stockReturn.returnedToPerson.fullName}}</span>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Approved By: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <a style="color:#428bca;" href="#"
                   ng-if="hasPermission('permission.stockReturn.edit') && basicVm.stockReturn.status == 'NEW'"
                   editable-text="basicVm.stockReturn.approvedBy"
                   onaftersave="basicVm.updateStockReturn()">
                    {{basicVm.stockReturn.approvedBy || 'Click to enter ApprovedBy'}}</a>

                <p ng-if="hasPermission('permission.stockReturn.edit') == false || basicVm.stockReturn.status == 'APPROVED'">
                    {{basicVm.stockReturn.approvedBy}}</p>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span>Notes: </span>
            </div>
            <div class="value col-xs-8 col-sm-9">
                <a style="color:#428bca;" href="#"
                   editable-text="basicVm.stockReturn.notes"
                   onaftersave="basicVm.updateStockReturn()">
                    {{basicVm.stockReturn.notes || 'Click to enter Notes'}}</a>
            </div>
        </div>

        <attributes-details-view attribute-id="stockReturnId" attribute-type="STOCKRETURN"
                                 has-permission="hasPermission('permission.stockReturn.edit')"></attributes-details-view>

    </div>
</div>
