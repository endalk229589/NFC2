package tutorial02.nfctutorials.nfc2;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.File;

import static android.nfc.NfcAdapter.getDefaultAdapter;
import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR1;


public class MainActivity extends AppCompatActivity {


    private Uri[] mFileUris = new Uri[10];

    NfcAdapter mNfcAdapter;

    // Flag to indicate that Android Beam is available

    boolean mAndroidBeamAvailable = false;
    private Uri fileUri;

    public void share(View view) {
        //Toast.makeText(this, "Share with", Toast.LENGTH_SHORT).show();
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Share With");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "harmonize.com/store/0987658985 Title - Ethiopia  artist - Teddy Afro ");

        startActivity(Intent.createChooser(emailIntent, "Share With"));


    }


    private class FileUriCallback implements NfcAdapter.CreateBeamUrisCallback {
        public FileUriCallback() {
        }

        /**
         * Create content URIs as needed to share with another device
         */
        @Override
        public Uri[] createBeamUris(NfcEvent event) {

            return mFileUris;
        }
    }

    private FileUriCallback mFileUriCallback;

    private final String manager = PackageManager.FEATURE_NFC;


    String transferFile = "ethiopia.jpg";
    final ThreadLocal<File> extDir = new ThreadLocal<File>() {
        @Override
        protected File initialValue() {
            return getExternalFilesDir(null);
        }
    };






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // NFC isn't available on the device
        PackageManager pktManager = MainActivity.this.getPackageManager();
        if (!pktManager.hasSystemFeature(manager)) {
             /*
             * Disable NFC features here.
             * For example, disable menu items or buttons that activate
             * NFC-related features
             */
            // Android Beam file transfer isn't supported
        } else if (Build.VERSION.SDK_INT < JELLY_BEAN_MR1) {
            // If Android Beam isn't available, don't continue.

            mAndroidBeamAvailable = false;
            /*
             * Disable Android Beam file transfer features here.
             */

            // Android Beam file transfer is available, continue
        } else {
            mNfcAdapter = getDefaultAdapter(this);


            mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        /*
         * Instantiate a new FileUriCallback to handle requests for
         * URIs
         */
            mFileUriCallback = new FileUriCallback();
            // Set the dynamic callback for URI requests.
            mNfcAdapter.setBeamPushUrisCallback(mFileUriCallback,this);

            File requestFile = new File(extDir.get(), transferFile);
            requestFile.setReadable(true, false);

            fileUri = Uri.fromFile(requestFile);
            if (fileUri != null) {
                mFileUris[0] = fileUri;
            } else {
                Log.e("My Activity", "No File URI available for file.");
            }



        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }


}



