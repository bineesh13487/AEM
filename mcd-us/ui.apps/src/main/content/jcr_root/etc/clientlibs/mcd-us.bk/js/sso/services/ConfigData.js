ssoApp.service('config', function () {
    var configElement = angular.element('#js-sso-config');
    var configData = (configElement.length > 0) ? angular.fromJson(configElement.data('json')) : {};

    var _modeKeys = {
            edit: 'edit',
            preview: 'preview',
            publish: 'publish',
            design: 'design',
            unknown: 'unknown'
        };
    var inEditor = false;
    var inEditorOrPreview = false;
    var AEMMode = _modeKeys.publish;

    if (typeof CQ === 'object' && typeof CQ.WCM === 'object') {
        if (typeof CQ.WCM.isEditMode === 'function' && CQ.WCM.isEditMode()) {
            AEMMode = _modeKeys.edit;
            inEditor = true;
            inEditorOrPreview = true;
        } else if (typeof CQ.WCM.isPreviewMode === 'function' && CQ.WCM.isPreviewMode()) {
            AEMMode = _modeKeys.preview;
            inEditor = false;
            inEditorOrPreview = true;
        } else if (typeof CQ.WCM.isDesignMode === 'function' && CQ.WCM.isDesignMode()) {
            AEMMode = _modeKeys.design;
            inEditor = true;
            inEditorOrPreview = true;
        } else {
            AEMMode = _modeKeys.unknown;
            inEditor = true;
            inEditorOrPreview = true;
        }
    }

    angular.extend(configData, {
        inEditor: inEditor,
        inEditorOrPreview: inEditorOrPreview,
        AEMMode: AEMMode
    });

    this.get = function (item) {
        if (typeof item === 'string') {
            if (configData.hasOwnProperty(item)) {
                return configData[item];
            }
            return;
        }
        return configData;
    };
});
