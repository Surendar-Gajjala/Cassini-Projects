//Define badges for quick access to the panes on the left side
let _MobileBadgesLeft = ['wv-modeltree'];
let _MobileLeftMoreBadge = [];


//Define badges for quick access to the panes on the right side
let _MobileRightMoreBadge = [];

//Define the number of files loaded per page
let _MobileFilesPerPage = 50;

//Define functions for the radial menu
let _MobileRadialMenuEntry = [
    {
        iconId: '3_isolieren',
        langId: 'ID_RIBBON_ISOLATE',
        function: function (client)
        {
            let xml = '<Call Method="ModifySelection"><SelectionModifier>Isolate</SelectionModifier></Call>';
            client.command.executeXML(xml);
        }
    },
    {
        iconId: '3_verbergen',
        langId: 'ID_RIBBON_HIDE',
        function: function (client)
        {
            let xml = '<Call Method="ModifySelection"><SelectionModifier>Hide</SelectionModifier></Call>';
            client.command.executeXML(xml);
        }
    },
    {
        iconId: '1_alles_anzeigen',
        langId: 'ID_RIBBON_SHOW_ALL',
        function: function (client)
        {
            let xml = '<Call Method="ShowAll"></Call>';
            client.command.executeXML(xml);
        }
    },
    {
        iconId: '3_markups_verbergen',
        langId: 'ID_RIBBON_HIDE_ALL_MARKUPS',
        function: function (client)
        {
            let xml = '<Call Method="HideAllMarkups"></Call>';
            client.command.executeXML(xml);
        }
    },
    {
        iconId: '4_focus',
        langId: 'ID_RIBBON_FOCUS',
        function: function (client)
        {
            let xml = '<Call Method="ModifySelection"><SelectionModifier>Focus</SelectionModifier></Call>'
            client.command.executeXML(xml);
        }
    },
    {
        iconId: '6_loeschen',
        langId: 'ID_RIBBON_DELETE',
        function: function (client)
        {
            let xml = '<Call Method="ModifySelection"><SelectionModifier>Delete</SelectionModifier></Call>';
            client.command.executeXML(xml);
        }
    },
    {
        iconId: '8_ghost_ausschalten',
        langId: 'ID_RIBBON_UNGHOST',
        function: function (client)
        {
            let xml = '<Call Method="UnGhostAll"></Call>';
            client.command.executeXML(xml);
        }
    },
    {
        iconId: '7_ghost',
        langId: 'ID_RIBBON_GHOST',
        function: function (client)
        {
            let xml = '<Call Method="ModifySelection"><SelectionModifier>Ghost</SelectionModifier></Call>';
            client.command.executeXML(xml);
        }
    }
];
