<li ng-if="mainVm.assignedTasks.length > 0 || hasRole('Staff') == true || isAdmin() == true|| hasRole('Administrator') == true " ui-sref='app.task.all({ mode: "ASSIGNED" })'>
    <button title="Assigned Tasks" class="btn btn-default dropdown-toggle tp-icon" dropdown-toggle>
      <i class="fa fa-list"></i>
      <span class="badge assign-tasks-badge">{{mainVm.assignedTasks.length}}</span>
    </button>
</li>

<li ng-if="mainVm.approvedTasks.length > 0 || hasRole('Officer') == true || isAdmin() == true|| hasRole('Administrator') == true " ui-sref='app.task.all({ mode: "APPROVED" })'>
  <button title="Approved Tasks" class="btn btn-default dropdown-toggle tp-icon" dropdown-toggle>
    <i class="fa fa-list"></i>
    <span class="badge pending-tasks-badge">{{mainVm.approvedTasks.length}}</span>
  </button>
</li>
