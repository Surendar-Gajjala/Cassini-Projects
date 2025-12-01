/**
 * Created by annap on 07/07/2016.
 */
  define(['app/shared/services/services.module',
          'app/shared/factories/httpFactory'],
           function (mdoule) {
          mdoule.factory('TmService', TmService);

          function TmService(httpFactory) {
              return {

              }
          }
      }
  );