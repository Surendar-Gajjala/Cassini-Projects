
function initPreloader() {
    var delay = 500;
    // Page Preloader
    jQuery('#preloader').delay(delay).fadeOut(function(){
        jQuery('#appview').delay(delay+500).show();
    });

    $(window).resize(function(){
        positionNotification();
    });

    $(document).on('keydown',function(evt) {
        if (evt.keyCode == 27) {
            hideNotification();
        }
    });
}

function initPanels() {
    // Close Button in Panels
    jQuery('.panel .panel-close').click(function(){
        jQuery(this).closest('.panel').fadeOut(200);
        return false;
    });

    // Minimize Button in Panels
    jQuery('.minimize').click(function(){
        var t = jQuery(this);
        var p = t.closest('.panel');
        if(!jQuery(this).hasClass('maximize')) {
            p.find('.panel-body, .panel-footer').slideUp(200);
            t.addClass('maximize');
            t.html('&plus;');
        } else {
            p.find('.panel-body, .panel-footer').slideDown(200);
            t.removeClass('maximize');
            t.html('&minus;');
        }
        return false;
    });
}

function initResizeEvent() {
    jQuery(window).resize(function(){

        /*
        showInnerLogo();

        if(jQuery('body').css('position') == 'relative') {
            jQuery('body').removeClass('leftpanel-collapsed chat-view');
        } else {
            jQuery('body').removeClass('chat-relative-view');
            jQuery('body').css({left: '', marginRight: ''});
        }

        repositionSearchForm();
        repositionTopNav();
        */

    });
}

function initRightPanel() {
    // Chat View
    jQuery('#chatview').click(function(){
        var body = jQuery('body');
        var bodypos = body.css('position');

        if(bodypos != 'relative') {

            if(!body.hasClass('chat-view')) {
                body.addClass('leftpanel-collapsed chat-view');
                jQuery('.nav-bracket ul').attr('style','');

            } else {

                body.removeClass('chat-view');

                if(!jQuery('.menutoggle').hasClass('menu-collapsed')) {
                    jQuery('body').removeClass('leftpanel-collapsed');
                    jQuery('.nav-bracket li.active ul').css({display: 'block'});
                } else {

                }
            }

        } else {

            if(!body.hasClass('chat-relative-view')) {

                body.addClass('chat-relative-view');
                body.css({left: ''});

            } else {
                body.removeClass('chat-relative-view');
            }
        }


        positionNotification();

    });
}

function initLeftPanel() {
    // Toggle Left Menu
    jQuery('.leftpanel .nav-parent > a').live('click', function() {

        var parent = jQuery(this).parent();
        var sub = parent.find('> ul');

        // Dropdown works only when leftpanel is not collapsed
        if(!jQuery('body').hasClass('leftpanel-collapsed')) {
            if(sub.is(':visible')) {
                sub.slideUp(200, function(){
                    parent.removeClass('nav-active');
                    //jQuery('.mainpanel').css({height: ''});
                });
            } else {
                closeVisibleSubMenu();
                parent.addClass('nav-active');
                sub.slideDown(200, function(){
                });
            }
        }
        return false;
    });

    // Add class everytime a mouse pointer hover over it
    jQuery('.nav-bracket > li').hover(function(){
        jQuery(this).addClass('nav-hover');
    }, function(){
        jQuery(this).removeClass('nav-hover');
    });

    // Handles form inside of dropdown
    jQuery('.dropdown-menu').find('form').click(function (e) {
        e.stopPropagation();
    });
}

/* This function will reposition search form to the left panel when viewed
 * in screens smaller than 767px and will return to top when viewed higher
 * than 767px
 */
function repositionSearchForm() {
    if(jQuery('.searchform').css('position') == 'relative') {
        jQuery('.searchform').insertBefore('.leftpanelinner .userlogged');
    } else {
        jQuery('.searchform').insertBefore('.header-right');
    }
}



