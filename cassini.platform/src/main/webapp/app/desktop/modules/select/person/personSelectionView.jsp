<div>
    <style scoped>
        .selector #freeTextSearchDirective {
            left: 10px !important;
            top: 36px !important;
        }

        .selector .search-box > input {
            width: 265px !important;
            border-radius: 15px !important;
            padding-right: 30px;
            height: 30px;
            display: inline-block !important;
            background-color: rgb(241, 243, 244);
            border: 1px solid #ddd;
            box-shadow: none;
        }

        .selector .search-box i.fa-search {
            z-index: 4 !important;
            position: absolute;
            margin-top: 28px !important;
            margin-left: 10px;
            color: grey;
            opacity: 0.5;
        }

        .selector .search-box i.clear-search {
            margin-left: -20px;
            color: gray;
            cursor: pointer;
            z-index: 4 !important;
            position: absolute;
            margin-top: 28px !important;
        }

        .selector .input-sm {
            font-size: 12px;
            padding: 7px 10px;
            padding-left: 30px !important;
            height: auto;
            margin-top: 21px;
            right: 0;
        }

        a.disabled {
            cursor: not-allowed;
            color: lightgrey;
        }

    </style>
    <div class="view-container" isfitcontent>
        <div class="view-toolbar selector" style="top: 30px; background-color: white">
            <free-text-search on-clear="personSelectVm.resetPage"
                              on-search="personSelectVm.freeTextSearch"></free-text-search>
            <div class="pull-right text-center" style="padding: 10px;margin-top: -5px;">
                <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                        Displaying {{personSelectVm.logins.numberOfElements}} of
                                            {{personSelectVm.logins.numberOfElements}}
                                    </span>
                            </medium>
                        </span>
                    <span class="mr10">Page {{personSelectVm.logins.totalElements != 0 ? personSelectVm.logins.number+1:0}} of {{personSelectVm.logins.totalPages}}</span>
                    <a href="" ng-click="personSelectVm.previousPage()"
                       ng-class="{'disabled': personSelectVm.logins.first}"><i
                            class="fa fa-arrow-circle-left mr10"></i></a>
                    <a href="" ng-click="personSelectVm.nextPage()"
                       ng-class="{'disabled': personSelectVm.logins.last}"><i
                            class="fa fa-arrow-circle-right"></i></a>
                </div>
            </div>
        </div>
        <div class="view-content no-padding" style="overflow-y: auto;padding: 10px !important;">

            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width: 40px" translate>SELECT
                    </th>
                    <th style="width: 200px;" translate>FIRST_NAME</th>
                    <th style="width: 200px;" translate>LAST_NAME</th>
                    <th style="width: 200px;" translate>PHONE_NUMBER</th>
                    <th style="width: 200px;" translate>EMAIL</th>
                </tr>
                </thead>

                <tbody>
                <tr ng-if="personSelectVm.loading == true">
                    <td colspan="12">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                             class="mr5"><span translate>LOADING_PERSON</span>
                    </span>
                    </td>
                </tr>
                <tr ng-if="personSelectVm.loading == false && personSelectVm.logins.content.length == 0">
                    <td colspan="12" translate>NO_PERSONS</td>
                </tr>

                <tr ng-repeat="login in personSelectVm.logins.content | filter: search"
                    ng-click="login.checked = !login.checked; personSelectVm.radioChange(login, $event)"
                    ng-dblclick="login.checked = !login.checked; personSelectVm.selectRadioChange(login, $event)">
                    <td style="width: 40px;">
                        <input type="radio" ng-checked="login.checked" name="login" value="login"
                               ng-click="personSelectVm.radioChange(login, $event)"
                               ng-dblclick="personSelectVm.selectRadioChange(login, $event)">
                    </td>
                    <td style="width: 200px;">{{login.firstName}}</td>
                    <td style="width: 200px;">{{login.lastName}}</td>
                    <td style="width: 200px;">{{login.phoneMobile}}</td>
                    <td style="width: 200px;">{{login.email}}</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

