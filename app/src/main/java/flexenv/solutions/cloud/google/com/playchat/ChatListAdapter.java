package flexenv.solutions.cloud.google.com.playchat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * Created by JOEL on 09/02/2018.
 */

public class ChatListAdapter extends BaseAdapter
{
    private Activity mactivity;
    private DatabaseReference mdatabaseReference;
    private  String mdisplayName;
    private ArrayList<DataSnapshot> snapshots;
    private ChildEventListener childEventListener=new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            snapshots.add(dataSnapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    public ChatListAdapter(Activity activity,DatabaseReference databaseReference,String displayName)
    {
        mactivity=activity;
        mdatabaseReference=databaseReference.child("messages");
        mdisplayName=displayName;
        mdatabaseReference.addChildEventListener(childEventListener);
        snapshots=new ArrayList<>();

    }
    static class ViewHolder{
        TextView authorname;
        TextView body;
        LinearLayout.LayoutParams params;
    }

    @Override
    public int getCount() {
        return snapshots.size();
    }

    @Override
    public InstantMessage getItem(int i) {
        DataSnapshot snapshot1=snapshots.get(i);
        return snapshot1.getValue(InstantMessage.class);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null)
        {
            LayoutInflater inflater=(LayoutInflater)mactivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.chat_msg_row,viewGroup,false);
            final ViewHolder holder=new ViewHolder();
            holder.authorname=(TextView)view.findViewById(R.id.author);
            holder.body=(TextView)view.findViewById(R.id.imessage);
            holder.params=(LinearLayout.LayoutParams)holder.authorname.getLayoutParams();
            view.setTag(holder);
        }
        final InstantMessage message=getItem(i);
        final ViewHolder holder=(ViewHolder)view.getTag();
        boolean isMe=message.getAuthor().equals(mdisplayName);
        setChatRowAppearance(isMe,holder);
        String author=message.getAuthor();
        holder.authorname.setText(author);
        String messagetext=message.getMessage();
        holder.body.setText(messagetext);
        return view;
    }
    public  void setChatRowAppearance(boolean isItMe,ViewHolder holder)
    {
        if(isItMe)
        {
            holder.params.gravity= Gravity.END;
            holder.authorname.setTextColor(Color.GREEN);
            holder.body.setBackgroundResource(R.drawable.bubble2);
        }
        else
        {
            holder.params.gravity= Gravity.START;
            holder.authorname.setTextColor(Color.BLUE);
            holder.body.setBackgroundResource(R.drawable.bubble1);
        }
        holder.authorname.setLayoutParams(holder.params);
        holder.body.setLayoutParams(holder.params);
    }

    public void cleanUp()
    {
        mdatabaseReference.removeEventListener(childEventListener);
    }
}
