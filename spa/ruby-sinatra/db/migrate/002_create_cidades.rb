class CreateCidades < ActiveRecord::Migration
  def up
    create_table :cidades do |t|
      t.string :nome
      t.references :estado
    end
  end
  def down
    drop_table :cidades
  end
end