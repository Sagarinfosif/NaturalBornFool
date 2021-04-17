package in.pureway.cinemaflix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.models.GetSoundDetailsPojo;

import java.util.List;

public class AdapterSoundVideoPlayer extends RecyclerView.Adapter<AdapterSoundVideoPlayer.ViewHolder> {

    Context context;
    List<GetSoundDetailsPojo.Details.SoundVideo> listSoundDetail;
    Select select;

    public interface Select{

    }


    public AdapterSoundVideoPlayer(Context context, List<GetSoundDetailsPojo.Details.SoundVideo> listSoundDetail, Select select) {
        this.context = context;
        this.listSoundDetail = listSoundDetail;
        this.select = select;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.list_video_single,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return listSoundDetail.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
