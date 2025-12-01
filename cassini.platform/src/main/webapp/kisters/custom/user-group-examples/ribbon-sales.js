/*
EXAMPLE ribbon for the user group salces (see user-group-index.html)
*/

var _functions = {
    'Command': false
};

//Hide analyze and measurement tab
var _Tabs = {
    "ID_RIBBON_ANALYZE": false,
    "ID_RIBBON_MEASUREMENT": false
};

//pane visibility on startup
var _InitialPanes = [
		{paneId: 'wv-modeltree', pos:1, minimized: false, visible: true },
		{ paneId: 'wv-views', pos: 1, minimized: false, visible: true },
		{ paneId: 'wv-properties', pos: 2, minimized: false, visible: true }
];


// Array containing the pane idx of panes that should never be displayed
var _DisabledPanes = [];

//sample hiding the markup pane
//var _DisabledPanes = ['wv-markup'];

//Flag whether the pane layout can be manipulated
//Locking the panes prohibits pane resizing and position memory
//The panes are still dockable and closable
var _PaneLock = false;

//Flag whether pane popouts are allowed or not
var _PopoutLock = false;


//ribbon
var _RibbonMenuVisibility = true;

//colapse ribbon initially
//var _RibbonMenuVisibility = false

//Use cached settings from the local storage
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
//Comment the line out to show the file name as scene and root names
//var _SceneNameOverride = '/';

//Change the default behaviour for license errors (redirect to error page).
//Set the value to 0  or comment the line out -> redirect in same window
//Set the value to 1 -> open new window with error page
//Set the value to 2 -> disable the license error handling
//var _AlternateLicenseErrorHandling = 1;

//Close panes on opening a new file
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
//}
/*var _ShortCuts = {
    70://F-key
    {
        ctrl: false,
        alt: false,
        shift: false,
        call: '<Call Method="ActivateInteraction"><Interaction>Transformation_FreeDrag</Interaction></Call>',
        paneId: undefined,
        ribbonKey: undefined
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
};*/