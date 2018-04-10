(function ($) {

    $.fn.xfield_validator = function (options) {

        // Overriding defaults with options supplied while calling.
        var settings = $.extend({}, $.fn.xfield_validator.defaults, options);


        // Private variables

        var multipleForms = this.length,
            $fieldErrors = [],
            $fieldErrorsGrouped = [],
            placeholder = '|x|';

        var allowedSpecialKeys = [8, 46]; // Tab = 9, Backspace = 8, Delete = 46,

        // Default email and URL Patterns
        var emailPattern = new RegExp(/^(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])$/);
        var urlPattern = new RegExp(/^(?:(ftp|ftps|http|https)?:\/\/)?[\w.-]+(?:\.[\w\.-]+)+[\w\-\._~:/?#[\]%@!\$&'\(\)\*\+,;=.]+$/);

        var $form, $field, $fieldType, $parent, $val, $valLength, $numVal, $maxLength,
            $maxNumber, $minNumber, $pattern, $patternCheck, $matchField, $matchFieldParent,
            success, $maxWords, $minWords, $wordsCount, $selectMin, $selectMax, $selectCount,
            radioGroupRequired, radioCheckCount, checkGroupRequired, checkedCount, checkGroupMin,
            checkGroupMax, maxFiles, minFiles, extension, fileName, filesArray = [], allowedFiles,
            tempV0, tempV1, tempV2, tempV3, formSubmission, previousGroup, runGroup, $lastGroupErrors,
            $formErrorsArea, $dateField, $dateEnd, $dateStart, $date, $checkEndDate, $dataMask,
            $dataMaskOptions, $hasMask, maxSize, maxSizeCheck, minSize, minSizeCheck, errorSizeUnit, fieldsRecord,
            newError, errWordsCount, validEmail, patternFound, validURL;

        // Default Mask Methods required for validation states
        var defaultMaskOptions = {
            onComplete: function (val, event, currentField) {
                $(currentField).addClass('mask-complete');
            },
            onChange: function (val, event, currentField) {
                $(currentField).removeClass('mask-complete');
            },
        };


        // Private Methods


        // Checks if all fields are validated and non has error, gives success or error class to form
        function formStatusCheck() {
            success = false;
            if ($form.find('.' + settings.parentsClass + '.req-val').length == $form.find('.' + settings.parentsClass + '.req-val' + '.val-processed').length) {
                if ($form.find('.' + settings.blockErrorClass).length) {
                    success = false;
                    $form.addClass(settings.formErrorClass);
                    $form.removeClass(settings.formSuccessClass);
                } else {
                    success = true;
                    $form.addClass(settings.formSuccessClass);
                    $form.removeClass(settings.formErrorClass);
                }
            }
            return success;
        }

        // Assigning req-val class on parent of required fields, this is for final form status checking.
        function setRequiredClasses() {
            $form.find('input,select, textarea').each(function (index, item) {
                if (($(item).attr('required') ||
                    $(item).closest('.' + settings.parentsClass).attr('data-group-required')) && !$(item).attr('data-skip-field')) {

                    $(item).closest('.' + settings.parentsClass).addClass('req-val');

                }
            });
        }

        //  Generates a detailed response of form status when you hit submit
        function generateResponse(formStatus){

            fieldsRecord = {
                formStatus: formStatus,
                fieldsStatus: []
            };

            tempV0 = [];
            tempV1 = null;

            $form.find('.' + settings.parentsClass).each(function(index,item){
                $parent = $(item);
                $field = $($parent.find(':input').not('.skip-from-validation').get(0));
                tempV1 = {
                    required: null,
                    name: null,
                    id: null,
                    validated: null,
                    isGroup: null,
                };

                if($parent.hasClass('req-val')){
                    tempV1.required = true;
                }else{
                    tempV1.required = false;
                }

                if($parent.hasClass('checkbox-group') || $parent.find('input[type=radio]').length ){
                    tempV1.isGroup = true;

                }else{
                    tempV1.isGroup = false;
                }

                if($parent.hasClass(settings.blockErrorClass)){
                    tempV1.validated = false;
                }else{
                    tempV1.validated = true;
                }

                if(tempV1.isGroup){
                    if($parent.attr('data-group-name')) {
                        tempV1.name = $parent.attr('data-group-name');
                    }else{
                        tempV1.name = null;
                    }

                    if($parent.attr('id')) {
                        tempV1.id = $parent.attr('id');
                    }else{
                        tempV1.id = null;
                    }

                }else{
                    if($field.attr('id')){
                        tempV1.id = $field.attr('id');
                    }else{
                        tempV1.id = null
                    }

                    if($field.attr('name')){
                        tempV1.name = $field.attr('name');
                    }else{
                        tempV1.name = null
                    }
                }
                fieldsRecord.fieldsStatus.push(tempV1);
            });

            return fieldsRecord
        }

        // The submit function that runs when you hit submit or enter,
        function submitFunc(event) {
            previousGroup = $(null);
            formSubmission = true;

            $form = $(this);
            $form.find('input, select, textarea').each(function (index, item) {
                validateField($(this));
            });
            formSubmission = false;
            if (formStatusCheck()) {
                if ($.isFunction(settings.complete)) {
                    settings.complete.call(this, generateResponse(true));
                }
                return true;
            } else {
                if ($.isFunction(settings.complete)) {
                    settings.complete.call(this,generateResponse(false));
                }
                event.stopImmediatePropagation();
                return false;
            }
        }

        // Applies focus out validation on fields
        function validateFocus() {
            tempV2 = $form.find('input[type=text],input[type=email],input[type=date],input[type=number],input[type=password],input[type=search],input[type=url],input[type=tel],input[type=week],input[type=time],input[type=month],input[type=datetime-local],textarea,select');
            tempV2.each(function (index) {


                if (settings.enableLastFieldFix) {
                    if (index == tempV2.length - 1) {
                        $(this).closest('.' + settings.parentsClass).after('<input type="button" data-skip-field class="xfield-focus-fix"/>');
                    }
                }

                $(this).blur(function (event) {
                    if ($(event.relatedTarget).prop('type') === 'submit') {
                        event.preventDefault();
                    } else {
                        validateField($(this));
                        $form = $(this).closest('form');
                        formStatusCheck();
                    }
                })

            });

            // find and activate focus fix if there is any such class to;
            $form.find('.xfield-focus-fix').focus(function (event) {
                tempV0 = $(this).nextAll().filter(':input')
                if (tempV0.length)
                    tempV0.get(0).focus();
            });

        }

        // Applies change validation on fields
        function validateChange() {
            $form.find('input[type=checkbox],input[type=radio],input[type=file],select').each(function () {
                $(this).change(function (event) {
                    validateField($(this));
                    $form = $(this).closest('form');
                    formStatusCheck();
                })
            });
        }

        // Applies keypress validation on fields
        function validateKey() {
            $form.find('input[type=text],input[type=email],input[type=date],input[type=number],input[type=password],input[type=search],input[type=url],input[type=tel],input[type=week],input[type=time],input[type=month],input[type=datetime-local],textarea').keyup(function (event) {
                if ((event.key.length == 1 || allowedSpecialKeys.indexOf(event.keyCode) != -1) && (event.which !== 0 && !event.ctrlKey && !event.metaKey && !event.altKey)) {
                    validateField($(this));
                    formStatusCheck();
                }
            });
        }

        // Makes error for pattern based errors
        function makeError(error, value) {
            if (value) {
                return error.replace(placeholder, value);
            }
            return error
        }

        // For setting error on field
        function setError(attrError, value, defaultError, wcError) {
            newError = $field.attr(attrError);
            errWordsCount = '';
            if (settings.enableWCError && wcError) {
                errWordsCount = ', Currents Words: ' + $wordsCount;
            }
            if (newError)
                return makeError(newError + errWordsCount, value);
            else
                return makeError(defaultError + errWordsCount, value);
        }

        // Main function for error processing for a field
        function processErrors() {
            // Showing all errors
            if ($fieldErrors.length) {
                //&& !settings.showErrorsGrouped
                if (settings.showErrorMessages) {
                    $fieldErrors.forEach(function (item, index) {
                        $parent.append(
                            $('<div class="' + settings.eachErrorClass + '">'+ item + '</div>')
                        )
                    });
                }
                xfieldSetError()
            } else {
                xfieldSetSuccess()
            }
            if (settings.showErrorsGrouped) {
                tempV0 = $fieldErrors.join('<br>');
                $formErrorsArea = $form.data('formErrorsArea');
                $formErrorsArea.find('.' + settings.singleErrorCl + '-' + $field.data('xfieldId')).html(tempV0);
            }
        }

        // For Setting Error from group
        function setErrorFromGroup(attrError, defaultError) {
            newError = $parent.attr(attrError);
            if (newError)
                return newError;
            else
                return defaultError;
        }

        // Skip a field
        function skipField() {
            return false;
        }

        // Function to prevent group validations from running multiple times ( one for each input element )
        function checkGroupRepeats() {
            runGroup = true;
            if (formSubmission) {
                if (!previousGroup.is($parent)) {
                    previousGroup = $parent;
                } else {
                    runGroup = false;
                    $fieldErrors = $lastGroupErrors;
                }
            }
        }

        // Clear error and success classes from field and parent
        function xfieldClearStatus() {
            $fieldErrors = [];
            $field.removeClass(settings.fieldSuccessClass).removeClass(settings.fieldErrorClass);
            $parent.removeClass(settings.blockSuccessClass).removeClass(settings.blockErrorClass);
            $parent.find('.xfields-error').remove();
        }

        // Set Error Class on field and parent
        function xfieldSetError() {
            $field.addClass(settings.fieldErrorClass);
            $parent.addClass(settings.blockErrorClass)
        }

        // Set Success Class on field and parent
        function xfieldSetSuccess() {
            $field.addClass(settings.fieldSuccessClass);
            $parent.addClass(settings.blockSuccessClass);
        }

        // Apply Mast taken from attribute
        function applyDataMaskByAttribute($field) {
            $dataMask = $($field).attr('data-mask-validate');
            $dataMaskOptions = $($field).attr('data-mask-validate-options');
            if ($dataMaskOptions) {
                $dataMaskOptions = JSON.parse($dataMaskOptions);
                $dataMaskOptions.onComplete = defaultMaskOptions.onComplete;
                $dataMaskOptions.onChange = defaultMaskOptions.onChange;
                $($field).mask($dataMask, $dataMaskOptions);
            } else {
                $($field).mask($dataMask, defaultMaskOptions);
            }
            $field.data('hasMask', true);
        }

        // Apply Mask
        function applyDataMask($field, $dataMask, $dataMaskOptions) {
            if ($dataMaskOptions) {
                $field.mask($dataMask, $dataMaskOptions);
            } else {
                $field.mask($dataMask, defaultMaskOptions);
            }
            $field.data('hasMask', true);
        }

        // Get and return date from field for comparision
        function getDate(field) {
            if (field.val())
                return new Date(field.datepicker('getDate'))
            else
                return false;
        }

        // Checks and matches allowed file types
        function checkFileTypes(filesArray) {
            tempV1 = 0;
            allowedFiles = $field.attr('data-allowed-fields') ? $.makeArray($field.attr('data-allowed-fields').split(',')) : settings.allowedFileTypes;
            if (allowedFiles != '*') {
                for (tempV1 = 0; tempV1 < filesArray.length; tempV1++) {
                    if (allowedFiles.indexOf(filesArray[tempV1].name.substr((filesArray[tempV1].name.lastIndexOf('.') + 1 ))) == -1) {
                        return true;
                    }
                }
            }
            return false;
        }

        // Validate a Field
        function validateField(paramField) {
            $form = $(paramField).closest('form');
            // Skipping disabled fields

            if (($(paramField).attr('disabled') && settings.skipDisabled) || $(paramField).attr('data-skip-field')) {
                return;
            }

            $field = $(paramField);
            $fieldType = $field.attr('type');
            $parent = $field.closest('.' + settings.parentsClass);
            $parent.addClass('val-processed');

            if (!$fieldType) {
                $fieldType = $field.prop("tagName").toLowerCase();
            }

            xfieldClearStatus();
            switch ($fieldType) {
                case 'button':
                    skipField();
                    break;
                case 'submit':
                    skipField();
                    break;
                case 'reset':
                    skipField();
                    break;
                case 'hidden':
                    skipField();
                    break;
                case 'range':
                    skipField();
                    break;
                case 'radio':
                    validateRadio();
                    break;
                case 'checkbox':
                    validateCheckbox();
                    break;
                case 'select':
                    validateSelect();
                    break;
                case 'textarea':
                    validateTextField();
                    break;
                default:
                    validateTextField();
            }
            return false;
        }

        // Validate Radios
        function validateRadio() {
            if ($parent.attr('data-skip-field'))
                return;

            radioGroupRequired = radioCheckCount = null;
            radioGroupRequired = $parent.attr('data-group-required');
            radioCheckCount = $parent.find('input[type=radio]:checked').length;

            checkGroupRepeats();
            if (radioGroupRequired && runGroup) {
                if (!radioCheckCount) {
                    $fieldErrors.push(setErrorFromGroup('data-error-required', settings.errorRequiredFieldRadio));
                }
                $lastGroupErrors = $fieldErrors;
            }
            processErrors();
        }

        // Validate Checkbox
        function validateCheckbox() {
            checkGroupRequired = checkedCount = checkGroupMin = checkGroupMax = null;

            if ($field.attr('required')) {
                if (!$field.prop('checked')) {
                    $fieldErrors.push(setError('data-error-required', null, settings.errorRequiredField, false));
                }
            }

            // Checkbox group
            if ($parent.hasClass('checkbox-group')) {

                if ($parent.attr('data-skip-field'))
                    return;

                checkGroupRepeats();
                if (runGroup) {
                    checkGroupRequired = $parent.attr('data-group-required');
                    checkedCount = $parent.find('input[type=checkbox]:checked').length;
                    checkGroupMin = $parent.attr('data-checkbox-min');
                    checkGroupMax = $parent.attr('data-checkbox-max');
                    if (checkGroupRequired || checkedCount) {
                        if (!checkedCount) {
                            $fieldErrors.push(setErrorFromGroup('data-error-required', settings.errorRequiredFieldCheck));
                        } else {
                            if (checkedCount < checkGroupMin) {
                                $fieldErrors.push(setError('data-abcedf', checkGroupMin, setErrorFromGroup('data-error-checkbox-min', settings.errorCheckMin), false));
                            }
                            if (checkedCount > checkGroupMax) {
                                $fieldErrors.push(setError('data-abcedf', checkGroupMax, setErrorFromGroup('data-error-checkbox-max', settings.errorCheckMax), false));
                            }
                        }
                    }
                    $lastGroupErrors = $fieldErrors;
                }
            }
            processErrors();
        }

        // Validate Select
        function validateSelect() {
            if ($field.attr('required')) {

                $selectMin = $selectMax = $selectCount = null;
                $val = $field.val();

                if (!$val) {
                    $fieldErrors.push(setError('data-error-required', null, settings.errorRequiredFieldSelect, false));
                }
                if ($field.prop('multiple')) {
                    $selectMin = $field.attr('data-select-min');
                    $selectMax = $field.attr('data-select-max');
                    $selectCount = $val.length;
                    if (!$selectCount) {
                        $fieldErrors.push(setError('data-error-required', null, settings.errorRequiredFieldSelect, false));
                    } else {
                        if ($selectCount < $selectMin) {
                            $fieldErrors.push(setError('data-error-select-min', $selectMin, settings.errorSelectMin, false));
                        }
                        if ($selectCount > $selectMax) {
                            $fieldErrors.push(setError('data-error-select-max', $selectMax, settings.errorSelectMax, false));
                        }
                    }
                }
            }
            processErrors();
        }

        // Validate Text Field
        function validateTextField() {
            xfieldClearStatus();
            $checkEndDate = false;

            if ($field.attr('required') || $field.val().trim()) {

                $valLength = $val = $numVal = $maxLength = $minLength = $maxNumber = $dateField = $minNumber = $pattern = $patternCheck = $matchField = $matchFieldParent = $matchField = $maxWords = $minWords = $wordsCount = maxFiles = minFiles = allowedFiles = $hasMask = null;
                $val = $field.val().trim();
                $valLength = $val.length;


                // Error if theres no value and skip all other validation in that case
                if (!$valLength) {
                    $fieldErrors.push(setError('data-error-required', null, settings.errorRequiredField, false));
                } else {

                    // Attribute values
                    $numVal = parseInt($val);
                    $wordsCount = $val.match(/\S+/g).length;
                    $maxLength = parseInt($field.attr('maxlength'));
                    $minLength = parseInt($field.attr('data-minlength'));
                    $maxNumber = parseInt($field.attr('max'));
                    $minNumber = parseInt($field.attr('min'));
                    $maxWords = parseInt($field.attr('data-max-words'));
                    $minWords = parseInt($field.attr('data-min-words'));
                    $dateField = $field.hasClass(settings.datepickerClass);
                    $pattern = $field.attr('regex');
                    $matchField = $field.attr('data-field-match');
                    $matchFieldParent = $field.attr('data-field-match-parent');
                    $hasMask = $field.data('hasMask');
                    $patternCheck = false;


                    // Error if exceedes max length
                    if ($maxLength) {
                        if ($valLength > ($maxLength)) {
                            $fieldErrors.push(setError('data-error-maxlength', $maxLength, settings.errorMaxLength, false));
                        }
                    }

                    // Error if less then min length
                    if ($minLength) {
                        if ($valLength < ($minLength)) {
                            $fieldErrors.push(setError('data-error-minlength', $minLength, settings.errorMinLength, false));
                        }
                    }

                    // Specific to number field only
                    if ($fieldType == 'number') {

                        if ($maxNumber) {
                            if ($numVal > $maxNumber) {
                                $fieldErrors.push(setError('data-error-max', $maxNumber, settings.errorMaxNumber, false));
                            }
                        }

                        if ($minNumber) {
                            if ($numVal < $minNumber) {
                                $fieldErrors.push(setError('data-error-min', $minNumber, settings.errorMinNumber, false));
                            }
                        }
                    }

                    // Specific to email address only
                    if ($fieldType == 'email') {
                        if (!$pattern) {
                            validEmail = $val.match(emailPattern)
                            if (!validEmail) {
                                $fieldErrors.push(setError('data-error-email', null, settings.errorEmail, false));
                            }
                        } else {
                            $pattern = new RegExp($pattern);
                            patternFound = $val.match($pattern)
                            if (!patternFound) {
                                $fieldErrors.push(setError('data-error-email', null, settings.errorEmail, false));
                            }
                            $patternCheck = true;
                        }
                    }

                    // SPECIFIC TO URL FIELDS
                    if ($fieldType == 'url') {
                        if (!$pattern) {
                            validURL = $val.match(urlPattern)
                            if (!validURL) {
                                $fieldErrors.push(setError('data-error-url', null, settings.errorURL, false));
                            }
                        } else {
                            $pattern = new RegExp($pattern);
                            patternFound = $val.match($pattern)
                            if (!patternFound) {
                                $fieldErrors.push(setError('data-error-url', null, settings.errorURL, false));
                            }
                            $patternCheck = true;
                        }
                    }


                    // pattern validation (reg ex)
                    if ($pattern && !$patternCheck) {
                        $pattern = new RegExp($pattern);
                        patternFound = $val.match($pattern)
                        if (!patternFound) {
                            $fieldErrors.push(setError('data-error-regex', null, settings.errorRegex, false));
                        }
                    }

                    if ($maxWords) {
                        if ($wordsCount > $maxWords) {
                            $fieldErrors.push(setError('data-error-max-words', $maxWords, settings.errorMaxWords, false));
                        }
                    }

                    if ($minWords) {
                        if ($wordsCount < $minWords) {
                            $fieldErrors.push(setError('data-error-min-words', $minWords, settings.errorMinWords, true));
                        }
                    }

                    // Check if field matches specied field
                    if ($matchField) {
                        if ($val != $('#' + $matchField).val()) {
                            $fieldErrors.push(setError('data-error-match', null, settings.errorMatch, false));
                        }
                    }

                    if ($hasMask) {
                        if ($field.hasClass('mask-complete')) {
                        } else {
                            $fieldErrors.push(setError('data-error-mask', null, settings.errorMask, false));
                        }
                    }


                    // Date field functions
                    if ($dateField) {
                        $dateEnd = $dateStart = $date = $checkEndDate = null;
                        $dateEnd = $field.attr('data-date-end');
                        $dateStart = $field.attr('data-date-start');
                        $date = getDate($field)
                        $checkEndDate = false;


                        if ($dateStart) {
                            tempV0 = $('#' + $dateStart);
                            $dateStart = (tempV0.length ? getDate(tempV0) : null);
                            if ($dateStart) {
                                if ($date < $dateStart)
                                    $fieldErrors.push(setError('data-error-enddate', null, settings.errorEndDate, false));
                            }
                        }

                        if ($dateEnd) {
                            $dateEnd = $('#' + $dateEnd);
                            $checkEndDate = true;
                        }
                    }

                    // SPECIFIT TO FILE FIELDS ONLY
                    if ($fieldType == 'file') {
                        filesArray = [];

                        errorSizeUnit = minSize = maxSize = null;
                        maxSizeCheck = minSizeCheck = false;


                        maxSize = parseInt($field.attr('data-max-size-file'));
                        minSize = parseInt($field.attr('data-min-size-file'));

                        maxFiles = parseInt($field.attr('data-max-files'));
                        minFiles = parseInt($field.attr('data-min-files'));

                        filesArray = $.map($field.prop("files"), function (val) {

                            if(maxSize && val.size > maxSize){
                                maxSizeCheck = true;
                            }

                            if(minSize && val.size < minSize){
                                minSizeCheck = true;
                            }

                            return {'name':val.name,'size':val.size};
                        });


                        if (maxFiles) {
                            if (filesArray.length > maxFiles) {
                                $fieldErrors.push(setError('data-error-max-files', maxFiles, settings.errorMaxFiles, false));
                            }
                        }
                        if (minFiles) {
                            if (filesArray.length < minFiles) {
                                $fieldErrors.push(setError('data-error-min-files', minFiles, settings.errorMinFiles, false));
                            }
                        }

                        if (checkFileTypes(filesArray)) {
                            $fieldErrors.push(setError('data-error-format-files', allowedFiles, settings.errorFormatFiles, false));
                        }

                        if(maxSizeCheck){
                            $fieldErrors.push(setError('data-error-max-size-files', maxSize, settings.errorMaxSizeFile, false));
                        }

                        if(minSizeCheck){
                            $fieldErrors.push(setError('data-error-min-size-files', minSize, settings.errorMinSizeFile, false));
                        }


                    }

                }

                processErrors();

                // ON CHANGING THE PARENT OF MATCHING FIELDS CHECK THE CHILD
                if ($matchFieldParent) {
                    $field = $('#' + $matchFieldParent);
                    $fieldType = $field.attr('type');
                    $parent = $field.closest('.' + settings.parentsClass);
                    validateTextField();
                }

                // check end date field
                if ($checkEndDate) {
                    $field = $dateEnd;
                    $fieldType = $field.attr('type');
                    $parent = $field.closest('.' + settings.parentsClass);
                    validateTextField();
                }

            }
        }


        if (settings.validateSingleField) {
            // Validate a single Field

            $field = $(this);
            $form = $(this).closest('form');
            validateField($field);
            if ($field.hasClass(settings.fieldErrorClass))
                return false;
            else
                return true;


        } else if (settings.runMasker || settings.mask) {
            // Apply masker on single field or set mask on all fields on the page

            var input, mask;
            if (this.is(document)) {
                $(document).find(':input').each(function () {
                    input = $(this);
                    mask = input.attr('data-mask-validate');
                    if (mask) {
                        applyDataMaskByAttribute(input);
                    }
                })
            } else {
                this.each(function () {

                    applyDataMask($(this), settings.mask, settings.maskOptions);

                });
            }
        } else {

            // Iterate and validate each matched element.
            return this.each(function () {


                $form = $($(this)[0]);
                $form.attr('novalidate', '');
                setRequiredClasses();

                if ($form.find('.xfield-last-field').length) {
                    settings.enableLastFieldFix = false;
                    $form.find('.xfield-last-field').after('<input type="button" data-skip-field class="xfield-focus-fix"/>');
                }


                if (settings.validateOnSubmit) {
                    $form.submit(submitFunc);
                }

                if (settings.labelAsterisk) {
                    $form.find('input,select ,textarea').each(function () {
                        if ($(this).prop('required')) {
                            $(this).closest('.' + settings.parentsClass).find('label:first-child').append('<span class="' + settings.asteriskClass + '">*</span>');
                        }
                    });
                }

                if (settings.validateOnFocusOut) {
                    validateFocus();
                }

                if (settings.validateOnChange)
                    validateChange();

                if (settings.validateOnKey)
                    validateKey();

                if (settings.validateOnId) {
                    if (multipleForms == 1) {
                        $('#' + settings.validateOnId).data("attachedForm", $form).click(function () {
                            $form = $(this).data('attachedForm').submit(settings.submitFunc).submit();
                        });
                    } else {
                        console.log('Error: can not use validateOnId with multiple forms, validate a single form instead.');
                    }
                }

                if (settings.validateOnClass) {
                    if (multipleForms == 1) {
                        $('.' + settings.validateOnClass).data("attachedForm", $form).click(function () {
                            $form = $(this).data('attachedForm').submit(settings.submitFunc).submit();
                        });
                    } else {
                        console.log('Error: can not use validateOnClass with multiple forms, validate a single form instead.');
                    }
                }

                if (settings.showErrorsGrouped) {
                    // Finding Errors Area
                    // Id has more priority if it specified
                    // ID or class are both first searached inside the form, if not found then searched on whole page

                    $formErrorsArea = null;
                    if (settings.formAreaId) {
                        if ($form.find('#' + settings.formAreaId).length) {
                            //$formErrorsArea = $form.find('#' + settings.formAreaId);
                            //$formErrorsArea.data('reserved', true);
                            tempV0 = $form.find('#' + settings.formAreaId);
                            tempV1 = 0;
                            while (tempV1 < tempV0.length) {
                                if (!$(tempV0[tempV1]).data('reserved')) {
                                    $formErrorsArea = $(tempV0[tempV1]);
                                    $formErrorsArea.data('reserved', true);
                                    break;
                                }
                                tempV1++;
                            }

                        } else if ($('#' + settings.formAreaId).length) {

                            tempV0 = $('#' + settings.formAreaId);
                            tempV1 = 0;
                            while (tempV1 < tempV0.length) {
                                if (!$(tempV0[tempV1]).data('reserved')) {
                                    $formErrorsArea = $(tempV0[tempV1]);
                                    $formErrorsArea.data('reserved', true);
                                    break;
                                }
                                tempV1++;
                            }

                        }

                        if (!$formErrorsArea) {
                            $form.prepend('<div id="' + settings.formAreaId + '"></div>');
                            $formErrorsArea = $form.find('#' + settings.formAreaId);
                            $formErrorsArea.data('reserved', true);
                        }


                    } else {

                        if ($form.find('.' + settings.formAreaClass).length) {
                            tempV0 = $form.find('.' + settings.formAreaClass);
                            tempV1 = 0;
                            while (tempV1 < tempV0.length) {
                                if (!$(tempV0[tempV1]).data('reserved')) {
                                    $formErrorsArea = $(tempV0[tempV1]);
                                    $formErrorsArea.data('reserved', true);
                                    break;
                                }
                                tempV1++;
                            }

                        } else if ($('.' + settings.formAreaClass).length) {
                            tempV0 = $('.' + settings.formAreaClass);
                            tempV1 = 0;
                            while (tempV1 < tempV0.length) {
                                if (!$(tempV0[tempV1]).data('reserved')) {
                                    $formErrorsArea = $(tempV0[tempV1]);
                                    $formErrorsArea.data('reserved', true);
                                    break;
                                }
                                tempV1++;
                            }
                        }

                        if (!$formErrorsArea) {
                            $form.prepend('<div class="' + settings.formAreaClass + '"></div>');
                            $formErrorsArea = $form.find('.' + settings.formAreaClass);
                            $formErrorsArea.data('reserved', true);
                        }
                    }


                    // Assigning id to all fields and creating areas for each field
                    tempV0 = null;
                    tempV1 = 0;
                    $parent = $(null);
                    $form.find(':input').each(function (index) {
                        if ($(this).attr('type') == 'radio' || $(this).attr('type') == 'checkbox') {
                            if (!$parent.is($(this).closest('.' + settings.parentsClass))) {
                                $parent = $(this).closest('.' + settings.parentsClass);
                                $(this).data('xfieldId', tempV1);
                                tempV0 = tempV1;
                                $formErrorsArea.append('<div class="' + settings.eachErrorClass + ' ' + settings.singleErrorCl + '-' + tempV1 + '">' + '</div>')
                                tempV1++;
                            } else {
                                $(this).data('xfieldId', tempV0);
                            }
                        } else {
                            $(this).data('xfieldId', tempV1)
                            $formErrorsArea.append('<div class="' + settings.eachErrorClass + ' ' + settings.singleErrorCl + '-' + tempV1 + '">' + '</div>')
                            tempV1++;
                        }
                    });
                    $form.data('xfield-errors', []);
                    $form.data('formErrorsArea', $formErrorsArea);

                }


            });

        }
    };


    // Default properties for the plugin

    $.fn.xfield_validator.defaults = {
                                                                // Options
        skipDisabled: true,
        validateOnSubmit: true,
        validateOnFocusOut: true,
        validateOnChange: true,
        validateOnKey: true,
        validateOnId: null,
        validateOnClass: null,
        complete: null,
        enableWCError: true,
        labelAsterisk: false,
        allowedFileTypes: '*',
        enableLastFieldFix: true,
        showErrorMessages: true,
                                                                // Errors Messages
        errorMaxLength: 'Error: Please enter maximum |x| characters',
        errorMinLength: 'Error: Please enter atleast |x| characters',
        errorMaxNumber: 'Error: Please enter a number less then or equal to |x|',
        errorMinNumber: 'Error: Please enter a number greater then or equal to |x| ',
        errorRequiredField: 'Error: This field is required',
        errorRequiredFieldSelect: 'Error: This field is required',
        errorRequiredFieldCheck: 'Error: This field is required',
        errorRequiredFieldRadio: 'Error: This field is required',
        errorRegex: 'Error: Incorrect Format used',
        errorEmail: 'Error: Incorrect Email Address',
        errorURL: 'Error: Incorrect URL',
        errorMatch: 'Fields dont match',
        errorMaxWords: 'Error: Please enter maximum |x| words',
        errorMinWords: 'Error: Please enter atleast |x| words',
        errorSelectMin: 'Minimum |x| values allowed',
        errorSelectMax: 'Maximum |x| options allowed',
        errorCheckMax: 'Maximum |x| checks allowed',
        errorCheckMin: 'Minimum |x| checks allowed',
        errorMaxFiles: 'Maximum |x| Files allowed',
        errorMinFiles: 'Minimum |x| Files allowed',
        errorFormatFiles: 'Wrong files format Only, |x| are allowed',
        errorEndDate: 'End date is less then starting date',
        errorMask: 'Incomplete Mask',
        errorMaxSizeFile: 'Max |x| Bytes file size allowed',
        errorMinSizeFile: 'Min |x| Bytes file size allowed',
                                                                // Error & Success Classes
        fieldErrorClass: 'xfield-error-field',
        fieldSuccessClass: 'xfield-success-field',
        blockErrorClass: 'xfield-error-block',
        blockSuccessClass: 'xfield-success-block',
        eachErrorClass: 'xfields-error',
        formErrorClass: 'xfields-error-form',
        formSuccessClass: 'xfields-success-form',
                                                                // More Classes
        parentsClass: 'form-group',
        asteriskClass: 'req-asterisk',
        datepickerClass: 'datepicker',
                                                                //  Form Area Settings
        showErrorsGrouped: false,
        formAreaClass: 'xfield-form-errors',
        formAreaId: false,
        singleErrorCl: 'field-error',
                                                                // Singular methods
        validateSingleField: false,
                                                                // Masking
        runMasker: false,
        mask: null,
        maskOptions: null,

    };

    $(document).xfield_validator({
        runMasker: true
    })

}(jQuery));
