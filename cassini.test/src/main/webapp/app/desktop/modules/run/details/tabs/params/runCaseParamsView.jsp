<div>
    <div class="row" id="testCaseDetailsDiv">


        <div class="panel panel-primary">
            <div class="panel-heading" style="font-size: 16px;font-weight: bold;padding: 5px;">Input Param</div>
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Value</th>
                </tr>
                </thead>
                <tr ng-if="runDetailsVm.testCases.length == 0">
                    <td colspan="10"><span>No input param</span></td>
                </tr>
                <tbody>
                <tr ng-repeat="inputParam in inputParams" style="color: black;">
                    <td>{{inputParam.name}}</td>
                    <td title="{{inputParam.description}}">{{inputParam.id}}</td>
                    <td>
                        <span ng-if="inputParam.dataType == 'INTEGER'">
                            {{inputParam.runInputParamValue.integerValue}}</span>
                        <span ng-if="inputParam.dataType == 'STRING'">{{inputParam.runInputParamValue.stringValue}}</span>
                        <span ng-if="inputParam.dataType == 'DOUBLE'">{{inputParam.runInputParamValue.doubleValue}}</span>
                        <span ng-if="inputParam.dataType == 'DATE'">{{inputParam.runInputParamValue.dateValue}}</span>
                    </td>


                </tr>
                </tbody>
            </table>
        </div>
        <br/>

        <div class="panel panel-primary">
            <div class="panel-heading" style="font-size: 16px;font-weight: bold;padding: 4px;">Output Param</div>
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Type</th>
                    <th>Description</th>
                    <th>Expected</th>
                    <th>Actual</th>
                    <th>Result</th>
                </tr>
                </thead>
                <tr ng-if="runDetailsVm.testCases.length == 0">
                    <td colspan="10"><span>No output param</span></td>
                </tr>
                <tbody>
                <tr ng-repeat="outputParam in outputParams" style="color: black;">
                    <td>{{outputParam.name}}</td>
                    <td>{{outputParam.dataType}}</td>
                    <td title="{{outputParam.description}}">{{outputParam.id}}</td>
                    <td>
                       <span ng-if="outputParam.dataType == 'INTEGER'">
                            {{outputParam.runOutPutParamExpectedValue.integerValue}}</span>
                        <span ng-if="outputParam.dataType == 'STRING'">{{outputParam.runOutPutParamExpectedValue.stringValue}}</span>
                        <span ng-if="outputParam.dataType == 'DOUBLE'">{{outputParam.runOutPutParamExpectedValue.doubleValue}}</span>
                        <span ng-if="outputParam.dataType == 'DATE'">{{outputParam.runOutPutParamExpectedValue.dateValue}}</span>
                    </td>
                    <td>
                       <span ng-if="outputParam.dataType == 'INTEGER'">
                            {{outputParam.runOutputParamActualValue.integerValue}}</span>
                        <span ng-if="outputParam.dataType == 'STRING'">{{outputParam.runOutputParamActualValue.stringValue}}</span>
                        <span ng-if="outputParam.dataType == 'DOUBLE'">{{outputParam.runOutputParamActualValue.doubleValue}}</span>
                        <span ng-if="outputParam.dataType == 'DATE'">{{outputParam.runOutputParamActualValue.dateValue}}</span>
                    </td>
                    <td>
                        <span ng-if="outputParam.dataType == 'INTEGER'">
                             <span ng-if=" outputParam.runOutPutParamExpectedValue.integerValue == outputParam.runOutputParamActualValue.integerValue ">TRUE</span>
                            <span ng-if=" outputParam.runOutPutParamExpectedValue.integerValue != outputParam.runOutputParamActualValue.integerValue ">FALSE</span>

                        </span>
                         <span ng-if="outputParam.dataType == 'STRING'">
                             <span ng-if=" outputParam.runOutPutParamExpectedValue.stringValue == outputParam.runOutputParamActualValue.stringValue ">TRUE</span>
                              <span ng-if=" outputParam.runOutPutParamExpectedValue.stringValue != outputParam.runOutputParamActualValue.stringValue ">FALSE</span>

                         </span>
                         <span ng-if="outputParam.dataType == 'DOUBLE'">
                             <span ng-if=" outputParam.runOutPutParamExpectedValue.doubleValue == outputParam.runOutputParamActualValue.doubleValue ">TRUE</span>
                         <span ng-if=" outputParam.runOutPutParamExpectedValue.doubleValue != outputParam.runOutputParamActualValue.doubleValue ">FALSE</span>

                         </span>
                         <span ng-if="outputParam.dataType == 'DATE'">
                             <span ng-if=" outputParam.runOutPutParamExpectedValue.dateValue == outputParam.runOutputParamActualValue.dateValue ">TRUE</span>
                             <span ng-if=" outputParam.runOutPutParamExpectedValue.dateValue != outputParam.runOutputParamActualValue.dateValue ">TRUE</span>

                         </span>

                    </td>

                </tr>
                </tbody>
            </table>
        </div>


    </div>
</div>
