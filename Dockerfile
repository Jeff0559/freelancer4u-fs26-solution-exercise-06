FROM eclipse-temurin:25-jdk-noble

# Node.js 22 und supervisor installieren
RUN apt-get update && \
    apt-get install -y curl supervisor && \
    curl -fsSL https://deb.nodesource.com/setup_22.x | bash - && \
    apt-get install -y nodejs && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# --- Backend bauen ---
# pom.xml und Maven Wrapper zuerst kopieren fuer besseres Layer-Caching
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

COPY src/ src/
RUN ./mvnw package -DskipTests

# --- Frontend bauen ---
COPY frontend/ frontend/
WORKDIR /app/frontend
RUN npm ci && npm run build

WORKDIR /app

# --- Supervisor konfigurieren ---
# Backend: Spring Boot auf Port 8080
# Frontend: SvelteKit Node-Adapter auf Port 3000
RUN mkdir -p /etc/supervisor/conf.d && cat > /etc/supervisor/conf.d/supervisord.conf <<'EOF'
[supervisord]
nodaemon=true
logfile=/dev/null
logfile_maxbytes=0

[program:backend]
command=java -jar /app/target/freelancer4u-0.0.1-SNAPSHOT.jar
directory=/app
autostart=true
autorestart=true
stdout_logfile=/dev/stdout
stdout_logfile_maxbytes=0
stderr_logfile=/dev/stderr
stderr_logfile_maxbytes=0

[program:frontend]
command=node /app/frontend/build/index.js
directory=/app/frontend
autostart=true
autorestart=true
; API_BASE_URL zeigt auf das Backend im gleichen Container
environment=PORT="3000",API_BASE_URL="http://localhost:8080"
stdout_logfile=/dev/stdout
stdout_logfile_maxbytes=0
stderr_logfile=/dev/stderr
stderr_logfile_maxbytes=0
EOF

# Backend (8080) und Frontend (3000) freigeben
EXPOSE 8080 3000

CMD ["/usr/bin/supervisord", "-c", "/etc/supervisor/conf.d/supervisord.conf"]
