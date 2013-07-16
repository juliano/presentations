class Estado < ActiveRecord::Base
end

class Cidade < ActiveRecord::Base
  belongs_to :estado
end

class Endereco < ActiveRecord::Base
  belongs_to :cidade
  validates :cidade, presence: true
  validates :cep, presence: true    
  validates :logradouro, presence: true

  attr_accessible :cidade_id, :cep, :logradouro

  def as_json(options)
    options = {
      include: {
        cidade: {
          include: :estado
        }
      }
    }
    super options
  end
end
