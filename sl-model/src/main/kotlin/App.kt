
import data.dao.Disciplines
import data.dao.DisciplinesDao
import org.hibernate.cfg.Configuration

fun main() {

    val sessionFactory = Configuration().configure().buildSessionFactory()

    val dao = DisciplinesDao(sessionFactory)

    val dis = Disciplines().apply {
        name = "Урок"
    }
    val dis1 = Disciplines().apply {
        name = "Урок"
    }

    println(dao.list)

    print(dis == dis1)
}