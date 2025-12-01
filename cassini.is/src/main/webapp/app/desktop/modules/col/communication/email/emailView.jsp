<div style="height: 100%">
    <style scoped>
        .email-window {
            height: 100% !important;
            display: flex;
            flex-direction: row;
            margin-left: auto;
            margin-right: auto;
            border: 1px solid #ddd;
            width: 1200px;
        }

        .email-window .left-panel {
            width: 400px;
            border-right: 1px solid #ddd;
            overflow-y: auto;
        }

        .email-window .right-panel {
            flex: 1;
            overflow-y: auto;
            display: flex;
            flex-direction: column;
        }

        .email-message {
            border-bottom: 1px solid #ddd;
            padding: 10px;
            cursor: pointer;
        }

        .email-message.selected {
            background-color: #ddeef8;
            cursor: pointer;
        }

        .email-message .from {

        }

        .email-message .attachment {

        }

        .email-message .subject {
            color: #0078D7;
            white-space: nowrap;
            overflow: hidden;
        }

        .email-message .timestamp {
            font-size: 10px;
            margin-left: 20px;
        }

        .email-message .message-text {
            color: #666;
            overflow: hidden;
            white-space: nowrap;
        }

        .email-message.selected .message-text {
        }

        .flex-parent {
            display: flex;
            flex-direction: row;
        }

        .flex-stretch {
            flex: 1;
        }

        .email-window .message-header {
            border-bottom: 1px solid #ddd;
            padding: 10px;
        }

        .email-window .message-header .header-field {
            margin-bottom: 5px;
            border-bottom: 1px dashed #ccc;
            padding-bottom: 5px;
        }

        .email-window .message-header .header-field .field-value {
            font-weight: bolder;
        }

        .email-window .message-header .header-field .field-value span {
            margin-right: 10px;
        }

        .email-window .message-header .header-field div:nth-child(2) {
            margin-left: 10px;
        }

        .email-window .message-body {
            flex: 1;
        }

        .email-window .message-header .header-field .recipient {
            background: #ddeef8;
            padding: 3px;
            border-radius: 3px;
            border: 1px solid #00d3ff;
        }

    </style>

    <div class="email-window" id="emailWindow">
        <div class="left-panel" load-next="emailVm.loadEmailMessages()">
            <div class="email-message" ng-repeat="message in emailVm.messages"
                 ng-class="{'selected': message.selected}"
                 ng-click="emailVm.showMessage(message)">
                <div class="flex-parent">
                    <div class="from flex-stretch">
                        <h4>{{message.from}}</h4>
                    </div>
                    <div class="attachment">
                        <i class="fa fa-paperclip" ng-if="message.attachments.length > 0"></i>
                    </div>
                </div>
                <div class="flex-parent">
                    <div class="subject flex-stretch">
                        {{message.subject}}
                    </div>
                    <div class="timestamp">
                        {{message.timestamp}}
                    </div>
                </div>
                <div class="message-text">
                    {{message.messageText || "&lt;No message&gt;"}}
                </div>
            </div>
            <div ng-if="emailVm.loadingMessages">
                <span style="font-size: 14px;text-align: center;padding-left: 15px;">
                   Loading Emails<img src="app/assets/images/loaders/loader19.gif" class="mr5">
                </span>
            </div>
        </div>
        <div class="right-panel">
            <div class="message-header" ng-show="emailVm.selectedMessage.id != null">
                <div class="flex-parent header-field">
                    <div>From :</div>
                    <div class="flex-stretch field-value">
                        <span>{{emailVm.selectedMessage.from}}</span>
                    </div>
                </div>
                <div class="flex-parent header-field">
                    <div>Recipients :</div>
                    <div class="flex-stretch field-value">
                        <span class="recipient"
                              ng-repeat="recipient in emailVm.selectedMessage.recipients track by $index">{{recipient}}</span>
                    </div>
                </div>
                <div class="flex-parent header-field">
                    <div>Date Received :</div>
                    <div class="flex-stretch field-value">
                        {{emailVm.selectedMessage.timestamp}}
                    </div>
                </div>
                <div class="flex-parent header-field">
                    <div>Subject :</div>
                    <div class="flex-stretch field-value">
                        {{emailVm.selectedMessage.subject}}
                    </div>
                </div>
                <div class="flex-parent header-field">
                    <div>Attachments :</div>
                    <div class="flex-stretch field-value">
                            <span ng-repeat="attachment in emailVm.selectedMessage.attachments track by $index">
                                <a href=""
                                   ng-click="emailVm.downloadAttachment(emailVm.selectedMessage.id, attachment)">{{attachment}}</a>
                            </span>
                    </div>
                </div>
            </div>
            <div class="message-body" ng-show="emailVm.selectedMessage.id != null">
                <iframe srcdoc="{{emailVm.selectedMessage.message | toTrusted}}" frameBorder="0" width="100%"
                        height="98%"></iframe>
            </div>
        </div>
    </div>

</div>