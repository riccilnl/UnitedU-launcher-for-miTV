package com.riccilnl.mitv.kids;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/**
 * A normal Android TV app shortcut to Xiaomi's original HOME "儿童" tab.
 * No account, watch-history, recommendations or parental-control data is accessed.
 */
public final class KidsChannelShortcutActivity extends Activity {
    public static final String STOCK_PACKAGE = "com.mitv.tvhome";
    public static final String STOCK_ALIAS = "com.mitv.tvhome.MainActivity";
    public static final String HOME_PAGE_ACTION = "com.mitv.tvhome.HOME_PAGE";
    public static final String PAGE_EXTRA = "page";
    public static final String KIDS_TAB = "儿童";

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Intent open = new Intent(HOME_PAGE_ACTION);
            open.setComponent(new ComponentName(STOCK_PACKAGE, STOCK_ALIAS));
            open.putExtra(PAGE_EXTRA, KIDS_TAB);
            open.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(open);
        } catch (ActivityNotFoundException | SecurityException ex) {
            Toast.makeText(this, "无法打开小米儿童频道：原厂桌面不可用", Toast.LENGTH_LONG).show();
        } finally {
            finish();
        }
    }
}
