
CQ.WCM.on("editablesready", function() {
    if($('.nav-tabs a').length) {
        $('.nav-tabs a').on('shown.bs.tab', function(event){
            handleAuthoring('.tab-content .tab-pane');
        });

        $('.nav-tabs a:first').trigger("shown.bs.tab");
    }
});
