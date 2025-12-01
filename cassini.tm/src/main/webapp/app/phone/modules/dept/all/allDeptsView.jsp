<div class="view-container md-whiteframe-z5" style="padding: 10px;">
    <style scoped>
        .md-block {
            margin: 0 !important;
        }
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
        .person:last-child {
            border-bottom: 0;
        }
        .person-label {
            color: dodgerblue;
        }
        .margin-bottom {
            margin-bottom: 10px;
        }

        .persons.layout-padding {
            padding: 0 !important;
        }

        .persons > .layout-column {
            padding: 0 !important;
        }
    </style>

    <div class="md-whiteframe-2dp" layout="column" ng-if="allDeptsVm.departments.length == 0">
        <div style="padding: 10px; background-color: #fff">
            No departments
        </div>
    </div>

    <div class="md-whiteframe-2dp" layout="column" ng-repeat="dept in allDeptsVm.departments" style="margin-bottom: 10px;">
        <div style="line-height: 20px;background-color: #F57F17" ng-click="allDeptsVm.togglePersons(dept)">
            <div layout="row">
                <div flex style="padding-left: 10px">
                    <h2 class="md-flex" style="display: inline-block; font-size: 20px;text-align: center; color: #fff">{{dept.name}}</h2>
                </div>
                <div class="text-right" style="padding-right: 10px; padding-top: 15px;">
                    <ng-md-icon icon="expand_more" ng-if="dept.showPersons == false" style="fill: #fff"></ng-md-icon>
                    <ng-md-icon icon="expand_less" ng-if="dept.showPersons == true" style="fill: #fff"></ng-md-icon>
                </div>
            </div>
        </div>
        <div>
            <md-content layout-padding class="persons" ng-show="dept.showPersons">
                <div layout="column">
                    <div flex ng-if="dept.persons.length == 0" style="padding: 10px;">
                        No persons
                    </div>
                    <div class="person" ng-repeat="person in dept.persons" ng-click="$parent.goToState('app.person.details', {personId: person.id})">
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
                                <div style="font-size: 15px;">{{person.phoneMobile}}</div>
                            </div>
                            <div class="text-right" style="margin-left: 10px;margin-top: 5px;">
                                <ng-md-icon icon="chevron_right" style="fill: #afabab;"></ng-md-icon>
                            </div>
                        </div>
                    </div>
                </div>
            </md-content>
        </div>
    </div>
</div>