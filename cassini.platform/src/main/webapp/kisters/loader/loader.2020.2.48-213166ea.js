
if (!('contains' in Array.prototype)) {
    Array.prototype['contains'] = function (child, startIndex) {
        return ''.indexOf.call(this, child, startIndex) !== -1;
    };
}

var vsClient = new VSWebClient()
var requestSVG = $.get({url:'svg-icons' + _WVVersion + '.xml', cache: true });
var requestCustomSVG = $.get({ url: 'custom-svg-icons.xml', cache: false });
var hasTouch = Modernizr.touchevents;

function useMobileUI()
{
    if (localStorage.getItem('WVUseMobile') === null)
    {
        return hasTouch;
    }
    else
    {
        return localStorage.getItem('WVUseMobile') === 'true';
    }
}

$.when.apply($, [requestSVG, requestCustomSVG]).done(function (svgDeferred, customDeferred)
{
    let svgContainer = document.createElement('div');
    svgContainer.classList.add('wv-svgs');
    svgContainer.innerHTML = svgDeferred[2].responseText;
    document.body.appendChild(svgContainer);

    let svgCustomContainer = document.createElement('div');
    svgCustomContainer.classList.add('wv-custom-svgs');
    svgCustomContainer.innerHTML = customDeferred[2].responseText;
    document.body.appendChild(svgCustomContainer);

    var loadApp = document.createElement('script');
    document.body.appendChild(loadApp);
    if (!useMobileUI()) {
        loadApp.src = 'dist/webviewer-bundle.2020.2.48-213166ea.js';
    } else { // load compact UI
        loadApp.src = 'dist/webviewer-mobile-bundle.2020.2.48-213166ea.js';
    }
})