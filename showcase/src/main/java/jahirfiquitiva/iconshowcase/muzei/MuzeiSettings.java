/*
 * Copyright (c) 2016.  Jahir Fiquitiva
 *
 * Licensed under the CreativeCommons Attribution-ShareAlike
 * 4.0 International License. You may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *    http://creativecommons.org/licenses/by-sa/4.0/legalcode
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Big thanks to the project contributors. Check them in the repository.
 *
 */

/*
 *
 */

package jahirfiquitiva.iconshowcase.muzei;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.RadioButton;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import jahirfiquitiva.iconshowcase.R;
import jahirfiquitiva.iconshowcase.dialogs.ISDialogs;
import jahirfiquitiva.iconshowcase.utilities.Preferences;
import jahirfiquitiva.iconshowcase.utilities.ThemeUtils;
import jahirfiquitiva.iconshowcase.utilities.Utils;
import jahirfiquitiva.iconshowcase.utilities.color.ToolbarColorizer;
import jahirfiquitiva.iconshowcase.views.CustomCoordinatorLayout;
import jahirfiquitiva.iconshowcase.views.FixedElevationAppBarLayout;

public class MuzeiSettings extends AppCompatActivity implements View.OnClickListener {

    private RadioButton minute, hour;
    private NumberPicker numberpicker;
    private Preferences mPrefs;
    private boolean mLastTheme, mLastNavBar;
    private Context context;
    private CustomCoordinatorLayout customCoordinatorLayout;
    private Toolbar toolbar;
    private static final String MARKET_URL = "https://play.google.com/store/apps/details?id=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ThemeUtils.onActivityCreateSetTheme(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ThemeUtils.onActivityCreateSetNavBar(this);
        }

        super.onCreate(savedInstanceState);

        context = this;

        int iconsColor = ThemeUtils.darkTheme ?
                ContextCompat.getColor(this, R.color.toolbar_text_dark) :
                ContextCompat.getColor(this, R.color.toolbar_text_light);

        setContentView(R.layout.muzei_settings);

        mPrefs = new Preferences(MuzeiSettings.this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        customCoordinatorLayout = (CustomCoordinatorLayout) findViewById(R.id.muzeiLayout);
        customCoordinatorLayout.setScrollAllowed(false);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        collapsingToolbarLayout.setCollapsedTitleTextColor(iconsColor);
        collapsingToolbarLayout.setTitle(Utils.getStringFromResources(this, R.string.muzei_settings));

        FixedElevationAppBarLayout appBarLayout = (FixedElevationAppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(false, false);

        numberpicker = (NumberPicker) findViewById(R.id.number_picker);
        numberpicker.setMaxValue(100);
        numberpicker.setMinValue(1);

        setDividerColor(numberpicker);

        minute = (RadioButton) findViewById(R.id.minute);
        hour = (RadioButton) findViewById(R.id.hour);

        if (mPrefs.areFeaturesEnabled()) {

            minute.setOnClickListener(this);
            hour.setOnClickListener(this);

            if (mPrefs.isRotateMinute()) {
                hour.setChecked(false);
                minute.setChecked(true);
                numberpicker.setValue(Utils.convertMillisToMinutes(mPrefs.getRotateTime()));
            } else {
                hour.setChecked(true);
                minute.setChecked(false);
                numberpicker.setValue(Utils.convertMillisToMinutes(mPrefs.getRotateTime()) / 60);
            }
        } else {
            showNotLicensedDialog();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.muzei, menu);
        MenuItem save = menu.findItem(R.id.save);
        int iconsColor = ThemeUtils.darkTheme ?
                ContextCompat.getColor(this, R.color.toolbar_text_dark) :
                ContextCompat.getColor(this, R.color.toolbar_text_light);
        ToolbarColorizer.tintSaveIcon(save, this, iconsColor);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (mPrefs.areFeaturesEnabled()) {
            if (i == R.id.save) {
                int rotate_time;
                if (minute.isChecked()) {
                    rotate_time = Utils.convertMinutesToMillis(numberpicker.getValue());
                    mPrefs.setRotateMinute(true);
                    mPrefs.setRotateTime(rotate_time);
                } else {
                    rotate_time = Utils.convertMinutesToMillis(numberpicker.getValue()) * 60;
                    mPrefs.setRotateMinute(false);
                    mPrefs.setRotateTime(rotate_time);
                }
                Intent intent = new Intent(MuzeiSettings.this, ArtSource.class);
                intent.putExtra("service", "restarted");
                startService(intent);
                Utils.showSimpleSnackbar(context, customCoordinatorLayout,
                        Utils.getStringFromResources(this, R.string.settings_saved), 1);
                finish();
                return true;
            }
        } else {
            showNotLicensedDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mLastTheme = ThemeUtils.darkTheme;
        mLastNavBar = ThemeUtils.coloredNavBar;
    }

    @Override
    protected void onResume() {
        super.onResume();
        int iconsColor = ThemeUtils.darkTheme ?
                ContextCompat.getColor(this, R.color.toolbar_text_dark) :
                ContextCompat.getColor(this, R.color.toolbar_text_light);
        ToolbarColorizer.colorizeToolbar(toolbar, iconsColor);
        if (mLastTheme != ThemeUtils.darkTheme
                || mLastNavBar != ThemeUtils.coloredNavBar) {
            this.recreate();
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (mPrefs.areFeaturesEnabled()) {
            if (i == R.id.minute) {
                if (minute.isChecked()) {
                    hour.setChecked(false);
                    minute.setChecked(true);
                }
            } else if (i == R.id.hour) {
                if (hour.isChecked()) {
                    minute.setChecked(false);
                    hour.setChecked(true);
                }
            }
        } else {
            showNotLicensedDialog();
        }
    }

    private void setDividerColor(NumberPicker picker) {
        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    pf.set(picker, ContextCompat.getDrawable(this, R.drawable.numberpicker));
                } catch (IllegalArgumentException | IllegalAccessException | Resources.NotFoundException e) {
                    //Do nothing
                }
                break;
            }
        }
    }

    private void showNotLicensedDialog() {
        ISDialogs.showLicenseFailDialog(this,
                new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_URL + getPackageName()));
                        startActivity(browserIntent);
                    }
                }, new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        finish();
                    }
                }, new MaterialDialog.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                }, new MaterialDialog.OnDismissListener() {

                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                    }
                });
    }
}