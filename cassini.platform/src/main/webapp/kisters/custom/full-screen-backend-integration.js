var queryString = window.location.search.substr(1);

var kvs = queryString.split('&');

var query = {};
for (let kv of kvs)
{
    let [k, v] = kv.split('=');
    query[k] = v;
}

let canvasWrapper = document.getElementById('wv-canvas');
let client = new VSWebClient();
var queryString = window.location.search.substr(1);
var kvs = queryString.split('&');
var query = {};
var newSession = true;
for (let kv of kvs)
{
    let [k, v] = kv.split('=');
    query[k] = v;
}
if (typeof query['session'] !== 'undefined')
{
    client.initOptions.sessionToConnect = query['session'];
    newSession = false;
}

client.initialize(canvasWrapper, 'ws://localhost:8085/ws');

client.on('initialized', () =>
{
    if (newSession)
    {
        client.SessionToken(decodeURIComponent(query['token']));
    }
    else
    {
        client.resize(canvasWrapper.clientWidth, canvasWrapper.clientHeight);
    }
    client.setDefaultMouse(true);
    client.setQuality(100, 100);
});

client.on('OpenFile', () =>
{
    client.resize(canvasWrapper.clientWidth, canvasWrapper.clientHeight);
});

client.on('NewScene3D', () =>
{
    client.resize(canvasWrapper.clientWidth, canvasWrapper.clientHeight);
});

var touchZoomDeactivated = true;
var touchPanDeactivated = true;
var touchRotateDeactivated = false;
var touchDoubleTabDeactivated = true;
var multiSelectDeactivated = true;
//send this calls by backend websocket to activate / deactivate touch features: 
//- touch pinch to zoom
//- touch pan
//- touch rotate
//- double tab fit
//<Call Method="RefreshScene" CustomId="DeactivateTouchZoom"></Call>
//<Call Method="RefreshScene" CustomId="ActivateTouchZoom"></Call>
//<Call Method="RefreshScene" CustomId="DeactivateTouchPan"></Call>
//<Call Method="RefreshScene" CustomId="ActivateTouchPan"></Call>
//<Call Method="RefreshScene" CustomId="DeactivateTouchRotate"></Call>
//<Call Method="RefreshScene" CustomId="ActivateTouchRotate"></Call>
//<Call Method="RefreshScene" CustomId="DeactivateTouchZoomAndPan"></Call>
//<Call Method="RefreshScene" CustomId="ActivateTouchZoomAndPan"></Call>
//<Call Method="RefreshScene" CustomId="DeatcivateDoubleTabFit"></Call>
//<Call Method="RefreshScene" CustomId="ActivateDoubleTabFit"></Call>
client.on('RefreshScene', function (e)
{
    if (e.CustomId == "DeactivateTouchZoom")
    {
        touchZoomDeactivated = true;
    }
    else if (e.CustomId == "ActivateTouchZoom")
    {
        touchZoomDeactivated = false;
    }
    else if (e.CustomId == "DeactivateTouchPan")
    {
        touchPanDeactivated = true;
    }
    else if (e.CustomId == "ActivateTouchPan")
    {
        touchPanDeactivated = false;
    }
    else if (e.CustomId == "DeactivateTouchRotate")
    {
        touchRotateDeactivated = true;
    }
    else if (e.CustomId == "ActivateTouchRotate")
    {
        touchRotateDeactivated = false;
    }
    else if (e.CustomId == "DeactivateTouchZoomAndPan")
    {
        touchZoomDeactivated = true;
        touchPanDeactivated = true;
    }
    else if (e.CustomId == "ActivateTouchZoomAndPan")
    {
        touchZoomDeactivated = false;
        touchPanDeactivated = false;
    }
    else if (e.CustomId == "DeatcivateDoubleTabFit")
    {
        touchDoubleTabDeactivated = true;
    }
    else if (e.CustomId == "ActivateDoubleTabFit")
    {
        touchDoubleTabDeactivated = false;
    }
    else if (e.CustomId == "TouchRotationModeRotate")
    {
        mouseLeftButtonMode = 'Rotate';
    }
    else if (e.CustomId == "TouchRotationModeTurntable")
    {
        mouseLeftButtonMode = 'Turbtable';
    }
    else if (e.CustomId == "ActivateMultiSelect")
    {
        multiSelectDeactivated = false;
    }
    else if (e.CustomId == "DeatcivateMultiSelect")
    {
        multiSelectDeactivated = true;
    }

});


window.onresize = e =>
{
    client.resize(canvasWrapper.clientWidth, canvasWrapper.clientHeight);
};

var mouseX, mouseY;
function handleMouseMove (e)
{
    mouseX = e.pageX;
    mouseY = e.pageY;
}

