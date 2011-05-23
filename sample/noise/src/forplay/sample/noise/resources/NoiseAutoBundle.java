package forplay.sample.noise.resources;

import com.google.gwt.core.client.GWT;
import forplay.core.AutoClientBundleWithLookup;

public interface NoiseAutoBundle extends AutoClientBundleWithLookup {
  static NoiseAutoBundle INSTANCE = GWT.create(NoiseAutoBundle.class);
}
