//Ribbon menu visibility and mobile menu
//CAUTION: Mobile menu has less group IDs than the full UI ribbon
//The examples hide the buttons/groups that are only available with the specific package
//Enable a block to hide the ribbon buttons only available with this licence feature

//Disable/Enable ribbon functions (single buttons)
var _functions = {
    //Convex hull is available with IP-Protection and Advanced analysis
    //"ID_RIBBON_CONVEX_HULL": false,
};

//Disable/Enable ribbon groups
var _RibbonGroups = {
    //Block: Advanced analysis
    //"ID_RIBBON_ADVANCED": false
    //Block: IP-Protection
    /*
    "ID_RIBBON_IP_PROTECTION": false,
    */
    //Block: Feature recognition
    /*
    "ID_RIBBON_FEATURE_RECOGNITION": false,
    */
    //Block: TechDoc
    /*
    "ID_RIBBON_CALLOUTS_GENERAL_PANEL": false,
    "ID_RIBBON_CALLOUTS_ALIGNMENT_PANEL": false,
    "ID_RIBBON_CALLOUTS_PANEL": false,
    "ID_RIBBON_CALLOUTS_CONNECTIONS_PANEL": false,
    "ID_RIBBON_VISIBLE_CALLOUTS_PANEL": false,
    "_15": false
    */
};

//Tabs
//Disable/Enable ribbon tabs
//This example does not represent a package (it will just hide the document tab as an example)
var _Tabs = {
    //"ID_RIBBON_DOCUMENT": false
};

//pane visibility on startup
var _InitialPanes = [
		{paneId: 'wv-modeltree', pos:1, minimized: false, visible: true },
		{ paneId: 'wv-views', pos: 1, minimized: false, visible: true },
		{ paneId: 'wv-properties', pos: 2, minimized: false, visible: true }
];


//Array containing the IDs of panes that should never be displayed
var _DisabledPanes = [];

//sample hiding the markup pane
//var _DisabledPanes = ['wv-markup'];

//Flag whether the pane layout can be manipulated
//Locking the panes prohibits pane resizing and position memory
//The panes are still dockable and closable
var _PaneLock = false;

//Flag whether pane popouts are allowed or not
var _PopoutLock = false;


//False sets initial Ribbon visibility state to collapsed (expands on mouse over)
var _RibbonMenuVisibility = true;

//Use cached settings from browser local storage (allows user specific settings)
var _UseCachedSettings = false;


//Reduced 2D set of functions (list tab/group/function IDs to hide the corresponding UI elements for 2D files)
var _2DReducedSet = ['ID_RIBBON_ALIGNMENT', 'ID_RIBBON_SELECTION', 'ID_RIBBON_NEIGHBOUR_HOOD', 'ID_RIBBON_COLOR', 'ID_RIBBON_NEUTRAL_AXIS',
    'ID_RIBBON_RENDERING_MODE', 'ID_RIBBON_GRAPHIC_EFFECTS_1', 'ID_RIBBON_FLYTHROUGH', 'ID_RIBBON_THICKNESS', 'ID_RIBBON_BOUNDING_BOX',
    'ID_RIBBON_FILTER', 'ID_RIBBON_RETESSELATE', 'ID_RIBBON_COORDINATE_SYSTEM', 'User', 'ID_RIBBON_ROTATE_2', 'ID_RIBBON_ANALYZE', 'ID_RIBBON_GRID',
    'ID_RIBBON_IP_PROTECTION', 'ID_RIBBON_EXTRACT', 'ID_RIBBON_TRANSFORMATION', 'ID_RIBBON_CUTTING_PLANE_FROM_POINT_NORMAL', 'ID_RIBBON_CUTTING_PLANE',
    'ID_PHYSICAL_PROPERTIES', 'ID_RIBBON_SHOW_GRID', 'ID_RIBBON_DISTORT', 'ID_VISIBILITY_TEST', 'ID_RIBBON_EXTRACT_FACES', 'ID_RIBBON_CONVEX_HULL',
    'ID_RIBBON_TECH_DOC', 'ID_RIBBON_BILL_OF_MATERIAL', 'ID_RIBBON_STATISTICS', 'ID_RIBBON_OPTIMIZE', 'ID_RIBBON_AREA_1', 'ID_RIBBON_WALL_THICKNESS',
    'ID_RIBBON_DRILLHOLE', 'ID_RIBBON_FOCUS', 'ID_RIBBON_SET_ORIGIN', 'ID_RIBBON_CONTROLS', 'ID_RIBBON_SELECTION_MODE'];

//Reduced 3D set of functions (list tab/group/function IDs to hide the corresponding UI elements for 2D files)
var _3DReducedSet = ['ID_RIBBON_DOCUMENT'];


//Show error alerts
var _ShowErrorAlerts = true;

