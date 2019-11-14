package com.development.Lakshya.basicshit.Adapters;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.development.Lakshya.basicshit.R;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotosViewHolder> {

    private PlacePhotoMetadata[] photoMetadataArray;
    private Context context;
    private GeoDataClient mGeoDataClient;


    public PhotosAdapter(PlacePhotoMetadata[] photoMetadataArray, Context context, GeoDataClient mGeoDataClient) {
        this.photoMetadataArray = photoMetadataArray;
        this.context = context;
        this.mGeoDataClient = mGeoDataClient;
    }

    @NonNull
    @Override
    public PhotosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = li.inflate(R.layout.photos_slider,parent,false);

        return new PhotosViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final PhotosViewHolder holder, int position) {
        PlacePhotoMetadata photoMetadata = photoMetadataArray[position];
        // Get the attribution text.
        CharSequence attribution = photoMetadata.getAttributions();
        Log.d("Photos Adapter", "instantiateItem: " + attribution);
        // Get a full-size bitmap for the photo.
        Task<PlacePhotoResponse> photoResponse = mGeoDataClient.getPhoto(photoMetadata);
        photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlacePhotoResponse> task) {


                PlacePhotoResponse photo = task.getResult();
                Bitmap bitmap = photo.getBitmap();
                Log.d("Photos Adapter", "onComplete: " + bitmap);
                holder.imageView.setImageBitmap(bitmap);

            }
        });



    }

    @Override
    public int getItemCount() {
        return photoMetadataArray.length;
    }


    class PhotosViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        public PhotosViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_places_photos);
        }
    }

//    private PlacePhotoMetadata[] photoMetadataArray;
//    private Context context;
//    private LayoutInflater inflater;
//    private GeoDataClient mGeoDataClient;
//    ImageView myImage;
//
//    public PhotosAdapter(PlacePhotoMetadata[] photoMetadataArray, Context context, GeoDataClient mGeoDataClient) {
//        this.photoMetadataArray = photoMetadataArray;
//        this.context = context;
//        this.mGeoDataClient = mGeoDataClient;
//        inflater = LayoutInflater.from(context);
//    }
//
//    @Override
//    public int getCount() {
//        Log.d("Photos Adapter ", "getCount: " + photoMetadataArray.length);
//        return photoMetadataArray.length;
//    }
//
//    @NonNull
//    @Override
//    public Object instantiateItem(@NonNull ViewGroup container, int position) {
//        Log.d("Photos Adapter ", "instantiateItem: ");
//        View myImageLayout = inflater.inflate(R.layout.photos_slider, container, false);
//        myImage = myImageLayout
//                .findViewById(R.id.iv_places_photos);
//        PlacePhotoMetadata photoMetadata = photoMetadataArray[position];
//        // Get the attribution text.
//        CharSequence attribution = photoMetadata.getAttributions();
//        Log.d("Photos Adapter", "instantiateItem: " + attribution);
//        // Get a full-size bitmap for the photo.
//        Task<PlacePhotoResponse> photoResponse = mGeoDataClient.getPhoto(photoMetadata);
//        photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
//            @Override
//            public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
//
//
//                PlacePhotoResponse photo = task.getResult();
//                Bitmap bitmap = photo.getBitmap();
//                Log.d("Photos Adapter", "onComplete: " + bitmap);
//                myImage.setImageBitmap(bitmap);
//
//            }
//        });
//
//        container.addView(myImageLayout, 0);
//        return myImageLayout;
//    }
//
//    @Override
//    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        container.removeView((View) object);
//    }
//
//    @Override
//    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
//        return view.equals(object);
//    }
}
