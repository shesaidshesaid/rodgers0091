# Gerenciador de Tarefas

Bem-vindo ao **Gerenciador de Tarefas**, uma aplicação web que permite cadastrar, editar, concluir e excluir tarefas. As tarefas são armazenadas em um banco de dados MySQL, e é possível adicionar fotos às tarefas, que podem ser protegidas por senha. O projeto utiliza uma arquitetura RESTful no backend e um frontend moderno com React.

## Demonstração

Você pode acessar a aplicação hospedada no Heroku através do seguinte link:

[Gerenciador de Tarefas - Aplicação Demo](https://rodgers0091-7359ca357275.herokuapp.com/)

## Sumário

- [Características](#características)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Pré-requisitos](#pré-requisitos)
- [Instalação](#instalação)
- [Configuração](#configuração)
- [Execução](#execução)
- [Endpoints da API](#endpoints-da-api)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Contribuição](#contribuição)
- [Licença](#licença)

## Características

- **CRUD de Tarefas**: Crie, leia, atualize e exclua tarefas.
- **Adicionar Fotos**: Adicione fotos às tarefas, que podem ser protegidas por senha.
- **Proteção de Fotos**: As fotos podem ser protegidas com uma senha que criptografa a imagem.
- **Marcar Tarefas como Concluídas**: Marque tarefas como concluídas.
- **Interface Amigável**: Frontend moderno utilizando React e Ant Design.
- **UX Melhorada**: Interface intuitiva com feedback ao usuário.
- **Backend RESTful**: API desenvolvida com Spring Boot e Hibernate.
- **Persistência em MySQL**: Dados armazenados em um banco de dados MySQL.

## Tecnologias Utilizadas

### Backend

- **Java 17**
- **Spring Boot 3.1.4**
- **Hibernate**
- **Spring Data JPA**
- **Spring Security Crypto**
- **MySQL Connector**
- **Maven**

### Frontend

- **React**
- **Redux**
- **Redux-Saga**
- **Ant Design**
- **Axios**

### Banco de Dados

- **MySQL**

## Pré-requisitos

- **Java JDK 17** ou superior
- **Node.js** (versão 14 ou superior)
- **MySQL** (versão 8 ou superior)
- **Maven** (versão 3.6 ou superior)
- **Git**

## Instalação

### Clonar o Repositório

```bash
git clone https://github.com/seu-usuario/gerenciador-de-tarefas.git
cd gerenciador-de-tarefas
```

## Configuração

### Backend

1. **Configurar o Banco de Dados MySQL**

   Crie um banco de dados MySQL para a aplicação.

   ```sql
   CREATE DATABASE gerenciador_tarefas;
   ```

2. **Configurar as Credenciais**

   No arquivo `src/main/resources/application.properties`, configure as credenciais do banco de dados:

   ```properties
   # Configurações do Banco de Dados
   spring.datasource.url=jdbc:mysql://localhost:3306/gerenciador_tarefas?useSSL=false&serverTimezone=UTC
   spring.datasource.username=seu_usuario
   spring.datasource.password=sua_senha

   # Configurações do Hibernate
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

   # Diretório para salvar as fotos
   file.upload-dir=uploads
   ```

3. **Configurar o Diretório de Uploads**

   Certifique-se de que o diretório para salvar as fotos (`uploads`) existe na raiz do projeto ou configure outro caminho em `application.properties`.

### Frontend

1. **Configurar a URL do Backend**

   No arquivo `src/utils/axiosInstance.js`, configure a URL base do backend:

   ```javascript
   const axiosInstance = axios.create({
     baseURL: 'http://localhost:8080', // Substitua pela URL do seu backend
   });
   ```

## Execução

### Backend

1. **Compilar e Executar a Aplicação**

   ```bash
   cd backend
   mvn clean install
   mvn spring-boot:run
   ```

   A aplicação estará disponível em `http://localhost:8080`.

### Frontend

1. **Instalar as Dependências**

   ```bash
   cd frontend
   npm install
   ```

2. **Iniciar a Aplicação**

   ```bash
   npm start
   ```

   A aplicação estará disponível em `http://localhost:3000`.

## Endpoints da API

- **GET /api/tarefas**: Lista todas as tarefas.
- **GET /api/tarefas/{id}**: Obtém uma tarefa específica.
- **POST /api/tarefas**: Cria uma nova tarefa.
- **PUT /api/tarefas/{id}**: Atualiza uma tarefa existente.
- **DELETE /api/tarefas/{id}**: Exclui uma tarefa.

## Estrutura do Projeto

### Backend

- **src/main/java**
  - **com.example.tarefas**
    - **TarefasApplication.java**: Classe principal da aplicação Spring Boot.
    - **controller**
      - **TarefaController.java**: Controlador REST para as tarefas.
    - **model**
      - **Tarefa.java**: Modelo da entidade Tarefa.
    - **repository**
      - **TarefaRepository.java**: Interface de acesso ao banco de dados.
    - **config**
      - **EncoderConfig.java**: Configuração do BCryptPasswordEncoder.

- **src/main/resources**
  - **application.properties**: Arquivo de configuração da aplicação.

### Frontend

- **src**
  - **components**
    - **TarefasList.js**: Componente para listar as tarefas.
    - **TarefaForm.js**: Formulário para adicionar/editar tarefas.
  - **store**
    - **actions**: Ações do Redux.
    - **reducers**: Redutores do Redux.
    - **sagas**: Sagas do Redux-Saga.
    - **store.js**: Configuração da store do Redux.
  - **utils**
    - **axiosInstance.js**: Instância configurada do Axios.

## Contribuição

Contribuições são bem-vindas! Por favor, abra uma issue ou um pull request para melhorias, correções de bugs ou novas funcionalidades.

## Licença

Este projeto está licenciado sob a [MIT License](LICENSE).

---

