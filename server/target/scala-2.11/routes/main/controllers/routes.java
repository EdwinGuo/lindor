
// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/EdwinGuo/datallite/lindor/server/conf/routes
// @DATE:Sat Aug 06 18:24:32 GMT 2016

package controllers;

import router.RoutesPrefix;

public class routes {
  
  public static final controllers.ReverseAssets Assets = new controllers.ReverseAssets(RoutesPrefix.byNamePrefix());
  public static final controllers.ReverseWeatherController WeatherController = new controllers.ReverseWeatherController(RoutesPrefix.byNamePrefix());

  public static class javascript {
    
    public static final controllers.javascript.ReverseAssets Assets = new controllers.javascript.ReverseAssets(RoutesPrefix.byNamePrefix());
    public static final controllers.javascript.ReverseWeatherController WeatherController = new controllers.javascript.ReverseWeatherController(RoutesPrefix.byNamePrefix());
  }

}
