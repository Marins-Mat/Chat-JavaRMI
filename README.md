# Chat-JavaRMI 💬

Chat em grupo via terminal, desenvolvido em **Java** utilizando **RMI (Remote Method Invocation)**. Vários clientes se conectam a um servidor central e todas as mensagens enviadas são transmitidas (broadcast) para todos os usuários conectados.

## 📋 Sumário

- [Sobre o projeto](#-sobre-o-projeto)
- [Funcionalidades](#-funcionalidades)
- [Tecnologias](#-tecnologias)
- [Arquitetura](#-arquitetura)
- [Estrutura do projeto](#-estrutura-do-projeto)
- [Como executar](#-como-executar)
- [Exemplo de uso](#-exemplo-de-uso)
- [Como funciona](#-como-funciona)

## 📖 Sobre o projeto

O **Chat-JavaRMI** é um sistema de chat distribuído que demonstra o uso do Java RMI para comunicação entre processos em máquinas (ou JVMs) diferentes.

O diferencial do projeto é o uso de **callbacks remotos**: além de o cliente chamar métodos no servidor, o próprio servidor chama métodos remotos nos clientes para entregar as mensagens, sem que eles precisem ficar consultando (polling) por novas mensagens.

## ✨ Funcionalidades

- Entrada de usuários no chat informando um nickname
- Envio de mensagens para todos os participantes conectados
- Notificações automáticas do sistema quando um usuário entra ou sai
- Saída do chat com o comando `sair`
- Interface via linha de comando (CLI)

## 🛠 Tecnologias

- **Java** (JDK 8 ou superior)
- **Java RMI** (`java.rmi`)

## 🏗 Arquitetura

```
┌──────────────┐                         ┌──────────────────┐
│   Cliente 1  │ ── enviarMensagem() ──▶ │                  │
│    (User)    │ ◀── exibirMensagem() ── │                  │
└──────────────┘                         │     Servidor     │
                                         │     (Server)     │
┌──────────────┐                         │                  │
│   Cliente 2  │ ── enviarMensagem() ──▶ │  RMI Registry    │
│    (User)    │ ◀── exibirMensagem() ── │   porta 1099     │
└──────────────┘                         └──────────────────┘
```

O servidor registra a si mesmo no RMI Registry com o nome `Servidor`. Ao entrar no chat, cada cliente envia ao servidor uma referência remota de si mesmo (`UserInterface`), que o servidor guarda em uma lista e usa para distribuir as mensagens.

## 📁 Estrutura do projeto

| Arquivo | Descrição |
| `ServerInterface.java` | Interface remota do servidor: `enviarMensagem`, `entrarNoChat`, `sairDoChat` |
| `UserInterface.java` | Interface remota do cliente (callback): `exibirMensagem` |
| `Server.java` | Implementação do servidor. Mantém a lista de usuários e faz o broadcast das mensagens |
| `User.java` | Implementação do cliente. Lê o teclado, envia mensagens e exibe as recebidas |

## 🚀 Como executar

### 1. Clone o repositório

```bash
git clone https://github.com/<seu-usuario>/Chat-JavaRMI.git
cd Chat-JavaRMI
```

### 2. Compile o projeto

Na pasta src que contém os arquivos `.java`:

```bash
javac *.java
```

### 3. Inicie o servidor

Em um terminal:

```bash
java Server
```

Saída esperada:

```
Servidor de Chat RMI iniciado com sucesso na porta 1099.
```

### 4. Inicie um ou mais clientes

Abra **um novo terminal para cada usuário** e execute:

```bash
java User
```

Informe seu nome de usuário quando solicitado e comece a conversar.

> ⚠️ O servidor precisa estar em execução **antes** de iniciar os clientes.

## 💡 Exemplo de uso

**Terminal do cliente "Ana":**

```
Digite o seu nome de usuario:
Ana
--- Conectado ao Chat! (Digite 'sair' para encerrar) ---
> Usuário: Ana entrou no chat
Usuário: Bruno entrou no chat
Olá, pessoal!
Olá, pessoal!
Oi, Ana!
sair
Você saiu do chat.
```

**Comandos disponíveis:**

| Comando | Ação |
| *qualquer texto* | Envia a mensagem para todos os usuários |
| `sair` | Sai do chat e encerra o cliente |

## ⚙️ Como funciona

1. **Servidor** cria o RMI Registry na porta `1099` com `LocateRegistry.createRegistry(1099)` e publica o objeto remoto com `Naming.rebind("Servidor", s)`.
2. **Cliente** localiza o servidor com `Naming.lookup("Servidor")` e chama `entrarNoChat(nick, usr)`, passando a própria referência remota.
3. O servidor adiciona o cliente à lista de usuários e envia a mensagem *"Usuário: X entrou no chat"* a todos.
4. Quando alguém envia uma mensagem, o cliente chama `enviarMensagem(msg, nick)` no servidor.
5. O servidor percorre a lista de usuários e chama `exibirMensagem(msg)` em cada um deles (**callback**).
6. Ao digitar `sair`, o cliente chama `sairDoChat`, remove a exportação do seu objeto remoto (`unexportObject`) e encerra.



## 📄 Licença

Este projeto está sob a licença MIT. Sinta-se à vontade para usar e modificar.

---

Feito com ☕ e Java RMI.
