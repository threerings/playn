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

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/**
 * Provides implementations for per-platform concrete {@link GroupLayer}s. Because of single
 * inheritance (and lack of traits) we have to delegate this implementation rather than provide an
 * abstract base class.
 */
public class GroupLayerImpl<L extends AbstractLayer>
{
  /** This group's children. */
  public List<L> children = new ArrayList<L>();

  /**
   * @return the index into the children array at which the layer was inserted (based on depth).
   */
  public int add(GroupLayer self, L child) {
    // if this child has equal or greater depth to the last child, we can append directly and avoid
    // a log(N) search; this is helpful when adding a Layer to the last index
    int count = children.size(), index;
    if (count == 0 || children.get(count-1).depth() <= child.depth()) {
      index = count;
    } else {
      // otherwise find the appropriate insertion point via binary search
      index = findInsertion(child.depth());
    }

    // remove the child from any existing parent, preventing multiple parents
    if (child.parent() != null) {
      child.parent().remove(child);
    }
    addChild(self, index, child);
    return index;
  }

  // TODO: remove this when GroupLayer.add(int,Layer) is removed
  public void add(GroupLayer self, int index, L child) {
    // remove the child from any existing parent, preventing multiple parents
    if (child.parent() != null) {
      child.parent().remove(child);
    }
    addChild(self, index, child);
  }

  public void remove(GroupLayer self, L child) {
    int index = child.parentIndex();
    if (index < 0 || child.parent() != self) {
      throw new UnsupportedOperationException(
        "Could not remove Layer because it is not a child of the GroupLayer");
    }
    removeChild(self, index);
  }

  // TODO: remove this when GroupLayer.remove(int) is removed
  public void remove(GroupLayer self, int index) {
    removeChild(self, index);
  }

  public void clear(GroupLayer self) {
    for (int i = children.size() - 1; i >= 0; i--) {
      L child = children.get(i);
      child.onRemove();
      child.setParent(null);
      child.setParentIndex(-1);
    }
    children.clear();
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

    int oldIndex = child.parentIndex();
    float newDepth = child.depth();
    int size = children.size();
    boolean ascending = (newDepth > oldDepth);
    int nextIndex = oldIndex;

    // swap children until we reach one end of the array or find the correct location for the child
    while ((ascending && ++nextIndex < size) || (!ascending && --nextIndex >= 0)) {
      child = children.get(nextIndex);
      if ((ascending && child.depth > newDepth) || (!ascending && child.depth < newDepth))
        break;
      Collections.swap(children, oldIndex, nextIndex);
      children.get(oldIndex).setParentIndex(oldIndex);
      oldIndex = nextIndex;
    }

    children.get(oldIndex).setParentIndex(oldIndex);

    return oldIndex;
  }

  /**
   * Add a child to children and update each child's parentIndex.
   */
  private void addChild(GroupLayer self, int index, L child) {
    children.add(index, child);
    for (int i = children.size() - 1; i > index; i--) {
      children.get(i).setParentIndex(i);
    }
    child.setParent(self);
    child.setParentIndex(index);
    child.onAdd();
  }

  /**
   * Remove a child from children and update each child's parentIndex.
   */
  private void removeChild(GroupLayer self, int index) {
    L child = children.remove(index);
    for (int i = children.size() - 1; i >= index; index--) {
      children.get(i).setParentIndex(i);
    }
    child.onRemove();
    child.setParent(null);
    child.setParentIndex(-1);
  }

  // who says you never have to write binary search?
  private int findInsertion(float depth) {
    int low = 0, high = children.size()-1;
    while (low <= high) {
      int mid = (low + high) >>> 1;
      float midDepth = children.get(mid).depth();
      if (depth > midDepth) {
        low = mid + 1;
      } else if (depth < midDepth) {
        high = mid - 1;
      } else {
        return mid;
      }
    }
    return low;
  }
}
