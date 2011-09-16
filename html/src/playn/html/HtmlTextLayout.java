/**
 * Copyright 2010 The PlayN Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package playn.html;

import com.google.gwt.canvas.dom.client.Context2d;
import playn.core.Font;
import playn.core.TextFormat;
import playn.core.TextLayout;

import java.util.ArrayList;
import java.util.List;

import static playn.core.PlayN.graphics;

class HtmlTextLayout implements TextLayout {

  private TextFormat format;
  private HtmlFontMetrics metrics;
  private float width, height;
  private List<Line> lines = new ArrayList<Line>();

  private static class Line {
    public final String text;
    public final float width;
    public Line(String text, float width) {
      this.text = text;
      this.width = width;
    }
  }

  HtmlTextLayout(Context2d ctx, String text, TextFormat format) {
    Font font = getFont(format);
    this.format = format;
    this.metrics = ((HtmlGraphics)graphics()).getFontMetrics(font);
    configContext(ctx);

    // normalize newlines in the text (Windows: CRLF -> LF, Mac OS pre-X: CR -> LF)
    text = text.replace("\r\n", "\n").replace('\r', '\n');

    if (format.shouldWrap() || text.indexOf('\n') != -1) {
      for (String line : text.split("\\n")) {
        String[] words = line.split("\\s"); // TODO: preserve intra-line whitespace
        for (int idx = 0; idx < words.length; ) {
          // note: measureLine has the side effect of adding the measured line to this.lines and
          // setting this.width to the maximum of the current width and the measured line width
          idx = measureLine(ctx, words, idx);
        }
      }
      height = metrics.height * lines.size();

    } else {
      width = (float)ctx.measureText(text).getWidth();
      height = metrics.height;
      lines.add(new Line(text, width));
    }

    // Canvas.measureText does not account for the extra width consumed by italic characters, so we
    // fudge in a fraction of an em and hope the font isn't too slanted
    switch (font.style()) {
    case ITALIC:      width += metrics.emwidth/8; break;
    case BOLD_ITALIC: width += metrics.emwidth/6; break;
    }
  }

  @Override
  public float width() {
    return width;
  }

  @Override
  public float height() {
    return height;
  }

  @Override
  public int lineCount() {
    return lines.size();
  }

  @Override
  public TextFormat format() {
    return format;
  }

  void draw(Context2d ctx, float x, float y) {
    configContext(ctx);

    if (format.effect instanceof TextFormat.Effect.Shadow) {
      TextFormat.Effect.Shadow seffect = (TextFormat.Effect.Shadow)format.effect;
      ctx.setShadowColor(HtmlGraphics.cssColorString(seffect.shadowColor));
      ctx.setShadowOffsetX(seffect.shadowOffsetX);
      ctx.setShadowOffsetY(seffect.shadowOffsetY);
      drawText(ctx, x, y);
    } else if (format.effect instanceof TextFormat.Effect.Outline) {
      TextFormat.Effect.Outline oeffect = (TextFormat.Effect.Outline)format.effect;
      ctx.save();
      ctx.setFillStyle(HtmlGraphics.cssColorString(oeffect.outlineColor));

      drawText(ctx, x + 0, y + 0);
      drawText(ctx, x + 0, y + 1);
      drawText(ctx, x + 0, y + 2);
      drawText(ctx, x + 1, y + 0);
      drawText(ctx, x + 1, y + 2);
      drawText(ctx, x + 2, y + 0);
      drawText(ctx, x + 2, y + 1);
      drawText(ctx, x + 2, y + 2);

      ctx.restore();
      drawText(ctx, x + 1, y + 1);
    } else {
      drawText(ctx, x, y);
    }
  }

  void drawText(Context2d ctx, float x, float y) {
      float ypos = 0;
      for (Line line : lines) {
        ctx.fillText(line.text, x + format.align.getX(line.width, width), y + ypos);
        ypos += metrics.height;
      }
  }

  void configContext(Context2d ctx) {
    Font font = getFont(format);
    String style = "";
    switch (font.style()) {
    case BOLD:        style = "bold";   break;
    case ITALIC:      style = "italic"; break;
    case BOLD_ITALIC: style = "bold italic"; break;
    }

    ctx.setFillStyle(HtmlGraphics.cssColorString(format.textColor));
    ctx.setFont(style + " " + font.size() + "px " + font.name());
    ctx.setTextBaseline(Context2d.TextBaseline.TOP);
  }

  Font getFont(TextFormat format) {
    return format.font == null ? HtmlFont.DEFAULT : format.font;
  }

  int measureLine(Context2d ctx, String[] words, int idx) {
    // we always put at least one word on a line
    String line = words[idx++];
    int startIdx = idx;

    // build a rough estimate line based on character count and emwidth
    for (; idx < words.length; idx++) {
      String nline = line + " " + words[idx];
      if (nline.length() * metrics.emwidth > format.wrapWidth) break;
      line = nline;
    }

    // now, based on exact measurements, either add more words...
    double lineWidth = ctx.measureText(line).getWidth();
    if (lineWidth < format.wrapWidth) {
      for (; idx < words.length; idx++) {
        String nline = line + " " + words[idx];
        double nlineWidth = ctx.measureText(nline).getWidth();
        if (nlineWidth > format.wrapWidth) break;
        line = nline;
        lineWidth = nlineWidth;
      }
    }

    // or pop words off...
    while (lineWidth > format.wrapWidth && idx > (startIdx+1)) { // don't pop off the last word
      line = line.substring(0, line.length() - words[--idx].length() - 1);
      lineWidth = ctx.measureText(line).getWidth();
    }

    // finally, if we're still over the limit (we have a single looong word), hard break
    if (lineWidth > format.wrapWidth) {
      StringBuilder remainder = new StringBuilder();
      while (lineWidth > format.wrapWidth && line.length() > 1) {
        // this could be more efficient, but this edge case should be rare enough not to matter
        int lastIdx = line.length()-1;
        remainder.insert(0, line.charAt(lastIdx));
        line = line.substring(0, lastIdx);
        lineWidth = ctx.measureText(line).getWidth();
      }
      words[--idx] = remainder.toString();
    }

    lines.add(new Line(line, (float)lineWidth));
    width = (float)Math.max(width, lineWidth);
    return idx;
  }
}
