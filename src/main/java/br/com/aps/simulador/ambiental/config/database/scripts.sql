-- Scripts para criar banco e tabelas para rodar projeto localmente

CREATE DATABASE simuladorambiental; -- cria o banco de dados

USE simuladorambiental; -- indica para o SGBD qual banco será utilizado

-- cria uma tabela dos tipos de transportes disponíveis
CREATE TABLE tipo_transporte (
    id_tipo_transporte INT PRIMARY KEY,
    descricao_tipo_transporte VARCHAR(100)
);

-- insere os dados na tabela tipo_transporte
INSERT INTO tipo_transporte (id_tipo_transporte, descricao_tipo_transporte) VALUES
(1, "Veículo de Grande Porte"),
(2, "Veículo de Médio Porte"),
(3, "Veículo de Pequeno Porte"),
(4, "Veículo Compartilhado de Grande Porte"),
(5, "Veículo Compartilhado de Médio Porte"),
(6, "Veículo Compartilhado de Pequeno Porte");

-- cria uma tabela das informações fornecidas do meio de transporte utilizado
CREATE TABLE informacoes_meio_transporte (
	cnh VARCHAR(11) PRIMARY KEY,
    id_tipo_transporte INT,
    tipo_combustivel VARCHAR(50),
    distancia_diaria_percorrida DECIMAL(10, 2),
    consumo_litro DECIMAL(10, 2),
    FOREIGN KEY (id_tipo_transporte) REFERENCES tipo_transporte(id_tipo_transporte)
);
