# presentar.md — Lo que falta hacer en la Mac antes de presentar

Instrucciones para **Claude Code en la Mac del alumno**. Todo lo de esta lista no se pudo
hacer en la nube porque allá no hay Android SDK ni emulador: la rama `mejora-ia` **nunca
se ha compilado ni probado**.

Sigue los pasos en orden. Al terminar cada uno, dile al usuario en una línea qué hiciste y
si salió bien. Al final, entrega el resumen del paso 9.

---

## Reglas (obligatorias)

1. **Ramas separadas siempre:** `main` = Fase 1 sin IA; `mejora-ia` = Fase 2 con IA +
   mejoras del profesor. **Nunca** hagas merge entre ellas ni aceptes un Pull Request de
   `mejora-ia` hacia `main`.
2. **`main` no se toca:** no se cambia código ni se hace commit en `main`.
3. **En `mejora-ia` solo se corrigen errores** que impidan compilar o que rompan el flujo
   del paso 4, y **solo con el permiso del usuario** (muéstrale el error y la corrección
   antes). Cada corrección va en su propio commit:
   - mensaje en español, sin tildes, que diga qué corrige;
   - **sin** líneas `Co-Authored-By` ni ninguna firma de IA;
   - agregada al final de `Semana 6/SaludPlusCitas/PROMPTS.md` como un prompt nuevo
     (prompt, respuesta resumida y qué se corrigió);
   - `git push origin mejora-ia`.
4. Pregunta antes de cualquier comando que borre algo (`reset --hard`, `clean`,
   `push --delete`, `worktree remove`).
5. Ignora `Semana 6/TecsupStore`: no es parte de esta presentación.

---

## 1. Preparar ramas, Android Studio y emulador

Sigue **`inicio.md`** (raíz de `mejora-ia`) del paso 1 al paso 7:

- actualizar `main` y `mejora-ia` (su historial se reescribió: lee la nota del paso 2);
- un worktree para `mejora-ia`;
- abrir los dos proyectos en Android Studio;
- un emulador con **API 26 o mayor**.

## 2. Compilar las dos ramas

En cada proyecto (`PROYECTO_MAIN` y `PROYECTO_IA`, ver `inicio.md`):

```bash
./gradlew assembleDebug
```

- `main` ya compilaba en la Mac: si falla, solo informa el error (regla 2).
- `mejora-ia` **es la primera vez que se compila**. Si falla:
  1. Muestra al usuario el error exacto (archivo, línea y mensaje).
  2. Propón la corrección mínima. Aplícala solo si el usuario acepta (regla 3).
  3. Vuelve a compilar hasta que salga `BUILD SUCCESSFUL`.

Es normal que Android Studio marque `LocalDate` con el aviso "Call requires API level 26":
no impide compilar.

## 3. Instalar la rama `mejora-ia` en el emulador

```bash
adb install -r "PROYECTO_IA/app/build/outputs/apk/debug/app-debug.apk"
adb shell am start -n com.saludplus.citas/.MainActivity
```

## 4. Probar el flujo de `mejora-ia` (con el usuario)

Tú no tocas el emulador: el usuario navega y te dice qué ve. Marca cada punto como
**OK** o **FALLA**. Si algo falla, aplica la regla 3.

**Sesión**
- [ ] Login con `juan@correo.com` / `123456` entra.
- [ ] Cerrar sesión y entrar con `987654321` / `123456` también funciona.
- [ ] Registro sin correo muestra error; con correo repetido dice que ya existe.
- [ ] Registro de María López (`912345678`, `maria@correo.com`, `654321`) **no entra
      directo**: va a Login con el correo ya escrito y el aviso "Cuenta creada, inicia
      sesión". Atrás no vuelve al Registro.

**Inicio y sedes**
- [ ] Inicio tiene el mosaico **Mis doctores** (y ya no "Mis citas").
- [ ] **Agendar cita** abre **Elige tu sede** con 12 sedes.
- [ ] Elegir **Miraflores → Medicina General** muestra 2 médicos, con chips de
      disponibilidad de colores ("Disponible … · N cupos").

**Calendario (Fecha y hora)**
- [ ] Con la **Dra. Lucía Fernández** dice "Atiende: Mar, Jue".
- [ ] Los días que no atiende salen grises, tachados y con "No atiende"; no se pueden
      tocar.
