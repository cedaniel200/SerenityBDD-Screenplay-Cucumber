# SerenityBDD + Screenplay + Cucumber 4

![Java](https://img.shields.io/badge/Java-8-blue)
![Serenity BDD](https://img.shields.io/badge/Serenity%20BDD-2.0.43-green)
![Cucumber](https://img.shields.io/badge/Cucumber-4.2.0-brightgreen)
![Gradle](https://img.shields.io/badge/Gradle-5.2.1-darkgreen)
[![License](https://img.shields.io/badge/License-MIT-yellow)](LICENSE)

Proyecto base para automatización de pruebas web con Serenity BDD, el patrón Screenplay y Cucumber 4 sobre Gradle.

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
- [Migración de Cucumber 2 a Cucumber 4](#migración-de-cucumber-2-a-cucumber-4)
- [Estado y limitaciones](#estado-y-limitaciones)
- [Licencia](#licencia)

## Qué demuestra

- Integración de Serenity BDD con Cucumber 4 mediante el runner `CucumberWithSerenity`.
- Uso de `OnStage` y `OnlineCast` para preparar el escenario y de `remember` / `recall` para guardar datos entre pasos.
- Expresiones de parámetros de Cucumber 4 en las definiciones de pasos: `{string}`, `{int}` y `{double}`.
- Un esqueleto de paquetes Screenplay (`task`, `interactions`, `userinterfaces`, `questions`, `models`, `exceptions`, `utils`) listo para rellenar.

> [!WARNING]
> El escenario incluido no abre ningún navegador ni comprueba nada: solo imprime mensajes por consola. Una ejecución en verde no valida el comportamiento de la aplicación. Ver [Pruebas](#pruebas) y [Estado y limitaciones](#estado-y-limitaciones).

## Stack

| Tecnología | Versión | Origen |
|---|---|---|
| Java (JDK) | 8 | [por confirmar] `build.gradle` no declara `sourceCompatibility` |
| Gradle (wrapper) | 5.2.1 | `gradle/wrapper/gradle-wrapper.properties` |
| Serenity BDD (`serenity-core`, `serenity-junit`) | 2.0.43 | `build.gradle` (`serenityVersion`) |
| Serenity Screenplay (`serenity-screenplay`, `serenity-screenplay-webdriver`) | 2.0.43 | `build.gradle` |
| Serenity Cucumber 4 | 1.0.5 | `build.gradle` (`serenityCucumberVersion`) |
| Cucumber JVM (`cucumber-core`, `cucumber-junit`) | 4.2.0 | `build.gradle` (`cucumberVersion`) |
| JUnit | 4.12 | `build.gradle` |
| AssertJ | 3.9.1 | `build.gradle` (declarada, sin uso en el código actual) |
| SLF4J Simple | 1.7.7 | `build.gradle` |

## Requisitos

- **JDK 8**. El proyecto usa Gradle 5.2.1 y Serenity 2.0.43; `build.gradle` no fija una versión de Java, así que la compilación depende del JDK con el que se invoque Gradle. [por confirmar: versión mínima y máxima soportada]
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

[por confirmar: ruta del informe agregado de Serenity. El plugin `net.serenity-bdd.aggregator` está aplicado, pero la carpeta de salida no está declarada en el repositorio y `target/` y `build/` están en `.gitignore`.]

## Estructura

```text
.
├── build.gradle                  # dependencias, plugin de Serenity, versión de Cucumber forzada
├── settings.gradle               # rootProject.name = 'automatizacion'
├── serenity.properties           # navegador y opciones de arranque de Chrome
├── gradlew / gradlew.bat         # wrapper de Gradle 5.2.1
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
| Informe agregado | `./gradlew aggregate` | Tarea del plugin `net.serenity-bdd.aggregator` [por confirmar: nombre de la tarea] |

Las propiedades `-D` llegan al JVM de los tests porque `build.gradle` asigna `systemProperties = System.properties as Map<String, ?>` a todas las tareas `Test`.

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

El `build.gradle` coloca `serenity-screenplay` y `serenity-screenplay-webdriver` en la configuración `implementation`, porque el código de Screenplay vive en `src/main/java`. Las dependencias de ejecución de pruebas (`serenity-core`, `serenity-junit`, `serenity-cucumber4`, `cucumber-core`, `cucumber-junit`, JUnit, AssertJ) están en `testImplementation`.

La ejecución de tests se paraleliza con `maxParallelForks = Runtime.runtime.availableProcessors()`.

## Migración de Cucumber 2 a Cucumber 4

Pasos aplicados en este proyecto:

1. Forzar la versión **4.x** de `cucumber-core` con `resolutionStrategy`, para que no gane una versión 2.x arrastrada de forma transitiva.
2. Declarar `io.cucumber:cucumber-core` y `io.cucumber:cucumber-junit` en versión **4.x**.
3. Sustituir `net.serenity-bdd:serenity-cucumber` por `net.serenity-bdd:serenity-cucumber4`.
4. Usar el runner `net.serenitybdd.cucumber.CucumberWithSerenity` y las expresiones de parámetros (`{string}`, `{int}`, `{double}`) en lugar de las anotaciones con regex.

Configuración real de este repositorio (`build.gradle`):

```groovy
ext {
    serenityVersion = '2.0.43'
    serenityCucumberVersion = '1.0.5'
    cucumberVersion = '4.2.0'
}

configurations.all {
    resolutionStrategy {
        force "io.cucumber:cucumber-core:$rootProject.ext.cucumberVersion"
    }
}

dependencies {
    testImplementation "net.serenity-bdd:serenity-core:$rootProject.ext.serenityVersion"
    testImplementation "net.serenity-bdd:serenity-junit:$rootProject.ext.serenityVersion"
    implementation "net.serenity-bdd:serenity-screenplay:$rootProject.ext.serenityVersion"
    implementation "net.serenity-bdd:serenity-screenplay-webdriver:$rootProject.ext.serenityVersion"

    testImplementation "net.serenity-bdd:serenity-cucumber4:$rootProject.ext.serenityCucumberVersion"
    testImplementation "io.cucumber:cucumber-core:$rootProject.ext.cucumberVersion"
    testImplementation "io.cucumber:cucumber-junit:$rootProject.ext.cucumberVersion"
}
```

Con Cucumber 4 los imports de las definiciones de pasos son `cucumber.api.java.*` y `cucumber.api.*`, como en `PruebaCucumber4StepDefinitions.java` y en el runner.

Anuncio original de la versión: <https://cucumber.io/blog/open-source/announcing-cucumber-jvm-4-2-0/>

## Estado y limitaciones

Puntos detectados al leer el código, no verificados ejecutando la suite:

| Punto | Detalle |
|---|---|
| Sin aserciones | El escenario no puede fallar por un resultado incorrecto. Es una demo de integración, no una prueba de negocio. |
| Sin uso de WebDriver | `serenity.properties` y los drivers están configurados, pero ningún paso abre un navegador. |
| Drivers solo para Windows | `chromedriver.exe` y `geckodriver.exe` no sirven en macOS ni Linux. [por confirmar: versión de los drivers] |
| `jcenter()` en `build.gradle` | JCenter está descontinuado. Si la resolución de dependencias falla, es el primer punto a revisar. [por confirmar: el build resuelve con los repositorios actuales] |
| Versión de Java no declarada | Sin `sourceCompatibility` / `targetCompatibility`, el resultado depende del JDK usado. |
| Escenario mal nombrado | El título `Crear Repositorio Exitosamente` no tiene relación con la feature `Sumar`: parece un resto de copia. |
| Actor fijado en el `When` | El paso `Andres suma {int} mas {double}` fija el nombre del actor, mientras el `Given` lo recibe por parámetro. |
| Paquetes vacíos | `task`, `interactions`, `questions`, etc. solo contienen `package-info.java`. |

## Licencia

MIT. Ver [LICENSE](LICENSE).
