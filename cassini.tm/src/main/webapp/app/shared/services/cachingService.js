define(['app/phone/phone.modules'],
    function(app) {
        app.service('cachingService',
            function() {
                this.cache = {};
            }
        );
    }
);