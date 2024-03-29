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

package jahirfiquitiva.apps.iconshowcase.sample.zooper;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.FileNotFoundException;

public class TemplateProvider extends ContentProvider {

    public int delete(@NonNull Uri paramUri, String paramString, String[] paramArrayOfString) {
        return 0;
    }

    public String getType(@NonNull Uri paramUri) {
        return null;
    }

    public Uri insert(@NonNull Uri paramUri, ContentValues paramContentValues) {
        return null;
    }

    public boolean onCreate() {
        return false;
    }

    public AssetFileDescriptor openAssetFile(@NonNull Uri paramUri, @NonNull String paramString)
            throws FileNotFoundException {
        if (paramUri.getPathSegments().size() > 0)
            try {
                if (getContext() == null) return null;
                final String name = paramUri.getPath().substring(1);
                return getContext().getAssets().openFd(name);
            } catch (Throwable localThrowable) {
                return null;
            }
        return null;
    }

    public Cursor query(@NonNull Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2) {
        MatrixCursor cursor = new MatrixCursor(new String[]{"string"});
        try {
            if (getContext() == null) return cursor;
            final String path = paramUri.getPath().substring(1);
            final String[] items = getContext().getAssets().list(path);
            for (String s : items) {
                cursor.newRow().add(s);
                cursor.moveToNext();
            }
            cursor.moveToFirst();
        } catch (Exception e) {
            cursor.close();
            throw new RuntimeException(e);
        }
        return cursor;
    }

    public int update(@NonNull Uri paramUri, ContentValues paramContentValues, String paramString, String[] paramArrayOfString) {
        return 0;
    }
}