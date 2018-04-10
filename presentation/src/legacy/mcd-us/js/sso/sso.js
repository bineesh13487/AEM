var ssoApp = angular.module('sso', ['ngCookies','ngSanitize']);

var ssoError = function (code, message) {
    throw new Error('MCD SSO Error - ' + code + ' - "' + message + '"');
}

window.aemScrollTo = function (offset) {
    if (typeof offset !== 'number') {
        offset = 0;
    }
    if (window.self !== window.top && $('#ContentScrollView', parent.document).length > 0) {
        $('#ContentScrollView', parent.document).animate({
            scrollTop: offset
        }, 300, 'swing');
    } else {
        $('html, body').animate({
            scrollTop: offset
        }, 300, 'swing');
    }
};

if (typeof Object.assign != 'function') {
    Object.assign = function(target) {
        'use strict';
        if (target == null) {
            throw new TypeError('Cannot convert undefined or null to object');
        }

        target = Object(target);
        for (var index = 1; index < arguments.length; index++) {
            var source = arguments[index];
            if (source != null) {
                for (var key in source) {
                    if (Object.prototype.hasOwnProperty.call(source, key)) {
                        target[key] = source[key];
                    }
                }
            }
        }
        return target;
    };
}
