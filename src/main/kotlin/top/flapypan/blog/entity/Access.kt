package top.flapypan.blog.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "t_access")
class Access {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0

    @ManyToOne
    @JoinColumn(name = "article_id")
    var article: Article? = null

    @Column(nullable = false)
    var referrer: String = ""

    @Column(nullable = false)
    var ua: String = ""

    @Column(nullable = false)
    var ip: String = ""

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    var createDate: LocalDateTime = LocalDateTime.now()
}
