<div id="project-headerbar" class="headerbar project-headerbar" project-header-bar>
    <div class="header-left" style="height: 50px;">
        <div class="topnav">
            <ul class="nav nav-horizontal">
                <li ng-repeat="item in projNavVm.navItems">

                    <a href="" ng-click="projNavVm.activateProjectTab(item.id)"
                       class="topmenu-item-link" ng-class="{'active': item.active == true}">
                        <span>{{item.label}}
                            <span title="assignedTasks" class="badge badge-success"
                                  ng-if='item.label == "Tasks" && assignedTasks.length > 0'>
                                <span ng-click="projNavVm.assignedClick();">{{assignedTasks.length}}</span>
                            </span>
                            <span title="inProgressTasks" class="badge badge-warning"
                                  ng-if='item.label == "Tasks" && inprogressTasks.length > 0'>
                                <span ng-click="projNavVm.inProgressClick();">{{inprogressTasks.length}}</span>
                            </span>
                        </span>
                    </a>
                </li>
            </ul>
        </div>
    </div>

    <%-- <div class="header-right" style="height: 50px;">
         <h3 title="{{projNavVm.activeProject.name}}" class="text-primary" style="margin: 0 10px 0 0; line-height: 50px">
             &lt;%&ndash;{{projNavVm.activeProject.name | limitTo: 15}}{{projNavVm.activeProject.name.length > 15 ? '...' : ''}}&ndash;%&gt;
         </h3>
     </div>--%>
</div>