<div>
    <style scoped>
        .panel-summary {
            height: 100px;
            padding: 15px;
        }
        .panel-summary h2,
        .panel-summary h1{
            text-align: center;
        }

        .panel-summary h1{
            font-size: 50px;
        }

        .panel-finish {
            background: #159957; /* fallback for old browsers */
            background: -webkit-linear-gradient(to right, #159957, #155799); /* Chrome 10-25, Safari 5.1-6 */
            background: linear-gradient(to right, #159957, #155799); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
        }

        .panel-inprogress {
            background: #fdc830; /* fallback for old browsers */
            background: -webkit-linear-gradient(to right, #fdc830, #f37335); /* Chrome 10-25, Safari 5.1-6 */
            background: linear-gradient(to right, #fdc830, #f37335); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
        }

        .panel-notyetstarted {
            background: #da4453; /* fallback for old browsers */
            background: -webkit-linear-gradient(to right, #da4453, #89216b); /* Chrome 10-25, Safari 5.1-6 */
            background: linear-gradient(to right, #da4453, #89216b); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
        }

        .panel-missile {
            background: #00b4db; /* fallback for old browsers */
            background: -webkit-linear-gradient(to right, #00b4db, #0083b0); /* Chrome 10-25, Safari 5.1-6 */
            background: linear-gradient(to right, #00b4db, #0083b0); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
        }
    </style>


    <div>
        <div class="row">
            <div class="col-sm-4">
                <div class="panel panel-summary panel-finish">
                    <div class="row">
                        <div class="col-sm-10" style="height: 100% !important;border-right: 2px solid #fff;">
                            <div style="width: 100%;height: 70px;">
                                <h2 style="margin: 0;line-height: 70px;color: #fff;">Sections Completed</h2>
                            </div>
                        </div>
                        <div class="col-sm-2">
                            <div style="width: 100%;height: 70px;">
                                <h1 style="margin: 0;line-height: 70px;color: #fff;">{{summaryVm.missileSummary.sectionsCompleted}}</h1>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-sm-4">
                <div class="panel panel-summary panel-inprogress">
                    <div class="row">
                        <div class="col-sm-10" style="height: 100% !important;border-right: 2px solid #fff;">
                            <div style="width: 100%;height: 70px;">
                                <h2 style="margin: 0;line-height: 70px;color: #fff;">Sections In Progress</h2>
                            </div>
                        </div>
                        <div class="col-sm-2">
                            <div style="width: 100%;height: 70px;">
                                <h1 style="margin: 0;line-height: 70px;color: #fff;">{{summaryVm.missileSummary.sectionsInProgress}}</h1>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-sm-4">
                <div class="panel panel-summary panel-notyetstarted">
                    <div class="row">
                        <div class="col-sm-10" style="height: 100% !important;border-right: 2px solid #fff;">
                            <div style="width: 100%;height: 70px;">
                                <h2 style="margin: 0;line-height: 70px;color: #fff;">Sections Not Yet Started</h2>
                            </div>
                        </div>
                        <div class="col-sm-2">
                            <div style="width: 100%;height: 70px;">
                                <h1 style="margin: 0;line-height: 70px;color: #fff;">{{summaryVm.missileSummary.sectionsNotYetStarted}}</h1>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <hr>
    <div style="padding-top: 0px;">
        <div id="barchart" style="height: 400px; width: 100%">

        </div>
    </div>
</div>