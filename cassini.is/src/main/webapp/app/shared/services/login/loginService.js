define(['app/shared/services/services.module', 'app/shared/factories/httpFactory'],
    function (module) {
        module.factory('LoginService',LoginService);

        function LoginService ($http, $q, httpFactory) {
            return {
                login: login,
                logout: logout,
                current: current,
                createLogin: createLogin,
                getLogins: getLogins,
                resetPassword: resetPassword,
                changePassword: changePassword,
                getLogin:getLogin,
                updateLogin:updateLogin
            };

            function login(user, password) {
                var url = "api/security/login/" + user + "/" + password;
                return httpFactory.get(url);
            }

            function logout() {
                var url = "api/security/logout";
                return httpFactory.get(url);
            }

            function current() {
                var url = "api/security/session/current?bust=" +(new Date()).getTime();
                return httpFactory.get(url);
            }

            function createLogin(login, phone, email) {
                var url = "api/security/login?phone={0}&email={1}".format(phone, email);
                return httpFactory.post(url, login);
            }

            function getLogins(pageable) {
                var url = "api/security/login?page={0}&size={1}".format(pageable.page-1, pageable.size);
                return httpFactory.get(url);
            }

            function getLogin(loginId){

                var url="api/logins/"+loginId;
                return httpFactory.get(url);
            }

            function resetPassword(login) {
                var url = "api/security/login/resetpassword";
                return httpFactory.post(url, login);
            }

            function updateLogin(login){
                var url="api/logins/"+login.id;
                return httpFactory.put(url,login);
            }

            function changePassword(oldPassword, newPassword) {
                var url = "api/security/login/changepassword?oldPassword={0}&newPassword={1}".
                        format(oldPassword, newPassword);
                return httpFactory.get(url);
            }
        }
    }
);