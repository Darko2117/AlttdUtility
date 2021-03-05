package com.darko.main.common;

import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class BukkitTasksCache {

    static List<BukkitTask> tasksToCancel = new ArrayList<>();

    public static void cancelRunningTasks() {

        for (BukkitTask task : tasksToCancel) {

            task.cancel();

        }

    }

    public static void addTask(BukkitTask task) {
        tasksToCancel.add(task);
    }

}
