# Cambios pedidos por el profesor (rama `mejora-ia`)

Guía de **qué se cambió, cómo se hizo y qué archivo va con qué** en HuellitasVet.
Todo respeta MVVM: la pantalla (View) solo observa `StateFlow` y avisa lo que hace el usuario;
las reglas están en el ViewModel; los datos pasan por la interface `VetRepository` hasta Room.

En el código, cada cambio está marcado con un comentario **`// Mejora: ...`**, y el paquete del
administrador empieza con un bloque **`// GUÍA`** ([`ui/admin/AdminComponentes.kt`](app/src/main/java/com/tecsup/huellitasvet/ui/admin/AdminComponentes.kt)).

---

## 0. Resumen rápido

| # | Pedido del profesor | Resultado |
|---|---|---|
| 1 | Paleta de colores buena, que coincida con la app, usando skills de diseño, sin plomos | Se **mantiene la paleta original** (verde dueño, azul veterinario). Los colores nuevos (tipos de servicio y administrador) se validaron con la skill `dataviz` y **no usan gris** |
| 2 | En servicios, tipo de servicio que el cliente pueda elegir | 3 tipos: **Salud, Estética, Prevención**. El dueño elige el tipo y luego el servicio en un **combobox** |
| 3 | Servicios en combobox (corte de pelo, baño de perro, etc.) | 10 servicios de ejemplo. El combobox muestra el ícono, la duración y el precio de cada uno |
| 4 | Registro con teléfono, correo y distrito | Nuevos campos **Celular** (9 dígitos, empieza con 9) y **Distrito** (combobox con 29 distritos de Lima) |
| 5 | Usuario administrador con todas sus funciones y control total | Nuevo rol **Admin**: panel, usuarios de los 3 roles, mascotas de todos, servicios y agenda. Crea, edita y elimina todo |

### Usuarios de prueba

| Rol | Correo | Clave |
|---|---|---|
| Dueño | ana@correo.test | 1234 |
| Dueño | luis@correo.test | 1234 |
| Veterinario | vet@huellitas.test | admin |
| Veterinario | mario@huellitas.test | admin |
| **Administrador** | **admin@huellitas.test** | **admin** |

> La base subió de **versión 2 a 3** (columnas nuevas). Como el proyecto usa
> `fallbackToDestructiveMigration()`, al instalar la nueva versión la base se borra y se vuelven a
> cargar los datos de prueba ([`DatosIniciales.kt`](app/src/main/java/com/tecsup/huellitasvet/data/local/DatosIniciales.kt)).

---

## 1. Paleta de colores

**Qué pidió el profesor:** colores buenos, que coincidan con la app y sin grises "plomo".

**Cómo se hizo:**

1. Se usó la skill de diseño **`dataviz`**, que trae un validador de paletas
   (`validate_palette.js`). El validador mide el contraste (WCAG) y la separación entre colores
   para personas con daltonismo (ΔE en OKLab).
2. **Paleta original intacta:** dueño `#059669`, veterinario `#1D4ED8`, estados de cita y modo oscuro
   quedan igual que antes, para no romper el diseño que ya tenía la app.
3. **Colores nuevos, con el mismo estilo de la app** (primario fuerte + contenedor claro + fondo teñido):

| Uso | Colores | Validación |
|---|---|---|
| Tipo **Salud** | azul `#2A78D6` | Los 3 tipos pasan **todos contra todos**: daltonismo ΔE 9.2 (mín. 8), visión normal ΔE 24 (mín. 15), en claro y en oscuro |
| Tipo **Estética** | naranja `#EB6834` | ″ |
| Tipo **Prevención** | verde agua `#1BAF7A` | ″ (la etiqueta de texto siempre acompaña al color) |
| **Administrador** | terracota `#C2410C`, contenedor `#FFEDD5`, fondo `#FFFAF5` | Blanco sobre el botón 5.2:1, texto secundario marrón `#8A4B2A` 6.7:1 (AA pide 4.5:1) |
| Admin, modo oscuro | `#FB923C` sobre fondo marrón `#1C120D` | Botón 6.9:1, texto 16.3:1 |

4. **Sin plomo en el administrador:** sus textos secundarios, bordes, fondos, Snackbar y el estado
   "Atendida" usan tonos cálidos o azul petróleo (`#0E7490`), nunca gris.
