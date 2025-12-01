<div class="modal-header">
    <h3>Select item</h3>
</div>
<div class="modal-body" style="padding: 20px; max-height: 500px;">

    <div class="row">

        <div class="col-md-3">
            <div class="panel panel-default" style="margin-bottom: 10px;">
                <h4 class="panel-heading text-center">Filters</h4>
            </div>
            <form>
                <div class="form-group"><input type="text" placeholder="Type" class="form-control"></div>
                <div class="form-group"><input type="text" placeholder="Number" class="form-control"></div>
                <div class="form-group"><input type="text" placeholder="Revision" class="form-control"></div>
                <div class="form-group"><input type="text" placeholder="Staus" class="form-control"></div>
                <button class="btn btn-success btn-sm pull-right">Search</button>
            </form>
        </div>
        <div class="col-md-9">
            <div class="row">
                <div class="panel panel-default" style="margin-bottom: 10px;">
                    <h4 class="panel-heading text-center">Results</h4>
                </div>
                <div class="col-md-12" style="padding:0px;max-height: 300px;overflow: auto;">
                    <table class="table table-striped">
                        <tbody>
                        <tr>
                            <th style="width: 80px; text-align: center">
                                <input name="" type="checkbox" value="">
                            </th>

                            <td style="vertical-align: middle;">
                                Type
                            </td>

                            <td style="vertical-align: middle;">
                                Number
                            </td>

                            <td style="vertical-align: middle;">
                                Revision
                            </td>
                            <td style="vertical-align: middle;">
                                Status
                            </td>
                        </tr>
                        <tr>
                            <th style="width: 80px; text-align: center">
                                <input name="" type="checkbox" value="">
                            </th>

                            <td style="vertical-align: middle;">
                                Type
                            </td>

                            <td style="vertical-align: middle;">
                                Number
                            </td>

                            <td style="vertical-align: middle;">
                                Revision
                            </td>
                            <td style="vertical-align: middle;">
                                Status
                            </td>
                        </tr>
                        <tr>
                            <th style="width: 80px; text-align: center">
                                <input name="" type="checkbox" value="">
                            </th>

                            <td style="vertical-align: middle;">
                                Type
                            </td>

                            <td style="vertical-align: middle;">
                                Number
                            </td>

                            <td style="vertical-align: middle;">
                                Revision
                            </td>
                            <td style="vertical-align: middle;">
                                Status
                            </td>
                        </tr>
                        <tr>
                            <th style="width: 80px; text-align: center">
                                <input name="" type="checkbox" value="">
                            </th>

                            <td style="vertical-align: middle;">
                                Type
                            </td>

                            <td style="vertical-align: middle;">
                                Number
                            </td>

                            <td style="vertical-align: middle;">
                                Revision
                            </td>
                            <td style="vertical-align: middle;">
                                Status
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>


        </div>
    </div>
</div>
<div class="modal-footer">
    <div class="row">
        <div class="col-md-6">
        </div>
        <div class="modal-buttons" class="col-md-6">
            <button type="button" class="btn btn-sm btn-default"
                    ng-click="refItemsSelectionVm.cancel()">Cancel
            </button>
            <button type="button" class="btn btn-sm btn-success"
                    ng-click="vm.ok()">Select
            </button>
        </div>
    </div>
</div>
</div>


<%--
<div class="modal-header">
    <h3>Select item</h3>
</div>
<div class="modal-body" style="padding: 20px; max-height: 500px;">
    <div class="row">
        <div class="col-md-3">
            <div class="panel panel-default" style="margin-bottom: 10px;">
                <h4 class="panel-heading text-center">Filters</h4>
            </div>
            <form>

                <div class="form-group"><input type="text" placeholder="Type" class="form-control"></div>
                <div class="form-group"><input type="text" placeholder="Number" class="form-control"></div>
                <div class="form-group"><input type="text" placeholder="Revision" class="form-control"></div>
                <div class="form-group"><input type="text" placeholder="Staus" class="form-control"></div>
                <button class="btn btn-success btn-sm pull-right">search</button>
            </form>
        </div>

        <div class="col-md-9">
            <div class="row">
                <div class="panel panel-default" style="margin-bottom: 10px;">
                    <h4 class="panel-heading text-center">Results</h4>
                </div>
                <div class="col-md-12" style="padding:0px;max-height: 300px;overflow: auto;">
                    <table class="table table-striped">

                        <tbody>
                        <tr>
                            <th style="width: 80px; text-align: center">
                                <input name="" type="" value="">

                            </th>

                            <td style="vertical-align: middle;">
                                Type
                            </td>

                            <td style="vertical-align: middle;">
                                Number
                            </td>

                            <td style="vertical-align: middle;">
                                Revision
                            </td>
                            <td style="vertical-align: middle;">
                                Status
                            </td>
                        </tr>
                        <tr>
                            <th style="width: 80px; text-align: center">
                                <input name="" type="checkbox" value="">

                            </th>

                            <td style="vertical-align: middle;">
                                Type
                            </td>

                            <td style="vertical-align: middle;">
                                Number
                            </td>

                            <td style="vertical-align: middle;">
                                Revision
                            </td>
                            <td style="vertical-align: middle;">
                                Status
                            </td>
                        </tr>
                        <tr>
                            <th style="width: 80px; text-align: center">
                                <input name="" type="checkbox" value="">

                            </th>

                            <td style="vertical-align: middle;">
                                Type
                            </td>

                            <td style="vertical-align: middle;">
                                Number
                            </td>

                            <td style="vertical-align: middle;">
                                Revision
                            </td>
                            <td style="vertical-align: middle;">
                                Status
                            </td>
                        </tr>
                        <tr>
                            <th style="width: 80px; text-align: center">
                                <input name="" type="checkbox" value="">

                            </th>

                            <td style="vertical-align: middle;">
                                Type
                            </td>

                            <td style="vertical-align: middle;">
                                Number
                            </td>

                            <td style="vertical-align: middle;">
                                Revision
                            </td>
                            <td style="vertical-align: middle;">
                                Status
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            &lt;%&ndash;<form action="">
                <input type="checkbox" name="item1" value="item1" class="pull-right"><br>
                <input type="checkbox" name="item2" value="item2" class="pull-right"><br>
                <input type="checkbox" name="item3" value="item3" class="pull-right"><br>
                <input type="checkbox" name="item4" value="item4" class="pull-right"><br>
                <input type="checkbox" name="item4" value="item4" class="pull-right"><br>
                <input type="checkbox" name="item4" value="item4" class="pull-right">
            </form>&ndash;%&gt;
        </div>

    </div>
    <div class="modal-footer">
        <div class="row">
            <div class="col-md-6">

            </div>
            <div class="modal-buttons" class="col-md-6">
                <button type="button" class="btn btn-sm btn-default"
                        ng-click="itemsSelectionVm.cancel()">Cancel
                </button>
                <button type="button" class="btn btn-sm btn-success"
                        ng-click="vm.ok()">Select
                </button>
            </div>
        </div>
    </div>
</div>--%>
