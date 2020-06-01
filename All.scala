package ca.mcit.bg

import java.io.{BufferedWriter, FileWriter}


import scala.io.Source

case class TripTable(route_id: Int, service_id: String, trip_id: String, trip_headsign: String, direction_id: Int, shape_id: Int, wheelchair_accessible: Int, note_fr: String, note_en: String) {
  def convertList(c : TripTable ) : List[String]={
    var  out = List[String]()

    out = c.route_id.toString :: c.service_id.toString :: c.trip_id.toString :: c.trip_headsign.toString :: c.direction_id.toString :: c.shape_id.toString :: c.wheelchair_accessible.toString :: c.note_fr.toString :: c.note_en.toString :: out
    out
  }
}

//case class is used here to hold the schema


case class RouteTable(route_id: Int, agency_id: String, route_short_name: String, route_long_name: String, route_type: String, route_url: String, route_color: String, route_text_color: String){
  def convertList(c : RouteTable ) : List[String]={
    var  out = List[String]()

    out = c.route_id.toString :: c.agency_id.toString :: c.route_short_name.toString :: c.route_long_name.toString :: c.route_type.toString :: c.route_url.toString :: c.route_color.toString :: c.route_text_color.toString :: out
    out
  }
}

case class CalenderTable(service_id:String,monday:Int,tuesday:Int,wednesday:Int,thursday:Int,friday:Int,saturday:Int,sunday:Int,start_date:Int,end_date:Int){
  def convertList(c : CalenderTable ) : List[String]={
    var  out = List[String]()
    out = c.service_id.toString :: c.monday.toString :: c.tuesday.toString :: c.wednesday.toString :: c.thursday.toString :: c.friday.toString :: c.saturday.toString :: c.sunday.toString :: c.start_date.toString :: c.end_date.toString :: out
    out
  }
}

case class FinalEnrich(ServiceId: String, route: RouteTable, trip : TripTable, cal : CalenderTable)


case class LookupTableForRoute(routes: List[RouteTable]) {
  private val lookupTable: Map[Int, RouteTable] =
    routes.map(route => route.route_id -> route).toMap
  def lookup(routeId: Int): RouteTable = lookupTable.getOrElse(routeId, null)
}
case class LookupTableForCalender(calenders : List[CalenderTable]) {
  private val lookupTable: Map[String, CalenderTable] =
    calenders.map(calendar => calendar.service_id -> calendar).toMap
  def lookup(serviceId: String): CalenderTable = lookupTable.getOrElse(serviceId, null)
}






object All extends App {

  def ReadingOfData(filePath: String): List[String] = {
    Source.fromFile (filePath).getLines.toList
  }

  def apply_Routes(data : List[String]): List[RouteTable] = {
    val valid_data = data.tail
    var final_data = List[RouteTable]()
    for(d <- valid_data){
      var c = ""
      if (d.takeRight(1) == ",") {
        c = d.concat("-")   //null handling
      }else{
        c =d
      }
      var f =  c.split(",").toList
      final_data = RouteTable (f (0).toInt, f (1).toString, f (2).toString, f (3).toString, f (4).toString, f (5).toString, f (6).toString, f (7).toString ) :: final_data
    }
    final_data
  }

  def apply_trips(data: List[String]) : List[TripTable] = {
    val valid_data = data.tail
    var final_data = List[TripTable]()
    for(d <-valid_data){
      var c = ""
      if (d.takeRight(2) == ",,") {
        c = d.replace(",,",",-,-")  //null handling
      }else{
        c =d
      }
      var f =  c.split(",").toList
      final_data = TripTable (f (0).toInt, f (1).toString, f (2).toString, f (3).toString, f (4).toInt, f (5).toInt, f (6).toInt, f (7).toString,f(8).toString ) :: final_data
    }
    final_data
  }

  def apply_calendars(data: List[String]) : List[CalenderTable] = {
    val valid_data = data.tail
    var final_data = List[CalenderTable]()
    for(d <-valid_data){
      var f =  d.split(",").toList
      final_data = CalenderTable (f (0).toString, f (1).toInt, f (2).toInt, f (3).toInt, f (4).toInt, f (5).toInt, f (6).toInt, f (7).toInt,f(8).toInt,f(9).toInt ) :: final_data
    }
    final_data
  }



  val Routes = ReadingOfData("C:\\Users\\user\\Downloads\\gtfs_stm\\routes.txt")
  val Trips =  ReadingOfData("C:\\Users\\user\\Downloads\\gtfs_stm\\trips.txt")
  val Calendar =  ReadingOfData("C:\\Users\\user\\Downloads\\gtfs_stm\\calendar.txt")

  var Route = apply_Routes(Routes)
  var routes = new LookupTableForRoute(Route)
  var trips = apply_trips(Trips)
  var calendar = apply_calendars(Calendar)
  var calendars = new LookupTableForCalender(calendar)
  //var Lookable_calendar = apply_Routes(Calendar)

  var finalEnrich1 = trips.map(trip => {
    val r : RouteTable = routes.lookup(trip.route_id)
    val s: String = trip.service_id
    val c: CalenderTable = calendars.lookup(trip.service_id)
    FinalEnrich(s,r,trip,c)
  })


  val write = new BufferedWriter(new FileWriter("C:\\Users\\user\\Downloads\\gtfs_stm\\output\\output$.csv"))
  val header: String = "service_id,monday,tuesday,wednesday,thursday,friday,saturday,sunday,start_date,end_date,route_id,service_id,trip_id,trip_headsign,direction_id,shape_id,wheelchair_accessible, note_fr,note_en,route_id,agency_id,route_short_name,route_long_name,route_type,route_url,route_color,route_text_color"
  write.write(header)

  for (line <- finalEnrich1) {
    write.newLine()

    var calout = line.cal.convertList(line.cal)
    for (c <- calout) {
      write.write(c)
      write.write(",")
    }
    var tripout = line.trip.convertList(line.trip)
    for (c <- tripout) {
      write.write(c)
      write.write(",")
    }
    var routeout = line.route.convertList(line.route)
    for (c <- routeout) {
      write.write(c)
      write.write(",")
    }
  }
  write.close()

}