/*---------------------------------------------------------------------------------------------------------------------*/
/* -------------------------------------Register mouse and touch handlers----------------------------------------------*/
/*---------------------------------------------------------------------------------------------------------------------*/
var mouseActive = false;
var touchActive = false;
var touchGestureActive = false;
var lastGestureDistance = 0;
var lastGestureDelta = 0;
var lastGestureCenterX = 0;
var lastGestureCenterY = 0;
var mouseLeftButtonMode = 'Rotate';
var hasTouchHandlers = false;
var hasClicked = false;


//Workaround to enable tap/double tap and kendo touch events (kendo does not provide the 'touchMove' event)
function stopDefaultMouseEventPropagation (e)
{
    e.preventDefault();
    e.stopPropagation();
}
canvasWrapper.ontouchmove = stopDefaultMouseEventPropagation;
canvasWrapper.onmousedown = function (e)
{
    hasClicked = true;
    mouseActive = true;
};

canvasWrapper.onmouseup = function (e)
{
    mouseActive = false;
};

if (!hasTouchHandlers)
{
    $(canvasWrapper).kendoTouch({
        multiTouch: true,
        enableSwipe: false,
        touchstart: function (e)
        {
            if (mouseActive || (e.event.originalEvent['pointerType'] && e.event.originalEvent['pointerType'] !== 'touch'))
            {
                e.sender.cancel();
            }
            else
            {
                touchActive = true;
            }
        },
        gesturestart: function (e)
        {
            touchGestureActive = true;
            HandleMouseForTouchGestureStart(e);
        },
        gesturechange: function (e)
        {
            HandleMouseForTouchGesture(e);
        },
        gestureend: function (e)
        {
            e.preventDefault();
            HandleMouseForTouchGestureEnd();
        },
        drag: function (e)
        {
            HandleTouchDrag(e);
        },
        dragstart: function (e)
        {
            HandleTouchDragStart(e);
        },
        dragend: function (e)
        {
            HandleTouchDragEnd(e);
            touchActive = false;
            touchGestureActive = false;
        },
        doubletap: function (e)
        {
            e.preventDefault();

            //touchDoubleTab - Fit
            if (!touchDoubleTabDeactivated)
            {
                var s = '<Call Method="FitAll"></Call>';
                client.command.executeXML(s);
            }
        },
        tap: function (e)
        {
            if (hasClicked)
            {
                hasClicked = false;
                return;
            }
            else
            {
                if (!mouseActive)
                {
                    e.preventDefault();
                    let boundingRect = canvasWrapper.getBoundingClientRect();
                    let offsetLeft = boundingRect.left;
                    let offsetTop = boundingRect.top;

                    var mouseX = e.touch.x.location - offsetLeft;
                    var mouseY = e.touch.y.location - offsetTop;

                    var button = 'Left';

                    let xml = '<Call Method="MouseMove" Response="false"><X>';
                    xml += mouseX;
                    xml += '</X><Y>';
                    xml += mouseY;
                    xml += '</Y><MouseButton>None</MouseButton></Call>';;

                    client.command.executeXMLNoMirror(xml);

                    xml = '<Call Method="MouseDown"><X>';
                    xml += mouseX + '</X><Y>';
                    xml += mouseY + '</Y>';
                    xml += '<MouseButton>';
                    xml += button + '</MouseButton></Call>';

                    client.command.executeXMLNoMirror(xml);

                    xml = '<Call Method="MouseUp"><X>';
                    xml += mouseX + '</X><Y>';
                    xml += mouseY + '</Y>';
                    xml += '<MouseButton>';
                    xml += button + '</MouseButton></Call>';

                    client.command.executeXMLNoMirror(xml);
                }
            }
        }
    });
}

/**    touch handlers */
function TouchPosCorrectionX (pos)
{
    let rect = canvasWrapper.getBoundingClientRect();
    var mouseX = pos - rect.left;
    return mouseX;
}

function TouchPosCorrectionY (pos)
{
    let rect = canvasWrapper.getBoundingClientRect();
    var mouseY = pos - rect.top;
    return mouseY;
}

function TouchHoldMove (e)
{
    if (!touchActive)
    {
        e.sender.cancel();
        return;
    }

    var mouseX = TouchPosCorrectionX(e.touch.x.client);
    var mouseY = TouchPosCorrectionY(e.touch.y.client);

    var mouseFlag = '';

    if ((e.event.altKey === true || e.event.ctrlKey === true) & !multiSelectDeactivated)
    {
        mouseFlag = '<MouseFlag>Ctrl</MouseFlag>';
    }

    //mouse move before down to simulate hover
    var s = '<Call Method="MouseMove" Response="true"><X>' + mouseX + '</X><Y>' + mouseY + '</Y><MouseButton>None</MouseButton></Call>';
    if (!holdActive)
    {
        s += '<Call Method="MouseDown" Response="false"><X>' + mouseX + '</X><Y>' + mouseY + '</Y><MouseButton>Left</MouseButton></Call>';
        holdActive = true;
    }
    client.command.executeXMLMouse(s);
}


