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
package playn.sample.payments.core;

import static playn.core.PlayN.*;

import java.util.Date;

import playn.core.Game;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Mouse;
import playn.core.Mouse.ButtonEvent;
import playn.core.PlayN;
import playn.payments.core.InAppPayments;
import playn.payments.core.InAppPayments.PurchaseRequest;
import playn.payments.core.InAppPayments.PurchaseResponse;
import playn.payments.core.InAppPaymentsFactory;

public class PaymentsDemo implements Game {

  private static String purchaseJWT = null;
  private InAppPayments inappPayments = InAppPaymentsFactory.payments();
  
  @Override
  public void init() {
    // create and add background image layer
    Image bgImage = assetManager().getImage("images/bg.png");
    ImageLayer bgLayer = graphics().createImageLayer(bgImage);
    graphics().rootLayer().add(bgLayer);
    
    Date cal = new Date();
    String iat = String.valueOf(cal.getTime());
    long oneday = 3600000L;
    String exp = String.valueOf(cal.getTime() + oneday);
    PurchaseRequest request = new PurchaseRequest.Impl("Gold Star",
        "A shining badge of distinction", "3.00", "USD", "some opaque data");

    inappPayments.encodeJWT(iat, exp, request, new InAppPayments.EncodeJWTCallback() {
      @Override
      public void successHandler(String JWT) {
        purchaseJWT = JWT;
        PlayN.log().info("Product is ready, click the screen!!!");
      }

      @Override
      public void failureHandler(String error) {
        PlayN.log().error(error);
      }
    });
    
    // add a listener for pointer (mouse, touch) input
    mouse().setListener(new Mouse.Adapter() {
      @Override
      public void onMouseDown(ButtonEvent event) {

        inappPayments.setCallback(new InAppPayments.Adapter() {
          @Override
          public void successHandler(PurchaseResponse result) {
            PlayN.log().info("Big Congratulation: you get your products!!!");
            PlayN.log().info("jwt:" + result.jwt());
            PlayN.log().info("orderId:" + result.orderId());
            PlayN.log().info("request.name:" + result.request().name());
            PlayN.log().info("request.description:" + result.request().description());
            PlayN.log().info("request.currencycode:" + result.request().currencyCode());
            PlayN.log().info("request.price:" + result.request().price());
            PlayN.log().info("request.sellerData:" + result.request().sellerData());
          }

          @Override
          public void failureHandler(PurchaseResponse result) {
            PlayN.log().warn("It is bad: you could not get your products!!!");
            PlayN.log().warn("jwt:" + result.jwt());
            PlayN.log().warn("request.name:" + result.request().name());
            PlayN.log().warn("request.description:" + result.request().description());
            PlayN.log().warn("request.currencycode:" + result.request().currencyCode());
            PlayN.log().warn("request.price:" + result.request().price());
            PlayN.log().warn("request.sellerData:" + result.request().sellerData());
          }
        });
        inappPayments.buy(purchaseJWT);
      }
    });

  }

  
  @Override
  public void paint(float alpha) {
    // the background layer automatically paints itself, so no custom painting is needed by default
  }

  @Override
  public void update(float delta) {
    
  }

  @Override
  public int updateRate() {
    return 25;
  }
}
