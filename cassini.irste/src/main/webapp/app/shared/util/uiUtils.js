var appNotification = null;

function initNotificationPanel() {
    appNotification = $('#appNotification');
    $(window).resize(positionNotification);

    $(document).on('keydown',function(evt) {
        if (evt.keyCode == 27) {
            hideNotification();
        }
    });
}

function initPreloader() {
}

function positionNotification() {
    var pos = $('#headerbar').position();
    if(pos != null) {
        var height = $('#headerbar').outerHeight();
        var width = $('#workspace').outerWidth();
        var h = $('#viewTitleContainer').outerHeight();

        var left = pos.left;
        var top = pos.top+height;
        if ($application.selectedProject != undefined && $application.selectedProject.locked == true) {
            top = top + 30;
        } else {
            top = pos.top + height;
        }
        var projNav = document.getElementById('project-headerbar');
        if(projNav != null && projNav != undefined) {
            top = top + 50;
        }

        appNotification.css({top: top, left: left, width:width, height: h, position:'absolute'});
    }
}



function showNotification() {
    positionNotification();
    appNotification.show();
    appNotification.removeClass('zoomOut');
    appNotification.addClass('zoomIn');
}

function hideNotification() {
    positionNotification();
    appNotification.removeClass('zoomIn');
    appNotification.addClass('zoomOut');
}

function launchUrl(url) {
    var winProps = 'scrollbars=yes,menubar=no,width=500, resizable=yes,toolbar=no,location=no,status=no';
    window.open(url, '', winProps);
}
