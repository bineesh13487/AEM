

var onloadCallback = function() {
    grecaptcha.render('g-recaptcha-container', {
        'sitekey' : $('.google_captcha_key').val()
    });
};


$(document).on('click', '.submit', function() {

    $.ajax({
        url: $('.resource_path').val() + '?g-recaptcha-response=' + $('#g-recaptcha-response').val(),
        type: 'POST',
        success: function(response) {
            console.log(response);
        },
        error: function() {
            console.log("error");
        }
    });
});