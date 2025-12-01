/*  Widget Types:
        Button = 1
        ButtonLet = 2
        Dropdown = 3
        Input = 4
*/
var UserConfiguration = {
    langId: 'Custom Tab',
    groups: [{
        langId: 'Demo Button',
        widgets: [
            { type: 1, langId: 'ID_RIBBON_DEACTIVATE_ALL', iconId: 'deactivate_all' }
        ],
        groupClick: function (context, id, group) {
            var client = context.client;
            var ribbon = context.ribbon;
            let xml ='<Call Method="ActivateInteraction"><Interaction>Deactivate_AllInteractions</Interaction></Call>'
            client.command.executeXMLAsync(xml)
        }
    },
    {
        langId: 'Demo Buttonlet',
        widgets: [
            { type: 2, langId: 'ID_RIBBON_CUTTING_PLANE_FROM_POINT_NORMAL', iconId: '1_1_schnittebene_aus_punkt', ribbonClass: ['Interaction'], interaction: 'Section_FromPointNormal' },
            { type: 2, langId: 'ID_RIBBON_DRAFT_ANGLE', iconId: '1_entformungsschraegen_analyse', ribbonClass: ['Interaction'], interaction: 'DraftAngle' }
        ],
        groupClick: function (context, id, group) {
            var client = context.client
            let ribbon = context.ribbon
            let clickedButton = group.getButtonById(id);
            let mode
            if(clickedButton.langId === 'ID_RIBBON_CUTTING_PLANE_FROM_POINT_NORMAL')
            {
                mode = 'Section_FromPointNormal'
            }
            else
            {
                mode = 'DraftAngle'
            }
            clickedButton.event = mode
            context.menu.handleActivateInteraction(client, mode)
        }
    }
	]
};


function generateUUID()
{
    var d = new Date().getTime();
    if (typeof performance !== 'undefined' && typeof performance.now === 'function')
    {
        d += performance.now();
    }
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c)
    {
        var r = (d + Math.random() * 16) % 16 | 0;
        d = Math.floor(d / 16);
        return (c === 'x' ? r : (r & 0x3 | 0x8)).toString(16);
    });
}