package com.theplatform.feeds_sample.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.MediaController;
import com.theplatform.adk.lifecycle.HasLifecycle;
import com.theplatform.adk.lifecycle.Lifecycle;

/**
 * Represents the sample player media controller.
 */
public class ADKMediaController extends MediaController implements HasLifecycle
{
    private boolean activityPausing = false;
    private boolean isPlayingOnActivityPause = false;
    private boolean destroyed = false;
    private final Lifecycle lifecycle = new LifecycleDefaultImpl();
    private MediaPlayerControl cachedMediaPlayerControl;
    private boolean unloaded = false;

    public ADKMediaController(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public ADKMediaController(Context context, boolean useFastForward)
    {
        super(context, useFastForward);
    }

    public ADKMediaController(Context context)
    {
        super(context);
    }

    public void unload()
    {
        unloaded = true;
        hide();
    }

    @Override
    public void show()
    {
        if (!unloaded)
            super.show();
    }

    @Override
    public void setMediaPlayer(final MediaPlayerControl player)
    {
        cachedMediaPlayerControl = player;
        super.setMediaPlayer(
                new MediaPlayerControl()
                {
                    @Override
                    public void start()
                    {
                        player.start();
                    }

                    @Override
                    public void pause()
                    {
                        player.pause();
                    }

                    @Override
                    public int getDuration()
                    {
                        return player.getDuration();
                    }

                    @Override
                    public int getCurrentPosition()
                    {
                        return player.getCurrentPosition();
                    }

                    @Override
                    public void seekTo(int position)
                    {
                        player.seekTo(position);
                    }

                    @Override
                    public boolean isPlaying()
                    {
                        boolean r = player.isPlaying();
                        if (activityPausing)
                        {
                            if (isPlayingOnActivityPause == r)
                            {
                                activityPausing = false;
                            }
                            r = isPlayingOnActivityPause;
                        }

                        return r;
                    }

                    @Override
                    public int getBufferPercentage()
                    {
                        return player.getBufferPercentage();
                    }

                    @Override
                    public boolean canPause()
                    {
                        return true;
                    }

                    @Override
                    public boolean canSeekBackward()
                    {
                        return true;
                    }

                    @Override
                    public boolean canSeekForward()
                    {
                        return true;
                    }

                    @Override
                    public int getAudioSessionId() {
                        return 0;
                    }
                });
    }

    @Override
    public Lifecycle getLifecycle()
    {
        return lifecycle;
    }

    public void load()
    {
        unloaded = false;
        show();
    }

    private class LifecycleDefaultImpl implements Lifecycle
    {
        @Override
        public void destroy()
        {
            destroyed = true;
            hide();
        }

        @Override
        public void onPause()
        {
            activityPausing = true;
            if (cachedMediaPlayerControl != null)
            {
                isPlayingOnActivityPause = cachedMediaPlayerControl.isPlaying();
            }
            else
            {
                isPlayingOnActivityPause = false;
            }
            hide();
        }

        @Override
        public void onResume()
        {
            if (activityPausing)
            {
                if (cachedMediaPlayerControl != null)
                    setMediaPlayer(cachedMediaPlayerControl);
            }
        }
    }
}
