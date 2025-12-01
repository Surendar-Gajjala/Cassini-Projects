<div style="position: relative;">
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin: 0;">
            <div>
                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-3 control-label">
                            <span translate>Type</span>: </label>

                        <div class="col-sm-7" style="margin: 12px 0 0 0 !important;">

                            <div class="form-check" style="border: 1px solid #ddd;padding:8px;margin-top: -10px !important;border-radius: 3px;">
                                <label class="form-check-label" style="margin-right: 5px">
                                    <input class="form-check-input" type="radio" name="gridRadios" id="gridRadios2"
                                           ng-click="newSearchVm.selectType('Private', $event)" checked><span
                                        style="padding: 2px;margin-left: 5px;" translate>Private</span>
                                </label>
                                <label class="form-check-label" style="margin-right: 33px;margin-left: 11px">
                                    <input class="form-check-input" type="radio" name="gridRadios" id="gridRadios1"
                                           ng-click="newSearchVm.selectType('Public', $event)"><span
                                        style="padding: 2px;margin-left: 5px;" translate>Public</span>
                                </label>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label"><span translate>NAME</span><span class="asterisk">*</span>:
                        </label>

                        <div class="col-sm-7">
                            <div>
                                <input type="text" class="form-control" name="title"
                                       ng-model="newSearchVm.newSearch.name">

                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label"><span translate>DESCRIPTION</span>: </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      ng-model="newSearchVm.newSearch.description"></textarea>
                        </div>
                    </div>
                </form>
                <br><br>
            </div>
        </div>
    </div>
</div>