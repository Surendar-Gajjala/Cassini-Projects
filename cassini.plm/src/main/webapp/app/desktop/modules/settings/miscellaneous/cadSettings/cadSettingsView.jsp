<h4 style="margin-left: 20px;" translate>DRAWING_TEMPLATES</h4>

<div class="responsive-table" style="padding: 10px;overflow: auto;">
    <style scoped>
        .col-width-300 {
            width: 300px;
        }
    </style>
    <table class="table table-striped highlight-row">
        <thead>
        <tr>
            <th style="width: 20px;">
                <i class="la la-plus" ng-click="cadSettingsVm.addTemplate()" title="{{addDrawingTemplate}}"
                   style="cursor: pointer"></i>
            </th>
            <th style="" translate>NAME</th>
            <th style="" translate>DESCRIPTION</th>
            <th translate>TEMPLATE_FILE</th>
            <th class="text-center" translate>ACTIONS</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="cadSettingsVm.drawingTemplates.length == 0">
            <td colspan="10"><span translate>NO_DRAWING_TEMPLATES</span></td>
        </tr>

        <tr ng-repeat="drawingTemplate in cadSettingsVm.drawingTemplates">
            <td></td>
            <td class="col-width-200">
                <span ng-if="drawingTemplate.editMode == undefined || drawingTemplate.editMode == false">{{drawingTemplate.name}}</span>
                <input type="text" class="form-control" ng-if="drawingTemplate.editMode"
                       ng-model="drawingTemplate.name"/>
            </td>
            <td>
                <span ng-if="drawingTemplate.editMode == undefined || drawingTemplate.editMode == false">{{drawingTemplate.description}}</span>
                <input type="text" class="form-control" ng-if="drawingTemplate.editMode"
                       ng-model="drawingTemplate.description"/>
            </td>
            <td class="col-width-300">
                <a href="" ng-click="cadSettingsVm.filePreview(drawingTemplate)" title="{{previewTitle}}"
                   ng-if="drawingTemplate.editMode == undefined || drawingTemplate.editMode == false">{{drawingTemplate.attachmentName}}</a>
                <input type="file" class="form-control" ng-if="drawingTemplate.editMode"
                       ng-file-model="drawingTemplate.file"/>
            </td>
            <td style="vertical-align: middle !important;width: 75px;" class="text-center">
                <i ng-if="drawingTemplate.editMode == true" title="{{saveTitle}}"
                   class="la la-check" ng-click="cadSettingsVm.saveDrawingTemplate(drawingTemplate)"></i>
                <i ng-if="drawingTemplate.editMode == true" title="{{cancelChangesTitle}}"
                   class="la la-times" ng-click="cadSettingsVm.cancelChanges(drawingTemplate)"></i>
                <i ng-if="drawingTemplate.editMode == undefined || drawingTemplate.editMode == false"
                   title="{{editTitle}}"
                   class="la la-pencil" ng-click="cadSettingsVm.editTemplate(drawingTemplate)"></i>
                <i ng-if="drawingTemplate.editMode == undefined || drawingTemplate.editMode == false"
                   title="{{deleteTitle}}"
                   class="la la-trash" ng-click="cadSettingsVm.deleteTemplate(drawingTemplate)"></i>
            </td>
        </tr>
        </tbody>
    </table>
</div>