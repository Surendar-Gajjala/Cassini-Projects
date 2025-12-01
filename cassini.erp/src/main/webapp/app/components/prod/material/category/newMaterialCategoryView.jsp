<div class="modal-header"
     style="background: #E4E7EA none repeat scroll 0% 0%; border-bottom: 1px solid #CCC;">
    <h3 style="margin-top:0px; margin-bottom:0px;">New Material Category</h3>
</div>
<br>

<div class="modal-body" style="max-height:500px; overflow: auto;">
    <div>
        <div class="row">
            <div class="col-xs-12 col-sm-6 col-sm-offset-3 col-md-6 col-md-offset-3">
                <form class="form-horizontal">
                    <div class="form-group" ng-show="errorMessage != ''">
                        <label class="col-sm-3 control-label"></label>

                        <div class="col-sm-8">
                            <span class="text-danger" style="font-size:18px;">{{errorMessage}}</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">Name<span
                                class="asterisk">*</span>: </label>

                        <div class="col-sm-8">
                            <input type="text" class="form-control" name="name" ng-model="newMaterialCat.name">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label">Description: </label>

                        <div class="col-sm-8">

                            <textarea class="form-control" rows="2" ng-model="newMaterialCat.description"></textarea>

                        </div>
                    </div>


                    <div class="form-group">
                        <label class="col-sm-3 control-label">Material Type<span
                                class="asterisk">*</span>: </label>

                        <div class="col-sm-8">
                            <ui-select ng-model="newMaterialCat.type" theme="bootstrap" style="width:100%">
                                <ui-select-match placeholder="Select type">{{$select.selected.name}}</ui-select-match>
                                <ui-select-choices repeat="type in materialTypes | filter: $select.search">
                                    <div ng-bind-html="type.name | highlight: $select.search"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">Parent Category<span
                                class="asterisk">*</span>:</label>

                        <div class="col-sm-8">
                            <div class="input-group">
                                <input class="form-control" type="text" placeholder="Select category..." disabled
                                       ng-model="newMaterialCat.category.name" ng-enter="performTextSearch()">

                                <div class="input-group-btn" dropdown>
                                    <button dropdown-toggle class="btn btn-success dropdown-toggle" type="button">
                                        Categories <span class="caret" style="margin-left: 5px;"></span>
                                    </button>
                                    <div class="dropdown-menu" role="menu" id="materialCategoriesDropdown">
                                        <div id="materialCategoriesTree" class="easyui-tree"
                                             style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 200px;height: 200px;">
                                            categories
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<div class="modal-footer" style="background-color: #F9F9F9;text-align: left;">
    <div class="row">
        <div class="col-md-8" style="text-align: right">
            <button type="button" class="btn btn-default" style="min-width: 80px"
                    ng-click="cancel()">Cancel
            </button>
            <button class="btn btn-primary" style="min-width: 80px"
                    ng-click="ok()">Create
            </button>
        </div>
    </div>
</div>
<br>