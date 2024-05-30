package ar.edu.unsam.algo2

interface Condicion {
    fun puedeMantenerse(programa: Programa): Boolean
}

class CondicionRating(val minimoPromedio: Double): Condicion{
    override fun puedeMantenerse(programa: Programa) = programa.promedioRating() >= minimoPromedio
}
class CondicionConductores(val maximoCond: Int): Condicion{
    override fun puedeMantenerse(programa: Programa) = programa.cantConductores() <= maximoCond
}
class CondicionPresupuesto(val maximoPresupuesto: Double): Condicion{
    override fun puedeMantenerse(programa: Programa) = programa.presupuesto() <= maximoPresupuesto
}
class CondicionConducEspecifico(val conductor: Conductor): Condicion{
    override fun puedeMantenerse(programa: Programa) = programa.conductores().contains(conductor)
}
class CondicionCombinado(val condiciones: Set<Condicion>, val esAnd: Boolean): Condicion{
    override fun puedeMantenerse(programa: Programa): Boolean{
        if(esAnd){
            return condiciones.all{it.puedeMantenerse(programa)}
        }
        return condiciones.any{it.puedeMantenerse(programa)}
    }
}