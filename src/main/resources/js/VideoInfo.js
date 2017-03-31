/**
 * Created by xiyuan_fengyu on 2017/3/31.
 */
loadJQuery(function () {

    window.scrollTo(0, 9999);

    var info = {};
    info.title = $(".v-title h1").attr("title");
    info.dianji = $("#dianji").text();
    info.danmu = $("#dm_count").text();
    info.yingbi = $("#v_ctimes").text();
    info.shoucang = $("#stow_count").text();
    info.links = links(".*/video/av[0-9]+.*");

    sendResult(info);

});