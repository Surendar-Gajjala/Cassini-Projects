<div style="margin-top: 20px;">
    <style scoped>

        /* ================ The Timeline ================ */

        .rev-timeline {
            position: relative;
            width: 660px;
            margin: 0 auto;
            margin-top: 20px;
            padding: 1em 0;
            list-style-type: none;
        }

        .rev-timeline:before {
            position: absolute;
            left: 50%;
            top: 0;
            content: ' ';
            display: block;
            width: 2px;
            height: 100%;
            margin-left: -3px;
            background-color: #c9c9c9;
            z-index: 5;
        }

        .rev-timeline li {
            padding: 1em 0;
        }

        .rev-timeline li:after {
            content: "";
            display: block;
            height: 0;
            clear: both;
            visibility: hidden;
        }

        .direction-l {
            position: relative;
            width: 300px;
            float: left;
            text-align: right;
        }

        .direction-r {
            position: relative;
            width: 300px;
            float: right;
        }

        .flag-wrapper {
            position: relative;
            display: inline-block;

            text-align: center;
        }

        .flag {
            position: relative;
            display: inline;
            background: rgb(248, 248, 248);
            padding: 6px 10px;
            border-radius: 5px;
            cursor: pointer;
            font-weight: 600;
            text-align: left;
        }

        .flag.item-released {
            background-color: #0390fd;
            color: #fff;
            border: #0390fd;
        }

        .flag.item-rejected {
            background-color: #d9534f;
            color: #fff;
            border: #d9534f;
        }

        .direction-l .flag {
            -webkit-box-shadow: -1px 1px 1px rgba(0, 0, 0, 0.15), 0 0 1px rgba(0, 0, 0, 0.15);
            -moz-box-shadow: -1px 1px 1px rgba(0, 0, 0, 0.15), 0 0 1px rgba(0, 0, 0, 0.15);
            box-shadow: -1px 1px 1px rgba(0, 0, 0, 0.15), 0 0 1px rgba(0, 0, 0, 0.15);
        }

        .direction-r .flag {
            -webkit-box-shadow: 1px 1px 1px rgba(0, 0, 0, 0.15), 0 0 1px rgba(0, 0, 0, 0.15);
            -moz-box-shadow: 1px 1px 1px rgba(0, 0, 0, 0.15), 0 0 1px rgba(0, 0, 0, 0.15);
            box-shadow: 1px 1px 1px rgba(0, 0, 0, 0.15), 0 0 1px rgba(0, 0, 0, 0.15);
        }

        .direction-l .flag:before,
        .direction-r .flag:before {
            position: absolute;
            top: 50%;
            right: -34px !important;
            content: ' ';
            display: block;
            width: 12px;
            height: 12px;
            margin-top: -10px;
            background: #2a6fa8;
            border-radius: 10px;
            border: 4px solid #2a6fa8;
            z-index: 10;
        }

        .direction-r .flag:before {
            left: -38px;
        }

        .direction-l .flag:after {
            content: "";
            position: absolute;
            left: 100% !important;
            top: 50%;
            height: 0;
            width: 0;
            margin-top: -8px;
            border: solid transparent;
            border-left-color: rgb(248, 248, 248);
            border-width: 8px;
            pointer-events: none;
        }

        .direction-l .flag.item-released:after {
            border-left-color: #0390fd;
        }

        .direction-l .flag.item-rejected:after {
            border-left-color: #d9534f;
        }

        .direction-r .flag:after {
            content: "";
            position: absolute;
            right: 100%;
            top: 50%;
            height: 0;
            width: 0;
            margin-top: -8px;
            border: solid transparent;
            border-right-color: rgb(248, 248, 248);
            border-width: 8px;
            pointer-events: none;
        }

        .direction-r .flag.item-released:after {
            border-right-color: #0390fd;
        }

        .direction-r .flag.item-rejected:after {
            border-right-color: #d9534f;
        }

        .time-wrapper {
            display: inline;
            line-height: 1em;
            font-size: 0.66666em;
            color: #2a6fa8;
            vertical-align: middle;
            margin-top: 4px;
        }

        .direction-l .time-wrapper {
            float: left;
        }

        .direction-r .time-wrapper {
            float: right;
        }

        .time {
            display: inline-block;
            padding: 4px 6px;
            /*background: rgb(248,248,248);*/
        }

        .desc {
            margin: 1em 0.75em 0 0;

            font-size: 0.77777em;
            font-style: italic;
            line-height: 1.5em;
        }

        .direction-r .desc {
            margin: 1em 0 0 0.75em;
        }

        /* ================ Timeline Media Queries ================ */

        @media screen and (max-width: 660px) {

            .rev-timeline {
                width: 100%;
                padding: 4em 0 1em 0;
            }

            .rev-timeline li {
                padding: 2em 0;
            }

            .direction-l,
            .direction-r {
                float: none;
                width: 100%;

                text-align: center;
            }

            .flag-wrapper {
                text-align: center;
            }

            .flag {
                background: rgb(255, 255, 255);
                z-index: 15;
            }

            .direction-l .flag:before,
            .direction-r .flag:before {
                position: absolute;
                top: -30px;
                left: 50%;
                content: ' ';
                display: block;
                width: 12px;
                height: 12px;
                margin-left: -9px;
                background: #fff;
                border-radius: 10px;
                border: 4px solid #2a6fa8;
                z-index: 10;
            }

            .direction-l .flag:after,
            .direction-r .flag:after {
                content: "";
                position: absolute;
                left: 50%;
                top: -8px;
                height: 0;
                width: 0;
                margin-left: -8px;
                border: solid transparent;
                border-bottom-color: rgb(255, 255, 255);
                border-width: 8px;
                pointer-events: none;
            }

            .time-wrapper {
                display: block;
                position: relative;
                margin: 4px 0 0 0;
                z-index: 14;
            }

            /* Tooltip container */
            .tooltip {
                position: relative;
                display: inline-block;
                border-bottom: 1px dotted black; /* If you want dots under the hoverable text */
            }

            .tooltip .tooltiptext {
                visibility: hidden;
                width: 120px;
                background- color: #fff;
                text-align: center;
                padding: 5px 0;
                border-radius: 6px;

                /* Position the tooltip text - see examples below! */
                position: absolute;
                z-index: 1;
            }

            /* Show the tooltip text when you mouse over the tooltip container */
            .tooltip:hover .tooltiptext {
                visibility: visible;
            }

            .direction-l .time-wrapper {
                float: none;
            }

            .direction-r .time-wrapper {
                float: none;
            }

            .desc {
                position: relative;
                margin: 1em 0 0 0;
                padding: 1em;
                background: rgb(245, 245, 245);
                -webkit-box-shadow: 0 0 1px rgba(0, 0, 0, 0.20);
                -moz-box-shadow: 0 0 1px rgba(0, 0, 0, 0.20);
                box-shadow: 0 0 1px rgba(0, 0, 0, 0.20);

                z-index: 15;
            }

            .direction-l .desc,
            .direction-r .desc {
                position: relative;
                margin: 1em 1em 0 1em;
                padding: 1em;

                z-index: 15;
            }

        }

        @media screen and (min-width: 400px ? ? max-width:

        660px

        ) {

            .direction-l .desc,
            .direction-r .desc {
                margin: 1em 4em 0 4em;
            }

        }

        .desc table {
            width: 100%;
        }

        .desc table td {
            padding: 5px;
        }

        .direction-l .desc {
            text-align: right;
        }

        .tooltip {
            position: relative;
            display: inline-block;
            border-bottom: 1px dotted black; /* If you want dots under the hoverable text */
        }

        /* Tooltip text */
        .tooltip .tooltiptext {
            visibility: hidden;
            width: 200px;
            background-color: #555;
            color: #fff;
            text-align: center;
            padding: 5px 0;
            border-radius: 6px;

            /* Position the tooltip text */
            position: absolute;
            z-index: 1;
            bottom: 125%;
            left: 50%;
            margin-left: -60px;

            /* Fade in tooltip */
            opacity: 0;
            transition: opacity 0.3s;
        }

        .tooltip .tooltiptext::after {
            content: "";
            position: absolute;
            top: 100%;
            left: 50%;
            margin-left: -5px;
            border-width: 5px;
            border-style: solid;
            border-color: #555 transparent transparent transparent;
        }

        .tooltip:hover .tooltiptext {
            visibility: visible;
            opacity: 1;
        }

        .rev-timeline .tooltip {
            border: none;
            position: absolute;
            text-align: left !important;
        }

        .rev-timeline .tooltip .tooltip-inner {
            text-align: left;
            max-width: 200px;
            width: 200px;
        }

        .rev-timeline .tooltip .tooltip-inner > div > div {
            margin-bottom: 5px;
        }

        .time-wrapper .time {
            color: #8d8e90;
        }
    </style>

    <p ng-if="revHistoryVm.showVersion == false" class="text-muted text-center" style="font-style: italic">
        (Click on revision to view that revision)</p>

    <p ng-if="revHistoryVm.showVersion == true" class="text-muted text-center" style="font-style: italic">
        (Click on version to view that version)</p>

    <ul class="timeline rev-timeline">
        <li ng-repeat="itemRevision in revHistoryVm.itemRevisionHisotry">
            <div ng-class="{'direction-r': $index%2==0, 'direction-l': $index%2!=0}">
                <div class="flag-wrapper">
                    <span class="flag" ng-if="itemRevision.revision != null"
                          ng-class="{'item-released': itemRevision.released,'item-rejected': itemRevision.rejected}"
                          ng-click="revHistoryVm.showItemRevision(itemRevision)">
                         Revision {{itemRevision.revision}}
                        <span ng-if="itemRevision.oldRevision != null && itemRevision.oldRevision != itemRevision.revision">&#8592;</span>
                        <span ng-if="itemRevision.oldRevision != itemRevision.revision">{{itemRevision.oldRevision}}</span>
                    </span>
                     <span class="flag" ng-if="itemRevision.version != null"
                           ng-class="{'item-released': itemRevision.released,'item-rejected': itemRevision.rejected}"
                           ng-click="revHistoryVm.showItemRevision(itemRevision)">
                         Version {{itemRevision.version}}
                     </span>
                    <span class="time-wrapper"><span class="time">{{itemRevision.modifiedDate}}</span></span>
                </div>
                <div class="desc">
                    <table>
                        <tr ng-repeat="history in itemRevision.statusHistory">
                            <td class="text-left">{{history.oldStatus.phase}}</td>
                            <td style="font-size: 18px;font-style: normal;">&rarr;</td>
                            <td class="text-left">{{history.newStatus.phase}}</td>
                            <td>{{history.timestamp}}</td>
                        </tr>
                    </table>

                    <span ng-if="itemRevision.notes != null" style="font-size: 16px;font-style: normal;">
                        <span style="font-size: 14px;">[Comment: {{itemRevision.notes}}]</span>
                     </span>

                    <div ng-if="itemRevision.plmeco != null && itemRevision.plmeco != ''">
                        ECO:
                        <a href="" tooltip-placement="right"
                           tooltip-trigger="'mouseenter'" uib-tooltip-html="'
                           <div>
                                <div>
                                    <span>Number:<br/> {{itemRevision.plmeco.ecoNumber}}</span>
                                </div>

                                <div>
                                    <span>Title:<br/> {{itemRevision.plmeco.title}}</span>
                                </div>
                                <div>
                                    <span>Description:<br/> {{itemRevision.plmeco.description}}</span>
                                </div>
                           </div>
                        '">
                            {{itemRevision.plmeco.ecoNumber}}
                        </a>
                    </div>
                    <div ng-if="itemRevision.dco != null && itemRevision.dco != ''">
                        DCO:
                        <a href="" tooltip-placement="right"
                           tooltip-trigger="'mouseenter'" uib-tooltip-html="'
                           <div>
                                <div>
                                    <span>Number:<br/> {{itemRevision.dco.dcoNumber}}</span>
                                </div>

                                <div>
                                    <span>Title:<br/> {{itemRevision.dco.title}}</span>
                                </div>
                                <div>
                                    <span>Description:<br/> {{itemRevision.dco.description}}</span>
                                </div>
                           </div>
                        '">
                            {{itemRevision.dco.dcoNumber}}
                        </a>
                    </div>
                    <div ng-if="itemRevision.mcoId != null && itemRevision.mcoId != ''">
                        MCO:
                        <a href="" tooltip-placement="right"
                           tooltip-trigger="'mouseenter'" uib-tooltip-html="'
                           <div>
                                <div>
                                    <span>Number:<br/> {{itemRevision.mcoNumber}}</span>
                                </div>

                                <div>
                                    <span>Title:<br/> {{itemRevision.mcoTitle}}</span>
                                </div>
                                <div>
                                    <span>Description:<br/> {{itemRevision.mcoDescription}}</span>
                                </div>
                           </div>
                        '">
                            {{itemRevision.mcoNumber}}
                        </a>
                    </div>
                    <div ng-if="itemRevision.mbomRevision != null && itemRevision.mbomRevision != ''">
                        MBOM:
                        <a href="" tooltip-placement="right"
                           tooltip-trigger="'mouseenter'" uib-tooltip-html="'
                           <div>
                                <div>
                                    <span>Number:<br/> {{itemRevision.mbomNumber}}</span>
                                </div>

                                <div>
                                    <span>Name:<br/> {{itemRevision.mbomName}}</span>
                                </div>
                                <div>
                                    <span>Revision:<br/> {{itemRevision.mbomRevisionName}}</span>
                                </div>
                           </div>
                        '">
                            {{itemRevision.mbomNumber}} - {{itemRevision.mbomRevisionName}}
                        </a>
                    </div>
                </div>
            </div>
        </li>
    </ul>
</div>