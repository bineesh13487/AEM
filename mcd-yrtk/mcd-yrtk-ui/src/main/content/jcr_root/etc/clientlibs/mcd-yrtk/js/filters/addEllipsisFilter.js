yrtkApp.filter('addEllipsis', function() {
    return function(input, scope) {
        if (input.length > 200) {
            // Replace this with the real implementation
            return input.substring(0, 200) + '...';
        } else {
            return input;
        }
    }
});