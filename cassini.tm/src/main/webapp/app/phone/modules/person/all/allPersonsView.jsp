<div class="view-container md-whiteframe-z5">
    <style scoped>
        .text-right {
            text-align: right;
        }
        .text-left {
            text-align: left;
        }
        .text-center {
            text-align: center;
        }
        .person {
            background-color:#FFFFFF;
            padding: 10px;
            border-bottom: 1px solid #ddd;
        }

        .person:first-child {
            border-top: 1px solid #ddd;
        }
        .search-box {
            margin: 5px;
            padding-left: 15px;
            background-color: #00bcd4;
            border-bottom: 1px solid #ddd;
            border-radius: 2px;
        }
        .search-box .md-block {
            margin: 0;
        }

        .search-box .md-input {
            border-color: #fff !important;
        }

        .search-box input {
            color: #fff !important;
        }

        .search-box button {
            margin-left: -10px !important;
        }
    </style>

    <div class="search-box">
        <div layout="row">
            <div flex>
                <md-input-container class="md-block">
                    <label>&nbsp;</label>
                    <input ng-enter="allPersonsVm.search()" ng-model="allPersonsVm.filters.searchQuery">
                </md-input-container>
            </div>
            <div>
                <md-button class="md-icon-button" ng-click="allPersonsVm.search()">
                    <ng-md-icon icon="search" style="fill: #fff;"></ng-md-icon>
                </md-button>
            </div>
        </div>
    </div>

    <div ng-if="allPersonsVm.loading == true"
         style="padding: 20px; background-color: #fff; border-top: 1px solid #ddd;border-bottom: 1px solid #ddd">
        <md-progress-linear md-mode="indeterminate"></md-progress-linear>
    </div>

    <div class="person" layout="column" ng-if="allPersonsVm.loading == false && (allPersonsVm.persons.length == 0 ||
                (allPersonsVm.persons.length == 1 &&allPersonsVm.persons[0].firstName == 'Administrator'))">
        <div layout="row">
            <div flex>
                No persons
            </div>
        </div>
    </div>

    <div class="person" layout="column" ng-repeat="person in allPersonsVm.persons"
         ng-if="person.firstName != 'Administrator'"
         ng-click="$parent.goToState('app.person.details', {personId: person.id})">
        <div layout="row">
            <div flex>
                <div style="font-size: 12px; color: gray">
                    Name
                </div>
                <div style="font-size: 15px;">{{person.firstName}} {{person.lastName}}</div>
            </div>
            <div flex class="text-right">
                <div style="font-size: 12px; color: gray">
                    Phone Number
                </div>
                <div style="font-size: 15px;">{{person.phoneMobile != null ? person.phoneMobile : 'N/A'}}</div>
            </div>
            <div class="text-right" style="margin-left: 10px;margin-top: 5px;">
                <ng-md-icon icon="chevron_right" style="fill: #afabab;"></ng-md-icon>
            </div>
        </div>
    </div>
</div>