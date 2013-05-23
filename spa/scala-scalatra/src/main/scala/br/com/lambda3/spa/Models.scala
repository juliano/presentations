package br.com.lambda3.spa

import org.squeryl.annotations.Column
import org.squeryl.{ Schema, KeyedEntity }
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.dsl.StatefulManyToOne
import org.json4s.JString

case class Estado(val id: Long, val nome: String) {
  def this() = this(0, "")
}

object Estado {
  import SpaDb._
  def all = from(estados)(select(_)).toList
}

case class Cidade(val id: Long, val nome: String, val estado_id: Long) extends KeyedEntity[Long] {
  def this() = this(0, "", 0)
}

object Cidade {
  import SpaDb._

  def allByEstado(estadoId: Long) =
    from(cidades)(c => where(c.estado_id === estadoId) select (c)).toList
}

case class Endereco(val id: Long, val logradouro: String, val cep: String, val cidade_id: Long)
  extends KeyedEntity[Long] {
  def this() = this(0, "", "", 0)
  def this(logradouro: String, cep: String, cidade_id: String) = 
    this(0, logradouro, cep, cidade_id.toLong)
  
  lazy val cidade = SpaDb.cidade2Enderecos.rightStateful(this)
}

object Endereco {
  import SpaDb._

  def all = from(enderecos)(select(_)).toList
  
  def byId(id: Long) = from(enderecos)(e => where(e.id === id) select (e)).toList
  
  def save(endereco: Endereco) = enderecos.insert(endereco)
}

object SpaDb extends Schema {
  val estados = table[Estado]("estados")
  val cidades = table[Cidade]("cidades")
  val enderecos = table[Endereco]("enderecos")

  val cidade2Enderecos = oneToManyRelation(cidades, enderecos).via(_.id === _.cidade_id)
  
  on(enderecos)(e => declare(
      e.id is(autoIncremented("enderecos_id_seq"))
  ))
}
