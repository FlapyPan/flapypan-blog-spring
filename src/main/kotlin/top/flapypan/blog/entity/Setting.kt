package top.flapypan.blog.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

/**
 * 设置实体类
 */
@Entity
@Table(name = "t_setting")
class Setting {

    @Id
    @Column(name = "s_key")
    var key: String = ""

    @Column(name = "s_value")
    var value: String? = null

    constructor()

    constructor(key: String, value: String?) {
        this.key = key
        this.value = value
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Setting
        return key == other.key
    }

    override fun hashCode() = key.hashCode()

}
