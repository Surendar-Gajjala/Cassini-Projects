<%--
  Created by IntelliJ IDEA.
  User: Nageshreddy
  Date: 10-12-2020
  Time: 15:21
  To change this template use File | Settings | File Templates.
--%>
<div style="height: 100%;">
    <style scoped>
        .ht-100 {
            height: 100%;
        }

        .ht-100-10 {
            height: calc(100% - 10px);
        }

        .profiles-container {
            margin-top: 10px;
            max-width: 350px;
            border-right: 1px solid #eee;
            padding-right: 10px;
            padding-left: 5px;
            overflow-y: auto;
        }

        .navigation-container {
            padding: 10px;
            overflow-y: auto;
        }

        .ckbox input[type="checkbox"]:checked + label::after {
            top: 2px;
            left: 1.5px;
        }

        .table tr.nav-expanded td {
            font-weight: 600;
        }

        .mb-10 {
            margin-bottom: 10px;
        }

        .profiles-container .profiles-toolbar {
            line-height: 20px;
            margin-bottom: 10px;
        }

        .profiles-container .profiles-toolbar i {
            cursor: pointer;
            padding: 5px;
        }

        .profiles-container .profiles-toolbar i.la-plus {
            line-height: unset;
        }

        .profiles-container .profiles-toolbar i:hover {
            background-color: #eee;
            border-radius: 3px;
        }

        .profiles-container .app-profile {
            line-height: 35px;
            border-bottom: 1px dotted #bbbcbf;
            cursor: pointer;
            height: 35px;
            padding-left: 10px;
            padding-right: 10px;
        }

        .profiles-container .app-profile.selected {
            background-color: #0081c2;
            color: #fff;
            border-top: 1px solid #0081c2;
            border-bottom: 1px solid #0081c2;
        }

        .profiles-container .app-profile:nth-child(2) {
            border-top: 1px dotted #bbbcbf;
        }

        .profiles-container .app-profile .prof-name {
        }

        .profiles-container .app-profile .profile-btns {
            max-width: 80px;
            display: none;
        }

        .profiles-container .app-profile:hover .profile-btns {
            display: block;
        }

        .profiles-container .app-profile .profile-name-input {
            border: 0 !important;
            background-color: whitesmoke !important;
            width: calc(100% - 80px);
            font-size: 14px !important;
            font-family: var(--cassini-font-family) !important;
            padding: 0;
            color: #050d24 !important;
        }

        .profiles-container .app-profile.selected .profile-name-input {
            color: #fff;
        }

        .navigation-container .table tr:last-child td {
            border-bottom: 1px solid #eee;
        }

        .navigation-container .table tr.select-all-row td {
            border: 0;
        }

        .navigation-container .ckbox input[type="checkbox"]:checked + label::after {
            top: 2px;
            left: 1px;
        }

    </style>

    <div class="ht-100 ht-100-10 d-flex">
        <div class="ht-100 flex profiles-container">
            <div class="profiles-toolbar">
                <span><i class="la la-plus" title="{{profilesVm.newProfileTitle}}"
                         ng-click="profilesVm.addNewProfile()"></i></span>
                <%--<span><i class="las la-save" title="{{profilesVm.saveTitle}}"
                         ng-show="profilesVm.selectedProfile != null"
                         ng-click="profilesVm.saveProfile(profilesVm.selectedProfile, true)"></i></span>--%>
            </div>
            <div class="app-profile d-flex"
                 ng-click="profilesVm.selectProfile(profile)"
                 ng-class="{'selected': profile.selected && !profile.editMode}"
                 ng-repeat="profile in profilesVm.profiles">
                <div class="flex prof-name"
                     title="Double click to edit"
                     ng-if="!profile.editMode"
                     ng-dblclick="profilesVm.setEditMode(profile)">
                    {{profile.name}}
                </div>
                <input id="profile{{$index}}"
                       ng-if="profile.editMode"
                       class="form-control profile-name-input" type="text"
                       ng-model="profile.name"
                       ng-keypress="profilesVm.onEnter($event, profile)">

                <div class="flex profile-btns text-right">
                    <i class="las la-copy" ng-if="!profile.editMode" title="{{profilesVm.copyProfileTitle}}"
                       ng-click="profilesVm.copyProfile(profile)"></i>
                    <i class="las la-edit" ng-if="!profile.editMode && !profile.defaultProfile"
                       title="{{profilesVm.editProfileTitle}}"
                       ng-click="profilesVm.setEditMode(profile)"></i>
                    <i class="las la-check" ng-if="profile.editMode" title="Save"
                       ng-click="profilesVm.saveProfile(profile)"></i>
                    <i class="las la-times" ng-if="!profile.editMode && !profile.defaultProfile"
                       title="{{profilesVm.deleteProfileTitle}}"
                       ng-click="profilesVm.deleteProfile(profile)"></i>
                    <i class="las la-times" ng-if="profile.editMode && !profile.isNew" title="Cancel"
                       ng-click="profilesVm.cancelEditProfile(profile)"></i>
                    <i class="las la-times" ng-if="profile.editMode && profile.isNew" title="Cancel"
                       ng-click="profilesVm.cancelProfile(profile)"></i>
                </div>
            </div>
        </div>
        <div class="ht-100 flex navigation-container" ng-show="profilesVm.selectedProfile != null">
            <h4 class="text-center mb-10">Select Application Features</h4>
            <table class="table">
                <tbody>
                <tr class="select-all-row">
                    <td class="column-width-50 text-center">
                        <div class="ckbox ckbox-primary" style="display: inline-block;">
                            <input id="selectAll" name="selectAll" type="checkbox"
                                   ng-disabled="profilesVm.selectedProfile.defaultProfile"
                                   ng-model="profilesVm.selectAll" ng-click="profilesVm.toggleSelectAll()">
                            <label for="selectAll" class="item-selection-checkbox"></label>
                        </div>
                    </td>
                </tr>
                <tr ng-repeat="nav in profilesVm.navigationItems" ng-class="{'nav-expanded': nav.expanded}">
                    <td class="column-width-50 text-center">
                        <div class="ckbox ckbox-primary" style="display: inline-block;">
                            <input id="nav{{$index}}" name="itemSelected" type="checkbox"
                                   ng-disabled="profilesVm.selectedProfile.defaultProfile"
                                   ng-model="nav.selected" ng-click="profilesVm.toggleSelection(nav)">
                            <label for="nav{{$index}}" class="item-selection-checkbox"></label>
                        </div>
                    </td>
                    <td class="column-width-20">
                            <span ng-if="nav.children.length  > 0">
                                <i class="fa" style="cursor: pointer; font-size: 16px;"
                                   title=""
                                   ng-class="{'fa-caret-right': !nav.expanded, 'fa-caret-down': nav.expanded}"
                                   ng-click="profilesVm.toggleNode(nav)"></i>
                            </span>
                    </td>
                    <td><span ng-class="{'ml-20': nav.level === 1}" translate>{{nav.name}}</span></td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>