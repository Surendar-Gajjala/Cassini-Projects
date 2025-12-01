<style>

  /* The Close Button */
  .img-model .closeimage {
    position: absolute;
    top: 50px;
    right: 50px;

    font-size: 40px;
    font-weight: bold;
    transition: 0.3s;
  }

  .img-model .closeimage:hover,
  .img-model .closeimage:focus {
    color: #bbb;
    text-decoration: none;
    cursor: pointer;
  }

  .browse-control {
    -moz-border-radius: 3px;
    -webkit-border-radius: 3px;
    border-radius: 3px;
    padding: 5px;
    height: auto;
    -moz-box-shadow: none;
    -webkit-box-shadow: none;
    box-shadow: none;
    font-size: 13px;
    border: 1px solid #ccc;
  }
</style>

<div ng-if="machineBasicVm.loading == true" style="padding: 30px;">
  <br/>
    <span style="font-size: 15px;">
       <%-- <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">Loading Machine details...--%>
    </span>
  <br/>
</div>

<div class="item-details" style="padding: 30px" ng-if="machineBasicVm.loading == false">
  <div class="row">
    <div class="label col-xs-4 col-sm-3 text-right">
      <span translate>MACHINE_NUMBER</span> :
    </div>
    <div class="value col-xs-8 col-sm-9">
      <span>{{machineBasicVm.machine.number}}</span>
    </div>
  </div>
  <div class="row">
    <div class="label col-xs-4 col-sm-3 text-right">
      <span translate>DESCRIPTION</span> :
    </div>
    <div class="value col-xs-8 col-sm-9">

      <a ng-if="hasPermission('machine','edit') && !machine.isReleased && machine.statusType != 'REJECTED'" href=""
         onaftersave="machineBasicVm.updateMachine()"
         editable-textarea="machineBasicVm.machine.description"><span ng-bind-html="(machineBasicVm.machine.description ) || 'ADD_DESCRIPTION' |
                translate" title="{{machineBasicVm.machine.description}}"></span> </a>
      <span ng-if="!hasPermission('machine','edit') || machine.isReleased || machine.statusType == 'REJECTED'">{{machineBasicVm.machine.description}}</span>
    </div>
  </div>



  <%--<basic-attribute-details-view object-type="CHANGE"
                                quality-type="CHANGE"
                                has-permission="hasPermission('change','dco','edit') && !dco.isReleased && dco.statusType != 'REJECTED'"
                                object-id="dco.id"></basic-attribute-details-view>--%>

</div>
