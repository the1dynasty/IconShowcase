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

package jahirfiquitiva.apps.iconshowcase.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private static boolean WITH_LICENSE_CHECKER = false,
            WITH_INSTALLED_FROM_AMAZON = false,
            WITH_DONATIONS_SECTION = true,
            DONATIONS_GOOGLE = false,
            DONATIONS_PAYPAL = true,
            DONATIONS_FLATTR = false,
            DONATIONS_BITCOIN = false;

    private static String GOOGLE_PUBKEY = "PUT_YOUR_KEY_HERE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Intent intent = new Intent(HomeActivity.this, jahirfiquitiva.iconshowcase.activities.ShowcaseActivity.class);
        intent.putExtra("license_check", WITH_LICENSE_CHECKER);
        intent.putExtra("allow_installs_from_amazon", WITH_INSTALLED_FROM_AMAZON);
        intent.putExtra("with_donations_section", WITH_DONATIONS_SECTION);

        if (WITH_DONATIONS_SECTION) {
            intent.putExtra("google_method", DONATIONS_GOOGLE);
            if (DONATIONS_GOOGLE) {
                intent.putExtra("pubkey", GOOGLE_PUBKEY);
            }
            intent.putExtra("paypal_method", DONATIONS_PAYPAL);
            intent.putExtra("flattr_method", DONATIONS_FLATTR);
            intent.putExtra("bitcoin_method", DONATIONS_BITCOIN);
        }

        startActivity(intent);

        finish();

    }

}