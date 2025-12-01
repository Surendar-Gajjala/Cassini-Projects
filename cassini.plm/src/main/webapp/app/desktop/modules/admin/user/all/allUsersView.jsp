<div>
    <style scoped>

        .users-container {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, max-content));
            grid-gap: 20px;
            justify-content: center;
            padding: 150px 15px 30px 15px;
            margin: 0 100px;
            bottom: 40px;
            height: calc(100% - 40px);
            overflow: auto;
        }

        .users-container .user-card {
            position: relative;
            height: 250px;
            background-color: #fff;
            box-shadow: 0 0 30px 0 rgba(82, 63, 105, .05);
            border-radius: 5px;
            padding: 20px;
            cursor: pointer;
        }

        .users-container .user-card:hover {
            box-shadow: 0 7px 14px rgba(147, 148, 150, 0.25),
            0 5px 5px rgba(177, 177, 179, 0.22);
            transition: all .2s ease;
            transform: translate3D(0, -1px, 0) scale(1.01);
        }

        .users-container .user-card .user-image {
            width: 75px;
            height: 75px;
            border-radius: 50%;
            background-color: #e1f0ff;
            color: #3699ff;
            margin-right: 15px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 20px;
        }

        .d-flex {
            display: flex !important;
        }

        .flex-column {
            flex-direction: column;
        }

        .align-items-center {
            -webkit-box-align: center !important;
            -ms-flex-align: center !important;
            align-items: center !important;
        }

        .user-card .label-name {
            font-size: 16px;
            font-weight: 600 !important;
        }

        .user-card .label-role {
            color: #707d91 !important;
        }

        .user-card .user-details {
            height: 155px;
            padding: 20px 0 20px 20px;
        }

        .user-card .user-details div {
            padding: 5px;
            margin-bottom: 5px;
            color: #707d91;
        }

        .user-card .user-details div i {
            margin-right: 5px;
        }

        .user-card .user-actions {
            height: 30px;
            text-align: center;
        }

        .user-card .user-actions .btn {
            margin-right: 5px;
        }

        .user-card .user-actions .btn:last-child {
            margin-right: 0;
        }

        #panel {
            padding: 5px 0;
            position: fixed;
            background-color: #f9fbfe;
            z-index: 5;
            width: 98%;
            text-align: center;
        }

        #panel .panels-container {

        }

        #panel .panels-container span:first-child {
            border-radius: 3px 0 0 3px;
        }

        .panel-summary-total {
            height: 34px;
            margin: 3px -20px 0 8px;
            display: inline-block;
            width: 230px;
            border-radius: 0 3px 3px 0;
            padding-left: 10px;
            line-height: 34px;
        }

        .panel-summary-total span:first-child {
            width: auto;
            height: 34px;
        }

        .panel-summary-total span:first-child h2 {
            margin: 0;
            line-height: 34px;
            color: #fff;
            font-size: 17px;
            display: inline-block;
            padding-right: 10px;
            border-right: 1px solid #fff;
            width: 160px;
        }

        .panel-summary-total span:nth-child(2) h1 {
            margin: 0;
            line-height: 34px;
            color: #fff;
            font-size: 18px;
            display: inline-block;
            width: 40px;
        }

        .panel-summary-total h2,
        .panel-summary-total h1 {
            text-align: center;
        }

        .panel-summary-total h1 {
            font-size: 16px;
        }

        .panel-summary {
            height: 34px;
            margin: 3px -20px 0 8px;
            display: inline-block;
            width: 180px;
            border-radius: 0 3px 3px 0;
            padding-left: 10px;
            line-height: 34px;
        }

        .panel-summary span:first-child {
            width: 150px;
            height: 34px;
        }

        .panel-summary span:first-child h2 {
            margin: 0;
            line-height: 34px;
            color: #fff;
            font-size: 17px;
            display: inline-block;
            padding-right: 10px;
            border-right: 1px solid #fff;
            width: 120px;
        }

        .panel-summary span:nth-child(2) h1 {
            margin: 0;
            line-height: 34px;
            color: #fff;
            font-size: 18px;
            display: inline-block;
            width: 40px;
        }

        .panel-summary h2,
        .panel-summary h1 {
            text-align: center;
        }

        .panel-summary h1 {
            font-size: 16px;
        }

        .panel-licenses {
            border-radius: 3px !important;
            background: green; /* fallback for old browsers */
            background: -webkit-linear-gradient(to left, green, green); /* Chrome 10-25, Safari 5.1-6 */
            background: linear-gradient(to left, green, green); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
        }

        .panel-total {
            border-radius: 3px !important;
            background: #005C97; /* fallback for old browsers */
            background: -webkit-linear-gradient(to left, #363795, #005C97); /* Chrome 10-25, Safari 5.1-6 */
            background: linear-gradient(to left, #363795, #005C97); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
        }

        .panel-finish {
            background: #159957; /* fallback for old browsers */
            background: -webkit-linear-gradient(to right, #159957, #155799); /* Chrome 10-25, Safari 5.1-6 */
            background: linear-gradient(to right, #159957, #155799); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
        }

        .panel-pass {
            background: #5cb85c; /* fallback for old browsers */
            background: -webkit-linear-gradient(to right, #5cb85c, #12b80b); /* Chrome 10-25, Safari 5.1-6 */
            background: linear-gradient(to right, #5cb85c, #12b80b); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
        }

        .panel-fail {
            background: #d9534f; /* fallback for old browsers */
            background: -webkit-linear-gradient(to right, #d98e8e, #d9534f); /* Chrome 10-25, Safari 5.1-6 */
            background: linear-gradient(to right, #d98e8e, #d9534f); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
        }

        .panel-inprogress {
            background: #fdc830; /* fallback for old browsers */
            background: -webkit-linear-gradient(to right, #fdc830, #f37335); /* Chrome 10-25, Safari 5.1-6 */
            background: linear-gradient(to right, #fdc830, #f37335); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
        }

        .count-cards {
            padding-top: 10px;
            position: fixed;
            width: 100%;
            z-index: 99;
            background-color: #fff;
        }

        .count-cards .card-widget {
            height: 100px;
            margin-right: 5px;
            box-shadow: 0 0 30px 0 rgba(82, 63, 105, .05);
            padding: 20px 20px;
            text-align: center;
        }

        .count-cards .card-widget.card-licenses {
            background-image: linear-gradient(to top, #cfd9df 0%, #e2ebf0 100%);
        }

        .count-cards .card-widget.card-total-users {
            background-image: linear-gradient(to top, #c1dfc4 0%, #deecdd 100%);
        }

        .count-cards .card-widget.card-active-users {
            background-image: linear-gradient(-225deg, #B7F8DB 0%, #50A7C2 100%);
        }

        .count-cards .card-widget.card-inactive-users {
            background-image: linear-gradient(45deg, #ff9a9e 0%, #fad0c4 99%, #fad0c4 100%);
        }

        .count-cards .card-widget.card-active-extenral {
            background-image: linear-gradient(to top, #6a85b6 0%, #bac8e0 100%);
        }

        .count-cards .card-widget.card-inactive-external {
            background-image: linear-gradient(to left, #BDBBBE 0%, #9D9EA3 100%), radial-gradient(88% 271%, rgba(255, 255, 255, 0.25) 0%, rgba(254, 254, 254, 0.25) 1%, rgba(0, 0, 0, 0.25) 100%), radial-gradient(50% 100%, rgba(255, 255, 255, 0.30) 0%, rgba(0, 0, 0, 0.30) 100%);
            background-blend-mode: normal, lighten, soft-light;
        }

        .count-cards .card-widget .card-count {
            font-size: 40px;
            font-weight: 400;
        }

        .count-cards .card-widget .card-label {
            margin-top: 20px;
            font-size: 18px;
            font-weight: 400;
        }

    </style>

    <div class="count-cards">
        <div class="row">
            <div class="col-sm-2">
                <div class="card-widget card-licenses">
                    <div class="card-count">{{allUsersVm.userCounts.activeUsers}}/{{allUsersVm.licenses}}</div>
                    <div class="card-label" translate>Total Licenses</div>
                </div>
            </div>

            <div class="col-sm-2">
                <div class="card-widget card-total-users">
                    <div class="card-count">{{allUsersVm.userCounts.totalUsers}}</div>
                    <div class="card-label" translate>Total Users</div>
                </div>
            </div>

            <div class="col-sm-2">
                <div class="card-widget card-active-users">
                    <div class="card-count">{{allUsersVm.userCounts.activeUsers}}</div>
                    <div class="card-label" translate>Active Users</div>
                </div>
            </div>

            <div class="col-sm-2">
                <div class="card-widget card-inactive-users">
                    <div class="card-count">{{allUsersVm.userCounts.inActiveUsers}}</div>
                    <div class="card-label" translate>Inactive Users</div>
                </div>
            </div>

            <div class="col-sm-2">
                <div class="card-widget card-active-extenral">
                    <div class="card-count">{{allUsersVm.userCounts.activeExternalUsers}}</div>
                    <div class="card-label" translate>Active External Users</div>
                </div>
            </div>

            <div class="col-sm-2">
                <div class="card-widget card-inactive-external">
                    <div class="card-count">{{allUsersVm.userCounts.inActiveExternalUsers}}</div>
                    <div class="card-label" translate>Inactive External Users</div>
                </div>
            </div>
        </div>
    </div>

    <div class="users-container" ng-show="adminVm.viewType == 'cards'">
        <div class="user-card"
             ng-repeat="login in allUsersVm.logins.content | orderBy:  ['(isSuperUser!=true)','(isActive!=true)','(external==true)', 'loginName']"
             ng-click="allUsersVm.showUserDetails(login)">
            <div class="ribbon ribbon-blue" ng-if="login.external"><span>EXTERNAL</span></div>
            <div class="ribbon ribbon-green" ng-if="login.isSuperUser"><span>ADMIN</span></div>
            <div class="ribbon ribbon-red" ng-if="!login.isActive"><span>INACTIVE</span></div>
            <div class="d-flex align-items-center">
                <person-avatar person-id="login.person.id" display="'normal'"></person-avatar>
                <div class="d-flex flex-column">
                    <div class="label-name">{{login.person.fullName}}</div>
                    <div class="label-role">{{login.person.defaultGroupObject.name}}</div>
                </div>
            </div>
            <div class="user-details">
                <div>
                    <i class="las la-user"></i>
                    <span ng-bind-html="login.person.fullName | highlightText: allUsersVm.filters.searchQuery"></span>
                </div>
                <div>
                    <i class="las la-envelope"></i>
                    <span style="word-break: break-all"
                          ng-bind-html="login.person.email | highlightText: allUsersVm.filters.searchQuery"></span>
                </div>
                <div>
                    <i class="las la-phone"></i>
                    <span ng-bind-html="login.person.phoneMobile | highlightText: allUsersVm.filters.searchQuery"></span>
                </div>
            </div>
        </div>
        <div ng-if="allUsersVm.noResults == true">
            <div class="no-data">
                <img src="app/assets/no_data_images/Files.png" alt="" class="image">

                <div class="message">{{ 'NO_SEARCH_RESULT_FOUND' | translate}}</div>
                <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE
                </div>
            </div>
        </div>
    </div>

    <div class="responsive-table" ng-show="adminVm.viewType == 'table'" style="top: 110px;">
        <table class="table table-striped highlight-row">
            <thead>
            <tr>
                <th></th>
                <th translate>NAME</th>
                <th translate>USERNAME</th>
                <th translate>DEFAULT_ROLE</th>
                <th translate>PHONE_NUMBER</th>
                <th translate>EMAIL</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="allUsersVm.loading == true">
                <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_USERS</span>
                        </span>
                </td>
            </tr>

            <tr ng-if="allUsersVm.loading == false && allUsersVm.logins.content.length == 0">
                <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                    <div class="no-data">
                        <img src="app/assets/no_data_images/Files.png" alt="" class="image">

                        <div class="message">{{ 'NO_SEARCH_RESULT_FOUND' | translate}}</div>
                        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                            NO_PERMISSION_MESSAGE
                        </div>
                    </div>
                </td>
            </tr>

            <tr ng-repeat="login in allUsersVm.logins.content | orderBy:  ['(isSuperUser!=true)','(isActive!=true)','(external==true)', 'loginName']"
                ng-click="allUsersVm.showUserDetails(login)">
                <td>
                    <span class="label label-primary" ng-if="login.external">EXTERNAL</span>
                    <span class="label label-warning" ng-if="login.isSuperUser && login.isActive">ADMIN</span>
                    <span class="label label-danger" ng-if="!login.isActive">INACTIVE</span>
                </td>
                <td class="col-width-250">
                    <span ng-bind-html="login.person.fullName | highlightText: allUsersVm.filters.searchQuery"></span>
                </td>
                <td>
                    <span ng-bind-html="login.loginName  | highlightText: allUsersVm.filters.searchQuery"></span>
                </td>
                <td>{{login.person.defaultGroupObject.name}}</td>
                <td>{{login.person.phoneMobile}}</td>
                <td>{{login.person.email}}</td>
            </tr>
            </tbody>
        </table>
    </div>
    <%--<div class="table-footer" style="position: fixed;bottom: 30px !important;background-color: #f9fbfe">--%>
    <%--<table-footer objects="allUsersVm.logins" pageable="allUsersVm.pageable"--%>
    <%--previous-page="allUsersVm.previousPage"--%>
    <%--next-page="allUsersVm.nextPage" page-size="allUsersVm.pageSize"></table-footer>--%>
    <%--</div>--%>
</div>
