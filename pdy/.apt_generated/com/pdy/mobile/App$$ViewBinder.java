// Generated code from Butter Knife. Do not modify!
package com.pdy.mobile;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class App$$ViewBinder<T extends com.pdy.mobile.App> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558439, "field 'paoBangLin'");
    target.paoBangLin = finder.castView(view, 2131558439, "field 'paoBangLin'");
    view = finder.findRequiredView(source, 2131558448, "field 'webviewsRela'");
    target.webviewsRela = finder.castView(view, 2131558448, "field 'webviewsRela'");
    view = finder.findRequiredView(source, 2131558443, "field 'paoBaLin'");
    target.paoBaLin = finder.castView(view, 2131558443, "field 'paoBaLin'");
    view = finder.findRequiredView(source, 2131558438, "field 'topLin'");
    target.topLin = finder.castView(view, 2131558438, "field 'topLin'");
    view = finder.findRequiredView(source, 2131558449, "field 'bottomImageLin'");
    target.bottomImageLin = finder.castView(view, 2131558449, "field 'bottomImageLin'");
    view = finder.findRequiredView(source, 2131558445, "method 'guanZhuRen'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.guanZhuRen();
        }
      });
    view = finder.findRequiredView(source, 2131558454, "method 'ClickWoDe'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.ClickWoDe();
        }
      });
    view = finder.findRequiredView(source, 2131558452, "method 'showMoreWindow'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.showMoreWindow(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558451, "method 'ClickPaoBang'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.ClickPaoBang();
        }
      });
    view = finder.findRequiredView(source, 2131558450, "method 'ClickPaoPao'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.ClickPaoPao();
        }
      });
    view = finder.findRequiredView(source, 2131558453, "method 'ClickPaoBa'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.ClickPaoBa();
        }
      });
    view = finder.findRequiredView(source, 2131558441, "method 'NiaoDianBang'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.NiaoDianBang();
        }
      });
    view = finder.findRequiredView(source, 2131558446, "method 'ChuiPaoPao'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.ChuiPaoPao();
        }
      });
    view = finder.findRequiredView(source, 2131558447, "method 'SouSuo'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.SouSuo();
        }
      });
    view = finder.findRequiredView(source, 2131558442, "method 'SouSuo'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.SouSuo();
        }
      });
    view = finder.findRequiredView(source, 2131558444, "method 'GuanZhuDianYing'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.GuanZhuDianYing();
        }
      });
    view = finder.findRequiredView(source, 2131558440, "method 'BaoDianBang'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.BaoDianBang();
        }
      });
    target.paoBangChangeImage = Finder.listOf(
        finder.<com.pdy.mobile.ClickChangeImage>findRequiredView(source, 2131558440, "field 'paoBangChangeImage'"),
        finder.<com.pdy.mobile.ClickChangeImage>findRequiredView(source, 2131558441, "field 'paoBangChangeImage'")
    );
    target.paoBaChangeImage = Finder.listOf(
        finder.<com.pdy.mobile.ClickChangeImage>findRequiredView(source, 2131558444, "field 'paoBaChangeImage'"),
        finder.<com.pdy.mobile.ClickChangeImage>findRequiredView(source, 2131558445, "field 'paoBaChangeImage'"),
        finder.<com.pdy.mobile.ClickChangeImage>findRequiredView(source, 2131558446, "field 'paoBaChangeImage'")
    );
  }

  @Override public void unbind(T target) {
    target.paoBangLin = null;
    target.webviewsRela = null;
    target.paoBaLin = null;
    target.topLin = null;
    target.bottomImageLin = null;
    target.paoBangChangeImage = null;
    target.paoBaChangeImage = null;
  }
}
