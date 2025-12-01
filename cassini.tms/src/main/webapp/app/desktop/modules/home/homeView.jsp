<div class="view-container"  fitcontent>
    <style scoped>
        video {
            border: 5px solid #ddd;
            background-color: #4b4646;
        }

        .rzslider {
            margin-top: 10px;
        }

        .trip {
            border-bottom: 1px solid #ddd;
            padding: 10px;
            cursor: pointer;
        }

        .trip.active {
            background-image: -webkit-linear-gradient(top,#337ab7 0,#2b669a 100%);
            background-image: -o-linear-gradient(top,#337ab7 0,#2b669a 100%);
            background-image: -webkit-gradient(linear,left top,left bottom,from(#337ab7),to(#2b669a));
            background-image: linear-gradient(to bottom,#337ab7 0,#2b669a 100%);
            filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#ff337ab7', endColorstr='#ff2b669a', GradientType=0);
            background-repeat: repeat-x;
            border-color: #2b669a;
            color: #fff !important;
        }

        .prop-label {
            color: #626262;
            font-size: 12px;
        }

        .prop-value {
            font-weight: bold;
            font-size: 16px;
        }

        .trip.active .prop-label {
            color: #eee;
        }

        .trip.active .prop-value {
            color: #fff;
        }


        .split-pane-divider {
            background: #EEE;
            left: 400px; /* Same as left component width */
            width: 5px;
        }

        .split-left-pane {
            min-width: 400px;
            max-width: 400px;
            padding: 0px;
            margin-top: 40px;
            border-top: 1px solid #ddd;
        }

        .split-right-pane {
            left: 400px; /* Same as left component width */
            margin-left: 5px; /* Same as divider width */
            padding: 10px;
        }
        .row {
            margin: 0;
        }

        .camera1 {

        }
        .camera2 {

        }
        .camera3 {

        }
        .camera4 {

        }

        .camera-label {
            color: #fff;
            position: absolute;
            bottom: 15px;
            right: 0;
            left: 0;
            text-align: center;
            font-size: 20px;
        }

        .trips-container {
            position: absolute;
            width: 400px;
            background-color: #fff;
            left: 31px;
            z-index: 9999;
            border-right: 3px solid #ddd;
            display: none;
        }

        .trip-header {
            display: inline-block;
            margin: 0;
            float: right;
            padding-top: 0px;
            padding-right: 10px;
            color: #2a6fa8;
        }

        .full-screen-video {
            position: fixed;
            top: 50px;
            left: 0;
            bottom: 100px;
            right: 0;
            width: 100% !important;
            height: calc(100% - 150px) !important;
            z-index: 99998;
        }

        .full-screen-video-with-map {
            position: fixed;
            top: 50px;
            left: 0;
            bottom: 100px;
            right: 0;
            width: calc(100% - 200px) !important;
            height: calc(100% - 150px) !important;
            z-index: 99998;
        }

        .full-screen-map {
            position: fixed;
            top: 50px;
            bottom: 100px;
            right: 0;
            width: 200px !important;
            height: calc(100% - 150px) !important;
            z-index: 99998;
        }

        .full-screen-toolbar {
            position: fixed;
            top: 0;
            left: 0;
            bottom: 50px;
            right: 0;
            width: 100% !important;
            height: 50px !important;
            z-index: 99998;
            background-color: #f6f6f6;
        }

        .full-screen-camera-label {
            position: fixed;
            left: 0;
            bottom: 15px;
            right: 0;
            width: 100% !important;
            z-index: 99999;
        }

        .full-screen-distance-bar {
            position: fixed;
            left: 0;
            bottom: 0;
            right: 0;
            width: 100% !important;
            height: 100px !important;
            z-index: 99997;
        }

        .angular-google-map-container {
            border: 5px solid #ddd;
            height: 200px;
        }

        .rz-tick-value {
            font-size: 12px;
        }

        .rz-tick-value::after {
            content: " Km";
        }

        .rz-bubble {
            font-size: 12px;
        }

        .rz-bubble::after {
            content: " Km";
        }

        .gm-style-iw + div {display: none;}
        .gm-style .gm-style-iw {
            text-align: center;
            font-weight: bolder;
        }

        .docs-window.gm-style-iw + div {display: block;}
        .gm-style .docs-window.gm-style-iw {
            text-align: left;
            font-weight: bolder;
        }

        #docsWindow h5 {
            margin: 0 0 10px 0;
        }

        .speed-header {
            display: inline;
            margin-left: 400px;
            color: #2a6fa8;
        }

        .show-docs-chk {
            display: inline-block;
            margin-top: -5px;
            vertical-align: middle;
            margin-left: 10px;
            font-size: 18px;
            color: #2a6fa8;
        }


    </style>
    <!--
    <div class="view-toolbar">

    </div>
    -->
    <div id="viewContent" class="view-content" style="padding: 10px">

        <div id="tripsList" class="trips-container">
            <div style="overflow-y: auto;">
                <div class="row trip"
                     ng-click="homeVm.showTripDetails(trip)"
                     ng-repeat="trip in homeVm.trips"
                     ng-class="{'active': trip.active}">
                    <div class="col-sm-4 text-left">
                        <span class="prop-label">From</span><br>
                        <span class="prop-value">{{trip.from}}</span>
                    </div>
                    <div class="col-sm-4 text-center">
                        <span class="prop-label">To</span><br>
                        <span class="prop-value">{{trip.to}}</span>
                    </div>
                    <div class="col-sm-4 text-right">
                        <span class="prop-label">Date</span><br>
                        <span class="prop-value">{{trip.date}}</span>
                    </div>
                </div>
            </div>
        </div>

        <div class="row" id='controls-toolbar'>
            <div class="col-sm-12" style="height: 50px;border:5px solid #ddd; padding: 5px; margin-bottom: 10px;">
                <div class="btn-group btn-group-sm">
                    <button class='btn btn-default' title='Play backward'
                            ng-disabled="homeVm.playingForward == false"
                            ng-click='homeVm.playBackward()'>
                        <i class='fa fa-backward'></i>
                    </button>
                    <button class='btn btn-default' title='Show full screen' ng-click='homeVm.showFullscreen()'><i class='fa fa-arrows-alt'></i></button>
                    <button class='btn btn-warning' ng-click='homeVm.playSpeed(0.20)'>-5x</button>
                    <button class='btn btn-warning' ng-click='homeVm.playSpeed(0.25)'>-4x</button>
                    <button class='btn btn-warning' ng-click='homeVm.playSpeed(0.33333)'>-3x</button>
                    <button class='btn btn-warning' ng-click='homeVm.playSpeed(0.5)'>-2x</button>
                    <button class='btn btn-success' ng-click='homeVm.playSpeed(1.0)'>1x</button>
                    <button class='btn btn-info' ng-click='homeVm.playSpeed(2.0)'>2x</button>
                    <button class='btn btn-info' ng-click='homeVm.playSpeed(3.0)'>3x</button>
                    <button class='btn btn-info' ng-click='homeVm.playSpeed(4.0)'>4x</button>
                    <button class='btn btn-info' ng-click='homeVm.playSpeed(5.0)'>5x</button>
                    <button  class='btn btn-danger' title='Pause video' ng-click='homeVm.stopVideos()' ng-disabled="!homeVm.playing"><i class='fa fa-pause'></i></button>
                    <button class='btn btn-default' title='Play forward'
                            ng-disabled="homeVm.playingForward == true"
                            ng-click='homeVm.playForward()'>
                        <i class='fa fa-forward'></i>
                    </button>
                </div>
                <div class="ckbox ckbox-primary show-docs-chk">
                    <input value="1" id="checkboxPrimary" checked="checked" type="checkbox"
                           ng-model="homeVm.showDocuments"
                            ng-click="homeVm.toggleDocumentMarkers()">
                    <label for="checkboxPrimary">Show Documents</label>
                </div>

                <h3 class="speed-header">{{'Calculated Speed: ' + homeVm.currentSpeed + ' Kmph'}}</h3>
                <h3 class='trip-header'>{{homeVm.selectedTrip.from + ' - ' + homeVm.selectedTrip.to}}</h3>
            </div>
        </div>
        <div class="row">
            <div id="videoPanel" class="col-sm-8" style=" padding: 0">
                <div class="row" style="height: 50%;">
                    <div class="col-sm-6" style="height: 100%; padding: 0">
                        <video id="camera1" preload="auto" style="width: 100%; height: 100%; border-right: 0">
                            <source class="videoSrc" src="app/assets/videos/cam1.mp4" type="video/mp4">
                        </video>
                        <br>
                        <div id='camera-label1' class="camera-label">
                            Camera 1
                        </div>
                    </div>
                    <div class="col-sm-6" style="height: 100%; padding: 0">
                        <video id="camera2" preload="auto" style="width: 100%; height: 100%">
                            <source class="videoSrc" src="app/assets/videos/cam2.mp4" type="video/mp4">
                        </video>
                        <br>
                        <div id='camera-label2' class="camera-label">
                            Camera 2
                        </div>
                    </div>
                </div>
                <div class="row" style="height: 50%;">
                    <div class="col-sm-6" style="height: 100%; padding: 0">
                        <video id="camera3" preload="auto" style="width: 100%; height: 100%; border-top: 0; border-right: 0">
                            <source class="videoSrc" src="app/assets/videos/cam3.mp4" type="video/mp4">
                        </video>
                        <br>
                        <div id='camera-label3' class="camera-label">
                            Camera 3
                        </div>
                    </div>
                    <div class="col-sm-6" style="height: 100%; padding: 0;">
                        <video id="camera4" preload="auto" style="width: 100%; height: 100%;border-top: 0;">
                            <source class="videoSrc" src="app/assets/videos/cam3.mp4" type="video/mp4">
                        </video>
                        <br>
                        <div id='camera-label4' class="camera-label">
                            Camera 4
                        </div>
                    </div>
                </div>
            </div>
            <div id="mapPanel" class="col-sm-4"  style=" padding-right: 0">
                <ui-gmap-google-map center='map.center' zoom='map.zoom'></ui-gmap-google-map>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-12" style=" padding: 0">
                <div id="distance-bar"
                        style="height: 90px; padding: 10px; padding-top: 30px; vertical-align: middle;border:5px solid #ddd; margin-top: 10px;">
                    <rzslider rz-slider-model="homeVm.slider.minValue"
                              rz-slider-options="homeVm.slider.options"></rzslider>
                </div>
            </div>
        </div>
    </div>
</div>
