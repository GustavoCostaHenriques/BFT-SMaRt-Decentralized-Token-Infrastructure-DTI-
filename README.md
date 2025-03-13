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
Certifique-se de ter o **Gradle** instalado e execute o seguinte comando para compilar e instalar:

```bash
 ./gradlew installDist
```

### 4️⃣ Iniciar os Servidores
Agora, abra **cinco terminais** e execute os seguintes comandos, um por terminal, para iniciar as réplicas do servidor:

```bash
 cd library/build/install/library
```

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
Para rodar um cliente, abra **um novo terminal** e execute o seguinte comando, substituindo `<client_id>` pelo ID do cliente que deseja criar:

```bash
 ./smartrun.sh dti.bftdti.BFTDtiInteractiveClient <client_id>
```

---

## 🛠️ Requisitos
- **Java 8+**
- **Gradle**
- **Git**
- **Múltiplos terminais para rodar as réplicas**

---

## 📌 Sobre o Projeto
A infraestrutura de token descentralizada desenvolvida neste projeto garante **resistência a falhas bizantinas (BFT)**, utilizando o protocolo de replicação **BFT-SMaRt**. Esse sistema é ideal para aplicações que exigem alta confiabilidade e segurança, como mercados de NFTs descentralizados.
