# back-end

### Instruções para executar IA

#### Detecção de Pessoas com YOLOv5

Este é um projeto de detecção de pessoas usando YOLOv5, uma rede neural para detecção de objetos em tempo real.

#### Pré-requisitos

Certifique-se de ter o Python instalado em sua máquina. Você pode baixá-lo e instalá-lo a partir do [site oficial do Python](https://www.python.org/downloads/).

Além disso, você precisa ter o OpenCV e o PyTorch instalados. Você pode instalá-los executando:

```markdown
```bash
pip install opencv-python torch torchvision
```

#### Instalação

1. Clone este repositório para o seu computador:

```bash
git clone https://github.com/seu-usuario/back-end.git
```

2. Navegue até o diretório do projeto:

```bash
cd cd back-end/ml/python
```

3. Execute o script Python:

```bash
python ia_detection.py
```

Isso iniciará a detecção de pessoas em tempo real usando a webcam do seu computador.

#### Finalizar Uso

- Pressione 'q' para sair do programa.

#### Notas

- Certifique-se de ter uma webcam conectada ao seu computador.
- Este código foi testado no Python 3.9.

# Java - Springboot

### Instruções para executar o backend java 

### Pré-requisitos

Certifique-se de ter a versão 17 do java instalado em sua máquina. Você pode baixá-lo e instála-lo a partir do [site oficial da Oracle](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html). 

Além disso é preciso ter o MySQL instalado para realizar a conexão com o banco de dados, você pode baixá-lo e instála-lo a partir do [site oficial do MySQL](https://dev.mysql.com/downloads/installer/), sendo ela a versão MySQL Community. 

### Instalação

1. Clone este repositório para o seu computador:
   
```bash
git clone https://github.com/seu-usuario/back-end.git
```

2. Navegue até o diretório do projeto:

```bash
cd /spring/src/main/java/techguardian/api
```

3. Execute o arquivo ApiApplication.java

### Instalação MySQL

1. Abra o aplicativo MySQL Workbench

2. Crie uma nova conexão, o nome está localizado em DDL.sql na raiz do projeto.

3. Crie as tabelas utilizando o código mysql contido no arquivo DDL.sql .

4. Por final modifique o arquivo application.properties localizado no java. Modifique a linha 8 caso tenha um username criado e a linha 9 com a sua senha de conexão. 

```bash
cd spring/src/main/resources
```
