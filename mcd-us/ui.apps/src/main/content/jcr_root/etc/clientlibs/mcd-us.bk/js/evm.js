
    //EVM image and text resize 
    function initEVMImageHeight() {
        var productImageHeight;
       
       
        $('.evm-cont img:visible').each(function() {
            if (!(!this.complete || typeof this.naturalWidth == "undefined" || this.naturalWidth == 0)) {
                productImageHeight = $(this).height();
                return false;
            }
        });
       
        $('.evm-cont img').height(productImageHeight);
      
    }

    $(document).ready(function() {
        if (window.innerWidth > 768) {
            initEVMImageHeight();
        }
    });
