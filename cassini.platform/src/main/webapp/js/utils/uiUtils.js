var appNotification = null;

function initNotificationPanel() {
    appNotification = $('#appNotification');
    $(window).resize(positionNotification);

    $(document).on('keydown', function (evt) {
        if (evt.keyCode == 27) {
            hideNotification();
        }
    });
}

function initPreloader() {
}

function positionNotification() {
    var pos = $('#headerbar').position();
    if (pos != null) {
        var height = $('#headerbar').outerHeight();
        var width = $('#workspace').outerWidth();
        var h = $('#viewTitleContainer').outerHeight();

        var left = pos.left;
        var top = pos.top + height;
        if ($application.selectedProject != undefined && $application.selectedProject.locked == true) {
            top = top + 30;
        } else {
            top = pos.top + height;
        }
        var projNav = document.getElementById('project-headerbar');
        if (projNav != null && projNav != undefined) {
            top = top + 50;
        }

        appNotification.css({top: top, left: left, width: width, height: h, position: 'absolute'});
    }
}


function showNotification() {
    positionNotification();
    appNotification.show();
    appNotification.removeClass('zoomOut');
    appNotification.addClass('zoomIn');
    setTimeout(function() {
        hideNotification();
    }, 5000);
}

function hideNotification() {
    positionNotification();
    appNotification.removeClass('zoomIn');
    appNotification.addClass('zoomOut');
    appNotification.hide();
}

function launchUrl(url) {
    var w = 1;
    var h = 1;
    // Fixes dual-screen position                         Most browsers      Firefox
    var dualScreenLeft = window.screenLeft != undefined ? window.screenLeft : window.screenX;
    var dualScreenTop = window.screenTop != undefined ? window.screenTop : window.screenY;

    var width = window.innerWidth ? window.innerWidth : document.documentElement.clientWidth ? document.documentElement.clientWidth : screen.width;
    var height = window.innerHeight ? window.innerHeight : document.documentElement.clientHeight ? document.documentElement.clientHeight : screen.height;

    var left = ((width / 2) - (w / 2)) + dualScreenLeft;
    var top = ((height / 2) - (h / 2)) + dualScreenTop;

    var winProps = 'scrollbars=yes,menubar=no,width=' + w + ', height=' + h + ',' + 'top=' + top +  ', left=' + left + 'resizable=yes,toolbar=no,location=no,status=no';
    var newWindow = window.open(url, '', winProps);
    // Puts focus on the newWindow
    if (window.focus) {
        newWindow.focus();
    }
}


function hidePreloader() {
    $(".sk-cube-grid .sk-cube").hide();
    $("#preloader").hide();
}

