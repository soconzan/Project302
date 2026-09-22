$(document).ready(function() {
    let pos = 0;
    const totalDivs = $('.img-count > div').length - 1;

    $('.img-count-dot').eq(0).addClass('sel');

    function updateDots() {
        $('.img-count-dot').removeClass('sel');
        $('.img-count-dot').eq(-pos).addClass('sel');
    }

    $('.img-next').click(function() {
        if (pos > -totalDivs) {
            pos -= 1;
            $('#imgContainer > div').css('transform', 'translateX(' + (500 * pos) + 'px)');
            updateDots();
        }
    });

    $('.img-prev').click(function() {
        if (pos < 0) {
            pos += 1;
            $('#imgContainer > div').css('transform', 'translateX(' + (500 * pos) + 'px)');
            updateDots();
        }
    });
});