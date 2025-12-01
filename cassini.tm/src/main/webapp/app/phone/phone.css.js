define([], function() {
    var viewsCss = {
            global: [
                {
                    href: 'app/assets/bower_components/angular-material/angular-material.css',
                    persist: true
                },
                {
                    href: 'app/assets/bower_components/angular-material/layouts/angular-material.layouts.css',
                    persist: true
                },
                {
                    href: 'app/assets/css/animate.min.css',
                    persist: true
                },
                {
                    href: 'app/assets/css/app/phone/app.css?bust=' + (new Date()).getTime(),
                    persist: true
                }
            ],
            app: [
                {
                    href: 'app/assets/css/app/phone/app.css?bust=' + (new Date()).getTime(),
                    persist: true
                }
            ]
        },
        getViewCss =  function(route) {
            var cssList = viewsCss.global;

            if(viewsCss[route] != null && route != null && route != 'global') {
                cssList = cssList.concat(viewsCss[route]);
            }

            return cssList;
        };
        return {
            getViewCss: getViewCss
        };
    }
);