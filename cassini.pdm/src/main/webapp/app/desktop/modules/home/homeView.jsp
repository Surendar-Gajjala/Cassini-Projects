<div>
    <style scoped>

        .search-box > input {
            height: 30px;
            border-radius: 15px;
            padding-left: 30px;
            display: inline-block !important;
            background-color: rgb(241, 243, 244);
            border: 1px solid #ddd;
            width: 300px;
            box-shadow: none;
            margin-top: 20px;
        }

        .search-box > input:hover {
            background-color: rgb(229, 231, 234);
        }

        .search-box > input:focus {
            border: 1px solid #ddd !important;
            box-shadow: none;
        }

        .search-box i.fa-search {
            position: relative;
            margin-top: 11px;
            color: grey;
            opacity: 0.5;
            margin-right: -28px !important;
        }

        .search-box i.fa-times-circle {
            margin-left: -25px;
            color: gray;
            cursor: pointer;
            margin-top: 27px;
        }

        .search-results {
            margin-top: 20px;
            display: flex;
            flex-direction: row;
            flex-wrap: wrap;
        }

        .search-results .search-result {
            position: relative;
            flex: 1;
            min-width: 200px;
            max-width: 200px;
            min-height: 200px;
            max-height: 200px;
            border: 1px solid #ddd;
            border-radius: 5px;
            margin: 10px;
            text-align: center;
            padding-top: 10px;
        }

        .search-results .search-result img {
            width: 150px;
            height: 150px;
        }

        .result-overlay {
            background-color: #2a6fa8;
            color: #fff;
            height: 38px;
            line-height: 38px;
            text-align: center;
            padding-left: 10px;
            display: flex;
            flex-direction: row;
        }

        .search-results .search-result .search-result-toolbar {
            display: none;
            position: absolute;
            top:25px;
            left: 0;
            width: 25px;
            border: 1px solid #ddd;
            border-left: 0px;
        }

        .search-results .search-result:hover .search-result-toolbar {
            display: block;
            cursor: pointer;
        }

        .search-results .search-result .search-result-toolbar div {
            border-bottom: 1px solid #ddd;
        }

        .search-results .search-result .search-result-toolbar div:last-child {
            border-bottom: 0;
        }

        .search-results .search-result .search-result-toolbar div:hover  {
            background-color: #ddd;
        }

        .result-overlay span {
            flex: 1;
            font-size: 12px;
        }

        .ribbon {
            position: absolute;
            right: -5px; top: -5px;
            z-index: 1;
            overflow: hidden;
            width: 75px; height: 75px;
            text-align: right;
        }
        .ribbon span {
            font-size: 10px;
            font-weight: bold;
            color: #FFF;
            text-transform: uppercase;
            text-align: center;
            line-height: 20px;
            transform: rotate(45deg);
            -webkit-transform: rotate(45deg);
            width: 100px;
            display: block;
            background: #79A70A;
            background: linear-gradient(#9BC90D 0%, #79A70A 100%);
            box-shadow: 0 3px 10px -5px rgba(0, 0, 0, 1);
            position: absolute;
            top: 19px; right: -21px;
        }
        .ribbon span::before {
            content: "";
            position: absolute; left: 0px; top: 100%;
            z-index: -1;
            border-left: 3px solid #79A70A;
            border-right: 3px solid transparent;
            border-bottom: 3px solid transparent;
            border-top: 3px solid #79A70A;
        }
        .ribbon span::after {
            content: "";
            position: absolute; right: 0px; top: 100%;
            z-index: -1;
            border-left: 3px solid transparent;
            border-right: 3px solid #79A70A;
            border-bottom: 3px solid transparent;
            border-top: 3px solid #79A70A;
        }
    </style>

    <div class="view-container" fitcontent>
        <div class="view-content home-content no-padding" style="overflow-y: auto;">
            <div class="search-box" style="text-align: center">
                <i class="fa fa-search"></i>
                <input type="search"
                       class="form-control input-sm req-search-form"
                       onfocus="this.setSelectionRange(0, this.value.length)"
                       ng-model="homeVm.searchText"
                       ng-model-options="{ debounce: 300 }"
                       ng-change="homeVm.search()">
                <i class="fa fa-times-circle" title="Clear search"
                   ng-show="homeVm.searchText != null && homeVm.searchText.length > 0"
                   ng-click="homeVm.searchText = '';homeVm.search()"></i>

                <a href="" style="position: relative;top: 3px;left: 5px;" ng-click="homeVm.searchText = '';homeVm.search()"><i class="fa fa-refresh"></i></a>
            </div>
            <div id="searchResults" class="search-results">
                <div class="search-result" ng-repeat="result in homeVm.files" id="file-{{result.id}}">
                    <div class="ribbon" ng-if="result.onshapeIds != null"><span>ONSHAPE</span></div>
                    <div class="search-result-toolbar">
                        <div title="Upload to onshape" ng-click="homeVm.translateToOnshape(result)"><i class="fa fa-upload"></i></div>
                        <div title="Show version history" ng-click="homeVm.showFileHistory(result.id)"><i class="fa fa-copy"></i></div>
                    </div>
                    <img ng-src="{{homeVm.getFileUrl(result)}}" alt=""
                         ng-if="result.name.toLowerCase().endsWith('.sldprt')"
                         onerror="this.src='app/assets/images/sldprt.png';">
                    <img ng-src="{{homeVm.getFileUrl(result)}}" alt=""
                         ng-if="result.name.toLowerCase().endsWith('.sldasm')"
                         onerror="this.src='app/assets/images/sldasm.png';">
                    <img src="app/assets/images/slddrw.png" alt=""
                         ng-if="result.name.toLowerCase().endsWith('.slddrw')">
                    <img src="app/assets/images/dwg.png" alt=""
                         ng-if="result.name.toLowerCase().endsWith('.dwg')">
                    <img src="app/assets/images/dxf.png" alt=""
                         ng-if="result.name.toLowerCase().endsWith('.dxf')">
                    <img src="app/assets/images/file.png" alt=""
                         ng-if="!result.name.toLowerCase().endsWith('.sldprt') &&
                         !result.name.toLowerCase().endsWith('.sldasm') &&
                         !result.name.toLowerCase().endsWith('.slddrw') &&
                         !result.name.toLowerCase().endsWith('.dwg') &&
                         !result.name.toLowerCase().endsWith('.dxf')">
                    <div class="result-overlay">
                        <span>{{result.name}} (v{{result.version}})</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

