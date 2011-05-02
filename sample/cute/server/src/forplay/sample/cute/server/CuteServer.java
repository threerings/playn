package forplay.sample.cute.server;

import forplay.server.GameServer;

public class CuteServer extends GameServer {

  public static void main(String[] args) throws Exception {
    new CuteServer(8080).run();
  }

  public CuteServer(int port) {
    super(port);
    addServlet("/rpc", new CuteServlet());
  }
}
