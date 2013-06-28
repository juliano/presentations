import json

class Estado:
  def __init__(self, db):
    self.db = db

  def all(self):
    self.db.execute('select * from estados')
    self.estados = self.db.fetchall()
    return self

  def to_json(self):
    return to_json(('id', 'nome'), self.estados)

class Cidade:
  def __init__(self, db):
    self.db = db

  def find_all_by_estado(self, estado_id):
    sql = "select estado_id, id, nome from cidades where estado_id=%s" % estado_id
    self.db.execute(sql)
    self.cidades = self.db.fetchall()
    return self

  def to_json(self):
    return to_json(('estado_id', 'id', 'nome'), self.cidades)

class Endereco:
  def __init__(self, db):
    self.db = db
    self.params = []
    self.id = None

  def all(self):
    sql = """select * from enderecos e
             inner join cidades c on e.cidade_id = c.id 
             inner join estados u on c.estado_id = u.id order by e.id"""
    self.db.execute(sql)
    self.enderecos = self.db.fetchall()
    return self

  def find(self, id):
    sql = """select * from enderecos e
             inner join cidades c on e.cidade_id = c.id 
             inner join estados u on c.estado_id = u.id 
             where e.id = %s order by e.id""" % id
    self.db.execute(sql)
    self.enderecos = self.db.fetchall()
    return self

  def build(self, params, id=None):
    logradouro, cep, cidade_id = params['logradouro'], params['cep'], params['cidade_id']
    if id:
      self.id = id
    self.params = (logradouro, cep, cidade_id)
    return self

  def save(self):
    sql = "insert into enderecos values(nextval('enderecos_id_seq'), '%s', '%s', %s)" % self.params
    self.db.execute(sql)

  def update(self):
    attrs = self.params + (self.id,)
    sql = "update enderecos set logradouro='%s', cep='%s', cidade_id=%s WHERE id=%s" % attrs
    self.db.execute(sql)

  def to_json(self):
    if self.id:
      attrs = self.params + (self.id,)
      return to_json(('logradouro', 'cep', 'cidade_id', 'id'), [attrs])
    if self.params:
      return to_json(('logradouro', 'cep', 'cidade_id'), [self.params])
    return self.__to_json(self.enderecos)

  def __to_json(self, rows):
    enderecos = [{'id':r[0],'logradouro':r[1],'cep':r[2],'cidade_id':r[3],
                  'cidade':{'id':r[4],'nome':r[5], 'estado_id':r[6],
                  'estado':{'id':r[7],'nome':r[8]}}} for r in rows]
    return json.dumps(enderecos)

def to_json(names, rows):
  def line(names, row):
    return [(name,row[i]) for i,name in enumerate(names, 0)]

  content = [dict(line(names, row)) for row in rows]
  return json.dumps(content)

