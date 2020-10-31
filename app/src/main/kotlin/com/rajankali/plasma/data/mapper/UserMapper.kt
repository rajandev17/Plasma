package com.rajankali.plasma.data.mapper

import com.rajankali.core.data.UserEntity
import com.rajankali.plasma.data.model.User
import javax.inject.Inject

class UserMapper @Inject constructor(): EntityMapper<UserEntity, User> {

    override fun toDomain(entity: UserEntity): User {
        return User(entity.name, entity.password)
    }

    override fun toEntity(domain: User): UserEntity {
        return UserEntity(domain.name, domain.password)
    }
}