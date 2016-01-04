package com.theplatform.feeds_sample.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import com.theplatform.pdk.smil.api.shared.data.TextTrack;

import java.util.ArrayList;
import java.util.List;

public class TextTrackButton extends Spinner
{
    private final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
            getContext(), android.R.layout.simple_spinner_item);

    private final OnItemSelectedListener internalOnItemSelectedListener = new OnItemSelectedListenerDefaultImpl();
    private OnItemSelectedListener externalOnItemSelectedListener = new OnItemSelectedListener()
    {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
        {

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView)
        {

        }
    };

    private boolean resetting = false;
    private String language = "off";

    public TextTrackButton(Context context)
    {
        super(context);
        setAdapter(arrayAdapter);
        addOnItemSelectedListener();
    }

    public TextTrackButton(Context context, int mode)
    {
        super(context, mode);
        setAdapter(arrayAdapter);
        addOnItemSelectedListener();
    }

    public TextTrackButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setAdapter(arrayAdapter);
        addOnItemSelectedListener();
    }

    public TextTrackButton(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        setAdapter(arrayAdapter);
        addOnItemSelectedListener();
    }

    public TextTrackButton(Context context, AttributeSet attrs, int defStyle, int mode)
    {
        super(context, attrs, defStyle, mode);
        setAdapter(arrayAdapter);
        addOnItemSelectedListener();
    }

    public synchronized void setTextTracks(TextTrack[] textTracks)
    {
        if (!resetting)
            return;

        resetting = false;

        textTracks = textTracks == null
                ? new TextTrack[0]
                : textTracks;

        if (textTracks.length == 0)
            setEnabled(false);
        else
            setEnabled(true);

        List<String> newTextTracks = new ArrayList<String>();
        newTextTracks.add("off");

        for (TextTrack textTrack : textTracks)
        {
            newTextTracks.add(textTrack.getName());
        }

        arrayAdapter.clear();
        arrayAdapter.addAll(newTextTracks);
        arrayAdapter.notifyDataSetChanged();

        setTextTrack(language);


        post(new Runnable()
             {
                 @Override
                 public void run()
                 {
                     if(getSelectedView() != null)
                     {
                         ((TextView) getSelectedView()).setText("[cc]");
                     }
                 }
             });
    }

    public synchronized void setTextTrack(String language)
    {
        this.language = language;
        int p = arrayAdapter.getPosition(language);

        if (p > -1)
        {
            setSelection(p, true);
        }
    }

    public synchronized void reset()
    {
        resetting = true;
        setEnabled(false);
        arrayAdapter.clear();
        arrayAdapter.add("off");
        arrayAdapter.notifyDataSetChanged();

        post(
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if (getSelectedView() != null)
                        {
                            ((TextView) getSelectedView()).setText("[cc]");
                        }
                    }
                });
    }

    @Override
    public synchronized void setOnItemSelectedListener(OnItemSelectedListener listener)
    {
        externalOnItemSelectedListener = listener;
    }

    private synchronized void addOnItemSelectedListener()
    {
        super.setOnItemSelectedListener(internalOnItemSelectedListener);
    }

    private class OnItemSelectedListenerDefaultImpl implements OnItemSelectedListener
    {
        @Override
        public void onItemSelected(final AdapterView<?> parent, View view,
                final int pos, final long id)
        {
            language = arrayAdapter.getItem(pos);
            if (!resetting)
                externalOnItemSelectedListener.onItemSelected(parent, view, pos, id);


            ((TextView) view).setText("[cc]");
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView)
        {
        }
    }
}
