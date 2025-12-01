define(
    [
        'app/shared/services/services.module'
    ],
    function(module) {
        module.factory('CachingService',
            function() {
                this.cache = {};
            }
        );
    }
);