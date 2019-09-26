package co.techmagic.hr.presentation.ui.bottom_nav;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import co.techmagic.hr.R;

public class BottomNavigationSetup {

    public static final int NAV_INDEX_TIME = 0;
    public static final int NAV_INDEX_TEAM = 1;
    public static final int NAV_INDEX_CALENDAR = 2;
    public static final int NAV_INDEX_PROFILE = 3;

    private LinearLayout bottomNavTime;
    private LinearLayout bottomNavTeam;
    private LinearLayout bottomNavCalendar;
    private LinearLayout bottomNavProfile;

    private final int selectedColor;
    private final int unSelectedColor;

    private final BottomNavigationSelectCallback selectCallback;

    public BottomNavigationSetup(View bottomNavigationLayout, BottomNavigationSelectCallback selectCallback) {
        final Context context = bottomNavigationLayout.getContext();

        this.selectedColor = ContextCompat.getColor(context, R.color.pine_green);
        this.unSelectedColor = ContextCompat.getColor(context, R.color.color_time_report_disabled_text);

        this.selectCallback = selectCallback;

        this.bottomNavTime = bottomNavigationLayout.findViewById(R.id.bottomNavTime);
        this.bottomNavTeam = bottomNavigationLayout.findViewById(R.id.bottomNavTeam);
        this.bottomNavCalendar = bottomNavigationLayout.findViewById(R.id.bottomNavCalendar);
        this.bottomNavProfile = bottomNavigationLayout.findViewById(R.id.bottomNavProfile);

        this.bottomNavTime.setOnClickListener(v -> selectTab(NAV_INDEX_TIME));
        this.bottomNavTeam.setOnClickListener(v -> selectTab(NAV_INDEX_TEAM));
        this.bottomNavCalendar.setOnClickListener(v -> selectTab(NAV_INDEX_CALENDAR));
        this.bottomNavProfile.setOnClickListener(v -> selectTab(NAV_INDEX_PROFILE));
    }

    public void selectTab(int index) {
        selectTabUi(index);
        selectCallback.onTabSelected(index);
    }

    public void selectTabUi(int tabIndex) {
        setBottomNavItemTint(bottomNavTime, tabIndex == NAV_INDEX_TIME ? selectedColor : unSelectedColor);
        setBottomNavItemTint(bottomNavTeam, tabIndex == NAV_INDEX_TEAM ? selectedColor : unSelectedColor);
        setBottomNavItemTint(bottomNavCalendar, tabIndex == NAV_INDEX_CALENDAR ? selectedColor : unSelectedColor);
        setBottomNavItemTint(bottomNavProfile, tabIndex == NAV_INDEX_PROFILE ? selectedColor : unSelectedColor);
    }

    private void setBottomNavItemTint(LinearLayout item, int selectedColor) {
        ImageView imageView = (ImageView) item.getChildAt(0);
        imageView.setImageTintList(ColorStateList.valueOf(selectedColor));

        TextView textView = (TextView) item.getChildAt(1);
        textView.setTextColor(selectedColor);
    }

}
