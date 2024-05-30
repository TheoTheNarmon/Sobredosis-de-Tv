package ar.edu.unsam.algo2

class Grilla {
    val programas: MutableList<Programa> = mutableListOf()
    val aRevicion: MutableList<Programa> = mutableListOf()
    val conductores: MutableSet<Conductor> = mutableSetOf()
    val condiciones: MutableList<Condicion> = mutableListOf()
    val acciones: MutableList<AccionPrograma> = mutableListOf()
    var diaRevicion: Dia = Dia.LUNES
    val observers: MutableList<Observer> = mutableListOf()

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
        observers.forEach { it.ejecutar(this, programa) }
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
                acciones.forEach { it.ejecutar(this, programa) }
            }
        }
    }

    fun agregarObserver(observer: Observer){
        observers.add(observer)
    }
}

interface Observer {
    fun ejecutar(grilla: Grilla, programa: Programa)
}

class ConductorSender(val mailSender: MailSender): Observer{
    override fun ejecutar(grilla: Grilla, programa: Programa){
        mailSender.enviarMail(Mail(asunto = "Oportunidad", contenido = "Fuiste elegido para conducir ${programa.titulo}"))
    }
}
class ClowinSender(val smsSender: SmsSender): Observer{
    override fun ejecutar(grilla: Grilla, programa: Programa){
        smsSender.enviarSms(Sms(mensaje = "$${programa.presupuesto().toString()} - ${programa.titulo} - CONSEGUIR SPONSOR URGENTE!"))
    }
}
class EliminadorProgramas: Observer{
    override fun ejecutar(grilla: Grilla, programa: Programa){
        grilla.aRevicion().filter{!grilla.programasAlAire().contains(it)}.forEach {
            grilla.aRevicion.remove(it)
        }
    }
}

class MailSender{
    fun enviarMail(mail: Mail){}
}
class Mail(val asunto: String, val contenido: String)

class SmsSender{
    fun enviarSms(sms: Sms){}
}
class Sms(val mensaje: String)