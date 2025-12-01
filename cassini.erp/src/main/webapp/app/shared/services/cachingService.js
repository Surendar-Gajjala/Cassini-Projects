define(['app/app.modules'],
    function(app) {
        app.service('cachingService',
            function() {
                this.cache = {};
            }
        );
    }
);