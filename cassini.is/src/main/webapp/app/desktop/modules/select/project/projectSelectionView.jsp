<style scoped>

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

</style>

<div class="view-container" fitcontent>
    <div class="view-toolbar selector" style="top: 30px; background-color: white">
        <free-text-search on-clear="projectSelectVm.resetPage"
                          on-search="projectSelectVm.freeTextSearch"></free-text-search>
        <div class="pull-right text-center" style="padding: 10px;margin-top: -5px;">
            <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                        Displaying {{projectSelectVm.projects.numberOfElements}} of
                                            {{projectSelectVm.projects.numberOfElements}}
                                    </span>
                            </medium>
                        </span>
                <span class="mr10">Page {{projectSelectVm.projects.totalElements != 0 ? projectSelectVm.projects.number+1:0}} of {{projectSelectVm.projects.totalPages}}</span>
                <a href="" ng-click="projectSelectVm.previousPage()"
                   ng-class="{'disabled': projectSelectVm.projects.first}"><i
                        class="fa fa-arrow-circle-left mr10"></i></a>
                <a href="" ng-click="projectSelectVm.nextPage()"
                   ng-class="{'disabled': projectSelectVm.projects.last}"><i
                        class="fa fa-arrow-circle-right"></i></a>
            </div>
        </div>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px !important;">

        <table class="table table-striped highlight-row">
            <thead>
            <tr>
                <th style="width: 40px;">Select</th>
                <th style="width: 200px;">Project Name</th>
                <th style="width: 200px;">Description</th>
                <th style="width: 200px;">Project Owner</th>
            </tr>
            </thead>

            <tbody>
            <tr ng-if="projectSelectVm.loading == true">
                <td colspan="12">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                             class="mr5">
                        <span translate>Loading projects</span>
                    </span>
                </td>
            </tr>
            <tr ng-if="projectSelectVm.loading == false && projectSelectVm.projects.content.length == 0">
                <td colspan="12">
                    <span translate>No projects</span>
                </td>
            </tr>

            <tr ng-repeat="project in projectSelectVm.projects.content | filter: search"
                ng-click="project.isChecked = !project.isChecked; projectSelectVm.radioChange(project, $event)"
                ng-dblClick="project.isChecked = !project.isChecked; projectSelectVm.selectRadioChange(project, $event)">
                <td style="width: 40px;">
                    <input type="radio" ng-checked="project.isChecked" name="project" value="project"
                           ng-dblClick="projectSelectVm.selectRadioChange(project, $event)"
                           ng-click="projectSelectVm.radioChange(project, $event)">
                </td>
                <td style="width: 200px;">{{project.name}}</td>
                <td style="width: 200px;">{{project.description}}</td>
                <td style="width: 200px;">{{project.projectOwnerObject.fullName}}</td>
            </tr>
            </tbody>
        </table>
    </div>
</div>