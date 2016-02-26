package vandy.mooc;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * An Activity that downloads an image, stores it in a local file on
 * the local device, and returns a Uri to the image file.
 */
public class DownloadImageActivity extends Activity {
    /**
     * Debugging tag used by the Android logger.
     */
    private final String TAG = getClass().getSimpleName();

    /**
     * Hook method called when a new instance of Activity is created.
     * One time initialization code goes here, e.g., UI layout and
     * some class scope variable initialization.
     *
     * @param savedInstanceState object that contains saved state information.
     */
    
    Uri uriImage;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Always call super class for necessary
        // initialization/implementation.
        // @@ TODO -- you fill in here.
    	super.onCreate(savedInstanceState);
    	Log.d(TAG, "onCreate()");
        // Get the URL associated with the Intent data.
        // @@ TODO -- you fill in here.
    	uriImage = getIntent().getData();
    	
        // Download the image in the background, create an Intent that
        // contains the path to the image file, and set this as the
        // result of the Activity.
    	
    	new Thread(){
			@Override
			public void run() {
				
				try {
					
					Uri uri = DownloadUtils.downloadImage(
	                        getApplicationContext(), uriImage);
	                
					Intent intent = new Intent();
	                intent.setData(uri);
	                
	                setResult(MainActivity.RESULT_OK, intent);
					
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				
				// @@ TODO -- you fill in here using the Android "HaMeR"
		        // concurrency framework.  Note that the finish() method
		        // should be called in the UI thread, whereas the other
		        // methods should be called in the background thread.  See
		        // http://stackoverflow.com/questions/20412871/is-it-safe-to-finish-an-android-activity-from-a-background-thread
		        // for more discussion about this topic.
				runOnUiThread(new Runnable(){
					@Override
					public void run(){
						Toast.makeText(DownloadImageActivity.this, "", Toast.LENGTH_SHORT).show();
						finish();
					}
				});
			}
		}.start();
    }
}
