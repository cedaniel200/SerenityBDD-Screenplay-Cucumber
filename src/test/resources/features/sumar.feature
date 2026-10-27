Feature: Sumar
  Yo como usuario quiero sumar para conocer el resultado de la operación

  Scenario: Crear Repositorio Exitosamente
    Given "Andres" quiere sumar
    When Andres suma 2 mas 3.4
    Then Andres deberia ver que el resultado es 5.4