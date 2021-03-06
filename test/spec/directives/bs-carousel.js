// Generated by CoffeeScript 1.6.3
(function() {
  'use strict';
  describe('Directive: bsCarousel', function() {
    var scope;
    beforeEach(module('ngApkreator'));
    scope = {};
    beforeEach(inject(function($controller, $rootScope) {
      return scope = $rootScope.$new();
    }));
    return it('should make hidden element visible', inject(function($compile) {
      var element;
      element = angular.element('<bs-carousel></bs-carousel>');
      element = $compile(element)(scope);
      return expect(element.text()).toBe('this is the bsCarousel directive');
    }));
  });

}).call(this);

/*
//@ sourceMappingURL=bs-carousel.map
*/
