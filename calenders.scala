package ca.mcit.bd


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

