$( document ).ready(function() {
    /*$(window).resize(function() {
        var $i = $('img#backimg');
        var $c = $img.parent();
        var i_ar = $i.width() / $i.height(), c_ar = $c.width() / $c.height();            
        $i.width(i_ar > c_ar ? $c.width() : $c.height() * (i_ar));
    });
    $(window).resize();*/

     //initialize swiper when document ready  
     var swiper = new Swiper('.swiper-container', {
        pagination: '.swiper-pagination',
        effect: 'coverflow',
        grabCursor: true,
        centeredSlides: true,
        slidesPerView: 'auto',
        coverflow: {
            rotate: 50,
            stretch: 0,
            depth: 100,
            modifier: 1,
            slideShadows : true
        }
    });    
});