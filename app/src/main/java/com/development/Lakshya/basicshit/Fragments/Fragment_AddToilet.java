package com.development.Lakshya.basicshit.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.development.Lakshya.basicshit.POJO.ToiletInfo;
import com.development.Lakshya.basicshit.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Fragment_AddToilet extends Fragment {

    private static final int MY_CAMERA_PERMISSION_CODE = 100 ;
    private static final int CAMERA_REQUEST = 101;
    private static final int READ_EXTERNAL_STORAGE_CODE = 102;
    private static final int ACTIVITY_SELECT_IMAGE = 103;
    public static final int THUMBNAIL_SIZE = 230;
    private static final int MY_LOCATION_PERMISSION_CODE = 100;
    private EditText etName, etAddress;
    private RatingBar overallRating, smellRating, hygieneRating;
    private Spinner placeSpinner, costSpinner;
    private CheckBox childrenCheckbox, maleCheckbox, femaleCheckbox, disabledCheckbox;
    private SwitchCompat towelSwitch, soapSwitch, jetSwitch, mirrorSwitch, dustbinSwitch, toiletPaperSwitch, waterSwitch, changingStationSwitch;
    private Button bv_submit;
    private ImageButton ib_addPhoto;
    private Bitmap bitmap;
    private Uri uri;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private FirebaseAnalytics mFirebaseAnalytics;

    private String eventName;
    private DatabaseReference addToiletDatabase;
    private StorageReference toiletImagesStorage;
    private StorageReference imageRef ;

    public static Fragment_AddToilet newInstance() {
        Fragment_AddToilet fragment = new Fragment_AddToilet();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(container == null)
            return null;

        View view = inflater.inflate(R.layout.fragment_add_toilet,container,false);
        etName = view.findViewById(R.id.et_name);
        etAddress = view.findViewById(R.id.et_address);
        overallRating = view.findViewById(R.id.ratingBar_overall);
        smellRating = view.findViewById(R.id.ratingBar_smell);
        hygieneRating = view.findViewById(R.id.ratingBar_hygiene);
        placeSpinner = view.findViewById(R.id.spinner_place);
        costSpinner = view.findViewById(R.id.spinner_cost);
        towelSwitch = view.findViewById(R.id.switch_towel);
        soapSwitch = view.findViewById(R.id.switch_soap);
        jetSwitch = view.findViewById(R.id.switch_jet);
        mirrorSwitch = view.findViewById(R.id.switch_mirror);
        dustbinSwitch = view.findViewById(R.id.switch_dustbin);
        toiletPaperSwitch = view.findViewById(R.id.switch_toiletpaper);
        waterSwitch = view.findViewById(R.id.switch_water);
        changingStationSwitch = view.findViewById(R.id.switch_changingStations);
        bv_submit  = view.findViewById(R.id.bv_submit);
        childrenCheckbox = view.findViewById(R.id.cb_children);
        maleCheckbox = view.findViewById(R.id.cb_male);
        femaleCheckbox = view.findViewById(R.id.cb_female);
        disabledCheckbox = view.findViewById(R.id.cb_disabled);
        ib_addPhoto = view.findViewById(R.id.ib_addPhoto);

        addToiletDatabase = FirebaseDatabase.getInstance().getReference("ToiletDatabase");
        toiletImagesStorage = FirebaseStorage.getInstance().getReference();

        LayerDrawable overallStars = (LayerDrawable) overallRating.getProgressDrawable();
        LayerDrawable smellStars = (LayerDrawable) smellRating.getProgressDrawable();
        LayerDrawable hygieneStars = (LayerDrawable) hygieneRating.getProgressDrawable();
        overallStars.getDrawable(2).setColorFilter(Color.parseColor("#FE6600"), PorterDuff.Mode.SRC_ATOP);
        overallStars.getDrawable(0).setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_ATOP);
        smellStars.getDrawable(2).setColorFilter(Color.parseColor("#FE6600"), PorterDuff.Mode.SRC_ATOP);
        smellStars.getDrawable(0).setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_ATOP);
        hygieneStars.getDrawable(2).setColorFilter(Color.parseColor("#FE6600"), PorterDuff.Mode.SRC_ATOP);
        hygieneStars.getDrawable(0).setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_ATOP);

