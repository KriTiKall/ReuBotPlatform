package data.dao

import lombok.NoArgsConstructor
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*


interface EntityId<Id> {
    fun getId(): Id
    fun setId(id: Id)
}

enum class LessonFormat {
    LEFT, RIGHT, FULL, BOTH
}

enum class PairPosition(val pos: Int) {
    FIRST(0), SECOND(1), THIRD(2), FOURTH(3), FIFTH(4), SIXTH(5), SEVENTH(6), EIGHTH(7)
}


@NoArgsConstructor
@Entity
@Table(schema = "model", name = "schedules")
data class Schedules(
    var name: String? = null,
    var date: LocalDate? = null,
    var hash: Long? = 0
) : EntityId<Long?> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long? = null

    override fun getId(): Long? {
        return id!!
    }

    override fun setId(id: Long?) {
        this.id = id
    }
}


@NoArgsConstructor
@Entity
@Table(schema = "model", name = "disciplines")
data class Disciplines(
    var name: String? = null
) : EntityId<Long?> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private var id: Long? = null

    override fun getId(): Long {
        return id!!
    }

    override fun setId(id: Long?) {
        this.id = id
    }
}

@NoArgsConstructor
@Entity
@Table(schema = "model", name = "teacher_posts")
data class TeacherPosts(
    var name: String? = null
) : EntityId<Long?> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private var id: Long? = null

    override fun getId(): Long {
        return id!!
    }

    override fun setId(id: Long?) {
        this.id = id
    }
}

@NoArgsConstructor
@Entity
@Table(schema = "model", name = "teachers")
data class Teachers(
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    var post: TeacherPosts? = null,
    var name: String? = null
) : EntityId<Long?> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private var id: Long? = null

    override fun getId(): Long {
        return id!!
    }

    override fun setId(id: Long?) {
        this.id = id
    }
}

@NoArgsConstructor
@Entity
@Table(schema = "model", name = "lessons")
data class Lessons(
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "schedule_id")
    var schedule: Schedules? = null,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "discipline_id")
    var discipline: Disciplines? = null,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id")
    var teacher: TeacherPosts? = null,
    var auditorium: String? = null,

    @Enumerated(EnumType.ORDINAL)
    var position: PairPosition? = null,

    @Enumerated(EnumType.STRING)
    var format: LessonFormat = LessonFormat.FULL
) : EntityId<Long?> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private var id: Long? = null

    override fun getId(): Long {
        return id!!
    }

    override fun setId(id: Long?) {
        this.id = id
    }
}

@NoArgsConstructor
@Entity
@Table(schema = "model", name = "schedule_updates")
data class ScheduleUpdates(
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "schedule_id")
    var schedules: Schedules? = null,

    var uploaded: LocalDateTime? = null,

    @Column(name = "last_update")
    var lastUpdate: LocalDateTime? = null
) : EntityId<Long?> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long? = null

    override fun getId(): Long {
        return id!!
    }

    override fun setId(id: Long?) {
        this.id = id
    }
}