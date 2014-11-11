package me.ivps.android.myweibo.othercomponent;

import me.ivps.android.myweibo.bean.android.MusicInfo;
import me.ivps.android.myweibo.support.debug.AppLogger;
import me.ivps.android.myweibo.support.lib.RecordOperationAppBroadcastReceiver;
import me.ivps.android.myweibo.support.utils.GlobalContext;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

/**
 * User: qii Date: 14-2-5
 */
public class MusicReceiver extends RecordOperationAppBroadcastReceiver {
    
    @Override
    public void onReceive(Context context, Intent intent) {
        String artist = intent.getStringExtra("artist");
        String album = intent.getStringExtra("album");
        String track = intent.getStringExtra("track");
        if (!TextUtils.isEmpty(track)) {
            MusicInfo musicInfo = new MusicInfo();
            musicInfo.setArtist(artist);
            musicInfo.setAlbum(album);
            musicInfo.setTrack(track);
            AppLogger.d("Music" + artist + ":" + album + ":" + track);
            GlobalContext.getInstance().updateMusicInfo(musicInfo);
        }
    }
}
