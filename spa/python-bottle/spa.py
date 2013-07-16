import bottle
import bottle_pgsql
from bottle import get, post, put, static_file, install, request, response

import model
from utils import jsonify

plugin = bottle_pgsql.Plugin('dbname=spa host=localhost user=spa password=spa')
install(plugin)

@get('/static/<filepath:path>')
def server_static(filepath):
  return static_file(filepath, root='static')

@get('/')
def home():
  return open('views/index.html').read()

@get('/estados')
@jsonify
def estados(db):
  Estado = model.Estado(db)

  return Estado.all().to_json()

@get('/estados/:estado_id/cidades')
@jsonify
def estado_cidades(estado_id, db):
  Cidade = model.Cidade(db)

  return Cidade.find_all_by_estado(estado_id).to_json()

@get('/enderecos')
@jsonify
def enderecos(db):
  Endereco = model.Endereco(db)

  return Endereco.all().to_json()

@get('/enderecos/:id')
@jsonify
def endereco(id, db):
  Endereco = model.Endereco(db)

  return Endereco.find(id).to_json()

@post('/enderecos')
@jsonify
def salvar_endereco(db):
  Endereco = model.Endereco(db)

  endereco = Endereco.build(request.json)
  endereco.save()
  return endereco.to_json()

@put('/enderecos/:id')
@jsonify
def alterar_endereco(id, db):
  Endereco = model.Endereco(db)

  endereco = Endereco.build(request.json, id)
  endereco.update()
  return endereco.to_json()

bottle.run(host='localhost', port=8080)

