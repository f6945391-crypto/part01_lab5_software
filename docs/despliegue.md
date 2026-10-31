# Guía para levantar el proyecto con Docker

Guía paso a paso para construir y ejecutar **Champions Predictions** en local
usando Docker (aplicación + base de datos PostgreSQL). Probada en Windows con
Docker Desktop.

> Resultado esperado: la app disponible en **http://localhost:8080**, con 6
> partidos ya sincronizados en la base de datos y el registro de predicciones
> funcionando.

---

## 1. Requisitos previos

- **Docker Desktop** instalado y **en ejecución**. Verifícalo:
  ```bash
  docker --version
  docker compose version
  ```
- Puertos libres en tu equipo:
  - **8080** → la aplicación.
  - **5434** → la base de datos (mapeada al 5432 interno del contenedor).

No necesitas tener Java, Maven ni PostgreSQL instalados: todo se construye y
corre dentro de los contenedores.

- **Token de football-data.org (OBLIGATORIO).** La app consume la API real de
  la Champions League. Regístrate gratis en
  <https://www.football-data.org/client/register> y recibirás un token por
  correo. Lo colocarás en el paso 3.

---

## 2. Ubicarte en la carpeta del proyecto

Abre una terminal en la carpeta que contiene el `docker-compose.yml`:

```bash
cd "C:\Usuarios\User\Labs_software\parte01"
```

---

## 3. Configurar variables de entorno (incluye el token)

Copia la plantilla y coloca tu **token** de football-data.org.

En **Git Bash / Linux / Mac**:

```bash
cp .env.example .env
```

En **CMD / PowerShell** (Windows):

```bat
copy .env.example .env
```

**Importante:** después de copiar, **abre `.env` y reemplaza `pon-aqui-tu-token`
por tu token real**. Si lo dejas con el placeholder, la API responde
`Your API token is invalid`. Y si `.env` **ya** tiene tu token,
**no vuelvas a ejecutar `cp`/`copy`**: sobrescribiría el archivo y borraría el token.

Variables disponibles:

| Variable | Para qué sirve | Por defecto |
|----------|----------------|-------------|
| `MATCH_API_MODE` | Fuente de partidos: `real` o `simulado` | `real` |
| `MATCH_API_TOKEN` | **Token de football-data.org** (obligatorio en modo `real`) | *(vacío)* |
| `MATCH_API_COMPETITION` | Competición (CL = Champions League) | `CL` |
| `DB_NAME` | Nombre de la base de datos | `champions_db` |
| `DB_USER` | Usuario de PostgreSQL | `postgres` |
| `DB_PASSWORD` | Contraseña de PostgreSQL | `postgres` |
| `APP_PORT` | Puerto de la app en tu equipo | `8080` |

> Sin token, el modo `real` no podrá traer partidos (el panel mostrará el error
> de sincronización). Para una prueba sin token puedes usar `MATCH_API_MODE=simulado`.

---

## 4. Construir y levantar

```bash
docker compose up --build
```

Qué ocurre:

1. **Build de la app:** Docker descarga la imagen de Java, compila el proyecto
   con Maven dentro del contenedor y genera el `.jar`.
   *(La primera vez tarda varios minutos; las siguientes son mucho más rápidas
   por la caché.)*
2. **Base de datos:** se levanta `postgres:16-alpine` y se espera a que esté
   **sana** (healthcheck) antes de arrancar la app.
3. **Aplicación:** arranca, se conecta a la base y **sincroniza los partidos**.

Sabrás que está listo cuando veas en los logs:

```
champions-db   | ... database system is ready to accept connections
champions-app  | ... Tomcat started on port 8080
champions-app  | ... Started ChampionsApplication
```

> Para dejarlo corriendo en segundo plano, usa `docker compose up --build -d`.

---

## 5. Abrir y verificar

1. Abre **http://localhost:8080**.
2. En el **panel** deberías ver **6 partidos** y la hora de la última sincronización.
3. Entra a **Partidos** → **Registrar predicción** en cualquier tarjeta.
   - El resultado debe coincidir con el marcador (p. ej. *Gana el local* con `2–1`).
   - Si no coincide, la app lo rechaza (validación de negocio).
4. Entra a **Predicciones** → tu pronóstico aparece guardado.

---

## 6. Evidencia de persistencia (volúmenes)

Los datos de PostgreSQL viven en el volumen `champions_pgdata`, así que
sobreviven a recrear los contenedores:

```bash
# 1. Registra una predicción desde la web.
# 2. Baja los contenedores SIN borrar el volumen:
docker compose down

# 3. Vuelve a levantar:
docker compose up

# 4. Entra a Predicciones: tu dato sigue ahí.
```

---

## 7. Comandos útiles

| Acción | Comando |
|--------|---------|
| Ver estado de los contenedores | `docker compose ps` |
| Ver logs de la app en vivo | `docker compose logs -f app` |
| Ver logs de la base | `docker compose logs -f db` |
| Detener (conserva datos) | `docker compose down` |
| Detener y **borrar datos** | `docker compose down -v` |
| Reconstruir desde cero | `docker compose build --no-cache` |

---

## 8. Solución de problemas

- **`port is already allocated` (8080 o 5434):** otro proceso usa el puerto.
  Ciérralo, o cambia `APP_PORT` en `.env` (para el 8080) o el mapeo del `db`
  en `docker-compose.yml` (para el 5434).
- **La app no conecta a la base al arranque:** gracias al *healthcheck* la app
  espera a que la base esté lista; si aun así falla, vuelve a ejecutar
  `docker compose up`.
- **El build falla por red:** reintenta `docker compose up --build`
  (descarga de imágenes/dependencias).

---

## 9. Alternativa: ejecutar sin Docker (local)

Si prefieres correrlo sin contenedores, necesitas **JDK 21** y **PostgreSQL**:

```bash
# Crear la base (Windows / PostgreSQL 18)
$env:PGPASSWORD='TU_CONTRASEÑA'; & 'C:\Program Files\PostgreSQL\18\bin\psql.exe' -U postgres -h localhost -c "CREATE DATABASE champions_db;"

# Ejecutar
.\mvnw.cmd spring-boot:run
```

Luego abre **http://localhost:8080**.

---

## 10. Siguiente paso (fuera de esta guía): nube

Para el despliegue en AWS, el flujo típico es: etiquetar la imagen
(`docker tag`), publicarla en un registro (Docker Hub / ECR), y en la instancia
cloud hacer `docker compose up -d` apuntando a esa imagen. Eso se documenta
aparte.