//Define default touch behaviour for mobile, default is rotate
var _MobileTouchControl = 'Rotate';

//Define the amount of files/folders loaded and shown per page in the file browser; default 100
var _FileBrowserPageSize = 100;

//Define default scene name to hide the file name from the tab and the master root node of the structure tree
//Comment the line out to show the file name as scene and root name
//var _SceneNameOverride = '/';

//Change the default behaviour for license errors (redirect to error page).
//Set the value to 0  or comment the line out -> redirect in same window
//Set the value to 1 -> open new window with error page
//Set the value to 2 -> disable the license error handling
var _AlternateLicenseErrorHandling = 0;

//Close active panes on opening a new file
var _ClosePanesOnOpenFile = true;

//Hide UI commands from command pane
var _HideUICommandLog = false;

//Print paper sizes
var _PrintPaperSizes = [
    { langId: "-", width: -1, height: -1, dpi: 600 },
    { langId: "A0", width: 841, height: 1189, dpi: 200 },
    { langId: "A1", width: 594, height: 841, dpi: 300 },
    { langId: "A2", width: 420, height: 594, dpi: 400 },
    { langId: "A3", width: 297, height: 420, dpi: 500 },
    { langId: "A4", width: 210, height: 297, dpi: 600 },
    { langId: "A5", width: 148, height: 210, dpi: 600 },
    { langId: "Letter", width: 216, height: 279, dpi: 600 },
    { langId: "Legal", width: 216, height: 256, dpi: 600 },
    { langId: "Ledger", width: 279, height: 432, dpi: 500 }
];


//Hide open file on URL open
var _ShowFileRibbonOnUrlOpen = false;

//Custom keyboard shortcuts (keyup) (Full Ribbon UI Only) and this will override the WebViewer defaults
//Attributes as follows:
//key: the actual key for the shortcut(the KeyCode as an integer is used to identify the exact key) see https://keycode.info/
//{
//ctrl: flag if ctrl must be pressed
//alt: flag if alt must be pressed
//shift: flag if shift must be pressed
//call: a XML call that should be send by pressing the key (can be set to undefined if no call should be send)
//paneId: the paneId of a pane that should be toggled (can be set to undefined if no pane should be toggled)
//ribbonKey: clicks the ribbon key (by the HTML elements ID), this will override call and paneId
//method: executes the defined JavaScript method. Can be set to undefined and will override the the other options.
//        The current VSWebClient object is passed to the method and can be used to send XML calls or listen to events / responses
//}
var _ShortCuts = {
    67://Alt-C-key to copy a mocked clicked in XML API comamnds (mouse move-down-up) on the current mouse position to the clipboard
    {
        ctrl: false,
        alt: true,
        shift: false,
        call: undefined,
        paneId: undefined,
        ribbonKey: undefined,
        method: function (client)
        {
            let moveCall = '<Call Method="MouseMove"><X>' + client.mouseX + '</X><Y>' + client.mouseY + '</Y><MouseButton>None</MouseButton></Call>';
            let downCall = '<Call Method="MouseDown"><X>' + client.mouseX + '</X><Y>' + client.mouseY + '</Y><MouseButton>Left</MouseButton></Call>';
            let upCall = '<Call Method="MouseUp"><X>' + client.mouseX + '</X><Y>' + client.mouseY + '</Y><MouseButton>Left</MouseButton></Call>';
            let hiddenText = document.createElement('textarea');
            hiddenText.style.position = 'fixed';
            hiddenText.style.top = '0';
            hiddenText.style.left = '0';
            hiddenText.style.width = '2em';
            hiddenText.style.height = '2em';
            hiddenText.style.padding = '0';
            hiddenText.style.border = 'none';
            hiddenText.style.outline = 'none';
            hiddenText.style.boxShadow = 'none';
            hiddenText.style.background = 'transparent';
            hiddenText.style.color = 'transparent';
            hiddenText.style.overflow = 'hidden';
            hiddenText.value = moveCall + '\n' + downCall + '\n' + upCall;
            document.body.appendChild(hiddenText);
            hiddenText.focus();
            hiddenText.select();
            try
            {
                document.execCommand('copy');
            } catch (err)
            {
            }
            document.body.removeChild(hiddenText);

        }
    },
    79://Alt-O-key to reload the last loaded file
    {
        ctrl: false,
        alt: true,
        shift: false,
        call: undefined,
        paneId: undefined,
        ribbonKey: undefined,
        method: function (client)
        {
            client.OpenFile(client.lastFileOpened);
        }
    },
    82://Alt-R-key to resize Scene to full HD
    {
        ctrl: false,
        alt: true,
        shift: false,
        call: '<Call Method="Resize"><Width>1920</Width><Height>1080</Height></Call>',
        paneId: undefined,
        ribbonKey: undefined
    }
};
/*var _ShortCuts = {
    70://F-key
    {
        ctrl: false,
        alt: false,
        shift: false,
        call: '<Call Method="ActivateInteraction"><Interaction>Transformation_FreeDrag</Interaction></Call>',
        paneId: undefined,
        ribbonKey: undefined,
        method: function (client)
        {
            client.command.executeXML('<Call Method="ModifySelection"><SelectionModifier>Ghost</SelectionModifier></Call>');
            client.once('ModifySelection', res =>
            {
                console.log(res)
            })
        }
    },
    65://A-key
    {
        ctrl: true,
        alt: false,
        shift: false,
        call: '<Call Method="ModifySelection"><SelectionModifier>Ghost</SelectionModifier></Call>',
        paneId: 'wv-distortion',
        ribbonKey: undefined
    },
    80://P-key
    {
        ctrl: true,
        alt: false,
        shift: false,
        call: undefined,
        paneId: undefined,
        ribbonKey: 'ID_RIBBON_PRINT'
    }
};
*/

