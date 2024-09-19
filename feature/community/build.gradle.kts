plugins {
    id("iguana.android.library")
    id("iguana.android.hilt")
    id("iguana.kotlin.hilt")
    id("iguana.android.feature")
}

android {
    namespace = "com.iguana.community"
}
dependencies {
    implementation(projects.core.domain)
}