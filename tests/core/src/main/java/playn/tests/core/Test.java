/**
 * Copyright 2011 The PlayN Authors
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

package playn.tests.core;

import playn.core.CanvasImage;
import playn.core.Font;
import playn.core.ImageLayer;
import playn.core.Layer;
import playn.core.Pointer;
import playn.core.TextFormat;
import playn.core.TextLayout;
import static playn.core.PlayN.graphics;

public abstract class Test {

  public static final int UPDATE_RATE = 25;

  public abstract String getName();

  public abstract String getDescription();

  public void init() {
  }

  public void update(int delta) {
  }

  public void paint(float alpha) {
  }

  public void dispose() {
  }

  public boolean usesPositionalInputs () {
    return false;
  }

  protected float addTest(float lx, float ly, Layer.HasSize layer, String descrip) {
    return addTest(lx, ly, layer, descrip, layer.width());
  }

  protected float addTest(float lx, float ly, Layer.HasSize layer, String descrip, float twidth) {
    return addTest(lx, ly, layer, layer.width(), layer.height(), descrip, twidth);
  }

  protected float addTest(float lx, float ly, Layer layer, float lwidth, float lheight,
                         String descrip) {
    return addTest(lx, ly, layer, lwidth, lheight, descrip, lwidth);
  }

  protected float addTest(float lx, float ly, Layer layer, float lwidth, float lheight,
                          String descrip, float twidth) {
    graphics().rootLayer().addAt(layer, lx + (twidth-lwidth)/2, ly);
    return addDescrip(descrip, lx, ly + lheight + 5, twidth);
  }

  protected float addDescrip(String descrip, float x, float y, float width) {
    ImageLayer layer = createDescripLayer(descrip, width);
    graphics().rootLayer().addAt(layer, Math.round(x + (width - layer.width())/2), y);
    return y + layer.height();
  }

  protected static ImageLayer createDescripLayer(String descrip, float width) {
    TextLayout layout = graphics().layoutText(
      descrip, TEXT_FMT.withWrapping(width, TextFormat.Alignment.CENTER));
    CanvasImage image = graphics().createImage(layout.width(), layout.height());
    image.canvas().setFillColor(0xFF000000);
    image.canvas().fillText(layout, 0, 0);
    return graphics().createImageLayer(image);
  }

  protected static CanvasImage formatText (String text, boolean border) {
    TextLayout layout = graphics().layoutText(text, TEXT_FMT);
    float margin = border ? 10 : 0;
    float width = layout.width()+2*margin, height = layout.height()+2*margin;
    CanvasImage image = graphics().createImage(width, height);
    image.canvas().setStrokeColor(0xFF000000);
    image.canvas().setFillColor(0xFF000000);
    image.canvas().fillText(layout, margin, margin);
    if (border)
      image.canvas().strokeRect(0, 0, width-1, height-1);
    return image;
  }

  protected static ImageLayer createButton (String text, final Runnable onClick) {
    CanvasImage image = formatText(text, true);
    ImageLayer button = graphics().createImageLayer(image);
    button.addListener(new Pointer.Adapter() {
      public void onPointerStart(Pointer.Event event) {
        onClick.run();
      }
    });
    return button;
  }

  protected static final TextFormat TEXT_FMT = new TextFormat().withFont(
    graphics().createFont("Helvetica", Font.Style.PLAIN, 12));
}
