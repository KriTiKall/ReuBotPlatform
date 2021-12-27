
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

fun main() {
//    val service = ScheduleSaveService()
//    val reader = ScheduleReader(ScheduleParser(), service)

    val exec = Executors.newSingleThreadScheduledExecutor()
    exec.scheduleAtFixedRate({

    }, 0, 15, TimeUnit.MINUTES)
}