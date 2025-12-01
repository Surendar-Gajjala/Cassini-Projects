<div class="modal-header"
     style="background: #E4E7EA none repeat scroll 0% 0%; border-bottom: 1px solid #CCC;">
  <h3 style="margin-top:0px; margin-bottom:0px;">New Product Category</h3>
</div>
<br>
<div class="modal-body" style="max-height:500px; overflow: auto;">
  <div>
    <div class="row">
      <div class="col-xs-12 col-sm-6 col-sm-offset-3 col-md-6 col-md-offset-3">
        <form class="form-horizontal">

          <div class="form-group">
            <label class="col-sm-3 control-label">Name: </label>
            <div class="col-sm-8">
              <input type="text" class="form-control" name="name" ng-model="newProductCat.name">
            </div>
          </div>
          
          <div class="form-group">
            <label class="col-sm-3 control-label">Code: </label>
            <div class="col-sm-8">
              <input type="text" class="form-control" name="name" ng-model="newProductCat.code">
            </div>
          </div>

          <div class="form-group">
            <label class="col-sm-3 control-label">Description: </label>
            <div class="col-sm-8">

              <textarea class="form-control" rows="2"   ng-model="newProductCat.description"></textarea>

            </div>
          </div>


          <div class="form-group">
            <label class="col-sm-3 control-label">Product Type: </label>
            <div class="col-sm-8">
              <ui-select ng-model="newProductCat.type" theme="bootstrap" style="width:100%">
                <ui-select-match placeholder="Select type">{{$select.selected.name}}</ui-select-match>
                <ui-select-choices repeat="type in productTypes | filter: $select.search">
                  <div ng-bind-html="type.name | highlight: $select.search"></div>
                </ui-select-choices>
              </ui-select>
            </div>
          </div>
          
          <div class="form-group">
                    <label class="col-sm-3 control-label">Business Unit: </label>
                    <div class="col-sm-8">
                        <ui-select ng-model="newProductCat.businessUnit" theme="bootstrap" style="width:100%">
                            <ui-select-match placeholder="Select business unit">{{$select.selected.name}}</ui-select-match>
                            <ui-select-choices repeat="bu in businessUnits | filter: $select.search">
                                <div ng-bind-html="bu.name | highlight: $select.search"></div>
                            </ui-select-choices>
                        </ui-select>
                    </div>
                </div>



          <div class="form-group">
            <label class="col-sm-3 control-label">Parent Category: </label>
            <div class="col-sm-8">
              <div class="input-group">
                <input class="form-control" type="text" placeholder="Select category..." disabled
                       ng-model="newProductCat.category.name" ng-enter="performTextSearch()">

                <div class="input-group-btn" dropdown>
                  <button dropdown-toggle class="btn btn-success dropdown-toggle" type="button">
                    Categories <span class="caret" style="margin-left: 5px;"></span>
                  </button>
                  <div class="dropdown-menu" role="menu" id="productCategoriesDropdown">
                    <div id="productCategoryTree" class="easyui-tree"
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
              ng-click="cancel()">Cancel</button>
      <button class="btn btn-primary" style="min-width: 80px"
              ng-click="ok()" >Create</button>
    </div>
  </div>
</div>
<br>