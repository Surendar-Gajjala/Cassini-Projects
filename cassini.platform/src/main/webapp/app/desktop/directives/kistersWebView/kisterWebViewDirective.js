/**
 * Created by Nageshreddy on 02-12-2020.
 */
define(
    [
        'app/desktop/desktop.app'
    ],

    function (module) {
        module.directive('kistersWebView', ['$compile', '$timeout', '$application', '$rootScope', '$translate', '$sce', function ($compile, $timeout, $application, $rootScope, $translate, $sce) {
            function link(scope, element, attrs) {

                var backendWebsocket = null;

                var kisterFormats = ['PDF', '3DVS', 'VSXML', 'SAT', 'SAB', '3DS', 'DWF', 'DWFX', 'IPT', 'IAM', 'MODEL', 'DLV', 'EXP',
                    'SESSION', 'CATPRODUCT', 'CATPART', 'CATShape', 'CGR', '3DXML', 'DAE', 'CPIXML', 'PRT', 'XPR', 'ASM', 'XAS', 'NEU',
                    'FBX', 'GLTF', 'GLB', 'MF1', 'ARC', 'UNV', 'PKG', 'IGS', 'IGES', 'IFC', 'JT', 'ES7', 'PRT', 'X_T', 'X_B', 'PLMXML',
                    'PRC', 'RVT', 'RFA', '3DM', 'ASM', 'PAR', 'PWD', 'PSM', 'SLDASM', 'SLDPRT', 'STP', 'STEP', 'STPZ', 'STPX', 'STPXZ',
                    'STL', 'U3D', 'VDA', 'WRL', 'VRML', 'OBJ'];

                var clientId = "";
                scope.registerKisterCallBack({
                    kisterCallBack: function (file, path, modelEle) {
                        if (backendWebsocket == null || (backendWebsocket != null && backendWebsocket.readyState === WebSocket.CLOSED)) {
                            backendWebsocket = new WebSocket("ws://localhost:9999/");
                            $timeout(function () {
                                sendMessageToWebSocket(file, path, modelEle);
                            }, 1000);
                        } else {
                            sendMessageToWebSocket(file, path, modelEle);
                        }
                    }
                });

                function sendMessageToWebSocket(file, path, modelEle) {
                    backendWebsocket.onmessage = receive.bind(this);
                    if (file.name != "" || file.name != null || file.name != undefined) {
                        var ext = getFileExtension(file.name);
                        //if (kisterFormats.includes(ext.toUpperCase())) {
                        showConvertedKistersFile(file, path, modelEle);
                        //}

                    }
                }

                function getFileExtension(filename) {
                    return filename.split('.').pop();
                }

                function validateToken() {
                    console.log("kister Token: " + $rootScope.kisterToken);
                    authenticateSession(backendWebsocket, $rootScope.kisterToken);
                }

                function authenticateSession(backendWebsocket, token) {
                    if (backendWebsocket.readyState === 1) {
                        var backendClientAuthenticationRequest = "<Request Type='AuthenticateSession'><Token>" + token + "</Token></Request>";
                        backendWebsocket.send(backendClientAuthenticationRequest);
                        console.log("authentication is succeeded");
                    }
                }

                function showConvertedKistersFile(file, path, modelEle) {
                    validateToken();
                    var modal;
                    var times1;
                    if (modelEle == null || modelEle == undefined) {
                        modal = document.getElementById('kister-model');
                        times1 = document.getElementById('closeImageId');
                        modal.style.display = "block";
                        var span = document.getElementsByClassName("closeImage1")[0];
                        span.onclick = function () {
                            modal.style.display = "none";
                            times1.style.display = "none";
                        };
                    } else {
                        modal = modelEle;
                    }

                    scope.fileUrl = "app/assets/bower_components/cassini-platform/kisters/custom/full-screen-backend-integration.html?token=" + $rootScope.kisterToken;

                    $('#WebViewFrame').attr('src', scope.fileUrl);

                    $timeout(function () {
                        openModel(file.id, path);
                    }, 4000);
                }

                function receive(ev) {
                    if (typeof ev.data === 'string') {
                        var msg = ev.data;
                        var parser = new DOMParser();
                        var parsedXML = parser.parseFromString(msg, "text/xml");
                        var events = parsedXML.getElementsByTagName('BackendEvent');
                        var callCount = events.length;
                        for (var i = 0; i < callCount; i++) {
                            id = events[i].children[0].innerHTML;
                            handleMessage(id, events[i]);
                        }
                    }
                }


                function handleMessage(id, parsedXML) {
                    var msg = parsedXML.innerHTML;
                    if (msg.indexOf('Token') !== -1 && msg.indexOf('Authentication') !== -1 && msg.indexOf('<Authentication>Succeeded') !== -1) {
                        var tok = parsedXML.children[2].innerHTML;

                        $('#' + tok).parent().remove();
                        clientId = id;
                        console.log("Communication ready for client: " + id);
                        $('.client-table-row').each(function (e) {
                            $(this).removeClass('client-table-row-active');
                            if (id === this.id) {
                                $(this).addClass('client-table-row-active');
                            }
                        })
                    }
                }

                function callToBackend(call) {
                    console.log("File is opening");
                    var prefix = "<Request Type='XMLCall'><ClientId>" + clientId + "</ClientId>";
                    var postfix = "</Request>";
                    backendWebsocket.send(prefix + call + postfix);
                }

                function openModel(fileId, path) {
                    var times1 = document.getElementById('closeImageId');
                    times1.style.display = 'block';
                    var fileValue = path;
                    var xml = '<Call Method="OpenFile"><FileName>' + fileValue + '</FileName></Call>';
                    callToBackend(xml);
                }
            }

            return {
                templateUrl: 'app/assets/bower_components/cassini-platform/app/desktop/directives/kistersWebView/kistersView.jsp',
                restrict: 'E',
                replace: false,
                scope: {
                    registerKisterCallBack: '&'
                }, link: link
            };
        }])
        ;
    }
);