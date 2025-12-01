<div class="view-container" style="overflow: hidden;">
    <div layout="column">
        <div layout="row"
             style="background-color: #FFF;padding: 10px 0 10px 10px;">
            <form flex id="search" autocomplete="off" ng-submit="search()" style="width: 100%;">
                <md-input-container class="md-icon-float" style="width: 96%; margin: 8px 8px 3px 0;">
                    <input placeholder="Search..." ng-model="searchText">
                </md-input-container>
                <md-button ng-click="search()"
                           style="min-width: 30px;margin: 8px 0 0 0;position: absolute;
                                right: 5px;top: 15px;line-height: 0 !important;min-height: 8px;">
                    <ng-md-icon icon="search" style="fill: grey"></ng-md-icon>
                </md-button>
                <label ng-if="pagedResults.totalElements > 0"
                       style="font-size: 14px; color: #B3B3B3;position: absolute;right: 40px;top: 28px">
                    {{pagedResults.totalElements}} results</label>
            </form>
        </div>
        <div style="padding: 10px;position: fixed;top: 121px;bottom: 0;left: 0;right: 0;overflow-x: hidden;
                overflow-y: auto;border-top: 5px solid #eee;" infinite-scroll load-more="search()">
            <div style="width: 100px; line-height: 50px; margin: auto" ng-if="noResults == true">
                <h3 style="color: lightgrey;">No results</h3>
            </div>

            <div ng-if="noResults == false" layout="row" ng-repeat="result in results" class="list-item md-whiteframe-z3"
                 style="background-color: #fff;padding: 10px; margin-bottom: 10px; border-radius: 3px;">
                <div flex layout="column">
                    <div style="font-size: 18px">
                        <a href="" ui-sref="app.crm.customer({customerId: result.id})" style="text-decoration: none;color: dodgerblue;">{{result.name}}</a>
                    </div>
                    <div layout="row" style="margin-top: 10px;">
                        <div flex style="text-align: left">
                            <div style="font-size: 12px; color: gray">
                                Region
                            </div>
                            <div style="font-size: 15px;">
                                {{result.salesRegion.name}}
                            </div>
                        </div>
                        <div flex style="text-align: center">
                            <div style="font-size: 12px; color: gray">
                                State
                            </div>
                            <div style="font-size: 15px;">
                                {{result.salesRegion.state.name}}
                            </div>
                        </div>
                        <div flex style="text-align: right">
                            <div style="font-size: 12px; color: gray">
                                Sales Rep
                            </div>
                            <div style="font-size: 15px;">
                                {{result.salesRep.firstName}}
                            </div>
                        </div>
                    </div>
                    <div layout="row" style="margin-top: 10px;">
                        <div flex style="text-align: left">
                            <div style="font-size: 12px; color: gray">
                                Contact
                            </div>
                            <div style="font-size: 15px;">
                                {{result.contactPerson.firstName}}
                            </div>
                        </div>
                        <div flex style="text-align: right">
                            <div style="font-size: 12px; color: gray">
                                Phone Number
                            </div>
                            <div class="phone-number" style="font-size: 15px;">
                                <!--<span ng-if="$parent.isPhoneAvailable() == false">{{result.contactPerson.phoneMobile}}</span>-->
                                <md-menu style="padding: 0;">
                                    <a href="" ng-click="$mdOpenMenu()">
                                        {{result.contactPerson.phoneMobile}}
                                    </a>
                                    <md-menu-content width="4">
                                        <md-menu-item>
                                            <md-button ng-click="$parent.callPhoneNumber(result.contactPerson.phoneMobile)">
                                                <ng-md-icon style="fill:gray" icon="phone"></ng-md-icon>
                                                Make a call
                                            </md-button>
                                        </md-menu-item>
                                        <md-menu-item>
                                            <md-button ng-click="$parent.addToContacts(result.contactPerson)">
                                                <ng-md-icon style="fill:gray" icon="perm_contact_cal"></ng-md-icon>
                                                Add to contacts
                                            </md-button>
                                        </md-menu-item>
                                    </md-menu-content>
                                </md-menu>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>