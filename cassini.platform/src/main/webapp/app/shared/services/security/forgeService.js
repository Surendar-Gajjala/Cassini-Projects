/**
 * Created by Nageshreddy on 09-05-2019.
 */
define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],

    function (mdoule) {
        mdoule.factory('ForgeService', ForgeService);
        var formats = ["3dm", "3ds", "asm", "catpart", "catproduct", "cgr", "collaboration", "dae", "dgn", "dlv3", "dwf", "dwfx", "dwg", "dwt", "dxf", "emodel", "exp", "f3d", "fbx",
            "g", "gbxml", "glb", "gltf", "iam", "idw", "ifc", "ige", "iges", "igs", "ipt", "iwm", "jt", "max", "model", "neu", "nwc", "nwd", "obj", "pdf", "pmlprj", "pmlprjz", "prt",
            "psmodel", "rcp", "rvt", "sab", "sat", "session", "skp", "sldasm", "sldprt", "ste", "step", "stl", "stla", "stlb", "stp", "stpz", "wire", "x_b", "x_t", "xas", "xpr", "zip"];

        function ForgeService(httpFactory) {
            return {
                getForgeAuthentication: getForgeAuthentication,
                showForgeFile: showForgeFile,
                isForgeActive: isForgeActive,
                verifyIsCADFile: verifyIsCADFile,
                checkForgeAuthentication: checkForgeAuthentication
            };

            function getForgeAuthentication() {
                var url = "api/platform/forge/authenticate";
                return httpFactory.get(url);
            }

            function showForgeFile(urn) {
                var url = "api/platform/forge/view/" + urn;
                return httpFactory.get(url);
            }

            function isForgeActive() {
                var url = "api/platform/forge/active";
                return httpFactory.get(url);
            }

            function verifyIsCADFile(file) {
                var ext = file.name.split('.').pop();
                return (formats.includes(ext.toLowerCase()));
            }

            function checkForgeAuthentication(clientId, clientKey) {
                var url = "api/platform/forge/authenticate/validate?clientId={0}&clientKey={1}".
                    format(clientId, clientKey);
                return httpFactory.get(url);
            }
        }
    }
);