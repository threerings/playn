/**
 * Copyright 2010 The ForPlay Authors
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
package forplay.sample.cute.server;

import static forplay.core.ForPlay.*;

import forplay.core.Json;
import forplay.java.JavaPlatform;
import forplay.sample.cute.core.CuteWorld;

import java.io.IOException;
import java.io.Reader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CuteServlet extends HttpServlet {

  private static final int BUF_SIZE = 4096;

  static {
    JavaPlatform.register();
  }

  private CuteWorld world = blankWorld(16, 16);

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse rsp)
      throws ServletException, IOException {
    String query = req.getQueryString();
    if ("map".equals(query)) {
      Json.Writer w = json().newWriter();
      world.write(w);
      rsp.getWriter().write(w.write());
      rsp.setStatus(HttpServletResponse.SC_OK);
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String payload = readFully(req.getReader());
    Json.Object data = json().parse(payload);

    String op = data.getString("op");
    if (op.equals("addTop")) {
      int tx = data.getInt("x");
      int ty = data.getInt("y");
      int type = data.getInt("type");

      System.out.println("addTop " + tx + ", " + ty + " : " + type);
      world.addTile(tx, ty, type);
    } else if (op.equals("removeTop")) {
      int tx = data.getInt("x");
      int ty = data.getInt("y");

      System.out.println("removeTop " + tx + ", " + ty);
      world.removeTopTile(tx, ty);
    }
  }

  private CuteWorld blankWorld(int width, int height) {
    CuteWorld world = new CuteWorld(width, height);
    for (int ty = 0; ty < height; ++ty) {
      for (int tx = 0; tx < width; ++tx) {
        world.addTile(tx, ty, 2);
      }
    }
    return world;
  }

  private String readFully(Reader reader) throws IOException {
    StringBuffer result = new StringBuffer();
    char[] buf = new char[BUF_SIZE];
    while (-1 != reader.read(buf)) {
      result.append(buf);
    }
    return result.toString();
  }
}
