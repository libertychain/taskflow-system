-- Inserir usuários iniciais
INSERT INTO users (email, password, name, role, department, created_at, updated_at) VALUES
('admin@techalves.com', 'admin123', 'Administrador', 'ADMIN', 'TI', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('manager@techalves.com', 'manager123', 'Gerente de Projetos', 'MANAGER', 'Gestão', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('user@techalves.com', 'user123', 'Usuário Comum', 'USER', 'Desenvolvimento', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('analyst@techalves.com', 'analyst123', 'Analista de Sistemas', 'USER', 'Análise', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('designer@techalves.com', 'designer123', 'Designer UI/UX', 'USER', 'Design', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Inserir projetos iniciais
INSERT INTO projects (name, description, start_date, end_date, status, created_at, created_by) VALUES
('Sistema de Gestão Interna', 'Desenvolvimento de sistema para gestão de processos internos', '2025-01-15', '2025-06-30', 'ACTIVE', CURRENT_TIMESTAMP, 'admin@techalves.com'),
('Portal do Cliente', 'Criação de portal web para atendimento ao cliente', '2025-02-01', '2025-07-15', 'ACTIVE', CURRENT_TIMESTAMP, 'manager@techalves.com'),
('Aplicativo Mobile', 'Desenvolvimento de app mobile para clientes', '2025-03-01', '2025-08-30', 'ACTIVE', CURRENT_TIMESTAMP, 'manager@techalves.com');

-- Inserir tarefas iniciais
INSERT INTO tasks (title, description, status, priority, due_date, assigned_to_id, project_id, created_at, created_by) VALUES
('Configurar ambiente de desenvolvimento', 'Preparar ambiente local e ferramentas necessárias', 'COMPLETED', 'HIGH', '2025-01-20', 3, 1, CURRENT_TIMESTAMP, 'admin@techalves.com'),
('Criar protótipo da interface', 'Desenvolver wireframes e protótipos iniciais', 'IN_PROGRESS', 'MEDIUM', '2025-02-15', 5, 2, CURRENT_TIMESTAMP, 'manager@techalves.com'),
('Implementar autenticação', 'Desenvolver sistema de login e controle de acesso', 'IN_PROGRESS', 'HIGH', '2025-02-10', 3, 1, CURRENT_TIMESTAMP, 'admin@techalves.com'),
('Testes de integração', 'Realizar testes de integração entre módulos', 'PENDING', 'MEDIUM', '2025-03-01', 4, 1, CURRENT_TIMESTAMP, 'manager@techalves.com'),
('Documentação técnica', 'Criar documentação técnica do sistema', 'PENDING', 'LOW', '2025-03-15', 4, 1, CURRENT_TIMESTAMP, 'user@techalves.com'),
('Design do dashboard', 'Criar interface do dashboard principal', 'IN_PROGRESS', 'HIGH', '2025-02-20', 5, 2, CURRENT_TIMESTAMP, 'designer@techalves.com'),
('API REST', 'Desenvolver endpoints da API REST', 'COMPLETED', 'HIGH', '2025-01-30', 3, 1, CURRENT_TIMESTAMP, 'admin@techalves.com'),
('Banco de dados', 'Modelar e implementar banco de dados', 'COMPLETED', 'HIGH', '2025-01-25', 4, 1, CURRENT_TIMESTAMP, 'analyst@techalves.com');