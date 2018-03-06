package com.demo_get_ids.pulkit.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.demo_get_ids.pulkit.R;
import com.demo_get_ids.pulkit.adapters.TotalAppsInstalledInfoAdapter;
import com.demo_get_ids.pulkit.models.AppsName;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TotalAppsInstalledInfoActivity extends AppCompatActivity {

    private RecyclerView rv_total_installed_apps;
    private List<AppsName> addNewItemList;
    private TotalAppsInstalledInfoAdapter installedInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_apps_installed_info);

        //Todo: pending to give permissions of phone and storage
        findIds();
        init();

    }

    private void findIds() {

        rv_total_installed_apps = (RecyclerView) findViewById(R.id.rv_total_installed_apps);
    }

    private void init() {

        rv_total_installed_apps.setLayoutManager(new LinearLayoutManager(getApplication()));
        AllAppsInstalledInPhone();
    }

    private void AllAppsInstalledInPhone() {

        addNewItemList = new ArrayList<>();
        addNewItemList.clear();

        addNewItemList = getInstalledApps();

        Collections.sort(addNewItemList, new Comparator<AppsName>() {
            @Override
            public int compare(AppsName appsName1, AppsName appsName2) {
                return (appsName1.getName().compareTo(appsName2.getName()));
            }
        });

        installedInfoAdapter = new TotalAppsInstalledInfoAdapter(getApplicationContext(), addNewItemList, new TotalAppsInstalledInfoAdapter.onClickListener() {
            @Override
            public void onClickButton(int position, int view, AppsName appsName) {
                //Todo: on click information
            }
        });
        rv_total_installed_apps.setAdapter(installedInfoAdapter);
    }

    private List<AppsName> getInstalledApps() {
        double apkSize = 0;
        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);

        ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> pidsTask = activityManager.getRunningAppProcesses();

//        for (int i = 0; i < pidsTask.size(); i++) {
//            String name = pidsTask.get(i).processName;
//            int pid = pidsTask.get(i).uid;
//
//            ActivityManager.RunningAppProcessInfo info = pidsTask.get(i);
//
//            double cpu = getCPUUsage(info.pid);
////            readCpuUsage(info.pid);
//            System.out.println(pid + "");
//        }

        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if ((isSystemPackage(p) == false)) {
                String appName = p.applicationInfo.loadLabel(getPackageManager()).toString();
                Drawable icon = p.applicationInfo.loadIcon(getPackageManager());
                String packageName = ((ApplicationInfo) p.applicationInfo).packageName;

                int pId = ((ApplicationInfo) p.applicationInfo).uid;

//                double cpu = getCPUUsage(info.pid);
                try {
                    apkSize = getApkSize(getApplicationContext(), packageName);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                addNewItemList.add(new AppsName(appName, icon, apkSize));
            } else {
                String appName = p.applicationInfo.loadLabel(getPackageManager()).toString();
                System.out.println(appName);
            }
        }
        return addNewItemList;
    }

    public double getCPUUsage(int pid) {
        Process process;
        String line = null;
        double cpuPer = 0;

        try {
            String[] cmd = {
                    "sh",
                    "-c",
                    "top -m 1000 -d 1 -n 1 | grep \"" + pid + "\" "};
            process = Runtime.getRuntime().exec(cmd);

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

            while ((line = stdInput.readLine()) != null) {
                if (line.contains("demo_get_ids")) {
                    String [] arr = line.split(" ");
                    for (int i = 0; i < arr.length; i++) {
                        if (arr[i].contains("%")) {
                            line = arr[i].replace("%", "");
                            cpuPer = Float.parseFloat(line);
                            break;
                        }
                    }
                    //System.out.println(s);
                }
            }

            line = stdInput.readLine();
        } catch (Exception e) {

        }
        return cpuPer;
    }


    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true : false;
    }

    public static long getApkSize(Context context, String packageName) throws PackageManager.NameNotFoundException {
        return new File(context.getPackageManager().getApplicationInfo(packageName, 0).publicSourceDir).length();
    }

    private float readCpuUsage(int pid) {
        try {
            RandomAccessFile reader = new RandomAccessFile("/proc/" + pid + "/stat", "r");
            String load = reader.readLine();

            String[] toks = load.split(" ");

            long idle1 = Long.parseLong(toks[5]);
            long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
                    + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

            try {
                Thread.sleep(360);
            } catch (Exception e) {
            }

            reader.seek(0);
            load = reader.readLine();
            reader.close();

            toks = load.split(" ");

            long idle2 = Long.parseLong(toks[5]);
            long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
                    + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

            return (float) (cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1));

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return 0;
    }

}
