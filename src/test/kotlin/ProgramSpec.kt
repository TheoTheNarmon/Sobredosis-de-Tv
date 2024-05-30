package ar.edu.unsam.algo2

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNot
import io.mockk.mockk
import io.mockk.verify

class ProgramSpec: DescribeSpec ({
    isolationMode = IsolationMode.InstancePerTest
    val pepeArgento = Conductor()
    val moniArgento = Conductor()
    val antiPepe = Conductor()
    val mamaMartha = Conductor()

    val garbarino = Sponsor()
    val musimundo = Sponsor()
    val carrefour = Sponsor()

    val programa1 = Programa(
        titulo = "Casados con hijos",
        conductores = listOf(pepeArgento, moniArgento),
        dia = Dia.DOMINGO,
        presupuesto = 20.0,
        duracion = 21,
        rating = listOf(8.7, 6.8, 4.7, 9.7, 7.0),
        sponsors = listOf(garbarino),
        condicion = CondicionCombinado(
            condiciones = setOf(
                CondicionRating(5.7),
                CondicionConducEspecifico(pepeArgento),
                CondicionPresupuesto(90.03)
            ), esAnd = false
        )
    )
    val programa2 = Programa(
        titulo = "Solteros con cuñados",
        conductores = listOf(antiPepe),
        dia = Dia.LUNES,
        presupuesto = 50.0,
        duracion = 21,
        rating = listOf(3.7, 2.8, 4.7, 3.7, 1.0),
        sponsors = listOf(musimundo),
        condicion = CondicionCombinado(
            condiciones = setOf(
                CondicionRating(5.7),
                CondicionConducEspecifico(pepeArgento),
                CondicionPresupuesto(50.03)
            ), esAnd = true
        )
    )
    val programa3 = Programa(
        titulo = "Cocina con Mama Martha",
        conductores = listOf(mamaMartha),
        dia = Dia.MIERCOLES,
        presupuesto = 100.0,
        duracion = 15,
        rating = listOf(5.7, 7.8, 3.6, 6.7, 9.0),
        sponsors = listOf(carrefour),
        condicion = CondicionConductores(3)
    )
    val programa4 = Programa(
        titulo = "Union de todos los programas",
        conductores = listOf(pepeArgento, moniArgento, antiPepe, mamaMartha),
        dia = Dia.VIERNES,
        presupuesto = 1000000.0,
        duracion = 15,
        rating = listOf(5.7, 7.8, 3.6, 6.7, 9.0),
        sponsors = listOf(carrefour),
        condicion = CondicionConductores(3)
    )

    val grilla = Grilla()

    describe("Programas y condiciones"){
        it("Casados con hijos puede mantenerse"){
            programa1.puedeMantenerse() shouldBe true
        }
        it("solteros con cuñados se irá del aire"){
            programa2.puedeMantenerse() shouldBe false
        }
        it("al programa de cocina no le sobran conductores"){
            programa3.puedeMantenerse() shouldBe true
        }
        it("a la union de programas le sobran conductores"){
            programa4.puedeMantenerse() shouldBe false
        }
    }
    describe("Grilla"){
        it("no tiene nada al principio"){
            grilla.programasAlAire() shouldBe listOf()
        }

        it("agrego un programa y se agrega"){
            grilla.agregarPrograma(programa1)
            grilla.programasAlAire() shouldBe listOf(programa1)
        }
        it("agrego 2 programas y hay 2 programas"){
            grilla.agregarPrograma(programa1)
            grilla.agregarPrograma(programa2)

            grilla.programasAlAire().size shouldBe 2
        }
        it("los conductores se agregan"){
            grilla.agregarPrograma(programa1)
            grilla.agregarPrograma(programa2)

            grilla.conductores.size shouldBe 3
        }
    }
    describe("revision de la grilla"){
        it("revicion"){
            grilla.agregarPrograma(programa1)
            grilla.agregarPrograma(programa2)

            grilla.agregarRevicion(programa1)
            grilla.agregarRevicion(programa2)

            grilla.agregarCondicciones(CondicionRating(5.7),CambiarDia(Dia.JUEVES))
            grilla.agregarCondicciones(CondicionConducEspecifico(mamaMartha),CambiarDia(Dia.MARTES))

            grilla.cambiarDiaRevicion(Dia.SABADO)
            grilla.revicion(Dia.SABADO)

            programa1.dia shouldBe Dia.MARTES
            programa2.dia shouldBe Dia.JUEVES
        }
    }

    describe("acciones"){
        it("partir el programa en 2"){
            grilla.agregarPrograma(programa1)
            programa1.ejecutarAccion(grilla, PartirPrograma())

            grilla.programasAlAire().contains(programa1) shouldBe false
            grilla.programasAlAire().size shouldBe 2
        }
    }
    describe("reemplazar por simpsons"){
        it("reemplazar por simpsons"){
            grilla.agregarPrograma(programa1)
            programa1.ejecutarAccion(grilla,ReemplazarPorSimpsons())

            grilla.programasAlAire().contains(programa1) shouldBe false
            grilla.programasAlAire().size shouldBe 1
        }
    }
    describe("fusionar programas"){
        it("fusionar programa"){
            grilla.agregarPrograma(programa1)
            grilla.agregarPrograma(programa2)
            programa1.ejecutarAccion(grilla, FusionarPrograma())

            grilla.programasAlAire().contains(programa1) shouldBe false
            grilla.programasAlAire().contains(programa2) shouldBe false

            grilla.programasAlAire().size shouldBe 1
        }
    }

    describe("Mails"){
        val mockedEnviaMail = mockk<MailSender>(relaxUnitFun = true)
        val mockedEnviaSms = mockk<SmsSender>(relaxUnitFun = true)


        it("eliminar programas que no estan en revision"){
            grilla.agregarPrograma(programa3)
            grilla.agregarRevicion(programa3)
            grilla.quitarPrograma(programa3)

            grilla.agregarObserver(EliminadorProgramas())
            grilla.agregarPrograma(programa1)

            grilla.aRevicion().size shouldBe 0
        }
        it("se envia mail al conductor"){
            grilla.agregarObserver(ConductorSender(mockedEnviaMail))
            grilla.agregarPrograma(programa1)

            verify(exactly = 1){
                mockedEnviaMail.enviarMail(
                    Mail(
                        asunto = "Oportunidad",
                        contenido = "Fuiste elegido para conducir ${programa1.titulo}"
                    ))
            }
        }
        it("se envia mail a los accionistas"){
            grilla.agregarObserver(ClowinSender(mockedEnviaSms))
            grilla.agregarPrograma(programa4)

            verify(exactly = 1){
                mockedEnviaSms.enviarSms(
                    Sms(
                        mensaje = "$1000000.0 - Union de todos los programas - CONSEGUIR SPONSOR URGENTE!"
                )
                )
            }
        }
    }
})