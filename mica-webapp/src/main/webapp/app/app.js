'use strict';

/* App Module */

var mica = angular.module('mica', [
  'angular-loading-bar',
  'http-auth-interceptor',
  'localytics.directives',
  'mica.config',
  'ngObiba',
  'mica.network',
  'mica.study',
  'mica.dataset',
  'mica.dataAccesConfig',
  'mica.data-access-request',
  'ngAnimate',
  'ngCookies',
  'ngResource',
  'ngRoute',
  'pascalprecht.translate',
  'tmh.dynamicLocale',
  'ui.bootstrap',
  'ngFileUpload',
  'angularUtils.directives.dirPagination'
]);

mica
  .config(['$routeProvider', '$httpProvider', '$translateProvider', 'tmhDynamicLocaleProvider', 'USER_ROLES', 'paginationTemplateProvider',
    function ($routeProvider, $httpProvider, $translateProvider, tmhDynamicLocaleProvider, USER_ROLES, paginationTemplateProvider) {
      $routeProvider
        .when('/login', {
          templateUrl: 'app/views/login.html',
          controller: 'LoginController',
          access: {
            authorizedRoles: [USER_ROLES.all]
          }
        })
        .when('/error', {
          templateUrl: 'app/views/error.html',
          access: {
            authorizedRoles: [USER_ROLES.all]
          }
        })
        .when('/settings', {
          templateUrl: 'app/views/settings.html',
          controller: 'SettingsController',
          access: {
            authorizedRoles: [USER_ROLES.all]
          }
        })
        .when('/password', {
          templateUrl: 'app/views/password.html',
          controller: 'PasswordController',
          access: {
            authorizedRoles: [USER_ROLES.all]
          }
        })
        .when('/sessions', {
          templateUrl: 'app/views/sessions.html',
          controller: 'SessionsController',
          resolve: {
            resolvedSessions: ['Sessions', function (Sessions) {
              return Sessions.get();
            }]
          },
          access: {
            authorizedRoles: [USER_ROLES.all]
          }
        })
        .when('/metrics', {
          templateUrl: 'app/views/metrics.html',
          controller: 'MetricsController',
          access: {
            authorizedRoles: [USER_ROLES.admin]
          }
        })
        .when('/caching', {
          templateUrl: 'app/views/caching.html',
          controller: 'CachingController',
          access: {
            authorizedRoles: [USER_ROLES.admin]
          }
        })
        .when('/logs', {
          templateUrl: 'app/views/logs.html',
          controller: 'LogsController',
          resolve: {
            resolvedLogs: ['LogsService', function (LogsService) {
              return LogsService.findAll();
            }]
          },
          access: {
            authorizedRoles: [USER_ROLES.admin]
          }
        })
        .when('/audits', {
          templateUrl: 'app/views/audits.html',
          controller: 'AuditsController',
          access: {
            authorizedRoles: [USER_ROLES.admin]
          }
        })
        .when('/logout', {
          templateUrl: 'app/views/main.html',
          controller: 'LogoutController',
          access: {
            authorizedRoles: [USER_ROLES.all]
          }
        })
        .when('/docs', {
          templateUrl: 'app/views/docs.html',
          access: {
            authorizedRoles: [USER_ROLES.admin]
          }
        })
        .otherwise({
          templateUrl: 'app/views/main.html',
          controller: 'MainController',
          access: {
            authorizedRoles: [USER_ROLES.all]
          }
        });

      // Initialize angular-translate
      $translateProvider
        .useStaticFilesLoader({
          prefix: 'i18n/',
          suffix: '.json'
        })
        .preferredLanguage('en')
        .fallbackLanguage('en')
        .useCookieStorage()
        .useSanitizeValueStrategy('escaped');

      paginationTemplateProvider.setPath('app/views/pagination-template.html');
      tmhDynamicLocaleProvider.localeLocationPattern('bower_components/angular-i18n/angular-locale_{{locale}}.js');
      tmhDynamicLocaleProvider.useCookieStorage('NG_TRANSLATE_LANG_KEY');
    }])

  // Workaround for bug #1404
  // https://github.com/angular/angular.js/issues/1404
  // Source: http://plnkr.co/edit/hSMzWC?p=preview
  .config(['$provide', function ($provide) {
    $provide.decorator('ngModelDirective', ['$delegate', function ($delegate) {
      var ngModel = $delegate[0], controller = ngModel.controller;
      ngModel.controller = ['$scope', '$element', '$attrs', '$injector', function (scope, element, attrs, $injector) {
        var $interpolate = $injector.get('$interpolate');
        attrs.$set('name', $interpolate(attrs.name || '')(scope));
        $injector.invoke(controller, this, {
          '$scope': scope,
          '$element': element,
          '$attrs': attrs
        });
      }];
      return $delegate;
    }]);
    $provide.decorator('formDirective', ['$delegate', function ($delegate) {
      var form = $delegate[0], controller = form.controller;
      form.controller = ['$scope', '$element', '$attrs', '$injector', function (scope, element, attrs, $injector) {
        var $interpolate = $injector.get('$interpolate');
        attrs.$set('name', $interpolate(attrs.name || attrs.ngForm || '')(scope));
        $injector.invoke(controller, this, {
          '$scope': scope,
          '$element': element,
          '$attrs': attrs
        });
      }];
      return $delegate;
    }]);
  }])

  .run(['$rootScope', '$location', '$http', 'AuthenticationSharedService', 'Session', 'USER_ROLES', 'ServerErrorUtils', 'UserProfileService',
    function ($rootScope, $location, $http, AuthenticationSharedService, Session, USER_ROLES, ServerErrorUtils, UserProfileService) {
      $rootScope.$on('$routeChangeStart', function (event, next) {
        $rootScope.authenticated = AuthenticationSharedService.isAuthenticated();
        $rootScope.hasRole = AuthenticationSharedService.isAuthorized;
        $rootScope.userRoles = USER_ROLES;
        $rootScope.subject = Session;
        $rootScope.userProfileService = UserProfileService;

        if (!$rootScope.authenticated) {
          $rootScope.$broadcast('event:auth-loginRequired');
        } else if (!AuthenticationSharedService.isAuthorized(next.access ? next.access.authorizedRoles : '*')) {
          $rootScope.$broadcast('event:auth-notAuthorized');
        }
      });

      // Call when the the client is confirmed
      $rootScope.$on('event:auth-loginConfirmed', function () {
        if ($location.path() === '/login') {
          $location.path('/').replace();
        }
      });

      // Call when the 401 response is returned by the server
      $rootScope.$on('event:auth-loginRequired', function () {
        Session.destroy();
        if ($location.path() !== '/' && $location.path() !== '') {
          $location.path('/login').replace();
        }
      });

      // Call when the 403 response is returned by the server
      $rootScope.$on('event:auth-notAuthorized', function () {
        if (!$rootScope.authenticated) {
          $location.path('/login').replace();
        } else {
          $rootScope.errorMessage = 'errors.403';
          $location.path('/error').replace();
        }
      });

      $rootScope.$on('event:unhandled-server-error', function (event, response) {
        $rootScope.errorMessage = ServerErrorUtils.buildMessage(response);
        $location.path('/error').replace();
      });

      // Call when the user logs out
      $rootScope.$on('event:auth-loginCancelled', function () {
        $rootScope.authenticated = undefined;
        $location.path('/login');
      });
    }]);
