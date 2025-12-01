<div>
    <style scoped>
        .cassini-panel {
            margin-bottom: 10px;
        }

        .cassini-panel-heading {
            padding: 1px;
            line-height: 50px;
            background-color: #ededed;
        }

        .cassini-panel-body {
            padding: 10px;
        }

        /*.view-content .table > div {*/
        /*line-height: 10px;*/
        /*}*/

        .view-content .table-footer > div {
            display: table-row;
            line-height: 30px;
        }

        .view-content .table-footer > div h5 {
            margin: 0;
        }

        .view-content .table-footer > div > div {
            display: table-cell;
            vertical-align: middle;
        }

        .view-content .table-footer > div > div > i {
            font-size: 16px;
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

        .img-model .closeImage1 {
            position: absolute;
            top: 50px;
            right: 50px;
            color: black;
            font-size: 40px;
            font-weight: bold;
            transition: 0.3s;
        }

        .img-model .closeImage1:hover,
        .img-model .closeImage1:focus {
            color: #bbb;
            text-decoration: none;
            cursor: pointer;
        }

        .img-model .closeImage2 {
            position: absolute;
            top: 50px;
            right: 50px;
            color: black;
            font-size: 40px;
            font-weight: bold;
            transition: 0.3s;
        }

        .img-model .closeImage2:hover,
        .img-model .closeImage2:focus {
            color: #bbb;
            text-decoration: none;
            cursor: pointer;
        }

        .img-model.modal {
            display: none; /* Hidden by default */
            position: fixed; /* Stay in place */
            z-index: 1; /* Sit on top */
            padding-top: 100px; /* IRSTELocation of the box */
            left: 0;
            top: 0;
            width: 100%; /* Full width */
            height: 100%; /* Full height */
            overflow: auto; /* Enable scroll if needed */
            background-color: rgb(0, 0, 0); /* Fallback color */
            background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
        }

        .img-model.modal1 {
            display: block; /* Hidden by default */
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

        #description {
            display: run-in;
            word-wrap: break-word;
            width: 300px;
            white-space: normal;
            text-align: left;
        }

        #name {
            display: run-in;
            word-wrap: break-word;
            width: 250px;
            white-space: normal;
            text-align: left;
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

        .img-model .closeImage1 {
            position: absolute;
            top: 50px;
            right: 50px;
            color: black;
            font-size: 40px;
            font-weight: bold;
            transition: 0.3s;
        }

        .img-model .closeImage1:hover,
        .img-model .closeImage1:focus {
            color: #bbb;
            text-decoration: none;
            cursor: pointer;
        }

        .img-model.modal {
            display: none; /* Hidden by default */
            position: fixed; /* Stay in place */
            z-index: 1; /* Sit on top */
            padding-top: 100px; /* IRSTELocation of the box */
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

        .lockedProject {
            background: #ff6666 !important;
            color: black;
        }

        table td {
            vertical-align: middle !important;
        }

        .new {
            color: white;
            background: #0f12f3cc !important;
        }

        .inprogress {
            color: white;
            background: orange !important;
        }

        .completed {
            color: white;
            background: #12a712 !important;
        }

    </style>

    <div class="view-container no-padding" style="overflow-y: auto;" applicationfitcontent>
        <div class="view-toolbar">
            <div style="text-align: center;margin-top: 15px;margin-left: 15px;color: #002451;font-weight: bold;font-size: 20px;">
                <span>Complaint Tracker</span>
            </div>
        </div>

        <div class="view-content no-padding" style="overflow-y: auto;">
            <div class="row" style="margin: 0;padding: 20px;">
                <div class="col-md-6 col-sm-8 col-md-offset-3 col-md-sm-4"
                     style="border: 1.5px solid grey;padding: 10px;border-radius: 5px;">
                    <form class="form-horizontal">
                        <div class="form-group">
                            <label class="col-sm-4 control-label">
                                <span><b>Complaint Number</b></span>
                                <span class="asterisk">*</span> :
                            </label>

                            <div class="col-sm-6">
                                <div class="input-group mb15">
                                    <input type="text" class="form-control" name="title"
                                           ng-model="homeVm.complaintNumber" ng-enter="homeVm.trackComplaint()">

                                    <div class="input-group-btn">
                                        <button class="btn btn-default" type="button" style="width: 85px"
                                                ng-click="homeVm.trackComplaint()">Search
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <h4 ng-if="homeVm.errorMessage != null" style="text-align: center;color: #ae0000;">
                            <b>{{homeVm.errorMessage}}</b>
                        </h4>

                        <div ng-if="homeVm.complaint != null">
                            <%--<div class="form-group">
                                <label class="col-sm-4 control-label">
                                    Complaint Number :
                                </label>

                                <div class="col-sm-6" style="margin-top: 10px;">
                                    <span>{{homeVm.complaint.complaintNumber}}</span>
                                </div>
                            </div>--%>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <b>Complainant :</b>
                                </label>

                                <div class="col-sm-6" style="margin-top: 10px;">
                                    <span>
                                {{homeVm.complaint.personObject.traineeId}}{{homeVm.complaint.personObject.designation}}</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <b>Status :</b>
                                </label>

                                <div class="col-sm-6" style="margin-top: 10px;">
                                    <span class="flag"
                                          ng-class="{'new':homeVm.complaint.status == 'NEW',
                                         'inprogress':homeVm.complaint.status == 'INPROGRESS',
                                         'completed':homeVm.complaint.status == 'COMPLETED'}">{{homeVm.complaint.status}}</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <b>Utility :</b>
                                </label>

                                <div class="col-sm-6" style="margin-top: 10px;">
                                    <span>{{homeVm.complaint.utility}}</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <b>Location :</b>
                                </label>

                                <div class="col-sm-6" style="margin-top: 10px;">
                                    <span>{{homeVm.complaint.location}}</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <b> Created Date :</b>
                                </label>

                                <div class="col-sm-6" style="margin-top: 10px;">
                                    <span>{{homeVm.complaint.createdDate}}</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">
                                    <b> Details :</b>
                                </label>

                                <div class="col-sm-6" style="margin-top: 10px;">
                                    <span>{{homeVm.complaint.details}}</span>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
