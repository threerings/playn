//
// $Id$

package playn.tests.core;

import playn.core.*;
import pythagoras.f.FloatMath;

import static playn.core.PlayN.*;

import playn.core.util.Callback;

public class SubImageTest extends Test {

  private int elapsed;
  private Image.Region osci;

  public SubImageTest(Platform platform) {
    super(platform);
  }

  @Override
  public String getName() {
    return "SubImageTest";
  }

  @Override
  public String getDescription() {
    return "Tests sub-image rendering in various circumstances.";
  }

  @Override
  public void init() {
    // create a canvas image and draw subimages of that
    int r = 30;
    CanvasImage cimg = graphics().createImage(2*r, 2*r);
    Canvas canvas = cimg.canvas();
    canvas.setFillColor(0xFF99CCFF);
    canvas.fillCircle(r, r, r);
    fragment("CanvasImage", cimg, 250, 160);

    // draw subimages of a simple static image
    Image orange = assets().getImage("images/orange.png");
    orange.addCallback(new Callback<Image>() {
      public void onSuccess(Image orange) {
        fragment("Image", orange, 250, 10);

        float pw = orange.width(), ph = orange.height(), phw = pw/2, phh = ph/2;
        final Image.Region orangerep = orange.subImage(0, phh/2, pw, phh);
        orangerep.setRepeat(true, true);

        // tile a sub-image, oh my!
        ImageLayer tiled = graphics().createImageLayer(orangerep);
        tiled.setSize(100, 100);
        addTest(10, 10, tiled, "ImageLayer tiled with subimage of Image");

        // use a subimage as a fill pattern
        CanvasImage pat = graphics().createImage(100, 100);
        pat.canvas().setFillPattern(orangerep.toPattern());
        pat.canvas().fillRect(0, 0, 100, 100);
        addTest(10, 160, graphics().createImageLayer(pat), "Canvas filled with subimage");

        // tile a sub-image of a surface image, oh my!
        SurfaceImage surf = graphics().createSurface(orange.width(), orange.height());
        surf.surface().drawImage(orange, 0, 0);
        Image.Region surfrep = surf.subImage(0, phh/2, pw, phh);
        surfrep.setRepeat(true, true);
        ImageLayer surftiled = graphics().createImageLayer(surfrep);
        surftiled.setSize(100, 100);
        addTest(10, 300, surftiled, "ImageLayer tiled with subimage of SurfaceImage");

        // draw a subimage to a canvas
        CanvasImage split = graphics().createImage(orange.width(), orange.height());
        split.canvas().drawImage(orange.subImage(0, 0, phw, phh), phw, phh);
        split.canvas().drawImage(orange.subImage(phw, 0, phw, phh), 0, phh);
        split.canvas().drawImage(orange.subImage(0, phh, phw, phh), phw, 0);
        split.canvas().drawImage(orange.subImage(phw, phh, phw, phh), 0, 0);
        addTest(140, 10, graphics().createImageLayer(split), "draw subimg into Canvas", 80);

        // draw a subimage in an immediate layer
        final Image.Region orangemid = orange.subImage(0, phh/2, pw, phh);
        ImmediateLayer imm = graphics().createImmediateLayer(new ImmediateLayer.Renderer() {
          public void render(Surface surf) {
            surf.drawImage(orangemid, 0, 0);
            surf.drawImage(orangemid, orangemid.width(), 0);
            surf.drawImage(orangemid, 0, orangemid.height());
            surf.drawImage(orangemid, orangemid.width(), orangemid.height());
          }
        });
        addTest(130, 100, imm, 2*orangemid.width(), 2*orangemid.height(),
                "draw subimg into Surface", 100);

        // draw a subimage whose bounds oscillate
        osci = orange.subImage(0, 0, orange.width(), orange.height());
        addTest(130, 190, graphics().createImageLayer(osci),
                "ImageLayer with subimage with changing width", 100);
      }
      public void onFailure(Throwable err) {
        log().warn("Failed to load orange image", err);
      }
    });
  }

  @Override
  public void update(int delta) {
    elapsed += delta;
    if (osci != null) {
      // round the width so that it sometimes goes to zero; just to be sure zero doesn't choke
      float width = Math.round(Math.abs(FloatMath.sin(elapsed/1000f)) * osci.parent().width());
      osci.setBounds(0, 0, width, osci.parent().height());
    }
  }

  protected void fragment(String source, Image image, float ox, float oy) {
    float hw = image.width()/2f, hh = image.height()/2f;
    Image ul = image.subImage(0, 0, hw, hh);
    Image ur = image.subImage(hw, 0, hw, hh);
    Image ll = image.subImage(0, hh, hw, hh);
    Image lr = image.subImage(hw, hh, hw, hh);
    Image ctr = image.subImage(hw/2, hh/2, hw, hh);

    float dx = hw + 10, dy = hh + 10;
    GroupLayer group = graphics().createGroupLayer();
    group.addAt(graphics().createImageLayer(ul), 0, 0);
    group.addAt(graphics().createImageLayer(ur), dx, 0);
    group.addAt(graphics().createImageLayer(ll), 0, dy);
    group.addAt(graphics().createImageLayer(lr), dx, dy);
    group.addAt(graphics().createImageLayer(ctr), dx/2, 2*dy);

    float xoff = image.width() + 20;
    group.addAt(scaleLayer(graphics().createImageLayer(ul), 2), xoff, 0);
    group.addAt(scaleLayer(graphics().createImageLayer(ur), 2), xoff+2*dx, 0);
    group.addAt(scaleLayer(graphics().createImageLayer(ll), 2), xoff, 2*dy);
    group.addAt(scaleLayer(graphics().createImageLayer(lr), 2), xoff+2*dx, 2*dy);

    graphics().rootLayer().addAt(group, ox, oy);
    addDescrip(source + " split into subimages, and scaled", ox, oy + image.height()*2 + 25,
               3*image.width()+40);
  }

  protected ImageLayer scaleLayer(ImageLayer layer, float scale) {
    layer.setScale(scale);
    return layer;
  }
}
