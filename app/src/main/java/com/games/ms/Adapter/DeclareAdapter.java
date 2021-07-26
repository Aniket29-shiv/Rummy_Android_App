    package com.games.ms.Adapter;

    import android.content.Context;
    import android.content.SharedPreferences;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ImageView;
    import android.widget.LinearLayout;
    import android.widget.RelativeLayout;
    import android.widget.TextView;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;

    import com.squareup.picasso.Picasso;
    import com.games.ms.R;
    import com.games.ms.model.CardModel;
    import com.games.ms.model.Funtions;

    import java.util.ArrayList;

    import static android.content.Context.MODE_PRIVATE;
    import static com.games.ms.Homepage.MY_PREFS_NAME;
    import static com.games.ms.Rummy2Player.removeLastChars;
    import static com.games.ms.model.Funtions.convertDpToPixel;

    public class DeclareAdapter extends RecyclerView.Adapter<DeclareAdapter.myholder> {

        Context context;
        ArrayList<CardModel> arrayList;

        public DeclareAdapter(Context context, ArrayList<CardModel> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_declare,parent,false);
            return new myholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull myholder holder, int position) {

            CardModel model = arrayList.get(position);


            if(model.user_name != null && !model.user_name.equals(""))
            {
                ((TextView) holder.itemView.findViewById(R.id.tv_username)).setText(""+arrayList.get(position).user_name);
            }
            else {
                ((TextView) holder.itemView.findViewById(R.id.tv_username)).setText(""+arrayList.get(position).user_id);
            }

            ((TextView) holder.itemView.findViewById(R.id.txtresult)).setText("Wrong Declare");
            ((TextView) holder.itemView.findViewById(R.id.tv_score)).setText(""+model.score);
            ((TextView) holder.itemView.findViewById(R.id.tv_win)).setText(""+model.won);

            SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);


            if(model.user_id.equalsIgnoreCase(prefs.getString("user_id", "")))
            {
                holder.itemView.findViewById(R.id.tv_you).setVisibility(View.VISIBLE);
            }
            else {
                holder.itemView.findViewById(R.id.tv_you).setVisibility(View.INVISIBLE);
            }

            if(model.user_id.equals(model.winner_user_id))
            {
                holder.itemView.findViewById(R.id.lnr_list).setBackgroundColor(context.getResources().getColor(R.color.blackbg));
                holder.itemView.findViewById(R.id.imgplwinner).setVisibility(View.VISIBLE);
                holder.itemView.findViewById(R.id.txtresult).setVisibility(View.GONE);

                ChangeTextViewColor(((TextView)holder.itemView.findViewById(R.id.tv_username)),"white");
                ChangeTextViewColor(((TextView)holder.itemView.findViewById(R.id.txtresult)),"white");
                ChangeTextViewColor(((TextView)holder.itemView.findViewById(R.id.tv_score)),"white");
                ChangeTextViewColor(((TextView)holder.itemView.findViewById(R.id.tv_win)),"white");

            }
            else
            {
                holder.itemView.findViewById(R.id.lnr_list).setBackgroundColor(context.getResources().getColor(R.color.white));
                holder.itemView.findViewById(R.id.imgplwinner).setVisibility(View.INVISIBLE);
                holder.itemView.findViewById(R.id.txtresult).setVisibility(View.VISIBLE);



                ChangeTextViewColor(((TextView)holder.itemView.findViewById(R.id.tv_username)),"black");
                ChangeTextViewColor(((TextView)holder.itemView.findViewById(R.id.txtresult)),"black");
                ChangeTextViewColor(((TextView)holder.itemView.findViewById(R.id.tv_score)),"black");
                ChangeTextViewColor(((TextView)holder.itemView.findViewById(R.id.tv_win)),"black");


            }


            if(arrayList.get(position).groups_cards != null)
            {
                grouplist_size = arrayList.get(position).groups_cards.size();
                for (int i = 0; i < arrayList.get(position).groups_cards.size() ; i++) {

                    ArrayList<CardModel> cardModelArrayList = arrayList.get(position).groups_cards.get(i).groups_cards;

                    CreateGroups(cardModelArrayList,i,holder.lnr_declareview,
                            arrayList.get(position).joker_card);

                }
            }

        }

        private void ChangeTextViewColor(TextView textView,String colors){

            if(colors.equalsIgnoreCase("white"))
            {
                textView.setTextColor(context.getResources().getColor(R.color.white));
            }
            else {
                textView.setTextColor(context.getResources().getColor(R.color.blackbg));
            }


        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }


        class myholder extends RecyclerView.ViewHolder{

            LinearLayout lnr_declareview;

            public myholder(@NonNull View itemView) {
                super(itemView);

                lnr_declareview = itemView.findViewById(R.id.lnr_declareview);

            }
        }

        int grouplist_size = 0;
        private void CreateGroups(ArrayList<CardModel> cardImageList,int count,LinearLayout rlt_addcardview,String joker_card) {

            View view = LayoutInflater.from(context).inflate(R.layout.item_grouplayout,  null);
            TextView tv_status = view.findViewById(R.id.tv_status);
            ImageView iv_status = view.findViewById(R.id.iv_status);
            LinearLayout lnr_group_card = view.findViewById(R.id.lnr_group_card);
            RelativeLayout rlt_icons = view.findViewById(R.id.rlt_icons);
            rlt_icons.setVisibility(View.GONE);

            tv_status.setText(""+cardImageList.get(0).group_value);
            lnr_group_card.setTag(""+cardImageList.get(0).value_grp);




            for (int i = 0; i < cardImageList.size() ; i++) {

                AddCardtoGroup(cardImageList.get(i).Image,i,lnr_group_card,cardImageList,joker_card);

            }



            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            int valueInPixels = (int) context.getResources().getDimension(R.dimen.margin_left_group);


//        if(grouplist_size >= 4)
//        {
            valueInPixels = 10 * grouplist_size;
//        }

            if(grouplist_size == 1)
                valueInPixels = 20;

            int leftmargin = 0;
            if(count == 0)
            {
                leftmargin = (int) convertDpToPixel(valueInPixels,context);
            }

            layoutParams.setMargins((int) leftmargin, (int) convertDpToPixel(0,context), 0, 0);

            ViewGroup.LayoutParams params = rlt_addcardview.getLayoutParams();

            rlt_addcardview.setLayoutParams(params);
            rlt_addcardview.requestLayout();

            rlt_addcardview.addView(view,layoutParams);

        }

        private void AddCardtoGroup(String image_card , final int countnumber, LinearLayout addlayout,
                                    final ArrayList<CardModel> arrayList,String joker_card) {

            View view = LayoutInflater.from(context).inflate(R.layout.item_card_win,  null);
            ImageView imgcards = view.findViewById(R.id.imgcard);
            final ImageView iv_jokercard = view.findViewById(R.id.iv_jokercard);

            view.setTag(image_card+"");

            String src_joker_cards = "";
            src_joker_cards = joker_card.substring(joker_card.length() - 1);

            if(src_joker_cards != null && !src_joker_cards.equals(""))
            {
                if(src_joker_cards.contains(image_card.substring(image_card.length() - 1)))
                {

                    iv_jokercard.setVisibility(View.VISIBLE);

                }
                else {
                    iv_jokercard.setVisibility(View.GONE);
                }
            }


            String imagename = image_card;
            if(image_card.contains("id")) {
                imagename = image_card.substring(11);
            }

            Funtions.LOGE("DeclareAdapter","imagename : "+imagename);

            if(imagename.substring(imagename.length() - 1).equalsIgnoreCase("_"))
            {
                imagename = removeLastChars(imagename,1);
            }


            String uri1 = "@drawable/" + imagename.toLowerCase();  // where myresource " +
            int imageResource1 = context.getResources().getIdentifier(uri1, null,
                    context.getPackageName());
            Picasso.with(context).load(imageResource1).into(imgcards);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);


            layoutParams.setMargins(0, (int) convertDpToPixel(0,context), (int) convertDpToPixel(-25,context), 0);


            ViewGroup.LayoutParams params = addlayout.getLayoutParams();



            if (countnumber==0){
                params.width = (int) convertDpToPixel(43,context);
            }
            else if(countnumber == 1)
            {
                params.width = (int) convertDpToPixel(50,context) * countnumber;
            }
            else if(countnumber == 2)
            {
                params.width = (int) convertDpToPixel(33,context) * countnumber;
            }
            else if(countnumber == 3)
            {
                params.width = (int) convertDpToPixel(27,context) * countnumber;
            }
            else if(countnumber == 5)
            {
                params.width = (int) convertDpToPixel(23,context) * countnumber;
            }
            else if(countnumber == 6)
            {
                params.width = (int) convertDpToPixel(18,context) * countnumber;
            }
            else {
                params.width = (int) convertDpToPixel(23,context) * countnumber;
            }



            addlayout.setLayoutParams(params);
            addlayout.requestLayout();

            addlayout.addView(view,layoutParams);

        }

        private void setMargins (View view, int left, int top, int right, int bottom) {
            if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                p.setMargins(left, top, right, bottom);
                view.requestLayout();

//            Funtions.LOGE("MainAcitvity","Left : "+left);

            }

        }


    }
