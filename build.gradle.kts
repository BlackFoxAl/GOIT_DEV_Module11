/*plugins {
    id("java")
}*/
plugins {
    id("war")
}

group = "org.example"
version = "2.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    compileOnly(dependencyNotation = "jakarta.servlet:jakarta.servlet-api:6.0.0")
    implementation("org.thymeleaf:thymeleaf:3.1.2.RELEASE")

}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.test {
    useJUnitPlatform()
}