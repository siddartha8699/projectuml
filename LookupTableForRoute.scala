package ca.mcit.bg

case class LookupTableForRoute(routes: List[RouteTable]) {
  private val lookupTable: Map[Int, RouteTable] =
    routes.map(route => route.route_id -> route).toMap
  def lookup(routeId: Int): RouteTable = lookupTable.getOrElse(routeId, null)
}
