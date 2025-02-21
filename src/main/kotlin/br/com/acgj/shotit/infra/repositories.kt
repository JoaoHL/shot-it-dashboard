package br.com.acgj.shotit.infra

import br.com.acgj.shotit.domain.User
import br.com.acgj.shotit.domain.Video
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface VideoRepository : JpaRepository<Video, Long>

@Repository
interface UserRepository: JpaRepository<User, Long> {
    fun findByEmail(email: String): Optional<User>
}