$( document ).ready(function() {
    $(window).resize(function() {
        var $i = $('img#image_to_resize');
        var $c = $img.parent();
        var i_ar = $i.width() / $i.height(), c_ar = $c.width() / $c.height();            
        $i.width(i_ar > c_ar ? $c.width() : $c.height() * (i_ar));
    });
    $(window).resize();

});