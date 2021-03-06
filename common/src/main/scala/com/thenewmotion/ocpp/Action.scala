package com.thenewmotion.ocpp

import xml.Elem
import soapenvelope12.Body
import scalax.{StringOption, richAny}

/**
 * @author Yaroslav Klymko
 */
object Action extends Enumeration {
  val Authorize,
  StartTransaction,
  StopTransaction,
  BootNotification,
  DiagnosticsStatusNotification,
  FirmwareStatusNotification,
  Heartbeat,
  MeterValues,
  StatusNotification = Value

  def fromHeader(header: String): Option[Value] =
    StringOption(header).flatMap {
      value =>
        val name = value.substring(1)
        values.find(_.toString equalsIgnoreCase name)
    }

  def fromBody(body: Body): Option[Action.Value] = (for {
    data <- body.any
    elem <- data.value.asInstanceOfOpt[Elem]
    action <- fromElem(elem)
  } yield action).headOption

  def fromElem(body: Elem): Option[Action.Value] = values.find(_.requestLabel equalsIgnoreCase body.label)

  private def labels(suffix: String): Map[Value, String] = values.map {
    value =>
      val (head :: tail) = value.toString.toList
      val label = (head.toLower :: tail).mkString + suffix
      value -> label
  }.toMap

  private val requestLabels: Map[Value, String] = labels("Request")
  private val responseLabels: Map[Value, String] = labels("Response")

  implicit def richValue(x: Value): RichValue = new RichValue(x)

  class RichValue(self: Value) {
    def requestLabel: String = requestLabels(self)
    def responseLabel: String = responseLabels(self)
  }
}