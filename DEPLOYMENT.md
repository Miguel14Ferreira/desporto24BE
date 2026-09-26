# Desporto24 BE - Deployment

## Production profile

Run the application with:

```text
ACTIVE_PROFILE=prod
```

Production infrastructure values are supplied at runtime. Do not commit
runtime secret values.

## Required runtime configuration

Database:

- `POSTGRES_HOST`
- `POSTGRES_PORT`
- `POSTGRES_DATABASE`
- `POSTGRES_USERNAME`
- `POSTGRES_PASSWORD`

Application/public URLs:

- `UI_APP_URL`
- `VERIFY_EMAIL_HOST`
- `CONTAINER_PORT`

Security:

- `JWT_SECRET`
- `AES_ENCRYPTION_KEY`

Email:

- `SMTP_USERNAME`
- `SMTP_PASSWORD`
- `SMTP_FROM_EMAIL`

MFA / SMS:

- `TWILIO_ACCOUNT_SID`
- `TWILIO_AUTH_TOKEN`
- `TWILIO_FROM_NUMBER`

Media storage:

- `MEDIA_PROVIDER`
- `MEDIA_LOCAL_ROOT`
- `MEDIA_PUBLIC_BASE_URL`

For S3-compatible storage also configure:

- `MEDIA_S3_BUCKET`
- `MEDIA_S3_REGION`
- `MEDIA_S3_ENDPOINT`
- `MEDIA_S3_ACCESS_KEY`
- `MEDIA_S3_SECRET_KEY`

Optional production controls:

- `HIBERNATE_DDL_AUTO`
- `SWAGGER_ENABLED`

Swagger is disabled by default in the production profile.

## Build

```bash
docker build -t desporto24-be .
```

## Run

Example shape only; inject actual values from the deployment platform or
secret manager:

```bash
docker run --rm \
  -p 8080:8080 \
  --env-file /path/to/runtime.env \
  -v desporto24-media:/app/data/media \
  desporto24-be
```

## Health check

```text
GET /actuator/health
```

Only the health endpoint is intended to be publicly reachable. Detailed
health information is hidden in the production profile.

## Media persistence

When `MEDIA_PROVIDER=local`, persist `/app/data/media` using a Docker
volume or host mount. Otherwise uploaded profile/session images are lost
when the container is replaced.

For horizontally scaled deployments, use S3-compatible storage instead
of local media.

## Database schema

The project currently relies on Hibernate schema management and does not
yet ship a migration framework. Keep `HIBERNATE_DDL_AUTO` aligned with
the deployment environment. Before switching production to `validate`,
introduce versioned database migrations.
