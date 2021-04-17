package in.pureway.cinemaflix.activity.videoEditor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.seerslab.argear.session.ARGContents;
//import com.seerslab.argear.session.ARGFrame;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.videoEditor.data.BeautyItemData;

import java.util.ArrayList;
import java.util.List;

public class BeautifyAdapter {
    Context context;
    List<BeautyItemData.BeautyItemInfo> list = new ArrayList<>();

//    BeautySelectedListener beautySelectedListener;

    private int mSelectedIndex = -1;

//    public interface BeautySelectedListener {
//        void onBeautySelected(ARGContents.BeautyType beautyType);
//
//        ARGFrame.Ratio getViewRatio();
//    }
//
//    public BeautifyAdapter(Context context, BeautySelectedListener beautySelectedListener) {
//        this.context = context;
//        this.beautySelectedListener = beautySelectedListener;
//    }
//
//    public void setData(List<BeautyItemData.BeautyItemInfo> data) {
//        list.clear();
//        list.addAll(data);
//        notifyDataSetChanged();
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_beauty, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        BeautyItemData.BeautyItemInfo mInfo;
//        mInfo = list.get(position);
//        if (beautySelectedListener.getViewRatio() == ARGFrame.Ratio.RATIO_FULL) {
//            if (mSelectedIndex == position) {
//                holder.img_beauty.setBackgroundResource(mInfo.mResource2);
//            } else {
//                holder.img_beauty.setBackgroundResource(mInfo.mResource1);
//            }
//        } else {
//            if (mSelectedIndex == position) {
//                holder.img_beauty.setBackgroundResource(mInfo.mResource1);
//            } else {
//                holder.img_beauty.setBackgroundResource(mInfo.mResource2);
//            }
//        }
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (beautySelectedListener.getViewRatio() == ARGFrame.Ratio.RATIO_FULL) {
//                    holder.img_beauty.setBackgroundResource(mInfo.mResource2);
//                } else {
//                    holder.img_beauty.setBackgroundResource(mInfo.mResource1);
//                }
//                notifyItemChanged(mSelectedIndex);
//                mSelectedIndex = holder.getLayoutPosition();
//                if (beautySelectedListener != null) {
//                    beautySelectedListener.onBeautySelected(mInfo.mBeautyType);
//                }
//            }
//        });
//    }
//
//    public void selectItem(ARGContents.BeautyType beautyType) {
//        for (int i = 0; i < list.size(); i++) {
//            BeautyItemData.BeautyItemInfo itemInfo = list.get(i);
//            if (beautyType == itemInfo.mBeautyType) {
//                mSelectedIndex = i;
//                break;
//            }
//        }
//        if (mSelectedIndex != -1) {
//            notifyDataSetChanged();
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return list.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        private ImageView img_beauty;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            img_beauty = itemView.findViewById(R.id.img_beauty);
//        }
//    }
}
