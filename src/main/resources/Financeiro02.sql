create database if not exists financeiro;

use financeiro;

create table empresa (
    cnpj varchar(18) primary key,
    nome_fantasia varchar(100) not null,
    razao_social varchar(150) not null,
    endereco varchar(200),
    telefone varchar(20)
);

create table imposto (
    id_imposto int auto_increment primary key,
    sigla varchar(20) not null,
    tipo varchar(50) not null,
    nome varchar(100) not null,
    aliquota decimal(5,2) not null,
    status varchar(20) not null,
    competencia date,
    base_de_calculo decimal(10,2)-- vêm de contabilidade
);


create table banco (
    id_banco int auto_increment primary key,
    codigo_banco varchar(10) not null,
    nome varchar(100) not null,
    nome_fantasia varchar(100),
    status varchar(20) not null,
    descricao varchar(200),
    data_banco datetime
);

create table fornecedor (
	id_fornecedor int auto_increment primary key,
    cnpj_cpf varchar(18) primary key,
    nome_razao_social varchar(150) not null,
    nome_fantasia varchar(100),
    telefone varchar(20),
    email varchar(100),
    endereco varchar(200),
    status varchar(20) not null
    
);

create table cliente (
    id_usuario int auto_increment primary key,
    nome_razao_social varchar(150) not null,
    nome_fantasia varchar(100),
    cpf_cnpj varchar(18) not null unique,
    email varchar(100),
    telefone varchar(20),
    endereco varchar(200),
    status varchar(20) not null -- colocar se esta ativos, BOOLEAN
);

create table plano_contas (
    id_plano_contas int auto_increment primary key,
    valor decimal(10,2),
    codigo varchar(20) not null,
    descricao varchar(100) not null,
    categoria varchar(50),
    tipo varchar(30),
    status varchar(20) not null
    
);

create table conta_bancaria (
    id_conta_bancaria int auto_increment primary key,
    id_banco int not null,
    id_empresa varchar(18) not null,
    agencia varchar(20) not null,
    numero_conta varchar(30) not null,
    tipo_conta varchar(30),
    saldo decimal(15,2) default 0.00,
    data_abertura date,
    status varchar(20) not null,
    constraint fk_conta_bancaria_banco
        foreign key (id_banco)
        references banco(id_banco),
    constraint fk_conta_bancaria_empresa
        foreign key (id_empresa)
        references empresa(cnpj)
);

create table conta_pagar (
    id_conta_pagar int auto_increment primary key,
    id_cliente varchar(18) not null,
    id_banco int,
    id_fornecedor varchar(18) not null,
    descricao varchar(200),
    vencimento_do_contrato date,
    num_documento varchar(50),
    data_emissao date,
    data_vencimento date,
    valor decimal(15,2) not null,
    status varchar(20) not null,
    constraint fk_conta_pagar_empresa
        foreign key (id_empresa) -- mudar o relacionamento com o cliente
        references empresa(cnpj),
    constraint fk_conta_pagar_banco
        foreign key (id_banco)
        references banco(id_banco),
    constraint fk_conta_pagar_fornecedor
        foreign key (id_fornecedor)
        references fornecedor(cnpj_cpf)
        
);

create table conta_receber (
    id_conta_receber int auto_increment primary key,
    id_cliente int not null,
    id_empresa varchar(18) not null,
    descricao varchar(200),
    data_emissao date,
    data_vencimento date,
    valor decimal(15,2) not null,
    valor_pago decimal(15,2) default 0.00,
    status varchar(20) not null,
    constraint fk_conta_receber_cliente
        foreign key (id_cliente)
        references cliente(id_cliente),
    constraint fk_conta_receber_empresa
        foreign key (id_empresa)
        references empresa(cnpj)
);

create table movimentacao_financeira (
    id_movimentacao_financeira int auto_increment primary key,
    id_conta_bancaria int not null,
    data_movimentacao date not null,
    tipo varchar(30) not null,
    descricao varchar(200),
    valor decimal(15,2) not null,
    saldo_anterior decimal(15,2),
    saldo_posterior decimal(15,2),
    status varchar(20) not null,
    constraint fk_movimentacao_conta_bancaria
        foreign key (id_conta_bancaria)
        references conta_bancaria(id_conta_bancaria)
);

create table pagamento_imposto (
    id_pagamento_imposto int auto_increment primary key,
    id_imposto int not null,
    id_plano_contas int not null,
    data_venc date,
    data_pagamento date,
    valor decimal(15,2) not null,
    cod_pagamento varchar(50),
    status varchar(20) not null,
    constraint fk_pagamento_imposto_imposto
        foreign key (id_imposto)
        references imposto(id_imposto),
    constraint fk_pagamento_imposto_plano
        foreign key (id_plano_contas)
        references plano_contas(id_plano_contas)
);
/*
create table dash_board_financeiro (
	-- saldo decimal, receitas decimal, despesas, fluxo_de_caixa, dados do grafico, 
    id_lancamento_contabil int auto_increment primary key,
    id_plano_contas int not null,
    id_movimentacao_financeira int,
    data_lancamento date not null,
    descricao varchar(200),
    valor decimal(15,2) not null,
    tipo varchar(20) not null,
    status varchar(20) not null,
    constraint fk_lancamento_plano
        foreign key (id_plano_contas)
        references plano_contas(id_plano_contas),
    constraint fk_lancamento_movimentacao
        foreign key (id_movimentacao_financeira)
        references movimentacao_financeira(id_movimentacao_financeira)
);
*/
-- criart tabela de nota_fiscal e de comprovante de recebimento.


create table pagamento (
    id_pagamento int auto_increment primary key,
    id_conta_pagar int not null,
    id_conta_bancaria int not null,
    data_pagamento date not null,
    valor decimal(15,2) not null,
    -- forma_pagamento varchar(50), receber da tabela de forma de pagamento o q vai ser pago.
    status varchar(20) not null,
    observacao varchar(255),
    constraint fk_pagamento_conta_pagar
        foreign key (id_conta_pagar)
        references conta_pagar(id_conta_pagar),
    constraint fk_pagamento_conta_bancaria
        foreign key (id_conta_bancaria)
        references conta_bancaria(id_conta_bancaria)
);

create table recebimento (
    id_recebimento int auto_increment primary key,
    id_conta_receber int not null,
    id_conta_bancaria int not null,
    data_recebimento date not null,
    valor decimal(15,2) not null,
    -- vem a forma de pagamento, forma_recebimento varchar(50),
    status varchar(20) not null,
    observacao varchar(255),
    constraint fk_recebimento_conta_receber
        foreign key (id_conta_receber)
        references conta_receber(id_conta_receber),
    constraint fk_recebimento_conta_bancaria
        foreign key (id_conta_bancaria)
        references conta_bancaria(id_conta_bancaria)
);
/* no momento não útil
create table orcamento (
    id_orcamento int auto_increment primary key,
    id_empresa varchar(18) not null,
    nome varchar(100) not null,
    descricao varchar(200),
    data_inicio date,
    data_fim date,
    valor_previsto decimal(15,2) not null,
    status varchar(20) not null,
    constraint fk_orcamento_empresa
        foreign key (id_empresa)
        references empresa(cnpj)
);
*/

 
