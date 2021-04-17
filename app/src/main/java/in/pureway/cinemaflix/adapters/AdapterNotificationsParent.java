package in.pureway.cinemaflix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.models.ModelNotifications;

import java.util.List;

public class AdapterNotificationsParent extends RecyclerView.Adapter<AdapterNotificationsParent.ViewHolder> {

    Context context;
    List<ModelNotifications.Detail> list;
    Select select;

    public interface Select{
        void followUser(int childposition,int parentPosition);
        void unfollowUser(int childposition,int parentPosition);
        void moveToProfile(int position,int childPosition);
        void moveToComment(int position,int childPosition, String videoId);
        void moveToVideo(int position, int  childPosition, String videoId);
        void openDialog(int position, int  childPosition);
        void RejectedVideoP(int position, String url);
        void DeletedVideoP(int position, String url);
    }

    public AdapterNotificationsParent(Context context, List<ModelNotifications.Detail> list, Select select) {
        this.context = context;
        this.list = list;
        this.select = select;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.list_notifications, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int Parentposition) {
        holder.tv_day_noti.setText(list.get(Parentposition).getDay());
        AdapterNotificationsChild adapterNotificationsChild=new AdapterNotificationsChild(context, list.get(Parentposition).getListdetails(), new AdapterNotificationsChild.Select() {
            @Override
            public void chooseNotification(int position) {
                select.moveToProfile(Parentposition,position);
            }

            @Override
            public void followUser(int position) {
                select.followUser(position,Parentposition);
            }

            @Override
            public void unfollowUser(int position) {
                select.unfollowUser(position,Parentposition);
            }

            @Override
            public void commentUser(int position, String videoId) {
                select.moveToComment(Parentposition, position, videoId);
            }

            @Override
            public void videoPlay(int position, String videoId) {
                select.moveToVideo(Parentposition,position , videoId);
            }

            @Override
            public void playDeletedVideo(int position, String videoUrl) {
                select.RejectedVideoP(position,videoUrl);
            }

            @Override
            public void DeletedVideo(int position, String Url) {
                select.DeletedVideoP(position, Url);
            }

            @Override
            public void adminDialog(int position, String message) {
                select.openDialog(Parentposition,position);

            }
        });
        holder.recycler_notications_child.setAdapter(adapterNotificationsChild);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView recycler_notications_child;
        private TextView tv_day_noti;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_day_noti=itemView.findViewById(R.id.tv_day_noti);
            recycler_notications_child=itemView.findViewById(R.id.recycler_notications_child);
        }
    }
}