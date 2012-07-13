package br.com.lambda3.tdc.spec

import org.specs2._

class AcceptanceSpec extends Specification { def is =

  "Especificação da String 'Hebe Camargo'"          ^
                                                    p^
  "A String 'Hebe Camargo' deve"                    ^
    "conter 15 caracteres"                          ! e1^
    "começar com 'Hebe'"                            ! e2^
    "terminar com 'amargo'"                         ! e3^
                                                    end
  
  def e1 = "Hello world" must have size 11
  def e2 = "Hello world" must startWith("Hello")
  def e3 = "Hello world" must endWith("world")
}