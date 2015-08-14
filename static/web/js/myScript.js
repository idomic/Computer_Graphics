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

function myFunction(str){
    document.getElementById("myImg").src = str;
    document.getElementById("flb-close").click();

    };


$(function () {
    $(":file").change(function () {
        if (this.files && this.files[0]) {
            var reader = new FileReader();
            reader.onload = imageIsLoaded;
            reader.readAsDataURL(this.files[0]);
        }
    });
});

function imageIsLoaded(e) {
    $('#myImg').attr('src', e.target.result);
};    