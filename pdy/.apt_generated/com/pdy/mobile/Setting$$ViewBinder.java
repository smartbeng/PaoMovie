// Generated code from Butter Knife. Do not modify!
package com.pdy.mobile;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class Setting$$ViewBinder<T extends com.pdy.mobile.Setting> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558660, "field 'autoPlayVideoSettingsText'");
    target.autoPlayVideoSettingsText = finder.castView(view, 2131558660, "field 'autoPlayVideoSettingsText'");
    view = finder.findRequiredView(source, 2131558665, "field 'cleanCacheText'");
    target.cleanCacheText = finder.castView(view, 2131558665, "field 'cleanCacheText'");
    view = finder.findRequiredView(source, 2131558659, "method 'AutoPlayVideoSettings'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.AutoPlayVideoSettings();
        }
      });
    view = finder.findRequiredView(source, 2131558661, "method 'ApplicationScore'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.ApplicationScore();
        }
      });
    view = finder.findRequiredView(source, 2131558650, "method 'exit'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.exit();
        }
      });
    view = finder.findRequiredView(source, 2131558662, "method 'FeedBack'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.FeedBack();
        }
      });
    view = finder.findRequiredView(source, 2131558664, "method 'CleanCache'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.CleanCache();
        }
      });
    view = finder.findRequiredView(source, 2131558516, "method 'Back'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.Back();
        }
      });
    view = finder.findRequiredView(source, 2131558663, "method 'About'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.About();
        }
      });
    view = finder.findRequiredView(source, 2131558656, "method 'ModifyPassword'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.ModifyPassword();
        }
      });
  }

  @Override public void unbind(T target) {
    target.autoPlayVideoSettingsText = null;
    target.cleanCacheText = null;
  }
}
