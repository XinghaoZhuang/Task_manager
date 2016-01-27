package com.example.tao.taskmgr;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Debug.MemoryInfo;

import android.app.ActivityManager;
import android.content.pm.PackageManager;
import android.util.Log;

import com.example.tao.taskmgr.dummy.DummyContent;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.tao.taskmgr.dummy.DummyContent;
import com.example.tao.taskmgr.dummy.DummyContent.DummyItem;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity {
    private List<ProcessInfo> processInfoList = null;
    private ActivityManager mActivityManager = null;
    private MatrixMultiplication matrix;
    private double powerUsage;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private View recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        processInfoList = new ArrayList<ProcessInfo>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        matrix = new MatrixMultiplication();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.item_detail_container) != null) {
            mTwoPane = true;
            if(savedInstanceState != null){
                return;
            }

        }
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(DummyContent.ITEMS));
    }



    @Override
    public void onStart() {
        super.onStart();
        client.connect();

        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ItemList Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.tao.taskmgr/http/host/path")
        );

        getRunningAppProcessInfo();
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ItemList Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.tao.taskmgr/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<DummyContent.DummyItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<DummyContent.DummyItem> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).pidStr);
            holder.mContentView.setText(mValues.get(position).processName);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(ItemDetailFragment.ARG_ITEM_ID, holder.mItem.pidStr);
                        ItemDetailFragment fragment = new ItemDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ItemDetailActivity.class);
                        intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, holder.mItem.pidStr);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public DummyContent.DummyItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
    private long getAppProcessTime(int pid) {
        FileInputStream in = null;
        String ret = null;
        try {
            in = new FileInputStream("/proc/" + pid + "/stat");
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            ret = os.toString();
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (ret == null) {
            return 0;
        }

        String[] s = ret.split(" ");
        if (s == null || s.length < 17) {
            return 0;
        }

        long utime = string2Long(s[13]);
        long stime = string2Long(s[14]);
        long cutime = string2Long(s[15]);
        long cstime = string2Long(s[16]);

        return utime + stime + cutime + cstime;
    }


    private long string2Long(String s) {
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
        }
        return 0;
    }
    private void getRunningAppProcessInfo() {
        if (!DummyContent.ITEM_MAP.isEmpty())
            DummyContent.ITEM_MAP.clear();
        if (!DummyContent.ITEMS.isEmpty())
            DummyContent.ITEMS.clear();
        processInfoList = new ArrayList<ProcessInfo>();


        List<ActivityManager.RunningAppProcessInfo> appProcessList = mActivityManager
                .getRunningAppProcesses();

        long r_time = 0;
        try {
            r_time = matrix.main(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        double totalTime = 0.0;
        for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessList) {
            int pid = appProcessInfo.pid;
            double time = getAppProcessTime(pid);
            totalTime = totalTime + time;
        }
        //DecimalFormat df = new java.text.DecimalFormat("0.00");

        for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessList) {
            // 进程ID号
            int pid = appProcessInfo.pid;
            // 用户ID 类似于Linux的权限不同，ID也就不同 比如 root等
            //int uid = appProcessInfo.uid;
            double ratio = 100 * getAppProcessTime(pid) / totalTime;
            powerUsage = ratio;
            //String processName = appProcessInfo.processName;
            int[] myMempid = new int[] { pid };
            Debug.MemoryInfo[] memoryInfo = mActivityManager
                    .getProcessMemoryInfo(myMempid);
            int memSize = memoryInfo[0].dalvikPrivateDirty;
            DummyItem item = new DummyItem(appProcessInfo.pid);
            item.pidStr = String.valueOf(appProcessInfo.pid);
            item.processName = appProcessInfo.processName;
            //TODO
            ProcessInfo processInfo = new ProcessInfo();
            processInfo.pkgnameList = appProcessInfo.pkgList ;
            processInfoList.add(processInfo);;
            item.batteryUsage = String.format("%.2f", powerUsage) + "%";
            item.cpuUsage = String.valueOf(Math.rint(powerUsage)) + "%";
            item.memoryUsage = String.valueOf(memSize) + "kb";
            DummyContent.addItem(item);
        }
        ((RecyclerView) recyclerView).getAdapter().notifyDataSetChanged();
    }


}