/* This function allows top navigation menu to move to left navigation menu
 * when viewed in screens lower than 1024px and will move it back when viewed
 * higher than 1024px
 */
function repositionTopNav() {
    if(jQuery('.nav-horizontal').length > 0) {

        // top navigation move to left nav
        // .nav-horizontal will set position to relative when viewed in screen below 1024
        if(jQuery('.nav-horizontal').css('position') == 'relative') {

            if(jQuery('.leftpanel .nav-bracket').length == 2) {
                jQuery('.nav-horizontal').insertAfter('.nav-bracket:eq(1)');
            } else {
                // only add to bottom if .nav-horizontal is not yet in the left panel
                if(jQuery('.leftpanel .nav-horizontal').length == 0)
                    jQuery('.nav-horizontal').appendTo('.leftpanelinner');
            }

            jQuery('.nav-horizontal').css({display: 'block'})
                .addClass('nav-pills nav-stacked nav-bracket');

            jQuery('.nav-horizontal .children').removeClass('dropdown-menu');
            jQuery('.nav-horizontal > li').each(function() {

                jQuery(this).removeClass('open');
                jQuery(this).find('a').removeAttr('class');
                jQuery(this).find('a').removeAttr('data-toggle');

            });

            if(jQuery('.nav-horizontal li:last-child').has('form')) {
                jQuery('.nav-horizontal li:last-child form').addClass('searchform').appendTo('.topnav');
                jQuery('.nav-horizontal li:last-child').hide();
            }

        } else {
            // move nav only when .nav-horizontal is currently from leftpanel
            // that is viewed from screen size above 1024
            if(jQuery('.leftpanel .nav-horizontal').length > 0) {

                jQuery('.nav-horizontal').removeClass('nav-pills nav-stacked nav-bracket')
                    .appendTo('.topnav');
                jQuery('.nav-horizontal .children').addClass('dropdown-menu').removeAttr('style');
                jQuery('.nav-horizontal li:last-child').show();
                jQuery('.searchform').removeClass('searchform').appendTo('.nav-horizontal li:last-child .dropdown-menu');
                jQuery('.nav-horizontal > li > a').each(function() {

                    jQuery(this).parent().removeClass('nav-active');

                    if(jQuery(this).parent().find('.dropdown-menu').length > 0) {
                        jQuery(this).attr('class','dropdown-toggle');
                        jQuery(this).attr('data-toggle','dropdown');
                    }

                });
            }

        }

    }
}



function closeVisibleSubMenu() {
    jQuery('.leftpanel .nav-parent').each(function() {
        var t = jQuery(this);
        if(t.hasClass('nav-active')) {
            t.find('> ul').slideUp(200, function(){
                t.removeClass('nav-active');
            });
        }
    });
}

function initToolTipAndPopover() {
    // Tooltip
    jQuery('.tooltips').tooltip({ container: 'body'});

    // Popover
    jQuery('.popovers').popover();

}

function initToggles() {
    $('.dropdown-toggle').dropdown()

    // Form Toggles
    jQuery('.toggle').toggles({on: true});

    jQuery('.toggle-chat1').toggles({on: false});
}

function initBody() {

    $(document.body).addClass('stickyheader');

    // Check if leftpanel is collapsed
    if(jQuery('body').hasClass('leftpanel-collapsed'))
        jQuery('.nav-bracket .children').css({display: ''});
}

