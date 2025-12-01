define(
    [
        'app/phone/phone.css'
    ],
    function(cssConfig) {
        return {
            routes: {
                'app.dept': {
                    url: '/dept',
                    abstract: true,
                    templateUrl: 'app/phone/modules/dept/deptMainView.jsp',
                    controller: 'DeptMainController as deptMainVm',
                    resolve: ['app/phone/modules/dept/deptMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.dept.all': {
                    url: '/all',
                    templateUrl: 'app/phone/modules/dept/all/allDeptsView.jsp',
                    controller: 'AllDeptsController as allDeptsVm',
                    resolve: ['app/phone/modules/dept/all/allDeptsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.dept.details': {
                    url: '/details/:deptId',
                    templateUrl: 'app/phone/modules/dept/details/deptDetailsView.jsp',
                    controller: 'DeptDetailsController as deptDetailsVm',
                    resolve: ['app/phone/modules/dept/details/deptDetailsController'],
                    css: cssConfig.getViewCss('app')
                },

                'app.dept.new': {
                    url: '/new',
                    templateUrl: 'app/phone/modules/dept/new/newDeptView.jsp',
                    controller: 'NewDeptController newDeptVm',
                    resolve: ['app/phone/modules/dept/new/newDeptController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);