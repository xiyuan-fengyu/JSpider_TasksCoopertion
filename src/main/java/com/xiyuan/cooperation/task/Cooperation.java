package com.xiyuan.cooperation.task;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.xiyuan.spider.annotation.*;
import com.xiyuan.spider.filter.BloonFilter;
import com.xiyuan.spider.manager.QueueManager;
import com.xiyuan.spider.queue.DefaultQueue;

/**
 * Created by xiyuan_fengyu on 2017/3/31.
 */
@Task
public class Cooperation {

    //启动时执行
    @OnStart(name = "获取B站首页中的所有视屏地址", url = "http://www.bilibili.com/", js = "js/AllLinks.js", timeout = 30)
    //每个1小时执行一次
    @OnTime(name = "获取B站首页中的所有视屏地址", cron = "0 0 */1 * * ? ", url = "http://www.bilibili.com/", js = "js/AllLinks.js", timeout = 30)
    //通过注解的方式，将所有视频地址添加到 bilibili_videos 整个队列中， 队列类型为 DefaultQueue， 通过 BloonFilter 来过滤已处理的地址
    @AddToQueue(name = "bilibili_videos", type = DefaultQueue.class, filter = BloonFilter.class)
    public JsonArray links(String url, JsonElement result) {
        return result.getAsJsonArray();
    }

    //早上6:30设置并行数为5，凌晨0:30设置并行数为10
    @OnMessage(name = "获取一个视屏的信息", fromQueue = "bilibili_videos", js = "js/VideoInfo.js", parallelConfig = "{'0 30 6 * * ? ': 5, '0 30 0 * * ? ': 10}", timeout = 30)
    public void videoInfo(String url, JsonElement result) {
        JsonObject videoInfo = result.getAsJsonObject();

        JsonArray urlsJA = videoInfo.get("links").getAsJsonArray();
        // 通过QueueManager.addToQueue往队列添加任务，和@AddToQueue注解等效，
        // 但是@AddToQueue只适用于往单个队列中添加任务，
        // QueueManager.addToQueue可以多次调用，往不同的队列添加任务
        // 添加任务的数据类型可以是： String, com.xiyuan.spider.message.Message（及其子类），Iterable<String>, Iterable<com.xiyuan.spider.message.Message>, String[], com.xiyuan.spider.message.Message[]
        QueueManager.addToQueue(urlsJA, "bilibili_videos", DefaultQueue.class, BloonFilter.class);

        videoInfo.remove("links");
        System.out.println(videoInfo);
    }

}
