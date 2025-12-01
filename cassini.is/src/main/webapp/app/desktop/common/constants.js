define(['app/desktop/desktop.app'], function (module) {
    module.factory('Constants',
        [
            function () {
                return {
                    App: {
                        ViewType: Enum.define("ViewType", ["APP", "PROJECT", "PROCUREMENT"])
                    }
                }
            }
        ]);
});