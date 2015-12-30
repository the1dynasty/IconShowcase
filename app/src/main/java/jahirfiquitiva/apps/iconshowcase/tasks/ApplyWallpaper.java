package jahirfiquitiva.apps.iconshowcase.tasks;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.melnykov.fab.FloatingActionButton;

import java.io.IOException;
import java.lang.ref.WeakReference;

import jahirfiquitiva.apps.iconshowcase.R;
import jahirfiquitiva.apps.iconshowcase.utilities.Util;

/**
 * Created by JAHIR on 09/07/2015.
 */
public class ApplyWallpaper extends AsyncTask<Void, String, Boolean> {
    private Context context;
    private Activity activity;
    private MaterialDialog dialog;
    private Bitmap resource;
    private View layout;
    private boolean isPicker;
    private Snackbar snackbar;
    private FloatingActionButton fab;

    private WeakReference<Activity> wrActivity;

    public ApplyWallpaper(Activity activity, MaterialDialog dialog, Bitmap resource, Boolean isPicker,
                          View layout, FloatingActionButton fab) {
        this.wrActivity = new WeakReference<Activity>(activity);
        this.dialog = dialog;
        this.resource = resource;
        this.isPicker = isPicker;
        this.layout = layout;
        this.fab = fab;
    }

    @Override
    protected void onPreExecute() {
        final Activity a = wrActivity.get();
        if (a != null) {
            this.context = a.getApplicationContext();
            this.activity = a;
        }
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        WallpaperManager wm = WallpaperManager.getInstance(context);
        Boolean worked;
        try {
            try {
                wm.setBitmap(scaleToActualAspectRatio(resource));
            } catch (OutOfMemoryError ex) {
                Util.showLog("OutOfMemoryError: " + ex.getLocalizedMessage());
                showRetrySnackbar();
            }
            worked = true;
        } catch (IOException e2) {
            worked = false;
        }
        return worked;
    }

    @Override
    protected void onPostExecute(Boolean worked) {
        if (worked) {
            dialog.dismiss();
            Snackbar longSnackbar = Snackbar.make(layout,
                    context.getString(R.string.set_as_wall_done), Snackbar.LENGTH_LONG);
            longSnackbar.show();
            longSnackbar.setCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar snackbar, int event) {
                    super.onDismissed(snackbar, event);
                    fab.show(true);
                }
            });
        } else {
            showRetrySnackbar();
        }
        if (isPicker) {
            activity.finish();
        }

    }

    public Bitmap scaleToActualAspectRatio(Bitmap bitmap) {
        if (bitmap != null) {
            boolean flag = true;
            int deviceWidth = activity.getWindowManager().getDefaultDisplay()
                    .getWidth();
            int deviceHeight = activity.getWindowManager().getDefaultDisplay()
                    .getHeight();
            int bitmapHeight = bitmap.getHeight();
            int bitmapWidth = bitmap.getWidth();
            if (bitmapWidth > deviceWidth) {
                flag = false;
                int scaledHeight = deviceHeight;
                int scaledWidth = (scaledHeight * bitmapWidth) / bitmapHeight;
                try {
                    if (scaledHeight > deviceHeight)
                        scaledHeight = deviceHeight;
                    bitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth,
                            scaledHeight, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (flag) {
                if (bitmapHeight > deviceHeight) {
                    int scaledHeight = deviceHeight;
                    int scaledWidth = (scaledHeight * bitmapWidth)
                            / bitmapHeight;
                    try {
                        if (scaledWidth > deviceWidth)
                            scaledWidth = deviceWidth;
                        bitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth,
                                scaledHeight, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return bitmap;
    }

    private void showRetrySnackbar() {
        String retry = context.getResources().getString(R.string.retry);
        snackbar = Snackbar
                .make(layout, R.string.error, Snackbar.LENGTH_INDEFINITE)
                .setAction(retry.toUpperCase(), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new ApplyWallpaper((Activity) context, dialog, resource, isPicker, layout, fab);
                    }
                });
        snackbar.setActionTextColor(context.getResources().getColor(R.color.accent));
        snackbar.show();
    }

}