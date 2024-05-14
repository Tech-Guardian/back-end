# FROM gitpod/workspace-python

# RUN pyenv install 3.9 \
#     && pyenv global 3.9

# USER gitpod
# RUN /bin/bash -c ". /home/gitpod/.sdkman/bin/sdkman-init.sh \
#     && sdk install java 17.0.8-oracle < /dev/null \
#     && sdk flush archives \
#     && sdk flush temp"

# FROM gitpod/workspace-mysql
# ---------------------------------------------------------------

FROM gitpod/workspace-full

# Instale as ferramentas necessárias aqui, como Python, Java, MySQL, etc.

# Instalar Python 3.9
USER gitpod
RUN pyenv install 3.9.0 \
    && pyenv global 3.9.0

# Instalar Java 17
USER root
RUN curl -s "https://get.sdkman.io" | bash \
    && /bin/bash -c ". /home/gitpod/.sdkman/bin/sdkman-init.sh \
    && sdk install java 17.0.8-oracle < /dev/null \
    && sdk flush archives \
    && sdk flush temp"

# Instalar MySQL
RUN sudo apt-get update \
    && sudo apt-get install -y mysql-server \
    && sudo service mysql start \
    && sleep 5 \
    && mysql -e "CREATE DATABASE IF NOT EXISTS techguardian" \
    && mysql -u root -e "CREATE USER 'user'@'localhost' IDENTIFIED BY 'pass123'" \
    && mysql -u root -e "GRANT SELECT, INSERT, DELETE, UPDATE ON techguardian.* TO 'user'@'localhost'"

# Execute o script DDL.sql para criar as tabelas e inserir dados de exemplo
COPY DDL.sql /home/gitpod/
RUN sudo service mysql start \
    && mysql -u root -ppass123 < /home/gitpod/DDL.sql
