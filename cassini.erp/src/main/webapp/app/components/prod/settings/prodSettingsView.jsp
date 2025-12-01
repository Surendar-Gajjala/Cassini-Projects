<div class="row settings-panel">
    <div class="col-md-12 bhoechie-tab-container" v-tabs>
        <div>
            <div class="bhoechie-tab-menu">
                <div class="list-group">
                    <a href="#" class="list-group-item active">
                        <h5>Material Type</h5>
                    </a>

                    <a href="#" class="list-group-item">
                        <h5>Product Type</h5>
                    </a>
                    <a href="#" class="list-group-item">
                        <h5>Work Shift</h5>
                    </a>
                  </div>
            </div>
            <div class="bhoechie-tab" style="">
                <div class="bhoechie-tab-content active">
                    <div class="row">
                        <div class="col-sm-12">
                            <div class="row">
                                <div class="col-md-6"><h3>Material Type</h3></div>
                                <div class="col-md-6 text-right mrtop"><button class="btn btn-sm btn-primary" data-ng-click="addMaterialType($event,settingTypes.material);">New Type</button></div>
                            </div>
                            <table class="table table-striped">
                                <thead>
                                <tr>
                                    <th>Actions</th>
                                    <th>Name</th>
                                    <th>Description</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr data-ng-repeat="materialType in materialTypes">
                                    <td>
                                        <div class="btn-group" dropdown ng-hide="materialType.editMode">
                                            <button type="button" class="btn btn-sm btn-default dropdown-toggle actionbtn" dropdown-toggle>
                                                <i class="fa fa-cog fa-fw"></i></span>
                                            </button>
                                            <ul class="dropdown-menu" role="menu">
                                                <li><a href="" ng-click="showEditMode(materialType)">Edit</a></li>
                                                <li><a href="" ng-click="removeItem($index,materialType,settingTypes.material);">Delete</a></li>
                                            </ul>
                                        </div>
                                        <div class="btn-group" ng-show="materialType.editMode">
                                            <button type="button" class="btn btn-sm btn-success" ng-click="acceptChanges(materialType,settingTypes.material)"><i
                                                    class="fa fa-check"></i></button>
                                            <button type="button" class="btn btn-sm btn-default" ng-click="hideEditMode(materialType,settingTypes.material);"><i
                                                    class="fa fa-times"></i></button>
                                        </div>
                                    </td>
                                    <td>
                                        <span ng-hide="materialType.editMode">{{materialType.name}}</span>
                                        <input ng-show="materialType.editMode" placeholder="Enter name" class="form-control" type="text" data-ng-model="materialType.name">
                                    </td>
                                    <td>
                                        <span ng-hide="materialType.editMode">{{materialType.description}}</span>
                                        <input ng-show="materialType.editMode" placeholder="Enter description" class="form-control" type="text" data-ng-model="materialType.description">
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>

                </div>
                <div class="bhoechie-tab-content">
                    <div class="row">
                        <div class="col-sm-12">
                            <div class="row">
                                <div class="col-md-6"><h3>Product Type</h3></div>

                                <div class="col-md-6 text-right mrtop"><button class="btn btn-sm btn-primary" data-ng-click="addMaterialType($event,settingTypes.product);">New Type</button></div>


                            </div>
                            <table class="table table-striped">
                                <thead>
                                <tr>
                                    <th>Actions</th>
                                    <th>Name</th>
                                    <th>Description</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr data-ng-repeat="productType in productTypes">
                                    <td>
                                        <div class="btn-group" dropdown ng-hide="productType.editMode">
                                            <button type="button" class="btn btn-sm btn-default dropdown-toggle actionbtn" dropdown-toggle>
                                                <i class="fa fa-cog fa-fw"></i></span>
                                            </button>
                                            <ul class="dropdown-menu" role="menu">
                                                <li><a href="" ng-click="showEditMode(productType)">Edit</a></li>
                                                <li><a href="" ng-click="removeItem($index,productType,settingTypes.product);">Delete</a></li>
                                            </ul>
                                        </div>
                                        <div class="btn-group" ng-show="productType.editMode">
                                            <button type="button" class="btn btn-sm btn-success" ng-click="acceptChanges(productType,settingTypes.product)"><i
                                                    class="fa fa-check"></i></button>
                                            <button type="button" class="btn btn-sm btn-default" ng-click="hideEditMode(productType,settingTypes.product);"><i
                                                    class="fa fa-times"></i></button>
                                        </div>
                                    </td>
                                    <td>
                                        <span ng-hide="productType.editMode">{{productType.name}}</span>
                                        <input ng-show="productType.editMode" placeholder="Enter name" class="form-control" type="text" data-ng-model="productType.name">
                                    </td>
                                    <td>
                                        <span ng-hide="productType.editMode">{{productType.description}}</span>
                                        <input ng-show="productType.editMode" placeholder="Enter description" class="form-control" type="text" data-ng-model="productType.description">
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="bhoechie-tab-content">
                    <div class="row">
                        <div class="col-sm-12">
                            <div class="row">
                                <div class="col-md-6"><h3>Work Shift</h3></div>

                                <div class="col-md-6 text-right mrtop"><button class="btn btn-sm btn-primary" data-ng-click="addMaterialType($event,settingTypes.workShift);">New Shift</button></div>


                            </div>
                            <table class="table table-striped">
                                <thead>
                                <tr>
                                    <th>Actions</th>
                                    <th class="col-md-3">Name</th>
                                    <th class="col-md-3">Start Time</th>
                                    <th class="col-md-3">End Time</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr data-ng-repeat="workShift in workShifts">
                                    <td>
                                        <div class="btn-group" dropdown ng-hide="workShift.editMode">
                                            <button type="button" class="btn btn-sm btn-default dropdown-toggle actionbtn" dropdown-toggle>
                                                <i class="fa fa-cog fa-fw"></i></span>
                                            </button>
                                            <ul class="dropdown-menu" role="menu">
                                                <li><a href="" ng-click="showEditMode(workShift)">Edit</a></li>
                                                <li><a href="" ng-click="removeItem($index,workShift,settingTypes.workShift);">Delete</a></li>
                                            </ul>
                                        </div>
                                        <div class="btn-group" ng-show="workShift.editMode">
                                            <button type="button" class="btn btn-sm btn-success" ng-click="acceptChanges(workShift,settingTypes.workShift)"><i
                                                    class="fa fa-check"></i></button>
                                            <button type="button" class="btn btn-sm btn-default" ng-click="hideEditMode(workShift,settingTypes.workShift);"><i
                                                    class="fa fa-times"></i></button>
                                        </div>
                                    </td>
                                    <td>
                                        <span ng-hide="workShift.editMode">{{workShift.name}}</span>
                                        <input ng-show="workShift.editMode" placeholder="Enter name" class="form-control" type="text" data-ng-model="workShift.name">
                                    </td>
                                    <td>
                                        <span ng-hide="workShift.editMode"><span ng-bind="timeFormatShift(workShift.startTime)"></span></span>
                                        <div   ng-show="workShift.editMode" class="input-group">
                                            <div class="bootstrap-timepicker timepicker">
                                                <input id="shiftStartTime" type="text" class="form-control" ng-model="workShift.startTime">
                                            </div>
                                            <span class="input-group-addon"><i class="glyphicon glyphicon-time"></i></span>
                                        </div>


                                    </td>
                                    <td>
                                        <span ng-hide="workShift.editMode"><span ng-bind="timeFormatShift(workShift.endTime)"></span></span>
                                        <div  ng-show="workShift.editMode" class="input-group">
                                            <div class="bootstrap-timepicker timepicker">
                                                <input id="endStartTime" type="text" class="form-control" ng-model="workShift.endTime">
                                            </div>
                                            <span class="input-group-addon"><i class="glyphicon glyphicon-time"></i></span>
                                        </div>

                                    </td>

                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
               </div>
        </div>
    </div>
</div>

