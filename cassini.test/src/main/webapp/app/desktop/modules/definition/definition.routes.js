/**
 * Created by SRAVAN on 7/30/2018.
 */
define(
    [
        'app/desktop/desktop.css'
    ],
    function(cssConfig){
        return{
            routes:{
                'app.definition':{
                    url:'/definition',
                    templateUrl:'app/desktop/modules/definition/definitionMainView.jsp',
                    controller: 'DefinitionMainController as definitionMainVm',
                    resolve: ['app/desktop/modules/definition/definitionMainController'],
                    css: cssConfig.getViewCss('app')
                },
            'app.definitions.newScript': {
                url: '/new',
                templateUrl: 'app/desktop/modules/definition/new/newScriptView.jsp',
                controller: 'NewScriptController as newScriptVm',
                resolve: ['app/desktop/modules/definition/new/newScriptController'],
                css: cssConfig.getViewCss('app')
            }
            }
        };
    }
);

