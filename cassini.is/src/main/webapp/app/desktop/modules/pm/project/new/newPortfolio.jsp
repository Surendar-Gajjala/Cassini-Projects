<div style="position: relative;">
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin-right: 48px;">
            <div>
                <br>

                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">Name :<span class="asterisk">*</span></label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="name"
                                   ng-model="newPortfolioVm.portfolio.name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">Description :</label>

                        <div class="col-sm-7">
                <textarea name="description" rows="5" class="form-control" style="resize: none"
                          ng-model="newPortfolioVm.portfolio.description"></textarea>
                        </div>
                    </div>

                </form>
                <br>
            </div>
        </div>
    </div>
</div>

