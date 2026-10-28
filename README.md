# SerenityBDD + Screenplay + Cucumber

![Java](https://img.shields.io/badge/Java-17-blue)
![Serenity BDD](https://img.shields.io/badge/Serenity%20BDD-5.4.1-green)
![Cucumber](https://img.shields.io/badge/Cucumber-8.0.4-brightgreen)
![Gradle](https://img.shields.io/badge/Gradle-9.8.2-darkgreen)
[![License](https://img.shields.io/badge/License-MIT-yellow)](LICENSE)

Plantilla para empezar un proyecto de automatización de pruebas con Serenity BDD, el patrón
Screenplay y Cucumber sobre Gradle. Estructura, runner e informe ya conectados.

Usa **Use this template** en GitHub para crear tu repositorio a partir de esta plantilla.

## Qué incluye

- **Screenplay** listo para llenar: `task`, `interactions`, `questions` y `userinterfaces`.
- **Cucumber 8 sobre JUnit Platform**: una suite descubre los `.feature` por directorio.
- **Informe HTML de Serenity** generado desde el plugin de Cucumber.
- **Gradle 9.8.2** con wrapper y toolchain de Java 17.
- **Docker** para ejecutar la suite sin instalar nada.

## Requisitos

- **JDK 17**. Gradle 9 necesita JDK 17 o superior; no funciona sobre JDK 11.
- **Gradle**: no hace falta instalarlo, el wrapper va incluido.
- **Un navegador instalado**, solo si vas a automatizar web: Chrome, Firefox, Edge o Safari. El driver lo resuelve Selenium Manager.

## Empezar

1. Crea tu repositorio con **Use this template**.
2. Reemplaza los dos marcadores del paquete base `co.com.yourcompany.certification.nameproject`: `yourcompany` por tu organización y `nameproject` por tu proyecto. Ajusta también el `group` de `build.gradle`.
3. Escribe tus `.feature` en `src/test/resources/features`.
4. Implementa los pasos en `stepdefinitions` y el código Screenplay en `src/main`.
5. Ejecuta `./gradlew test` y abre el informe.

El escenario incluido es una demo del andamiaje: borra cuando escribas el tuyo.

## Estructura

```text
src/main/java/co/com/yourcompany/certification/nameproject
├── tasks/            # qué hace el actor
├── interactions/     # interacción con la interfaz
├── userinterfaces/   # localizadores
├── questions/        # qué valida el actor
├── models/           # datos de prueba
├── exceptions/
└── utils/

src/test
├── java/co/com/yourcompany/certification/nameproject/runners/CucumberTestSuite.java
├── java/co/com/yourcompany/certification/nameproject/stepdefinitions/
└── resources/features/
```

Esta es la **estructura base**, no una lista cerrada. Añade los paquetes que tu proyecto
necesite: `builders`, `enumerables`, `validations` u otros, dentro del mismo paquete base.

Los paquetes de `src/main` vienen vacíos a propósito: son donde va tu código.

## Ejemplos construidos sobre esta plantilla

- [serenity-screenplay-saucedemo](https://github.com/cedaniel200/serenity-screenplay-saucedemo):
  flujo de compra web completo, con login, carrito y checkout.
- [serenity-screenplay-rest-soap](https://github.com/cedaniel200/serenity-screenplay-rest-soap):
  la misma estructura aplicada a APIs REST y SOAP.

## Ejecutar

```sh
./gradlew clean build -i   # compila y ejecuta con log detallado
./gradlew test             # solo las pruebas
./gradlew aggregate        # genera el informe HTML
```

El informe queda en `target/site/serenity/index.html`. `build` ya incluye `test`, pero no
regenera el HTML: para eso hace falta `aggregate`.

En Windows, `gradlew.bat`.

## Configuración

`serenity.properties` viene apuntado a Chrome, pero `webdriver.driver` acepta cualquiera de
los navegadores que soporta Serenity. El navegador es una línea de configuración, no una
dependencia de la plantilla:

| Valor | Navegador |
|---|---|
| `chrome` | Chrome o Chromium |
| `firefox` | Firefox |
| `edge` | Microsoft Edge |
| `safari` | Safari (macOS) |
| `remote` | Selenium Grid o navegador remoto |
| `appium` | Aplicaciones móviles |

```properties
webdriver.driver=chrome
chrome.switches=--start-maximized
```

`chrome.switches` solo aplica a Chrome. Para Firefox, las preferencias van en
`firefox.preferences.<clave>=<valor>`. Para un navegador remoto: `webdriver.driver=remote` y
`webdriver.remote.url=<url-del-grid>`.

## Docker

```sh
docker build -t <nombre> .
docker run --rm <nombre>
docker compose up --abort-on-container-exit   # informe en ./target/site/serenity
```

La imagen no trae navegador; apunta Serenity a un Selenium Grid cuando lo necesites. El
montaje es `./target`, no `./target/site/serenity`: la tarea `clearReports` borra ese
directorio y no puede eliminar un punto de montaje.

## Ten en cuenta

- **El ejemplo no abre navegador.** Comprueba que la suite y el informe funcionan, no es un
  ejemplo de negocio.
- **Un solo runner.** Si añades otra clase con `@Suite`, tendrás dos ejecuciones.
- **`serenity-gradle-plugin` 2.4.34** es la última publicada; funciona con Gradle 9.8.2 y no
  hay sustituto. Revísalo si Gradle cambia de major.

## Licencia

[MIT](LICENSE) © Cesar Daniel
