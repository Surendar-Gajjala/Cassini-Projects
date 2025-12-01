<div style="height: 100%; overflow-y: auto;margin-left: calc(50% - 200px);">
    <style scoped>
        ul.timeline {
            width: 100%;
            margin-left: -30px !important;
            margin-top: 0;
        }

        ul.timeline:before {
            left: 68px;
            width: 2px;
            background: #ddd;
        }

        ul.timeline .direction-r {
            margin-left: 100px;
            float: unset;
        }

        ul.timeline .direction-r .flag {
            background: rgb(231, 228, 228);
        }

        ul.timeline .direction-r .flag:before {
            width: 15px;
            height: 15px;
            margin-top: -10px;
            background: #2a6fa8;
            border-radius: 10px;
            border: 2px solid #2a6fa8;
            left: -41px;
            top: 18px;
        }

        ul.timeline .direction-r .flag:after {
            border-right-color: rgb(231, 228, 228);
        }

        .file-version {
            font-size: 14px !important;
            font-style: normal !important;
            padding-top: 10px;
        }
    </style>
    <ul class="timeline" style="margin-left: 100px;">
        <li ng-repeat="fileVersion in timelineVm.fileVersions">
            <div class="direction-r">
                <div style="position: relative;display: inline-block;text-align: left;">
                    <span class="flag">Version {{fileVersion.attachedToRevision}}.{{fileVersion.version}}</span>
                    <span class="time-wrapper"></span>
                </div>
                <div class="desc">
                    <span style="font-size: 16px;font-style: normal;">
                        <span style="font-size: 14px;">{{fileVersion.createdDate}}</span>
                        <span style="font-style: italic;font-size: 14px;">
                            ( {{fileVersion.createdByObject.fullName}} )
                        </span>
                    </span>

                    <div>
                        Comments: <br>
                        {{fileVersion.commitObject.message}}
                    </div>
                    <div class="file-version">
                        <img ng-src="{{timelineVm.getThumbnailUrl(fileVersion)}}" alt="" width="250"
                             style="border: 1px solid #ebebeb;"
                             onerror="this.src='app/assets/images/cassini-logo-greyscale-text.png';">
                    </div>
                </div>
            </div>
        </li>
    </ul>
</div>