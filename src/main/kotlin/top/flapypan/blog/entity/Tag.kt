package top.flapypan.blog.entity

import jakarta.persistence.*

/**
 * 标签实体类
 */
@Entity
@Table(name = "t_tag")
class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0

    @Column(nullable = false, unique = true)
    var name: String = ""
}
