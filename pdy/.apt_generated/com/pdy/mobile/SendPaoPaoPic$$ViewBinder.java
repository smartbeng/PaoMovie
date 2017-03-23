// Generated code from Butter Knife. Do not modify!
package com.pdy.mobile;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SendPaoPaoPic$$ViewBinder<T extends com.pdy.mobile.SendPaoPaoPic> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558633, "field 'pdyBao' and method 'ClickBao'");
    target.pdyBao = finder.castView(view, 2131558633, "field 'pdyBao'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.ClickBao();
        }
      });
    view = finder.findRequiredView(source, 2131558648, "field 'imageGrid'");
    target.imageGrid = finder.castView(view, 2131558648, "field 'imageGrid'");
    view = finder.findRequiredView(source, 2131558641, "field 'promptRela'");
    target.promptRela = finder.castView(view, 2131558641, "field 'promptRela'");
    view = finder.findRequiredView(source, 2131558637, "field 'paoPaoQuan' and method 'PaoPaoQuan'");
    target.paoPaoQuan = finder.castView(view, 2131558637, "field 'paoPaoQuan'");
    ((android.widget.AdapterView<?>) view).setOnItemSelectedListener(
      new android.widget.AdapterView.OnItemSelectedListener() {
        @Override public void onItemSelected(
          android.widget.AdapterView<?> p0,
          android.view.View p1,
          int p2,
          long p3
        ) {
          target.PaoPaoQuan(p2);
        }
        @Override public void onNothingSelected(
          android.widget.AdapterView<?> p0
        ) {
          
        }
      });
    view = finder.findRequiredView(source, 2131558647, "field 'uploadProgress'");
    target.uploadProgress = finder.castView(view, 2131558647, "field 'uploadProgress'");
    view = finder.findRequiredView(source, 2131558468, "field 'titleView'");
    target.titleView = finder.castView(view, 2131558468, "field 'titleView'");
    view = finder.findRequiredView(source, 2131558634, "field 'pdyNiao' and method 'ClickNiao'");
    target.pdyNiao = finder.castView(view, 2131558634, "field 'pdyNiao'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.ClickNiao();
        }
      });
    view = finder.findRequiredView(source, 2131558517, "field 'content' and method 'TextChange'");
    target.content = finder.castView(view, 2131558517, "field 'content'");
    ((android.widget.TextView) view).addTextChangedListener(
      new android.text.TextWatcher() {
        @Override public void onTextChanged(
          java.lang.CharSequence p0,
          int p1,
          int p2,
          int p3
        ) {
          target.TextChange();
        }
        @Override public void beforeTextChanged(
          java.lang.CharSequence p0,
          int p1,
          int p2,
          int p3
        ) {
          
        }
        @Override public void afterTextChanged(
          android.text.Editable p0
        ) {
          
        }
      });
    view = finder.findRequiredView(source, 2131558509, "field 'donutProgress'");
    target.donutProgress = finder.castView(view, 2131558509, "field 'donutProgress'");
    view = finder.findRequiredView(source, 2131558631, "field 'currTextNum'");
    target.currTextNum = finder.castView(view, 2131558631, "field 'currTextNum'");
    view = finder.findRequiredView(source, 2131558630, "method 'Send'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.Send();
        }
      });
    view = finder.findRequiredView(source, 2131558642, "method 'ClickClose'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.ClickClose();
        }
      });
    view = finder.findRequiredView(source, 2131558635, "method 'ClickPrompt'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.ClickPrompt();
        }
      });
    view = finder.findRequiredView(source, 2131558649, "method 'ClickAddImage'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.ClickAddImage();
        }
      });
    view = finder.findRequiredView(source, 2131558516, "method 'ClickBack'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.ClickBack();
        }
      });
  }

  @Override public void unbind(T target) {
    target.pdyBao = null;
    target.imageGrid = null;
    target.promptRela = null;
    target.paoPaoQuan = null;
    target.uploadProgress = null;
    target.titleView = null;
    target.pdyNiao = null;
    target.content = null;
    target.donutProgress = null;
    target.currTextNum = null;
  }
}
