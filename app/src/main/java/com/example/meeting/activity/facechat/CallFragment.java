/*
 *  Copyright 2015 The WebRTC Project Authors. All rights reserved.
 *
 *  Use of this source code is governed by a BSD-style license
 *  that can be found in the LICENSE file in the root of the source
 *  tree. An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */

package com.example.meeting.activity.facechat;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.meeting.R;

import org.webrtc.RendererCommon.ScalingType;

/**
 * Fragment for call control.
 */
public class CallFragment extends Fragment {
  private View controlView;
  private ImageButton disconnectButton;
  private ImageButton cameraSwitchButton;
  private ImageButton toggleMuteButton;
  private ImageButton cameraMuteButton;
  private ImageButton btnCaptuer;
  private OnCallEvents callEvents;
  private ScalingType scalingType;
  private ImageButton btnFace;
  private boolean videoCallEnabled = true;

  /**
   * Call control interface for container activity.
   */
  public interface OnCallEvents {
    void onCallHangUp();
    void onCameraSwitch();
    void onCaptureFormatChange(int width, int height, int framerate);
    boolean onToggleMic();
    boolean onToggleVideo();
    void asd();
    boolean changeFace();
  }

  @Override
  public View onCreateView(
          LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    controlView = inflater.inflate(R.layout.fragment_call, container, false);

    // Create UI controls.

    disconnectButton = controlView.findViewById(R.id.button_call_disconnect);
    cameraSwitchButton = controlView.findViewById(R.id.button_call_switch_camera);
    toggleMuteButton = controlView.findViewById(R.id.button_call_toggle_mic);
    cameraMuteButton = controlView.findViewById(R.id.button_camera_off);
    btnCaptuer = controlView.findViewById(R.id.btnCapture);
    btnFace = controlView.findViewById(R.id.btnFace);

    disconnectButton.setColorFilter(Color.parseColor("#ff2222"));
    cameraSwitchButton.setColorFilter(Color.parseColor("#ffffff"));
    toggleMuteButton.setColorFilter(Color.parseColor("#ffffff"));
    cameraMuteButton.setColorFilter(Color.parseColor("#ffffff"));
    btnCaptuer.setColorFilter(Color.parseColor("#ffffff"));
    btnFace.setColorFilter(Color.parseColor("#ffffff"));


    btnFace.setOnClickListener(view -> {
      boolean enabled = callEvents.changeFace();
      btnFace.setAlpha(enabled ? 1.0f : 0.3f);});

    // Add buttons click events.
    disconnectButton.setOnClickListener(view -> callEvents.onCallHangUp());

    cameraSwitchButton.setOnClickListener(view -> callEvents.onCameraSwitch());

    btnCaptuer.setOnClickListener(view -> callEvents.asd());

    scalingType = ScalingType.SCALE_ASPECT_FILL;

    toggleMuteButton.setOnClickListener(view -> {
      boolean enabled = callEvents.onToggleMic();
      toggleMuteButton.setAlpha(enabled ? 1.0f : 0.3f);
    });

    cameraMuteButton.setOnClickListener(view -> {
      boolean enabled = callEvents.onToggleVideo();
      cameraMuteButton.setAlpha(enabled ? 1.0f : 0.3f);
    });

    return controlView;
  }

  @Override
  public void onStart() {
    super.onStart();

    if (!videoCallEnabled) {
      cameraSwitchButton.setVisibility(View.INVISIBLE);
    }

  }

  // TODO(sakal): Replace with onAttach(Context) once we only support API level 23+.
  @SuppressWarnings("deprecation")
  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    callEvents = (OnCallEvents) activity;
  }
}
