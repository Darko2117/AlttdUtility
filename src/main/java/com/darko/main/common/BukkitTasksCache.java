package com.darko.main.common;

import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class BukkitTasksCache {

    private static final List<BukkitTask> runningTasks = new ArrayList<>();

    public static void cancelRunningTasks() {
        for (BukkitTask task : runningTasks) {
            task.cancel();
        }
    }

    public static void addTask(BukkitTask task) {
        runningTasks.add(task);
    }

}
