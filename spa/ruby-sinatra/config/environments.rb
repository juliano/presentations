require 'sinatra/activerecord'

ActiveRecord::Base.establish_connection(YAML::load(File.open('config/database.yml'))['development'])
ActiveRecord::Base.include_root_in_json = false
