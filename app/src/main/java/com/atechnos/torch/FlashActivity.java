package com.atechnos.torch;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SlidingDrawer;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FlashActivity extends Activity {

    ImageButton btnSwitch;

    private Camera camera;
    private boolean isFlashOn;
    private boolean hasFlash;
    Camera.Parameters params;
    MediaPlayer mp;
    int REQUEST_CODE = 1;

    InterstitialAd mInterstitialAd;
    AdView adView;
    ListView simpleListView;
    String url_to_hit = "http://173.254.29.44/appmenu/admin/api/product_details.php?appid=appid&category_id=1";
    //  private ProgressDialog progressDialog;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_torch);
        progressBar = (ProgressBar) findViewById(R.id.HeaderProgress);


        // final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.mylayout);
        SlidingDrawer simpleSlidingDrawer = (SlidingDrawer) findViewById(R.id.simpleSlidingDrawer); // initiate the SlidingDrawer
        final ImageView handleButton = (ImageView) findViewById(R.id.handle);
        final Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(1000);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        handleButton.startAnimation(animation);// inititate a Button which is used for handling the content of SlidingDrawer
        simpleListView = (ListView) findViewById(R.id.simpleListView);
        simpleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SlidingModel model = (SlidingModel) parent.getItemAtPosition(position);

                Intent intent = new Intent(Intent.ACTION_VIEW,

                        // Uri.parse("market://search?q==" + packageName));
                        Uri.parse(model.getAppurl()));
                // progressDialog.dismiss();
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
        simpleSlidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
                JsonTask jsonTask = new JsonTask();
                Boolean available;
                if (isNetworkAvailable(FlashActivity.this)) {
                    handleButton.setImageResource(R.drawable.jyright);
                    jsonTask.execute(url_to_hit);
                } else

                {
                    available = false;
                    jsonTask.cancel(available);
                    // tv.setText("you r not connected to intermnet");
                    //Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_LONG).show();


                }


                Log.e("Link", "value" + url_to_hit);
                // relativeLayout.getBackground().setAlpha(80);
            }
        });
        // implement setOnDrawerCloseListener event
        simpleSlidingDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {
                // change the handle button text
                // handleButton.setText("Open");
                handleButton.setImageResource(R.drawable.jyleft);
                // relativeLayout.getBackground().setAlpha(250);


                // relativeLayout.setBackgroundResource(R.drawable.arabic);
            }
        });
        simpleListView.setDivider(null);
        chkPermission();

        btnSwitch = (ImageButton) findViewById(R.id.btnSwitch);

        hasFlash = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!hasFlash) {
            Toast.makeText(FlashActivity.this, "Sorry, This device doesn't support FlashLight", Toast.LENGTH_LONG).show();
        } else {
            btnSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isFlashOn) {
                        turnOffFlash();
                    } else {
                        turnOnFlash();
                    }
                }
            });
        }

        adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        // showInterStitialAd();
    }

    // getting camera parameters
    private void getCamera() {
        if (camera == null) {
            try {
                camera = Camera.open();
                params = camera.getParameters();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
    }

    /*
    * Turning On flash
    */
    private void turnOnFlash() {
        if (!isFlashOn) {
            if (camera == null || params == null) {
                return;
            }

            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(params);
            camera.startPreview();
            isFlashOn = true;
            toggleButtonImage();
        }
    }

    private void turnOffFlash() {
        if (isFlashOn) {
            if (camera == null || params == null) {
                return;
            }

            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
            isFlashOn = false;
            toggleButtonImage();
        }
    }

    private void toggleButtonImage() {
        if (isFlashOn) {
            btnSwitch.setImageResource(R.drawable.btn_switch_on);
        } else {
            btnSwitch.setImageResource(R.drawable.btn_switch_off);
        }
    }

    public void showInterStitialAd(){
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_id));
        AdRequest adRequest = new AdRequest.Builder()
                .build();

        mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }


    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        turnOffFlash();
        if (camera != null) {
            camera.release();
            camera = null;
        }
      //  showInterstitial();
        super.onDestroy();
    }

    private void chkPermission() {
        if (ContextCompat.checkSelfPermission(FlashActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(FlashActivity.this,
                    Manifest.permission.CAMERA)) {
                Toast.makeText(FlashActivity.this, "Camera Permission is needed to Light Flash", Toast.LENGTH_LONG).show();

            } else {
                ActivityCompat.requestPermissions(FlashActivity.this,
                        new String[]{
                                Manifest.permission.CAMERA, Manifest.permission.FLASHLIGHT
                        }, REQUEST_CODE
                );
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getCamera();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        finish();
        startActivity(new Intent(FlashActivity.this, FlashActivity.class));
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    public boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting() && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    class JsonTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            // progressDialog.setIndeterminate(true);
            //  progressDialog.setCancelable(false);
            //  progressDialog.setTitle("Connecting Server!");
            //  progressDialog.setMessage("Loading Please Wait");

            //  progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            BufferedReader bufferedReader = null;
            HttpURLConnection httpURLConnection = null;
            // Log.i("jsontask","inside doing ");

            try {
                URL url = new URL(params[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                //   httpURLConnection.setReadTimeout(10000 /* milliseconds */);
                //  httpURLConnection.setConnectTimeout(15000 /* milliseconds */);
                //connection connect
                httpURLConnection.connect();
                int response = httpURLConnection.getResponseCode();
                Log.d("Link", "The response is: " + response);
                //get the data from server into inputstream
                InputStream inputStream = httpURLConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                //read the data line by line
                StringBuffer reult = new StringBuffer();

                Log.d("Link", "The result is: " + reult.toString());

                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    reult.append(line);
                }
                return reult.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                try {
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.INVISIBLE);
            // progressDialog.cancel();
            Log.d("Link", "The response is: " + s);


            List<SlidingModel> slidingmodels = null;
            JSONArray parentarray = null;

            try {
                slidingmodels = new ArrayList<>();

                parentarray = new JSONArray(s);
                for (int i = 0; i < parentarray.length(); i++) {

                    JSONObject parentobject = parentarray.getJSONObject(i);
                    SlidingModel slidingModel = new SlidingModel();
                    slidingModel.setProduct_id(parentobject.getInt("id"));
                    slidingModel.setProduct_name(parentobject.getString("product_name"));
                    slidingModel.setImage(parentobject.getString("img"));
                    slidingModel.setAppurl(parentobject.getString("showurl"));
                    slidingmodels.add(slidingModel);

                    Log.e("Link", "Inage" + parentobject.getString("showurl"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            SlidingAdapter movieAdapter = new SlidingAdapter(getApplicationContext(), R.layout.createlist_item, slidingmodels);
            simpleListView.setAdapter(movieAdapter);


        }
    }

}

