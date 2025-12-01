<style scoped>
    .stickyheader {
        height: 473px !important;
        overflow: auto !important;
    }
</style>
<div class="stickyheader">
    <style scoped>
        .plugin-container {
            width: 800px;
            margin-left: auto;
            margin-right: auto;
        }

        .plugins-table {
            border: 1px solid #efefef !important;
            background-color: #fff;
        }

        .plugins-table > tbody > tr > td {
            padding: 20px !important;
        }

        .plugins-table > tbody > tr:hover {
            /*box-shadow: 0 2px 5px 0 rgba(0, 0, 0, 0.16), 0 2px 10px 0 rgba(0, 0, 0, 0.12);*/
        }

        .plugins-table .plugin-name {
            font-size: 20px;
            font-weight: bold;
        }

        .plugins-table .plugin-description {
            font-size: 15px;
            margin-top: 10px;
        }

        .plugins-table .plugin-developer {
            margin-top: 10px;
        }

        .plugins-table .enable-button {
            background-color: #0060df;
            width: 100px;
            color: #fff;
            font-size: 14px;
        }

        .plugins-table .enable-button:hover {
            background-color: #003eaa;
        }

        .plugins-table .disable-button {
            background-color: #ea3859;
            width: 100px;
            color: #fff;
            font-size: 14px;
        }

        .plugins-table .disable-button:hover {
            background-color: #b62726;
        }

        .ng-leave {
            display: none !important;
        }

        .plugins-table .plugin-settings {
            font-size: 16px;
            cursor: pointer;
            margin-left: 5px;
        }
    </style>

    <div class="plugin-container">
        <table class="table plugins-table">
            <tbody>
            <tr ng-if="pluginsVm.loading == false && pluginsVm.plugins.length == 0" style="background-image: var(--cassini-linear-gradient);">
                <td colspan="2">There are no plugins.</td>
            </tr>
            <tr ng-repeat="plugin in pluginsVm.plugins" style="background-image: var(--cassini-linear-gradient);"
                ng-if="pluginsVm.loading == false && pluginsVm.plugins.length > 0">
                <td>
                    <div class="plugin-name">{{plugin.name}}</div>
                    <div class="">by <a target="_blank" ng-href="{{plugin.developer.url}}">{{plugin.developer.name}}</a>
                        <span class="plugin-settings" title="Plugin settings"><i class="la la-gear"></i></span>
                    </div>
                    <div class="plugin-description">{{plugin.description}}</div>
                </td>
                <td style="width: 50px">
                    <button class="btn btn-sm enable-button" ng-if="!plugin.enabled"
                            ng-click="pluginsVm.enablePlugin(plugin)">Enable
                    </button>
                    <button class="btn btn-sm disable-button" ng-if="plugin.enabled"
                            ng-click="pluginsVm.disablePlugin(plugin)">Disable
                    </button>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>