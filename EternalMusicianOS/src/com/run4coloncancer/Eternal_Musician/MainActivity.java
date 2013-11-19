package com.run4coloncancer.Eternal_Musician;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	private FileInputStream fis;
	private MediaPlayer mediaPlayer;
	private SongGen songGenerator;
	private File f;
	private Button playOrPauseButton;
	private EditText editText;
	
	public MainActivity() {
		mediaPlayer = new MediaPlayer();
	}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playOrPauseButton = (Button) findViewById(R.id.button_playOrPause);
        editText = (EditText) findViewById(R.id.BPM);
        f = new File (getFilesDir(), "Song0.mid");
        if (mediaPlayer.isPlaying()) {
        } else {
        	songGenerator = new SongGen();
            songGenerator.go(getFilesDir());
        	try {
            	fis = new FileInputStream(f);
            	FileDescriptor fd = fis.getFD();
            	mediaPlayer.setDataSource(fd);
            	mediaPlayer.setOnCompletionListener(new finishedListener());
            	mediaPlayer.prepare();
            } catch (Exception ex) {ex.printStackTrace();}
            mediaPlayer.start();
            playOrPauseButton.setText("Pause");
        }
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	mediaPlayer.stop();
    	try {
    		fis.close();
    	} catch (Exception ex) {ex.printStackTrace();}
    }

    public class finishedListener implements OnCompletionListener {
    	public void onCompletion(MediaPlayer mp) {
        	if (editText.getText().toString().equals("")) {
        		nextSong(0);
        	} else {
        		nextSong(Integer.parseInt(editText.getText().toString()));
        	}
    	}
    }
    
    public void endStream (View view) {
    	try {
    		fis.close();
    	} catch (Exception ex) {ex.printStackTrace();}
    	mediaPlayer.stop();
    }
    
    public void nextSong (View view) {
    	int x;
    	if (editText.getText().toString().equals("")) {
    		x=0;
    	} else {
    		x = Integer.parseInt(editText.getText().toString());
    	}
    	mediaPlayer.reset();
    	try {
    		fis.close();
    		f.delete();
    	} catch (Exception ex) {ex.printStackTrace();}
    	if ((x>39 && x<221) || x==0 ) {
    		newSong(x);
    	} else {
    		Context context = getApplicationContext();
    		CharSequence text = "Tempo must be a value between 40 and 220!";
    		int duration = Toast.LENGTH_SHORT;
    		Toast toast = Toast.makeText(context, text, duration);
    		toast.show();
    	}
    	try {
    		fis = new FileInputStream(f);
    		FileDescriptor fd = fis.getFD();
    		mediaPlayer.setDataSource(fd);
    		mediaPlayer.prepare();
    	} catch (Exception ex){ex.printStackTrace();}
    	mediaPlayer.start();
    	playOrPauseButton.setText("Pause");
    }
    
    public void nextSong(int x) {
    	mediaPlayer.reset();
    	try {
    		fis.close();
    		f.delete();
    	} catch (Exception ex) {ex.printStackTrace();}
    	if ((x>39 && x<221) || x==0 ) {
    		newSong(x);
    	} else {
    		Context context = getApplicationContext();
    		CharSequence text = "Tempo must be a value between 40 and 220!";
    		int duration = Toast.LENGTH_SHORT;
    		Toast toast = Toast.makeText(context, text, duration);
    		toast.show();
    	}
    	try {
    		fis = new FileInputStream(f);
    		FileDescriptor fd = fis.getFD();
    		mediaPlayer.setDataSource(fd);
    		mediaPlayer.prepare();
    	} catch (Exception ex){ex.printStackTrace();}
    	mediaPlayer.start();
    	playOrPauseButton.setText("Pause");
    }
    
    public void playOrPause (View view) {
    	if (mediaPlayer.isPlaying()) {
    		mediaPlayer.pause();
    		playOrPauseButton.setText("Play");
    	} else {
    		mediaPlayer.start();
    		playOrPauseButton.setText("Pause");
    	}
    }
    
    public void newSong (int x) {
    	if (x==0){
    		songGenerator.go(getFilesDir());
    	} else {
    		songGenerator.go(getFilesDir(), x);
    	}
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
