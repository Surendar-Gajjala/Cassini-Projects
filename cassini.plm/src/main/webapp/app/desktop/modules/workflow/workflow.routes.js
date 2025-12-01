define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.workflow': {
                    url: '/workflow',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/workflow/workflowMainView.jsp',
                    controller: 'WorkflowMainController as wfMainVm',
                    resolve: ['app/desktop/modules/workflow/workflowMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.workflow.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/workflow/all/workflowsView.jsp',
                    controller: 'WorkflowsController as workflowsVm',
                    resolve: ['app/desktop/modules/workflow/all/workflowsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.workflow.instances': {
                    url: '/instances',
                    templateUrl: 'app/desktop/modules/workflow/instances/workflowInstancesView.jsp',
                    controller: 'WorkflowInstancesController as workflowInstancesVm',
                    resolve: ['app/desktop/modules/workflow/instances/workflowInstancesController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.workflow.editor': {
                    url: '/editor?mode?workflow',
                    templateUrl: 'app/desktop/modules/workflow/editor/workflowEditorView.jsp',
                    controller: 'WorkflowEditorController as wfEditorVm',
                    resolve: ['app/desktop/modules/workflow/editor/workflowEditorController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.workflow.viewer': {
                    url: '/viewer',
                    templateUrl: 'app/desktop/modules/workflow/viewer/workflowViewerView.jsp',
                    controller: 'WorkflowViewerController as wfViewerVm',
                    resolve: ['app/desktop/modules/workflow/viewer/workflowViewerController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);