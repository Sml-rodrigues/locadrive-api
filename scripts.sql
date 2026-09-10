-- ========================================================
-- LOCADRIVE 2.0 - ESQUEMA DO BANCO DE DADOS (POSTGRESQL)
-- ========================================================

-- 1. TABELA DE CLIENTES
CREATE TABLE IF NOT EXISTS clientes (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    telefone VARCHAR(20),
    data_cadastro TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 2. TABELA DE VEÍCULOS
CREATE TABLE IF NOT EXISTS veiculos (
    id BIGSERIAL PRIMARY KEY,
    placa VARCHAR(10) NOT NULL UNIQUE,
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    ano_fabricacao INT NOT NULL,
    valor_diaria NUMERIC(10, 2) NOT NULL CHECK (valor_diaria > 0),
    status VARCHAR(20) NOT NULL DEFAULT 'DISPONIVEL'
);

-- 3. TABELA DE LOCAÇÕES
CREATE TABLE IF NOT EXISTS locacoes (
    id BIGSERIAL PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    veiculo_id BIGINT NOT NULL,
    data_inicio TIMESTAMP WITH TIME ZONE NOT NULL,
    data_fim_prevista TIMESTAMP WITH TIME ZONE NOT NULL,
    data_devolucao TIMESTAMP WITH TIME ZONE,
    valor_diaria_aplicado NUMERIC(10, 2) NOT NULL,
    valor_total NUMERIC(10, 2),
    valor_multa NUMERIC(10, 2) DEFAULT 0.00,
    status VARCHAR(20) NOT NULL DEFAULT 'EM_ANDAMENTO',

    -- Definição de Chaves Estrangeiras (Integridade Referencial)
    CONSTRAINT fk_locacao_cliente FOREIGN KEY (cliente_id) 
        REFERENCES clientes(id) ON DELETE RESTRICT,
        
    CONSTRAINT fk_locacao_veiculo FOREIGN KEY (veiculo_id) 
        REFERENCES veiculos(id) ON DELETE RESTRICT,

    -- Validação de Regra de Negócio Básica
    CONSTRAINT chk_datas_validas CHECK (data_fim_prevista >= data_inicio)
);

-- 4. ÍNDICES PARA ALAVANCAR A PERFORMANCE DE BUSCAS
CREATE INDEX idx_clientes_cpf ON clientes(cpf);
CREATE INDEX idx_veiculos_placa ON veiculos(placa);
CREATE INDEX idx_locacoes_status ON locacoes(status);
