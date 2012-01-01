/**
 * Copyright 2011 The PlayN Authors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package playn.core;

import java.util.Iterator;

import playn.core.json.JsonParserException;
import playn.core.json.JsonSink;

/**
 * PlayN JSON parsing and serialization interface.
 */
public interface Json {
  /**
   * A {@link JsonSink} that writes JSON to a string.
   */
  interface Writer extends JsonSink<Writer> {

    /**
     * Completes this JSON writing session and returns the internal representation as a
     * {@link String}.
     */
    String write();
  }

  /**
   * A JSON array that assumes all values are of a uniform JSON type.
   */
  interface TypedArray<T> extends Iterable<T> {
    /**
     * Returns the number of values in this array.
     */
    public int length();

    /**
     * Returns the value at the given index, or the default value for <code>T</code> if there's a
     * value of a different type at the index.
     * 
     * @throws ArrayIndexOutOfBoundsException if <code>index &lt; 0</code> or <code>index >=
     * length</code>
     */
    public T get(int index);

    /**
     * Returns the value at the given index, or the specified default value if there's a value of a
     * different type at the index.
     * 
     * @throws ArrayIndexOutOfBoundsException if <code>index &lt; 0</code> or <code>index >=
     * length</code>
     */
    public T get(int index, T dflt);

    /**
     * Returns an iterator over the values of the assumed type in this array. If a value at a given
     * index isn't of the assumed type, the default value for the assumed type will be returned by
     * <code>next</code>.
     */
    @Override
    public Iterator<T> iterator();
  }

  /**
   * Represents a parsed JSON array as a simple int->value map.
   */
  interface Array {

    /**
     * Gets the length of this array.
     */
    int length();

    /**
     * Gets the boolean value at the given index, or <code>false</code> if there is no value at this
     * index.
     */
    boolean getBoolean(int index);

    /**
     * Gets the boolean value at the given index, or the default if there is no value at this index.
     */
    boolean getBoolean(int index, boolean dflt);

    /**
     * Gets the double value at the given index, or <code>0</code> if there is no value at this
     * index.
     */
    double getDouble(int index);

    /**
     * Gets the double value at the given index, or the default if there is no value at this index.
     */
    double getDouble(int index, double dflt);

    /**
     * Gets the integer value at the given index, or <code>0</code> if there is no value at this
     * index.
     */
    int getInt(int index);

    /**
     * Gets the integer value at the given index, or the default if there is no value at this index.
     */
    int getInt(int index, int dflt);

    /**
     * Gets the float value at the given index, or <code>0</code> if there is no value at this
     * index.
     */
    float getNumber(int index);

    /**
     * Gets the float value at the given index, or the default if there is no value at this index.
     */
    float getNumber(int index, float dflt);

    /**
     * Gets the string value at the given index, or <code>null</code> if there is no value at this
     * index.
     */
    String getString(int index);

    /**
     * Gets the string value at the given index, or the default if there is no value at this index.
     */
    String getString(int index, String dflt);

    /**
     * Gets the object value at the given index, or <code>null</code> if there is no value at this
     * index.
     */
    Object getObject(int index);

    /**
     * Gets the object value at the given index, or the default if there is no value at this index.
     */
    Object getObject(int index, Object dflt);

    /**
     * Gets the array value at the given index, or <code>null</code> if there is no value at this
     * index.
     */
    Array getArray(int index);

    /**
     * Gets the array value at the given index, or the default if there is no value at this index.
     */
    Array getArray(int index, Array dflt);

    /**
     * Gets an array at the given index that assumes its values are of the given json type, or
     * <code>null</code> if there is no value at this index.
     * 
     * @param jsonType one of Json.Object, Boolean, Integer, Double, or String
     * 
     * @throws IllegalArgumentException if jsonType is of an invalid type.
     */
    <T> TypedArray<T> getArray(int index, Class<T> jsonType);

    /**
     * Returns <code>true</code> if the value at the given index is an array.
     */
    boolean isArray(int index);

    /**
     * Returns <code>true</code> if the value at the given index is a boolean.
     */
    boolean isBoolean(int index);

    /**
     * Returns <code>true</code> if the value at the given index is null.
     */
    boolean isNull(int index);

    /**
     * Returns <code>true</code> if the value at the given index is a number.
     */
    boolean isNumber(int index);

    /**
     * Returns <code>true</code> if the value at the given index is a string.
     */
    boolean isString(int index);

    /**
     * Returns <code>true</code> if the value at the given index is an object.
     */
    boolean isObject(int index);

    /**
     * Appends a JSON boolean, null, number, object, or array value.
     */
    void add(java.lang.Object value);

    /**
     * Inserts a JSON boolean, null, number, object, or array value at the given index. If the index
     * is past the end of the array, the array is null-padded to the given index.
     */
    void add(int index, java.lang.Object value);

    /**
     * Removes a JSON value from the given index. If the index is out of bounds, this is a no-op.
     */
    void remove(int index);
    
    /**
     * Sets a JSON boolean, null, number, object, or array value at the given index. If the index
     * is past the end of the array, the array is null-padded to the given index.
     */
    void set(int index, java.lang.Object value);

    /**
     * Writes this object to a {@link JsonSink}.
     */
    <T extends JsonSink<T>> JsonSink<T> write(JsonSink<T> sink);
  }