function initModals() {
    // BOOTSTRAP MODAL
    $( document ).on( 'loaded.bs.modal', '.modal', function( e ){
        var targetID = e.target.id,
            handler = $('[data-target="#' + targetID + '"]'),
            dataScripts = handler.data( 'scripts' ),
            scripts = ( dataScripts ) ? dataScripts.replace(/\s+/g, '') : false;

        if( scripts ){
            scripts = scripts.split( ',' );
            $.each( scripts, function( i, val ){
                $( 'body' ).createScript( val );
            });
        }
    })
    .on( 'hide.bs.modal', '.modal', function () {
    // hidden open popover
    $( document ).find( '[data-toggle=popover], [rel*=popover]' ).popover('hide');
    })
    // to support modal stackable
    .on( 'shown.bs.modal', function(e){
        var $modalBackdrop = $( 'body > .modal-backdrop' ),
            isStackable = ($modalBackdrop.length > 1) ? true : false;

        if ( isStackable ) {
            var zIndex = parseInt( $modalBackdrop.first().css( 'z-index' ) );

            $modalBackdrop.each( function(){
                var $backdrop = $( this );

                if(! $backdrop.is(':first') ){
                    zIndex = parseInt( $backdrop.css( 'z-index' ) );
                }
                if(! $backdrop.is(':last') ){
                    $backdrop.next().css( 'z-index', zIndex + 10 );
                }
            });

            var lastZindex = parseInt( $modalBackdrop.last().css('z-index') );
            $( e.target ).css( 'z-index', lastZindex + 10 );
        }
    });
    // END BOOTSTRAP MODAL
}

function positionNotification() {
    var pos = $('#headerbar').position();
    var height = $('#headerbar').outerHeight();
    var width = $('#workspace').outerWidth();
    var h = $('#viewTitleContainer').outerHeight();

    var left = pos.left;
    var top = pos.top+height;

    $('#appNotification').css({top: top, left: left, width:width, height: h, position:'absolute'});
}

function showNotification() {
    positionNotification();

    $('#appNotification').removeClass('fadeOutUpBig');
    $('#appNotification').show();
    $('#appNotification').addClass('fadeInDownBig');
}

function hideNotification() {
    $('#appNotification').removeClass('fadeInDownBig');
    $('#appNotification').addClass('fadeOutUpBig');
    setTimeout(function() {
        $('#appNotification').hide();
        $('#appNotification').removeClass('fadeInDownBig');
        $('#appNotification').removeClass('fadeOutUpBig');
    }, 100);
}

var workspaceFullScreen = false;
function toggleWorkspaceFullScreen() {
    if(!workspaceFullScreen) {
        $(".leftpanel").hide();
        $(".headerbar").hide();
        $("#workspace").addClass("workspace-fullscreen");
    }
    else {
        $("#workspace").removeClass("workspace-fullscreen");
        $(".leftpanel").show();
        $(".headerbar").show();
    }
    workspaceFullScreen = !workspaceFullScreen;
    positionNotification();
}

var mainpanelFullScreen = false;
function toggleMainPanelFullScreen() {
    if(!mainpanelFullScreen) {
        $(".leftpanel").hide();
        //$(".headerbar").hide();
        $("#mainPanel").addClass("mainpanel-fullscreen");
    }
    else {
        $("#mainPanel").removeClass("mainpanel-fullscreen");
        $(".leftpanel").show();
        //$(".headerbar").show();
    }
    mainpanelFullScreen = !mainpanelFullScreen;
    showInnerLogo();
    positionNotification();
}

function showInnerLogo() {
    if(mainpanelFullScreen == true) {
        $('.menutoggle').addClass('menu-toggle-collapsed');
        $(".inner-logo").show();
    }
    else {
        $(".inner-logo").hide();
        $(".menutoggle").hide();
        $('.menutoggle').removeClass('menu-toggle-collapsed');
        $(".menutoggle").show();
    }
}

var helpVisible = false;
function toggleHelp() {
    if(!helpVisible) {
        $("#helpPanel").css({right: 0});
        $("#helpPanel").animate({width: 400});
    }
    else {
        $("#helpPanel").animate({width: 0});
        $("#helpPanel").css({right: -50});
    }

    helpVisible = !helpVisible;
}

function hideHelp() {
    $("#helpPanel").animate({width: 0});
    $("#helpPanel").css({right: -50});
    helpVisible = false;
}
