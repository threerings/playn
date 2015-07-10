package playn.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores Keyboard modifiers used in {@link playn.core.Keyboard.Event}.
 */
public class Modifiers extends ArrayList<Key> {

  public Modifiers() {
    super();
  }

  public Modifiers(List<Key> modifiers) {
    super(modifiers);
  }

  public boolean isAltPressed() {
    return contains(Key.ALT);
  }

  public boolean isCtrlPressed() {
    return contains(Key.CONTROL);
  }

  public boolean isShiftPressed() {
    return contains(Key.SHIFT);
  }

  public boolean isMetaPressed() {
    return contains(Key.META);
  }
}
