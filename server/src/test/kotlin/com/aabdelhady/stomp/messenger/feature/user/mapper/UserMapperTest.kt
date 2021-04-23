package com.aabdelhady.stomp.messenger.feature.user.mapper

import com.aabdelhady.stomp.messenger.test.createDummyUser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class UserMapperTest {
    private val userMapper = UserMapper()

    @Test
    fun mapList_shouldMapCorrectlyToUserResponse() {
        // given
        val userA = createDummyUser()
        val userB = createDummyUser()

        // when
        val response = userMapper.mapList(listOf(userA, userB))

        // then
        assertEquals(2, response.size)

        assertEquals(userA.id, response[0].id)
        assertEquals(userA.firstName, response[0].firstName)
        assertEquals(userA.lastName, response[0].lastName)
        assertEquals(userA.profilePictureUrl, response[0].profilePictureUrl)

        assertEquals(userB.id, response[1].id)
        assertEquals(userB.firstName, response[1].firstName)
        assertEquals(userB.lastName, response[1].lastName)
        assertEquals(userB.profilePictureUrl, response[1].profilePictureUrl)
    }
}
