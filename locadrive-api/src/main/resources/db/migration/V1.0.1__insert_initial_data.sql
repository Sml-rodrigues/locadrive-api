INSERT INTO tb_cliente (id, nome, cpf, email, cnh, telefone)
VALUES (nextval('tb_cliente_seq'), 'João Silva', '99988877700', 'joao@email.com', '12345678901', '11999998888');

INSERT INTO tb_veiculo (id, modelo, marca, placa, ano_fabricacao, valor_diaria, status)
VALUES (nextval('tb_veiculo_seq'), 'Corolla', 'Toyota', 'ABC1D23', 2023, 200.00, 'DISPONIVEL');