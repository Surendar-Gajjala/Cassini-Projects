<div>
    <style scoped>
        .vault-container {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, max-content));
            grid-gap: 10px;
            justify-content: center;
            padding: 30px;
            width: calc(100% - 350px);
            overflow-y: auto;
            height: 100%;
        }

        .vault-object-details {
            border-left: 1px solid #ddd;
            width: 350px;
            padding: 20px;
            /*background-color: #f8f8f8;*/
            background-image: var(--cassini-linear-gradient);
            overflow-y: auto;
            transition: width 5s;
        }

        .vault-container .vault-child {
            flex: 1;
            display: flex;
            flex-flow: column;
            border: 1px solid #ddd;
            border-radius: 5px;
            height: 255px;
            max-width: 300px;
            min-width: 300px;
            margin: 20px;
            padding: 10px;
            cursor: pointer;
            position: relative;
            background-image: var(--cassini-linear-gradient);
        }

        .vault-container .vault-child:hover,
        .vault-container .vault-child.selected {
            box-shadow: 0 0 12px rgb(54, 128, 223);
            border: 1px solid rgb(96, 182, 255);
        }

        .vault-container .vault-child .vault-child-image {
            flex: 1;
            width: 100%;
            text-align: center;
        }

        .vault-container .vault-child.load-more {
            background-image: linear-gradient(to top, #accbee 0%, #e7f0fd 100%);
        }

        .vault-container .vault-child .vault-child-image img {
            width: 160px;
        }

        .vault-container .vault-child .vault-child-image.file-thumbnail {
            flex: 1;
            max-width: 202px;
            margin-left: auto;
            margin-right: auto;
        }

        .file-thumbnail img {
            border: 1px solid #ddd;
            border-radius: 5px;
            background-color: #fff;
        }

        .vault-container .vault-child .vault-child-body {
            flex: 1;
            display: flex;
            flex-flow: column nowrap;
        }

        .vault-container .vault-child .vault-child-body .file-size {
            padding: 5px;
            background-color: #1ABC9C;
            color: #fff;
            border-radius: 3px;
        }

        .vault-container .vault-child .vault-child-body .file-versions {
            padding: 3px 10px;
            background-color: #ff5252;
            color: #fff;
            border-radius: 3px;
            min-width: 50px;
        }

        .vault-container .vault-child .vault-child-body .file-current-version {
            padding: 3px 10px;
            background-color: #3B3E3E;
            color: #fff;
            border-radius: 3px;
            min-width: 50px;
        }

        .vault-container .vault-child .vault-child-body h5 {
            white-space: -moz-pre-wrap !important; /* Mozilla, since 1999 */
            white-space: -webkit-pre-wrap; /* Chrome & Safari */
            white-space: -pre-wrap; /* Opera 4-6 */
            white-space: -o-pre-wrap; /* Opera 7 */
            word-wrap: break-word; /* Internet Explorer 5.5+ */
            word-break: break-all;
            white-space: normal;
        }

        .flex-container {
            display: flex;
        }

        .flex-item {
            flex: 1
        }

        .vault-child .vault-footer {
            flex: 1;
            color: #7b8a90;
            display: flex;
            font-size: 12px;
            justify-content: end;
            padding: 10px;
        }

        .vault-child .vault-icons {
            display: flex;
            justify-content: end;
        }

        .vault-child .vault-icons i {
            margin-left: 5px;
            cursor: pointer;
            font-size: 16px;
        }

        .vault-child .vault-footer div {
            flex: 1;
        }

        .vault-object-details .selected-file-image img {
            box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2);
            transition: 0.3s;
            border-radius: 5px;
            background-color: #fff;
        }

        div.flex-row {
            display: flex;
            flex-direction: row;
            align-items: flex-start;
            align-content: flex-start
        }

        div.flex-row > div {
            flex: 1;
            align-self: flex-start
        }

        div.metrics-container {
        }

        div.metrics-counts-label {
            max-width: 20px;
            min-height: 20px;
            min-width: 20px;
            max-height: 20px;
            text-align: center;
            line-height: 20px;
            border-radius: 50%;
            color: #fff;
            font-size: 10px;
        }

        div.metrics-counts {
            max-width: 20px;
            min-height: 20px;
            min-width: 20px;
            max-height: 20px;
            line-height: 20px;
            text-align: left;
            font-size: 12px;
            margin-left: 5px;
        }

        div.flex-start {
            justify-content: start;
        }

        div.flex-center {
            justify-content: center;
        }

        div.flex-end {
            justify-content: end;
        }

        div.assemblies {
            background-color: #54a0ff;
        }

        div.parts {
            background-color: #1dd1a1;
        }

        div.drawings {
            background-color: #ff6b6b;
        }

        div.commits {
            background-color: #ffa502;
        }

        .mt-10 {
            margin-top: 10px;
        }

        .mb-10 {
            margin-bottom: 10px;
        }

        .mt-20 {
            margin-top: 20px;
        }

        .ml-5 {
            margin-left: 5px;
        }

        .mr-5 {
            margin-left: 5px;
        }

        .ml-10 {
            margin-left: 10px;
        }

        .mr-10 {
            margin-left: 10px;
        }

        .view-toolbar .path-label {
            /*float: right;*/
            margin-right: 10px;
            margin-top: 5px;
        }

        .vault-object-details .object-details-info {
            margin-top: 20px;
        }

        .vault-object-details .object-details-info .details-row {
            margin-bottom: 15px;
        }

        .vault-object-details .object-details-info .details-row div.details-label {
            color: #6e8da7;
        }

        .vault-object-details .object-details-info .details-row .file-tags {
            display: flex;
        }

        .vault-object-details .object-details-info .details-row .file-tags .file-tag {
            background-color: #1e85e0;
            color: #fff;
            padding: 0 5px;
            margin-right: 5px;
            border-radius: 3px;
        }

        .no-data-container {
            position: absolute;
            top: 45%;
            left: 50%;
            /* bring your own prefixes */
            transform: translate(-50%, -50%);
        }

        .no-data-container .no-data-img img {
            width: 500px
        }

        .no-data-container .no-data-message {
            font-size: 25px;
            font-weight: 300;
        }

        .pb-animate-if-fadeLeft {
            -webkit-transition: opacity 500ms linear;
            transition: opacity 500ms linear;
            -webkit-transition: -webkit-transform 500ms cubic-bezier(0.19, 1, 0.22, 1);
            transition: -webkit-transform 500ms cubic-bezier(0.19, 1, 0.22, 1);
            transition: transform 500ms cubic-bezier(0.19, 1, 0.22, 1);
            transition: transform 500ms cubic-bezier(0.19, 1, 0.22, 1), -webkit-transform 500ms cubic-bezier(0.19, 1, 0.22, 1);
            -webkit-backface-visibility: visible;
            backface-visibility: visible;
            -webkit-transform: translateX(0);
            transform: translateX(0);
        }

        .pb-animate-if-fadeLeft.ng-enter, .pb-animate-if-fadeLeft.ng-leave.ng-leave-active {
            -webkit-transition: opacity 80ms linear;
            transition: opacity 80ms linear;
            opacity: 0;
            -webkit-transform: translateX(20px);
            transform: translateX(20px);
        }

        .pb-animate-if-fadeLeft.ng-leave, .pb-animate-if-fadeLeft.ng-enter.ng-enter-active {
            opacity: 1;
        }

        .show-scroll {
            overflow-y: auto;
        }

        .show-scroll .vault-container {
            width: calc(100% - 10px);
            overflow-y: hidden;
            height: max-content;
        }

        .vault-item-toolbar {
            position: absolute;
            width: 30px;
            height: 30px;
            text-align: center;
            margin-left: 259px !important;
            margin-top: -10px;
            line-height: 30px;
            padding-right: 2px;
        }

        .vault-item-toolbar:hover {
            background-color: #60b6ff;
            border-top-right-radius: 5px;
        }

        .viewer-container {
            position: absolute;
            left: 0;
            top: 51px;
            bottom: 0;
            right: 350px;
            background-color: #fff;
        }

        .viewer-container.show-full {
            right: 0;
        }

        .placeholder-container {
            position: absolute;
            top: 45%;
            left: 50%;
            transform: translate(-50%, -50%);
        }

        .placeholder-container .placeholder-img img {
            width: 400px
        }

        .placeholder-container .placeholder-message {
            font-size: 20px;
            font-weight: 300;
        }

        /* The Close Button */
        .viewer-container .close-button {
            position: absolute;
            right: 15px;
            top: 15px;
            width: 38px;
            height: 38px;
            opacity: 0.3;
        }

        .viewer-container .close-button:hover {
            opacity: 0.6;
            border-radius: 50%;
            background-color: #ddd;
        }

        .viewer-container .close-button:before, .close-button:after {
            position: absolute;
            top: 7px;
            left: 18px;
            content: ' ';
            height: 22px;
            width: 2px;
            background-color: #333;
        }

        .viewer-container .close-button:before {
            transform: rotate(45deg) !important;
        }

        .viewer-container .close-button:after {
            transform: rotate(-45deg) !important;
        }

        #freeTextSearchDirective {
            top: 7px !important;
        }

    </style>

    <div class="view-container" fitcontent>
        <div class="view-toolbar">
            <span style="font-weight: 600;font-size: 20px;padding-right: 10px;"
                  ng-if="pdmFilesVm.filters.fileType == 'ASSEMBLY'" translate>ASSEMBLIES</span>
            <span style="font-weight: 600;font-size: 20px;padding-right: 10px;"
                  ng-if="pdmFilesVm.filters.fileType == 'PART'" translate>PARTS</span>
            <span style="font-weight: 600;font-size: 20px;padding-right: 10px;"
                  ng-if="pdmFilesVm.filters.fileType == 'DRAWING'" translate>DRAWINGS</span>
            <button class="btn btn-sm btn-default"
                    style="" title="{{preferredPage}}"
                    ng-click="savePreferredPage()">
                <i class="fa fa fa-anchor" style=""></i>
            </button>

            <free-text-search on-clear="pdmFilesVm.resetPage" search-term="pdmFilesVm.searchText"
                              on-search="pdmFilesVm.freeTextSearch"
                              filter-search="pdmFilesVm.filterSearch"></free-text-search>
        </div>

        <div class="view-content no-padding" style="display: flex;"
             ng-class="{'show-scroll': pdmFilesVm.selectedFile == null}">
            <div class="vault-container" ng-class="{'show-full': pdmFilesVm.selectedFile == null}">
                <div class="no-data-container text-center"
                     ng-if="!pdmFilesVm.loading && pdmFilesVm.filesList.length === 0">
                    <div class="no-data-img">
                        <img ng-if="pdmFilesVm.filters.fileType == 'ASSEMBLY'"
                             src="app/assets/images/no-data.png" alt="">
                        <img ng-if="pdmFilesVm.filters.fileType == 'PART'"
                             src="app/assets/no_data_images/ManufacturerParts.png" alt="">
                    </div>
                    <div class="no-data-message">
                        <span ng-if="pdmFilesVm.filters.fileType == 'ASSEMBLY'">  {{ 'NO_ASSEMBLIES ' | translate }} </span>
                        <span ng-if="pdmFilesVm.filters.fileType == 'PART'">  {{ 'NO_PARTS' | translate }} </span>

                    </div>
                </div>
                <div class="vault-child" ng-repeat="child in pdmFilesVm.filesList"
                     ng-class="{'selected': child.id === pdmFilesVm.selectedFile.id}"
                     ng-click="pdmFilesVm.onClickChild(child)">

                    <div class="vault-item-toolbar" ng-click="$event.stopPropagation();"
                         uib-dropdown dropdown-append-to-body>
                        <i class="fa fa-ellipsis-h" uib-dropdown-toggle></i>
                        <ul uib-dropdown-menu class="dropdown-menu" role="menu"
                            style="z-index: 9999 !important;">
                            <li ng-hide="child.file.fileType=='PART' || child.file.fileType=='DRAWING'"><a href=""
                                                                                                           ng-click="pdmFilesVm.showAssemblyDetails($event, child)">Open
                                Details</a>
                            </li>
                            <%--<li><a href="" ng-click="pdmFilesVm.showVisualization($event, child)">Show AutoDesk
                                Visualization</a>
                            </li>
                            <li><a href="" ng-click="pdmFilesVm.showInKistersWebViewer($event, child)">Show Kisters
                                Visualization</a></li>--%>
                        </ul>
                    </div>

                    <div class="vault-child-image file-thumbnail">
                        <img ng-src="{{pdmFilesVm.getThumbnailUrl(child)}}" alt="" width="200"
                             onerror="this.src='app/assets/images/cassini-logo-greyscale-text.png';">
                    </div>
                    <div class="vault-child-body">
                        <h5 class="text-center"><span ng-bind-html="child.name | highlightText: freeTextQuery"></span>
                        </h5>

                        <div class="flex-container" style="margin-top:auto; margin-bottom: 5px; font-size: 12px">
                            <div class="flex-item ml-10">
                                <span>File Size</span> <br> <span class="file-size">{{pdmFilesVm.fileSizeToString(child.size)}}</span>
                            </div>
                            <div class="flex-item text-center">
                                <span>Version</span> <br> <span class="file-current-version">{{child.version}}</span>
                            </div>
                            <div class="flex-item text-right mr-10">
                                <span>Versions</span> <br> <span class="file-versions">{{child.file.versions}}</span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="vault-child load-more" ng-if="!pdmFilesVm.files.last" ng-click="pdmFilesVm.nextPage()">
                    <h4 class="no-data-container">Load more...</h4>
                </div>
            </div>

            <div class="vault-object-details pb-animate-if-fadeLeft" ng-if="pdmFilesVm.selectedFile != null">
                <div ng-if="pdmFilesVm.selectedFile != null">
                    <div class="row">
                        <div class="col-sm-8">
                            <span style="font-weight: 600;font-size: 20px;" class="ng-binding">Details</span>
                        </div>
                        <div class="col-sm-4" style="text-align: right">
                            <span ng-click="pdmFilesVm.hideDetails()" class="la la-times" style="cursor: pointer"
                                  title="close"></span>
                        </div>
                    </div>

                    <div class="selected-file-image mt-20">
                        <img ng-src="{{pdmFilesVm.getThumbnailUrl(pdmFilesVm.selectedFile)}}" alt="" width="100%"
                             onerror="this.src='app/assets/images/cassini-logo-greyscale-text.png';">
                    </div>
                    <h5 class="text-center">{{pdmFilesVm.selectedFile.name}}</h5>

                    <div class="object-details-info">
                        <div class="details-row">
                            <div class="details-label">File Name</div>
                            <div class="details-value">{{pdmFilesVm.selectedFile.name}}</div>
                        </div>

                        <div class="details-row">
                            <div class="details-label">File Size</div>
                            <div class="details-value">{{pdmFilesVm.fileSizeToString(pdmFilesVm.selectedFile.size)}}
                            </div>
                        </div>

                        <div class="details-row">
                            <div class="details-label">Version</div>
                            <div class="details-value">{{pdmFilesVm.selectedFile.version}}</div>
                        </div>

                        <div class="details-row">
                            <div class="details-label">Versions</div>
                            <div class="details-value">{{pdmFilesVm.selectedFile.file.versions}}</div>
                        </div>

                        <div class="details-row">
                            <div class="details-label">Last Modified</div>
                            <div class="details-value">{{pdmFilesVm.selectedFile.modifiedDate}}</div>
                        </div>

                        <div class="details-row">
                            <div class="details-label">Tags</div>
                            <div class="file-tags">
                                <div class="file-tag">Pivot</div>
                                <div class="file-tag">Clamp</div>
                                <div class="file-tag">Fork</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="viewer-container"
         ng-show="pdmFilesVm.showViewerContainer"
         ng-class="{'show-full': pdmFilesVm.selectedFile === null}">
        <div class="placeholder-container text-center"
             ng-if="!pdmFilesVm.showViewer && !pdmFilesVm.kistersViewer">
            <div class="placeholder-img">
                <img src="app/assets/images/loading-vector.png" alt="">
            </div>
            <div class="placeholder-message">
                <img src="app/assets/bower_components/cassini-platform/images/loaders/loader5.gif"
                     class="mr5" style="opacity: 1;"><span>Generating visualization model...</span>
            </div>
        </div>
        <iframe id="forgeFrame"
                src=""
                ng-show="pdmFilesVm.showViewer"
                frameborder="0" height="100%" width="100%"></iframe>
        <%--<kisters-web-view register-kister-call-back="registerKisterCallBack(kisterCallBack)"
                          contwidth="conWidth"></kisters-web-view>--%>
        <iframe ng-show="pdmFilesVm.kistersViewer" id="WebViewFrame" src="" width="100%"
                height="95%"></iframe>
        <div>
            <a href="" title="Close viewer"
               ng-click="pdmFilesVm.closeViewerContainer()"
               class="close-button pull-right"
               style="display: inline-block"></a>
        </div>
    </div>
    <kisters-web-view register-kister-call-back="registerKisterCallBack(kisterCallBack)"
                      contwidth="conWidth"></kisters-web-view>
</div>