//
//        maleCheckbox.setHighlightColor(Color.parseColor("#FE6600"));
//        femaleCheckbox.setHighlightColor(Color.parseColor("#FE6600"));
//        disabledCheckbox.setHighlightColor(Color.parseColor("#FE6600"));
//        childrenCheckbox.setHighlightColor(Color.parseColor("#FE6600"));
//
//        towelSwitch.setHighlightColor(Color.parseColor("#FE6600"));
//        soapSwitch.setHighlightColor(Color.parseColor("#FE6600"));
//        jetSwitch.setHighlightColor(Color.parseColor("#FE6600"));
//        mirrorSwitch.setHighlightColor(Color.parseColor("#FE6600"));
//        dustbinSwitch.setHighlightColor(Color.parseColor("#FE6600"));
//        toiletPaperSwitch.setHighlightColor(Color.parseColor("#FE6600"));
//        waterSwitch.setHighlightColor(Color.parseColor("#FE6600"));
//        changingStationSwitch.setHighlightColor(Color.parseColor("#FE6600"));

        if(isAdded()) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
            mFusedLocationProviderClient = LocationServices
                    .getFusedLocationProviderClient(getActivity());

            bv_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle params = new Bundle();
                    params.putInt("SubmitToiletId",bv_submit.getId());
                    eventName = "Submit Toilet Information";
                    mFirebaseAnalytics.logEvent(eventName,params);

                    String name = etName.getText().toString();
                    String address = etAddress.getText().toString();
                    float overallRatingNumStars = overallRating.getRating();
                    float smellRatingNumStars = smellRating.getRating();
                    float hygieneRatingNumStars = hygieneRating.getRating();
                    String placeType = placeSpinner.getSelectedItem().toString();
                    String cost = costSpinner.getSelectedItem().toString();
                    boolean towelSwitchSelected = towelSwitch.isChecked();
                    boolean soapSwitchSelected = soapSwitch.isChecked();
                    boolean jetSwitchSelected = jetSwitch.isChecked();
                    boolean mirrorSwitchSelected = mirrorSwitch.isChecked();
                    boolean dustbinSwitchSelected = dustbinSwitch.isChecked();
                    boolean toiletPaperSwitchSelected = toiletPaperSwitch.isChecked();
                    boolean waterSwitchSelected = waterSwitch.isChecked();
                    boolean changingStationSwitchSelected = changingStationSwitch.isChecked();
                    boolean childFriendlyCheckbox = childrenCheckbox.isChecked();
                    boolean maleCheckboxSelected = maleCheckbox.isChecked();
                    boolean femaleCheckboxSelected = femaleCheckbox.isChecked();
                    boolean disabledCheckboxSelected = disabledCheckbox.isChecked();

                    if(validate(name,address,placeType,cost,overallRatingNumStars,smellRatingNumStars,hygieneRatingNumStars,towelSwitchSelected,
                                soapSwitchSelected,jetSwitchSelected,mirrorSwitchSelected,dustbinSwitchSelected,toiletPaperSwitchSelected,waterSwitchSelected,
                                changingStationSwitchSelected,childFriendlyCheckbox,maleCheckboxSelected,femaleCheckboxSelected,disabledCheckboxSelected)){

                        ToiletInfo toiletInfo = new ToiletInfo(name,address,placeType,cost,overallRatingNumStars,smellRatingNumStars,hygieneRatingNumStars,towelSwitchSelected,
                                soapSwitchSelected,jetSwitchSelected,mirrorSwitchSelected,dustbinSwitchSelected,toiletPaperSwitchSelected,waterSwitchSelected,
                                changingStationSwitchSelected,childFriendlyCheckbox,maleCheckboxSelected,femaleCheckboxSelected,disabledCheckboxSelected);

                        DatabaseReference databaseReference = addToiletDatabase.push();
                        databaseReference.setValue(toiletInfo);

                        String path = databaseReference.toString();
                        String toiletRef = path.substring(path.lastIndexOf('/') + 1);

                        imageRef = toiletImagesStorage.child(toiletRef);

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();

                        Toast.makeText(getActivity(), "Toilet has been added", Toast.LENGTH_SHORT).show();

                        UploadTask uploadTask = imageRef.putBytes(data);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Log.d("Validated", "onFailure: ");
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                // ...
                                Log.d("Validated", "onSuccess: ");

                            }
                        });

                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
                        transaction.replace(R.id.map_fragment, Fragment_AddToilet.newInstance());
                        transaction.commit();
                    }

                }
            });

            ib_addPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle params = new Bundle();
                    params.putInt("AddPhotoId",ib_addPhoto.getId());
                    eventName = "Add toilet Photo";
                    mFirebaseAnalytics.logEvent(eventName,params);
                    selectImage();
                }
            });
        }

        return view;
    }

    private void loadAddress(){
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOCATION_PERMISSION_CODE);
            return;
        } else {
            final Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
            getMarkedAddress(locationResult);


        }
    }

    private void setMarkedAddress(String address){
        etAddress.setText(address);
    }


    private void getMarkedAddress(Task<Location> locationResult) {
        final String[] strAdd = {""};
        locationResult.addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {

                    Location location = task.getResult();

                    Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if (addresses != null) {
                            Address returnedAddress = addresses.get(0);
                            StringBuilder strReturnedAddress = new StringBuilder("");

                            for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                                strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                            }
                            strAdd[0] = strReturnedAddress.toString();
                            setMarkedAddress(strAdd[0]);
                            Log.w("My Current address", strAdd[0]);
                        } else {
                            Log.w("My Current address", "No Address returned!");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.w("My Current address", "Cannot get Address!");
                    }
                }
            }
        });

    }

    private void selectImage() {

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(options,new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int index) {

                if(options[index].equals("Take Photo"))
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            Log.d("permissions take photo", "onClick: ");
                            requestPermissions(new String[]{Manifest.permission.CAMERA},
                                    MY_CAMERA_PERMISSION_CODE);
                        } else {

                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, CAMERA_REQUEST);
                        }
                    }
                }
                else if(options[index].equals("Choose from Gallery"))
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            Log.d("permission select photo", "onClick: ");
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    READ_EXTERNAL_STORAGE_CODE);
                        } else {
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, "Select Picture"), ACTIVITY_SELECT_IMAGE);
                        }
                    }

                }
                else if(options[index].equals("Cancel"))
                {
                    dialog.dismiss();
                }

            }
        });
        builder.show();
    }



    private boolean validate(String name, String address, String placeType, String cost,
                             float overallRatingNumStars, float smellRatingNumStars, float hygieneRatingNumStars,
                             boolean towelSwitchSelected, boolean soapSwitchSelected, boolean jetSwitchSelected, boolean mirrorSwitchSelected,
                             boolean dustbinSwitchSelected, boolean toiletPaperSwitchSelected, boolean waterSwitchSelected, boolean changingStationSwitchSelected,
                             boolean childFriendlyCheckbox, boolean maleCheckboxSelected, boolean femaleCheckboxSelected, boolean disabledCheckboxSelected){



        if(bitmap == null){
            Toast.makeText(getActivity(), "Please provide a photo", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (name.isEmpty()) {
            etName.setError("Name is required");
            etName.requestFocus();
            return false;
        }
        if (address.isEmpty()) {
            etAddress.setError("Address is required");
            etAddress.requestFocus();
            return false;
        }
        if(hygieneRatingNumStars == 0)
        {
            Toast.makeText(getActivity(), "Please provide the Hygiene Rating", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(overallRatingNumStars == 0)
        {
            Toast.makeText(getActivity(), "Please provide the Overall Rating", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(smellRatingNumStars == 0)
        {
            Toast.makeText(getActivity(), "Please provide the Smell Rating", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("permission take photo", "onRequestPermissionsResult: ");
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            Log.d("permission take photo", "onRequestPermissionsResult2: ");
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(getActivity(), "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
        else if (requestCode == READ_EXTERNAL_STORAGE_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "reading external storage permission granted", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), ACTIVITY_SELECT_IMAGE);
            } else {
                Toast.makeText(getActivity(), "reading external storage permission denied", Toast.LENGTH_LONG).show();
            }
        }
        else if(requestCode == MY_LOCATION_PERMISSION_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    loadAddress();
                }
            }
            else {
                Toast.makeText(getActivity(), "location permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");
            int width = ib_addPhoto.getWidth();
            int height = ib_addPhoto.getHeight();
            Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap,width,height,false);
            ib_addPhoto.setImageBitmap(bitmap2);
            loadAddress();
        }
        else if (requestCode == ACTIVITY_SELECT_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                int width = ib_addPhoto.getWidth();
                int height = ib_addPhoto.getHeight();
                Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap,width,height,false);


                // Log.d(TAG, String.valueOf(bitmap));

                ib_addPhoto.setImageBitmap(bitmap2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}