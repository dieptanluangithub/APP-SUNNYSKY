package hmd.example.firebaseprojectstudyenglish.luyennghe;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;

import hmd.example.firebaseprojectstudyenglish.singletonpattern.MessageObject;



public class MediaPlayerUtils {
    public static final String URL_MEDIA_SAMPLE  = "https://github.com/duchmdev/huynhminhduc_assets_android_firebase_project_study_english/raw/main/LuyenNghe/Hey%20Mama%20-%20David%20Guetta%20feat_%20Nicki%20Mina.mp3";

    public static final String LOG_TAG= "MediaPlayerTutorial";


    // String videoURL = "https://raw.githubusercontent.com/o7planning/webexamples/master/_testdatas_/yodel.mp3";
    public static void playURLMedia(Context context, MediaPlayer mediaPlayer, String videoURL)  {
        try {
            Log.i(LOG_TAG, "Media URL: "+ videoURL);

            Uri uri= Uri.parse( videoURL );
            //Toast.makeText(context,"Select source: "+ uri,Toast.LENGTH_SHORT).show();
            mediaPlayer.setDataSource(context, uri);
            mediaPlayer.prepareAsync();

        } catch(Exception e) {
            MessageObject messageObject = MessageObject.getInstance();
            Log.e(LOG_TAG, "Error Play URL Media: "+ e.getMessage());
            messageObject.ShowDialogMessage(Gravity.CENTER,
                    context,
                    "Error Play URL Media: "+ e.getMessage(),
                    0);
            //Toast.makeText(context,"Error Play URL Media: "+ e.getMessage(),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
