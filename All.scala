package ca.mcit.bg

import java.io.{BufferedWriter, FileWriter}


import scala.io.Source


object All extends App {

  


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
