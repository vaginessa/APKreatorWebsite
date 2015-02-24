// Generated by CoffeeScript 1.6.3
(function() {
  'use strict';
  angular.module('ngApkreator').controller('HeaderCtrl', function($scope, $rootScope, $compile, $http) {
    $rootScope.headerMenuItems = [
      {
        name: "Home",
        url: "#/"
      }, {
        name: "App Creator",
        url: "#/app-creator"
      }, {
        name: "Features",
        url: "#/features"
      }, {
        name: "Contact",
        url: "#"
      }
    ];
    $scope.signIn = function() {
      var dialog;
      dialog = $scope.compileHtml("<div ng-include ng-controller=\"SignInCtrl\" src=\"'views/parts/dialogs/sign-in.html'\"></div>");
      vex.open().append(dialog).bind("vexClose", function() {});
    };
    $scope.signUp = function() {
      var dialog;
      dialog = $scope.compileHtml("<div ng-include ng-controller=\"SignUpCtrl\" src=\"'views/parts/dialogs/sign-up.html'\"></div>");
      vex.open().append(dialog).bind("vexClose", function() {
        return $scope.signOut();
      });
    };
    $scope.signOut = function() {};
    $scope.expr = function(expr, locals) {
      return $scope.$eval(expr, locals);
    };
    return $scope.compileHtml = function(html) {
      var linkThat;
      linkThat = $compile(html);
      return linkThat($scope);
    };
  });

}).call(this);

/*
//@ sourceMappingURL=header.map
*/
