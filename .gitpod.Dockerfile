# Usar uma imagem base que inclua o Python
FROM gitpod/workspace-full

# Instalar as ferramentas necessárias, como Python, Java, MySQL, etc.

# Instalar Python 3.9
USER gitpod
RUN pyenv install 3.9.0 \
    && pyenv global 3.9.0

# Definir o Python 3.9 como padrão
ENV PATH /home/gitpod/.pyenv/versions/3.9.0/bin:$PATH

# Atualizar pip
RUN pip install --upgrade pip

# Copiar o arquivo requirements.txt para o container
COPY requirements.txt /home/gitpod/

# Instalar as dependências do Python
RUN pip install -r /home/gitpod/requirements.txt

# Instalar Java 17
USER root
RUN curl -s "https://get.sdkman.io" | bash \
    && /bin/bash -c ". /home/gitpod/.sdkman/bin/sdkman-init.sh \
    && sdk install java 17.0.8-oracle < /dev/null \
    && sdk flush archives \
    && sdk flush temp"

# Instalar MySQL
RUN apt-get update \
    && apt-get install -y mysql-server \
    && service mysql start \
    && sleep 5 \
    && mysql -e "CREATE DATABASE IF NOT EXISTS techguardian" \
    && mysql -u root -e "CREATE USER 'user'@'localhost' IDENTIFIED BY 'pass123'" \
    && mysql -u root -e "GRANT SELECT, INSERT, DELETE, UPDATE ON techguardian.* TO 'user'@'localhost'"

# Copiar e executar o script DDL.sql para criar as tabelas e inserir dados de exemplo
COPY DDL.sql /home/gitpod/
RUN service mysql start \
    && mysql -u root -ppass123 < /home/gitpod/DDL.sql
