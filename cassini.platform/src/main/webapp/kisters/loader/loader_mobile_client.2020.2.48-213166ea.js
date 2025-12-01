var _KISTERSWebViewerHideFullClient = true;
if (!('contains' in Array.prototype)) {
    Array.prototype['contains'] = function (child, startIndex) {
        return ''.indexOf.call(this, child, startIndex) !== -1;
    };
}

var vsClient = new VSWebClient()
var requestSVG = $.ajax({url:'svg-icons' + _WVVersion + '.xml', cache: true});
var requestCustomSVG = $.ajax({ url: 'custom-svg-icons.xml', cache: false });
var hasTouch = Modernizr.touchevents;

$.when.apply($, [requestSVG, requestCustomSVG]).done(function (svgDeferred, customDeferred) {
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
    loadApp.src = 'dist/webviewer-mobile-bundle.js';
})