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

package playn.core;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Provides implementations for per-platform concrete {@link GroupLayer}s. Because of single
 * inheritance (and lack of traits) we have to delegate this implementation rather than provide an
 * abstract base class.
 */
// TODO(pdr): write a custom LinkedList implementation with an auxiliary hash table for
//            O(1) Layer->ListIterator operations. This will let us early-out in depthChanged
//            if the depth doesn't actually change.
public class GroupLayerImpl<L extends AbstractLayer>
{
  /** This group's children. */
  public List<L> children = new LinkedList<L>();
  
  /**
   * @return the index into the children array at which the layer was inserted (based on depth).
   */
  public int add(GroupLayer self, L child) {
    int index = 0;

    // check whether the last child has the same depth as this child, in which case append this
    // child to our list; this is a fast path for when all children have the same depth
    if (children.isEmpty() || ((LinkedList<L>)children).getLast().depth() <= child.depth()) {
      index = children.size();
      children.add(child);
    } else {
      index = orderedAdd(child);
    }

    // remove the child from any existing parent, preventing multiple parents
    if (child.parent() != null) {
      child.parent().remove(child);
    }
    child.setParent(self);
    child.onAdd();
    return index;
  }

  // TODO: remove this when GroupLayer.add(int,Layer) is removed
  public void add(GroupLayer self, int index, L child) {
    // remove the child from any existing parent, preventing multiple parents
    if (child.parent() != null) {
      child.parent().remove(child);
    }
    children.add(index, child);
    child.setParent(self);
    child.onAdd();
  }

  public void remove(GroupLayer self, L child) {
    if (!children.remove(child)) {
      throw new UnsupportedOperationException(
          "Could not remove Layer because it is not a child of the GroupLayer");
    }
  }

  // TODO: remove this when GroupLayer.remove(int) is removed
  public void remove(GroupLayer self, int index) {
    remove(index);
  }

  public void clear(GroupLayer self) {
    while (!children.isEmpty()) {
      remove(0);
    }
  }

  public void destroy(GroupLayer self) {
    AbstractLayer[] toDestroy = children.toArray(new AbstractLayer[children.size()]);
    // first remove all children efficiently
    self.clear();
    // now that the children have been detached, destroy them
    for (AbstractLayer child : toDestroy) {
      child.destroy();
    }
  }

  public void onAdd(GroupLayer self) {
    for (L child : children) {
      child.onAdd();
    }
  }

  public void onRemove(GroupLayer self) {
    for (L child : children) {
      child.onRemove();
    }
  }

  /**
   * @return the new index of the depth-changed layer.
   */
  public int depthChanged(GroupLayer self, Layer layer, float oldDepth) {
    // structuring things such that Java's type system knew what was going on here would require
    // making AbstractLayer and ParentLayer more complex than is worth it
    @SuppressWarnings("unchecked") L child = (L)layer;

    children.remove(child);
    return orderedAdd(child);
  }

  private void remove(int index) {
    L child = children.remove(index);
    child.onRemove();
    child.setParent(null);
  }

  /**
   * Insert the child in the ordered linked list, returning the position it was added at.
   */
  private int orderedAdd(L child) {
    float childDepth = child.depth();
    int index = 0;

    ListIterator<L> iter = children.listIterator();
    while (iter.hasNext()) {
      if (childDepth <= iter.next().depth()) {
        iter.previous();
        break;
      }
      index++;
    }
    iter.add(child);

    return index;
  }
}
