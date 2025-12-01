define(
    [
        'app/desktop/modules/definition/definition.module'
    ],
    function (module) {
        module.controller('NewScriptController', NewScriptController);

        function NewScriptController($scope, $rootScope, $timeout, $state, $stateParams, $cookies, $window) {
            var vm = this;
            var script = $scope.data.selectedScript;
            var updateScript = $scope.data.getScript;
            var updateScriptPython = $scope.data.getScriptPython;
            vm.script = null;
            /* ------- Create Script -----*/
            function create() {
                var editor = ace.edit("editor");
                var code = editor.getValue();
                vm.scriptType = code;
                $scope.callback(vm.scriptType);
                $rootScope.hideSidePanel();
            }

            /* ------ Load Script -----*/
            function loadScriptData() {
                $timeout(function () {
                        if (script == "GROOVY") {
                            script = "ace/mode/groovy";
                            var editor = ace.edit("editor");
                            editor.setTheme("ace/theme/monokai")
                            if (updateScript == undefined) {
                                editor.focus();
                                editor.setShowPrintMargin(false);
                                editor.getSession().setUseWorker(false);
                                editor.getSession().setMode(script);
                            }
                            else {
                                editor.setValue(updateScript, 1);
                                editor.setShowPrintMargin(false);
                                editor.focus();
                                editor.getSession().setUseWorker(false);
                                editor.getSession().setMode(script);
                            }

                        }

                        if (script == "PYTHON") {
                            script = "ace/mode/python";
                            var editor = ace.edit("editor");
                            editor.setTheme("ace/theme/monokai")
                            if (updateScriptPython == undefined) {
                                editor.focus();
                                editor.setShowPrintMargin(false);
                                editor.getSession().setUseWorker(false);
                                editor.getSession().setMode(script);
                            }
                            else {
                                editor.setValue(updateScriptPython, 1);
                                editor.setShowPrintMargin(false);
                                editor.focus();
                                editor.getSession().setUseWorker(false);
                                editor.getSession().setMode(script);
                            }
                        }
                    }

                    ,
                    1000
                )
                ;
            }

            (function () {
                $rootScope.$on('app.definitions.newScript', create);
                loadScriptData();

            })();
        }
    }
)
;
