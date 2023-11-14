package top.flapypan.blog.entity

import jakarta.persistence.*

/**
 * 固定链接实体类
 */
@Entity
@Table(name = "t_link")
class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0

    @Column(nullable = false)
    var name: String = ""

    @Column(nullable = false)
    var url: String = ""
}
