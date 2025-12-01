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
            height: calc(100% - 40px) !important;
        }

        /*.responsive-table {
            height: 100%;
            overflow-y: auto;
        }*/

        .vault-object-details {
            border-left: 1px solid #ddd;
            width: 350px;
            padding: 20px;
            /*background-color: #f8f8f8;*/
            background-image: var(--cassini-linear-gradient);
            overflow-y: auto;
            transition: width 5s;
            z-index: 11;
        }

        .vault-container .vault-child {
            flex: 1;
            display: flex;
            flex-flow: column;
            border: 1px solid #ddd;
            border-radius: 5px;
            height: 225px;
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
            /*display: flex;*/
            display: none;
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
            margin-right: 10px;
            margin-top: 5px;
            font-size: 20px;
            padding-right: 10px;
            font-weight: 300;
            border-right: 1px solid #ddd;
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
            width: 100%;
            overflow-y: auto;
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

        .vault-children table th.actions-column {
            width: 24px;
            text-align: center;
        }

        .view-content .vault-children {
            width: calc(100% - 350px);
        }

        .view-content .vault-children.show-full {
            width: 100%;
        }

        .view-content .responsive-table {
            padding: 10px;
            position: absolute;
            bottom: 40px;
            top: 0;
            overflow: auto;
        }

        .auto-width {
            width: calc(100% - 350px) !important;
        }

        .view-content .responsive-table table thead th {
            position: -webkit-sticky;
            position: sticky;
            top: -10px;
            z-index: 5;
        }

        .view-content .table-footer {
            padding: 0 10px 0 10px;
            position: absolute;
            bottom: 0px !important;
            height: 40px;
            width: 100%;
            border-top: 1px solid #D3D7DB;
            display: table;
        }

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

        a.disabled {
            cursor: not-allowed;
            color: lightgrey;
        }

        .dropdown:hover .dropdown-menu {
            display: block;
            margin-top: 0;
        / / remove the gap so it doesn 't close
        }

        .highlight-row tbody tr.selected td:first-child {
            background: #3498db !important;
            background-image: -webkit-linear-gradient(top, #3498db, #2980b9) !important;
            background-image: -moz-linear-gradient(top, #3498db, #2980b9) !important;
            background-image: -ms-linear-gradient(top, #3498db, #2980b9) !important;
            background-image: -o-linear-gradient(top, #3498db, #2980b9) !important;
            background-image: linear-gradient(to bottom, #3498db, #2980b9) !important;
            color: #fff !important;
        }

        tbody > tr > td i.la-gear {
            visibility: hidden;
        }

        tbody > tr:hover > td i.la-gear {
            visibility: visible;
        }

    </style>

    <div class="view-container" fitcontent>
        <div class="view-toolbar">
            <span class="path-label" style=""
                  ng-bind-html="vaultDetailsVm.getBreadCrumbsLink(vaultDetailsVm.vault, vaultDetailsVm.folder);"></span>

            <div class="btn-group">
                <button class="btn btn-sm" ng-click="vaultDetailsVm.viewType='table';vaultDetailsVm.resizeContainer();"
                        title="Table view">
                    <i class="las la-th"></i>
                </button>
                <button class="btn btn-sm" ng-click="vaultDetailsVm.viewType='cards';vaultDetailsVm.resizeContainer();"
                        title="Card view">
                    <i class="las la-border-all"></i>
                </button>
            </div>
            <free-text-search on-clear="vaultDetailsVm.resetPage" search-term="vaultDetailsVm.searchText"
                              on-search="vaultDetailsVm.freeTextSearch"
                              filter-search="vaultDetailsVm.filterSearch"></free-text-search>
        </div>

        <div class="view-content no-padding" style="display: flex;overflow-y: auto;"
             ng-if="vaultDetailsVm.viewType == 'cards'"
             ng-class="{'show-scroll': vaultDetailsVm.selectedFile == null}">
            <div class="vault-container" ng-class="{'show-full': vaultDetailsVm.selectedFile == null}">
                <div class="no-data-container text-center"
                     ng-if="!vaultDetailsVm.loading && vaultDetailsVm.children.content.length === 0">
                    <div class="no-data-img">
                        <img src="app/assets/images/no-data.png" alt="">
                    </div>
                    <div class="no-data-message">
                        Ooops...there is no data here!
                    </div>
                </div>
                <div class="vault-child" ng-repeat="child in vaultDetailsVm.children.content"
                     ng-class="{'selected': child.id === vaultDetailsVm.selectedFile.id}"
                     ng-click="vaultDetailsVm.onClickChild(child)">

                    <div class="vault-item-toolbar" ng-click="$event.stopPropagation();"
                         uib-dropdown dropdown-append-to-body
                         ng-if="child.objectType === 'PDM_FILEVERSION'">
                        <i class="fa fa-ellipsis-h" uib-dropdown-toggle></i>
                        <ul uib-dropdown-menu class="dropdown-menu" role="menu"
                            style="z-index: 9999 !important;">
                            <li ng-show="child.file.fileType=='PART'">
                                <a href="" ng-click="vaultDetailsVm.showPartDetails($event, child.id)">Open
                                    Details</a>
                            </li>
                            <li ng-show="child.file.fileType=='ASSEMBLY'">
                                <a href=""
                                   ng-click="vaultDetailsVm.showAssemblyDetails($event, child.id)">Open
                                    Details</a>
                            </li>
                            <%--<li><a href="" ng-click="vaultDetailsVm.showVisualization($event, child)">Show AutoDesk
                                Visualization</a></li>
                            <li><a href="" ng-click="vaultDetailsVm.showInKistersWebViewer($event, child)">Show Kisters
                                Visualization</a></li>--%>
                        </ul>
                    </div>

                    <div class="vault-child-image" ng-if="child.objectType === 'PDM_FOLDER'">
                        <img src="app/assets/images/folder-green.png" alt="" width="100">
                    </div>

                    <div class="vault-child-body" ng-if="child.objectType === 'PDM_FOLDER'"
                         ng-click="vaultDetailsVm.onClickChild(child)">
                        <h5 class="text-center"><span
                                ng-bind-html="child.name | highlightText: freeTextQuery"></span></h5>
                    </div>
                    <div class="vault-footer" ng-if="child.objectType === 'PDM_FOLDER'">
                        <div class="metrics-container flex-row flex-start" title="Assemblies">
                            <div class="metrics-counts-label assemblies">A</div>
                            <div class="metrics-counts">{{child.assemblasCount}}</div>
                        </div>
                        <div class="metrics-container flex-row flex-center" title="Parts">
                            <div class="metrics-counts-label parts">P</div>
                            <div class="metrics-counts">{{child.partsCount}}</div>
                        </div>
                        <div class="metrics-container flex-row flex-center" title="Drawings">
                            <div class="metrics-counts-label drawings">D</div>
                            <div class="metrics-counts">{{child.drawingsCount}}</div>
                        </div>
                        <div class="metrics-container flex-row flex-end" title="Commits">
                            <div class="metrics-counts-label commits">C</div>
                            <div class="metrics-counts">{{child.commitsCount}}</div>
                        </div>
                    </div>

                    <div class="vault-child-image file-thumbnail" ng-if="child.objectType === 'PDM_FILEVERSION'">
                        <img ng-src="{{vaultDetailsVm.getThumbnailUrl(child)}}" alt="" width="200"
                             onerror="this.src='app/assets/images/cassini-logo-greyscale-text.png';">
                    </div>
                    <div class="vault-child-body" ng-if="child.objectType === 'PDM_FILEVERSION'">
                        <h5 class="text-center"><span
                                ng-bind-html="child.name | highlightText: freeTextQuery"></span></h5>

                        <div class="flex-container" style="margin-top:auto; margin-bottom: 5px; font-size: 12px">
                            <div class="flex-item ml-10">
                                <span>File Size</span> <br> <span class="file-size">{{child.size}}</span>
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
            </div>
            <div class="table-footer" ng-class="{'auto-width':vaultDetailsVm.selectedFile != null}">
                <table-footer objects="vaultDetailsVm.children" pageable="vaultDetailsVm.pageable"
                              previous-page="vaultDetailsVm.previousPage"
                              next-page="vaultDetailsVm.nextPage"
                              page-size="vaultDetailsVm.pageSize"></table-footer>
            </div>

            <div class="vault-object-details pb-animate-if-fadeLeft" ng-if="vaultDetailsVm.selectedFile != null">
                <div ng-if="vaultDetailsVm.selectedFile != null">
                    <div class="row">
                        <div class="col-sm-8">
                            <span style="font-weight: 600;font-size: 20px;" class="ng-binding">Details</span>
                        </div>
                        <div class="col-sm-4" style="text-align: right">
                            <span ng-click="vaultDetailsVm.hideDetails()" class="la la-times" style="cursor: pointer"
                                  title="close"></span>
                        </div>
                    </div>

                    <div class="selected-file-image mt-20">
                        <img ng-src="{{vaultDetailsVm.getThumbnailUrl(vaultDetailsVm.selectedFile)}}" alt=""
                             width="100%"
                             onerror="this.src='app/assets/images/cassini-logo-greyscale-text.png';">
                    </div>
                    <h5 class="text-center">{{vaultDetailsVm.selectedFile.name}}</h5>

                    <div class="object-details-info">
                        <div class="details-row">
                            <div class="details-label">File Name</div>
                            <div class="details-value">{{vaultDetailsVm.selectedFile.name}}</div>
                        </div>

                        <div class="details-row">
                            <div class="details-label">File Size</div>
                            <div class="details-value">
                                {{vaultDetailsVm.selectedFile.size}}
                            </div>
                        </div>

                        <div class="details-row">
                            <div class="details-label">Part Number</div>
                            <div class="details-value">
                                <a href=""
                                   ui-sref="app.items.details({itemId: vaultDetailsVm.selectedFile.attachedToObject.master.plmItem.latestRevision})">
                                    {{vaultDetailsVm.selectedFile.attachedToObject.master.plmItem.itemNumber}}
                                </a>
                            </div>
                        </div>

                        <div class="details-row">
                            <div class="details-label">Version</div>
                            <div class="details-value">{{vaultDetailsVm.selectedFile.version}}</div>
                        </div>

                        <div class="details-row">
                            <div class="details-label">Versions</div>
                            <div class="details-value">{{vaultDetailsVm.selectedFile.file.versions}}</div>
                        </div>

                        <div class="details-row">
                            <div class="details-label">Last Modified</div>
                            <div class="details-value">{{vaultDetailsVm.selectedFile.modifiedDate}}</div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="viewer-container"
                 ng-show="vaultDetailsVm.showViewerContainer"
                 ng-class="{'show-full': vaultDetailsVm.selectedFile === null}">
                <div class="placeholder-container text-center"
                     ng-if="!vaultDetailsVm.showViewer && !vaultDetailsVm.kistersViewer">
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
                        ng-show="vaultDetailsVm.showViewer"
                        frameborder="0" height="100%" width="100%"></iframe>
                <iframe ng-show="vaultDetailsVm.kistersViewer" id="WebViewFrame" src="" width="100%"
                        height="95%"></iframe>
                <div>
                    <a href="" title="Close viewer"
                       ng-click="vaultDetailsVm.closeViewerContainer()"
                       class="close-button pull-right"
                       style="display: inline-block"></a>
                </div>
            </div>
            <kisters-web-view register-kister-call-back="registerKisterCallBack(kisterCallBack)"
                              contwidth="conWidth"></kisters-web-view>
        </div>

        <div class="view-content no-padding" style="display: flex;position: relative !important;"
             ng-if="vaultDetailsVm.viewType == 'table'"
             ng-class="{'show-scroll': vaultDetailsVm.selectedFile == null}">
            <div class="vault-children" ng-if="vaultDetailsVm.viewType == 'table'"
                 ng-class="{'show-full': vaultDetailsVm.selectedFile == null}">
                <div class="responsive-table" ng-class="{'auto-width':vaultDetailsVm.selectedFile != null}">
                    <table class="table table-striped bom-table highlight-row">
                        <thead>
                        <tr>
                            <th class="actions-column"></th>
                            <th>Name</th>
                            <th class="text-center">Revision.Version</th>
                            <th>Part Number</th>
                            <th>File Size</th>
                            <th>Last Modified</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="child in vaultDetailsVm.children.content"
                            ng-class="{'selected': child.id === vaultDetailsVm.selectedFile.id}"
                            ng-click="vaultDetailsVm.onClickChild(child)">
                            <td>
                                <span uib-dropdown dropdown-append-to-body
                                      ng-if="child.objectType != 'PDM_FOLDER'">
                                    <i class="la la-gear" style="cursor: pointer"
                                       onclick="event.stopPropagation()"
                                       dropdown-toggle
                                       uib-dropdown-toggle></i>
                                    <ul uib-dropdown-menu class="dropdown-menu" role="menu"
                                        style="z-index: 9999 !important;">
                                        <li ng-show="child.file.fileType=='PART'">
                                            <a href="" onclick="event.stopPropagation()"
                                               ng-click="vaultDetailsVm.showPartDetails($event, child.id)">Open
                                                Details</a>
                                        </li>
                                        <li ng-show="child.file.fileType=='ASSEMBLY'">
                                            <a href="" onclick="event.stopPropagation()"
                                               ng-click="vaultDetailsVm.showAssemblyDetails($event, child.id)">Open
                                                Details</a>
                                        </li>
                                    </ul>
                                </span>
                            </td>
                            <td colspan="4" ng-if="child.objectType === 'PDM_FOLDER'">
                                <span style="position: absolute;margin-top: -2px;">
                                    <img src="app/assets/images/folder-green.png" alt="" width="16">
                                </span>
                                <span style="margin-left: 24px;"><span
                                        ng-bind-html="child.name | highlightText: freeTextQuery"></span></span>
                            </td>
                            <td ng-if="child.objectType === 'PDM_FILEVERSION'">
                                <span ng-if="child.file.fileType == 'ASSEMBLY'">
                                    <img src="app/assets/images/assembly-icon.png" alt="" width="16">
                                </span>
                                <span ng-if="child.file.fileType == 'PART'">
                                    <img src="app/assets/images/part-icon.png" alt="" width="16">
                                </span>
                                <span ng-if="child.file.fileType == 'DRAWING'">
                                    <img src="app/assets/images/drawing-icon.png" alt="" width="16">
                                </span>
                                <span><span
                                        ng-bind-html="child.name | highlightText: freeTextQuery"></span></span>
                            </td>
                            <td class="text-center" ng-if="child.objectType === 'PDM_FILEVERSION'">
                                {{child.attachedToObject.revision}}.{{child.version}}
                            </td>
                            <td ng-if="child.objectType === 'PDM_FILEVERSION'">
                                <a href="" onclick="event.stopPropagation()"
                                   ui-sref="app.items.details({itemId: child.attachedToObject.master.plmItem.latestRevision})">
                                    {{child.attachedToObject.master.plmItem.itemNumber}}
                                </a>
                            </td>
                            <td ng-if="child.objectType === 'PDM_FILEVERSION'">{{child.size}}</td>
                            <td>{{child.modifiedDate}}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="table-footer" ng-class="{'auto-width':vaultDetailsVm.selectedFile != null}">
                    <table-footer objects="vaultDetailsVm.children" pageable="vaultDetailsVm.pageable"
                                  previous-page="vaultDetailsVm.previousPage"
                                  next-page="vaultDetailsVm.nextPage"
                                  page-size="vaultDetailsVm.pageSize"></table-footer>
                </div>
            </div>
            <div class="vault-object-details pb-animate-if-fadeLeft" ng-if="vaultDetailsVm.selectedFile != null">
                <div ng-if="vaultDetailsVm.selectedFile != null">
                    <div class="row">
                        <div class="col-sm-8">
                            <span style="font-weight: 600;font-size: 20px;" class="ng-binding">Details</span>
                        </div>
                        <div class="col-sm-4" style="text-align: right">
                            <span ng-click="vaultDetailsVm.hideDetails()" class="la la-times" style="cursor: pointer"
                                  title="close"></span>
                        </div>
                    </div>

                    <div class="selected-file-image mt-20">
                        <img ng-src="{{vaultDetailsVm.getThumbnailUrl(vaultDetailsVm.selectedFile)}}" alt=""
                             width="100%"
                             onerror="this.src='app/assets/images/cassini-logo-greyscale-text.png';">
                    </div>
                    <h5 class="text-center">{{vaultDetailsVm.selectedFile.name}}</h5>

                    <div class="object-details-info">
                        <div class="details-row">
                            <div class="details-label">File Name</div>
                            <div class="details-value">{{vaultDetailsVm.selectedFile.name}}</div>
                        </div>

                        <div class="details-row">
                            <div class="details-label">File Size</div>
                            <div class="details-value">
                                {{vaultDetailsVm.selectedFile.size}}
                            </div>
                        </div>

                        <div class="details-row">
                            <div class="details-label">Part Number</div>
                            <div class="details-value">
                                <a href=""
                                   ui-sref="app.items.details({itemId: vaultDetailsVm.selectedFile.attachedToObject.master.plmItem.latestRevision})">
                                    {{vaultDetailsVm.selectedFile.attachedToObject.master.plmItem.itemNumber}}
                                </a>
                            </div>
                        </div>

                        <div class="details-row">
                            <div class="details-label">Version</div>
                            <div class="details-value">{{vaultDetailsVm.selectedFile.version}}</div>
                        </div>

                        <div class="details-row">
                            <div class="details-label">Versions</div>
                            <div class="details-value">{{vaultDetailsVm.selectedFile.file.versions}}</div>
                        </div>

                        <div class="details-row">
                            <div class="details-label">Last Modified</div>
                            <div class="details-value">{{vaultDetailsVm.selectedFile.modifiedDate}}</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>

</div>