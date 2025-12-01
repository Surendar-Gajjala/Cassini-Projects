<div style="position: relative;">
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px">
        <div class="row" style="margin: 0;">
            <div>
                <br>

                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">Name <span class="asterisk">*</span>: </label>

                        <div class="col-sm-8">
                            <input type="text" class="form-control" name="type" placeholder="Enter Site Name"
                                   ng-model="newSiteVm.site.name">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">Description : </label>

                        <div class="col-sm-8">
                        <textarea name="description" rows="5" class="form-control" style="resize: none"
                                  placeholder="Enter Site Description"
                                  ng-model="newSiteVm.site.description"></textarea>
                        </div>
                    </div>

                    <br>
                    <h4 ng-if="newSiteVm.newSiteAttributes.length  > 0 || newSiteVm.attributes.length > 0"
                        class="section-title" style="color: black;">Attributes
                    </h4>
                    <br>

                    <div>
                        <form class="form-horizontal">
                            <attributes-view show-objects="selectObjectValues"
                                             attributes="newSiteVm.requiredAttributes"></attributes-view>
                            <br>
                            <attributes-view show-objects="selectObjectValues"
                                             attributes="newSiteVm.attributes"></attributes-view>
                            <br>
                            <br>
                        </form>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

