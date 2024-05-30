package ar.edu.unsam.algo2

val homeroSimpson = Conductor()

interface AccionPrograma {
    fun ejecutar(programa: Programa)
}

class PartirPrograma(): AccionPrograma{
    override fun ejecutar(programa: Programa) {
        grilla.quitarPrograma(programa)
        grilla.agregarPrograma(Programa(titulo = "${programa.titulo} al aire", conductores = programa.conductores().take(programa.cantConductores()/2), sponsors = programa.sponsors, dia = programa.dia, presupuesto = programa.presupuesto()/2, duracion = programa.duracion, rating = programa.rating, condicion = programa.condicion))
        grilla.agregarPrograma(Programa(titulo = "programa sin nombre", conductores = programa.conductores().drop(programa.cantConductores()/2), sponsors = programa.sponsors, dia = programa.dia, presupuesto = programa.presupuesto()/2, duracion = programa.duracion, rating = programa.rating, condicion = programa.condicion))
    }
}
class ReemplazarPorSimpsons(): AccionPrograma{
    override fun ejecutar(programa: Programa) {
        grilla.quitarPrograma(programa)
        grilla.agregarPrograma(Programa(
            titulo = "Los Simpsons", conductores = listOf(homeroSimpson), dia = programa.dia, presupuesto = 500000.0, duracion = 30, rating = listOf(9.7,9.3,8.7,10.0,7.7), sponsors = listOf() ,condicion = CondicionRating(5.0)))
    }
}
class FusionarPrograma(): AccionPrograma{
    override fun ejecutar(programa: Programa) {
        val lugar = grilla.programasAlAire().indexOf(programa)
        lateinit var programa2: Programa
        grilla.quitarPrograma(programa)
        if(lugar >= grilla.programasAlAire().size){
            programa2 = grilla.programas.get(0)
        }
        else{
            programa2 = grilla.programas.get(lugar + 1)
        }
        grilla.quitarPrograma(programa2)
        grilla.agregarPrograma(Programa(
            titulo = "${programa.titulo} y ${programa2.titulo}",
            conductores = mutableListOf(programa.conductores().first(), programa2.conductores().first()),
            presupuesto = listOf(programa.presupuesto,programa2.presupuesto).min(),
            sponsors = listOf(programa.sponsors,programa2.sponsors).random(),
            duracion = programa.duracion + programa2.duracion,
            dia = programa.dia,
            rating = programa.rating,
            condicion = CondicionCombinado(condiciones = setOf(programa.condicion,programa2.condicion), esAnd = true)
        ))
    }
}
class CambiarDia(val diaNuevo: Dia): AccionPrograma{
    override fun ejecutar(programa: Programa) {
        programa.cambiarDia(diaNuevo)
    }
}


