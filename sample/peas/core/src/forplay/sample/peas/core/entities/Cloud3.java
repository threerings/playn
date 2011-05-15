package forplay.sample.peas.core.entities;

import forplay.sample.peas.core.PeaWorld;

public class Cloud3 extends Cloud1 {
  @SuppressWarnings("hiding")
  public static String TYPE = "Cloud3";

  public Cloud3(PeaWorld peaWorld) {
    super(peaWorld);
  }

  @Override
  float getVelocity() {
    return 0.002f;
  }

  @Override
  String getImagePath() {
    return "images/Cloud3.png";
  }
}
