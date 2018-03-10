package flexenv.solutions.cloud.google.com.playchat;

import android.app.Activity;
import android.content.Context;
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



public class UserListAdapter extends BaseAdapter {
    private Activity mactivity;
    private DatabaseReference mdatabaseReference;
    private ArrayList<DataSnapshot> snapshots;
    private String mdisplayName;
    private ChildEventListener childEventListener=new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
           if(!mdisplayName.equals(dataSnapshot.toString())) {
               snapshots.add(dataSnapshot);
           }
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
    public UserListAdapter(Activity activity, DatabaseReference databaseReference,String displayName)
    {
        mactivity=activity;
        mdatabaseReference=databaseReference.child("user");
        mdisplayName=displayName;
        mdatabaseReference.addChildEventListener(childEventListener);
        snapshots=new ArrayList<>();

    }

    static class ViewHolder{

        TextView authorname;
        LinearLayout.LayoutParams params;
    }

    @Override
    public int getCount() {
        return snapshots.size();
    }

    @Override
    public User getItem(int i) {
        DataSnapshot snapshot1=snapshots.get(i);
        return snapshot1.getValue(User.class);
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
            final UserListAdapter.ViewHolder holder=new UserListAdapter.ViewHolder();
            holder.authorname=(TextView)view.findViewById(R.id.author);
            holder.params=(LinearLayout.LayoutParams)holder.authorname.getLayoutParams();
            view.setTag(holder);
        }
        final User user=getItem(i);
        final UserListAdapter.ViewHolder holder=(UserListAdapter.ViewHolder)view.getTag();
        String author=user.getAuthor();
        holder.authorname.setText(author);
        return view;
    }
    public void cleanUp()
    {
        mdatabaseReference.removeEventListener(childEventListener);
    }
}
