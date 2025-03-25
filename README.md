# BFT-SMaRt Decentralized Token Infrastructure (DTI)

Este projeto visa construir uma infraestrutura descentralizada de tokens para suportar um mercado de NFTs, utilizando a biblioteca de replicação **BFT-SMaRt**.

---

## 🚀 Como Executar o Projeto

### 1️⃣ Clonar a Biblioteca BFT-SMaRt
Primeiro, é necessário clonar a biblioteca BFT-SMaRt. Para isso, execute o seguinte comando:

```bash
 git clone https://github.com/bft-smart/library.git
```

### 2️⃣ Mover o Código do Projeto
Após clonar a biblioteca, navegue até o diretório correto e copie os arquivos deste projeto para lá:

```bash
 cd library/src/main/java
```

Cole o diretório **dti** dentro desta pasta.

### 3️⃣ Instalar Dependências
Certifique-se de ter o **Gradle** instalado e execute o seguinte comando na diretoria library para compilar e instalar:

```bash
 ./gradlew installDist
```

### 4️⃣ Iniciar os Servidores
Agora, mude para a diretoria **library/build/install/library** e abra **cinco terminais Linux** e execute os seguintes comandos, um por terminal, para iniciar as réplicas do servidor:

```bash
 ./smartrun.sh dti.bftdti.BFTDtiServer 0
```

```bash
 ./smartrun.sh dti.bftdti.BFTDtiServer 1
```

```bash
 ./smartrun.sh dti.bftdti.BFTDtiServer 2
```

```bash
 ./smartrun.sh dti.bftdti.BFTDtiServer 3
```

### 5️⃣ Executar um Cliente
Para rodar um cliente, abra **um novo terminal Linux** e vá de novo para a diretoria **library/build/install/library** e execute o seguinte comando, substituindo `<client_id>` pelo ID do cliente que deseja criar:

```bash
 ./smartrun.sh dti.bftdti.BFTDtiInteractiveClient <client_id>
```

---

## 🛠️ Requisitos
- **Java 8+**
- **Gradle**
- **Múltiplos terminais Linux para rodar as réplicas**

---

## 📌 Sobre o Projeto
A infraestrutura de token descentralizada desenvolvida neste projeto garante **resistência a falhas bizantinas (BFT)**, utilizando o protocolo de replicação **BFT-SMaRt**. Como foi explicitado no enunciado o único cliente capaz de criar moedas (operação MINT) é o que tiver ID 4. Para além disso, todo o resto do projeto funciona tal como pedido.
