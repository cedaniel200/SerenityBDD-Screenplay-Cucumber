package com.cedaniel200.stepdefinitions;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;

import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

public class PruebaCucumber4StepDefinitions {

    public static final String OPERACION = "OPERACION";

    @Before
    public void prepararEscenario(){
        OnStage.setTheStage(new OnlineCast());
    }

    @Given("{string} quiere sumar")
    public void abrirCalculadora(String nombreActor) {
        theActorCalled(nombreActor);
        System.out.println(nombreActor + " Abre la calculadora");
    }

    @When("Andres suma {int} mas {double}")
    public void sumar(Integer numeroUno, Double numeroDos) {
        double resultado = numeroUno + numeroDos;
        theActorInTheSpotlight().remember(OPERACION, resultado);
        System.out.println("Sumar " + numeroUno + " + " + numeroDos);
    }

    @Then("Andres deberia ver que el resultado es {double}")
    public void verificarResultado(Double resultado) {
        double resultadoOperacion = theActorInTheSpotlight().recall(OPERACION);
        boolean esCorrecto = resultadoOperacion == resultado;
        System.out.println("Resultado debe ser " + resultado);
        System.out.println("Resultado es " + esCorrecto);
    }

}