5. ¿Por qué solo 3 tipos? La skill indica que, cuando los colores pueden aparecer juntos
   (chips en fila), solo **3** colores se distinguen bien todos contra todos. Con 4 tipos siempre
   había un par que fallaba la prueba.

**Archivos:**

| Archivo | Qué tiene |
|---|---|
| [`ui/theme/Color.kt`](app/src/main/java/com/tecsup/huellitasvet/ui/theme/Color.kt) | Colores originales + `Admin*` + `Tipo*` |
| [`ui/theme/Theme.kt`](app/src/main/java/com/tecsup/huellitasvet/ui/theme/Theme.kt) | `VetTheme` (sin cambios para dueño/vet) + `esquemaAdminClaro/Oscuro`, `primarioDe(rol)`, `colorDeTipo(tipo)` |
| [`ui/components/EstadoCitaChip.kt`](app/src/main/java/com/tecsup/huellitasvet/ui/components/EstadoCitaChip.kt) | "Atendida" en azul petróleo solo dentro del tema del administrador |

---

## 2. Tipo de servicio + combobox de servicios

**Flujo del dueño:**

```
Inicio → tarjeta del tipo (Salud / Estética / Prevención)
       → Pedir cita (el tipo ya viene elegido)
       → combobox "Servicio de estética" (Baño de perro, Corte de pelo...)
       → tarjeta con el detalle (descripción, duración, precio) → fecha → hora → Confirmar
```

**Capa por capa:**

| Capa | Archivo | Cambio |
|---|---|---|
| Modelo | [`model/TipoServicio.kt`](app/src/main/java/com/tecsup/huellitasvet/model/TipoServicio.kt) (nuevo) | `enum TipoServicio { SALUD, ESTETICA, PREVENCION }` con etiqueta y descripción |
| Entidad | [`data/local/entity/Servicio.kt`](app/src/main/java/com/tecsup/huellitasvet/data/local/entity/Servicio.kt) | Nueva columna `tipo` |
| Datos | [`data/local/DatosIniciales.kt`](app/src/main/java/com/tecsup/huellitasvet/data/local/DatosIniciales.kt) | 10 servicios con su tipo (Baño de perro, Baño de gato, Corte de pelo, Corte de uñas, Limpieza dental...) |
| DAO | [`data/local/dao/CitaDao.kt`](app/src/main/java/com/tecsup/huellitasvet/data/local/dao/CitaDao.kt) | "Citas por servicio" ahora también devuelve el `tipo` |
| Modelo | [`model/ConteoServicio.kt`](app/src/main/java/com/tecsup/huellitasvet/model/ConteoServicio.kt) | Campo `tipo` (coincide con el alias del SELECT) |
| ViewModel | [`viewmodel/PedirCitaViewModel.kt`](app/src/main/java/com/tecsup/huellitasvet/viewmodel/PedirCitaViewModel.kt) | `tipo` (StateFlow), `serviciosDelTipo` (combine), `elegirTipo()` borra el servicio si cambia el tipo |
| ViewModel | [`viewmodel/ServiciosViewModel.kt`](app/src/main/java/com/tecsup/huellitasvet/viewmodel/ServiciosViewModel.kt) | Filtro por tipo: `filtroTipo`, `serviciosFiltrados`, `filtrar()` |
| ViewModel | [`viewmodel/PanelViewModel.kt`](app/src/main/java/com/tecsup/huellitasvet/viewmodel/PanelViewModel.kt) | Cada barra del gráfico lleva su tipo |
| Componente | [`ui/components/VetCombo.kt`](app/src/main/java/com/tecsup/huellitasvet/ui/components/VetCombo.kt) (nuevo) | **Combobox reutilizable** (`ExposedDropdownMenuBox`), se usa en 5 lugares |
| Componente | [`ui/components/TipoServicioChip.kt`](app/src/main/java/com/tecsup/huellitasvet/ui/components/TipoServicioChip.kt) (nuevo) | Tarjeta para elegir el tipo + etiqueta pequeña de color |
| Vista dueño | [`ui/dueno/PedirCitaScreen.kt`](app/src/main/java/com/tecsup/huellitasvet/ui/dueno/PedirCitaScreen.kt) | Tipo (chips) → servicio (combobox) → detalle |
| Vista dueño | [`ui/dueno/InicioScreen.kt`](app/src/main/java/com/tecsup/huellitasvet/ui/dueno/InicioScreen.kt) | La sección "Servicios" muestra los 3 tipos con su cantidad |
| Vista vet | [`ui/veterinario/ServiciosScreen.kt`](app/src/main/java/com/tecsup/huellitasvet/ui/veterinario/ServiciosScreen.kt) | Filtros por tipo, etiqueta de color, 7 íconos (`iconoServicio`) |
| Vista vet | [`ui/veterinario/FormularioServicioScreen.kt`](app/src/main/java/com/tecsup/huellitasvet/ui/veterinario/FormularioServicioScreen.kt) | Elegir el tipo y uno de 7 íconos |
| Vista vet | [`ui/veterinario/PanelScreen.kt`](app/src/main/java/com/tecsup/huellitasvet/ui/veterinario/PanelScreen.kt) | Barras del color de su tipo + leyenda |
| Navegación | [`ui/navigation/Rutas.kt`](app/src/main/java/com/tecsup/huellitasvet/ui/navigation/Rutas.kt), [`NavVet.kt`](app/src/main/java/com/tecsup/huellitasvet/ui/navigation/NavVet.kt) | `pedir_cita?mascotaId=&tipo=` (el tipo llega desde Inicio) |

