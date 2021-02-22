package com.od.makh.myapplication.Application;

import android.app.Application;

import com.od.makh.myapplication.Model.Category;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import wiadevelopers.com.library.DivarUtils;

public class G extends Application {

    public static final ArrayList<Category> CATEGORIES = new ArrayList<>();
    public static final String TAG = "MyDivarTag";

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(getApplicationContext());
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .build();
        Realm.getInstance(realmConfiguration);

        DivarUtils.init(getApplicationContext());
        setCategories();
    }

    private void setCategories()
    {

        CATEGORIES.add(new Category("1", "املاک",false));

        CATEGORIES.add(new Category("11", "فروش مسکونی",false));
        CATEGORIES.add(new Category("111", "آپارتمان",true));
        CATEGORIES.add(new Category("112", "خانه و ویلا",true));
        CATEGORIES.add(new Category("113", "زمین و کلنگی",true));

        CATEGORIES.add(new Category("12", "اجاره مسکونی",false));
        CATEGORIES.add(new Category("121", "آپارتمان",true));
        CATEGORIES.add(new Category("122", "خانه و ویلا",true));

        CATEGORIES.add(new Category("13", "فروش اداری و تجاری (مغازه، دفتر، صنعتی)",false));
        CATEGORIES.add(new Category("131", "دفتر کار، اتاق اداری، مطب",true));
        CATEGORIES.add(new Category("132", "مغازه و غرفه",true));
        CATEGORIES.add(new Category("133", "صنعتی، کشاورزی و تجاری",true));

        CATEGORIES.add(new Category("14", "اجاره اداری و تجاری (مغازه، دفتر، صنعتی)",false));
        CATEGORIES.add(new Category("141", "دفتر کار، اتاق اداری، مطب",true));
        CATEGORIES.add(new Category("142", "مغازه و غرفه",true));
        CATEGORIES.add(new Category("143", "صنعتی، کشاورزی و تجاری",true));

    }

    public static String getTitle(String id)
    {
        for (int i = 0; i <CATEGORIES.size() ; i++)
        {
            if (id.equalsIgnoreCase(CATEGORIES.get(i).getId()))
                return CATEGORIES.get(i).getTitle();
        }
        return null;
    }
}
