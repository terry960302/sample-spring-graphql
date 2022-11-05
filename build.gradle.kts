import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.5"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"

	// kotlin no default constructor error 해결용
	kotlin("plugin.noarg") version "1.6.21"
	kotlin("plugin.allopen") version "1.6.21"

	// https://github.com/Netflix/dgs-codegen
	id("com.netflix.dgs.codegen") version "5.6.0"


}

group = "com.ritier"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

	// jpa + db conn
	implementation("org.postgresql:postgresql:42.5.0")
	implementation("org.springframework:spring-jdbc:5.3.23")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")

	// swagger
	implementation("org.springdoc:springdoc-openapi-ui:1.6.12")

	// mapper
	implementation("org.modelmapper:modelmapper:3.1.0")

	// graphql
	implementation(platform("com.netflix.graphql.dgs:graphql-dgs-platform-dependencies:latest.release"))
	implementation("com.netflix.graphql.dgs:graphql-dgs-spring-boot-starter")
	implementation("com.netflix.graphql.dgs:graphql-dgs-extended-scalars")
}

extra["kotlin.version"] = "1.4.31"
extra["graphql-java.version"] = "19.2"

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

// kotlin no default constructor error 해결용
allOpen {
	annotation("javax.persistence.Entity")
	annotation("javax.persistence.MappedSuperclass")
	annotation("javax.persistence.Embeddable")
}

noArg {
	annotation("javax.persistence.Entity")
	annotation("javax.persistence.MappedSuperclass")
	annotation("javax.persistence.Embeddable")
}


// code gen for graphql DGS
tasks.withType<com.netflix.graphql.dgs.codegen.gradle.GenerateJavaTask> {
	schemaPaths = mutableListOf("${projectDir}/src/main/resources/schema") // List of directories containing schema files
	packageName = "com.ritier.samplespringgraphql" // The package name to use to generate sources
	generateDataTypes = true
	language = "kotlin"
	typeMapping = mutableMapOf(
	)
//	generateClient = true // Enable generating the type safe query API
}