---

## 3. Registro con celular y distrito

| Capa | Archivo | Cambio |
|---|---|---|
| Entidad | [`data/local/entity/Usuario.kt`](app/src/main/java/com/tecsup/huellitasvet/data/local/entity/Usuario.kt) | Columnas `telefono` y `distrito` |
| Modelo | [`model/Distritos.kt`](app/src/main/java/com/tecsup/huellitasvet/model/Distritos.kt) (nuevo) | Lista de 29 distritos de Lima |
| Modelo | [`model/Validaciones.kt`](app/src/main/java/com/tecsup/huellitasvet/model/Validaciones.kt) (nuevo) | Reglas compartidas (registro y admin): nombre, correo, **celular** `^9\d{8}$`, **distrito**, clave |
| ViewModel | [`viewmodel/SesionViewModel.kt`](app/src/main/java/com/tecsup/huellitasvet/viewmodel/SesionViewModel.kt) | `registrar(nombre, correo, telefono, distrito, clave, confirmar)`; nuevos errores `telefono` y `distrito` |
| Vista | [`ui/dueno/RegistroScreen.kt`](app/src/main/java/com/tecsup/huellitasvet/ui/dueno/RegistroScreen.kt) | Campo Celular (solo dígitos, máx. 9) + combobox Distrito |
| Vista | [`ui/veterinario/AjustesScreen.kt`](app/src/main/java/com/tecsup/huellitasvet/ui/veterinario/AjustesScreen.kt) / [`ui/dueno/PerfilScreen.kt`](app/src/main/java/com/tecsup/huellitasvet/ui/dueno/PerfilScreen.kt) | La tarjeta del usuario muestra "celular · distrito" (`detalleDe`) |

---

## 4. Administrador (control total)

### ¿Qué es el administrador?

Es un **tercer rol** (`Rol.ADMINISTRADOR`) que gestiona toda la clínica. No atiende citas como el
veterinario ni pide citas como el dueño: **crea, edita y elimina** a los demás.

> Nota: en el proyecto base el veterinario ya se llamaba `Rol.ADMIN`. Para no romper lo existente
> se dejó así y el nuevo rol se llama `ADMINISTRADOR` (en pantalla: "Admin").

### ¿Qué puede hacer?

| Sección | Funciones |
|---|---|
| **Panel** | Total de usuarios, citas y mascotas; mosaico Dueños / Veterinarios / Mascotas / Servicios; acciones rápidas |
| **Usuarios** | Pestañas Dueños / Veterinarios / Admins, búsqueda (nombre, correo, distrito, celular), crear, editar, eliminar |
| **Mascotas** | Las de **todos** los dueños: buscar, ver ficha, crear (eligiendo el dueño en un combobox), editar, eliminar |
| **Servicios** | Las mismas pantallas del veterinario (crear, editar, activar/desactivar, tipo) |
| **Agenda** | La agenda del veterinario (confirmar, atender, cancelar citas) |
| **Ajustes** | Ícono ⚙ del panel: modo oscuro y cerrar sesión |

### Reglas de seguridad (en `AdminViewModel`)