//About dialog links
var _AboutLinks = [
    "https://viewer.kisters.de/en/kisters-products/3dviewstation-webviewer.html",
    "https://viewer.kisters.de/help/webviewer",
    "http://viewer.kisters.de/en/news/news.html",
    "https://viewer.kisters.de/en/blog.html",
    "https://www.youtube.com/user/KistersViewer",
    "https://twitter.com/KistersViewer",
    "https://www.facebook.com/KistersViewer"
];

//User the offline version for the context sensitive help
var _UseOfflineHelp = false;

//List of ignored errors and warnings
var _IgnoredErrors = ['ERR_GRAPHICS_DRIVER_IS_IN_USE'];

//Ask for confirmation on page leave
var _HidePageLeaveWarning = true;

//UI color definitions 
//var _AppMainColor = '#D67C1C';
//var _AppRibbonHeaderTextColor = 'white';

//var _AppMainColor = '#E1E1E1';
//var _AppRibbonHeaderTextColor = 'black';

//Color palettes
var _ColorPalette = [
    ['#000000', '#191919', '#323232', '#4B4B4B', '#646464'],
    ['#7D7D7D', '#9B9B9B', '#B4B4B4', '#CDCDCD', '#E6E6E6'],
    ['#800000', '#FF0000', '#808000', '#FFFF00', '#008000'],
    ['#00FF00', '#008080', '#00FFFF', '#000080', '#0000FF'],
    ['#800080', '#FF00FF', '#4682B4', '#F07E09', '#FFFFFF']
];

var _ColorPaletteWithAlpha = [
    ['#FF000000', '#FF191919', '#FF323232', '#FF4B4B4B', '#FF646464'],
    ['#FF7D7D7D', '#FF9B9B9B', '#FFB4B4B4', '#FFCDCDCD', '#FFE6E6E6'],
    ['#FF800000', '#FFFF0000', '#FF808000', '#FFFFFF00', '#FF008000'],
    ['#FF00FF00', '#FF008080', '#FF00FFFF', '#FF000080', '#FF0000FF'],
    ['#FF800080', '#FFFF00FF', '#FF4682B4', '#FFF07E09', '#FFFFFFFF']
];


//Hide open print PDF in browser button
var _BrowserPrintButtonDeactivated = false;

//Disable the dialog to restore sessions after a browser crash
var _DisableSessionRestore = true;


/*----------------------------------------------------------------------------------------------------------------------------------------------*/

//Native loading settings

//Show loading options slider dialog for native CAD files  
var _NativeLoadingModal = true

//Hide the switch to decide between 2D or 3D prefered loading
var _Hide2DSwitch = false;


//Import dialog presets
var _IncludeHiddenNativeLoading = '<Settings Version="2.110.0"><Import3D><General><TessellationLOD>ExtraHigh</TessellationLOD><ReadMode>PreferGeometry</ReadMode></General><Filter>'
        + '<ReadSolids>true</ReadSolids><ReadSurfaces>true</ReadSurfaces><ReadWireframes>true</ReadWireframes><ReadPMI>true</ReadPMI>'
        + '<ReadAttributes>true</ReadAttributes><ReadHiddenStructure>true</ReadHiddenStructure><ReadHiddenGeometry>true</ReadHiddenGeometry><ReadViews>true</ReadViews><Extended>'
        + '<LoadDefaultConfiguration>false</LoadDefaultConfiguration><ReadStructureOnly>false</ReadStructureOnly></Extended></Filter><Miscellaneous>'
        + '<WireGeneration><DeleteOldWiresOnLoad>false</DeleteOldWiresOnLoad><GenerateWiresOnLoad>true</GenerateWiresOnLoad></WireGeneration>'
        + '</Miscellaneous><JT><TessellationLOD>High</TessellationLOD></JT><PDF><ForceRead3D>false</ForceRead3D></PDF><Views><IgnoreViews>false</IgnoreViews>'
		+ '<IgnoreAnnotationViews>false</IgnoreAnnotationViews></Views></Import3D></Settings>';

