package com.thenewmotion.ocpp

import org.specs2.mutable.SpecificationWithJUnit
import ChargeBoxAddress.unapply

/**
 * @author Yaroslav Klymko
 */
class ChargeBoxAddressSpec extends SpecificationWithJUnit with SoapUtils {
  "ChargeBoxAddress" should {
    "parse address" in {
      unapply(envelopeFrom("v15/heartbeatRequest.xml")) must beSome("http://address")
    }
    "ignore 'http://www.w3.org/2005/08/addressing/anonymous'" in {
      unapply(envelopeFrom("v12/heartbeatRequest.xml")) must beNone
    }
  }
}