- No puede eliminar **su propia cuenta**.
- Siempre debe quedar **al menos un administrador**.
- Eliminar a un dueño borra también **sus mascotas y sus citas** (se avisa en el diálogo, con la cantidad).
- Eliminar una mascota borra también **su historial de citas**.
- Los borrados son una **transacción** (`db.withTransaction`): citas → mascotas → usuario, en ese
  orden, para respetar las llaves foráneas. Si algo falla, no se borra nada a medias.
- Al editar un usuario, dejar la contraseña vacía **conserva** la anterior.

### Archivos del administrador

| Capa | Archivo | Qué hace |
|---|---|---|
| Modelo | [`model/Rol.kt`](app/src/main/java/com/tecsup/huellitasvet/model/Rol.kt) | Nuevo valor `ADMINISTRADOR("Admin")` |
| DAO | [`data/local/dao/UsuarioDao.kt`](app/src/main/java/com/tecsup/huellitasvet/data/local/dao/UsuarioDao.kt) | `observarPorRol`, `contarPorRol`, `existeCorreoDeOtro`, `actualizar`, `eliminar` |
| DAO | [`data/local/dao/MascotaDao.kt`](app/src/main/java/com/tecsup/huellitasvet/data/local/dao/MascotaDao.kt) | `eliminarDeDueno`, `contarTodas` |
| DAO | [`data/local/dao/CitaDao.kt`](app/src/main/java/com/tecsup/huellitasvet/data/local/dao/CitaDao.kt) | `eliminarDeMascota`, `eliminarDeDueno`, `contarTodas` |
| Repository | [`data/repository/VetRepository.kt`](app/src/main/java/com/tecsup/huellitasvet/data/repository/VetRepository.kt) | 8 funciones nuevas declaradas primero en la **interface** |
| Repository | [`data/repository/VetRepositoryRoom.kt`](app/src/main/java/com/tecsup/huellitasvet/data/repository/VetRepositoryRoom.kt) | Implementación, con las transacciones de borrado en cascada |
| ViewModel | [`viewmodel/AdminViewModel.kt`](app/src/main/java/com/tecsup/huellitasvet/viewmodel/AdminViewModel.kt) (nuevo) | Resumen, usuarios por rol + búsqueda, mascotas + búsqueda, guardar/eliminar con reglas |
| ViewModel | [`viewmodel/VetViewModelFactory.kt`](app/src/main/java/com/tecsup/huellitasvet/viewmodel/VetViewModelFactory.kt) | Crea el `AdminViewModel` |
| Vista | [`ui/admin/AdminPanelScreen.kt`](app/src/main/java/com/tecsup/huellitasvet/ui/admin/AdminPanelScreen.kt) (nuevo) | Panel |
| Vista | [`ui/admin/AdminUsuariosScreen.kt`](app/src/main/java/com/tecsup/huellitasvet/ui/admin/AdminUsuariosScreen.kt) (nuevo) | Lista de usuarios + diálogo de eliminar + Snackbar |
| Vista | [`ui/admin/AdminFormUsuarioScreen.kt`](app/src/main/java/com/tecsup/huellitasvet/ui/admin/AdminFormUsuarioScreen.kt) (nuevo) | Formulario con combobox de rol y distrito |
| Vista | [`ui/admin/AdminMascotasScreen.kt`](app/src/main/java/com/tecsup/huellitasvet/ui/admin/AdminMascotasScreen.kt) (nuevo) | Lista de mascotas + eliminar |
| Vista | [`ui/admin/AdminComponentes.kt`](app/src/main/java/com/tecsup/huellitasvet/ui/admin/AdminComponentes.kt) (nuevo) | `colorDeRol`, `AvatarIniciales`, `DialogoEliminar` (+ bloque GUÍA) |
| Vista | [`ui/dueno/FormularioMascotaScreen.kt`](app/src/main/java/com/tecsup/huellitasvet/ui/dueno/FormularioMascotaScreen.kt) | Parámetro opcional `duenos`: si llega, muestra el combobox "Dueño" |
| Vista | [`ui/dueno/LoginScreen.kt`](app/src/main/java/com/tecsup/huellitasvet/ui/dueno/LoginScreen.kt) | Tercera pestaña "Admin" con su ícono y texto |
| Vista | [`ui/veterinario/AjustesScreen.kt`](app/src/main/java/com/tecsup/huellitasvet/ui/veterinario/AjustesScreen.kt) | `esAdministrador = true` oculta el "modo práctica" |
| Navegación | [`ui/navigation/Rutas.kt`](app/src/main/java/com/tecsup/huellitasvet/ui/navigation/Rutas.kt) | 11 rutas `admin_*` |
| Navegación | [`ui/navigation/BarraNavegacion.kt`](app/src/main/java/com/tecsup/huellitasvet/ui/navigation/BarraNavegacion.kt) | `itemsAdmin`: Panel, Usuarios, Mascotas, Servicios, Agenda |
| Navegación | [`ui/navigation/NavVet.kt`](app/src/main/java/com/tecsup/huellitasvet/ui/navigation/NavVet.kt) | Grafo `grafo_admin`; `irAlInicio(ADMINISTRADOR)`; `adminVm()` comparte un solo `AdminViewModel` |
| Tema | [`ui/theme/Theme.kt`](app/src/main/java/com/tecsup/huellitasvet/ui/theme/Theme.kt) / [`Color.kt`](app/src/main/java/com/tecsup/huellitasvet/ui/theme/Color.kt) | Esquema cálido del administrador |
| Datos | [`data/local/DatosIniciales.kt`](app/src/main/java/com/tecsup/huellitasvet/data/local/DatosIniciales.kt) | Usuario `admin@huellitas.test` y un segundo veterinario |

