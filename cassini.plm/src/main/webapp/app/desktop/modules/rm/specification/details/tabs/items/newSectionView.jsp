<div style="position: relative;">
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin: 0;">
            <div>
                <br>

                <form class="form-horizontal">
                    <div class=form-group>
                        <label class="col-sm-4 control-label">
                            <span translate>NAME</span>
                            <span class="asterisk">*</span>:
                        </label>

                        <div class="col-sm-7">
                            <input type="text" class="form-control" name="title" placeholder="{{'ENTER_SECTION_NAME' | translate}}"
                                   ng-model="newSecVm.newSection.name" autofocus>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>DESCRIPTION</span><span class="asterisk">*</span>:
                        </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="6" style="resize: none" placeholder="{{'ENTER_DESCRIPTION_TITLE' | translate}}"
                                      ng-model="newSecVm.newSection.description"></textarea>
                        </div>
                    </div>
                </form>

            </div>
            <br><br>
        </div>
    </div>
</div>
