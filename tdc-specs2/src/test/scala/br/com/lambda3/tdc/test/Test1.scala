package br.com.lambda3.tdc.test

import org.scalatest.FunSuite
import br.com.lambda3.tdc.Fibonacci

class SimpleTest extends FunSuite {

  test("A String 'Hebe Camargo' deve conter 12 caracteres") {
    assert("Hebe Camargo".length === 12)
  }

  test("A String 'Hebe Camargo' deve começar com 'Hebe'") {
    assert("Hebe Camargo".startsWith("Hebe"))
  }

  test("A String 'Hebe Camargo' deve terminar com 'amargo'") {
    assert("Hebe Camargo".endsWith("amargo"))
  }
}