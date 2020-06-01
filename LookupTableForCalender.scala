package ca.mcit.bg

case class LookupTableForCalender(calenders : List[CalenderTable]) {
  private val lookupTable: Map[String, CalenderTable] =
    calenders.map(calendar => calendar.service_id -> calendar).toMap
  def lookup(serviceId: String): CalenderTable = lookupTable.getOrElse(serviceId, null)
}
