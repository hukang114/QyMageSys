package com.qymage.sys.common.util;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 类名：
 * 类描述：定时任务类
 * 创建人：J.S
 * 修改人：J.S
 * 创建时间：2019/9/2017:25
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0.0
 */
public class MyTimeTask {
    private Timer timer;
    private TimerTask task;
    private long time;

    public MyTimeTask(long time, TimerTask task) {
        this.task = task;
        this.time = time;
        if (timer == null) {
            timer = new Timer();
        }
    }

    public void start() {
        timer.schedule(task, 0, time);//每隔time时间段就执行一次
    }

    public void stop() {
        if (timer != null) {
            timer.cancel();
            if (task != null) {
                task.cancel();  //将原任务从队列中移除
            }
        }
    }

}
