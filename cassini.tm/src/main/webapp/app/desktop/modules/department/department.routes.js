
/**
 * Created by Anusha on 11-07-2016.
 */
define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.department': {
                    url: '/department',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/department/departmentMainView.jsp',
                    controller: 'DepartmentMainController as departmentMainVm',
                    resolve: ['app/desktop/modules/department/departmentMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.department.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/department/all/allDepartmentsView.jsp',
                    controller: 'AllDepartmentsController as allDepartmentsVm',
                    resolve: ['app/desktop/modules/department/all/allDepartmentsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.department.details': {
                    url: '/:departmentId',
                    templateUrl: 'app/desktop/modules/department/details/departmentDetailsView.jsp',
                    controller: 'DepartmentsDetailsController as departmentsDetailsVm',
                    resolve: ['app/desktop/modules/department/details/departmentDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.department.new': {
                    url: '/new',
                    templateUrl: 'app/desktop/modules/department/new/NewDepartmentView.jsp',
                    controller: 'NewDepartmentController newDepartmentVm',
                    resolve: ['app/desktop/modules/department/new/newDepartmentController'],
                    css: cssConfig.getViewCss('app')
                },

            }
        };
    }
);