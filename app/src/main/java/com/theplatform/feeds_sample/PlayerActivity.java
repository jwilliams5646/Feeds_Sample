package com.theplatform.feeds_sample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.theplatform.adk.Player;
import com.theplatform.adk.PlayerError;
import com.theplatform.feeds_sample.R;
import com.theplatform.adk.player.event.api.data.*;
import com.theplatform.adk.plugins.ads.freewheel.FreeWheelAdvertiserImplementation;
import com.theplatform.adk.plugins.ads.freewheel.configuration.FreeWheelConfiguration;
import com.theplatform.feeds_sample.ui.ADKMediaController;
import com.theplatform.feeds_sample.ui.TextTrackButton;
import com.theplatform.pdk.ads.api.AdvertiserImplementation;
import com.theplatform.util.log.debug.Debug;

import java.net.URL;
import java.util.Collections;

/**
 * Represents the main sample player application activity.
 */
public class PlayerActivity extends Activity
{
    private Player player;
    private ADKMediaController adkMediaController;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        /**********************************************************************
         *
         * Configure and instantiate FreeWheel Advertiser Plugin
         *
         **********************************************************************/

        // instantiate and set FreeWheel configuration object
/*        final FreeWheelConfiguration freeWheelConfiguration = new FreeWheelConfiguration();
        freeWheelConfiguration.setAdsURL("http://cue.v.fwmrm.net/");
        freeWheelConfiguration.setNetworkId(90750);
        freeWheelConfiguration.setSiteSectionFallbackId(0);
        freeWheelConfiguration.setSiteSectionId("3pqa_section");
        freeWheelConfiguration.setVideoAssetFallbackId(0);
        freeWheelConfiguration.setProfile("90750:3pqa_html5");

        // instantiate FreeWheel Advertiser Plugin, inject configuration object
        final AdvertiserImplementation freeWheelAdvertiserImplementation = new FreeWheelAdvertiserImplementation(
                this, freeWheelConfiguration);*/


        /**********************************************************************
         *
         * Configure and instantiate ADK Player
         *
         **********************************************************************/

        final ViewGroup tpPlayer = (ViewGroup) this.findViewById(R.id.tpPlayer);

        final TextTrackButton textTrackButton = (TextTrackButton) findViewById(R.id.text_track_button);
        textTrackButton.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener()
                {
                    @Override
                    public void onItemSelected(final AdapterView<?> parent, View view,
                                               final int pos, final long id)
                    {
                        /**
                         * set text tracks from UI change
                         */
                        player.getTextTracksClient().setTextTrackByIndex(pos - 1);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView)
                    {

                    }
                });

        player = new Player(
                // inject player container view into ADK Player
                tpPlayer

                // inject FreeWheel Advertiser Plugin into Player
                //Collections.singleton(
                        //freeWheelAdvertiserImplementation)
        );

        adkMediaController = new ADKMediaController(this, true);
        adkMediaController.setAnchorView(tpPlayer);
        adkMediaController.unload();

        tpPlayer.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        runOnUiThread(
                                new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
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
                ReleaseStartEvent.getType(), new PlayerEventListener<ReleaseStartEvent>()
                {
                    @Override
                    public void onPlayerEvent(ReleaseStartEvent event)
                    {
                        runOnUiThread(
                                new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        textTrackButton.reset();
                                    }
                                });
                    }
                });

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

                                        /**
                                         * Populate text track UI with any available text tracks
                                         */
                                        if (!event.getClip().isAd())
                                            textTrackButton.setTextTracks(event.getClip().getAvailableTextTracks());
                                    }
                                });
                    }
                });

        player.asEventDispatcher().addEventListener(
                TextTrackSwitchedEvent.getType(),
                new PlayerEventListener<TextTrackSwitchedEvent>()
                {
                    @Override
                    public void onPlayerEvent(final TextTrackSwitchedEvent event)
                    {
                        runOnUiThread(
                                new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        Toast.makeText(
                                                getApplicationContext(),
                                                "TextTrackSwitched " + (event.getOldTextTrack() == null
                                                        ? null
                                                        : event.getOldTextTrack().getLanguage())
                                                        + " " + (event.getNewTextTrack() == null
                                                        ? null
                                                        : event.getNewTextTrack().getLanguage()),
                                                Toast.LENGTH_SHORT).show();

                                        /**
                                         * Update text tracks UI with any text tracks changes
                                         */
                                        if (event.getNewTextTrack() != null)
                                            textTrackButton.setTextTrack(event.getNewTextTrack().getTitle());
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

        /**********************************************************************
         *
         * Build demo application UI
         *
         **********************************************************************/

        final EditText enterUrl = (EditText) findViewById(R.id.edit_message);
        enterUrl.setSingleLine();

        final Activity activity = this;

        Button buttonSet = (Button) findViewById(R.id.button_set);
        buttonSet.setOnClickListener(
                new View.OnClickListener()
                {

                    public void onClick(View v)
                    {
                        try
                        {
                            runOnUiThread(
                                    new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            adkMediaController.unload();
                                        }
                                    });
                            URL url = new URL(enterUrl.getText().toString());
                            player.playReleaseUrl(url);
                        }
                        catch (Exception e)
                        {

                            AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
                            alertDialog.setTitle("Invalid URL");
                            alertDialog.setMessage("You've entered an invalid URL");
                            alertDialog.setCancelable(true);
                            alertDialog.show();
                        }
                    }
                });

        Button buttonLoad = (Button) findViewById(R.id.button_load);
        buttonLoad.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        try
                        {
                            runOnUiThread(
                                    new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            adkMediaController.unload();
                                        }
                                    });
                            URL url = new URL(enterUrl.getText().toString());
                            player.loadReleaseUrl(url);
                        }
                        catch (Exception e)
                        {

                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                            alertDialog.setMessage("You've entered an invalid URL");
                            alertDialog.setPositiveButton(
                                    "OK", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i)
                                        {
                                            dialogInterface.dismiss();
                                        }
                                    });
                            alertDialog.setCancelable(true);
                            alertDialog.create().show();
                        }
                    }
                }
        );
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent)
    {
        Debug.get()
                .log("ADKDemo, onKeyDown: " + keyCode);

        if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK)
        {
            Debug.get()
                    .log("ADKDemo, onKeyDown: keyCode is KEYCODE_BACK, returning false");
            moveTaskToBack(true);
            return false;
        }
        return super.onKeyDown(keyCode, keyEvent);
    }
}