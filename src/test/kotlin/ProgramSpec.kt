package ar.edu.unsam.algo2

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class ProgramSpec: DescribeSpec ({
    isolationMode = IsolationMode.InstancePerTest
    val pepeArgento = Conductor()
    val moniArgento = Conductor()
    val antiPepe = Conductor()
    val mamaMartha = Conductor()

    val garbarino = Sponsor()
    val musimundo = Sponsor()
    val carrefour = Sponsor()
    val mcDonald = Sponsor()

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
        presupuesto = 100.0,
        duracion = 15,
        rating = listOf(5.7, 7.8, 3.6, 6.7, 9.0),
        sponsors = listOf(carrefour),
        condicion = CondicionConductores(3)
    )

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
    describe("Grilla y acciones"){
        grilla.agregarPrograma(programa1)
        grilla.agregarPrograma(programa2)
        grilla.agregarPrograma(programa3)
        grilla.agregarPrograma(programa4)

        it("condiciones de la grilla"){
            grilla.agregarCondiciones(CondicionRating(5.0))
            grilla.agregarCondiciones(CondicionConductores(3))
            grilla.agregarAccion(CambiarDia(Dia.JUEVES))

            grilla.agregarRevicion(programa1)
            grilla.agregarRevicion(programa2)
            grilla.agregarRevicion(programa3)
            grilla.agregarRevicion(programa4)

            grilla.revicion(Dia.LUNES)

            programa1.dia shouldBe Dia.DOMINGO
            programa2.dia shouldBe Dia.JUEVES
            programa3.dia shouldBe Dia.MIERCOLES
            programa4.dia shouldBe Dia.JUEVES
        }

        it("agregar programa"){
            grilla.programas.size shouldBe 4
        }

        it("Fusionar Programa"){
            programa2.ejecutarAccion(FusionarPrograma())

            grilla.programas.contains(programa2) shouldBe false
            grilla.programas.contains(programa3) shouldBe false
            grilla.programas.size shouldBe 3
        }
        it("Reemplazar por Simpsons"){
            programa2.ejecutarAccion(ReemplazarPorSimpsons())

            grilla.programas.contains(programa2) shouldBe false
            grilla.programas.size shouldBe 4
        }
        it("Partir programa"){
            programa2.ejecutarAccion(PartirPrograma())

            grilla.programas.size shouldBe 5
        }
    }
})