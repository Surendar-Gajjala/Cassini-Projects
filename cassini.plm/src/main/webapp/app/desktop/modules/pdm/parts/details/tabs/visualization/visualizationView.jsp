<div>
    <style scoped>
        .visualization-view {
            position: absolute;
            top: 50px;
            bottom: 0;
            left: 0px;
            right: 0px;
        }

        .placeholder-container {
            position: absolute;
            top: 45%;
            left: 50%;
            transform: translate(-50%, -50%);
        }

        .placeholder-container .placeholder-img img {
            width: 300px
        }

        .placeholder-container .placeholder-message {
            font-size: 20px;
            font-weight: 300;
        }
    </style>
    <div class="visualization-view">
        <div class="placeholder-container text-center" ng-if="!visualizationVm.showViewer">
            <div class="placeholder-img">
                <img src="app/assets/no_data_images/loading-vector.png" alt="">
            </div>
            <div class="placeholder-message">
                <img src="app/assets/bower_components/cassini-platform/images/loaders/loader5.gif"
                     class="mr5" style="opacity: 1;"><span>Generating visualization model...</span>

            </div>
        </div>
        <iframe id="forgeFrame"
                src=""
                ng-show="visualizationVm.showViewer"
                frameborder="0" height="100%" width="100%"></iframe>
        <kisters-web-view register-kister-call-back="registerKisterCallBack(kisterCallBack)"></kisters-web-view>
    </div>
</div>