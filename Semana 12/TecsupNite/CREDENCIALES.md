# Credenciales de TecsupNite

Todo lo necesario para entrar a la app y a Firebase. El repositorio es público: estas cuentas son solo de prueba para el laboratorio.

## Usuarios de la app

| Rol | Correo | Contraseña |
|---|---|---|
| Admin | `admin@tecsupnite.com` | `Esotilincito123@` |
| Usuario | `usuario@tecsupnite.com` | `Esotilincito123@` |

El admin ve la pestaña Admin. El usuario normal no la ve.

## Firebase

- **Proyecto:** `tecsupnite` (consola: https://console.firebase.google.com/project/tecsupnite)
- **Cuenta dueña:** la cuenta de Google de Renzo León (iniciar sesión en la consola con ella)
- **App Android:** paquete `com.tecsup.fortnite`
- **Archivo de configuración:** `app/google-services.json` (ya está en el repo)
- **Authentication:** correo y contraseña activado
- **Firestore, colección `usuarios`:**

| Documento (UID) | Correo | Campo `rol` |
|---|---|---|
| `Gu013fdtsiZIdW6c36aqLpuDaYt2` | `admin@tecsupnite.com` | `admin` |
| `immuh3SLYpNWoAlrKaprE314auf2` | `usuario@tecsupnite.com` | `usuario` |

Cada documento también tiene el campo `nombre`.

Si se borra un usuario de Authentication, su UID cambia al crearlo de nuevo y hay que renombrar su documento en `usuarios`.

## API de Fortnite

`https://fortnite-api.com` no necesita clave para los endpoints que usa la app (`v2/shop` y `v2/cosmetics/br/{id}`).

## GitHub

- **Repositorio:** https://github.com/RXDG2908/Moviles-Android-CD (cuenta `RXDG2908`)
- **Release con el APK:** https://github.com/RXDG2908/Moviles-Android-CD/releases/tag/v1.0
