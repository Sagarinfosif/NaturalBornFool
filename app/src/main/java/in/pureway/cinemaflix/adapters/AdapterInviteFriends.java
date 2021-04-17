package in.pureway.cinemaflix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.models.ModelInviteFriends;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterInviteFriends extends RecyclerView.Adapter<AdapterInviteFriends.ViewHolder> {

    List<ModelInviteFriends> list;
    Context context;
    Select select;

    public interface Select{
        void inviteFriend(int position);
    }

    public AdapterInviteFriends(List<ModelInviteFriends> list, Context context, Select select) {
        this.list = list;
        this.context = context;
        this.select = select;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.list_contacts,parent,false);
       return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Glide.with(context).load(list.get(position).getImage()).placeholder(context.getResources().getDrawable(R.drawable.ic_user1)).into(holder.img_contact);
        holder.tv_contact_name.setText(list.get(position).getContactName());
        holder.tv_contact_number.setText(list.get(position).getContactNo());
        holder.btn_invite_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.inviteFriend(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView img_contact;
        private Button btn_invite_invite;
        private TextView tv_contact_number,tv_contact_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_contact=itemView.findViewById(R.id.img_contact);
            btn_invite_invite=itemView.findViewById(R.id.btn_invite_invite);
            tv_contact_number=itemView.findViewById(R.id.tv_contact_number);
            tv_contact_name=itemView.findViewById(R.id.tv_contact_name);
        }
    }
}