  /**
   * Represents a parsed JSON object as a simple string->value map.
   */
  interface Object {
    /**
     * Gets the boolean value at the given key, or <code>false</code> if there is no value at this
     * key.
     */
    boolean getBoolean(String key);

    /**
     * Gets the boolean value at the given key, or the default if there is no value at this key.
     */
    boolean getBoolean(String key, boolean dflt);

    /**
     * Gets the double value at the given key, or <code>0</code> if there is no value at this key.
     */
    double getDouble(String key);

    /**
     * Gets the double value at the given key, or the default if there is no value at this key.
     */
    double getDouble(String key, double dflt);

    /**
     * Gets the integer value at the given key, or <code>0</code> if there is no value at this key.
     */
    int getInt(String key);

    /**
     * Gets the integer value at the given key, or the default if there is no value at this key.
     */
    int getInt(String key, int dflt);

    /**
     * Gets the float value at the given key, or <code>0</code> if there is no value at this key.
     */
    float getNumber(String key);

    /**
     * Gets the float value at the given key, or the default if there is no value at this key.
     */
    float getNumber(String key, float dflt);

    /**
     * Gets the string value at the given key, or <code>null</code> if there is no value at this
     * key.
     */
    String getString(String key);

    /**
     * Gets the string value at the given key, or the default if there is no value at this key.
     */
    String getString(String key, String dflt);

    /**
     * Gets the object value at the given key, or <code>null</code> if there is no value at this
     * key.
     */
    Object getObject(String key);

    /**
     * Gets the object value at the given key, or the default if there is no value at this key.
     */
    Object getObject(String key, Object dflt);

    /**
     * Gets the array value at the given key, or <code>null</code> if there is no value at this key.
     */
    Array getArray(String key);

    /**
     * Gets the array value at the given key, or the default if there is no value at this key.
     */
    Array getArray(String key, Array dflt);

    /**
     * Gets an array at the given key that assumes its values are of the given json type, or
     * <code>null</code> if there is no value at this key.
     * 
     * @param jsonType one of Json.Object, Boolean, Integer, Double, or String
     * 
     * @throws IllegalArgumentException if jsonType is of an invalid type.
     */
    <T> TypedArray<T> getArray(String key, Class<T> valueType);

    /**
     * Gets an array at the given key that assumes its values are of the given json type, or the
     * default if there is no value at this key.
     * 
     * @param jsonType one of Json.Object, Boolean, Integer, Double, or String
     * @param dflt An existing typed array
     * 
     * @throws IllegalArgumentException if jsonType is of an invalid type.
     */
    <T> TypedArray<T> getArray(String key, Class<T> valueType, TypedArray<T> dflt);

    /**
     * Returns true if this object contains a value at the specified key, false if not.
     */
    boolean containsKey(String key);

    /**
     * Gets a set of keys for this JSON object. Use {@link Object#keys()} instead.
     */
    @Deprecated
    TypedArray<String> getKeys();

    /**
     * Gets a snapshot of the current set of keys for this JSON object. Modifications to the object
     * will not be reflected in this set of keys.
     */
    TypedArray<String> keys();

    /**
     * Returns <code>true</code> if the value at the given key is an array.
     */
    boolean isArray(String key);

    /**
     * Returns <code>true</code> if the value at the given key is a boolean.
     */
    boolean isBoolean(String key);

    /**
     * Returns <code>true</code> if the value at the given key is null.
     */
    boolean isNull(String key);

    /**
     * Returns <code>true</code> if the value at the given key is a number.
     */
    boolean isNumber(String key);

    /**
     * Returns <code>true</code> if the value at the given key is a string.
     */
    boolean isString(String key);

    /**
     * Returns <code>true</code> if the value at the given key is an object.
     */
    boolean isObject(String key);

    /**
     * Inserts a JSON null, object, array or string value at the given key.
     */
    void put(String key, java.lang.Object value);

    /**
     * Removes a JSON value at the given key.
     */
    void remove(String key);
    
    /**
     * Writes this object to a {@link JsonSink}.
     */
    <T extends JsonSink<T>> JsonSink<T> write(JsonSink<T> sink);
  }

  Array createArray();

  Object createObject();

  boolean isObject(java.lang.Object o);

  boolean isArray(java.lang.Object o);

  /**
   * Creates a new {@link Writer}, which can be used to serialize data into the JSON format.
   * 
   * <code>
   * // An example of using the JSON writer interface.
   * String jsonString = json.newWriter()
   *     .object()
   *         .value("x", 10)
   *         .value("y", 10)
   *         .object("nestedObject")
   *              .value("id", "xyz123")
   *         .end()
   *         .array("nestedArray")
   *             .value(1)
   *             .value(2)
   *             .value(3)
   *             .value(4)
   *             .value(5)
   *         .end()
   *     .end()
   * .done();
   * 
   * // Produces:
   * {
   *   'x': 10,
   *   'y': 10,
   *   'nestedObject': {
   *     'id': 'xyz123'
   *   },
   *   'nestedArray': [
   *     1, 2, 3, 4, 5
   *   ]
   * }
   * </code>
   */
  Writer newWriter();

  /**
   * Parses the given JSON string into an {@link Object} that can be dynamically introspected.
   */
  Object parse(String json) throws JsonParserException;

  /**
   * Parses the given JSON string into an {@link Array} that can be dynamically introspected.
   */
  Array parseArray(String json) throws JsonParserException;
}
