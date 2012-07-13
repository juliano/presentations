package br.com.lambda3.tdc.spec;

import org.specs2.mutable._

class HebeSpec extends Specification {

  "A String 'Hebe Camargo'" should {
    "conter 12 caracteres" in {
      "Hebe Camargo" must have size 12
    }
    "começar com 'Hebe'" in {
      "Hebe Camargo" must startWith("Hebe")
    }
    "terminar com 'amargo'" in {
      "Hebe Camargo" must endWith("amargo")
    }
  }
}
