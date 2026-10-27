# SerenityBDD + Screenplay + Cucumber

![Java](https://img.shields.io/badge/Java-17-blue)
![Serenity BDD](https://img.shields.io/badge/Serenity%20BDD-5.4.1-green)
![Cucumber](https://img.shields.io/badge/Cucumber-8.0.4-brightgreen)
![Gradle](https://img.shields.io/badge/Gradle-9.8.2-darkgreen)
[![License](https://img.shields.io/badge/License-MIT-yellow)](LICENSE)

Plantilla para empezar un proyecto de automatización de pruebas con Serenity BDD, el patrón Screenplay y Cucumber sobre Gradle. Trae la estructura, el runner y el informe ya conectados: se clona, se renombra el paquete base y se empiezan a escribir escenarios.

## Tabla de contenido

- [Qué incluye](#qué-incluye)
- [Requisitos](#requisitos)
- [Cómo empezar](#cómo-empezar)
- [Estructura](#estructura)
- [Cómo se ejecuta](#cómo-se-ejecuta)
- [Escribir tus pruebas](#escribir-tus-pruebas)
- [Configuración](#configuración)
- [Informe de resultados](#informe-de-resultados)
- [Docker](#docker)
- [Ten en cuenta](#ten-en-cuenta)
- [Licencia](#licencia)

## Qué incluye

- **Serenity BDD 5.4.1** con el patrón **Screenplay**: paquetes `task`, `interactions`, `questions` y `userinterfaces` listos para llenar.
- **Cucumber 8** ejecutado sobre **JUnit Platform**: una suite (`CucumberTestSuite`) que descubre los `.feature` de `src/test/resources/features`.
- **Informe HTML de Serenity** generado desde el plugin de Cucumber, sin runner propio.
- **Gradle 9.8.2** con wrapper incluido y toolchain de Java 17.
- **Docker** para ejecutar la suite sin instalar nada.

## Requisitos

- **JDK 17**. El build fija un toolchain de Java 17, así que compila y ejecuta con ese JDK aunque Gradle arranque con otra versión. Gradle 9 necesita JDK 17 o superior; no funciona sobre JDK 11.
- **Gradle**: no hace falta instalarlo. El repositorio incluye el wrapper (`gradlew`, `gradlew.bat`).
- **Chrome** instalado, solo si vas a automatizar un navegador. `serenity.properties` apunta a Chrome por defecto.
- **Driver del navegador**: no hay que descargarlo ni versionarlo. Selenium Manager, incluido en Selenium 4, detecta el navegador y resuelve el driver en la primera ejecución (necesita conexión esa vez).

## Cómo empezar

1. Clona el repositorio y renómbralo.
2. Cambia el paquete base `com.cedaniel200` por el tuyo (el `package-info.java` de la raíz lo recuerda). Ajusta también el `group` en `build.gradle`.
3. Escribe tu primer `.feature` en `src/test/resources/features`.
4. Implementa los pasos en `src/test/java/com/cedaniel200/stepdefinitions`.
5. Añade tareas, interacciones y preguntas de Screenplay en `src/main/java/com/cedaniel200`.
6. Ejecuta `./gradlew test` y abre el informe.

El escenario que viene incluido es una demostración mínima del andamiaje: comprueba que la suite, los pasos y el informe funcionan de punta a punta. No es un ejemplo de negocio; bórralo cuando escribas el tuyo.

## Estructura

```text
.
├── build.gradle              # dependencias, toolchain 17, plugin de Serenity
├── settings.gradle           # nombre del proyecto Gradle
├── serenity.properties       # navegador y opciones de arranque
├── Dockerfile                # imagen para ejecutar la suite
├── docker-compose.yml        # ejecución con el informe montado en el host
├── gradlew / gradlew.bat     # wrapper de Gradle 9.8.2
└── src
    ├── main/java/com/cedaniel200
    │   ├── task/             # tareas: el "qué hace" el actor
    │   ├── interactions/     # interacciones con la interfaz
    │   ├── userinterfaces/   # localizadores de elementos
    │   ├── questions/        # preguntas: el "qué valida" el actor
    │   ├── models/           # datos de prueba
    │   ├── exceptions/       # excepciones propias
    │   └── utils/            # utilidades
    └── test
        ├── java/com/cedaniel200
        │   ├── runners/CucumberTestSuite.java        # entrada de la suite
        │   └── stepdefinitions/                      # definiciones de pasos
        └── resources/features/                       # escenarios .feature
```

Los paquetes de `src/main` vienen vacíos, solo con su `package-info.java`: son los sitios donde va tu código Screenplay, no clases a medio hacer.

## Cómo se ejecuta

Desde la raíz del proyecto:

```sh
# Compila y ejecuta las pruebas con log detallado
./gradlew clean build -i

# Solo las pruebas
./gradlew test

# Genera el informe HTML a partir de los resultados
./gradlew aggregate
```

En Windows, `gradlew.bat` en lugar de `./gradlew`.

`build` incluye `test`, así que no hace falta invocarlas por separado. La opción `-i` deja ver los `System.out.println` de los pasos.

## Escribir tus pruebas

Un escenario:

```gherkin
Feature: Iniciar sesión

  Scenario: Credenciales válidas
    Given "Ana" está en la página de login
    When Ana inicia sesión con credenciales válidas
    Then Ana debería ver el panel principal
```

Y sus pasos:

```java
package com.cedaniel200.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class LoginStepDefinitions {

    @Given("{word} está en la página de login")
    public void estaEnLaPaginaDeLogin(String actor) {
        // abre el navegador y navega
    }

    @When("{word} inicia sesión con credenciales válidas")
    public void iniciaSesion(String actor) {
        // ejecuta la tarea de Screenplay
    }

    @Then("{word} debería ver el panel principal")
    public void deberiaVerElPanel(String actor) {
        // plantea una Question y la valida con aserción
    }
}
```

Dos reglas que ahorran tiempo:

- **Los pasos no contienen lógica de negocio.** Delegan en tareas y preguntas de Screenplay.
- **Toda validación termina en una aserción.** Un `Then` que imprime un booleano no prueba nada.

La suite descubre los `.feature` por directorio: añadir un archivo nuevo no requiere tocar código. Si tus pasos viven en otro paquete, añade el paquete al parámetro `cucumber.glue` de `CucumberTestSuite`.

## Configuración

`serenity.properties`:

| Propiedad | Valor | Para qué |
|---|---|---|
| `webdriver.driver` | `chrome` | Navegador que usa Serenity |
| `chrome.switches` | `--start-maximized` | Argumentos de arranque de Chrome |

Otras opciones habituales:

```properties
webdriver.base.url=<url-de-tu-aplicacion>
webdriver.timeouts.implicitlywait=<milisegundos>

# Selenium Grid o navegador remoto
webdriver.driver=remote
webdriver.remote.url=<url-del-grid>
```

## Informe de resultados

`./gradlew test` deja los resultados en `target/site/serenity/` y `./gradlew aggregate` construye el HTML en `target/site/serenity/index.html`.

El informe incluye requisitos, escenarios, pasos y capturas. No se versiona: `target/` está fuera de git.

## Docker

```sh
# Construir la imagen
docker build -t serenitybddscreenplaycucumber4 .

# Ejecutar las pruebas dentro del contenedor
docker run --rm serenitybddscreenplaycucumber4

# Con Compose: el informe queda en ./target/site/serenity
docker compose up --abort-on-container-exit
```

| Aspecto | Valor |
|---|---|
| Imagen base | `amazoncorretto:17` (JDK 17, arm64 y amd64) |
| Comando | `./gradlew clean test aggregate --no-daemon` |
| Paquetes extra | `findutils`: el wrapper de Gradle 9 invoca `xargs` |
| Navegador | no incluido |

> [!NOTE]
> La imagen no trae navegador. Cuando tus escenarios lo necesiten, apunta Serenity a un Selenium Grid (`webdriver.driver=remote`, `webdriver.remote.url=<url>`) o añade Chrome a la imagen. El montaje es `./target:/app/target` y no `./target/site/serenity`: la tarea `clearReports` borra ese directorio y no puede eliminar un punto de montaje.

## Ten en cuenta

- **El ejemplo no usa navegador.** El escenario incluido suma dos números: sirve para comprobar la suite y el informe, no para ver Screenplay en acción. Los drivers y `serenity.properties` están preparados para cuando escribas escenarios web.
- **La suite es la entrada.** Si añades otro runner o una clase con `@Suite`, tendrás dos ejecuciones. Mantén una sola.
- **`serenity-gradle-plugin` es 2.4.34**, la última publicada. Funciona con Gradle 9.8.2 y no hay sustituto; si Gradle cambia de major, revísalo.
- **El informe necesita `aggregate`.** `build` ejecuta las pruebas pero no regenera el HTML.

## Licencia

[MIT](LICENSE) © Cesar Daniel
