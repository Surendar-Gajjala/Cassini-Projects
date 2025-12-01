<style>
    .popover-content {
        overflow: auto !important;
    }
    .sticky-header {
        position: sticky;
        top: -10px !important;
        z-index: 10 !important;
    }
</style>

<div style="max-height: 300px;min-width: 400px !important;">
    <table class="table table-striped">
        <thead >
        <tr class="sticky-header">
            <th style="" translate>NAME</th>
            <th style="width: 80px; text-align: center;" translate>VERSION</th>
            <th style="width: 165px;" translate >MODIFIED_DATE</th>
        </tr>
        <tr ng-if="object.itemFiles.length == 0" style="border-top: 1px solid lightgrey;">
            <td colspan="10"
                style="width: 100px;background: #fcfffa !important;color: blue !important;text-align: left;" translate>
                NO_FILES
            </td>
        </tr>
        </thead>
        <tbody>
        <tr ng-repeat="file in object.itemFiles">
            <td>
                    <span class="level{{file.level}}" ng-show="file.fileType == 'FOLDER'"
                          ng-click="toggleNode(file)">
                        <i class="mr5 fa" title="{{ExpandCollapse}}"
                           style="cursor: pointer !important; font-size: 18px;color: limegreen !important;"
                           ng-class="{'fa-folder': (file.expanded == false || file.expanded == null || file.expanded == undefined),
                           'fa-folder-open': file.expanded == true}">
                        </i>
                          <span class="autoClick" title="{{file.name}}"
                                style="cursor: pointer !important;white-space: normal;word-wrap: break-word;"
                                ng-bind-html="file.name | limitTo:50 | highlightText: searchText">
                        </span>
                        {{file.name.length > 48 ? '...':''}}
                        <span class="label label-default"
                              style="font-size: 12px;background-color: #e4dddd;font-style: italic;"
                              ng-if="file.count > 0" ng-bind-html="file.count"></span>
                    </span>

                    <span class="imageTooltip level{{file.level}}" ng-show="file.fileType == 'FILE'">
                        <span>
                                <a href="" ng-click="downloadFile(file)" title="{{file.name}} - {{downloadFileTitle}}">
                                    <span ng-bind-html="file.name | limitTo:50 | highlightText: searchText"></span>
                                    {{file.name.length > 48 ? '...':''}}
                                </a>
                        </span>
                    </span>
            </td>
            <td style="width: 80px; text-align: center"> {{file.version}}</td>
            <td> {{file.modifiedDate}}</td>
        </tr>
        </tbody>
    </table>
</div>
