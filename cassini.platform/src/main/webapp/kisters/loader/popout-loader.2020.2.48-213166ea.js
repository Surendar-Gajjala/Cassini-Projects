var requestSVG = $.ajax({url:'svg-icons.xml', cache:false});
var requestCustomSVG = $.ajax({ url: 'custom-svg-icons.xml', cache: false });

$.when.apply($, [requestSVG, requestCustomSVG]).done(function (svgDeferred, customDeferred) {
    let svgContainer = document.createElement('div');
    svgContainer.classList.add('wv-svgs');
    svgContainer.innerHTML = svgDeferred[2].responseText;
    document.body.appendChild(svgContainer);

    let svgCustomContainer = document.createElement('div');
    svgCustomContainer.classList.add('wv-custom-svgs');
    svgCustomContainer.innerHTML = customDeferred[2].responseText;
    document.body.appendChild(svgCustomContainer);
})