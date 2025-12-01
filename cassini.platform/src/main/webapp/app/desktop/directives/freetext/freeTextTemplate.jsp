<div id="freeTextSearchDirective"
     style="width: 250px;position: absolute;top: 38px;right: 10px;">

    <style scoped>
        .search-box > input {
            width: 100%;
            padding-left: 30px;
            height: 30px;
            display: inline-block !important;
            background-color: rgb(241, 243, 244);
            border: 1px solid #ddd;
            box-shadow: none;
            padding-right: 30px;
        }

        .search-box > input:hover {
            background-color: rgb(229, 231, 234);
        }

        .search-box > input:focus {
            box-shadow: none;
        }

        .search-box i.fa-search {
            z-index: 4 !important;
            position: absolute;
            margin-top: 11px;
            margin-left: 10px;
            color: grey;
            opacity: 0.5;
        }

        .search-box i.clear-search {
            margin-left: -25px;
            cursor: pointer;
            z-index: 4 !important;
            position: absolute;
            margin-top: 11px;
            font-size: 14px;
            opacity: 0.6;
        }
    </style>
    <form id="freeTextSearch">
        <div class="search-box">
            <i class="fa fa-search"></i>

            <input type="text" id="freeTextSearchInput"
                   autocomplete="off"
                   class="form-control input-sm"
                   ng-model="searchTerm"
                   ng-model-options="{ debounce: 500 }"
                   ng-change="onSearch(searchTerm)"
                   ng-init="showClear()"
                   ng-enter="onSearch(searchTerm)">
            <i class="la la-times clear-search" title="{{clearTitleSearch}}"
               ng-show="searchTerm.length > 0 || filterSearch == true"
               ng-click="searchTerm = '';clear()"></i>
        </div>
    </form>
</div>