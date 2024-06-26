package ar.edu.unsam.algo2

class Programa(val titulo: String,
               val conductores: List<Conductor>,
               val sponsors: List<Sponsor>,
               var dia: Dia,
               val presupuesto: Double,
               val duracion: Int,
               val rating: List<Double>,
               val condicion: Condicion
    ){
    fun promedioRating(): Double = rating.average()
    fun conductores(): List<Conductor> = conductores
    fun cantConductores(): Int = conductores().size
    fun presupuesto(): Double = presupuesto

    fun cambiarDia(diaNuevo: Dia){
        dia = diaNuevo
    }

    fun puedeMantenerse() = condicion.puedeMantenerse(this)

    fun ejecutarAccion(grilla: Grilla,accionPrograma: AccionPrograma){
        accionPrograma.ejecutar(grilla,this)
    }
}

class Conductor {}
class Sponsor{}

enum class Dia{
    LUNES,
    MARTES,
    MIERCOLES,
    JUEVES,
    VIERNES,
    SABADO,
    DOMINGO
}