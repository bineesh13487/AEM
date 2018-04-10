ssoApp.service('backendValidationParser', function () {


    this.parseFieldErrorDetails = function (details, profileObject) {
        var results = [];
        var i;
        var _detail;
        var _type;
        var _id;
        var _field;
        var _tree;

        for (i = 0; i < details.length; i++) {
            _detail = details[i];
            _tree = _detail.Path.split('/');
            if (_detail.Field === 'number') {
                _id = _tree[3];
                _type = profileObject.profile.base.phone[_id].type;
                if (_type == 'mobile') {
                    results.push('mobileNumber');
                } else if (_type == 'home') {
                    results.push('phoneNumber');
                }
            } else if (_detail.Field === 'zipCode') {
                _id = _tree[3];
                _type = profileObject.profile.base.address[_id].addressType;
                _field = 'postalCode';
                if (_type == 'mailing1') {
                    results.push('mailingAddress_' + _field);
                    results.push('postalCode');
                } else if (_type == 'mailing2') {
                    results.push('mailingAddress2_' + _field);
                } else if (_type == 'billing') {
                    results.push('billingAddress_' + _field);
                } else if (_type == 'delivery') {
                    results.push('deliveryAddress_' + _field);
                }
            } else if (_detail.Field === 'emailAddress') {
                results.push('email');
            } else {
                results.push(_detail.Field);
            }

        }
        return results;
    };
    
});