package br.com.lambda3.tdc.spec

import org.specs2.mutable.Specification
import br.com.lambda3.tdc.Fibonacci

class FibonacciSpec extends Specification {

  "A sequência de fibonacci" should {
    "ter como primeiro elemento 0"in {
      new Fibonacci().elemento(1) must_== 0
    }
    "ter 165580141 como 42.º elemento " in {
      new Fibonacci().elemento(42) must_== 165580141
    }
  }
}