function HandleMouseForTouchGestureStart (touchevent)
{
    //initialize
    lastGestureDistance = touchevent.distance;

    lastGestureCenterX = TouchPosCorrectionX(touchevent.center.x);
    lastGestureCenterY = TouchPosCorrectionY(touchevent.center.y);
    //activate pan
    var sCommands = '<Call Method="SetMouseButtonFunctions" Response="false"><LeftMouseButtonFunction>Pan</LeftMouseButtonFunction></Call>';
    //down pan
    sCommands += '<Call Method="MouseDown" Response="false"><X>' + lastGestureCenterX + '</X><Y>' + lastGestureCenterY + '</Y><MouseButton>Left</MouseButton></Call>';
    client.command.executeXMLMouse(sCommands);
}

function HandleMouseForTouchGesture (touchevent)
{
    var bSendZoom = false;
    lastGestureDelta = touchevent.distance - lastGestureDistance;

    if (Math.abs(lastGestureDelta) > 10)
    {
        bSendZoom = true;
        lastGestureDistance = touchevent.distance;
    }

    lastGestureCenterX = TouchPosCorrectionX(touchevent.center.x);
    lastGestureCenterY = TouchPosCorrectionY(touchevent.center.y);

    var sMouseButtonDown = '<MouseButton>Left</MouseButton>';
    var callMove = '<Call Method="MouseMove" Response="true"><X>' + lastGestureCenterX + '</X><Y>' + lastGestureCenterY + '</Y>' + sMouseButtonDown + '</Call>';

    if (bSendZoom & !touchZoomDeactivated)
    {
        //touch - Zoom
        client.command.executeXMLZoomMouseWheel(lastGestureCenterX, lastGestureCenterY, lastGestureDelta);
    }
    else if (!touchPanDeactivated)
    {
        //touch - Pan
        client.command.executeXMLMouse(callMove);
    }
}

function HandleMouseForTouchGestureEnd ()
{
    //combined touch pan and zoom
    var sCommands = '<Call Method="MouseUp" Response="false"><X>' + lastGestureCenterX + '</X><Y>' + lastGestureCenterY + '</Y><MouseButton>Left</MouseButton></Call>';
    //reset mouse mode
    sCommands += '<Call Method="SetMouseButtonFunctions" Response="false"><LeftMouseButtonFunction>' + mouseLeftButtonMode + '</LeftMouseButtonFunction></Call>';
    client.command.executeXMLMouse(sCommands);
}

function HandleTouchDragStart (touchevent)
{

    if (!touchGestureActive && !mouseActive)
    {
        touchevent.preventDefault();

        var mouseX = TouchPosCorrectionX(touchevent.touch.x.client);
        var mouseY = TouchPosCorrectionY(touchevent.touch.y.client);

        if (mouseLeftButtonMode === 'RectZoom' || mouseLeftButtonMode == 'RectSelect')
        {
            client.startRect(mouseX, mouseY);
        }

        //mouse move before down to simulate hover
        var s = '<Call Method="MouseMove" Response="true"><X>' + mouseX + '</X><Y>' + mouseY + '</Y><MouseButton>None</MouseButton></Call>';
        s += '<Call Method="MouseDown" Response="false"><X>' + mouseX + '</X><Y>' + mouseY + '</Y><MouseButton>Left</MouseButton></Call>';
        client.command.executeXMLMouse(s);
    }
}

function HandleTouchDrag (touchevent)
{
    if (!touchGestureActive && !mouseActive)
    {
        touchevent.preventDefault();

        var mouseX = TouchPosCorrectionX(touchevent.touch.x.client);
        var mouseY = TouchPosCorrectionY(touchevent.touch.y.client);

        var sMouseButtonDown = '<MouseButton>Left</MouseButton>';
        var callMove = '<Call Method="MouseMove" Response="true"><X>' + mouseX + '</X><Y>' + mouseY + '</Y>' + sMouseButtonDown + '</Call>';

        if ((mouseLeftButtonMode === 'RectZoom' || mouseLeftButtonMode == 'RectSelect') && drawRect
            && interactionMode.indexOf('Flythrough') === -1)
        {
            client.drawRect(mouseX, mouseY);
        }
        else if (!touchRotateDeactivated)
        {
            //touch - Rotate
            client.command.executeXMLMouseMove(callMove);
        }
    }
}

function HandleTouchDragEnd (touchevent)
{
    if (!touchGestureActive && !mouseActive)
    {
        touchevent.preventDefault();
        var mouseX = TouchPosCorrectionX(touchevent.touch.x.client);
        var mouseY = TouchPosCorrectionY(touchevent.touch.y.client);

        var xml = '<Call Method="MouseUp" Response="false"><X>' + mouseX + '</X><Y>' + mouseY + '</Y><MouseButton>Left</MouseButton></Call>';
        client.command.executeXMLMouse(xml);
    }
}
