package com.example.tao.taskmgr.dummy;

import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();


    public static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.pidStr, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public int pid;
        public String pidStr;
        public String processName;
        //public String packageName;
        public String cpuUsage;
        public String memoryUsage;
        public String batteryUsage;
        public String details;

        public DummyItem(int pid) {
            this.pid = pid;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("Process Name:\t").append(processName).append("\n")
                    //.append("Package Name:\t").append(packageName).append("\n")
                    .append("PID:\t").append(pidStr).append("\n")
                    .append("CPU Usage:\t").append(cpuUsage).append("\n")
                    .append("Memory Usage:\t").append(memoryUsage).append("\n")
                    .append("Battery Usage:\t").append(batteryUsage).append("\n");
            return builder.toString();
        }

    }
}
