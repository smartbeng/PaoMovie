// Generated code from Butter Knife. Do not modify!
package com.pdy.mobile;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class FeedBack$$ViewBinder<T extends com.pdy.mobile.FeedBack> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558517, "field 'content'");
    target.content = finder.castView(view, 2131558517, "field 'content'");
    view = finder.findRequiredView(source, 2131558518, "field 'email'");
    target.email = finder.castView(view, 2131558518, "field 'email'");
    view = finder.findRequiredView(source, 2131558516, "method 'Back'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.Back();
        }
      });
    view = finder.findRequiredView(source, 2131558519, "method 'Submit'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.Submit();
        }
      });
  }

  @Override public void unbind(T target) {
    target.content = null;
    target.email = null;
  }
}
