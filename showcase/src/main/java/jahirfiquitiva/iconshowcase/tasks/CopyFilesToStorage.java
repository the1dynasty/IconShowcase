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

package jahirfiquitiva.iconshowcase.tasks;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Environment;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CopyFilesToStorage extends AsyncTask<Void, String, Boolean> {

    private Context context;
    private MaterialDialog dialog;
    private String folder;

    public CopyFilesToStorage(Context context, MaterialDialog dialog, String folder) {
        this.context = context;
        this.dialog = dialog;
        this.folder = folder;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Boolean worked;
        try {
            AssetManager assetManager = context.getAssets();
            String[] files = assetManager.list(folder);

            if (files != null) {
                for (String filename : files) {
                    InputStream in;
                    OutputStream out;
                    try {
                        in = assetManager.open(folder + "/" + filename);
                        out = new FileOutputStream(Environment.getExternalStorageDirectory().toString() + "/ZooperWidget/" + getFolderName(folder) + "/" + filename);
                        copyFiles(in, out);
                        in.close();
                        out.close();
                    } catch (Exception e) {
                        //Do nothing
                    }
                }
            }
            worked = true;
        } catch (Exception e2) {
            worked = false;
        }
        return worked;
    }

    @Override
    protected void onPostExecute(Boolean worked) {
        dialog.dismiss();
    }

    private void copyFiles(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[2048];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        out.flush();
    }

    private String getFolderName(String folder) {
        switch (folder) {
            case "fonts":
                return "Fonts";
            case "iconsets":
                return "IconSets";
            default:
                return folder;
        }
    }

}