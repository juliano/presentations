class CreateEnderecos < ActiveRecord::Migration
  def up
    create_table :enderecos do |t|
      t.string :logradouro
      t.string :cep
      t.references :cidade      
    end
  end
  def down
    drop_table :enderecos
  end
end