### ¿Cómo se conecta todo? (de la pantalla a Room)

```
AdminUsuariosScreen ──(toca eliminar)──> DialogoEliminar ──(confirma)──> AdminViewModel.eliminarUsuario()
        ▲                                                                         │
        │ StateFlow usuarios / mensaje                                            ▼
        │                                                         VetRepository (interface)
        │                                                                         │
        └─────── Room emite la lista nueva (Flow) ◄── VetRepositoryRoom.eliminarUsuarioConTodo()
                                                       withTransaction { citas → mascotas → usuario }
```

Como las listas vienen de consultas Room con `Flow`, la pantalla se actualiza **sola** después de
crear o borrar. No hace falta recargar nada.

---

## 5. Otros detalles

- `VetCombo` es **un solo componente** para todos los combobox: distrito (registro y admin), servicio
  (pedir cita), rol y dueño (admin).
- Las validaciones de usuario están en **un solo lugar** ([`model/Validaciones.kt`](app/src/main/java/com/tecsup/huellitasvet/model/Validaciones.kt)) y las usan el
  registro y el administrador.
- El gráfico "Citas por servicio" del veterinario ahora usa el color del tipo (antes era un solo azul).

## 6. Capturas ([`capturas/fase2`](capturas/fase2))

| Archivo | Muestra |
|---|---|
| [`14_registro_validaciones.png`](capturas/fase2/14_registro_validaciones.png) | Errores de celular, distrito y contraseña |
| [`15_inicio_tipos.png`](capturas/fase2/15_inicio_tipos.png) | Tipos de servicio en Inicio |
| [`16_pedir_cita_combo.png`](capturas/fase2/16_pedir_cita_combo.png) | Combobox con los servicios de Estética |
| [`17_pedir_cita_detalle.png`](capturas/fase2/17_pedir_cita_detalle.png) | Servicio elegido con su detalle |
| [`18_cita_creada.png`](capturas/fase2/18_cita_creada.png) | La cita en Mis citas |
| [`19_panel_vet_tipos.png`](capturas/fase2/19_panel_vet_tipos.png) | Barras por tipo en el panel del veterinario |
| [`20_servicios_tipo.png`](capturas/fase2/20_servicios_tipo.png) | Filtros y etiquetas de tipo |
| [`21_admin_panel.png`](capturas/fase2/21_admin_panel.png) | Panel del administrador |
| [`22_admin_usuarios.png`](capturas/fase2/22_admin_usuarios.png) | Lista de dueños |
| [`23_admin_vet_creado.png`](capturas/fase2/23_admin_vet_creado.png) | Veterinario creado (Snackbar) |
| [`24_admin_eliminar.png`](capturas/fase2/24_admin_eliminar.png) | Confirmación de borrado en cascada |
| [`25_admin_mascotas.png`](capturas/fase2/25_admin_mascotas.png) | Mascotas después de eliminar a Luis |
| [`26_admin_mascota_dueno.png`](capturas/fase2/26_admin_mascota_dueno.png) | Combobox de dueño al crear una mascota |
