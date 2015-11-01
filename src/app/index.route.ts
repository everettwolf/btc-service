module btcService {
     'use strict';

     export class RouterConfig {
          /** @ngInject */
          constructor($routeProvider:ng.route.IRouteProvider) {
               $routeProvider
                    .when('/', {
                         templateUrl: 'app/main/main.html',
                         controller: 'MainController',
                         controllerAs: 'main'
                    })
                    .when('/sandbox', {
                         templateUrl: 'app/main/sandbox.html',
                         controller: 'MainController',
                         controllerAs: 'main'
                    })
                    .otherwise({
                         redirectTo: '/'
                    });
          }
     }
}
