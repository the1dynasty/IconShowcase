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

package jahirfiquitiva.iconshowcase.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import jahirfiquitiva.iconshowcase.R;
import jahirfiquitiva.iconshowcase.models.FAQsItem;

public class FAQsAdapter extends RecyclerView.Adapter<FAQsAdapter.FAQsHolder> {

    private List<FAQsItem> faqs;
    private Context context;

    public FAQsAdapter(List<FAQsItem> faqs, Context context) {
        this.faqs = faqs;
        this.context = context;
    }

    @Override
    public FAQsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new FAQsHolder(
                inflater.inflate(context.getResources().getBoolean(R.bool.faqs_cards) ?
                        R.layout.card_faq :
                        R.layout.item_faq, parent, false));
    }

    @Override
    public void onBindViewHolder(FAQsHolder holder, int position) {

        FAQsItem faq = faqs.get(position);

        holder.txtQuestion.setText(faq.getQuestion());
        holder.txtAnswer.setText(faq.getAnswer());

        holder.view.setTag(position);

    }

    @Override
    public int getItemCount() {
        return faqs == null ? 0 : faqs.size();
    }

    class FAQsHolder extends RecyclerView.ViewHolder {

        final View view;
        LinearLayout layout;
        CardView card;
        TextView txtQuestion;
        TextView txtAnswer;

        FAQsHolder(View v) {
            super(v);
            view = v;
            if (context.getResources().getBoolean(R.bool.faqs_cards)) {
                card = (CardView) v.findViewById(R.id.faq_card);
            } else {
                layout = (LinearLayout) v.findViewById(R.id.faq_card);
            }
            txtAnswer = (TextView) v.findViewById(R.id.faq_answer);
            txtQuestion = (TextView) v.findViewById(R.id.faq_question);
        }
    }

}
