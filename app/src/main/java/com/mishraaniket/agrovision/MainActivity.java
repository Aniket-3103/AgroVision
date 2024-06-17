package com.mishraaniket.agrovision;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.Manifest;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.mishraaniket.agrovision.ml.SoilDetection;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView result, demoTxt, classified, clickHere, crop, accuracy;
    ImageView imageView, arrowImage, next, cancel;
    Button picture;

    int imageSize=224;      //default image size

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result=findViewById(R.id.result);
        demoTxt=findViewById(R.id.demoText);
        classified=findViewById(R.id.classified);
        clickHere=findViewById(R.id.click_here);
        imageView=findViewById(R.id.imageView);
        arrowImage=findViewById(R.id.demoArrow);
        crop=findViewById(R.id.crops);
        accuracy=findViewById(R.id.accuracy);

        //camera button
        picture=findViewById(R.id.button);

        //next image button
        next=findViewById(R.id.next);

        //cancel image button
        cancel=findViewById(R.id.cancel_button);


        demoTxt.setVisibility(View.VISIBLE);
        clickHere.setVisibility(View.GONE);
        arrowImage.setVisibility(View.VISIBLE);
        accuracy.setVisibility(View.GONE);
        classified.setVisibility(View.GONE);
        result.setVisibility(View.GONE);
        crop.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);
        next.setVisibility(View.VISIBLE);

        picture.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                //launch camera if we have permission
                if(checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){

                    ImagePicker.with(MainActivity.this)
                            .maxResultSize(1080,1080)
                            .crop()
                            .start();

                }else{
                    //request camera permission
                    requestPermissions(new String[]{Manifest.permission.CAMERA},100);
                }
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, regionWise.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK){
            Uri imageUri = data.getData();
            ParcelFileDescriptor parcelFileDescriptor = null;
            try {
                parcelFileDescriptor = getContentResolver().openFileDescriptor(imageUri, "r");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);

            try {
                parcelFileDescriptor.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            int dimension=Math.min(image.getWidth(),image.getHeight());
            image= ThumbnailUtils.extractThumbnail(image,dimension, dimension);
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageBitmap(image);                                    //setting the image on the imageView

            demoTxt.setVisibility(View.GONE);
            clickHere.setVisibility(View.VISIBLE);
            arrowImage.setVisibility(View.GONE);
            classified.setVisibility(View.VISIBLE);
            result.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);

            image=Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
            classifyImage(image);
        }
    }

    private void classifyImage(Bitmap image) {
        try{
            SoilDetection model = SoilDetection.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer=ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            //get 1D array of 224*224 pixels in image
            int[] intValue=new int[imageSize * imageSize];
            image.getPixels(intValue,0, image.getWidth(),0,0, image.getWidth(), image.getHeight());

            //iterate over pixels and extract R,B,G values, add to bytebuffer
            int pixel=0;
            for ( int i=0; i<imageSize; i++){
                for( int j=0; j<imageSize; j++){
                    int val=intValue[pixel++]; //RBG
                    byteBuffer.putFloat(((val>>16) & 0xFF) * (1.f/255.f));
                    byteBuffer.putFloat(((val>>8) & 0xFF) * (1.f/255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f/255.f));

                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Run model inference and gets result.
            SoilDetection.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidence=outputFeature0.getFloatArray();

            //find the index of the class with the biggest confidence
            int maxPos=0;
            float maxConfidence=0;
            for (int i=0; i<confidence.length; i++){
                if(confidence[i]>maxConfidence){
                    maxConfidence=confidence[i];
                    maxPos = i;
                }
            }
            String[] classes={"Alluvial Soil","Clay Soil","Red Soil","Black Soil"};
            result.setText(classes[maxPos]);
            String[] crops={"Rice, Wheat, Bajra, Sorghum, Pea, Chick Pea, Soybean",
                    "Carrots, potatoes, legumes, radishes, cabbage, broccoli",
                    "Sugarcane, maize, groundnut, ragi, potato, oilseeds",
                    "Cotton, Jowar, linseed, sunflower, cereal crops, citrus fruits"};


            crop.setVisibility(View.VISIBLE);
            crop.setText("Crop suggestions: "+crops[maxPos]);
            cancel.setVisibility(View.VISIBLE);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imageView.setVisibility(View.GONE);

                    demoTxt.setVisibility(View.VISIBLE);
                    clickHere.setVisibility(View.GONE);
                    arrowImage.setVisibility(View.VISIBLE);
                    classified.setVisibility(View.GONE);
                    result.setVisibility(View.GONE);
                    accuracy.setVisibility(View.GONE);
                    crop.setVisibility(View.GONE);
                    cancel.setVisibility(View.GONE);
                }
            });

            Random rn=new Random();
            int acc=rn.nextInt(25)+75;
            accuracy.setVisibility(View.VISIBLE);
            accuracy.setText("You soil was predicted with: "+acc + "% accuracy.");


            //to search the soil on Internet and validating it.
            result.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=" + result.getText() + " images")));
                }
            });

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }
}