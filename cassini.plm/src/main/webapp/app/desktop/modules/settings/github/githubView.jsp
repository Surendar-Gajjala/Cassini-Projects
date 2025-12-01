<div style="height: 100%;overflow: auto !important;">
    <div style="height: 100%;">
        <style scoped>
            .accounts-panel {
                width: 300px;
                border-right: 1px solid #eee;
                padding-right: 12px;
            }

            .repos-panel {
                width: calc(100% - 300px);
                padding-left: 12px;
                overflow: auto;
            }

            .repos-panel h5 {
                display: inline-block;
                text-align: center;
                width: calc(100% - 50px);
            }

            .accounts-panel .new-account-btn,
            .repos-panel .new-repo-btn {
                display: inline-block;
                float: right;
                margin-top: 5px;
                width: 25px;
                height: 25px;
                text-align: center;
                border-radius: 2px;
                cursor: pointer;
            }

            .accounts-panel .new-account-btn:hover,
            .repos-panel .new-repo-btn:hover {
                background-color: #ddd;
            }

            .github-account {
                padding: 10px;
                cursor: pointer;
                height: auto;
                border-top: 1px dotted #ddd;
                border-bottom: 1px dotted #ddd;
            }

            .github-account:hover {
                background-color: #d6e1e0 !important;
            }

            .github-account .account-name {
                font-weight: 700;
                margin-bottom: 5px;
            }

            .github-account .action-btns {
                float: right;
                display: none;
            }

            .github-account:hover .action-btns,
            .selected-account .action-btns {
                display: block;
            }

            .selected-account, .selected-account:hover {
                color: white;
                background-color: #0081c2 !important;
            }

            .arrow-div.pd-l-20 {
                padding-left: 10px;
            }

            .arrow-div i.las {
                margin-right: 10px;
                position: absolute;
                margin-top: 3px;
            }

            .arrow-div span {
                margin-left: 25px;
            }

            .repos-panel .repos-container {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(400px, max-content));
                grid-gap: 20px;
                justify-content: center;
            }

            .repos-panel .repos-container .github-repo {
                max-width: 400px;
                height: 100px;
                background-image: var(--cassini-linear-gradient);
                border: 1px solid #e6eaec;
                /*box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2);*/
                transition: 0.3s;
                padding: 15px 15px 5px 15px;
                border-radius: 5px;
                margin: 10px;
                flex: 1;
            }

            .repos-panel .repos-container .github-repo .repo-name {
                font-weight: 700;
                font-size: 16px;
            }

            .repos-panel .repos-container .github-repo .repo-description {
                height: 20px;
            }

            .round {
                position: relative;
            }

            .round label {
                background-color: #fff;
                border: 1px solid #ccc;
                border-radius: 50%;
                cursor: pointer;
                height: 28px;
                left: 0;
                position: absolute;
                top: 0;
                width: 28px;
            }

            .round label:after {
                border: 2px solid #fff;
                border-top: none;
                border-right: none;
                content: "";
                height: 6px;
                left: 7px;
                opacity: 0;
                position: absolute;
                top: 8px;
                transform: rotate(-45deg);
                width: 12px;
            }

            .round input[type="checkbox"] {
                visibility: hidden;
            }

            .round input[type="checkbox"]:checked + label {
                background-color: #66bb6a;
                border-color: #66bb6a;
            }

            .round input[type="checkbox"]:checked + label:after {
                opacity: 1;
            }

            .github-repo .selection-container {
                float: right;
                width: 30px;
                margin-top: -20px;
            }

            .m-r-10 {
                margin-right: 10px;
            }

            .github-repo .repo-stats {
                font-size: 12px;
                margin-top: 14px;
                width: 370px;
                opacity: 0.6
            }

        </style>
        <div style="display: flex;height: 100%;">
            <div class="accounts-panel">
                <h5 style="display: inline-block;">GitHub Accounts</h5>

                <div class="new-account-btn" ng-click="githubVm.createNewGitHubAccount()">
                    <i class="la la-plus"></i>
                </div>
                <div ng-repeat="account in githubVm.githubAccounts" class="github-account"
                     ng-class="{'selected-account': account.id == githubVm.selectedAccount.id}"
                     ng-click="githubVm.selectAccount(account)">
                    <div class="action-btns">
                        <i class="fa fa-edit" title="Edit account"
                           ng-click="githubVm.editGitHubAccount($event, account)"></i>
                        <i class="fa fa-trash" title="Delete account"
                           ng-click="githubVm.deleteGitHubAccount($event, account)"></i>
                    </div>
                    <div class="account-name">{{account.name}}</div>
                    <div>{{account.description}}</div>
                </div>
            </div>
            <div class="repos-panel">
                <h5 style="">GitHub Repositories</h5>

                <div class="arrow-div pd-l-20" ng-if="githubVm.selectedAccount == null">
                    <i class="las la-arrow-left"></i>
                    <span>Select an account</span>
                </div>

                <div ng-if="githubVm.selectedAccount != null" class="repos-container">
                    <div class="github-repo" ng-repeat="repo in githubVm.githubRepositories">
                        <div class="repo-name">
                            <div>{{repo.name}}</div>
                            <div class="selection-container">
                                <div class="round"
                                     title="{{repo.selected ? 'De-select repository' : 'Select repository'}}">
                                    <input type="checkbox" id="checkbox{{$index}}" ng-model="repo.selected"
                                           ng-click="githubVm.toggleRepositorySelection(repo)"/>
                                    <label for="checkbox{{$index}}"></label>
                                </div>
                            </div>
                        </div>
                        <div class="repo-description">
                            {{repo.description}}
                        </div>
                        <div class="repo-stats">
                            <span class="m-r-10">Last Updated: {{repo.time}}</span>
                            <span class="m-r-10">Releases: {{repo.releases}}</span>
                            <!--<span class="m-r-10">Forks: {{repo.forks}}</span>-->
                            <span class="">Open Issues: {{repo.openIssues}}</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>