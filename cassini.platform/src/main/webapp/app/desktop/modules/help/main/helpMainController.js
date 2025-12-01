define(
    [
        'app/assets/bower_components/cassini-platform/app/desktop/modules/help/help.module',
        'jquery-ui',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives'
    ],
    function (module) {
        module.controller('HelpMainController', HelpMainController);

        function HelpMainController($scope, $rootScope, $timeout, $interval, $state, $cookies, $translate,
                                    httpFactory) {

            var vm = this;
            $rootScope.viewInfo.icon = "fa fa-book";
            $rootScope.viewInfo.title = $translate.instant('HELP');

            var nodeId = 0;
            var helpTree = null;
            var rootNode = null;


            function onSelect(node) {
                var data = helpTree.tree('getData', node.target);
                if (data != null && data.attributes.helpToc != null &&
                    data.attributes.helpToc.file != null &&
                    data.attributes.helpToc.file != undefined) {
                    $('#helpContentsFrame').hide();
                    $('#helpContentsFrame').attr('src', 'app/help/' + data.attributes.helpToc.file)
                }
            }

            function updateIframeCss() {
                var head = $("#helpContentsFrame").contents().find("head");
                var css = '<style type="text/css">' +
                    'body{padding-right:20px !important} ' +
                    'h1{font-size: 18pt}' +
                    'h1, u{color: #2a6fa8 !important;}' +
                    '.whtbtnshow{display:none !important} ' +
                    '</style>';

                $(head).append(css);

                $('#helpContentsFrame').show();
            }

            window.updateIframeCss = updateIframeCss;

            function initHelpTree() {
                helpTree = $('#helpTree').tree({
                    data: [
                        {
                            id: nodeId,
                            text: 'Cassini Help',
                            iconCls: 'help-root',
                            attributes: {
                                helpToc: null
                            },
                            children: []
                        }
                    ],
                    onSelect: onSelect
                });

                rootNode = helpTree.tree('find', 0);

                loadHelp();
            }

            function loadHelp() {
                httpFactory.get('app/help/help.json?' + "bust=" + (new Date()).getTime()).then(
                    function (data) {
                        var nodes = [];
                        angular.forEach(data.toc, function (helpToc) {
                            var node = makeNode(helpToc);

                            if (helpToc.children != null && helpToc.children != undefined && helpToc.children.length > 0) {
                                node.state = "closed";
                                visitChildren(node, helpToc.children);
                            }

                            nodes.push(node);
                        });

                        helpTree.tree('append', {
                            parent: rootNode.target,
                            data: nodes
                        });
                    }
                )
            }

            function visitChildren(parent, helpTocs) {
                var nodes = [];
                angular.forEach(helpTocs, function (helpToc) {
                    var node = makeNode(helpToc);

                    if (helpToc.children != null && helpToc.children != undefined && helpToc.children.length > 0) {
                        node.state = 'closed';
                        visitChildren(node, helpToc.children);
                    }

                    nodes.push(node);
                });

                if (nodes.length > 0) {
                    parent.children = nodes;
                }
            }

            function makeNode(helpToc) {
                var cls = 'help-toc';
                if (helpToc.file != null &&
                    helpToc.file != undefined &&
                    helpToc.file != "") {
                    cls = 'help-file';
                }
                return {
                    id: ++nodeId,
                    text: helpToc.label,
                    iconCls: cls,
                    attributes: {
                        helpToc: helpToc
                    }
                };
            }

            (function () {
                initHelpTree();
            })();
        }
    }
);