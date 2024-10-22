package dev.nin0.chat.api.rest.request

import dev.nin0.chat.BuildConfig

open class Route(private val route: String) {
    val url = "${BuildConfig.API_URL}$route"
    override fun toString() = route
}

object Routes {

    object Auth: Route("/auth") {

        internal val Login = Route("$this/login")

    }

}