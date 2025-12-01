<div>
    <table class="table table-striped">
        <thead>
        <tr>
            <th>TimeStamp</th>
            <th>Message</th>
            <th>MessageType</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-repeat="message in messages">
            <td>{{message.timeStamp}}</td>
            <td>{{message.message}}</td>
            <td>{{message.messageType}}</td>
        </tr>
        </tbody>
    </table>
</div>