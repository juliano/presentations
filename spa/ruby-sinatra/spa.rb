require 'sinatra/base'
require './config/environments'
require './models/model'

class Spa < Sinatra::Base
  get '/' do
    erb :index
  end

  get '/estados' do
    content_type :json
    Estado.all.to_json
  end

  get '/estados/:estado_id/cidades' do |estado_id|
    content_type :json
    Cidade.find_all_by_estado_id(estado_id).to_json
  end

  get '/enderecos' do
    content_type :json
    Endereco.includes(cidade: :estado).all.to_json
  end

  get '/enderecos/:id' do |id|    
    content_type :json
    Endereco.find(id).to_json
  end  

  post '/enderecos' do
    salvar_endereco Endereco.new
  end

  put '/enderecos/:id' do |id|
    endereco = Endereco.find id
    salvar_endereco endereco
  end

  private

  def salvar_endereco endereco
    content_type :json    
    data = JSON.parse(request.body.read)
    endereco.assign_attributes data

    if endereco.save
      endereco.to_json
    else
      status 400
      body endereco.errors.map { | k, v | "#{k} #{v}" }.join('. ')
    end
  end
end

