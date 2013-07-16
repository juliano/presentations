
from bottle import hook, response

def jsonify():
  response.content_type = 'application/json'

