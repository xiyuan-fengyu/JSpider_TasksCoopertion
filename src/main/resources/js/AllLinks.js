/**
 * Created by xiyuan_fengyu on 2017/3/31.
 */
loadJQuery(function () {

    var scrollY = 0;
    var winH = $(window).height();
    waitFor(function () {
        window.scrollTo(0, scrollY);
        scrollY += winH;
        return $(".container-row:last-child ul.rm-list li").length > 0;
    }, function () {
        sendResult(links(".*/video/av[0-9]+.*"));
    });

});