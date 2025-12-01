<div class="view-container">
    <div class="view-toolbar">
        <button class="btn btn-sm btn-success" ng-click="allProjectsVm.createProject();">Create Project</button>
        <free-text-search on-clear="allProjectsVm.resetPage"
                          on-search="allProjectsVm.freeTextSearch"></free-text-search>
    </div>

    <div class="view-content">
        <div style="padding: 10px;">
            <div class="pull-right text-center">
                <span ng-if="allProjectsVm.loading == false"><small>Page {{allProjectsVm.projects.number+1}} of
                    {{allProjectsVm.projects.totalPages}}
                </small></span>
                <br>

                <div class="btn-group" style="margin-bottom: 0">
                    <button class="btn btn-xs btn-default"
                            ng-click="allProjectsVm.previousPage()"
                            ng-disabled="allProjectsVm.projects.first">
                        <i class="fa fa-chevron-left"></i>
                    </button>
                    <button class="btn btn-xs btn-default"
                            ng-click="allProjectsVm.nextPage()"
                            ng-disabled="allProjectsVm.projects.last">
                        <i class="fa fa-chevron-right"></i>
                    </button>
                </div>
                <br>
                    <span ng-if="allProjectsVm.loading == false"><small>{{allProjectsVm.projects.totalElements}}
                        Projects
                    </small></span>
            </div>
            <div>
                <a href="" class="btn btn-xs btn-danger" ng-if="allProjectsVm.clear == true"
                   ng-click="allProjectsVm.clearFilter()">Clear Filters</a>
            </div>
        </div>

        <table class="table table-striped">
            <thead>
            <tr>
                <th style="width: 200px;">Name</th>
                <th style="width: 200px;">Description</th>
              <%--  <th style="width: 150px;">Actions</th>--%>
            </tr>

            </thead>
            <tbody>
            <tr ng-repeat="project in allProjectsVm.projects.content">
                <td><a href="" ng-click="allProjectsVm.showProjectDetails(project)">{{project.name}}</a></td>
                <td>{{project.description}}</td>
                <%--<td>
                    <button title="Delete Project" class="btn btn-xs btn-danger"
                            ng-click="allProjectsVm.deleteProject(project)"><i class="fa fa-trash"></i></button>
                </td>--%>
            </tr>
            </tbody>
        </table>

    </div>
</div>