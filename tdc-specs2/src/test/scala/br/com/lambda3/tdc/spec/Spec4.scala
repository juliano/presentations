package br.com.lambda3.tdc.spec

import br.com.lambda3.tdc.Fibonacci

import org.specs2.mutable.Specification
import org.specs2.specification.Scope

class FibonacciNinjaSpec extends Specification {

  "A sequência de fibonacci" should {
    "ter 0 como 1.º elemento " in new fib {
      seq.elemento(1) must_== 0
    }
    "ter 1 como 3.º elemento " in new fib {
      seq.elemento(3) must_== 1
    }
    "ter 2 como 4.º elemento " in new fib {
      seq.elemento(4) must_== 2
    }
    "ter 3 como 5.º elemento " in new fib {
      seq.elemento(5) must_== 3
    }
    "ter 5 como 6.º elemento " in new fib {
      seq.elemento(6) must_== 5
    }
    "ter 165580141 como 42.º elemento " in new fib {
      seq.elemento(42) must_== 165580141
    }
  }

  trait fib extends Scope {
    val seq = new Fibonacci
  }
}

