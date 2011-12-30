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
package playn.core.json;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Writer;
import java.nio.CharBuffer;
import java.util.Collection;
import java.util.Map;

//@formatter:off
/**
 * Factory for JSON writers that target {@link String}s and {@link Appendable}s. 
 * 
 * Creates writers that write JSON to a {@link String}, an {@link OutputStream}, or an
 * {@link Appendable} such as a {@link StringBuilder}, a {@link Writer} a {@link PrintStream} or a {@link CharBuffer}.
 * 
 * <pre>
 * String json = JsonEmitter.string()
 *     .object()
 *         .array("a")
 *             .value(1)
 *             .value(2)
 *         .end()
 *         .value("b", false)
 *         .value("c", true)
 *     .end()
 * .done();
 * </pre>
 */
//@formatter:on
final class JsonWriter {
  private JsonWriter() {
  }

  //@formatter:off
  /**
   * Creates a new {@link JsonStringWriter}.
   * 
   * <pre>
   * String json = JsonEmitter.string()
   *     .object()
   *         .array("a")
   *             .value(1)
   *             .value(2)
   *         .end()
   *         .value("b", false)
   *         .value("c", true)
   *     .end()
   * .done();
   * </pre>
   */
  //@formatter:on
  public static JsonStringWriter string() {
    return new JsonStringWriter();
  }

  /**
   * Emits a single value (a JSON primitive such as a {@link Number}, {@link Boolean}, {@link String}, a {@link Map}
   * or {@link JsonObject}, or a {@link Collection} or {@link JsonArray}.
   * 
   * Emit a {@link String}, JSON-escaped:
   * 
   * <pre>
   * JsonEmitter.string(&quot;abc\n\&quot;&quot;) // &quot;\&quot;abc\\n\\&quot;\&quot;&quot;
   * </pre>
   * 
   * <pre>
   * JsonObject obj = new JsonObject();
   * obj.put("abc", 1);
   * JsonEmitter.string(obj) // "{\"abc\":1}"
   * </pre>
   */
  public static String string(Object value) {
    return new JsonStringWriter().value(value).write();
  }

  /**
   * Creates a {@link JsonAppendableWriter} that can output to an {@link Appendable} subclass, such as a
   * {@link StringBuilder}, a {@link Writer} a {@link PrintStream} or a {@link CharBuffer}.
   */
  public static JsonAppendableWriter on(Appendable appendable) {
    return new JsonAppendableWriter(appendable);
  }

  //@formatter:off
  /**
   * Creates a {@link JsonAppendableWriter} that can output to an {@link PrintStream} subclass.
   * 
   * <pre>
   * JsonWriter.on(System.out)
   *     .object()
   *       .value(&quot;a&quot;, 1)
   *       .value(&quot;b&quot;, 2)
   *     .end()
   *   .done();
   * </pre>
   */
  //@formatter:on
  public static JsonAppendableWriter on(PrintStream appendable) {
    // Cast is necessary to allow GWT to compile this
    return new JsonAppendableWriter((Appendable)appendable);
  }

  //@formatter:off
  /**
   * Creates a {@link JsonAppendableWriter} that can output to an {@link OutputStream} subclass. Uses the UTF-8
   * {@link Charset}. To specify a different charset, use the {@link JsonWriter#on(Appendable)} method with an
   * {@link OutputStreamWriter}.
   * 
   * <pre>
   * JsonWriter.on(System.out)
   *     .object()
   *       .value(&quot;a&quot;, 1)
   *       .value(&quot;b&quot;, 2)
   *     .end()
   *   .done();
   * </pre>
   */
  //@formatter:on
  // TODO(mmastrac): Implement in future playn-server
  //  public static JsonAppendableWriter on(OutputStream out) {
  //    return new JsonAppendableWriter(new OutputStreamWriter(out, Charset.forName("UTF-8")));
  //  }
  
  /**
   * Escape a string value.
   * @param value
   * @return
   */
  public static String escape(String value) {
    String s = string(value);
    return s.substring(1, s.length() - 1);
  }
}
