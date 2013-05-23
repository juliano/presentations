$(document).ajaxError(function (event, jqxhr, settings, exception) {
    var badRequestMessage = function (jqxhr) {
        var defaultMsg = "Solicitação inválida";
        var msg = jqxhr.responseText || defaultMsg;
        return msg;
    };

    var map = {
        400: badRequestMessage(jqxhr),
        500: "Servidor falhou o processamento da solicitação"
    };
    var message = _.result(map, jqxhr.status) || "Erro ao comunicar com o servidor";
    alert(message);

    if (400 !== jqxhr.status) {
        throw new Error("Erro 500 na integração:\n" + jqxhr.responseText);
    }
});

var App = {};

App.region = {
    el: $('#content'),
    show: function (view) {
        this.close();
        view.render();
        this.open(view);
        this.currentView = view;
    },
    close: function () {
        if (!this.currentView) { return; }
        this.currentView.remove();
        delete this.currentView;
    },
    open: function (view) {
        this.el.empty().append(view.el);
    }
};

App.listarEnderecos = function () {
    var view = new App.EnderecosView({
        collection: App.enderecos
    });

    App.region.show(view);

    App.enderecos.fetch();
};

App.formularioEndereco = function (model) {
    var view = new App.FormularioEnderecoView({
        model: model
    });

    App.region.show(view);
    view.setupComboEstados();    

    $.when(App.estados.fetch()).done(function() {
        if (!model.isNew()) {
            view.comboEstados.$el.val(view.comboEstados.$el.attr('value'));            
            $.when(view.comboEstados.carregarCidades()).done(function() {
                view.comboEstados.$cidades.val(view.comboEstados.$cidades.attr('value'));
            });
        }
    });   
};

App.novoEndereco = function () {
    App.formularioEndereco(new App.Endereco);
};

App.editarEndereco = function (model) {
    App.formularioEndereco(model);
};

App.Router = Backbone.Router.extend({
    routes: {
        '': 'home',
        'enderecos': 'listarEnderecos',
        'enderecos/novo': 'novoEndereco',
        'enderecos/:id': 'editarEndereco',        
    },
    '': function() {
    },
    listarEnderecos: function () {
        App.listarEnderecos();
    },
    novoEndereco: function () {
        App.novoEndereco();
    },
    editarEndereco: function (id) {
        var model = new App.Endereco({ id: id });
        $.when(model.fetch()).done(function() {
            App.editarEndereco(model);
        });
    }    
});

App.ComboView = Backbone.View.extend({
    template: Mustache.compile($('#opcaoComboTp').html()),
    initialize: function (options) {
        this.listenTo(this.collection, 'sync', this.render);
    },
    render: function () {
        var html = '<option value="">Selecione</option>';
        this.collection.each(function (model) {
            var option = this.template({
                nome: model.attributes.nome,
                valor: model.id
            });
            html += option;
        }, this);
        this.$el.html(html);
    }
});

App.ComboEstadosView = App.ComboView.extend({
    initialize: function(options) {
        this.$cidades = options.$cidades;
        App.ComboView.prototype.initialize.apply(this, arguments);
        
        var that = this;
        this.$el.change(function() {
            that.carregarCidades()
        });
    },
    carregarCidades: function () {
        if (!this.$el.val()) {
            this.$cidades.empty();
            return;
        }

        var cidades = new App.Cidades({
            codigoEstado: this.$el.val()
        });
        this.comboCidades = new App.ComboView({
            el: this.$cidades,
            collection: cidades
        });
        return cidades.fetch();
    },        
});

App.Estados = Backbone.Collection.extend({
    url: 'estados'
});

App.Cidades = Backbone.Collection.extend({
    initialize: function(options) {
        this.codigoEstado = options.codigoEstado;
    },
    url: function() {
        return 'estados/' + this.codigoEstado + '/cidades';
    }
});

App.Enderecos = Backbone.Collection.extend({
    url: 'enderecos'
});

App.Endereco = Backbone.Model.extend({
    urlRoot: 'enderecos'
});

App.FormularioEnderecoView = Backbone.View.extend({
    template: Mustache.compile($('#formularioEnderecoTp').html()),
    events: {
        'click [data-action=cancel]': 'cancel',
        'click [data-action=submit]': 'submit'
    },
    render: function () {
        var data = { data: this.model.toJSON() };
        this.$el.html(this.template(data));
        return this;
    },
    submit: function () {
        this.model.save({
            cidade_id: this.$('[name=cidade]').val(),
            cep: this.$('[name=cep]').val(),
            logradouro: this.$('[name=logradouro]').val()
        }, { success: this.listarEnderecos });
    },
    cancel: function () {
        this.listarEnderecos();
    },
    listarEnderecos: function () {
        App.listarEnderecos();
        App.router.navigate('enderecos');
    },
    setupComboEstados: function () {
        this.comboEstados = new App.ComboEstadosView({
            el: this.$('[name=estado]'),
            $cidades: this.$('[name=cidade]'),
            collection: App.estados
        });
    }
});

App.EnderecosView = Backbone.View.extend({
    template: Mustache.compile($('#enderecosTp').html()),
    initialize: function () {
        this.listenTo(this.collection, 'sync', this.renderData);
    },
    events: {
        'click [data-action=new]': 'new'
    },
    render: function () {
        this.$el.html(this.template());
        return this;
    },
    renderData: function (source) {
        if (!(source instanceof Backbone.Collection)) {
            return;
        }
        var tbody = this.$('tbody');
        this.collection.each(function (model) {
            var view = new App.EnderecoView({
                model: model,
            });
            view.$el.appendTo(tbody);
            view.render();
        }, this);
    },
    'new': function (e) {
        e.preventDefault();
        App.novoEndereco();
        App.router.navigate('enderecos/novo');
    }
});

App.EnderecoView = Backbone.View.extend({
    tagName: 'tr',
    template: Mustache.compile($('#enderecoTp').html()),
    events: {
        'click [data-action=delete]': 'delete',
        'click': 'select'
    },
    render: function () {
        var content = this.template({ data: this.model.toJSON()});
        this.$el.html(content);
        return this;
    },
    'delete': function (e) {
    },
    select: function () {
        App.editarEndereco(this.model);
        App.router.navigate('enderecos/' + this.model.id);        
    }
});

App.router = new App.Router;
App.enderecos = new App.Enderecos;
App.estados = new App.Estados;
Backbone.history.start();