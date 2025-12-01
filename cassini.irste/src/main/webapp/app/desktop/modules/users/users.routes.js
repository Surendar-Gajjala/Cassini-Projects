/**
 * Created by Nageshreddy on 08-11-2018.
 */
define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.users': {
                    url: '/users',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/users/usersView.jsp',
                    controller: 'UsersController as usersVm',
                    resolve: ['app/desktop/modules/users/usersController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.users.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/users/all/allUsersView.jsp',
                    controller: 'AllUsersController as allUsersVm',
                    resolve: ['app/desktop/modules/users/all/allUsersController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);