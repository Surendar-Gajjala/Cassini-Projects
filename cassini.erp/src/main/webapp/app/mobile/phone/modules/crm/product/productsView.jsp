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
                <div>
                    <img style="height: 60px; width: 60px" ng-src="{{getPictureUrl(result)}}" alt="Item image">
                </div>
                <div flex layout="column" style="margin-left: 10px;">
                    <div style="font-size: 18px">
                        {{result.name}}
                    </div>
                    <div flex></div>
                    <div layout="row">
                        <div flex style="text-align: left" layout="row">
                            <div style="font-size: 12px; color: gray; margin-right: 8px;">
                                Category:
                            </div>
                            <div flex style="font-size: 15px; line-height: 15px;">
                                {{result.category.name}}
                            </div>
                        </div>
                    </div>
                    <div flex></div>
                    <div layout="row">
                        <div flex style="text-align: left" layout="row">
                            <div style="font-size: 12px; color: gray; margin-right: 8px;">
                                Unit Price:
                            </div>
                            <div flex style="font-size: 15px; line-height: 15px;">
                                {{result.unitPrice | currency:"&#x20b9; ":0}}
                            </div>
                        </div>
                        <div flex style="text-align: left" layout="row">
                            <div style="font-size: 12px; color: gray; margin-right: 8px;">
                                Inventory:
                            </div>
                            <div flex style="font-size: 15px; line-height: 15px;">
                                {{result.inventory.inventory | currency:"":0}}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>