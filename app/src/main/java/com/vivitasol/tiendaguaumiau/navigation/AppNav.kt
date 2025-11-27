package com.vivitasol.tiendaguaumiau.navigation

sealed class Route(val route: String) {
    data object Splash : Route("splash")
    data object Login : Route("login")
    data object Register : Route("register")
    data object MenuShell : Route("menu_shell")

    data object Option1 : Route("option1")
    data object ProductDetail : Route("option1/detail/{productId}") {
        fun build(productId: Int) = "option1/detail/$productId"
    }

    data object Option2 : Route("option2")
    data object Option2Detail : Route("option2/detail/{id}") {
        fun build(id: String) = "option2/detail/$id"
    }

    data object Option3 : Route("option3")
    data object Option4 : Route("option4")
    data object Option5 : Route("option5")
    data object AboutUs : Route("about_us")
}