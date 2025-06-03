# HOTELBAO - Sistema de Controle de Hotel (Backend)

Este projeto tem como objetivo desenvolver o **backend funcional** de um sistema de controle de hotel, chamado **HOTELBAO**, utilizando conceitos avançados de programação backend.

## Disciplina

**Programação Backend**  
Professor: Bruno Ferreira  
Turma: Integral  
Entrega: até 25/06/2025  
Apresentação: 26/06/2025  
Nota máxima: 30 pontos  

Aluno: Leandro Marcos de Deus   
Matrícula: 0056443  

## Objetivos

- Reforçar conceitos de programação backend.
- Simular o desenvolvimento de um sistema com múltiplos requisitos.
- Implementar controle de usuários, quartos e estadias.
- Gerar relatórios e cupom fiscal via API.
- Utilizar boas práticas: comentários, identação e legibilidade do código.

## Entrega

- Entregar via **GitHub** (link no Google Classroom).
- **Trabalho em dupla**, mas **apresentação é individual**.
- Apenas **um aluno envia** com o nome dos dois no repositório.

---

## Perfis de Usuário

| Perfil              | Permissões |
|---------------------|------------|
| **Não autenticado** | Listar quartos |
| **Cliente**         | Criar conta, reservar quartos, ver relatórios, recuperar senha |
| **Administrador**   | Todas as permissões + cadastrar clientes e quartos (senha = telefone do cliente) |

---

## Funcionalidades

### 1. Cadastro de Clientes e Quartos
- **Clientes**: código, nome, e-mail, login, senha, celular
- **Quartos**: código, descrição, valor, URL da imagem

### 2. CRUD de Estadias
- Associar cliente, quarto e data (1 diária).
- Verificar conflito de datas no mesmo quarto.

### 3. Relatórios
- Lista de clientes
- Lista de quartos
- Lista de estadias
- Estadia **mais cara** por cliente
- Estadia **mais barata** por cliente
- **Valor total** de estadias de um cliente

### 4. Cupom Fiscal
- Exibir dados do cliente + estadias + valor total
- Validar presença de dados obrigatórios

### 5. Limpar Base de Dados
- Resetar dados para o estado inicial (com mensagens confirmando ação)

---

## Requisitos Técnicos

### Requisitos de Impressão (para relatórios)
- Perguntar se o usuário deseja imprimir
- Exibir mensagem adequada se não houver dados

### Requisitos do Cupom Fiscal
- Mostrar todos os dados do cliente
- Deve existir ao menos uma estadia válida
- Somente quartos com descrição e valor devem ser somados

---

## Exemplo de Fluxo

1. Administrador cadastra clientes e quartos.
2. Cliente se registra e realiza uma reserva.
3. Cliente ou admin gera relatórios.
4. Admin imprime cupom fiscal.
5. Base pode ser limpa com opção específica.

---

## Extras Obrigatórios

- Documentação **Swagger** completa
- HATEOAS implementado em todos os endpoints
- Testes automatizados para todos os endpoints

## Extra para Ponto Adicional

- **Frontend opcional**: pode ser feito em Java, JavaScript, Python ou C#.
- Até **+5 pontos** extras pela qualidade da UX.

---

## Regras Importantes

- Trabalhos com **cópia** receberão **nota 0**
- O código será avaliado pela **legibilidade e comentários**
- A entrega fora do prazo será desconsiderada

---

## Exemplo de Execução via Postman/Swagger

> Este projeto é focado em backend. Você pode testar todas as funcionalidades usando Postman ou Swagger UI.

---

## Contato

Em caso de dúvidas, entre em contato com o professor durante as aulas ou pelo Google Classroom.
