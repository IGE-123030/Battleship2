# Usar a versão mais recente e estável
FROM mcr.microsoft.com/devcontainers/java:1-21-bookworm

# 1. Remove o repositório problemático do Yarn
# 2. Atualiza os pacotes
# 3. Instala o docker.io
RUN rm -f /etc/apt/sources.list.d/yarn.list && \
    apt-get update && apt-get install -y \
    docker.io \
    && apt-get clean
