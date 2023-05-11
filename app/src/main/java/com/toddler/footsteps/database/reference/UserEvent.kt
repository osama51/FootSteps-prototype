package com.toddler.footsteps.database.reference


sealed interface UserEvent {
    object SaveUser: UserEvent
    object DeleteUser: UserEvent
    object GetUsers: UserEvent
    object GetUsersOrderedByTimestamp: UserEvent
    data class GetUsersByTitle(val title: String): UserEvent
}