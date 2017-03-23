// Generated code from Butter Knife. Do not modify!
package com.pdy.mobile;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class VideoPlayer$$ViewBinder<T extends com.pdy.mobile.VideoPlayer> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558728, "field 'videoControl' and method 'VideoControl'");
    target.videoControl = finder.castView(view, 2131558728, "field 'videoControl'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.VideoControl();
        }
      });
    view = finder.findRequiredView(source, 2131558726, "field 'videoView'");
    target.videoView = finder.castView(view, 2131558726, "field 'videoView'");
    view = finder.findRequiredView(source, 2131558516, "method 'Back'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.Back();
        }
      });
    view = finder.findRequiredView(source, 2131558727, "method 'XuanZhong'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.XuanZhong();
        }
      });
  }

  @Override public void unbind(T target) {
    target.videoControl = null;
    target.videoView = null;
  }
}
