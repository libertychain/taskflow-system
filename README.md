# Sistema de Gestão de Tarefas Corporativas - TechAlves Soluções

## 🚀 Visão Geral

Sistema completo de gestão de tarefas e projetos desenvolvido para a disciplina Desenvolvimento de Sistemas Corporativos. O sistema possui autenticação, gestão de tarefas, dashboard analytics e interface moderna e responsiva.

## ✨ Funcionalidades

### 🔐 Autenticação & Autorização
- Sistema de login seguro
- Diferentes níveis de permissão (Admin, Manager, User)
- Controle de acesso baseado em roles

### 📋 Gestão de Tarefas
- Criar, editar e excluir tarefas
- Atribuir tarefas a usuários
- Definir prioridades e prazos
- Filtrar tarefas por status, responsável e prioridade
- Mudança de status com drag & drop

### 📊 Dashboard Analytics
- Visualização de métricas em tempo real
- Gráficos de progresso de projetos
- Estatísticas de produtividade
- Relatórios de desempenho

### 🎯 Gestão de Projetos
- Criar e gerenciar projetos
- Atribuir tarefas a projetos
- Acompanhar progresso
- Gerenciar equipes

## 🛠️ Tecnologias Utilizadas

### Backend
- **Java 17** - Linguagem principal
- **Spring Boot 3.0** - Framework principal
- **Spring Security** - Autenticação e autorização
- **Spring Data JPA** - Persistência de dados
- **H2 Database** - Banco de dados em memória
- **Maven** - Gerenciamento de dependências

### Frontend
- **HTML5** - Estrutura semântica
- **CSS3 + Tailwind** - Estilização moderna
- **JavaScript ES6+** - Interatividade
- **Chart.js** - Visualização de dados
- **Axios** - Comunicação com API

## 📋 Pré-requisitos

- Java 17 ou superior
- Maven 3.6 ou superior
- Eclipse IDE 4.0 ou superior
- Tomcat 9 ou superior
- Git

## 🚦 Instalação e Execução

### 1. Clonar o Repositório
```bash
git clone https://github.com/seu-usuario/taskflow-system.git
cd taskflow-system
```

### 2. Configurar o Backend
```bash
# Entrar no diretório backend
cd backend

# Instalar dependências
mvn clean install

# Executar a aplicação
mvn spring-boot:run
```

### 3. Configurar o Frontend
```bash
# Entrar no diretório frontend
cd ../frontend

# Abrir o arquivo index.html no navegador
# ou servir com servidor local
python -m http.server 8081
```

### 4. Acessar o Sistema
- **Frontend**: http://localhost:8081
- **Backend API**: http://localhost:8080
- **H2 Console**: http://localhost:8080/h2-console

## 🔑 Credenciais de Acesso

### Usuários Padrão (dados iniciais)
| Email | Senha | Role | Descrição |
|-------|-------|------|-----------|
| admin@techalves.com | admin123 | ADMIN | Administrador do sistema |
| manager@techalves.com | manager123 | MANAGER | Gerente de projetos |
| user@techalves.com | user123 | USER | Usuário comum |

## 📁 Estrutura do Projeto

```
backend/
├── src/main/java/com/techalves/taskmanager/
│   ├── config/         # Configurações de segurança e CORS
│   ├── controller/     # REST Controllers
│   ├── model/          # Entidades JPA
│   ├── repository/     # Repositórios Spring Data
│   └── service/        # Lógica de negócio
├── src/main/resources/
│   ├── application.properties
│   └── data.sql       # Dados iniciais
└── pom.xml            # Dependências Maven

frontend/
├── index.html          # Página de login
├── dashboard.html      # Dashboard principal
├── tasks.html          # Gestão de tarefas
├── projects.html       # Gestão de projetos
├── main.js            # JavaScript principal
└── resources/         # Imagens e assets
```

## 🔧 Configuração do Banco de Dados

### H2 Console (Desenvolvimento)
- **URL**: http://localhost:8080/h2-console
- **JDBC URL**: jdbc:h2:mem:taskdb
- **Username**: sa
- **Password**: (vazio)

### application.properties
```properties
# Configurações do servidor
server.port=8080
spring.application.name=taskflow-system

# Configurações do banco H2
spring.datasource.url=jdbc:h2:mem:taskdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA e Hibernate
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.defer-datasource-initialization=true

# Segurança
spring.security.user.name=admin
spring.security.user.password=admin123
```

## 🎯 Endpoints da API

### Autenticação
- `POST /api/auth/login` - Realizar login
- `POST /api/auth/logout` - Realizar logout
- `GET /api/auth/user` - Informações do usuário atual

### Tarefas
- `GET /api/tasks` - Listar todas as tarefas
- `GET /api/tasks/{id}` - Buscar tarefa por ID
- `POST /api/tasks` - Criar nova tarefa
- `PUT /api/tasks/{id}` - Atualizar tarefa
- `DELETE /api/tasks/{id}` - Excluir tarefa
- `PATCH /api/tasks/{id}/status` - Alterar status da tarefa

### Projetos
- `GET /api/projects` - Listar todos os projetos
- `GET /api/projects/{id}` - Buscar projeto por ID
- `POST /api/projects` - Criar novo projeto
- `PUT /api/projects/{id}` - Atualizar projeto
- `DELETE /api/projects/{id}` - Excluir projeto

## 🎨 Design Visual

### Paleta de Cores
- **Primária**: Azul escuro (#1e293b)
- **Secundária**: Azul claro (#3b82f6)
- **Sucesso**: Verde (#10b981)
- **Alerta**: Amarelo (#f59e0b)
- **Erro**: Vermelho (#ef4444)
- **Fundo**: Cinza claro (#f8fafc)

### Tipografia
- **Títulos**: Inter Bold
- **Corpo**: Inter Regular
- **Responsivo**: Mobile-first

## 🧪 Testes

### Testes Unitários
```bash
# Executar testes do backend
mvn test

# Testar endpoints com curl
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@techalves.com","password":"admin123"}'
```

### Testes de Integração
- Verificar autenticação em todos os endpoints protegidos
- Testar CRUD completo de tarefas
- Validar permissões por role
- Testar responsividade do frontend

## 🚀 Deploy

### GitHub
1. Criar repositório no GitHub
2. Fazer push do código
3. Configurar README.md
4. Adicionar descrição e tags

### Produção
- Configurar banco de dados PostgreSQL/MySQL
- Configurar variáveis de ambiente
- Build com Maven: `mvn clean package`
- Deploy no servidor Tomcat

## 📚 Documentação Adicional

- [Guia de Desenvolvimento Completo](GUIA_DESENVOLVIMENTO_SISTEMA.md)
- [Documentação da API](docs/api.md)
- [Manual do Usuário](docs/user-manual.md)

## 🤝 Contribuição

1. Faça um Fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está licenciado sob a MIT License - veja o arquivo [LICENSE](LICENSE) para detalhes.

## 👥 Autores

- **Seu Nome** - Trabalho inicial - [SeuGitHub](https://github.com/seu-usuario)

## Agradecimentos

- Prof. Francisco Chagas de Lima Júnior - UERN
- Equipe da Disciplina Desenvolvimento de Sistemas Corporativos
- Comunidade Spring Boot e desenvolvimento web

**Nota**: Este projeto foi desenvolvido como trabalho acadêmico para a disciplina Desenvolvimento de Sistemas Corporativos da UERN.

