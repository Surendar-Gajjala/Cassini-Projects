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
                'app.complaints': {
                    url: '/complaints',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/complaint/complaintsView.jsp',
                    controller: 'ComplaintsController as compsVm',
                    resolve: ['app/desktop/modules/complaint/complaintsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.complaints.all': {
                    url: '/complaints/all',
                    templateUrl: 'app/desktop/modules/complaint/all/allComplaintsView.jsp',
                    controller: 'AllComplaintsController as allCompsVm',
                    resolve: ['app/desktop/modules/complaint/all/allComplaintsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.complaints.details': {
                    url: '/details/:complaintId',
                    templateUrl: 'app/desktop/modules/complaint/details/complaintDetailsView.jsp',
                    controller: 'ComplaintDetailsController as detailsVm',
                    resolve: ['app/desktop/modules/complaint/details/complaintDetailsController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);