package br.com.lambda3.tdc

class Fibonacci {

  val seq: Stream[BigInt] = 0 #:: 1 #:: (seq zip seq.tail).map { case (a, b) => a + b }

  def elemento(posicao: Int): BigInt = seq(posicao - 1)
}