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

package jahirfiquitiva.iconshowcase.utilities.glide;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.GlideModule;

import jahirfiquitiva.iconshowcase.R;

public class GlideConfiguration implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        boolean runsMinSDK = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
        boolean enableHDWalls = context.getResources().getBoolean(R.bool.high_definition_walls);

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        boolean lowRAMDevice = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            lowRAMDevice = activityManager.isLowRamDevice();
        }

        if (runsMinSDK) {
            if (lowRAMDevice) {
                builder.setDecodeFormat(DecodeFormat.PREFER_RGB_565);
            } else {
                if (enableHDWalls) {
                    builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
                } else {
                    builder.setDecodeFormat(DecodeFormat.PREFER_RGB_565);
                }
            }
        } else {
            builder.setDecodeFormat(DecodeFormat.PREFER_RGB_565);
        }
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        // register ModelLoaders here.
    }
}
