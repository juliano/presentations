package br.com.lambda3.spa

import org.scalatra._
import scalate.ScalateSupport

import org.json4s.{ DefaultFormats, Formats }
import org.scalatra.json._

class SpaServlet extends SpaStack with JacksonJsonSupport {
  
  before() {
    contentType = formats("json")
  }

  get("/") {
    contentType = "text/html"
    ssp("/index")
  }

  get("/estados") {
    Estado.all
  }
  
  get("/estados/:estado_id/cidades") {
    val estadoId = params("estado_id").toLong
    Cidade.allByEstado(estadoId)
  }
  
  get("/enderecos") {
    Endereco.all
  }
  
  get("/enderecos/:id") {
    val id = params("id").toLong
    Endereco.byId(id)
  }
  
  post("/enderecos") {
    val endereco = parsedBody.extract[Endereco]
    List(Endereco.save(endereco))
  }
  
  put("/enderecos/:id") {
    val endereco = parsedBody.extract[Endereco]
    List(endereco)
  }
  
  protected implicit val jsonFormats: Formats = DefaultFormats
}
