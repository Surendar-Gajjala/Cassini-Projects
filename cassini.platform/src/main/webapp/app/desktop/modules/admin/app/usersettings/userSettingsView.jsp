<div class="view-container" fitcontent>
    <style scoped>
        .admin-root {
            background: transparent url("app/assets/bower_components/cassini-platform/images/admin.png") no-repeat !important;
            height: 16px;
        }

        .users-node {
            background: transparent url("app/assets/bower_components/cassini-platform/images/users.png") no-repeat !important;
            height: 16px;
        }

        .user-node {
            background: transparent url("app/assets/bower_components/cassini-platform/images/user.png") no-repeat !important;
            height: 16px;
        }

        .external-user {
            background: transparent url("app/assets/bower_components/cassini-platform/images/external.png") no-repeat !important;
            height: 16px;
        }
        .inActive-user {
            background: transparent url("app/assets/bower_components/cassini-platform/images/inActiveUser.jpg") no-repeat !important;
            height: 16px;
        }

        .roles-node {
            background: transparent url("app/assets/bower_components/cassini-platform/images/role.png") no-repeat !important;
            height: 16px;
        }

        .groups-node {
            background: transparent url("app/assets/bower_components/cassini-platform/images/usergroups.png") no-repeat !important;
            height: 16px;
        }

        .permissions-node {
            background: transparent url("app/assets/bower_components/cassini-platform/images/permissions.png") no-repeat !important;
            height: 16px;
        }
    </style>

    <div class="view-content no-padding" style="overflow: hidden;padding: 10px;">
        <div id="contextMenu" class="context-menu dropdown clearfix"
             style="position: fixed;display:none; z-index: 9999">
            <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu" style=""  >
                <li><a tabindex="-1" href="" id="addType" ng-click="userVm.onCreateSelect()" translate>CREATE</a></li>
                <li ><a tabindex="-1" href="" id="deleteType" ng-click="userVm.onDeleteSelect()" translate>DELETE</a>
                </li>
            </ul>
        </div>

        <div class="split-pane fixed-left">
            <div class="split-pane-component split-left-pane">
                <div id="classificationContainer" class="classification-pane" data-toggle="context"
                     data-target="#context-menu">
                    <ul id="adminTree" class="easyui-tree">
                    </ul>
                </div>
            </div>
            <div class="split-pane-divider"></div>
            <div id="userSettingsView" class="split-pane-component split-right-pane noselect"
                 style="left: 300px;overflow-y: hidden;">
                <div ng-include="userVm.template" onload="setViewHeight()"></div>
            </div>
        </div>
    </div>
</div>


