package com.theplatform.feeds_sample;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.view.ViewGroup;
import android.widget.Toast;

import com.theplatform.adk.Player;
import com.theplatform.adk.PlayerError;
import com.theplatform.adk.player.event.api.data.MediaStartEvent;
import com.theplatform.adk.player.event.api.data.PlayerEventListener;
import com.theplatform.adk.player.event.api.data.ReleaseEndEvent;
import com.theplatform.adk.player.event.api.data.ReleaseStartEvent;
import com.theplatform.adk.player.event.api.data.TextTrackSwitchedEvent;
import com.theplatform.feeds_sample.ui.ADKMediaController;
import com.theplatform.util.log.debug.Debug;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FeedPlayerActivity extends Activity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    private Player player;
    private ADKMediaController adkMediaController;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
/*            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);*/
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_feed_player);
        String urLink = null;
        try{
            Intent intent = getIntent();
            urLink = intent.getStringExtra("link") + "&mbr=true";
        }catch(Exception e){
            Toast.makeText(this, "No video link present", Toast.LENGTH_LONG);
        }

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        //mContentView = findViewById(R.id.fullscreen_content);


        // Set up the user interaction to manually show or hide the system UI.
/*        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });*/

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
        final ViewGroup tpPlayer = (ViewGroup) this.findViewById(R.id.tpPlayer);
        player = new Player(tpPlayer);

        adkMediaController = new ADKMediaController(this, true);
        adkMediaController.setAnchorView(tpPlayer);
        adkMediaController.unload();

        tpPlayer.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        runOnUiThread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        adkMediaController.setMediaPlayer(player.asMediaPlayerControl());
                                        adkMediaController.show();
                                    }
                                });
                    }
                });

        /**********************************************************************
         *
         * Register event listeners
         *
         **********************************************************************/



        player.asEventDispatcher().addEventListener(
                ReleaseEndEvent.getType(), new PlayerEventListener<ReleaseEndEvent>()
                {
                    @Override
                    public void onPlayerEvent(ReleaseEndEvent event)
                    {
                        runOnUiThread(
                                new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        Toast.makeText(getApplicationContext(), "ReleaseEndEvent", Toast.LENGTH_SHORT)
                                                .show();
                                        adkMediaController.unload();
                                    }
                                });
                    }
                });

        player.asEventDispatcher().addEventListener(
                MediaStartEvent.getType(), new PlayerEventListener<MediaStartEvent>()
                {
                    @Override
                    public void onPlayerEvent(final MediaStartEvent event)
                    {

                        runOnUiThread(
                                new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        if (event.getClip().getClipIndex() == 0)
                                        {
                                            adkMediaController.setMediaPlayer(player.asMediaPlayerControl());
                                            adkMediaController.load();
                                        }

                                        if (event.getClip().isAd())
                                        {
                                            Toast.makeText(
                                                    getApplicationContext(), "MediaStartEvent for ad",
                                                    Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                        else
                                        {
                                            Toast.makeText(
                                                    getApplicationContext(), "MediaStartEvent for content",
                                                    Toast.LENGTH_SHORT)
                                                    .show();
                                        }

                                    }
                                });
                    }
                });

        player.asEventDispatcher().addEventListener(
                TextTrackSwitchedEvent.getType(),
                new PlayerEventListener<TextTrackSwitchedEvent>() {
                    @Override
                    public void onPlayerEvent(final TextTrackSwitchedEvent event) {
                        runOnUiThread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(
                                                getApplicationContext(),
                                                "TextTrackSwitched " + (event.getOldTextTrack() == null
                                                        ? null
                                                        : event.getOldTextTrack().getLanguage())
                                                        + " " + (event.getNewTextTrack() == null
                                                        ? null
                                                        : event.getNewTextTrack().getLanguage()),
                                                Toast.LENGTH_SHORT).show();

                                    }
                                });
                    }
                });


        /**********************************************************************
         *
         * Add error listener
         *
         **********************************************************************/

        player.addErrorListener(
                new Player.ErrorListener()
                {
                    @Override
                    public void onError(PlayerError playerError)
                    {
                        Debug.get().error(
                                "got player error. type: " + playerError.getPlayerErrorType() + ", extra code: "
                                        + playerError.getPlayerExtraCode());


                        if (playerError.getMessage() != null)
                        {
                            Debug.get().error(playerError.getMessage());
                        }
                    }
                });

        URL url = null;
        try {
            url = new URL(urLink);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        player.loadReleaseUrl(url);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button.
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        /**************************************************************************************
         *
         *  Destroys the Player. No other methods may be called on the Player after destroy.
         *
         **************************************************************************************/
        if (player != null)
            player.getLifecycle().destroy();

        player = null;

        if (adkMediaController != null)
            adkMediaController.getLifecycle().destroy();

        adkMediaController = null;
    }

    @Override
    protected void onPause()
    {
        if (adkMediaController != null)
            adkMediaController.getLifecycle().onPause();

        if (player != null)
            player.getLifecycle().onPause();

        super.onPause();
    }

    @Override
    protected void onResume()
    {
        if (adkMediaController != null)
            adkMediaController.getLifecycle().onResume();

        if (player != null)
            player.getLifecycle().onResume();

        super.onResume();
    }
}
