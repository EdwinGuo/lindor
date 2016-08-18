
// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/EdwinGuo/datallite/lindor/server/conf/routes
// @DATE:Thu Aug 18 01:54:38 GMT 2016

import play.api.routing.JavaScriptReverseRoute
import play.api.mvc.{ QueryStringBindable, PathBindable, Call, JavascriptLiteral }
import play.core.routing.{ HandlerDef, ReverseRouteContext, queryString, dynamicString }


import _root_.controllers.Assets.Asset

// @LINE:6
package controllers.javascript {
  import ReverseRouteContext.empty

  // @LINE:21
  class ReverseAssets(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:21
    def versioned: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.Assets.versioned",
      """
        function(file1) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "assets/" + (""" + implicitly[PathBindable[Asset]].javascriptUnbind + """)("file", file1)})
        }
      """
    )
  
  }

  // @LINE:6
  class ReverseWeatherController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:9
    def ajaxCall: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.WeatherController.ajaxCall",
      """
        function(city0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "weather/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("city", encodeURIComponent(city0))})
        }
      """
    )
  
    // @LINE:15
    def busCall: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.WeatherController.busCall",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "bus"})
        }
      """
    )
  
    // @LINE:18
    def busStatus: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.WeatherController.busStatus",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "chart"})
        }
      """
    )
  
    // @LINE:12
    def twitterCall: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.WeatherController.twitterCall",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "twitter"})
        }
      """
    )
  
    // @LINE:6
    def index: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.WeatherController.index",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + """"})
        }
      """
    )
  
  }


}
