
define(
    [
        'app/desktop/desktop.root',
    ],

    function () {
        var module = angular.module('tm.person',
            [
                'app.root'
            ]
        );

        module.config(configFunc);

        function configFunc($stateProvider, $urlRouterProvider, $locationProvider,
                            $controllerProvider, $compileProvider, $filterProvider,
                            $provide, $logProvider, $httpProvider, dependencyProvider) {

            module.controller = $controllerProvider.register;
            module.directive = $compileProvider.directive;
            module.filter = $filterProvider.register;
            module.factory = $provide.factory;
            module.service = $provide.service;
            module.stateProvider = $stateProvider;
        }

        return module;
    }
);
























/*

  define(
      [
          'app/app.root'
      ],

      function() {
          var module = angular.module('tm.person',
              [
                  'tm.root'
              ]
          );

          module.config(
              [
                  '$stateProvider',
                  '$urlRouterProvider',
                  '$locationProvider',
                  '$controllerProvider',
                  '$compileProvider',
                  '$filterProvider',
                  '$provide',
                  '$logProvider',
                  '$httpProvider',
                  'dependencyProvider',

                  function(${DS}stateProvider, ${DS}urlRouterProvider, ${DS}locationProvider,
                           ${DS}controllerProvider, ${DS}compileProvider, ${DS}filterProvider,
                           ${DS}provide, ${DS}logProvider, ${DS}httpProvider, dependencyProvider) {

                      module.controller = ${DS}controllerProvider.register;
                      module.directive = ${DS}compileProvider.directive;
                      module.filter = ${DS}filterProvider.register;
                      module.factory = ${DS}provide.factory;
                      module.service = ${DS}provide.service;
                      module.stateProvider = ${DS}stateProvider;
                  }
              ]
          );

          return module;
      }
  );*/
