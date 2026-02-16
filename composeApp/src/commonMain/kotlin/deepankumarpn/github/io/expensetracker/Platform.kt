package deepankumarpn.github.io.expensetracker

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform