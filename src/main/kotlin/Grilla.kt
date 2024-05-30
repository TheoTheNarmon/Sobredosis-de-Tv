package ar.edu.unsam.algo2

object grilla {
    val programas: MutableList<Programa> = mutableListOf()
    val aRevicion: MutableList<Programa> = mutableListOf()
    val conductores: MutableSet<Conductor> = mutableSetOf()
    val condiciones: MutableList<Condicion> = mutableListOf()
    val acciones: MutableList<AccionPrograma> = mutableListOf()
    var diaRevicion: Dia = Dia.LUNES
    val observers: List<Observer> = mutableListOf()

    fun aRevicion() = aRevicion
    fun programasAlAire() = programas

    fun agregarCondiciones(condicion: Condicion){
        condiciones.add(condicion)
    }
    fun agregarAccion(accionPrograma: AccionPrograma){
        acciones.add(accionPrograma)
    }
    fun agregarRevicion(programa: Programa){
        if(!programas.contains(programa)){
            throw BuisnessException("el programa no está en la lista")
        }
        aRevicion.add(programa)
    }
    fun agregarPrograma(programa: Programa){
        programas.add(programa)
        programa.conductores().forEach{conductores.add(it)}
        observers.forEach { it.ejecutar(programa) }
    }
    fun quitarPrograma(programa: Programa){
        if(!programas.contains(programa)){
            throw BuisnessException("el programa no está en la lista")
        }
        programas.remove(programa)
    }
    fun cambiarDiaRevicion(dia: Dia){
        diaRevicion = dia
    }

    fun revicion(dia: Dia){
        if (dia != diaRevicion){
            throw BuisnessException("Hoy no es el dia de la revicion")
        }
        aRevicion.forEach {programa ->
            if(!condiciones.all { it.puedeMantenerse(programa) }){
                acciones.forEach { it.ejecutar(programa) }
            }
        }
    }
}

interface Observer {
    fun ejecutar(programa: Programa)
}

class ConductorSender: Observer{
    override fun ejecutar(programa: Programa){
        mailSender.enviarMail(Mail(asunto = "Oportunidad", contenido = "Fuiste elegido para conducir ${programa.titulo}"))
    }
}
class ClowinSender: Observer{
    override fun ejecutar(programa: Programa){
        smsSender.enviarSms(Sms(mensaje = "$${programa.presupuesto()} - ${programa.titulo} - CONSEGUIR SPONSOR URGENTE!"))
    }
}
class EliminadorProgramas: Observer{
    override fun ejecutar(programa: Programa){
        grilla.aRevicion().filter{!grilla.programasAlAire().contains(it)}.forEach {
            grilla.quitarPrograma(it)
        }
    }
}

object mailSender{
    fun enviarMail(mail: Mail): String = "¡Te llego un Correo! Asunto = ${mail.asunto}, Contenido = ${mail.contenido}"
}
class Mail(val asunto: String, val contenido: String)

object smsSender{
    fun enviarSms(sms: Sms): String = "Te ha llegado un mensaje = ${sms.mensaje}"
}
class Sms(val mensaje: String)