/* Usage:
    <valid-form inputs="inputs" submit-fn="submitFn(session, ref)" form-name="formName" session="session" ref="ref" submitBtnText="Submit"></valid-form>

    Controller scope:
        $scope.inputs = [
            name: String,
            label: String, // html label for input
            required: Boolean,
            type: String, // html input type, ex: 'email', 'text', 'checkbox'
            validation: Regex,
            errorMsg: String,
            requireMsg: String,
            callbackMsg: Null // leave undefined until needed to show,
            description: String, // text to show below an input - always visible
            note: String // text to show after clicking on 'Why?'
        ],
        $scope.session = {} - reference model for validation
        $scope.submitFn = function for clicking submit
        $scope.formName = the name of the form,
        $scope.ref = Array - another reference for the directive link controller that modifies the original inputs array
*/

"use strict";

ssoApp.directive('dynamicName', ['$compile', function($compile){
    return {
        restrict:"A",
        terminal:true,
        priority:1000,
        link:function(scope,element,attrs){
            element.attr('name', scope.$eval(attrs.dynamicName));
            element.removeAttr("dynamic-name");
            $compile(element)(scope);
        }
    };
}]);

ssoApp.directive('validForm', ['config', '$timeout', '$sce', function(config, $timeout, $sce) {
    return {
        restrict: 'E',
        scope: {
            inputs: '=?',
            submitFn: '&',
            formName: '=',
            hideOnInitFields: '=?hideOnInit',
            hideCheck: '&?',
            session: '=',
            ref: '=',
            fieldNames: '=?',
            submitbtntext: '@',
            blockingValidationCheck: '=?',
            requiredfieldstext: '@',
            noVisibleFieldsCallback: '&?',
            vm: '=?',
            afterRender: '&?',
            errorMsg: '=?'
        },
        templateUrl: '/etc/clientlibs/mcd-us/js/sso/html/valid-form.html',
        replace: true,
        link: function(scope, elm, attrs) {
            var pw0, pw1, pw2;
            scope.pw0 = {};
            scope.pw1 = {};
            scope.pw2 = {};
            var linkPasswords = false;
            var passwordCount = 0;
            var passwordFields = [];
            var matchPass;
            var matchPassConfirm;
            scope.whyText = config.get('config').whyLinkText;
            scope.vm = scope;
            scope.noneRequired = true;
            scope.inputs = scope.inputs || [];
            var inputs = scope.inputs;

            var displayFieldsOnInitResults = {};
            var hiddenFields = scope.hideOnInitFields || [];
            var fieldNames = [];

            var _selectOpts;
            var optsI;




            function shouldHideOnInit (field) {
                var toDisplay = false;
                var thisValidation = field.validation ? new RegExp(field.validation) : '';
                if (hiddenFields.indexOf(field.name) > -1) {
                    if (!scope.session[field.name] && field.type !== 'password') {
                        toDisplay = true;
                    } else if (thisValidation && !thisValidation.exec(scope.session[field.name]) && field.type !== 'password') {
                        toDisplay = true;
                    } else if (field.type === 'password' && !scope.session[field.name] && (!scope.session.type || scope.session.type !== 'social')) {
                        toDisplay = true;
                    }
                } else {
                    toDisplay = true;
                }
                return toDisplay;
            }

            // set up form directive
            for (var i = 0; i < scope.inputs.length; i++) {
                var field = scope.inputs[i];
                fieldNames.push(field.name);
                if (field.type === 'password') {
                    passwordCount++;
                    passwordFields.push(field);
                }
                if (scope.noneRequired && field.required) {
                    scope.noneRequired = false;
                }

                if (field.validation) {
                    field.validation = new RegExp(field.validation, 'i');
                }
                if (field.type === 'checkbox') {
                    if (!scope.session[field.name]) {
                        if (field.checkedByDefault) {
                            scope.session[field.name] = field.value ? field.value : 'Y';
                        } else {
                            scope.session[field.name] = field.value ? '' : 'N';
                        }
                    }
                }
                if (field.type === 'select') {
                    if (!scope.session[field.name]) {
                        if (field.options) {
                            scope.session[field.name] = field.options[0].value;
                        }
                    } else if (field.options) {
                        _selectOpts = [];
                        for (optsI = 0; optsI < field.options.length; optsI++) {
                            _selectOpts.push(field.options[optsI].value);
                        }
                        if (_selectOpts.indexOf(scope.session[field.name]) < 1) {
                            scope.session[field.name] = field.options[0].value;
                        }
                    }
                }
                if (field.type === 'radio') {
                    if(!scope.session[field.name]) {
                        scope.session[field.name] = field.value ? '' : 'N';
                    }
                }
                scope.ref.push(field);
            }

            var first, second;
            if (passwordCount === 3) {
                scope.notMatching = true;
                scope.pw0 = scope.inputs[scope.inputs.indexOf(passwordFields[0])];
                scope.pw1 = scope.inputs[scope.inputs.indexOf(passwordFields[1])];
                scope.pw2 = scope.inputs[scope.inputs.indexOf(passwordFields[2])];
                matchPass = scope.pw1.name;
                matchPassConfirm = scope.pw2.name;
            } else if (passwordCount === 2) {
                scope.notMatching = true;
                scope.pw1 = scope.inputs[scope.inputs.indexOf(passwordFields[0])];
                scope.pw2 = scope.inputs[scope.inputs.indexOf(passwordFields[1])];
                matchPass = scope.pw1.name;
                matchPassConfirm = scope.pw2.name;
            } else {
                scope.notMatching = false;
            }
            scope.fieldNames = fieldNames;
            var confirmPwWatch;

            if (scope.notMatching) {
                scope.$watch('session.' + scope.pw1.name, function(valid, lastVal) {
                    // only find correct field to change once
                    if (!confirmPwWatch) {
                        for (var i = 0; i < scope.inputs.length; i++) {
                            var field = scope.inputs[i];

                            if (field.name === scope.pw2.name) {
                                confirmPwWatch = field;
                            }
                        }
                    }

                    if (scope[scope.formName][scope.pw2.name] && scope[scope.formName][scope.pw2.name].$dirty && scope.session[scope.pw1.name] !== scope.session[scope.pw2.name]) {
                        confirmPwWatch.doesNotMatch = true;
                        scope.notMatching = true;
                    } else if (scope[scope.formName][scope.pw2.name] && scope[scope.formName][scope.pw2.name].$dirty) {
                        confirmPwWatch.doesNotMatch = false;
                        scope.notMatching = false;
                    }


                });
            }

            scope.$watchCollection('session' , function(valid, lastVal) {
                scope.checkMatchForSubmit();
            });

            scope.checkMatchForSubmit = function() {
                var notMatchingPass = false;
                for (var i = 0; i < scope.inputs.length; i++) {
                    var field = scope.inputs[i];

                    if ((field.name === scope.pw1.name || field.name === scope.pw2.name) && field.doesNotMatch) {
                        notMatchingPass = true;
                    }
                }
                return notMatchingPass;
            }


            var visibleFields = false;
            if (typeof scope.noVisibleFieldsCallback === 'function') {
                for (var n = 0; n < scope.inputs.length; n++) {
                    if (shouldHideOnInit(scope.inputs[n]) && scope.inputs[n].type != 'hidden') {
                        visibleFields = true;
                        break;
                    }
                }
                if (hiddenFields.indexOf('password') > -1 && hiddenFields.indexOf('passwordConfirm') > -1) {
                    scope.notMatching = false;
                }
                if (!visibleFields) {
                    scope.noVisibleFieldsCallback();
                }
            }

            scope.$on('scrollToTop', function () {
                var top = 0;
                var $e = elm.parent('.ssoMainContent');
                if ($e.length) {
                    top = Math.max($e.offset().top - 115, 0);
                }
                window.aemScrollTo(top);
            });

            scope.$on('scrollToErrors', function () {
                $timeout(function () {
                    var $inputs = elm.find('.ng-invalid, .ng-invalid-required');
                    var topMost;
                    if ($inputs.length) {
                        topMost = $inputs.first().parent().offset().top;
                        $inputs.each(function () {
                            var _input = $(this);
                            var _top = _input.parent().offset().top;
                            if (_top < topMost) {
                                topMost = _top;
                            }
                        });
                        window.aemScrollTo(topMost - 115);
                    }
                }, 10);
            });

            scope.isBlockingValidationPresent = false;
            scope.blockingValidation = function (field) {
                return false;
            };
            if (typeof scope.blockingValidationCheck === 'function') {
                scope.isBlockingValidationPresent = true;
                scope.blockingValidation = function (field) {
                    var check = scope.blockingValidationCheck(field.name);
                    if (check) {
                        scope.notMatching = true;
                    } else {
                        scope.notMatching = false;
                    }
                    return check;
                };
            }

            function dashAndLowerCase(value) {
                return value.replace(/\s+/g, '-').toLowerCase();
            }

            scope.checkPassMatch = function(field) {
                if (field.name !== scope.pw2.name && field.name === scope.pw1.name) {
                    if (scope.session[field.name] !== scope.session[matchPassConfirm]) {
                    	scope.pw2.doesNotMatch = true;
                    	scope.notMatching = true;
                	} else if (scope.session[field.name] === scope.session[matchPassConfirm]) {
                    	if(scope.session[field.name] === undefined){
							scope.pw2.doesNotMatch = true;
                    		scope.notMatching = true;
                    	} else{
                    		scope.pw2.doesNotMatch = false;
                    		scope.notMatching = false;
                    	}
                    	return;
                	}
                }

                else {
                    if (scope.session[field.name] !== scope.session[matchPass]) {
                    	field.doesNotMatch = true;
                    	scope.notMatching = true;
                	} else if (scope.session[field.name] === scope.session[matchPass]) {
                    	if(scope.session[field.name] === undefined){
							field.doesNotMatch = true;
                    		scope.notMatching = true;
                    	} else{
                    		field.doesNotMatch = false;
                    		scope.notMatching = false;
                    	}

                	}
                }
                return;
            };

            // if (typeof scope.hideCheck !== 'function') {
            //     scope.hideCheck = function () {return true;};
            // }

            scope.hideOnInit = function (field) {
                var toDisplay = false;
                if (hiddenFields.length > 0 && scope.hideCheck()) {
                    if (typeof displayFieldsOnInitResults[field.name] === 'boolean') {
                        toDisplay = displayFieldsOnInitResults[field.name];
                    } else {
                        toDisplay = shouldHideOnInit(field);
                    }
                    displayFieldsOnInitResults[field.name] = toDisplay;
                } else {
                    toDisplay = true;
                }
                return toDisplay;
            };

            scope.inputClass = function(field) {
                var classes = [];
                if (field.type === 'checkbox') {
                    classes.push('sso-checkbox');
                } else if (field.note) {
                    classes.push('sso-input-with-tooltip');
                } else {
                    classes.push('sso-input');
                }

                if (field.required) {
                    classes.push('sso-required');
                }

                var classStr = classes.join(' ');

                return classStr;
            };

            scope.getClassName = function(field) {
                var classes = ['js-sso-field-wrapper'];
                if (field.type === 'checkbox') {
                    classes.push('sso-fac sso-fac-checkbox sso-fac-default');
                }
                else if(field.label && field.type !== 'select'){
                     classes.push('sso-input-wrapper-label');
                }
                else if(field.label && field.type == 'select'){
                    classes.push('label-with-checkbox');
                }
                else {
                    classes.push('sso-input-wrapper');
                }

                if (field.type === 'password') {
                    classes.push('sso-form-input-password');
                } else if (field.type === 'select') {
                    classes.push('sso-form-input-select');
                }

                if (field.label) {
                    classes.push('sso-input-haslabel');
                }

                var classStr = classes.join(' ');
                return classStr;
            };

            scope.trusted = function(html) {
                return $sce.trustAsHtml(html);
            };

            scope.showNote = function(e, field) {
                field.showNote = !!!field.showNote;
                var target = angular.element(e.target);
                target.attr('aria-expanded', field.showNote);
            };

            if (scope.afterRender && typeof scope.afterRender == 'function') {
                $timeout(function () {scope.$evalAsync(scope.afterRender);}, 0);
            }
        }
    };
}]);