var _CompleteNativeLoading = '<Settings Version="2.110.0"><Import3D><General><ReadMode>PreferGeometry</ReadMode><TessellationLOD>High</TessellationLOD></General><Filter>'
        + '<ReadSolids>true</ReadSolids><ReadSurfaces>true</ReadSurfaces><ReadWireframes>true</ReadWireframes><ReadPMI>true</ReadPMI>'
        + '<ReadAttributes>true</ReadAttributes><ReadHiddenStructure>false</ReadHiddenStructure><ReadHiddenGeometry>false</ReadHiddenGeometry><ReadViews>true</ReadViews><Extended>'
        + '<LoadDefaultConfiguration>false</LoadDefaultConfiguration><ReadStructureOnly>false</ReadStructureOnly></Extended></Filter><Miscellaneous>'
        + '<WireGeneration><DeleteOldWiresOnLoad>false</DeleteOldWiresOnLoad><GenerateWiresOnLoad>false</GenerateWiresOnLoad></WireGeneration>'
        + '</Miscellaneous><JT><TessellationLOD>Medium</TessellationLOD></JT><PDF><ForceRead3D>false</ForceRead3D></PDF><Views><IgnoreViews>false</IgnoreViews>'
		+ '<IgnoreAnnotationViews>false</IgnoreAnnotationViews></Views></Import3D></Settings>';

var _ModerateNativeLoading = '<Settings Version="2.110.0"><Import3D><General><ReadMode>PreferTessellation</ReadMode><TessellationLOD>Medium</TessellationLOD></General>'
        + '<Filter><ReadSolids>true</ReadSolids><ReadSurfaces>true</ReadSurfaces><ReadWireframes>true</ReadWireframes><ReadPMI>true</ReadPMI>'
        + '<ReadAttributes>true</ReadAttributes><ReadHiddenStructure>false</ReadHiddenStructure><ReadHiddenGeometry>false</ReadHiddenGeometry><ReadViews>true</ReadViews><Extended>'
        + '<LoadDefaultConfiguration>false</LoadDefaultConfiguration><ReadStructureOnly>false</ReadStructureOnly></Extended></Filter><Miscellaneous>'
        + '<WireGeneration><DeleteOldWiresOnLoad>false</DeleteOldWiresOnLoad><GenerateWiresOnLoad>false</GenerateWiresOnLoad></WireGeneration>'
        + '</Miscellaneous><JT><TessellationLOD>Medium</TessellationLOD></JT><PDF><ForceRead3D>true</ForceRead3D></PDF><Views><IgnoreViews>false</IgnoreViews>'
		+ '<IgnoreAnnotationViews>true</IgnoreAnnotationViews></Views></Import3D></Settings>';

var _FastNativeLoading = '<Settings Version="2.110.0"><Import3D><General><ReadMode>PreferTessellation</ReadMode><TessellationLOD>Low</TessellationLOD></General>'
        + '<Filter><ReadSolids>true</ReadSolids><ReadSurfaces>true</ReadSurfaces><ReadWireframes>false</ReadWireframes><ReadPMI>false</ReadPMI>'
        + '<ReadAttributes>false</ReadAttributes><ReadHiddenStructure>false</ReadHiddenStructure><ReadHiddenGeometry>false</ReadHiddenGeometry><ReadViews>false</ReadViews><Extended>'
        + '<LoadDefaultConfiguration>true</LoadDefaultConfiguration><ReadStructureOnly>false</ReadStructureOnly></Extended></Filter><Miscellaneous>'
        + '<WireGeneration><DeleteOldWiresOnLoad>false</DeleteOldWiresOnLoad><GenerateWiresOnLoad>false</GenerateWiresOnLoad></WireGeneration>'
        + '</Miscellaneous><JT><TessellationLOD>Low</TessellationLOD></JT><PDF><ForceRead3D>true</ForceRead3D></PDF><Views><IgnoreViews>true</IgnoreViews>'
		+ '<IgnoreAnnotationViews>true</IgnoreAnnotationViews></Views></Import3D></Settings>';
/*----------------------------------------------------------------------------------------------------------------------------------------------*/



//Mobile Settings

//Define badges for quick access to the panes on the left side
let _MobileBadgesLeft = ['wv-modeltree', 'wv-views'];
let _MobileLeftMoreBadge = ['wv-properties', 'wv-settings', 'wv-selections'];


//Define badges for quick access to the panes on the right side
let _MobileRightMoreBadge = ['wv-distortion', 'wv-visibilitytest', 'wv-textsearch'];

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


