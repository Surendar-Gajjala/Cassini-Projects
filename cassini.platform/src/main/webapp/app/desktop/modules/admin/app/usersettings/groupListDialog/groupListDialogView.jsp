<div style="padding: 10px; height: auto;">
  <div class="row">
    <div class="col-md-12" style="padding:0px; height: auto;overflow: auto;">
      <table class="table table striped">
        <thead>
        <tr>
          <th></th>
          <th translate>NAME</th>
          <th translate>DESCRIPTION</th>
        </tr>
        </thead>
        <tr ng-if="groupDialogVm.loading == true">
          <td colspan="6">
                <span style="font-size: 15px;">
                    <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif" class="mr5">
                    <span translate>LOADING_GROUPS</span>
                </span>
          </td>
        </tr>

        <tr ng-if="groupDialogVm.loading == false && groupDialogVm.groupList.content.length == 0">
          <td colspan="6"><span translate>NO_GROUPS</span></td>
        </tr>

        <tr ng-repeat="group in groupDialogVm.groupList">
          <td style="width:50px;"><input type="checkbox" class="form-control"
                                         ng-model="group.isSelected"></td>
          <td style="vertical-align: middle;">{{group.name}}</td>
          <td style="vertical-align: middle;">{{group.description}}</td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
  <br>
  <br>
</div>
