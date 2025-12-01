<div class="sidepanel-widget user-tasks-widget">
    <style scoped>
        .switch {
            position: relative;
            display: inline-block;
            width: 60px;
            height: 34px;
        }

        .switch input {
            opacity: 0;
            width: 0;
            height: 0;
        }

        .switch .slider {
            position: absolute;
            cursor: pointer;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background-color: #ccc;
            -webkit-transition: .4s;
            transition: .4s;
        }

        .switch .slider:before {
            position: absolute;
            content: "";
            height: 26px;
            width: 26px;
            left: 4px;
            bottom: 4px;
            background-color: white;
            -webkit-transition: .4s;
            transition: .4s;
        }

        .switch input:checked + .slider {
            background-color: #2196F3;
        }

        .switch input:focus + .slider {
            box-shadow: 0 0 1px #2196F3;
        }

        .switch input:checked + .slider:before {
            -webkit-transform: translateX(26px);
            -ms-transform: translateX(26px);
            transform: translateX(26px);
        }

        /* Rounded sliders */
        .switch .slider.round {
            border-radius: 34px;
        }

        .switch .slider.round:before {
            border-radius: 50%;
        }
    </style>

    <div class="widget-body">
        <div class='responsive-table'
             style="height: 100%;overflow:auto;width: 100%;position: relative;padding: 10px !important;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr style="font-size: 14px;">
                    <th><input type="checkbox" checked="" ng-model="userWidgetsVm.selectAll" ng-change="userWidgetsVm.selectAllWidget()"
                            title="{{(userWidgetsVm.selectAll ? 'UN_SELECT_ALL' : 'SELECT_ALL') | translate}}"></th>
                    <th translate>NAME</th>
                    <th class="col-width-150" style="text-align: right" translate>Selection</th>
                </tr>
                </thead>

                <tbody>
                <tr ng-repeat="widget in widgets">
                    <td></td>
                    <td style="vertical-align: middle !important;">
                        <span>{{widget.name}}</span>
                    </td>
                    <td class="col-width-150" style="text-align: right !important; padding-right: 10px !important;">
                        <input type="checkbox" id="{{widget.name}}" switch="none" checked=""
                               ng-model="widget.checked" ng-change="userWidgetsVm.selectWidget(widget)">
                        <label for="{{widget.name}}" data-on-label="Yes" data-off-label="No" style="margin: 0 !important"></label>
                    </td>

                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>