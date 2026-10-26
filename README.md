# SerenityBDD + Screenplay + Cucumber 8

![Java](https://img.shields.io/badge/Java-17-blue)
![Serenity BDD](https://img.shields.io/badge/Serenity%20BDD-5.4.1-green)
![Cucumber](https://img.shields.io/badge/Cucumber-8.0.4-brightgreen)
![Gradle](https://img.shields.io/badge/Gradle-9.6.1-darkgreen)
[![License](https://img.shields.io/badge/License-MIT-yellow)](LICENSE)

Proyecto base para automatización de pruebas web con Serenity BDD, el patrón Screenplay y Cucumber 8 sobre Gradle.

> [!NOTE]
> Los nombres de archivo y de clase conservan el sufijo `Cucumber4` (`PruebaCucumber4`, `prueba_cucumber4.feature`) porque se mantuvieron al migrar de Cucumber 2 a 4 y de 4 a 8. Son nombres históricos, no la versión en uso.

## Tabla de contenido

- [Qué demuestra](#qué-demuestra)
- [Stack](#stack)
- [Requisitos](#requisitos)
- [Cómo se ejecuta](#cómo-se-ejecuta)
- [Estructura](#estructura)
- [Uso](#uso)
- [Pruebas](#pruebas)
- [Configuración](#configuración)
- [Patrón y arquitectura](#patrón-y-arquitectura)
- [Migración de Cucumber 4 a Cucumber 8](#migración-de-cucumber-4-a-cucumber-8)
- [Estado y limitaciones](#estado-y-limitaciones)
- [Licencia](#licencia)

## Qué demuestra

- Integración de Serenity BDD con Cucumber 8 mediante el runner `CucumberWithSerenity`.
- Uso de `OnStage` y `OnlineCast` para preparar el escenario y de `remember` / `recall` para guardar datos entre pasos.
- Expresiones de parámetros de Cucumber 8 en las definiciones de pasos: `{string}`, `{int}` y `{double}`.
- Un esqueleto de paquetes Screenplay (`task`, `interactions`, `userinterfaces`, `questions`, `models`, `exceptions`, `utils`) listo para rellenar.

> [!WARNING]
> El escenario incluido no abre ningún navegador ni comprueba nada: solo imprime mensajes por consola. Una ejecución en verde no valida el comportamiento de la aplicación. Ver [Pruebas](#pruebas) y [Estado y limitaciones](#estado-y-limitaciones).

## Stack

| Tecnología | Versión | Origen |
|---|---|---|
| Java (JDK) | 17 | `build.gradle` (`java.toolchain`, `JavaLanguageVersion.of(17)`) |
| Gradle (wrapper) | 9.6.1 | `gradle/wrapper/gradle-wrapper.properties` |
| Serenity BDD (`serenity-core`, `serenity-junit`) | 5.4.1 | `build.gradle` (`serenityVersion`) |
| Serenity Screenplay (`serenity-screenplay`, `serenity-screenplay-webdriver`) | 5.4.1 | `build.gradle` |
| Serenity Cucumber (`serenity-cucumber`) | 5.4.1 | `build.gradle` |
| Cucumber JVM (`cucumber-core`, `cucumber-java`, `cucumber-junit`) | 8.0.4 | Transitiva de `serenity-cucumber:5.4.1`; no se declara versión |
| Plugin de Gradle de Serenity (`serenity-gradle-plugin`) | 2.4.34 | `build.gradle` (`serenityGradlePluginVersion`) |
| JUnit | 4.13.2 | `build.gradle` |
| AssertJ | 3.27.7 | `build.gradle` (declarada, sin uso en el código actual) |
| SLF4J Simple | 2.0.20 | `build.gradle` |

## Requisitos

- **JDK 17**. El `build.gradle` fija un toolchain de Java 17: compila y ejecuta los tests con un JDK 17 aunque Gradle arranque con otra versión. Ese JDK 17 debe estar instalado y ser detectable por Gradle. Gradle 9.6.1, a su vez, necesita JDK 17 o superior para arrancar; no funciona sobre JDK 11.
- **Gradle**: no hace falta instalarlo. El repositorio incluye el wrapper (`gradlew`, `gradlew.bat`, `gradle/wrapper/`).
- **Driver de navegador**: el repositorio incluye `chromedriver.exe` y `geckodriver.exe` en la raíz.
- **Chrome** instalado, porque `serenity.properties` fija `webdriver.driver=chrome` (ver [Configuración](#configuración)).

> [!WARNING]
> Los dos drivers versionados son ejecutables de **Windows**. En macOS o Linux hay que aportar el binario equivalente para la plataforma y la versión de Chrome o Firefox instalada. Las versiones de los drivers del repositorio no se pueden verificar sin ejecutarlos. [por confirmar: versión de cada driver]

## Cómo se ejecuta

Situado en la raíz del repositorio:

```sh
# Compila y ejecuta la suite Cucumber (macOS / Linux)
./gradlew clean build -i
```

```bat
:: Equivalente en Windows
gradlew.bat clean build -i
```

`build` incluye la tarea `test`, así que no hace falta invocarlas por separado. La opción `-i` sube el nivel de log a `info` y deja ver los `System.out.println` que imprimen los pasos.

`build.gradle` activa `gradle.startParameter.continueOnFailure = true`, de modo que Gradle no se detiene en el primer fallo de tarea.

Para ejecutar solo las pruebas:

```sh
./gradlew test
```

La ejecución deja los resultados en `target/site/serenity/`. El informe HTML agregado se construye con:

```sh
./gradlew aggregate
```

y queda en `target/site/serenity/index.html`. `target/` está en `.gitignore`, así que el informe no se versiona.

## Estructura

```text
.
├── build.gradle                  # dependencias, toolchain 17, plugin de Serenity
├── settings.gradle               # rootProject.name = 'automatizacion'
├── serenity.properties           # navegador y opciones de arranque de Chrome
├── gradlew / gradlew.bat         # wrapper de Gradle 9.6.1
├── gradle/wrapper/               # jar y properties del wrapper
├── chromedriver.exe              # driver de Chrome para Windows
├── geckodriver.exe               # driver de Firefox para Windows
├── LICENSE                       # MIT
└── src
    ├── main/java/com/cedaniel200
    │   ├── package-info.java      # nota para renombrar el paquete base
    │   ├── exceptions/            # excepciones técnicas y de negocio
    │   ├── interactions/          # interacciones directas con la UI
    │   ├── models/                # modelo de dominio y builders
    │   ├── questions/             # consultas sobre el estado de la aplicación
    │   ├── task/                  # tareas de negocio del actor
    │   ├── userinterfaces/        # page objects y page elements
    │   └── utils/                 # utilidades
    └── test
        ├── java/com/cedaniel200
        │   ├── runners/PruebaCucumber4.java              # runner Cucumber + Serenity
        │   └── stepdefinitions/PruebaCucumber4StepDefinitions.java
        └── resources/features/prueba_cucumber4.feature   # único feature del proyecto
```

Los paquetes de `src/main/java` contienen únicamente un `package-info.java` con la descripción del paquete: son andamiaje, no hay clases de Screenplay implementadas. El código ejecutable está todo en `src/test`.

## Uso

| Tarea | Comando | Qué hace |
|---|---|---|
| Compilar y probar | `./gradlew clean build` | Compila y ejecuta los tests de Cucumber |
| Solo pruebas | `./gradlew test` | Ejecuta la suite sin empaquetar |
| Con detalle en consola | `./gradlew clean build -i` | Igual que `build` con log `info` |
| Cambiar de navegador | `./gradlew test -Dwebdriver.driver=<navegador>` | Sobrescribe `serenity.properties` |
| Informe agregado | `./gradlew aggregate` | Genera el informe HTML en `target/site/serenity/index.html` |

Las propiedades `-D` de la línea de comandos llegan al JVM de los tests porque `build.gradle` asigna `systemProperties = gradle.startParameter.systemPropertiesArgs` a todas las tareas `Test`. No se copia el resto de propiedades del sistema: hacerlo arrastraba el `java.home` del daemon al JVM del toolchain.

Para convertir el proyecto en la base de un proyecto real:

1. Renombra el paquete base `com.cedaniel200` por el de tu organización (el propio `package-info.java` lo sugiere).
2. Crea las clases de `task`, `interactions`, `userinterfaces` y `questions` en `src/main/java`.
3. Añade features en `src/test/resources/features` y actualiza el atributo `features` del runner.
4. Amplía el `glue` del runner si las definiciones de pasos viven en otro paquete.

## Pruebas

Hay **un** feature y **un** escenario:

| Feature | Escenario | Pasos |
|---|---|---|
| `prueba_cucumber4.feature` | `Crear Repositorio Exitosamente` | `Given "Andres" quiere sumar`, `When Andres suma 2 mas 3.4`, `Then Andres deberia ver que el resultado es 5.4` |

Lo que la suite ejecuta realmente:

- `prepararEscenario()` monta el escenario con `OnStage.setTheStage(new OnlineCast())` antes de cada escenario.
- El `Given` registra al actor con `theActorCalled(nombreActor)`.
- El `When` suma los dos parámetros en Java y guarda el resultado con `remember`.
- El `Then` recupera el valor con `recall`, calcula un `boolean` y lo imprime.

Lo que **no** hace:

- No hay aserciones: el `boolean` del `Then` solo se imprime, así que el escenario pasa aunque el resultado sea incorrecto.
- No se abre ninguna URL ni se interactúa con la interfaz: no se usan `Task`, `Interaction`, `Question` ni `PageObject`, pese a tener las dependencias de Screenplay WebDriver.
- La comparación del resultado se hace con `==` sobre `double`.

> [!NOTE]
> El runner fija `features = {"src/test/resources/features/prueba_cucumber4.feature"}`. Un `.feature` nuevo no se ejecutará hasta que se añada su ruta (o un directorio) a ese atributo.

No hay tests unitarios, ni pipeline de CI, ni tareas de análisis estático en el repositorio.

## Configuración

`serenity.properties` en la raíz:

| Propiedad | Valor | Efecto |
|---|---|---|
| `webdriver.driver` | `chrome` | Navegador que usa Serenity por defecto |
| `chrome.switches` | `--start-maximized` | Argumento de arranque de Chrome |

No hay variables de entorno requeridas ni ficheros de credenciales en el repositorio.

Si el proyecto evoluciona hacia pruebas contra un servicio real, las credenciales deberían llegar por variables de entorno o propiedades `-D`, nunca por `serenity.properties` versionado. Marcadores previstos: `<user>`, `<password>`, `<base-url>`, `<api-key>`.

## Patrón y arquitectura

Screenplay con el reparto de responsabilidades habitual:

| Paquete | Responsabilidad |
|---|---|
| `task` | Tareas de negocio que ejecuta el actor (`task` en singular es el nombre real del paquete) |
| `interactions` | Acciones directas sobre la interfaz |
| `userinterfaces` | Page objects y page elements |
| `questions` | Consultas sobre el estado de la aplicación |
| `models` | Modelo de dominio y builders |
| `exceptions` | Excepciones técnicas y de negocio |
| `utils` | Utilidades |

El `build.gradle` coloca `serenity-screenplay` y `serenity-screenplay-webdriver` en la configuración `implementation`, porque el código de Screenplay vive en `src/main/java`. Las dependencias de ejecución de pruebas (`serenity-core`, `serenity-junit`, `serenity-cucumber`, JUnit, AssertJ) están en `testImplementation`.

La ejecución de tests se paraleliza con `maxParallelForks = Runtime.runtime.availableProcessors()`.

## Migración de Cucumber 4 a Cucumber 8

Cambios aplicados en este orden:

1. **Wrapper**: Gradle 5.2.1 → 9.6.1. Gradle 9 ya no expone `jcenter()`, así que se eliminó de los bloques `repositories`, junto con `mavenLocal()`.
2. **Dependencias**: Serenity 2.0.43 → 5.4.1 en todos los artefactos. `serenity-cucumber4:1.0.5` desaparece; su sustituto es `serenity-cucumber:5.4.1`. Se elimina el `resolutionStrategy.force` de `cucumber-core`: `serenity-cucumber` aporta Cucumber 8.0.4 (`cucumber-core`, `cucumber-java` y `cucumber-junit`) como transitivas, sin fijar versión.
3. **Java**: toolchain 17. Serenity 5.4.1 se publica para Java 17 y Gradle 9 necesita JDK 17 o superior para arrancar.
4. **Código de test**: `cucumber.api.*` dejó de existir en Cucumber 5. Los imports pasan a `io.cucumber.junit.CucumberOptions`, `io.cucumber.junit.CucumberOptions.SnippetType` e `io.cucumber.java.*`. La lógica de los pasos no cambia.
5. **Resto de versiones**: JUnit 4.13.2, AssertJ 3.27.7, SLF4J Simple 2.0.20 y `serenity-gradle-plugin` 2.4.34, la última publicada.

Configuración actual de `build.gradle`:

```groovy
ext {
    serenityVersion = '5.4.1'
    serenityGradlePluginVersion = '2.4.34'
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

dependencies {
    testImplementation "net.serenity-bdd:serenity-core:$rootProject.ext.serenityVersion"
    testImplementation "net.serenity-bdd:serenity-junit:$rootProject.ext.serenityVersion"
    implementation "net.serenity-bdd:serenity-screenplay:$rootProject.ext.serenityVersion"
    implementation "net.serenity-bdd:serenity-screenplay-webdriver:$rootProject.ext.serenityVersion"

    testImplementation "net.serenity-bdd:serenity-cucumber:$rootProject.ext.serenityVersion"

    testImplementation 'junit:junit:4.13.2'
    testImplementation 'org.assertj:assertj-core:3.27.7'
    testImplementation 'org.slf4j:slf4j-simple:2.0.20'
}
```

Con Cucumber 8 los imports de las definiciones de pasos son `io.cucumber.java.*` y las opciones del runner viven en `io.cucumber.junit.CucumberOptions`, con `SnippetType` como enum anidado.

El runner sigue siendo `net.serenitybdd.cucumber.CucumberWithSerenity` sobre JUnit 4. El compilador lo marca como deprecado para eliminación, igual que `io.cucumber.junit.CucumberOptions`: Serenity empuja hacia JUnit Platform, pero cambiar el runner queda fuera de este cambio.

## Estado y limitaciones

Puntos detectados en el código y en la ejecución actual:

| Punto | Detalle |
|---|---|
| Sin aserciones | El escenario no puede fallar por un resultado incorrecto. Es una demo de integración, no una prueba de negocio. |
| Sin uso de WebDriver | `serenity.properties` y los drivers están configurados, pero ningún paso abre un navegador. |
| Drivers solo para Windows | `chromedriver.exe` y `geckodriver.exe` no sirven en macOS ni Linux. [por confirmar: versión de los drivers] |
| Runner JUnit 4 deprecado | `CucumberWithSerenity` y `io.cucumber.junit.CucumberOptions` están marcados como deprecados para eliminación en Serenity 5.4.1 / Cucumber 8. |
| Nombres con sufijo `Cucumber4` | Las clases y el feature conservan el nombre de la migración anterior; la versión real es Cucumber 8. |
| Escenario mal nombrado | El título `Crear Repositorio Exitosamente` no tiene relación con la feature `Sumar`: parece un resto de copia. |
| Actor fijado en el `When` | El paso `Andres suma {int} mas {double}` fija el nombre del actor, mientras el `Given` lo recibe por parámetro. |
| Paquetes vacíos | `task`, `interactions`, `questions`, etc. solo contienen `package-info.java`. |

## Licencia

MIT. Ver [LICENSE](LICENSE).
