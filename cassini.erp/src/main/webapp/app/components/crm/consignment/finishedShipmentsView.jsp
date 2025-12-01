<script type="text/ng-template" id="consignments-view-tb">
    <button class="btn btn-sm btn-default" ng-click="loadConsignments()"><i class="fa fa-refresh"></i></button>
    <button class="btn btn-sm btn-primary" ng-click="Back()">Home</button>
</script>

<div ng-show="mode == 'all'">
    <div class="row" style="margin-bottom: 20px">
        <div class="col-md-2" style="margin-top: 20px;">
        </div>
        <div class="col-md-10"  style="text-align: right" ng-show="consignments.numberOfElements > 0">
            <div style="text-align: right;">
                <pagination total-items="consignments.totalElements"
                            items-per-page="pageable.size"
                            max-size="5"
                            boundary-links="true"
                            ng-model="pageable.page"
                            ng-change="pageChanged()">
                </pagination>
            </div>

            <div style="margin-top: -25px;">
                <small>Total {{consignments.totalElements}} consignments</small>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-12 responsive-table">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th style="width:100px; text-align: center;">Actions</th>
                    <th>Number</th>
                    <th style="text-align: center">Invoice Number</th>
                    <th>PO Number</th>
                    <th style="width:150px;">Contents</th>
                    <th style="width:250px;">Consignee</th>
                    <th>Shipped To</th>
                    <th>Through</th>
                    <th>Shipping Cost</th>
                    <th>LR Number</th>
                    <th>Vehicle</th>
                    <th>Driver</th>
                    <th style="width:200px;">Shipper</th>
                    <th>Shipped Date</th>
                    <th>Paid By</th>
                    <th style="text-align: right">Value</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td style="width:100px; text-align: center;">
                        <div class="btn-group" style="margin-bottom: 0px;">
                            <button title="Apply Filters"  type="button" class="btn btn-sm btn-success" ng-click="applyFilters()">
                                <i class="fa fa-search"></i>
                            </button>
                            <button title="Clear Filters" type="button" class="btn btn-sm btn-default" ng-click="resetFilters()">
                                <i class="fa fa-times"></i>
                            </button>
                        </div>
                    </td>
                    <td style="width: 200px;">
                        <table-filter placeholder="Number" filters="filters"
                                      property="number" apply-filters="applyFilters()" />
                    </td>

                    <td style="width: 200px">
                        <table-filter placeholder="Invoice" filters="filters" align="center"
                                      property="invoiceNumber" apply-filters="applyFilters()" />
                    </td>
                    <td style="width: 200px;">
                        <table-filter placeholder="PO Number" filters="filters"
                                      property="poNumber" apply-filters="applyFilters()" />
                    </td>
                    <td style="width: 200px">
                        <table-filter placeholder="Contents" filters="filters" align="center"
                                      property="contents" apply-filters="applyFilters()" />
                    </td>
                    <td style="width:250px;">
                        <table-filter placeholder="Consignee" filters="filters"
                                      property="consignee" apply-filters="applyFilters()" />
                    </td>
                    <td style="width:250px;">
                        <table-filter placeholder="Shipped To" filters="filters"
                                      property="shippedTo" apply-filters="applyFilters()" />
                    </td>
                    <td style="width:250px;">
                        <table-filter placeholder="Through" filters="filters"
                                      property="through" apply-filters="applyFilters()" />
                    </td>
                    <td style="width:250px;">
                        <table-filter placeholder="Cost" filters="filters"
                                      property="shppingCost" apply-filters="applyFilters()" />
                    </td>
                    <td style="width: 200px; text-align: right">
                        <table-filter placeholder="LR Number" filters="filters" property="confirmationNumber" apply-filters="applyFilters()" />
                    </td>
                    <td style="width: 150px; text-align: right">
                        <table-filter placeholder="Vehicle" filters="filters" property="vehicle" apply-filters="applyFilters()" />
                    </td>
                    <td style="width: 150px; text-align: right">
                        <table-filter placeholder="Driver" filters="filters" property="driver" apply-filters="applyFilters()" />
                    </td>
                    <td style="width:250px;">
                        <table-filter placeholder="Shipper" filters="filters"
                                      property="shipper" apply-filters="applyFilters()" />
                    </td>

                    <td style="width: 300px; text-align: center">
                        <input placeholder="Date"
                               style="width: 100%; text-align: left"
                               options="dateRangeOptions"
                               date-range-picker
                               clearable="true"
                               class="form-control date-picker"
                               ng-enter="applyFilters()"
                               ng-class="{ hasFilter: (filters.shippedDate.startDate != null && filters.shippedDate.endDate != '') }"
                               type="text" ng-model="filters.shippedDate" />
                    </td>

                    <td style="width:250px;">
                        <ui-select ng-model="filters.paidBy" on-select="applyFilters()" theme="bootstrap" style="width:100%">
                            <ui-select-match allow-clear="true" placeholder="Type">{{$select.selected}}</ui-select-match>
                            <ui-select-choices repeat="paidBy in ['CUSTOMER','SELF'] | filter: $select.search">
                                <div ng-bind-html="paidBy | highlight: $select.search"></div>
                            </ui-select-choices>
                        </ui-select>
                    </td>

                    <td style="width: 150px; text-align: right">
                        <table-filter placeholder="Value" align="right" filters="filters" property="value" apply-filters="applyFilters()" />
                    </td>
                </tr>

                <tr ng-if="consignments.totalElements == 0 || loading == true">
                    <td colspan="15">
                        <span ng-hide="loading">There are no consignments</span>
                        <span ng-show="loading">
                            <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading consignments...
                        </span>
                    </td>
                </tr>

                <tr ng-repeat="consignment in consignments.content"
                    class="order-row">
                    <td class="col-center actions-col">
                        <div class="btn-group" dropdown style="margin-bottom: 0px;">
                            <button type="button" class="btn btn-sm btn-default dropdown-toggle" dropdown-toggle>
                                <i class="fa fa-cog fa-fw"></i>
                            </button>
                            <ul class="dropdown-menu" role="menu">
                                <li><a href="" ng-click="showConsignmentDetails(consignment)">Open Consignment</a></li>
                            </ul>
                        </div>
                    </td>
                    <td style="width: 200px">
                        <a href="" ng-click="showConsignmentDetails(consignment)">{{consignment.number}}</a>
                    </td>
                    <td style="width: 200px; text-align: center" ng-init="invoices = getInvoiceNumbers(consignment)">
                        <span ng-if="invoices.length == 0">N/A</span>

                        <div ng-if="invoices.length > 0" class="btn-group" dropdown style="margin-bottom: 0px;">
                                <span ng-if="invoices.length == 1">
                                    {{invoices[0]}}
                                </span>
                            <a href="" popover="{{consignment.invs}}"
                               popover-trigger="mouseenter"
                               ng-if="invoices.length > 1">
                                {{invoices.length}} invoice numbers
                            </a>
                        </div>
                    </td>
                    <td style="width: 200px; text-align: center">
                        <span ng-if="consignment.poNumbers.length == 0">N/A</span>

                        <div ng-if="consignment.poNumbers.length > 0" class="btn-group" dropdown
                             style="margin-bottom: 0px;">
                                <span ng-if="consignment.poNumbers.length == 1">
                                    {{consignment.poNumbers[0]}}
                                </span>

                            <a href="" popover="{{consignment.pos}}"
                               popover-trigger="mouseenter"
                               ng-if="consignment.poNumbers.length > 1">
                                {{consignment.poNumbers.length}} PO Numbers
                            </a>
                        </div>
                    </td>

                    <td style="text-align: left;width:150px">{{consignment.contents}}</td>
                    <td style="text-align: left;width:250px">{{consignment.consignee}}</td>

                    <td style="text-align: left;width:250px">
                        {{consignment.shippingAddress.city}}
                    </td>
                    <td style="text-align: left;width:250px">
                        {{consignment.through}}
                    </td>
                    <td style="text-align: center;width: 250px;">
                        <span href="#" ng-if="consignment.paidBy == 'SELF'">
                            {{consignment.shippingCost}}
                        </span>
                        <!--
                        <a href="#" ng-if="consignment.paidBy == 'SELF'" editable-text="consignment.shippingCost" title="Click to edit"
                           onaftersave="updateConsignment(consignment)">
                            {{consignment.shippingCost || 'Click to edit'}}
                        </a>
                        -->
                        <span ng-if="consignment.paidBy == 'CUSTOMER'">N/A</span>
                    </td>

                    <td style="text-align: center">
                        <!--
                        <span href="#"
                              ng-if="consignment.shipper.name != 'Self' &&
                                    consignment.vehicle != null &&
                                    consignment.driver != null">
                            {{consignment.confirmationNumber}}
                        </span>

                        <span ng-if="consignment.shipper.name == 'Self' && consignment.confirmationNumber != null">Bill Received</span>
                        -->
                        <a href="#" editable-text="consignment.confirmationNumber"
                           ng-if="consignment.shipper.name != 'Self' &&
                                    consignment.shipper.name != 'Default' &&
                                    consignment.vehicle != null &&
                                    consignment.driver != null"
                           onaftersave="updateConsignment(consignment)">
                            {{consignment.confirmationNumber || 'Click to enter'}}
                        </a>
                        <span ng-if="consignment.shipper.name == 'Self' || consignment.shipper.name == 'Default'">Bill Received</span>
                    </td>
                    <td style="text-align: center">
                        <!--
                        {{consignment.vehicle.number}}
                        -->
                        <a href="" class="editable editable-empty editable-click" ng-click="selectVehicle(consignment)"
                           ng-if="consignment.vehicle == null">Click to select</a>
                        <a href="" class="editable editable-click" ng-click="selectVehicle(consignment)">{{consignment.vehicle.number}}</a>
                    </td>
                    <td style="text-align: center">
                        <!--
                        {{consignment.driver.firstName}}
                        -->
                        <a href="" class="editable editable-empty editable-click" ng-if="consignment.driver == null"
                           ng-click="selectDriver(consignment)"
                           ng-if="consignment.driver == null">Click to select</a>
                        <a href="" class="editable editable-click" ng-click="selectDriver(consignment)">
                            {{consignment.driver.firstName}}</a>
                    </td>
                    <td style="text-align: left;width: 250px;">{{consignment.shipper.name}}</td>
                    <td style="width: 200px; text-align: left">{{consignment.shippedDate}}</td>

                    <td style="text-align: center;width: 250px;">{{consignment.paidBy}}</td>
                    <td style="width: 150px; text-align: right;">{{consignment.value | currency:"&#x20b9; ":0}}</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>