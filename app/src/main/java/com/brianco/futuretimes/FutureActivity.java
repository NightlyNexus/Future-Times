package com.brianco.futuretimes;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

import com.brianco.futuretimes.fragment.AboutFragment;
import com.brianco.futuretimes.fragment.CalendarFragment;
import com.brianco.futuretimes.fragment.ConnectFragment;

public class FutureActivity extends ActionBarActivity {

    private static final String KEY_READ_CHECKED = "KEY_READ_CHECKED";
    private static final String KEY_CALENDAR_CHECKED = "KEY_CALENDAR_CHECKED";
    private static final String KEY_ABOUT_CHECKED = "KEY_ABOUT_CHECKED";
    private static final String KEY_CONNECT_CHECKED = "KEY_CONNECT_CHECKED";

    private DrawerLayout mDrawerLayout;
    private LinearLayout mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar mToolBar;
    private CheckedTextView mReadView;
    private CheckedTextView mCalendarView;
    private CheckedTextView mAboutView;
    private CheckedTextView mConnectView;

    private void doDrawerClosed() {
        invalidateOptionsMenu();
    }

    private void doDrawerOpened() {
        invalidateOptionsMenu();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_future);
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (LinearLayout) findViewById(R.id.navigation_drawer);
        mReadView = (CheckedTextView) mDrawerList.findViewById(R.id.drawer_read);
        mCalendarView = (CheckedTextView) mDrawerList.findViewById(R.id.drawer_calendar);
        mAboutView = (CheckedTextView) mDrawerList.findViewById(R.id.drawer_about);
        mConnectView = (CheckedTextView) mDrawerList.findViewById(R.id.drawer_connect);
        final Drawable drawable = getResources().getDrawable(R.drawable.star_background);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            this.mToolBar.setBackground(drawable);
        } else {
            mToolBar.setBackgroundDrawable(drawable);
        }
        setSupportActionBar(mToolBar);
        setupNavigationDrawer();
        setClickListeners();
        if (savedInstanceState == null) {
            doReadClick();
        } else {
            doFragmentTitleChange();
            mReadView.setChecked(savedInstanceState.getBoolean(KEY_READ_CHECKED, false));
            mCalendarView.setChecked(savedInstanceState.getBoolean(KEY_CALENDAR_CHECKED, false));
            mAboutView.setChecked(savedInstanceState.getBoolean(KEY_ABOUT_CHECKED, false));
            mConnectView.setChecked(savedInstanceState.getBoolean(KEY_CONNECT_CHECKED, false));
        }
        getFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                        doFragmentTitleChange();
                    }
                });
    }

    private void doFragmentTitleChange() {
        final Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
        if ((fragment instanceof TitleSettable)) {
            ((TitleSettable) fragment).setTitle();
        }
    }

    private void doReadClick() {
        setTitle(mReadView.getText());
        mReadView.setChecked(true);
        mCalendarView.setChecked(false);
        mAboutView.setChecked(false);
        mConnectView.setChecked(false);
        changeContentFragment(new FutureFragment());
    }

    private void setClickListeners() {
        mReadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doReadClick();
            }
        });
        mCalendarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTitle(mCalendarView.getText());
                mCalendarView.setChecked(true);
                mReadView.setChecked(false);
                mAboutView.setChecked(false);
                mConnectView.setChecked(false);
                changeContentFragment(new CalendarFragment());
            }
        });
        mAboutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTitle(mAboutView.getText());
                mAboutView.setChecked(true);
                mReadView.setChecked(false);
                mCalendarView.setChecked(false);
                mConnectView.setChecked(false);
                changeContentFragment(new AboutFragment());
            }
        });
        mConnectView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTitle(mConnectView.getText());
                mConnectView.setChecked(true);
                mReadView.setChecked(false);
                mCalendarView.setChecked(false);
                mAboutView.setChecked(false);
                changeContentFragment(new ConnectFragment());
            }
        });
    }

    private void changeContentFragment(final Fragment fragment) {
        mDrawerLayout.closeDrawer(mDrawerList);
        // pop entire back stack
        getFragmentManager().popBackStackImmediate(
                null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        Fragment currFrag = getFragmentManager().findFragmentById(R.id.container);
        FragmentTransaction transaction = getFragmentManager().beginTransaction()
                .addToBackStack(null);
        if (currFrag != null) {
            transaction.remove(currFrag);
        }
        transaction
                .add(R.id.container, fragment)
                .commit();
    }

    private void setupNavigationDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolBar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                FutureActivity.this.doDrawerClosed();
            }

            @Override
            public void onDrawerOpened(View view) {
                super.onDrawerOpened(view);
                FutureActivity.this.doDrawerOpened();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                // disable hamburger-arrow animation
                super.onDrawerSlide(drawerView, 0);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.status_bar));
    }

    public void launchReaderFragment(Page page) {
        final Bundle bundle = new Bundle();
        bundle.putSerializable(ReaderFragment.KEY_PAGE, page);
        final ReaderFragment fragment = new ReaderFragment();
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .addToBackStack(null)
                .remove(getFragmentManager().findFragmentById(R.id.container))
                .add(R.id.container, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (mDrawerToggle.onOptionsItemSelected(menuItem)) {
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
        if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
            doDrawerOpened();
        } else {
            doDrawerClosed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(KEY_READ_CHECKED, mReadView.isChecked());
        outState.putBoolean(KEY_CALENDAR_CHECKED, mCalendarView.isChecked());
        outState.putBoolean(KEY_ABOUT_CHECKED, mAboutView.isChecked());
        outState.putBoolean(KEY_CONNECT_CHECKED, mConnectView.isChecked());
        super.onSaveInstanceState(outState);
    }
}
