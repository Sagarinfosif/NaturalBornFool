package in.pureway.cinemaflix.activity.Video_Recording;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.pureway.cinemaflix.R;
import jp.co.cyberagent.android.gpuimage.GPUImageView;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageBilateralBlurFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageBoxBlurFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageCGAColorspaceFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageColorInvertFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageCrosshatchFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageExposureFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGammaFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGaussianBlurFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGrayscaleFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageHalftoneFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageHazeFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageHighlightShadowFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageHueFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageLuminanceFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageLuminanceThresholdFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageMonochromeFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageOpacityFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImagePixelationFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImagePosterizeFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageRGBFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSaturationFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSepiaToneFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSharpenFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSolarizeFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSphereRefractionFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSwirlFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageToneCurveFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageVibranceFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageVignetteFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageWeakPixelInclusionFilter;


/**
 * Created by AQEEL on 3/24/2019.
 */


// this is the class which will show you a Filters lists when you preview the video
// after created it


public class Filter_Adapter extends RecyclerView.Adapter<Filter_Adapter.CustomViewHolder> {
    public Context context;

    List<FilterType> datalist;

    Bitmap image;



    public interface OnItemClickListener {
        void onItemClick(View view, int postion, FilterType item);
    }

    public OnItemClickListener listener;


    public Filter_Adapter(Context context, List<FilterType> arrayList, OnItemClickListener listener) {
        this.context = context;
        datalist = arrayList;
        this.listener = listener;
        image = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.ic_bg_filter);

    }

    private Bitmap pictureDrawableToBitmap(PictureDrawable pictureDrawable){
        Bitmap bmp = Bitmap.createBitmap(pictureDrawable.getIntrinsicWidth(), pictureDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        canvas.drawPicture(pictureDrawable.getPicture());
        return bmp;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_filter_layout, viewGroup, false);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView fiter_txt;
        private GPUImageView ivPhoto;

        public CustomViewHolder(View view) {
            super(view);
            fiter_txt = view.findViewById(R.id.filter_txt);
            ivPhoto = view.findViewById(R.id.iv_photo);
        }

        public void bind(final int pos, final FilterType item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v, pos, item);
                }
            });
        }
    }


    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int i) {
        holder.setIsRecyclable(false);
        String s = datalist.get(i).name();
        holder.fiter_txt.setText(s);
        if (Preview_Video_A.select_postion == i) {
            holder.ivPhoto.setBackgroundColor(context.getResources().getColor(R.color.red));
        }

        Bitmap bm = ((BitmapDrawable) context.getResources().getDrawable(R.drawable.ic_bg_filter)).getBitmap();
        Uri uri = Uri.parse("android.resource://in.pureway.cinemaflix/drawable/ic_bg_filter");
        holder.ivPhoto.setImage(uri);

        switch (s){
            case "BRIGHTNESS":
                holder.ivPhoto.setFilter(new GPUImageBrightnessFilter());
                break;

            case "EXPOSURE":
                holder.ivPhoto.setFilter(new GPUImageExposureFilter());
                break;

            case "FILTER_GROUP_SAMPLE":
                holder.ivPhoto.setFilter(new GPUImageVignetteFilter());
                break;

            case "GAMMA":
                holder.ivPhoto.setFilter(new GPUImageGammaFilter());
                break;

            case "GRAY_SCALE":
                holder.ivPhoto.setFilter(new GPUImageGrayscaleFilter());
                break;

            case "HAZE":
                holder.ivPhoto.setFilter(new GPUImageHazeFilter());
                break;

            case "HIGHLIGHT_SHADOW":
                holder.ivPhoto.setFilter(new GPUImageHighlightShadowFilter());
                break;

            case "HUE":
                holder.ivPhoto.setFilter(new GPUImageHueFilter());
                break;
            case "INVERT":
                holder.ivPhoto.setFilter(new GPUImageColorInvertFilter());
                break;

            case "LUMINANCE":
                holder.ivPhoto.setFilter(new GPUImageLuminanceFilter());
                break;

            case "MONOCHROME":
                holder.ivPhoto.setFilter(new GPUImageMonochromeFilter());
                break;

            case "OPACITY":
                holder.ivPhoto.setFilter(new GPUImageOpacityFilter());
                break;

            case "PIXELATION":
                holder.ivPhoto.setFilter(new GPUImagePixelationFilter());
                break;

            case "POSTERIZE":
                holder.ivPhoto.setFilter(new GPUImagePosterizeFilter());
                break;

            case "RGB":
                holder.ivPhoto.setFilter(new GPUImageRGBFilter());
                break;

            case "SATURATION":
                holder.ivPhoto.setFilter(new GPUImageSaturationFilter());
                break;

            case "SEPIA":
                holder.ivPhoto.setFilter(new GPUImageSepiaToneFilter());
                break;

            case "SHARP":
                holder.ivPhoto.setFilter(new GPUImageSharpenFilter());
                break;

            case "BILATERAL_BLUR":
                holder.ivPhoto.setFilter(new GPUImageBilateralBlurFilter());
                break;

            case "BOX_BLUR":
                holder.ivPhoto.setFilter(new GPUImageBoxBlurFilter());
                break;

            case "BULGE_DISTORTION":
                holder.ivPhoto.setFilter(new GPUImageLuminanceFilter());
                break;

            case "CGA_COLORSPACE":
                holder.ivPhoto.setFilter(new GPUImageCGAColorspaceFilter());
                break;

            case "CONTRAST":
                holder.ivPhoto.setFilter(new GPUImageContrastFilter());
                break;

            case "CROSSHATCH":
                holder.ivPhoto.setFilter(new GPUImageCrosshatchFilter());
                break;

            case "GAUSSIAN_FILTER":
                holder.ivPhoto.setFilter(new GPUImageGaussianBlurFilter());
                break;

            case "HALFTONE":
                holder.ivPhoto.setFilter(new GPUImageHalftoneFilter());
                break;

            case "LUMINANCE_THRESHOLD":
                holder.ivPhoto.setFilter(new GPUImageLuminanceThresholdFilter());
                break;

            case "SOLARIZE":
                holder.ivPhoto.setFilter(new GPUImageSolarizeFilter());
                break;

            case "SPHERE_REFRACTION":
                holder.ivPhoto.setFilter(new GPUImageSphereRefractionFilter());
                break;

            case "SWIRL":
                holder.ivPhoto.setFilter(new GPUImageSwirlFilter());
                break;

            case "TONE":
                holder.ivPhoto.setFilter(new GPUImageToneCurveFilter());
                break;


            case "VIBRANCE":
                holder.ivPhoto.setFilter(new GPUImageVibranceFilter());
                break;

            case "VIGNETTE":
                holder.ivPhoto.setFilter(new GPUImageVignetteFilter());
                break;


            case "WEAK_PIXEL":
                holder.ivPhoto.setFilter(new GPUImageWeakPixelInclusionFilter());
                break;


            case "ZOOM_BLUR":
                holder.ivPhoto.setFilter(new GPUImageBoxBlurFilter());
                break;

        }


        holder.bind(i, datalist.get(i), listener);
    }

    public void recycle_bitmap(){
        image.recycle();
    }


}


