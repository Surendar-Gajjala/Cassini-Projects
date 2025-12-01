<div style="padding: 10px">
    <style scoped>
        .timeline {
            width: 250px;
        }
        .timeline::before {
            left: -80px;
        }
        .timeline .flag {
            cursor: pointer;
        }
        .timeline .time-wrapper {
            margin-top: 1px;
        }
        .timeline .time {
            font-size: 12px;
        }
    </style>


    <h4>{{fileHistoryVm.fileName}}</h4>
    <p class="text-muted" style="font-style: italic">(Click on version to download)</p>
    <ul class="timeline">
        <li ng-repeat="fileVersion in fileHistoryVm.fileVersions">
            <div class="direction-r">
                <div class="flag-wrapper">
                    <span class="flag" style="" ng-click="fileHistoryVm.downloadFile(fileVersion)">Version {{fileVersion.version}}</span>
                    <span class="time-wrapper">
                    <span class="time">{{fileVersion.timeStamp}}</span>
                </span>
                </div>
                <div class="desc"></div>
            </div>
        </li>
    </ul>
</div>