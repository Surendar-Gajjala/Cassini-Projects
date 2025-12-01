<div>
    <style scoped>

        .view-details {

        }

        .view-details .view-header {
            padding: 20px;
            border-bottom: 1px solid #eee;
        }

        .view-details .view-header .view-title {
            font-size: 16px;
            font-weight: 600;
        }

        .view-details .view-header .view-summary {
            color: #707d91;
            font-weight: 300;
        }

        .round {
            position: relative;
        }

        .round label {
            background-color: #fff;
            border: 1px solid #ccc;
            border-radius: 50%;
            cursor: pointer;
            height: 28px;
            left: 0;
            position: absolute;
            top: 0;
            width: 28px;
        }

        .round label:after {
            border: 2px solid #fff;
            border-top: none;
            border-right: none;
            content: "";
            height: 6px;
            left: 7px;
            opacity: 0;
            position: absolute;
            top: 8px;
            transform: rotate(-45deg);
            width: 12px;
        }

        .round input[type="checkbox"] {
            visibility: hidden;
        }

        .round input[type="checkbox"]:checked + label {
            background-color: #3699ff;
            border-color: #3699ff;
        }

        .round input[type="checkbox"]:checked + label:after {
            opacity: 1;
        }

        .user-role {
            background-color: #eff5fc;
            border: 1px solid #dde8f5;
            padding: 10px 10px;
            border-radius: 5px;
        }

        .user-role .selection-container {
            float: right;
            width: 30px;
            margin-top: -12px;
        }

        .user-role .role-name {
            font-weight: 600;
        }

        .user-role .role-name i {
            margin-left: 5px;
            font-size: 14px;
        }

        .user-role .role-description {
            color: #707d91;
            font-size: 13px;
        }

        .view-details .view-content {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(320px, max-content));
            grid-gap: 30px;
            justify-content: center;
            padding-top: 30px;
            padding-bottom: 30px;
        }

        #freeTextSearchDirective {
            margin-top: -15px !important;
        }

        input.form-control[type=text], input.form-control[type=password], .ui-select-container input.form-control[type=search] {
            padding: 8px 25px !important;
        }

        .view-header #freeTextSearchDirective #freeTextSearchInput {
            width: 195px;
        }

        .search-box {
            padding-left: 50px !important;
        }

    </style>

    <div class="view-details">
        <div class="view-header">
            <div class="view-title" translate>ROLE_USERS</div>
            <div class="view-summary" translate>ASSIGN_OR_REMOVE_USERS</div>
            <free-text-search on-clear="roleUsersVm.resetPage" search-term="roleUsersVm.filters.searchQuery"
                              on-search="roleUsersVm.freeTextSearch"></free-text-search>
        </div>
        <div class="view-content">
            <div class="user-role" style="display: flex;"
                 ng-repeat="user in roleUsersVm.roleUsers | orderBy:'person.fullName' ">
                <div style="width: 70px;">
                    <person-avatar person-id="user.person.id" display="'small'"></person-avatar>
                </div>
                <div style="width: calc(100% - 70px)">
                    <div>
                        <div class="role-name">
                            <span ng-bind-html="user.person.fullName | limitTo: 18 | highlightText: roleUsersVm.filters.searchQuery"
                                  title="{{user.person.fullName}}"></span>
                            <span ng-if="user.person.fullName.length > 18" title="{{user.person.fullName}}">...</span>
                            <i class="las la-cog" ng-if="user.person.defaultGroup == personGroup.groupId"
                               title="Default role"></i>
                        </div>
                        <div class="selection-container">
                            <div class="round"
                                 title="{{user.person.selected ? 'De-select user' : 'Select user'}}">
                                <input type="checkbox" id="checkbox{{$index}}" ng-model="user.person.selected"
                                       ng-click="roleUsersVm.attachedRoleUser(user)"
                                       ng-disabled="{{user.person.defaultGroup == personGroup.groupId}}"/>
                                <label for="checkbox{{$index}}"></label>
                            </div>
                        </div>
                    </div>
                    <div class="role-description">
                        {{user.person.defaultGroupObject.name}}
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>