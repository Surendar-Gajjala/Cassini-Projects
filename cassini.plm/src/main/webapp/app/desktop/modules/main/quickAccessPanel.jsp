<div>
    <style scoped>

        .quick-access-panel {
            border: 1px solid #2b5876;
            border-radius: 5px;
            width: 600px;
            height: 450px;
            z-index: 9990;
            margin-left: -300px;
            margin-top: -225px;
            display: none;
            opacity: 0.9 !important;
            overflow-y: auto;
            background: #005C97; /* fallback for old browsers */
            background: -webkit-linear-gradient(to right, #363795, #005C97); /* Chrome 10-25, Safari 5.1-6 */
            background: linear-gradient(to right, #363795, #005C97); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */

        }

        .shortcut {
        }

        .shortcut a:hover {
            color: #54EEFF !important;
        }

        .quick-access-panel.centered {
            position: absolute;
            top: 50%;
            left: 50%;
        }

        #quickAccess .content {
            margin: 20px auto;
            width: 90%;
            display: -webkit-flex; /* Safari */
            -webkit-flex-flow: wrap; /* Safari 6.1+ */
            display: flex;
            flex-flow: wrap;
            text-align: center;
        }

        #quickAccess .content > div {
            margin: 20px;
            width: 132px;
        }

        #quickAccess .content a {
            color: white;
            -webkit-filter: drop-shadow(0 1px 3px rgba(0, 0, 0, 0.4));
            filter: drop-shadow(0 1px 3px rgba(0, 0, 0, 0.4));
            font-size: 14px;
            text-align: center;
            text-shadow: 1px 1px 0 rgba(0, 0, 0, 0.5);
            white-space: nowrap;
        }

        #quickAccess .content a img {
            width: 64px;
            height: 64px;
        }

        #quickAccess .content a i {
            font-size: 64px;
            margin-bottom: 10px;
        }

        #quickAccess .content a:active {
            opacity: .6;
        }

        #quickAccess .content a:link,
        #quickAccess .content a:visited {
            text-decoration: none;
        }

        #quickAccess .content a[class^="icon"] {
            background-position: top;
            background-repeat: no-repeat;
            display: inline-block;
            height: 64px;
            padding: 64px 0 0 0;
            margin: 40px;
            width: 64px;
        }

        .close-btn {
            font: 17px/100% arial, sans-serif !important;
            position: absolute;
            right: 10px;
            text-decoration: none;
            text-shadow: 0 1px 0 #fff;
            top: 10px;
            color: #fff;
            margin-right: 5px;
            opacity: 0.8 !important;
        }

        .close-btn:hover, .close-btn:visited,
        .close-btn:link {
            opacity: 1.0 !important;
            text-shadow: none;
            color: #fff;
            text-decoration: none;
            font-size: 17px;
        }

        .close-btn:active {
            opacity: 0.6 !important;
            text-shadow: none;
            color: #fff;
            text-decoration: none;
        }

        #quickAccess [class^="flaticon-"]::before,
        #quickAccess [class*=" flaticon-"]::before,
        #quickAccess [class^="flaticon-"]::after,
        #quickAccess [class*=" flaticon-"]::after {
            font-size: 64px;
        }

        a {
            cursor: pointer;
        }

        a:hover, a:active {
            text-decoration: none;
        }
    </style>


    <div id="quickAccess" class="centered quick-access-panel style-8" ng-click="mainVm.stopPropagation($event)">
        <a class="close-btn" ng-click="quickAccessVm.close();">x</a>

        <div class="content">
            <div class="text-center shortcut">
                <a href="" ng-click="quickAccessVm.newItem()">
                    <i class="fa fa-th"></i>
                    <div>New Item</div>
                </a>
            </div>
            <div class="text-center shortcut">
                <a href="" ng-click="quickAccessVm.newProject()">
                    <i class="fa fa-calendar"></i>
                    <div>New Project</div>
                </a>
            </div>
            <div class="text-center shortcut">
                <a href="" ng-click="quickAccessVm.newSpecification()">
                    <i class="fa fa-wpforms"></i>
                    <div>New Specification</div>
                </a>
            </div>
            <div class="text-center shortcut">
                <a href="" ng-click="quickAccessVm.newECO()">
                    <i class="fa fa-ils"></i>
                    <div>New ECO</div>
                </a>
            </div>
            <div class="text-center shortcut">
                <a href="" ng-click="quickAccessVm.newWorkflow()">
                    <i class="fa flaticon-plan2"></i>
                    <div>New Workflow</div>
                </a>
            </div>
            <div class="text-center shortcut">
                <a href="" ng-click="quickAccessVm.newManufacturer()">
                    <i class="fa flaticon-office42"></i>
                    <div>New Manufacturer</div>
                </a>
            </div>
            <div class="text-center shortcut">
                <a href="" ng-click="quickAccessVm.gotoClassification()">
                    <i class="fa fa-book"></i>
                    <div>Classification</div>
                </a>
            </div>
            <div class="text-center shortcut">
                <a href="" ng-click="quickAccessVm.gotoAdmin()">
                    <i class="fa fa-lock"></i>
                    <div>Admin</div>
                </a>
            </div>
            <div class="text-center shortcut">
                <a href="" ng-click="quickAccessVm.gotoSettings()">
                    <i class="fa fa-wrench"></i>
                    <div>Settings</div>
                </a>
            </div>
        </div>
    </div>
</div>