app.filter( 'CharEllipsisFilter', function() {
    return function( input,length ) {
          if (input.length > length){
            input= input.replace(/<[^>]*>/g, '');
            return  input.substring(0,length) + '...';
            }
            return input;
  }
});