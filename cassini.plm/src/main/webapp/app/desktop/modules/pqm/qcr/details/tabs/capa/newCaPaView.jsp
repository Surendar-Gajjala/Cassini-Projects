<div style="position: relative;padding-bottom: 150px;">
    <style scoped>
        table td {
            padding-top: 0px !important;
            padding-bottom: 0px !important;
        }

    </style>
    <div style="overflow-y: auto; overflow-x: hidden; padding: 20px;">
        <div class="row" style="margin: 0;">
            <div>
                <br>

                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>ROOT_CAUSE_ANALYSIS</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                             <textarea class="form-control" rows="3" style="resize: none"
                                       ng-if="newCaPaVm.capaMode == 'EDIT' || newCaPaVm.capaMode == 'NEW'"
                                       ng-model="newCaPaVm.newCaPa.rootCauseAnalysis"></textarea>

                            <p ng-if="newCaPaVm.capaMode == 'AUDIT'" style="margin-top: 10px;">
                                {{newCaPaVm.newCaPa.rootCauseAnalysis}}</p>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>CORRECTIVE_ACTION</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      ng-if="newCaPaVm.capaMode == 'EDIT' || newCaPaVm.capaMode == 'NEW'"
                                      ng-model="newCaPaVm.newCaPa.correctiveAction"></textarea>

                            <p ng-if="newCaPaVm.capaMode == 'AUDIT'" style="margin-top: 10px;">
                                {{newCaPaVm.newCaPa.correctiveAction}}</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>PREVENTIVE_ACTION</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      ng-if="newCaPaVm.capaMode == 'EDIT' || newCaPaVm.capaMode == 'NEW'"
                                      ng-model="newCaPaVm.newCaPa.preventiveAction"></textarea>

                            <p ng-if="newCaPaVm.capaMode == 'AUDIT'" style="margin-top: 10px;">
                                {{newCaPaVm.newCaPa.preventiveAction}}</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">
                            <span translate>CAPA_NOTES</span> : </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      ng-if="newCaPaVm.capaMode == 'EDIT' || newCaPaVm.capaMode == 'NEW'"
                                      ng-model="newCaPaVm.newCaPa.capaNotes"></textarea>

                            <p ng-if="newCaPaVm.capaMode == 'AUDIT'" style="margin-top: 10px;">
                                {{newCaPaVm.newCaPa.capaNotes}}</p>
                        </div>
                    </div>

                    <div class="form-group" ng-if="newCaPaVm.capaMode == 'AUDIT'">
                        <label class="col-sm-4 control-label">
                            <span translate>AUDIT_RESULT</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <ui-select ng-model="newCaPaVm.newCaPa.result" theme="bootstrap">
                                <ui-select-match placeholder="Select Result">{{$select.selected}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="result in newCaPaVm.results">
                                    <div ng-bind="result"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>

                    <div class="form-group" ng-if="newCaPaVm.capaMode == 'AUDIT'">
                        <label class="col-sm-4 control-label">
                            <span translate>AUDIT_NOTES</span>
                            <span class="asterisk">*</span> : </label>

                        <div class="col-sm-7">
                            <textarea class="form-control" rows="3" style="resize: none"
                                      ng-model="newCaPaVm.newCaPa.auditNotes"></textarea>
                        </div>
                    </div>

                </form>
            </div>
        </div>
    </div>
</div>
