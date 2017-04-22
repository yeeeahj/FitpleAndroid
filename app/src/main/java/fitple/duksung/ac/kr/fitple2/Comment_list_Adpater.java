package fitple.duksung.ac.kr.fitple2;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by YEJIN on 2016-10-05.
 */
public class Comment_list_Adpater  extends RecyclerView.Adapter<Comment_list_Adpater.ViewHolder> implements View.OnClickListener{

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.comment_item, parent, false);



        return new ViewHolder(itemView, viewType);
    }

    @Override
    public void onBindViewHolder(Comment_list_Adpater.ViewHolder holder, int position) {

        //date fromat
        SimpleDateFormat format = new SimpleDateFormat("HH:mm EEE");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            holder.comment_date.setText( format.format(format2.parse(CommentActivity.commentList.get(position).getDate())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.comment_nickname.setText(CommentActivity.commentList.get(position).getNickname());
        holder.comment_comment.setText(CommentActivity.commentList.get(position).getComment());
        holder.comment_seq = CommentActivity.commentList.get(position).getComment_seq();
        holder.member_seq = CommentActivity.commentList.get(position).getMember_seq();
        Log.d("position",""+position);



    }

    @Override
    public int getItemCount() {
        Log.d("comment count",""+CommentActivity.commentList.size());
        return CommentActivity.commentList.size();
    }

    @Override
    public void onClick(View v) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView comment_profile;
        TextView comment_nickname;
        TextView comment_comment;
        TextView comment_date;
        int comment_seq;
        int member_seq;



        public ViewHolder(View itemView, int viewType){
            super(itemView);
            comment_profile= (ImageView)itemView.findViewById(R.id.comment_profile);
            comment_nickname= (TextView)itemView.findViewById(R.id.comment_nickname);
            comment_comment= (TextView)itemView.findViewById(R.id.comment_comment);


            comment_date= (TextView)itemView.findViewById(R.id.comment_date);



        }
        @Override
        public void onClick(View v) {

        }
    }

    public void viewRefresh(){
        notifyDataSetChanged();
        notifyItemInserted(CommentActivity.commentList.size()+1);
    }
}
