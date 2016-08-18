
// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/EdwinGuo/datallite/lindor/server/conf/routes
// @DATE:Thu Aug 18 01:54:38 GMT 2016


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
