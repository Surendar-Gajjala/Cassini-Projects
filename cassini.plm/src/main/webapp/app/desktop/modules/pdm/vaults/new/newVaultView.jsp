<div>
    <style scoped>

        .sidepanel-body {
            padding: 30px;
        }

    </style>

    <div class="sidepanel-body">
        <form class="form-horizontal">
            <div class="form-group">
                <label class="col-sm-4 control-label">
                    <span translate>NAME</span>
                    <span class="asterisk">*</span> :
                </label>

                <div class="col-sm-8">
                    <input type="text" class="form-control" name="title" placeholder="{{'ENTER_VAULT_NAME' | translate}}"
                           ng-model="newVaultVm.newVault.name" autofocus>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-4 control-label">
                    <span translate>DESCRIPTION</span>
                    <span class="asterisk">*</span>:
                </label>

                <div class="col-sm-8">
                    <textarea class="form-control" rows="3" style="resize: none" placeholder="{{'ENTER_DESCRIPTION_TITLE' | translate}}"
                              ng-model="newVaultVm.newVault.description"></textarea>
                </div>
            </div>
        </form>
    </div>
</div>