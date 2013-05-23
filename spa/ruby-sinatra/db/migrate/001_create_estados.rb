class CreateEstados < ActiveRecord::Migration
  def up
    create_table :estados do |t|
      t.string :nome
    end
  end
  def down
    drop_table :estados
  end
end