- [ ] Los demás días salen en verde, amarillo o rojo con "N cupos"; hay leyenda.
- [ ] La flecha `<` no hace nada en la semana actual; `>` cambia los días y el mes.
- [ ] Elegir un día y cambiar de semana **borra** el día y la hora elegidos.
- [ ] Al elegir un día salen los 9 horarios: libres en verde, ocupados tachados en gris,
      el elegido en azul. Al cambiar de día se borra la hora.
- [ ] "Continuar" solo se habilita con día y hora elegidos.

**Confirmar y mis citas**
- [ ] Confirmar muestra la fecha en español ("Martes 13 de octubre 2026") y la sede con
      su dirección.
- [ ] Cita agendada muestra la sede. Atrás vuelve al Inicio.
- [ ] Al volver a Fecha y hora con la misma doctora y el mismo día, la hora reservada sale
      tachada y el día tiene un cupo menos.
- [ ] La pestaña **Citas** muestra la sede arriba y la cita. **Cambiar sede** a otra deja
      la lista vacía.

**Mis doctores**
- [ ] Médicos agrupados por especialidad, con buscador ("rojas" filtra).
- [ ] Tocar un médico que no atiende en la sede elegida pide primero una de sus sedes.

**Cerrar sesión**
- [ ] Perfil → Cerrar sesión vuelve al Splash. Al entrar de nuevo y tocar la pestaña
      Citas, pide la sede otra vez.

## 5. Probar rápido la rama `main`

Instala `main` (paso 8 de `inicio.md`) y comprueba con el usuario que el flujo de la
Fase 1 sigue funcionando: login con teléfono `987654321` / `123456` → agendar una cita →
Mis citas → Perfil. Si algo falla, solo infórmalo (regla 2).

## 6. Capturas e informe en Word

Sigue **`Semana 6/SaludPlusCitas/informe/capturas.md`** (en la carpeta de `mejora-ia`):
toma las 27 capturas y genera `Informe_Lab06_SaludPlus.docx`. Al abrirlo, actualiza el
índice (clic derecho → Actualizar campos).

Recuérdale al usuario que lea y ajuste con sus palabras las secciones **7 (preguntas de
reflexión)** y **8 (observaciones y conclusiones)**: la guía pide que sean de elaboración
individual.

## 7. Limpieza en GitHub (pregunta antes)

- Existe una rama sobrante, `ccr-d6cb0a63-vz5uhj`, que quedó de la sesión en la nube. Su
  contenido ya está en `mejora-ia`. Con permiso del usuario:

  ```bash
  git push origin --delete ccr-d6cb0a63-vz5uhj
  ```

  Si GitHub no lo permite, dile al usuario que la borre en
  `https://github.com/RXDG2908/Moviles-Android-CD/branches` (ícono de papelera).
- El Pull Request #1 (de `mejora-ia` a `main`) figura como "merged", pero ese merge ya se deshizo en `main`. **No
  lo reabras** ni crees uno nuevo.

## 8. Dejar todo listo para presentar

- Emulador encendido con la rama **`main`** instalada y abierta en el Splash.
- Las dos ventanas de Android Studio abiertas (main y mejora-ia).
- Barra de estado normal: `adb shell am broadcast -a com.android.systemui.demo -e command exit`.
- Ten a mano los comandos para cambiar de rama en el emulador (paso 8 de `inicio.md`).

## 9. Resumen final para el usuario

1. Último commit de `main` y de `mejora-ia`, y la confirmación de que están separadas.
2. Compilación de cada rama: OK o error.
3. Resultado de las pruebas del paso 4 y del paso 5 (lista OK / FALLA).
4. Correcciones hechas en `mejora-ia` (si hubo), con sus commits.
5. Informe: ruta del Word y capturas faltantes (si hay).
6. Estado de la rama `ccr-d6cb0a63-vz5uhj`.
7. Credenciales para la demo:

   | Rama | Usuario | Contraseña |
   |---|---|---|
   | `main` | teléfono `987654321` | `123456` |
   | `mejora-ia` | `juan@correo.com` o `987654321` | `123456` |

8. El guion de la demo está al final de `inicio.md`.
