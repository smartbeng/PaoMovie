// Generated code from Butter Knife. Do not modify!
package com.pdy.mobile;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class AutoPlayVideoSettings$$ViewBinder<T extends com.pdy.mobile.AutoPlayVideoSettings> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558520, "method 'Determine'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.Determine();
        }
      });
    view = finder.findRequiredView(source, 2131558522, "method 'NetworkWifi'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.NetworkWifi();
        }
      });
    view = finder.findRequiredView(source, 2131558521, "method 'Wifi'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.Wifi();
        }
      });
    view = finder.findRequiredView(source, 2131558523, "method 'NoWifiNetwork'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.NoWifiNetwork();
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
    target.checkBoxs = Finder.listOf(
        finder.<android.widget.CheckBox>findRequiredView(source, 2131558521, "field 'checkBoxs'"),
        finder.<android.widget.CheckBox>findRequiredView(source, 2131558522, "field 'checkBoxs'"),
        finder.<android.widget.CheckBox>findRequiredView(source, 2131558523, "field 'checkBoxs'")
    );
  }

  @Override public void unbind(T target) {
    target.checkBoxs = null;
  }
}
