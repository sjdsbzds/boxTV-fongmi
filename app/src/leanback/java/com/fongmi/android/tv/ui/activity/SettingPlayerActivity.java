package com.fongmi.android.tv.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.provider.Settings;
import android.view.View;

import androidx.viewbinding.ViewBinding;

import com.fongmi.android.tv.R;
import com.fongmi.android.tv.Setting;
import com.fongmi.android.tv.databinding.ActivitySettingPlayerBinding;
import com.fongmi.android.tv.impl.BufferCallback;
import com.fongmi.android.tv.impl.DanmuMaxLineCallback;
import com.fongmi.android.tv.impl.SubtitleCallback;
import com.fongmi.android.tv.impl.UaCallback;
import com.fongmi.android.tv.player.ExoUtil;
import com.fongmi.android.tv.player.Players;
import com.fongmi.android.tv.ui.base.BaseActivity;
import com.fongmi.android.tv.ui.dialog.BufferDialog;
import com.fongmi.android.tv.ui.dialog.DanmuMaxLineDialog;
import com.fongmi.android.tv.ui.dialog.SubtitleDialog;
import com.fongmi.android.tv.ui.dialog.UaDialog;
import com.fongmi.android.tv.utils.ResUtil;

public class SettingPlayerActivity extends BaseActivity implements UaCallback, BufferCallback, SubtitleCallback, DanmuMaxLineCallback {

    private ActivitySettingPlayerBinding mBinding;
    private String[] player;
    private String[] danmuSpeed;
    private String[] danmuSize;
    private String[] caption;
    private String[] http;
    private String[] flag;

    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, SettingPlayerActivity.class));
    }

    private String getSwitch(boolean value) {
        return getString(value ? R.string.setting_on : R.string.setting_off);
    }

    @Override
    protected ViewBinding getBinding() {
        return mBinding = ActivitySettingPlayerBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
        mBinding.playerText.setText((player = ResUtil.getStringArray(R.array.select_player))[Setting.getPlayer()]);
        mBinding.uaText.setText(Setting.getUa());
        mBinding.tunnelText.setText(getSwitch(Setting.isTunnel()));
        mBinding.bufferText.setText(String.valueOf(Setting.getBuffer()));
        mBinding.subtitleText.setText(String.valueOf(Setting.getSubtitle()));
        mBinding.caption.setVisibility(Setting.hasCaption() ? View.VISIBLE : View.GONE);
        mBinding.http.setVisibility(Players.isExo(Setting.getPlayer()) ? View.VISIBLE : View.GONE);
        mBinding.buffer.setVisibility(Players.isExo(Setting.getPlayer()) ? View.VISIBLE : View.GONE);
        mBinding.tunnel.setVisibility(Players.isExo(Setting.getPlayer()) ? View.VISIBLE : View.GONE);
        mBinding.flagText.setText((flag = ResUtil.getStringArray(R.array.select_flag))[Setting.getFlag()]);
        mBinding.httpText.setText((http = ResUtil.getStringArray(R.array.select_exo_http))[Setting.getHttp()]);
        mBinding.captionText.setText((caption = ResUtil.getStringArray(R.array.select_caption))[Setting.isCaption() ? 1 : 0]);
        mBinding.danmuMaxLineText.setText(String.valueOf(Setting.getDanmuMaxLine(3)));
        mBinding.danmuSizeText.setText((danmuSize = ResUtil.getStringArray(R.array.select_danmu_size))[Setting.getDanmuSize()]);
        mBinding.danmuSpeedText.setText((danmuSpeed = ResUtil.getStringArray(R.array.select_danmu_speed))[Setting.getDanmuSpeed()]);
    }

    @Override
    protected void initEvent() {
        mBinding.player.setOnClickListener(this::setPlayer);
        mBinding.ua.setOnClickListener(this::onUa);
        mBinding.http.setOnClickListener(this::setHttp);
        mBinding.flag.setOnClickListener(this::setFlag);
        mBinding.buffer.setOnClickListener(this::onBuffer);
        mBinding.tunnel.setOnClickListener(this::setTunnel);
        mBinding.caption.setOnClickListener(this::setCaption);
        mBinding.subtitle.setOnClickListener(this::onSubtitle);
        mBinding.caption.setOnLongClickListener(this::onCaption);
        mBinding.danmuSpeed.setOnClickListener(this::setDanmuSpeed);
        mBinding.danmuSize.setOnClickListener(this::setDanmuSize);
        mBinding.danmuMaxLine.setOnClickListener(this::onDanmuMaxLine);
    }

    private void onUa(View view) {
        UaDialog.create(this).show();
    }

    private void setPlayer(View view) {
        int index = Setting.getPlayer();
        Setting.putPlayer(index = index == player.length - 1 ? 0 : ++index);
        mBinding.playerText.setText(player[index]);
    }

    @Override
    public void setUa(String ua) {
        mBinding.uaText.setText(ua);
        Setting.putUa(ua);
    }

    private void setHttp(View view) {
        int index = Setting.getHttp();
        Setting.putHttp(index = index == http.length - 1 ? 0 : ++index);
        mBinding.httpText.setText(http[index]);
        ExoUtil.reset();
    }

    private void setFlag(View view) {
        int index = Setting.getFlag();
        Setting.putFlag(index = index == flag.length - 1 ? 0 : ++index);
        mBinding.flagText.setText(flag[index]);
    }

    private void onBuffer(View view) {
        BufferDialog.create(this).show();
    }

    @Override
    public void setBuffer(int times) {
        mBinding.bufferText.setText(String.valueOf(times));
        Setting.putBuffer(times);
    }

    private void setTunnel(View view) {
        Setting.putTunnel(!Setting.isTunnel());
        mBinding.tunnelText.setText(getSwitch(Setting.isTunnel()));
    }

    private void setCaption(View view) {
        Setting.putCaption(!Setting.isCaption());
        mBinding.captionText.setText(caption[Setting.isCaption() ? 1 : 0]);
    }

    private boolean onCaption(View view) {
        if (Setting.isCaption()) startActivity(new Intent(Settings.ACTION_CAPTIONING_SETTINGS));
        return Setting.isCaption();
    }

    private void onSubtitle(View view) {
        SubtitleDialog.create(this).show();
    }

    @Override
    public void setSubtitle(int size) {
        mBinding.subtitleText.setText(String.valueOf(size));
    }

    public void setDanmuSpeed(View view) {
        int index = Setting.getDanmuSpeed();
        Setting.putDanmuSpeed(index = index == danmuSpeed.length - 1 ? 0 : ++index);
        mBinding.danmuSpeedText.setText(danmuSpeed[index]);
    }

    public void setDanmuSize(View view) {
        int index = Setting.getDanmuSize();
        Setting.putDanmuSize(index = index == danmuSize.length - 1 ? 0 : ++index);
        mBinding.danmuSizeText.setText(danmuSize[index]);
    }

    public void onDanmuMaxLine(View view) {
        DanmuMaxLineDialog.create(this).show();
    }

    @Override
    public void setDanmuMaxLine(int maxLine) {
        mBinding.danmuMaxLineText.setText(String.valueOf(maxLine));
        Setting.putDanmuMaxLine(maxLine);
    }

}
