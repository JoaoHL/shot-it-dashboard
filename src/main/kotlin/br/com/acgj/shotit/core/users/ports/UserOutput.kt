package br.com.acgj.shotit.core.users.ports

import br.com.acgj.shotit.core.domain.User

data class UserProfileDTO(val name: String, val profilePicture: String){
    constructor(user: User): this (user.name, user.profilePicture!!)
}
