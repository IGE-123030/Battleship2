# Usar a versão mais recente e estável (bookworm)
FROM mcr.microsoft.com/devcontainers/java:1-21-bookworm

# Atualizar pacotes e instalar apenas o docker.io (Git e Maven já estão incluídos)
RUN apt-get update && apt-get install -y \
    docker.io \
    && apt-get clean
